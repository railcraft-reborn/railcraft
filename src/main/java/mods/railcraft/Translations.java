package mods.railcraft;

public class Translations {

  public static class Tab {

    private static final String PREFIX = "itemGroup.";

    public static final String RAILCRAFT =
        PREFIX + format("%railcraft");
    public static final String RAILCRAFT_OUTFITTED_TRACKS =
        PREFIX + format("%railcraft_outfitted_tracks");
    public static final String RAILCRAFT_DECORATIVE_BLOCKS =
        PREFIX + format("%railcraft_decorative_blocks");

    public static String withoutPrefix(String key) {
      return key.substring(PREFIX.length());
    }
  }

  public static class Screen {

    public static final String STEAM_TURBINE_ROTOR =
        format("screen.%railcraft.steam_turbine.rotor");
    public static final String STEAM_TURBINE_OUTPUT =
        format("screen.%railcraft.steam_turbine.output");
    public static final String STEAM_TURBINE_USAGE =
        format("screen.%railcraft.steam_turbine.usage");
    public static final String STEAM_MODE_DESC_IDLE =
        format("screen.%railcraft.locomotive.steam.mode.description.idle");
    public static final String STEAM_MODE_DESC_RUNNING =
        format("screen.%railcraft.locomotive.steam.mode.description.running");
    public static final String STEAM_MODE_DESC_SHUTDOWN =
        format("screen.%railcraft.locomotive.steam.mode.description.shutdown");
    public static final String LOCOMOTIVE_LOCK_LOCKED =
        format("screen.%railcraft.locomotive.lock.locked");
    public static final String LOCOMOTIVE_LOCK_UNLOCKED =
        format("screen.%railcraft.locomotive.lock.unlocked");
    public static final String LOCOMOTIVE_LOCK_PRIVATE =
        format("screen.%railcraft.locomotive.lock.private");
    public static final String LOCOMOTIVE_MODE_SHUTDOWN =
        format("screen.%railcraft.locomotive.mode.shutdown");
    public static final String LOCOMOTIVE_MODE_RUNNING =
        format("screen.%railcraft.locomotive.mode.running");
    public static final String LOCOMOTIVE_MODE_IDLE =
        format("screen.%railcraft.locomotive.mode.idle");
    public static final String SINGAL_CONTROLLER_BOX_DEFAULT =
        format("screen.%railcraft.signal_controller_box.default_aspect");
    public static final String SINGAL_CONTROLLER_BOX_POWERED =
        format("screen.%railcraft.signal_controller_box.powered_aspect");
    public static final String SIGNAL_CAPACITOR_BOX_DURATION =
        format("screen.%railcraft.signal_capacitor_box.duration");
    public static final String ACTION_SIGNAL_BOX_LOCKED =
        format("screen.%railcraft.action_signal_box.lock.locked");
    public static final String ACTION_SIGNAL_BOX_UNLOCKED =
        format("screen.%railcraft.action_signal_box.lock.unlocked");
    public static final String SWITCH_TRACK_MOTOR_REDSTONE =
        format("screen.%railcraft.switch_track_motor.redstone_triggered");
    public static final String CART_FILTERS =
        format("screen.%railcraft.manipulator.cart_filters");
    public static final String ITEM_MANIPULATOR_FILTERS =
        format("screen.%railcraft.item_manipulator.filters");
    public static final String ITEM_MANIPULATOR_BUFFER =
        format("screen.%railcraft.item_manipulator.buffer");
    public static final String TUNNEL_BORE_HEAD =
        format("screen.%railcraft.tunnel_bore.head");
    public static final String TUNNEL_BORE_FUEL =
        format("screen.%railcraft.tunnel_bore.fuel");
    public static final String TUNNEL_BORE_BALLAST =
        format("screen.%railcraft.tunnel_bore.ballast");
    public static final String TUNNEL_BORE_TRACK =
        format("screen.%railcraft.tunnel_bore.track");
    public static final String MULTIBLOCK_ASSEMBLY_FAILED =
        format("screen.%railcraft.multiblock.assembly_failed");
    public static final String EMBARKING_TRACK_RADIUS =
        format("screen.%railcraft.embarking_track.radius");
    public static final String LAUNCHER_TRACK_LAUNCH_FORCE =
        format("screen.%railcraft.launcher_track.launch_force");
  }

  public static class Tips {

