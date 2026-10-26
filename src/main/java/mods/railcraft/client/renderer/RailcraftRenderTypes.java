package mods.railcraft.client.renderer;

import java.util.function.Function;
import mods.railcraft.api.core.RailcraftConstants;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.rendertype.RenderSetup;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Util;

public class RailcraftRenderTypes {

  private static final RenderSetup POSITION_COLOR_LIGHTMAP_SETUP =
      RenderSetup.builder(RenderPipelines.OUTLINE_CULL)
          .useLightmap()
          .bufferSize(256)
          .createRenderSetup();

  public static final RenderType POSITION_COLOR_LIGHTMAP = RenderType.create(
      RailcraftConstants.id("leash").toString(),
      POSITION_COLOR_LIGHTMAP_SETUP);

  private static final Function<Identifier, RenderType> ENTITY_TRANSLUCENT_CULL = Util.memoize(
      texture -> RenderType.create(
          RailcraftConstants.id("entity_translucent_cull").toString(),
          RenderSetup.builder(RenderPipelines.ENTITY_TRANSLUCENT_CULL)
              .withTexture("Sampler0", texture)
              .useLightmap()
              .useOverlay()
              .affectsCrumbling()
              .sortOnUpload()
              .setOutline(RenderSetup.OutlineProperty.AFFECTS_OUTLINE)
              .createRenderSetup()));

  /**
   * Like {@link net.minecraft.client.renderer.rendertype.RenderTypes#entityTranslucent} but with
   * back faces culled, so the inside of see-through models isn't drawn over the outside.
   */
  public static RenderType entityTranslucentCull(Identifier texture) {
    return ENTITY_TRANSLUCENT_CULL.apply(texture);
  }
}
