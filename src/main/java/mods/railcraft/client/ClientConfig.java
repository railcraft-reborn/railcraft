package mods.railcraft.client;

import net.minecraftforge.common.ForgeConfigSpec;

public class ClientConfig {

  public final ForgeConfigSpec.BooleanValue enableGhostTrain;
  public final ForgeConfigSpec.BooleanValue enablePolarExpress;
  public final ForgeConfigSpec.IntValue locomotiveLightLevel;

  public ClientConfig(ForgeConfigSpec.Builder builder) {
    this.enableGhostTrain = builder
        .comment("change to false to disable Ghost Train rendering")
        .define("enableGhostTrain", true);
    this.enablePolarExpress = builder
        .comment("change to false to disable Polar Express (snow) rendering")
        .define("enablePolarExpress", true);
    this.locomotiveLightLevel = builder
        .comment(
            "change '14' to a number ranging from '0' to '15' to represent the dynamic lighting of the locomotive when Dynamic Lights mod is present.\nIf it is '0' then locomotive lightning will be disabled.")
        .defineInRange("locomotiveLightLevel", 14, 0, 15);
  }
}
