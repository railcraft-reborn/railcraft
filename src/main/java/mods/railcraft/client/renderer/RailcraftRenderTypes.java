package mods.railcraft.client.renderer;

import mods.railcraft.api.core.RailcraftConstants;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.rendertype.RenderSetup;
import net.minecraft.client.renderer.rendertype.RenderType;

public class RailcraftRenderTypes {

  private static final RenderSetup POSITION_COLOR_LIGHTMAP_SETUP =
      RenderSetup.builder(RenderPipelines.OUTLINE_CULL)
          .useLightmap()
          .bufferSize(256)
          .createRenderSetup();

  public static final RenderType POSITION_COLOR_LIGHTMAP = RenderType.create(
      RailcraftConstants.id("leash").toString(),
      POSITION_COLOR_LIGHTMAP_SETUP);
}
