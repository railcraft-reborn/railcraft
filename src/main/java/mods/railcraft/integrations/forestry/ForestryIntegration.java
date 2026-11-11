package mods.railcraft.integrations.forestry;

import net.neoforged.fml.ModList;

public final class ForestryIntegration {

  public static final String MOD_ID = "forestry";

  private ForestryIntegration() {}

  public static boolean isLoaded() {
    return ModList.get().isLoaded(MOD_ID);
  }
}
