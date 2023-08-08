package mods.railcraft.world.level.block.track.outfitted;

import java.util.List;
import java.util.function.Supplier;
import mods.railcraft.Translations;
import mods.railcraft.api.track.TrackType;
import mods.railcraft.world.entity.vehicle.locomotive.Locomotive;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class WhistleTrackBlock extends PoweredOutfittedTrackBlock {

  public WhistleTrackBlock(Supplier<? extends TrackType> trackType, Properties properties) {
    super(trackType, properties);
  }

  @Override
  public void onMinecartPass(BlockState state, Level level, BlockPos pos, AbstractMinecart cart) {
    super.onMinecartPass(state, level, pos, cart);
    if (isPowered(state) && cart instanceof Locomotive locomotive) {
      locomotive.whistle();
    }
  }

  @Override
  public void appendHoverText(ItemStack stack, BlockGetter level, List<Component> lines,
      TooltipFlag flag) {
    lines.add(Component.translatable(Translations.Tips.WHISTLE_TRACK)
        .withStyle(ChatFormatting.GRAY));
    lines.add(Component.translatable(Translations.Tips.APPLY_REDSTONE_TO_ENABLE)
        .withStyle(ChatFormatting.RED));
  }
}
