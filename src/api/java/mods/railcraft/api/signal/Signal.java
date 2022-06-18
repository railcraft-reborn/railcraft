package mods.railcraft.api.signal;

import mods.railcraft.api.core.BlockEntityLike;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Nameable;

public interface Signal extends BlockEntityLike, Nameable {

  void setCustomName(Component name);
}
