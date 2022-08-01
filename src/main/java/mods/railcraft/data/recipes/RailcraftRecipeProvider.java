package mods.railcraft.data.recipes;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import mods.railcraft.Railcraft;
import mods.railcraft.data.recipes.providers.BlastFurnaceRecipeProvider;
import mods.railcraft.data.recipes.providers.CokeOvenRecipeProvider;
import mods.railcraft.data.recipes.providers.CrusherRecipeProvider;
import mods.railcraft.data.recipes.providers.RollingRecipeProvider;
import mods.railcraft.tags.RailcraftTags;
import mods.railcraft.util.VariantRegistrar;
import mods.railcraft.world.item.RailcraftItems;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
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
import net.minecraftforge.common.Tags;

public class RailcraftRecipeProvider extends RecipeProvider {

  public RailcraftRecipeProvider(DataGenerator dataGenerator) {
    super(dataGenerator);
  }

  @Override
  protected void buildCraftingRecipes(Consumer<FinishedRecipe> consumer) {
    CokeOvenRecipeProvider.buildRecipes(consumer);
    BlastFurnaceRecipeProvider.buildRecipes(consumer);
    CrusherRecipeProvider.buildRecipes(consumer);
    RollingRecipeProvider.buildRecipes(consumer);
    /*
     * ===================================== RAILCRAFT TOOLS =====================================
     */
    /* === Crowbar === */
    this.crowbarFromMaterial(consumer, RailcraftItems.IRON_CROWBAR.get(),
        Tags.Items.INGOTS_IRON);
    this.crowbarFromMaterial(consumer, RailcraftItems.STEEL_CROWBAR.get(),
        RailcraftTags.Items.STEEL_INGOT);
    this.crowbarFromMaterial(consumer, RailcraftItems.DIAMOND_CROWBAR.get(),
        Tags.Items.GEMS_DIAMOND);

    ShapedRecipeBuilder.shaped(RailcraftItems.SIGNAL_BLOCK_SURVEYOR.get())
        .define('C', Items.COMPASS)
        .define('G', Tags.Items.GLASS_PANES)
        .define('B', Blocks.STONE_BUTTON)
        .define('R', Tags.Items.DUSTS_REDSTONE)
        .pattern(" C ")
        .pattern("BGB")
        .pattern(" R ")
        .unlockedBy("has_redstone", has(Tags.Items.DUSTS_REDSTONE))
        .save(consumer);

    ShapedRecipeBuilder.shaped(RailcraftItems.SIGNAL_TUNER.get())
        .define('T', Items.REDSTONE_TORCH)
        .define('C', RailcraftItems.RECEIVER_CIRCUIT.get())
        .define('B', Blocks.STONE_BUTTON)
        .pattern(" T ")
        .pattern("BCB")
        .unlockedBy("has_redstone", has(Tags.Items.DUSTS_REDSTONE))
        .unlockedBy("has_circuit", has(RailcraftItems.RECEIVER_CIRCUIT.get()))
        .save(consumer);

    ShapedRecipeBuilder.shaped(RailcraftItems.GOGGLES.get())
        .define('S', RailcraftTags.Items.STEEL_INGOT)
        .define('G', Tags.Items.GLASS_PANES)
        .define('C', RailcraftItems.RECEIVER_CIRCUIT.get())
        .define('L', Tags.Items.LEATHER)
        .pattern("GCG")
        .pattern("S S")
        .pattern("LLL")
        .unlockedBy("has_steel", has(RailcraftTags.Items.STEEL_INGOT))
        .unlockedBy("has_circuit", has(RailcraftItems.RECEIVER_CIRCUIT.get()))
        .save(consumer);

    ShapedRecipeBuilder.shaped(RailcraftItems.WHISTLE_TUNER.get())
        .define('N', RailcraftTags.Items.STEEL_NUGGET)
        .pattern("N N")
        .pattern("NNN")
        .pattern(" N ")
        .unlockedBy("has_nugget", has(RailcraftTags.Items.STEEL_NUGGET))
        .save(consumer);

    ShapedRecipeBuilder.shaped(RailcraftItems.OVERALLS.get())
        .define('W', Items.CYAN_WOOL)
        .pattern("WWW")
        .pattern("W W")
        .pattern("W W")
        .unlockedBy("has_wool", has(Items.CYAN_WOOL))
        .save(consumer);

    // this is a tool i think
    ShapedRecipeBuilder.shaped(RailcraftItems.REFINED_FIRESTONE.get())
        .define('L', Items.LAVA_BUCKET)
        .define('R', Items.REDSTONE_BLOCK)
        .define('S', RailcraftItems.CUT_FIRESTONE.get())
        .pattern("LRL")
        .pattern("RSR")
        .pattern("LRL")
        .unlockedBy("has_firestone", has(RailcraftItems.CUT_FIRESTONE.get()))
        .save(consumer, Railcraft.ID + ":firestone_lava_refinement");

    ShapedRecipeBuilder.shaped(RailcraftItems.REFINED_FIRESTONE.get())
        .define('L', Items.LAVA_BUCKET)
        .define('R', Items.REDSTONE_BLOCK)
        .define('S', RailcraftItems.CRACKED_FIRESTONE.get())
        .define('F', RailcraftItems.RAW_FIRESTONE.get())
        .pattern("LFL")
        .pattern("RSR")
        .pattern("LRL")
        .unlockedBy("has_firestone", has(RailcraftItems.CRACKED_FIRESTONE.get()))
        .save(consumer, Railcraft.ID + ":firestone_cracked_fixing");

    /*
     * ===================================== RAILCRAFT BLOCKS =====================================
     */
    buildMultiblockBlocks(consumer);
    buildBlockStorageRecipes(consumer);
    buildGears(consumer);
    buildKits(consumer);
    buildTankBlocks(consumer);
    buildPost(consumer, RailcraftItems.POST, RailcraftTags.Items.POST);
    buildStrengthenedGlass(consumer);
    buildTie(consumer);
    buildCement(consumer);
    buildRails(consumer);
    buildSteelItems(consumer);
    buildTunnelBoreHead(consumer);
    buildMaul(consumer);

    /*
     * ===================================== RAILCRAFT CRAFTING COMPONENTS =========================
     */
    this.circuitFromMaterial(consumer, RailcraftItems.CONTROLLER_CIRCUIT.get(),
        Items.RED_WOOL);
    this.circuitFromMaterial(consumer, RailcraftItems.RECEIVER_CIRCUIT.get(),
        Items.GREEN_WOOL);
    this.circuitFromMaterial(consumer, RailcraftItems.SIGNAL_CIRCUIT.get(),
        Items.YELLOW_WOOL);

    this.conversion(consumer, RailcraftItems.CHARGE_SPOOL_MEDIUM.get(),
        RailcraftItems.CHARGE_SPOOL_SMALL.get(), 3, "charge_spool_small_from_medium");
    this.conversion(consumer, RailcraftItems.CHARGE_SPOOL_LARGE.get(),
        RailcraftItems.CHARGE_SPOOL_MEDIUM.get(), 3, "charge_spool_medium_from_large");

    ShapelessRecipeBuilder.shapeless(RailcraftItems.WOODEN_RAIL.get(), 6)
        .requires(RailcraftItems.WOODEN_TIE.get())
        .requires(Tags.Items.INGOTS_IRON)
        .unlockedBy("has_wooden_tie", has(Items.RAIL))
        .save(consumer);

    ShapelessRecipeBuilder.shapeless(RailcraftItems.STANDARD_RAIL.get())
        .requires(Items.RAIL, 8)
        .unlockedBy("has_rail", has(Items.RAIL))
        .save(consumer, Railcraft.ID + ":rail_deconstruction");

    ShapelessRecipeBuilder.shapeless(RailcraftItems.WOODEN_RAILBED.get())
        .requires(RailcraftItems.WOODEN_TIE.get(), 4)
        .unlockedBy(getHasName(RailcraftItems.WOODEN_TIE.get()),
            has(RailcraftItems.WOODEN_TIE.get()))
        .save(consumer);

    ShapelessRecipeBuilder.shapeless(RailcraftItems.STONE_RAILBED.get())
        .requires(RailcraftItems.STONE_TIE.get(), 4)
        .unlockedBy(getHasName(RailcraftItems.WOODEN_TIE.get()),
            has(RailcraftItems.WOODEN_TIE.get()))
        .save(consumer);

    ShapedRecipeBuilder.shaped(Items.TORCH, 8)
        .pattern("a")
        .pattern("b")
        .pattern("c")
        .define('a', RailcraftItems.CREOSOTE_BOTTLE.get())
        .define('b', ItemTags.WOOL)
        .define('c', Items.STICK)
        .unlockedBy(getHasName(RailcraftItems.CREOSOTE_BOTTLE.get()),
            has(RailcraftItems.CREOSOTE_BOTTLE.get()))
        .save(consumer, new ResourceLocation(Railcraft.ID, "torch_creosote"));
  }

