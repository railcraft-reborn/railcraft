package mods.railcraft.world.level.material;

import net.minecraft.world.level.Level;

public interface FuelProvider {

  float consumeFuel(Level level);

  float getHeatStep();
}
