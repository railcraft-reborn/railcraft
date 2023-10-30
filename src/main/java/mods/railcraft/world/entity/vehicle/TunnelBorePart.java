package mods.railcraft.world.entity.vehicle;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.Pose;
import net.neoforged.neoforge.entity.PartEntity;


public class TunnelBorePart extends PartEntity<TunnelBore> {

  private final float forwardOffset;
  private final float sideOffset;

  private final EntityDimensions size;

  public TunnelBorePart(TunnelBore parent, float width, float height,
      float forwardOffset) {
    this(parent, width, height, forwardOffset, 0.0F);
  }

  public TunnelBorePart(TunnelBore parent, float width, float height,
      float forwardOffset, float sideOffset) {
    super(parent);
    this.size = EntityDimensions.scalable(width, height);
    this.refreshDimensions();
    this.forwardOffset = forwardOffset;
    this.sideOffset = sideOffset;
    this.updatePosition();
  }

  @Override
  public EntityDimensions getDimensions(Pose pose) {
    return this.size;
  }

  @Override
  public void tick() {
    super.tick();
    this.updatePosition();
  }

  private void updatePosition() {
    double x = this.getParent().getOffsetX(getParent().getX(), this.forwardOffset, this.sideOffset);
    double z = this.getParent().getOffsetZ(getParent().getZ(), this.forwardOffset, this.sideOffset);
    this.moveTo(x, this.getParent().getY() + 0.3F, z, 0.0F, 0.0F);
  }

  @Override
  protected void defineSynchedData() {}

  @Override
  protected void readAdditionalSaveData(CompoundTag compound) {}

  @Override
  protected void addAdditionalSaveData(CompoundTag compound) {}

  @Override
  public boolean canBeCollidedWith() {
    return true;
  }

  @Override
  public boolean hurt(DamageSource damageSource, float amount) {
    return !isInvulnerableTo(damageSource)
        && getParent().attackEntityFromPart(this, damageSource, amount);
  }

  @Override
  public boolean is(Entity entity) {
    return this == entity || getParent() == entity;
  }
}
