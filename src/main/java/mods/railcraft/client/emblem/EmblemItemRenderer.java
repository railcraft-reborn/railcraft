package mods.railcraft.client.emblem;

import net.minecraft.world.item.ItemStack;

/**
 *
 * @author CovertJaguar <https://www.railcraft.info/>
 */
public interface EmblemItemRenderer {

  void renderIn3D(String ident, boolean foil);

  void renderIn3D(ItemStack stack, boolean foil);
}