    public static final String ROUTING_TICKET_ISSUER =
        format("tips.%railcraft.routing.ticket.issuer");
    public static final String ROUTING_TICKET_DEST =
        format("tips.%railcraft.routing.ticket.dest");
    public static final String ROUTING_TICKET_BLANK =
        format("tips.%railcraft.routing.ticket.blank");
    public static final String LOCOMOTIVE_SLOT_TICKET =
        format("tips.%railcraft.locomotive.slot.ticket");
    public static final String LOCOMOTIVE_ITEM_OWNER =
        format("tips.%railcraft.locomotive.item.owner");
    public static final String LOCOMOTIVE_ITEM_PRIMARY =
        format("tips.%railcraft.locomotive.item.primary");
    public static final String LOCOMOTIVE_ITEM_SECONDARY =
        format("tips.%railcraft.locomotive.item.secondary");
    public static final String LOCOMOTIVE_ITEM_WHISTLE =
        format("tips.%railcraft.locomotive.item.whistle");
    public static final String CRUSHED_OBSIDIAN =
        format("tips.%railcraft.crushed_obsidian");
    public static final String SPIKE_MAUL =
        format("tips.%railcraft.spike_maul");
    public static final String GOOGLES_DESC =
        format("tips.%railcraft.googles.desc");
    public static final String GOOGLES_AURA =
        format("tips.%railcraft.googles.aura");
    public static final String GOOGLES_AURA_NONE =
        format("tips.%railcraft.googles.aura.none");
    public static final String GOOGLES_AURA_SHUNTING =
        format("tips.%railcraft.googles.aura.shunting");
    public static final String GOOGLES_AURA_SIGNALLING =
        format("tips.%railcraft.googles.aura.signalling");
    public static final String GOOGLES_AURA_SURVEYING =
        format("tips.%railcraft.googles.aura.surveying");
    public static final String GOOGLES_AURA_TRACKING =
        format("tips.%railcraft.googles.aura.tracking");
    public static final String GOOGLES_AURA_TUNING =
        format("tips.%railcraft.googles.aura.tuning");
    public static final String GOOGLES_AURA_WORLDSPIKE =
        format("tips.%railcraft.googles.aura.worldspike");
    public static final String CRAWBAR_DESC =
        format("tips.%railcraft.crowbar.desc");
    public static final String CRAWBAR_LINK_BROKEN =
        format("tips.%railcraft.crowbar.link.broken");
    public static final String CRAWBAR_LINK_CREATED =
        format("tips.%railcraft.crowbar.link.created");
    public static final String CRAWBAR_LINK_FAILED =
        format("tips.%railcraft.crowbar.link.failed");
    public static final String CRAWBAR_LINK_STARTED =
        format("tips.%railcraft.crowbar.link.started");
    public static final String CRAWBAR_SEASON_DESC =
        format("tips.%railcraft.crowbar.seasons.desc");
    public static final String CRAWBAR_SEASON_NONE =
        format("tips.%railcraft.crowbar.seasons.none");
    public static final String CRAWBAR_SEASON_DEFAULT =
        format("tips.%railcraft.crowbar.seasons.default");
    public static final String CRAWBAR_SEASON_HALLOWEEN =
        format("tips.%railcraft.crowbar.seasons.halloween");
    public static final String CRAWBAR_SEASON_CHRISTMAS =
        format("tips.%railcraft.crowbar.seasons.christmas");
    public static final String SIGNAL_LABEL_DESC1 =
        format("tips.%railcraft.signal_label.desc1");
    public static final String SIGNAL_LABEL_DESC2 =
        format("tips.%railcraft.signal_label.desc2");
    public static final String SENDS_SIGNALS_TO_RECEIVERS =
        format("tips.%railcraft.send_signals_to_receivers");
    public static final String SIGNAL_RECEIVER_BOX =
        format("tips.%railcraft.signal_receiver_box");
    public static final String SIGNAL_SEQUENCER_BOX =
        format("tips.%railcraft.signal_sequencer_box");
    public static final String SIGNAL_INTERLOCK_BOX =
        format("tips.%railcraft.signal_interlock_box");
    public static final String SIGNAL_BLOCK_RELAY_BOX =
        format("tips.%railcraft.signal_block_relay_box");
    public static final String FIRESTONE_EMPTY =
        format("tips.%railcraft.firestone.empty");
    public static final String FIRESTONE_CHARGED =
        format("tips.%railcraft.firestone.charged");
    public static final String CURRENT_MODE =
        format("tips.%railcraft.current_mode");
    public static final String LOCKING_TRACK_LOCKDOWN =
        format("tips.%railcraft.locking_track.lockdown");
    public static final String LOCKING_TRACK_TRAIN_LOCKDOWN =
        format("tips.%railcraft.locking_track.train_lockdown");
    public static final String LOCKING_TRACK_HOLDING =
        format("tips.%railcraft.locking_track.holding");
    public static final String LOCKING_TRACK_TRAIN_HOLDING =
        format("tips.%railcraft.locking_track.train_holding");
    public static final String LOCKING_TRACK_BOARDING =
        format("tips.%railcraft.locking_track.boarding");
    public static final String LOCKING_TRACK_BOARDING_REVERSED =
        format("tips.%railcraft.locking_track.boarding_reversed");
    public static final String LOCKING_TRACK_TRAIN_BOARDING =
        format("tips.%railcraft.locking_track.train_boarding");
    public static final String LOCKING_TRACK_TRAIN_BOARDING_REVERSED =
        format("tips.%railcraft.locking_track.train_boarding_reversed");
    public static final String COUPLER_TRACK_COUPLER =
        format("tips.%railcraft.coupler_track.coupler");
    public static final String COUPLER_TRACK_DECOUPLER =
        format("tips.%railcraft.coupler_track.decoupler");
    public static final String COUPLER_TRACK_AUTO_COUPLER =
        format("tips.%railcraft.coupler_track.auto_coupler");
    public static final String TRACK_KIT_CORNERS_UNSUPPORTED =
        format("tips.%railcraft.track_kit.corners_unsupported");
    public static final String TRACK_KIT_SLOPES_UNSUPPORTED =
        format("tips.%railcraft.track_kit.slopes_unsupported");
    public static final String TRACK_KIT_INVALID_TRACK_TYPE =
        format("tips.%railcraft.track_kit.invalid_track_type");
    public static final String MANIPULATOR_REDSTONE_MODE_COMPLETE =
        format("tips.%railcraft.manipulator.redstone_mode.complete");
    public static final String MANIPULATOR_REDSTONE_MODE_COMPLETE_DESC =
        format("tips.%railcraft.manipulator.redstone_mode.complete.desc");
    public static final String MANIPULATOR_REDSTONE_MODE_IMMEDIATE =
        format("tips.%railcraft.manipulator.redstone_mode.immediate");
    public static final String MANIPULATOR_REDSTONE_MODE_IMMEDIATE_DESC =
        format("tips.%railcraft.manipulator.redstone_mode.immediate.desc");
    public static final String MANIPULATOR_REDSTONE_MODE_MANUAL =
        format("tips.%railcraft.manipulator.redstone_mode.manual");
    public static final String MANIPULATOR_REDSTONE_MODE_MANUAL_DESC =
        format("tips.%railcraft.manipulator.redstone_mode.manual.desc");
    public static final String MANIPULATOR_REDSTONE_MODE_PARTIAL =
        format("tips.%railcraft.manipulator.redstone_mode.partial");
    public static final String MANIPULATOR_REDSTONE_MODE_PARTIAL_DESC =
        format("tips.%railcraft.manipulator.redstone_mode.partial.desc");
    public static final String MANIPULATOR_TRANSFER_MODE_ALL =
        format("tips.%railcraft.manipulator.transfer_mode.all");
    public static final String MANIPULATOR_TRANSFER_MODE_ALL_DESC =
        format("tips.%railcraft.manipulator.transfer_mode.all.desc");
    public static final String MANIPULATOR_TRANSFER_MODE_EXCESS =
        format("tips.%railcraft.manipulator.transfer_mode.excess");
    public static final String MANIPULATOR_TRANSFER_MODE_EXCESS_DESC =
        format("tips.%railcraft.manipulator.transfer_mode.excess.desc");
    public static final String MANIPULATOR_TRANSFER_MODE_STOCK =
        format("tips.%railcraft.manipulator.transfer_mode.stock");
    public static final String MANIPULATOR_TRANSFER_MODE_STOCK_DESC =
        format("tips.%railcraft.manipulator.transfer_mode.stock.desc");
    public static final String MANIPULATOR_TRANSFER_MODE_TRANSFER =
        format("tips.%railcraft.manipulator.transfer_mode.transfer");
    public static final String MANIPULATOR_TRANSFER_MODE_TRANSFER_DESC =
        format("tips.%railcraft.manipulator.transfer_mode.transfer.desc");

