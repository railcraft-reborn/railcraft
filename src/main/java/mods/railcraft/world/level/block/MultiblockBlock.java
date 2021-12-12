package mods.railcraft.world.level.block;

import mods.railcraft.advancements.criterion.RailcraftCriteriaTriggers;
import mods.railcraft.world.level.block.entity.multiblock.MultiblockBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;

public abstract class MultiblockBlock<T extends MultiblockBlockEntity<?>> extends BaseEntityBlock {

  public static final BooleanProperty PARENT = BooleanProperty.create("parent");
  private final Class<T> blockEntityType;

  /**
   * Create a new multiblock TE.
   */
  public MultiblockBlock(Class<T> blockEntityType, Properties properties) {
    super(properties);
    this.blockEntityType = blockEntityType;
    // TODO: this MIGHT be causing stupid overhead (placing defaultstate is slow)
    this.registerDefaultState(this.addDefaultBlockState(this.stateDefinition.any()));
  }

  protected BlockState addDefaultBlockState(BlockState defaultBlockState) {
    defaultBlockState.setValue(PARENT, Boolean.FALSE);
    return defaultBlockState;
  }

  // man
  @Override
  public RenderShape getRenderShape(BlockState blockState) {
    return RenderShape.MODEL;
  }

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
    super.createBlockStateDefinition(builder);
    builder.add(PARENT);
  }

  @Override
  public InteractionResult use(BlockState blockState, Level level,
      BlockPos pos, Player player, InteractionHand hand, BlockHitResult rayTraceResult) {
    BlockEntity blockEntity = level.getBlockEntity(pos);

    if (level.isClientSide() || hand.equals(InteractionHand.OFF_HAND)) {
      return InteractionResult.SUCCESS;
    }

    // everything under here must be serverside.
    if (!this.blockEntityType.isInstance(blockEntity)) {
      return InteractionResult.PASS;
    }

    T recast = this.blockEntityType.cast(blockEntity);

    if (recast.getParent() == null) {
      boolean ttmpResult = recast.tryToMakeParent(rayTraceResult.getDirection());

      if (!recast.isFormed() && !ttmpResult) { // it failed and it's not assembled.
        player.displayClientMessage(
            new TranslatableComponent("screen.multiblock.assembly_failed"), true);
        return InteractionResult.PASS;
      }

      if (ttmpResult) {
        RailcraftCriteriaTriggers.MULTIBLOCK_FORM.trigger((ServerPlayer) player, recast);
      }
      return InteractionResult.PASS;
    }
    NetworkHooks.openGui((ServerPlayer) player, recast, pos);
    // player.openMenu(recast);
    return InteractionResult.sidedSuccess(level.isClientSide());
  }
}
