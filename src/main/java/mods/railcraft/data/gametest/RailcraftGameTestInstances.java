package mods.railcraft.data.gametest;

import java.util.function.Function;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.data.gametest.instances.BlockSignalTestInstance;
import mods.railcraft.data.gametest.instances.DetectorTrackTestInstance;
import mods.railcraft.data.gametest.instances.ForceTrackTestInstance;
import mods.railcraft.data.gametest.instances.InterlockTestInstance;
import mods.railcraft.data.gametest.instances.RoutingTestInstance;
import mods.railcraft.data.gametest.instances.RoutingTrackTestInstance;
import mods.railcraft.data.gametest.instances.block_signal_relay_box.BlockSignalRelayBoxComplexTestInstance;
import mods.railcraft.data.gametest.instances.block_signal_relay_box.BlockSignalRelayBoxTestInstance;
import mods.railcraft.data.gametest.instances.cart_dispenser.CartDispenserDispenseTestInstance;
import mods.railcraft.data.gametest.instances.cart_dispenser.CartDispenserPickUpTestInstance;
import mods.railcraft.data.gametest.instances.disembarking_track.DisembarkingTrackActiveTestInstance;
import mods.railcraft.data.gametest.instances.disembarking_track.DisembarkingTrackPassiveTestInstance;
import mods.railcraft.data.gametest.instances.dual_block_signal.DualBlockSignalPrimary;
import mods.railcraft.data.gametest.instances.dual_block_signal.DualBlockSignalSecondary;
import mods.railcraft.data.gametest.instances.elevator_track.ElevatorTrackDownTestInstance;
import mods.railcraft.data.gametest.instances.elevator_track.ElevatorTrackUpTestInstance;
import mods.railcraft.data.gametest.instances.embarking_track.EmbarkingTrackActiveTestInstance;
import mods.railcraft.data.gametest.instances.embarking_track.EmbarkingTrackPassiveTestInstance;
import mods.railcraft.data.gametest.instances.gated_track.GatedTrackActiveTestInstance;
import mods.railcraft.data.gametest.instances.gated_track.GatedTrackPassiveTestInstance;
import mods.railcraft.data.gametest.instances.junction_track.JunctionTrackNorthTestInstance;
import mods.railcraft.data.gametest.instances.junction_track.JunctionTrackWestTestInstance;
import mods.railcraft.data.gametest.instances.launcher_track.LauncherTrackActiveTestInstance;
import mods.railcraft.data.gametest.instances.launcher_track.LauncherTrackPassiveTestInstance;
import mods.railcraft.data.gametest.instances.one_way_track.OneWayTrackActiveTestInstance;
import mods.railcraft.data.gametest.instances.one_way_track.OneWayTrackPassiveTestInstance;
import mods.railcraft.data.gametest.instances.tank.TankEntityInsideTestInstance;
import mods.railcraft.data.gametest.instances.tank.TankEntityOutsideTestInstance;
import mods.railcraft.data.gametest.instances.tank.TankKeepsFluidTestInstance;
import mods.railcraft.data.gametest.instances.tank.TankReformsTestInstance;
import mods.railcraft.data.gametest.instances.turnout_track.TurnoutTrackActiveReverseTestInstance;
import mods.railcraft.data.gametest.instances.turnout_track.TurnoutTrackActiveTestInstance;
import mods.railcraft.data.gametest.instances.turnout_track.TurnoutTrackPassiveReverseTestInstance;
import mods.railcraft.data.gametest.instances.turnout_track.TurnoutTrackPassiveTestInstance;
import mods.railcraft.data.gametest.instances.wye_track.WyeTrackActiveTestInstance;
import mods.railcraft.data.gametest.instances.wye_track.WyeTrackPassiveTestInstance;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.gametest.framework.GameTestInstance;
import net.minecraft.gametest.framework.TestData;
import net.minecraft.gametest.framework.TestEnvironmentDefinition;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Rotation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

public class RailcraftGameTestInstances {

  private static final DeferredRegister<MapCodec<? extends GameTestInstance>> deferredRegister =
      DeferredRegister.create(BuiltInRegistries.TEST_INSTANCE_TYPE, RailcraftConstants.ID);

