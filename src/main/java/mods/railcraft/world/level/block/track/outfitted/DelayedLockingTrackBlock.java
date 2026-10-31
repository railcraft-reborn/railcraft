package mods.railcraft.world.level.block.track.outfitted;

import java.util.function.Supplier;
import org.jetbrains.annotations.Nullable;
import mods.railcraft.api.track.TrackType;
import mods.railcraft.client.ScreenFactories;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntityTypes;
import mods.railcraft.world.level.block.entity.track.DelayedLockingTrackBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.minecart.AbstractMinecart;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

public class DelayedLockingTrackBlock extends PoweredOutfittedTrackBlock implements EntityBlock {

  public static final BooleanProperty HOLDING = BooleanProperty.create("holding");

  public DelayedLockingTrackBlock(Supplier<? extends TrackType> trackType,
      Properties properties) {
    super(trackType, properties);
  }

  @Override
  protected BlockState buildDefaultState(BlockState blockState) {
    return super.buildDefaultState(blockState).setValue(HOLDING, false);
  }

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
    super.createBlockStateDefinition(builder);
    builder.add(HOLDING);
  }

  @Override
  protected boolean crowbarWhack(BlockState blockState, Level level, BlockPos blockPos,
      Player player, InteractionHand hand, ItemStack itemStack) {
    if (level.isClientSide()) {
      level.getBlockEntity(blockPos, RailcraftBlockEntityTypes.DELAYED_LOCKING_TRACK.get())
          .ifPresent(ScreenFactories::openDelayedLockingTrackScreen);
    }
    return true;
  }

  @Override
  public void onMinecartPass(BlockState blockState, Level level, BlockPos pos,
      AbstractMinecart cart) {
    super.onMinecartPass(blockState, level, pos, cart);
    level.getBlockEntity(pos, RailcraftBlockEntityTypes.DELAYED_LOCKING_TRACK.get())
        .ifPresent(track -> track.minecartPassed(cart));
  }

  @Override
  public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
    return new DelayedLockingTrackBlockEntity(blockPos, blockState);
  }

  @Nullable
  @Override
  public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState,
      BlockEntityType<T> type) {
    return level.isClientSide() ? null
        : BaseEntityBlock.createTickerHelper(type,
            RailcraftBlockEntityTypes.DELAYED_LOCKING_TRACK.get(),
            DelayedLockingTrackBlockEntity::serverTick);
  }

  public static boolean isHolding(BlockState blockState) {
    return blockState.getValue(HOLDING);
  }

  public static BlockState setHolding(BlockState blockState, boolean holding) {
    return blockState.setValue(HOLDING, holding);
  }

}
