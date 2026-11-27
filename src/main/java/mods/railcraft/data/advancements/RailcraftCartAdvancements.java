package mods.railcraft.data.advancements;

import mods.railcraft.Translations;
import mods.railcraft.advancements.BedCartSleepTrigger;
import mods.railcraft.advancements.CartLinkingTrigger;
import mods.railcraft.advancements.JukeboxCartPlayMusicTrigger;
import mods.railcraft.advancements.SetSeasonTrigger;
import mods.railcraft.advancements.SurpriseTrigger;
import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.world.item.RailcraftItems;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementType;
import net.minecraft.advancements.triggers.InventoryChangeTrigger;
import net.minecraft.data.advancements.AdvancementSubProvider;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Items;

class RailcraftCartAdvancements extends AdvancementSubProvider {

  RailcraftCartAdvancements(BootstrapContext<Advancement> output) {
    super(output);
  }

  @Override
  public void generate() {
    var root = Advancement.Builder.advancement()
        .rootDisplay(
            RailcraftItems.DIAMOND_CROWBAR.get(),
            Component.translatable(Translations.Advancement.Carts.ROOT),
            Component.translatable(Translations.Advancement.Carts.ROOT_DESC),
            Identifier.withDefaultNamespace("gui/advancements/backgrounds/stone"),
            AdvancementType.TASK,
            true, false, false)
        .addCriterion("inv_changed",
            InventoryChangeTrigger.TriggerInstance.hasItems(RailcraftItems.IRON_CROWBAR.get()))
        .save(this.output, RailcraftConstants.id("carts/root"));

    Advancement.Builder.advancement()
        .display(
            RailcraftItems.STEEL_CROWBAR.get(),
            Component.translatable(Translations.Advancement.Carts.LINK_CARTS),
            Component.translatable(Translations.Advancement.Carts.LINK_CARTS_DESC),
            AdvancementType.TASK,
            true, false, false)
        .addCriterion("linked_carts", CartLinkingTrigger.hasLinked())
        .parent(root)
        .save(this.output, RailcraftConstants.id("carts/link_carts"));

    Advancement.Builder.advancement()
        .display(
            RailcraftItems.SEASONS_CROWBAR.get(),
            Component.translatable(Translations.Advancement.Carts.SEASONS),
            Component.translatable(Translations.Advancement.Carts.SEASONS_DESC),
            AdvancementType.GOAL,
            true, false, false)
        .addCriterion("on_season_set", SetSeasonTrigger.onSeasonSet())
        .parent(root)
        .save(this.output, RailcraftConstants.id("carts/seasons"));

    var rcLocomotive = Advancement.Builder.advancement()
        .display(
            RailcraftItems.STEAM_LOCOMOTIVE.get(),
            Component.translatable(Translations.Advancement.Carts.LOCOMOTIVE),
            Component.translatable(Translations.Advancement.Carts.LOCOMOTIVE_DESC),
            AdvancementType.CHALLENGE,
            true, true, false)
        .addCriterion("has_locomotives",
            InventoryChangeTrigger.TriggerInstance.hasItems(RailcraftItems.STEAM_LOCOMOTIVE.get()))
        .parent(root)
        .save(this.output, RailcraftConstants.id("carts/locomotive"));

    Advancement.Builder.advancement()
        .display(
            Items.MINECART,
            Component.translatable(Translations.Advancement.Carts.BED_CART),
            Component.translatable(Translations.Advancement.Carts.BED_CART_DESC),
            AdvancementType.TASK,
            true, false, false)
        .addCriterion("has_slept_in_rc_bed", BedCartSleepTrigger.hasSlept())
        .parent(rcLocomotive)
        .save(this.output, RailcraftConstants.id("carts/bed_cart"));

    Advancement.Builder.advancement()
        .display(
            Items.MINECART,
            Component.translatable(Translations.Advancement.Carts.JUKEBOX_CART),
            Component.translatable(Translations.Advancement.Carts.JUKEBOX_CART_DESC),
            AdvancementType.TASK,
            true, false, false)
        .addCriterion("stal_played", JukeboxCartPlayMusicTrigger.hasPlayedAnyMusic())
        .parent(rcLocomotive)
        .save(this.output, RailcraftConstants.id("carts/jukebox_cart"));

    Advancement.Builder.advancement()
        .display(
            Items.MINECART,
            Component.translatable(Translations.Advancement.Carts.SURPRISE),
            Component.translatable(Translations.Advancement.Carts.SURPRISE_DESC),
            AdvancementType.TASK,
            true, true, false)
        .addCriterion("has_exploded_track", SurpriseTrigger.hasExplodedCart())
        .parent(rcLocomotive)
        .save(this.output, RailcraftConstants.id("carts/surprise"));
  }
}
