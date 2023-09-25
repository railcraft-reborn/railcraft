package mods.railcraft.client.model;

import java.util.Set;
import com.google.common.collect.Sets;
import mods.railcraft.Railcraft;
import net.minecraft.client.model.geom.ModelLayerLocation;

public class RailcraftModelLayers {

  private static final Set<ModelLayerLocation> allModels = Sets.newHashSet();

  public static final ModelLayerLocation ELECTRIC_LOCOMOTIVE = register("electric_locomotive");
  public static final ModelLayerLocation ELECTRIC_LOCOMOTIVE_SNOW =
      register("electric_locomotive_snow");

  public static final ModelLayerLocation STEAM_LOCOMOTIVE = register("steam_locomotive");
  public static final ModelLayerLocation STEAM_LOCOMOTIVE_SNOW = register("steam_locomotive_snow");

  public static final ModelLayerLocation LOW_SIDES_MINECART = register("low_sides_minecart");
  public static final ModelLayerLocation LOW_SIDES_MINECART_SNOW =
      register("low_sides_minecart_snow");

  public static final ModelLayerLocation ELECTRIC_LOCOMOTIVE_LAMP =
      register("electric_locomotive_lamp");

  public static final ModelLayerLocation MAINTENANCE_LAMP = register("maintenance_lamp");
  public static final ModelLayerLocation MAINTENANCE = register("maintenance");

  public static final ModelLayerLocation TUNNEL_BORE = register("tunnel_bore");

  public static final ModelLayerLocation MINECART_SNOW = register("minecart_snow");

  public static final ModelLayerLocation CUBE = register("cube");

  private static ModelLayerLocation register(String model) {
    return register(model, "main");
  }

  private static ModelLayerLocation register(String model, String layer) {
    ModelLayerLocation layerLocation = createLocation(model, layer);
    if (!allModels.add(layerLocation)) {
      throw new IllegalStateException("Duplicate registration for " + layerLocation);
    } else {
      return layerLocation;
    }
  }

  private static ModelLayerLocation createLocation(String model, String layer) {
    return new ModelLayerLocation(Railcraft.rl(model), layer);
  }
}
