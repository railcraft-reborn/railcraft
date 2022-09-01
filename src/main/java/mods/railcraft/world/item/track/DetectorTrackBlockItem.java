package mods.railcraft.world.item.track;

import java.util.List;
import mods.railcraft.Translations.Tips;
import mods.railcraft.world.level.block.track.outfitted.DetectorTrackBlock;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

public class DetectorTrackBlockItem extends BlockItem {

    public DetectorTrackBlockItem(DetectorTrackBlock block, Properties properties) {
        super(block, properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, Level level, List<Component> tooltipComponents,
        TooltipFlag isAdvanced) {
        tooltipComponents.add(Component.translatable(Tips.DETECTOR_TRACK).withStyle(ChatFormatting.GRAY));
        tooltipComponents.add(Component.translatable(Tips.HIT_CROWBAR_TO_CHANGE_DETECTION_DIRECTION).withStyle(ChatFormatting.BLUE));
        tooltipComponents.add(Component.translatable(Tips.COMPARATOR_OUTPUT_FROM_CARTS).withStyle(ChatFormatting.RED));
    }
}
