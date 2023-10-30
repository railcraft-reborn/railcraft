package mods.railcraft.util;

import java.util.Optional;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.IForgeRegistry;

public final class JsonUtil {

  private JsonUtil() {}

  public static <T> Optional<T> getFromRegistry(
      JsonObject object, String memberName, IForgeRegistry<T> registry) {
    return getAsString(object, memberName)
        .map(ResourceLocation::tryParse)
        .map(registry::getValue);
  }

  public static Optional<String> getAsString(JsonObject object, String memberName) {
    return get(object, memberName).map(JsonElement::getAsString);
  }

  public static Optional<Boolean> getAsBoolean(JsonObject object, String memberName) {
    return get(object, memberName).map(JsonElement::getAsBoolean);
  }

  public static Optional<JsonObject> getAsJsonObject(JsonObject object, String memberName) {
    return get(object, memberName).map(JsonElement::getAsJsonObject);
  }

  public static Optional<JsonElement> get(JsonObject object, String memberName) {
    return object.has(memberName)
        ? Optional.of(object.get(memberName))
        : Optional.empty();
  }
}