  private void crowbarFromMaterial(Consumer<FinishedRecipe> finishedRecipe,
      Item itemOut, TagKey<Item> materialTag) {
    ShapedRecipeBuilder.shaped(itemOut)
        .define('I', materialTag)
        .define('D', Tags.Items.DYES_RED)
        .pattern(" DI")
        .pattern("DID")
        .pattern("ID ")
        .unlockedBy(getHasName(Blocks.RAIL), has(Blocks.RAIL))
        .save(finishedRecipe);
  }

  private void conversion(Consumer<FinishedRecipe> finishedRecipe, ItemLike from, ItemLike to,
      int count, String optionalName) {
    ResourceLocation path;
    if (optionalName.isEmpty()) {
      path = RecipeBuilder.getDefaultRecipeId(to);
    } else {
      path = new ResourceLocation(Railcraft.ID, optionalName);
    }
    ShapelessRecipeBuilder.shapeless(to, count)
        .requires(from)
        .unlockedBy(getHasName(from), has(from))
        .save(finishedRecipe, path);
  }

  private void circuitFromMaterial(Consumer<FinishedRecipe> finishedRecipe,
      Item itemOut, Item woolItem) {
    ShapedRecipeBuilder.shaped(itemOut)
        .define('W', woolItem)
        .define('R', Items.REPEATER)
        .define('S', Tags.Items.DUSTS_REDSTONE)
        .define('G', Tags.Items.INGOTS_GOLD)
        .define('L', Tags.Items.GEMS_LAPIS)
        .define('B', Tags.Items.SLIMEBALLS)
        .pattern(" RW")
        .pattern("BGS")
        .pattern("WSL")
        .unlockedBy(getHasName(Items.REDSTONE), has(Tags.Items.DUSTS_REDSTONE))
        .save(finishedRecipe);
  }

