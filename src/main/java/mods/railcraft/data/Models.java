package mods.railcraft.data;

import java.util.Optional;
import mods.railcraft.Railcraft;
import net.minecraft.data.ModelsUtil;
import net.minecraft.data.StockTextureAliases;
import net.minecraft.util.ResourceLocation;

public class Models {

  public static final ModelsUtil POST_COLUMN =
      create("post_full_column", "_full_column", StockTextureAliases.TEXTURE);
  public static final ModelsUtil POST_TOP_COLUMN =
      create("post_top_column", "_top_column", StockTextureAliases.TEXTURE);
  public static final ModelsUtil POST_DOUBLE_CONNECTION =
      create("post_double_connection", "_double_connection", StockTextureAliases.TEXTURE);
  public static final ModelsUtil POST_PLATFORM =
      create("post_platform", "_platform", StockTextureAliases.TEXTURE);
  public static final ModelsUtil POST_SMALL_COLUMN =
      create("post_small_column", "_small_column", StockTextureAliases.TEXTURE);
  public static final ModelsUtil POST_SINGLE_CONNECTION =
      create("post_single_connection", "_single_connection", StockTextureAliases.TEXTURE);
  public static final ModelsUtil POST_INVENTORY =
      create("post_inventory", "_inventory", StockTextureAliases.TEXTURE);

  private static ModelsUtil create(String name, String suffix,
      StockTextureAliases... textureAliases) {
    return new ModelsUtil(Optional.of(new ResourceLocation(Railcraft.ID, "block/" + name)),
        Optional.of(suffix), textureAliases);
  }
}