    public static final String COAL_COKE_BLOCK = format("tips.%railcraft.coal_coke_block");
  }

  public static class Container {

    public static final String MANUAL_ROLLING_MACHINE =
        format("container.%railcraft.manual_rolling_machine");
    public static final String COKE_OVEN =
        format("container.%railcraft.coke_oven");
    public static final String BLAST_FURNACE =
        format("container.%railcraft.blast_furnace");
    public static final String CRUSHER =
        format("container.%railcraft.crusher");
    public static final String TANK =
        format("container.%railcraft.tank");
    public static final String STEAM_TURBINE =
        format("container.%railcraft.steam_turbine");
    public static final String SOLID_FUELED_STEAM_BOILER =
        format("container.%railcraft.solid_fueled_steam_boiler");
    public static final String FLUID_FUELED_STEAM_BOILER =
        format("container.%railcraft.fluid_fueled_steam_boiler");
  }

  public static class Jei {

    public static final String METAL_ROLLING =
      format("jei.%railcraft.category.rolling");
    public static final String COKE_OVEN =
        format("jei.%railcraft.category.coke_oven");
    public static final String BLAST_FURNACE =
        format("jei.%railcraft.category.blast_furnace");
    public static final String CRUSHER =
        format("jei.%railcraft.category.crusher");
    public static final String CRUSHER_TIP =
        format("jei.%railcraft.tips.crusher");
  }

