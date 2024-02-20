package mods.railcraft;

import java.util.List;
import java.util.stream.IntStream;
import mods.railcraft.api.core.RailcraftConstants;

public class Translations {

  public static class Tab {

    public static final String RAILCRAFT = "itemGroup." + RailcraftConstants.ID;
    public static final String RAILCRAFT_OUTFITTED_TRACKS = RAILCRAFT + "_outfitted_tracks";
    public static final String RAILCRAFT_DECORATIVE_BLOCKS = RAILCRAFT + "_decorative_blocks";
  }

  public static class Screen {

    public static final String STEAM_TURBINE_ROTOR = makeKey("screen", "steam_turbine.rotor");
    public static final String STEAM_TURBINE_OUTPUT = makeKey("screen", "steam_turbine.output");
    public static final String STEAM_TURBINE_USAGE = makeKey("screen", "steam_turbine.usage");
    public static final String STEAM_MODE_DESC_IDLE =
        makeKey("screen", "locomotive.steam.mode.description.idle");
    public static final String STEAM_MODE_DESC_RUNNING =
        makeKey("screen", "locomotive.steam.mode.description.running");
    public static final String STEAM_MODE_DESC_SHUTDOWN =
        makeKey("screen", "locomotive.steam.mode.description.shutdown");
    public static final String ELECTRIC_MODE_DESC_RUNNING =
        makeKey("screen", "locomotive.electric.mode.description.running");
    public static final String ELECTRIC_MODE_DESC_SHUTDOWN =
        makeKey("screen", "locomotive.electric.mode.description.shutdown");
    public static final String CREATIVE_MODE_DESC_RUNNING =
        makeKey("screen", "locomotive.creative.mode.description.running");
    public static final String CREATIVE_MODE_DESC_SHUTDOWN =
        makeKey("screen", "locomotive.creative.mode.description.shutdown");
    public static final String LOCOMOTIVE_LOCK_LOCKED =
        makeKey("screen", "locomotive.lock.locked");
    public static final String LOCOMOTIVE_LOCK_UNLOCKED =
        makeKey("screen", "locomotive.lock.unlocked");
    public static final String LOCOMOTIVE_LOCK_PRIVATE =
        makeKey("screen", "locomotive.lock.private");
    public static final String LOCOMOTIVE_MODE_SHUTDOWN =
        makeKey("screen", "locomotive.mode.shutdown");
    public static final String LOCOMOTIVE_MODE_RUNNING =
        makeKey("screen", "locomotive.mode.running");
    public static final String LOCOMOTIVE_MODE_IDLE = makeKey("screen", "locomotive.mode.idle");
    public static final String SINGAL_CONTROLLER_BOX_DEFAULT =
        makeKey("screen", "signal_controller_box.default_aspect");
    public static final String SINGAL_CONTROLLER_BOX_POWERED =
        makeKey("screen", "signal_controller_box.powered_aspect");
    public static final String SIGNAL_CAPACITOR_BOX_DURATION =
        makeKey("screen", "signal_capacitor_box.duration");
    public static final String ACTION_SIGNAL_BOX_LOCKED =
        makeKey("screen", "action_signal_box.lock.locked");
    public static final String ACTION_SIGNAL_BOX_UNLOCKED =
        makeKey("screen", "action_signal_box.lock.unlocked");
    public static final String SWITCH_TRACK_MOTOR_REDSTONE =
        makeKey("screen", "switch_track_motor.redstone_triggered");
    public static final String CART_FILTERS =
        makeKey("screen", "manipulator.cart_filters");
    public static final String ITEM_MANIPULATOR_FILTERS =
        makeKey("screen", "item_manipulator.filters");
    public static final String ITEM_MANIPULATOR_BUFFER =
        makeKey("screen", "item_manipulator.buffer");
    public static final String TUNNEL_BORE_HEAD = makeKey("screen", "tunnel_bore.head");
    public static final String TUNNEL_BORE_FUEL = makeKey("screen", "tunnel_bore.fuel");
    public static final String TUNNEL_BORE_BALLAST = makeKey("screen", "tunnel_bore.ballast");
    public static final String TUNNEL_BORE_TRACK = makeKey("screen", "tunnel_bore.track");
    public static final String MULTIBLOCK_ASSEMBLY_FAILED =
        makeKey("screen", "multiblock.assembly_failed");
    public static final String EMBARKING_TRACK_RADIUS =
        makeKey("screen", "embarking_track.radius");
    public static final String LAUNCHER_TRACK_LAUNCH_FORCE =
        makeKey("screen", "launcher_track.launch_force");
    public static final String WATER_TANK_SEE_SKY = makeKey("screen", "water_tank.sky");
    public static final String WATER_TANK_BASE_RATE = makeKey("screen", "water_tank.base_rate");
    public static final String WATER_TANK_HUMIDITY = makeKey("screen", "water_tank.humidity");
    public static final String WATER_TANK_PRECIPITATION =
        makeKey("screen", "water_tank.precipitation");
    public static final String WATER_TANK_TEMP = makeKey("screen", "water_tank.temperature");
    public static final String WATER_TANK_FINAL_RATE = makeKey("screen", "water_tank.final_rate");
    public static final String PATTERN = makeKey("screen", "track_layer.patter");
    public static final String STOCK = makeKey("screen", "track_layer.stock");
    public static final String UNDER = makeKey("screen", "track_undercutter.under");
    public static final String SIDES = makeKey("screen", "track_undercutter.sides");
    public static final String HELP = makeKey("screen", "help");
    public static final String NAME = makeKey("screen", "name");
    public static final String GOLDEN_TICKET_TITLE = makeKey("screen", "golden_ticket.title");
    public static final String GOLDEN_TICKET_DESC_1 = makeKey("screen", "golden_ticket.desc1");
    public static final String GOLDEN_TICKET_DESC_2 = makeKey("screen", "golden_ticket.desc2");
    public static final String GOLDEN_TICKET_ABOUT = makeKey("screen", "golden_ticket.about");
    public static final String GOLDEN_TICKET_HELP = makeKey("screen", "golden_ticket.help");
    public static final String ROUTING_TABLE_BOOK = makeKey("screen", "routing_table_book");
    public static final String SWITCH_TRACK_ROUTER_PUBLIC_RAILWAY =
        makeKey("screen", "router_block_entity.public_railway");
    public static final String SWITCH_TRACK_ROUTER_PUBLIC_RAILWAY_DESC =
        makeKey("screen", "router_block_entity.public_railway.desc");
    public static final String SWITCH_TRACK_ROUTER_PRIVATE_RAILWAY =
        makeKey("screen", "router_block_entity.private_railway");
    public static final String SWITCH_TRACK_ROUTER_PRIVATE_RAILWAY_DESC =
        makeKey("screen", "router_block_entity.private_railway.desc");
    public static final String LOCOMOTIVE_DETECTOR_PRIMARY =
        makeKey("screen", "locomotive_detector.primary_dye_color");
    public static final String LOCOMOTIVE_DETECTOR_SECONDARY =
        makeKey("screen", "locomotive_detector.secondary_dye_color");
    public static final String FILTER =
        makeKey("screen", "filter");
    public static final String TANK_DETECTOR_VOID = makeKey("screen", "tank_detector.void");
    public static final String TANK_DETECTOR_EMPTY = makeKey("screen", "tank_detector.empty");
    public static final String TANK_DETECTOR_NOT_EMPTY =
        makeKey("screen", "tank_detector.not_empty");
    public static final String TANK_DETECTOR_FULL = makeKey("screen", "tank_detector.full");
    public static final String TANK_DETECTOR_QUARTER = makeKey("screen", "tank_detector.quarter");
    public static final String TANK_DETECTOR_HALF = makeKey("screen", "tank_detector.half");
    public static final String TANK_DETECTOR_MOST = makeKey("screen", "tank_detector.most");
    public static final String TANK_DETECTOR_LESS_THAN_QUARTER =
        makeKey("screen", "tank_detector.less_than_quarter");
    public static final String TANK_DETECTOR_LESS_THAN_HALF =
        makeKey("screen", "tank_detector.less_than_half");
    public static final String TANK_DETECTOR_LESS_THAN_MOST =
        makeKey("screen", "tank_detector.less_than_most");
    public static final String TANK_DETECTOR_LESS_THAN_FULL =
        makeKey("screen", "tank_detector.less_than_full");
    public static final String TANK_DETECTOR_ANALOG = makeKey("screen", "tank_detector.analog");
    public static final String TRAIN_DETECTOR_SIZE =
        makeKey("screen", "train_detector.size");
    public static final String ITEM_DETECTOR_EMPTY =
        makeKey("screen", "item_detector.primary_mode.empty");
    public static final String ITEM_DETECTOR_FULL =
        makeKey("screen", "item_detector.primary_mode.full");
    public static final String ITEM_DETECTOR_ANYTHING =
        makeKey("screen", "item_detector.primary_mode.anything");
    public static final String ITEM_DETECTOR_FILTERED =
        makeKey("screen", "item_detector.primary_mode.filtered");
    public static final String ITEM_DETECTOR_NOT_EMPTY =
        makeKey("screen", "item_detector.primary_mode.not_empty");
    public static final String ITEM_DETECTOR_ANALOG =
        makeKey("screen", "item_detector.primary_mode.analog");
    public static final String ITEM_DETECTOR_AT_LEAST =
        makeKey("screen", "item_detector.filter_mode.at_least");
    public static final String ITEM_DETECTOR_AT_MOST =
        makeKey("screen", "item_detector.filter_mode.at_most");
    public static final String ITEM_DETECTOR_EXACTLY =
        makeKey("screen", "item_detector.filter_mode.exactly");
    public static final String ITEM_DETECTOR_LESS_THAN =
        makeKey("screen", "item_detector.filter_mode.less_than");
    public static final String ITEM_DETECTOR_GREATER_THAN =
        makeKey("screen", "item_detector.filter_mode.greater_than");
  }