  private void buildRails(Consumer<FinishedRecipe> consumer) {
    railsFromMaterials(consumer, RailcraftItems.ABANDONED_TRACK.get(), 32,
        RailcraftItems.STANDARD_RAIL.get(), RailcraftItems.WOODEN_TIE.get());
    railsFromMaterials(consumer, RailcraftItems.STRAP_IRON_TRACK.get(), 32,
        RailcraftItems.WOODEN_RAIL.get(), RailcraftItems.WOODEN_RAILBED.get());
    railsFromMaterials(consumer, Items.RAIL, 32,
        RailcraftItems.STANDARD_RAIL.get(), RailcraftItems.WOODEN_RAILBED.get());
    railsFromMaterials(consumer, RailcraftItems.REINFORCED_TRACK.get(), 32,
        RailcraftItems.REINFORCED_RAIL.get(), RailcraftItems.STONE_RAILBED.get());
    railsFromMaterials(consumer, RailcraftItems.ELECTRIC_TRACK.get(), 32,
        RailcraftItems.ELECTRIC_RAIL.get(), RailcraftItems.STONE_RAILBED.get());
    railsFromMaterials(consumer, RailcraftItems.HIGH_SPEED_TRACK.get(), 32,
        RailcraftItems.HIGH_SPEED_RAIL.get(), RailcraftItems.STONE_RAILBED.get());
    railsFromMaterials(consumer, RailcraftItems.HIGH_SPEED_ELECTRIC_TRACK.get(), 32,
        RailcraftItems.HIGH_SPEED_RAIL.get(), RailcraftItems.STONE_RAILBED.get(),
        RailcraftItems.ELECTRIC_RAIL.get());
    railsFromMaterials(consumer, RailcraftItems.ELEVATOR_TRACK.get(), 8,
        RailcraftItems.ADVANCED_RAIL.get(), RailcraftItems.STANDARD_RAIL.get(),
        Items.REDSTONE);
  }

  private void railsFromMaterials(Consumer<FinishedRecipe> finishedRecipe,
      Item result, int count,  Item railType, Item railBedType) {
    ShapedRecipeBuilder.shaped(result, count)
        .pattern("a a")
        .pattern("aba")
        .pattern("a a")
        .define('a', railType)
        .define('b', railBedType)
        .unlockedBy(getHasName(railType), has(railType))
        .unlockedBy(getHasName(railBedType), has(railBedType))
        // this is deliberate as vanilla ones fail to properly build (rails.json already exists)
        .save(finishedRecipe,
            new ResourceLocation(Railcraft.ID,
                RecipeBuilder.getDefaultRecipeId(result).getPath()));
  }

  private void railsFromMaterials(Consumer<FinishedRecipe> finishedRecipe,
      Item result, int count, Item railType, Item railBedType, Item optionalItem) {
    ShapedRecipeBuilder.shaped(result, count)
        .pattern("aca")
        .pattern("aba")
        .pattern("aca")
        .define('a', railType)
        .define('b', railBedType)
        .define('c', optionalItem)
        .unlockedBy(getHasName(railType), has(railType))
        .unlockedBy(getHasName(railBedType), has(railBedType))
        .save(finishedRecipe);
  }

  private void buildSteelItems(Consumer<FinishedRecipe> consumer) {
    ShapedRecipeBuilder.shaped(RailcraftItems.STEEL_ANVIL.get())
        .pattern("aaa")
        .pattern(" b ")
        .pattern("bbb")
        .define('a', RailcraftTags.Items.STEEL_BLOCK)
        .define('b', RailcraftTags.Items.STEEL_INGOT)
        .unlockedBy(getHasName(RailcraftItems.STEEL_BLOCK.get()),
            has(RailcraftItems.STEEL_BLOCK.get()))
        .save(consumer);
    ShapedRecipeBuilder.shaped(RailcraftItems.STEEL_SHEARS.get())
        .pattern(" a")
        .pattern("a ")
        .define('a', RailcraftTags.Items.STEEL_INGOT)
        .unlockedBy(getHasName(RailcraftItems.STEEL_INGOT.get()),
            has(RailcraftItems.STEEL_INGOT.get()))
        .save(consumer);
    ShapedRecipeBuilder.shaped(RailcraftItems.STEEL_SWORD.get())
        .pattern("a")
        .pattern("a")
        .pattern("b")
        .define('a', RailcraftTags.Items.STEEL_INGOT)
        .define('b', Items.STICK)
        .unlockedBy(getHasName(RailcraftItems.STEEL_INGOT.get()),
            has(RailcraftItems.STEEL_INGOT.get()))
        .save(consumer);
    ShapedRecipeBuilder.shaped(RailcraftItems.STEEL_SHOVEL.get())
        .pattern("a")
        .pattern("b")
        .pattern("b")
        .define('a', RailcraftTags.Items.STEEL_INGOT)
        .define('b', Items.STICK)
        .unlockedBy(getHasName(RailcraftItems.STEEL_INGOT.get()),
            has(RailcraftItems.STEEL_INGOT.get()))
        .save(consumer);
    ShapedRecipeBuilder.shaped(RailcraftItems.STEEL_PICKAXE.get())
        .pattern("aaa")
        .pattern(" b ")
        .pattern(" b ")
        .define('a', RailcraftTags.Items.STEEL_INGOT)
        .define('b', Items.STICK)
        .unlockedBy(getHasName(RailcraftItems.STEEL_INGOT.get()),
            has(RailcraftItems.STEEL_INGOT.get()))
        .save(consumer);
    ShapedRecipeBuilder.shaped(RailcraftItems.STEEL_AXE.get())
        .pattern("aa")
        .pattern("ab")
        .pattern(" b")
        .define('a', RailcraftTags.Items.STEEL_INGOT)
        .define('b', Items.STICK)
        .unlockedBy(getHasName(RailcraftItems.STEEL_INGOT.get()),
            has(RailcraftItems.STEEL_INGOT.get()))
        .save(consumer);
    ShapedRecipeBuilder.shaped(RailcraftItems.STEEL_HOE.get())
        .pattern("aa")
        .pattern(" b")
        .pattern(" b")
        .define('a', RailcraftTags.Items.STEEL_INGOT)
        .define('b', Items.STICK)
        .unlockedBy(getHasName(RailcraftItems.STEEL_INGOT.get()),
            has(RailcraftItems.STEEL_INGOT.get()))
        .save(consumer);
    ShapedRecipeBuilder.shaped(RailcraftItems.STEEL_BOOTS.get())
        .pattern("a a")
        .pattern("a a")
        .define('a', RailcraftTags.Items.STEEL_INGOT)
        .unlockedBy(getHasName(RailcraftItems.STEEL_INGOT.get()),
            has(RailcraftItems.STEEL_INGOT.get()))
        .save(consumer);
    ShapedRecipeBuilder.shaped(RailcraftItems.STEEL_LEGGINGS.get())
        .pattern("aaa")
        .pattern("a a")
        .pattern("a a")
        .define('a', RailcraftTags.Items.STEEL_INGOT)
        .unlockedBy(getHasName(RailcraftItems.STEEL_INGOT.get()),
            has(RailcraftItems.STEEL_INGOT.get()))
        .save(consumer);
    ShapedRecipeBuilder.shaped(RailcraftItems.STEEL_CHESTPLATE.get())
        .pattern("a a")
        .pattern("aaa")
        .pattern("aaa")
        .define('a', RailcraftTags.Items.STEEL_INGOT)
        .unlockedBy(getHasName(RailcraftItems.STEEL_INGOT.get()),
            has(RailcraftItems.STEEL_INGOT.get()))
        .save(consumer);
    ShapedRecipeBuilder.shaped(RailcraftItems.STEEL_HELMET.get())
        .pattern("aaa")
        .pattern("a a")
        .define('a', RailcraftTags.Items.STEEL_INGOT)
        .unlockedBy(getHasName(RailcraftItems.STEEL_INGOT.get()),
            has(RailcraftItems.STEEL_INGOT.get()))
        .save(consumer);
  }

