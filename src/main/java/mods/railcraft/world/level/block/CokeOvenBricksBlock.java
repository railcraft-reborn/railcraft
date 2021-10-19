package mods.railcraft.world.level.block;

import mods.railcraft.world.level.block.entity.multiblock.CokeOvenBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public class CokeOvenBricksBlock extends Block {
  public static final BooleanProperty LIT = BlockStateProperties.LIT;
  public static final BooleanProperty PARENT = BooleanProperty.create("parent");

  public CokeOvenBricksBlock(Properties properties) {
    super(properties);
    this.registerDefaultState(this.stateDefinition.any()
        .setValue(LIT, Boolean.valueOf(false))
        .setValue(PARENT, Boolean.valueOf(false)));
  }

  @Override
  protected void createBlockStateDefinition(
        StateContainer.Builder<Block, BlockState> stateContainer) {
    stateContainer.add(LIT, PARENT);
  }

  @Override
  public TileEntity createTileEntity(BlockState blockState, IBlockReader level) {
    return new CokeOvenBlockEntity();
  }

  @Override
  public boolean hasTileEntity(BlockState blockState) {
    return true;
  }

  @Override
  public ActionResultType use(BlockState blockState, World level,
      BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult rayTraceResult) {
    TileEntity blockEntity = level.getBlockEntity(pos);
    if (!(blockEntity instanceof CokeOvenBlockEntity)) {
      return ActionResultType.PASS;
    }
    CokeOvenBlockEntity recast = (CokeOvenBlockEntity) blockEntity;

    if (!recast.isFormed() && !recast.tryToMakeParent(rayTraceResult.getDirection())) {
      return ActionResultType.PASS;
    }

    recast = recast.getParent();
    if (recast == null) {
      return ActionResultType.PASS;
    }

    player.openMenu(recast);
    // player.awardStat(Stats.INTERACT_WITH_CRAFTING_TABLE);
    // TODO: interaction stats

    return ActionResultType.sidedSuccess(level.isClientSide());
  }

}
