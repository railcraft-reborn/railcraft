package mods.railcraft.data.recipes;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.data.recipes.builders.RailcraftSpecialRecipeBuilder;
import mods.railcraft.tags.RailcraftTags;
import mods.railcraft.util.VariantSet;
import mods.railcraft.world.item.RailcraftItems;
import mods.railcraft.world.item.crafting.ChestMinecartDisassemblyRecipe;
import mods.railcraft.world.item.crafting.LocomotivePaintingRecipe;
import mods.railcraft.world.item.crafting.RotorRepairRecipe;
import mods.railcraft.world.item.crafting.StoneTieRecipe;
import mods.railcraft.world.item.crafting.TicketDuplicateRecipe;
import mods.railcraft.world.item.crafting.VoidChestMinecartDisassemblyRecipe;
import mods.railcraft.world.item.crafting.WoodenTieRecipe;
import mods.railcraft.world.item.crafting.WorldSpikeMinecartDisassemblyRecipe;
import mods.railcraft.world.level.block.DecorativeBlock;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.SingleItemRecipeBuilder;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Tuple;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.Tags;

public class RailcraftRecipeProvider extends RecipeProvider {

  private final HolderLookup.RegistryLookup<Item> items;

  protected RailcraftRecipeProvider(HolderLookup.Provider registries, RecipeOutput output) {
    super(registries, output);
    this.items = registries.lookupOrThrow(Registries.ITEM);
  }

  public static class Runner extends RecipeProvider.Runner {

    public Runner(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
      super(output, registries);
    }

    @Override
    protected RecipeProvider createRecipeProvider(HolderLookup.Provider registries, RecipeOutput output) {
      return new RailcraftRecipeProvider(registries, output);
    }

    @Override
    public String getName() {
      return "RailcraftRecipeProvider";
    }
  }

  @Override
  protected void buildRecipes() {
    buildMultiblockBlocks();
    buildBlockStorageRecipes();
    buildIngotsRecipes();
    buildGears();
    buildKits();
    buildTankBlocks();
    buildPost();
    buildStrengthenedGlass();
    buildTie();
    buildCement();
    buildRails();
    buildTracks();
    buildSteelItems();
    buildTunnelBoreHead();
    buildMaul();
    buildOreSmelt();
    buildTurbineParts();
    buildChargeItems();
    buildSignalBox();
    buildSignals();
    buildCircuit();
    buildMiscItems();
    buildCartsVariant();
    buildSwitch();
    buildLoaders();
    buildCrowbars();
    buildFirestones();
    buildDecorativeStone();
    buildBattery();
    buildFrame();
    buildDetectors();
    buildWorldSpike();
  }

  private void conversion(ItemLike from, ItemLike to, int count, String optionalName) {
    Identifier path;
    if (optionalName.isEmpty()) {
      path = RecipeBuilder.getDefaultRecipeId(to);
    } else {
      path = RailcraftConstants.id(optionalName);
    }
    shapeless(RecipeCategory.MISC, to, count)
        .requires(from)
        .unlockedBy(getHasName(from), has(from))
        .save(output, path.toString());
  }


  private void buildRails() {
    railsFromMaterials(RailcraftItems.ABANDONED_TRACK.get(), 32,
        RailcraftItems.STANDARD_RAIL.get(), RailcraftItems.WOODEN_TIE.get());
    railsFromMaterials(RailcraftItems.STRAP_IRON_TRACK.get(), 32,
        RailcraftItems.WOODEN_RAIL.get(), RailcraftItems.WOODEN_RAILBED.get());
    railsFromMaterials(Items.RAIL, 32,
        RailcraftItems.STANDARD_RAIL.get(), RailcraftItems.WOODEN_RAILBED.get());
    railsFromMaterials(RailcraftItems.REINFORCED_TRACK.get(), 32,
        RailcraftItems.REINFORCED_RAIL.get(), RailcraftItems.STONE_RAILBED.get());
    railsFromMaterials(RailcraftItems.ELECTRIC_TRACK.get(), 32,
        RailcraftItems.ELECTRIC_RAIL.get(), RailcraftItems.STONE_RAILBED.get());
    railsFromMaterials(RailcraftItems.HIGH_SPEED_TRACK.get(), 32,
        RailcraftItems.HIGH_SPEED_RAIL.get(), RailcraftItems.STONE_RAILBED.get());
    railsFromMaterials(RailcraftItems.HIGH_SPEED_ELECTRIC_TRACK.get(), 32,
        RailcraftItems.HIGH_SPEED_RAIL.get(), RailcraftItems.STONE_RAILBED.get(),
        RailcraftItems.ELECTRIC_RAIL.get());
    railsFromMaterials(RailcraftItems.ELEVATOR_TRACK.get(), 8,
        RailcraftItems.ADVANCED_RAIL.get(), RailcraftItems.STANDARD_RAIL.get(),
        Items.REDSTONE);

    shapeless(RecipeCategory.MISC, RailcraftItems.WOODEN_RAIL.get(), 6)
        .requires(RailcraftItems.WOODEN_TIE.get())
        .requires(Tags.Items.INGOTS_IRON)
        .unlockedBy(getHasName(RailcraftItems.WOODEN_TIE.get()),
            has(RailcraftItems.WOODEN_TIE.get()))
        .save(output);

    shapeless(RecipeCategory.MISC, RailcraftItems.STANDARD_RAIL.get())
        .requires(Items.RAIL, 8)
        .unlockedBy(getHasName(Items.RAIL), has(Items.RAIL))
        .save(output, RailcraftConstants.id("standard_rail_from_rail").toString());

    shapeless(RecipeCategory.MISC, RailcraftItems.WOODEN_RAILBED.get())
        .requires(RailcraftItems.WOODEN_TIE.get(), 4)
        .unlockedBy(getHasName(RailcraftItems.WOODEN_TIE.get()),
            has(RailcraftItems.WOODEN_TIE.get()))
        .save(output);

    shapeless(RecipeCategory.MISC, RailcraftItems.STONE_RAILBED.get())
        .requires(RailcraftItems.STONE_TIE.get(), 4)
        .unlockedBy(getHasName(RailcraftItems.WOODEN_TIE.get()),
            has(RailcraftItems.WOODEN_TIE.get()))
        .save(output);
  }

  private void railsFromMaterials(Item result, int count, Item railType, Item railBedType) {
    shaped(RecipeCategory.MISC, result, count)
        .pattern("a a")
        .pattern("aba")
        .pattern("a a")
        .define('a', railType)
        .define('b', railBedType)
        .unlockedBy(getHasName(railType), has(railType))
        .unlockedBy(getHasName(railBedType), has(railBedType))
        .save(output);
  }

  private void railsFromMaterials(Item result, int count, Item railType, Item railBedType,
      Item optionalItem) {
    shaped(RecipeCategory.MISC, result, count)
        .pattern("aca")
        .pattern("aba")
        .pattern("aca")
        .define('a', railType)
        .define('b', railBedType)
        .define('c', optionalItem)
        .unlockedBy(getHasName(railType), has(railType))
        .unlockedBy(getHasName(railBedType), has(railBedType))
        .save(output);
  }

