package mods.railcraft.util.effects;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;

/**
 * @author CovertJaguar <https://www.railcraft.info>
 */
public class EffectManager {

  public interface IEffectSource {

    BlockPos getPos();

    Vec3 getPosF();

    default boolean isDead() {
      return false;
    }
  }

  public static class EffectSourceBlockPos implements IEffectSource {

    private final BlockPos pos;
    private final Vec3 posF;

    private EffectSourceBlockPos(BlockPos pos) {
      this.pos = pos;
      this.posF = new Vec3(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
    }

    @Override
    public BlockPos getPos() {
      return pos;
    }

    @Override
    public Vec3 getPosF() {
      return posF;
    }
  }

  public static class EffectSourceVector3d implements IEffectSource {

    private final BlockPos pos;
    private final Vec3 posF;

    private EffectSourceVector3d(Vec3 pos) {
      this.pos = new BlockPos(pos);
      this.posF = pos;
    }

    @Override
    public BlockPos getPos() {
      return pos;
    }

    @Override
    public Vec3 getPosF() {
      return posF;
    }
  }

  public static class EffectSourceTile implements IEffectSource {

    private final BlockEntity source;

    private EffectSourceTile(BlockEntity source) {
      this.source = source;
    }

    @Override
    public BlockPos getPos() {
      return source.getBlockPos();
    }

    @Override
    public Vec3 getPosF() {
      BlockPos pos = source.getBlockPos();
      return new Vec3(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
    }

    @Override
    public boolean isDead() {
      return source.isRemoved();
    }
  }

  public static class EffectSourceEntity implements IEffectSource {

    private final Entity source;

    private EffectSourceEntity(Entity source) {
      this.source = source;
    }

    @Override
    public BlockPos getPos() {
      return source.blockPosition();
    }

    @Override
    public Vec3 getPosF() {
      return new Vec3(source.getX(), source.getY() + source.getMyRidingOffset(), source.getZ());
    }

    @Override
    public boolean isDead() {
      return !source.isAlive();
    }
  }

  public static IEffectSource getEffectSource(Object source) {
    if (source instanceof BlockEntity) {
      return new EffectSourceTile((BlockEntity) source);
    } else if (source instanceof Entity) {
      return new EffectSourceEntity((Entity) source);
    } else if (source instanceof BlockPos) {
      return new EffectSourceBlockPos((BlockPos) source);
    } else if (source instanceof Vec3) {
      return new EffectSourceVector3d((Vec3) source);
    }
    throw new IllegalArgumentException("Invalid Effect Source");
  }
}
