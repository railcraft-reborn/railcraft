package mods.railcraft.world.level.block.worldspike;

import org.jetbrains.annotations.Nullable;
import com.mojang.serialization.MapCodec;
import mods.railcraft.Railcraft;
import mods.railcraft.Translations;
import mods.railcraft.integrations.jei.JeiSearchable;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntityTypes;
import mods.railcraft.world.level.block.entity.worldspike.WorldSpikeBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class WorldSpikeBlock extends BaseEntityBlock implements JeiSearchable {

  private static final MapCodec<WorldSpikeBlock> CODEC = simpleCodec(WorldSpikeBlock::new);

  public WorldSpikeBlock(Properties properties) {
    super(properties);
  }

  @Override
  protected MapCodec<? extends BaseEntityBlock> codec() {
    return CODEC;
  }

  @Override
  public RenderShape getRenderShape(BlockState pState) {
    return RenderShape.MODEL;
  }

  @Override
  public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState,
      boolean movedByPiston) {
    super.onPlace(state, level, pos, oldState, movedByPiston);
    if (level instanceof ServerLevel serverLevel) {
      this.forceChunk(serverLevel, pos, true);
    }
  }

  @Override
  public void onRemove(BlockState state, Level level, BlockPos pos, BlockState oldState,
      boolean movedByPiston) {
    super.onRemove(state, level, pos, oldState, movedByPiston);
    if (level instanceof ServerLevel serverLevel && !state.is(oldState.getBlock())) {
      this.forceChunk(serverLevel, pos, false);
    }
  }

  private void forceChunk(ServerLevel serverLevel, BlockPos pos, boolean add) {
    var chunkPos = new ChunkPos(pos);
    for (int x = chunkPos.x - 1; x <= chunkPos.x + 1; x++) {
      for (int z = chunkPos.z - 1; z <= chunkPos.z + 1; z++) {
        Railcraft.CHUNK_CONTROLLER.forceChunk(serverLevel, pos, x, z, add, false);
      }
    }
  }

  @Nullable
  @Override
  public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
    return new WorldSpikeBlockEntity(blockPos, blockState);
  }

  @Nullable
  @Override
  public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState,
      BlockEntityType<T> type) {
    return level.isClientSide() ? null
        : createTickerHelper(type, RailcraftBlockEntityTypes.WORLD_SPIKE.get(),
            WorldSpikeBlockEntity::serverTick);
  }

  @Override
  public Component jeiDescription() {
    return Component.translatable(Translations.Jei.WORLD_SPIKE);
  }
}