  private void buildTunnelBoreHead(Consumer<FinishedRecipe> consumer) {
    tunnelBoreHead(consumer, RailcraftItems.BRONZE_TUNNEL_BORE_HEAD.get(),
        RailcraftTags.Items.BRONZE_BLOCK);
    tunnelBoreHead(consumer, RailcraftItems.IRON_TUNNEL_BORE_HEAD.get(),
        Tags.Items.STORAGE_BLOCKS_IRON);
    tunnelBoreHead(consumer, RailcraftItems.STEEL_TUNNEL_BORE_HEAD.get(),
        RailcraftTags.Items.STEEL_BLOCK);
    tunnelBoreHead(consumer, RailcraftItems.DIAMOND_TUNNEL_BORE_HEAD.get(),
        Tags.Items.STORAGE_BLOCKS_DIAMOND);
  }

  private static void tunnelBoreHead(Consumer<FinishedRecipe> consumer,
      Item result,
      TagKey<Item> center) {
    ShapedRecipeBuilder.shaped(result)
        .pattern("aaa")
        .pattern("aba")
        .pattern("aaa")
        .define('a', RailcraftTags.Items.STEEL_INGOT)
        .define('b', center)
        .unlockedBy(getHasName(RailcraftItems.STEEL_INGOT.get()),
            has(RailcraftItems.STEEL_INGOT.get()))
        .save(consumer);
  }

  private void buildMaul(Consumer<FinishedRecipe> consumer) {
    ShapedRecipeBuilder.shaped(RailcraftItems.IRON_SPIKE_MAUL.get())
        .pattern("aca")
        .pattern(" b ")
        .pattern(" b ")
        .define('a', Tags.Items.INGOTS_IRON)
        .define('b', Items.STICK)
        .define('c', Tags.Items.STORAGE_BLOCKS_IRON)
        .unlockedBy(getHasName(Items.IRON_BLOCK), has(Items.IRON_BLOCK))
        .save(consumer);
    ShapedRecipeBuilder.shaped(RailcraftItems.STEEL_SPIKE_MAUL.get())
        .pattern("aca")
        .pattern(" b ")
        .pattern(" b ")
        .define('a', RailcraftTags.Items.STEEL_INGOT)
        .define('b', Items.STICK)
        .define('c', RailcraftTags.Items.STEEL_BLOCK)
        .unlockedBy(getHasName(RailcraftItems.STEEL_BLOCK.get()),
            has(RailcraftItems.STEEL_BLOCK.get()))
        .save(consumer);
    ShapedRecipeBuilder.shaped(RailcraftItems.DIAMOND_SPIKE_MAUL.get())
        .pattern("aca")
        .pattern(" b ")
        .pattern(" b ")
        .define('a', Tags.Items.GEMS_DIAMOND)
        .define('b', Items.STICK)
        .define('c', Tags.Items.STORAGE_BLOCKS_DIAMOND)
        .unlockedBy(getHasName(Items.DIAMOND_BLOCK), has(Items.DIAMOND_BLOCK))
        .save(consumer);
  }

