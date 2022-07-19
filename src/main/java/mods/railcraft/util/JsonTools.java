package mods.railcraft.util;

import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistry;

import javax.annotation.Nullable;
import java.util.function.Function;

public final class JsonTools {

  @Nullable
  public static <T> T whenPresent(JsonObject object, String tag,
      Function<JsonObject, ? extends T> doesExisFunction, @Nullable T fallback) {
    if (object.has(tag)) {
      return doesExisFunction.apply(object.getAsJsonObject(tag));
    }
    return fallback;
  }

  public static @Nullable Boolean nullableBoolean(JsonObject object, String tag) {
    return object.has(tag) ? object.get(tag).getAsBoolean() : null;
  }

  public static @Nullable <T> T getFromRegistryWhenPresent(
      JsonObject object, String tag, IForgeRegistry<T> registry, @Nullable T fallback) {
    if (object.has(tag)) {
      String string = object.get(tag).getAsString();
      T ret = registry.getValue(new ResourceLocation(string));
      return ret == null ? fallback : ret;
    }
    return fallback;
  }

  private JsonTools() {}
}
