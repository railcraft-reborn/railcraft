package mods.railcraft.world.level.block.steamboiler;

import java.util.Random;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.Property;

public abstract class FireboxBlock extends SteamBoilerBlock {

  public static final Property<Boolean> LIT = BlockStateProperties.LIT;

  public FireboxBlock(Properties properties) {
    super(properties);
    this.registerDefaultState(this.stateDefinition.any()
        .setValue(LIT, false));
  }

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
    builder.add(LIT);
  }

  @Override
  public void animateTick(BlockState blockState, Level level, BlockPos blockPos, Random random) {
    if (blockState.getValue(LIT)) {
      var x = blockPos.getX() + 0.5D;
      var y = blockPos.getY() + 0.4D;
      var z = blockPos.getZ() + 0.5D;
      if (random.nextDouble() < 0.1D) {
        level.playLocalSound(x, y, z, SoundEvents.FURNACE_FIRE_CRACKLE, SoundSource.BLOCKS, 1.0F,
            1.0F, false);
      }

      for (var direction : Direction.Plane.HORIZONTAL) {
        var axis = direction.getAxis();
        var offset = 0.52D;
        var horizontalOffset = random.nextDouble() * 0.6D - 0.3D;
        var xOffset = axis == Direction.Axis.X ? direction.getStepX() * offset : horizontalOffset;
        var verticalOffset = random.nextDouble() * 6.0D / 16.0D;
        var zOffset = axis == Direction.Axis.Z ? direction.getStepZ() * offset : horizontalOffset;
        level.addParticle(ParticleTypes.SMOKE, x + xOffset, y + verticalOffset, z + zOffset,
            0.0D, 0.0D, 0.0D);
        level.addParticle(ParticleTypes.FLAME, x + xOffset, y + verticalOffset, z + zOffset,
            0.0D, 0.0D, 0.0D);
      }
    }
  }
}