  private void buildTracks() {
    tracks(RailcraftItems.ABANDONED_ACTIVATOR_TRACK.get(),
        RailcraftItems.ACTIVATOR_TRACK_KIT.get(), RailcraftItems.ABANDONED_TRACK.get());
    tracks(RailcraftItems.ABANDONED_BOOSTER_TRACK.get(),
        RailcraftItems.BOOSTER_TRACK_KIT.get(), RailcraftItems.ABANDONED_TRACK.get());
    tracks(RailcraftItems.ABANDONED_BUFFER_STOP_TRACK.get(),
        RailcraftItems.BUFFER_STOP_TRACK_KIT.get(), RailcraftItems.ABANDONED_TRACK.get());
    tracks(RailcraftItems.ABANDONED_COUPLER_TRACK.get(),
        RailcraftItems.COUPLER_TRACK_KIT.get(), RailcraftItems.ABANDONED_TRACK.get());
    tracks(RailcraftItems.ABANDONED_CONTROL_TRACK.get(),
        RailcraftItems.CONTROL_TRACK_KIT.get(), RailcraftItems.ABANDONED_TRACK.get());
    tracks(RailcraftItems.ABANDONED_DETECTOR_TRACK.get(),
        RailcraftItems.DETECTOR_TRACK_KIT.get(), RailcraftItems.ABANDONED_TRACK.get());
    tracks(RailcraftItems.ABANDONED_DISEMBARKING_TRACK.get(),
        RailcraftItems.DISEMBARKING_TRACK_KIT.get(), RailcraftItems.ABANDONED_TRACK.get());
    tracks(RailcraftItems.ABANDONED_EMBARKING_TRACK.get(),
        RailcraftItems.EMBARKING_TRACK_KIT.get(), RailcraftItems.ABANDONED_TRACK.get());
    tracks(RailcraftItems.ABANDONED_DUMPING_TRACK.get(),
        RailcraftItems.DUMPING_TRACK_KIT.get(), RailcraftItems.ABANDONED_TRACK.get());
    tracks(RailcraftItems.ABANDONED_GATED_TRACK.get(),
        RailcraftItems.GATED_TRACK_KIT.get(), RailcraftItems.ABANDONED_TRACK.get());
    tracks(RailcraftItems.ABANDONED_LAUNCHER_TRACK.get(),
        RailcraftItems.LAUNCHER_TRACK_KIT.get(), RailcraftItems.ABANDONED_TRACK.get());
    tracks(RailcraftItems.ABANDONED_LOCKING_TRACK.get(),
        RailcraftItems.LOCKING_TRACK_KIT.get(), RailcraftItems.ABANDONED_TRACK.get());
    tracks(RailcraftItems.ABANDONED_WHISTLE_TRACK.get(),
        RailcraftItems.WHISTLE_TRACK_KIT.get(), RailcraftItems.ABANDONED_TRACK.get());
    tracks(RailcraftItems.ABANDONED_LOCOMOTIVE_TRACK.get(),
        RailcraftItems.LOCOMOTIVE_TRACK_KIT.get(), RailcraftItems.ABANDONED_TRACK.get());
    tracks(RailcraftItems.ABANDONED_THROTTLE_TRACK.get(),
        RailcraftItems.THROTTLE_TRACK_KIT.get(), RailcraftItems.ABANDONED_TRACK.get());
    tracks(RailcraftItems.ABANDONED_ONE_WAY_TRACK.get(),
        RailcraftItems.ONE_WAY_TRACK_KIT.get(), RailcraftItems.ABANDONED_TRACK.get());
    tracks(RailcraftItems.ABANDONED_ROUTING_TRACK.get(),
        RailcraftItems.ROUTING_TRACK_KIT.get(), RailcraftItems.ABANDONED_TRACK.get());

    tracks(RailcraftItems.IRON_ACTIVATOR_TRACK.get(),
        RailcraftItems.ACTIVATOR_TRACK_KIT.get(), Items.RAIL);
    tracks(RailcraftItems.IRON_BOOSTER_TRACK.get(),
        RailcraftItems.BOOSTER_TRACK_KIT.get(), Items.RAIL);
    tracks(RailcraftItems.IRON_BUFFER_STOP_TRACK.get(),
        RailcraftItems.BUFFER_STOP_TRACK_KIT.get(), Items.RAIL);
    tracks(RailcraftItems.IRON_COUPLER_TRACK.get(),
        RailcraftItems.COUPLER_TRACK_KIT.get(), Items.RAIL);
    tracks(RailcraftItems.IRON_CONTROL_TRACK.get(),
        RailcraftItems.CONTROL_TRACK_KIT.get(), Items.RAIL);
    tracks(RailcraftItems.IRON_DETECTOR_TRACK.get(),
        RailcraftItems.DETECTOR_TRACK_KIT.get(), Items.RAIL);
    tracks(RailcraftItems.IRON_DISEMBARKING_TRACK.get(),
        RailcraftItems.DISEMBARKING_TRACK_KIT.get(), Items.RAIL);
    tracks(RailcraftItems.IRON_EMBARKING_TRACK.get(),
        RailcraftItems.EMBARKING_TRACK_KIT.get(), Items.RAIL);
    tracks(RailcraftItems.IRON_DUMPING_TRACK.get(),
        RailcraftItems.DUMPING_TRACK_KIT.get(), Items.RAIL);
    tracks(RailcraftItems.IRON_GATED_TRACK.get(),
        RailcraftItems.GATED_TRACK_KIT.get(), Items.RAIL);
    tracks(RailcraftItems.IRON_LAUNCHER_TRACK.get(),
        RailcraftItems.LAUNCHER_TRACK_KIT.get(), Items.RAIL);
    tracks(RailcraftItems.IRON_LOCKING_TRACK.get(),
        RailcraftItems.LOCKING_TRACK_KIT.get(), Items.RAIL);
    tracks(RailcraftItems.IRON_WHISTLE_TRACK.get(),
        RailcraftItems.WHISTLE_TRACK_KIT.get(), Items.RAIL);
    tracks(RailcraftItems.IRON_LOCOMOTIVE_TRACK.get(),
        RailcraftItems.LOCOMOTIVE_TRACK_KIT.get(), Items.RAIL);
    tracks(RailcraftItems.IRON_THROTTLE_TRACK.get(),
        RailcraftItems.THROTTLE_TRACK_KIT.get(), Items.RAIL);
    tracks(RailcraftItems.IRON_ONE_WAY_TRACK.get(),
        RailcraftItems.ONE_WAY_TRACK_KIT.get(), Items.RAIL);
    tracks(RailcraftItems.IRON_ROUTING_TRACK.get(),
        RailcraftItems.ROUTING_TRACK_KIT.get(), Items.RAIL);

    tracks(RailcraftItems.STRAP_IRON_ACTIVATOR_TRACK.get(),
        RailcraftItems.ACTIVATOR_TRACK_KIT.get(), RailcraftItems.STRAP_IRON_TRACK.get());
    tracks(RailcraftItems.STRAP_IRON_BOOSTER_TRACK.get(),
        RailcraftItems.BOOSTER_TRACK_KIT.get(), RailcraftItems.STRAP_IRON_TRACK.get());
    tracks(RailcraftItems.STRAP_IRON_BUFFER_STOP_TRACK.get(),
        RailcraftItems.BUFFER_STOP_TRACK_KIT.get(), RailcraftItems.STRAP_IRON_TRACK.get());
    tracks(RailcraftItems.STRAP_IRON_COUPLER_TRACK.get(),
        RailcraftItems.COUPLER_TRACK_KIT.get(), RailcraftItems.STRAP_IRON_TRACK.get());
    tracks(RailcraftItems.STRAP_IRON_CONTROL_TRACK.get(),
        RailcraftItems.CONTROL_TRACK_KIT.get(), RailcraftItems.STRAP_IRON_TRACK.get());
    tracks(RailcraftItems.STRAP_IRON_DETECTOR_TRACK.get(),
        RailcraftItems.DETECTOR_TRACK_KIT.get(), RailcraftItems.STRAP_IRON_TRACK.get());
    tracks(RailcraftItems.STRAP_IRON_DISEMBARKING_TRACK.get(),
        RailcraftItems.DISEMBARKING_TRACK_KIT.get(), RailcraftItems.STRAP_IRON_TRACK.get());
    tracks(RailcraftItems.STRAP_IRON_EMBARKING_TRACK.get(),
        RailcraftItems.EMBARKING_TRACK_KIT.get(), RailcraftItems.STRAP_IRON_TRACK.get());
    tracks(RailcraftItems.STRAP_IRON_DUMPING_TRACK.get(),
        RailcraftItems.DUMPING_TRACK_KIT.get(), RailcraftItems.STRAP_IRON_TRACK.get());
    tracks(RailcraftItems.STRAP_IRON_GATED_TRACK.get(),
        RailcraftItems.GATED_TRACK_KIT.get(), RailcraftItems.STRAP_IRON_TRACK.get());
    tracks(RailcraftItems.STRAP_IRON_LAUNCHER_TRACK.get(),
        RailcraftItems.LAUNCHER_TRACK_KIT.get(), RailcraftItems.STRAP_IRON_TRACK.get());
    tracks(RailcraftItems.STRAP_IRON_LOCKING_TRACK.get(),
        RailcraftItems.LOCKING_TRACK_KIT.get(), RailcraftItems.STRAP_IRON_TRACK.get());
    tracks(RailcraftItems.STRAP_IRON_WHISTLE_TRACK.get(),
        RailcraftItems.WHISTLE_TRACK_KIT.get(), RailcraftItems.STRAP_IRON_TRACK.get());
    tracks(RailcraftItems.STRAP_IRON_LOCOMOTIVE_TRACK.get(),
        RailcraftItems.LOCOMOTIVE_TRACK_KIT.get(), RailcraftItems.STRAP_IRON_TRACK.get());
    tracks(RailcraftItems.STRAP_IRON_THROTTLE_TRACK.get(),
        RailcraftItems.THROTTLE_TRACK_KIT.get(), RailcraftItems.STRAP_IRON_TRACK.get());
    tracks(RailcraftItems.STRAP_IRON_ONE_WAY_TRACK.get(),
        RailcraftItems.ONE_WAY_TRACK_KIT.get(), RailcraftItems.STRAP_IRON_TRACK.get());
    tracks(RailcraftItems.STRAP_IRON_ROUTING_TRACK.get(),
        RailcraftItems.ROUTING_TRACK_KIT.get(), RailcraftItems.STRAP_IRON_TRACK.get());

    tracks(RailcraftItems.REINFORCED_ACTIVATOR_TRACK.get(),
        RailcraftItems.ACTIVATOR_TRACK_KIT.get(), RailcraftItems.REINFORCED_TRACK.get());
    tracks(RailcraftItems.REINFORCED_BOOSTER_TRACK.get(),
        RailcraftItems.BOOSTER_TRACK_KIT.get(), RailcraftItems.REINFORCED_TRACK.get());
    tracks(RailcraftItems.REINFORCED_BUFFER_STOP_TRACK.get(),
        RailcraftItems.BUFFER_STOP_TRACK_KIT.get(), RailcraftItems.REINFORCED_TRACK.get());
    tracks(RailcraftItems.REINFORCED_COUPLER_TRACK.get(),
        RailcraftItems.COUPLER_TRACK_KIT.get(), RailcraftItems.REINFORCED_TRACK.get());
    tracks(RailcraftItems.REINFORCED_CONTROL_TRACK.get(),
        RailcraftItems.CONTROL_TRACK_KIT.get(), RailcraftItems.REINFORCED_TRACK.get());
    tracks(RailcraftItems.REINFORCED_DETECTOR_TRACK.get(),
        RailcraftItems.DETECTOR_TRACK_KIT.get(), RailcraftItems.REINFORCED_TRACK.get());
    tracks(RailcraftItems.REINFORCED_DISEMBARKING_TRACK.get(),
        RailcraftItems.DISEMBARKING_TRACK_KIT.get(), RailcraftItems.REINFORCED_TRACK.get());
    tracks(RailcraftItems.REINFORCED_EMBARKING_TRACK.get(),
        RailcraftItems.EMBARKING_TRACK_KIT.get(), RailcraftItems.REINFORCED_TRACK.get());
    tracks(RailcraftItems.REINFORCED_DUMPING_TRACK.get(),
        RailcraftItems.DUMPING_TRACK_KIT.get(), RailcraftItems.REINFORCED_TRACK.get());
    tracks(RailcraftItems.REINFORCED_GATED_TRACK.get(),
        RailcraftItems.GATED_TRACK_KIT.get(), RailcraftItems.REINFORCED_TRACK.get());
    tracks(RailcraftItems.REINFORCED_LAUNCHER_TRACK.get(),
        RailcraftItems.LAUNCHER_TRACK_KIT.get(), RailcraftItems.REINFORCED_TRACK.get());
    tracks(RailcraftItems.REINFORCED_LOCKING_TRACK.get(),
        RailcraftItems.LOCKING_TRACK_KIT.get(), RailcraftItems.REINFORCED_TRACK.get());
    tracks(RailcraftItems.REINFORCED_WHISTLE_TRACK.get(),
        RailcraftItems.WHISTLE_TRACK_KIT.get(), RailcraftItems.REINFORCED_TRACK.get());
    tracks(RailcraftItems.REINFORCED_LOCOMOTIVE_TRACK.get(),
        RailcraftItems.LOCOMOTIVE_TRACK_KIT.get(), RailcraftItems.REINFORCED_TRACK.get());
    tracks(RailcraftItems.REINFORCED_THROTTLE_TRACK.get(),
        RailcraftItems.THROTTLE_TRACK_KIT.get(), RailcraftItems.REINFORCED_TRACK.get());
    tracks(RailcraftItems.REINFORCED_ONE_WAY_TRACK.get(),
        RailcraftItems.ONE_WAY_TRACK_KIT.get(), RailcraftItems.REINFORCED_TRACK.get());
    tracks(RailcraftItems.REINFORCED_ROUTING_TRACK.get(),
        RailcraftItems.ROUTING_TRACK_KIT.get(), RailcraftItems.REINFORCED_TRACK.get());

    tracks(RailcraftItems.ELECTRIC_ACTIVATOR_TRACK.get(),
        RailcraftItems.ACTIVATOR_TRACK_KIT.get(), RailcraftItems.ELECTRIC_TRACK.get());
    tracks(RailcraftItems.ELECTRIC_BOOSTER_TRACK.get(),
        RailcraftItems.BOOSTER_TRACK_KIT.get(), RailcraftItems.ELECTRIC_TRACK.get());
    tracks(RailcraftItems.ELECTRIC_BUFFER_STOP_TRACK.get(),
        RailcraftItems.BUFFER_STOP_TRACK_KIT.get(), RailcraftItems.ELECTRIC_TRACK.get());
    tracks(RailcraftItems.ELECTRIC_COUPLER_TRACK.get(),
        RailcraftItems.COUPLER_TRACK_KIT.get(), RailcraftItems.ELECTRIC_TRACK.get());
    tracks(RailcraftItems.ELECTRIC_CONTROL_TRACK.get(),
        RailcraftItems.CONTROL_TRACK_KIT.get(), RailcraftItems.ELECTRIC_TRACK.get());
    tracks(RailcraftItems.ELECTRIC_DETECTOR_TRACK.get(),
        RailcraftItems.DETECTOR_TRACK_KIT.get(), RailcraftItems.ELECTRIC_TRACK.get());
    tracks(RailcraftItems.ELECTRIC_DISEMBARKING_TRACK.get(),
        RailcraftItems.DISEMBARKING_TRACK_KIT.get(), RailcraftItems.ELECTRIC_TRACK.get());
    tracks(RailcraftItems.ELECTRIC_EMBARKING_TRACK.get(),
        RailcraftItems.EMBARKING_TRACK_KIT.get(), RailcraftItems.ELECTRIC_TRACK.get());
    tracks(RailcraftItems.ELECTRIC_DUMPING_TRACK.get(),
        RailcraftItems.DUMPING_TRACK_KIT.get(), RailcraftItems.ELECTRIC_TRACK.get());
    tracks(RailcraftItems.ELECTRIC_GATED_TRACK.get(),
        RailcraftItems.GATED_TRACK_KIT.get(), RailcraftItems.ELECTRIC_TRACK.get());
    tracks(RailcraftItems.ELECTRIC_LAUNCHER_TRACK.get(),
        RailcraftItems.LAUNCHER_TRACK_KIT.get(), RailcraftItems.ELECTRIC_TRACK.get());
    tracks(RailcraftItems.ELECTRIC_LOCKING_TRACK.get(),
        RailcraftItems.LOCKING_TRACK_KIT.get(), RailcraftItems.ELECTRIC_TRACK.get());
    tracks(RailcraftItems.ELECTRIC_WHISTLE_TRACK.get(),
        RailcraftItems.WHISTLE_TRACK_KIT.get(), RailcraftItems.ELECTRIC_TRACK.get());
    tracks(RailcraftItems.ELECTRIC_LOCOMOTIVE_TRACK.get(),
        RailcraftItems.LOCOMOTIVE_TRACK_KIT.get(), RailcraftItems.ELECTRIC_TRACK.get());
    tracks(RailcraftItems.ELECTRIC_THROTTLE_TRACK.get(),
        RailcraftItems.THROTTLE_TRACK_KIT.get(), RailcraftItems.ELECTRIC_TRACK.get());
    tracks(RailcraftItems.ELECTRIC_ONE_WAY_TRACK.get(),
        RailcraftItems.ONE_WAY_TRACK_KIT.get(), RailcraftItems.ELECTRIC_TRACK.get());
    tracks(RailcraftItems.ELECTRIC_ROUTING_TRACK.get(),
        RailcraftItems.ROUTING_TRACK_KIT.get(), RailcraftItems.ELECTRIC_TRACK.get());

    tracks(RailcraftItems.HIGH_SPEED_ACTIVATOR_TRACK.get(),
        RailcraftItems.ACTIVATOR_TRACK_KIT.get(), RailcraftItems.HIGH_SPEED_TRACK.get());
    tracks(RailcraftItems.HIGH_SPEED_BOOSTER_TRACK.get(),
        RailcraftItems.BOOSTER_TRACK_KIT.get(), RailcraftItems.HIGH_SPEED_TRACK.get());
    tracks(RailcraftItems.HIGH_SPEED_DETECTOR_TRACK.get(),
        RailcraftItems.DETECTOR_TRACK_KIT.get(), RailcraftItems.HIGH_SPEED_TRACK.get());
    tracks(RailcraftItems.HIGH_SPEED_LOCKING_TRACK.get(),
        RailcraftItems.LOCKING_TRACK_KIT.get(), RailcraftItems.HIGH_SPEED_TRACK.get());
    tracks(RailcraftItems.HIGH_SPEED_WHISTLE_TRACK.get(),
        RailcraftItems.WHISTLE_TRACK_KIT.get(), RailcraftItems.HIGH_SPEED_TRACK.get());
    tracks(RailcraftItems.HIGH_SPEED_LOCOMOTIVE_TRACK.get(),
        RailcraftItems.LOCOMOTIVE_TRACK_KIT.get(), RailcraftItems.HIGH_SPEED_TRACK.get());
    tracks(RailcraftItems.HIGH_SPEED_THROTTLE_TRACK.get(),
        RailcraftItems.THROTTLE_TRACK_KIT.get(), RailcraftItems.HIGH_SPEED_TRACK.get());
    tracks(RailcraftItems.HIGH_SPEED_TRANSITION_TRACK.get(),
        RailcraftItems.TRANSITION_TRACK_KIT.get(), RailcraftItems.HIGH_SPEED_TRACK.get());

    tracks(RailcraftItems.HIGH_SPEED_ELECTRIC_ACTIVATOR_TRACK.get(),
        RailcraftItems.ACTIVATOR_TRACK_KIT.get(), RailcraftItems.HIGH_SPEED_ELECTRIC_TRACK.get());
    tracks(RailcraftItems.HIGH_SPEED_ELECTRIC_BOOSTER_TRACK.get(),
        RailcraftItems.BOOSTER_TRACK_KIT.get(), RailcraftItems.HIGH_SPEED_ELECTRIC_TRACK.get());
    tracks(RailcraftItems.HIGH_SPEED_ELECTRIC_DETECTOR_TRACK.get(),
        RailcraftItems.DETECTOR_TRACK_KIT.get(), RailcraftItems.HIGH_SPEED_ELECTRIC_TRACK.get());
    tracks(RailcraftItems.HIGH_SPEED_ELECTRIC_LOCKING_TRACK.get(),
        RailcraftItems.LOCKING_TRACK_KIT.get(), RailcraftItems.HIGH_SPEED_ELECTRIC_TRACK.get());
    tracks(RailcraftItems.HIGH_SPEED_ELECTRIC_WHISTLE_TRACK.get(),
        RailcraftItems.WHISTLE_TRACK_KIT.get(), RailcraftItems.HIGH_SPEED_ELECTRIC_TRACK.get());
    tracks(RailcraftItems.HIGH_SPEED_ELECTRIC_LOCOMOTIVE_TRACK.get(),
        RailcraftItems.LOCOMOTIVE_TRACK_KIT.get(), RailcraftItems.HIGH_SPEED_ELECTRIC_TRACK.get());
    tracks(RailcraftItems.HIGH_SPEED_ELECTRIC_THROTTLE_TRACK.get(),
        RailcraftItems.THROTTLE_TRACK_KIT.get(), RailcraftItems.HIGH_SPEED_ELECTRIC_TRACK.get());
    tracks(RailcraftItems.HIGH_SPEED_ELECTRIC_TRANSITION_TRACK.get(),
        RailcraftItems.TRANSITION_TRACK_KIT.get(), RailcraftItems.HIGH_SPEED_ELECTRIC_TRACK.get());

    wyeTracks(RailcraftItems.STRAP_IRON_WYE_TRACK.get(),
        RailcraftItems.WOODEN_RAIL.get(), RailcraftItems.WOODEN_RAILBED.get());
    wyeTracks(RailcraftItems.ABANDONED_WYE_TRACK.get(),
        RailcraftItems.STANDARD_RAIL.get(), RailcraftItems.WOODEN_TIE.get());
    wyeTracks(RailcraftItems.IRON_WYE_TRACK.get(),
        RailcraftItems.STANDARD_RAIL.get(), RailcraftItems.WOODEN_RAILBED.get());
    wyeTracks(RailcraftItems.REINFORCED_WYE_TRACK.get(),
        RailcraftItems.REINFORCED_RAIL.get(), RailcraftItems.STONE_RAILBED.get());
    wyeTracks(RailcraftItems.ELECTRIC_WYE_TRACK.get(),
        RailcraftItems.ELECTRIC_RAIL.get(), RailcraftItems.STONE_RAILBED.get());
    wyeTracks(RailcraftItems.HIGH_SPEED_WYE_TRACK.get(),
        RailcraftItems.HIGH_SPEED_RAIL.get(), RailcraftItems.STONE_RAILBED.get());

    turnoutTracks(RailcraftItems.STRAP_IRON_TURNOUT_TRACK.get(),
        RailcraftItems.WOODEN_RAIL.get(), RailcraftItems.WOODEN_RAILBED.get());
    turnoutTracks(RailcraftItems.ABANDONED_TURNOUT_TRACK.get(),
        RailcraftItems.STANDARD_RAIL.get(), RailcraftItems.WOODEN_TIE.get());
    turnoutTracks(RailcraftItems.IRON_TURNOUT_TRACK.get(),
        RailcraftItems.STANDARD_RAIL.get(), RailcraftItems.WOODEN_RAILBED.get());
    turnoutTracks(RailcraftItems.REINFORCED_TURNOUT_TRACK.get(),
        RailcraftItems.REINFORCED_RAIL.get(), RailcraftItems.STONE_RAILBED.get());
    turnoutTracks(RailcraftItems.ELECTRIC_TURNOUT_TRACK.get(),
        RailcraftItems.ELECTRIC_RAIL.get(), RailcraftItems.STONE_RAILBED.get());
    turnoutTracks(RailcraftItems.HIGH_SPEED_TURNOUT_TRACK.get(),
        RailcraftItems.HIGH_SPEED_RAIL.get(), RailcraftItems.STONE_RAILBED.get());

    junctionTracks(RailcraftItems.STRAP_IRON_JUNCTION_TRACK.get(),
        RailcraftItems.WOODEN_RAIL.get(), RailcraftItems.WOODEN_RAILBED.get());
    junctionTracks(RailcraftItems.ABANDONED_JUNCTION_TRACK.get(),
        RailcraftItems.STANDARD_RAIL.get(), RailcraftItems.WOODEN_TIE.get());
    junctionTracks(RailcraftItems.IRON_JUNCTION_TRACK.get(),
        RailcraftItems.STANDARD_RAIL.get(), RailcraftItems.WOODEN_RAILBED.get());
    junctionTracks(RailcraftItems.REINFORCED_JUNCTION_TRACK.get(),
        RailcraftItems.REINFORCED_RAIL.get(), RailcraftItems.STONE_RAILBED.get());
    junctionTracks(RailcraftItems.ELECTRIC_JUNCTION_TRACK.get(),
        RailcraftItems.ELECTRIC_RAIL.get(), RailcraftItems.STONE_RAILBED.get());
    junctionTracks(RailcraftItems.HIGH_SPEED_JUNCTION_TRACK.get(),
        RailcraftItems.HIGH_SPEED_RAIL.get(), RailcraftItems.STONE_RAILBED.get());


    shaped(RecipeCategory.MISC, RailcraftItems.HIGH_SPEED_ELECTRIC_WYE_TRACK.get(), 16)
        .pattern("aba")
        .pattern("aac")
        .pattern("aba")
        .define('a', RailcraftItems.HIGH_SPEED_RAIL.get())
        .define('b', RailcraftItems.ELECTRIC_RAIL.get())
        .define('c', RailcraftItems.STONE_RAILBED.get())
        .unlockedBy(getHasName(RailcraftItems.HIGH_SPEED_RAIL.get()),
            has(RailcraftItems.HIGH_SPEED_RAIL.get()))
        .save(output);
    shaped(RecipeCategory.MISC, RailcraftItems.HIGH_SPEED_ELECTRIC_TURNOUT_TRACK.get(), 16)
        .pattern("aca")
        .pattern("aba")
        .pattern("aba")
        .define('a', RailcraftItems.HIGH_SPEED_RAIL.get())
        .define('b', RailcraftItems.ELECTRIC_RAIL.get())
        .define('c', RailcraftItems.STONE_RAILBED.get())
        .unlockedBy(getHasName(RailcraftItems.HIGH_SPEED_RAIL.get()),
            has(RailcraftItems.HIGH_SPEED_RAIL.get()))
        .save(output);
    shaped(RecipeCategory.MISC, RailcraftItems.HIGH_SPEED_ELECTRIC_JUNCTION_TRACK.get(), 16)
        .pattern("aba")
        .pattern("bcb")
        .pattern("aba")
        .define('a', RailcraftItems.HIGH_SPEED_RAIL.get())
        .define('b', RailcraftItems.ELECTRIC_RAIL.get())
        .define('c', RailcraftItems.STONE_RAILBED.get())
        .unlockedBy(getHasName(RailcraftItems.HIGH_SPEED_RAIL.get()),
            has(RailcraftItems.HIGH_SPEED_RAIL.get()))
        .save(output);
  }

