package mods.railcraft.util.effects;

import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;

/**
 * @author CovertJaguar <https://www.railcraft.info>
 */
public class EffectManager {

  public interface IEffectSource {

    BlockPos getPos();

    Vector3d getPosF();

    default boolean isDead() {
      return false;
    }
  }

  public static class EffectSourceBlockPos implements IEffectSource {

    private final BlockPos pos;
    private final Vector3d posF;

    private EffectSourceBlockPos(BlockPos pos) {
      this.pos = pos;
      this.posF = new Vector3d(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
    }

    @Override
    public BlockPos getPos() {
      return pos;
    }

    @Override
    public Vector3d getPosF() {
      return posF;
    }
  }

  public static class EffectSourceVector3d implements IEffectSource {

    private final BlockPos pos;
    private final Vector3d posF;

    private EffectSourceVector3d(Vector3d pos) {
      this.pos = new BlockPos(pos);
      this.posF = pos;
    }

    @Override
    public BlockPos getPos() {
      return pos;
    }

    @Override
    public Vector3d getPosF() {
      return posF;
    }
  }

  public static class EffectSourceTile implements IEffectSource {

    private final TileEntity source;

    private EffectSourceTile(TileEntity source) {
      this.source = source;
    }

    @Override
    public BlockPos getPos() {
      return source.getBlockPos();
    }

    @Override
    public Vector3d getPosF() {
      BlockPos pos = source.getBlockPos();
      return new Vector3d(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
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
    public Vector3d getPosF() {
      return new Vector3d(source.getX(), source.getY() + source.getMyRidingOffset(), source.getZ());
    }

    @Override
    public boolean isDead() {
      return !source.isAlive();
    }
  }

  public static IEffectSource getEffectSource(Object source) {
    if (source instanceof TileEntity) {
      return new EffectSourceTile((TileEntity) source);
    } else if (source instanceof Entity) {
      return new EffectSourceEntity((Entity) source);
    } else if (source instanceof BlockPos) {
      return new EffectSourceBlockPos((BlockPos) source);
    } else if (source instanceof Vector3d) {
      return new EffectSourceVector3d((Vector3d) source);
    }
    throw new IllegalArgumentException("Invalid Effect Source");
  }
}
