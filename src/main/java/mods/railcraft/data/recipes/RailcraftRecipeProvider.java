package mods.railcraft.data.recipes;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.data.recipes.builders.RailcraftSpecialRecipeBuilder;
import mods.railcraft.data.recipes.providers.BlastFurnaceRecipeProvider;
import mods.railcraft.data.recipes.providers.CokeOvenRecipeProvider;
import mods.railcraft.data.recipes.providers.CrusherRecipeProvider;
import mods.railcraft.data.recipes.providers.RollingRecipeProvider;
import mods.railcraft.tags.RailcraftTags;
import mods.railcraft.util.VariantRegistrar;
import mods.railcraft.world.item.RailcraftItems;
import mods.railcraft.world.item.crafting.RailcraftRecipeSerializers;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.data.recipes.SingleItemRecipeBuilder;
import net.minecraft.data.recipes.SpecialRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Tuple;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.crafting.ConditionalRecipe;
import net.minecraftforge.common.crafting.conditions.ModLoadedCondition;
import vazkii.patchouli.api.PatchouliAPI;

public class RailcraftRecipeProvider extends RecipeProvider {

  public RailcraftRecipeProvider(PackOutput packOutput) {
    super(packOutput);
  }

  @Override
  protected void buildRecipes(Consumer<FinishedRecipe> consumer) {
    CokeOvenRecipeProvider.genRecipes(consumer);
    BlastFurnaceRecipeProvider.genRecipes(consumer);
    CrusherRecipeProvider.genRecipes(consumer);
    RollingRecipeProvider.genRecipes(consumer);

    buildMultiblockBlocks(consumer);
    buildBlockStorageRecipes(consumer);
    buildIngotsRecipes(consumer);
    buildGears(consumer);
    buildKits(consumer);
    buildTankBlocks(consumer);
    buildPost(consumer);
    buildStrengthenedGlass(consumer);
    buildTie(consumer);
    buildCement(consumer);
    buildRails(consumer);
    buildTracks(consumer);
    buildSteelItems(consumer);
    buildTunnelBoreHead(consumer);
    buildMaul(consumer);
    buildOreSmelt(consumer);
    buildTurbineParts(consumer);
    buildChargeItems(consumer);
    buildSignalBox(consumer);
    buildSignals(consumer);
    buildCircuit(consumer);
    buildMiscItems(consumer);
    buildCartsVariant(consumer);
    buildSwitch(consumer);
    buildLoaders(consumer);
    buildCrowbars(consumer);
    buildFirestones(consumer);
    buildQuarriedStone(consumer);
    buildAbyssalStone(consumer);
    buildBattery(consumer);
    buildFrame(consumer);
    buildDetectors(consumer);
    buildWorldSpike(consumer);
  }