  public static class Signal {

    public static final String SIGNAL_SURVEYOR_INVALID_TRACK =
        format("signal.%railcraft.surveyor.invalid_track");
    public static final String SIGNAL_SURVEYOR_BEGIN = format("signal.%railcraft.surveyor.begin");
    public static final String SIGNAL_SURVEYOR_SUCCESS = format("signal.%railcraft.surveyor.success");
    public static final String SIGNAL_SURVEYOR_INVALID_PAIR = format("signal.%railcraft.surveyor.invalid_pair");
    public static final String SIGNAL_SURVEYOR_LOST = format("signal.%railcraft.surveyor.lost");
    public static final String SIGNAL_SURVEYOR_UNLOADED =
        format("signal.%railcraft.surveyor.unloaded");
    public static final String SIGNAL_SURVEYOR_ABANDONED =
        format("signal.%railcraft.surveyor.abandoned");
    public static final String SIGNAL_SURVEYOR_INVALID_BLOCK = format("signal.%railcraft.surveyor.invalid_block");

    public static final String SIGNAL_TUNER_BEGIN = format("signal.%railcraft.tuner.begin");
    public static final String SIGNAL_TUNER_ABANDONED = format("signal.%railcraft.tuner.abandoned");
    public static final String SIGNAL_TUNER_UNLOADED = format("signal.%railcraft.tuner.unloaded");
    public static final String SIGNAL_TUNER_LOST = format("signal.%railcraft.tuner.lost");
    public static final String SIGNAL_TUNER_SUCCESS = format("signal.%railcraft.tuner.success");
  }

  public static class SignalAspect {
    public static final String GREEN = format("signal.%railcraft.aspect.green");
    public static final String YELLOW = format("signal.%railcraft.aspect.yellow");
    public static final String RED = format("signal.%railcraft.aspect.red");
    public static final String BLINK_YELLOW = format("signal.%railcraft.aspect.blink_yellow");
    public static final String BLINK_RED = format("signal.%railcraft.aspect.blink_red");
    public static final String OFF = format("signal.%railcraft.aspect.off");
  }

  public static class SignalCapacitor {
    public static final String RISING_EDGE = format("signal.%railcraft.capacitor.rising_edge");
    public static final String RISING_EDGE_DESC =
        format("signal.%railcraft.capacitor.rising_edge.desc");
    public static final String FALLING_EDGE = format("signal.%railcraft.capacitor.falling_edge");
    public static final String FALLING_EDGE_DESC =
        format("signal.%railcraft.capacitor.falling_edge.desc");
  }

