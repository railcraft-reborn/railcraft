package mods.railcraft.data.recipes.providers;

import java.util.concurrent.CompletableFuture;
import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.data.recipes.builders.BlastFurnaceRecipeBuilder;
import mods.railcraft.world.item.RailcraftItems;
import mods.railcraft.world.level.block.RailcraftBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.common.Tags;

public class BlastFurnaceRecipeProvider extends RecipeProvider {

  private final HolderLookup.RegistryLookup<Item> items;

  private BlastFurnaceRecipeProvider(HolderLookup.Provider registries, RecipeOutput recipeOutput) {
    super(registries, recipeOutput);
    this.items = registries.lookupOrThrow(Registries.ITEM);
  }

  public static class Runner extends RecipeProvider.Runner {

    public Runner(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
      super(output, registries);
    }

    @Override
    protected RecipeProvider createRecipeProvider(HolderLookup.Provider registries, RecipeOutput output) {
      return new BlastFurnaceRecipeProvider(registries, output);
    }

    @Override
    public String getName() {
      return "BlastFurnaceRecipeProvider";
    }
  }

  @Override
  protected void buildRecipes() {}

  public void genRecipes(RecipeOutput recipeOutput) {
    BlastFurnaceRecipeBuilder
        .smelting(RailcraftBlocks.STEEL_BLOCK.get(), 1,
            Ingredient.of(this.items.getOrThrow(Tags.Items.STORAGE_BLOCKS_IRON)), 9, 9)
        .unlockedBy("has_iron_block", has(Tags.Items.STORAGE_BLOCKS_IRON))
        .save(recipeOutput, getRailcraftBlastingRecipeName(RailcraftItems.STEEL_BLOCK.get()).toString());

    BlastFurnaceRecipeBuilder
        .smelting(RailcraftItems.STEEL_INGOT.get(),
            Ingredient.of(this.items.getOrThrow(Tags.Items.INGOTS_IRON)), 1, 1)
        .unlockedBy("has_iron_ingots", has(Tags.Items.INGOTS_IRON))
        .save(recipeOutput, getRailcraftBlastingRecipeName(Items.IRON_INGOT).toString());

    consumeIronSteelItems();
  }

  private void consumeIronSteelItems() {
    blastFurnaceSmelting(Items.BUCKET, 3);
    blastFurnaceSmelting(Items.IRON_HELMET, 5);
    blastFurnaceSmelting(Items.IRON_CHESTPLATE, 8);
    blastFurnaceSmelting(Items.IRON_LEGGINGS, 7);
    blastFurnaceSmelting(Items.IRON_BOOTS, 4);
    blastFurnaceSmelting(Items.IRON_HORSE_ARMOR, 4);
    blastFurnaceSmelting(Items.IRON_SWORD, 2);
    blastFurnaceSmelting(Items.IRON_SHOVEL, 1);
    blastFurnaceSmelting(Items.IRON_PICKAXE, 3);
    blastFurnaceSmelting(Items.IRON_AXE, 3);
    blastFurnaceSmelting(Items.IRON_HOE, 2);
    blastFurnaceSmelting(Items.SHEARS, 2);
    blastFurnaceSmelting(Items.IRON_DOOR, 2);
    blastFurnaceSmelting(Items.IRON_TRAPDOOR, 4);
    blastFurnaceRecycling(RailcraftItems.STEEL_HELMET.get(), 4);
    blastFurnaceRecycling(RailcraftItems.STEEL_CHESTPLATE.get(), 7);
    blastFurnaceRecycling(RailcraftItems.STEEL_LEGGINGS.get(), 6);
    blastFurnaceRecycling(RailcraftItems.STEEL_BOOTS.get(), 3);
    blastFurnaceRecycling(RailcraftItems.STEEL_SWORD.get(), 1);
    blastFurnaceRecycling(RailcraftItems.STEEL_PICKAXE.get(), 2);
    blastFurnaceRecycling(RailcraftItems.STEEL_HOE.get(), 1);
    blastFurnaceRecycling(RailcraftItems.STEEL_AXE.get(), 2);
    blastFurnaceRecycling(RailcraftItems.STEEL_SHEARS.get(), 1);
    blastFurnaceRecycling(RailcraftItems.IRON_CROWBAR.get(), 2);
  }

  private void blastFurnaceSmelting(ItemLike item, int multiplier) {
    BlastFurnaceRecipeBuilder
        .smelting(RailcraftItems.STEEL_INGOT.get(), Ingredient.of(item), multiplier, multiplier)
        .unlockedBy(getHasName(item), has(item))
        .save(output, getRailcraftBlastingRecipeName(item).toString());
  }

  private void blastFurnaceRecycling(ItemLike item, int multiplier) {
    BlastFurnaceRecipeBuilder
        .recycling(RailcraftItems.STEEL_INGOT.get(), Ingredient.of(item), multiplier)
        .unlockedBy(getHasName(item), has(item))
        .save(output, getRailcraftBlastingRecipeName(item).toString());
  }

  private static ResourceLocation getRailcraftBlastingRecipeName(ItemLike item) {
    var tag = BuiltInRegistries.ITEM.getKey(item.asItem()).getPath();
    return RailcraftConstants.rl("blasting_" + tag);
  }
}