  private void tracks(Item result, Item kit, Item baseTrack) {
    shapeless(RecipeCategory.MISC, result)
        .requires(kit)
        .requires(baseTrack)
        .unlockedBy(getHasName(kit), has(kit))
        .save(output);
  }

  private void wyeTracks(Item result, Item rail, Item railBed) {
    shaped(RecipeCategory.MISC, result, 16)
        .pattern("aaa")
        .pattern("aab")
        .pattern("aaa")
        .define('a', rail)
        .define('b', railBed)
        .unlockedBy(getHasName(rail), has(rail))
        .save(output);
  }

  private void turnoutTracks(Item result, Item rail, Item railBed) {
    shaped(RecipeCategory.MISC, result, 16)
        .pattern("aba")
        .pattern("aaa")
        .pattern("aaa")
        .define('a', rail)
        .define('b', railBed)
        .unlockedBy(getHasName(rail), has(rail))
        .save(output);
  }

  private void junctionTracks(Item result, Item rail, Item railBed) {
    shaped(RecipeCategory.MISC, result, 16)
        .pattern("aaa")
        .pattern("aba")
        .pattern("aaa")
        .define('a', rail)
        .define('b', railBed)
        .unlockedBy(getHasName(rail), has(rail))
        .save(output);
  }

  private void buildSteelItems() {
    shaped(RecipeCategory.MISC, RailcraftItems.STEEL_ANVIL.get())
        .pattern("aaa")
        .pattern(" b ")
        .pattern("bbb")
        .define('a', RailcraftTags.Items.STEEL_BLOCK)
        .define('b', RailcraftTags.Items.STEEL_INGOT)
        .unlockedBy(getHasName(RailcraftItems.STEEL_BLOCK.get()),
            has(RailcraftTags.Items.STEEL_BLOCK))
        .save(output);
    shaped(RecipeCategory.MISC, RailcraftItems.STEEL_SHEARS.get())
        .pattern(" a")
        .pattern("a ")
        .define('a', RailcraftTags.Items.STEEL_INGOT)
        .unlockedBy(getHasName(RailcraftItems.STEEL_INGOT.get()),
            has(RailcraftTags.Items.STEEL_INGOT))
        .save(output);
    shaped(RecipeCategory.MISC, RailcraftItems.STEEL_SWORD.get())
        .pattern("a")
        .pattern("a")
        .pattern("b")
        .define('a', RailcraftTags.Items.STEEL_INGOT)
        .define('b', Tags.Items.RODS_WOODEN)
        .unlockedBy(getHasName(RailcraftItems.STEEL_INGOT.get()),
            has(RailcraftTags.Items.STEEL_INGOT))
        .save(output);
    shaped(RecipeCategory.MISC, RailcraftItems.STEEL_SHOVEL.get())
        .pattern("a")
        .pattern("b")
        .pattern("b")
        .define('a', RailcraftTags.Items.STEEL_INGOT)
        .define('b', Tags.Items.RODS_WOODEN)
        .unlockedBy(getHasName(RailcraftItems.STEEL_INGOT.get()),
            has(RailcraftTags.Items.STEEL_INGOT))
        .save(output);
    shaped(RecipeCategory.MISC, RailcraftItems.STEEL_PICKAXE.get())
        .pattern("aaa")
        .pattern(" b ")
        .pattern(" b ")
        .define('a', RailcraftTags.Items.STEEL_INGOT)
        .define('b', Tags.Items.RODS_WOODEN)
        .unlockedBy(getHasName(RailcraftItems.STEEL_INGOT.get()),
            has(RailcraftTags.Items.STEEL_INGOT))
        .save(output);
    shaped(RecipeCategory.MISC, RailcraftItems.STEEL_AXE.get())
        .pattern("aa")
        .pattern("ab")
        .pattern(" b")
        .define('a', RailcraftTags.Items.STEEL_INGOT)
        .define('b', Tags.Items.RODS_WOODEN)
        .unlockedBy(getHasName(RailcraftItems.STEEL_INGOT.get()),
            has(RailcraftTags.Items.STEEL_INGOT))
        .save(output);
    shaped(RecipeCategory.MISC, RailcraftItems.STEEL_HOE.get())
        .pattern("aa")
        .pattern(" b")
        .pattern(" b")
        .define('a', RailcraftTags.Items.STEEL_INGOT)
        .define('b', Tags.Items.RODS_WOODEN)
        .unlockedBy(getHasName(RailcraftItems.STEEL_INGOT.get()),
            has(RailcraftTags.Items.STEEL_INGOT))
        .save(output);
    shaped(RecipeCategory.MISC, RailcraftItems.STEEL_BOOTS.get())
        .pattern("a a")
        .pattern("a a")
        .define('a', RailcraftTags.Items.STEEL_INGOT)
        .unlockedBy(getHasName(RailcraftItems.STEEL_INGOT.get()),
            has(RailcraftTags.Items.STEEL_INGOT))
        .save(output);
    shaped(RecipeCategory.MISC, RailcraftItems.STEEL_LEGGINGS.get())
        .pattern("aaa")
        .pattern("a a")
        .pattern("a a")
        .define('a', RailcraftTags.Items.STEEL_INGOT)
        .unlockedBy(getHasName(RailcraftItems.STEEL_INGOT.get()),
            has(RailcraftTags.Items.STEEL_INGOT))
        .save(output);
    shaped(RecipeCategory.MISC, RailcraftItems.STEEL_CHESTPLATE.get())
        .pattern("a a")
        .pattern("aaa")
        .pattern("aaa")
        .define('a', RailcraftTags.Items.STEEL_INGOT)
        .unlockedBy(getHasName(RailcraftItems.STEEL_INGOT.get()),
            has(RailcraftTags.Items.STEEL_INGOT))
        .save(output);
    shaped(RecipeCategory.MISC, RailcraftItems.STEEL_HELMET.get())
        .pattern("aaa")
        .pattern("a a")
        .define('a', RailcraftTags.Items.STEEL_INGOT)
        .unlockedBy(getHasName(RailcraftItems.STEEL_INGOT.get()),
            has(RailcraftTags.Items.STEEL_INGOT))
        .save(output);
  }

  private void buildTunnelBoreHead() {
    tunnelBoreHead(RailcraftItems.BRONZE_TUNNEL_BORE_HEAD.get(),
        RailcraftTags.Items.BRONZE_BLOCK);
    tunnelBoreHead(RailcraftItems.IRON_TUNNEL_BORE_HEAD.get(),
        Tags.Items.STORAGE_BLOCKS_IRON);
    tunnelBoreHead(RailcraftItems.STEEL_TUNNEL_BORE_HEAD.get(),
        RailcraftTags.Items.STEEL_BLOCK);
    tunnelBoreHead(RailcraftItems.DIAMOND_TUNNEL_BORE_HEAD.get(),
        Tags.Items.STORAGE_BLOCKS_DIAMOND);
  }

  private void tunnelBoreHead(Item result, TagKey<Item> center) {
    shaped(RecipeCategory.MISC, result)
        .pattern("aaa")
        .pattern("aba")
        .pattern("aaa")
        .define('a', RailcraftTags.Items.STEEL_INGOT)
        .define('b', center)
        .unlockedBy(getHasName(RailcraftItems.STEEL_INGOT.get()),
            has(RailcraftTags.Items.STEEL_INGOT))
        .save(output);
  }

  private void buildMaul() {
    shaped(RecipeCategory.MISC, RailcraftItems.IRON_SPIKE_MAUL.get())
        .pattern("aca")
        .pattern(" b ")
        .pattern(" b ")
        .define('a', Tags.Items.INGOTS_IRON)
        .define('b', Tags.Items.RODS_WOODEN)
        .define('c', Tags.Items.STORAGE_BLOCKS_IRON)
        .unlockedBy(getHasName(Items.IRON_BLOCK), has(Tags.Items.STORAGE_BLOCKS_IRON))
        .save(output);
    shaped(RecipeCategory.MISC, RailcraftItems.STEEL_SPIKE_MAUL.get())
        .pattern("aca")
        .pattern(" b ")
        .pattern(" b ")
        .define('a', RailcraftTags.Items.STEEL_INGOT)
        .define('b', Tags.Items.RODS_WOODEN)
        .define('c', RailcraftTags.Items.STEEL_BLOCK)
        .unlockedBy(getHasName(RailcraftItems.STEEL_BLOCK.get()),
            has(RailcraftTags.Items.STEEL_BLOCK))
        .save(output);
    shaped(RecipeCategory.MISC, RailcraftItems.DIAMOND_SPIKE_MAUL.get())
        .pattern("aca")
        .pattern(" b ")
        .pattern(" b ")
        .define('a', Tags.Items.GEMS_DIAMOND)
        .define('b', Tags.Items.RODS_WOODEN)
        .define('c', Tags.Items.STORAGE_BLOCKS_DIAMOND)
        .unlockedBy(getHasName(Items.DIAMOND_BLOCK), has(Tags.Items.STORAGE_BLOCKS_DIAMOND))
        .save(output);
  }

  private void buildOreSmelt() {
    List<ItemLike> leadSmeltables = List.of(
        RailcraftItems.LEAD_ORE.get(),
        RailcraftItems.DEEPSLATE_LEAD_ORE.get(),
        RailcraftItems.LEAD_RAW.get());
    oreSmelting(leadSmeltables, RecipeCategory.MISC, RailcraftItems.LEAD_INGOT.get(),
        1, 200, "lead_ingot");
    oreBlasting(leadSmeltables, RecipeCategory.MISC, RailcraftItems.LEAD_INGOT.get(),
        1, 100, "lead_ingot");

    List<ItemLike> nickelSmeltables =
        List.of(
            RailcraftItems.NICKEL_ORE.get(),
            RailcraftItems.DEEPSLATE_NICKEL_ORE.get(),
            RailcraftItems.NICKEL_RAW.get());
    oreSmelting(nickelSmeltables, RecipeCategory.MISC, RailcraftItems.NICKEL_INGOT.get(),
        1, 200, "nickel_ingot");
    oreBlasting(nickelSmeltables, RecipeCategory.MISC, RailcraftItems.NICKEL_INGOT.get(),
        1, 100, "nickel_ingot");

    List<ItemLike> silverSmeltables =
        List.of(
            RailcraftItems.SILVER_ORE.get(),
            RailcraftItems.DEEPSLATE_SILVER_ORE.get(),
            RailcraftItems.SILVER_RAW.get());
    oreSmelting(silverSmeltables, RecipeCategory.MISC, RailcraftItems.SILVER_INGOT.get(),
        1, 200, "silver_ingot");
    oreBlasting(silverSmeltables, RecipeCategory.MISC, RailcraftItems.SILVER_INGOT.get(),
        1, 100, "silver_ingot");

    List<ItemLike> tinSmeltables =
        List.of(
            RailcraftItems.TIN_ORE.get(),
            RailcraftItems.DEEPSLATE_TIN_ORE.get(),
            RailcraftItems.TIN_RAW.get());
    oreSmelting(tinSmeltables, RecipeCategory.MISC, RailcraftItems.TIN_INGOT.get(),
        1, 200, "tin_ingot");
    oreBlasting(tinSmeltables, RecipeCategory.MISC, RailcraftItems.TIN_INGOT.get(),
        1, 100, "tin_ingot");

    List<ItemLike> zincSmeltables =
        List.of(
            RailcraftItems.ZINC_ORE.get(),
            RailcraftItems.DEEPSLATE_ZINC_ORE.get(),
            RailcraftItems.ZINC_RAW.get());
    oreSmelting(zincSmeltables, RecipeCategory.MISC, RailcraftItems.ZINC_INGOT.get(),
        1, 200, "zinc_ingot");
    oreBlasting(zincSmeltables, RecipeCategory.MISC, RailcraftItems.ZINC_INGOT.get(),
        1, 100, "zinc_ingot");
  }

