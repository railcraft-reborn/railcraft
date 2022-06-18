package mods.railcraft.client.renderer;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;

public class RailcraftRenderTypes extends RenderStateShard {

  public static final RenderType POSITION_COLOR_LIGHTMAP = RenderType.create("leash",
      DefaultVertexFormat.POSITION_COLOR_LIGHTMAP, VertexFormat.Mode.QUADS, 256, false, false,
      RenderType.CompositeState.builder()
          .setShaderState(POSITION_COLOR_LIGHTMAP_SHADER)
          .setTextureState(NO_TEXTURE)
          .setCullState(NO_CULL)
          .setLightmapState(LIGHTMAP)
          .createCompositeState(false));

  private RailcraftRenderTypes(String p_110161_, Runnable p_110162_, Runnable p_110163_) {
    super(p_110161_, p_110162_, p_110163_);
  }
}
