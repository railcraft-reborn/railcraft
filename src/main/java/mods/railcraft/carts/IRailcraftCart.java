/*------------------------------------------------------------------------------
 Copyright (c) CovertJaguar, 2011-2019
 http://railcraft.info
 This code is the property of CovertJaguar
 and may only be used with explicit written
 permission unless otherwise specified on the
 license page at http://railcraft.info/wiki/info:license.
 -----------------------------------------------------------------------------*/

package mods.railcraft.carts;

import org.apache.commons.lang3.ArrayUtils;
import mods.railcraft.NBTPlugin;
import mods.railcraft.Railcraft;
import mods.railcraft.plugins.SeasonPlugin;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.world.GameRules;

/**
 * @author CovertJaguar <http://www.railcraft.info/>
 */
public interface IRailcraftCart {

  DataParameter<Byte> SEASON = DataSerializers.BYTE.createAccessor(225);

  ItemStack getContents();

  default void initEntityFromItem(ItemStack stack) {}

  Item getItem();

  default ItemStack createCartItem(AbstractMinecartEntity cart) {
    ItemStack stack = getItem().getDefaultInstance();
    if (!stack.isEmpty() && cart.hasCustomName())
      stack.setHoverName(cart.getCustomName());
    return stack;
  }

  default ItemStack[] getComponents(AbstractMinecartEntity cart) {
    ItemStack contents = getContents();
    if (!contents.isEmpty())
      return new ItemStack[] {new ItemStack(Items.MINECART), contents};
    return new ItemStack[] {createCartItem(cart)};
  }

  default ItemStack[] getItemsDropped(AbstractMinecartEntity cart) {
    if (Railcraft.serverConfig.cartsBreakOnDrop.get())
      return getComponents(cart);
    else
      return new ItemStack[] {createCartItem(cart)};
  }

  default void killAndDrop(AbstractMinecartEntity cart) {
    cart.remove();
    if (!cart.level.getGameRules().getBoolean(GameRules.RULE_DOENTITYDROPS))
      return;
    ItemStack[] drops = getItemsDropped(cart);
    if (!Railcraft.serverConfig.cartsBreakOnDrop.get() && cart.hasCustomName()
        && !ArrayUtils.isEmpty(drops))
      drops[0].setHoverName(cart.getCustomName());
    for (ItemStack item : drops) {
      if (!item.isEmpty())
        cart.spawnAtLocation(item, 0.0F);
    }
  }

  default void cartInit() {
    getAsEntity().getEntityData().define(SEASON, (byte) 0);
  }

  default SeasonPlugin.Season getSeason() {
    return SeasonPlugin.Season.VALUES[getAsEntity().getEntityData().get(SEASON)];
  }

  default void setSeason(SeasonPlugin.Season season) {
    getAsEntity().getEntityData().set(SEASON, (byte) season.ordinal());
  }

  default AbstractMinecartEntity getAsEntity() {
    return (AbstractMinecartEntity) this;
  }

  default CompoundNBT saveToNBT(CompoundNBT nbt) {
    NBTPlugin.writeEnumOrdinal(nbt, "season", getSeason());
    return nbt;
  }

  default void loadFromNBT(CompoundNBT nbt) {
    setSeason(NBTPlugin.readEnumOrdinal(nbt, "season", SeasonPlugin.Season.VALUES,
        SeasonPlugin.Season.DEFAULT));
  }
}