  private void buildTurbineParts() {
    shaped(RecipeCategory.MISC, RailcraftItems.TURBINE_DISK.get())
        .pattern("aaa")
        .pattern("aba")
        .pattern("aaa")
        .define('a', RailcraftItems.TURBINE_BLADE.get())
        .define('b', RailcraftTags.Items.STEEL_INGOT)
        .unlockedBy(getHasName(RailcraftItems.TURBINE_BLADE.get()),
            has(RailcraftItems.TURBINE_BLADE.get()))
        .save(output);

    shaped(RecipeCategory.MISC, RailcraftItems.TURBINE_ROTOR.get())
        .pattern("aaa")
        .define('a', RailcraftItems.TURBINE_DISK.get())
        .unlockedBy(getHasName(RailcraftItems.TURBINE_DISK.get()),
            has(RailcraftItems.TURBINE_DISK.get()))
        .save(output);

    shaped(RecipeCategory.MISC, RailcraftItems.STEAM_TURBINE.get())
        .pattern("aba")
        .pattern("bcb")
        .pattern("aba")
        .define('a', RailcraftTags.Items.STEEL_BLOCK)
        .define('b', RailcraftTags.Items.STEEL_PLATE)
        .define('c', RailcraftItems.CHARGE_MOTOR.get())
        .unlockedBy(getHasName(RailcraftItems.CHARGE_MOTOR.get()),
            has(RailcraftItems.CHARGE_MOTOR.get()))
        .save(output);
    RailcraftSpecialRecipeBuilder.special(RotorRepairRecipe::new)
        .save(output, "rotor_repair");
  }

  private void buildSignalBox() {
    signalBox(RailcraftItems.SIGNAL_CONTROLLER_BOX.get(),
        RailcraftItems.CONTROLLER_CIRCUIT.get(), Items.REDSTONE);
    signalBox(RailcraftItems.SIGNAL_RECEIVER_BOX.get(),
        RailcraftItems.RECEIVER_CIRCUIT.get(), Items.REDSTONE);
    signalBox(RailcraftItems.ANALOG_SIGNAL_CONTROLLER_BOX.get(),
        RailcraftItems.CONTROLLER_CIRCUIT.get(), Items.COMPARATOR);
    signalBox(RailcraftItems.SIGNAL_CAPACITOR_BOX.get(),
        Items.REPEATER, Items.REDSTONE);
    signalBox(RailcraftItems.SIGNAL_SEQUENCER_BOX.get(),
        Items.COMPARATOR, Items.REDSTONE);


    var circuit = RailcraftItems.CONTROLLER_CIRCUIT.get();
    shaped(RecipeCategory.MISC, RailcraftItems.SIGNAL_INTERLOCK_BOX.get())
        .pattern(" d ")
        .pattern("aba")
        .pattern("aca")
        .define('a', Items.IRON_INGOT)
        .define('b', circuit)
        .define('c', Items.REDSTONE)
        .define('d', RailcraftItems.RECEIVER_CIRCUIT.get())
        .unlockedBy(getHasName(circuit), has(circuit))
        .save(output);
    circuit = RailcraftItems.SIGNAL_CIRCUIT.get();
    shaped(RecipeCategory.MISC, RailcraftItems.SIGNAL_BLOCK_RELAY_BOX.get())
        .pattern(" c ")
        .pattern("aba")
        .pattern("aca")
        .define('a', Items.IRON_INGOT)
        .define('b', circuit)
        .define('c', Items.REDSTONE)
        .unlockedBy(getHasName(circuit), has(circuit))
        .save(output);
    circuit = RailcraftItems.RADIO_CIRCUIT.get();
    shaped(RecipeCategory.MISC, RailcraftItems.TOKEN_SIGNAL_BOX.get())
        .pattern(" c ")
        .pattern("aba")
        .pattern("aca")
        .define('a', Items.IRON_INGOT)
        .define('b', circuit)
        .define('c', Items.REDSTONE)
        .unlockedBy(getHasName(circuit), has(circuit))
        .save(output);
  }

  private void signalBox(Item result, Item circuit, Item bottomItem) {
    shaped(RecipeCategory.MISC, result)
        .pattern("aba")
        .pattern("aca")
        .define('a', Items.IRON_INGOT)
        .define('b', circuit)
        .define('c', bottomItem)
        .unlockedBy(getHasName(circuit), has(circuit))
        .save(output);
  }

  private void buildSignals() {
    singleSignal(RailcraftItems.BLOCK_SIGNAL.get(), RailcraftItems.SIGNAL_CIRCUIT.get());
    singleSignal(RailcraftItems.DISTANT_SIGNAL.get(), RailcraftItems.RECEIVER_CIRCUIT.get());
    singleSignal(RailcraftItems.TOKEN_SIGNAL.get(), RailcraftItems.RADIO_CIRCUIT.get());

    dualSignal(RailcraftItems.DUAL_BLOCK_SIGNAL.get(), RailcraftItems.SIGNAL_CIRCUIT.get());
    dualSignal(RailcraftItems.DUAL_DISTANT_SIGNAL.get(), RailcraftItems.RECEIVER_CIRCUIT.get());
    dualSignal(RailcraftItems.DUAL_TOKEN_SIGNAL.get(), RailcraftItems.RADIO_CIRCUIT.get());

    shaped(RecipeCategory.MISC, RailcraftItems.SIGNAL_LAMP.get())
        .pattern("ab ")
        .pattern("ace")
        .pattern("adf")
        .define('a', Items.GLASS_PANE)
        .define('b', Tags.Items.DYES_LIME)
        .define('c', Tags.Items.DYES_YELLOW)
        .define('d', Tags.Items.DYES_RED)
        .define('e', Items.GLOWSTONE_DUST)
        .define('f', Items.REDSTONE)
        .unlockedBy(getHasName(Items.GLOWSTONE_DUST), has(Items.GLOWSTONE_DUST))
        .save(output);
  }

  private void singleSignal(Item result, Item circuit) {
    shaped(RecipeCategory.MISC, result)
        .pattern("abc")
        .pattern(" dc")
        .define('a', RailcraftItems.SIGNAL_LAMP.get())
        .define('b', circuit)
        .define('c', Items.IRON_INGOT)
        .define('d', Tags.Items.DYES_BLACK)
        .unlockedBy(getHasName(circuit), has(circuit))
        .save(output);
  }

  private void dualSignal(Item result, Item circuit) {
    shaped(RecipeCategory.MISC, result)
        .pattern("abc")
        .pattern(" dc")
        .pattern("aec")
        .define('a', RailcraftItems.SIGNAL_LAMP.get())
        .define('b', circuit)
        .define('c', Items.IRON_INGOT)
        .define('d', Tags.Items.DYES_BLACK)
        .define('e', RailcraftItems.RECEIVER_CIRCUIT.get())
        .unlockedBy(getHasName(circuit), has(circuit))
        .save(output);
  }

  private void buildCircuit() {
    circuitFromMaterial(RailcraftItems.CONTROLLER_CIRCUIT.get(), Items.RED_WOOL);
    circuitFromMaterial(RailcraftItems.RECEIVER_CIRCUIT.get(), Items.GREEN_WOOL);
    circuitFromMaterial(RailcraftItems.SIGNAL_CIRCUIT.get(), Items.YELLOW_WOOL);
    circuitFromMaterial(RailcraftItems.RADIO_CIRCUIT.get(), Items.BLUE_WOOL);
  }

  private void circuitFromMaterial(Item itemOut, Item woolItem) {
    shaped(RecipeCategory.MISC, itemOut)
        .define('W', woolItem)
        .define('R', Items.REPEATER)
        .define('S', Tags.Items.DUSTS_REDSTONE)
        .define('G', Tags.Items.INGOTS_GOLD)
        .define('L', Tags.Items.GEMS_LAPIS)
        .define('B', Tags.Items.SLIME_BALLS)
        .pattern(" RW")
        .pattern("BGS")
        .pattern("WSL")
        .unlockedBy(getHasName(Items.REDSTONE), has(Tags.Items.DUSTS_REDSTONE))
        .save(output);
  }

  private void buildSwitch() {
    switchItem(RailcraftItems.SWITCH_TRACK_LEVER.get(), Items.LEVER);
    switchItem(RailcraftItems.SWITCH_TRACK_MOTOR.get(), RailcraftItems.RECEIVER_CIRCUIT.get());
    shapeless(RecipeCategory.MISC, RailcraftItems.SWITCH_TRACK_ROUTER.get())
        .requires(RailcraftItems.SWITCH_TRACK_MOTOR.get())
        .requires(RailcraftItems.ROUTING_DETECTOR.get())
        .unlockedBy(getHasName(RailcraftItems.SWITCH_TRACK_MOTOR.get()),
            has(RailcraftItems.SWITCH_TRACK_MOTOR.get()))
        .save(output);
  }

  private void switchItem(Item result, Item circuit) {
    shaped(RecipeCategory.MISC, result)
        .pattern("abc")
        .pattern("def")
        .define('a', Tags.Items.DYES_RED)
        .define('b', Tags.Items.DYES_BLACK)
        .define('c', Items.BONE_MEAL)
        .define('d', Items.PISTON)
        .define('e', circuit)
        .define('f', Items.IRON_INGOT)
        .unlockedBy(getHasName(circuit), has(circuit))
        .save(output);
  }

  private void buildLoaders() {
    shaped(RecipeCategory.MISC, RailcraftItems.ITEM_LOADER.get())
        .pattern("aaa")
        .pattern("aba")
        .pattern("aca")
        .define('a', Items.COBBLESTONE)
        .define('b', Items.HOPPER)
        .define('c', RailcraftItems.ITEM_DETECTOR.get())
        .unlockedBy(getHasName(RailcraftItems.ITEM_DETECTOR.get()),
            has(RailcraftItems.ITEM_DETECTOR.get()))
        .save(output);
    shaped(RecipeCategory.MISC, RailcraftItems.ADVANCED_ITEM_LOADER.get())
        .pattern("aba")
        .pattern("bcb")
        .pattern("ada")
        .define('a', RailcraftTags.Items.STEEL_INGOT)
        .define('b', Items.REDSTONE)
        .define('c', RailcraftItems.ITEM_LOADER.get())
        .define('d', RailcraftItems.STEEL_SHOVEL.get())
        .unlockedBy(getHasName(RailcraftItems.ITEM_LOADER.get()),
            has(RailcraftItems.ITEM_LOADER.get()))
        .save(output);
    shaped(RecipeCategory.MISC, RailcraftItems.ITEM_UNLOADER.get())
        .pattern("aaa")
        .pattern("aba")
        .pattern("aca")
        .define('a', Items.COBBLESTONE)
        .define('b', RailcraftItems.ITEM_DETECTOR.get())
        .define('c', Items.HOPPER)
        .unlockedBy(getHasName(RailcraftItems.ITEM_DETECTOR.get()),
            has(RailcraftItems.ITEM_DETECTOR.get()))
        .save(output);
    shaped(RecipeCategory.MISC, RailcraftItems.ADVANCED_ITEM_UNLOADER.get())
        .pattern("aba")
        .pattern("bcb")
        .pattern("ada")
        .define('a', RailcraftTags.Items.STEEL_INGOT)
        .define('b', Items.REDSTONE)
        .define('c', RailcraftItems.ITEM_UNLOADER.get())
        .define('d', RailcraftItems.STEEL_SHOVEL.get())
        .unlockedBy(getHasName(RailcraftItems.ITEM_UNLOADER.get()),
            has(RailcraftItems.ITEM_UNLOADER.get()))
        .save(output);
    shaped(RecipeCategory.MISC, RailcraftItems.FLUID_LOADER.get())
        .pattern("aba")
        .pattern("a a")
        .pattern("aca")
        .define('a', Items.GLASS)
        .define('b', Items.HOPPER)
        .define('c', RailcraftItems.DETECTOR_TRACK_KIT.get())
        .unlockedBy(getHasName(RailcraftItems.DETECTOR_TRACK_KIT.get()),
            has(RailcraftItems.DETECTOR_TRACK_KIT.get()))
        .save(output);
    shaped(RecipeCategory.MISC, RailcraftItems.FLUID_UNLOADER.get())
        .pattern("aba")
        .pattern("a a")
        .pattern("aca")
        .define('a', Items.GLASS)
        .define('b', RailcraftItems.DETECTOR_TRACK_KIT.get())
        .define('c', Items.HOPPER)
        .unlockedBy(getHasName(RailcraftItems.DETECTOR_TRACK_KIT.get()),
            has(RailcraftItems.DETECTOR_TRACK_KIT.get()))
        .save(output);
    shapeless(RecipeCategory.MISC, RailcraftItems.CART_DISPENSER.get())
        .requires(Items.DISPENSER)
        .requires(Items.MINECART)
        .unlockedBy(getHasName(Items.DISPENSER),
            has(Items.DISPENSER))
        .unlockedBy(getHasName(Items.MINECART),
            has(Items.MINECART))
        .save(output);
    shaped(RecipeCategory.MISC, RailcraftItems.TRAIN_DISPENSER.get())
        .pattern("aba")
        .pattern("bcb")
        .pattern("aba")
        .define('a', Items.REDSTONE)
        .define('b', RailcraftTags.Items.CROWBAR)
        .define('c', RailcraftItems.CART_DISPENSER.get())
        .unlockedBy(getHasName(RailcraftItems.CART_DISPENSER.get()),
            has(RailcraftItems.CART_DISPENSER.get()))
        .save(output);
  }

  private void buildCrowbars() {
    crowbar(RailcraftItems.IRON_CROWBAR.get(),
        Tags.Items.INGOTS_IRON);
    crowbar(RailcraftItems.STEEL_CROWBAR.get(),
        RailcraftTags.Items.STEEL_INGOT);
    crowbar(RailcraftItems.DIAMOND_CROWBAR.get(),
        Tags.Items.GEMS_DIAMOND);
  }

  private void crowbar(Item itemOut, TagKey<Item> materialTag) {
    shaped(RecipeCategory.MISC, itemOut)
        .pattern(" ba")
        .pattern("bab")
        .pattern("ab ")
        .define('a', materialTag)
        .define('b', Tags.Items.DYES_RED)
        .unlockedBy(getHasName(Items.RAIL), has(Items.RAIL))
        .save(output);
  }

  private void buildFirestones() {
    shaped(RecipeCategory.MISC, RailcraftItems.REFINED_FIRESTONE.get())
        .pattern("LRL")
        .pattern("RSR")
        .pattern("LRL")
        .define('L', Items.LAVA_BUCKET)
        .define('R', Items.REDSTONE_BLOCK)
        .define('S', RailcraftItems.CUT_FIRESTONE.get())
        .unlockedBy(getHasName(RailcraftItems.CUT_FIRESTONE.get()),
            has(RailcraftItems.CUT_FIRESTONE.get()))
        .save(output, RailcraftConstants.id("firestone_lava_refinement").toString());

    shaped(RecipeCategory.MISC, RailcraftItems.REFINED_FIRESTONE.get())
        .pattern("LFL")
        .pattern("RSR")
        .pattern("LRL")
        .define('L', Items.LAVA_BUCKET)
        .define('R', Items.REDSTONE_BLOCK)
        .define('S', RailcraftItems.CRACKED_FIRESTONE.get())
        .define('F', RailcraftItems.RAW_FIRESTONE.get())
        .unlockedBy(getHasName(RailcraftItems.CRACKED_FIRESTONE.get()),
            has(RailcraftItems.CRACKED_FIRESTONE.get()))
        .save(output, RailcraftConstants.id("firestone_cracked_fixing").toString());

    shaped(RecipeCategory.MISC, RailcraftItems.CUT_FIRESTONE.get())
        .pattern(" a ")
        .pattern("aba")
        .pattern(" a ")
        .define('a', Items.NETHERITE_PICKAXE)
        .define('b', RailcraftItems.RAW_FIRESTONE.get())
        .unlockedBy(getHasName(RailcraftItems.RAW_FIRESTONE.get()),
            has(RailcraftItems.RAW_FIRESTONE.get()))
        .save(output);
  }

