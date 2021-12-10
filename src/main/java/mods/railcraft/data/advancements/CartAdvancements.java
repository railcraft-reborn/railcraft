package mods.railcraft.data.advancements;

import java.util.function.Consumer;

import mods.railcraft.Railcraft;
import mods.railcraft.advancements.criterion.BedCartSleepTrigger;
import mods.railcraft.advancements.criterion.CartLinkingTrigger;
import mods.railcraft.advancements.criterion.JukeboxCartPlayMusicTrigger;
import mods.railcraft.advancements.criterion.SetSeasonTrigger;
import mods.railcraft.advancements.criterion.SurpriseTrigger;
import mods.railcraft.world.item.RailcraftItems;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.FrameType;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.TickTrigger;
import net.minecraft.world.item.Items;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.TranslatableComponent;

public class CartAdvancements implements Consumer<Consumer<Advancement>> {

  @Override
  public void accept(Consumer<Advancement> consumer) {

    // the three bool: show toast, announce to chat, hidden

    Advancement rcRoot = Advancement.Builder.advancement()
        .display(
            RailcraftItems.DIAMOND_CROWBAR.get(),
            new TranslatableComponent("advancements.railcraft.carts.root.name"),
            new TranslatableComponent("advancements.railcraft.carts.root.desc"),
            new ResourceLocation("textures/gui/advancements/backgrounds/stone.png"),
            FrameType.TASK,
            true, false, false)
        .addCriterion("default_unlock", new TickTrigger.TriggerInstance(EntityPredicate.Composite.ANY))
        .save(consumer, Railcraft.ID + ":carts/root");

    Advancement.Builder.advancement()
        .display(
            RailcraftItems.STEEL_CROWBAR.get(),
            new TranslatableComponent("advancements.railcraft.carts.link_carts.name"),
            new TranslatableComponent("advancements.railcraft.carts.link_carts.desc"),
            null,
            FrameType.TASK,
            true, false, false)
        .addCriterion("linked_carts",
            CartLinkingTrigger.Instance.hasLinked())
        .parent(rcRoot)
        .save(consumer, Railcraft.ID + ":carts/link_carts");

    Advancement.Builder.advancement()
        .display(
            RailcraftItems.SEASONS_CROWBAR.get(),
            new TranslatableComponent("advancements.railcraft.carts.seasons.name"),
            new TranslatableComponent("advancements.railcraft.carts.seasons.desc"),
            null,
            FrameType.GOAL,
            true, false, false)
        .addCriterion("on_season_set", SetSeasonTrigger.Instance.onSeasonSet())
        .parent(rcRoot)
        .save(consumer, Railcraft.ID + ":carts/seasons");

    Advancement rcLocomotive = Advancement.Builder.advancement()
        .display(
            RailcraftItems.STEAM_LOCOMOTIVE.get(),
            new TranslatableComponent("advancements.railcraft.carts.locomotive.name"),
            new TranslatableComponent("advancements.railcraft.carts.locomotive.desc"),
            null,
            FrameType.CHALLENGE,
            true, true, false)
        .addCriterion("has_locomotives",
            InventoryChangeTrigger.TriggerInstance.hasItems(RailcraftItems.STEAM_LOCOMOTIVE.get()))
        .parent(rcRoot)
        .save(consumer, Railcraft.ID + ":carts/locomotive");

    Advancement.Builder.advancement()
        .display(
            Items.MINECART,
            new TranslatableComponent("advancements.railcraft.carts.bed_cart.name"),
            new TranslatableComponent("advancements.railcraft.carts.bed_cart.desc"),
            null,
            FrameType.TASK,
            true, false, false)
        .addCriterion("has_slept_in_rc_bed", BedCartSleepTrigger.Instance.hasSlept())
        .parent(rcLocomotive)
        .save(consumer, Railcraft.ID + ":carts/bed_cart");

    Advancement.Builder.advancement()
        .display(
            Items.MINECART,
            new TranslatableComponent("advancements.railcraft.carts.jukebox_cart.name"),
            new TranslatableComponent("advancements.railcraft.carts.jukebox_cart.desc"),
            null,
            FrameType.TASK,
            true, false, false)
        .addCriterion("stal_played", JukeboxCartPlayMusicTrigger.Instance.hasPlayedAnyMusic())
        .parent(rcLocomotive)
        .save(consumer, Railcraft.ID + ":carts/jukebox_cart");

    Advancement.Builder.advancement()
        .display(
            Items.MINECART,
            new TranslatableComponent("advancements.railcraft.carts.surprise.name"),
            new TranslatableComponent("advancements.railcraft.carts.surprise.desc"),
            null,
            FrameType.TASK,
            true, true, false)
        .addCriterion("has_exploded_track", SurpriseTrigger.Instance.hasExplodedCart())
        .parent(rcLocomotive)
        .save(consumer, Railcraft.ID + ":carts/surprise");
  }

}
