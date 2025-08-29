package mods.railcraft.integrations.jei;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import mods.railcraft.world.item.crafting.BlastFurnaceRecipe;
import mods.railcraft.world.item.crafting.CokeOvenRecipe;
import mods.railcraft.world.item.crafting.CrusherRecipe;
import mods.railcraft.world.item.crafting.RollingRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;

public final class JeiRecipeSync {

  private static final List<RecipeHolder<BlastFurnaceRecipe>> BLAST_FURNACE_RECIPES = new ArrayList<>();

  public static void setBlastFurnaceRecipes(
      Collection<RecipeHolder<BlastFurnaceRecipe>> blastFurnaceRecipes) {
    BLAST_FURNACE_RECIPES.clear();
    BLAST_FURNACE_RECIPES.addAll(blastFurnaceRecipes);
  }

  public static List<RecipeHolder<BlastFurnaceRecipe>> getBlastFurnaceRecipes() {
    return BLAST_FURNACE_RECIPES;
  }

  private static final List<RecipeHolder<RollingRecipe>> ROLLING_RECIPES = new ArrayList<>();

  public static void setRollingRecipes(Collection<RecipeHolder<RollingRecipe>> rollingRecipes) {
    ROLLING_RECIPES.clear();
    ROLLING_RECIPES.addAll(rollingRecipes);
  }

  public static List<RecipeHolder<RollingRecipe>> getRollingRecipes() {
    return ROLLING_RECIPES;
  }

  private static final List<RecipeHolder<CokeOvenRecipe>> COKING_RECIPES = new ArrayList<>();

  public static void setCokingRecipes(Collection<RecipeHolder<CokeOvenRecipe>> cokingRecipes) {
    COKING_RECIPES.clear();
    COKING_RECIPES.addAll(cokingRecipes);
  }

  public static List<RecipeHolder<CokeOvenRecipe>> getCokingRecipes() {
    return COKING_RECIPES;
  }

  private static final List<RecipeHolder<CrusherRecipe>> CRUSHING_RECIPES = new ArrayList<>();

  public static void setCrushingRecipes(Collection<RecipeHolder<CrusherRecipe>> crushingRecipes) {
    CRUSHING_RECIPES.clear();
    CRUSHING_RECIPES.addAll(crushingRecipes);
  }

  public static List<RecipeHolder<CrusherRecipe>> getCrushingRecipes() {
    return CRUSHING_RECIPES;
  }

  public static void clearAll() {
    BLAST_FURNACE_RECIPES.clear();
    ROLLING_RECIPES.clear();
    COKING_RECIPES.clear();
    CRUSHING_RECIPES.clear();
  }
}
