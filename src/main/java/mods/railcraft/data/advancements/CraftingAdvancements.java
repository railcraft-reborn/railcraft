package mods.railcraft.data.advancements;

import java.util.function.Consumer;

import mods.railcraft.Railcraft;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.FrameType;
import net.minecraft.advancements.criterion.InventoryChangeTrigger;
import net.minecraft.block.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;

public class CraftingAdvancements implements Consumer<Consumer<Advancement>> {

  @Override
  public void accept(Consumer<Advancement> consumer) {

    // the three bool: show toast, announce to chat, hidden

    // prerequisite for rolling table?
    Advancement railAdvancement = Advancement.Builder.advancement()
        .display(
            Blocks.RAIL,
            new TranslationTextComponent("advancements.crafting.rail.title"),
            new TranslationTextComponent("advancements.crafting.rail.description"),
            new ResourceLocation("textures/gui/advancements/backgrounds/stone.png"),
            FrameType.TASK,
            true, false, false)
        .addCriterion("has_vanilla_track",
            InventoryChangeTrigger.Instance.hasItems(Blocks.RAIL))
        .save(consumer, Railcraft.ID + ":crafting/rail");

  }

}
