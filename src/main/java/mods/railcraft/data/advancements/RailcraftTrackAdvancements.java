package mods.railcraft.data.advancements;

import java.util.function.Consumer;
import mods.railcraft.Translations;
import mods.railcraft.advancements.MultiBlockFormedTrigger;
import mods.railcraft.advancements.SpikeMaulUseTrigger;
import mods.railcraft.advancements.UseTrackKitTrigger;
import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.world.item.RailcraftItems;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntityTypes;
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

class RailcraftTrackAdvancements implements AdvancementProvider.AdvancementGenerator {

  @Override
  public void generate(HolderLookup.Provider registries, Consumer<AdvancementHolder> consumer,
      ExistingFileHelper fileHelper) {
    var root = Advancement.Builder.advancement()
        .display(
            RailcraftItems.REINFORCED_TRACK.get(),
            Component.translatable(Translations.Advancement.Tracks.ROOT),
            Component.translatable(Translations.Advancement.Tracks.ROOT_DESC),
            new ResourceLocation("textures/gui/advancements/backgrounds/stone.png"),
            AdvancementType.TASK,
            false, false, false)
        .addCriterion("inv_changed",
            InventoryChangeTrigger.TriggerInstance.hasItems(RailcraftItems.WOODEN_TIE.get()))
        .save(consumer, RailcraftConstants.rl("tracks/root"), fileHelper);

    Advancement.Builder.advancement()
        .display(
            RailcraftItems.RAW_FIRESTONE.get(),
            Component.translatable(Translations.Advancement.Tracks.FIRESTONE),
            Component.translatable(Translations.Advancement.Tracks.FIRESTONE_DESC),
            null,
            AdvancementType.CHALLENGE,
            true, true, false)
        .parent(root)
        .addCriterion("inv_changed",
            InventoryChangeTrigger.TriggerInstance.hasItems(RailcraftItems.RAW_FIRESTONE.get()))
        .save(consumer, RailcraftConstants.rl("tracks/firestone"), fileHelper);

    Advancement.Builder.advancement()
        .display(
            RailcraftItems.BLAST_FURNACE_BRICKS.get(),
            Component.translatable(Translations.Advancement.Tracks.BLAST_FURNACE),
            Component.translatable(Translations.Advancement.Tracks.BLAST_FURNACE_DESC),
            null,
            AdvancementType.TASK,
            true, false, false)
        .parent(root)
        .addCriterion("blast_furnace_formed", MultiBlockFormedTrigger
            .formedMultiBlock(RailcraftBlockEntityTypes.BLAST_FURNACE.get()))
        .save(consumer, RailcraftConstants.rl("tracks/blast_furnace"), fileHelper);

    Advancement.Builder.advancement()
        .display(
            RailcraftItems.COKE_OVEN_BRICKS.get(),
            Component.translatable(Translations.Advancement.Tracks.COKE_OVEN),
            Component.translatable(Translations.Advancement.Tracks.COKE_OVEN_DESC),
            null,
            AdvancementType.TASK,
            true, true, false)
        .parent(root)
        .addCriterion("has_coke_oven", MultiBlockFormedTrigger
            .formedMultiBlock(RailcraftBlockEntityTypes.COKE_OVEN.get()))
        .save(consumer, RailcraftConstants.rl("tracks/coke_oven"), fileHelper);

    var rollingTable = Advancement.Builder.advancement()
        .display(
            RailcraftItems.MANUAL_ROLLING_MACHINE.get(),
            Component.translatable(Translations.Advancement.Tracks.MANUAL_ROLLING_MACHINE),
            Component.translatable(Translations.Advancement.Tracks.MANUAL_ROLLING_MACHINE_DESC),
            null,
            AdvancementType.GOAL,
            true, false, false)
        .parent(root)
        .addCriterion("inv_changed",
            InventoryChangeTrigger.TriggerInstance
                .hasItems(RailcraftItems.MANUAL_ROLLING_MACHINE.get()))
        .save(consumer, RailcraftConstants.rl("tracks/manual_rolling_machine"), fileHelper);

    Advancement.Builder.advancement()
        .display(
            RailcraftItems.CRUSHER.get(),
            Component.translatable(Translations.Advancement.Tracks.CRUSHER),
            Component.translatable(Translations.Advancement.Tracks.CRUSHER_DESC),
            null,
            AdvancementType.GOAL,
            true, false, false)
        .parent(root)
        .addCriterion("inv_changed",
            InventoryChangeTrigger.TriggerInstance.hasItems(RailcraftItems.CRUSHER.get()))
        .save(consumer, RailcraftConstants.rl("tracks/crusher"), fileHelper);

    var basicTrack = Advancement.Builder.advancement()
        .display(
            Items.RAIL,
            Component.translatable(Translations.Advancement.Tracks.REGULAR_TRACK),
            Component.translatable(Translations.Advancement.Tracks.REGULAR_TRACK_DESC),
            null,
            AdvancementType.TASK,
            true, false, false)
        .parent(rollingTable)
        .addCriterion("inv_changed", InventoryChangeTrigger.TriggerInstance.hasItems(Items.RAIL))
        .save(consumer, RailcraftConstants.rl("tracks/regular_track"), fileHelper);

    Advancement.Builder.advancement()
        .display(
            RailcraftItems.BUFFER_STOP_TRACK_KIT.get(),
            Component.translatable(Translations.Advancement.Tracks.TRACK_KIT),
            Component.translatable(Translations.Advancement.Tracks.TRACK_KIT_DESC),
            null,
            AdvancementType.GOAL,
            true, true, false)
        .parent(basicTrack)
        .addCriterion("has_used_track_kit", UseTrackKitTrigger.hasUsedTrackKit())
        .save(consumer, RailcraftConstants.rl("tracks/track_kit"), fileHelper);

    Advancement.Builder.advancement()
        .display(
            RailcraftItems.IRON_SPIKE_MAUL.get(),
            Component.translatable(Translations.Advancement.Tracks.JUNCTIONS),
            Component.translatable(Translations.Advancement.Tracks.JUNCTIONS_DESC),
            null,
            AdvancementType.GOAL,
            true, false, false)
        .parent(basicTrack)
        .addCriterion("has_used_spikemaul", SpikeMaulUseTrigger.hasUsedSpikeMaul())
        .save(consumer, RailcraftConstants.rl("tracks/junctions"), fileHelper);

    Advancement.Builder.advancement()
        .display(
            RailcraftItems.HIGH_SPEED_TRACK.get(),
            Component.translatable(Translations.Advancement.Tracks.HIGH_SPEED_TRACK),
            Component.translatable(Translations.Advancement.Tracks.HIGH_SPEED_TRACK_DESC),
            null,
            AdvancementType.CHALLENGE,
            true, true, false)
        .parent(rollingTable)
        .addCriterion("inv_changed",
            InventoryChangeTrigger.TriggerInstance.hasItems(RailcraftItems.HIGH_SPEED_TRACK.get()))
        .save(consumer, RailcraftConstants.rl("tracks/high_speed_track"), fileHelper);

    Advancement.Builder.advancement()
        .display(
            RailcraftItems.STRAP_IRON_TRACK.get(),
            Component.translatable(Translations.Advancement.Tracks.WOODEN_TRACK),
            Component.translatable(Translations.Advancement.Tracks.WOODEN_TRACK_DESC),
            null,
            AdvancementType.TASK,
            true, false, false)
        .parent(rollingTable)
        .addCriterion("inv_changed",
            InventoryChangeTrigger.TriggerInstance.hasItems(RailcraftItems.STRAP_IRON_TRACK.get()))
        .save(consumer, RailcraftConstants.rl("tracks/wooden_track"), fileHelper);
  }
}