  public static class Tips {

    public static final String ROUTING_TICKET_ISSUER = makeKey("tips", "routing.ticket.issuer");
    public static final String ROUTING_TICKET_DEST = makeKey("tips", "routing.ticket.dest");
    public static final String ROUTING_TICKET_BLANK = makeKey("tips", "routing.ticket.blank");
    public static final String ROUTING_TABLE_BOOK_LAST_EDIT =
        makeKey("tips", "routing.routing_table_book.last_edit");
    public static final String LOCOMOTIVE_SLOT_TICKET = makeKey("tips", "locomotive.slot.ticket");
    public static final String LOCOMOTIVE_ITEM_OWNER = makeKey("tips", "locomotive.item.owner");
    public static final String LOCOMOTIVE_ITEM_PRIMARY =
        makeKey("tips", "locomotive.item.primary");
    public static final String LOCOMOTIVE_ITEM_SECONDARY =
        makeKey("tips", "locomotive.item.secondary");
    public static final String LOCOMOTIVE_ITEM_WHISTLE =
        makeKey("tips", "locomotive.item.whistle");
    public static final String TRACK_LAYER = makeKey("tips", "track_layer");
    public static final String TRACK_RELAYER = makeKey("tips", "track_relayer");
    public static final String TRACK_REMOVER = makeKey("tips", "track_remover");
    public static final String TRACK_UNDERCUTTER = makeKey("tips", "track_undercutter");
    public static final String CRUSHED_OBSIDIAN = makeKey("tips", "crushed_obsidian");
    public static final String COKE_OVEN = makeKey("tips", "coke_oven");
    public static final String BLAST_FURNACE = makeKey("tips", "blast_furnace");
    public static final String CRUSHER = makeKey("tips", "crusher");
    public static final String WATER_TANK_SIDING = makeKey("tips", "water_tank_siding");
    public static final String MULTIBLOCK2X2X2 = makeKey("tips", "multiblock2x2x2");
    public static final String MULTIBLOCK3X2X2 = makeKey("tips", "multiblock3x2x2");
    public static final String STEAM_TURBINE_DESC_1 = makeKey("tips", "steam_turbine_desc1");
    public static final String STEAM_TURBINE_DESC_2 = makeKey("tips", "steam_turbine_desc2");
    public static final String STEAM_TURBINE_DESC_3 = makeKey("tips", "steam_turbine_desc3");
    public static final String SPIKE_MAUL = makeKey("tips", "spike_maul");
    public static final String OVERALLS = makeKey("tips", "overalls");
    public static final String CLEAR = makeKey("tips", "clear");
    public static final String GOOGLES_DESC = makeKey("tips", "googles.desc");
    public static final String GOOGLES_AURA = makeKey("tips", "googles.aura");
    public static final String GOOGLES_AURA_NONE = makeKey("tips", "googles.aura.none");
    public static final String GOOGLES_AURA_SHUNTING = makeKey("tips", "googles.aura.shunting");
    public static final String GOOGLES_AURA_SIGNALLING =
        makeKey("tips", "googles.aura.signalling");
    public static final String GOOGLES_AURA_SURVEYING = makeKey("tips", "googles.aura.surveying");
    public static final String GOOGLES_AURA_TRACKING = makeKey("tips", "googles.aura.tracking");
    public static final String GOOGLES_AURA_TUNING = makeKey("tips", "googles.aura.tuning");
    public static final String GOOGLES_AURA_WORLDSPIKE =
        makeKey("tips", "googles.aura.worldspike");
    public static final String CROWBAR_DESC = makeKey("tips", "crowbar.desc");
    public static final String CROWBAR_LINK_BROKEN = makeKey("tips", "crowbar.link.broken");
    public static final String CROWBAR_LINK_CREATED = makeKey("tips", "crowbar.link.created");
    public static final String CROWBAR_LINK_FAILED = makeKey("tips", "crowbar.link.failed");
    public static final String CROWBAR_LINK_STARTED = makeKey("tips", "crowbar.link.started");
    public static final String CROWBAR_SEASON_DESC = makeKey("tips", "crowbar.season.desc");
    public static final String SIGNAL_LABEL_DESC1 = makeKey("tips", "signal_label.desc1");
    public static final String SIGNAL_LABEL_DESC2 = makeKey("tips", "signal_label.desc2");
    public static final String SENDS_SIGNALS_TO_RECEIVERS =
        makeKey("tips", "send_signals_to_receivers");
    public static final String SIGNAL_RECEIVER_BOX = makeKey("tips", "signal_receiver_box");
    public static final String SIGNAL_SEQUENCER_BOX = makeKey("tips", "signal_sequencer_box");
    public static final String SIGNAL_INTERLOCK_BOX = makeKey("tips", "signal_interlock_box");
    public static final String SIGNAL_BLOCK_RELAY_BOX = makeKey("tips", "signal_block_relay_box");
    public static final String FIRESTONE_EMPTY = makeKey("tips", "firestone.empty");
    public static final String FIRESTONE_CHARGED = makeKey("tips", "firestone.charged");
    public static final String RAW_FIRESTONE = makeKey("tips", "firestone.raw");
    public static final String CUT_FIRESTONE = makeKey("tips", "firestone.cut");
    public static final String FIRESTONE_ORE = makeKey("tips", "firestone.ore");
    public static final String CURRENT_MODE = makeKey("tips", "current_mode");
    public static final String TRACK_KIT_CORNERS_UNSUPPORTED =
        makeKey("tips", "track_kit.corners_unsupported");
    public static final String TRACK_KIT_SLOPES_UNSUPPORTED =
        makeKey("tips", "track_kit.slopes_unsupported");
    public static final String TRACK_KIT_INVALID_TRACK_TYPE =
        makeKey("tips", "track_kit.invalid_track_type");
    public static final String COAL_COKE_BLOCK = makeKey("tips", "coal_coke_block");
    public static final String FORCE_TRACK_EMITTER = makeKey("tips", "force_track_emitter");
    public static final String FEED_STATION = makeKey("tips", "feed_station");
    public static final String LOGBOOK = makeKey("tips", "logbook");
    public static final String ROLLING_MACHINE = makeKey("tips", "rolling_machine");
    public static final String ITEM_LOADER = makeKey("tips", "item_loader");
    public static final String ITEM_UNLOADER = makeKey("tips", "item_unloader");
    public static final String FLUID_LOADER = makeKey("tips", "fluid_loader");
    public static final String FLUID_UNLOADER = makeKey("tips", "fluid_unloader");
    public static final String CART_DISPENSER = makeKey("tips", "cart_dispenser");
    public static final String TRAIN_DISPENSER = makeKey("tips", "train_dispenser");
    public static final String BLOCK_SIGNAL = makeKey("tips", "block_signal");
    public static final String DISTANT_SIGNAL = makeKey("tips", "distant_signal");
    public static final String TOKEN_SIGNAL = makeKey("tips", "token_signal");
    public static final String SIGNAL_BLOCKS = makeKey("tips", "signal_blocks");
    public static final String CONTROLLERS = makeKey("tips", "controllers");
    public static final String SIGNAL_SURVEYOR = makeKey("tips", "signal_surveyor");
    public static final String TOKEN_AREA = makeKey("tips", "token_area");
    public static final String FRAME = makeKey("tips", "frame");
    public static final String SIGNAL_BLOCK_SURVEYOR = makeKey("tips", "signal_block_surveyor");
    public static final String LINKS_CONTROLLERS_TO_RECEIVERS =
        makeKey("tips", "links_controllers_to_receivers");
    public static final String FUELED_BOILER_FIREBOX = makeKey("tips", "fueled_boiler_firebox");
    public static final String PRESSURE_BOILER_TANK = makeKey("tips", "pressure_boiler_tank");
    public static final String PRESSURE_BOILER_TANK_PRODUCTION =
        makeKey("tips", "pressure_boiler_tank.production");
    public static final String DIMENSIONS = makeKey("tips", "dimensions");
    public static final String APPLY_REDSTONE_TO_ENABLE =
        makeKey("tips", "apply_redstone_to_enable");
    public static final String APPLY_REDSTONE_TO_DISABLE =
        makeKey("tips", "apply_redstone_to_disable");
    public static final String APPLY_REDSTONE_TO_CHANGE_DIRECTION =
        makeKey("tips", "apply_redstone_to_change_direction");
    public static final String APPLY_REDSTONE_TO_OPEN =
        makeKey("tips", "apply_redstone_to_open");
    public static final String APPLY_REDSTONE_TO_RELEASE_CARTS =
        makeKey("tips", "apply_redstone_to_release_carts");
    public static final String APPLY_REDSTONE_TO_DISPENSE_CARTS =
        makeKey("tips", "apply_redstone_to_dispense_carts");
    public static final String APPLY_REDSTONE_TO_DISPENSE_TRAINS =
        makeKey("tips", "apply_redstone_to_release_trains");
    public static final String COMPARATOR_OUTPUT_FROM_CARTS =
        makeKey("tips", "comparator_output_from_carts");
    public static final String HIT_CROWBAR_TO_CHANGE_DIRECTION =
        makeKey("tips", "hit_crowbar_to_change_direction");
    public static final String HIT_CROWBAR_TO_CHANGE_DETECTION_DIRECTION =
        makeKey("tips", "hit_crowbar_to_change_detection_direction");
    public static final String HIT_CROWBAR_TO_CHANGE_RANGE =
        makeKey("tips", "hit_crowbar_to_change_range");
    public static final String HIT_CROWBAR_TO_CHANGE_MODE =
        makeKey("tips", "hit_crowbar_to_change_mode");
    public static final String HIT_CROWBAR_TO_CHANGE_FORCE =
        makeKey("tips", "hit_crowbar_to_change_force");
    public static final String HIT_CROWBAR_TO_CHANGE_TICKET =
        makeKey("tips", "hit_crowbar_to_change_ticket");
    public static final String HIT_CROWBAR_TO_ROTATE =
        makeKey("tips", "hit_crowbar_to_rotate");
    public static final String PAIR_WITH_CONTROL_TRACK =
        makeKey("tips", "pair_with_control_track");
    public static final String PLACE_OVER_TRACK = makeKey("tips", "place_over_track");
    public static final String PLACE_UNDER_TRACK = makeKey("tips", "place_under_track");
    public static final String PLACE_ABOVE_TRACK = makeKey("tips", "place_above_track");
    public static final String SLOW_UNPOWERED = makeKey("tips", "slow_unpowered");
    public static final String ACTIVATOR_TRACK = makeKey("tips", "activator_track");
    public static final String BOOSTER_TRACK = makeKey("tips", "booster_track");
    public static final String BUFFER_STOP_TRACK = makeKey("tips", "buffer_stop_track");
    public static final String CONTROL_TRACK = makeKey("tips", "control_track");
    public static final String DETECTOR_TRACK = makeKey("tips", "detector_track");
    public static final String DISEMBARKING_TRACK = makeKey("tips", "disembarking_track");
    public static final String EMBARKING_TRACK = makeKey("tips", "embarking_track");
    public static final String GATED_TRACK = makeKey("tips", "gated_track");
    public static final String COUPLER_TRACK = makeKey("tips", "coupler_track");
    public static final String LAUNCHER_TRACK = makeKey("tips", "launcher_track");
    public static final String ONE_WAY_TRACK = makeKey("tips", "one_way_track");
    public static final String WHISTLE_TRACK = makeKey("tips", "whistle_track");
    public static final String LOCOMOTIVE_TRACK = makeKey("tips", "locomotive_track");
    public static final String THROTTLE_TRACK = makeKey("tips", "throttle_track");
    public static final String LOCKING_TRACK = makeKey("tips", "locking_track");
    public static final String TRANSITION_TRACK = makeKey("tips", "transition_track");
    public static final String ROUTING_TRACK = makeKey("tips", "routing_track");
    public static final String SWITCH_TRACKS = makeKey("tips", "switch_track_lever");
    public static final String DUMPING_TRACK = makeKey("tips", "dumping_track");

