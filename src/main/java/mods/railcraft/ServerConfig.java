package mods.railcraft;

import java.util.ArrayList;
import java.util.List;
import com.google.common.collect.Lists;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;

public class ServerConfig {

  public final ForgeConfigSpec.DoubleValue highSpeedTrackMaxSpeed;
  public final ForgeConfigSpec.ConfigValue<List<? extends String>> highSpeedTrackIgnoredEntities;
  public final ForgeConfigSpec.DoubleValue strapIronTrackMaxSpeed;
  public final ForgeConfigSpec.BooleanValue chestAllowFluids;
  public final ForgeConfigSpec.ConfigValue<List<? extends String>> cargoBlacklist;
  public final ForgeConfigSpec.BooleanValue locomotiveDamageMobs;
  public final ForgeConfigSpec.DoubleValue locomotiveHorsepower;
  public final ForgeConfigSpec.BooleanValue solidCarts;
  public final ForgeConfigSpec.BooleanValue cartsCollideWithItems;
  public final ForgeConfigSpec.DoubleValue boreMininigSpeedMultiplier;
  public final ForgeConfigSpec.BooleanValue boreDestorysBlocks;
  public final ForgeConfigSpec.BooleanValue boreMinesAllBlocks;
  public final ForgeConfigSpec.BooleanValue cartsBreakOnDrop;
  public final ForgeConfigSpec.DoubleValue fuelPerSteamMultiplier;
  public final ForgeConfigSpec.DoubleValue steamLocomotiveEfficiency;

  public ServerConfig(ForgeConfigSpec.Builder builder) {
    builder.push("highSpeedTrack");
    {
      this.highSpeedTrackMaxSpeed = builder
          .comment(
              "change to limit max speed on high speed rails, useful if your computer can't keep up with chunk loading\n"
                  + "iron tracks operate at 0.4 blocks per tick")
          .defineInRange("maxSpeed", 1.0D, 0.6D, 1.2D);
      List<String> defaultEntities = Lists.newArrayList(
          "minecraft:bat",
          "minecraft:blaze",
          "minecraft:cave_spider",
          "minecraft:chicken",
          "minecraft:parrot",
          "minecraft:rabbit",
          "minecraft:spider",
          "minecraft:vex");
      this.highSpeedTrackIgnoredEntities = builder
          .comment(
              "add entity names to exclude them from explosions caused by high speed collisions")
          .defineList("ignoredEntities", defaultEntities,
              obj -> ResourceLocation.isValidResourceLocation(obj.toString()));
    }
    builder.pop();
    this.strapIronTrackMaxSpeed = builder
        .comment("change to limit max speed on strap iron rails\n" +
            "iron tracks strapIronTrackMaxSpeed at 0.4 blocks per tick")
        .defineInRange("maxSpeed", 0.12D, 0.1D, 0.3D);
    this.chestAllowFluids = builder
        .comment("change to 'true' to allow fluid containers in Chest and Cargo Carts")
        .define("chestAllowFluids", false);
    this.cargoBlacklist = builder.defineList("cargoBlacklist", ArrayList::new,
        obj -> ResourceLocation.isValidResourceLocation(obj.toString()));
    locomotiveDamageMobs = builder
        .comment(
            "change to 'false' to disable Locomotive damage on mobs, they will still knockback mobs")
        .define("damageMobs", true);
    locomotiveHorsepower = builder
        .comment("controls how much power locomotives have and how many carts they can pull\n"
            + "be warned, longer trains have a greater chance for glitches\n"
            + "as such it HIGHLY recommended you do not change this")
        .defineInRange("horsepower", 15.0D, 15.0D, 45.0D);

    this.solidCarts = builder
        .comment(
            "change to false to return minecarts to vanilla player vs cart collision behavior\n"
                + "in vanilla minecarts are ghost like can be walked through\n"
                + "but making carts solid also makes them hard to push by hand\n"
                + "this setting is ignored if aren't using the Railcraft Collision Handler")
        .define("solidCarts", true);

    this.cartsCollideWithItems = builder
        .comment(
            "change to 'true' to restore minecart collisions with dropped items, ignored if 'register.collision.handler=false'")
        .define("cartsCollideWithItems", false);
    this.boreMininigSpeedMultiplier = builder
        .comment(
            "adjust the speed at which the Bore mines blocks, min=0.1, default=1.0, max=50.0")
        .defineInRange("boreMininigSpeedMultiplier", 1.0D, 0.1D, 50.0D);
    this.boreDestorysBlocks = builder
        .comment(
            "change to true to cause the Bore to destroy the blocks it mines instead of dropping them")
        .define("boreDestorysBlocks", false);
    this.boreMinesAllBlocks = builder
        .comment(
            "change to false to enable mining checks, use true setting with caution, especially on servers")
        .define("boreMinesAllBlocks", true);

    this.cartsBreakOnDrop = builder
        .comment("change to 'true' to restore vanilla behavior")
        .define("cartsBreakOnDrop", false);

    this.fuelPerSteamMultiplier = builder
        .comment("adjust the amount of fuel used to create Steam")
        .defineInRange("fuelPerSteamMultiplier", 1.0F, 0.2F, 6.0F);

    this.steamLocomotiveEfficiency = builder
        .comment("adjust the multiplier used when calculating fuel use")
        .defineInRange("steamLocomotiveEfficiency", 3.0F, 0.2F, 12F);
  }
}
