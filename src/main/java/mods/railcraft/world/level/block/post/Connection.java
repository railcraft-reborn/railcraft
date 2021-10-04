package mods.railcraft.world.level.block.post;

import net.minecraft.util.IStringSerializable;

public enum Connection implements IStringSerializable {

  NONE("name"),
  SINGLE("single"),
  DOUBLE("double");

  private final String name;

  private Connection(String name) {
    this.name = name;
  }

  @Override
  public String getSerializedName() {
    return this.name;
  }
}