  public static class Advancement {
    public static class Tracks {
      public static final String ROOT = format("advancements.%railcraft.tracks.root.name");
      public static final String ROOT_DESC = format("advancements.%railcraft.tracks.root.desc");
      public static final String MANUAL_ROLLING_MACHINE =
          format("advancements.%railcraft.tracks.manual_rolling_machine.name");
      public static final String MANUAL_ROLLING_MACHINE_DESC =
          format("advancements.%railcraft.tracks.manual_rolling_machine.desc");
      public static final String BLAST_FURNACE =
          format("advancements.%railcraft.tracks.blast_furnace.name");
      public static final String BLAST_FURNACE_DESC =
          format("advancements.%railcraft.tracks.blast_furnace.desc");
      public static final String COKE_OVEN =
          format("advancements.%railcraft.tracks.coke_oven.name");
      public static final String COKE_OVEN_DESC =
          format("advancements.%railcraft.tracks.coke_oven.desc");
      public static final String FIRESTONE =
          format("advancements.%railcraft.tracks.firestone.name");
      public static final String FIRESTONE_DESC =
          format("advancements.%railcraft.tracks.firestone.desc");
      public static final String HIGH_SPEED_TRACK =
          format("advancements.%railcraft.tracks.high_speed_track.name");
      public static final String HIGH_SPEED_TRACK_DESC =
          format("advancements.%railcraft.tracks.high_speed_track.desc");
      public static final String JUNCTIONS = format("advancements.%railcraft.tracks.junctions.name");
      public static final String JUNCTIONS_DESC =
          format("advancements.%railcraft.tracks.junctions.desc");
      public static final String REGULAR_TRACK =
          format("advancements.%railcraft.tracks.regular_track.name");
      public static final String REGULAR_TRACK_DESC =
          format("advancements.%railcraft.tracks.regular_track.desc");
      public static final String CRUSHER = format("advancements.%railcraft.tracks.crusher.name");
      public static final String CRUSHER_DESC = format("advancements.%railcraft.tracks.crusher.desc");
      public static final String TRACK_KIT = format("advancements.%railcraft.tracks.track_kit.name");
      public static final String TRACK_KIT_DESC =
          format("advancements.%railcraft.tracks.track_kit.desc");
      public static final String WOODEN_TRACK =
          format("advancements.%railcraft.tracks.wooden_track.name");
      public static final String WOODEN_TRACK_DESC =
          format("advancements.%railcraft.tracks.wooden_track.desc");
    }

    public static class Carts {
      public static final String ROOT = format("advancements.%railcraft.carts.root.name");
      public static final String ROOT_DESC = format("advancements.%railcraft.carts.root.desc");
      public static final String BED_CART = format("advancements.%railcraft.carts.bed_cart.name");
      public static final String BED_CART_DESC =
          format("advancements.%railcraft.carts.bed_cart.desc");
      public static final String JUKEBOX_CART =
          format("advancements.%railcraft.carts.jukebox_cart.name");
      public static final String JUKEBOX_CART_DESC =
          format("advancements.%railcraft.carts.jukebox_cart.desc");
      public static final String LINK_CARTS =
          format("advancements.%railcraft.carts.link_carts.name");
      public static final String LINK_CARTS_DESC =
          format("advancements.%railcraft.carts.link_carts.desc");
      public static final String LOCOMOTIVE =
          format("advancements.%railcraft.carts.locomotive.name");
      public static final String LOCOMOTIVE_DESC =
          format("advancements.%railcraft.carts.locomotive.desc");
      public static final String SEASONS = format("advancements.%railcraft.carts.seasons.name");
      public static final String SEASONS_DESC =
          format("advancements.%railcraft.carts.seasons.desc");
      public static final String SURPRISE =
          format("advancements.%railcraft.carts.surprise.name");
      public static final String SURPRISE_DESC =
          format("advancements.%railcraft.carts.surprise.desc");
    }
  }

  public static class Subtitle {
    public static final String STEAM_WHISTLE =
        format("subtitle.%railcraft.locomotive.steam.whistle");
    public static final String ELECTRIC_WHISTLE =
        format("subtitle.%railcraft.locomotive.electric.whistle");
    public static final String STEAM_BURST =
        format("subtitle.%railcraft.machine.steam.burst");
    public static final String STEAM_HISS =
        format("subtitle.%railcraft.machine.steam.hiss");
    public static final String MACHINE_ZAP =
        format("subtitle.%railcraft.machine.zap");
  }

  private static String format(String key) {
    return key.replace("%railcraft", Railcraft.ID);
  }
}