    public static final String MANUAL_OPERATION = makeKey("tips", "manual_operation");
    public static final String SCRIPTED_LOGIC_OPERATION =
        makeKey("tips", "scripted_logic_operation");
    public static final String RELEVANT_TOOLS = makeKey("tips", "relevant_tools");
    public static final String SIGNAL_TUNER = makeKey("tips", "signal_tuner");
    public static final String AERIAL_LINKAGES = makeKey("tips", "aerial_linkages");
    public static final String RECEIVERS = makeKey("tips", "receivers");
    public static final String REDSTONE_LINKAGE = makeKey("tips", "redstone_linkage");
    public static final String LISTEN = makeKey("tips", "listen");
    public static final String CLICK_TO_CRAFT = makeKey("tips", "click_to_craft");
    public static final String ABANDONED_TRACK = makeKey("tips", "abandoned_track");
    public static final String REINFORCED_TRACK = makeKey("tips", "reinforced_track");
    public static final String STRAP_IRON_TRACK = makeKey("tips", "strap_iron_track");
    public static final String DANGER = makeKey("tips", "danger");
    public static final String DERAILMENT_RISK = makeKey("tips", "derailment_risk");
    public static final String HIGH_VOLTAGE = makeKey("tips", "high_voltage");
    public static final String HIGH_SPEED = makeKey("tips", "high_speed");
    public static final String HIGH_VOLTAGE_SPEED = makeKey("tips", "high_voltage_speed");
    public static final String POWERED_BY_ELECTRICITY = makeKey("tips", "powered_by_electricity");
    public static final String USE_ELECTRIC_LOCOMOTIVE =
        makeKey("tips", "use_electric_locomotive");
    public static final String VERY_FAST = makeKey("tips", "very_fast");
    public static final String REQUIRE_BOOSTERS_TRANSITION =
        makeKey("tips", "require_boosters_transition");
    public static final String CANNOT_MAKE_CORNERS_HIGH_SPEED =
        makeKey("tips", "cannot_make_corners_high_speed");
    public static final String EXPLOSION_RESISTANT = makeKey("tips", "explosion_resistant");
    public static final String CHARGE_NETWORK_BATTERY = makeKey("tips", "charge_network_battery");
    public static final String CHARGE_NETWORK_EMPTY_BATTERY =
        makeKey("tips", "charge_network_empty_battery");
    public static final String CAPACITY = makeKey("tips", "capacity");
    public static final String MAX_DRAW = makeKey("tips", "max_draw");
    public static final String LOSS = makeKey("tips", "loss");
    public static final String EFFICIENCY = makeKey("tips", "efficiency");
    public static final String TYPE_RECHARGEABLE = makeKey("tips", "type_rechargeable");
    public static final String TYPE_DISPOSABLE = makeKey("tips", "type_disposable");
    public static final String EMPTY = makeKey("tips", "empty");
  }

