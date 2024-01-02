package mods.railcraft.data.advancements;

import java.util.function.Consumer;
import mods.railcraft.Translations;
import mods.railcraft.advancements.BedCartSleepTrigger;
import mods.railcraft.advancements.CartLinkingTrigger;
import mods.railcraft.advancements.JukeboxCartPlayMusicTrigger;
import mods.railcraft.advancements.SetSeasonTrigger;
import mods.railcraft.advancements.SurpriseTrigger;
import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.world.item.RailcraftItems;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementType;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.common.data.AdvancementProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

class RailcraftCartAdvancements implements AdvancementProvider.AdvancementGenerator {

  @Override
  public void generate(HolderLookup.Provider registries, Consumer<AdvancementHolder> consumer,
      ExistingFileHelper fileHelper) {
    var root = Advancement.Builder.advancement()
        .display(
            RailcraftItems.DIAMOND_CROWBAR.get(),
            Component.translatable(Translations.Advancement.Carts.ROOT),
            Component.translatable(Translations.Advancement.Carts.ROOT_DESC),
            new ResourceLocation("textures/gui/advancements/backgrounds/stone.png"),
            AdvancementType.TASK,
            true, false, false)
        .addCriterion("inv_changed",
            InventoryChangeTrigger.TriggerInstance.hasItems(RailcraftItems.IRON_CROWBAR.get()))
        .save(consumer, RailcraftConstants.rl("carts/root"), fileHelper);

    Advancement.Builder.advancement()
        .display(
            RailcraftItems.STEEL_CROWBAR.get(),
            Component.translatable(Translations.Advancement.Carts.LINK_CARTS),
            Component.translatable(Translations.Advancement.Carts.LINK_CARTS_DESC),
            null,
            AdvancementType.TASK,
            true, false, false)
        .addCriterion("linked_carts", CartLinkingTrigger.hasLinked())
        .parent(root)
        .save(consumer, RailcraftConstants.rl("carts/link_carts"), fileHelper);

    Advancement.Builder.advancement()
        .display(
            RailcraftItems.SEASONS_CROWBAR.get(),
            Component.translatable(Translations.Advancement.Carts.SEASONS),
            Component.translatable(Translations.Advancement.Carts.SEASONS_DESC),
            null,
            AdvancementType.GOAL,
            true, false, false)
        .addCriterion("on_season_set", SetSeasonTrigger.onSeasonSet())
        .parent(root)
        .save(consumer, RailcraftConstants.rl("carts/seasons"), fileHelper);

    var rcLocomotive = Advancement.Builder.advancement()
        .display(
            RailcraftItems.STEAM_LOCOMOTIVE.get(),
            Component.translatable(Translations.Advancement.Carts.LOCOMOTIVE),
            Component.translatable(Translations.Advancement.Carts.LOCOMOTIVE_DESC),
            null,
            AdvancementType.CHALLENGE,
            true, true, false)
        .addCriterion("has_locomotives",
            InventoryChangeTrigger.TriggerInstance.hasItems(RailcraftItems.STEAM_LOCOMOTIVE.get()))
        .parent(root)
        .save(consumer, RailcraftConstants.rl("carts/locomotive"), fileHelper);

    Advancement.Builder.advancement()
        .display(
            Items.MINECART,
            Component.translatable(Translations.Advancement.Carts.BED_CART),
            Component.translatable(Translations.Advancement.Carts.BED_CART_DESC),
            null,
            AdvancementType.TASK,
            true, false, false)
        .addCriterion("has_slept_in_rc_bed", BedCartSleepTrigger.hasSlept())
        .parent(rcLocomotive)
        .save(consumer, RailcraftConstants.rl("carts/bed_cart"), fileHelper);

    Advancement.Builder.advancement()
        .display(
            Items.MINECART,
            Component.translatable(Translations.Advancement.Carts.JUKEBOX_CART),
            Component.translatable(Translations.Advancement.Carts.JUKEBOX_CART_DESC),
            null,
            AdvancementType.TASK,
            true, false, false)
        .addCriterion("stal_played", JukeboxCartPlayMusicTrigger.hasPlayedAnyMusic())
        .parent(rcLocomotive)
        .save(consumer, RailcraftConstants.rl("carts/jukebox_cart"), fileHelper);

    Advancement.Builder.advancement()
        .display(
            Items.MINECART,
            Component.translatable(Translations.Advancement.Carts.SURPRISE),
            Component.translatable(Translations.Advancement.Carts.SURPRISE_DESC),
            null,
            AdvancementType.TASK,
            true, true, false)
        .addCriterion("has_exploded_track", SurpriseTrigger.hasExplodedCart())
        .parent(rcLocomotive)
        .save(consumer, RailcraftConstants.rl("carts/surprise"), fileHelper);
  }
}