  private void buildMiscItems() {
    shaped(RecipeCategory.MISC, RailcraftItems.FEED_STATION.get())
        .pattern("aba")
        .pattern("bcb")
        .pattern("aba")
        .define('a', ItemTags.PLANKS)
        .define('b', Items.GOLDEN_CARROT)
        .define('c', RailcraftTags.Items.STEEL_PLATE)
        .unlockedBy(getHasName(RailcraftItems.STEEL_PLATE.get()),
            has(RailcraftTags.Items.STEEL_PLATE))
        .save(output);
    shaped(RecipeCategory.MISC, RailcraftItems.CHIMNEY.get())
        .pattern(" a ")
        .pattern("bcb")
        .define('a', Items.NETHERRACK)
        .define('b', Tags.Items.DUSTS_REDSTONE)
        .define('c', Items.CAULDRON)
        .unlockedBy(getHasName(Items.REDSTONE), has(Items.REDSTONE))
        .save(output);
    shaped(RecipeCategory.MISC, RailcraftItems.LOGBOOK.get())
        .pattern(" a ")
        .pattern("bcb")
        .pattern("ddd")
        .define('a', Items.WRITABLE_BOOK)
        .define('b', Items.GOLD_INGOT)
        .define('c', Items.RED_WOOL)
        .define('d', ItemTags.PLANKS)
        .unlockedBy(getHasName(Items.WRITABLE_BOOK), has(Items.WRITABLE_BOOK))
        .save(output);
    shaped(RecipeCategory.MISC, RailcraftItems.MANUAL_ROLLING_MACHINE.get())
        .pattern("aba")
        .pattern("bcb")
        .pattern("aba")
        .define('a', RailcraftTags.Items.BRONZE_GEAR)
        .define('b', Items.PISTON)
        .define('c', Items.CRAFTING_TABLE)
        .unlockedBy(getHasName(Items.PISTON), has(Items.PISTON))
        .save(output);
    shaped(RecipeCategory.MISC, RailcraftItems.POWERED_ROLLING_MACHINE.get())
        .pattern("aba")
        .pattern("bcb")
        .pattern("ada")
        .define('a', RailcraftTags.Items.STEEL_GEAR)
        .define('b', Items.PISTON)
        .define('c', Items.CRAFTING_TABLE)
        .define('d', RailcraftItems.CHARGE_MOTOR.get())
        .unlockedBy(getHasName(RailcraftItems.MANUAL_ROLLING_MACHINE.get()),
            has(RailcraftItems.MANUAL_ROLLING_MACHINE.get()))
        .save(output);
    shapeless(RecipeCategory.MISC, RailcraftItems.GOLDEN_TICKET.get())
        .requires(Items.PAPER)
        .requires(Tags.Items.NUGGETS_GOLD)
        .unlockedBy(getHasName(Items.PAPER), has(Items.PAPER))
        .save(output);
    RailcraftSpecialRecipeBuilder.special(TicketDuplicateRecipe::new)
        .save(output, getItemName(RailcraftItems.TICKET.get()));
    shapeless(RecipeCategory.MISC, RailcraftItems.ROUTING_TABLE_BOOK.get())
        .requires(Items.WRITABLE_BOOK)
        .requires(Tags.Items.DYES_BLUE)
        .unlockedBy(getHasName(Items.WRITABLE_BOOK), has(Items.WRITABLE_BOOK))
        .save(output);
    shaped(RecipeCategory.MISC, RailcraftItems.OVERALLS.get())
        .pattern("aaa")
        .pattern("a a")
        .pattern("a a")
        .define('a', Items.CYAN_WOOL)
        .unlockedBy(getHasName(Items.CYAN_WOOL), has(Items.CYAN_WOOL))
        .save(output);
    shaped(RecipeCategory.MISC, RailcraftItems.WHISTLE_TUNER.get())
        .pattern("a a")
        .pattern("aaa")
        .pattern(" a ")
        .define('a', RailcraftTags.Items.STEEL_NUGGET)
        .unlockedBy(getHasName(RailcraftItems.STEEL_NUGGET.get()),
            has(RailcraftTags.Items.STEEL_NUGGET))
        .save(output);
    shapeless(RecipeCategory.MISC, RailcraftItems.SIGNAL_LABEL.get())
        .requires(Items.PAPER)
        .requires(RailcraftTags.Items.STEEL_NUGGET)
        .unlockedBy(getHasName(RailcraftItems.STEEL_NUGGET.get()),
            has(RailcraftItems.STEEL_NUGGET.get()))
        .save(output);
    shaped(RecipeCategory.MISC, RailcraftItems.FORCE_TRACK_EMITTER.get())
        .pattern("aba")
        .pattern("cdc")
        .pattern("aba")
        .define('a', RailcraftTags.Items.TIN_PLATE)
        .define('b', RailcraftTags.Items.ENDER_DUST)
        .define('c', RailcraftItems.CHARGE_COIL.get())
        .define('d', Tags.Items.STORAGE_BLOCKS_DIAMOND)
        .unlockedBy(getHasName(RailcraftItems.ENDER_DUST.get()),
            has(RailcraftItems.ENDER_DUST.get()))
        .save(output);
    shaped(RecipeCategory.MISC, RailcraftItems.SIGNAL_BLOCK_SURVEYOR.get())
        .pattern(" a ")
        .pattern("cbc")
        .pattern(" d ")
        .define('a', Items.COMPASS)
        .define('b', Tags.Items.GLASS_PANES)
        .define('c', Blocks.STONE_BUTTON)
        .define('d', Tags.Items.DUSTS_REDSTONE)
        .unlockedBy(getHasName(Items.REDSTONE), has(Tags.Items.DUSTS_REDSTONE))
        .save(output);
    shaped(RecipeCategory.MISC, RailcraftItems.SIGNAL_TUNER.get())
        .define('a', Items.REDSTONE_TORCH)
        .define('b', RailcraftItems.RECEIVER_CIRCUIT.get())
        .define('c', Blocks.STONE_BUTTON)
        .pattern(" a ")
        .pattern("cbc")
        .unlockedBy(getHasName(Items.REDSTONE), has(Tags.Items.DUSTS_REDSTONE))
        .unlockedBy(getHasName(RailcraftItems.RECEIVER_CIRCUIT.get()),
            has(RailcraftItems.RECEIVER_CIRCUIT.get()))
        .save(output);
    shaped(RecipeCategory.MISC, RailcraftItems.GOGGLES.get())
        .pattern("aba")
        .pattern("c c")
        .pattern("ddd")
        .define('a', Tags.Items.GLASS_PANES)
        .define('b', RailcraftItems.RECEIVER_CIRCUIT.get())
        .define('c', RailcraftTags.Items.STEEL_INGOT)
        .define('d', Tags.Items.LEATHERS)
        .unlockedBy(getHasName(RailcraftItems.STEEL_INGOT.get()),
            has(RailcraftTags.Items.STEEL_INGOT))
        .unlockedBy(getHasName(RailcraftItems.RECEIVER_CIRCUIT.get()),
            has(RailcraftItems.RECEIVER_CIRCUIT.get()))
        .save(output);
    shaped(RecipeCategory.MISC, RailcraftItems.WATER_TANK_SIDING.get(), 6)
        .pattern("aaa")
        .pattern("bcb")
        .pattern("aaa")
        .define('a', ItemTags.PLANKS)
        .define('b', Tags.Items.INGOTS_IRON)
        .define('c', Items.SLIME_BALL)
        .unlockedBy(getHasName(Items.IRON_INGOT),
            has(Tags.Items.INGOTS_IRON))
        .unlockedBy(getHasName(Items.SLIME_BALL),
            has(Items.SLIME_BALL))
        .save(output);
    shapeless(RecipeCategory.MISC, Items.GUNPOWDER, 2)
        .requires(RailcraftTags.Items.SALTPETER_DUST)
        .requires(RailcraftTags.Items.SALTPETER_DUST)
        .requires(RailcraftTags.Items.SULFUR_DUST)
        .requires(RailcraftTags.Items.CHARCOAL_DUST)
        .unlockedBy(getHasName(RailcraftItems.SALTPETER_DUST.get()),
            has(RailcraftTags.Items.SALTPETER_DUST))
        .unlockedBy(getHasName(RailcraftItems.SULFUR_DUST.get()),
            has(RailcraftTags.Items.SULFUR_DUST))
        .save(output);

    shapeless(RecipeCategory.MISC, RailcraftItems.VOID_DUST, 3)
        .requires(RailcraftTags.Items.COAL_DUST)
        .requires(RailcraftTags.Items.ENDER_DUST)
        .requires(RailcraftTags.Items.OBSIDIAN_DUST)
        .unlockedBy(getHasName(RailcraftItems.COAL_DUST.get()),
            has(RailcraftTags.Items.COAL_DUST))
        .unlockedBy(getHasName(RailcraftItems.ENDER_DUST.get()),
            has(RailcraftTags.Items.ENDER_DUST))
        .unlockedBy(getHasName(RailcraftItems.OBSIDIAN_DUST.get()),
            has(RailcraftTags.Items.OBSIDIAN_DUST))
        .save(output);
    shaped(RecipeCategory.MISC, RailcraftItems.VOID_CHEST)
        .pattern("aaa")
        .pattern("aba")
        .pattern("aaa")
        .define('a', Items.OBSIDIAN)
        .define('b', RailcraftItems.VOID_DUST)
        .unlockedBy(getHasName(Items.OBSIDIAN), has(Items.OBSIDIAN))
        .unlockedBy(getHasName(RailcraftItems.VOID_DUST), has(RailcraftItems.VOID_DUST))
        .save(output);

    //TODO: Restore
    /*RailcraftSpecialRecipeBuilder.special(PatchouliBookCrafting::new)
        .save(output.withConditions(modLoaded(PatchouliAPI.MOD_ID)), "patchouli_book_crafting");*/
  }

  private void buildCartsVariant() {
    shaped(RecipeCategory.MISC, RailcraftItems.CARGO_MINECART.get())
        .pattern("a")
        .pattern("b")
        .define('a', Items.BARREL)
        .define('b', Items.MINECART)
        .unlockedBy(getHasName(Items.BARREL), has(Items.BARREL))
        .save(output);

    shaped(RecipeCategory.MISC, RailcraftItems.TANK_MINECART.get())
        .pattern("a")
        .pattern("b")
        .define('a', RailcraftTags.Items.STRENGTHENED_GLASS)
        .define('b', Items.MINECART)
        .unlockedBy(getHasName(RailcraftItems.STRENGTHENED_GLASS.variantFor(DyeColor.WHITE).get()),
            has(RailcraftItems.STRENGTHENED_GLASS.variantFor(DyeColor.WHITE).get()))
        .save(output);

    shaped(RecipeCategory.MISC, RailcraftItems.ENERGY_MINECART)
        .pattern("aba")
        .pattern("bcb")
        .pattern("aba")
        .define('a', RailcraftTags.Items.LEAD_INGOT)
        .define('b', Items.REDSTONE_BLOCK)
        .define('c', Items.MINECART)
        .unlockedBy(getHasName(RailcraftItems.LEAD_INGOT.get()),
            has(RailcraftItems.LEAD_INGOT.get()))
        .save(output);

    shaped(RecipeCategory.MISC, RailcraftItems.VOID_CHEST_MINECART)
        .pattern("a")
        .pattern("b")
        .define('a', RailcraftItems.VOID_CHEST)
        .define('b', Items.MINECART)
        .unlockedBy(getHasName(RailcraftItems.VOID_CHEST), has(RailcraftItems.VOID_CHEST))
        .save(output);

    shaped(RecipeCategory.MISC, RailcraftItems.WORLD_SPIKE_MINECART)
        .pattern("a")
        .pattern("b")
        .define('a', RailcraftItems.WORLD_SPIKE)
        .define('b', Items.MINECART)
        .unlockedBy(getHasName(RailcraftItems.WORLD_SPIKE),
            has(RailcraftItems.WORLD_SPIKE))
        .save(output);

    shaped(RecipeCategory.MISC, RailcraftItems.TUNNEL_BORE.get())
        .pattern("aba")
        .pattern("cbc")
        .pattern(" d ")
        .define('a', RailcraftTags.Items.STEEL_BLOCK)
        .define('b', Items.MINECART)
        .define('c', Items.FURNACE)
        .define('d', Items.CHEST_MINECART)
        .unlockedBy(getHasName(Items.MINECART), has(Items.MINECART))
        .save(output);

    shaped(RecipeCategory.MISC, RailcraftItems.STEAM_LOCOMOTIVE.get())
        .pattern("aab")
        .pattern("aab")
        .pattern("cdd")
        .define('a', RailcraftTags.Items.IRON_TANK_WALL)
        .define('b', RailcraftItems.BLAST_FURNACE_BRICKS.get())
        .define('c', Items.IRON_BARS)
        .define('d', Items.MINECART)
        .unlockedBy(getHasName(Items.MINECART), has(Items.MINECART))
        .save(output);
    shaped(RecipeCategory.MISC, RailcraftItems.ELECTRIC_LOCOMOTIVE.get())
        .pattern("ab ")
        .pattern("cdc")
        .pattern("efe")
        .define('a', Items.REDSTONE_LAMP)
        .define('b', RailcraftTags.Items.STEEL_PLATE)
        .define('c', RailcraftItems.CHARGE_MOTOR.get())
        .define('d', Ingredient.of(RailcraftItems.NICKEL_IRON_BATTERY.get(),
            RailcraftItems.NICKEL_ZINC_BATTERY.get()))
        .define('e', RailcraftTags.Items.STEEL_GEAR)
        .define('f', Items.MINECART)
        .unlockedBy(getHasName(Items.MINECART), has(Items.MINECART))
        .save(output);

    RailcraftSpecialRecipeBuilder.special(LocomotivePaintingRecipe::new)
        .save(output, "locomotive_color_variant");
    RailcraftSpecialRecipeBuilder.special(ChestMinecartDisassemblyRecipe::new)
        .save(output, "chest_minecart_disassembly");
    RailcraftSpecialRecipeBuilder.special(WorldSpikeMinecartDisassemblyRecipe::new)
        .save(output, "worldspike_minecart_disassembly");
    RailcraftSpecialRecipeBuilder.special(VoidChestMinecartDisassemblyRecipe::new)
        .save(output, "void_chest_minecart_disassembly");

    shaped(RecipeCategory.MISC, RailcraftItems.TRACK_LAYER.get())
        .pattern("aba")
        .pattern("cdc")
        .pattern("efe")
        .define('a', Tags.Items.DYES_YELLOW)
        .define('b', Items.REDSTONE_LAMP)
        .define('c', Items.ANVIL)
        .define('d', RailcraftTags.Items.STEEL_BLOCK)
        .define('e', Items.DISPENSER)
        .define('f', Items.MINECART)
        .unlockedBy(getHasName(Items.MINECART), has(Items.MINECART))
        .save(output);

    shaped(RecipeCategory.MISC, RailcraftItems.TRACK_RELAYER.get())
        .pattern("aba")
        .pattern("cdc")
        .pattern("efe")
        .define('a', Tags.Items.DYES_YELLOW)
        .define('b', Items.REDSTONE_LAMP)
        .define('c', Items.BLAZE_ROD)
        .define('d', RailcraftTags.Items.STEEL_BLOCK)
        .define('e', Items.DIAMOND_PICKAXE)
        .define('f', Items.MINECART)
        .unlockedBy(getHasName(Items.MINECART), has(Items.MINECART))
        .save(output);

    shaped(RecipeCategory.MISC, RailcraftItems.TRACK_REMOVER.get())
        .pattern("aba")
        .pattern("cdc")
        .pattern("efe")
        .define('a', Tags.Items.DYES_YELLOW)
        .define('b', Items.REDSTONE_LAMP)
        .define('c', Items.STICKY_PISTON)
        .define('d', RailcraftTags.Items.STEEL_BLOCK)
        .define('e', RailcraftTags.Items.CROWBAR)
        .define('f', Items.MINECART)
        .unlockedBy(getHasName(Items.MINECART), has(Items.MINECART))
        .save(output);
    shaped(RecipeCategory.MISC, RailcraftItems.TRACK_UNDERCUTTER.get())
        .pattern("aba")
        .pattern("cdc")
        .pattern("efe")
        .define('a', Tags.Items.DYES_YELLOW)
        .define('b', Items.REDSTONE_LAMP)
        .define('c', Items.PISTON)
        .define('d', RailcraftTags.Items.STEEL_BLOCK)
        .define('e', Items.DIAMOND_SHOVEL)
        .define('f', Items.MINECART)
        .unlockedBy(getHasName(Items.MINECART), has(Items.MINECART))
        .save(output);
  }

