package mods.railcraft.world.level.block.post;

import net.minecraft.util.StringRepresentable;

public enum Connection implements StringRepresentable {

  NONE("none"),
  SINGLE("single"),
  DOUBLE("double");

  private final String name;

  Connection(String name) {
    this.name = name;
  }

  @Override
  public String getSerializedName() {
    return this.name;
  }
}
