package zeroneye.countdown.api;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.common.util.Constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

public class TimeData extends WorldSavedData {
    public static final int MAX_TIME = 31536000; //One year
    public final HashMap<UUID, Long> playersCountdown = new HashMap<>();
    public final List<UUID> playerSync = new ArrayList<>();
    public static long globalDefault;
    public long globalCountdown = globalDefault;
    public static boolean globalSync;

    public TimeData() {
        super("countdown_data");
    }

    public void addPlayerTime(UUID id, long time, boolean sync) {
        setPlayerTime(id, getPlayerTime(id) + time, sync);
    }

    public long getPlayerTime(UUID id) {
        return this.playersCountdown.get(id);
    }

    public void setPlayerTime(UUID id, long time, boolean sync) {
        this.playersCountdown.put(id, Math.max(0, Math.min(time, MAX_TIME)));
        if (sync) {
            this.playerSync.add(id);
        }
        markDirty();
    }

    public void addGlobalTime(long time, boolean sync) {
        setGlobalTime(getGlobalCountdown() + time, sync);
    }

    public long getGlobalCountdown() {
        return globalCountdown;
    }

    public void setGlobalTime(long time, boolean sync) {
        this.globalCountdown = Math.max(0, Math.min(time, MAX_TIME));
        globalSync = sync;
        markDirty();
    }

    @Override
    public void read(CompoundNBT nbt) {
        this.playersCountdown.clear();
        ListNBT listNBT = nbt.getList("PlayersTime", Constants.NBT.TAG_COMPOUND);
        IntStream.range(0, listNBT.size()).mapToObj(listNBT::getCompound).forEach(nbt1 -> {
            UUID id = nbt1.getUniqueId("Id");
            long time = nbt1.getLong("Time");
            this.playersCountdown.put(id, Math.min(time, MAX_TIME));
        });
        this.globalCountdown = nbt.getLong("GlobalTime");
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        ListNBT listNBT = new ListNBT();
        this.playersCountdown.forEach((uuid, time) -> {
            CompoundNBT nbt = new CompoundNBT();
            nbt.putUniqueId("Id", uuid);
            nbt.putLong("Time", Math.min(time, MAX_TIME));
            listNBT.add(nbt);
        });
        compound.put("PlayersTime", listNBT);
        compound.putLong("GlobalTime", this.globalCountdown);
        return compound;
    }
}