  private void buildKits(Consumer<FinishedRecipe> consumer) {
    kits(consumer, RailcraftItems.ACTIVATOR_TRACK_KIT.get(), 8, List.of(
            new Tuple<>(Ingredient.of(Tags.Items.DUSTS_REDSTONE), 2)
        ));
    kits(consumer, RailcraftItems.BOOSTER_TRACK_KIT.get(), 16, List.of(
        new Tuple<>(Ingredient.of(RailcraftItems.ADVANCED_RAIL.get()), 2),
        new Tuple<>(Ingredient.of(Tags.Items.DUSTS_REDSTONE), 1)
    ));
    kits(consumer, RailcraftItems.BUFFER_STOP_TRACK_KIT.get(), 2, List.of(
        new Tuple<>(Ingredient.of(Tags.Items.INGOTS_IRON), 2)
    ));
    kits(consumer, RailcraftItems.CONTROL_TRACK_KIT.get(), 16, List.of(
        new Tuple<>(Ingredient.of(RailcraftItems.ADVANCED_RAIL.get()), 1),
        new Tuple<>(Ingredient.of(Tags.Items.DUSTS_REDSTONE), 1)
    ));
    kits(consumer, RailcraftItems.DETECTOR_TRACK_KIT.get(), 8, List.of(
        new Tuple<>(Ingredient.of(Items.STONE_PRESSURE_PLATE), 1),
        new Tuple<>(Ingredient.of(Tags.Items.DUSTS_REDSTONE), 1)
    ));
    kits(consumer, RailcraftItems.DISEMBARKING_TRACK_KIT.get(), 4, List.of(
        new Tuple<>(Ingredient.of(Items.STONE_PRESSURE_PLATE), 1),
        new Tuple<>(Ingredient.of(Items.LEAD), 1),
        new Tuple<>(Ingredient.of(Tags.Items.DUSTS_REDSTONE), 1)
    ));
    kits(consumer, RailcraftItems.EMBARKING_TRACK_KIT.get(), 4, List.of(
        new Tuple<>(Ingredient.of(Items.ENDER_PEARL), 1),
        new Tuple<>(Ingredient.of(Items.LEAD), 1),
        new Tuple<>(Ingredient.of(Tags.Items.DUSTS_REDSTONE), 1)
    ));
    kits(consumer, RailcraftItems.GATED_TRACK_KIT.get(), 4, List.of(
        new Tuple<>(Ingredient.of(Tags.Items.FENCE_GATES), 1),
        new Tuple<>(Ingredient.of(RailcraftItems.ADVANCED_RAIL.get()), 1),
        new Tuple<>(Ingredient.of(Tags.Items.DUSTS_REDSTONE), 1)
    ));
    kits(consumer, RailcraftItems.LOCKING_TRACK_KIT.get(), 4, List.of(
        new Tuple<>(Ingredient.of(Items.STONE_PRESSURE_PLATE), 1),
        new Tuple<>(Ingredient.of(Items.STICKY_PISTON), 1),
        new Tuple<>(Ingredient.of(Tags.Items.DUSTS_REDSTONE), 1)
    ));
    kits(consumer, RailcraftItems.ONE_WAY_TRACK_KIT.get(), 8, List.of(
        new Tuple<>(Ingredient.of(Items.STONE_PRESSURE_PLATE), 1),
        new Tuple<>(Ingredient.of(Items.PISTON), 1),
        new Tuple<>(Ingredient.of(Tags.Items.DUSTS_REDSTONE), 1)
    ));
    kits(consumer, RailcraftItems.LAUNCHER_TRACK_KIT.get(), 1, List.of(
        new Tuple<>(Ingredient.of(Items.PISTON), 1),
        new Tuple<>(Ingredient.of(RailcraftTags.Items.STEEL_BLOCK), 2),
        new Tuple<>(Ingredient.of(Tags.Items.DUSTS_REDSTONE), 1)
    ));
    kits(consumer, RailcraftItems.LOCOMOTIVE_TRACK_KIT.get(), 4, List.of(
        new Tuple<>(Ingredient.of(RailcraftItems.SIGNAL_LAMP.get()), 1),
        new Tuple<>(Ingredient.of(Tags.Items.DUSTS_REDSTONE), 1)
    ));
    kits(consumer, RailcraftItems.TRANSITION_TRACK_KIT.get(), 8, List.of(
        new Tuple<>(Ingredient.of(RailcraftItems.ADVANCED_RAIL.get()), 2),
        new Tuple<>(Ingredient.of(Tags.Items.DUSTS_REDSTONE), 2)
    ));
    kits(consumer, RailcraftItems.COUPLER_TRACK_KIT.get(), 4, List.of(
        new Tuple<>(Ingredient.of(Items.LEAD), 1),
        new Tuple<>(Ingredient.of(Tags.Items.DUSTS_REDSTONE), 1)
    ));
  }

  private static void kits(Consumer<FinishedRecipe> finishedRecipe,
      Item result,
      int count,
      List<Tuple<Ingredient, Integer>> ingredients) {

      var builder = ShapelessRecipeBuilder.shapeless(result, count)
          .requires(ItemTags.PLANKS)
          .requires(RailcraftItems.TRACK_PARTS.get());

      for(var ingredient : ingredients) {
        builder = builder.requires(ingredient.getA(), ingredient.getB());
      }
      builder
          .unlockedBy(getHasName(RailcraftItems.TRACK_PARTS.get()),
              has(RailcraftItems.TRACK_PARTS.get()))
          .save(finishedRecipe);
  }

  private void buildGears(Consumer<FinishedRecipe> consumer) {
    square2x2(consumer, RailcraftTags.Items.BRONZE_INGOT,
        RailcraftItems.BUSHING_GEAR.get(), 1, "_bronze");
    square2x2(consumer, RailcraftTags.Items.BRASS_INGOT,
        RailcraftItems.BUSHING_GEAR.get(), 1, "_brass");

    gear(consumer, RailcraftItems.IRON_GEAR.get(),
        Tags.Items.INGOTS_IRON);
    gear(consumer, RailcraftItems.COPPER_GEAR.get(),
        Tags.Items.INGOTS_COPPER);
    gear(consumer, RailcraftItems.GOLD_GEAR.get(),
        Tags.Items.INGOTS_GOLD);
    gear(consumer, RailcraftItems.STEEL_GEAR.get(),
        RailcraftTags.Items.STEEL_INGOT);
    gear(consumer, RailcraftItems.TIN_GEAR.get(),
        RailcraftTags.Items.TIN_INGOT);
    gear(consumer, RailcraftItems.ZINC_GEAR.get(),
        RailcraftTags.Items.ZINC_INGOT);
    gear(consumer, RailcraftItems.BRASS_GEAR.get(),
        RailcraftTags.Items.BRASS_INGOT);
    gear(consumer, RailcraftItems.BRONZE_GEAR.get(),
        RailcraftTags.Items.BRONZE_INGOT);
    gear(consumer, RailcraftItems.NICKEL_GEAR.get(),
        RailcraftTags.Items.NICKEL_INGOT);
    gear(consumer, RailcraftItems.INVAR_GEAR.get(),
        RailcraftTags.Items.INVAR_INGOT);
    gear(consumer, RailcraftItems.SILVER_GEAR.get(),
        RailcraftTags.Items.SILVER_INGOT);
    gear(consumer, RailcraftItems.LEAD_GEAR.get(),
        RailcraftTags.Items.LEAD_INGOT);
  }

