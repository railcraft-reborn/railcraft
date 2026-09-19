package mods.railcraft.data.tags;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.tags.RailcraftTags;
import mods.railcraft.world.entity.npc.RailcraftVillagerTrades;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.KeyTagProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.tags.VillagerTradeTags;
import net.minecraft.world.item.trading.VillagerTrade;

public class RailcraftVillagerTradeTagsProvider extends KeyTagProvider<VillagerTrade> {

  public RailcraftVillagerTradeTagsProvider(PackOutput output,
      CompletableFuture<HolderLookup.Provider> registries) {
    super(output, Registries.VILLAGER_TRADE, registries, RailcraftConstants.ID);
  }

  @Override
  protected void addTags(HolderLookup.Provider registries) {
    this.tag(RailcraftTags.VillagerTrades.TRACKMAN_LEVEL_1)
        .add(RailcraftVillagerTrades.TRACKMAN_1_COAL_EMERALD)
        .add(RailcraftVillagerTrades.TRACKMAN_1_COAL_COKE_EMERALD)
        .add(RailcraftVillagerTrades.TRACKMAN_1_EMERALD_RAIL);

    // Both level 2 and level 3 hand out track kits
    addAll(RailcraftTags.VillagerTrades.TRACKMAN_LEVEL_2,
        RailcraftVillagerTrades.TRACKMAN_TRACK_KITS);
    addAll(RailcraftTags.VillagerTrades.TRACKMAN_LEVEL_3,
        RailcraftVillagerTrades.TRACKMAN_TRACK_KITS);

    this.tag(RailcraftTags.VillagerTrades.TRACKMAN_LEVEL_3)
        .add(RailcraftVillagerTrades.TRACKMAN_3_EMERALD_STEEL_CROWBAR)
        .add(RailcraftVillagerTrades.TRACKMAN_3_EMERALD_WHISTLE_TUNER)
        .add(RailcraftVillagerTrades.TRACKMAN_3_EMERALD_SIGNAL_BLOCK_SURVEYOR)
        .add(RailcraftVillagerTrades.TRACKMAN_3_EMERALD_SIGNAL_TUNER)
        .add(RailcraftVillagerTrades.TRACKMAN_3_EMERALD_GOGGLES)
        .add(RailcraftVillagerTrades.TRACKMAN_3_EMERALD_OVERALLS);

    this.tag(RailcraftTags.VillagerTrades.CARTMAN_LEVEL_1)
        .add(RailcraftVillagerTrades.CARTMAN_1_COAL_EMERALD)
        .add(RailcraftVillagerTrades.CARTMAN_1_COAL_COKE_EMERALD);

    addAll(RailcraftTags.VillagerTrades.CARTMAN_LEVEL_2,
        RailcraftVillagerTrades.CARTMAN_2_CARTS);
    addAll(RailcraftTags.VillagerTrades.CARTMAN_LEVEL_2,
        RailcraftVillagerTrades.CARTMAN_2_VANILLA_CARTS);

    addAll(RailcraftTags.VillagerTrades.CARTMAN_LEVEL_3,
        RailcraftVillagerTrades.CARTMAN_3_CARTS);
    addAll(RailcraftTags.VillagerTrades.CARTMAN_LEVEL_3,
        RailcraftVillagerTrades.CARTMAN_3_VANILLA_CARTS);
    addAll(RailcraftTags.VillagerTrades.CARTMAN_LEVEL_3,
        RailcraftVillagerTrades.CARTMAN_3_LOCOMOTIVES);

    this.tag(VillagerTradeTags.TOOLSMITH_LEVEL_1)
        .add(RailcraftVillagerTrades.TOOLSMITH_1_COAL_EMERALD)
        .add(RailcraftVillagerTrades.TOOLSMITH_1_COAL_COKE_EMERALD)
        .add(RailcraftVillagerTrades.TOOLSMITH_1_IRON_EMERALD);
    this.tag(VillagerTradeTags.TOOLSMITH_LEVEL_2)
        .add(RailcraftVillagerTrades.TOOLSMITH_2_EMERALD_IRON_STEEL_INGOT)
        .add(RailcraftVillagerTrades.TOOLSMITH_2_EMERALD_STEEL_INGOT)
        .add(RailcraftVillagerTrades.TOOLSMITH_2_EMERALD_SLAG);
    this.tag(VillagerTradeTags.TOOLSMITH_LEVEL_3)
        .add(RailcraftVillagerTrades.TOOLSMITH_3_EMERALD_STEEL_GEAR);

    this.tag(VillagerTradeTags.ARMORER_LEVEL_1)
        .add(RailcraftVillagerTrades.ARMORER_1_COAL_EMERALD)
        .add(RailcraftVillagerTrades.ARMORER_1_COAL_COKE_EMERALD);
    this.tag(VillagerTradeTags.ARMORER_LEVEL_2)
        .add(RailcraftVillagerTrades.ARMORER_2_COPPER_EMERALD)
        .add(RailcraftVillagerTrades.ARMORER_2_TIN_EMERALD)
        .add(RailcraftVillagerTrades.ARMORER_2_ZINC_EMERALD)
        .add(RailcraftVillagerTrades.ARMORER_2_NICKEL_EMERALD);
    this.tag(VillagerTradeTags.ARMORER_LEVEL_3)
        .add(RailcraftVillagerTrades.ARMORER_3_EMERALD_BRASS_INGOT)
        .add(RailcraftVillagerTrades.ARMORER_3_EMERALD_BRONZE_INGOT)
        .add(RailcraftVillagerTrades.ARMORER_3_EMERALD_INVAR_INGOT)
        .add(RailcraftVillagerTrades.ARMORER_3_EMERALD_BRONZE_GEAR)
        .add(RailcraftVillagerTrades.ARMORER_3_EMERALD_BRASS_GEAR)
        .add(RailcraftVillagerTrades.ARMORER_3_EMERALD_INVAR_GEAR);
  }

  private void addAll(TagKey<VillagerTrade> tag, List<ResourceKey<VillagerTrade>> trades) {
    var appender = this.tag(tag);
    trades.forEach(appender::add);
  }
}