  public static class Container {
    public static final String COKE_OVEN = makeKey("container", "coke_oven");
    public static final String BLAST_FURNACE = makeKey("container", "blast_furnace");
    public static final String CRUSHER = makeKey("container", "crusher");
    public static final String STEAM_OVEN = makeKey("container", "steam_oven");
    public static final String WATER_TANK_SIDING = makeKey("container", "water_tank_siding");
    public static final String TANK = makeKey("container", "tank");
    public static final String STEAM_TURBINE = makeKey("container", "steam_turbine");
    public static final String SOLID_FUELED_STEAM_BOILER =
        makeKey("container", "solid_fueled_steam_boiler");
    public static final String FLUID_FUELED_STEAM_BOILER =
        makeKey("container", "fluid_fueled_steam_boiler");
  }

  public static class Jei {

    public static final String METAL_ROLLING = makeKey("jei", "category.rolling");
    public static final String COKE_OVEN = makeKey("jei", "category.coke_oven");
    public static final String BLAST_FURNACE = makeKey("jei", "category.blast_furnace");
    public static final String CRUSHER = makeKey("jei", "category.crusher");
    public static final String CRUSHER_TIP = makeKey("jei", "tips.crusher");

    public static final String MANUAL_ROLLING_MACHINE =
        makeKey("jei", "desc.manual_rolling_machine");
    public static final String POWERED_ROLLING_MACHINE =
        makeKey("jei", "desc.powered_rolling_machine");
    public static final String FEED_STATION = makeKey("jei", "desc.feed_station");
    public static final String LOGBOOK = makeKey("jei", "desc.logbook");
    public static final String BLOCK_SIGNAL = makeKey("jei", "desc.block_signal");
    public static final String DISTANT_SIGNAL = makeKey("jei", "desc.distant_signal");
    public static final String TOKEN_SIGNAL = makeKey("jei", "desc.token_signal");
    public static final String DUAL_BLOCK_SIGNAL =
        makeKey("jei", "desc.dual_block_signal");
    public static final String DUAL_DISTANT_SIGNAL =
        makeKey("jei", "desc.dual_distant_signal");
    public static final String DUAL_TOKEN_SIGNAL =
        makeKey("jei", "desc.dual_token_signal");
    public static final String TUNNEL_BORE = makeKey("jei", "desc.tunnel_bore");
    public static final String NICKEL_IRON_BATTERY =
        makeKey("jei", "desc.nickel_iron_battery");
    public static final String NICKEL_ZINC_BATTERY =
        makeKey("jei", "desc.nickel_zinc_battery");
    public static final String DISPOSABLE_BATTERY =
        makeKey("jei", "desc.disposable_battery");
    public static final String DISPOSABLE_BATTERY_EMPTY =
        makeKey("jei", "desc.disposable_battery_empty");