  private static void gear(Consumer<FinishedRecipe> finishedRecipe,
      Item itemOut,
      TagKey<Item> materialTag) {
    ShapedRecipeBuilder.shaped(itemOut)
        .pattern(" a ")
        .pattern("aba")
        .pattern(" a ")
        .define('a', materialTag)
        .define('b', RailcraftItems.BUSHING_GEAR.get())
        .unlockedBy("has_material", has(materialTag))
        .save(finishedRecipe);
  }

  private void buildMultiblockBlocks(Consumer<FinishedRecipe> consumer) {
    ShapedRecipeBuilder.shaped(RailcraftItems.FLUID_FUELED_FIREBOX.get())
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
        .save(consumer);
    ShapedRecipeBuilder.shaped(RailcraftItems.SOLID_FUELED_FIREBOX.get())
        .pattern("aaa")
        .pattern("aba")
        .pattern("aca")
        .define('a', Items.NETHER_BRICK)
        .define('b', Items.FIRE_CHARGE)
        .define('c', Items.FURNACE)
        .unlockedBy(getHasName(Items.FIRE_CHARGE), has(Items.FIRE_CHARGE))
        .save(consumer);

    ShapedRecipeBuilder.shaped(RailcraftItems.COKE_OVEN_BRICKS.get(), 2)
        .pattern("aba")
        .pattern("bcb")
        .pattern("aba")
        .define('a', Items.SAND)
        .define('b', Items.BRICK)
        .define('c', Items.CLAY)
        .unlockedBy(getHasName(Items.BRICK), has(Items.BRICK))
        .save(consumer);
    ShapedRecipeBuilder.shaped(RailcraftItems.BLAST_FURNACE_BRICKS.get(), 4)
        .pattern("aba")
        .pattern("bcb")
        .pattern("aba")
        .define('a', Items.SOUL_SAND)
        .define('b', Items.NETHER_BRICK)
        .define('c', Items.MAGMA_CREAM)
        .unlockedBy(getHasName(Items.MAGMA_CREAM), has(Items.MAGMA_CREAM))
        .save(consumer);
    ShapedRecipeBuilder.shaped(RailcraftItems.HIGH_PRESSURE_STEAM_BOILER_TANK.get(), 2)
        .pattern("a")
        .pattern("b")
        .pattern("a")
        .define('a', RailcraftTags.Items.STEEL_PLATE)
        .define('b', RailcraftTags.Items.INVAR_PLATE)
        .unlockedBy(getHasName(RailcraftItems.STEEL_PLATE.get()), has(RailcraftItems.STEEL_PLATE.get()))
        .save(consumer);
    ShapedRecipeBuilder.shaped(RailcraftItems.LOW_PRESSURE_STEAM_BOILER_TANK.get(), 2)
        .pattern("a")
        .pattern("b")
        .pattern("a")
        .define('a', RailcraftTags.Items.IRON_PLATE)
        .define('b', RailcraftTags.Items.INVAR_PLATE)
        .unlockedBy(getHasName(RailcraftItems.IRON_PLATE.get()), has(RailcraftItems.IRON_PLATE.get()))
        .save(consumer);
  }

  private void buildBlockStorageRecipes(Consumer<FinishedRecipe> consumer) {
    nineBlockStorageRecipes(consumer, RailcraftItems.STEEL_NUGGET.get(),
        RailcraftItems.STEEL_INGOT.get(), "steel_ingot_from_steel_nugget");
    nineBlockStorageRecipes(consumer, RailcraftItems.TIN_NUGGET.get(),
        RailcraftItems.TIN_INGOT.get(), "tin_ingot_from_tin_nugget");
    nineBlockStorageRecipes(consumer, RailcraftItems.ZINC_NUGGET.get(),
        RailcraftItems.ZINC_INGOT.get(), "zinc_ingot_from_zinc_nugget");
    nineBlockStorageRecipes(consumer, RailcraftItems.BRASS_NUGGET.get(),
        RailcraftItems.BRASS_INGOT.get(), "brass_ingot_from_brass_nugget");
    nineBlockStorageRecipes(consumer, RailcraftItems.BRONZE_NUGGET.get(),
        RailcraftItems.BRONZE_INGOT.get(), "bronze_ingot_from_bronze_nugget");
    nineBlockStorageRecipes(consumer, RailcraftItems.NICKEL_NUGGET.get(),
        RailcraftItems.NICKEL_INGOT.get(), "nickel_ingot_from_nickel_nugget");
    nineBlockStorageRecipes(consumer, RailcraftItems.INVAR_NUGGET.get(),
        RailcraftItems.INVAR_INGOT.get(), "invar_ingot_from_invar_nugget");
    nineBlockStorageRecipes(consumer, RailcraftItems.SILVER_NUGGET.get(),
        RailcraftItems.SILVER_INGOT.get(), "silver_ingot_from_silver_nugget");
    nineBlockStorageRecipes(consumer, RailcraftItems.LEAD_NUGGET.get(),
        RailcraftItems.LEAD_INGOT.get(), "lead_ingot_from_lead_nugget");

    nineBlockStorageRecipes(consumer, RailcraftItems.STEEL_INGOT.get(),
        RailcraftItems.STEEL_BLOCK.get(), "steel_block_from_steel_ingot");
    nineBlockStorageRecipes(consumer, RailcraftItems.TIN_INGOT.get(),
        RailcraftItems.TIN_BLOCK.get(), "tin_block_from_tin_ingot");
    nineBlockStorageRecipes(consumer, RailcraftItems.ZINC_INGOT.get(),
        RailcraftItems.ZINC_BLOCK.get(), "zinc_block_from_zinc_ingot");
    nineBlockStorageRecipes(consumer, RailcraftItems.BRASS_INGOT.get(),
        RailcraftItems.BRASS_BLOCK.get(), "brass_block_from_brass_ingot");
    nineBlockStorageRecipes(consumer, RailcraftItems.BRONZE_INGOT.get(),
        RailcraftItems.BRONZE_BLOCK.get(), "bronze_block_from_bronze_ingot");
    nineBlockStorageRecipes(consumer, RailcraftItems.NICKEL_INGOT.get(),
        RailcraftItems.NICKEL_BLOCK.get(), "nickel_block_from_nickel_ingot");
    nineBlockStorageRecipes(consumer, RailcraftItems.INVAR_INGOT.get(),
        RailcraftItems.INVAR_BLOCK.get(), "invar_block_from_invar_ingot");
    nineBlockStorageRecipes(consumer, RailcraftItems.SILVER_INGOT.get(),
        RailcraftItems.SILVER_BLOCK.get(), "silver_block_from_silver_ingot");
    nineBlockStorageRecipes(consumer, RailcraftItems.LEAD_INGOT.get(),
        RailcraftItems.LEAD_BLOCK.get(), "lead_block_from_lead_ingot");


    nineBlockStorageRecipes(consumer, RailcraftItems.COAL_COKE.get(),
        RailcraftItems.COKE_BLOCK.get(), "coke_block_from_coal_coke");
  }