  private void buildChargeItems() {
    this.conversion(RailcraftItems.CHARGE_SPOOL_MEDIUM.get(),
        RailcraftItems.CHARGE_SPOOL_SMALL.get(), 3, "charge_spool_small_from_medium");
    this.conversion(RailcraftItems.CHARGE_SPOOL_LARGE.get(),
        RailcraftItems.CHARGE_SPOOL_MEDIUM.get(), 3, "charge_spool_medium_from_large");

    shaped(RecipeCategory.MISC, RailcraftItems.CHARGE_TERMINAL.get())
        .pattern(" b ")
        .pattern("aaa")
        .define('a', RailcraftTags.Items.BRASS_INGOT)
        .define('b', RailcraftTags.Items.BRASS_PLATE)
        .unlockedBy(getHasName(RailcraftItems.BRASS_INGOT.get()),
            has(RailcraftTags.Items.BRASS_INGOT))
        .save(output);

    shaped(RecipeCategory.MISC, RailcraftItems.CHARGE_COIL.get())
        .pattern("aaa")
        .pattern("bbb")
        .pattern("aaa")
        .define('a', RailcraftItems.CHARGE_SPOOL_SMALL.get())
        .define('b', RailcraftTags.Items.IRON_PLATE)
        .unlockedBy(getHasName(RailcraftItems.CHARGE_SPOOL_SMALL.get()),
            has(RailcraftItems.CHARGE_SPOOL_SMALL.get()))
        .save(output);

    shaped(RecipeCategory.MISC, RailcraftItems.CHARGE_MOTOR.get())
        .pattern(" a ")
        .pattern("bcb")
        .pattern(" d ")
        .define('a', RailcraftTags.Items.STEEL_INGOT)
        .define('b', RailcraftTags.Items.TIN_PLATE)
        .define('c', RailcraftItems.CHARGE_COIL.get())
        .define('d', RailcraftItems.CHARGE_TERMINAL.get())
        .unlockedBy(getHasName(RailcraftItems.CHARGE_SPOOL_SMALL.get()),
            has(RailcraftItems.CHARGE_SPOOL_SMALL.get()))
        .save(output);

    shaped(RecipeCategory.MISC, RailcraftItems.CHARGE_METER.get())
        .pattern("a a")
        .pattern("bcb")
        .pattern(" d ")
        .define('a', Tags.Items.INGOTS_COPPER)
        .define('b', Items.STONE_BUTTON)
        .define('c', Items.GLASS_PANE)
        .define('d', RailcraftTags.Items.BRASS_INGOT)
        .unlockedBy(getHasName(RailcraftItems.BRASS_INGOT.get()),
            has(RailcraftItems.BRASS_INGOT.get()))
        .save(output);
  }

  private void buildKits() {
    kits(RailcraftItems.ACTIVATOR_TRACK_KIT.get(), 8, List.of(
        new Tuple<>(Ingredient.of(this.items.getOrThrow(Tags.Items.DUSTS_REDSTONE)), 2)));
    kits(RailcraftItems.BOOSTER_TRACK_KIT.get(), 16, List.of(
        new Tuple<>(Ingredient.of(RailcraftItems.ADVANCED_RAIL.get()), 2),
        new Tuple<>(Ingredient.of(this.items.getOrThrow(Tags.Items.DUSTS_REDSTONE)), 1)));
    kits(RailcraftItems.BUFFER_STOP_TRACK_KIT.get(), 2, List.of(
        new Tuple<>(Ingredient.of(this.items.getOrThrow(Tags.Items.INGOTS_IRON)), 2)));
    kits(RailcraftItems.CONTROL_TRACK_KIT.get(), 16, List.of(
        new Tuple<>(Ingredient.of(RailcraftItems.ADVANCED_RAIL.get()), 1),
        new Tuple<>(Ingredient.of(this.items.getOrThrow(Tags.Items.DUSTS_REDSTONE)), 1)));
    kits(RailcraftItems.DETECTOR_TRACK_KIT.get(), 8, List.of(
        new Tuple<>(Ingredient.of(Items.STONE_PRESSURE_PLATE), 1),
        new Tuple<>(Ingredient.of(this.items.getOrThrow(Tags.Items.DUSTS_REDSTONE)), 1)));
    kits(RailcraftItems.DISEMBARKING_TRACK_KIT.get(), 4, List.of(
        new Tuple<>(Ingredient.of(Items.STONE_PRESSURE_PLATE), 1),
        new Tuple<>(Ingredient.of(Items.LEAD), 1),
        new Tuple<>(Ingredient.of(this.items.getOrThrow(Tags.Items.DUSTS_REDSTONE)), 1)));
    kits(RailcraftItems.EMBARKING_TRACK_KIT.get(), 4, List.of(
        new Tuple<>(Ingredient.of(Items.ENDER_PEARL), 1),
        new Tuple<>(Ingredient.of(Items.LEAD), 1),
        new Tuple<>(Ingredient.of(this.items.getOrThrow(Tags.Items.DUSTS_REDSTONE)), 1)));
    kits(RailcraftItems.DUMPING_TRACK_KIT.get(), 4, List.of(
        new Tuple<>(Ingredient.of(this.items.getOrThrow(RailcraftTags.Items.STEEL_PLATE)), 1),
        new Tuple<>(Ingredient.of(this.items.getOrThrow(Tags.Items.DUSTS_REDSTONE)), 1)));
    kits(RailcraftItems.GATED_TRACK_KIT.get(), 4, List.of(
        new Tuple<>(Ingredient.of(this.items.getOrThrow(Tags.Items.FENCE_GATES)), 1),
        new Tuple<>(Ingredient.of(RailcraftItems.ADVANCED_RAIL.get()), 1),
        new Tuple<>(Ingredient.of(this.items.getOrThrow(Tags.Items.DUSTS_REDSTONE)), 1)));
    kits(RailcraftItems.LOCKING_TRACK_KIT.get(), 4, List.of(
        new Tuple<>(Ingredient.of(Items.STONE_PRESSURE_PLATE), 1),
        new Tuple<>(Ingredient.of(Items.STICKY_PISTON), 1),
        new Tuple<>(Ingredient.of(this.items.getOrThrow(Tags.Items.DUSTS_REDSTONE)), 1)));
    kits(RailcraftItems.ONE_WAY_TRACK_KIT.get(), 8, List.of(
        new Tuple<>(Ingredient.of(Items.STONE_PRESSURE_PLATE), 1),
        new Tuple<>(Ingredient.of(Items.PISTON), 1),
        new Tuple<>(Ingredient.of(this.items.getOrThrow(Tags.Items.DUSTS_REDSTONE)), 1)));
    kits(RailcraftItems.LAUNCHER_TRACK_KIT.get(), 1, List.of(
        new Tuple<>(Ingredient.of(Items.PISTON), 1),
        new Tuple<>(Ingredient.of(this.items.getOrThrow(RailcraftTags.Items.STEEL_BLOCK)), 2),
        new Tuple<>(Ingredient.of(this.items.getOrThrow(Tags.Items.DUSTS_REDSTONE)), 1)));
    kits(RailcraftItems.LOCOMOTIVE_TRACK_KIT.get(), 4, List.of(
        new Tuple<>(Ingredient.of(RailcraftItems.SIGNAL_LAMP.get()), 1),
        new Tuple<>(Ingredient.of(this.items.getOrThrow(Tags.Items.DUSTS_REDSTONE)), 1)));
    kits(RailcraftItems.TRANSITION_TRACK_KIT.get(), 8, List.of(
        new Tuple<>(Ingredient.of(RailcraftItems.ADVANCED_RAIL.get()), 2),
        new Tuple<>(Ingredient.of(this.items.getOrThrow(Tags.Items.DUSTS_REDSTONE)), 2)));
    kits(RailcraftItems.COUPLER_TRACK_KIT.get(), 4, List.of(
        new Tuple<>(Ingredient.of(Items.LEAD), 1),
        new Tuple<>(Ingredient.of(this.items.getOrThrow(Tags.Items.DUSTS_REDSTONE)), 1)));
    kits(RailcraftItems.ROUTING_TRACK_KIT.get(), 8, List.of(
        new Tuple<>(Ingredient.of(
            RailcraftItems.TICKET.get(), RailcraftItems.GOLDEN_TICKET.get()), 1),
        new Tuple<>(Ingredient.of(this.items.getOrThrow(Tags.Items.DUSTS_REDSTONE)), 1)));
    kits(RailcraftItems.THROTTLE_TRACK_KIT.get(), 4, List.of(
        new Tuple<>(Ingredient.of(this.items.getOrThrow(Tags.Items.DYES_YELLOW)), 1),
        new Tuple<>(Ingredient.of(this.items.getOrThrow(Tags.Items.DYES_BLACK)), 1),
        new Tuple<>(Ingredient.of(this.items.getOrThrow(Tags.Items.DUSTS_REDSTONE)), 1)));
    kits(RailcraftItems.WHISTLE_TRACK_KIT.get(), 8, List.of(
        new Tuple<>(Ingredient.of(this.items.getOrThrow(Tags.Items.DYES_YELLOW)), 1),
        new Tuple<>(Ingredient.of(this.items.getOrThrow(Tags.Items.DYES_BLACK)), 1),
        new Tuple<>(Ingredient.of(Items.NOTE_BLOCK), 1),
        new Tuple<>(Ingredient.of(this.items.getOrThrow(Tags.Items.DUSTS_REDSTONE)), 1)));
  }

  private void kits(Item result, int count, List<Tuple<Ingredient, Integer>> ingredients) {
    var builder = shapeless(RecipeCategory.MISC, result, count)
        .requires(ItemTags.PLANKS)
        .requires(RailcraftItems.TRACK_PARTS.get());

    for (var ingredient : ingredients) {
      builder = builder.requires(ingredient.getA(), ingredient.getB());
    }
    builder
        .unlockedBy(getHasName(RailcraftItems.TRACK_PARTS.get()),
            has(RailcraftItems.TRACK_PARTS.get()))
        .save(output);
  }

  private void buildGears() {
    square2x2(RailcraftTags.Items.BRONZE_INGOT,
        RailcraftItems.BUSHING_GEAR.get(), 1, "_bronze");
    square2x2(RailcraftTags.Items.BRASS_INGOT,
        RailcraftItems.BUSHING_GEAR.get(), 1, "_brass");

    gear(RailcraftItems.IRON_GEAR.get(),
        Tags.Items.INGOTS_IRON);
    gear(RailcraftItems.COPPER_GEAR.get(),
        Tags.Items.INGOTS_COPPER);
    gear(RailcraftItems.GOLD_GEAR.get(),
        Tags.Items.INGOTS_GOLD);
    gear(RailcraftItems.STEEL_GEAR.get(),
        RailcraftTags.Items.STEEL_INGOT);
    gear(RailcraftItems.TIN_GEAR.get(),
        RailcraftTags.Items.TIN_INGOT);
    gear(RailcraftItems.ZINC_GEAR.get(),
        RailcraftTags.Items.ZINC_INGOT);
    gear(RailcraftItems.BRASS_GEAR.get(),
        RailcraftTags.Items.BRASS_INGOT);
    gear(RailcraftItems.BRONZE_GEAR.get(),
        RailcraftTags.Items.BRONZE_INGOT);
    gear(RailcraftItems.NICKEL_GEAR.get(),
        RailcraftTags.Items.NICKEL_INGOT);
    gear(RailcraftItems.INVAR_GEAR.get(),
        RailcraftTags.Items.INVAR_INGOT);
    gear(RailcraftItems.SILVER_GEAR.get(),
        RailcraftTags.Items.SILVER_INGOT);
    gear(RailcraftItems.LEAD_GEAR.get(),
        RailcraftTags.Items.LEAD_INGOT);
  }

  private void gear(Item itemOut, TagKey<Item> materialTag) {
    shaped(RecipeCategory.MISC, itemOut)
        .pattern(" a ")
        .pattern("aba")
        .pattern(" a ")
        .define('a', materialTag)
        .define('b', RailcraftItems.BUSHING_GEAR.get())
        .unlockedBy("has_material", has(materialTag))
        .save(output);
  }

  private void buildMultiblockBlocks() {
    shaped(RecipeCategory.MISC, RailcraftItems.FLUID_FUELED_FIREBOX.get())
        .pattern("aca")
        .pattern("bdb")
        .pattern("aea")
        .define('a', RailcraftTags.Items.INVAR_PLATE)
        .define('b', Items.IRON_BARS)
        .define('c', Items.BUCKET)
        .define('d', Items.FIRE_CHARGE)
        .define('e', Items.FURNACE)
        .unlockedBy(getHasName(RailcraftItems.INVAR_PLATE.get()),
            has(RailcraftItems.INVAR_PLATE.get()))
        .save(output);
    shaped(RecipeCategory.MISC, RailcraftItems.SOLID_FUELED_FIREBOX.get())
        .pattern("aaa")
        .pattern("aba")
        .pattern("aca")
        .define('a', Items.NETHER_BRICK)
        .define('b', Items.FIRE_CHARGE)
        .define('c', Items.FURNACE)
        .unlockedBy(getHasName(Items.FIRE_CHARGE), has(Items.FIRE_CHARGE))
        .save(output);

    shaped(RecipeCategory.MISC, RailcraftItems.COKE_OVEN_BRICKS.get(), 2)
        .pattern("aba")
        .pattern("bcb")
        .pattern("aba")
        .define('a', Items.SAND)
        .define('b', Items.BRICK)
        .define('c', Items.CLAY)
        .unlockedBy(getHasName(Items.BRICK), has(Items.BRICK))
        .save(output);
    shaped(RecipeCategory.MISC, RailcraftItems.BLAST_FURNACE_BRICKS.get(), 4)
        .pattern("aba")
        .pattern("bcb")
        .pattern("aba")
        .define('a', Items.SOUL_SAND)
        .define('b', Items.NETHER_BRICK)
        .define('c', Items.MAGMA_CREAM)
        .unlockedBy(getHasName(Items.MAGMA_CREAM), has(Items.MAGMA_CREAM))
        .save(output);
    shaped(RecipeCategory.MISC, RailcraftItems.CRUSHER.get(), 4)
        .pattern("aba")
        .pattern("bcb")
        .pattern("ada")
        .define('a', Items.DIAMOND)
        .define('b', Items.PISTON)
        .define('c', RailcraftTags.Items.STEEL_BLOCK)
        .define('d', RailcraftItems.CHARGE_MOTOR.get())
        .unlockedBy(getHasName(RailcraftItems.CHARGE_MOTOR.get()),
            has(RailcraftItems.CHARGE_MOTOR.get()))
        .save(output);
    shaped(RecipeCategory.MISC, RailcraftItems.HIGH_PRESSURE_STEAM_BOILER_TANK.get(), 2)
        .pattern("a")
        .pattern("b")
        .pattern("a")
        .define('a', RailcraftTags.Items.STEEL_PLATE)
        .define('b', RailcraftTags.Items.INVAR_PLATE)
        .unlockedBy(getHasName(RailcraftItems.STEEL_PLATE.get()),
            has(RailcraftItems.STEEL_PLATE.get()))
        .save(output);
    shaped(RecipeCategory.MISC, RailcraftItems.LOW_PRESSURE_STEAM_BOILER_TANK.get(), 2)
        .pattern("a")
        .pattern("b")
        .pattern("a")
        .define('a', RailcraftTags.Items.IRON_PLATE)
        .define('b', RailcraftTags.Items.INVAR_PLATE)
        .unlockedBy(getHasName(RailcraftItems.IRON_PLATE.get()),
            has(RailcraftItems.IRON_PLATE.get()))
        .save(output);
    shaped(RecipeCategory.MISC, RailcraftItems.STEAM_OVEN.get(), 4)
        .pattern("aaa")
        .pattern("aba")
        .pattern("aaa")
        .define('a', RailcraftTags.Items.STEEL_PLATE)
        .define('b', Items.FURNACE)
        .unlockedBy(getHasName(RailcraftItems.STEEL_PLATE.get()),
            has(RailcraftItems.STEEL_PLATE.get()))
        .save(output);
  }

