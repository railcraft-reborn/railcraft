package mods.railcraft.world.level.block.post;

import net.minecraft.util.StringRepresentable;

public enum Connection implements StringRepresentable {

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