  private static void nineBlockStorageRecipes(Consumer<FinishedRecipe> pFinishedRecipeConsumer,
      ItemLike pUnpacked, ItemLike pPacked, String pPackingRecipeName) {
    ShapelessRecipeBuilder.shapeless(pUnpacked, 9)
        .requires(pPacked)
        .unlockedBy(getHasName(pPacked), has(pPacked))
        .save(pFinishedRecipeConsumer);
    ShapedRecipeBuilder.shaped(pPacked)
        .pattern("aaa")
        .pattern("aaa")
        .pattern("aaa")
        .define('a', pUnpacked)
        .unlockedBy(getHasName(pUnpacked), has(pUnpacked))
        .save(pFinishedRecipeConsumer, new ResourceLocation(Railcraft.ID, pPackingRecipeName));
  }

  private void buildStrengthenedGlass(Consumer<FinishedRecipe> consumer) {
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
      ShapedRecipeBuilder.shaped(result, 6)
          .pattern("aba")
          .pattern("aca")
          .pattern("ada")
          .define('a', Tags.Items.GLASS)
          .define('b', ingredient.getValue())
          .define('c', RailcraftItems.SALTPETER_DUST.get())
          .define('d', Items.WATER_BUCKET)
          .unlockedBy(getHasName(RailcraftItems.SALTPETER_DUST.get()),
              has(RailcraftItems.SALTPETER_DUST.get()))
          .save(consumer, new ResourceLocation(Railcraft.ID, recipeName));
    }

