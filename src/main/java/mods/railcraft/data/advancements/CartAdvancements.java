package mods.railcraft.data.advancements;

import java.util.function.Consumer;

import mods.railcraft.Railcraft;
import mods.railcraft.advancements.criterion.CartLinkingTrigger;
import mods.railcraft.world.item.RailcraftItems;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.FrameType;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.advancements.criterion.InventoryChangeTrigger;
import net.minecraft.advancements.criterion.TickTrigger;
import net.minecraft.block.Blocks;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;

public class CartAdvancements implements Consumer<Consumer<Advancement>> {

  @Override
  public void accept(Consumer<Advancement> consumer) {

    // the three bool: show toast, announce to chat, hidden

    Advancement rcRoot = Advancement.Builder.advancement()
        .display(
            RailcraftItems.DIAMOND_CROWBAR.get(),
            new TranslationTextComponent("advancements.railcraft.carts.root.name"),
            new TranslationTextComponent("advancements.railcraft.carts.root.desc"),
            new ResourceLocation("textures/gui/advancements/backgrounds/stone.png"),
            FrameType.TASK,
            true, false, false)
        .addCriterion("default_unlock", new TickTrigger.Instance(EntityPredicate.AndPredicate.ANY))
        .save(consumer, Railcraft.ID + ":carts/root");

    Advancement.Builder.advancement()
        .display(
            RailcraftItems.STEEL_CROWBAR.get(),
            new TranslationTextComponent("advancements.railcraft.carts.link_carts.name"),
            new TranslationTextComponent("advancements.railcraft.carts.link_carts.desc"),
            new ResourceLocation("textures/gui/advancements/backgrounds/stone.png"),
            FrameType.TASK,
            true, false, false)
        .addCriterion("linked_carts",
            CartLinkingTrigger.Instance.hasLinked())
        .parent(rcRoot)
        .save(consumer, Railcraft.ID + ":carts/link_carts");

    Advancement rcLocomotive = Advancement.Builder.advancement()
        .display(
            RailcraftItems.STEAM_LOCOMOTIVE.get(),
            new TranslationTextComponent("advancements.railcraft.carts.locomotive.name"),
            new TranslationTextComponent("advancements.railcraft.carts.locomotive.desc"),
            new ResourceLocation("textures/gui/advancements/backgrounds/stone.png"),
            FrameType.CHALLENGE,
            true, true, false)
        .addCriterion("has_locomotives",
            InventoryChangeTrigger.Instance.hasItems(RailcraftItems.STEAM_LOCOMOTIVE.get()))
        .parent(rcRoot)
        .save(consumer, Railcraft.ID + ":carts/locomotive");

    Advancement.Builder.advancement()
        .display(
            Items.MINECART,
            new TranslationTextComponent("advancements.railcraft.carts.bed_cart.name"),
            new TranslationTextComponent("advancements.railcraft.carts.bed_cart.desc"),
            new ResourceLocation("textures/gui/advancements/backgrounds/stone.png"),
            FrameType.TASK,
            true, false, false)
        .addCriterion("has_vanilla_track",
            InventoryChangeTrigger.Instance.hasItems(Blocks.RAIL))
        .parent(rcLocomotive)
        .save(consumer, Railcraft.ID + ":carts/bed_cart");

    Advancement.Builder.advancement()
        .display(
            Items.MINECART,
            new TranslationTextComponent("advancements.railcraft.carts.jukebox_cart.name"),
            new TranslationTextComponent("advancements.railcraft.carts.jukebox_cart.desc"),
            new ResourceLocation("textures/gui/advancements/backgrounds/stone.png"),
            FrameType.TASK,
            true, false, false)
        .addCriterion("has_vanilla_track",
            InventoryChangeTrigger.Instance.hasItems(Blocks.RAIL))
        .parent(rcLocomotive)
        .save(consumer, Railcraft.ID + ":carts/jukebox_cart");
  }

}
