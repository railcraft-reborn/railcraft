package mods.railcraft.world.level.levelgen.placement;

import net.minecraft.util.StringRepresentable;

public enum WorldgenOre implements StringRepresentable {

  LEAD("lead"),
  TIN("tin"),
  SULFUR("sulfur"),
  ZINC("zinc"),
  NICKEL("nickel"),
  SILVER("silver"),
  SALTPETER("saltpeter");

  public static final StringRepresentable.EnumCodec<WorldgenOre> CODEC =
      StringRepresentable.fromEnum(WorldgenOre::values);

  private final String name;

  WorldgenOre(String name) {
    this.name = name;
  }

  public String getConfigKey() {
    return this.name + "Ore";
  }

  @Override
  public String getSerializedName() {
    return this.name;
  }
}
