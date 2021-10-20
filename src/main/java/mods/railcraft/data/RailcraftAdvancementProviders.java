package mods.railcraft.data;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Sets;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

import mods.railcraft.data.advancements.CartAdvancements;
import net.minecraft.advancements.Advancement;
import net.minecraft.data.AdvancementProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DirectoryCache;
import net.minecraft.data.IDataProvider;
import net.minecraft.util.ResourceLocation;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class RailcraftAdvancementProviders extends AdvancementProvider {

  private static final Logger LOGGER = LogManager.getLogger();
  private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().create();
  private final DataGenerator generator;
  private final List<Consumer<Consumer<Advancement>>> tabs =
      ImmutableList.of(new CartAdvancements());

  public RailcraftAdvancementProviders(DataGenerator dataGenerator) {
    super(dataGenerator);
    this.generator = dataGenerator;
  }

  @Override
  public void run(DirectoryCache directoryCache) throws IOException {
    Path path = this.generator.getOutputFolder();
    Set<ResourceLocation> set = Sets.newHashSet();
    Consumer<Advancement> consumer = (advancement) -> {
      if (!set.add(advancement.getId())) {
        throw new IllegalStateException("Duplicate advancement " + advancement.getId());
      } else {
        Path path1 = createPath(path, advancement);

        try {
          IDataProvider.save(GSON, directoryCache,
              advancement.deconstruct().serializeToJson(), path1);
        } catch (IOException ioexception) {
          LOGGER.error("Couldn't save advancement {}", path1, ioexception);
        }

      }
    };

    for (Consumer<Consumer<Advancement>> consumer1 : this.tabs) {
      consumer1.accept(consumer);
    }
  }

  private static Path createPath(Path path, Advancement advancement) {
    return path.resolve("data/" + advancement.getId().getNamespace()
        + "/advancements/" + advancement.getId().getPath() + ".json");
  }

  @Override
  public String getName() {
    return "Railcraft Advancements";
  }
}
