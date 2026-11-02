package strhercules.chickens.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;

import java.util.function.Consumer;

/** Shared tooltip presentation for the machine upgrade items. */
public final class UpgradeItem extends Item {
    public enum Kind {
        SPEED("speedupgrade"),
        STACK("stackupgrade"),
        STORAGE("storagecapacity"),
        RANGE("rangeupgrade"),
        RF("rfupgrade"),
        RF_CAPACITY("rf_capacity_upgrade"),
        RF_EFFICIENCY("rf_efficiency_upgrade"),
        RF_OUTPUT("rf_output_upgrade"),
        RF_EXCITER("rf_exciter_upgrade"),
        RF_STABILIZER("rf_stabilizer_upgrade"),
        RF_SURGE("rf_surge_upgrade"),
        RF_GOVERNOR("rf_governor_upgrade");

        private final String id;

        Kind(String id) {
            this.id = id;
        }
    }

    private final Kind kind;
    private final boolean robotCompatible;

    public UpgradeItem(Properties properties, Kind kind) {
        this(properties, kind, true);
    }

    public UpgradeItem(Properties properties, Kind kind, boolean robotCompatible) {
        super(properties);
        this.kind = kind;
        this.robotCompatible = robotCompatible;
    }

    public boolean isRobotCompatible() {
        return robotCompatible;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay display,
            Consumer<Component> tooltip, TooltipFlag flag) {
        tooltip.accept(Component.translatable("item.chickens." + kind.id + ".tooltip")
                .withStyle(ChatFormatting.GRAY));
    }
}
