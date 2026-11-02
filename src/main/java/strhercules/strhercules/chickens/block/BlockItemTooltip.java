package strhercules.chickens.block;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import java.util.function.Consumer;

public interface BlockItemTooltip {
    void appendItemTooltip(ItemStack stack, Consumer<Component> tooltip);
}
