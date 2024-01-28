package mods.railcraft;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.common.ForgeConfigSpec.Builder;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;
import net.minecraftforge.common.ForgeConfigSpec.DoubleValue;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;

public class RailcraftConfig {

  public static final Client CLIENT;
  public static final Common COMMON;
  public static final Server SERVER;
  private static final ForgeConfigSpec CLIENT_SPEC, COMMON_SPEC, SERVER_SPEC;

  static {
    final var commonPair = new ForgeConfigSpec.Builder().configure(Common::new);
    COMMON_SPEC = commonPair.getRight();
    COMMON = commonPair.getLeft();

    final var clientPair = new ForgeConfigSpec.Builder().configure(Client::new);
    CLIENT_SPEC = clientPair.getRight();
    CLIENT = clientPair.getLeft();

    final var serverPair = new ForgeConfigSpec.Builder().configure(Server::new);
    SERVER_SPEC = serverPair.getRight();
    SERVER = serverPair.getLeft();
  }

  public static void registerConfig(ModLoadingContext context) {
    context.registerConfig(ModConfig.Type.CLIENT, RailcraftConfig.CLIENT_SPEC);
    context.registerConfig(ModConfig.Type.COMMON, RailcraftConfig.COMMON_SPEC);
    context.registerConfig(ModConfig.Type.SERVER, RailcraftConfig.SERVER_SPEC);
  }

  public static class Server {

    public final DoubleValue highSpeedTrackMaxSpeed;
    public final ConfigValue<List<? extends String>> highSpeedTrackIgnoredEntities;
    public final DoubleValue strapIronTrackMaxSpeed;
    public final BooleanValue chestAllowFluids;
    public final ConfigValue<List<? extends String>> cargoBlacklist;
    public final BooleanValue locomotiveDamageMobs;
    public final DoubleValue locomotiveHorsepower;
    public final BooleanValue solidCarts;
    public final BooleanValue cartsCollideWithItems;
    public final DoubleValue boreMiningSpeedMultiplier;
    public final BooleanValue boreDestroysBlocks;
    public final BooleanValue boreMinesAllBlocks;
    public final BooleanValue cartsBreakOnDrop;
    public final DoubleValue steamLocomotiveEfficiency;
    public final IntValue tankCartFluidTransferRate;
    public final IntValue tankCartFluidCapacity;
    public final BooleanValue tankStackingEnabled;
    public final IntValue maxTankSize;
    public final IntValue tankCapacityPerBlock;
    public final IntValue waterCollectionRate;
    public final IntValue maxLauncherTrackForce;
    public final DoubleValue lossMultiplier;

    public final DoubleValue fuelMultiplier;
    public final DoubleValue fuelPerSteamMultiplier;

    public final IntValue cartDispenserDelay;

    public final BooleanValue changeDungeonLoot;

