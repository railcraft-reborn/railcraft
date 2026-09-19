package mods.railcraft.world.entity.vehicle;

import net.minecraft.util.StringRepresentable;

public enum LaunchState implements StringRepresentable {

  LAUNCHING("launching"), LAUNCHED("launched"), LANDED("landed");

  public static final StringRepresentable.EnumCodec<LaunchState> CODEC =
      StringRepresentable.fromEnum(LaunchState::values);

  private final String name;

  LaunchState(String name) {
    this.name = name;
  }

  public String getName() {
    return this.name;
  }

  @Override
  public String getSerializedName() {
    return this.getName();
  }
}
