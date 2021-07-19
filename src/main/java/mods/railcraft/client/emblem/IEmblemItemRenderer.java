package mods.railcraft.client.emblem;

import net.minecraft.item.ItemStack;

/**
 *
 * @author CovertJaguar <http://www.railcraft.info/>
 */
public interface IEmblemItemRenderer {

  void renderIn3D(String ident, boolean foil);

  void renderIn3D(ItemStack stack, boolean foil);
}
