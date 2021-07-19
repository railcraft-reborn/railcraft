package mods.railcraft.world.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntitySize;
import net.minecraft.entity.Pose;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.DamageSource;
import net.minecraftforge.entity.PartEntity;

/**
 * Pseudo-Entity used to refine the Tunnel Bore collision boxes.
 *
 * Created by CovertJaguar on 11/19/2015.
 */
public class TunnelBorePartEntity extends PartEntity<TunnelBoreEntity> {

  public final String partName;
  public final float forwardOffset;
  public final float sideOffset;

  private final EntitySize size;

  public TunnelBorePartEntity(TunnelBoreEntity parent, String partName, float width, float height,
      float forwardOffset) {
    this(parent, partName, width, height, forwardOffset, 0.0F);
  }

  public TunnelBorePartEntity(TunnelBoreEntity parent, String partName, float width, float height,
      float forwardOffset, float sideOffset) {
    super(parent);
    this.size = EntitySize.scalable(width, height);
    this.partName = partName;
    this.forwardOffset = forwardOffset;
    this.sideOffset = sideOffset;
  }

  @Override
  public EntitySize getDimensions(Pose pose) {
    return this.size;
  }

  @Override
  public void tick() {
    super.tick();
    double x = getParent().getOffsetX(getParent().getX(), forwardOffset, sideOffset);
    double z = getParent().getOffsetZ(getParent().getZ(), forwardOffset, sideOffset);
    moveTo(x, getParent().getY() + 0.3F, z, 0.0F, 0.0F);
  }

  @Override
  protected void defineSynchedData() {}

  @Override
  protected void readAdditionalSaveData(CompoundNBT p_70037_1_) {}

  @Override
  protected void addAdditionalSaveData(CompoundNBT p_213281_1_) {}

  /**
   * Returns true if other Entities should be prevented from moving through this Entity.
   */
  @Override
  public boolean canBeCollidedWith() {
    return true;
  }

  /**
   * Called when the entity is attacked.
   */
  @Override
  public boolean hurt(DamageSource damageSource, float p_70097_2_) {
    return !isInvulnerableTo(damageSource)
        && getParent().attackEntityFromPart(this, damageSource, p_70097_2_);
  }

  /**
   * Returns true if Entity argument is equal to this Entity
   */
  @Override
  public boolean is(Entity entity) {
    return this == entity || getParent() == entity;
  }
}