  public static void register(IEventBus modEventBus) {
    deferredRegister.register(modEventBus);
    deferredRegister.register("detector_track", () -> DetectorTrackTestInstance.CODEC);
    deferredRegister.register("force_track_active", () -> ForceTrackTestInstance.CODEC);
    deferredRegister.register("complex_routing", () -> RoutingTestInstance.CODEC);
    deferredRegister.register("interlock", () -> InterlockTestInstance.CODEC);
    deferredRegister.register("cart_dispenser_dispense", () -> CartDispenserDispenseTestInstance.CODEC);
    deferredRegister.register("cart_dispenser_pick_up", () -> CartDispenserPickUpTestInstance.CODEC);
    deferredRegister.register("disembarking_track_active", () -> DisembarkingTrackActiveTestInstance.CODEC);
    deferredRegister.register("disembarking_track_passive", () -> DisembarkingTrackPassiveTestInstance.CODEC);
    deferredRegister.register("embarking_track_active", () -> EmbarkingTrackActiveTestInstance.CODEC);
    deferredRegister.register("embarking_track_passive", () -> EmbarkingTrackPassiveTestInstance.CODEC);
    deferredRegister.register("gated_track_active", () -> GatedTrackActiveTestInstance.CODEC);
    deferredRegister.register("gated_track_passive", () -> GatedTrackPassiveTestInstance.CODEC);
    deferredRegister.register("launcher_track_active", () -> LauncherTrackActiveTestInstance.CODEC);
    deferredRegister.register("launcher_track_passive", () -> LauncherTrackPassiveTestInstance.CODEC);
    deferredRegister.register("one_way_track_active", () -> OneWayTrackActiveTestInstance.CODEC);
    deferredRegister.register("one_way_track_passive", () -> OneWayTrackPassiveTestInstance.CODEC);
    deferredRegister.register("wye_track_active", () -> WyeTrackActiveTestInstance.CODEC);
    deferredRegister.register("wye_track_passive", () -> WyeTrackPassiveTestInstance.CODEC);
    deferredRegister.register("turnout_track_active", () -> TurnoutTrackActiveTestInstance.CODEC);
    deferredRegister.register("turnout_track_passive", () -> TurnoutTrackPassiveTestInstance.CODEC);
    deferredRegister.register("turnout_track_active_reverse", () -> TurnoutTrackActiveReverseTestInstance.CODEC);
    deferredRegister.register("turnout_track_passive_reverse", () -> TurnoutTrackPassiveReverseTestInstance.CODEC);
    deferredRegister.register("elevator_track_up", () -> ElevatorTrackUpTestInstance.CODEC);
    deferredRegister.register("elevator_track_down", () -> ElevatorTrackDownTestInstance.CODEC);
    deferredRegister.register("junction_track_west", () -> JunctionTrackWestTestInstance.CODEC);
    deferredRegister.register("junction_track_north", () -> JunctionTrackNorthTestInstance.CODEC);
    deferredRegister.register("block_signal_relay_box", () -> BlockSignalRelayBoxTestInstance.CODEC);
    deferredRegister.register("block_signal_relay_box_complex", () -> BlockSignalRelayBoxComplexTestInstance.CODEC);
    deferredRegister.register("block_signal", () -> BlockSignalTestInstance.CODEC);
    deferredRegister.register("routing_track", () -> RoutingTrackTestInstance.CODEC);
    deferredRegister.register("dual_block_signal_primary", () -> DualBlockSignalPrimary.CODEC);
    deferredRegister.register("dual_block_signal_secondary", () -> DualBlockSignalSecondary.CODEC);
    deferredRegister.register("tank_keeps_fluid", () -> TankKeepsFluidTestInstance.CODEC);
    deferredRegister.register("tank_entity_inside", () -> TankEntityInsideTestInstance.CODEC);
    deferredRegister.register("tank_entity_outside", () -> TankEntityOutsideTestInstance.CODEC);
    deferredRegister.register("tank_reforms", () -> TankReformsTestInstance.CODEC);
  }

