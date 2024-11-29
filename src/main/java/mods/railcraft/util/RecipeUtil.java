package mods.railcraft.util;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;
import mods.railcraft.RailcraftConfig;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

// Code copied from ImmersiveEngineering
public class RecipeUtil {

  public static <T> Optional<T> getPreferredElementByMod(Stream<T> list,
      Function<T, ResourceLocation> getName) {
    var mods = RailcraftConfig.SERVER.preferredOres.get();
    return list.min(
        Comparator.<T>comparingInt(t -> {
          var name = getName.apply(t);
          var modId = name.getNamespace();
          int idx = mods.indexOf(modId);
          if (idx < 0) {
            return mods.size();
          }
          return idx;
        }).thenComparing(getName)
    );
  }

  public static ItemStack getPreferredStackByMod(List<Holder<Item>> array) {
    return getPreferredElementByMod(array.stream().map(Holder::value), BuiltInRegistries.ITEM::getKey)
        .map(ItemStack::new)
        .orElseThrow(() -> new RuntimeException("Empty array?"));
  }
}
