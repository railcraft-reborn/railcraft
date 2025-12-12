package mods.railcraft.world.level.block.track.outfitted;

import java.util.function.Supplier;
import org.jspecify.annotations.Nullable;
import mods.railcraft.api.track.TrackType;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntityTypes;
import mods.railcraft.world.level.block.entity.track.DumpingTrackBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.minecart.AbstractMinecart;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class DumpingTrackBlock extends PoweredOutfittedTrackBlock implements EntityBlock {

  public DumpingTrackBlock(Supplier<? extends TrackType> trackType, Properties properties) {
    super(trackType, properties);
  }

  @Nullable
  @Override
  public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
    return new DumpingTrackBlockEntity(pos, state);
  }

  @Nullable
  @Override
  public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState,
      BlockEntityType<T> type) {
    return level.isClientSide() ? null
        : BaseEntityBlock.createTickerHelper(type, RailcraftBlockEntityTypes.DUMPING_TRACK.get(),
            DumpingTrackBlockEntity::serverTick);
  }

  @Override
  public int getMaxSupportedDistance() {
    var distance = super.getMaxSupportedDistance();
    return distance > 0 ? distance : 1;
  }

  @Override
  public void onMinecartPass(BlockState state, Level level, BlockPos pos, AbstractMinecart cart) {
    super.onMinecartPass(state, level, pos, cart);
    level.getBlockEntity(pos, RailcraftBlockEntityTypes.DUMPING_TRACK.get())
        .ifPresent(dumpingTrack -> dumpingTrack.minecartPassed(cart, isPowered(state)));
  }

  @Override
  protected boolean crowbarWhack(BlockState blockState, Level level, BlockPos blockPos,
      Player player, InteractionHand hand, ItemStack itemStack) {
    if (player instanceof ServerPlayer serverPlayer) {
      level.getBlockEntity(blockPos, RailcraftBlockEntityTypes.DUMPING_TRACK.get())
          .ifPresent(dumpingTrack -> serverPlayer.openMenu(dumpingTrack, blockPos));
    }
    return true;
  }
}
