package mods.railcraft.data.tags;

import java.util.concurrent.CompletableFuture;
import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.tags.RailcraftTags;
import mods.railcraft.world.level.block.DecorativeBlock;
import mods.railcraft.world.level.block.RailcraftBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;

public class RailcraftBlockTagsProvider extends BlockTagsProvider {

  public RailcraftBlockTagsProvider(PackOutput packOutput,
      CompletableFuture<HolderLookup.Provider> registries) {
    super(packOutput, registries, RailcraftConstants.ID);
  }

  @SuppressWarnings("unchecked")
  @Override
  protected void addTags(HolderLookup.Provider registries) {
    this.tag(RailcraftTags.Blocks.BALLAST)
        .addTag(Tags.Blocks.GRAVELS);
    this.tag(RailcraftTags.Blocks.TRACK_UNDERCUTTER_INVALID_BALLAST)
        .addTags(Tags.Blocks.SANDS, BlockTags.SAND);
    this.tag(RailcraftTags.Blocks.SWITCH_TRACK_ACTUATOR)
        .add(RailcraftBlocks.SWITCH_TRACK_LEVER.key())
        .add(RailcraftBlocks.SWITCH_TRACK_MOTOR.key())
        .add(RailcraftBlocks.SWITCH_TRACK_ROUTER.key());
    this.tag(BlockTags.RAILS)
        .addTag(RailcraftTags.Blocks.ABANDONED_TRACK)
        .addTag(RailcraftTags.Blocks.ELECTRIC_TRACK)
        .addTag(RailcraftTags.Blocks.HIGH_SPEED_TRACK)
        .addTag(RailcraftTags.Blocks.HIGH_SPEED_ELECTRIC_TRACK)
        .addTag(RailcraftTags.Blocks.IRON_TRACK)
        .addTag(RailcraftTags.Blocks.REINFORCED_TRACK)
        .addTag(RailcraftTags.Blocks.STRAP_IRON_TRACK)
        .add(RailcraftBlocks.FORCE_TRACK.key());
    this.tag(BlockTags.CLIMBABLE)
        .add(RailcraftBlocks.ELEVATOR_TRACK.key());
    this.tag(RailcraftTags.Blocks.ASPECT_EMITTER)
        .add(
            RailcraftBlocks.SIGNAL_CAPACITOR_BOX.key(),
            RailcraftBlocks.SIGNAL_RECEIVER_BOX.key(),
            RailcraftBlocks.SIGNAL_BLOCK_RELAY_BOX.key(),
            RailcraftBlocks.SIGNAL_SEQUENCER_BOX.key(),
            RailcraftBlocks.TOKEN_SIGNAL_BOX.key());
    this.tag(RailcraftTags.Blocks.ASPECT_RECEIVER)
        .add(
            RailcraftBlocks.SIGNAL_CAPACITOR_BOX.key(),
            RailcraftBlocks.SIGNAL_CONTROLLER_BOX.key(),
            RailcraftBlocks.SIGNAL_INTERLOCK_BOX.key(),
            RailcraftBlocks.SIGNAL_SEQUENCER_BOX.key());
    this.tag(RailcraftTags.Blocks.ABANDONED_TRACK)
        .add(
            RailcraftBlocks.ABANDONED_TRACK.key(),
            RailcraftBlocks.ABANDONED_LOCKING_TRACK.key(),
            RailcraftBlocks.ABANDONED_DELAYED_LOCKING_TRACK.key(),
            RailcraftBlocks.ABANDONED_MESSENGER_TRACK.key(),
            RailcraftBlocks.ABANDONED_ACTIVATOR_TRACK.key(),
            RailcraftBlocks.ABANDONED_BOOSTER_TRACK.key(),
            RailcraftBlocks.ABANDONED_CONTROL_TRACK.key(),
            RailcraftBlocks.ABANDONED_GATED_TRACK.key(),
            RailcraftBlocks.ABANDONED_DETECTOR_TRACK.key(),
            RailcraftBlocks.ABANDONED_COUPLER_TRACK.key(),
            RailcraftBlocks.ABANDONED_EMBARKING_TRACK.key(),
            RailcraftBlocks.ABANDONED_DISEMBARKING_TRACK.key(),
            RailcraftBlocks.ABANDONED_DUMPING_TRACK.key(),
            RailcraftBlocks.ABANDONED_TURNOUT_TRACK.key(),
            RailcraftBlocks.ABANDONED_WYE_TRACK.key(),
            RailcraftBlocks.ABANDONED_JUNCTION_TRACK.key(),
            RailcraftBlocks.ABANDONED_LAUNCHER_TRACK.key(),
            RailcraftBlocks.ABANDONED_ONE_WAY_TRACK.key(),
            RailcraftBlocks.ABANDONED_WHISTLE_TRACK.key(),
            RailcraftBlocks.ABANDONED_LOCOMOTIVE_TRACK.key(),
            RailcraftBlocks.ABANDONED_THROTTLE_TRACK.key(),
            RailcraftBlocks.ABANDONED_ROUTING_TRACK.key(),
            RailcraftBlocks.ABANDONED_BUFFER_STOP_TRACK.key());
    this.tag(RailcraftTags.Blocks.ELECTRIC_TRACK)
        .add(
            RailcraftBlocks.ELECTRIC_TRACK.key(),
            RailcraftBlocks.ELECTRIC_LOCKING_TRACK.key(),
            RailcraftBlocks.ELECTRIC_DELAYED_LOCKING_TRACK.key(),
            RailcraftBlocks.ELECTRIC_MESSENGER_TRACK.key(),
            RailcraftBlocks.ELECTRIC_ACTIVATOR_TRACK.key(),
            RailcraftBlocks.ELECTRIC_BOOSTER_TRACK.key(),
            RailcraftBlocks.ELECTRIC_CONTROL_TRACK.key(),
            RailcraftBlocks.ELECTRIC_GATED_TRACK.key(),
            RailcraftBlocks.ELECTRIC_DETECTOR_TRACK.key(),
            RailcraftBlocks.ELECTRIC_COUPLER_TRACK.key(),
            RailcraftBlocks.ELECTRIC_EMBARKING_TRACK.key(),
            RailcraftBlocks.ELECTRIC_DISEMBARKING_TRACK.key(),
            RailcraftBlocks.ELECTRIC_DUMPING_TRACK.key(),
            RailcraftBlocks.ELECTRIC_TURNOUT_TRACK.key(),
            RailcraftBlocks.ELECTRIC_WYE_TRACK.key(),
            RailcraftBlocks.ELECTRIC_JUNCTION_TRACK.key(),
            RailcraftBlocks.ELECTRIC_LAUNCHER_TRACK.key(),
            RailcraftBlocks.ELECTRIC_ONE_WAY_TRACK.key(),
            RailcraftBlocks.ELECTRIC_WHISTLE_TRACK.key(),
            RailcraftBlocks.ELECTRIC_LOCOMOTIVE_TRACK.key(),
            RailcraftBlocks.ELECTRIC_THROTTLE_TRACK.key(),
            RailcraftBlocks.ELECTRIC_ROUTING_TRACK.key(),
            RailcraftBlocks.ELECTRIC_BUFFER_STOP_TRACK.key());
    this.tag(RailcraftTags.Blocks.HIGH_SPEED_TRACK)
        .add(
            RailcraftBlocks.HIGH_SPEED_TRACK.key(),
            RailcraftBlocks.HIGH_SPEED_TRANSITION_TRACK.key(),
            RailcraftBlocks.HIGH_SPEED_LOCKING_TRACK.key(),
            RailcraftBlocks.HIGH_SPEED_DELAYED_LOCKING_TRACK.key(),
            RailcraftBlocks.HIGH_SPEED_MESSENGER_TRACK.key(),
            RailcraftBlocks.HIGH_SPEED_ACTIVATOR_TRACK.key(),
            RailcraftBlocks.HIGH_SPEED_BOOSTER_TRACK.key(),
            RailcraftBlocks.HIGH_SPEED_DETECTOR_TRACK.key(),
            RailcraftBlocks.HIGH_SPEED_TURNOUT_TRACK.key(),
            RailcraftBlocks.HIGH_SPEED_WYE_TRACK.key(),
            RailcraftBlocks.HIGH_SPEED_JUNCTION_TRACK.key(),
            RailcraftBlocks.HIGH_SPEED_WHISTLE_TRACK.key(),
            RailcraftBlocks.HIGH_SPEED_LOCOMOTIVE_TRACK.key(),
            RailcraftBlocks.HIGH_SPEED_THROTTLE_TRACK.key());
    this.tag(RailcraftTags.Blocks.HIGH_SPEED_ELECTRIC_TRACK)
        .add(
            RailcraftBlocks.HIGH_SPEED_ELECTRIC_TRACK.key(),
            RailcraftBlocks.HIGH_SPEED_ELECTRIC_TRANSITION_TRACK.key(),
            RailcraftBlocks.HIGH_SPEED_ELECTRIC_LOCKING_TRACK.key(),
            RailcraftBlocks.HIGH_SPEED_ELECTRIC_DELAYED_LOCKING_TRACK.key(),
            RailcraftBlocks.HIGH_SPEED_ELECTRIC_MESSENGER_TRACK.key(),
            RailcraftBlocks.HIGH_SPEED_ELECTRIC_ACTIVATOR_TRACK.key(),
            RailcraftBlocks.HIGH_SPEED_ELECTRIC_BOOSTER_TRACK.key(),
            RailcraftBlocks.HIGH_SPEED_ELECTRIC_DETECTOR_TRACK.key(),
            RailcraftBlocks.HIGH_SPEED_ELECTRIC_TURNOUT_TRACK.key(),
            RailcraftBlocks.HIGH_SPEED_ELECTRIC_WYE_TRACK.key(),
            RailcraftBlocks.HIGH_SPEED_ELECTRIC_JUNCTION_TRACK.key(),
            RailcraftBlocks.HIGH_SPEED_ELECTRIC_WHISTLE_TRACK.key(),
            RailcraftBlocks.HIGH_SPEED_ELECTRIC_LOCOMOTIVE_TRACK.key(),
            RailcraftBlocks.HIGH_SPEED_ELECTRIC_THROTTLE_TRACK.key());
    this.tag(RailcraftTags.Blocks.IRON_TRACK)
        .add(
            RailcraftBlocks.IRON_LOCKING_TRACK.key(),
            RailcraftBlocks.IRON_DELAYED_LOCKING_TRACK.key(),
            RailcraftBlocks.IRON_MESSENGER_TRACK.key(),
            RailcraftBlocks.IRON_ACTIVATOR_TRACK.key(),
            RailcraftBlocks.IRON_BOOSTER_TRACK.key(),
            RailcraftBlocks.IRON_CONTROL_TRACK.key(),
            RailcraftBlocks.IRON_GATED_TRACK.key(),
            RailcraftBlocks.IRON_DETECTOR_TRACK.key(),
            RailcraftBlocks.IRON_COUPLER_TRACK.key(),
            RailcraftBlocks.IRON_EMBARKING_TRACK.key(),
            RailcraftBlocks.IRON_DISEMBARKING_TRACK.key(),
            RailcraftBlocks.IRON_DUMPING_TRACK.key(),
            RailcraftBlocks.IRON_TURNOUT_TRACK.key(),
            RailcraftBlocks.IRON_WYE_TRACK.key(),
            RailcraftBlocks.IRON_JUNCTION_TRACK.key(),
            RailcraftBlocks.IRON_LAUNCHER_TRACK.key(),
            RailcraftBlocks.IRON_ONE_WAY_TRACK.key(),
            RailcraftBlocks.IRON_WHISTLE_TRACK.key(),
            RailcraftBlocks.IRON_LOCOMOTIVE_TRACK.key(),
            RailcraftBlocks.IRON_THROTTLE_TRACK.key(),
            RailcraftBlocks.IRON_ROUTING_TRACK.key(),
            RailcraftBlocks.IRON_BUFFER_STOP_TRACK.key());
    this.tag(RailcraftTags.Blocks.REINFORCED_TRACK)
        .add(
            RailcraftBlocks.REINFORCED_TRACK.key(),
            RailcraftBlocks.REINFORCED_LOCKING_TRACK.key(),
            RailcraftBlocks.REINFORCED_DELAYED_LOCKING_TRACK.key(),
            RailcraftBlocks.REINFORCED_MESSENGER_TRACK.key(),
            RailcraftBlocks.REINFORCED_ACTIVATOR_TRACK.key(),
            RailcraftBlocks.REINFORCED_BOOSTER_TRACK.key(),
            RailcraftBlocks.REINFORCED_CONTROL_TRACK.key(),
            RailcraftBlocks.REINFORCED_GATED_TRACK.key(),
            RailcraftBlocks.REINFORCED_DETECTOR_TRACK.key(),
            RailcraftBlocks.REINFORCED_COUPLER_TRACK.key(),
            RailcraftBlocks.REINFORCED_EMBARKING_TRACK.key(),
            RailcraftBlocks.REINFORCED_DISEMBARKING_TRACK.key(),
            RailcraftBlocks.REINFORCED_DUMPING_TRACK.key(),
            RailcraftBlocks.REINFORCED_TURNOUT_TRACK.key(),
            RailcraftBlocks.REINFORCED_WYE_TRACK.key(),
            RailcraftBlocks.REINFORCED_JUNCTION_TRACK.key(),
            RailcraftBlocks.REINFORCED_LAUNCHER_TRACK.key(),
            RailcraftBlocks.REINFORCED_ONE_WAY_TRACK.key(),
            RailcraftBlocks.REINFORCED_WHISTLE_TRACK.key(),
            RailcraftBlocks.REINFORCED_LOCOMOTIVE_TRACK.key(),
            RailcraftBlocks.REINFORCED_THROTTLE_TRACK.key(),
            RailcraftBlocks.REINFORCED_ROUTING_TRACK.key(),
            RailcraftBlocks.REINFORCED_BUFFER_STOP_TRACK.key());
    this.tag(RailcraftTags.Blocks.STRAP_IRON_TRACK)
        .add(
            RailcraftBlocks.STRAP_IRON_TRACK.key(),
            RailcraftBlocks.STRAP_IRON_LOCKING_TRACK.key(),
            RailcraftBlocks.STRAP_IRON_DELAYED_LOCKING_TRACK.key(),
            RailcraftBlocks.STRAP_IRON_MESSENGER_TRACK.key(),
            RailcraftBlocks.STRAP_IRON_ACTIVATOR_TRACK.key(),
            RailcraftBlocks.STRAP_IRON_BOOSTER_TRACK.key(),
            RailcraftBlocks.STRAP_IRON_CONTROL_TRACK.key(),
            RailcraftBlocks.STRAP_IRON_GATED_TRACK.key(),
            RailcraftBlocks.STRAP_IRON_DETECTOR_TRACK.key(),
            RailcraftBlocks.STRAP_IRON_COUPLER_TRACK.key(),
            RailcraftBlocks.STRAP_IRON_EMBARKING_TRACK.key(),
            RailcraftBlocks.STRAP_IRON_DISEMBARKING_TRACK.key(),
            RailcraftBlocks.STRAP_IRON_DUMPING_TRACK.key(),
            RailcraftBlocks.STRAP_IRON_TURNOUT_TRACK.key(),
            RailcraftBlocks.STRAP_IRON_WYE_TRACK.key(),
            RailcraftBlocks.STRAP_IRON_JUNCTION_TRACK.key(),
            RailcraftBlocks.STRAP_IRON_LAUNCHER_TRACK.key(),
            RailcraftBlocks.STRAP_IRON_ONE_WAY_TRACK.key(),
            RailcraftBlocks.STRAP_IRON_WHISTLE_TRACK.key(),
            RailcraftBlocks.STRAP_IRON_LOCOMOTIVE_TRACK.key(),
            RailcraftBlocks.STRAP_IRON_THROTTLE_TRACK.key(),
            RailcraftBlocks.STRAP_IRON_ROUTING_TRACK.key(),
            RailcraftBlocks.STRAP_IRON_BUFFER_STOP_TRACK.key());

    this.tag(RailcraftTags.Blocks.SIGNAL)
        .add(
            RailcraftBlocks.BLOCK_SIGNAL.key(),
            RailcraftBlocks.DISTANT_SIGNAL.key(),
            RailcraftBlocks.TOKEN_SIGNAL.key(),
            RailcraftBlocks.DUAL_BLOCK_SIGNAL.key(),
            RailcraftBlocks.DUAL_DISTANT_SIGNAL.key(),
            RailcraftBlocks.DUAL_TOKEN_SIGNAL.key());
    this.tag(RailcraftTags.Blocks.MINEABLE_WITH_CROWBAR)
        .add(RailcraftBlocks.ANALOG_SIGNAL_CONTROLLER_BOX.key(), RailcraftBlocks.ELEVATOR_TRACK.key())
        .addTag(RailcraftTags.Blocks.ASPECT_EMITTER)
        .addTag(RailcraftTags.Blocks.ASPECT_RECEIVER)
        .addTag(RailcraftTags.Blocks.SIGNAL)
        .addTag(RailcraftTags.Blocks.SWITCH_TRACK_ACTUATOR)
        .addTag(BlockTags.RAILS)
        .addTag(RailcraftTags.Blocks.DETECTOR);

    this.tag(BlockTags.ANVIL)
        .add(
            RailcraftBlocks.STEEL_ANVIL.key(),
            RailcraftBlocks.CHIPPED_STEEL_ANVIL.key(),
            RailcraftBlocks.DAMAGED_STEEL_ANVIL.key());

    this.tag(BlockTags.MINEABLE_WITH_AXE)
        .add(RailcraftBlocks.MANUAL_ROLLING_MACHINE.key())
        .add(RailcraftBlocks.WATER_TANK_SIDING.key());

    this.tag(BlockTags.MINEABLE_WITH_PICKAXE)
        .add(RailcraftBlocks.FIRESTONE_ORE.key())
        .add(RailcraftBlocks.LOW_PRESSURE_STEAM_BOILER_TANK.key())
        .add(RailcraftBlocks.HIGH_PRESSURE_STEAM_BOILER_TANK.key())
        .add(RailcraftBlocks.SOLID_FUELED_FIREBOX.key())
        .add(RailcraftBlocks.FLUID_FUELED_FIREBOX.key())
        .add(RailcraftBlocks.STEAM_TURBINE.key())
        .add(RailcraftBlocks.HOBBYIST_STEAM_ENGINE.key())
        .add(RailcraftBlocks.COMMERCIAL_STEAM_ENGINE.key())
        .add(RailcraftBlocks.INDUSTRIAL_STEAM_ENGINE.key())
        .add(RailcraftBlocks.COKE_OVEN_BRICKS.key())
        .add(RailcraftBlocks.BLAST_FURNACE_BRICKS.key())
        .add(RailcraftBlocks.POWERED_ROLLING_MACHINE.key())
        .add(RailcraftBlocks.CRUSHER.key())
        .add(RailcraftBlocks.STEAM_OVEN.key())
        .add(RailcraftBlocks.FEED_STATION.key())
        .add(RailcraftBlocks.CHIMNEY.key())
        .add(RailcraftBlocks.LOGBOOK.key())
        .add(RailcraftBlocks.COAL_COKE_BLOCK.key())
        .add(RailcraftBlocks.CRUSHED_OBSIDIAN.key())
        .add(RailcraftBlocks.ADVANCED_ITEM_LOADER.key())
        .add(RailcraftBlocks.ADVANCED_ITEM_UNLOADER.key())
        .add(RailcraftBlocks.ITEM_LOADER.key())
        .add(RailcraftBlocks.ITEM_UNLOADER.key())
        .add(RailcraftBlocks.FLUID_LOADER.key())
        .add(RailcraftBlocks.FLUID_UNLOADER.key())
        .add(RailcraftBlocks.CART_DISPENSER.key())
        .add(RailcraftBlocks.TRAIN_DISPENSER.key())
        .add(RailcraftBlocks.STEEL_BLOCK.key())
        .add(RailcraftBlocks.BRASS_BLOCK.key())
        .add(RailcraftBlocks.BRONZE_BLOCK.key())
        .add(RailcraftBlocks.INVAR_BLOCK.key())
        .add(RailcraftBlocks.LEAD_BLOCK.key())
        .add(RailcraftBlocks.NICKEL_BLOCK.key())
        .add(RailcraftBlocks.SILVER_BLOCK.key())
        .add(RailcraftBlocks.TIN_BLOCK.key())
        .add(RailcraftBlocks.ZINC_BLOCK.key())
        .add(RailcraftBlocks.FORCE_TRACK_EMITTER.key())
        .add(RailcraftBlocks.NICKEL_IRON_BATTERY.key())
        .add(RailcraftBlocks.NICKEL_ZINC_BATTERY.key())
        .add(RailcraftBlocks.ZINC_SILVER_BATTERY.key())
        .add(RailcraftBlocks.ZINC_SILVER_BATTERY_EMPTY.key())
        .add(RailcraftBlocks.ZINC_CARBON_BATTERY.key())
        .add(RailcraftBlocks.ZINC_CARBON_BATTERY_EMPTY.key())
        .add(RailcraftBlocks.FRAME.key())
        .add(RailcraftBlocks.WIRE.key())
        .add(RailcraftBlocks.PERSONAL_WORLD_SPIKE.key())
        .add(RailcraftBlocks.WORLD_SPIKE.key())
        .add(RailcraftBlocks.VOID_CHEST.key())
        .add(RailcraftBlocks.LEAD_ORE.key(), RailcraftBlocks.DEEPSLATE_LEAD_ORE.key())
        .add(RailcraftBlocks.NICKEL_ORE.key(), RailcraftBlocks.DEEPSLATE_NICKEL_ORE.key())
        .add(RailcraftBlocks.SILVER_ORE.key(), RailcraftBlocks.DEEPSLATE_SILVER_ORE.key())
        .add(RailcraftBlocks.SULFUR_ORE.key(), RailcraftBlocks.DEEPSLATE_SULFUR_ORE.key())
        .add(RailcraftBlocks.TIN_ORE.key(), RailcraftBlocks.DEEPSLATE_TIN_ORE.key())
        .add(RailcraftBlocks.ZINC_ORE.key(), RailcraftBlocks.DEEPSLATE_ZINC_ORE.key())
        .add(RailcraftBlocks.SALTPETER_ORE.key())
        .addTags(RailcraftTags.Blocks.POST,
            RailcraftTags.Blocks.STRENGTHENED_GLASS,
            RailcraftTags.Blocks.IRON_TANK_GAUGE,
            RailcraftTags.Blocks.IRON_TANK_VALVE,
            RailcraftTags.Blocks.IRON_TANK_WALL,
            RailcraftTags.Blocks.STEEL_TANK_GAUGE,
            RailcraftTags.Blocks.STEEL_TANK_VALVE,
            RailcraftTags.Blocks.STEEL_TANK_WALL)
        .addTag(RailcraftTags.Blocks.ABYSSAL)
        .addTag(RailcraftTags.Blocks.QUARRIED);
    RailcraftBlocks.DECORATIVE_BRICK_STAIRS.boundVariants()
        .forEach(x -> this.tag(BlockTags.MINEABLE_WITH_PICKAXE).add(x.builtInRegistryHolder().key()));
    RailcraftBlocks.DECORATIVE_PAVER_STAIRS.boundVariants()
        .forEach(x -> this.tag(BlockTags.MINEABLE_WITH_PICKAXE).add(x.builtInRegistryHolder().key()));
    RailcraftBlocks.DECORATIVE_BRICK_SLAB.boundVariants()
        .forEach(x -> this.tag(BlockTags.MINEABLE_WITH_PICKAXE).add(x.builtInRegistryHolder().key()));
    RailcraftBlocks.DECORATIVE_PAVER_SLAB.boundVariants()
        .forEach(x -> this.tag(BlockTags.MINEABLE_WITH_PICKAXE).add(x.builtInRegistryHolder().key()));

    this.tag(BlockTags.NEEDS_DIAMOND_TOOL)
        .add(RailcraftBlocks.FIRESTONE_ORE.key())
        .add(RailcraftBlocks.CRUSHED_OBSIDIAN.key())
        .add(RailcraftBlocks.WORLD_SPIKE.key())
        .add(RailcraftBlocks.PERSONAL_WORLD_SPIKE.key())
        .add(RailcraftBlocks.VOID_CHEST.key());

    this.tag(BlockTags.NEEDS_IRON_TOOL)
        .add(RailcraftBlocks.STEEL_BLOCK.key())
        .add(RailcraftBlocks.BRASS_BLOCK.key())
        .add(RailcraftBlocks.BRONZE_BLOCK.key())
        .add(RailcraftBlocks.INVAR_BLOCK.key())
        .add(RailcraftBlocks.LEAD_BLOCK.key())
        .add(RailcraftBlocks.NICKEL_BLOCK.key())
        .add(RailcraftBlocks.SILVER_BLOCK.key())
        .add(RailcraftBlocks.TIN_BLOCK.key())
        .add(RailcraftBlocks.ZINC_BLOCK.key())
        .add(RailcraftBlocks.FORCE_TRACK_EMITTER.key());

    this.tag(BlockTags.NEEDS_STONE_TOOL)
        .add(RailcraftBlocks.LOW_PRESSURE_STEAM_BOILER_TANK.key())
        .add(RailcraftBlocks.HIGH_PRESSURE_STEAM_BOILER_TANK.key())
        .add(RailcraftBlocks.SOLID_FUELED_FIREBOX.key())
        .add(RailcraftBlocks.FLUID_FUELED_FIREBOX.key())
        .add(RailcraftBlocks.STEAM_TURBINE.key())
        .add(RailcraftBlocks.HOBBYIST_STEAM_ENGINE.key())
        .add(RailcraftBlocks.COMMERCIAL_STEAM_ENGINE.key())
        .add(RailcraftBlocks.INDUSTRIAL_STEAM_ENGINE.key())
        .add(RailcraftBlocks.COAL_COKE_BLOCK.key())
        .add(RailcraftBlocks.CRUSHER.key())
        .add(RailcraftBlocks.STEAM_OVEN.key())
        .add(RailcraftBlocks.BLAST_FURNACE_BRICKS.key())
        .add(RailcraftBlocks.FEED_STATION.key())
        .add(RailcraftBlocks.CHIMNEY.key())
        .add(RailcraftBlocks.LOGBOOK.key())
        .add(RailcraftBlocks.LEAD_ORE.key(), RailcraftBlocks.DEEPSLATE_LEAD_ORE.key())
        .add(RailcraftBlocks.NICKEL_ORE.key(), RailcraftBlocks.DEEPSLATE_NICKEL_ORE.key())
        .add(RailcraftBlocks.SILVER_ORE.key(), RailcraftBlocks.DEEPSLATE_SILVER_ORE.key())
        .add(RailcraftBlocks.SULFUR_ORE.key(), RailcraftBlocks.DEEPSLATE_SULFUR_ORE.key())
        .add(RailcraftBlocks.TIN_ORE.key(), RailcraftBlocks.DEEPSLATE_TIN_ORE.key())
        .add(RailcraftBlocks.ZINC_ORE.key(), RailcraftBlocks.DEEPSLATE_ZINC_ORE.key())
        .add(RailcraftBlocks.SALTPETER_ORE.key())
        .addTags(RailcraftTags.Blocks.POST,
            RailcraftTags.Blocks.STRENGTHENED_GLASS,
            RailcraftTags.Blocks.IRON_TANK_GAUGE,
            RailcraftTags.Blocks.IRON_TANK_VALVE,
            RailcraftTags.Blocks.IRON_TANK_WALL,
            RailcraftTags.Blocks.STEEL_TANK_GAUGE,
            RailcraftTags.Blocks.STEEL_TANK_VALVE,
            RailcraftTags.Blocks.STEEL_TANK_WALL);

    RailcraftBlocks.STRENGTHENED_GLASS.boundVariants()
        .forEach(x -> this.tag(RailcraftTags.Blocks.STRENGTHENED_GLASS).add(x.builtInRegistryHolder().key()));
    RailcraftBlocks.IRON_TANK_GAUGE.boundVariants()
        .forEach(x -> this.tag(RailcraftTags.Blocks.IRON_TANK_GAUGE).add(x.builtInRegistryHolder().key()));
    RailcraftBlocks.IRON_TANK_VALVE.boundVariants()
        .forEach(x -> this.tag(RailcraftTags.Blocks.IRON_TANK_VALVE).add(x.builtInRegistryHolder().key()));
    RailcraftBlocks.IRON_TANK_WALL.boundVariants()
        .forEach(x -> this.tag(RailcraftTags.Blocks.IRON_TANK_WALL).add(x.builtInRegistryHolder().key()));
    RailcraftBlocks.STEEL_TANK_GAUGE.boundVariants()
        .forEach(x -> this.tag(RailcraftTags.Blocks.STEEL_TANK_GAUGE).add(x.builtInRegistryHolder().key()));
    RailcraftBlocks.STEEL_TANK_VALVE.boundVariants()
        .forEach(x -> this.tag(RailcraftTags.Blocks.STEEL_TANK_VALVE).add(x.builtInRegistryHolder().key()));
    RailcraftBlocks.STEEL_TANK_WALL.boundVariants()
        .forEach(x -> this.tag(RailcraftTags.Blocks.STEEL_TANK_WALL).add(x.builtInRegistryHolder().key()));
    RailcraftBlocks.POST.boundVariants()
        .forEach(x -> this.tag(RailcraftTags.Blocks.POST).add(x.builtInRegistryHolder().key()));

    this.tag(RailcraftTags.Blocks.QUARRIED)
        .add(
            RailcraftBlocks.DECORATIVE_STONE.variantFor(DecorativeBlock.QUARRIED).key(),
            RailcraftBlocks.DECORATIVE_COBBLESTONE.variantFor(DecorativeBlock.QUARRIED).key(),
            RailcraftBlocks.POLISHED_DECORATIVE_STONE.variantFor(DecorativeBlock.QUARRIED).key(),
            RailcraftBlocks.CHISELED_DECORATIVE_STONE.variantFor(DecorativeBlock.QUARRIED).key(),
            RailcraftBlocks.ETCHED_DECORATIVE_STONE.variantFor(DecorativeBlock.QUARRIED).key(),
            RailcraftBlocks.DECORATIVE_BRICKS.variantFor(DecorativeBlock.QUARRIED).key(),
            RailcraftBlocks.DECORATIVE_PAVER.variantFor(DecorativeBlock.QUARRIED).key());
    this.tag(RailcraftTags.Blocks.QUARRIED_REPLACEABLE_BLOCKS)
        .addTags(BlockTags.DIRT)
        .addTags(BlockTags.BASE_STONE_OVERWORLD)
        .addTags(Tags.Blocks.ORES)
        .add(Blocks.CLAY.builtInRegistryHolder().key())
        .add(Blocks.GRAVEL.builtInRegistryHolder().key());

    this.tag(RailcraftTags.Blocks.ABYSSAL)
        .add(
            RailcraftBlocks.DECORATIVE_STONE.variantFor(DecorativeBlock.ABYSSAL).key(),
            RailcraftBlocks.DECORATIVE_COBBLESTONE.variantFor(DecorativeBlock.ABYSSAL).key(),
            RailcraftBlocks.POLISHED_DECORATIVE_STONE.variantFor(DecorativeBlock.ABYSSAL).key(),
            RailcraftBlocks.CHISELED_DECORATIVE_STONE.variantFor(DecorativeBlock.ABYSSAL).key(),
            RailcraftBlocks.ETCHED_DECORATIVE_STONE.variantFor(DecorativeBlock.ABYSSAL).key(),
            RailcraftBlocks.DECORATIVE_BRICKS.variantFor(DecorativeBlock.ABYSSAL).key(),
            RailcraftBlocks.DECORATIVE_PAVER.variantFor(DecorativeBlock.ABYSSAL).key());

    this.tag(RailcraftTags.Blocks.DETECTOR)
        .add(
            RailcraftBlocks.ADVANCED_DETECTOR.key(),
            RailcraftBlocks.AGE_DETECTOR.key(),
            RailcraftBlocks.ANIMAL_DETECTOR.key(),
            RailcraftBlocks.ANY_DETECTOR.key(),
            RailcraftBlocks.EMPTY_DETECTOR.key(),
            RailcraftBlocks.ITEM_DETECTOR.key(),
            RailcraftBlocks.LOCOMOTIVE_DETECTOR.key(),
            RailcraftBlocks.MOB_DETECTOR.key(),
            RailcraftBlocks.PLAYER_DETECTOR.key(),
            RailcraftBlocks.ROUTING_DETECTOR.key(),
            RailcraftBlocks.SHEEP_DETECTOR.key(),
            RailcraftBlocks.TANK_DETECTOR.key(),
            RailcraftBlocks.TRAIN_DETECTOR.key(),
            RailcraftBlocks.VILLAGER_DETECTOR.key());

    this.tag(Tags.Blocks.ORE_RATES_SINGULAR)
        .add(RailcraftBlocks.LEAD_ORE.key())
        .add(RailcraftBlocks.NICKEL_ORE.key())
        .add(RailcraftBlocks.SILVER_ORE.key())
        .add(RailcraftBlocks.TIN_ORE.key())
        .add(RailcraftBlocks.ZINC_ORE.key())
        .add(RailcraftBlocks.DEEPSLATE_LEAD_ORE.key())
        .add(RailcraftBlocks.DEEPSLATE_NICKEL_ORE.key())
        .add(RailcraftBlocks.DEEPSLATE_SILVER_ORE.key())
        .add(RailcraftBlocks.DEEPSLATE_TIN_ORE.key())
        .add(RailcraftBlocks.DEEPSLATE_ZINC_ORE.key())
        .add(RailcraftBlocks.FIRESTONE_ORE.key());

    this.tag(Tags.Blocks.ORE_RATES_DENSE)
        .add(RailcraftBlocks.SULFUR_ORE.key())
        .add(RailcraftBlocks.DEEPSLATE_SULFUR_ORE.key())
        .add(RailcraftBlocks.SALTPETER_ORE.key());

    this.tag(RailcraftTags.Blocks.LEAD_ORE)
        .add(RailcraftBlocks.LEAD_ORE.key())
        .add(RailcraftBlocks.DEEPSLATE_LEAD_ORE.key());

    this.tag(RailcraftTags.Blocks.NICKEL_ORE)
        .add(RailcraftBlocks.NICKEL_ORE.key())
        .add(RailcraftBlocks.DEEPSLATE_NICKEL_ORE.key());

    this.tag(RailcraftTags.Blocks.SILVER_ORE)
        .add(RailcraftBlocks.SILVER_ORE.key())
        .add(RailcraftBlocks.DEEPSLATE_SILVER_ORE.key());

    this.tag(RailcraftTags.Blocks.SULFUR_ORE)
        .add(RailcraftBlocks.SULFUR_ORE.key())
        .add(RailcraftBlocks.DEEPSLATE_SULFUR_ORE.key());

    this.tag(RailcraftTags.Blocks.TIN_ORE)
        .add(RailcraftBlocks.TIN_ORE.key())
        .add(RailcraftBlocks.DEEPSLATE_TIN_ORE.key());

    this.tag(RailcraftTags.Blocks.ZINC_ORE)
        .add(RailcraftBlocks.ZINC_ORE.key())
        .add(RailcraftBlocks.DEEPSLATE_ZINC_ORE.key());

    this.tag(RailcraftTags.Blocks.SALTPETER_ORE)
        .add(RailcraftBlocks.SALTPETER_ORE.key());

    this.tag(Tags.Blocks.ORES)
        .add(RailcraftBlocks.FIRESTONE_ORE.key())
        .addTags(RailcraftTags.Blocks.LEAD_ORE,
            RailcraftTags.Blocks.NICKEL_ORE,
            RailcraftTags.Blocks.SILVER_ORE,
            RailcraftTags.Blocks.SULFUR_ORE,
            RailcraftTags.Blocks.TIN_ORE,
            RailcraftTags.Blocks.ZINC_ORE,
            RailcraftTags.Blocks.SALTPETER_ORE);

    this.tag(Tags.Blocks.ORES_IN_GROUND_STONE)
        .add(RailcraftBlocks.LEAD_ORE.key())
        .add(RailcraftBlocks.NICKEL_ORE.key())
        .add(RailcraftBlocks.SILVER_ORE.key())
        .add(RailcraftBlocks.TIN_ORE.key())
        .add(RailcraftBlocks.ZINC_ORE.key())
        .add(RailcraftBlocks.SULFUR_ORE.key())
        .add(RailcraftBlocks.SALTPETER_ORE.key());
    this.tag(Tags.Blocks.ORES_IN_GROUND_DEEPSLATE)
        .add(RailcraftBlocks.DEEPSLATE_LEAD_ORE.key())
        .add(RailcraftBlocks.DEEPSLATE_NICKEL_ORE.key())
        .add(RailcraftBlocks.DEEPSLATE_SILVER_ORE.key())
        .add(RailcraftBlocks.DEEPSLATE_TIN_ORE.key())
        .add(RailcraftBlocks.DEEPSLATE_ZINC_ORE.key())
        .add(RailcraftBlocks.DEEPSLATE_SULFUR_ORE.key());
    this.tag(Tags.Blocks.ORES_IN_GROUND_NETHERRACK)
        .add(RailcraftBlocks.FIRESTONE_ORE.key());

    this.tag(Tags.Blocks.ENDERMAN_PLACE_ON_BLACKLIST)
        .addTag(RailcraftTags.Blocks.STRENGTHENED_GLASS)
        .addTag(RailcraftTags.Blocks.IRON_TANK_GAUGE)
        .addTag(RailcraftTags.Blocks.IRON_TANK_VALVE)
        .addTag(RailcraftTags.Blocks.IRON_TANK_WALL)
        .addTag(RailcraftTags.Blocks.STEEL_TANK_GAUGE)
        .addTag(RailcraftTags.Blocks.STEEL_TANK_VALVE)
        .addTag(RailcraftTags.Blocks.STEEL_TANK_WALL);
    this.tag(RailcraftTags.Blocks.TUNNEL_BORE_MINEABLE_BLOCKS)
        .add(
            Blocks.CLAY.builtInRegistryHolder().key(),
            Blocks.SNOW.builtInRegistryHolder().key(),
            Blocks.CACTUS.builtInRegistryHolder().key(),
            Blocks.CARROTS.builtInRegistryHolder().key(),
            Blocks.MOSSY_COBBLESTONE.builtInRegistryHolder().key(),
            Blocks.COCOA.builtInRegistryHolder().key(),
            Blocks.WHEAT.builtInRegistryHolder().key(),
            Blocks.DEAD_BUSH.builtInRegistryHolder().key(),
            Blocks.FIRE.builtInRegistryHolder().key(),
            Blocks.GLOWSTONE.builtInRegistryHolder().key(),
            Blocks.ICE.builtInRegistryHolder().key(),
            Blocks.MELON.builtInRegistryHolder().key(),
            Blocks.MELON_STEM.builtInRegistryHolder().key(),
            Blocks.BROWN_MUSHROOM.builtInRegistryHolder().key(),
            Blocks.BROWN_MUSHROOM_BLOCK.builtInRegistryHolder().key(),
            Blocks.RED_MUSHROOM.builtInRegistryHolder().key(),
            Blocks.RED_MUSHROOM_BLOCK.builtInRegistryHolder().key(),
            Blocks.MYCELIUM.builtInRegistryHolder().key(),
            Blocks.NETHER_WART.builtInRegistryHolder().key(),
            Blocks.POTATOES.builtInRegistryHolder().key(),
            Blocks.PUMPKIN.builtInRegistryHolder().key(),
            Blocks.PUMPKIN_STEM.builtInRegistryHolder().key(),
            Blocks.SAND.builtInRegistryHolder().key(),
            Blocks.SANDSTONE.builtInRegistryHolder().key(),
            Blocks.SOUL_SAND.builtInRegistryHolder().key(),
            Blocks.FARMLAND.builtInRegistryHolder().key(),
            Blocks.TORCH.builtInRegistryHolder().key(),
            Blocks.VINE.builtInRegistryHolder().key(),
            Blocks.COBWEB.builtInRegistryHolder().key(),
            Blocks.END_STONE.builtInRegistryHolder().key())
        .addTags(Tags.Blocks.ORES,
            Tags.Blocks.NETHERRACKS,
            Tags.Blocks.COBBLESTONES,
            Tags.Blocks.STONES,
            Tags.Blocks.OBSIDIANS,
            Tags.Blocks.GRAVELS,
            BlockTags.DIRT,
            BlockTags.LEAVES,
            BlockTags.SAPLINGS,
            BlockTags.LOGS,
            BlockTags.FLOWERS,
            BlockTags.REPLACEABLE);
    this.tag(RailcraftTags.Blocks.TUNNEL_BORE_REPLACEABLE_BLOCKS)
        .add(Blocks.TORCH.builtInRegistryHolder().key())
        .addTags(BlockTags.FLOWERS,
            BlockTags.REPLACEABLE);
  }
}
