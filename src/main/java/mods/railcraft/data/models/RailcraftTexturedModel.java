package mods.railcraft.data.models;

import net.minecraft.data.models.model.TextureMapping;
import net.minecraft.data.models.model.TexturedModel;

public class RailcraftTexturedModel {

  public static final TexturedModel.Provider STEEL_ANVIL =
      TexturedModel.createDefault(TextureMapping::top, RailcraftModelTemplates.STEEL_ANVIL);
}
