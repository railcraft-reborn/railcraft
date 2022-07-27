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
  }

  public static class Container {

    public static final String MANUAL_ROLLING_MACHINE =
        format("container.%railcraft.manual_rolling_machine");
    public static final String COKE_OVEN =
        format("container.%railcraft.coke_oven");
    public static final String BLAST_FURNACE =
        format("container.%railcraft.blast_furnace");
    public static final String TANK =
        format("container.%railcraft.tank");
    public static final String STEAM_TURBINE =
        format("container.%railcraft.steam_turbine");
    public static final String SOLID_FUELED_STEAM_BOILER =
        format("container.%railcraft.solid_fueled_steam_boiler");
    public static final String FLUID_FUELED_STEAM_BOILER =
        format("container.%railcraft.fluid_fueled_steam_boiler");
  }

  private static String format(String key) {
    return key.replace("%railcraft", Railcraft.ID);
  }
}
