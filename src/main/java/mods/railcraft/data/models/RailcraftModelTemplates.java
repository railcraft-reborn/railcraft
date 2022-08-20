package mods.railcraft.data.models;

import java.util.Optional;
import mods.railcraft.Railcraft;
import net.minecraft.data.models.model.ModelTemplate;
import net.minecraft.data.models.model.TextureSlot;
import net.minecraft.resources.ResourceLocation;

public class RailcraftModelTemplates {

  public static final ModelTemplate ELEVATOR_TRACK =
      create("template_elevator_track", TextureSlot.TEXTURE);

  public static final ModelTemplate FORCE_TRACK =
      create("template_force_track", TextureSlot.RAIL);

  public static final ModelTemplate FACE_OVERLAY =
      create("face_overlay", TextureSlot.TEXTURE);

  public static final ResourceLocation BUFFER_STOP =
      new ResourceLocation(Railcraft.ID, "block/buffer_stop");

  private static ModelTemplate create(String name, TextureSlot... textureAliases) {
    return new ModelTemplate(Optional.of(new ResourceLocation(Railcraft.ID, "block/" + name)),
        Optional.empty(), textureAliases);
  }
}
