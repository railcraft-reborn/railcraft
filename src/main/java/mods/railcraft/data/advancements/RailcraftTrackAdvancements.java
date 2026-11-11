package mods.railcraft.data.advancements;

import mods.railcraft.Translations;
import mods.railcraft.advancements.MultiBlockFormedTrigger;
import mods.railcraft.advancements.SpikeMaulUseTrigger;
import mods.railcraft.advancements.UseTrackKitTrigger;
import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.world.item.RailcraftItems;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntityTypes;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementType;
import net.minecraft.advancements.triggers.InventoryChangeTrigger;
import net.minecraft.data.advancements.AdvancementSubProvider;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Items;

class RailcraftTrackAdvancements extends AdvancementSubProvider {

  RailcraftTrackAdvancements(BootstrapContext<Advancement> output) {
    super(output);
  }

  @Override
  public void generate() {
    var root = Advancement.Builder.advancement()
        .rootDisplay(
            RailcraftItems.REINFORCED_TRACK.get(),
            Component.translatable(Translations.Advancement.Tracks.ROOT),
            Component.translatable(Translations.Advancement.Tracks.ROOT_DESC),
            Identifier.withDefaultNamespace("gui/advancements/backgrounds/stone"),
            AdvancementType.TASK,
            false, false, false)
        .addCriterion("inv_changed",
            InventoryChangeTrigger.TriggerInstance.hasItems(RailcraftItems.WOODEN_TIE.get()))
        .save(this.output, RailcraftConstants.id("tracks/root"));

    Advancement.Builder.advancement()
        .display(
            RailcraftItems.RAW_FIRESTONE.get(),
            Component.translatable(Translations.Advancement.Tracks.FIRESTONE),
            Component.translatable(Translations.Advancement.Tracks.FIRESTONE_DESC),
            AdvancementType.CHALLENGE,
            true, true, false)
        .parent(root)
        .addCriterion("inv_changed",
            InventoryChangeTrigger.TriggerInstance.hasItems(RailcraftItems.RAW_FIRESTONE.get()))
        .save(this.output, RailcraftConstants.id("tracks/firestone"));

    Advancement.Builder.advancement()
        .display(
            RailcraftItems.BLAST_FURNACE_BRICKS.get(),
            Component.translatable(Translations.Advancement.Tracks.BLAST_FURNACE),
            Component.translatable(Translations.Advancement.Tracks.BLAST_FURNACE_DESC),
            AdvancementType.TASK,
            true, false, false)
        .parent(root)
        .addCriterion("blast_furnace_formed", MultiBlockFormedTrigger
            .formedMultiBlock(RailcraftBlockEntityTypes.BLAST_FURNACE.get()))
        .save(this.output, RailcraftConstants.id("tracks/blast_furnace"));

    Advancement.Builder.advancement()
        .display(
            RailcraftItems.COKE_OVEN_BRICKS.get(),
            Component.translatable(Translations.Advancement.Tracks.COKE_OVEN),
            Component.translatable(Translations.Advancement.Tracks.COKE_OVEN_DESC),
            AdvancementType.TASK,
            true, true, false)
        .parent(root)
        .addCriterion("has_coke_oven", MultiBlockFormedTrigger
            .formedMultiBlock(RailcraftBlockEntityTypes.COKE_OVEN.get()))
        .save(this.output, RailcraftConstants.id("tracks/coke_oven"));

    var rollingTable = Advancement.Builder.advancement()
        .display(
            RailcraftItems.MANUAL_ROLLING_MACHINE.get(),
            Component.translatable(Translations.Advancement.Tracks.MANUAL_ROLLING_MACHINE),
            Component.translatable(Translations.Advancement.Tracks.MANUAL_ROLLING_MACHINE_DESC),
            AdvancementType.GOAL,
            true, false, false)
        .parent(root)
        .addCriterion("inv_changed",
            InventoryChangeTrigger.TriggerInstance
                .hasItems(RailcraftItems.MANUAL_ROLLING_MACHINE.get()))
        .save(this.output, RailcraftConstants.id("tracks/manual_rolling_machine"));

    Advancement.Builder.advancement()
        .display(
            RailcraftItems.CRUSHER.get(),
            Component.translatable(Translations.Advancement.Tracks.CRUSHER),
            Component.translatable(Translations.Advancement.Tracks.CRUSHER_DESC),
            AdvancementType.GOAL,
            true, false, false)
        .parent(root)
        .addCriterion("inv_changed",
            InventoryChangeTrigger.TriggerInstance.hasItems(RailcraftItems.CRUSHER.get()))
        .save(this.output, RailcraftConstants.id("tracks/crusher"));

    var basicTrack = Advancement.Builder.advancement()
        .display(
            Items.RAIL,
            Component.translatable(Translations.Advancement.Tracks.REGULAR_TRACK),
            Component.translatable(Translations.Advancement.Tracks.REGULAR_TRACK_DESC),
            AdvancementType.TASK,
            true, false, false)
        .parent(rollingTable)
        .addCriterion("inv_changed", InventoryChangeTrigger.TriggerInstance.hasItems(Items.RAIL))
        .save(this.output, RailcraftConstants.id("tracks/regular_track"));

    Advancement.Builder.advancement()
        .display(
            RailcraftItems.BUFFER_STOP_TRACK_KIT.get(),
            Component.translatable(Translations.Advancement.Tracks.TRACK_KIT),
            Component.translatable(Translations.Advancement.Tracks.TRACK_KIT_DESC),
            AdvancementType.GOAL,
            true, true, false)
        .parent(basicTrack)
        .addCriterion("has_used_track_kit", UseTrackKitTrigger.hasUsedTrackKit())
        .save(this.output, RailcraftConstants.id("tracks/track_kit"));

    Advancement.Builder.advancement()
        .display(
            RailcraftItems.IRON_SPIKE_MAUL.get(),
            Component.translatable(Translations.Advancement.Tracks.JUNCTIONS),
            Component.translatable(Translations.Advancement.Tracks.JUNCTIONS_DESC),
            AdvancementType.GOAL,
            true, false, false)
        .parent(basicTrack)
        .addCriterion("has_used_spikemaul", SpikeMaulUseTrigger.hasUsedSpikeMaul())
        .save(this.output, RailcraftConstants.id("tracks/junctions"));

    Advancement.Builder.advancement()
        .display(
            RailcraftItems.HIGH_SPEED_TRACK.get(),
            Component.translatable(Translations.Advancement.Tracks.HIGH_SPEED_TRACK),
            Component.translatable(Translations.Advancement.Tracks.HIGH_SPEED_TRACK_DESC),
            AdvancementType.CHALLENGE,
            true, true, false)
        .parent(rollingTable)
        .addCriterion("inv_changed",
            InventoryChangeTrigger.TriggerInstance.hasItems(RailcraftItems.HIGH_SPEED_TRACK.get()))
        .save(this.output, RailcraftConstants.id("tracks/high_speed_track"));

    Advancement.Builder.advancement()
        .display(
            RailcraftItems.STRAP_IRON_TRACK.get(),
            Component.translatable(Translations.Advancement.Tracks.WOODEN_TRACK),
            Component.translatable(Translations.Advancement.Tracks.WOODEN_TRACK_DESC),
            AdvancementType.TASK,
            true, false, false)
        .parent(rollingTable)
        .addCriterion("inv_changed",
            InventoryChangeTrigger.TriggerInstance.hasItems(RailcraftItems.STRAP_IRON_TRACK.get()))
        .save(this.output, RailcraftConstants.id("tracks/wooden_track"));
  }
}
