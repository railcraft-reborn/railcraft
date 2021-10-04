package mods.railcraft.world.level.block.post;

import net.minecraft.util.IStringSerializable;

public enum Column implements IStringSerializable {

  NORMAL("normal"),
  SHORT("short");

  private final String name;

  private Column(String name) {
    this.name = name;
  }

  @Override
  public String getSerializedName() {
    return this.name;
  }
}
