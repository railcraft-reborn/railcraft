package mods.railcraft.data.recipes;

import java.util.function.Consumer;

import mods.railcraft.tags.RailcraftTags;
import mods.railcraft.world.item.RailcraftItems;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.Tags;

public class RecipeBlocks extends CustomRecipeProvider {

  private RecipeBlocks(DataGenerator datagen) {
    super(datagen);
  }

  public static void registerBlocks(Consumer<FinishedRecipe> consumer) {
    ShapedRecipeBuilder.shaped(RailcraftItems.COKE_OVEN_BRICKS.get())
      .define('B', Items.BRICK)
      .define('S', Tags.Items.SAND)
      .pattern("SBS")
      .pattern("BSB")
      .pattern("SBS")
      .unlockedBy(getHasName(Items.BRICK), has(Items.BRICK))
      .unlockedBy(getHasName(Items.SAND), has(Tags.Items.SAND))
      .save(consumer);

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

    //TODO: TANK_GAUGE I don't know the recipe
  }

}