    coloredBlockVariant(consumer, colorItems, tagItem);
  }

  private void buildTie(Consumer<FinishedRecipe> consumer) {
    ShapedRecipeBuilder.shaped(RailcraftItems.WOODEN_TIE.get())
        .pattern(" a ")
        .pattern("bbb")
        .define('a', RailcraftItems.CREOSOTE_BUCKET.get())
        .define('b', ItemTags.WOODEN_SLABS)
        .unlockedBy(getHasName(RailcraftItems.CREOSOTE_BUCKET.get()),
            has(RailcraftItems.CREOSOTE_BUCKET.get()))
        .save(consumer);
    ShapedRecipeBuilder.shaped(RailcraftItems.WOODEN_TIE.get())
        .pattern(" a ")
        .pattern("bbb")
        .define('a', RailcraftItems.CREOSOTE_BOTTLE.get())
        .define('b', ItemTags.WOODEN_SLABS)
        .unlockedBy(getHasName(RailcraftItems.CREOSOTE_BOTTLE.get()),
            has(RailcraftItems.CREOSOTE_BOTTLE.get()))
        .save(consumer, new ResourceLocation(Railcraft.ID, "wooden_tie_bottle"));
    ShapedRecipeBuilder.shaped(RailcraftItems.STONE_TIE.get())
        .pattern(" a ")
        .pattern("bcb")
        .define('a', Items.WATER_BUCKET)
        .define('b', RailcraftItems.BAG_OF_CEMENT.get())
        .define('c', RailcraftItems.REBAR.get())
        .unlockedBy(getHasName(RailcraftItems.BAG_OF_CEMENT.get()),
            has(RailcraftItems.BAG_OF_CEMENT.get()))
        .save(consumer);
  }

  private void buildCement(Consumer<FinishedRecipe> consumer) {
    ShapedRecipeBuilder.shaped(RailcraftItems.BAG_OF_CEMENT.get(), 2)
        .pattern("ab")
        .pattern("ba")
        .define('a', Items.GRAVEL)
        .define('b', Items.QUARTZ)
        .unlockedBy(getHasName(Items.QUARTZ), has(Items.QUARTZ))
        .save(consumer);
    ShapedRecipeBuilder.shaped(RailcraftItems.BAG_OF_CEMENT.get(), 2)
        .pattern("ab")
        .pattern("ca")
        .define('a', Items.GRAVEL)
        .define('b', Items.QUARTZ)
        .define('c', RailcraftItems.SLAG_DUST.get())
        .unlockedBy(getHasName(RailcraftItems.SLAG_DUST.get()), has(RailcraftItems.SLAG_DUST.get()))
        .save(consumer, new ResourceLocation(Railcraft.ID, "bag_of_cement_slag"));
  }

  private static void tankWall(Consumer<FinishedRecipe> consumer,
      ItemLike ingredient,
      VariantRegistrar<DyeColor, BlockItem> colorItems,
      TagKey<Item> tagItem) {
    var result = colorItems.variantFor(DyeColor.WHITE).get();
    var name = RecipeBuilder.getDefaultRecipeId(result).getPath();
    ShapedRecipeBuilder.shaped(result, 8)
        .pattern("aa")
        .pattern("aa")
        .define('a', ingredient)
        .unlockedBy(getHasName(ingredient), has(ingredient))
        .save(consumer, Railcraft.ID + ":" + name.substring(name.indexOf('_') + 1));

    coloredBlockVariant(consumer, colorItems, tagItem);
  }

  private static void tankValve(Consumer<FinishedRecipe> consumer,
      ItemLike ingredient,
      VariantRegistrar<DyeColor, BlockItem> colorItems,
      TagKey<Item> tagItem) {
    var result = colorItems.variantFor(DyeColor.WHITE).get();
    var name = RecipeBuilder.getDefaultRecipeId(result).getPath();
    ShapedRecipeBuilder.shaped(result, 8)
        .pattern("aba")
        .pattern("bcb")
        .pattern("aba")
        .define('a', Items.IRON_BARS)
        .define('b', ingredient)
        .define('c', Items.LEVER)
        .unlockedBy(getHasName(ingredient), has(ingredient))
        .save(consumer, new ResourceLocation(Railcraft.ID, name.substring(name.indexOf('_') + 1)));

    coloredBlockVariant(consumer, colorItems, tagItem);
  }

  private static void tankGauge(Consumer<FinishedRecipe> consumer,
      ItemLike ingredient,
      VariantRegistrar<DyeColor, BlockItem> colorItems,
      TagKey<Item> tagItem) {
    var result = colorItems.variantFor(DyeColor.WHITE).get();
    var name = RecipeBuilder.getDefaultRecipeId(result).getPath();
    ShapedRecipeBuilder.shaped(result, 8)
        .pattern("aba")
        .pattern("bab")
        .pattern("aba")
        .define('a', Items.GLASS_PANE)
        .define('b', ingredient)
        .unlockedBy(getHasName(ingredient), has(ingredient))
        .save(consumer, new ResourceLocation(Railcraft.ID, name.substring(name.indexOf('_') + 1)));

    coloredBlockVariant(consumer, colorItems, tagItem);
  }

  private void buildTankBlocks(Consumer<FinishedRecipe> consumer) {
    tankWall(consumer,
        RailcraftItems.IRON_PLATE.get(),
        RailcraftItems.IRON_TANK_WALL,
        RailcraftTags.Items.IRON_TANK_WALL);
    tankWall(consumer,
        RailcraftItems.STEEL_PLATE.get(),
        RailcraftItems.STEEL_TANK_WALL,
        RailcraftTags.Items.STEEL_TANK_WALL);
    tankValve(consumer,
        RailcraftItems.IRON_PLATE.get(),
        RailcraftItems.IRON_TANK_VALVE,
        RailcraftTags.Items.IRON_TANK_VALVE);
    tankValve(consumer,
        RailcraftItems.STEEL_PLATE.get(),
        RailcraftItems.STEEL_TANK_VALVE,
        RailcraftTags.Items.STEEL_TANK_VALVE);

    tankGauge(consumer,
        RailcraftItems.IRON_PLATE.get(),
        RailcraftItems.IRON_TANK_GAUGE,
        RailcraftTags.Items.IRON_TANK_GAUGE);
    tankGauge(consumer,
        RailcraftItems.STEEL_PLATE.get(),
        RailcraftItems.STEEL_TANK_GAUGE,
        RailcraftTags.Items.STEEL_TANK_GAUGE);
  }

  private void buildPost(Consumer<FinishedRecipe> consumer,
      VariantRegistrar<DyeColor, BlockItem> colorItems,
      TagKey<Item> tagItem) {
    coloredBlockVariant(consumer, colorItems, tagItem, DyeColor.BLACK);
  }

  private static void square2x2(Consumer<FinishedRecipe> finishedRecipe,
      TagKey<Item> ingredient,
      Item result,
      int quantity,
      String suffix) {
    var name = RecipeBuilder.getDefaultRecipeId(result).getPath();
    ShapedRecipeBuilder.shaped(result, quantity)
        .pattern("aa")
        .pattern("aa")
        .define('a', ingredient)
        .unlockedBy("has_material", has(ingredient))
        .save(finishedRecipe, new ResourceLocation(Railcraft.ID, name + suffix));
  }

  private static void coloredBlockVariant(Consumer<FinishedRecipe> consumer,
      VariantRegistrar<DyeColor, BlockItem> colorItems,
      TagKey<Item> tagItem) {
    coloredBlockVariant(consumer, colorItems, tagItem, DyeColor.WHITE);
  }

  private static void coloredBlockVariant(Consumer<FinishedRecipe> consumer,
      VariantRegistrar<DyeColor, BlockItem> colorItems,
      TagKey<Item> tagItem,
      DyeColor baseColor) {
    var base = colorItems.variantFor(baseColor).get();
    for (var dyeColor : DyeColor.values()) {
      ShapedRecipeBuilder.shaped(colorItems.variantFor(dyeColor).get(), 8)
          .pattern("aaa")
          .pattern("aba")
          .pattern("aaa")
          .define('a', tagItem)
          .define('b', DyeItem.byColor(dyeColor))
          .unlockedBy(getHasName(base), has(base))
          .save(consumer);
    }
  }

  @Override
  public String getName() {
    return "Railcraft Recipes";
  }
}
