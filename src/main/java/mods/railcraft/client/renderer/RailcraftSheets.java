package mods.railcraft.client.renderer;

import mods.railcraft.Railcraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

public class RailcraftSheets {

  public static final ResourceLocation SIGNAL_ASPECTS_SHEET =
      new ResourceLocation(Railcraft.ID, "textures/atlas/signal_aspects.png");
  public static final ResourceLocation SIGNAL_BOX_ASPECTS_SHEET =
      new ResourceLocation(Railcraft.ID, "textures/atlas/signal_box_aspects.png");
  public static final ResourceLocation SIGNAL_BOXES_SHEET =
      new ResourceLocation(Railcraft.ID, "textures/atlas/signal_boxes.png");

  public static final RenderType SIGNAL_ASPECTS_TYPE =
      RenderType.entityCutout(SIGNAL_ASPECTS_SHEET);
  public static final RenderType SIGNAL_BOX_ASPECTS_TYPE =
      RenderType.entityCutout(SIGNAL_BOX_ASPECTS_SHEET);
  public static final RenderType SIGNAL_BOXES_TYPE =
      RenderType.entityCutout(SIGNAL_BOXES_SHEET);
}
