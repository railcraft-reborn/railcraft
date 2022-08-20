package mods.railcraft.data.models;

import java.util.Optional;
import mods.railcraft.Railcraft;
import net.minecraft.data.models.model.ModelTemplate;
import net.minecraft.data.models.model.TextureSlot;
import net.minecraft.resources.ResourceLocation;

public class RailcraftModelTemplates {


  public static final ModelTemplate STEAM_BOILER_TANK_NE =
      create("template_steam_boiler_tank_ne", "_ne", TextureSlot.END, TextureSlot.SIDE);
  public static final ModelTemplate STEAM_BOILER_TANK_NEW =
      create("template_steam_boiler_tank_new", "_new", TextureSlot.END, TextureSlot.SIDE);
  public static final ModelTemplate STEAM_BOILER_TANK_NSE =
      create("template_steam_boiler_tank_nse", "_nse", TextureSlot.END, TextureSlot.SIDE);
  public static final ModelTemplate STEAM_BOILER_TANK_NSW =
      create("template_steam_boiler_tank_nsw", "_nsw", TextureSlot.END, TextureSlot.SIDE);
  public static final ModelTemplate STEAM_BOILER_TANK_NW =
      create("template_steam_boiler_tank_nw", "_nw", TextureSlot.END, TextureSlot.SIDE);
  public static final ModelTemplate STEAM_BOILER_TANK_SE =
      create("template_steam_boiler_tank_se", "_se", TextureSlot.END, TextureSlot.SIDE);
  public static final ModelTemplate STEAM_BOILER_TANK_SEW =
      create("template_steam_boiler_tank_sew", "_sew", TextureSlot.END, TextureSlot.SIDE);
  public static final ModelTemplate STEAM_BOILER_TANK_SW =
      create("template_steam_boiler_tank_sw", "_sw", TextureSlot.END, TextureSlot.SIDE);

  public static final ModelTemplate STEAM_BOILER_TANK =
      create("template_steam_boiler_tank", TextureSlot.END, TextureSlot.SIDE);

  public static final ModelTemplate ELEVATOR_TRACK =
      create("template_elevator_track", TextureSlot.TEXTURE);

  public static final ModelTemplate FORCE_TRACK =
      create("template_force_track", TextureSlot.RAIL);

  public static final ModelTemplate FACE_OVERLAY =
      create("face_overlay", TextureSlot.TEXTURE);

  public static final ResourceLocation BUFFER_STOP =
      new ResourceLocation(Railcraft.ID, "block/buffer_stop");

  public static final ModelTemplate STEEL_ANVIL = create("template_steel_anvil", TextureSlot.TOP);

  private static ModelTemplate create(String name, TextureSlot... textureAliases) {
    return new ModelTemplate(Optional.of(new ResourceLocation(Railcraft.ID, "block/" + name)),
        Optional.empty(), textureAliases);
  }

  private static ModelTemplate create(String name, String suffix,
      TextureSlot... textureAliases) {
    return new ModelTemplate(Optional.of(new ResourceLocation(Railcraft.ID, "block/" + name)),
        Optional.of(suffix), textureAliases);
  }
}
