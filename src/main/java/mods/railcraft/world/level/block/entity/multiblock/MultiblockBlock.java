package mods.railcraft.world.level.block.entity.multiblock;

import mods.railcraft.advancements.criterion.RailcraftCriteriaTriggers;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

public abstract class MultiblockBlock extends Block {
  public static final BooleanProperty PARENT = BooleanProperty.create("parent");

  /**
   * Create a new multiblock TE.
   */
  public MultiblockBlock(Properties properties) {
    super(properties);
    this.registerDefaultState(this.addDefaultBlockState(this.stateDefinition.any()));
  }

  protected BlockState addDefaultBlockState(BlockState defaultBlockState) {
    defaultBlockState.setValue(PARENT, Boolean.valueOf(false));
    return defaultBlockState;
  }

  @Override
  protected void createBlockStateDefinition(
        StateContainer.Builder<Block, BlockState> stateContainer) {
    super.createBlockStateDefinition(stateContainer);
    stateContainer.add(PARENT);
  }

  @Override
  public boolean hasTileEntity(BlockState blockState) {
    return true;
  }

  @Override
  public ActionResultType use(BlockState blockState, World level,
      BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult rayTraceResult) {
    TileEntity blockEntity = level.getBlockEntity(pos);

    if (!(blockEntity instanceof MultiblockEntity<?>)) {
      return ActionResultType.PASS;
    }

    if (level.isClientSide()) {
      return ActionResultType.SUCCESS;
    }
    // everything under here must be serverside.

    MultiblockEntity<?> recast = (MultiblockEntity<?>) blockEntity;

    if (recast.getParent() == null) {
      boolean ttmpResult = recast.tryToMakeParent(rayTraceResult.getDirection());

      if (!recast.isFormed() && !ttmpResult) { // it failed and it's not assembled.
        player.displayClientMessage(
            new TranslationTextComponent("multiblock.assembly_failed"), true);
        return ActionResultType.PASS;
      }

      if (ttmpResult) {
        RailcraftCriteriaTriggers.MULTIBLOCK_FORM.trigger((ServerPlayerEntity)player, recast);
      }
      return ActionResultType.PASS;
    }
    player.openMenu(recast);
    return ActionResultType.sidedSuccess(level.isClientSide());
  }
}
