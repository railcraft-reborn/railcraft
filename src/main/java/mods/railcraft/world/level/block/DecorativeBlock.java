package mods.railcraft.world.level.block;

import net.minecraft.util.StringRepresentable;

public enum DecorativeBlock implements StringRepresentable {

  QUARRIED("quarried"),
  ABYSSAL("abyssal"),
  JADED("jaded"),
  //BLEACHEDBONE("bleachedbone"),
  /*BLOODSTAINED("bloodstained"),
  FROSTBOUND("frostbound"),
  INFERNAL("infernal"),
  SANDY("sandy"),
  BADLANDS("badlands"),
  NETHER("nether"),
  RED_NETHER("red_nether"),
  ANDESITE("andesite"),
  DIORITE("diorite"),
  GRANITE("granite"),
  PEARLIZED("pearlized"),*/;

  private final String name;

  DecorativeBlock(String name) {
    this.name = name;
  }

  @Override
  public String getSerializedName() {
    return this.name;
  }
}
