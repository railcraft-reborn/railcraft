package mods.railcraft.data.advancements;

import java.util.function.Consumer;
import mods.railcraft.Railcraft;
import mods.railcraft.data.loot.packs.RailcraftAdvancementRewardLoot;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.critereon.PlayerTrigger;
import net.minecraft.core.HolderLookup;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.ForgeAdvancementProvider;

class RailcraftAdvancements implements ForgeAdvancementProvider.AdvancementGenerator {

  @Override
  public void generate(HolderLookup.Provider registries, Consumer<Advancement> consumer,
      ExistingFileHelper fileHelper) {
    Advancement.Builder.advancement()
        .addCriterion("tick", PlayerTrigger.TriggerInstance.tick())
        .rewards(AdvancementRewards.Builder.loot(RailcraftAdvancementRewardLoot.PATCHOULI_BOOK))
        .save(consumer, Railcraft.rl("grant_book_on_first_join"), fileHelper);
  }
}
