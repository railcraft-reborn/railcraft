package mods.railcraft.world.item;

import java.util.List;
import mods.railcraft.Translations;
import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.world.level.block.RailcraftBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTab.TabVisibility;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.util.MutableHashedLinkedMap;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class RailcraftCreativeModeTabs {

  public static final DeferredRegister<CreativeModeTab> deferredRegister =
      DeferredRegister.create(Registries.CREATIVE_MODE_TAB, RailcraftConstants.ID);
  private static final TabVisibility DEFAULT_VISIBILITY = TabVisibility.PARENT_AND_SEARCH_TABS;
  public static final RegistryObject<CreativeModeTab> MAIN_TAB =
      deferredRegister.register("main_tab", () -> CreativeModeTab.builder()
          .withTabsBefore(CreativeModeTabs.SPAWN_EGGS)
          .title(Component.translatable(Translations.Tab.RAILCRAFT))
          .icon(() -> new ItemStack(RailcraftItems.IRON_CROWBAR.get()))
          .displayItems((params, output) -> {
            output.accept(RailcraftItems.LOW_PRESSURE_STEAM_BOILER_TANK.get());
            output.accept(RailcraftItems.HIGH_PRESSURE_STEAM_BOILER_TANK.get());
            output.accept(RailcraftItems.SOLID_FUELED_FIREBOX.get());
            output.accept(RailcraftItems.FLUID_FUELED_FIREBOX.get());
            output.accept(RailcraftItems.TURBINE_BLADE.get());
            output.accept(RailcraftItems.TURBINE_DISK.get());
            output.accept(RailcraftItems.TURBINE_ROTOR.get());
            output.accept(RailcraftItems.STEAM_TURBINE.get());
            output.accept(RailcraftItems.STEAM_OVEN.get());
            output.accept(RailcraftItems.MANUAL_ROLLING_MACHINE.get());
            output.accept(RailcraftItems.POWERED_ROLLING_MACHINE.get());
            output.accept(RailcraftItems.CRUSHER.get());
            output.accept(RailcraftItems.COKE_OVEN_BRICKS.get());
            output.accept(RailcraftItems.BLAST_FURNACE_BRICKS.get());
            output.accept(RailcraftItems.WATER_TANK_SIDING.get());
            output.accept(RailcraftItems.FEED_STATION.get());
            output.accept(RailcraftItems.FRAME_BLOCK.get());
            output.accept(RailcraftItems.CHARGE_METER.get());
            output.accept(RailcraftItems.NICKEL_ZINC_BATTERY.get());
            output.accept(RailcraftItems.NICKEL_IRON_BATTERY.get());
            output.accept(RailcraftItems.ZINC_CARBON_BATTERY.get());
            output.accept(RailcraftItems.ZINC_CARBON_BATTERY_EMPTY.get());
            output.accept(RailcraftItems.ZINC_SILVER_BATTERY.get());
            output.accept(RailcraftItems.ZINC_SILVER_BATTERY_EMPTY.get());
            output.accept(RailcraftItems.STEEL_ANVIL.get());
            output.accept(RailcraftItems.CHIPPED_STEEL_ANVIL.get());
            output.accept(RailcraftItems.DAMAGED_STEEL_ANVIL.get());

            output.accept(RailcraftItems.CRUSHED_OBSIDIAN.get());
            output.accept(RailcraftItems.COAL_COKE.get());
            output.accept(RailcraftItems.COKE_BLOCK.get());

            output.accept(RailcraftItems.SULFUR_ORE.get());
            output.accept(RailcraftItems.DEEPSLATE_SULFUR_ORE.get());
            output.accept(RailcraftItems.SALTPETER_ORE.get());
            output.accept(RailcraftItems.LEAD_ORE.get());
            output.accept(RailcraftItems.DEEPSLATE_LEAD_ORE.get());
            output.accept(RailcraftItems.NICKEL_ORE.get());
            output.accept(RailcraftItems.DEEPSLATE_NICKEL_ORE.get());
            output.accept(RailcraftItems.SILVER_ORE.get());
            output.accept(RailcraftItems.DEEPSLATE_SILVER_ORE.get());
            output.accept(RailcraftItems.TIN_ORE.get());
            output.accept(RailcraftItems.DEEPSLATE_TIN_ORE.get());
            output.accept(RailcraftItems.ZINC_ORE.get());
            output.accept(RailcraftItems.DEEPSLATE_ZINC_ORE.get());

            output.accept(RailcraftItems.LEAD_RAW.get());
            output.accept(RailcraftItems.NICKEL_RAW.get());
            output.accept(RailcraftItems.SILVER_RAW.get());
            output.accept(RailcraftItems.TIN_RAW.get());
            output.accept(RailcraftItems.ZINC_RAW.get());

            output.accept(RailcraftItems.LEAD_BLOCK.get());
            output.accept(RailcraftItems.NICKEL_BLOCK.get());
            output.accept(RailcraftItems.SILVER_BLOCK.get());
            output.accept(RailcraftItems.TIN_BLOCK.get());
            output.accept(RailcraftItems.ZINC_BLOCK.get());
            output.accept(RailcraftItems.STEEL_BLOCK.get());
            output.accept(RailcraftItems.BRASS_BLOCK.get());
            output.accept(RailcraftItems.BRONZE_BLOCK.get());
            output.accept(RailcraftItems.INVAR_BLOCK.get());

            output.accept(RailcraftItems.LEAD_INGOT.get());
            output.accept(RailcraftItems.NICKEL_INGOT.get());
            output.accept(RailcraftItems.SILVER_INGOT.get());
            output.accept(RailcraftItems.TIN_INGOT.get());
            output.accept(RailcraftItems.ZINC_INGOT.get());
            output.accept(RailcraftItems.STEEL_INGOT.get());
            output.accept(RailcraftItems.BRASS_INGOT.get());
            output.accept(RailcraftItems.BRONZE_INGOT.get());
            output.accept(RailcraftItems.INVAR_INGOT.get());

            output.accept(RailcraftItems.LEAD_NUGGET.get());
            output.accept(RailcraftItems.NICKEL_NUGGET.get());
            output.accept(RailcraftItems.SILVER_NUGGET.get());
            output.accept(RailcraftItems.TIN_NUGGET.get());
            output.accept(RailcraftItems.ZINC_NUGGET.get());
            output.accept(RailcraftItems.STEEL_NUGGET.get());
            output.accept(RailcraftItems.BRASS_NUGGET.get());
            output.accept(RailcraftItems.BRONZE_NUGGET.get());
            output.accept(RailcraftItems.INVAR_NUGGET.get());

            output.accept(RailcraftItems.IRON_PLATE.get());
            output.accept(RailcraftItems.COPPER_PLATE.get());
            output.accept(RailcraftItems.GOLD_PLATE.get());
            output.accept(RailcraftItems.LEAD_PLATE.get());
            output.accept(RailcraftItems.NICKEL_PLATE.get());
            output.accept(RailcraftItems.SILVER_PLATE.get());
            output.accept(RailcraftItems.TIN_PLATE.get());
            output.accept(RailcraftItems.ZINC_PLATE.get());
            output.accept(RailcraftItems.STEEL_PLATE.get());
            output.accept(RailcraftItems.BRASS_PLATE.get());
            output.accept(RailcraftItems.BRONZE_PLATE.get());
            output.accept(RailcraftItems.INVAR_PLATE.get());

            output.accept(RailcraftItems.BUSHING_GEAR.get());
            output.accept(RailcraftItems.IRON_GEAR.get());
            output.accept(RailcraftItems.COPPER_GEAR.get());
            output.accept(RailcraftItems.GOLD_GEAR.get());
            output.accept(RailcraftItems.LEAD_GEAR.get());
            output.accept(RailcraftItems.NICKEL_GEAR.get());
            output.accept(RailcraftItems.SILVER_GEAR.get());
            output.accept(RailcraftItems.TIN_GEAR.get());
            output.accept(RailcraftItems.ZINC_GEAR.get());
            output.accept(RailcraftItems.STEEL_GEAR.get());
            output.accept(RailcraftItems.BRASS_GEAR.get());
            output.accept(RailcraftItems.BRONZE_GEAR.get());
            output.accept(RailcraftItems.INVAR_GEAR.get());

            output.accept(RailcraftItems.CARBON_ELECTRODE.get());
            output.accept(RailcraftItems.IRON_ELECTRODE.get());
            output.accept(RailcraftItems.COPPER_ELECTRODE.get());
            output.accept(RailcraftItems.GOLD_ELECTRODE.get());
            output.accept(RailcraftItems.LEAD_ELECTRODE.get());
            output.accept(RailcraftItems.NICKEL_ELECTRODE.get());
            output.accept(RailcraftItems.SILVER_ELECTRODE.get());
            output.accept(RailcraftItems.TIN_ELECTRODE.get());
            output.accept(RailcraftItems.ZINC_ELECTRODE.get());
            output.accept(RailcraftItems.STEEL_ELECTRODE.get());
            output.accept(RailcraftItems.BRASS_ELECTRODE.get());
            output.accept(RailcraftItems.BRONZE_ELECTRODE.get());
            output.accept(RailcraftItems.INVAR_ELECTRODE.get());

            output.accept(RailcraftItems.SALTPETER_DUST.get());
            output.accept(RailcraftItems.COAL_DUST.get());
            output.accept(RailcraftItems.CHARCOAL_DUST.get());
            output.accept(RailcraftItems.SLAG.get());
            output.accept(RailcraftItems.ENDER_DUST.get());
            output.accept(RailcraftItems.SULFUR_DUST.get());
            output.accept(RailcraftItems.OBSIDIAN_DUST.get());
            output.accept(RailcraftItems.BRONZE_BLOCK.get());
            output.accept(RailcraftItems.INVAR_BLOCK.get());

            output.accept(RailcraftItems.IRON_TUNNEL_BORE_HEAD.get());
            output.accept(RailcraftItems.BRONZE_TUNNEL_BORE_HEAD.get());
            output.accept(RailcraftItems.STEEL_TUNNEL_BORE_HEAD.get());
            output.accept(RailcraftItems.DIAMOND_TUNNEL_BORE_HEAD.get());

            output.accept(RailcraftItems.ITEM_LOADER.get());
            output.accept(RailcraftItems.ADVANCED_ITEM_LOADER.get());
            output.accept(RailcraftItems.ITEM_UNLOADER.get());
            output.accept(RailcraftItems.ADVANCED_ITEM_UNLOADER.get());
            output.accept(RailcraftItems.FLUID_LOADER.get());
            output.accept(RailcraftItems.FLUID_UNLOADER.get());
            output.accept(RailcraftItems.CART_DISPENSER.get());
            output.accept(RailcraftItems.TRAIN_DISPENSER.get());

            output.accept(RailcraftBlocks.ADVANCED_DETECTOR.get());
            output.accept(RailcraftBlocks.AGE_DETECTOR.get());
            output.accept(RailcraftBlocks.ANIMAL_DETECTOR.get());
            output.accept(RailcraftBlocks.ANY_DETECTOR.get());
            output.accept(RailcraftBlocks.EMPTY_DETECTOR.get());
            output.accept(RailcraftBlocks.ITEM_DETECTOR.get());
            output.accept(RailcraftBlocks.LOCOMOTIVE_DETECTOR.get());
            output.accept(RailcraftBlocks.MOB_DETECTOR.get());
            output.accept(RailcraftBlocks.PLAYER_DETECTOR.get());
            output.accept(RailcraftBlocks.ROUTING_DETECTOR.get());
            output.accept(RailcraftBlocks.SHEEP_DETECTOR.get());
            output.accept(RailcraftBlocks.TANK_DETECTOR.get());
            output.accept(RailcraftBlocks.TRAIN_DETECTOR.get());
            output.accept(RailcraftBlocks.VILLAGER_DETECTOR.get());

            output.accept(RailcraftItems.IRON_SPIKE_MAUL.get());
            output.accept(RailcraftItems.STEEL_SPIKE_MAUL.get());
            output.accept(RailcraftItems.DIAMOND_SPIKE_MAUL.get());

            output.accept(RailcraftItems.IRON_CROWBAR.get());
            output.accept(RailcraftItems.STEEL_CROWBAR.get());
            output.accept(RailcraftItems.DIAMOND_CROWBAR.get());
            output.accept(RailcraftItems.SEASONS_CROWBAR.get());

            output.accept(RailcraftItems.REBAR.get());
            output.accept(RailcraftItems.WHISTLE_TUNER.get());
            output.accept(RailcraftItems.SIGNAL_TUNER.get());
            output.accept(RailcraftItems.SIGNAL_BLOCK_SURVEYOR.get());
            output.accept(RailcraftItems.SIGNAL_LABEL.get());
            output.accept(RailcraftItems.GOLDEN_TICKET.get());
            output.accept(RailcraftItems.TICKET.get());
            output.accept(RailcraftItems.ROUTING_TABLE_BOOK.get());
            output.accept(RailcraftItems.LOGBOOK.get());
            output.accept(RailcraftItems.GOGGLES.get());
            output.accept(RailcraftItems.OVERALLS.get());

            output.accept(RailcraftItems.CONTROLLER_CIRCUIT.get());
            output.accept(RailcraftItems.RECEIVER_CIRCUIT.get());
            output.accept(RailcraftItems.SIGNAL_CIRCUIT.get());
            output.accept(RailcraftItems.RADIO_CIRCUIT.get());

            output.accept(RailcraftItems.FIRESTONE_ORE.get());
            RailcraftItems.RAW_FIRESTONE.get().fillItemCategory(output);
            RailcraftItems.REFINED_FIRESTONE.get().fillItemCategory(output);
            RailcraftItems.CRACKED_FIRESTONE.get().fillItemCategory(output);
            RailcraftItems.CUT_FIRESTONE.get().fillItemCategory(output);

            output.accept(RailcraftItems.BAG_OF_CEMENT.get());
            output.accept(RailcraftItems.TRACK_PARTS.get());
            output.accept(RailcraftItems.WOODEN_TIE.get());
            output.accept(RailcraftItems.STONE_TIE.get());
            output.accept(RailcraftItems.WOODEN_RAILBED.get());
            output.accept(RailcraftItems.STONE_RAILBED.get());

            output.accept(RailcraftItems.WOODEN_RAIL.get());
            output.accept(RailcraftItems.STANDARD_RAIL.get());
            output.accept(RailcraftItems.ADVANCED_RAIL.get());
            output.accept(RailcraftItems.REINFORCED_RAIL.get());
            output.accept(RailcraftItems.HIGH_SPEED_RAIL.get());
            output.accept(RailcraftItems.ELECTRIC_RAIL.get());

            output.accept(RailcraftItems.FORCE_TRACK_EMITTER.get());
            output.accept(RailcraftItems.SIGNAL_LAMP.get());
            output.accept(RailcraftItems.CHARGE_SPOOL_SMALL.get());
            output.accept(RailcraftItems.CHARGE_SPOOL_MEDIUM.get());
            output.accept(RailcraftItems.CHARGE_SPOOL_LARGE.get());
            output.accept(RailcraftItems.CHARGE_MOTOR.get());
            output.accept(RailcraftItems.CHARGE_COIL.get());
            output.accept(RailcraftItems.CHARGE_TERMINAL.get());

            output.accept(RailcraftItems.CREOSOTE_BOTTLE.get());
            output.accept(RailcraftItems.CREOSOTE_BUCKET.get());

            output.accept(RailcraftItems.IRON_TANK_GAUGE.variantFor(DyeColor.WHITE).get());
            output.accept(RailcraftItems.IRON_TANK_VALVE.variantFor(DyeColor.WHITE).get());
            output.accept(RailcraftItems.IRON_TANK_WALL.variantFor(DyeColor.WHITE).get());
            output.accept(RailcraftItems.STEEL_TANK_GAUGE.variantFor(DyeColor.WHITE).get());
            output.accept(RailcraftItems.STEEL_TANK_VALVE.variantFor(DyeColor.WHITE).get());
            output.accept(RailcraftItems.STEEL_TANK_WALL.variantFor(DyeColor.WHITE).get());
          })
          .build());
  public static final RegistryObject<CreativeModeTab> OUTFITTED_TRACKS =
      deferredRegister.register("outfitted_tracks", () -> CreativeModeTab.builder()
          .withTabsBefore(MAIN_TAB.getId())
          .title(Component.translatable(Translations.Tab.RAILCRAFT_OUTFITTED_TRACKS))
          .icon(() -> new ItemStack(RailcraftItems.IRON_DETECTOR_TRACK.get()))
          .displayItems((params, output) -> {
            /*
             * TODO:
             *  - Messenger track
             *  - Delayed locking track
             */
            output.accept(RailcraftItems.ABANDONED_ACTIVATOR_TRACK.get());
            output.accept(RailcraftItems.ABANDONED_BOOSTER_TRACK.get());
            output.accept(RailcraftItems.ABANDONED_BUFFER_STOP_TRACK.get());
            output.accept(RailcraftItems.ABANDONED_COUPLER_TRACK.get());
            output.accept(RailcraftItems.ABANDONED_CONTROL_TRACK.get());
            output.accept(RailcraftItems.ABANDONED_DETECTOR_TRACK.get());
            output.accept(RailcraftItems.ABANDONED_EMBARKING_TRACK.get());
            output.accept(RailcraftItems.ABANDONED_DISEMBARKING_TRACK.get());
            output.accept(RailcraftItems.ABANDONED_DUMPING_TRACK.get());
            output.accept(RailcraftItems.ABANDONED_GATED_TRACK.get());
            output.accept(RailcraftItems.ABANDONED_LAUNCHER_TRACK.get());
            output.accept(RailcraftItems.ABANDONED_LOCKING_TRACK.get());
            output.accept(RailcraftItems.ABANDONED_WHISTLE_TRACK.get());
            output.accept(RailcraftItems.ABANDONED_LOCOMOTIVE_TRACK.get());
            output.accept(RailcraftItems.ABANDONED_THROTTLE_TRACK.get());
            output.accept(RailcraftItems.ABANDONED_ROUTING_TRACK.get());
            output.accept(RailcraftItems.ABANDONED_ONE_WAY_TRACK.get());
            output.accept(RailcraftItems.ABANDONED_WYE_TRACK.get());
            output.accept(RailcraftItems.ABANDONED_TURNOUT_TRACK.get());
            output.accept(RailcraftItems.ABANDONED_JUNCTION_TRACK.get());

            output.accept(RailcraftItems.IRON_ACTIVATOR_TRACK.get());
            output.accept(RailcraftItems.IRON_BOOSTER_TRACK.get());
            output.accept(RailcraftItems.IRON_BUFFER_STOP_TRACK.get());
            output.accept(RailcraftItems.IRON_COUPLER_TRACK.get());
            output.accept(RailcraftItems.IRON_CONTROL_TRACK.get());
            output.accept(RailcraftItems.IRON_DETECTOR_TRACK.get());
            output.accept(RailcraftItems.IRON_EMBARKING_TRACK.get());
            output.accept(RailcraftItems.IRON_DISEMBARKING_TRACK.get());
            output.accept(RailcraftItems.IRON_DUMPING_TRACK.get());
            output.accept(RailcraftItems.IRON_GATED_TRACK.get());
            output.accept(RailcraftItems.IRON_LAUNCHER_TRACK.get());
            output.accept(RailcraftItems.IRON_LOCKING_TRACK.get());
            output.accept(RailcraftItems.IRON_WHISTLE_TRACK.get());
            output.accept(RailcraftItems.IRON_LOCOMOTIVE_TRACK.get());
            output.accept(RailcraftItems.IRON_THROTTLE_TRACK.get());
            output.accept(RailcraftItems.IRON_ROUTING_TRACK.get());
            output.accept(RailcraftItems.IRON_ONE_WAY_TRACK.get());
            output.accept(RailcraftItems.IRON_WYE_TRACK.get());
            output.accept(RailcraftItems.IRON_TURNOUT_TRACK.get());
            output.accept(RailcraftItems.IRON_JUNCTION_TRACK.get());

            output.accept(RailcraftItems.STRAP_IRON_ACTIVATOR_TRACK.get());
            output.accept(RailcraftItems.STRAP_IRON_BOOSTER_TRACK.get());
            output.accept(RailcraftItems.STRAP_IRON_BUFFER_STOP_TRACK.get());
            output.accept(RailcraftItems.STRAP_IRON_COUPLER_TRACK.get());
            output.accept(RailcraftItems.STRAP_IRON_CONTROL_TRACK.get());
            output.accept(RailcraftItems.STRAP_IRON_DETECTOR_TRACK.get());
            output.accept(RailcraftItems.STRAP_IRON_EMBARKING_TRACK.get());
            output.accept(RailcraftItems.STRAP_IRON_DISEMBARKING_TRACK.get());
            output.accept(RailcraftItems.STRAP_IRON_DUMPING_TRACK.get());
            output.accept(RailcraftItems.STRAP_IRON_GATED_TRACK.get());
            output.accept(RailcraftItems.STRAP_IRON_LAUNCHER_TRACK.get());
            output.accept(RailcraftItems.STRAP_IRON_LOCKING_TRACK.get());
            output.accept(RailcraftItems.STRAP_IRON_WHISTLE_TRACK.get());
            output.accept(RailcraftItems.STRAP_IRON_LOCOMOTIVE_TRACK.get());
            output.accept(RailcraftItems.STRAP_IRON_THROTTLE_TRACK.get());
            output.accept(RailcraftItems.STRAP_IRON_ROUTING_TRACK.get());
            output.accept(RailcraftItems.STRAP_IRON_ONE_WAY_TRACK.get());
            output.accept(RailcraftItems.STRAP_IRON_WYE_TRACK.get());
            output.accept(RailcraftItems.STRAP_IRON_TURNOUT_TRACK.get());
            output.accept(RailcraftItems.STRAP_IRON_JUNCTION_TRACK.get());

            output.accept(RailcraftItems.REINFORCED_ACTIVATOR_TRACK.get());
            output.accept(RailcraftItems.REINFORCED_BOOSTER_TRACK.get());
            output.accept(RailcraftItems.REINFORCED_BUFFER_STOP_TRACK.get());
            output.accept(RailcraftItems.REINFORCED_COUPLER_TRACK.get());
            output.accept(RailcraftItems.REINFORCED_CONTROL_TRACK.get());
            output.accept(RailcraftItems.REINFORCED_DETECTOR_TRACK.get());
            output.accept(RailcraftItems.REINFORCED_EMBARKING_TRACK.get());
            output.accept(RailcraftItems.REINFORCED_DISEMBARKING_TRACK.get());
            output.accept(RailcraftItems.REINFORCED_DUMPING_TRACK.get());
            output.accept(RailcraftItems.REINFORCED_GATED_TRACK.get());
            output.accept(RailcraftItems.REINFORCED_LAUNCHER_TRACK.get());
            output.accept(RailcraftItems.REINFORCED_LOCKING_TRACK.get());
            output.accept(RailcraftItems.REINFORCED_WHISTLE_TRACK.get());
            output.accept(RailcraftItems.REINFORCED_LOCOMOTIVE_TRACK.get());
            output.accept(RailcraftItems.REINFORCED_THROTTLE_TRACK.get());
            output.accept(RailcraftItems.REINFORCED_ROUTING_TRACK.get());
            output.accept(RailcraftItems.REINFORCED_ONE_WAY_TRACK.get());
            output.accept(RailcraftItems.REINFORCED_WYE_TRACK.get());
            output.accept(RailcraftItems.REINFORCED_TURNOUT_TRACK.get());
            output.accept(RailcraftItems.REINFORCED_JUNCTION_TRACK.get());

            output.accept(RailcraftItems.ELECTRIC_ACTIVATOR_TRACK.get());
            output.accept(RailcraftItems.ELECTRIC_BOOSTER_TRACK.get());
            output.accept(RailcraftItems.ELECTRIC_BUFFER_STOP_TRACK.get());
            output.accept(RailcraftItems.ELECTRIC_COUPLER_TRACK.get());
            output.accept(RailcraftItems.ELECTRIC_CONTROL_TRACK.get());
            output.accept(RailcraftItems.ELECTRIC_DETECTOR_TRACK.get());
            output.accept(RailcraftItems.ELECTRIC_EMBARKING_TRACK.get());
            output.accept(RailcraftItems.ELECTRIC_DISEMBARKING_TRACK.get());
            output.accept(RailcraftItems.ELECTRIC_DUMPING_TRACK.get());
            output.accept(RailcraftItems.ELECTRIC_GATED_TRACK.get());
            output.accept(RailcraftItems.ELECTRIC_LAUNCHER_TRACK.get());
            output.accept(RailcraftItems.ELECTRIC_LOCKING_TRACK.get());
            output.accept(RailcraftItems.ELECTRIC_WHISTLE_TRACK.get());
            output.accept(RailcraftItems.ELECTRIC_LOCOMOTIVE_TRACK.get());
            output.accept(RailcraftItems.ELECTRIC_THROTTLE_TRACK.get());
            output.accept(RailcraftItems.ELECTRIC_ROUTING_TRACK.get());
            output.accept(RailcraftItems.ELECTRIC_ONE_WAY_TRACK.get());
            output.accept(RailcraftItems.ELECTRIC_WYE_TRACK.get());
            output.accept(RailcraftItems.ELECTRIC_TURNOUT_TRACK.get());
            output.accept(RailcraftItems.ELECTRIC_JUNCTION_TRACK.get());

            output.accept(RailcraftItems.HIGH_SPEED_ACTIVATOR_TRACK.get());
            output.accept(RailcraftItems.HIGH_SPEED_BOOSTER_TRACK.get());
            output.accept(RailcraftItems.HIGH_SPEED_DETECTOR_TRACK.get());
            output.accept(RailcraftItems.HIGH_SPEED_LOCKING_TRACK.get());
            output.accept(RailcraftItems.HIGH_SPEED_WHISTLE_TRACK.get());
            output.accept(RailcraftItems.HIGH_SPEED_LOCOMOTIVE_TRACK.get());
            output.accept(RailcraftItems.HIGH_SPEED_THROTTLE_TRACK.get());
            output.accept(RailcraftItems.HIGH_SPEED_TRANSITION_TRACK.get());
            output.accept(RailcraftItems.HIGH_SPEED_WYE_TRACK.get());
            output.accept(RailcraftItems.HIGH_SPEED_TURNOUT_TRACK.get());
            output.accept(RailcraftItems.HIGH_SPEED_JUNCTION_TRACK.get());

            output.accept(RailcraftItems.HIGH_SPEED_ELECTRIC_ACTIVATOR_TRACK.get());
            output.accept(RailcraftItems.HIGH_SPEED_ELECTRIC_BOOSTER_TRACK.get());
            output.accept(RailcraftItems.HIGH_SPEED_ELECTRIC_DETECTOR_TRACK.get());
            output.accept(RailcraftItems.HIGH_SPEED_ELECTRIC_LOCKING_TRACK.get());
            output.accept(RailcraftItems.HIGH_SPEED_ELECTRIC_WHISTLE_TRACK.get());
            output.accept(RailcraftItems.HIGH_SPEED_ELECTRIC_LOCOMOTIVE_TRACK.get());
            output.accept(RailcraftItems.HIGH_SPEED_ELECTRIC_THROTTLE_TRACK.get());
            output.accept(RailcraftItems.HIGH_SPEED_ELECTRIC_TRANSITION_TRACK.get());
            output.accept(RailcraftItems.HIGH_SPEED_ELECTRIC_WYE_TRACK.get());
            output.accept(RailcraftItems.HIGH_SPEED_ELECTRIC_TURNOUT_TRACK.get());
            output.accept(RailcraftItems.HIGH_SPEED_ELECTRIC_JUNCTION_TRACK.get());
          }).build());
  public static final RegistryObject<CreativeModeTab> DECORATIVE_BLOCKS =
      deferredRegister.register("decorative_blocks", () -> CreativeModeTab.builder()
          .withTabsBefore(OUTFITTED_TRACKS.getId())
          .title(Component.translatable(Translations.Tab.RAILCRAFT_DECORATIVE_BLOCKS))
          .icon(() -> new ItemStack(
              RailcraftItems.STRENGTHENED_GLASS.variantFor(DyeColor.BLACK).get()))
          .displayItems((params, output) -> {
            output.accept(RailcraftItems.QUARRIED_STONE.get());
            output.accept(RailcraftItems.QUARRIED_COBBLESTONE.get());
            output.accept(RailcraftItems.POLISHED_QUARRIED_STONE.get());
            output.accept(RailcraftItems.CHISELED_QUARRIED_STONE.get());
            output.accept(RailcraftItems.ETCHED_QUARRIED_STONE.get());
            output.accept(RailcraftItems.QUARRIED_BRICKS.get());
            output.accept(RailcraftItems.QUARRIED_BRICK_STAIRS.get());
            output.accept(RailcraftItems.QUARRIED_BRICK_SLAB.get());
            output.accept(RailcraftItems.QUARRIED_PAVER.get());
            output.accept(RailcraftItems.QUARRIED_PAVER_STAIRS.get());
            output.accept(RailcraftItems.QUARRIED_PAVER_SLAB.get());
            output.accept(RailcraftItems.ABYSSAL_STONE.get());
            output.accept(RailcraftItems.ABYSSAL_COBBLESTONE.get());
            output.accept(RailcraftItems.POLISHED_ABYSSAL_STONE.get());
            output.accept(RailcraftItems.CHISELED_ABYSSAL_STONE.get());
            output.accept(RailcraftItems.ETCHED_ABYSSAL_STONE.get());
            output.accept(RailcraftItems.ABYSSAL_BRICKS.get());
            output.accept(RailcraftItems.ABYSSAL_BRICK_STAIRS.get());
            output.accept(RailcraftItems.ABYSSAL_BRICK_SLAB.get());
            output.accept(RailcraftItems.ABYSSAL_PAVER.get());
            output.accept(RailcraftItems.ABYSSAL_PAVER_STAIRS.get());
            output.accept(RailcraftItems.ABYSSAL_PAVER_SLAB.get());

            for (var color : DyeColor.values()) {
              output.accept(RailcraftItems.STRENGTHENED_GLASS.variantFor(color).get());
            }
            for (var color : DyeColor.values()) {
              output.accept(RailcraftItems.POST.variantFor(color).get());
            }
            for (var color : DyeColor.values()) {
              output.accept(RailcraftItems.IRON_TANK_GAUGE.variantFor(color).get());
            }
            for (var color : DyeColor.values()) {
              output.accept(RailcraftItems.IRON_TANK_VALVE.variantFor(color).get());
            }
            for (var color : DyeColor.values()) {
              output.accept(RailcraftItems.IRON_TANK_WALL.variantFor(color).get());
            }
            for (var color : DyeColor.values()) {
              output.accept(RailcraftItems.STEEL_TANK_GAUGE.variantFor(color).get());
            }
            for (var color : DyeColor.values()) {
              output.accept(RailcraftItems.STEEL_TANK_VALVE.variantFor(color).get());
            }
            for (var color : DyeColor.values()) {
              output.accept(RailcraftItems.STEEL_TANK_WALL.variantFor(color).get());
            }
          }).build());

  public static void register(IEventBus modEventBus) {
    deferredRegister.register(modEventBus);
  }

  public static void addToolsAndUtilities(
      MutableHashedLinkedMap<ItemStack, TabVisibility> entries) {
    entries.putAfter(
        new ItemStack(Items.SHEARS),
        new ItemStack(RailcraftItems.STEEL_SHEARS.get()),
        DEFAULT_VISIBILITY);
    entries.putAfter(
        new ItemStack(Items.CHEST_MINECART),
        new ItemStack(RailcraftItems.TANK_MINECART.get()),
        DEFAULT_VISIBILITY);

    var addAfterIronHoe = List.of(
        Items.IRON_HOE,
        RailcraftItems.STEEL_SHOVEL.get(),
        RailcraftItems.STEEL_PICKAXE.get(),
        RailcraftItems.STEEL_AXE.get(),
        RailcraftItems.STEEL_HOE.get());
    var addAfterTNTMinecart = List.of(
        Items.TNT_MINECART,
        RailcraftItems.TRACK_LAYER.get(),
        RailcraftItems.TRACK_RELAYER.get(),
        RailcraftItems.TRACK_REMOVER.get(),
        RailcraftItems.TRACK_UNDERCUTTER.get(),

        RailcraftItems.TUNNEL_BORE.get(),
        RailcraftItems.STEAM_LOCOMOTIVE.get(),
        RailcraftItems.ELECTRIC_LOCOMOTIVE.get(),
        RailcraftItems.CREATIVE_LOCOMOTIVE.get());
    var addAfterActivatorRail = List.of(
        Items.ACTIVATOR_RAIL,
        RailcraftItems.ABANDONED_TRACK.get(),
        RailcraftItems.ELECTRIC_TRACK.get(),
        RailcraftItems.HIGH_SPEED_TRACK.get(),
        RailcraftItems.HIGH_SPEED_ELECTRIC_TRACK.get(),
        RailcraftItems.REINFORCED_TRACK.get(),
        RailcraftItems.STRAP_IRON_TRACK.get(),
        RailcraftItems.ELEVATOR_TRACK.get(),
        RailcraftItems.TRANSITION_TRACK_KIT.get(),
        RailcraftItems.LOCKING_TRACK_KIT.get(),
        RailcraftItems.BUFFER_STOP_TRACK_KIT.get(),
        RailcraftItems.ACTIVATOR_TRACK_KIT.get(),
        RailcraftItems.BOOSTER_TRACK_KIT.get(),
        RailcraftItems.CONTROL_TRACK_KIT.get(),
        RailcraftItems.GATED_TRACK_KIT.get(),
        RailcraftItems.DETECTOR_TRACK_KIT.get(),
        RailcraftItems.COUPLER_TRACK_KIT.get(),
        RailcraftItems.EMBARKING_TRACK_KIT.get(),
        RailcraftItems.DISEMBARKING_TRACK_KIT.get(),
        RailcraftItems.DUMPING_TRACK_KIT.get(),
        RailcraftItems.LAUNCHER_TRACK_KIT.get(),
        RailcraftItems.ONE_WAY_TRACK_KIT.get(),
        RailcraftItems.WHISTLE_TRACK_KIT.get(),
        RailcraftItems.LOCOMOTIVE_TRACK_KIT.get(),
        RailcraftItems.THROTTLE_TRACK_KIT.get(),
        RailcraftItems.ROUTING_TRACK_KIT.get(),
        RailcraftItems.SWITCH_TRACK_LEVER.get(),
        RailcraftItems.SWITCH_TRACK_MOTOR.get(),
        RailcraftItems.SWITCH_TRACK_ROUTER.get(),
        RailcraftItems.ANALOG_SIGNAL_CONTROLLER_BOX.get(),
        RailcraftItems.SIGNAL_SEQUENCER_BOX.get(),
        RailcraftItems.SIGNAL_CAPACITOR_BOX.get(),
        RailcraftItems.SIGNAL_INTERLOCK_BOX.get(),
        RailcraftItems.SIGNAL_BLOCK_RELAY_BOX.get(),
        RailcraftItems.SIGNAL_RECEIVER_BOX.get(),
        RailcraftItems.SIGNAL_CONTROLLER_BOX.get(),
        RailcraftItems.BLOCK_SIGNAL.get(),
        RailcraftItems.DISTANT_SIGNAL.get(),
        RailcraftItems.TOKEN_SIGNAL.get(),
        RailcraftItems.DUAL_BLOCK_SIGNAL.get(),
        RailcraftItems.DUAL_DISTANT_SIGNAL.get(),
        RailcraftItems.DUAL_TOKEN_SIGNAL.get());

    addItemsToTab(addAfterIronHoe, entries);
    addItemsToTab(addAfterTNTMinecart, entries);
    addItemsToTab(addAfterActivatorRail, entries);
  }

  public static void addCombat(
      MutableHashedLinkedMap<ItemStack, TabVisibility> entries) {
    entries.putAfter(
        new ItemStack(Items.IRON_SWORD),
        new ItemStack(RailcraftItems.STEEL_SWORD.get()),
        DEFAULT_VISIBILITY);
    entries.putAfter(
        new ItemStack(Items.IRON_AXE),
        new ItemStack(RailcraftItems.STEEL_AXE.get()),
        DEFAULT_VISIBILITY);

    var addAfterIronBoots = List.of(
        Items.IRON_BOOTS,
        RailcraftItems.STEEL_HELMET.get(),
        RailcraftItems.STEEL_CHESTPLATE.get(),
        RailcraftItems.STEEL_LEGGINGS.get(),
        RailcraftItems.STEEL_BOOTS.get());
    addItemsToTab(addAfterIronBoots, entries);
  }

  private static void addItemsToTab(List<Item> list,
      MutableHashedLinkedMap<ItemStack, TabVisibility> entries) {
    for (int i = 1; i < list.size(); i++) {
      entries.putAfter(
          new ItemStack(list.get(i - 1)),
          new ItemStack(list.get(i)),
          DEFAULT_VISIBILITY);
    }
  }
}
