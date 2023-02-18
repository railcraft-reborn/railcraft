package mods.railcraft.util;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import mods.railcraft.mixin.SlotMixin;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;

/**
 * Annotated {@link Container} classes will have their {@link Slot} instances call
 * {@link Container#canPlaceItem(int, net.minecraft.world.item.ItemStack)}.
 * 
 * @see {@link SlotMixin}
 * 
 * @author Sm0keySa1m0n
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ValidateSlots {}
