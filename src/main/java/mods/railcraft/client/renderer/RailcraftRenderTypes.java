package mods.railcraft.client.renderer;

import mods.railcraft.api.core.RailcraftConstants;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;

public class RailcraftRenderTypes extends RenderStateShard {

  public static final RenderType POSITION_COLOR_LIGHTMAP = RenderType.create(
      RailcraftConstants.rl("leash").toString(),
      256,
      false,
      false,
      RenderPipelines.OUTLINE_NO_CULL,
      RenderType.CompositeState.builder()
          .setTextureState(NO_TEXTURE)
          .setLightmapState(LIGHTMAP)
          .createCompositeState(false));

  private RailcraftRenderTypes(String name, Runnable setupState, Runnable clearState) {
    super(name, setupState, clearState);
  }
}