  public static void bootstrap(BootstrapContext<GameTestInstance> bootstrap) {
    registerTestInstance(bootstrap, "detector_track", DetectorTrackTestInstance::new);
    registerTestInstance(bootstrap, "force_track_active", ForceTrackTestInstance::new);
    registerTestInstance(bootstrap, "complex_routing", RoutingTestInstance::new);
    registerTestInstance(bootstrap, "interlock", InterlockTestInstance::new);
    registerTestInstance(bootstrap, "cart_dispenser_dispense", "cart_dispenser", CartDispenserDispenseTestInstance::new);
    registerTestInstance(bootstrap, "cart_dispenser_pick_up", "cart_dispenser", CartDispenserPickUpTestInstance::new);
    registerTestInstance(bootstrap, "disembarking_track_active", DisembarkingTrackActiveTestInstance::new);
    registerTestInstance(bootstrap, "disembarking_track_passive", DisembarkingTrackPassiveTestInstance::new);
    registerTestInstance(bootstrap, "embarking_track_active", EmbarkingTrackActiveTestInstance::new);
    registerTestInstance(bootstrap, "embarking_track_passive", EmbarkingTrackPassiveTestInstance::new);
    registerTestInstance(bootstrap, "gated_track_active", GatedTrackActiveTestInstance::new);
    registerTestInstance(bootstrap, "gated_track_passive", GatedTrackPassiveTestInstance::new);
    registerTestInstance(bootstrap, "launcher_track_active", LauncherTrackActiveTestInstance::new);
    registerTestInstance(bootstrap, "launcher_track_passive", LauncherTrackPassiveTestInstance::new);
    registerTestInstance(bootstrap, "one_way_track_active", OneWayTrackActiveTestInstance::new);
    registerTestInstance(bootstrap, "one_way_track_passive", OneWayTrackPassiveTestInstance::new);
    registerTestInstance(bootstrap, "wye_track_active", WyeTrackActiveTestInstance::new);
    registerTestInstance(bootstrap, "wye_track_passive", WyeTrackPassiveTestInstance::new);
    registerTestInstance(bootstrap, "turnout_track_active", TurnoutTrackActiveTestInstance::new);
    registerTestInstance(bootstrap, "turnout_track_passive", TurnoutTrackPassiveTestInstance::new);
    registerTestInstance(bootstrap, "turnout_track_active_reverse", TurnoutTrackActiveReverseTestInstance::new);
    registerTestInstance(bootstrap, "turnout_track_passive_reverse", TurnoutTrackPassiveReverseTestInstance::new);
    registerTestInstance(bootstrap, "elevator_track_up", ElevatorTrackUpTestInstance::new);
    registerTestInstance(bootstrap, "elevator_track_down", ElevatorTrackDownTestInstance::new);
    registerTestInstance(bootstrap, "junction_track_west", "junction_track", JunctionTrackWestTestInstance::new);
    registerTestInstance(bootstrap, "junction_track_north", "junction_track", JunctionTrackNorthTestInstance::new);

    registerTestInstance(bootstrap, "block_signal_relay_box_no_cart", "block_signal_relay_box",
        t -> new BlockSignalRelayBoxTestInstance(false, t));
    registerTestInstance(bootstrap, "block_signal_relay_box_with_cart", "block_signal_relay_box",
        t -> new BlockSignalRelayBoxTestInstance(true, t));
    registerTestInstance(bootstrap, "block_signal_relay_box_complex_no_cart", "block_signal_relay_box_complex",
        t -> new BlockSignalRelayBoxComplexTestInstance(false, t));
    registerTestInstance(bootstrap, "block_signal_relay_box_complex_with_cart", "block_signal_relay_box_complex",
        t -> new BlockSignalRelayBoxComplexTestInstance(true, t));
    registerTestInstance(bootstrap, "block_signal_no_cart", "block_signal",
        t -> new BlockSignalTestInstance(false, t));
    registerTestInstance(bootstrap, "block_signal_with_cart", "block_signal",
        t -> new BlockSignalTestInstance(true, t));
    registerTestInstance(bootstrap, "routing_track_without_pulled_lever", "routing_track",
        t -> new RoutingTrackTestInstance(false, t));
    registerTestInstance(bootstrap, "routing_track_with_pulled_lever", "routing_track",
        t -> new RoutingTrackTestInstance(true, t));
    registerTestInstance(bootstrap, "dual_block_signal_no_cart", "dual_block_signal",
        t -> new DualBlockSignalPrimary(false, t));
    registerTestInstance(bootstrap, "dual_block_signal_with_cart", "dual_block_signal",
        t -> new DualBlockSignalPrimary(true, t));
    registerTestInstance(bootstrap, "dual_block_signal_secondary_green", "dual_block_signal",
        t -> new DualBlockSignalSecondary(false, t));
    registerTestInstance(bootstrap, "dual_block_signal_secondary_yellow", "dual_block_signal",
        t -> new DualBlockSignalSecondary(true, t));

    registerTestInstance(bootstrap, "tank_keeps_fluid", "iron_tank", TankKeepsFluidTestInstance::new);
    registerTestInstance(bootstrap, "tank_entity_inside", "iron_tank", TankEntityInsideTestInstance::new);
    registerTestInstance(bootstrap, "tank_entity_outside", "iron_tank", TankEntityOutsideTestInstance::new);
    registerTestInstance(bootstrap, "tank_reforms", "iron_tank", TankReformsTestInstance::new);
  }

  private static <T extends GameTestInstance> void registerTestInstance(
      BootstrapContext<GameTestInstance> bootstrap,
      String name, Function<TestData<Holder<TestEnvironmentDefinition<?>>>, T> factory) {
    registerTestInstance(bootstrap, name, name, factory);
  }

  private static <T extends GameTestInstance> void registerTestInstance(
      BootstrapContext<GameTestInstance> bootstrap,
      String path, String template,
      Function<TestData<Holder<TestEnvironmentDefinition<?>>>, T> factory) {
    bootstrap.register(
        ResourceKey.create(Registries.TEST_INSTANCE, RailcraftConstants.id(path)),
        factory.apply(getDefaultTestData(bootstrap, template))
    );
  }

  private static TestData<Holder<TestEnvironmentDefinition<?>>> getDefaultTestData(
      BootstrapContext<GameTestInstance> bootstrap, String template) {
    var environments = bootstrap.lookup(Registries.TEST_ENVIRONMENT);
    return new TestData<>(
        environments.getOrThrow(RailcraftTestEnvironments.DEFAULT),
        RailcraftConstants.id("test/" + template),
        400,
        0,
        true,
        Rotation.NONE,
        false,
        1,
        1,
        true,
        0
    );
  }

  public static <T extends GameTestInstance> MapCodec<T> defaultCodec(
      Function<TestData<Holder<TestEnvironmentDefinition<?>>>, T> factory) {
    return RecordCodecBuilder.mapCodec(instance -> instance.group(
        TestData.CODEC.forGetter(t -> t.info)
    ).apply(instance, factory));
  }
}
