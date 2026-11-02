package strhercules.chickens.blockentity;

import net.minecraft.core.Direction;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

import javax.annotation.Nullable;
import java.util.Optional;

/** Persisted per-face automation policy shared by the mod's machines. */
public final class MachineSideConfig {
    public enum Channel {
        ITEMS,
        FLUIDS,
        CHEMICALS,
        ENERGY
    }

    public enum Mode {
        NONE,
        INPUT,
        OUTPUT,
        BOTH;

        public boolean allowsInput() {
            return this == INPUT || this == BOTH;
        }

        public boolean allowsOutput() {
            return this == OUTPUT || this == BOTH;
        }

        public Mode next() {
            return values()[(ordinal() + 1) % values().length];
        }
    }

    private static final String TAG = "MachineSideConfig";
    private final Mode[][] modes = new Mode[Direction.values().length][Channel.values().length];

    public MachineSideConfig() {
        reset();
    }

    public void reset() {
        for (Direction direction : Direction.values()) {
            for (Channel channel : Channel.values()) {
                modes[direction.ordinal()][channel.ordinal()] = Mode.BOTH;
            }
        }
    }

    public Mode get(@Nullable Direction direction, Channel channel) {
        if (direction == null) {
            return Mode.BOTH;
        }
        return modes[direction.ordinal()][channel.ordinal()];
    }

    public boolean allows(@Nullable Direction direction, Channel channel, boolean input) {
        Mode mode = get(direction, channel);
        return input ? mode.allowsInput() : mode.allowsOutput();
    }

    public Mode cycle(@Nullable Direction direction, Channel channel) {
        if (direction == null) {
            return Mode.BOTH;
        }
        Mode next = get(direction, channel).next();
        modes[direction.ordinal()][channel.ordinal()] = next;
        return next;
    }

    public void save(ValueOutput parent) {
        ValueOutput config = parent.child(TAG);
        for (Direction direction : Direction.values()) {
            ValueOutput side = config.child(direction.getName());
            for (Channel channel : Channel.values()) {
                side.putByte(channel.name(), (byte) get(direction, channel).ordinal());
            }
        }
    }

    public void load(ValueInput parent) {
        reset();
        Optional<ValueInput> config = parent.child(TAG);
        if (config.isEmpty()) {
            return;
        }
        for (Direction direction : Direction.values()) {
            Optional<ValueInput> side = config.get().child(direction.getName());
            if (side.isEmpty()) {
                continue;
            }
            for (Channel channel : Channel.values()) {
                int ordinal = side.get().getByteOr(channel.name(), (byte) -1);
                if (ordinal >= 0 && ordinal < Mode.values().length) {
                    modes[direction.ordinal()][channel.ordinal()] = Mode.values()[ordinal];
                }
            }
        }
    }
}
