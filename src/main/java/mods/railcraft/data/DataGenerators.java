package mods.railcraft.data;

import mods.railcraft.data.models.RailcraftModelProvider;
import mods.railcraft.data.recipes.RailcraftRecipeProvider;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerators {
  @SubscribeEvent
  public static void gatherData(GatherDataEvent event) {
    var generator = event.getGenerator();
    var fileHelper = event.getExistingFileHelper();
    var blockTags = new RailcraftBlockTagsProvider(generator, fileHelper);
    generator.addProvider(event.includeServer(), blockTags);
    generator.addProvider(event.includeServer(), new RailcraftItemTagsProvider(generator, blockTags, fileHelper));
    generator.addProvider(event.includeServer(), new RailcraftFluidTagsProvider(generator, fileHelper));
    generator.addProvider(event.includeServer(), new RailcraftLootTableProvider(generator));
    generator.addProvider(event.includeServer(), new RailcraftAdvancementProviders(generator, fileHelper));
    generator.addProvider(event.includeServer(), new RailcraftRecipeProvider(generator));
    generator.addProvider(event.includeClient(), new RailcraftModelProvider(generator));
    generator.addProvider(event.includeClient(), new RailcraftLang(generator));
  }
}
