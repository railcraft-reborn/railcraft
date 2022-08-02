package mods.railcraft.data.advancements;

import java.util.function.Consumer;
import mods.railcraft.Railcraft;
import mods.railcraft.Translations.Advancment.Tracks;
import mods.railcraft.advancements.MultiBlockFormedTrigger;
import mods.railcraft.advancements.SpikeMaulUseTrigger;
import mods.railcraft.advancements.UseTrackKitTrigger;
import mods.railcraft.world.item.RailcraftItems;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntityTypes;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.FrameType;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.data.ExistingFileHelper;

public class TrackAdvancements {

  public static void register(Consumer<Advancement> consumer, ExistingFileHelper fileHelper) {

    Advancement rcRoot = Advancement.Builder.advancement()
        .display(
            RailcraftItems.REINFORCED_TRACK.get(),
            Component.translatable(Tracks.ROOT),
            Component.translatable(Tracks.ROOT_DESC),
            new ResourceLocation("textures/gui/advancements/backgrounds/stone.png"),
            FrameType.TASK,
            false, false, false)
        .addCriterion("inv_changed",
            InventoryChangeTrigger.TriggerInstance.hasItems(RailcraftItems.WOODEN_TIE.get()))
        .save(consumer, new ResourceLocation(Railcraft.ID, "tracks/root"), fileHelper);

    Advancement.Builder.advancement()
        .display(
            RailcraftItems.RAW_FIRESTONE.get(),
            Component.translatable(Tracks.FIRESTONE),
            Component.translatable(Tracks.FIRESTONE_DESC),
            null,
            FrameType.CHALLENGE,
            true, true, false)
        .parent(rcRoot)
        .addCriterion("inv_changed",
            InventoryChangeTrigger.TriggerInstance.hasItems(RailcraftItems.RAW_FIRESTONE.get()))
        .save(consumer, new ResourceLocation(Railcraft.ID, "tracks/firestone"), fileHelper);

     Advancement.Builder.advancement()
         .display(
             RailcraftItems.BLAST_FURNACE_BRICKS.get(),
             Component.translatable(Tracks.BLAST_FURNACE),
             Component.translatable(Tracks.BLAST_FURNACE_DESC),
             null,
             FrameType.TASK,
             true, false, false)
         .parent(rcRoot)
         .addCriterion("blast_furnace_formed", MultiBlockFormedTrigger.Instance
             .formedMultiBlock(RailcraftBlockEntityTypes.BLAST_FURNACE.get()))
         .save(consumer, new ResourceLocation(Railcraft.ID, "tracks/blast_furnace"), fileHelper);

    Advancement.Builder.advancement()
        .display(
            RailcraftItems.COKE_OVEN_BRICKS.get(),
            Component.translatable(Tracks.COKE_OVEN),
            Component.translatable(Tracks.COKE_OVEN_DESC),
            null,
            FrameType.TASK,
            true, true, false)
        .parent(rcRoot)
        .addCriterion("has_coke_oven", MultiBlockFormedTrigger.Instance
            .formedMultiBlock(RailcraftBlockEntityTypes.COKE_OVEN.get()))
        .save(consumer, new ResourceLocation(Railcraft.ID, "tracks/coke_oven"), fileHelper);

    Advancement rollingTable = Advancement.Builder.advancement()
        .display(
            RailcraftItems.MANUAL_ROLLING_MACHINE.get(),
            Component.translatable(Tracks.MANUAL_ROLLING_MACHINE),
            Component.translatable(Tracks.MANUAL_ROLLING_MACHINE_DESC),
            null,
            FrameType.GOAL,
            true, false, false)
        .parent(rcRoot)
        .addCriterion("inv_changed",
            InventoryChangeTrigger.TriggerInstance.hasItems(RailcraftItems.MANUAL_ROLLING_MACHINE.get()))
        .save(consumer, new ResourceLocation(Railcraft.ID, "tracks/manual_rolling_machine"), fileHelper);

     Advancement.Builder.advancement()
         .display(
             RailcraftItems.CRUSHER.get(),
             Component.translatable(Tracks.CRUSHER),
             Component.translatable(Tracks.CRUSHER_DESC),
             null,
             FrameType.GOAL,
             true, false, false)
         .parent(rcRoot)
         .addCriterion("inv_changed",
             InventoryChangeTrigger.TriggerInstance.hasItems(RailcraftItems.CRUSHER.get()))
         .save(consumer, new ResourceLocation(Railcraft.ID, "tracks/crusher"), fileHelper);

    Advancement basicTrack = Advancement.Builder.advancement()
        .display(
            Items.RAIL,
            Component.translatable(Tracks.REGULAR_TRACK),
            Component.translatable(Tracks.REGULAR_TRACK_DESC),
            null,
            FrameType.TASK,
            true, false, false)
        .parent(rollingTable)
        .addCriterion("inv_changed", InventoryChangeTrigger.TriggerInstance.hasItems(Items.RAIL))
        .save(consumer, new ResourceLocation(Railcraft.ID, "tracks/regular_track"), fileHelper);

    Advancement.Builder.advancement()
        .display(
            RailcraftItems.BUFFER_STOP_TRACK_KIT.get(),
            Component.translatable(Tracks.TRACK_KIT),
            Component.translatable(Tracks.TRACK_KIT_DESC),
            null,
            FrameType.GOAL,
            true, true, false)
        .parent(basicTrack)
        .addCriterion("has_used_track_kit", UseTrackKitTrigger.Instance.hasUsedTrackKit())
        .save(consumer, new ResourceLocation(Railcraft.ID, "tracks/track_kit"), fileHelper);

    Advancement.Builder.advancement()
        .display(
            RailcraftItems.IRON_SPIKE_MAUL.get(),
            Component.translatable(Tracks.JUNCTIONS),
            Component.translatable(Tracks.JUNCTIONS_DESC),
            null,
            FrameType.GOAL,
            true, false, false)
        .parent(basicTrack)
        .addCriterion("has_used_spikemaul", SpikeMaulUseTrigger.Instance.hasUsedSpikeMaul())
        .save(consumer, new ResourceLocation(Railcraft.ID, "tracks/junctions"), fileHelper);

    Advancement.Builder.advancement()
        .display(
            RailcraftItems.HIGH_SPEED_TRACK.get(),
            Component.translatable(Tracks.HIGH_SPEED_TRACK),
            Component.translatable(Tracks.HIGH_SPEED_TRACK_DESC),
            null,
            FrameType.CHALLENGE,
            true, true, false)
        .parent(rollingTable)
        .addCriterion("inv_changed",
            InventoryChangeTrigger.TriggerInstance.hasItems(RailcraftItems.HIGH_SPEED_TRACK.get()))
        .save(consumer, new ResourceLocation(Railcraft.ID, "tracks/high_speed_track"), fileHelper);

    Advancement.Builder.advancement()
        .display(
            RailcraftItems.STRAP_IRON_TRACK.get(),
            Component.translatable(Tracks.WOODEN_TRACK),
            Component.translatable(Tracks.WOODEN_TRACK_DESC),
            null,
            FrameType.TASK,
            true, false, false)
        .parent(rollingTable)
        .addCriterion("inv_changed",
            InventoryChangeTrigger.TriggerInstance.hasItems(RailcraftItems.STRAP_IRON_TRACK.get()))
        .save(consumer, new ResourceLocation(Railcraft.ID, "tracks/wooden_track"), fileHelper);
  }
}