  private void buildBlockStorageRecipes() {
    nineBlockStorageRecipes(RailcraftItems.STEEL_NUGGET.get(),
        RailcraftItems.STEEL_INGOT.get(), "steel_ingot_from_steel_nugget");
    nineBlockStorageRecipes(RailcraftItems.TIN_NUGGET.get(),
        RailcraftItems.TIN_INGOT.get(), "tin_ingot_from_tin_nugget");
    nineBlockStorageRecipes(RailcraftItems.ZINC_NUGGET.get(),
        RailcraftItems.ZINC_INGOT.get(), "zinc_ingot_from_zinc_nugget");
    nineBlockStorageRecipes(RailcraftItems.BRASS_NUGGET.get(),
        RailcraftItems.BRASS_INGOT.get(), "brass_ingot_from_brass_nugget");
    nineBlockStorageRecipes(RailcraftItems.BRONZE_NUGGET.get(),
        RailcraftItems.BRONZE_INGOT.get(), "bronze_ingot_from_bronze_nugget");
    nineBlockStorageRecipes(RailcraftItems.NICKEL_NUGGET.get(),
        RailcraftItems.NICKEL_INGOT.get(), "nickel_ingot_from_nickel_nugget");
    nineBlockStorageRecipes(RailcraftItems.INVAR_NUGGET.get(),
        RailcraftItems.INVAR_INGOT.get(), "invar_ingot_from_invar_nugget");
    nineBlockStorageRecipes(RailcraftItems.SILVER_NUGGET.get(),
        RailcraftItems.SILVER_INGOT.get(), "silver_ingot_from_silver_nugget");
    nineBlockStorageRecipes(RailcraftItems.LEAD_NUGGET.get(),
        RailcraftItems.LEAD_INGOT.get(), "lead_ingot_from_lead_nugget");

    nineBlockStorageRecipes(RailcraftItems.STEEL_INGOT.get(),
        RailcraftItems.STEEL_BLOCK.get(), "steel_block_from_steel_ingot");
    nineBlockStorageRecipes(RailcraftItems.TIN_INGOT.get(),
        RailcraftItems.TIN_BLOCK.get(), "tin_block_from_tin_ingot");
    nineBlockStorageRecipes(RailcraftItems.ZINC_INGOT.get(),
        RailcraftItems.ZINC_BLOCK.get(), "zinc_block_from_zinc_ingot");
    nineBlockStorageRecipes(RailcraftItems.BRASS_INGOT.get(),
        RailcraftItems.BRASS_BLOCK.get(), "brass_block_from_brass_ingot");
    nineBlockStorageRecipes(RailcraftItems.BRONZE_INGOT.get(),
        RailcraftItems.BRONZE_BLOCK.get(), "bronze_block_from_bronze_ingot");
    nineBlockStorageRecipes(RailcraftItems.NICKEL_INGOT.get(),
        RailcraftItems.NICKEL_BLOCK.get(), "nickel_block_from_nickel_ingot");
    nineBlockStorageRecipes(RailcraftItems.INVAR_INGOT.get(),
        RailcraftItems.INVAR_BLOCK.get(), "invar_block_from_invar_ingot");
    nineBlockStorageRecipes(RailcraftItems.SILVER_INGOT.get(),
        RailcraftItems.SILVER_BLOCK.get(), "silver_block_from_silver_ingot");
    nineBlockStorageRecipes(RailcraftItems.LEAD_INGOT.get(),
        RailcraftItems.LEAD_BLOCK.get(), "lead_block_from_lead_ingot");


    nineBlockStorageRecipes(RailcraftItems.COAL_COKE.get(),
        RailcraftItems.COAL_COKE_BLOCK.get(), "coal_coke_block_from_coal_coke");
  }

  private void nineBlockStorageRecipes(ItemLike unpacked, ItemLike packed, String packingRecipeName) {
    shapeless(RecipeCategory.MISC, unpacked, 9)
        .requires(packed)
        .unlockedBy(getHasName(packed), has(packed))
        .save(output);
    shaped(RecipeCategory.MISC, packed)
        .pattern("aaa")
        .pattern("aaa")
        .pattern("aaa")
        .define('a', unpacked)
        .unlockedBy(getHasName(unpacked), has(unpacked))
        .save(output, RailcraftConstants.id(packingRecipeName).toString());
  }

  private void buildIngotsRecipes() {
    shaped(RecipeCategory.MISC, RailcraftItems.BRONZE_INGOT.get(), 4)
        .pattern("ab")
        .pattern("bb")
        .define('a', RailcraftTags.Items.TIN_INGOT)
        .define('b', Tags.Items.INGOTS_COPPER)
        .unlockedBy(getHasName(RailcraftItems.TIN_INGOT.get()),
            has(RailcraftTags.Items.TIN_INGOT))
        .unlockedBy(getHasName(Items.COPPER_INGOT), has(Tags.Items.INGOTS_COPPER))
        .save(output, RailcraftConstants.id("bronze_ingot_crafted_with_ingots").toString());
    shaped(RecipeCategory.MISC, RailcraftItems.BRASS_INGOT.get(), 4)
        .pattern("ab")
        .pattern("bb")
        .define('a', RailcraftTags.Items.ZINC_INGOT)
        .define('b', Tags.Items.INGOTS_COPPER)
        .unlockedBy(getHasName(RailcraftItems.ZINC_INGOT.get()),
            has(RailcraftTags.Items.ZINC_INGOT))
        .unlockedBy(getHasName(Items.COPPER_INGOT), has(Tags.Items.INGOTS_COPPER))
        .save(output, RailcraftConstants.id("brass_ingot_crafted_with_ingots").toString());
    shaped(RecipeCategory.MISC, RailcraftItems.INVAR_INGOT.get(), 3)
        .pattern("ab")
        .pattern("b ")
        .define('a', RailcraftTags.Items.NICKEL_INGOT)
        .define('b', Tags.Items.INGOTS_IRON)
        .unlockedBy(getHasName(RailcraftItems.NICKEL_INGOT.get()),
            has(RailcraftTags.Items.NICKEL_INGOT))
        .unlockedBy(getHasName(Items.IRON_INGOT), has(Tags.Items.INGOTS_IRON))
        .save(output, RailcraftConstants.id("invar_ingot_crafted_with_ingots").toString());
  }

  private void buildStrengthenedGlass() {
    var ingredients = Map.of(
        "tin", RailcraftTags.Items.TIN_INGOT,
        "nickel", RailcraftTags.Items.NICKEL_INGOT,
        "invar", RailcraftTags.Items.INVAR_INGOT,
        "brass", RailcraftTags.Items.BRASS_INGOT,
        "iron", Tags.Items.INGOTS_IRON);

    var colorItems = RailcraftItems.STRENGTHENED_GLASS;
    var tagItem = RailcraftTags.Items.STRENGTHENED_GLASS;

    var result = colorItems.variantFor(DyeColor.WHITE).get();
    var name = RecipeBuilder.getDefaultRecipeId(result).getPath();

    for (var ingredient : ingredients.entrySet()) {
      var recipeName = name.substring(name.indexOf('_') + 1) + "_" + ingredient.getKey();
      shaped(RecipeCategory.MISC, result, 6)
          .pattern("aba")
          .pattern("aca")
          .pattern("ada")
          .define('a', Tags.Items.GLASS_BLOCKS)
          .define('b', ingredient.getValue())
          .define('c', RailcraftItems.SALTPETER_DUST.get())
          .define('d', Items.WATER_BUCKET)
          .unlockedBy(getHasName(RailcraftItems.SALTPETER_DUST.get()),
              has(RailcraftItems.SALTPETER_DUST.get()))
          .save(output, RailcraftConstants.id(recipeName).toString());
    }

    coloredBlockVariant(colorItems, tagItem);
  }

  private void buildTie() {
    RailcraftSpecialRecipeBuilder.special(WoodenTieRecipe::new)
        .save(output, getItemName(RailcraftItems.WOODEN_TIE.get()));
    RailcraftSpecialRecipeBuilder.special(StoneTieRecipe::new)
        .save(output, getItemName(RailcraftItems.STONE_TIE.get()));
  }

  private void buildCement() {
    shaped(RecipeCategory.MISC, RailcraftItems.BAG_OF_CEMENT.get(), 2)
        .pattern("ab")
        .pattern("ba")
        .define('a', Items.GRAVEL)
        .define('b', Items.QUARTZ)
        .unlockedBy(getHasName(Items.QUARTZ), has(Items.QUARTZ))
        .save(output);
    shaped(RecipeCategory.MISC, RailcraftItems.BAG_OF_CEMENT.get(), 2)
        .pattern("ab")
        .pattern("ca")
        .define('a', Items.GRAVEL)
        .define('b', Items.QUARTZ)
        .define('c', RailcraftItems.SLAG.get())
        .unlockedBy(getHasName(RailcraftItems.SLAG.get()), has(RailcraftItems.SLAG.get()))
        .save(output, RailcraftConstants.id("bag_of_cement_slag").toString());
  }

  private void tankWall(TagKey<Item> ingredientTag,
      VariantSet<DyeColor, Item, BlockItem> colorItems, TagKey<Item> tagItem) {
    var result = colorItems.variantFor(DyeColor.WHITE).get();
    var name = RecipeBuilder.getDefaultRecipeId(result).getPath();
    var ingredient = ingredientTag.equals(RailcraftTags.Items.IRON_PLATE)
        ? "has_iron_plate" : "has_steel_plate";
    shaped(RecipeCategory.MISC, result, 8)
        .pattern("aa")
        .pattern("aa")
        .define('a', ingredientTag)
        .unlockedBy(ingredient, has(ingredientTag))
        .save(output, RailcraftConstants.id(name.substring(name.indexOf('_') + 1)).toString());

    coloredBlockVariant(colorItems, tagItem);
  }

  private void tankValve(TagKey<Item> ingredientTag,
      VariantSet<DyeColor, Item, BlockItem> colorItems, TagKey<Item> tagItem) {
    var result = colorItems.variantFor(DyeColor.WHITE).get();
    var name = RecipeBuilder.getDefaultRecipeId(result).getPath();
    var ingredient = ingredientTag.equals(RailcraftTags.Items.IRON_PLATE)
        ? "has_iron_plate" : "has_steel_plate";
    shaped(RecipeCategory.MISC, result, 8)
        .pattern("aba")
        .pattern("bcb")
        .pattern("aba")
        .define('a', Items.IRON_BARS)
        .define('b', ingredientTag)
        .define('c', Items.LEVER)
        .unlockedBy(ingredient, has(ingredientTag))
        .save(output, RailcraftConstants.id(name.substring(name.indexOf('_') + 1)).toString());

    coloredBlockVariant(colorItems, tagItem);
  }

  private void tankGauge(TagKey<Item> ingredientTag,
      VariantSet<DyeColor, Item, BlockItem> colorItems, TagKey<Item> tagItem) {
    var result = colorItems.variantFor(DyeColor.WHITE).get();
    var name = RecipeBuilder.getDefaultRecipeId(result).getPath();
    var ingredient = ingredientTag.equals(RailcraftTags.Items.IRON_PLATE)
        ? "has_iron_plate" : "has_steel_plate";
    shaped(RecipeCategory.MISC, result, 8)
        .pattern("aba")
        .pattern("bab")
        .pattern("aba")
        .define('a', Items.GLASS_PANE)
        .define('b', ingredientTag)
        .unlockedBy(ingredient, has(ingredientTag))
        .save(output, RailcraftConstants.id(name.substring(name.indexOf('_') + 1)).toString());

    coloredBlockVariant(colorItems, tagItem);
  }

  private void buildTankBlocks() {
    tankWall(RailcraftTags.Items.IRON_PLATE, RailcraftItems.IRON_TANK_WALL,
        RailcraftTags.Items.IRON_TANK_WALL);
    tankWall(RailcraftTags.Items.STEEL_PLATE, RailcraftItems.STEEL_TANK_WALL,
        RailcraftTags.Items.STEEL_TANK_WALL);
    tankValve(RailcraftTags.Items.IRON_PLATE, RailcraftItems.IRON_TANK_VALVE,
        RailcraftTags.Items.IRON_TANK_VALVE);
    tankValve(RailcraftTags.Items.STEEL_PLATE, RailcraftItems.STEEL_TANK_VALVE,
        RailcraftTags.Items.STEEL_TANK_VALVE);
    tankGauge(RailcraftTags.Items.IRON_PLATE, RailcraftItems.IRON_TANK_GAUGE,
        RailcraftTags.Items.IRON_TANK_GAUGE);
    tankGauge(RailcraftTags.Items.STEEL_PLATE, RailcraftItems.STEEL_TANK_GAUGE,
        RailcraftTags.Items.STEEL_TANK_GAUGE);
  }

  private void buildPost() {
    coloredBlockVariant(RailcraftItems.POST, RailcraftTags.Items.POST, DyeColor.BLACK);
  }