    public static final String PAINT = makeKey("jei.gui", "paint");
    public static final String COPY_TAG = makeKey("jei.gui", "copy_tag");
    public static final String REPAIR = makeKey("jei.gui", "repair");
    public static final String SPLIT = makeKey("jei.gui", "split");
  }

  public static class Signal {

    public static final String SIGNAL_SURVEYOR_INVALID_TRACK =
        makeKey("signal", "surveyor.invalid_track");
    public static final String SIGNAL_SURVEYOR_BEGIN = makeKey("signal", "surveyor.begin");
    public static final String SIGNAL_SURVEYOR_SUCCESS =
        makeKey("signal", "surveyor.success");
    public static final String SIGNAL_SURVEYOR_INVALID_PAIR =
        makeKey("signal", "surveyor.invalid_pair");
    public static final String SIGNAL_SURVEYOR_LOST = makeKey("signal", "surveyor.lost");
    public static final String SIGNAL_SURVEYOR_ABANDONED =
        makeKey("signal", "surveyor.abandoned");
    public static final String SIGNAL_SURVEYOR_INVALID_BLOCK =
        makeKey("signal", "surveyor.invalid_block");

    public static final String SIGNAL_TUNER_BEGIN = makeKey("signal", "tuner.begin");
    public static final String SIGNAL_TUNER_ABANDONED = makeKey("signal", "tuner.abandoned");
    public static final String SIGNAL_TUNER_INVALID_CONTROLLER =
        makeKey("signal", "tuner.invalid_controller");
    public static final String SIGNAL_TUNER_INVALID_RECEIVER =
        makeKey("signal", "tuner.invalid_receiver");
    public static final String SIGNAL_TUNER_ALREADY_PAIRED =
        makeKey("signal", "tuner.already_paired");
    public static final String SIGNAL_TUNER_LOST = makeKey("signal", "tuner.lost");
    public static final String SIGNAL_TUNER_SUCCESS = makeKey("signal", "tuner.success");
  }

