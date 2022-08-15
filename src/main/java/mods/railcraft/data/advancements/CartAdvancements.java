package mods.railcraft.data.advancements;

import java.util.function.Consumer;
import mods.railcraft.Railcraft;
import mods.railcraft.Translations.Advancement.Carts;
import mods.railcraft.advancements.BedCartSleepTrigger;
import mods.railcraft.advancements.CartLinkingTrigger;
import mods.railcraft.advancements.JukeboxCartPlayMusicTrigger;
import mods.railcraft.advancements.SetSeasonTrigger;
import mods.railcraft.advancements.SurpriseTrigger;
import mods.railcraft.world.item.RailcraftItems;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.FrameType;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.data.ExistingFileHelper;

public class CartAdvancements {

  public static void register(Consumer<Advancement> consumer, ExistingFileHelper fileHelper) {
    var rcRoot = Advancement.Builder.advancement()
        .display(
            RailcraftItems.DIAMOND_CROWBAR.get(),
            Component.translatable(Carts.ROOT),
            Component.translatable(Carts.ROOT_DESC),
            new ResourceLocation("textures/gui/advancements/backgrounds/stone.png"),
            FrameType.TASK,
            true, false, false)
        .addCriterion("inv_changed",
            InventoryChangeTrigger.TriggerInstance.hasItems(RailcraftItems.IRON_CROWBAR.get()))
        .save(consumer, new ResourceLocation(Railcraft.ID, "carts/root"), fileHelper);

    Advancement.Builder.advancement()
        .display(
            RailcraftItems.STEEL_CROWBAR.get(),
            Component.translatable(Carts.LINK_CARTS),
            Component.translatable(Carts.LINK_CARTS_DESC),
            null,
            FrameType.TASK,
            true, false, false)
        .addCriterion("linked_carts", CartLinkingTrigger.Instance.hasLinked())
        .parent(rcRoot)
        .save(consumer, new ResourceLocation(Railcraft.ID, "carts/link_carts"), fileHelper);

    Advancement.Builder.advancement()
        .display(
            RailcraftItems.SEASONS_CROWBAR.get(),
            Component.translatable(Carts.SEASONS),
            Component.translatable(Carts.SEASONS_DESC),
            null,
            FrameType.GOAL,
            true, false, false)
        .addCriterion("on_season_set", SetSeasonTrigger.Instance.onSeasonSet())
        .parent(rcRoot)
        .save(consumer, new ResourceLocation(Railcraft.ID, "carts/seasons"), fileHelper);

    Advancement rcLocomotive = Advancement.Builder.advancement()
        .display(
            RailcraftItems.STEAM_LOCOMOTIVE.get(),
            Component.translatable(Carts.LOCOMOTIVE),
            Component.translatable(Carts.LOCOMOTIVE_DESC),
            null,
            FrameType.CHALLENGE,
            true, true, false)
        .addCriterion("has_locomotives",
            InventoryChangeTrigger.TriggerInstance.hasItems(RailcraftItems.STEAM_LOCOMOTIVE.get()))
        .parent(rcRoot)
        .save(consumer, new ResourceLocation(Railcraft.ID, "carts/locomotive"), fileHelper);

    Advancement.Builder.advancement()
        .display(
            Items.MINECART,
            Component.translatable(Carts.BED_CART),
            Component.translatable(Carts.BED_CART_DESC),
            null,
            FrameType.TASK,
            true, false, false)
        .addCriterion("has_slept_in_rc_bed", BedCartSleepTrigger.Instance.hasSlept())
        .parent(rcLocomotive)
        .save(consumer, new ResourceLocation(Railcraft.ID, "carts/bed_cart"), fileHelper);

    Advancement.Builder.advancement()
        .display(
            Items.MINECART,
            Component.translatable(Carts.JUKEBOX_CART),
            Component.translatable(Carts.JUKEBOX_CART_DESC),
            null,
            FrameType.TASK,
            true, false, false)
        .addCriterion("stal_played", JukeboxCartPlayMusicTrigger.Instance.hasPlayedAnyMusic())
        .parent(rcLocomotive)
        .save(consumer, new ResourceLocation(Railcraft.ID, "carts/jukebox_cart"), fileHelper);

    Advancement.Builder.advancement()
        .display(
            Items.MINECART,
            Component.translatable(Carts.SURPRISE),
            Component.translatable(Carts.SURPRISE_DESC),
            null,
            FrameType.TASK,
            true, true, false)
        .addCriterion("has_exploded_track", SurpriseTrigger.Instance.hasExplodedCart())
        .parent(rcLocomotive)
        .save(consumer, new ResourceLocation(Railcraft.ID, "carts/surprise"), fileHelper);
  }
}