  private void buildDecorativeStone() {
    for (var type : DecorativeBlock.values()) {
      square2x2(RailcraftItems.DECORATIVE_STONE.variantFor(type).get(),
          RailcraftItems.POLISHED_DECORATIVE_STONE.variantFor(type).get(), 4,
          "_from_%s_stone".formatted(type.getSerializedName()));
      square2x2(RailcraftItems.DECORATIVE_COBBLESTONE.variantFor(type).get(),
          RailcraftItems.POLISHED_DECORATIVE_STONE.variantFor(type).get(), 4,
          "_from_%s_cobblestone".formatted(type.getSerializedName()));
      SingleItemRecipeBuilder.stonecutting(
              Ingredient.of(RailcraftItems.DECORATIVE_STONE.variantFor(type).get()),
              RecipeCategory.MISC, RailcraftItems.POLISHED_DECORATIVE_STONE.variantFor(type).get())
          .unlockedBy(getHasName(RailcraftItems.DECORATIVE_STONE.variantFor(type).get()),
              has(RailcraftItems.DECORATIVE_STONE.variantFor(type).get()))
          .save(output,
              RailcraftConstants.id("polished_%s_stone_from_%s_stone_in_stonecutter"
                  .formatted(type.getSerializedName(), type.getSerializedName())).toString());
      SingleItemRecipeBuilder.stonecutting(
              Ingredient.of(RailcraftItems.DECORATIVE_COBBLESTONE.variantFor(type).get()),
              RecipeCategory.MISC, RailcraftItems.POLISHED_DECORATIVE_STONE.variantFor(type).get())
          .unlockedBy(getHasName(RailcraftItems.DECORATIVE_COBBLESTONE.variantFor(type).get()),
              has(RailcraftItems.DECORATIVE_COBBLESTONE.variantFor(type).get()))
          .save(output,
              RailcraftConstants.id("polished_%s_stone_from_%s_cobblestone_in_stonecutter"
                  .formatted(type.getSerializedName(), type.getSerializedName())).toString());
      shaped(RecipeCategory.MISC,
              RailcraftItems.CHISELED_DECORATIVE_STONE.variantFor(type).get(), 8)
          .pattern("aaa")
          .pattern("a a")
          .pattern("aaa")
          .define('a', RailcraftItems.POLISHED_DECORATIVE_STONE.variantFor(type).get())
          .unlockedBy(getHasName(RailcraftItems.POLISHED_DECORATIVE_STONE.variantFor(type).get()),
              has(RailcraftItems.POLISHED_DECORATIVE_STONE.variantFor(type).get()))
          .save(output);
      shaped(RecipeCategory.MISC,
              RailcraftItems.ETCHED_DECORATIVE_STONE.variantFor(type).get(), 8)
          .pattern("aaa")
          .pattern("aba")
          .pattern("aaa")
          .define('a', RailcraftItems.POLISHED_DECORATIVE_STONE.variantFor(type).get())
          .define('b', Items.GUNPOWDER)
          .unlockedBy(getHasName(RailcraftItems.POLISHED_DECORATIVE_STONE.variantFor(type).get()),
              has(RailcraftItems.POLISHED_DECORATIVE_STONE.variantFor(type).get()))
          .save(output);
      square2x2(RailcraftItems.POLISHED_DECORATIVE_STONE.variantFor(type).get(),
          RailcraftItems.DECORATIVE_BRICKS.variantFor(type).get(), 4, "");
      stairBuilder(RailcraftItems.DECORATIVE_BRICK_STAIRS.variantFor(type).get(),
          Ingredient.of(RailcraftItems.DECORATIVE_BRICKS.variantFor(type).get()))
          .unlockedBy(getHasName(RailcraftItems.DECORATIVE_BRICKS.variantFor(type).get()),
              has(RailcraftItems.DECORATIVE_BRICKS.variantFor(type).get()))
          .save(output);
      slab(RecipeCategory.MISC,
          RailcraftItems.DECORATIVE_BRICK_SLAB.variantFor(type).get(),
          RailcraftItems.DECORATIVE_BRICKS.variantFor(type).get());
      square2x2(RailcraftItems.DECORATIVE_BRICKS.variantFor(type).get(),
          RailcraftItems.DECORATIVE_PAVER.variantFor(type).get(), 4, "");
      stairBuilder(RailcraftItems.DECORATIVE_PAVER_STAIRS.variantFor(type).get(),
          Ingredient.of(RailcraftItems.DECORATIVE_PAVER.variantFor(type).get()))
          .unlockedBy(getHasName(RailcraftItems.DECORATIVE_PAVER.variantFor(type).get()),
              has(RailcraftItems.DECORATIVE_PAVER.variantFor(type).get()))
          .save(output);
      slab(RecipeCategory.MISC,
          RailcraftItems.DECORATIVE_PAVER_SLAB.variantFor(type).get(),
          RailcraftItems.DECORATIVE_PAVER.variantFor(type).get());
    }
  }

  private void buildBattery() {
    battery(RailcraftItems.NICKEL_ZINC_BATTERY.get(),
        RailcraftItems.NICKEL_ELECTRODE.get(), RailcraftItems.ZINC_ELECTRODE.get());
    battery(RailcraftItems.NICKEL_IRON_BATTERY.get(),
        RailcraftItems.NICKEL_ELECTRODE.get(), RailcraftItems.IRON_ELECTRODE.get());
    battery(RailcraftItems.ZINC_SILVER_BATTERY.get(),
        RailcraftItems.ZINC_ELECTRODE.get(), RailcraftItems.SILVER_ELECTRODE.get());
    battery(RailcraftItems.ZINC_CARBON_BATTERY.get(),
        RailcraftItems.ZINC_ELECTRODE.get(), RailcraftItems.CARBON_ELECTRODE.get());
  }

  private void battery(Item result, Item left, Item right) {
    shaped(RecipeCategory.MISC, result)
        .pattern("aba")
        .pattern("cde")
        .pattern("cfe")
        .define('a', RailcraftItems.CHARGE_TERMINAL.get())
        .define('b', RailcraftItems.CHARGE_SPOOL_MEDIUM.get())
        .define('c', left)
        .define('d', RailcraftTags.Items.SALTPETER_DUST)
        .define('e', right)
        .define('f', Items.WATER_BUCKET)
        .unlockedBy(getHasName(left), has(left))
        .unlockedBy(getHasName(right), has(right))
        .save(output);
  }

  private void buildFrame() {
    frame(6, RailcraftTags.Items.IRON_PLATE, "_iron_plate");
    frame(6, RailcraftTags.Items.BRONZE_PLATE, "_bronze_plate");
    frame(6, RailcraftTags.Items.BRASS_PLATE, "_brass_plate");
    frame(10, RailcraftTags.Items.STEEL_PLATE, "_steel_plate");
  }

  private void frame(int count, TagKey<Item> tag, String suffix) {
    var name = RecipeBuilder.getDefaultRecipeId(RailcraftItems.FRAME_BLOCK.get()).getPath();
    shaped(RecipeCategory.MISC, RailcraftItems.FRAME_BLOCK.get(), count)
        .pattern("aaa")
        .pattern("b b")
        .pattern("bbb")
        .define('a', tag)
        .define('b', RailcraftItems.REBAR.get())
        .unlockedBy(getHasName(RailcraftItems.REBAR.get()), has(RailcraftItems.REBAR.get()))
        .save(output, RailcraftConstants.id(name + suffix).toString());
  }

  private void buildDetectors() {
    shaped(RecipeCategory.MISC, RailcraftItems.ADVANCED_DETECTOR.get())
        .pattern("aaa")
        .pattern("aba")
        .pattern("aaa")
        .define('a', RailcraftTags.Items.STEEL_INGOT)
        .define('b', Items.STONE_PRESSURE_PLATE)
        .unlockedBy(getHasName(RailcraftItems.STEEL_INGOT.get()),
            has(RailcraftItems.STEEL_INGOT.get()))
        .save(output);
    shaped(RecipeCategory.MISC, RailcraftItems.AGE_DETECTOR.get())
        .pattern("aaa")
        .pattern("aba")
        .pattern("aaa")
        .define('a', Items.DARK_OAK_LOG)
        .define('b', Items.STONE_PRESSURE_PLATE)
        .unlockedBy(getHasName(Items.SPRUCE_LOG), has(Items.SPRUCE_LOG))
        .save(output);
    shaped(RecipeCategory.MISC, RailcraftItems.ANIMAL_DETECTOR.get())
        .pattern("aaa")
        .pattern("aba")
        .pattern("aaa")
        .define('a', Items.OAK_LOG)
        .define('b', Items.STONE_PRESSURE_PLATE)
        .unlockedBy(getHasName(Items.SPRUCE_LOG), has(Items.SPRUCE_LOG))
        .save(output);
    shaped(RecipeCategory.MISC, RailcraftItems.ANY_DETECTOR.get())
        .pattern("aaa")
        .pattern("aba")
        .pattern("aaa")
        .define('a', Items.STONE)
        .define('b', Items.STONE_PRESSURE_PLATE)
        .unlockedBy(getHasName(Items.STONE), has(Items.STONE))
        .save(output);
    shaped(RecipeCategory.MISC, RailcraftItems.EMPTY_DETECTOR.get())
        .pattern("aaa")
        .pattern("aba")
        .pattern("aaa")
        .define('a', Items.STONE_BRICKS)
        .define('b', Items.STONE_PRESSURE_PLATE)
        .unlockedBy(getHasName(Items.STONE_BRICKS), has(Items.STONE_BRICKS))
        .save(output);
    shaped(RecipeCategory.MISC, RailcraftItems.ITEM_DETECTOR.get())
        .pattern("aaa")
        .pattern("aba")
        .pattern("aaa")
        .define('a', Items.STRIPPED_ACACIA_WOOD)
        .define('b', Items.STONE_PRESSURE_PLATE)
        .unlockedBy(getHasName(Items.STRIPPED_ACACIA_WOOD), has(Items.STRIPPED_ACACIA_WOOD))
        .save(output);
    shaped(RecipeCategory.MISC, RailcraftItems.LOCOMOTIVE_DETECTOR.get())
        .pattern("aaa")
        .pattern("aba")
        .pattern("aaa")
        .define('a', RailcraftItems.BLAST_FURNACE_BRICKS.get())
        .define('b', Items.STONE_PRESSURE_PLATE)
        .unlockedBy(getHasName(RailcraftItems.BLAST_FURNACE_BRICKS.get()),
            has(RailcraftItems.BLAST_FURNACE_BRICKS.get()))
        .save(output);
    shaped(RecipeCategory.MISC, RailcraftItems.MOB_DETECTOR.get())
        .pattern("aaa")
        .pattern("aba")
        .pattern("aaa")
        .define('a', Items.MOSSY_COBBLESTONE)
        .define('b', Items.STONE_PRESSURE_PLATE)
        .unlockedBy(getHasName(Items.MOSSY_COBBLESTONE), has(Items.MOSSY_COBBLESTONE))
        .save(output);
    shaped(RecipeCategory.MISC, RailcraftItems.PLAYER_DETECTOR.get())
        .pattern("aaa")
        .pattern("aba")
        .pattern("aaa")
        .define('a', Items.STONE_SLAB)
        .define('b', Items.STONE_PRESSURE_PLATE)
        .unlockedBy(getHasName(Items.STONE_SLAB), has(Items.STONE_SLAB))
        .save(output);
    shaped(RecipeCategory.MISC, RailcraftItems.ROUTING_DETECTOR.get())
        .pattern("aaa")
        .pattern("aba")
        .pattern("aaa")
        .define('a', Items.CHISELED_QUARTZ_BLOCK)
        .define('b', Items.STONE_PRESSURE_PLATE)
        .unlockedBy(getHasName(Items.CHISELED_QUARTZ_BLOCK), has(Items.CHISELED_QUARTZ_BLOCK))
        .save(output);
    shaped(RecipeCategory.MISC, RailcraftItems.SHEEP_DETECTOR.get())
        .pattern("aaa")
        .pattern("aba")
        .pattern("aaa")
        .define('a', ItemTags.WOOL)
        .define('b', Items.STONE_PRESSURE_PLATE)
        .unlockedBy(getHasName(Items.WHITE_WOOL), has(Items.WHITE_WOOL))
        .save(output);
    shaped(RecipeCategory.MISC, RailcraftItems.TANK_DETECTOR.get())
        .pattern("aaa")
        .pattern("aba")
        .pattern("aaa")
        .define('a', Items.BRICK)
        .define('b', Items.STONE_PRESSURE_PLATE)
        .unlockedBy(getHasName(Items.BRICK), has(Items.BRICK))
        .save(output);
    shaped(RecipeCategory.MISC, RailcraftItems.TRAIN_DETECTOR.get())
        .pattern("aaa")
        .pattern("aba")
        .pattern("aaa")
        .define('a', Items.NETHER_BRICK)
        .define('b', Items.STONE_PRESSURE_PLATE)
        .unlockedBy(getHasName(Items.NETHER_BRICK), has(Items.NETHER_BRICK))
        .save(output);
    shaped(RecipeCategory.MISC, RailcraftItems.VILLAGER_DETECTOR.get())
        .pattern("aaa")
        .pattern("aba")
        .pattern("aaa")
        .define('a', Items.LEATHER)
        .define('b', Items.STONE_PRESSURE_PLATE)
        .unlockedBy(getHasName(Items.LEATHER), has(Items.LEATHER))
        .save(output);
  }

  private void buildWorldSpike() {
    shaped(RecipeCategory.MISC, RailcraftItems.WORLD_SPIKE.get())
        .pattern("gog")
        .pattern("dpd")
        .pattern("gog")
        .define('d', Tags.Items.GEMS_DIAMOND)
        .define('g', Tags.Items.INGOTS_GOLD)
        .define('p', Items.ENDER_PEARL)
        .define('o', Items.OBSIDIAN)
        .unlockedBy(getHasName(Items.ENDER_PEARL), has(Items.ENDER_PEARL))
        .save(output);
    shaped(RecipeCategory.MISC, RailcraftItems.PERSONAL_WORLD_SPIKE.get())
        .pattern("gog")
        .pattern("dpd")
        .pattern("gog")
        .define('d', Tags.Items.GEMS_EMERALD)
        .define('g', Tags.Items.INGOTS_GOLD)
        .define('p', Items.ENDER_PEARL)
        .define('o', Items.OBSIDIAN)
        .unlockedBy(getHasName(Items.ENDER_PEARL), has(Items.ENDER_PEARL))
        .save(output);
  }

  private void square2x2(TagKey<Item> ingredient, Item result, int quantity, String suffix) {
  var name = RecipeBuilder.getDefaultRecipeId(result).getPath();
  shaped(RecipeCategory.MISC, result, quantity)
      .pattern("aa")
      .pattern("aa")
      .define('a', ingredient)
      .unlockedBy("has_material", has(ingredient))
      .save(output, RailcraftConstants.id(name + suffix).toString());
}

  private void square2x2(Item ingredient,
      Item result, int quantity, String suffix) {
    var builder = shaped(RecipeCategory.MISC, result, quantity)
        .pattern("aa")
        .pattern("aa")
        .define('a', ingredient)
        .unlockedBy(getHasName(ingredient), has(ingredient));
    if (suffix.isEmpty()) {
      builder.save(output);
    } else {
      var name = RecipeBuilder.getDefaultRecipeId(result).getPath();
      builder.save(output, RailcraftConstants.id(name + suffix).toString());
    }
  }

  private void coloredBlockVariant(VariantSet<DyeColor, Item, BlockItem> colorItems,
      TagKey<Item> tagItem) {
    coloredBlockVariant(colorItems, tagItem, DyeColor.WHITE);
  }

  private void coloredBlockVariant(VariantSet<DyeColor, Item, BlockItem> colorItems,
      TagKey<Item> tagItem, DyeColor baseColor) {
    var base = colorItems.variantFor(baseColor).get();
    for (var dyeColor : DyeColor.values()) {
      shaped(RecipeCategory.MISC, colorItems.variantFor(dyeColor).get(), 8)
          .pattern("aaa")
          .pattern("aba")
          .pattern("aaa")
          .define('a', tagItem)
          .define('b', DyeItem.byColor(dyeColor))
          .unlockedBy(getHasName(base), has(base))
          .save(output);
    }
  }
}
