package mods.railcraft.world.level.block.track.outfitted;

import java.util.List;
import java.util.function.Supplier;
import org.jetbrains.annotations.Nullable;
import mods.railcraft.Translations;
import mods.railcraft.api.track.TrackType;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntityTypes;
import mods.railcraft.world.level.block.entity.track.LockingTrackBlockEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.Property;

public class LockingTrackBlock extends PoweredOutfittedTrackBlock implements EntityBlock {

  public static final Property<LockingMode> LOCKING_MODE =
      EnumProperty.create("locking_mode", LockingMode.class);

  public LockingTrackBlock(Supplier<? extends TrackType> trackType, Properties properties) {
    super(trackType, properties);
  }

  @Override
  protected BlockState buildDefaultState(BlockState blockState) {
    return super.buildDefaultState(blockState)
        .setValue(LOCKING_MODE, LockingMode.LOCKDOWN);
  }

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
    super.createBlockStateDefinition(builder);
    builder.add(LOCKING_MODE);
  }

  @Override
  protected boolean crowbarWhack(BlockState state, Level level, BlockPos pos,
      Player player, InteractionHand hand, ItemStack itemStack) {
    var mode = LockingTrackBlock.getLockingMode(state);
    var newMode = player.isCrouching() ? mode.previous() : mode.next();
    var res = level.setBlockAndUpdate(pos, state.setValue(LOCKING_MODE, newMode));
    if (!level.isClientSide()) {
      level.getBlockEntity(pos, RailcraftBlockEntityTypes.LOCKING_TRACK.get())
          .ifPresent(lockingTrack -> lockingTrack.setLockingModeController(newMode.create(lockingTrack)));
    }
    var currentMode = Component.translatable(Translations.Tips.CURRENT_MODE);
    var modeDisplay = newMode.getDisplayName().copy().withStyle(ChatFormatting.DARK_PURPLE);
    player.displayClientMessage(currentMode.append(" ").append(modeDisplay), true);
    return res;
  }

  @Override
  public void onMinecartPass(BlockState blockState, Level level, BlockPos pos,
      AbstractMinecart cart) {
    super.onMinecartPass(blockState, level, pos, cart);
    level.getBlockEntity(pos, RailcraftBlockEntityTypes.LOCKING_TRACK.get())
        .ifPresent(lockingTrack -> lockingTrack.minecartPassed(cart));
  }

  @Override
  public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
    return new LockingTrackBlockEntity(blockPos, blockState);
  }

  @Nullable
  @Override
  public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState,
      BlockEntityType<T> type) {
    return level.isClientSide() ? null
        : BaseEntityBlock.createTickerHelper(type,
            RailcraftBlockEntityTypes.LOCKING_TRACK.get(),
            LockingTrackBlockEntity::serverTick);
  }

  public static LockingMode getLockingMode(BlockState blockState) {
    return blockState.getValue(LOCKING_MODE);
  }

  @Override
  public void appendHoverText(ItemStack stack, BlockGetter level, List<Component> lines,
      TooltipFlag flag) {
    lines.add(Component.translatable(Translations.Tips.LOCKING_TRACK)
        .withStyle(ChatFormatting.GRAY));
    lines.add(Component.translatable(Translations.Tips.HIT_CROWBAR_TO_CHANGE_MODE)
        .withStyle(ChatFormatting.BLUE));
    lines.add(Component.translatable(Translations.Tips.APPLY_REDSTONE_TO_RELEASE_CARTS)
        .withStyle(ChatFormatting.RED));
  }
}