  public static class SignalAspect {

    public static final String GREEN = makeKey("signal", "aspect.green");
    public static final String YELLOW = makeKey("signal", "aspect.yellow");
    public static final String RED = makeKey("signal", "aspect.red");
    public static final String BLINK_YELLOW = makeKey("signal", "aspect.blink_yellow");
    public static final String BLINK_RED = makeKey("signal", "aspect.blink_red");
    public static final String OFF = makeKey("signal", "aspect.off");
  }

  public static class SignalCapacitor {

    public static final String RISING_EDGE = makeKey("signal", "capacitor.rising_edge");
    public static final String RISING_EDGE_DESC =
        makeKey("signal", "capacitor.rising_edge.desc");
    public static final String FALLING_EDGE = makeKey("signal", "capacitor.falling_edge");
    public static final String FALLING_EDGE_DESC =
        makeKey("signal", "capacitor.falling_edge.desc");
  }

  public static class Advancement {

    public static class Tracks {

      public static final String ROOT = makeKey("advancements", "tracks.root.name");
      public static final String ROOT_DESC = makeKey("advancements", "tracks.root.desc");
      public static final String MANUAL_ROLLING_MACHINE =
          makeKey("advancements", "tracks.manual_rolling_machine.name");
      public static final String MANUAL_ROLLING_MACHINE_DESC =
          makeKey("advancements", "tracks.manual_rolling_machine.desc");
      public static final String BLAST_FURNACE =
          makeKey("advancements", "tracks.blast_furnace.name");
      public static final String BLAST_FURNACE_DESC =
          makeKey("advancements", "tracks.blast_furnace.desc");
      public static final String COKE_OVEN =
          makeKey("advancements", "tracks.coke_oven.name");
      public static final String COKE_OVEN_DESC =
          makeKey("advancements", "tracks.coke_oven.desc");
      public static final String FIRESTONE =
          makeKey("advancements", "tracks.firestone.name");
      public static final String FIRESTONE_DESC =
          makeKey("advancements", "tracks.firestone.desc");
      public static final String HIGH_SPEED_TRACK =
          makeKey("advancements", "tracks.high_speed_track.name");
      public static final String HIGH_SPEED_TRACK_DESC =
          makeKey("advancements", "tracks.high_speed_track.desc");
      public static final String JUNCTIONS =
          makeKey("advancements", "tracks.junctions.name");
      public static final String JUNCTIONS_DESC =
          makeKey("advancements", "tracks.junctions.desc");
      public static final String REGULAR_TRACK =
          makeKey("advancements", "tracks.regular_track.name");
      public static final String REGULAR_TRACK_DESC =
          makeKey("advancements", "tracks.regular_track.desc");
      public static final String CRUSHER = makeKey("advancements", "tracks.crusher.name");
      public static final String CRUSHER_DESC =
          makeKey("advancements", "tracks.crusher.desc");
      public static final String TRACK_KIT =
          makeKey("advancements", "tracks.track_kit.name");
      public static final String TRACK_KIT_DESC =
          makeKey("advancements", "tracks.track_kit.desc");
      public static final String WOODEN_TRACK =
          makeKey("advancements", "tracks.wooden_track.name");
      public static final String WOODEN_TRACK_DESC =
          makeKey("advancements", "tracks.wooden_track.desc");
    }

