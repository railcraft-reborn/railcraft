package mods.railcraft.util;

import java.util.function.Function;
import javax.annotation.Nullable;
import com.google.gson.JsonObject;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;

public final class JsonTools {

  public static @Nullable <T> T whenPresent(JsonObject object, String tag,
      Function<JsonObject, ? extends T> function, @Nullable T fallback) {
    if (object.has(tag)) {
      return function.apply(object.getAsJsonObject(tag));
    }
    return fallback;
  }

  public static @Nullable Boolean nullableBoolean(JsonObject object, String tag) {
    return object.has(tag) ? object.get(tag).getAsBoolean() : null;
  }

  public static @Nullable <T extends IForgeRegistryEntry<T>> T getFromRegistryWhenPresent(
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
