package mods.railcraft.data.worldgen;

import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import mods.railcraft.Railcraft;
import mods.railcraft.data.worldgen.RailcraftConfigurableOre.PLACEMENT_TYPE;
import mods.railcraft.world.level.block.RailcraftBlocks;
import net.minecraft.data.worldgen.features.OreFeatures;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;

public class RailcraftOrePlacements {

  public static final RailcraftConfigurableOre TIN_ORE = Builder.of(new ResourceLocation(Railcraft.ID, "ore_tin"))
      .addRuledReplacement(Pair.of(OreFeatures.STONE_ORE_REPLACEABLES, RailcraftBlocks.TIN_ORE.getId()))
      .addRuledReplacement(Pair.of(OreFeatures.DEEPSLATE_ORE_REPLACEABLES, RailcraftBlocks.DEEPSLATE_TIN_ORE.getId()))
      .setCount(9)
      .setMaxGroupSize(30)
      .setMinYSpawn(136)
      .setMaxYSpawn(319)
      .build();

  public static final class Builder {
    private ResourceLocation id;
    private List<Pair<RuleTest, ResourceLocation>> testReplacement;
    private PLACEMENT_TYPE placementType;
    private int count;
    private int maxGroupSize;
    private int minY;
    private int maxY;

    Builder(ResourceLocation id, List<Pair<RuleTest, ResourceLocation>> oreBlockTypes,
      PLACEMENT_TYPE placementType, int count, int maxGroupSize, // pls confirm if its max vein count, count one is chunk max i think
      int minY, int maxY) {

      this.id = id;
      this.testReplacement = oreBlockTypes;
      this.placementType = placementType;
      this.count = count;
      this.maxGroupSize = maxGroupSize;
      this.minY = minY;
      this.maxY = maxY;
    }

    public static Builder of(ResourceLocation id) {
      return new Builder(id, List.of(), PLACEMENT_TYPE.COMMON, 9, 30, 136, 319);
    }

    public Builder setRuledReplacement(List<Pair<RuleTest, ResourceLocation>> replaceables) {
      this.testReplacement = replaceables;
      return this;
    }

    public Builder addRuledReplacement(Pair<RuleTest, ResourceLocation> replacementThing) {
      this.testReplacement.add(replacementThing);
      return this;
    }

    public Builder setPlacementType(PLACEMENT_TYPE placementType) {
      this.placementType = placementType;
      return this;
    }

    public Builder setCount(int count) {
      this.count = count;
      return this;
    }

    public Builder setMaxGroupSize(int newsize) {
      this.maxGroupSize = newsize;
      return this;
    }

    public Builder setMinYSpawn(int newMinY) {
      this.minY = newMinY;
      return this;
    }

    public Builder setMaxYSpawn(int newMaxY) {
      this.maxY = newMaxY;
      return this;
    }

    public RailcraftConfigurableOre build() {
      // anti stupidity check
      if (this.minY > this.maxY) {
        throw new Error("[Railcraft] One of the ore builders registered their minimum Y level HIGHER than the max Y level. ID: " + this.id);
      }

      return new RailcraftConfigurableOre(this.id, this.testReplacement, this.placementType,
          this.count, this.maxGroupSize, this.minY, this.maxY);
    }
  }
}
