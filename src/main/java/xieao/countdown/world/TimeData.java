package xieao.countdown.world;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.world.GameType;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.Constants;
import xieao.countdown.config.Config;
import xieao.countdown.network.packet.SetTime;
import xieao.lib.Lollipop;
import xieao.lib.util.Player;
import xieao.lib.util.Server;

import java.util.HashMap;
import java.util.UUID;
import java.util.stream.IntStream;

public class TimeData extends WorldSavedData {
    public static final int MAX_TIME = 31536000; //One year
    public final HashMap<UUID, Long> playersTime = new HashMap<>();
    public long globalTime = Config.GENERAL.time.get();

    @OnlyIn(Dist.CLIENT) //Used for the hud renderer
    public static final TimeData CLIENT_INSTANCE = new TimeData();

    public TimeData() {
        super("countdown_data");
    }

    public void addPlayerTime(UUID id, long time, boolean sync) {
        setPlayerTime(id, getPlayerTime(id) + time, sync);
    }

    public long getPlayerTime(UUID id) {
        return this.playersTime.get(id);
    }

    public void setPlayerTime(UUID id, long time, boolean sync) {
        this.playersTime.put(id, Math.min(time, MAX_TIME));
        markDirty();
        if (sync) {
            Player.get(id).ifPresent(player -> {
                Lollipop.NET.toClient(new SetTime(time), player);
                if (time > 0 && player.isSpectator()) {
                    player.setGameType(GameType.SURVIVAL);
                }
            });
        }
    }

    public void addGlobalTime(long time, boolean sync) {
        setGlobalTime(getGlobalTime() + time, sync);
    }

    public long getGlobalTime() {
        return globalTime;
    }

    public void setGlobalTime(long time, boolean sync) {
        this.globalTime = Math.min(time, MAX_TIME);
        markDirty();
        if (sync) {
            Lollipop.NET.toAll(new SetTime(time));
            Server.get().getPlayerList().getPlayers().forEach(player -> {
                if (time > 0 && player.isSpectator()) {
                    player.setGameType(GameType.SURVIVAL);
                }
            });
        }
    }

    @Override
    public void read(CompoundNBT nbt) {
        this.playersTime.clear();
        ListNBT listNBT = nbt.getList("PlayersTime", Constants.NBT.TAG_COMPOUND);
        IntStream.range(0, listNBT.size()).mapToObj(listNBT::getCompound).forEach(nbt1 -> {
            UUID id = nbt1.getUniqueId("Id");
            long time = nbt1.getLong("Time");
            this.playersTime.put(id, Math.min(time, MAX_TIME));
        });
        this.globalTime = nbt.getLong("GlobalTime");
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        ListNBT listNBT = new ListNBT();
        this.playersTime.forEach((uuid, time) -> {
            CompoundNBT nbt = new CompoundNBT();
            nbt.putUniqueId("Id", uuid);
            nbt.putLong("Time", Math.min(time, MAX_TIME));
            listNBT.add(nbt);
        });
        compound.put("PlayersTime", listNBT);
        compound.putLong("GlobalTime", this.globalTime);
        return compound;
    }
}
