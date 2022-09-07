package mods.railcraft.world.item;

import java.util.List;
import mods.railcraft.Translations.Tips;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

public class PressureBoilerTankBlockItem extends BlockItem {

    public PressureBoilerTankBlockItem(Block block, Properties properties) {
        super(block, properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, Level level, List<Component> tooltipComponents,
        TooltipFlag isAdvanced) {
        tooltipComponents.add(Component.translatable(Tips.PRESSURE_BOILER_TANK).withStyle(ChatFormatting.GRAY));
        var dimensions = String.join(", ",List.of(
            "1x1x1",
            "2x2x2",
            "2x3x2",
            "3x2x3",
            "3x3x3",
            "3x4x3"));
        tooltipComponents.add(Component.translatable(Tips.DIMENSIONS, dimensions).withStyle(ChatFormatting.GRAY));

        int production = 0;
        if(stack.is(RailcraftItems.HIGH_PRESSURE_STEAM_BOILER_TANK.get()))
            production = 20;
        else if (stack.is(RailcraftItems.LOW_PRESSURE_STEAM_BOILER_TANK.get()))
            production = 10;
        tooltipComponents.add(Component.translatable(Tips.PRESSURE_BOILER_TANK_PRODUCTION, production).withStyle(ChatFormatting.GRAY));
    }
}
