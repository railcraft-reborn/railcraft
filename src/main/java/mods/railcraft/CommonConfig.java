package mods.railcraft;

import net.minecraftforge.common.ForgeConfigSpec;

public class CommonConfig {

  public final ForgeConfigSpec.BooleanValue enableSeasons;
  public final ForgeConfigSpec.IntValue christmas;
  public final ForgeConfigSpec.IntValue halloween;
  public final ForgeConfigSpec.IntValue harvest;

  public CommonConfig(ForgeConfigSpec.Builder builder) {
    enableSeasons = builder.define("enableSeasons", true);
    christmas = builder
        .comment("Controls whether Christmas mode is (0) enabled, (1) forced, or (2) disabled")
        .defineInRange("christmas", 0, 0, 2);
    halloween = builder
        .comment("Controls whether Halloween mode is (0) enabled, (1) forced, or (2) disabled")
        .defineInRange("halloween", 0, 0, 2);
    harvest = builder
        .comment("Controls whether Harvest mode is (0) enabled, (1) forced, or (2) disabled")
        .defineInRange("harvest", 0, 0, 2);
  }
}