    private Server(Builder builder) {
      builder.comment("High Speed Track Configuration");
      builder.push("highSpeedTrack");
      {
        this.highSpeedTrackMaxSpeed = builder
            .comment(
                "Change to limit max speed on high speed rails, useful if your computer can't keep up with chunk loading",
                "iron tracks operate at 0.4 blocks per tick")
            .defineInRange("maxSpeed", 1.0D, 0.6D, 1.2D);

        final var defaultEntities = List.of(
            "minecraft:bat", "minecraft:blaze", "minecraft:cave_spider",
            "minecraft:chicken", "minecraft:parrot", "minecraft:rabbit",
            "minecraft:spider", "minecraft:vex", "minecraft:bee");

        this.highSpeedTrackIgnoredEntities = builder
            .comment(
                "Add entity names to exclude them from explosions caused by high speed collisions")
            .defineList("ignoredEntities", defaultEntities,
                obj -> ResourceLocation.isValidResourceLocation(obj.toString()));
      }
      builder.pop();

      this.strapIronTrackMaxSpeed = builder
          .comment(
              "Change to limit max speed on strap iron rails. Vanilla iron rails goes as fast as 0.4D/tick")
          .defineInRange("maxSpeed", 0.12D, 0.1D, 0.3D);

      this.chestAllowFluids = builder
          .comment("Change to 'true' to allow fluid containers in Chest and Cargo Carts")
          .define("chestAllowFluids", false);

      this.cargoBlacklist = builder
          .comment("List of items that the cargo loader will ignore")
          .defineList("cargoBlacklist", ArrayList::new,
              obj -> ResourceLocation.isValidResourceLocation(obj.toString()));

      this.locomotiveDamageMobs = builder
          .comment(
              "change to 'false' to disable Locomotive damage on mobs, they will still knockback mobs")
          .define("damageMobs", true);
      this.locomotiveHorsepower = builder
          .comment("Controls how much power locomotives have and how many carts they can pull",
              "be warned, longer trains have a greater chance for glitches",
              "as such it HIGHLY recommended you do not change this")
          .defineInRange("horsepower", 15.0D, 15.0D, 45.0D);

      this.solidCarts = builder
          .comment(
              "Change to false to return minecarts to vanilla player vs cart collision behavior",
              "In vanilla minecarts are ghost-like can be walked through",
              "but making carts solid also makes them harder to push by hand")
          .define("solidCarts", true);

      this.cartsCollideWithItems = builder
          .comment("Change to 'true' to restore minecart collisions with dropped items")
          .define("cartsCollideWithItems", false);

      this.boreMiningSpeedMultiplier = builder
          .comment(
              "Adjust the speed at which the Bore mines blocks, min=0.1, default=1.0, max=50.0")
          .defineInRange("boreMiningSpeedMultiplier", 1.0D, 0.1D, 50.0D);

      this.boreDestroysBlocks = builder
          .comment(
              "Change to true to cause the Bore to destroy the blocks it mines instead of dropping them")
          .define("boreDestroysBlocks", false);

      this.boreMinesAllBlocks = builder
          .comment(
              "Change to false to enable mining checks, use true setting with caution, especially on servers")
          .define("boreMinesAllBlocks", true);

      this.cartsBreakOnDrop = builder
          .comment("Change to \"true\" to restore vanilla behavior")
          .define("cartsBreakOnDrop", false);

      this.steamLocomotiveEfficiency = builder
          .comment("Adjust the multiplier used when calculating fuel use.")
          .defineInRange("steamLocomotiveEfficiency", 3.0F, 0.2F, 12F);

      this.tankCartFluidTransferRate = builder
          .comment(
              "Tank cart fluid transfer rate in milli-buckets per tick, min=4, default=32, max=2048.")
          .defineInRange("tankCartFluidTransferRate", 32, 4, 2048);

      this.tankCartFluidCapacity = builder
          .comment("Tank cart capacity in buckets, min=4, default=32, max=512")
          .defineInRange("tankCartFluidCapacity", 32, 4, 512);

      this.tankStackingEnabled = builder
          .comment("Change to false to disable the stacking of tanks.")
          .define("tankStackingEnabled", false);

      this.maxTankSize = builder
          .comment(
              "Allows you to set the max tank base dimension, valid values are 3, 5, 7, and 9.")
          .defineInRange("maxTankSize", 9, 3, 9);

      this.tankCapacityPerBlock = builder
          .comment(
              "Allows you to set how many buckets (1000 milliBuckets) of fluid each iron tank block can carry")
          .defineInRange("tankCapacityPerBlock", 16, 1, 1600);

      this.waterCollectionRate = builder
          .comment(
              "The base rate of water in milliBuckets that can be gathered from the local environment, applied every 16 ticks to every block that can see the sky")
          .defineInRange("waterCollectionRate", 4, 0, 1000);

      this.maxLauncherTrackForce = builder
          .comment("change the value to your desired max launch rail force")
          .defineInRange("maxLauncherTrackForce", 30, 5, 50);

      this.cartDispenserDelay = builder
          .comment("Set the minimum number of seconds between cart dispensing")
          .defineInRange("cartDispenserDelay", 0, 0, Integer.MAX_VALUE);

      builder.push("charge");
      {
        this.lossMultiplier = builder
            .comment("adjust the losses for the Charge network")
            .defineInRange("lossMultiplier", 1.0D, 0.2D, 10.0D);
      }
      builder.pop();

      builder.push("steam");
      {
        this.fuelMultiplier = builder
            .comment("adjust the heat value of Fuel in a Boiler")
            .defineInRange("fuelMultiplier", 1.0F, 0.2F, 10F);
        this.fuelPerSteamMultiplier = builder
            .comment("Adjust the amount of fuel used to create steam.")
            .defineInRange("fuelPerSteamMultiplier", 1.0F, 0.2F, 6.0F);
      }
      builder.pop();

      this.changeDungeonLoot = builder
          .comment("change the vanilla dungeon loot")
          .define("changeDungeonLoot", true);
    }
  }

  public static class Common {

    public final BooleanValue seasonsEnabled;
    public final IntValue christmas;
    public final IntValue halloween;
    public final IntValue harvest;

    private Common(Builder builder) {
      builder.comment("General configuration settings")
          .push("common");

      this.seasonsEnabled = builder
          .comment("Enable season-based item & train effects?")
          .define("seasonsEnabled", true);

      this.christmas = builder
          .comment("Controls whether Christmas mode is (0) enabled, (1) forced, or (2) disabled")
          .defineInRange("christmas", 0, 0, 2);

      this.halloween = builder
          .comment("Controls whether Halloween mode is (0) enabled, (1) forced, or (2) disabled")
          .defineInRange("halloween", 0, 0, 2);

      this.harvest = builder
          .comment("Controls whether Harvest mode is (0) enabled, (1) forced, or (2) disabled")
          .defineInRange("harvest", 0, 0, 2);

      builder.pop();
    }
  }

  public static class Client {

    public final BooleanValue ghostTrainEnabled;
    public final BooleanValue polarExpressEnabled;
    public final BooleanValue showMessageBeta;
    public final IntValue locomotiveLightLevel;

    private Client(Builder builder) {
      this.ghostTrainEnabled = builder
          .comment("Change to false to disable Ghost Train rendering")
          .define("ghostTrainEnabled", true);

      this.polarExpressEnabled = builder
          .comment("Change to false to disable Polar Express (snow) rendering")
          .define("polarExpressEnabled", true);

      this.locomotiveLightLevel = builder
          .comment(
              "Change '14' to a number ranging from '0' to '15' to represent the dynamic lighting of the locomotive when Dynamic Lights mod is present.",
              "If it is '0' then locomotive lightning will be disabled.")
          .defineInRange("locomotiveLightLevel", 14, 0, 15);

      this.showMessageBeta = builder
          .comment("Set to false to disable the message that informs you that you are using a beta version.")
          .define("showMessageBeta", true);
    }
  }

}
