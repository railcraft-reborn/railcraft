package mods.railcraft.util;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;
import mods.railcraft.RailcraftConfig;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

// Code copied from ImmersiveEngineering
public class RecipeUtil {

  public static <T> Optional<T> getPreferredElementbyMod(Stream<T> list,
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

  public static ItemStack getPreferredStackbyMod(ItemStack[] array) {
    return getPreferredElementbyMod(Arrays.stream(array), stack -> ForgeRegistries.ITEMS.getKey(stack.getItem()))
        .orElseThrow(() -> new RuntimeException("Empty array?"));
  }
}
