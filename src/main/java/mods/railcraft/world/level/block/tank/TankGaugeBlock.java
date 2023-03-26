package mods.railcraft.world.level.block.tank;

import mods.railcraft.util.LevelUtil;
import mods.railcraft.world.level.block.AbstractStrengthenedGlassBlock;
import mods.railcraft.world.level.block.entity.multiblock.MultiblockBlockEntity;
import mods.railcraft.world.level.block.entity.multiblock.MultiblockListener;
import mods.railcraft.world.level.block.entity.tank.TankBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.gameevent.GameEventListener;
import net.minecraft.world.phys.BlockHitResult;

import org.jetbrains.annotations.Nullable;

public abstract class TankGaugeBlock extends AbstractStrengthenedGlassBlock implements EntityBlock {

  public static final IntegerProperty LEVEL = BlockStateProperties.LEVEL;

  public TankGaugeBlock(Properties properties) {
    super(properties);
    this.registerDefaultState(this.stateDefinition.any()
        .setValue(TYPE, Type.SINGLE)
        .setValue(LEVEL, 0));
  }

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
    super.createBlockStateDefinition(builder);
    builder.add(LEVEL);
  }

  @Override
  public boolean isValidSpawn(BlockState state, BlockGetter level, BlockPos pos,
      SpawnPlacements.Type type, EntityType<?> entityType) {
    return false;
  }

  @Nullable
  @Override
  public <T extends BlockEntity> GameEventListener getListener(ServerLevel level, T blockEntity) {
    return blockEntity instanceof MultiblockBlockEntity<?, ?> multiblock
        ? new MultiblockListener(multiblock)
        : null;
  }

  @Override
  public InteractionResult use(BlockState blockState, Level level,
      BlockPos pos, Player player, InteractionHand hand, BlockHitResult rayTraceResult) {

    if (level.isClientSide()) {
      return InteractionResult.SUCCESS;
    }

    return LevelUtil.getBlockEntity(level, pos, TankBlockEntity.class)
        .flatMap(MultiblockBlockEntity::getMembership)
        .map(MultiblockBlockEntity.Membership::master)
        .map(master -> {
          master.use((ServerPlayer) player, hand);
          return InteractionResult.CONSUME;
        })
        .orElse(InteractionResult.PASS);
  }
}