    public static class Carts {

      public static final String ROOT = makeKey("advancements", "carts.root.name");
      public static final String ROOT_DESC = makeKey("advancements", "carts.root.desc");
      public static final String BED_CART = makeKey("advancements", "carts.bed_cart.name");
      public static final String BED_CART_DESC =
          makeKey("advancements", "carts.bed_cart.desc");
      public static final String JUKEBOX_CART =
          makeKey("advancements", "carts.jukebox_cart.name");
      public static final String JUKEBOX_CART_DESC =
          makeKey("advancements", "carts.jukebox_cart.desc");
      public static final String LINK_CARTS =
          makeKey("advancements", "carts.link_carts.name");
      public static final String LINK_CARTS_DESC =
          makeKey("advancements", "carts.link_carts.desc");
      public static final String LOCOMOTIVE =
          makeKey("advancements", "carts.locomotive.name");
      public static final String LOCOMOTIVE_DESC =
          makeKey("advancements", "carts.locomotive.desc");
      public static final String SEASONS = makeKey("advancements", "carts.seasons.name");
      public static final String SEASONS_DESC =
          makeKey("advancements", "carts.seasons.desc");
      public static final String SURPRISE =
          makeKey("advancements", "carts.surprise.name");
      public static final String SURPRISE_DESC =
          makeKey("advancements", "carts.surprise.desc");
    }
  }

