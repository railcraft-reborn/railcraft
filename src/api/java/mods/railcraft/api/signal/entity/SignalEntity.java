package mods.railcraft.api.signal.entity;

import mods.railcraft.api.core.BlockEntityLike;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Nameable;

/**
 * A signal or a signal box.
 *
 * @author Sm0keySa1m0n
 */
public interface SignalEntity extends BlockEntityLike, Nameable {

  void setCustomName(Component name);
}
