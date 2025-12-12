package mods.railcraft.world.level.block.track.outfitted;

import java.util.function.Supplier;
import org.jspecify.annotations.Nullable;
import mods.railcraft.api.track.TrackType;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntityTypes;
import mods.railcraft.world.level.block.entity.track.RoutingTrackBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.minecart.AbstractMinecart;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class RoutingTrackBlock extends PoweredOutfittedTrackBlock implements EntityBlock {

  public RoutingTrackBlock(Supplier<? extends TrackType> trackType, Properties properties) {
    super(trackType, properties);
  }

  @Nullable
  @Override
  public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
    return new RoutingTrackBlockEntity(pos, state);
  }

  @Override
  public void onMinecartPass(BlockState state, Level level, BlockPos pos, AbstractMinecart cart) {
    super.onMinecartPass(state, level, pos, cart);
    level.getBlockEntity(pos, RailcraftBlockEntityTypes.ROUTING_TRACK.get())
        .ifPresent(routingTrack -> routingTrack.minecartPassed(cart));
  }

  @Override
  protected boolean crowbarWhack(BlockState blockState, Level level, BlockPos blockPos,
      Player player, InteractionHand hand, ItemStack itemStack) {
    if (player instanceof ServerPlayer serverPlayer) {
      level.getBlockEntity(blockPos, RailcraftBlockEntityTypes.ROUTING_TRACK.get())
          .ifPresent(blockEntity -> serverPlayer.openMenu(blockEntity, blockPos));
    }
    return true;
  }
}