  private void conversion(Consumer<FinishedRecipe> finishedRecipe, ItemLike from, ItemLike to,
      int count, String optionalName) {
    ResourceLocation path;
    if (optionalName.isEmpty()) {
      path = RecipeBuilder.getDefaultRecipeId(to);
    } else {
      path = RailcraftConstants.rl(optionalName);
    }
    ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, to, count)
        .requires(from)
        .unlockedBy(getHasName(from), has(from))
        .save(finishedRecipe, path);
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

    ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, RailcraftItems.WOODEN_RAIL.get(), 6)
        .requires(RailcraftItems.WOODEN_TIE.get())
        .requires(Tags.Items.INGOTS_IRON)
        .unlockedBy(getHasName(RailcraftItems.WOODEN_TIE.get()),
            has(RailcraftItems.WOODEN_TIE.get()))
        .save(consumer);

    ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, RailcraftItems.STANDARD_RAIL.get())
        .requires(Items.RAIL, 8)
        .unlockedBy(getHasName(Items.RAIL), has(Items.RAIL))
        .save(consumer, RailcraftConstants.rl("standard_rail_from_rail"));

    ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, RailcraftItems.WOODEN_RAILBED.get())
        .requires(RailcraftItems.WOODEN_TIE.get(), 4)
        .unlockedBy(getHasName(RailcraftItems.WOODEN_TIE.get()),
            has(RailcraftItems.WOODEN_TIE.get()))
        .save(consumer);

    ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, RailcraftItems.STONE_RAILBED.get())
        .requires(RailcraftItems.STONE_TIE.get(), 4)
        .unlockedBy(getHasName(RailcraftItems.WOODEN_TIE.get()),
            has(RailcraftItems.WOODEN_TIE.get()))
        .save(consumer);
  }

  private static void railsFromMaterials(Consumer<FinishedRecipe> finishedRecipe,
      Item result, int count, Item railType, Item railBedType) {
    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, result, count)
        .pattern("a a")
        .pattern("aba")
        .pattern("a a")
        .define('a', railType)
        .define('b', railBedType)
        .unlockedBy(getHasName(railType), has(railType))
        .unlockedBy(getHasName(railBedType), has(railBedType))
        .save(finishedRecipe,
            new ResourceLocation(result.equals(Items.RAIL) ? "minecraft" : RailcraftConstants.ID,
                RecipeBuilder.getDefaultRecipeId(result).getPath()));
  }

  private static void railsFromMaterials(Consumer<FinishedRecipe> finishedRecipe,
      Item result, int count, Item railType, Item railBedType, Item optionalItem) {
    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, result, count)
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

  private void buildTracks(Consumer<FinishedRecipe> consumer) {
    tracks(consumer, RailcraftItems.ABANDONED_ACTIVATOR_TRACK.get(),
        RailcraftItems.ACTIVATOR_TRACK_KIT.get(), RailcraftItems.ABANDONED_TRACK.get());
    tracks(consumer, RailcraftItems.ABANDONED_BOOSTER_TRACK.get(),
        RailcraftItems.BOOSTER_TRACK_KIT.get(), RailcraftItems.ABANDONED_TRACK.get());
    tracks(consumer, RailcraftItems.ABANDONED_BUFFER_STOP_TRACK.get(),
        RailcraftItems.BUFFER_STOP_TRACK_KIT.get(), RailcraftItems.ABANDONED_TRACK.get());
    tracks(consumer, RailcraftItems.ABANDONED_COUPLER_TRACK.get(),
        RailcraftItems.COUPLER_TRACK_KIT.get(), RailcraftItems.ABANDONED_TRACK.get());
    tracks(consumer, RailcraftItems.ABANDONED_CONTROL_TRACK.get(),
        RailcraftItems.CONTROL_TRACK_KIT.get(), RailcraftItems.ABANDONED_TRACK.get());
    tracks(consumer, RailcraftItems.ABANDONED_DETECTOR_TRACK.get(),
        RailcraftItems.DETECTOR_TRACK_KIT.get(), RailcraftItems.ABANDONED_TRACK.get());
    tracks(consumer, RailcraftItems.ABANDONED_DISEMBARKING_TRACK.get(),
        RailcraftItems.DISEMBARKING_TRACK_KIT.get(), RailcraftItems.ABANDONED_TRACK.get());
    tracks(consumer, RailcraftItems.ABANDONED_EMBARKING_TRACK.get(),
        RailcraftItems.EMBARKING_TRACK_KIT.get(), RailcraftItems.ABANDONED_TRACK.get());
    tracks(consumer, RailcraftItems.ABANDONED_DUMPING_TRACK.get(),
        RailcraftItems.DUMPING_TRACK_KIT.get(), RailcraftItems.ABANDONED_TRACK.get());
    tracks(consumer, RailcraftItems.ABANDONED_GATED_TRACK.get(),
        RailcraftItems.GATED_TRACK_KIT.get(), RailcraftItems.ABANDONED_TRACK.get());
    tracks(consumer, RailcraftItems.ABANDONED_LAUNCHER_TRACK.get(),
        RailcraftItems.LAUNCHER_TRACK_KIT.get(), RailcraftItems.ABANDONED_TRACK.get());
    tracks(consumer, RailcraftItems.ABANDONED_LOCKING_TRACK.get(),
        RailcraftItems.LOCKING_TRACK_KIT.get(), RailcraftItems.ABANDONED_TRACK.get());
    tracks(consumer, RailcraftItems.ABANDONED_WHISTLE_TRACK.get(),
        RailcraftItems.WHISTLE_TRACK_KIT.get(), RailcraftItems.ABANDONED_TRACK.get());
    tracks(consumer, RailcraftItems.ABANDONED_LOCOMOTIVE_TRACK.get(),
        RailcraftItems.LOCOMOTIVE_TRACK_KIT.get(), RailcraftItems.ABANDONED_TRACK.get());
    tracks(consumer, RailcraftItems.ABANDONED_THROTTLE_TRACK.get(),
        RailcraftItems.THROTTLE_TRACK_KIT.get(), RailcraftItems.ABANDONED_TRACK.get());
    tracks(consumer, RailcraftItems.ABANDONED_ONE_WAY_TRACK.get(),
        RailcraftItems.ONE_WAY_TRACK_KIT.get(), RailcraftItems.ABANDONED_TRACK.get());
    tracks(consumer, RailcraftItems.ABANDONED_ROUTING_TRACK.get(),
        RailcraftItems.ROUTING_TRACK_KIT.get(), RailcraftItems.ABANDONED_TRACK.get());

    tracks(consumer, RailcraftItems.IRON_ACTIVATOR_TRACK.get(),
        RailcraftItems.ACTIVATOR_TRACK_KIT.get(), Items.RAIL);
    tracks(consumer, RailcraftItems.IRON_BOOSTER_TRACK.get(),
        RailcraftItems.BOOSTER_TRACK_KIT.get(), Items.RAIL);
    tracks(consumer, RailcraftItems.IRON_BUFFER_STOP_TRACK.get(),
        RailcraftItems.BUFFER_STOP_TRACK_KIT.get(), Items.RAIL);
    tracks(consumer, RailcraftItems.IRON_COUPLER_TRACK.get(),
        RailcraftItems.COUPLER_TRACK_KIT.get(), Items.RAIL);
    tracks(consumer, RailcraftItems.IRON_CONTROL_TRACK.get(),
        RailcraftItems.CONTROL_TRACK_KIT.get(), Items.RAIL);
    tracks(consumer, RailcraftItems.IRON_DETECTOR_TRACK.get(),
        RailcraftItems.DETECTOR_TRACK_KIT.get(), Items.RAIL);
    tracks(consumer, RailcraftItems.IRON_DISEMBARKING_TRACK.get(),
        RailcraftItems.DISEMBARKING_TRACK_KIT.get(), Items.RAIL);
    tracks(consumer, RailcraftItems.IRON_EMBARKING_TRACK.get(),
        RailcraftItems.EMBARKING_TRACK_KIT.get(), Items.RAIL);
    tracks(consumer, RailcraftItems.IRON_DUMPING_TRACK.get(),
        RailcraftItems.DUMPING_TRACK_KIT.get(), Items.RAIL);
    tracks(consumer, RailcraftItems.IRON_GATED_TRACK.get(),
        RailcraftItems.GATED_TRACK_KIT.get(), Items.RAIL);
    tracks(consumer, RailcraftItems.IRON_LAUNCHER_TRACK.get(),
        RailcraftItems.LAUNCHER_TRACK_KIT.get(), Items.RAIL);
    tracks(consumer, RailcraftItems.IRON_LOCKING_TRACK.get(),
        RailcraftItems.LOCKING_TRACK_KIT.get(), Items.RAIL);
    tracks(consumer, RailcraftItems.IRON_WHISTLE_TRACK.get(),
        RailcraftItems.WHISTLE_TRACK_KIT.get(), Items.RAIL);
    tracks(consumer, RailcraftItems.IRON_LOCOMOTIVE_TRACK.get(),
        RailcraftItems.LOCOMOTIVE_TRACK_KIT.get(), Items.RAIL);
    tracks(consumer, RailcraftItems.IRON_THROTTLE_TRACK.get(),
        RailcraftItems.THROTTLE_TRACK_KIT.get(), Items.RAIL);
    tracks(consumer, RailcraftItems.IRON_ONE_WAY_TRACK.get(),
        RailcraftItems.ONE_WAY_TRACK_KIT.get(), Items.RAIL);
    tracks(consumer, RailcraftItems.IRON_ROUTING_TRACK.get(),
        RailcraftItems.ROUTING_TRACK_KIT.get(), Items.RAIL);

    tracks(consumer, RailcraftItems.STRAP_IRON_ACTIVATOR_TRACK.get(),
        RailcraftItems.ACTIVATOR_TRACK_KIT.get(), RailcraftItems.STRAP_IRON_TRACK.get());
    tracks(consumer, RailcraftItems.STRAP_IRON_BOOSTER_TRACK.get(),
        RailcraftItems.BOOSTER_TRACK_KIT.get(), RailcraftItems.STRAP_IRON_TRACK.get());
    tracks(consumer, RailcraftItems.STRAP_IRON_BUFFER_STOP_TRACK.get(),
        RailcraftItems.BUFFER_STOP_TRACK_KIT.get(), RailcraftItems.STRAP_IRON_TRACK.get());
    tracks(consumer, RailcraftItems.STRAP_IRON_COUPLER_TRACK.get(),
        RailcraftItems.COUPLER_TRACK_KIT.get(), RailcraftItems.STRAP_IRON_TRACK.get());
    tracks(consumer, RailcraftItems.STRAP_IRON_CONTROL_TRACK.get(),
        RailcraftItems.CONTROL_TRACK_KIT.get(), RailcraftItems.STRAP_IRON_TRACK.get());
    tracks(consumer, RailcraftItems.STRAP_IRON_DETECTOR_TRACK.get(),
        RailcraftItems.DETECTOR_TRACK_KIT.get(), RailcraftItems.STRAP_IRON_TRACK.get());
    tracks(consumer, RailcraftItems.STRAP_IRON_DISEMBARKING_TRACK.get(),
        RailcraftItems.DISEMBARKING_TRACK_KIT.get(), RailcraftItems.STRAP_IRON_TRACK.get());
    tracks(consumer, RailcraftItems.STRAP_IRON_EMBARKING_TRACK.get(),
        RailcraftItems.EMBARKING_TRACK_KIT.get(), RailcraftItems.STRAP_IRON_TRACK.get());
    tracks(consumer, RailcraftItems.STRAP_IRON_DUMPING_TRACK.get(),
        RailcraftItems.DUMPING_TRACK_KIT.get(), RailcraftItems.STRAP_IRON_TRACK.get());
    tracks(consumer, RailcraftItems.STRAP_IRON_GATED_TRACK.get(),
        RailcraftItems.GATED_TRACK_KIT.get(), RailcraftItems.STRAP_IRON_TRACK.get());
    tracks(consumer, RailcraftItems.STRAP_IRON_LAUNCHER_TRACK.get(),
        RailcraftItems.LAUNCHER_TRACK_KIT.get(), RailcraftItems.STRAP_IRON_TRACK.get());
    tracks(consumer, RailcraftItems.STRAP_IRON_LOCKING_TRACK.get(),
        RailcraftItems.LOCKING_TRACK_KIT.get(), RailcraftItems.STRAP_IRON_TRACK.get());
    tracks(consumer, RailcraftItems.STRAP_IRON_WHISTLE_TRACK.get(),
        RailcraftItems.WHISTLE_TRACK_KIT.get(), RailcraftItems.STRAP_IRON_TRACK.get());
    tracks(consumer, RailcraftItems.STRAP_IRON_LOCOMOTIVE_TRACK.get(),
        RailcraftItems.LOCOMOTIVE_TRACK_KIT.get(), RailcraftItems.STRAP_IRON_TRACK.get());
    tracks(consumer, RailcraftItems.STRAP_IRON_THROTTLE_TRACK.get(),
        RailcraftItems.THROTTLE_TRACK_KIT.get(), RailcraftItems.STRAP_IRON_TRACK.get());
    tracks(consumer, RailcraftItems.STRAP_IRON_ONE_WAY_TRACK.get(),
        RailcraftItems.ONE_WAY_TRACK_KIT.get(), RailcraftItems.STRAP_IRON_TRACK.get());
    tracks(consumer, RailcraftItems.STRAP_IRON_ROUTING_TRACK.get(),
        RailcraftItems.ROUTING_TRACK_KIT.get(), RailcraftItems.STRAP_IRON_TRACK.get());

    tracks(consumer, RailcraftItems.REINFORCED_ACTIVATOR_TRACK.get(),
        RailcraftItems.ACTIVATOR_TRACK_KIT.get(), RailcraftItems.REINFORCED_TRACK.get());
    tracks(consumer, RailcraftItems.REINFORCED_BOOSTER_TRACK.get(),
        RailcraftItems.BOOSTER_TRACK_KIT.get(), RailcraftItems.REINFORCED_TRACK.get());
    tracks(consumer, RailcraftItems.REINFORCED_BUFFER_STOP_TRACK.get(),
        RailcraftItems.BUFFER_STOP_TRACK_KIT.get(), RailcraftItems.REINFORCED_TRACK.get());
    tracks(consumer, RailcraftItems.REINFORCED_COUPLER_TRACK.get(),
        RailcraftItems.COUPLER_TRACK_KIT.get(), RailcraftItems.REINFORCED_TRACK.get());
    tracks(consumer, RailcraftItems.REINFORCED_CONTROL_TRACK.get(),
        RailcraftItems.CONTROL_TRACK_KIT.get(), RailcraftItems.REINFORCED_TRACK.get());
    tracks(consumer, RailcraftItems.REINFORCED_DETECTOR_TRACK.get(),
        RailcraftItems.DETECTOR_TRACK_KIT.get(), RailcraftItems.REINFORCED_TRACK.get());
    tracks(consumer, RailcraftItems.REINFORCED_DISEMBARKING_TRACK.get(),
        RailcraftItems.DISEMBARKING_TRACK_KIT.get(), RailcraftItems.REINFORCED_TRACK.get());
    tracks(consumer, RailcraftItems.REINFORCED_EMBARKING_TRACK.get(),
        RailcraftItems.EMBARKING_TRACK_KIT.get(), RailcraftItems.REINFORCED_TRACK.get());
    tracks(consumer, RailcraftItems.REINFORCED_DUMPING_TRACK.get(),
        RailcraftItems.DUMPING_TRACK_KIT.get(), RailcraftItems.REINFORCED_TRACK.get());
    tracks(consumer, RailcraftItems.REINFORCED_GATED_TRACK.get(),
        RailcraftItems.GATED_TRACK_KIT.get(), RailcraftItems.REINFORCED_TRACK.get());
    tracks(consumer, RailcraftItems.REINFORCED_LAUNCHER_TRACK.get(),
        RailcraftItems.LAUNCHER_TRACK_KIT.get(), RailcraftItems.REINFORCED_TRACK.get());
    tracks(consumer, RailcraftItems.REINFORCED_LOCKING_TRACK.get(),
        RailcraftItems.LOCKING_TRACK_KIT.get(), RailcraftItems.REINFORCED_TRACK.get());
    tracks(consumer, RailcraftItems.REINFORCED_WHISTLE_TRACK.get(),
        RailcraftItems.WHISTLE_TRACK_KIT.get(), RailcraftItems.REINFORCED_TRACK.get());
    tracks(consumer, RailcraftItems.REINFORCED_LOCOMOTIVE_TRACK.get(),
        RailcraftItems.LOCOMOTIVE_TRACK_KIT.get(), RailcraftItems.REINFORCED_TRACK.get());
    tracks(consumer, RailcraftItems.REINFORCED_THROTTLE_TRACK.get(),
        RailcraftItems.THROTTLE_TRACK_KIT.get(), RailcraftItems.REINFORCED_TRACK.get());
    tracks(consumer, RailcraftItems.REINFORCED_ONE_WAY_TRACK.get(),
        RailcraftItems.ONE_WAY_TRACK_KIT.get(), RailcraftItems.REINFORCED_TRACK.get());
    tracks(consumer, RailcraftItems.REINFORCED_ROUTING_TRACK.get(),
        RailcraftItems.ROUTING_TRACK_KIT.get(), RailcraftItems.REINFORCED_TRACK.get());

    tracks(consumer, RailcraftItems.ELECTRIC_ACTIVATOR_TRACK.get(),
        RailcraftItems.ACTIVATOR_TRACK_KIT.get(), RailcraftItems.ELECTRIC_TRACK.get());
    tracks(consumer, RailcraftItems.ELECTRIC_BOOSTER_TRACK.get(),
        RailcraftItems.BOOSTER_TRACK_KIT.get(), RailcraftItems.ELECTRIC_TRACK.get());
    tracks(consumer, RailcraftItems.ELECTRIC_BUFFER_STOP_TRACK.get(),
        RailcraftItems.BUFFER_STOP_TRACK_KIT.get(), RailcraftItems.ELECTRIC_TRACK.get());
    tracks(consumer, RailcraftItems.ELECTRIC_COUPLER_TRACK.get(),
        RailcraftItems.COUPLER_TRACK_KIT.get(), RailcraftItems.ELECTRIC_TRACK.get());
    tracks(consumer, RailcraftItems.ELECTRIC_CONTROL_TRACK.get(),
        RailcraftItems.CONTROL_TRACK_KIT.get(), RailcraftItems.ELECTRIC_TRACK.get());
    tracks(consumer, RailcraftItems.ELECTRIC_DETECTOR_TRACK.get(),
        RailcraftItems.DETECTOR_TRACK_KIT.get(), RailcraftItems.ELECTRIC_TRACK.get());
    tracks(consumer, RailcraftItems.ELECTRIC_DISEMBARKING_TRACK.get(),
        RailcraftItems.DISEMBARKING_TRACK_KIT.get(), RailcraftItems.ELECTRIC_TRACK.get());
    tracks(consumer, RailcraftItems.ELECTRIC_EMBARKING_TRACK.get(),
        RailcraftItems.EMBARKING_TRACK_KIT.get(), RailcraftItems.ELECTRIC_TRACK.get());
    tracks(consumer, RailcraftItems.ELECTRIC_DUMPING_TRACK.get(),
        RailcraftItems.DUMPING_TRACK_KIT.get(), RailcraftItems.ELECTRIC_TRACK.get());
    tracks(consumer, RailcraftItems.ELECTRIC_GATED_TRACK.get(),
        RailcraftItems.GATED_TRACK_KIT.get(), RailcraftItems.ELECTRIC_TRACK.get());
    tracks(consumer, RailcraftItems.ELECTRIC_LAUNCHER_TRACK.get(),
        RailcraftItems.LAUNCHER_TRACK_KIT.get(), RailcraftItems.ELECTRIC_TRACK.get());
    tracks(consumer, RailcraftItems.ELECTRIC_LOCKING_TRACK.get(),
        RailcraftItems.LOCKING_TRACK_KIT.get(), RailcraftItems.ELECTRIC_TRACK.get());
    tracks(consumer, RailcraftItems.ELECTRIC_WHISTLE_TRACK.get(),
        RailcraftItems.WHISTLE_TRACK_KIT.get(), RailcraftItems.ELECTRIC_TRACK.get());
    tracks(consumer, RailcraftItems.ELECTRIC_LOCOMOTIVE_TRACK.get(),
        RailcraftItems.LOCOMOTIVE_TRACK_KIT.get(), RailcraftItems.ELECTRIC_TRACK.get());
    tracks(consumer, RailcraftItems.ELECTRIC_THROTTLE_TRACK.get(),
        RailcraftItems.THROTTLE_TRACK_KIT.get(), RailcraftItems.ELECTRIC_TRACK.get());
    tracks(consumer, RailcraftItems.ELECTRIC_ONE_WAY_TRACK.get(),
        RailcraftItems.ONE_WAY_TRACK_KIT.get(), RailcraftItems.ELECTRIC_TRACK.get());
    tracks(consumer, RailcraftItems.ELECTRIC_ROUTING_TRACK.get(),
        RailcraftItems.ROUTING_TRACK_KIT.get(), RailcraftItems.ELECTRIC_TRACK.get());

    tracks(consumer, RailcraftItems.HIGH_SPEED_ACTIVATOR_TRACK.get(),
        RailcraftItems.ACTIVATOR_TRACK_KIT.get(), RailcraftItems.HIGH_SPEED_TRACK.get());
    tracks(consumer, RailcraftItems.HIGH_SPEED_BOOSTER_TRACK.get(),
        RailcraftItems.BOOSTER_TRACK_KIT.get(), RailcraftItems.HIGH_SPEED_TRACK.get());
    tracks(consumer, RailcraftItems.HIGH_SPEED_DETECTOR_TRACK.get(),
        RailcraftItems.DETECTOR_TRACK_KIT.get(), RailcraftItems.HIGH_SPEED_TRACK.get());
    tracks(consumer, RailcraftItems.HIGH_SPEED_LOCKING_TRACK.get(),
        RailcraftItems.LOCKING_TRACK_KIT.get(), RailcraftItems.HIGH_SPEED_TRACK.get());
    tracks(consumer, RailcraftItems.HIGH_SPEED_WHISTLE_TRACK.get(),
        RailcraftItems.WHISTLE_TRACK_KIT.get(), RailcraftItems.HIGH_SPEED_TRACK.get());
    tracks(consumer, RailcraftItems.HIGH_SPEED_LOCOMOTIVE_TRACK.get(),
        RailcraftItems.LOCOMOTIVE_TRACK_KIT.get(), RailcraftItems.HIGH_SPEED_TRACK.get());
    tracks(consumer, RailcraftItems.HIGH_SPEED_THROTTLE_TRACK.get(),
        RailcraftItems.THROTTLE_TRACK_KIT.get(), RailcraftItems.HIGH_SPEED_TRACK.get());
    tracks(consumer, RailcraftItems.HIGH_SPEED_TRANSITION_TRACK.get(),
        RailcraftItems.TRANSITION_TRACK_KIT.get(), RailcraftItems.HIGH_SPEED_TRACK.get());

    tracks(consumer, RailcraftItems.HIGH_SPEED_ELECTRIC_ACTIVATOR_TRACK.get(),
        RailcraftItems.ACTIVATOR_TRACK_KIT.get(), RailcraftItems.HIGH_SPEED_ELECTRIC_TRACK.get());
    tracks(consumer, RailcraftItems.HIGH_SPEED_ELECTRIC_BOOSTER_TRACK.get(),
        RailcraftItems.BOOSTER_TRACK_KIT.get(), RailcraftItems.HIGH_SPEED_ELECTRIC_TRACK.get());
    tracks(consumer, RailcraftItems.HIGH_SPEED_ELECTRIC_DETECTOR_TRACK.get(),
        RailcraftItems.DETECTOR_TRACK_KIT.get(), RailcraftItems.HIGH_SPEED_ELECTRIC_TRACK.get());
    tracks(consumer, RailcraftItems.HIGH_SPEED_ELECTRIC_LOCKING_TRACK.get(),
        RailcraftItems.LOCKING_TRACK_KIT.get(), RailcraftItems.HIGH_SPEED_ELECTRIC_TRACK.get());
    tracks(consumer, RailcraftItems.HIGH_SPEED_ELECTRIC_WHISTLE_TRACK.get(),
        RailcraftItems.WHISTLE_TRACK_KIT.get(), RailcraftItems.HIGH_SPEED_ELECTRIC_TRACK.get());
    tracks(consumer, RailcraftItems.HIGH_SPEED_ELECTRIC_LOCOMOTIVE_TRACK.get(),
        RailcraftItems.LOCOMOTIVE_TRACK_KIT.get(), RailcraftItems.HIGH_SPEED_ELECTRIC_TRACK.get());
    tracks(consumer, RailcraftItems.HIGH_SPEED_ELECTRIC_THROTTLE_TRACK.get(),
        RailcraftItems.THROTTLE_TRACK_KIT.get(), RailcraftItems.HIGH_SPEED_ELECTRIC_TRACK.get());
    tracks(consumer, RailcraftItems.HIGH_SPEED_ELECTRIC_TRANSITION_TRACK.get(),
        RailcraftItems.TRANSITION_TRACK_KIT.get(), RailcraftItems.HIGH_SPEED_ELECTRIC_TRACK.get());

    wyeTracks(consumer, RailcraftItems.STRAP_IRON_WYE_TRACK.get(),
        RailcraftItems.WOODEN_RAIL.get(), RailcraftItems.WOODEN_RAILBED.get());
    wyeTracks(consumer, RailcraftItems.ABANDONED_WYE_TRACK.get(),
        RailcraftItems.STANDARD_RAIL.get(), RailcraftItems.WOODEN_TIE.get());
    wyeTracks(consumer, RailcraftItems.IRON_WYE_TRACK.get(),
        RailcraftItems.STANDARD_RAIL.get(), RailcraftItems.WOODEN_RAILBED.get());
    wyeTracks(consumer, RailcraftItems.REINFORCED_WYE_TRACK.get(),
        RailcraftItems.REINFORCED_RAIL.get(), RailcraftItems.STONE_RAILBED.get());
    wyeTracks(consumer, RailcraftItems.ELECTRIC_WYE_TRACK.get(),
        RailcraftItems.ELECTRIC_RAIL.get(), RailcraftItems.STONE_RAILBED.get());
    wyeTracks(consumer, RailcraftItems.HIGH_SPEED_WYE_TRACK.get(),
        RailcraftItems.HIGH_SPEED_RAIL.get(), RailcraftItems.STONE_RAILBED.get());

    turnoutTracks(consumer, RailcraftItems.STRAP_IRON_TURNOUT_TRACK.get(),
        RailcraftItems.WOODEN_RAIL.get(), RailcraftItems.WOODEN_RAILBED.get());
    turnoutTracks(consumer, RailcraftItems.ABANDONED_TURNOUT_TRACK.get(),
        RailcraftItems.STANDARD_RAIL.get(), RailcraftItems.WOODEN_TIE.get());
    turnoutTracks(consumer, RailcraftItems.IRON_TURNOUT_TRACK.get(),
        RailcraftItems.STANDARD_RAIL.get(), RailcraftItems.WOODEN_RAILBED.get());
    turnoutTracks(consumer, RailcraftItems.REINFORCED_TURNOUT_TRACK.get(),
        RailcraftItems.REINFORCED_RAIL.get(), RailcraftItems.STONE_RAILBED.get());
    turnoutTracks(consumer, RailcraftItems.ELECTRIC_TURNOUT_TRACK.get(),
        RailcraftItems.ELECTRIC_RAIL.get(), RailcraftItems.STONE_RAILBED.get());
    turnoutTracks(consumer, RailcraftItems.HIGH_SPEED_TURNOUT_TRACK.get(),
        RailcraftItems.HIGH_SPEED_RAIL.get(), RailcraftItems.STONE_RAILBED.get());

    junctionTracks(consumer, RailcraftItems.STRAP_IRON_JUNCTION_TRACK.get(),
        RailcraftItems.WOODEN_RAIL.get(), RailcraftItems.WOODEN_RAILBED.get());
    junctionTracks(consumer, RailcraftItems.ABANDONED_JUNCTION_TRACK.get(),
        RailcraftItems.STANDARD_RAIL.get(), RailcraftItems.WOODEN_TIE.get());
    junctionTracks(consumer, RailcraftItems.IRON_JUNCTION_TRACK.get(),
        RailcraftItems.STANDARD_RAIL.get(), RailcraftItems.WOODEN_RAILBED.get());
    junctionTracks(consumer, RailcraftItems.REINFORCED_JUNCTION_TRACK.get(),
        RailcraftItems.REINFORCED_RAIL.get(), RailcraftItems.STONE_RAILBED.get());
    junctionTracks(consumer, RailcraftItems.ELECTRIC_JUNCTION_TRACK.get(),
        RailcraftItems.ELECTRIC_RAIL.get(), RailcraftItems.STONE_RAILBED.get());
    junctionTracks(consumer, RailcraftItems.HIGH_SPEED_JUNCTION_TRACK.get(),
        RailcraftItems.HIGH_SPEED_RAIL.get(), RailcraftItems.STONE_RAILBED.get());


    ShapedRecipeBuilder
        .shaped(RecipeCategory.MISC, RailcraftItems.HIGH_SPEED_ELECTRIC_WYE_TRACK.get(), 16)
        .pattern("aba")
        .pattern("aac")
        .pattern("aba")
        .define('a', RailcraftItems.HIGH_SPEED_RAIL.get())
        .define('b', RailcraftItems.ELECTRIC_RAIL.get())
        .define('c', RailcraftItems.STONE_RAILBED.get())
        .unlockedBy(getHasName(RailcraftItems.HIGH_SPEED_RAIL.get()),
            has(RailcraftItems.HIGH_SPEED_RAIL.get()))
        .save(consumer);
    ShapedRecipeBuilder
        .shaped(RecipeCategory.MISC, RailcraftItems.HIGH_SPEED_ELECTRIC_TURNOUT_TRACK.get(), 16)
        .pattern("aca")
        .pattern("aba")
        .pattern("aba")
        .define('a', RailcraftItems.HIGH_SPEED_RAIL.get())
        .define('b', RailcraftItems.ELECTRIC_RAIL.get())
        .define('c', RailcraftItems.STONE_RAILBED.get())
        .unlockedBy(getHasName(RailcraftItems.HIGH_SPEED_RAIL.get()),
            has(RailcraftItems.HIGH_SPEED_RAIL.get()))
        .save(consumer);
    ShapedRecipeBuilder
        .shaped(RecipeCategory.MISC, RailcraftItems.HIGH_SPEED_ELECTRIC_JUNCTION_TRACK.get(), 16)
        .pattern("aba")
        .pattern("bcb")
        .pattern("aba")
        .define('a', RailcraftItems.HIGH_SPEED_RAIL.get())
        .define('b', RailcraftItems.ELECTRIC_RAIL.get())
        .define('c', RailcraftItems.STONE_RAILBED.get())
        .unlockedBy(getHasName(RailcraftItems.HIGH_SPEED_RAIL.get()),
            has(RailcraftItems.HIGH_SPEED_RAIL.get()))
        .save(consumer);
  }

  private static void tracks(Consumer<FinishedRecipe> finishedRecipe, Item result,
      Item kit, Item baseTrack) {
    ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, result)
        .requires(kit)
        .requires(baseTrack)
        .unlockedBy(getHasName(kit), has(kit))
        .save(finishedRecipe);
  }

  private static void wyeTracks(Consumer<FinishedRecipe> finishedRecipe, Item result,
      Item rail, Item railBed) {
    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, result, 16)
        .pattern("aaa")
        .pattern("aab")
        .pattern("aaa")
        .define('a', rail)
        .define('b', railBed)
        .unlockedBy(getHasName(rail), has(rail))
        .save(finishedRecipe);
  }

  private static void turnoutTracks(Consumer<FinishedRecipe> finishedRecipe, Item result,
      Item rail, Item railBed) {
    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, result, 16)
        .pattern("aba")
        .pattern("aaa")
        .pattern("aaa")
        .define('a', rail)
        .define('b', railBed)
        .unlockedBy(getHasName(rail), has(rail))
        .save(finishedRecipe);
  }

  private static void junctionTracks(Consumer<FinishedRecipe> finishedRecipe, Item result,
      Item rail, Item railBed) {
    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, result, 16)
        .pattern("aaa")
        .pattern("aba")
        .pattern("aaa")
        .define('a', rail)
        .define('b', railBed)
        .unlockedBy(getHasName(rail), has(rail))
        .save(finishedRecipe);
  }

  private void buildSteelItems(Consumer<FinishedRecipe> consumer) {
    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RailcraftItems.STEEL_ANVIL.get())
        .pattern("aaa")
        .pattern(" b ")
        .pattern("bbb")
        .define('a', RailcraftTags.Items.STEEL_BLOCK)
        .define('b', RailcraftTags.Items.STEEL_INGOT)
        .unlockedBy(getHasName(RailcraftItems.STEEL_BLOCK.get()),
            has(RailcraftTags.Items.STEEL_BLOCK))
        .save(consumer);
    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RailcraftItems.STEEL_SHEARS.get())
        .pattern(" a")
        .pattern("a ")
        .define('a', RailcraftTags.Items.STEEL_INGOT)
        .unlockedBy(getHasName(RailcraftItems.STEEL_INGOT.get()),
            has(RailcraftTags.Items.STEEL_INGOT))
        .save(consumer);
    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RailcraftItems.STEEL_SWORD.get())
        .pattern("a")
        .pattern("a")
        .pattern("b")
        .define('a', RailcraftTags.Items.STEEL_INGOT)
        .define('b', Items.STICK)
        .unlockedBy(getHasName(RailcraftItems.STEEL_INGOT.get()),
            has(RailcraftTags.Items.STEEL_INGOT))
        .save(consumer);
    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RailcraftItems.STEEL_SHOVEL.get())
        .pattern("a")
        .pattern("b")
        .pattern("b")
        .define('a', RailcraftTags.Items.STEEL_INGOT)
        .define('b', Items.STICK)
        .unlockedBy(getHasName(RailcraftItems.STEEL_INGOT.get()),
            has(RailcraftTags.Items.STEEL_INGOT))
        .save(consumer);
    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RailcraftItems.STEEL_PICKAXE.get())
        .pattern("aaa")
        .pattern(" b ")
        .pattern(" b ")
        .define('a', RailcraftTags.Items.STEEL_INGOT)
        .define('b', Items.STICK)
        .unlockedBy(getHasName(RailcraftItems.STEEL_INGOT.get()),
            has(RailcraftTags.Items.STEEL_INGOT))
        .save(consumer);
    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RailcraftItems.STEEL_AXE.get())
        .pattern("aa")
        .pattern("ab")
        .pattern(" b")
        .define('a', RailcraftTags.Items.STEEL_INGOT)
        .define('b', Items.STICK)
        .unlockedBy(getHasName(RailcraftItems.STEEL_INGOT.get()),
            has(RailcraftTags.Items.STEEL_INGOT))
        .save(consumer);
    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RailcraftItems.STEEL_HOE.get())
        .pattern("aa")
        .pattern(" b")
        .pattern(" b")
        .define('a', RailcraftTags.Items.STEEL_INGOT)
        .define('b', Items.STICK)
        .unlockedBy(getHasName(RailcraftItems.STEEL_INGOT.get()),
            has(RailcraftTags.Items.STEEL_INGOT))
        .save(consumer);
    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RailcraftItems.STEEL_BOOTS.get())
        .pattern("a a")
        .pattern("a a")
        .define('a', RailcraftTags.Items.STEEL_INGOT)
        .unlockedBy(getHasName(RailcraftItems.STEEL_INGOT.get()),
            has(RailcraftTags.Items.STEEL_INGOT))
        .save(consumer);
    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RailcraftItems.STEEL_LEGGINGS.get())
        .pattern("aaa")
        .pattern("a a")
        .pattern("a a")
        .define('a', RailcraftTags.Items.STEEL_INGOT)
        .unlockedBy(getHasName(RailcraftItems.STEEL_INGOT.get()),
            has(RailcraftTags.Items.STEEL_INGOT))
        .save(consumer);
    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RailcraftItems.STEEL_CHESTPLATE.get())
        .pattern("a a")
        .pattern("aaa")
        .pattern("aaa")
        .define('a', RailcraftTags.Items.STEEL_INGOT)
        .unlockedBy(getHasName(RailcraftItems.STEEL_INGOT.get()),
            has(RailcraftTags.Items.STEEL_INGOT))
        .save(consumer);
    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RailcraftItems.STEEL_HELMET.get())
        .pattern("aaa")
        .pattern("a a")
        .define('a', RailcraftTags.Items.STEEL_INGOT)
        .unlockedBy(getHasName(RailcraftItems.STEEL_INGOT.get()),
            has(RailcraftTags.Items.STEEL_INGOT))
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
    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, result)
        .pattern("aaa")
        .pattern("aba")
        .pattern("aaa")
        .define('a', RailcraftTags.Items.STEEL_INGOT)
        .define('b', center)
        .unlockedBy(getHasName(RailcraftItems.STEEL_INGOT.get()),
            has(RailcraftTags.Items.STEEL_INGOT))
        .save(consumer);
  }

  private void buildMaul(Consumer<FinishedRecipe> consumer) {
    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RailcraftItems.IRON_SPIKE_MAUL.get())
        .pattern("aca")
        .pattern(" b ")
        .pattern(" b ")
        .define('a', Tags.Items.INGOTS_IRON)
        .define('b', Items.STICK)
        .define('c', Tags.Items.STORAGE_BLOCKS_IRON)
        .unlockedBy(getHasName(Items.IRON_BLOCK), has(Tags.Items.STORAGE_BLOCKS_IRON))
        .save(consumer);
    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RailcraftItems.STEEL_SPIKE_MAUL.get())
        .pattern("aca")
        .pattern(" b ")
        .pattern(" b ")
        .define('a', RailcraftTags.Items.STEEL_INGOT)
        .define('b', Items.STICK)
        .define('c', RailcraftTags.Items.STEEL_BLOCK)
        .unlockedBy(getHasName(RailcraftItems.STEEL_BLOCK.get()),
            has(RailcraftTags.Items.STEEL_BLOCK))
        .save(consumer);
    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RailcraftItems.DIAMOND_SPIKE_MAUL.get())
        .pattern("aca")
        .pattern(" b ")
        .pattern(" b ")
        .define('a', Tags.Items.GEMS_DIAMOND)
        .define('b', Items.STICK)
        .define('c', Tags.Items.STORAGE_BLOCKS_DIAMOND)
        .unlockedBy(getHasName(Items.DIAMOND_BLOCK), has(Tags.Items.STORAGE_BLOCKS_DIAMOND))
        .save(consumer);
  }

  private void buildOreSmelt(Consumer<FinishedRecipe> consumer) {
    List<ItemLike> leadSmeltables = List.of(
        RailcraftItems.LEAD_ORE.get(),
        RailcraftItems.DEEPSLATE_LEAD_ORE.get(),
        RailcraftItems.LEAD_RAW.get());
    oreSmelting(consumer, leadSmeltables, RecipeCategory.MISC, RailcraftItems.LEAD_INGOT.get(),
        1, 200, "lead_ingot");
    oreBlasting(consumer, leadSmeltables, RecipeCategory.MISC, RailcraftItems.LEAD_INGOT.get(),
        1, 100, "lead_ingot");

    List<ItemLike> nickelSmeltables =
        List.of(
            RailcraftItems.NICKEL_ORE.get(),
            RailcraftItems.DEEPSLATE_NICKEL_ORE.get(),
            RailcraftItems.NICKEL_RAW.get());
    oreSmelting(consumer, nickelSmeltables, RecipeCategory.MISC, RailcraftItems.NICKEL_INGOT.get(),
        1, 200, "nickel_ingot");
    oreBlasting(consumer, nickelSmeltables, RecipeCategory.MISC, RailcraftItems.NICKEL_INGOT.get(),
        1, 100, "nickel_ingot");

    List<ItemLike> silverSmeltables =
        List.of(
            RailcraftItems.SILVER_ORE.get(),
            RailcraftItems.DEEPSLATE_SILVER_ORE.get(),
            RailcraftItems.SILVER_RAW.get());
    oreSmelting(consumer, silverSmeltables, RecipeCategory.MISC, RailcraftItems.SILVER_INGOT.get(),
        1, 200, "silver_ingot");
    oreBlasting(consumer, silverSmeltables, RecipeCategory.MISC, RailcraftItems.SILVER_INGOT.get(),
        1, 100, "silver_ingot");

    List<ItemLike> tinSmeltables =
        List.of(
            RailcraftItems.TIN_ORE.get(),
            RailcraftItems.DEEPSLATE_TIN_ORE.get(),
            RailcraftItems.TIN_RAW.get());
    oreSmelting(consumer, tinSmeltables, RecipeCategory.MISC, RailcraftItems.TIN_INGOT.get(),
        1, 200, "tin_ingot");
    oreBlasting(consumer, tinSmeltables, RecipeCategory.MISC, RailcraftItems.TIN_INGOT.get(),
        1, 100, "tin_ingot");

    List<ItemLike> zincSmeltables =
        List.of(
            RailcraftItems.ZINC_ORE.get(),
            RailcraftItems.DEEPSLATE_ZINC_ORE.get(),
            RailcraftItems.ZINC_RAW.get());
    oreSmelting(consumer, zincSmeltables, RecipeCategory.MISC, RailcraftItems.ZINC_INGOT.get(),
        1, 200, "zinc_ingot");
    oreBlasting(consumer, zincSmeltables, RecipeCategory.MISC, RailcraftItems.ZINC_INGOT.get(),
        1, 100, "zinc_ingot");
  }

  private void buildTurbineParts(Consumer<FinishedRecipe> consumer) {
    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RailcraftItems.TURBINE_DISK.get())
        .pattern("aaa")
        .pattern("aba")
        .pattern("aaa")
        .define('a', RailcraftItems.TURBINE_BLADE.get())
        .define('b', RailcraftTags.Items.STEEL_INGOT)
        .unlockedBy(getHasName(RailcraftItems.TURBINE_BLADE.get()),
            has(RailcraftItems.TURBINE_BLADE.get()))
        .save(consumer);

    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RailcraftItems.TURBINE_ROTOR.get())
        .pattern("aaa")
        .define('a', RailcraftItems.TURBINE_DISK.get())
        .unlockedBy(getHasName(RailcraftItems.TURBINE_DISK.get()),
            has(RailcraftItems.TURBINE_DISK.get()))
        .save(consumer);

    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RailcraftItems.STEAM_TURBINE.get())
        .pattern("aba")
        .pattern("bcb")
        .pattern("aba")
        .define('a', RailcraftTags.Items.STEEL_BLOCK)
        .define('b', RailcraftTags.Items.STEEL_PLATE)
        .define('c', RailcraftItems.CHARGE_MOTOR.get())
        .unlockedBy(getHasName(RailcraftItems.CHARGE_MOTOR.get()),
            has(RailcraftItems.CHARGE_MOTOR.get()))
        .save(consumer);
    RailcraftSpecialRecipeBuilder.special(RailcraftRecipeSerializers.ROTOR_REPAIR.get())
        .save(consumer, "rotor_repair");
  }

  private void buildSignalBox(Consumer<FinishedRecipe> consumer) {
    signalBox(consumer, RailcraftItems.SIGNAL_CONTROLLER_BOX.get(),
        RailcraftItems.CONTROLLER_CIRCUIT.get(), Items.REDSTONE);
    signalBox(consumer, RailcraftItems.SIGNAL_RECEIVER_BOX.get(),
        RailcraftItems.RECEIVER_CIRCUIT.get(), Items.REDSTONE);
    signalBox(consumer, RailcraftItems.ANALOG_SIGNAL_CONTROLLER_BOX.get(),
        RailcraftItems.CONTROLLER_CIRCUIT.get(), Items.COMPARATOR);
    signalBox(consumer, RailcraftItems.SIGNAL_CAPACITOR_BOX.get(),
        Items.REPEATER, Items.REDSTONE);
    signalBox(consumer, RailcraftItems.SIGNAL_SEQUENCER_BOX.get(),
        Items.COMPARATOR, Items.REDSTONE);


    var circuit = RailcraftItems.CONTROLLER_CIRCUIT.get();
    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RailcraftItems.SIGNAL_INTERLOCK_BOX.get())
        .pattern(" d ")
        .pattern("aba")
        .pattern("aca")
        .define('a', Items.IRON_INGOT)
        .define('b', circuit)
        .define('c', Items.REDSTONE)
        .define('d', RailcraftItems.RECEIVER_CIRCUIT.get())
        .unlockedBy(getHasName(circuit), has(circuit))
        .save(consumer);
    circuit = RailcraftItems.SIGNAL_CIRCUIT.get();
    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RailcraftItems.SIGNAL_BLOCK_RELAY_BOX.get())
        .pattern(" c ")
        .pattern("aba")
        .pattern("aca")
        .define('a', Items.IRON_INGOT)
        .define('b', circuit)
        .define('c', Items.REDSTONE)
        .unlockedBy(getHasName(circuit), has(circuit))
        .save(consumer);
  }

  private static void signalBox(Consumer<FinishedRecipe> finishedRecipe,
      Item result, Item circuit, Item bottomItem) {
    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, result)
        .pattern("aba")
        .pattern("aca")
        .define('a', Items.IRON_INGOT)
        .define('b', circuit)
        .define('c', bottomItem)
        .unlockedBy(getHasName(circuit), has(circuit))
        .save(finishedRecipe);
  }

  private void buildSignals(Consumer<FinishedRecipe> consumer) {
    singleSignal(consumer, RailcraftItems.BLOCK_SIGNAL.get(), RailcraftItems.SIGNAL_CIRCUIT.get());
    singleSignal(consumer, RailcraftItems.DISTANT_SIGNAL.get(),
        RailcraftItems.RECEIVER_CIRCUIT.get());
    singleSignal(consumer, RailcraftItems.TOKEN_SIGNAL.get(), RailcraftItems.RADIO_CIRCUIT.get());

    dualSignal(consumer, RailcraftItems.DUAL_BLOCK_SIGNAL.get(),
        RailcraftItems.SIGNAL_CIRCUIT.get());
    dualSignal(consumer, RailcraftItems.DUAL_DISTANT_SIGNAL.get(),
        RailcraftItems.RECEIVER_CIRCUIT.get());
    dualSignal(consumer, RailcraftItems.DUAL_TOKEN_SIGNAL.get(),
        RailcraftItems.RADIO_CIRCUIT.get());

    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RailcraftItems.SIGNAL_LAMP.get())
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
        .save(consumer);
  }

  private static void singleSignal(Consumer<FinishedRecipe> finishedRecipe,
      Item result, Item circuit) {
    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, result)
        .pattern("abc")
        .pattern(" dc")
        .define('a', RailcraftItems.SIGNAL_LAMP.get())
        .define('b', circuit)
        .define('c', Items.IRON_INGOT)
        .define('d', Tags.Items.DYES_BLACK)
        .unlockedBy(getHasName(circuit), has(circuit))
        .save(finishedRecipe);
  }

  private static void dualSignal(Consumer<FinishedRecipe> finishedRecipe,
      Item result, Item circuit) {
    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, result)
        .pattern("abc")
        .pattern(" dc")
        .pattern("aec")
        .define('a', RailcraftItems.SIGNAL_LAMP.get())
        .define('b', circuit)
        .define('c', Items.IRON_INGOT)
        .define('d', Tags.Items.DYES_BLACK)
        .define('e', RailcraftItems.RECEIVER_CIRCUIT.get())
        .unlockedBy(getHasName(circuit), has(circuit))
        .save(finishedRecipe);
  }

  private void buildCircuit(Consumer<FinishedRecipe> consumer) {
    circuitFromMaterial(consumer, RailcraftItems.CONTROLLER_CIRCUIT.get(),
        Items.RED_WOOL);
    circuitFromMaterial(consumer, RailcraftItems.RECEIVER_CIRCUIT.get(),
        Items.GREEN_WOOL);
    circuitFromMaterial(consumer, RailcraftItems.SIGNAL_CIRCUIT.get(),
        Items.YELLOW_WOOL);
    circuitFromMaterial(consumer, RailcraftItems.RADIO_CIRCUIT.get(),
        Items.BLUE_WOOL);
  }

  private static void circuitFromMaterial(Consumer<FinishedRecipe> finishedRecipe,
      Item itemOut, Item woolItem) {
    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, itemOut)
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

  private void buildSwitch(Consumer<FinishedRecipe> consumer) {
    switchItem(consumer, RailcraftItems.SWITCH_TRACK_LEVER.get(), Items.LEVER);
    switchItem(consumer, RailcraftItems.SWITCH_TRACK_MOTOR.get(), RailcraftItems.RECEIVER_CIRCUIT.get());
    ShapelessRecipeBuilder
        .shapeless(RecipeCategory.MISC, RailcraftItems.SWITCH_TRACK_ROUTER.get())
        .requires(RailcraftItems.SWITCH_TRACK_MOTOR.get())
        .requires(RailcraftItems.ROUTING_DETECTOR.get())
        .unlockedBy(getHasName(RailcraftItems.SWITCH_TRACK_MOTOR.get()),
            has(RailcraftItems.SWITCH_TRACK_MOTOR.get()))
        .save(consumer);
  }

  private static void switchItem(Consumer<FinishedRecipe> finishedRecipe,
      Item result, Item circuit) {
    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, result)
        .pattern("abc")
        .pattern("def")
        .define('a', Tags.Items.DYES_RED)
        .define('b', Tags.Items.DYES_BLACK)
        .define('c', Items.BONE_MEAL)
        .define('d', Items.PISTON)
        .define('e', circuit)
        .define('f', Items.IRON_INGOT)
        .unlockedBy(getHasName(circuit), has(circuit))
        .save(finishedRecipe);
  }

  private void buildLoaders(Consumer<FinishedRecipe> consumer) {
    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RailcraftItems.ITEM_LOADER.get())
        .pattern("aaa")
        .pattern("aba")
        .pattern("aca")
        .define('a', Items.COBBLESTONE)
        .define('b', Items.HOPPER)
        .define('c', RailcraftItems.ITEM_DETECTOR.get())
        .unlockedBy(getHasName(RailcraftItems.ITEM_DETECTOR.get()),
            has(RailcraftItems.ITEM_DETECTOR.get()))
        .save(consumer);
    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RailcraftItems.ADVANCED_ITEM_LOADER.get())
        .pattern("aba")
        .pattern("bcb")
        .pattern("ada")
        .define('a', RailcraftTags.Items.STEEL_INGOT)
        .define('b', Items.REDSTONE)
        .define('c', RailcraftItems.ITEM_LOADER.get())
        .define('d', RailcraftItems.STEEL_SHOVEL.get())
        .unlockedBy(getHasName(RailcraftItems.ITEM_LOADER.get()),
            has(RailcraftItems.ITEM_LOADER.get()))
        .save(consumer);
    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RailcraftItems.ITEM_UNLOADER.get())
        .pattern("aaa")
        .pattern("aba")
        .pattern("aca")
        .define('a', Items.COBBLESTONE)
        .define('b', RailcraftItems.ITEM_DETECTOR.get())
        .define('c', Items.HOPPER)
        .unlockedBy(getHasName(RailcraftItems.ITEM_DETECTOR.get()),
            has(RailcraftItems.ITEM_DETECTOR.get()))
        .save(consumer);
    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RailcraftItems.ADVANCED_ITEM_UNLOADER.get())
        .pattern("aba")
        .pattern("bcb")
        .pattern("ada")
        .define('a', RailcraftTags.Items.STEEL_INGOT)
        .define('b', Items.REDSTONE)
        .define('c', RailcraftItems.ITEM_UNLOADER.get())
        .define('d', RailcraftItems.STEEL_SHOVEL.get())
        .unlockedBy(getHasName(RailcraftItems.ITEM_UNLOADER.get()),
            has(RailcraftItems.ITEM_UNLOADER.get()))
        .save(consumer);
    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RailcraftItems.FLUID_LOADER.get())
        .pattern("aba")
        .pattern("a a")
        .pattern("aca")
        .define('a', Items.GLASS)
        .define('b', Items.HOPPER)
        .define('c', RailcraftItems.DETECTOR_TRACK_KIT.get())
        .unlockedBy(getHasName(RailcraftItems.DETECTOR_TRACK_KIT.get()),
            has(RailcraftItems.DETECTOR_TRACK_KIT.get()))
        .save(consumer);
    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RailcraftItems.FLUID_UNLOADER.get())
        .pattern("aba")
        .pattern("a a")
        .pattern("aca")
        .define('a', Items.GLASS)
        .define('b', RailcraftItems.DETECTOR_TRACK_KIT.get())
        .define('c', Items.HOPPER)
        .unlockedBy(getHasName(RailcraftItems.DETECTOR_TRACK_KIT.get()),
            has(RailcraftItems.DETECTOR_TRACK_KIT.get()))
        .save(consumer);
    ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, RailcraftItems.CART_DISPENSER.get())
        .requires(Items.DISPENSER)
        .requires(Items.MINECART)
        .unlockedBy(getHasName(Items.DISPENSER),
            has(Items.DISPENSER))
        .unlockedBy(getHasName(Items.MINECART),
            has(Items.MINECART))
        .save(consumer);
    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RailcraftItems.TRAIN_DISPENSER.get())
        .pattern("aba")
        .pattern("bcb")
        .pattern("aba")
        .define('a', Items.REDSTONE)
        .define('b', RailcraftTags.Items.CROWBAR)
        .define('c', RailcraftItems.CART_DISPENSER.get())
        .unlockedBy(getHasName(RailcraftItems.CART_DISPENSER.get()),
            has(RailcraftItems.CART_DISPENSER.get()))
        .save(consumer);
  }

  private void buildCrowbars(Consumer<FinishedRecipe> consumer) {
    crowbar(consumer, RailcraftItems.IRON_CROWBAR.get(),
        Tags.Items.INGOTS_IRON);
    crowbar(consumer, RailcraftItems.STEEL_CROWBAR.get(),
        RailcraftTags.Items.STEEL_INGOT);
    crowbar(consumer, RailcraftItems.DIAMOND_CROWBAR.get(),
        Tags.Items.GEMS_DIAMOND);
  }

  private static void crowbar(Consumer<FinishedRecipe> finishedRecipe, Item itemOut,
      TagKey<Item> materialTag) {
    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, itemOut)
        .pattern(" ba")
        .pattern("bab")
        .pattern("ab ")
        .define('a', materialTag)
        .define('b', Tags.Items.DYES_RED)
        .unlockedBy(getHasName(Items.RAIL), has(Items.RAIL))
        .save(finishedRecipe);
  }

  private void buildFirestones(Consumer<FinishedRecipe> consumer) {
    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RailcraftItems.REFINED_FIRESTONE.get())
        .pattern("LRL")
        .pattern("RSR")
        .pattern("LRL")
        .define('L', Items.LAVA_BUCKET)
        .define('R', Items.REDSTONE_BLOCK)
        .define('S', RailcraftItems.CUT_FIRESTONE.get())
        .unlockedBy(getHasName(RailcraftItems.CUT_FIRESTONE.get()),
            has(RailcraftItems.CUT_FIRESTONE.get()))
        .save(consumer, RailcraftConstants.rl("firestone_lava_refinement"));

    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RailcraftItems.REFINED_FIRESTONE.get())
        .pattern("LFL")
        .pattern("RSR")
        .pattern("LRL")
        .define('L', Items.LAVA_BUCKET)
        .define('R', Items.REDSTONE_BLOCK)
        .define('S', RailcraftItems.CRACKED_FIRESTONE.get())
        .define('F', RailcraftItems.RAW_FIRESTONE.get())
        .unlockedBy(getHasName(RailcraftItems.CRACKED_FIRESTONE.get()),
            has(RailcraftItems.CRACKED_FIRESTONE.get()))
        .save(consumer, RailcraftConstants.rl("firestone_cracked_fixing"));

    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RailcraftItems.CUT_FIRESTONE.get())
        .pattern(" a ")
        .pattern("aba")
        .pattern(" a ")
        .define('a', Items.NETHERITE_PICKAXE)
        .define('b', RailcraftItems.RAW_FIRESTONE.get())
        .unlockedBy(getHasName(RailcraftItems.RAW_FIRESTONE.get()),
            has(RailcraftItems.RAW_FIRESTONE.get()))
        .save(consumer);
  }

  private void buildMiscItems(Consumer<FinishedRecipe> consumer) {
    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RailcraftItems.FEED_STATION.get())
        .pattern("aba")
        .pattern("bcb")
        .pattern("aba")
        .define('a', ItemTags.PLANKS)
        .define('b', Items.GOLDEN_CARROT)
        .define('c', RailcraftTags.Items.STEEL_PLATE)
        .unlockedBy(getHasName(RailcraftItems.STEEL_PLATE.get()),
            has(RailcraftTags.Items.STEEL_PLATE))
        .save(consumer);
    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RailcraftItems.LOGBOOK.get())
        .pattern(" a ")
        .pattern("bcb")
        .pattern("ddd")
        .define('a', Items.WRITABLE_BOOK)
        .define('b', Items.GOLD_INGOT)
        .define('c', Items.RED_WOOL)
        .define('d', ItemTags.PLANKS)
        .unlockedBy(getHasName(Items.WRITABLE_BOOK), has(Items.WRITABLE_BOOK))
        .save(consumer);
    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RailcraftItems.MANUAL_ROLLING_MACHINE.get())
        .pattern("aba")
        .pattern("bcb")
        .pattern("aba")
        .define('a', RailcraftTags.Items.BRONZE_GEAR)
        .define('b', Items.PISTON)
        .define('c', Items.CRAFTING_TABLE)
        .unlockedBy(getHasName(Items.PISTON), has(Items.PISTON))
        .save(consumer);
    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RailcraftItems.POWERED_ROLLING_MACHINE.get())
        .pattern("aba")
        .pattern("bcb")
        .pattern("ada")
        .define('a', RailcraftTags.Items.STEEL_GEAR)
        .define('b', Items.PISTON)
        .define('c', Items.CRAFTING_TABLE)
        .define('d', RailcraftItems.CHARGE_MOTOR.get())
        .unlockedBy(getHasName(RailcraftItems.MANUAL_ROLLING_MACHINE.get()),
            has(RailcraftItems.MANUAL_ROLLING_MACHINE.get()))
        .save(consumer);
    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Items.TORCH, 8)
        .pattern("a")
        .pattern("b")
        .pattern("c")
        .define('a', RailcraftItems.CREOSOTE_BOTTLE.get())
        .define('b', ItemTags.WOOL)
        .define('c', Items.STICK)
        .unlockedBy(getHasName(RailcraftItems.CREOSOTE_BOTTLE.get()),
            has(RailcraftItems.CREOSOTE_BOTTLE.get()))
        .save(consumer, RailcraftConstants.rl("torch_creosote"));
    ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, RailcraftItems.GOLDEN_TICKET.get())
        .requires(Items.PAPER)
        .requires(Tags.Items.NUGGETS_GOLD)
        .unlockedBy(getHasName(Items.PAPER), has(Items.PAPER))
        .save(consumer);
    RailcraftSpecialRecipeBuilder.special(RailcraftRecipeSerializers.TICKET_DUPLICATE.get())
        .save(consumer, getItemName(RailcraftItems.TICKET.get()));
    ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, RailcraftItems.ROUTING_TABLE_BOOK.get())
        .requires(Items.WRITABLE_BOOK)
        .requires(Tags.Items.DYES_BLUE)
        .unlockedBy(getHasName(Items.WRITABLE_BOOK), has(Items.WRITABLE_BOOK))
        .save(consumer);
    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RailcraftItems.OVERALLS.get())
        .pattern("aaa")
        .pattern("a a")
        .pattern("a a")
        .define('a', Items.CYAN_WOOL)
        .unlockedBy(getHasName(Items.CYAN_WOOL), has(Items.CYAN_WOOL))
        .save(consumer);
    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RailcraftItems.WHISTLE_TUNER.get())
        .pattern("a a")
        .pattern("aaa")
        .pattern(" a ")
        .define('a', RailcraftTags.Items.STEEL_NUGGET)
        .unlockedBy(getHasName(RailcraftItems.STEEL_NUGGET.get()),
            has(RailcraftTags.Items.STEEL_NUGGET))
        .save(consumer);
    ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, RailcraftItems.SIGNAL_LABEL.get())
        .requires(Items.PAPER)
        .requires(RailcraftTags.Items.STEEL_NUGGET)
        .unlockedBy(getHasName(RailcraftItems.STEEL_NUGGET.get()),
            has(RailcraftItems.STEEL_NUGGET.get()))
        .save(consumer);
    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RailcraftItems.FORCE_TRACK_EMITTER.get())
        .pattern("aba")
        .pattern("cdc")
        .pattern("aba")
        .define('a', RailcraftTags.Items.TIN_PLATE)
        .define('b', RailcraftItems.ENDER_DUST.get())
        .define('c', RailcraftItems.CHARGE_COIL.get())
        .define('d', Tags.Items.STORAGE_BLOCKS_DIAMOND)
        .unlockedBy(getHasName(RailcraftItems.ENDER_DUST.get()),
            has(RailcraftItems.ENDER_DUST.get()))
        .save(consumer);
    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RailcraftItems.SIGNAL_BLOCK_SURVEYOR.get())
        .pattern(" a ")
        .pattern("cbc")
        .pattern(" d ")
        .define('a', Items.COMPASS)
        .define('b', Tags.Items.GLASS_PANES)
        .define('c', Blocks.STONE_BUTTON)
        .define('d', Tags.Items.DUSTS_REDSTONE)
        .unlockedBy(getHasName(Items.REDSTONE), has(Tags.Items.DUSTS_REDSTONE))
        .save(consumer);
    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RailcraftItems.SIGNAL_TUNER.get())
        .define('a', Items.REDSTONE_TORCH)
        .define('b', RailcraftItems.RECEIVER_CIRCUIT.get())
        .define('c', Blocks.STONE_BUTTON)
        .pattern(" a ")
        .pattern("cbc")
        .unlockedBy(getHasName(Items.REDSTONE), has(Tags.Items.DUSTS_REDSTONE))
        .unlockedBy(getHasName(RailcraftItems.RECEIVER_CIRCUIT.get()),
            has(RailcraftItems.RECEIVER_CIRCUIT.get()))
        .save(consumer);
    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RailcraftItems.GOGGLES.get())
        .pattern("aba")
        .pattern("c c")
        .pattern("ddd")
        .define('a', Tags.Items.GLASS_PANES)
        .define('b', RailcraftItems.RECEIVER_CIRCUIT.get())
        .define('c', RailcraftTags.Items.STEEL_INGOT)
        .define('d', Tags.Items.LEATHER)
        .unlockedBy(getHasName(RailcraftItems.STEEL_INGOT.get()),
            has(RailcraftTags.Items.STEEL_INGOT))
        .unlockedBy(getHasName(RailcraftItems.RECEIVER_CIRCUIT.get()),
            has(RailcraftItems.RECEIVER_CIRCUIT.get()))
        .save(consumer);
    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RailcraftItems.WATER_TANK_SIDING.get(), 6)
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
        .save(consumer);
    ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.GUNPOWDER, 2)
        .requires(RailcraftTags.Items.SALTPETER_DUST)
        .requires(RailcraftTags.Items.SALTPETER_DUST)
        .requires(RailcraftTags.Items.SULFUR_DUST)
        .requires(RailcraftTags.Items.CHARCOAL_DUST)
        .unlockedBy(getHasName(RailcraftItems.SALTPETER_DUST.get()),
            has(RailcraftTags.Items.SALTPETER_DUST))
        .unlockedBy(getHasName(RailcraftItems.SULFUR_DUST.get()),
            has(RailcraftTags.Items.SULFUR_DUST))
        .save(consumer);

    ConditionalRecipe.builder()
        .addCondition(new ModLoadedCondition(PatchouliAPI.MOD_ID))
        .addRecipe(c ->
            SpecialRecipeBuilder.special(RailcraftRecipeSerializers.PATCHOULI_BOOK_CRAFTING.get())
                .save(c, RailcraftConstants.rl("patchouli_book_crafting").toString()))
        .build(consumer, RailcraftConstants.rl("patchouli_book_crafting"));
  }

  private void buildCartsVariant(Consumer<FinishedRecipe> consumer) {
    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RailcraftItems.TANK_MINECART.get())
        .pattern("a")
        .pattern("b")
        .define('a', RailcraftTags.Items.STRENGTHENED_GLASS)
        .define('b', Items.MINECART)
        .unlockedBy(getHasName(RailcraftItems.STRENGTHENED_GLASS.variantFor(DyeColor.WHITE).get()),
            has(RailcraftItems.STRENGTHENED_GLASS.variantFor(DyeColor.WHITE).get()))
        .save(consumer);

    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RailcraftItems.TUNNEL_BORE.get())
        .pattern("aba")
        .pattern("cbc")
        .pattern(" d ")
        .define('a', RailcraftTags.Items.STEEL_BLOCK)
        .define('b', Items.MINECART)
        .define('c', Items.FURNACE)
        .define('d', Items.CHEST_MINECART)
        .unlockedBy(getHasName(Items.MINECART), has(Items.MINECART))
        .save(consumer);

    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RailcraftItems.STEAM_LOCOMOTIVE.get())
        .pattern("aab")
        .pattern("aab")
        .pattern("cdd")
        .define('a', RailcraftTags.Items.IRON_TANK_WALL)
        .define('b', RailcraftItems.BLAST_FURNACE_BRICKS.get())
        .define('c', Items.IRON_BARS)
        .define('d', Items.MINECART)
        .unlockedBy(getHasName(Items.MINECART), has(Items.MINECART))
        .save(consumer);
    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RailcraftItems.ELECTRIC_LOCOMOTIVE.get())
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
        .save(consumer);

    RailcraftSpecialRecipeBuilder.special(RailcraftRecipeSerializers.LOCOMOTIVE_PAINTING.get())
        .save(consumer, "locomotive_color_variant");
    RailcraftSpecialRecipeBuilder.special(RailcraftRecipeSerializers.CART_DISASSEMBLY.get())
        .save(consumer, "cart_disassembly");

    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RailcraftItems.TRACK_LAYER.get())
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
        .save(consumer);

    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RailcraftItems.TRACK_RELAYER.get())
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
        .save(consumer);

    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RailcraftItems.TRACK_REMOVER.get())
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
        .save(consumer);
    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RailcraftItems.TRACK_UNDERCUTTER.get())
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
        .save(consumer);
  }

  private void buildChargeItems(Consumer<FinishedRecipe> consumer) {
    this.conversion(consumer, RailcraftItems.CHARGE_SPOOL_MEDIUM.get(),
        RailcraftItems.CHARGE_SPOOL_SMALL.get(), 3, "charge_spool_small_from_medium");
    this.conversion(consumer, RailcraftItems.CHARGE_SPOOL_LARGE.get(),
        RailcraftItems.CHARGE_SPOOL_MEDIUM.get(), 3, "charge_spool_medium_from_large");

    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RailcraftItems.CHARGE_TERMINAL.get())
        .pattern(" b ")
        .pattern("aaa")
        .define('a', RailcraftTags.Items.BRASS_INGOT)
        .define('b', RailcraftTags.Items.BRASS_PLATE)
        .unlockedBy(getHasName(RailcraftItems.BRASS_INGOT.get()),
            has(RailcraftTags.Items.BRASS_INGOT))
        .save(consumer);

    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RailcraftItems.CHARGE_COIL.get())
        .pattern("aaa")
        .pattern("bbb")
        .pattern("aaa")
        .define('a', RailcraftItems.CHARGE_SPOOL_SMALL.get())
        .define('b', RailcraftTags.Items.IRON_PLATE)
        .unlockedBy(getHasName(RailcraftItems.CHARGE_SPOOL_SMALL.get()),
            has(RailcraftItems.CHARGE_SPOOL_SMALL.get()))
        .save(consumer);

    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RailcraftItems.CHARGE_MOTOR.get())
        .pattern(" a ")
        .pattern("bcb")
        .pattern(" d ")
        .define('a', RailcraftTags.Items.STEEL_INGOT)
        .define('b', RailcraftTags.Items.TIN_PLATE)
        .define('c', RailcraftItems.CHARGE_COIL.get())
        .define('d', RailcraftItems.CHARGE_TERMINAL.get())
        .unlockedBy(getHasName(RailcraftItems.CHARGE_SPOOL_SMALL.get()),
            has(RailcraftItems.CHARGE_SPOOL_SMALL.get()))
        .save(consumer);

    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RailcraftItems.CHARGE_METER.get())
        .pattern("a a")
        .pattern("bcb")
        .pattern(" d ")
        .define('a', Tags.Items.INGOTS_COPPER)
        .define('b', Items.STONE_BUTTON)
        .define('c', Items.GLASS_PANE)
        .define('d', RailcraftTags.Items.BRASS_INGOT)
        .unlockedBy(getHasName(RailcraftItems.BRASS_INGOT.get()),
            has(RailcraftItems.BRASS_INGOT.get()))
        .save(consumer);
  }

  private void buildKits(Consumer<FinishedRecipe> consumer) {
    kits(consumer, RailcraftItems.ACTIVATOR_TRACK_KIT.get(), 8, List.of(
        new Tuple<>(Ingredient.of(Tags.Items.DUSTS_REDSTONE), 2)));
    kits(consumer, RailcraftItems.BOOSTER_TRACK_KIT.get(), 16, List.of(
        new Tuple<>(Ingredient.of(RailcraftItems.ADVANCED_RAIL.get()), 2),
        new Tuple<>(Ingredient.of(Tags.Items.DUSTS_REDSTONE), 1)));
    kits(consumer, RailcraftItems.BUFFER_STOP_TRACK_KIT.get(), 2, List.of(
        new Tuple<>(Ingredient.of(Tags.Items.INGOTS_IRON), 2)));
    kits(consumer, RailcraftItems.CONTROL_TRACK_KIT.get(), 16, List.of(
        new Tuple<>(Ingredient.of(RailcraftItems.ADVANCED_RAIL.get()), 1),
        new Tuple<>(Ingredient.of(Tags.Items.DUSTS_REDSTONE), 1)));
    kits(consumer, RailcraftItems.DETECTOR_TRACK_KIT.get(), 8, List.of(
        new Tuple<>(Ingredient.of(Items.STONE_PRESSURE_PLATE), 1),
        new Tuple<>(Ingredient.of(Tags.Items.DUSTS_REDSTONE), 1)));
    kits(consumer, RailcraftItems.DISEMBARKING_TRACK_KIT.get(), 4, List.of(
        new Tuple<>(Ingredient.of(Items.STONE_PRESSURE_PLATE), 1),
        new Tuple<>(Ingredient.of(Items.LEAD), 1),
        new Tuple<>(Ingredient.of(Tags.Items.DUSTS_REDSTONE), 1)));
    kits(consumer, RailcraftItems.EMBARKING_TRACK_KIT.get(), 4, List.of(
        new Tuple<>(Ingredient.of(Items.ENDER_PEARL), 1),
        new Tuple<>(Ingredient.of(Items.LEAD), 1),
        new Tuple<>(Ingredient.of(Tags.Items.DUSTS_REDSTONE), 1)));
    kits(consumer, RailcraftItems.DUMPING_TRACK_KIT.get(), 4, List.of(
        new Tuple<>(Ingredient.of(RailcraftTags.Items.STEEL_PLATE), 1),
        new Tuple<>(Ingredient.of(Tags.Items.DUSTS_REDSTONE), 1)));
    kits(consumer, RailcraftItems.GATED_TRACK_KIT.get(), 4, List.of(
        new Tuple<>(Ingredient.of(Tags.Items.FENCE_GATES), 1),
        new Tuple<>(Ingredient.of(RailcraftItems.ADVANCED_RAIL.get()), 1),
        new Tuple<>(Ingredient.of(Tags.Items.DUSTS_REDSTONE), 1)));
    kits(consumer, RailcraftItems.LOCKING_TRACK_KIT.get(), 4, List.of(
        new Tuple<>(Ingredient.of(Items.STONE_PRESSURE_PLATE), 1),
        new Tuple<>(Ingredient.of(Items.STICKY_PISTON), 1),
        new Tuple<>(Ingredient.of(Tags.Items.DUSTS_REDSTONE), 1)));
    kits(consumer, RailcraftItems.ONE_WAY_TRACK_KIT.get(), 8, List.of(
        new Tuple<>(Ingredient.of(Items.STONE_PRESSURE_PLATE), 1),
        new Tuple<>(Ingredient.of(Items.PISTON), 1),
        new Tuple<>(Ingredient.of(Tags.Items.DUSTS_REDSTONE), 1)));
    kits(consumer, RailcraftItems.LAUNCHER_TRACK_KIT.get(), 1, List.of(
        new Tuple<>(Ingredient.of(Items.PISTON), 1),
        new Tuple<>(Ingredient.of(RailcraftTags.Items.STEEL_BLOCK), 2),
        new Tuple<>(Ingredient.of(Tags.Items.DUSTS_REDSTONE), 1)));
    kits(consumer, RailcraftItems.LOCOMOTIVE_TRACK_KIT.get(), 4, List.of(
        new Tuple<>(Ingredient.of(RailcraftItems.SIGNAL_LAMP.get()), 1),
        new Tuple<>(Ingredient.of(Tags.Items.DUSTS_REDSTONE), 1)));
    kits(consumer, RailcraftItems.TRANSITION_TRACK_KIT.get(), 8, List.of(
        new Tuple<>(Ingredient.of(RailcraftItems.ADVANCED_RAIL.get()), 2),
        new Tuple<>(Ingredient.of(Tags.Items.DUSTS_REDSTONE), 2)));
    kits(consumer, RailcraftItems.COUPLER_TRACK_KIT.get(), 4, List.of(
        new Tuple<>(Ingredient.of(Items.LEAD), 1),
        new Tuple<>(Ingredient.of(Tags.Items.DUSTS_REDSTONE), 1)));
    kits(consumer, RailcraftItems.ROUTING_TRACK_KIT.get(), 8, List.of(
        new Tuple<>(Ingredient.of(
            RailcraftItems.TICKET.get(), RailcraftItems.GOLDEN_TICKET.get()), 1),
        new Tuple<>(Ingredient.of(Tags.Items.DUSTS_REDSTONE), 1)));
    kits(consumer, RailcraftItems.THROTTLE_TRACK_KIT.get(), 4, List.of(
        new Tuple<>(Ingredient.of(Tags.Items.DYES_YELLOW), 1),
        new Tuple<>(Ingredient.of(Tags.Items.DYES_BLACK), 1),
        new Tuple<>(Ingredient.of(Tags.Items.DUSTS_REDSTONE), 1)));
    kits(consumer, RailcraftItems.WHISTLE_TRACK_KIT.get(), 8, List.of(
        new Tuple<>(Ingredient.of(Tags.Items.DYES_YELLOW), 1),
        new Tuple<>(Ingredient.of(Tags.Items.DYES_BLACK), 1),
        new Tuple<>(Ingredient.of(Items.NOTE_BLOCK), 1),
        new Tuple<>(Ingredient.of(Tags.Items.DUSTS_REDSTONE), 1)));
  }

  private static void kits(Consumer<FinishedRecipe> finishedRecipe,
      Item result, int count, List<Tuple<Ingredient, Integer>> ingredients) {

    var builder = ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, result, count)
        .requires(ItemTags.PLANKS)
        .requires(RailcraftItems.TRACK_PARTS.get());

    for (var ingredient : ingredients) {
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
    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, itemOut)
        .pattern(" a ")
        .pattern("aba")
        .pattern(" a ")
        .define('a', materialTag)
        .define('b', RailcraftItems.BUSHING_GEAR.get())
        .unlockedBy("has_material", has(materialTag))
        .save(finishedRecipe);
  }

  private void buildMultiblockBlocks(Consumer<FinishedRecipe> consumer) {
    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RailcraftItems.FLUID_FUELED_FIREBOX.get())
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
    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RailcraftItems.SOLID_FUELED_FIREBOX.get())
        .pattern("aaa")
        .pattern("aba")
        .pattern("aca")
        .define('a', Items.NETHER_BRICK)
        .define('b', Items.FIRE_CHARGE)
        .define('c', Items.FURNACE)
        .unlockedBy(getHasName(Items.FIRE_CHARGE), has(Items.FIRE_CHARGE))
        .save(consumer);

    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RailcraftItems.COKE_OVEN_BRICKS.get(), 2)
        .pattern("aba")
        .pattern("bcb")
        .pattern("aba")
        .define('a', Items.SAND)
        .define('b', Items.BRICK)
        .define('c', Items.CLAY)
        .unlockedBy(getHasName(Items.BRICK), has(Items.BRICK))
        .save(consumer);
    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RailcraftItems.BLAST_FURNACE_BRICKS.get(), 4)
        .pattern("aba")
        .pattern("bcb")
        .pattern("aba")
        .define('a', Items.SOUL_SAND)
        .define('b', Items.NETHER_BRICK)
        .define('c', Items.MAGMA_CREAM)
        .unlockedBy(getHasName(Items.MAGMA_CREAM), has(Items.MAGMA_CREAM))
        .save(consumer);
    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RailcraftItems.CRUSHER.get(), 4)
        .pattern("aba")
        .pattern("bcb")
        .pattern("ada")
        .define('a', Items.DIAMOND)
        .define('b', Items.PISTON)
        .define('c', RailcraftTags.Items.STEEL_BLOCK)
        .define('d', RailcraftItems.CHARGE_MOTOR.get())
        .unlockedBy(getHasName(RailcraftItems.CHARGE_MOTOR.get()),
            has(RailcraftItems.CHARGE_MOTOR.get()))
        .save(consumer);
    ShapedRecipeBuilder
        .shaped(RecipeCategory.MISC, RailcraftItems.HIGH_PRESSURE_STEAM_BOILER_TANK.get(), 2)
        .pattern("a")
        .pattern("b")
        .pattern("a")
        .define('a', RailcraftTags.Items.STEEL_PLATE)
        .define('b', RailcraftTags.Items.INVAR_PLATE)
        .unlockedBy(getHasName(RailcraftItems.STEEL_PLATE.get()),
            has(RailcraftItems.STEEL_PLATE.get()))
        .save(consumer);
    ShapedRecipeBuilder
        .shaped(RecipeCategory.MISC, RailcraftItems.LOW_PRESSURE_STEAM_BOILER_TANK.get(), 2)
        .pattern("a")
        .pattern("b")
        .pattern("a")
        .define('a', RailcraftTags.Items.IRON_PLATE)
        .define('b', RailcraftTags.Items.INVAR_PLATE)
        .unlockedBy(getHasName(RailcraftItems.IRON_PLATE.get()),
            has(RailcraftItems.IRON_PLATE.get()))
        .save(consumer);
    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RailcraftItems.STEAM_OVEN.get(), 4)
        .pattern("aaa")
        .pattern("aba")
        .pattern("aaa")
        .define('a', RailcraftTags.Items.STEEL_PLATE)
        .define('b', Items.FURNACE)
        .unlockedBy(getHasName(RailcraftItems.STEEL_PLATE.get()),
            has(RailcraftItems.STEEL_PLATE.get()))
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

  private static void nineBlockStorageRecipes(Consumer<FinishedRecipe> consumer,
      ItemLike unpacked, ItemLike packed, String packingRecipeName) {
    ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, unpacked, 9)
        .requires(packed)
        .unlockedBy(getHasName(packed), has(packed))
        .save(consumer);
    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, packed)
        .pattern("aaa")
        .pattern("aaa")
        .pattern("aaa")
        .define('a', unpacked)
        .unlockedBy(getHasName(unpacked), has(unpacked))
        .save(consumer, RailcraftConstants.rl(packingRecipeName));
  }

  private void buildIngotsRecipes(Consumer<FinishedRecipe> consumer) {
    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RailcraftItems.BRONZE_INGOT.get(), 4)
        .pattern("ab")
        .pattern("bb")
        .define('a', RailcraftTags.Items.TIN_INGOT)
        .define('b', Tags.Items.INGOTS_COPPER)
        .unlockedBy(getHasName(RailcraftItems.TIN_INGOT.get()),
            has(RailcraftTags.Items.TIN_INGOT))
        .unlockedBy(getHasName(Items.COPPER_INGOT), has(Tags.Items.INGOTS_COPPER))
        .save(consumer, RailcraftConstants.rl("bronze_ingot_crafted_with_ingots"));
    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RailcraftItems.BRASS_INGOT.get(), 4)
        .pattern("ab")
        .pattern("bb")
        .define('a', RailcraftTags.Items.ZINC_INGOT)
        .define('b', Tags.Items.INGOTS_COPPER)
        .unlockedBy(getHasName(RailcraftItems.ZINC_INGOT.get()),
            has(RailcraftTags.Items.ZINC_INGOT))
        .unlockedBy(getHasName(Items.COPPER_INGOT), has(Tags.Items.INGOTS_COPPER))
        .save(consumer, RailcraftConstants.rl("brass_ingot_crafted_with_ingots"));
    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RailcraftItems.INVAR_INGOT.get(), 3)
        .pattern("ab")
        .pattern("b ")
        .define('a', RailcraftTags.Items.NICKEL_INGOT)
        .define('b', Tags.Items.INGOTS_IRON)
        .unlockedBy(getHasName(RailcraftItems.NICKEL_INGOT.get()),
            has(RailcraftTags.Items.NICKEL_INGOT))
        .unlockedBy(getHasName(Items.IRON_INGOT), has(Tags.Items.INGOTS_IRON))
        .save(consumer, RailcraftConstants.rl("invar_ingot_crafted_with_ingots"));
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
      ShapedRecipeBuilder.shaped(RecipeCategory.MISC, result, 6)
          .pattern("aba")
          .pattern("aca")
          .pattern("ada")
          .define('a', Tags.Items.GLASS)
          .define('b', ingredient.getValue())
          .define('c', RailcraftItems.SALTPETER_DUST.get())
          .define('d', Items.WATER_BUCKET)
          .unlockedBy(getHasName(RailcraftItems.SALTPETER_DUST.get()),
              has(RailcraftItems.SALTPETER_DUST.get()))
          .save(consumer, RailcraftConstants.rl(recipeName));
    }

    coloredBlockVariant(consumer, colorItems, tagItem);
  }

  private void buildTie(Consumer<FinishedRecipe> consumer) {
    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RailcraftItems.WOODEN_TIE.get(), 3)
        .pattern(" a ")
        .pattern("bbb")
        .define('a', RailcraftItems.CREOSOTE_BUCKET.get())
        .define('b', ItemTags.WOODEN_SLABS)
        .unlockedBy(getHasName(RailcraftItems.CREOSOTE_BUCKET.get()),
            has(RailcraftItems.CREOSOTE_BUCKET.get()))
        .save(consumer);
    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RailcraftItems.WOODEN_TIE.get())
        .pattern(" a ")
        .pattern("bbb")
        .define('a', RailcraftItems.CREOSOTE_BOTTLE.get())
        .define('b', ItemTags.WOODEN_SLABS)
        .unlockedBy(getHasName(RailcraftItems.CREOSOTE_BOTTLE.get()),
            has(RailcraftItems.CREOSOTE_BOTTLE.get()))
        .save(consumer, RailcraftConstants.rl("wooden_tie_bottle"));
    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RailcraftItems.STONE_TIE.get())
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
    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RailcraftItems.BAG_OF_CEMENT.get(), 2)
        .pattern("ab")
        .pattern("ba")
        .define('a', Items.GRAVEL)
        .define('b', Items.QUARTZ)
        .unlockedBy(getHasName(Items.QUARTZ), has(Items.QUARTZ))
        .save(consumer);
    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RailcraftItems.BAG_OF_CEMENT.get(), 2)
        .pattern("ab")
        .pattern("ca")
        .define('a', Items.GRAVEL)
        .define('b', Items.QUARTZ)
        .define('c', RailcraftItems.SLAG.get())
        .unlockedBy(getHasName(RailcraftItems.SLAG.get()), has(RailcraftItems.SLAG.get()))
        .save(consumer, RailcraftConstants.rl("bag_of_cement_slag"));
  }

  private static void tankWall(Consumer<FinishedRecipe> consumer,
      TagKey<Item> ingredientTag,
      VariantRegistrar<DyeColor, BlockItem> colorItems,
      TagKey<Item> tagItem) {
    var result = colorItems.variantFor(DyeColor.WHITE).get();
    var name = RecipeBuilder.getDefaultRecipeId(result).getPath();
    var ingredient = ingredientTag.equals(RailcraftTags.Items.IRON_PLATE)
        ? "has_iron_plate" : "has_steel_plate";
    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, result, 8)
        .pattern("aa")
        .pattern("aa")
        .define('a', ingredientTag)
        .unlockedBy(ingredient, has(ingredientTag))
        .save(consumer, RailcraftConstants.rl(name.substring(name.indexOf('_') + 1)));

    coloredBlockVariant(consumer, colorItems, tagItem);
  }

  private static void tankValve(Consumer<FinishedRecipe> consumer,
      TagKey<Item> ingredientTag,
      VariantRegistrar<DyeColor, BlockItem> colorItems,
      TagKey<Item> tagItem) {
    var result = colorItems.variantFor(DyeColor.WHITE).get();
    var name = RecipeBuilder.getDefaultRecipeId(result).getPath();
    var ingredient = ingredientTag.equals(RailcraftTags.Items.IRON_PLATE)
        ? "has_iron_plate" : "has_steel_plate";
    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, result, 8)
        .pattern("aba")
        .pattern("bcb")
        .pattern("aba")
        .define('a', Items.IRON_BARS)
        .define('b', ingredientTag)
        .define('c', Items.LEVER)
        .unlockedBy(ingredient, has(ingredientTag))
        .save(consumer, RailcraftConstants.rl(name.substring(name.indexOf('_') + 1)));

    coloredBlockVariant(consumer, colorItems, tagItem);
  }

  private static void tankGauge(Consumer<FinishedRecipe> consumer,
      TagKey<Item> ingredientTag,
      VariantRegistrar<DyeColor, BlockItem> colorItems,
      TagKey<Item> tagItem) {
    var result = colorItems.variantFor(DyeColor.WHITE).get();
    var name = RecipeBuilder.getDefaultRecipeId(result).getPath();
    var ingredient = ingredientTag.equals(RailcraftTags.Items.IRON_PLATE)
        ? "has_iron_plate" : "has_steel_plate";
    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, result, 8)
        .pattern("aba")
        .pattern("bab")
        .pattern("aba")
        .define('a', Items.GLASS_PANE)
        .define('b', ingredientTag)
        .unlockedBy(ingredient, has(ingredientTag))
        .save(consumer, RailcraftConstants.rl(name.substring(name.indexOf('_') + 1)));

    coloredBlockVariant(consumer, colorItems, tagItem);
  }

  private void buildTankBlocks(Consumer<FinishedRecipe> consumer) {
    tankWall(consumer,
        RailcraftTags.Items.IRON_PLATE,
        RailcraftItems.IRON_TANK_WALL,
        RailcraftTags.Items.IRON_TANK_WALL);
    tankWall(consumer,
        RailcraftTags.Items.STEEL_PLATE,
        RailcraftItems.STEEL_TANK_WALL,
        RailcraftTags.Items.STEEL_TANK_WALL);
    tankValve(consumer,
        RailcraftTags.Items.IRON_PLATE,
        RailcraftItems.IRON_TANK_VALVE,
        RailcraftTags.Items.IRON_TANK_VALVE);
    tankValve(consumer,
        RailcraftTags.Items.STEEL_PLATE,
        RailcraftItems.STEEL_TANK_VALVE,
        RailcraftTags.Items.STEEL_TANK_VALVE);
    tankGauge(consumer,
        RailcraftTags.Items.IRON_PLATE,
        RailcraftItems.IRON_TANK_GAUGE,
        RailcraftTags.Items.IRON_TANK_GAUGE);
    tankGauge(consumer,
        RailcraftTags.Items.STEEL_PLATE,
        RailcraftItems.STEEL_TANK_GAUGE,
        RailcraftTags.Items.STEEL_TANK_GAUGE);
  }

  private void buildPost(Consumer<FinishedRecipe> consumer) {
    coloredBlockVariant(consumer, RailcraftItems.POST, RailcraftTags.Items.POST, DyeColor.BLACK);
  }

  private void buildQuarriedStone(Consumer<FinishedRecipe> consumer) {
    square2x2(consumer, RailcraftItems.QUARRIED_STONE.get(),
        RailcraftItems.POLISHED_QUARRIED_STONE.get(), 4, "_from_quarried_stone");
    square2x2(consumer, RailcraftItems.QUARRIED_COBBLESTONE.get(),
        RailcraftItems.POLISHED_QUARRIED_STONE.get(), 4, "_from_quarried_cobblestone");
    SingleItemRecipeBuilder.stonecutting(
        Ingredient.of(new ItemStack(RailcraftItems.QUARRIED_STONE.get())),
        RecipeCategory.MISC, RailcraftItems.POLISHED_QUARRIED_STONE.get())
        .unlockedBy(getHasName(RailcraftItems.QUARRIED_STONE.get()),
            has(RailcraftItems.QUARRIED_STONE.get()))
        .save(consumer,
            RailcraftConstants.rl("polished_quarried_stone_from_quarried_stone_in_stonecutter"));
    SingleItemRecipeBuilder.stonecutting(
        Ingredient.of(new ItemStack(RailcraftItems.QUARRIED_COBBLESTONE.get())),
        RecipeCategory.MISC, RailcraftItems.POLISHED_QUARRIED_STONE.get())
        .unlockedBy(getHasName(RailcraftItems.QUARRIED_COBBLESTONE.get()),
            has(RailcraftItems.QUARRIED_COBBLESTONE.get()))
        .save(consumer,
            RailcraftConstants.rl("polished_quarried_stone_from_quarried_cobblestone_in_stonecutter"));
    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RailcraftItems.CHISELED_QUARRIED_STONE.get(), 8)
        .pattern("aaa")
        .pattern("a a")
        .pattern("aaa")
        .define('a', RailcraftItems.POLISHED_QUARRIED_STONE.get())
        .unlockedBy(getHasName(RailcraftItems.POLISHED_QUARRIED_STONE.get()),
            has(RailcraftItems.POLISHED_QUARRIED_STONE.get()))
        .save(consumer);
    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RailcraftItems.ETCHED_QUARRIED_STONE.get(), 8)
        .pattern("aaa")
        .pattern("aba")
        .pattern("aaa")
        .define('a', RailcraftItems.POLISHED_QUARRIED_STONE.get())
        .define('b', Items.GUNPOWDER)
        .unlockedBy(getHasName(RailcraftItems.POLISHED_QUARRIED_STONE.get()),
            has(RailcraftItems.POLISHED_QUARRIED_STONE.get()))
        .save(consumer);
    square2x2(consumer, RailcraftItems.POLISHED_QUARRIED_STONE.get(),
        RailcraftItems.QUARRIED_BRICKS.get(), 4, "");
    square2x2(consumer, RailcraftItems.QUARRIED_BRICKS.get(),
        RailcraftItems.QUARRIED_PAVER.get(), 4, "");

    stairBuilder(RailcraftItems.QUARRIED_BRICK_STAIRS.get(),
        Ingredient.of(RailcraftItems.QUARRIED_BRICKS.get()))
            .unlockedBy(getHasName(RailcraftItems.QUARRIED_BRICKS.get()),
                has(RailcraftItems.QUARRIED_BRICKS.get()))
            .save(consumer);
    stairBuilder(RailcraftItems.QUARRIED_PAVER_STAIRS.get(),
        Ingredient.of(RailcraftItems.QUARRIED_PAVER.get()))
            .unlockedBy(getHasName(RailcraftItems.QUARRIED_PAVER.get()),
                has(RailcraftItems.QUARRIED_PAVER.get()))
            .save(consumer);
    slab(consumer, RecipeCategory.MISC,
        RailcraftItems.QUARRIED_BRICK_SLAB.get(), RailcraftItems.QUARRIED_BRICKS.get());
    slab(consumer, RecipeCategory.MISC,
        RailcraftItems.QUARRIED_PAVER_SLAB.get(), RailcraftItems.QUARRIED_PAVER.get());
  }

  private void buildAbyssalStone(Consumer<FinishedRecipe> consumer) {
    square2x2(consumer, RailcraftItems.ABYSSAL_STONE.get(),
        RailcraftItems.POLISHED_ABYSSAL_STONE.get(), 4, "_from_abyssal_stone");
    square2x2(consumer, RailcraftItems.ABYSSAL_COBBLESTONE.get(),
        RailcraftItems.POLISHED_ABYSSAL_STONE.get(), 4, "_from_abyssal_cobblestone");
    SingleItemRecipeBuilder.stonecutting(
            Ingredient.of(new ItemStack(RailcraftItems.ABYSSAL_STONE.get())),
            RecipeCategory.MISC, RailcraftItems.POLISHED_ABYSSAL_STONE.get())
        .unlockedBy(getHasName(RailcraftItems.ABYSSAL_STONE.get()),
            has(RailcraftItems.ABYSSAL_STONE.get()))
        .save(consumer,
            RailcraftConstants.rl("polished_abyssal_stone_from_abyssal_stone_in_stonecutter"));
    SingleItemRecipeBuilder.stonecutting(
            Ingredient.of(new ItemStack(RailcraftItems.ABYSSAL_COBBLESTONE.get())),
            RecipeCategory.MISC, RailcraftItems.POLISHED_ABYSSAL_STONE.get())
        .unlockedBy(getHasName(RailcraftItems.ABYSSAL_COBBLESTONE.get()),
            has(RailcraftItems.ABYSSAL_COBBLESTONE.get()))
        .save(consumer,
            RailcraftConstants.rl("polished_abyssal_stone_from_abyssal_cobblestone_in_stonecutter"));
    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RailcraftItems.CHISELED_ABYSSAL_STONE.get(), 8)
        .pattern("aaa")
        .pattern("a a")
        .pattern("aaa")
        .define('a', RailcraftItems.POLISHED_ABYSSAL_STONE.get())
        .unlockedBy(getHasName(RailcraftItems.POLISHED_ABYSSAL_STONE.get()),
            has(RailcraftItems.POLISHED_ABYSSAL_STONE.get()))
        .save(consumer);
    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RailcraftItems.ETCHED_ABYSSAL_STONE.get(), 8)
        .pattern("aaa")
        .pattern("aba")
        .pattern("aaa")
        .define('a', RailcraftItems.POLISHED_ABYSSAL_STONE.get())
        .define('b', Items.GUNPOWDER)
        .unlockedBy(getHasName(RailcraftItems.POLISHED_ABYSSAL_STONE.get()),
            has(RailcraftItems.POLISHED_ABYSSAL_STONE.get()))
        .save(consumer);
    square2x2(consumer, RailcraftItems.POLISHED_ABYSSAL_STONE.get(),
        RailcraftItems.ABYSSAL_BRICKS.get(), 4, "");
    square2x2(consumer, RailcraftItems.ABYSSAL_BRICKS.get(),
        RailcraftItems.ABYSSAL_PAVER.get(), 4, "");

    stairBuilder(RailcraftItems.ABYSSAL_BRICK_STAIRS.get(),
        Ingredient.of(RailcraftItems.ABYSSAL_BRICKS.get()))
        .unlockedBy(getHasName(RailcraftItems.ABYSSAL_BRICKS.get()),
            has(RailcraftItems.ABYSSAL_BRICKS.get()))
        .save(consumer);
    stairBuilder(RailcraftItems.ABYSSAL_PAVER_STAIRS.get(),
        Ingredient.of(RailcraftItems.ABYSSAL_PAVER.get()))
        .unlockedBy(getHasName(RailcraftItems.ABYSSAL_PAVER.get()),
            has(RailcraftItems.ABYSSAL_PAVER.get()))
        .save(consumer);
    slab(consumer, RecipeCategory.MISC,
        RailcraftItems.ABYSSAL_BRICK_SLAB.get(), RailcraftItems.ABYSSAL_BRICKS.get());
    slab(consumer, RecipeCategory.MISC,
        RailcraftItems.ABYSSAL_PAVER_SLAB.get(), RailcraftItems.ABYSSAL_PAVER.get());
  }

  private void buildBattery(Consumer<FinishedRecipe> consumer) {
    battery(consumer, RailcraftItems.NICKEL_ZINC_BATTERY.get(),
        RailcraftItems.NICKEL_ELECTRODE.get(), RailcraftItems.ZINC_ELECTRODE.get());
    battery(consumer, RailcraftItems.NICKEL_IRON_BATTERY.get(),
        RailcraftItems.NICKEL_ELECTRODE.get(), RailcraftItems.IRON_ELECTRODE.get());
    battery(consumer, RailcraftItems.ZINC_SILVER_BATTERY.get(),
        RailcraftItems.ZINC_ELECTRODE.get(), RailcraftItems.SILVER_ELECTRODE.get());
    battery(consumer, RailcraftItems.ZINC_CARBON_BATTERY.get(),
        RailcraftItems.ZINC_ELECTRODE.get(), RailcraftItems.CARBON_ELECTRODE.get());
  }

  private static void battery(Consumer<FinishedRecipe> consumer, Item result,
      Item left, Item right) {
    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, result)
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
        .save(consumer);
  }

  private void buildFrame(Consumer<FinishedRecipe> consumer) {
    frame(6, RailcraftTags.Items.IRON_PLATE, "_iron_plate", consumer);
    frame(6, RailcraftTags.Items.BRONZE_PLATE, "_bronze_plate", consumer);
    frame(6, RailcraftTags.Items.BRASS_PLATE, "_brass_plate", consumer);
    frame(10, RailcraftTags.Items.STEEL_PLATE, "_steel_plate", consumer);
  }

  private void frame(int count, TagKey<Item> tag, String suffix,
      Consumer<FinishedRecipe> consumer) {
    var name = RecipeBuilder.getDefaultRecipeId(RailcraftItems.FRAME_BLOCK.get()).getPath();
    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RailcraftItems.FRAME_BLOCK.get(), count)
        .pattern("aaa")
        .pattern("b b")
        .pattern("bbb")
        .define('a', tag)
        .define('b', RailcraftItems.REBAR.get())
        .unlockedBy(getHasName(RailcraftItems.REBAR.get()), has(RailcraftItems.REBAR.get()))
        .save(consumer, RailcraftConstants.rl(name + suffix));
  }

  private void buildDetectors(Consumer<FinishedRecipe> consumer) {
    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RailcraftItems.ADVANCED_DETECTOR.get())
        .pattern("aaa")
        .pattern("aba")
        .pattern("aaa")
        .define('a', RailcraftTags.Items.STEEL_INGOT)
        .define('b', Items.STONE_PRESSURE_PLATE)
        .unlockedBy(getHasName(RailcraftItems.STEEL_INGOT.get()),
            has(RailcraftItems.STEEL_INGOT.get()))
        .save(consumer);
    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RailcraftItems.AGE_DETECTOR.get())
        .pattern("aaa")
        .pattern("aba")
        .pattern("aaa")
        .define('a', Items.DARK_OAK_LOG)
        .define('b', Items.STONE_PRESSURE_PLATE)
        .unlockedBy(getHasName(Items.SPRUCE_LOG), has(Items.SPRUCE_LOG))
        .save(consumer);
    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RailcraftItems.ANIMAL_DETECTOR.get())
        .pattern("aaa")
        .pattern("aba")
        .pattern("aaa")
        .define('a', Items.OAK_LOG)
        .define('b', Items.STONE_PRESSURE_PLATE)
        .unlockedBy(getHasName(Items.SPRUCE_LOG), has(Items.SPRUCE_LOG))
        .save(consumer);
    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RailcraftItems.ANY_DETECTOR.get())
        .pattern("aaa")
        .pattern("aba")
        .pattern("aaa")
        .define('a', Items.STONE)
        .define('b', Items.STONE_PRESSURE_PLATE)
        .unlockedBy(getHasName(Items.STONE), has(Items.STONE))
        .save(consumer);
    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RailcraftItems.EMPTY_DETECTOR.get())
        .pattern("aaa")
        .pattern("aba")
        .pattern("aaa")
        .define('a', Items.STONE_BRICKS)
        .define('b', Items.STONE_PRESSURE_PLATE)
        .unlockedBy(getHasName(Items.STONE_BRICKS), has(Items.STONE_BRICKS))
        .save(consumer);
    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RailcraftItems.ITEM_DETECTOR.get())
        .pattern("aaa")
        .pattern("aba")
        .pattern("aaa")
        .define('a', Items.STRIPPED_ACACIA_WOOD)
        .define('b', Items.STONE_PRESSURE_PLATE)
        .unlockedBy(getHasName(Items.STRIPPED_ACACIA_WOOD), has(Items.STRIPPED_ACACIA_WOOD))
        .save(consumer);
    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RailcraftItems.LOCOMOTIVE_DETECTOR.get())
        .pattern("aaa")
        .pattern("aba")
        .pattern("aaa")
        .define('a', RailcraftItems.BLAST_FURNACE_BRICKS.get())
        .define('b', Items.STONE_PRESSURE_PLATE)
        .unlockedBy(getHasName(RailcraftItems.BLAST_FURNACE_BRICKS.get()),
            has(RailcraftItems.BLAST_FURNACE_BRICKS.get()))
        .save(consumer);
    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RailcraftItems.MOB_DETECTOR.get())
        .pattern("aaa")
        .pattern("aba")
        .pattern("aaa")
        .define('a', Items.MOSSY_COBBLESTONE)
        .define('b', Items.STONE_PRESSURE_PLATE)
        .unlockedBy(getHasName(Items.MOSSY_COBBLESTONE), has(Items.MOSSY_COBBLESTONE))
        .save(consumer);
    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RailcraftItems.PLAYER_DETECTOR.get())
        .pattern("aaa")
        .pattern("aba")
        .pattern("aaa")
        .define('a', Items.STONE_SLAB)
        .define('b', Items.STONE_PRESSURE_PLATE)
        .unlockedBy(getHasName(Items.STONE_SLAB), has(Items.STONE_SLAB))
        .save(consumer);
    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RailcraftItems.ROUTING_DETECTOR.get())
        .pattern("aaa")
        .pattern("aba")
        .pattern("aaa")
        .define('a', Items.CHISELED_QUARTZ_BLOCK)
        .define('b', Items.STONE_PRESSURE_PLATE)
        .unlockedBy(getHasName(Items.CHISELED_QUARTZ_BLOCK), has(Items.CHISELED_QUARTZ_BLOCK))
        .save(consumer);
    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RailcraftItems.SHEEP_DETECTOR.get())
        .pattern("aaa")
        .pattern("aba")
        .pattern("aaa")
        .define('a', ItemTags.WOOL)
        .define('b', Items.STONE_PRESSURE_PLATE)
        .unlockedBy(getHasName(Items.WHITE_WOOL), has(Items.WHITE_WOOL))
        .save(consumer);
    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RailcraftItems.TANK_DETECTOR.get())
        .pattern("aaa")
        .pattern("aba")
        .pattern("aaa")
        .define('a', Items.BRICK)
        .define('b', Items.STONE_PRESSURE_PLATE)
        .unlockedBy(getHasName(Items.BRICK), has(Items.BRICK))
        .save(consumer);
    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RailcraftItems.TRAIN_DETECTOR.get())
        .pattern("aaa")
        .pattern("aba")
        .pattern("aaa")
        .define('a', Items.NETHER_BRICK)
        .define('b', Items.STONE_PRESSURE_PLATE)
        .unlockedBy(getHasName(Items.NETHER_BRICK), has(Items.NETHER_BRICK))
        .save(consumer);
    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RailcraftItems.VILLAGER_DETECTOR.get())
        .pattern("aaa")
        .pattern("aba")
        .pattern("aaa")
        .define('a', Items.LEATHER)
        .define('b', Items.STONE_PRESSURE_PLATE)
        .unlockedBy(getHasName(Items.LEATHER), has(Items.LEATHER))
        .save(consumer);
  }

  private static void buildWorldSpike(Consumer<FinishedRecipe> consumer) {
    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RailcraftItems.WORLD_SPIKE.get())
        .pattern("gog")
        .pattern("dpd")
        .pattern("gog")
        .define('d', Tags.Items.GEMS_DIAMOND)
        .define('g', Tags.Items.INGOTS_GOLD)
        .define('p', Items.ENDER_PEARL)
        .define('o', Items.OBSIDIAN)
        .unlockedBy("has_material", has(Items.ENDER_PEARL))
        .save(consumer);
    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RailcraftItems.PERSONAL_WORLD_SPIKE.get())
        .pattern("gog")
        .pattern("dpd")
        .pattern("gog")
        .define('d', Tags.Items.GEMS_EMERALD)
        .define('g', Tags.Items.INGOTS_GOLD)
        .define('p', Items.ENDER_PEARL)
        .define('o', Items.OBSIDIAN)
        .unlockedBy("has_material", has(Items.ENDER_PEARL))
        .save(consumer);
  }

  private static void square2x2(Consumer<FinishedRecipe> finishedRecipe,
    TagKey<Item> ingredient, Item result, int quantity, String suffix) {
  var name = RecipeBuilder.getDefaultRecipeId(result).getPath();
  ShapedRecipeBuilder.shaped(RecipeCategory.MISC, result, quantity)
      .pattern("aa")
      .pattern("aa")
      .define('a', ingredient)
      .unlockedBy("has_material", has(ingredient))
      .save(finishedRecipe, RailcraftConstants.rl(name + suffix));
}

  private static void square2x2(Consumer<FinishedRecipe> finishedRecipe,
      Item ingredient,
      Item result,
      int quantity,
      String suffix) {
    var name = RecipeBuilder.getDefaultRecipeId(result).getPath();
    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, result, quantity)
        .pattern("aa")
        .pattern("aa")
        .define('a', ingredient)
        .unlockedBy(getHasName(ingredient), has(ingredient))
        .save(finishedRecipe, RailcraftConstants.rl(name + suffix));
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
      ShapedRecipeBuilder.shaped(RecipeCategory.MISC, colorItems.variantFor(dyeColor).get(), 8)
          .pattern("aaa")
          .pattern("aba")
          .pattern("aaa")
          .define('a', tagItem)
          .define('b', DyeItem.byColor(dyeColor))
          .unlockedBy(getHasName(base), has(base))
          .save(consumer);
    }
  }
}
