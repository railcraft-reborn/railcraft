package mods.railcraft.data.models;

import com.google.common.collect.Maps;
import com.google.gson.JsonElement;
import com.mojang.logging.LogUtils;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Supplier;
import mods.railcraft.world.level.block.RailcraftBlocks;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.models.blockstates.BlockStateGenerator;
import net.minecraft.data.models.model.DelegatedModel;
import net.minecraft.data.models.model.ModelLocationUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;
import org.slf4j.Logger;

public class RailcraftModelProvider implements DataProvider {

  private static final Logger logger = LogUtils.getLogger();
  private final DataGenerator generator;

  public RailcraftModelProvider(DataGenerator generator) {
    this.generator = generator;
  }

  public void run(CachedOutput cachedOutput) {
    Map<Block, BlockStateGenerator> blockStates = Maps.newHashMap();
    Consumer<BlockStateGenerator> blockStateConsumer = (blockState) -> {
      Block block = blockState.getBlock();
      BlockStateGenerator existingBlockState = blockStates.put(block, blockState);
      if (existingBlockState != null) {
        throw new IllegalStateException("Duplicate blockstate definition for " + block);
      }
    };
    Map<ResourceLocation, Supplier<JsonElement>> models = Maps.newHashMap();
    BiConsumer<ResourceLocation, Supplier<JsonElement>> modelConsumer = (model, json) -> {
      Supplier<JsonElement> existingJson = models.put(model, json);
      if (existingJson != null) {
        throw new IllegalStateException("Duplicate model definition for " + model);
      }
    };

    Set<Item> skippedAutoModels = new HashSet<>();
    new RailcraftBlockModelGenerators(blockStateConsumer, modelConsumer, skippedAutoModels::add)
        .run();

    RailcraftBlocks.entries().forEach(entry -> {
      var block = entry.get();
      var item = Item.BY_BLOCK.get(block);
      if (item != null && !skippedAutoModels.contains(item)) {
        var itemModel = ModelLocationUtils.getModelLocation(item);
        if (!models.containsKey(itemModel)) {
          models.put(itemModel,
              new DelegatedModel(ModelLocationUtils.getModelLocation(block)));
        }
      }
    });

    Path outputFolder = this.generator.getOutputFolder();

    this.saveCollection(cachedOutput, outputFolder, blockStates,
        RailcraftModelProvider::createBlockStatePath);
    this.saveCollection(cachedOutput, outputFolder, models,
        RailcraftModelProvider::createModelPath);
  }

  private <T> void saveCollection(CachedOutput cachedOutput, Path p_240081_2_,
      Map<T, ? extends Supplier<JsonElement>> p_240081_3_, BiFunction<Path, T, Path> p_240081_4_) {
    p_240081_3_.forEach((p_240088_3_, p_240088_4_) -> {
      Path path = p_240081_4_.apply(p_240081_2_, p_240088_3_);

      try {
        DataProvider.saveStable(cachedOutput, p_240088_4_.get(), path);
      } catch (Exception exception) {
        logger.error("Couldn't save {}", path, exception);
      }

    });
  }

  private static Path createBlockStatePath(Path parentDir, Block block) {
    ResourceLocation blockName = ForgeRegistries.BLOCKS.getKey(block);
    return parentDir.resolve("assets/" + blockName.getNamespace() + "/blockstates/"
        + blockName.getPath() + ".json");
  }

  private static Path createModelPath(Path parentDir, ResourceLocation modelLocation) {
    return parentDir.resolve(
        "assets/" + modelLocation.getNamespace() + "/models/" + modelLocation.getPath() + ".json");
  }

  @Override
  public String getName() {
    return "Railcraft Block State Definitions";
  }
}
