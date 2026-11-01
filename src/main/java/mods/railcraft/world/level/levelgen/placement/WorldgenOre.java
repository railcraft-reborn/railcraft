package mods.railcraft.world.level.levelgen.placement;

import net.minecraft.util.StringRepresentable;

/**
 * The ores Railcraft generates in the world, each of which can be turned off in the server config.
 */
public enum WorldgenOre implements StringRepresentable {

  LEAD("lead", "leadOre"),
  TIN("tin", "tinOre"),
  SULFUR("sulfur", "sulfurOre"),
  ZINC("zinc", "zincOre"),
  NICKEL("nickel", "nickelOre"),
  SILVER("silver", "silverOre"),
  QUARRIED_STONE("quarried_stone", "quarriedStone"),
  SALTPETER("saltpeter", "saltpeterOre"),
  FIRESTONE("firestone", "firestoneOre");

  public static final StringRepresentable.EnumCodec<WorldgenOre> CODEC =
      StringRepresentable.fromEnum(WorldgenOre::values);

  private final String name;
  private final String configKey;

  WorldgenOre(String name, String configKey) {
    this.name = name;
    this.configKey = configKey;
  }

  public String getConfigKey() {
    return this.configKey;
  }

  @Override
  public String getSerializedName() {
    return this.name;
  }
}
