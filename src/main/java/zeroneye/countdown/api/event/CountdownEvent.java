package zeroneye.countdown.api.event;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;

public class CountdownEvent extends Event {
    public long seconds;

    CountdownEvent(long seconds) {
        this.seconds = seconds;
    }

    /**
     * Event for when countdown timer change in player mode (config: global=false).
     * the amount of seconds left
     * this event is server side only
     */
    public static class Player extends CountdownEvent {
        public final PlayerEntity player;

        public Player(PlayerEntity player, long seconds) {
            super(seconds);
            this.player = player;
        }
    }

    /**
     * Event for when countdown timer change in global mode (config: global=true).
     * the amount of seconds left
     * this event is server side only
     */
    public static class Global extends CountdownEvent {
        public Global(long seconds) {
            super(seconds);
        }
    }

    /**
     * Event for when countdown time is ended and befor the game is over.
     * this event is server side only
     * <p>
     * If cancelled, the player will not set to spectator mode.
     */
    @Cancelable
    public static class GameOver extends Player {
        public GameOver(PlayerEntity player, long seconds) {
            super(player, seconds);
        }
    }
}
