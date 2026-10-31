package mods.railcraft.world.level.block.charge;

import java.util.List;
import java.util.Map;
import mods.railcraft.Translations;
import mods.railcraft.api.charge.Charge;
import mods.railcraft.api.charge.ChargeStorage;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

public class FrameBlock extends ChargeBlock {

  public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

  private static final int UPDATE_INTERVAL = 20;

  private static final Spec CHARGE_SPEC = new Spec(ConnectType.BLOCK, 0.5f,
      new ChargeStorage.Spec(ChargeStorage.State.RECHARGEABLE, 12000, 4000, 1));

  public FrameBlock(Properties properties) {
    super(properties);
    this.registerDefaultState(this.stateDefinition.any().setValue(POWERED, false));
  }

  @Override
  public BlockState getStateForPlacement(BlockPlaceContext context) {
    return this.defaultBlockState().setValue(POWERED, false);
  }

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
    builder.add(POWERED);
  }

  @Override
  public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState,
      boolean isMoving) {
    super.onPlace(state, level, pos, oldState, isMoving);
    this.scheduleUpdate(level, pos);
  }

  @Override
  protected void randomTick(BlockState state, ServerLevel level, BlockPos pos,
      RandomSource random) {
    super.randomTick(state, level, pos, random);
    // Frames placed before the powered state was tracked have no scheduled tick yet
    this.scheduleUpdate(level, pos);
  }

  /**
   * Periodically refreshes the powered state: a frame is powered when its charge grid has charge
   * available, whether it comes from Railcraft wires or from third party energy cables feeding the
   * frame's own storage.
   */
  @Override
  public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
    super.tick(state, level, pos, random);
    boolean powered = Charge.distribution.network(level).access(pos).hasCapacity(1);
    if (state.getValue(POWERED) != powered) {
      level.setBlock(pos, state.setValue(POWERED, powered), Block.UPDATE_CLIENTS);
    }
    level.scheduleTick(pos, this, UPDATE_INTERVAL);
  }

  private void scheduleUpdate(Level level, BlockPos pos) {
    if (!level.isClientSide() && !level.getBlockTicks().hasScheduledTick(pos, this)) {
      level.scheduleTick(pos, this, UPDATE_INTERVAL);
    }
  }

  @Override
  public Map<Charge, Spec> getChargeSpecs(BlockState state, ServerLevel level, BlockPos pos) {
    return Map.of(Charge.distribution, CHARGE_SPEC);
  }

  @Override
  public void appendHoverText(ItemStack stack, Item.TooltipContext context,
      List<Component> tooltip, TooltipFlag flag) {
    tooltip.add(Component.translatable(Translations.Tips.FRAME).withStyle(ChatFormatting.GRAY));
  }
}