  public static class Subtitle {

    public static final String STEAM_WHISTLE = makeKey("subtitle", "locomotive.steam.whistle");
    public static final String ELECTRIC_WHISTLE =
        makeKey("subtitle", "locomotive.electric.whistle");
    public static final String STEAM_BURST = makeKey("subtitle", "machine.steam.burst");
    public static final String STEAM_HISS = makeKey("subtitle", "machine.steam.hiss");
    public static final String MACHINE_ZAP = makeKey("subtitle", "machine.zap");
  }

  public static class DamageSource {
    public static final List<String> BORE = IntStream.rangeClosed(1, 6)
        .mapToObj(i -> makeKey("death", "bore." + i)).toList();
    public static final List<String> CRUSHER = IntStream.rangeClosed(1, 8)
        .mapToObj(i -> makeKey("death", "crusher." + i)).toList();
    public static final List<String> ELECTRIC = IntStream.rangeClosed(1, 6)
        .mapToObj(i -> makeKey("death", "electric." + i)).toList();
    public static final List<String> STEAM = IntStream.rangeClosed(1, 6)
        .mapToObj(i -> makeKey("death", "steam." + i)).toList();
    public static final List<String> TRACK_ELECTRIC = IntStream.rangeClosed(1, 6)
        .mapToObj(i -> makeKey("death", "track_electric." + i)).toList();
    public static final List<String> TRAIN = IntStream.rangeClosed(1, 6)
        .mapToObj(i -> makeKey("death", "train." + i)).toList();
  }

  public static class RoutingTable {
    public static final List<String> MANUAL_PAGES = IntStream.rangeClosed(1, 12)
        .mapToObj(i -> makeKey("manual", "routing_table.page." + i)).toList();

    public static final String ERROR_BLANK = makeKey("error.routing", "blank");
    public static final String ERROR_INVALID_CONSTANT =
        makeKey("error.routing", "invalid_constant");
    public static final String ERROR_MALFORMED_SYNTAX =
        makeKey("error.routing", "malformed_syntax");
    public static final String ERROR_INSUFFICIENT_OPERAND =
        makeKey("error.routing", "insufficient_operand");
    public static final String ERROR_INVALID_OPERAND =
        makeKey("error.routing", "invalid_operand");
    public static final String ERROR_INVALID_REGEX =
        makeKey("error.routing", "invalid_regex");
    public static final String ERROR_UNSUPPORTED_REGEX =
        makeKey("error.routing", "unsupported_regex");
    public static final String UNRECOGNIZED_KEYWORD =
        makeKey("error.routing", "unrecognized_keyword");
  }

  public static class ChargeMeter {
    public static final String START = makeKey("charge_meter", "start");
    public static final String CART = makeKey("charge_meter", "cart");
    public static final String NETWORK = makeKey("charge_meter", "network");
    public static final String NODE = makeKey("charge_meter", "node");
    public static final String PRODUCER = makeKey("charge_meter", "producer");
  }

  public static class KeyBinding {
    public static final String CATEGORY = makeKey("key", "category");
    public static final String CHANGE_AURA = makeKey("key", "change_aura");
  }

  public static class EnchantmentDescriptions {
    public static final String DESTRUCTION = makeKey("enchantment", "destruction.desc");
    public static final String IMPLOSION = makeKey("enchantment", "implosion.desc");
    public static final String SMACK = makeKey("enchantment", "smack.desc");
    public static final String WRECKING = makeKey("enchantment", "wrecking.desc");
  }

  public static String makeKey(String type, String name) {
    return type + "." + RailcraftConstants.ID + "." + name;
  }
}
