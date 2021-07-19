/*------------------------------------------------------------------------------
 Copyright (c) CovertJaguar, 2011-2019
 http://railcraft.info
 This code is the property of CovertJaguar
 and may only be used with explicit written
 permission unless otherwise specified on the
 license page at http://railcraft.info/wiki/info:license.
 -----------------------------------------------------------------------------*/

package mods.railcraft.carts;

import java.util.ArrayList;
import java.util.List;
import mods.railcraft.api.carts.IItemCart;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.entity.item.minecart.MinecartEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

/**
 * It also contains some generic code that most carts will find useful.
 *
 * @author CovertJaguar <http://www.railcraft.info>
 */
public abstract class CartBase extends MinecartEntity implements IRailcraftCart, IItemCart {

  protected CartBase(EntityType<?> entityType, World world) {
    super(entityType, world);
  }

  protected CartBase(World world, double x, double y, double z) {
    super(world, x, y, z);
  }

  @Override
  protected void defineSynchedData() {
    super.defineSynchedData();
    cartInit();
  }

  @Override
  protected void addAdditionalSaveData(CompoundNBT compound) {
    super.addAdditionalSaveData(compound);
    saveToNBT(compound);
  }

  @Override
  protected void readAdditionalSaveData(CompoundNBT compound) {
    super.readAdditionalSaveData(compound);
    loadFromNBT(compound);
  }

  @Override
  public void initEntityFromItem(ItemStack stack) {}

  @Override
  public final ItemStack getCartItem() {
    return createCartItem(this);
  }

  @Override
  public void destroy(DamageSource par1DamageSource) {
    killAndDrop(this);
  }

  @Override
  public final AbstractMinecartEntity.Type getMinecartType() {
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean isPoweredCart() {
    return false;
  }

  @Override
  public boolean canBeRidden() {
    return false;
  }

  @Override
  public boolean canPassItemRequests(ItemStack stack) {
    return false;
  }

  @Override
  public boolean canAcceptPushedItem(AbstractMinecartEntity requester, ItemStack stack) {
    return false;
  }

  @Override
  public boolean canProvidePulledItem(AbstractMinecartEntity requester, ItemStack stack) {
    return false;
  }

  /**
   * Checks if the entity is in range to render.
   */
  @Override
  public boolean shouldRenderAtSqrDistance(double distance) {
    return CartTools.isInRangeToRenderDist(this, distance);
  }

  public List<String> getDebugOutput() {
    List<String> debug = new ArrayList<>();
    debug.add("Railcraft Entity Data Dump");
    debug.add("Object: " + this);
    debug.add(
        String.format("Coordinates: d=%d, %s", this.level.dimension().toString(), this.position()));
    debug.add("Owner: " + CartTools.getCartOwnerEntity(this));
    debug.add("LinkA: " + CartTools.getCartOwnerEntity(this));
    return debug;
  }
}
