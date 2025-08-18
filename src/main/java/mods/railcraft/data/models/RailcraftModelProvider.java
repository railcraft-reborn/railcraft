package mods.railcraft.data.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;
import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.api.track.ArrowDirection;
import mods.railcraft.client.color.item.LocomotiveColor;
import mods.railcraft.data.RailcraftBlockFamilies;
import mods.railcraft.world.entity.vehicle.locomotive.Locomotive;
import mods.railcraft.world.item.LocomotiveItem;
import mods.railcraft.world.item.RailcraftItems;
import mods.railcraft.world.level.block.AbstractStrengthenedGlassBlock;
import mods.railcraft.world.level.block.ChimneyBlock;
import mods.railcraft.world.level.block.CrusherMultiblockBlock;
import mods.railcraft.world.level.block.DecorativeBlock;
import mods.railcraft.world.level.block.ForceTrackEmitterBlock;
import mods.railcraft.world.level.block.FurnaceMultiblockBlock;
import mods.railcraft.world.level.block.LogBookBlock;
import mods.railcraft.world.level.block.RailcraftBlocks;
import mods.railcraft.world.level.block.SteamOvenBlock;
import mods.railcraft.world.level.block.SteamTurbineBlock;
import mods.railcraft.world.level.block.charge.BatteryBlock;
import mods.railcraft.world.level.block.charge.DisposableBatteryBlock;
import mods.railcraft.world.level.block.charge.EmptyBatteryBlock;
import mods.railcraft.world.level.block.charge.FrameBlock;
import mods.railcraft.world.level.block.detector.DetectorBlock;
import mods.railcraft.world.level.block.entity.track.CouplerTrackBlockEntity;
import mods.railcraft.world.level.block.manipulator.FluidManipulatorBlock;
import mods.railcraft.world.level.block.manipulator.ManipulatorBlock;
import mods.railcraft.world.level.block.post.Column;
import mods.railcraft.world.level.block.post.Connection;
import mods.railcraft.world.level.block.post.PostBlock;
import mods.railcraft.world.level.block.signal.DualSignalBlock;
import mods.railcraft.world.level.block.signal.SignalBoxBlock;
import mods.railcraft.world.level.block.signal.SingleSignalBlock;
import mods.railcraft.world.level.block.steamboiler.FireboxBlock;
import mods.railcraft.world.level.block.steamboiler.SteamBoilerTankBlock;
import mods.railcraft.world.level.block.tank.BaseTankBlock;
import mods.railcraft.world.level.block.tank.TankValveBlock;
import mods.railcraft.world.level.block.track.AbandonedTrackBlock;
import mods.railcraft.world.level.block.track.ElevatorTrackBlock;
import mods.railcraft.world.level.block.track.ForceTrackBlock;
import mods.railcraft.world.level.block.track.TrackBlock;
import mods.railcraft.world.level.block.track.actuator.SwitchTrackActuatorBlock;
import mods.railcraft.world.level.block.track.actuator.SwitchTrackLeverBlock;
import mods.railcraft.world.level.block.track.actuator.SwitchTrackRouterBlock;
import mods.railcraft.world.level.block.track.outfitted.ActivatorTrackBlock;
import mods.railcraft.world.level.block.track.outfitted.BoosterTrackBlock;
import mods.railcraft.world.level.block.track.outfitted.BufferStopTrackBlock;
import mods.railcraft.world.level.block.track.outfitted.ControlTrackBlock;
import mods.railcraft.world.level.block.track.outfitted.CouplerTrackBlock;
import mods.railcraft.world.level.block.track.outfitted.DetectorTrackBlock;
import mods.railcraft.world.level.block.track.outfitted.DisembarkingTrackBlock;
import mods.railcraft.world.level.block.track.outfitted.DumpingTrackBlock;
import mods.railcraft.world.level.block.track.outfitted.EmbarkingTrackBlock;
import mods.railcraft.world.level.block.track.outfitted.GatedTrackBlock;
import mods.railcraft.world.level.block.track.outfitted.JunctionTrackBlock;
import mods.railcraft.world.level.block.track.outfitted.LauncherTrackBlock;
import mods.railcraft.world.level.block.track.outfitted.LockingMode;
import mods.railcraft.world.level.block.track.outfitted.LockingTrackBlock;
import mods.railcraft.world.level.block.track.outfitted.LocomotiveTrackBlock;
import mods.railcraft.world.level.block.track.outfitted.OneWayTrackBlock;
import mods.railcraft.world.level.block.track.outfitted.OutfittedTrackBlock;
import mods.railcraft.world.level.block.track.outfitted.PoweredOutfittedTrackBlock;
import mods.railcraft.world.level.block.track.outfitted.ReversibleOutfittedTrackBlock;
import mods.railcraft.world.level.block.track.outfitted.RoutingTrackBlock;
import mods.railcraft.world.level.block.track.outfitted.SwitchTrackBlock;
import mods.railcraft.world.level.block.track.outfitted.ThrottleTrackBlock;
import mods.railcraft.world.level.block.track.outfitted.TransitionTrackBlock;
import mods.railcraft.world.level.block.track.outfitted.TurnoutTrackBlock;
import mods.railcraft.world.level.block.track.outfitted.WhistleTrackBlock;
import mods.railcraft.world.level.block.track.outfitted.WyeTrackBlock;
import mods.railcraft.world.level.block.worldspike.WorldSpikeBlock;
import net.minecraft.Util;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.blockstates.Condition;
import net.minecraft.client.data.models.blockstates.MultiPartGenerator;
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.client.data.models.blockstates.PropertyDispatch;
import net.minecraft.client.data.models.blockstates.Variant;
import net.minecraft.client.data.models.blockstates.VariantProperties;
import net.minecraft.client.data.models.model.ItemModelUtils;
import net.minecraft.client.data.models.model.ModelLocationUtils;
import net.minecraft.client.data.models.model.ModelTemplate;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.client.data.models.model.TexturedModel;
import net.minecraft.client.renderer.item.BlockModelWrapper;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.BlockFamily;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.AnvilBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.properties.RailShape;

public class RailcraftModelProvider extends ModelProvider {

  static final String CUTOUT = "cutout";

  private static final UnaryOperator<ModelTemplate> CUTOUT_OP =
      t -> t.extend().renderType(CUTOUT).build();

  public RailcraftModelProvider(PackOutput packOutput) {
    super(packOutput, RailcraftConstants.ID);
  }

  @Override
  protected void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
    itemModels.generateFlatItem(RailcraftItems.SIGNAL_LABEL.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.SIGNAL_LAMP.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.TURBINE_ROTOR.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.TURBINE_BLADE.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.TURBINE_DISK.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.IRON_TUNNEL_BORE_HEAD.get(),
        ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.BRONZE_TUNNEL_BORE_HEAD.get(),
        ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.STEEL_TUNNEL_BORE_HEAD.get(),
        ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.DIAMOND_TUNNEL_BORE_HEAD.get(),
        ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.TANK_MINECART.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.ENERGY_MINECART.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.WORLD_SPIKE_MINECART.get(),
        ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.TRACK_LAYER.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.TRACK_RELAYER.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.TRACK_REMOVER.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.TRACK_UNDERCUTTER.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.TUNNEL_BORE.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.SIGNAL_BLOCK_SURVEYOR.get(),
        ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.SIGNAL_TUNER.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.WHISTLE_TUNER.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.GOGGLES.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.OVERALLS.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.STEEL_SHEARS.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.STEEL_BOOTS.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.STEEL_CHESTPLATE.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.STEEL_LEGGINGS.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.STEEL_HELMET.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.CREOSOTE_BUCKET.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.GOLDEN_TICKET.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.TICKET.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.ROUTING_TABLE_BOOK.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.CONTROLLER_CIRCUIT.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.RECEIVER_CIRCUIT.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.SIGNAL_CIRCUIT.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.RADIO_CIRCUIT.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.BAG_OF_CEMENT.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.WOODEN_RAILBED.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.WOODEN_TIE.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.STONE_RAILBED.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.STONE_TIE.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.COAL_COKE.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.BRASS_INGOT.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.BRASS_NUGGET.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.BRONZE_INGOT.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.BRONZE_NUGGET.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.STEEL_INGOT.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.STEEL_NUGGET.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.TIN_INGOT.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.TIN_NUGGET.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.ZINC_INGOT.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.ZINC_NUGGET.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.NICKEL_INGOT.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.NICKEL_NUGGET.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.INVAR_INGOT.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.INVAR_NUGGET.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.LEAD_INGOT.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.LEAD_NUGGET.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.SILVER_INGOT.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.SILVER_NUGGET.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.TIN_RAW.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.ZINC_RAW.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.NICKEL_RAW.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.SILVER_RAW.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.LEAD_RAW.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.STEEL_PLATE.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.IRON_PLATE.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.TIN_PLATE.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.GOLD_PLATE.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.LEAD_PLATE.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.ZINC_PLATE.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.BRASS_PLATE.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.INVAR_PLATE.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.BRONZE_PLATE.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.COPPER_PLATE.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.NICKEL_PLATE.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.SILVER_PLATE.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.BUSHING_GEAR.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.STEEL_GEAR.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.IRON_GEAR.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.TIN_GEAR.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.GOLD_GEAR.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.LEAD_GEAR.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.ZINC_GEAR.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.BRASS_GEAR.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.INVAR_GEAR.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.BRONZE_GEAR.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.COPPER_GEAR.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.NICKEL_GEAR.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.SILVER_GEAR.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.STEEL_ELECTRODE.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.IRON_ELECTRODE.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.TIN_ELECTRODE.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.GOLD_ELECTRODE.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.LEAD_ELECTRODE.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.ZINC_ELECTRODE.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.BRASS_ELECTRODE.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.INVAR_ELECTRODE.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.BRONZE_ELECTRODE.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.CARBON_ELECTRODE.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.COPPER_ELECTRODE.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.NICKEL_ELECTRODE.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.SILVER_ELECTRODE.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.SALTPETER_DUST.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.COAL_DUST.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.CHARCOAL_DUST.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.SLAG.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.ENDER_DUST.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.SULFUR_DUST.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.OBSIDIAN_DUST.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.TRACK_PARTS.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.TRANSITION_TRACK_KIT.get(),
        ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.LOCKING_TRACK_KIT.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.BUFFER_STOP_TRACK_KIT.get(),
        ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.ACTIVATOR_TRACK_KIT.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.BOOSTER_TRACK_KIT.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.CONTROL_TRACK_KIT.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.GATED_TRACK_KIT.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.DETECTOR_TRACK_KIT.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.COUPLER_TRACK_KIT.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.EMBARKING_TRACK_KIT.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.DISEMBARKING_TRACK_KIT.get(),
        ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.DUMPING_TRACK_KIT.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.LAUNCHER_TRACK_KIT.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.ONE_WAY_TRACK_KIT.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.WHISTLE_TRACK_KIT.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.LOCOMOTIVE_TRACK_KIT.get(),
        ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.THROTTLE_TRACK_KIT.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.ROUTING_TRACK_KIT.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.CHARGE_SPOOL_LARGE.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.CHARGE_SPOOL_MEDIUM.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.CHARGE_SPOOL_SMALL.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.CHARGE_COIL.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.CHARGE_TERMINAL.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.CHARGE_MOTOR.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.CHARGE_METER.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.RAW_FIRESTONE.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.REFINED_FIRESTONE.get(), ModelTemplates.FLAT_ITEM);

    itemModels.generateFlatItem(RailcraftItems.STEEL_SWORD.get(),
        ModelTemplates.FLAT_HANDHELD_ITEM);
    itemModels.generateFlatItem(RailcraftItems.STEEL_SHOVEL.get(),
        ModelTemplates.FLAT_HANDHELD_ITEM);
    itemModels.generateFlatItem(RailcraftItems.STEEL_PICKAXE.get(),
        ModelTemplates.FLAT_HANDHELD_ITEM);
    itemModels.generateFlatItem(RailcraftItems.STEEL_AXE.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
    itemModels.generateFlatItem(RailcraftItems.STEEL_HOE.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
    itemModels.generateFlatItem(RailcraftItems.DIAMOND_CROWBAR.get(),
        ModelTemplates.FLAT_HANDHELD_ITEM);
    itemModels.generateFlatItem(RailcraftItems.IRON_CROWBAR.get(),
        ModelTemplates.FLAT_HANDHELD_ITEM);
    itemModels.generateFlatItem(RailcraftItems.STEEL_CROWBAR.get(),
        ModelTemplates.FLAT_HANDHELD_ITEM);
    itemModels.generateFlatItem(RailcraftItems.SEASONS_CROWBAR.get(),
        ModelTemplates.FLAT_HANDHELD_ITEM);
    itemModels.generateFlatItem(RailcraftItems.IRON_SPIKE_MAUL.get(),
        ModelTemplates.FLAT_HANDHELD_ITEM);
    itemModels.generateFlatItem(RailcraftItems.STEEL_SPIKE_MAUL.get(),
        ModelTemplates.FLAT_HANDHELD_ITEM);
    itemModels.generateFlatItem(RailcraftItems.DIAMOND_SPIKE_MAUL.get(),
        ModelTemplates.FLAT_HANDHELD_ITEM);

    itemModels.generateFlatItem(RailcraftItems.WOODEN_RAIL.get(),
        ModelTemplates.FLAT_HANDHELD_ROD_ITEM);
    itemModels.generateFlatItem(RailcraftItems.STANDARD_RAIL.get(),
        ModelTemplates.FLAT_HANDHELD_ROD_ITEM);
    itemModels.generateFlatItem(RailcraftItems.ADVANCED_RAIL.get(),
        ModelTemplates.FLAT_HANDHELD_ROD_ITEM);
    itemModels.generateFlatItem(RailcraftItems.ELECTRIC_RAIL.get(),
        ModelTemplates.FLAT_HANDHELD_ROD_ITEM);
    itemModels.generateFlatItem(RailcraftItems.HIGH_SPEED_RAIL.get(),
        ModelTemplates.FLAT_HANDHELD_ROD_ITEM);
    itemModels.generateFlatItem(RailcraftItems.REINFORCED_RAIL.get(),
        ModelTemplates.FLAT_HANDHELD_ROD_ITEM);
    itemModels.generateFlatItem(RailcraftItems.REBAR.get(), ModelTemplates.FLAT_HANDHELD_ROD_ITEM);

    this.generateFirestone(itemModels, RailcraftItems.CUT_FIRESTONE.get());
    this.generateFirestone(itemModels, RailcraftItems.CRACKED_FIRESTONE.get());

    this.generateLocomotive(itemModels, RailcraftItems.STEAM_LOCOMOTIVE.get());
    this.generateLocomotive(itemModels, RailcraftItems.ELECTRIC_LOCOMOTIVE.get());
    this.generateLocomotive(itemModels, RailcraftItems.CREATIVE_LOCOMOTIVE.get());

    /// BLOCKS
    for (var dyeColor : DyeColor.values()) {
      this.createStrengthenedGlass(blockModels,
          RailcraftBlocks.STRENGTHENED_GLASS.variantFor(dyeColor).get());
      this.createStrengthenedGlass(blockModels,
          RailcraftBlocks.IRON_TANK_GAUGE.variantFor(dyeColor).get());
      this.createStrengthenedGlass(blockModels,
          RailcraftBlocks.STEEL_TANK_GAUGE.variantFor(dyeColor).get());
      this.createTankValve(blockModels, RailcraftBlocks.IRON_TANK_VALVE.variantFor(dyeColor).get(),
          RailcraftBlocks.IRON_TANK_WALL.variantFor(dyeColor).get());
      this.createTankValve(blockModels, RailcraftBlocks.STEEL_TANK_VALVE.variantFor(dyeColor).get(),
          RailcraftBlocks.STEEL_TANK_WALL.variantFor(dyeColor).get());
      this.createCubeColumnBlock(blockModels,
          RailcraftBlocks.IRON_TANK_WALL.variantFor(dyeColor).get());
      this.createCubeColumnBlock(blockModels,
          RailcraftBlocks.STEEL_TANK_WALL.variantFor(dyeColor).get());
      this.createPost(blockModels, RailcraftBlocks.POST.variantFor(dyeColor).get());
    }

    this.createAnvil(blockModels, RailcraftBlocks.STEEL_ANVIL.get());
    this.createAnvil(blockModels, RailcraftBlocks.CHIPPED_STEEL_ANVIL.get());
    this.createAnvil(blockModels, RailcraftBlocks.DAMAGED_STEEL_ANVIL.get());

    this.createSteamOven(blockModels, RailcraftBlocks.STEAM_OVEN.get());
    this.createFrameBlock(blockModels, RailcraftBlocks.FRAME.get());
    this.createLogBookBlock(blockModels, RailcraftBlocks.LOGBOOK.get());
    this.createChimneyBlock(blockModels, RailcraftBlocks.CHIMNEY.get());
    this.createWorldSpikeBlock(blockModels, RailcraftBlocks.WORLD_SPIKE.get());
    this.createWorldSpikeBlock(blockModels, RailcraftBlocks.PERSONAL_WORLD_SPIKE.get());

    this.createFirebox(blockModels, RailcraftBlocks.SOLID_FUELED_FIREBOX.get());
    this.createFirebox(blockModels, RailcraftBlocks.FLUID_FUELED_FIREBOX.get());
    this.createFurnaceMultiblockBricks(blockModels, RailcraftBlocks.COKE_OVEN_BRICKS.get());
    this.createFurnaceMultiblockBricks(blockModels, RailcraftBlocks.BLAST_FURNACE_BRICKS.get());
    this.createCrusherMultiblockBricks(blockModels, RailcraftBlocks.CRUSHER.get());
    this.createSteamBoilerTank(blockModels, RailcraftBlocks.LOW_PRESSURE_STEAM_BOILER_TANK.get());
    this.createSteamBoilerTank(blockModels, RailcraftBlocks.HIGH_PRESSURE_STEAM_BOILER_TANK.get());
    this.createSteamTurbine(blockModels, RailcraftBlocks.STEAM_TURBINE.get());

    this.createElevatorTrack(blockModels, RailcraftBlocks.ELEVATOR_TRACK.get());
    this.createForceTrack(blockModels, RailcraftBlocks.FORCE_TRACK.get());
    this.createForceTrackEmitter(blockModels, RailcraftBlocks.FORCE_TRACK_EMITTER.get());

    this.createRechargeableBattery(blockModels, RailcraftBlocks.NICKEL_ZINC_BATTERY.get());
    this.createRechargeableBattery(blockModels, RailcraftBlocks.NICKEL_IRON_BATTERY.get());
    this.createDisposableBattery(blockModels, RailcraftBlocks.ZINC_CARBON_BATTERY.get(),
        RailcraftBlocks.ZINC_CARBON_BATTERY_EMPTY.get());
    this.createDisposableBattery(blockModels, RailcraftBlocks.ZINC_SILVER_BATTERY.get(),
        RailcraftBlocks.ZINC_SILVER_BATTERY_EMPTY.get());

    this.createCubeTopBottomBlock(blockModels, RailcraftBlocks.MANUAL_ROLLING_MACHINE.get());
    this.createCubeTopBottomBlock(blockModels, RailcraftBlocks.POWERED_ROLLING_MACHINE.get());

    this.createCubeColumnBlock(blockModels, RailcraftBlocks.FEED_STATION.get());
    this.createCubeColumnBlock(blockModels, RailcraftBlocks.WATER_TANK_SIDING.get());

    this.createFluidManipulator(blockModels, RailcraftBlocks.FLUID_LOADER.get());
    this.createFluidManipulator(blockModels, RailcraftBlocks.FLUID_UNLOADER.get());
    this.createManipulator(blockModels, RailcraftBlocks.ITEM_LOADER.get());
    this.createManipulator(blockModels, RailcraftBlocks.ITEM_UNLOADER.get());
    this.createDirectionalManipulator(blockModels, RailcraftBlocks.ADVANCED_ITEM_LOADER.get());
    this.createDirectionalManipulator(blockModels, RailcraftBlocks.ADVANCED_ITEM_UNLOADER.get());
    this.createDirectionalManipulator(blockModels, RailcraftBlocks.CART_DISPENSER.get());
    this.createDirectionalManipulator(blockModels, RailcraftBlocks.TRAIN_DISPENSER.get());

    this.createDirectionalDetector(blockModels, RailcraftBlocks.ADVANCED_DETECTOR.get());
    this.createDirectionalDetector(blockModels, RailcraftBlocks.AGE_DETECTOR.get());
    this.createDirectionalDetector(blockModels, RailcraftBlocks.ANIMAL_DETECTOR.get());
    this.createDirectionalDetector(blockModels, RailcraftBlocks.ANY_DETECTOR.get());
    this.createDirectionalDetector(blockModels, RailcraftBlocks.EMPTY_DETECTOR.get());
    this.createDirectionalDetector(blockModels, RailcraftBlocks.ITEM_DETECTOR.get());
    this.createDirectionalDetector(blockModels, RailcraftBlocks.LOCOMOTIVE_DETECTOR.get());
    this.createDirectionalDetector(blockModels, RailcraftBlocks.MOB_DETECTOR.get());
    this.createDirectionalDetector(blockModels, RailcraftBlocks.PLAYER_DETECTOR.get());
    this.createDirectionalDetector(blockModels, RailcraftBlocks.ROUTING_DETECTOR.get());
    this.createDirectionalDetector(blockModels, RailcraftBlocks.SHEEP_DETECTOR.get());
    this.createDirectionalDetector(blockModels, RailcraftBlocks.TANK_DETECTOR.get());
    this.createDirectionalDetector(blockModels, RailcraftBlocks.TRAIN_DETECTOR.get());
    this.createDirectionalDetector(blockModels, RailcraftBlocks.VILLAGER_DETECTOR.get());

    this.createSignalBoxBlock(blockModels, RailcraftBlocks.ANALOG_SIGNAL_CONTROLLER_BOX.get());
    this.createSignalBoxBlock(blockModels, RailcraftBlocks.SIGNAL_INTERLOCK_BOX.get());
    this.createSignalBoxBlock(blockModels, RailcraftBlocks.SIGNAL_BLOCK_RELAY_BOX.get());
    this.createSignalBoxBlock(blockModels, RailcraftBlocks.SIGNAL_CAPACITOR_BOX.get());
    this.createSignalBoxBlock(blockModels, RailcraftBlocks.TOKEN_SIGNAL_BOX.get());
    this.createSignalBoxBlock(blockModels, RailcraftBlocks.SIGNAL_CONTROLLER_BOX.get());
    this.createSignalBoxBlock(blockModels, RailcraftBlocks.SIGNAL_RECEIVER_BOX.get());
    this.createSignalBoxBlock(blockModels, RailcraftBlocks.SIGNAL_SEQUENCER_BOX.get());

    this.createSingleSignalBlock(blockModels, RailcraftBlocks.BLOCK_SIGNAL.get());
    this.createSingleSignalBlock(blockModels, RailcraftBlocks.DISTANT_SIGNAL.get());
    this.createSingleSignalBlock(blockModels, RailcraftBlocks.TOKEN_SIGNAL.get());

    this.createDualSignalBlock(blockModels, RailcraftBlocks.DUAL_BLOCK_SIGNAL.get());
    this.createDualSignalBlock(blockModels, RailcraftBlocks.DUAL_DISTANT_SIGNAL.get());
    this.createDualSignalBlock(blockModels, RailcraftBlocks.DUAL_TOKEN_SIGNAL.get());

    this.createSwitchTrackLever(blockModels, RailcraftBlocks.SWITCH_TRACK_LEVER.get());
    this.createSwitchTrackMotorOrRouter(blockModels, RailcraftBlocks.SWITCH_TRACK_MOTOR.get());
    this.createSwitchTrackMotorOrRouter(blockModels, RailcraftBlocks.SWITCH_TRACK_ROUTER.get());

    blockModels.createTrivialCube(RailcraftBlocks.STEEL_BLOCK.get());
    blockModels.createTrivialCube(RailcraftBlocks.BRASS_BLOCK.get());
    blockModels.createTrivialCube(RailcraftBlocks.BRONZE_BLOCK.get());
    blockModels.createTrivialCube(RailcraftBlocks.INVAR_BLOCK.get());
    blockModels.createTrivialCube(RailcraftBlocks.LEAD_BLOCK.get());
    blockModels.createTrivialCube(RailcraftBlocks.NICKEL_BLOCK.get());
    blockModels.createTrivialCube(RailcraftBlocks.SILVER_BLOCK.get());
    blockModels.createTrivialCube(RailcraftBlocks.TIN_BLOCK.get());
    blockModels.createTrivialCube(RailcraftBlocks.ZINC_BLOCK.get());
    blockModels.createTrivialCube(RailcraftBlocks.COAL_COKE_BLOCK.get());
    blockModels.createTrivialCube(RailcraftBlocks.CRUSHED_OBSIDIAN.get());

    blockModels.createTrivialCube(RailcraftBlocks.LEAD_ORE.get());
    blockModels.createTrivialCube(RailcraftBlocks.NICKEL_ORE.get());
    blockModels.createTrivialCube(RailcraftBlocks.SILVER_ORE.get());
    blockModels.createTrivialCube(RailcraftBlocks.SULFUR_ORE.get());
    blockModels.createTrivialCube(RailcraftBlocks.TIN_ORE.get());
    blockModels.createTrivialCube(RailcraftBlocks.ZINC_ORE.get());
    blockModels.createTrivialCube(RailcraftBlocks.DEEPSLATE_LEAD_ORE.get());
    blockModels.createTrivialCube(RailcraftBlocks.DEEPSLATE_NICKEL_ORE.get());
    blockModels.createTrivialCube(RailcraftBlocks.DEEPSLATE_SILVER_ORE.get());
    blockModels.createTrivialCube(RailcraftBlocks.DEEPSLATE_SULFUR_ORE.get());
    blockModels.createTrivialCube(RailcraftBlocks.DEEPSLATE_TIN_ORE.get());
    blockModels.createTrivialCube(RailcraftBlocks.DEEPSLATE_ZINC_ORE.get());
    blockModels.createTrivialCube(RailcraftBlocks.SALTPETER_ORE.get());
    blockModels.createTrivialCube(RailcraftBlocks.FIRESTONE_ORE.get());

    RailcraftBlockFamilies.getAllFamilies()
        .filter(BlockFamily::shouldGenerateModel)
        .forEach(family -> blockModels.family(family.getBaseBlock()).generateFor(family));
    for (var type : DecorativeBlock.values()) {
      blockModels.createTrivialCube(RailcraftBlocks.DECORATIVE_STONE.variantFor(type).get());
      blockModels.createTrivialCube(RailcraftBlocks.DECORATIVE_COBBLESTONE.variantFor(type).get());
      blockModels.createTrivialCube(
          RailcraftBlocks.POLISHED_DECORATIVE_STONE.variantFor(type).get());
      blockModels.createTrivialCube(
          RailcraftBlocks.CHISELED_DECORATIVE_STONE.variantFor(type).get());
      blockModels.createTrivialCube(RailcraftBlocks.ETCHED_DECORATIVE_STONE.variantFor(type).get());
    }

    this.createAbandonedTracks(blockModels,
        RailcraftBlocks.ABANDONED_TRACK.get(),
        RailcraftBlocks.ABANDONED_LOCKING_TRACK.get(),
        RailcraftBlocks.ABANDONED_BUFFER_STOP_TRACK.get(),
        RailcraftBlocks.ABANDONED_ACTIVATOR_TRACK.get(),
        RailcraftBlocks.ABANDONED_BOOSTER_TRACK.get(),
        RailcraftBlocks.ABANDONED_CONTROL_TRACK.get(),
        RailcraftBlocks.ABANDONED_GATED_TRACK.get(),
        RailcraftBlocks.ABANDONED_DETECTOR_TRACK.get(),
        RailcraftBlocks.ABANDONED_COUPLER_TRACK.get(),
        RailcraftBlocks.ABANDONED_EMBARKING_TRACK.get(),
        RailcraftBlocks.ABANDONED_DISEMBARKING_TRACK.get(),
        RailcraftBlocks.ABANDONED_DUMPING_TRACK.get(),
        RailcraftBlocks.ABANDONED_TURNOUT_TRACK.get(),
        RailcraftBlocks.ABANDONED_WYE_TRACK.get(),
        RailcraftBlocks.ABANDONED_JUNCTION_TRACK.get(),
        RailcraftBlocks.ABANDONED_LAUNCHER_TRACK.get(),
        RailcraftBlocks.ABANDONED_ONE_WAY_TRACK.get(),
        RailcraftBlocks.ABANDONED_WHISTLE_TRACK.get(),
        RailcraftBlocks.ABANDONED_LOCOMOTIVE_TRACK.get(),
        RailcraftBlocks.ABANDONED_THROTTLE_TRACK.get(),
        RailcraftBlocks.ABANDONED_ROUTING_TRACK.get());

    this.createOutfittedTracks(blockModels,
        Blocks.RAIL,
        RailcraftBlocks.IRON_LOCKING_TRACK.get(),
        RailcraftBlocks.IRON_BUFFER_STOP_TRACK.get(),
        RailcraftBlocks.IRON_ACTIVATOR_TRACK.get(),
        RailcraftBlocks.IRON_BOOSTER_TRACK.get(),
        RailcraftBlocks.IRON_CONTROL_TRACK.get(),
        RailcraftBlocks.IRON_GATED_TRACK.get(),
        RailcraftBlocks.IRON_DETECTOR_TRACK.get(),
        RailcraftBlocks.IRON_COUPLER_TRACK.get(),
        RailcraftBlocks.IRON_EMBARKING_TRACK.get(),
        RailcraftBlocks.IRON_DISEMBARKING_TRACK.get(),
        RailcraftBlocks.IRON_DUMPING_TRACK.get(),
        RailcraftBlocks.IRON_TURNOUT_TRACK.get(),
        RailcraftBlocks.IRON_WYE_TRACK.get(),
        RailcraftBlocks.IRON_JUNCTION_TRACK.get(),
        RailcraftBlocks.IRON_LAUNCHER_TRACK.get(),
        RailcraftBlocks.IRON_ONE_WAY_TRACK.get(),
        RailcraftBlocks.IRON_WHISTLE_TRACK.get(),
        RailcraftBlocks.IRON_LOCOMOTIVE_TRACK.get(),
        RailcraftBlocks.IRON_THROTTLE_TRACK.get(),
        RailcraftBlocks.IRON_ROUTING_TRACK.get());

    this.createTracks(blockModels,
        RailcraftBlocks.STRAP_IRON_TRACK.get(),
        RailcraftBlocks.STRAP_IRON_LOCKING_TRACK.get(),
        RailcraftBlocks.STRAP_IRON_BUFFER_STOP_TRACK.get(),
        RailcraftBlocks.STRAP_IRON_ACTIVATOR_TRACK.get(),
        RailcraftBlocks.STRAP_IRON_BOOSTER_TRACK.get(),
        RailcraftBlocks.STRAP_IRON_CONTROL_TRACK.get(),
        RailcraftBlocks.STRAP_IRON_GATED_TRACK.get(),
        RailcraftBlocks.STRAP_IRON_DETECTOR_TRACK.get(),
        RailcraftBlocks.STRAP_IRON_COUPLER_TRACK.get(),
        RailcraftBlocks.STRAP_IRON_EMBARKING_TRACK.get(),
        RailcraftBlocks.STRAP_IRON_DISEMBARKING_TRACK.get(),
        RailcraftBlocks.STRAP_IRON_DUMPING_TRACK.get(),
        RailcraftBlocks.STRAP_IRON_TURNOUT_TRACK.get(),
        RailcraftBlocks.STRAP_IRON_WYE_TRACK.get(),
        RailcraftBlocks.STRAP_IRON_JUNCTION_TRACK.get(),
        RailcraftBlocks.STRAP_IRON_LAUNCHER_TRACK.get(),
        RailcraftBlocks.STRAP_IRON_ONE_WAY_TRACK.get(),
        RailcraftBlocks.STRAP_IRON_WHISTLE_TRACK.get(),
        RailcraftBlocks.STRAP_IRON_LOCOMOTIVE_TRACK.get(),
        RailcraftBlocks.STRAP_IRON_THROTTLE_TRACK.get(),
        RailcraftBlocks.STRAP_IRON_ROUTING_TRACK.get());

    this.createTracks(blockModels,
        RailcraftBlocks.REINFORCED_TRACK.get(),
        RailcraftBlocks.REINFORCED_LOCKING_TRACK.get(),
        RailcraftBlocks.REINFORCED_BUFFER_STOP_TRACK.get(),
        RailcraftBlocks.REINFORCED_ACTIVATOR_TRACK.get(),
        RailcraftBlocks.REINFORCED_BOOSTER_TRACK.get(),
        RailcraftBlocks.REINFORCED_CONTROL_TRACK.get(),
        RailcraftBlocks.REINFORCED_GATED_TRACK.get(),
        RailcraftBlocks.REINFORCED_DETECTOR_TRACK.get(),
        RailcraftBlocks.REINFORCED_COUPLER_TRACK.get(),
        RailcraftBlocks.REINFORCED_EMBARKING_TRACK.get(),
        RailcraftBlocks.REINFORCED_DISEMBARKING_TRACK.get(),
        RailcraftBlocks.REINFORCED_DUMPING_TRACK.get(),
        RailcraftBlocks.REINFORCED_TURNOUT_TRACK.get(),
        RailcraftBlocks.REINFORCED_WYE_TRACK.get(),
        RailcraftBlocks.REINFORCED_JUNCTION_TRACK.get(),
        RailcraftBlocks.REINFORCED_LAUNCHER_TRACK.get(),
        RailcraftBlocks.REINFORCED_ONE_WAY_TRACK.get(),
        RailcraftBlocks.REINFORCED_WHISTLE_TRACK.get(),
        RailcraftBlocks.REINFORCED_LOCOMOTIVE_TRACK.get(),
        RailcraftBlocks.REINFORCED_THROTTLE_TRACK.get(),
        RailcraftBlocks.REINFORCED_ROUTING_TRACK.get());

    this.createTracks(blockModels,
        RailcraftBlocks.ELECTRIC_TRACK.get(),
        RailcraftBlocks.ELECTRIC_LOCKING_TRACK.get(),
        RailcraftBlocks.ELECTRIC_BUFFER_STOP_TRACK.get(),
        RailcraftBlocks.ELECTRIC_ACTIVATOR_TRACK.get(),
        RailcraftBlocks.ELECTRIC_BOOSTER_TRACK.get(),
        RailcraftBlocks.ELECTRIC_CONTROL_TRACK.get(),
        RailcraftBlocks.ELECTRIC_GATED_TRACK.get(),
        RailcraftBlocks.ELECTRIC_DETECTOR_TRACK.get(),
        RailcraftBlocks.ELECTRIC_COUPLER_TRACK.get(),
        RailcraftBlocks.ELECTRIC_EMBARKING_TRACK.get(),
        RailcraftBlocks.ELECTRIC_DISEMBARKING_TRACK.get(),
        RailcraftBlocks.ELECTRIC_DUMPING_TRACK.get(),
        RailcraftBlocks.ELECTRIC_TURNOUT_TRACK.get(),
        RailcraftBlocks.ELECTRIC_WYE_TRACK.get(),
        RailcraftBlocks.ELECTRIC_JUNCTION_TRACK.get(),
        RailcraftBlocks.ELECTRIC_LAUNCHER_TRACK.get(),
        RailcraftBlocks.ELECTRIC_ONE_WAY_TRACK.get(),
        RailcraftBlocks.ELECTRIC_WHISTLE_TRACK.get(),
        RailcraftBlocks.ELECTRIC_LOCOMOTIVE_TRACK.get(),
        RailcraftBlocks.ELECTRIC_THROTTLE_TRACK.get(),
        RailcraftBlocks.ELECTRIC_ROUTING_TRACK.get());

    this.createHighSpeedTracks(blockModels,
        RailcraftBlocks.HIGH_SPEED_TRACK.get(),
        RailcraftBlocks.HIGH_SPEED_TRANSITION_TRACK.get(),
        RailcraftBlocks.HIGH_SPEED_LOCKING_TRACK.get(),
        RailcraftBlocks.HIGH_SPEED_ACTIVATOR_TRACK.get(),
        RailcraftBlocks.HIGH_SPEED_BOOSTER_TRACK.get(),
        RailcraftBlocks.HIGH_SPEED_DETECTOR_TRACK.get(),
        RailcraftBlocks.HIGH_SPEED_TURNOUT_TRACK.get(),
        RailcraftBlocks.HIGH_SPEED_WYE_TRACK.get(),
        RailcraftBlocks.HIGH_SPEED_JUNCTION_TRACK.get(),
        RailcraftBlocks.HIGH_SPEED_WHISTLE_TRACK.get(),
        RailcraftBlocks.HIGH_SPEED_LOCOMOTIVE_TRACK.get(),
        RailcraftBlocks.HIGH_SPEED_THROTTLE_TRACK.get());

    this.createHighSpeedTracks(blockModels,
        RailcraftBlocks.HIGH_SPEED_ELECTRIC_TRACK.get(),
        RailcraftBlocks.HIGH_SPEED_ELECTRIC_TRANSITION_TRACK.get(),
        RailcraftBlocks.HIGH_SPEED_ELECTRIC_LOCKING_TRACK.get(),
        RailcraftBlocks.HIGH_SPEED_ELECTRIC_ACTIVATOR_TRACK.get(),
        RailcraftBlocks.HIGH_SPEED_ELECTRIC_BOOSTER_TRACK.get(),
        RailcraftBlocks.HIGH_SPEED_ELECTRIC_DETECTOR_TRACK.get(),
        RailcraftBlocks.HIGH_SPEED_ELECTRIC_TURNOUT_TRACK.get(),
        RailcraftBlocks.HIGH_SPEED_ELECTRIC_WYE_TRACK.get(),
        RailcraftBlocks.HIGH_SPEED_ELECTRIC_JUNCTION_TRACK.get(),
        RailcraftBlocks.HIGH_SPEED_ELECTRIC_WHISTLE_TRACK.get(),
        RailcraftBlocks.HIGH_SPEED_ELECTRIC_LOCOMOTIVE_TRACK.get(),
        RailcraftBlocks.HIGH_SPEED_ELECTRIC_THROTTLE_TRACK.get());
  }

  private void generateLocomotive(ItemModelGenerators itemModels, LocomotiveItem item) {
    var path = BuiltInRegistries.ITEM.getKey(item).getPath()
        .replace("creative", "electric");
    var rl = itemModels.generateLayeredItem(item,
        modLocation("item/%s_layer0".formatted(path)),
        modLocation("item/%s_layer1".formatted(path)));

    itemModels.itemModelOutput.accept(item,
        new BlockModelWrapper.Unbaked(rl,
            List.of(
                new LocomotiveColor(0),
                new LocomotiveColor(1)
            )
        )
    );
  }

  private void generateFirestone(ItemModelGenerators itemModels, Item item) {
    var rl = itemModels.generateLayeredItem(item,
        modLocation("item/firestone"), TextureMapping.getItemTexture(item));
    itemModels.itemModelOutput.accept(item, ItemModelUtils.plainModel(rl));
  }

  /// BLOCKS
  private void createStrengthenedGlass(BlockModelGenerators blockModels, Block block) {
    var singleModel = TexturedModel.CUBE
        .updateTemplate(CUTOUT_OP)
        .updateTexture(tm ->
            tm.put(TextureSlot.ALL, TextureMapping.getBlockTexture(block, "_top")))
        .createWithSuffix(block, "_single", blockModels.modelOutput);
    var topModel = TexturedModel.COLUMN
        .updateTemplate(CUTOUT_OP)
        .updateTexture(tm ->
            tm.put(TextureSlot.SIDE, TextureMapping.getBlockTexture(block, "_side_top")))
        .createWithSuffix(block, "_top", blockModels.modelOutput);
    var centerModel = TexturedModel.COLUMN
        .updateTemplate(CUTOUT_OP)
        .updateTexture(tm ->
            tm.put(TextureSlot.SIDE, TextureMapping.getBlockTexture(block, "_side_center")))
        .createWithSuffix(block, "_center", blockModels.modelOutput);
    var bottomModel = TexturedModel.COLUMN
        .updateTemplate(CUTOUT_OP)
        .updateTexture(tm ->
            tm.put(TextureSlot.SIDE, TextureMapping.getBlockTexture(block, "_side_bottom")))
        .createWithSuffix(block, "_bottom", blockModels.modelOutput);

    blockModels.blockStateOutput.accept(
        MultiVariantGenerator.multiVariant(block)
            .with(
                PropertyDispatch.property(AbstractStrengthenedGlassBlock.TYPE)
                    .select(AbstractStrengthenedGlassBlock.Type.SINGLE,
                        Variant.variant().with(VariantProperties.MODEL, singleModel))
                    .select(
                        AbstractStrengthenedGlassBlock.Type.TOP,
                        Variant.variant().with(VariantProperties.MODEL, topModel))
                    .select(
                        AbstractStrengthenedGlassBlock.Type.CENTER,
                        Variant.variant().with(VariantProperties.MODEL, centerModel))
                    .select(
                        AbstractStrengthenedGlassBlock.Type.BOTTOM,
                        Variant.variant().with(VariantProperties.MODEL, bottomModel))
            )
    );
    blockModels.registerSimpleItemModel(block, singleModel);
  }

  private void createTankValve(BlockModelGenerators blockModels, TankValveBlock block,
      BaseTankBlock wallBlock) {
    var tmVertical = new TextureMapping()
        .put(TextureSlot.DOWN, TextureMapping.getBlockTexture(block, "_top"))
        .put(TextureSlot.UP, TextureMapping.getBlockTexture(block, "_top"))
        .put(TextureSlot.NORTH, TextureMapping.getBlockTexture(wallBlock, "_side"))
        .put(TextureSlot.SOUTH, TextureMapping.getBlockTexture(wallBlock, "_side"))
        .put(TextureSlot.EAST, TextureMapping.getBlockTexture(wallBlock, "_side"))
        .put(TextureSlot.WEST, TextureMapping.getBlockTexture(wallBlock, "_side"))
        .put(TextureSlot.PARTICLE, TextureMapping.getBlockTexture(wallBlock, "_top"));

    var verticalModel = ModelTemplates.CUBE.create(block, tmVertical, blockModels.modelOutput);

    var tmHorizontal = new TextureMapping()
        .put(TextureSlot.DOWN, TextureMapping.getBlockTexture(wallBlock, "_top"))
        .put(TextureSlot.UP, TextureMapping.getBlockTexture(wallBlock, "_top"))
        .put(TextureSlot.NORTH, TextureMapping.getBlockTexture(block, "_front"))
        .put(TextureSlot.SOUTH, TextureMapping.getBlockTexture(block, "_front"))
        .put(TextureSlot.EAST, TextureMapping.getBlockTexture(wallBlock, "_side"))
        .put(TextureSlot.WEST, TextureMapping.getBlockTexture(wallBlock, "_side"))
        .put(TextureSlot.PARTICLE, TextureMapping.getBlockTexture(wallBlock, "_top"));

    var horizontalModel = ModelTemplates.CUBE
        .createWithSuffix(block, "_horizontal", tmHorizontal, blockModels.modelOutput);

    blockModels.blockStateOutput.accept(
        MultiVariantGenerator.multiVariant(block)
            .with(
                PropertyDispatch.property(BlockStateProperties.AXIS)
                    .select(Direction.Axis.Y,
                        Variant.variant().with(VariantProperties.MODEL, verticalModel))
                    .select(Direction.Axis.Z,
                        Variant.variant().with(VariantProperties.MODEL, horizontalModel))
                    .select(Direction.Axis.X,
                        Variant.variant().with(VariantProperties.MODEL, horizontalModel)
                            .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
            )
    );
    blockModels.registerSimpleItemModel(block, verticalModel);
  }

  private void createCubeColumnBlock(BlockModelGenerators blockModels, Block block) {
    var tm = new TextureMapping()
        .put(TextureSlot.SIDE, TextureMapping.getBlockTexture(block, "_side"))
        .put(TextureSlot.END, TextureMapping.getBlockTexture(block, "_top"));

    var model = ModelTemplates.CUBE_COLUMN.create(block, tm, blockModels.modelOutput);

    blockModels.blockStateOutput.accept(BlockModelGenerators.createSimpleBlock(block, model));
    blockModels.registerSimpleItemModel(block, model);
  }

  private void createPost(BlockModelGenerators blockModels, PostBlock block) {
    var texture = TextureMapping.defaultTexture(block);
    var postFullColumnTemplate = modLocation("block/template_post_full_column");
    var postDoubleConnectionTemplate = modLocation("block/template_post_double_connection");
    var postTopColumnTemplate = modLocation("block/template_post_top_column");
    var postSmallColumnTemplate = modLocation("block/template_post_small_column");
    var postPlatformTemplate = modLocation("block/template_post_platform");
    var postSingleConnectionTemplate = modLocation("block/template_post_single_connection");
    var postInventoryTemplate = modLocation("block/post_inventory");

    var fullColumnModel = ModelTemplates.SINGLE_FACE
        .extend().parent(postFullColumnTemplate).suffix("_full_column").build()
        .create(block, texture, blockModels.modelOutput);
    var doubleConnectionModel = ModelTemplates.SINGLE_FACE
        .extend().parent(postDoubleConnectionTemplate).suffix("_double_connection").build()
        .create(block, texture, blockModels.modelOutput);
    var topColumnModel = ModelTemplates.SINGLE_FACE
        .extend().parent(postTopColumnTemplate).suffix("_top_column").build()
        .create(block, texture, blockModels.modelOutput);
    var smallColumnModel = ModelTemplates.SINGLE_FACE
        .extend().parent(postSmallColumnTemplate).suffix("_small_column").build()
        .create(block, texture, blockModels.modelOutput);
    var platformModel = ModelTemplates.SINGLE_FACE
        .extend().parent(postPlatformTemplate).suffix("_platform").build()
        .create(block, texture, blockModels.modelOutput);
    var singleConnectionModel = ModelTemplates.SINGLE_FACE
        .extend().parent(postSingleConnectionTemplate).suffix("_single_connection").build()
        .create(block, texture, blockModels.modelOutput);
    var inventoryModel = ModelTemplates.SINGLE_FACE
        .extend().parent(postInventoryTemplate).suffix("_inventory").build()
        .create(block, texture, blockModels.modelOutput);

    blockModels.blockStateOutput.accept(
        MultiPartGenerator.multiPart(block)
            .with(
                Condition.condition().term(PostBlock.COLUMN, Column.PLATFORM),
                Variant.variant().with(VariantProperties.MODEL, platformModel))
            .with(
                Condition.condition().term(PostBlock.COLUMN, Column.TOP),
                Variant.variant().with(VariantProperties.MODEL, topColumnModel))
            .with(
                Condition.condition().term(PostBlock.COLUMN, Column.SMALL),
                Variant.variant().with(VariantProperties.MODEL, smallColumnModel))
            .with(
                Condition.condition().term(PostBlock.COLUMN, Column.FULL),
                Variant.variant().with(VariantProperties.MODEL, fullColumnModel))
            .with(
                Condition.condition().term(PostBlock.NORTH, Connection.SINGLE),
                Variant.variant().with(VariantProperties.MODEL, singleConnectionModel)
                    .with(VariantProperties.UV_LOCK, true))
            .with(
                Condition.condition().term(PostBlock.NORTH, Connection.DOUBLE),
                Variant.variant().with(VariantProperties.MODEL, doubleConnectionModel)
                    .with(VariantProperties.UV_LOCK, true))
            .with(
                Condition.condition().term(PostBlock.SOUTH, Connection.SINGLE),
                Variant.variant().with(VariantProperties.MODEL, singleConnectionModel)
                    .with(VariantProperties.UV_LOCK, true)
                    .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180))
            .with(
                Condition.condition().term(PostBlock.SOUTH, Connection.DOUBLE),
                Variant.variant().with(VariantProperties.MODEL, doubleConnectionModel)
                    .with(VariantProperties.UV_LOCK, true)
                    .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180))
            .with(
                Condition.condition().term(PostBlock.EAST, Connection.SINGLE),
                Variant.variant().with(VariantProperties.MODEL, singleConnectionModel)
                    .with(VariantProperties.UV_LOCK, true)
                    .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
            .with(
                Condition.condition().term(PostBlock.EAST, Connection.DOUBLE),
                Variant.variant().with(VariantProperties.MODEL, doubleConnectionModel)
                    .with(VariantProperties.UV_LOCK, true)
                    .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
            .with(
                Condition.condition().term(PostBlock.WEST, Connection.SINGLE),
                Variant.variant().with(VariantProperties.MODEL, singleConnectionModel)
                    .with(VariantProperties.UV_LOCK, true)
                    .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270))
            .with(
                Condition.condition().term(PostBlock.WEST, Connection.DOUBLE),
                Variant.variant().with(VariantProperties.MODEL, doubleConnectionModel)
                    .with(VariantProperties.UV_LOCK, true)
                    .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270))
    );
    blockModels.registerSimpleItemModel(block, inventoryModel);
  }

  private void createCubeTopBottomBlock(BlockModelGenerators blockModels, Block block) {
    var sideTexture = TextureMapping.getBlockTexture(block, "_side");
    var bottomTexture = TextureMapping.getBlockTexture(block, "_bottom");
    var topTexture = TextureMapping.getBlockTexture(block, "_top");

    var model = TexturedModel.CUBE_TOP_BOTTOM
        .updateTexture(tm -> tm
            .put(TextureSlot.SIDE, sideTexture)
            .put(TextureSlot.BOTTOM, bottomTexture)
            .put(TextureSlot.TOP, topTexture))
        .create(block, blockModels.modelOutput);

    blockModels.blockStateOutput.accept(BlockModelGenerators.createSimpleBlock(block, model));
    blockModels.registerSimpleItemModel(block, model);
  }

  private void createDirectionalDetector(BlockModelGenerators blockModels, DetectorBlock block) {
    var sideTexture = TextureMapping.getBlockTexture(block, "_side");
    var frontTexture = TextureMapping.getBlockTexture(block, "_front");
    var frontPoweredTexture = TextureMapping.getBlockTexture(block, "_front_powered");

    var tm = new TextureMapping()
        .put(TextureSlot.SIDE, sideTexture)
        .put(TextureSlot.FRONT, frontTexture)
        .put(TextureSlot.TOP, sideTexture);

    var model = ModelTemplates.CUBE_ORIENTABLE.create(block, tm, blockModels.modelOutput);
    var poweredModel = ModelTemplates.CUBE_ORIENTABLE
        .createWithSuffix(block, "_powered", tm.put(TextureSlot.FRONT, frontPoweredTexture),
            blockModels.modelOutput);

    blockModels.blockStateOutput.accept(
        MultiVariantGenerator.multiVariant(block)
            .with(BlockModelGenerators.createBooleanModelDispatch(BlockStateProperties.POWERED,
                poweredModel, model))
            .with(BlockModelGenerators.createFacingDispatch()));
    blockModels.registerSimpleItemModel(block, model);
  }

  private void createSignalBoxBlock(BlockModelGenerators blockModels, SignalBoxBlock signalBlock) {
    var model = RailcraftModelTemplates.SIGNAL_BOX_TEMPLATE_PROVIDER
        .create(signalBlock, blockModels.modelOutput);

    var signalBoxCapModel = modLocation("block/signal_box_cap");
    var signalBoxConnectorModel = modLocation("block/signal_box_connector");

    blockModels.blockStateOutput.accept(
        MultiPartGenerator.multiPart(signalBlock)
            .with(
                Condition.condition().term(SignalBoxBlock.CAP, true),
                Variant.variant().with(VariantProperties.MODEL, signalBoxCapModel))
            .with(
                Condition.condition().term(SignalBoxBlock.NORTH, true),
                Variant.variant().with(VariantProperties.MODEL, signalBoxConnectorModel))
            .with(
                Condition.condition().term(SignalBoxBlock.EAST, true),
                Variant.variant().with(VariantProperties.MODEL, signalBoxConnectorModel)
                    .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
            .with(
                Condition.condition().term(SignalBoxBlock.SOUTH, true),
                Variant.variant().with(VariantProperties.MODEL, signalBoxConnectorModel)
                    .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180))
            .with(
                Condition.condition().term(SignalBoxBlock.WEST, true),
                Variant.variant().with(VariantProperties.MODEL, signalBoxConnectorModel)
                    .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270))
    );
    blockModels.registerSimpleItemModel(signalBlock, model);
  }

  private void createSingleSignalBlock(BlockModelGenerators blockModels,
      SingleSignalBlock signalBlock) {
    var signalPostModel = modLocation("block/signal_post");
    var signalModel = modLocation("block/signal");
    var signalConnectorModel = modLocation("block/signal_connector");

    var signalInventory = modLocation("block/signal_inventory");

    blockModels.blockStateOutput.accept(
        MultiPartGenerator.multiPart(signalBlock)
            .with(
                Condition.condition().term(SingleSignalBlock.DOWN, true),
                Variant.variant().with(VariantProperties.MODEL, signalPostModel))
            .with(
                Condition.condition().term(SingleSignalBlock.FACING, Direction.NORTH),
                Variant.variant().with(VariantProperties.MODEL, signalModel))
            .with(
                Condition.condition().term(SingleSignalBlock.FACING, Direction.EAST),
                Variant.variant().with(VariantProperties.MODEL, signalModel)
                    .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
            .with(
                Condition.condition().term(SingleSignalBlock.FACING, Direction.SOUTH),
                Variant.variant().with(VariantProperties.MODEL, signalModel)
                    .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180))
            .with(
                Condition.condition().term(SingleSignalBlock.FACING, Direction.WEST),
                Variant.variant().with(VariantProperties.MODEL, signalModel)
                    .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270))
            .with(
                Condition.condition().term(SingleSignalBlock.NORTH, true),
                Variant.variant().with(VariantProperties.MODEL, signalConnectorModel)
                    .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270))
            .with(
                Condition.condition().term(SingleSignalBlock.EAST, true),
                Variant.variant().with(VariantProperties.MODEL, signalConnectorModel))
            .with(
                Condition.condition().term(SingleSignalBlock.SOUTH, true),
                Variant.variant().with(VariantProperties.MODEL, signalConnectorModel)
                    .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
            .with(
                Condition.condition().term(SingleSignalBlock.WEST, true),
                Variant.variant().with(VariantProperties.MODEL, signalConnectorModel)
                    .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180))
    );
    blockModels.registerSimpleItemModel(signalBlock, signalInventory);
  }

  private void createDualSignalBlock(BlockModelGenerators blockModels,
      DualSignalBlock signalBlock) {
    var model = RailcraftModelTemplates.DUAL_SIGNAL_INVENTORY_TEMPLATE_PROVIDER
        .create(signalBlock, blockModels.modelOutput);

    blockModels.registerSimpleItemModel(signalBlock, model);

    var signalModel = modLocation("block/dual_signal");
    var signalConnectorModel = modLocation("block/signal_connector");

    blockModels.blockStateOutput.accept(
        MultiPartGenerator.multiPart(signalBlock)
            .with(
                Condition.condition().term(DualSignalBlock.FACING, Direction.NORTH),
                Variant.variant().with(VariantProperties.MODEL, signalModel))
            .with(
                Condition.condition().term(DualSignalBlock.FACING, Direction.EAST),
                Variant.variant().with(VariantProperties.MODEL, signalModel)
                    .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
            .with(
                Condition.condition().term(DualSignalBlock.FACING, Direction.SOUTH),
                Variant.variant().with(VariantProperties.MODEL, signalModel)
                    .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180))
            .with(
                Condition.condition().term(DualSignalBlock.FACING, Direction.WEST),
                Variant.variant().with(VariantProperties.MODEL, signalModel)
                    .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270))
            .with(
                Condition.condition().term(DualSignalBlock.NORTH, true),
                Variant.variant().with(VariantProperties.MODEL, signalConnectorModel)
                    .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270))
            .with(
                Condition.condition().term(DualSignalBlock.EAST, true),
                Variant.variant().with(VariantProperties.MODEL, signalConnectorModel))
            .with(
                Condition.condition().term(DualSignalBlock.SOUTH, true),
                Variant.variant().with(VariantProperties.MODEL, signalConnectorModel)
                    .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
            .with(
                Condition.condition().term(DualSignalBlock.WEST, true),
                Variant.variant().with(VariantProperties.MODEL, signalConnectorModel)
                    .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180))
    );
  }

  private MultiPartGenerator createSwitchTrackActuatorBlockCommon(SwitchTrackActuatorBlock block) {
    var flagRedModel = modLocation("block/switch_track_actuator_flag_red");
    var flagNeutralRedModel = modLocation("block/switch_track_actuator_flag_neutral_red");
    var flagWhiteModel = modLocation("block/switch_track_actuator_flag_white");
    var flagNeutralWhiteModel = modLocation("block/switch_track_actuator_flag_neutral_white");

    var builder = MultiPartGenerator.multiPart(block);
    var arrowDirectionMap =
        Util.make(new EnumMap<ArrowDirection, VariantProperties.Rotation>(ArrowDirection.class),
            x -> {
              x.put(ArrowDirection.NORTH, VariantProperties.Rotation.R0);
              x.put(ArrowDirection.EAST, VariantProperties.Rotation.R90);
              x.put(ArrowDirection.SOUTH, VariantProperties.Rotation.R180);
              x.put(ArrowDirection.WEST, VariantProperties.Rotation.R270);
              x.put(ArrowDirection.NORTH_SOUTH, VariantProperties.Rotation.R0);
              x.put(ArrowDirection.EAST_WEST, VariantProperties.Rotation.R90);
            });

    for (var arrow : ArrowDirection.values()) {
      ResourceLocation modelRed, modelWhite;
      if (arrow == ArrowDirection.NORTH_SOUTH || arrow == ArrowDirection.EAST_WEST) {
        modelRed = flagNeutralRedModel;
        modelWhite = flagNeutralWhiteModel;
      } else {
        modelRed = flagRedModel;
        modelWhite = flagWhiteModel;
      }
      builder
          .with(
              Condition.condition().term(SwitchTrackLeverBlock.RED_ARROW_DIRECTION, arrow),
              Variant.variant()
                  .with(VariantProperties.MODEL, modelRed)
                  .with(VariantProperties.Y_ROT, arrowDirectionMap.get(arrow)))
          .with(
              Condition.condition().term(SwitchTrackLeverBlock.WHITE_ARROW_DIRECTION, arrow),
              Variant.variant()
                  .with(VariantProperties.MODEL, modelWhite)
                  .with(VariantProperties.Y_ROT, arrowDirectionMap.get(arrow)));
    }
    return builder;
  }

  private void createSwitchTrackLever(BlockModelGenerators blockModels,
      SwitchTrackLeverBlock block) {
    var switchTrackActuatorModel = modLocation("block/switch_track_actuator");
    var switchTrackActuatorLeverOnModel = modLocation("block/switch_track_actuator_lever_on");
    var switchTrackActuatorLeverOffModel = modLocation("block/switch_track_actuator_lever_off");

    var switchTrackInventory = modLocation("block/switch_track_lever_inventory");
    blockModels.registerSimpleItemModel(block, switchTrackInventory);

    var directionMap = Util.make(
        new EnumMap<Direction, VariantProperties.Rotation>(Direction.class), x -> {
          x.put(Direction.NORTH, VariantProperties.Rotation.R0);
          x.put(Direction.EAST, VariantProperties.Rotation.R90);
          x.put(Direction.SOUTH, VariantProperties.Rotation.R180);
          x.put(Direction.WEST, VariantProperties.Rotation.R270);
        });

    var builder = this.createSwitchTrackActuatorBlockCommon(block);
    for (var direction : Direction.Plane.HORIZONTAL) {
      var rot = directionMap.get(direction);

      builder
          .with(
              Condition.condition().term(SwitchTrackLeverBlock.FACING, direction),
              Variant.variant()
                  .with(VariantProperties.MODEL, switchTrackActuatorModel)
                  .with(VariantProperties.Y_ROT, rot))
          .with(
              Condition.and(
                  Condition.condition().term(SwitchTrackLeverBlock.FACING, direction),
                  Condition.condition().term(SwitchTrackLeverBlock.SWITCHED, true)),
              Variant.variant()
                  .with(VariantProperties.MODEL, switchTrackActuatorLeverOnModel)
                  .with(VariantProperties.Y_ROT, rot))
          .with(
              Condition.and(
                  Condition.condition().term(SwitchTrackLeverBlock.FACING, direction),
                  Condition.condition().term(SwitchTrackLeverBlock.SWITCHED, false)),
              Variant.variant()
                  .with(VariantProperties.MODEL, switchTrackActuatorLeverOffModel)
                  .with(VariantProperties.Y_ROT, rot));
    }
    blockModels.blockStateOutput.accept(builder);
  }

  private void createSwitchTrackMotorOrRouter(BlockModelGenerators blockModels,
      SwitchTrackActuatorBlock block) {
    var isRouter = block instanceof SwitchTrackRouterBlock;
    var switchTrackActuator = modLocation("block/switch_track_actuator");
    var switchTrackActuatorRouter = modLocation("block/switch_track_actuator_router");

    var switchTrackInventory = modLocation(
        "block/switch_track_" + (isRouter ? "router" : "motor") + "_inventory");
    blockModels.registerSimpleItemModel(block, switchTrackInventory);

    var directionMap = Util.make(
        new EnumMap<Direction, VariantProperties.Rotation>(Direction.class), x -> {
          x.put(Direction.NORTH, VariantProperties.Rotation.R0);
          x.put(Direction.EAST, VariantProperties.Rotation.R90);
          x.put(Direction.SOUTH, VariantProperties.Rotation.R180);
          x.put(Direction.WEST, VariantProperties.Rotation.R270);
        });

    var builder = this.createSwitchTrackActuatorBlockCommon(block);
    for (var direction : Direction.Plane.HORIZONTAL) {
      var rot = directionMap.get(direction);
      var model = isRouter ? switchTrackActuatorRouter : switchTrackActuator;

      builder
          .with(
              Condition.condition().term(SwitchTrackLeverBlock.FACING, direction),
              Variant.variant()
                  .with(VariantProperties.MODEL, model)
                  .with(VariantProperties.Y_ROT, rot));
    }
    blockModels.blockStateOutput.accept(builder);
  }

  private void createAnvil(BlockModelGenerators blockModels, AnvilBlock block) {
    var model = RailcraftModelTemplates.STEEL_ANVIL_TEMPLATE_PROVIDER
        .create(block, blockModels.modelOutput);

    blockModels.blockStateOutput.accept(
        BlockModelGenerators.createSimpleBlock(block, model)
            .with(BlockModelGenerators.createHorizontalFacingDispatchAlt()));
  }

  private void createSteamOven(BlockModelGenerators blockModels, SteamOvenBlock block) {
    var sideTexture = TextureMapping.getBlockTexture(block, "_side");
    var frontTexture = TextureMapping.getBlockTexture(block, "_front");
    var topTexture = TextureMapping.getBlockTexture(block, "_top");

    var bottomLeftTexture = TextureMapping.getBlockTexture(block, "_bottom_left");
    var bottomRightTexture = TextureMapping.getBlockTexture(block, "_bottom_right");
    var topLeftTexture = TextureMapping.getBlockTexture(block, "_top_left");
    var topRightTexture = TextureMapping.getBlockTexture(block, "_top_right");

    var defaultModel = ModelTemplates.CUBE_ORIENTABLE
        .create(block, new TextureMapping()
            .put(TextureSlot.SIDE, sideTexture)
            .put(TextureSlot.FRONT, frontTexture)
            .put(TextureSlot.TOP, topTexture), blockModels.modelOutput);
    var bottomLeftModel = ModelTemplates.CUBE_ORIENTABLE
        .createWithOverride(block, "_bottom_left", new TextureMapping()
            .put(TextureSlot.SIDE, sideTexture)
            .put(TextureSlot.FRONT, bottomLeftTexture)
            .put(TextureSlot.TOP, topTexture), blockModels.modelOutput);
    var bottomRightModel = ModelTemplates.CUBE_ORIENTABLE
        .createWithOverride(block, "_bottom_right", new TextureMapping()
            .put(TextureSlot.SIDE, sideTexture)
            .put(TextureSlot.FRONT, bottomRightTexture)
            .put(TextureSlot.TOP, topTexture), blockModels.modelOutput);
    var topLeftModel = ModelTemplates.CUBE_ORIENTABLE
        .createWithOverride(block, "_top_left", new TextureMapping()
            .put(TextureSlot.SIDE, sideTexture)
            .put(TextureSlot.FRONT, topLeftTexture)
            .put(TextureSlot.TOP, topTexture), blockModels.modelOutput);
    var topRightModel = ModelTemplates.CUBE_ORIENTABLE
        .createWithOverride(block, "_top_right", new TextureMapping()
            .put(TextureSlot.SIDE, sideTexture)
            .put(TextureSlot.FRONT, topRightTexture)
            .put(TextureSlot.TOP, topTexture), blockModels.modelOutput);

    blockModels.blockStateOutput.accept(
        MultiVariantGenerator.multiVariant(block)
            .with(
                PropertyDispatch.property(SteamOvenBlock.TYPE)
                    .select(SteamOvenBlock.Type.DEFAULT,
                        Variant.variant().with(VariantProperties.MODEL, defaultModel))
                    .select(SteamOvenBlock.Type.DOOR_TOP_LEFT,
                        Variant.variant().with(VariantProperties.MODEL, topLeftModel))
                    .select(SteamOvenBlock.Type.DOOR_TOP_RIGHT,
                        Variant.variant().with(VariantProperties.MODEL, topRightModel))
                    .select(SteamOvenBlock.Type.DOOR_BOTTOM_LEFT,
                        Variant.variant().with(VariantProperties.MODEL, bottomLeftModel))
                    .select(SteamOvenBlock.Type.DOOR_BOTTOM_RIGHT,
                        Variant.variant().with(VariantProperties.MODEL, bottomRightModel))
            ).with(BlockModelGenerators.createHorizontalFacingDispatch())
    );
    blockModels.registerSimpleItemModel(block, defaultModel);
  }

  private void createFrameBlock(BlockModelGenerators blockModels, FrameBlock block) {
    var sideTexture = TextureMapping.getBlockTexture(block, "_side");
    var topTexture = TextureMapping.getBlockTexture(block, "_top");
    var topPoweredTexture = TextureMapping.getBlockTexture(block, "_top_powered");

    var model = RailcraftModelTemplates.FRAME_TEMPLATE
        .extend().renderType(CUTOUT).build()
        .create(block, new TextureMapping()
            .put(TextureSlot.SIDE, sideTexture)
            .put(TextureSlot.TOP, topTexture), blockModels.modelOutput);

    var modelPowered = RailcraftModelTemplates.FRAME_TEMPLATE
        .extend().renderType(CUTOUT).build()
        .createWithSuffix(block, "_powered", new TextureMapping()
            .put(TextureSlot.SIDE, sideTexture)
            .put(TextureSlot.TOP, topPoweredTexture), blockModels.modelOutput);

    blockModels.blockStateOutput.accept(
        MultiVariantGenerator.multiVariant(block)
            .with(BlockModelGenerators
                .createBooleanModelDispatch(FrameBlock.POWERED, modelPowered, model)));
    blockModels.registerSimpleItemModel(block, model);
  }

  private void createLogBookBlock(BlockModelGenerators blockModels, LogBookBlock block) {
    var model = RailcraftModelTemplates.LOGBOOK_TEMPLATE_PROVIDER
        .create(block, blockModels.modelOutput);

    var variant = new Variant();
    variant.with(VariantProperties.MODEL, model);

    blockModels.blockStateOutput.accept(
        MultiVariantGenerator.multiVariant(block, variant)
            .with(BlockModelGenerators.createHorizontalFacingDispatch()));
    blockModels.registerSimpleItemModel(block, model);
  }

  private void createChimneyBlock(BlockModelGenerators blockModels, ChimneyBlock block) {
    var model = RailcraftModelTemplates.CHIMNEY_TEMPLATE_PROVIDER
        .create(block, blockModels.modelOutput);
    blockModels.blockStateOutput.accept(BlockModelGenerators.createSimpleBlock(block, model));
    blockModels.registerSimpleItemModel(block, model);
  }

  private void createWorldSpikeBlock(BlockModelGenerators blockModels, WorldSpikeBlock block) {
    var sideTexture = TextureMapping.getBlockTexture(block, "_side");
    var topTexture = TextureMapping.getBlockTexture(block, "_top");

    var model = ModelTemplates.CUBE_BOTTOM_TOP
        .create(block, new TextureMapping()
            .put(TextureSlot.SIDE, sideTexture)
            .put(TextureSlot.TOP, topTexture)
            .put(TextureSlot.BOTTOM, topTexture), blockModels.modelOutput);
    blockModels.blockStateOutput.accept(BlockModelGenerators.createSimpleBlock(block, model));
    blockModels.registerSimpleItemModel(block, model);
  }

  private void createFirebox(BlockModelGenerators blockModels, FireboxBlock block) {
    var endTexture = TextureMapping.getBlockTexture(block, "_end");
    var sideTexture = TextureMapping.getBlockTexture(block, "_side");
    var sideLitTexture = TextureMapping.getBlockTexture(block, "_side_lit");

    var model = ModelTemplates.CUBE_COLUMN
        .create(block, new TextureMapping()
            .put(TextureSlot.SIDE, sideTexture)
            .put(TextureSlot.END, endTexture), blockModels.modelOutput);
    var litModel = ModelTemplates.CUBE_COLUMN
        .createWithSuffix(block, "_lit", new TextureMapping()
            .put(TextureSlot.SIDE, sideLitTexture)
            .put(TextureSlot.END, endTexture), blockModels.modelOutput);

    blockModels.blockStateOutput.accept(
        MultiVariantGenerator.multiVariant(block)
            .with(BlockModelGenerators.createBooleanModelDispatch(FireboxBlock.LIT, litModel,
                model)));
    blockModels.registerSimpleItemModel(block, model);
  }

  private void createFurnaceMultiblockBricks(BlockModelGenerators blockModels,
      FurnaceMultiblockBlock block) {
    var blockTexture = TextureMapping.getBlockTexture(block);
    var sideTexture = TextureMapping.getBlockTexture(block, "_window");
    var sideLitTexture = TextureMapping.getBlockTexture(block, "_window_lit");

    var bricksModel = TexturedModel.CUBE.create(block, blockModels.modelOutput);
    var windowModel = ModelTemplates.CUBE_BOTTOM_TOP
        .createWithSuffix(block, "_window", new TextureMapping()
            .put(TextureSlot.SIDE, sideTexture)
            .put(TextureSlot.BOTTOM, blockTexture)
            .put(TextureSlot.TOP, blockTexture), blockModels.modelOutput);
    var litWindowModel = ModelTemplates.CUBE_BOTTOM_TOP
        .createWithSuffix(block, "_window_lit", new TextureMapping()
            .put(TextureSlot.SIDE, sideLitTexture)
            .put(TextureSlot.BOTTOM, blockTexture)
            .put(TextureSlot.TOP, blockTexture), blockModels.modelOutput);

    blockModels.blockStateOutput.accept(
        MultiVariantGenerator.multiVariant(block)
            .with(
                PropertyDispatch.properties(FurnaceMultiblockBlock.WINDOW,
                        FurnaceMultiblockBlock.LIT)
                    .generate((window, lit) -> {
                      ResourceLocation model;
                      if (!window) {
                        model = bricksModel;
                      } else if (lit) {
                        model = litWindowModel;
                      } else {
                        model = windowModel;
                      }
                      return Variant.variant().with(VariantProperties.MODEL, model);
                    })
            ));
    blockModels.registerSimpleItemModel(block, bricksModel);
  }

  private void createCrusherMultiblockBricks(BlockModelGenerators blockModels,
      CrusherMultiblockBlock block) {
    var topTexture = TextureMapping.getBlockTexture(block, "_top");
    var sideTexture = TextureMapping.getBlockTexture(block, "_side_exporter");
    var bottomTexture = TextureMapping.getBlockTexture(block, "_side");

    var baseModel = ModelTemplates.CUBE_TOP
        .create(block, new TextureMapping()
            .put(TextureSlot.TOP, topTexture)
            .put(TextureSlot.SIDE, bottomTexture), blockModels.modelOutput);
    var outputModel = ModelTemplates.CUBE_BOTTOM_TOP
        .createWithSuffix(block, "_exporter", new TextureMapping()
            .put(TextureSlot.SIDE, sideTexture)
            .put(TextureSlot.BOTTOM, bottomTexture)
            .put(TextureSlot.TOP, topTexture), blockModels.modelOutput);

    var modelMap = new HashMap<CrusherMultiblockBlock.Type, ResourceLocation>();
    CrusherMultiblockBlock.TYPE.getAllValues()
        .forEach(type -> {
          if (modelMap.containsKey(type.value())) {
            return;
          }
          var suffix = "_top_" + type.value().getSerializedName();
          var model = ModelTemplates.CUBE_TOP
              .createWithSuffix(block, suffix, new TextureMapping()
                  .put(TextureSlot.TOP, TextureMapping.getBlockTexture(block, suffix))
                  .put(TextureSlot.SIDE, bottomTexture), blockModels.modelOutput);
          modelMap.put(type.value(), model);
        });

    blockModels.blockStateOutput.accept(
        MultiVariantGenerator.multiVariant(block)
            .with(
                PropertyDispatch.properties(CrusherMultiblockBlock.TYPE,
                        CrusherMultiblockBlock.ROTATED, CrusherMultiblockBlock.OUTPUT)
                    .generate((type, rotated, output) -> {
                      if (output) {
                        return Variant.variant().with(VariantProperties.MODEL, outputModel);
                      } else if (type.equals(CrusherMultiblockBlock.Type.NONE)) {
                        return Variant.variant().with(VariantProperties.MODEL, baseModel);
                      } else {
                        return Variant.variant().with(VariantProperties.MODEL, modelMap.get(type))
                            .with(VariantProperties.Y_ROT, rotated ? VariantProperties.Rotation.R90
                                : VariantProperties.Rotation.R0);
                      }
                    })
            ));
    blockModels.registerSimpleItemModel(block, baseModel);
  }

  private void createSteamBoilerTank(BlockModelGenerators blockModels, SteamBoilerTankBlock block) {
    var end = TextureMapping.getBlockTexture(block, "_end");
    var side = TextureMapping.getBlockTexture(block, "_side");

    var allModel = ModelTemplates.CUBE_COLUMN
        .createWithSuffix(block, "_all", new TextureMapping()
            .put(TextureSlot.SIDE, side)
            .put(TextureSlot.END, end), blockModels.modelOutput);

    var model = RailcraftModelTemplates.STEAM_BOILER_TEMPLATE_PROVIDER
        .create(block, blockModels.modelOutput);
    var northEastModel = RailcraftModelTemplates.STEAM_BOILER_NE_TEMPLATE_PROVIDER
        .create(block, blockModels.modelOutput);
    var northEastWestModel = RailcraftModelTemplates.STEAM_BOILER_NEW_TEMPLATE_PROVIDER
        .create(block, blockModels.modelOutput);
    var northSouthEastModel = RailcraftModelTemplates.STEAM_BOILER_NSE_TEMPLATE_PROVIDER
        .create(block, blockModels.modelOutput);
    var northSouthWestModel = RailcraftModelTemplates.STEAM_BOILER_NSW_TEMPLATE_PROVIDER
        .create(block, blockModels.modelOutput);
    var northWestModel = RailcraftModelTemplates.STEAM_BOILER_NW_TEMPLATE_PROVIDER
        .create(block, blockModels.modelOutput);
    var southEastModel = RailcraftModelTemplates.STEAM_BOILER_SE_TEMPLATE_PROVIDER
        .create(block, blockModels.modelOutput);
    var southEastWestModel = RailcraftModelTemplates.STEAM_BOILER_SEW_TEMPLATE_PROVIDER
        .create(block, blockModels.modelOutput);
    var southWestModel = RailcraftModelTemplates.STEAM_BOILER_SW_TEMPLATE_PROVIDER
        .create(block, blockModels.modelOutput);

    blockModels.blockStateOutput.accept(
        MultiVariantGenerator.multiVariant(block)
            .with(
                PropertyDispatch.property(SteamBoilerTankBlock.CONNECTION_TYPE)
                    .generate((type) -> Variant.variant()
                        .with(VariantProperties.MODEL, switch (type) {
                          case ALL -> allModel;
                          case NONE -> model;
                          case NORTH_EAST -> northEastModel;
                          case SOUTH_EAST -> southEastModel;
                          case SOUTH_WEST -> southWestModel;
                          case NORTH_WEST -> northWestModel;
                          case NORTH_SOUTH_EAST -> northSouthEastModel;
                          case SOUTH_EAST_WEST -> southEastWestModel;
                          case NORTH_EAST_WEST -> northEastWestModel;
                          case NORTH_SOUTH_WEST -> northSouthWestModel;
                        }))
            ));
    blockModels.registerSimpleItemModel(block, model);
  }

  private void createSteamTurbine(BlockModelGenerators blockModels, Block block) {
    var sideTexture = TextureMapping.getBlockTexture(block, "_side");

    var itemModel = this.createSteamTurbineModel(blockModels, block,
        sideTexture, "_inventory", false);
    blockModels.registerSimpleItemModel(block, itemModel);

    var noneVariant = ModelTemplates.CUBE_ALL.
        createWithOverride(block, "_side",
            TextureMapping.singleSlot(TextureSlot.ALL, sideTexture), blockModels.modelOutput);

    var modelMap = new HashMap<SteamTurbineBlock.Type, ResourceLocation>();
    SteamTurbineBlock.TYPE.getAllValues()
        .forEach(type -> {
          if (modelMap.containsKey(type.value()) || type.value()
              .equals(SteamTurbineBlock.Type.NONE)) {
            return;
          }
          var model = this.createSteamTurbineModel(blockModels, block,
              sideTexture, "_" + type.value().getSerializedName(),
              type.value() != SteamTurbineBlock.Type.WINDOW);
          modelMap.put(type.value(), model);
        });

    blockModels.blockStateOutput.accept(
        MultiVariantGenerator.multiVariant(block)
            .with(
                PropertyDispatch.properties(SteamTurbineBlock.TYPE, SteamTurbineBlock.ROTATED)
                    .generate((type, rotated) -> {
                      if (type == SteamTurbineBlock.Type.NONE) {
                        return Variant.variant().with(VariantProperties.MODEL, noneVariant);
                      } else {
                        return Variant.variant().with(VariantProperties.MODEL, modelMap.get(type))
                            .with(VariantProperties.Y_ROT, rotated ?
                                VariantProperties.Rotation.R90 : VariantProperties.Rotation.R0);
                      }
                    })
            )
    );
  }

  private ResourceLocation createSteamTurbineModel(BlockModelGenerators blockModels, Block block,
      ResourceLocation sideTexture, String suffix, boolean rotated) {
    var frontTexture = TextureMapping.getBlockTexture(block, suffix);

    var tm = new TextureMapping()
        .put(TextureSlot.DOWN, sideTexture)
        .put(TextureSlot.UP, sideTexture)
        .put(TextureSlot.NORTH, rotated ? sideTexture : frontTexture)
        .put(TextureSlot.SOUTH, rotated ? sideTexture : frontTexture)
        .put(TextureSlot.EAST, rotated ? frontTexture : sideTexture)
        .put(TextureSlot.WEST, rotated ? frontTexture : sideTexture)
        .put(TextureSlot.PARTICLE, sideTexture);

    return RailcraftModelTemplates.MIRRORED_CUBE_TEMPLATE
        .createWithSuffix(block, suffix, tm, blockModels.modelOutput);
  }

  private void createRechargeableBattery(BlockModelGenerators blockModels, BatteryBlock block) {
    var model = RailcraftModelTemplates.BATTERY_TEMPLATE_PROVIDER
        .create(block, blockModels.modelOutput);
    blockModels.blockStateOutput.accept(BlockModelGenerators.createSimpleBlock(block, model));
    blockModels.registerSimpleItemModel(block, model);
  }

  private void createDisposableBattery(BlockModelGenerators blockModels,
      DisposableBatteryBlock battery, EmptyBatteryBlock emptyBattery) {
    this.createRechargeableBattery(blockModels, battery);
    var top = TextureMapping.getBlockTexture(battery, "_top_burned");
    var bottom = TextureMapping.getBlockTexture(battery, "_bottom");
    var sideA = TextureMapping.getBlockTexture(battery, "_side_a_burned");
    var sideB = TextureMapping.getBlockTexture(battery, "_side_b_burned");

    var tm = new TextureMapping()
        .put(TextureSlot.TOP, top)
        .put(TextureSlot.BOTTOM, bottom)
        .put(RailcraftTextureSlot.SIDE_A, sideA)
        .put(RailcraftTextureSlot.SIDE_B, sideB);

    var model = RailcraftModelTemplates.BATTERY_TEMPLATE
        .create(emptyBattery, tm, blockModels.modelOutput);

    blockModels.blockStateOutput.accept(
        BlockModelGenerators.createSimpleBlock(emptyBattery, model));
    blockModels.registerSimpleItemModel(emptyBattery, model);
  }

  private void createForceTrackEmitter(BlockModelGenerators blockModels,
      ForceTrackEmitterBlock block) {
    var front = TextureMapping.getBlockTexture(block, "_facing");
    var side = TextureMapping.getBlockTexture(block, "_side");
    var frontColored = TextureMapping.getBlockTexture(block, "_facing_colored");
    var sideColored = TextureMapping.getBlockTexture(block, "_side_colored");

    var frontUnpowered = TextureMapping.getBlockTexture(block, "_facing_unpowered");
    var sideUnpowered = TextureMapping.getBlockTexture(block, "_side_unpowered");
    var frontColoredUnpowered = TextureMapping.getBlockTexture(block,
        "_facing_unpowered_colored");
    var sideColoredUnpowered = TextureMapping.getBlockTexture(block, "_side_unpowered_colored");

    var tmUnpowered = new TextureMapping()
        .put(TextureSlot.FRONT, frontUnpowered)
        .put(TextureSlot.SIDE, sideUnpowered)
        .put(RailcraftTextureSlot.COLORED_FRONT, frontColoredUnpowered)
        .put(RailcraftTextureSlot.COLORED_SIDE, sideColoredUnpowered)
        .put(TextureSlot.PARTICLE, sideUnpowered);

    var modelUnpowered = RailcraftModelTemplates.FORCE_TRACK_EMITTER_TEMPLATE
        .extend().renderType(CUTOUT).build()
        .createWithOverride(block, "_unpowered", tmUnpowered, blockModels.modelOutput);

    var tmPowered = new TextureMapping()
        .put(TextureSlot.FRONT, front)
        .put(TextureSlot.SIDE, side)
        .put(RailcraftTextureSlot.COLORED_FRONT, frontColored)
        .put(RailcraftTextureSlot.COLORED_SIDE, sideColored)
        .put(TextureSlot.PARTICLE, side);

    var modelPowered = RailcraftModelTemplates.FORCE_TRACK_EMITTER_TEMPLATE
        .extend().renderType(CUTOUT).build()
        .createWithSuffix(block, "_powered", tmPowered, blockModels.modelOutput);

    blockModels.blockStateOutput.accept(
        MultiVariantGenerator.multiVariant(block)
            .with(
                BlockModelGenerators.createBooleanModelDispatch(ForceTrackEmitterBlock.POWERED,
                    modelPowered, modelUnpowered)));
    blockModels.registerSimpleItemModel(block, modelUnpowered);
  }

  private void createForceTrack(BlockModelGenerators blockModels, ForceTrackBlock block) {
    var texture = TextureMapping.getBlockTexture(block);

    var model = RailcraftModelTemplates.FORCE_TRACK_TEMPLATE
        .extend().renderType(CUTOUT).build()
        .create(block, TextureMapping.singleSlot(TextureSlot.RAIL, texture),
            blockModels.modelOutput);

    blockModels.blockStateOutput.accept(
        MultiVariantGenerator.multiVariant(block)
            .with(
                PropertyDispatch.property(ForceTrackBlock.SHAPE)
                    .select(RailShape.NORTH_SOUTH,
                        Variant.variant().with(VariantProperties.MODEL, model))
                    .select(RailShape.EAST_WEST,
                        Variant.variant().with(VariantProperties.MODEL, model)
                            .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90)
                    )));
  }

  private void createElevatorTrack(BlockModelGenerators blockModels, ElevatorTrackBlock block) {
    var texture = TextureMapping.defaultTexture(block).get(TextureSlot.TEXTURE);
    var textureOn = TextureMapping.getBlockTexture(block, "_on");

    var model = RailcraftModelTemplates.ELEVATOR_TRACK_TEMPLATE
        .extend().renderType(CUTOUT).build()
        .create(block, TextureMapping.singleSlot(TextureSlot.TEXTURE, texture),
            blockModels.modelOutput);
    var activeModel = RailcraftModelTemplates.ELEVATOR_TRACK_TEMPLATE
        .extend().renderType(CUTOUT).build()
        .createWithSuffix(block, "_on", TextureMapping.singleSlot(TextureSlot.TEXTURE, textureOn),
            blockModels.modelOutput);

    blockModels.blockStateOutput.accept(
        MultiVariantGenerator.multiVariant(block)
            .with(
                PropertyDispatch.properties(ElevatorTrackBlock.POWERED, ElevatorTrackBlock.FACING)
                    .generate((powered, facing) -> {
                      var yRot = switch (facing) {
                        case SOUTH -> VariantProperties.Rotation.R180;
                        case EAST -> VariantProperties.Rotation.R90;
                        case WEST -> VariantProperties.Rotation.R270;
                        default -> VariantProperties.Rotation.R0;
                      };
                      return Variant.variant()
                          .with(VariantProperties.MODEL, powered ? activeModel : model)
                          .with(VariantProperties.Y_ROT, yRot);
                    })
            )
    );
    blockModels.registerSimpleFlatItemModel(block);
  }

  private void createFluidManipulator(BlockModelGenerators blockModels,
      FluidManipulatorBlock<?> block) {
    var tm = TextureMapping.cubeBottomTop(block);
    var model = ModelTemplates.CUBE_BOTTOM_TOP
        .extend().renderType(CUTOUT).build()
        .create(block, tm, blockModels.modelOutput);

    blockModels.blockStateOutput.accept(BlockModelGenerators.createSimpleBlock(block, model));

    var side = modLocation("block/fluid_manipulator_side_inventory");
    var bottom = TextureMapping.getBlockTexture(block, "_bottom");
    var top = TextureMapping.getBlockTexture(block, "_top");

    var modelInventory = ModelTemplates.CUBE_BOTTOM_TOP
        .createWithSuffix(block, "_inventory", tm
                .put(TextureSlot.SIDE, side)
                .put(TextureSlot.BOTTOM, bottom)
                .put(TextureSlot.TOP, top),
            blockModels.modelOutput);
    blockModels.registerSimpleItemModel(block, modelInventory);
  }

  private void createManipulator(BlockModelGenerators blockModels, ManipulatorBlock<?> block) {
    var tm = TextureMapping.cubeBottomTop(block);
    var model = ModelTemplates.CUBE_BOTTOM_TOP.create(block, tm, blockModels.modelOutput);

    blockModels.blockStateOutput.accept(BlockModelGenerators.createSimpleBlock(block, model));
    blockModels.registerSimpleItemModel(block, model);
  }

  private void createDirectionalManipulator(BlockModelGenerators blockModels,
      ManipulatorBlock<?> block) {
    var tm = TextureMapping.orientableCubeOnlyTop(block);
    var model = ModelTemplates.CUBE_ORIENTABLE.create(block, tm, blockModels.modelOutput);

    var variant = new Variant();
    variant.with(VariantProperties.MODEL, model);

    blockModels.blockStateOutput.accept(
        MultiVariantGenerator.multiVariant(block, variant)
            .with(BlockModelGenerators.createFacingDispatch()));
    blockModels.registerSimpleItemModel(block, model);
  }

  private void createAbandonedTracks(BlockModelGenerators blockModels,
      TrackBlock block, LockingTrackBlock lockingTrackBlock,
      BufferStopTrackBlock bufferStopTrackBlock, ActivatorTrackBlock activatorTrackBlock,
      BoosterTrackBlock boosterTrackBlock, ControlTrackBlock controlTrackBlock,
      GatedTrackBlock gatedTrackBlock, DetectorTrackBlock detectorTrackBlock,
      CouplerTrackBlock couplerTrackBlock, EmbarkingTrackBlock embarkingTrackBlock,
      DisembarkingTrackBlock disembarkingTrackBlock, DumpingTrackBlock dumpingTrackBlock,
      TurnoutTrackBlock turnoutTrackBlock, WyeTrackBlock wyeTrackBlock,
      JunctionTrackBlock junctionTrackBlock, LauncherTrackBlock launcherTrackBlock,
      OneWayTrackBlock oneWayTrackBlock, WhistleTrackBlock whistleTrackBlock,
      LocomotiveTrackBlock locomotiveTrackBlock, ThrottleTrackBlock throttleTrackBlock,
      RoutingTrackBlock routingTrackBlock) {
    this.createAbandonedFlexTrack(blockModels, block);
    this.createOutfittedTracks(blockModels, block, lockingTrackBlock, bufferStopTrackBlock,
    activatorTrackBlock,
        boosterTrackBlock, controlTrackBlock, gatedTrackBlock, detectorTrackBlock,
        couplerTrackBlock, embarkingTrackBlock, disembarkingTrackBlock, dumpingTrackBlock,
        turnoutTrackBlock, wyeTrackBlock, junctionTrackBlock, launcherTrackBlock, oneWayTrackBlock,
        whistleTrackBlock, locomotiveTrackBlock, throttleTrackBlock, routingTrackBlock);
  }

  private void createAbandonedFlexTrack(BlockModelGenerators blockModels, TrackBlock block) {
    var texture0 = TextureMapping.getBlockTexture(block, "_0");
    var texture1 = TextureMapping.getBlockTexture(block, "_1");
    var cornerTexture = TextureMapping.getBlockTexture(block, "_corner");

    var flatModel0 = ModelTemplates.RAIL_FLAT
        .extend().renderType(CUTOUT).build()
        .createWithSuffix(block, "_0", TextureMapping.singleSlot(TextureSlot.RAIL, texture0),
            blockModels.modelOutput);
    var flatModel1 = ModelTemplates.RAIL_FLAT
        .extend().renderType(CUTOUT).build()
        .createWithSuffix(block, "_1", TextureMapping.singleSlot(TextureSlot.RAIL, texture1),
            blockModels.modelOutput);
    var cornerModel = ModelTemplates.RAIL_CURVED
        .extend().renderType(CUTOUT).build()
        .create(block,
            TextureMapping.singleSlot(TextureSlot.RAIL, cornerTexture), blockModels.modelOutput);
    var raisedNorthEastModel = ModelTemplates.RAIL_RAISED_NE
        .extend().renderType(CUTOUT).build()
        .create(block,
            TextureMapping.singleSlot(TextureSlot.RAIL, texture0), blockModels.modelOutput);
    var raisedSouthWestModel = ModelTemplates.RAIL_RAISED_SW
        .extend().renderType(CUTOUT).build()
        .create(block,
            TextureMapping.singleSlot(TextureSlot.RAIL, texture0), blockModels.modelOutput);

    blockModels.blockStateOutput.accept(
        MultiPartGenerator.multiPart(block)
            .with(
                Condition.condition().term(AbandonedTrackBlock.GRASS, true),
                Variant.variant().with(VariantProperties.MODEL, mcLocation("block/short_grass")))
            .with(
                Condition.condition().term(BlockStateProperties.RAIL_SHAPE, RailShape.NORTH_SOUTH),
                List.of(
                    Variant.variant().with(VariantProperties.MODEL, flatModel0),
                    Variant.variant().with(VariantProperties.MODEL, flatModel1)))
            .with(
                Condition.condition().term(BlockStateProperties.RAIL_SHAPE, RailShape.EAST_WEST),
                List.of(
                    Variant.variant().with(VariantProperties.MODEL, flatModel0)
                        .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90),
                    Variant.variant().with(VariantProperties.MODEL, flatModel1)
                        .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90)))
            .with(
                Condition.condition().term(BlockStateProperties.RAIL_SHAPE, RailShape.ASCENDING_EAST),
                Variant.variant().with(VariantProperties.MODEL, raisedNorthEastModel)
                        .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
            .with(
                Condition.condition().term(BlockStateProperties.RAIL_SHAPE, RailShape.ASCENDING_WEST),
                Variant.variant().with(VariantProperties.MODEL, raisedSouthWestModel)
                    .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
            .with(
                Condition.condition().term(BlockStateProperties.RAIL_SHAPE, RailShape.ASCENDING_NORTH),
                Variant.variant().with(VariantProperties.MODEL, raisedNorthEastModel))
            .with(
                Condition.condition().term(BlockStateProperties.RAIL_SHAPE, RailShape.ASCENDING_SOUTH),
                Variant.variant().with(VariantProperties.MODEL, raisedSouthWestModel))
            .with(
                Condition.condition().term(BlockStateProperties.RAIL_SHAPE, RailShape.SOUTH_EAST),
                Variant.variant().with(VariantProperties.MODEL, cornerModel))
            .with(
                Condition.condition().term(BlockStateProperties.RAIL_SHAPE, RailShape.SOUTH_WEST),
                Variant.variant().with(VariantProperties.MODEL, cornerModel)
                    .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
            .with(
                Condition.condition().term(BlockStateProperties.RAIL_SHAPE, RailShape.NORTH_WEST),
                Variant.variant().with(VariantProperties.MODEL, cornerModel)
                    .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180))
            .with(
                Condition.condition().term(BlockStateProperties.RAIL_SHAPE, RailShape.NORTH_EAST),
                Variant.variant().with(VariantProperties.MODEL, cornerModel)
                    .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270))
    );
    blockModels.registerSimpleFlatItemModel(block, "_0");
  }

  private void createTracks(BlockModelGenerators blockModels,
      TrackBlock block, LockingTrackBlock lockingTrackBlock,
      BufferStopTrackBlock bufferStopTrackBlock, ActivatorTrackBlock activatorTrackBlock,
      BoosterTrackBlock boosterTrackBlock, ControlTrackBlock controlTrackBlock,
      GatedTrackBlock gatedTrackBlock, DetectorTrackBlock detectorTrackBlock,
      CouplerTrackBlock couplerTrackBlock, EmbarkingTrackBlock embarkingTrackBlock,
      DisembarkingTrackBlock disembarkingTrackBlock, DumpingTrackBlock dumpingTrackBlock,
      TurnoutTrackBlock turnoutTrackBlock, WyeTrackBlock wyeTrackBlock,
      JunctionTrackBlock junctionTrackBlock, LauncherTrackBlock launcherTrackBlock,
      OneWayTrackBlock oneWayTrackBlock, WhistleTrackBlock whistleTrackBlock,
      LocomotiveTrackBlock locomotiveTrackBlock, ThrottleTrackBlock throttleTrackBlock,
      RoutingTrackBlock routingTrackBlock) {
    this.createFlexTrack(blockModels, block);
    this.createOutfittedTracks(blockModels, block, lockingTrackBlock, bufferStopTrackBlock,
        activatorTrackBlock, boosterTrackBlock, controlTrackBlock, gatedTrackBlock,
        detectorTrackBlock, couplerTrackBlock, embarkingTrackBlock, disembarkingTrackBlock,
        dumpingTrackBlock, turnoutTrackBlock, wyeTrackBlock, junctionTrackBlock, launcherTrackBlock,
        oneWayTrackBlock, whistleTrackBlock, locomotiveTrackBlock, throttleTrackBlock,
        routingTrackBlock);
  }

  private void createFlexTrack(BlockModelGenerators blockModels, TrackBlock block) {
    var textureMapping = TextureMapping.rail(block);
    var textureMappingCorner = TextureMapping.rail(TextureMapping.getBlockTexture(block, "_corner"));

    var flatModel = ModelTemplates.RAIL_FLAT
        .extend().renderType(CUTOUT).build()
        .create(block, textureMapping, blockModels.modelOutput);
    var cornerModel = ModelTemplates.RAIL_CURVED
        .extend().renderType(CUTOUT).build()
        .create(block, textureMappingCorner, blockModels.modelOutput);
    var raisedNorthEastModel = ModelTemplates.RAIL_RAISED_NE
        .extend().renderType(CUTOUT).build()
        .create(block, textureMapping, blockModels.modelOutput);
    var raisedSouthWestModel = ModelTemplates.RAIL_RAISED_SW
        .extend().renderType(CUTOUT).build()
        .create(block, textureMapping, blockModels.modelOutput);
    blockModels.registerSimpleFlatItemModel(block);
    blockModels.blockStateOutput.accept(
        MultiVariantGenerator.multiVariant(block)
            .with(
                PropertyDispatch.property(BlockStateProperties.RAIL_SHAPE)
                    .select(RailShape.NORTH_SOUTH, Variant.variant()
                        .with(VariantProperties.MODEL, flatModel))
                    .select(RailShape.EAST_WEST, Variant.variant().with(VariantProperties.MODEL, flatModel)
                        .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
                    .select(RailShape.ASCENDING_EAST, Variant.variant().with(VariantProperties.MODEL, raisedNorthEastModel)
                        .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
                    .select(RailShape.ASCENDING_WEST, Variant.variant().with(VariantProperties.MODEL, raisedSouthWestModel)
                        .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
                    .select(RailShape.ASCENDING_NORTH, Variant.variant().with(VariantProperties.MODEL, raisedNorthEastModel))
                    .select(RailShape.ASCENDING_SOUTH, Variant.variant().with(VariantProperties.MODEL, raisedSouthWestModel))
                    .select(RailShape.SOUTH_EAST, Variant.variant().with(VariantProperties.MODEL, cornerModel))
                    .select(RailShape.SOUTH_WEST, Variant.variant().with(VariantProperties.MODEL, cornerModel)
                        .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
                    .select(RailShape.NORTH_WEST, Variant.variant().with(VariantProperties.MODEL, cornerModel)
                        .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180))
                    .select(RailShape.NORTH_EAST, Variant.variant().with(VariantProperties.MODEL, cornerModel)
                        .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270))));
  }

  private void createOutfittedTracks(BlockModelGenerators blockModels,
      Block block, LockingTrackBlock lockingTrackBlock,
      BufferStopTrackBlock bufferStopTrackBlock, ActivatorTrackBlock activatorTrackBlock,
      BoosterTrackBlock boosterTrackBlock, ControlTrackBlock controlTrackBlock,
      GatedTrackBlock gatedTrackBlock, DetectorTrackBlock detectorTrackBlock,
      CouplerTrackBlock couplerTrackBlock, EmbarkingTrackBlock embarkingTrackBlock,
      DisembarkingTrackBlock disembarkingTrackBlock, DumpingTrackBlock dumpingTrackBlock,
      TurnoutTrackBlock turnoutTrackBlock, WyeTrackBlock wyeTrackBlock,
      JunctionTrackBlock junctionTrackBlock, LauncherTrackBlock launcherTrackBlock,
      OneWayTrackBlock oneWayTrackBlock, WhistleTrackBlock whistleTrackBlock,
      LocomotiveTrackBlock locomotiveTrackBlock, ThrottleTrackBlock throttleTrackBlock,
      RoutingTrackBlock routingTrackBlock) {
    var outfittedTrackModels = this.createOutfittedTrackModelSet(blockModels, block);

    var activatorTrackModels =
        this.createTrackModelSet(RailcraftModelTemplates.TrackType.ACTIVATOR_TRACK);
    var activeActivatorTrackModels =
        this.createTrackModelSet(RailcraftModelTemplates.TrackType.ACTIVATOR_TRACK_ON);
    this.createActiveOutfittedTrack(blockModels, activatorTrackBlock, true, false,
        outfittedTrackModels, activatorTrackModels, activeActivatorTrackModels);

    var boosterTrackModels =
        this.createTrackModelSet(RailcraftModelTemplates.TrackType.BOOSTER_TRACK);
    var activeBoosterTrackModels =
        this.createTrackModelSet(RailcraftModelTemplates.TrackType.BOOSTER_TRACK_ON);
    this.createActiveOutfittedTrack(blockModels, boosterTrackBlock, true, false,
        outfittedTrackModels, boosterTrackModels, activeBoosterTrackModels);

    var embarkingTrackModels =
        this.createTrackModelSet(RailcraftModelTemplates.TrackType.EMBARKING_TRACK);
    var activeEmbarkingTrackModels =
        this.createTrackModelSet(RailcraftModelTemplates.TrackType.EMBARKING_TRACK_ON);
    this.createActiveOutfittedTrack(blockModels, embarkingTrackBlock, true, false,
        outfittedTrackModels, embarkingTrackModels, activeEmbarkingTrackModels);

    var dumpingTrackModels =
        this.createTrackModelSet(RailcraftModelTemplates.TrackType.DUMPING_TRACK);
    var activeDumpingTrackModels =
        this.createTrackModelSet(RailcraftModelTemplates.TrackType.DUMPING_TRACK_ON);
    this.createActiveOutfittedTrack(blockModels, dumpingTrackBlock, true, false,
        outfittedTrackModels, dumpingTrackModels, activeDumpingTrackModels);

    var launcherTrackModels =
        this.createTrackModelSet(RailcraftModelTemplates.TrackType.LAUNCHER_TRACK);
    var activeLauncherTrackModels =
        this.createTrackModelSet(RailcraftModelTemplates.TrackType.LAUNCHER_TRACK_ON);
    this.createActiveOutfittedTrack(blockModels, launcherTrackBlock, false, false,
        outfittedTrackModels, launcherTrackModels, activeLauncherTrackModels);

    var oneWayTrackModels =
        this.createTrackModelSet(RailcraftModelTemplates.TrackType.ONE_WAY_TRACK);
    var activeOneWayTrackModels =
        this.createTrackModelSet(RailcraftModelTemplates.TrackType.ONE_WAY_TRACK_ON);
    this.createActiveOutfittedTrack(blockModels, oneWayTrackBlock, false, true,
        outfittedTrackModels, oneWayTrackModels, activeOneWayTrackModels);

    var routingTrackModels =
        this.createTrackModelSet(RailcraftModelTemplates.TrackType.ROUTING_TRACK);
    var activeRoutingTrackModels =
        this.createTrackModelSet(RailcraftModelTemplates.TrackType.ROUTING_TRACK_ON);
    this.createActiveOutfittedTrack(blockModels, routingTrackBlock, true, false,
        outfittedTrackModels, routingTrackModels, activeRoutingTrackModels);

    var whistleTrackModels =
        this.createTrackModelSet(RailcraftModelTemplates.TrackType.WHISTLE_TRACK);
    var activeWhistleTrackModels =
        this.createTrackModelSet(RailcraftModelTemplates.TrackType.WHISTLE_TRACK_ON);
    this.createActiveOutfittedTrack(blockModels, whistleTrackBlock, true, false,
        outfittedTrackModels, whistleTrackModels, activeWhistleTrackModels);

    var detectorTrackModels =
        this.createTrackModelSet(RailcraftModelTemplates.TrackType.DETECTOR_TRACK);
    var activeDetectorTrackModels =
        this.createTrackModelSet(RailcraftModelTemplates.TrackType.DETECTOR_TRACK_ON);
    var travelDetectorTrackModels =
        this.createTrackModelSet(RailcraftModelTemplates.TrackType.DETECTOR_TRACK_TRAVEL);
    var activeTravelDetectorTrackModels =
        this.createTrackModelSet(RailcraftModelTemplates.TrackType.DETECTOR_TRACK_TRAVEL_ON);
    this.createDetectorTrack(blockModels, detectorTrackBlock, outfittedTrackModels,
        detectorTrackModels, activeDetectorTrackModels,
        travelDetectorTrackModels, activeTravelDetectorTrackModels);

    var locomotiveTrackShutdownModel =
        this.createTrackModelSet(RailcraftModelTemplates.TrackType.LOCOMOTIVE_TRACK_SHUTDOWN);
    var activeLocomotiveTrackShutdownModel =
        this.createTrackModelSet(RailcraftModelTemplates.TrackType.LOCOMOTIVE_TRACK_SHUTDOWN_ON);
    var locomotiveTrackIdleModel =
        this.createTrackModelSet(RailcraftModelTemplates.TrackType.LOCOMOTIVE_TRACK_IDLE);
    var activeLocomotiveTrackIdleModel =
        this.createTrackModelSet(RailcraftModelTemplates.TrackType.LOCOMOTIVE_TRACK_IDLE_ON);
    var locomotiveTrackRunningModel =
        this.createTrackModelSet(RailcraftModelTemplates.TrackType.LOCOMOTIVE_TRACK_RUNNING);
    var activeLocomotiveTrackRunningModel =
        this.createTrackModelSet(RailcraftModelTemplates.TrackType.LOCOMOTIVE_TRACK_RUNNING_ON);
    this.createLocomotiveTrack(blockModels, locomotiveTrackBlock, outfittedTrackModels,
        locomotiveTrackShutdownModel, activeLocomotiveTrackShutdownModel,
        locomotiveTrackIdleModel, activeLocomotiveTrackIdleModel,
        locomotiveTrackRunningModel, activeLocomotiveTrackRunningModel);

    var controlTrackModels =
        this.createTrackModelSet(RailcraftModelTemplates.TrackType.CONTROL_TRACK);
    this.createControlTrack(blockModels, controlTrackBlock, outfittedTrackModels,
        controlTrackModels);
    this.createGatedTrack(blockModels, gatedTrackBlock, outfittedTrackModels, controlTrackModels);

    var couplerTrackCoupler =
        this.createTrackModelSet(RailcraftModelTemplates.TrackType.COUPLER_TRACK_COUPLER);
    var activeCouplerTrackCoupler =
        this.createTrackModelSet(RailcraftModelTemplates.TrackType.COUPLER_TRACK_COUPLER_ON);
    var couplerTrackDecoupler =
        this.createTrackModelSet(RailcraftModelTemplates.TrackType.COUPLER_TRACK_DECOUPLER);
    var activeCouplerTrackDecoupler =
        this.createTrackModelSet(RailcraftModelTemplates.TrackType.COUPLER_TRACK_DECOUPLER_ON);
    var couplerTrackAutoCoupler =
        this.createTrackModelSet(RailcraftModelTemplates.TrackType.COUPLER_TRACK_AUTO_COUPLER);
    var activeCouplerTrackAutoCoupler =
        this.createTrackModelSet(RailcraftModelTemplates.TrackType.COUPLER_TRACK_AUTO_COUPLER_ON);
    this.createCouplerTrack(blockModels, couplerTrackBlock, outfittedTrackModels,
        couplerTrackCoupler, activeCouplerTrackCoupler, couplerTrackDecoupler,
        activeCouplerTrackDecoupler, couplerTrackAutoCoupler, activeCouplerTrackAutoCoupler);

    var disembarkingTrackLeft =
        this.createTrackModelSet(RailcraftModelTemplates.TrackType.DISEMBARKING_TRACK_LEFT);
    var activeDisembarkingTrackLeft =
        this.createTrackModelSet(RailcraftModelTemplates.TrackType.DISEMBARKING_TRACK_LEFT_ON);
    var disembarkingTrackRight =
        this.createTrackModelSet(RailcraftModelTemplates.TrackType.DISEMBARKING_TRACK_RIGHT);
    var activeDisembarkingTrackRight =
        this.createTrackModelSet(RailcraftModelTemplates.TrackType.DISEMBARKING_TRACK_RIGHT_ON);
    this.createDisembarkingTrack(blockModels, disembarkingTrackBlock, outfittedTrackModels,
        disembarkingTrackLeft, activeDisembarkingTrackLeft,
        disembarkingTrackRight, activeDisembarkingTrackRight);

    this.createLockingTrack(blockModels, lockingTrackBlock, outfittedTrackModels.flatModel());
    this.createThrottleTrack(blockModels, throttleTrackBlock, outfittedTrackModels.flatModel());
    this.createBufferStopTrack(blockModels, bufferStopTrackBlock, outfittedTrackModels.flatModel());
    this.createTurnoutTrack(blockModels, turnoutTrackBlock);
    this.createWyeTrack(blockModels, wyeTrackBlock);
    this.createJunctionTrack(blockModels, junctionTrackBlock);
  }

  private void createHighSpeedTracks(BlockModelGenerators blockModels, TrackBlock block,
      TransitionTrackBlock transitionTrackBlock, LockingTrackBlock lockingTrackBlock,
      ActivatorTrackBlock activatorTrackBlock, BoosterTrackBlock boosterTrackBlock,
      DetectorTrackBlock detectorTrackBlock, TurnoutTrackBlock turnoutTrackBlock,
      WyeTrackBlock wyeTrackBlock, JunctionTrackBlock junctionTrackBlock,
      WhistleTrackBlock whistleTrackBlock, LocomotiveTrackBlock locomotiveTrackBlock,
      ThrottleTrackBlock throttleTrackBlock) {
    this.createFlexTrack(blockModels, block);
    var outfittedTrackModels = this.createOutfittedTrackModelSet(blockModels, block);

    var activatorTrackModels =
        this.createTrackModelSet(RailcraftModelTemplates.TrackType.ACTIVATOR_TRACK);
    var activeActivatorTrackModels =
        this.createTrackModelSet(RailcraftModelTemplates.TrackType.ACTIVATOR_TRACK_ON);
    this.createActiveOutfittedTrack(blockModels, activatorTrackBlock, true, false,
        outfittedTrackModels, activatorTrackModels, activeActivatorTrackModels);

    var boosterTrackModels =
        this.createTrackModelSet(RailcraftModelTemplates.TrackType.BOOSTER_TRACK);
    var activeBoosterTrackModels =
        this.createTrackModelSet(RailcraftModelTemplates.TrackType.BOOSTER_TRACK_ON);
    this.createActiveOutfittedTrack(blockModels, boosterTrackBlock, true, false,
        outfittedTrackModels, boosterTrackModels, activeBoosterTrackModels);

    var transitionTrackModels =
        this.createTrackModelSet(RailcraftModelTemplates.TrackType.TRANSITION_TRACK);
    var activeTransitionTrackModels =
        this.createTrackModelSet(RailcraftModelTemplates.TrackType.TRANSITION_TRACK_ON);
    this.createActiveOutfittedTrack(blockModels, transitionTrackBlock, true, true,
        outfittedTrackModels, transitionTrackModels, activeTransitionTrackModels);

    var whistleTrackModels =
        this.createTrackModelSet(RailcraftModelTemplates.TrackType.WHISTLE_TRACK);
    var activeWhistleTrackModels =
        this.createTrackModelSet(RailcraftModelTemplates.TrackType.WHISTLE_TRACK_ON);
    this.createActiveOutfittedTrack(blockModels, whistleTrackBlock, true, false,
        outfittedTrackModels, whistleTrackModels, activeWhistleTrackModels);

    var detectorTrackModels =
        this.createTrackModelSet(RailcraftModelTemplates.TrackType.DETECTOR_TRACK);
    var activeDetectorTrackModels =
        this.createTrackModelSet(RailcraftModelTemplates.TrackType.DETECTOR_TRACK_ON);
    var travelDetectorTrackModels =
        this.createTrackModelSet(RailcraftModelTemplates.TrackType.DETECTOR_TRACK_TRAVEL);
    var activeTravelDetectorTrackModels =
        this.createTrackModelSet(RailcraftModelTemplates.TrackType.DETECTOR_TRACK_TRAVEL_ON);
    this.createDetectorTrack(blockModels, detectorTrackBlock, outfittedTrackModels,
        detectorTrackModels, activeDetectorTrackModels, travelDetectorTrackModels,
        activeTravelDetectorTrackModels);

    var locomotiveTrackShutdownModel =
        this.createTrackModelSet(RailcraftModelTemplates.TrackType.LOCOMOTIVE_TRACK_SHUTDOWN);
    var activeLocomotiveTrackShutdownModel =
        this.createTrackModelSet(RailcraftModelTemplates.TrackType.LOCOMOTIVE_TRACK_SHUTDOWN_ON);
    var locomotiveTrackIdleModel =
        this.createTrackModelSet(RailcraftModelTemplates.TrackType.LOCOMOTIVE_TRACK_IDLE);
    var activeLocomotiveTrackIdleModel =
        this.createTrackModelSet(RailcraftModelTemplates.TrackType.LOCOMOTIVE_TRACK_IDLE_ON);
    var locomotiveTrackRunningModel =
        this.createTrackModelSet(RailcraftModelTemplates.TrackType.LOCOMOTIVE_TRACK_RUNNING);
    var activeLocomotiveTrackRunningModel =
        this.createTrackModelSet(RailcraftModelTemplates.TrackType.LOCOMOTIVE_TRACK_RUNNING_ON);

    this.createLocomotiveTrack(blockModels, locomotiveTrackBlock, outfittedTrackModels,
        locomotiveTrackShutdownModel, activeLocomotiveTrackShutdownModel,
        locomotiveTrackIdleModel, activeLocomotiveTrackIdleModel,
        locomotiveTrackRunningModel, activeLocomotiveTrackRunningModel);

    this.createLockingTrack(blockModels, lockingTrackBlock, outfittedTrackModels.flatModel());
    this.createThrottleTrack(blockModels, throttleTrackBlock, outfittedTrackModels.flatModel());
    this.createTurnoutTrack(blockModels, turnoutTrackBlock);
    this.createWyeTrack(blockModels, wyeTrackBlock);
    this.createJunctionTrack(blockModels, junctionTrackBlock);
  }

  private StraightTrackModelSet createOutfittedTrackModelSet(BlockModelGenerators blockModels, Block block) {
    if (BuiltInRegistries.BLOCK.getKey(block).getNamespace().equals(ResourceLocation.DEFAULT_NAMESPACE)) {
      return this.createTrackModelSet(RailcraftModelTemplates.TrackType.RAIL_OUTFITTED);
    } else {
      return this.createTrackModelSet(blockModels, block);
    }
  }

  private StraightTrackModelSet createTrackModelSet(BlockModelGenerators blockModels, Block block) {
    var flat =
        RailcraftModelTemplates.OUTFITTED_TRACK_PROVIDER.RAIL_FLAT.getProvider()
            .create(block, blockModels.modelOutput);
    var raisedNE =
        RailcraftModelTemplates.OUTFITTED_TRACK_PROVIDER.RAIL_RAISED_NE.getProvider()
            .create(block, blockModels.modelOutput);
    var raisedSW =
        RailcraftModelTemplates.OUTFITTED_TRACK_PROVIDER.RAIL_RAISED_SW.getProvider()
            .create(block, blockModels.modelOutput);
    return new StraightTrackModelSet(flat, raisedNE, raisedSW);
  }

  private StraightTrackModelSet createTrackModelSet(RailcraftModelTemplates.TrackType trackType) {
    return new StraightTrackModelSet(
        RailcraftModelTemplates.TRACK_PROVIDER.RAIL_FLAT.getModel(trackType),
        RailcraftModelTemplates.TRACK_PROVIDER.RAIL_RAISED_NE.getModel(trackType),
        RailcraftModelTemplates.TRACK_PROVIDER.RAIL_RAISED_SW.getModel(trackType));
  }

  private void createActiveOutfittedTrack(BlockModelGenerators blockModels, Block block,
      boolean allowedOnSlopes, boolean reversible, StraightTrackModelSet trackModels,
      StraightTrackModelSet trackKitModels, StraightTrackModelSet activeTrackKitModels) {

    var multiPartGenerator = MultiPartGenerator.multiPart(block);
    trackModels.apply(multiPartGenerator, OutfittedTrackBlock.SHAPE, allowedOnSlopes, false);

    if (reversible) {
      trackKitModels.apply(multiPartGenerator, OutfittedTrackBlock.SHAPE, allowedOnSlopes, false,
          Condition.condition()
              .term(ReversibleOutfittedTrackBlock.REVERSED, false)
              .term(PoweredOutfittedTrackBlock.POWERED, false));
      activeTrackKitModels.apply(multiPartGenerator, OutfittedTrackBlock.SHAPE, allowedOnSlopes, false,
          Condition.condition()
              .term(ReversibleOutfittedTrackBlock.REVERSED, false)
              .term(PoweredOutfittedTrackBlock.POWERED, true));

      trackKitModels.apply(multiPartGenerator, OutfittedTrackBlock.SHAPE, allowedOnSlopes, true,
          Condition.condition()
              .term(ReversibleOutfittedTrackBlock.REVERSED, true)
              .term(PoweredOutfittedTrackBlock.POWERED, false));
      activeTrackKitModels.apply(multiPartGenerator, OutfittedTrackBlock.SHAPE, allowedOnSlopes, true,
          Condition.condition()
              .term(ReversibleOutfittedTrackBlock.REVERSED, true)
              .term(PoweredOutfittedTrackBlock.POWERED, true));
    } else {
      trackKitModels.apply(multiPartGenerator, OutfittedTrackBlock.SHAPE, allowedOnSlopes, false,
          Condition.condition().term(PoweredOutfittedTrackBlock.POWERED, false));
      activeTrackKitModels.apply(multiPartGenerator, OutfittedTrackBlock.SHAPE, allowedOnSlopes, false,
          Condition.condition().term(PoweredOutfittedTrackBlock.POWERED, true));
    }
    blockModels.blockStateOutput.accept(multiPartGenerator);
    blockModels.registerSimpleFlatItemModel(block.asItem());
  }

  private void createDetectorTrack(BlockModelGenerators blockModels, DetectorTrackBlock block,
      StraightTrackModelSet trackModels, StraightTrackModelSet detectorTrackModels,
      StraightTrackModelSet activeDetectorTrackModels, StraightTrackModelSet travelDetectorTrackModels,
      StraightTrackModelSet activeTravelDetectorTrackModels) {
    var multiPartGenerator = MultiPartGenerator.multiPart(block);
    trackModels.apply(multiPartGenerator, DetectorTrackBlock.SHAPE, true, false);

    detectorTrackModels.apply(multiPartGenerator, DetectorTrackBlock.SHAPE, true, false,
        Condition.condition()
            .term(DetectorTrackBlock.MODE, DetectorTrackBlock.Mode.BI_DIRECTIONAL)
            .term(DetectorTrackBlock.POWERED, false));

    activeDetectorTrackModels.apply(multiPartGenerator, DetectorTrackBlock.SHAPE, true, false,
        Condition.condition()
            .term(DetectorTrackBlock.MODE, DetectorTrackBlock.Mode.BI_DIRECTIONAL)
            .term(DetectorTrackBlock.POWERED, true));

    travelDetectorTrackModels.apply(multiPartGenerator, DetectorTrackBlock.SHAPE, true, false,
        Condition.condition()
            .term(DetectorTrackBlock.MODE, DetectorTrackBlock.Mode.TRAVEL)
            .term(DetectorTrackBlock.POWERED, false));

    activeTravelDetectorTrackModels.apply(multiPartGenerator, DetectorTrackBlock.SHAPE, true, false,
        Condition.condition()
            .term(DetectorTrackBlock.MODE, DetectorTrackBlock.Mode.TRAVEL)
            .term(DetectorTrackBlock.POWERED, true));

    travelDetectorTrackModels.apply(multiPartGenerator, DetectorTrackBlock.SHAPE, true, true,
        Condition.condition()
            .term(DetectorTrackBlock.MODE, DetectorTrackBlock.Mode.TRAVEL_REVERSED)
            .term(DetectorTrackBlock.POWERED, false));
    activeTravelDetectorTrackModels.apply(multiPartGenerator, DetectorTrackBlock.SHAPE, true, true,
        Condition.condition()
            .term(DetectorTrackBlock.MODE, DetectorTrackBlock.Mode.TRAVEL_REVERSED)
            .term(DetectorTrackBlock.POWERED, true));
    blockModels.blockStateOutput.accept(multiPartGenerator);
    blockModels.registerSimpleFlatItemModel(block.asItem());
  }

  private void createLocomotiveTrack(BlockModelGenerators blockModels,
      LocomotiveTrackBlock block, StraightTrackModelSet trackModel,
      StraightTrackModelSet locomotiveTrackShutdownModel,
      StraightTrackModelSet activeLocomotiveTrackShutdownModel,
      StraightTrackModelSet locomotiveTrackIdleModel,
      StraightTrackModelSet activeLocomotiveTrackIdleModel,
      StraightTrackModelSet locomotiveTrackRunningModel,
      StraightTrackModelSet activeLocomotiveTrackRunningModel) {
    var multiPartGenerator = MultiPartGenerator.multiPart(block);
    trackModel.apply(multiPartGenerator, LocomotiveTrackBlock.SHAPE, true, false);

    this.addLocomotiveMode(multiPartGenerator, Locomotive.Mode.SHUTDOWN, locomotiveTrackShutdownModel,
        activeLocomotiveTrackShutdownModel);
    this.addLocomotiveMode(multiPartGenerator, Locomotive.Mode.IDLE, locomotiveTrackIdleModel,
        activeLocomotiveTrackIdleModel);
    this.addLocomotiveMode(multiPartGenerator, Locomotive.Mode.RUNNING, locomotiveTrackRunningModel,
        activeLocomotiveTrackRunningModel);

    blockModels.blockStateOutput.accept(multiPartGenerator);
    blockModels.registerSimpleFlatItemModel(block.asItem());
  }

  private void addLocomotiveMode(MultiPartGenerator multiPartGenerator,
      Locomotive.Mode locomotiveMode, StraightTrackModelSet model,
      StraightTrackModelSet poweredModel) {
    model.apply(multiPartGenerator, LocomotiveTrackBlock.SHAPE, true, false,
        Condition.condition()
            .term(LocomotiveTrackBlock.LOCOMOTIVE_MODE, locomotiveMode)
            .term(LocomotiveTrackBlock.POWERED, false));
    poweredModel.apply(multiPartGenerator, LocomotiveTrackBlock.SHAPE, true, false,
        Condition.condition()
            .term(LocomotiveTrackBlock.LOCOMOTIVE_MODE, locomotiveMode)
            .term(LocomotiveTrackBlock.POWERED, true));
  }

  private void createControlTrack(BlockModelGenerators blockModels, ControlTrackBlock block,
      StraightTrackModelSet trackModels, StraightTrackModelSet controlTrackModels) {
    var multiPartGenerator = MultiPartGenerator.multiPart(block);
    trackModels.apply(multiPartGenerator, OutfittedTrackBlock.SHAPE, true, false);

    controlTrackModels.apply(multiPartGenerator, OutfittedTrackBlock.SHAPE, true, false,
        Condition.condition()
            .term(PoweredOutfittedTrackBlock.POWERED, false)
            .term(ControlTrackBlock.REVERSED, false));
    controlTrackModels.apply(multiPartGenerator, OutfittedTrackBlock.SHAPE, true, false,
        Condition.condition()
            .term(PoweredOutfittedTrackBlock.POWERED, true)
            .term(ControlTrackBlock.REVERSED, true));
    controlTrackModels.apply(multiPartGenerator, OutfittedTrackBlock.SHAPE, true, true,
        Condition.condition()
            .term(PoweredOutfittedTrackBlock.POWERED, true)
            .term(ControlTrackBlock.REVERSED, false));
    controlTrackModels.apply(multiPartGenerator, OutfittedTrackBlock.SHAPE, true, true,
        Condition.condition()
            .term(PoweredOutfittedTrackBlock.POWERED, false)
            .term(ControlTrackBlock.REVERSED, true));

    blockModels.blockStateOutput.accept(multiPartGenerator);
    blockModels.registerSimpleFlatItemModel(block.asItem());
  }

  private void createGatedTrack(BlockModelGenerators blockModels, GatedTrackBlock block,
      StraightTrackModelSet trackModels, StraightTrackModelSet controlTrackModels) {
    var closedGateModel = ModelLocationUtils.getModelLocation(Blocks.OAK_FENCE_GATE);
    var openGateModel = ModelLocationUtils.getModelLocation(Blocks.OAK_FENCE_GATE, "_open");
    var closedWallGateModel = ModelLocationUtils.getModelLocation(Blocks.OAK_FENCE_GATE, "_wall");
    var openWallGateModel = ModelLocationUtils.getModelLocation(Blocks.OAK_FENCE_GATE, "_wall_open");

    var multiPartGenerator = MultiPartGenerator.multiPart(block)
        .with(
            Condition.condition().term(OutfittedTrackBlock.SHAPE, RailShape.NORTH_SOUTH),
            Variant.variant().with(VariantProperties.MODEL, trackModels.flatModel())
        )
        .with(
            Condition.condition().term(OutfittedTrackBlock.SHAPE, RailShape.EAST_WEST),
            Variant.variant().with(VariantProperties.MODEL, trackModels.flatModel())
                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90)
        )
        .with(
            Condition.and(
                Condition.condition().term(GatedTrackBlock.ONE_WAY, true),
                Condition.condition().term(OutfittedTrackBlock.SHAPE, RailShape.NORTH_SOUTH),
                Condition.condition().term(ReversibleOutfittedTrackBlock.REVERSED, false)
            ),
            Variant.variant().with(VariantProperties.MODEL, controlTrackModels.flatModel())
        )
        .with(
            Condition.and(
                Condition.condition().term(GatedTrackBlock.ONE_WAY, true),
                Condition.condition().term(OutfittedTrackBlock.SHAPE, RailShape.NORTH_SOUTH),
                Condition.condition().term(ReversibleOutfittedTrackBlock.REVERSED, true)
            ),
            Variant.variant().with(VariantProperties.MODEL, controlTrackModels.flatModel())
                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180)
        )
        .with(
            Condition.and(
                Condition.condition().term(GatedTrackBlock.ONE_WAY, true),
                Condition.condition().term(OutfittedTrackBlock.SHAPE, RailShape.EAST_WEST),
                Condition.condition().term(ReversibleOutfittedTrackBlock.REVERSED, false)
            ),
            Variant.variant().with(VariantProperties.MODEL, controlTrackModels.flatModel())
                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90)
        )
        .with(
            Condition.and(
                Condition.condition().term(GatedTrackBlock.ONE_WAY, true),
                Condition.condition().term(OutfittedTrackBlock.SHAPE, RailShape.EAST_WEST),
                Condition.condition().term(ReversibleOutfittedTrackBlock.REVERSED, true)
            ),
            Variant.variant().with(VariantProperties.MODEL, controlTrackModels.flatModel())
                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270)
        );

    this.addGateVariants(multiPartGenerator, false, false, closedGateModel);
    this.addGateVariants(multiPartGenerator, true, false, openGateModel);
    this.addGateVariants(multiPartGenerator, false, true, closedWallGateModel);
    this.addGateVariants(multiPartGenerator, true, true, openWallGateModel);

    blockModels.blockStateOutput.accept(multiPartGenerator);
    blockModels.registerSimpleFlatItemModel(block.asItem());
  }

  private void addGateVariants(MultiPartGenerator multiPartGenerator, boolean open, boolean inWall,
      ResourceLocation model) {
    multiPartGenerator
        .with(
            Condition.and(
                Condition.condition().term(OutfittedTrackBlock.SHAPE, RailShape.NORTH_SOUTH),
                Condition.condition().term(ReversibleOutfittedTrackBlock.REVERSED, false),
                Condition.condition().term(GatedTrackBlock.OPEN, open),
                Condition.condition().term(GatedTrackBlock.IN_WALL, inWall)
            ),
            Variant.variant().with(VariantProperties.MODEL, model)
                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180)
        )
        .with(
            Condition.and(
                Condition.condition().term(OutfittedTrackBlock.SHAPE, RailShape.NORTH_SOUTH),
                Condition.condition().term(ReversibleOutfittedTrackBlock.REVERSED, true),
                Condition.condition().term(GatedTrackBlock.OPEN, open),
                Condition.condition().term(GatedTrackBlock.IN_WALL, inWall)
            ),
            Variant.variant().with(VariantProperties.MODEL, model)
        )
        .with(
            Condition.and(
                Condition.condition().term(OutfittedTrackBlock.SHAPE, RailShape.EAST_WEST),
                Condition.condition().term(ReversibleOutfittedTrackBlock.REVERSED, false),
                Condition.condition().term(GatedTrackBlock.OPEN, open),
                Condition.condition().term(GatedTrackBlock.IN_WALL, inWall)
            ),
            Variant.variant().with(VariantProperties.MODEL, model)
                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270)
        )
        .with(
            Condition.and(
                Condition.condition().term(OutfittedTrackBlock.SHAPE, RailShape.EAST_WEST),
                Condition.condition().term(ReversibleOutfittedTrackBlock.REVERSED, true),
                Condition.condition().term(GatedTrackBlock.OPEN, open),
                Condition.condition().term(GatedTrackBlock.IN_WALL, inWall)
            ),
            Variant.variant().with(VariantProperties.MODEL, model)
                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90)
        );
  }

  private void createCouplerTrack(BlockModelGenerators blockModels,
      CouplerTrackBlock block, StraightTrackModelSet trackModels,
      StraightTrackModelSet couplerTrackCoupler, StraightTrackModelSet activeCouplerTrackCoupler,
      StraightTrackModelSet couplerTrackDecoupler,
      StraightTrackModelSet activeCouplerTrackDecoupler,
      StraightTrackModelSet couplerTrackAutoCoupler,
      StraightTrackModelSet activeCouplerTrackAutoCoupler) {
    var multiPartGenerator = MultiPartGenerator.multiPart(block);
    trackModels.apply(multiPartGenerator, CouplerTrackBlock.SHAPE, true, false);

    couplerTrackCoupler.apply(multiPartGenerator, CouplerTrackBlock.SHAPE, true, false,
        Condition.condition()
            .term(CouplerTrackBlock.MODE, CouplerTrackBlockEntity.Mode.COUPLER)
            .term(CouplerTrackBlock.POWERED, false));
    activeCouplerTrackCoupler.apply(multiPartGenerator, CouplerTrackBlock.SHAPE, true, false,
        Condition.condition()
            .term(CouplerTrackBlock.MODE, CouplerTrackBlockEntity.Mode.COUPLER)
            .term(CouplerTrackBlock.POWERED, true));
    couplerTrackDecoupler.apply(multiPartGenerator, CouplerTrackBlock.SHAPE, true, false,
        Condition.condition()
            .term(CouplerTrackBlock.MODE, CouplerTrackBlockEntity.Mode.DECOUPLER)
            .term(CouplerTrackBlock.POWERED, false));
    activeCouplerTrackDecoupler.apply(multiPartGenerator, CouplerTrackBlock.SHAPE, true, false,
        Condition.condition()
            .term(CouplerTrackBlock.MODE, CouplerTrackBlockEntity.Mode.DECOUPLER)
            .term(CouplerTrackBlock.POWERED, true));
    couplerTrackAutoCoupler.apply(multiPartGenerator, CouplerTrackBlock.SHAPE, true, false,
        Condition.condition()
            .term(CouplerTrackBlock.MODE, CouplerTrackBlockEntity.Mode.AUTO_COUPLER)
            .term(CouplerTrackBlock.POWERED, false));
    activeCouplerTrackAutoCoupler.apply(multiPartGenerator, CouplerTrackBlock.SHAPE, true, false,
        Condition.condition()
            .term(CouplerTrackBlock.MODE, CouplerTrackBlockEntity.Mode.AUTO_COUPLER)
            .term(CouplerTrackBlock.POWERED, true));

    blockModels.blockStateOutput.accept(multiPartGenerator);
    blockModels.registerSimpleFlatItemModel(block.asItem());
  }

  private void createDisembarkingTrack(BlockModelGenerators blockModels,
      DisembarkingTrackBlock block, StraightTrackModelSet trackModels,
      StraightTrackModelSet disembarkingTrackLeft, StraightTrackModelSet activeDisembarkingTrackLeft,
      StraightTrackModelSet disembarkingTrackRight, StraightTrackModelSet activeDisembarkingTrackRight) {
    var multiPartGenerator = MultiPartGenerator.multiPart(block);
    trackModels.apply(multiPartGenerator, CouplerTrackBlock.SHAPE, true, false);

    disembarkingTrackLeft.apply(multiPartGenerator, CouplerTrackBlock.SHAPE, true, false,
        Condition.condition()
            .term(DisembarkingTrackBlock.MIRRORED, false)
            .term(PoweredOutfittedTrackBlock.POWERED, false));
    activeDisembarkingTrackLeft.apply(multiPartGenerator, CouplerTrackBlock.SHAPE, true, false,
        Condition.condition()
            .term(DisembarkingTrackBlock.MIRRORED, false)
            .term(PoweredOutfittedTrackBlock.POWERED, true));
    disembarkingTrackRight.apply(multiPartGenerator, CouplerTrackBlock.SHAPE, true, false,
        Condition.condition()
            .term(DisembarkingTrackBlock.MIRRORED, true)
            .term(PoweredOutfittedTrackBlock.POWERED, false));
    activeDisembarkingTrackRight.apply(multiPartGenerator, CouplerTrackBlock.SHAPE, true, false,
        Condition.condition()
            .term(DisembarkingTrackBlock.MIRRORED, true)
            .term(PoweredOutfittedTrackBlock.POWERED, true));

    blockModels.blockStateOutput.accept(multiPartGenerator);
    blockModels.registerSimpleFlatItemModel(block.asItem());
  }

  private void createLockingTrack(BlockModelGenerators blockModels, LockingTrackBlock block,
      ResourceLocation trackModel) {
    var lockdownModel =
        RailcraftModelTemplates.LOCKING_TRACK_PROVIDER.LOCKDOWN.getModel();
    var trainLockdownModel =
        RailcraftModelTemplates.LOCKING_TRACK_PROVIDER.TRAIN_LOCKDOWN.getModel();
    var holdingModel =
        RailcraftModelTemplates.LOCKING_TRACK_PROVIDER.HOLDING.getModel();
    var trainHoldingModel =
        RailcraftModelTemplates.LOCKING_TRACK_PROVIDER.TRAIN_HOLDING.getModel();
    var boardingModel =
        RailcraftModelTemplates.LOCKING_TRACK_PROVIDER.BOARDING.getModel();
    var boardingReversedModel =
        RailcraftModelTemplates.LOCKING_TRACK_PROVIDER.BOARDING_REVERSED.getModel();
    var trainBoardingModel =
        RailcraftModelTemplates.LOCKING_TRACK_PROVIDER.TRAIN_BOARDING.getModel();
    var trainBoardingReversedModel =
        RailcraftModelTemplates.LOCKING_TRACK_PROVIDER.TRAIN_BOARDING_REVERSED.getModel();

    var activeLockdownModel =
        RailcraftModelTemplates.LOCKING_TRACK_PROVIDER.LOCKDOWN_ON.getModel();
    var activeTrainLockdownModel =
        RailcraftModelTemplates.LOCKING_TRACK_PROVIDER.TRAIN_LOCKDOWN_ON.getModel();
    var activeHoldingModel =
        RailcraftModelTemplates.LOCKING_TRACK_PROVIDER.HOLDING_ON.getModel();
    var activeTrainHoldingModel =
        RailcraftModelTemplates.LOCKING_TRACK_PROVIDER.TRAIN_HOLDING_ON.getModel();
    var activeBoardingModel =
        RailcraftModelTemplates.LOCKING_TRACK_PROVIDER.BOARDING_ON.getModel();
    var activeBoardingReversedModel =
        RailcraftModelTemplates.LOCKING_TRACK_PROVIDER.BOARDING_REVERSED_ON.getModel();
    var activeTrainBoardingModel =
        RailcraftModelTemplates.LOCKING_TRACK_PROVIDER.TRAIN_BOARDING_ON.getModel();
    var activeTrainBoardingReversedModel =
        RailcraftModelTemplates.LOCKING_TRACK_PROVIDER.TRAIN_BOARDING_REVERSED_ON.getModel();

    var multiPartGenerator = MultiPartGenerator.multiPart(block)
        .with(
            Condition.condition().term(LockingTrackBlock.SHAPE, RailShape.NORTH_SOUTH),
            Variant.variant().with(VariantProperties.MODEL, trackModel)
        )
        .with(
            Condition.condition().term(LockingTrackBlock.SHAPE, RailShape.EAST_WEST),
            Variant.variant().with(VariantProperties.MODEL, trackModel)
                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90)
        );

    this.addLockingMode(LockingMode.LOCKDOWN, lockdownModel, activeLockdownModel, multiPartGenerator);
    this.addLockingMode(LockingMode.TRAIN_LOCKDOWN, trainLockdownModel, activeTrainLockdownModel,
        multiPartGenerator);
    this.addLockingMode(LockingMode.HOLDING, holdingModel, activeHoldingModel, multiPartGenerator);
    this.addLockingMode(LockingMode.TRAIN_HOLDING, trainHoldingModel, activeTrainHoldingModel,
        multiPartGenerator);
    this.addLockingMode(LockingMode.BOARDING, boardingModel, activeBoardingModel, multiPartGenerator);
    this.addLockingMode(LockingMode.BOARDING_REVERSED, boardingReversedModel,
        activeBoardingReversedModel, multiPartGenerator);
    this.addLockingMode(LockingMode.TRAIN_BOARDING, trainBoardingModel, activeTrainBoardingModel,
        multiPartGenerator);
    this.addLockingMode(LockingMode.TRAIN_BOARDING_REVERSED, trainBoardingReversedModel,
        activeTrainBoardingReversedModel, multiPartGenerator);

    blockModels.blockStateOutput.accept(multiPartGenerator);
    blockModels.registerSimpleFlatItemModel(block.asItem());
  }

  private void addLockingMode(LockingMode lockingMode, ResourceLocation model,
      ResourceLocation poweredModel, MultiPartGenerator multiPartGenerator) {
    multiPartGenerator
        .with(
            Condition.and(
                Condition.condition().term(LockingTrackBlock.LOCKING_MODE, lockingMode),
                Condition.condition().term(LockingTrackBlock.POWERED, false),
                Condition.condition().term(LockingTrackBlock.SHAPE, RailShape.NORTH_SOUTH)
            ),
            Variant.variant().with(VariantProperties.MODEL, model)
        )
        .with(
            Condition.and(
                Condition.condition().term(LockingTrackBlock.LOCKING_MODE, lockingMode),
                Condition.condition().term(LockingTrackBlock.POWERED, true),
                Condition.condition().term(LockingTrackBlock.SHAPE, RailShape.NORTH_SOUTH)
            ),
            Variant.variant().with(VariantProperties.MODEL, poweredModel)
        )
        .with(
            Condition.and(
                Condition.condition().term(LockingTrackBlock.LOCKING_MODE, lockingMode),
                Condition.condition().term(LockingTrackBlock.POWERED, false),
                Condition.condition().term(LockingTrackBlock.SHAPE, RailShape.EAST_WEST)
            ),
            Variant.variant().with(VariantProperties.MODEL, model)
                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90)
        )
        .with(
            Condition.and(
                Condition.condition().term(LockingTrackBlock.LOCKING_MODE, lockingMode),
                Condition.condition().term(LockingTrackBlock.POWERED, true),
                Condition.condition().term(LockingTrackBlock.SHAPE, RailShape.EAST_WEST)
            ),
            Variant.variant().with(VariantProperties.MODEL, poweredModel)
                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90)
        );
  }

  private void createThrottleTrack(BlockModelGenerators blockModels, ThrottleTrackBlock block,
      ResourceLocation trackModel) {

    var speed1 =
        RailcraftModelTemplates.THROTTLE_TRACK_PROVIDER.THROTTLE_TRACK_1.getModel(false, false);
    var speed2 =
        RailcraftModelTemplates.THROTTLE_TRACK_PROVIDER.THROTTLE_TRACK_2.getModel(false, false);
    var speed3 =
        RailcraftModelTemplates.THROTTLE_TRACK_PROVIDER.THROTTLE_TRACK_3.getModel(false, false);
    var speed4 =
        RailcraftModelTemplates.THROTTLE_TRACK_PROVIDER.THROTTLE_TRACK_4.getModel(false, false);

    var speed1Reverse =
        RailcraftModelTemplates.THROTTLE_TRACK_PROVIDER.THROTTLE_TRACK_1.getModel(false, true);
    var speed2Reverse =
        RailcraftModelTemplates.THROTTLE_TRACK_PROVIDER.THROTTLE_TRACK_2.getModel(false, true);
    var speed3Reverse =
        RailcraftModelTemplates.THROTTLE_TRACK_PROVIDER.THROTTLE_TRACK_3.getModel(false, true);
    var speed4Reverse =
        RailcraftModelTemplates.THROTTLE_TRACK_PROVIDER.THROTTLE_TRACK_4.getModel(false, true);

    var activeSpeed1 =
        RailcraftModelTemplates.THROTTLE_TRACK_PROVIDER.THROTTLE_TRACK_1.getModel(true, false);
    var activeSpeed2 =
        RailcraftModelTemplates.THROTTLE_TRACK_PROVIDER.THROTTLE_TRACK_2.getModel(true, false);
    var activeSpeed3 =
        RailcraftModelTemplates.THROTTLE_TRACK_PROVIDER.THROTTLE_TRACK_3.getModel(true, false);
    var activeSpeed4 =
        RailcraftModelTemplates.THROTTLE_TRACK_PROVIDER.THROTTLE_TRACK_4.getModel(true, false);

    var activeSpeed1Reverse =
        RailcraftModelTemplates.THROTTLE_TRACK_PROVIDER.THROTTLE_TRACK_1.getModel(true, true);
    var activeSpeed2Reverse =
        RailcraftModelTemplates.THROTTLE_TRACK_PROVIDER.THROTTLE_TRACK_2.getModel(true, true);
    var activeSpeed3Reverse =
        RailcraftModelTemplates.THROTTLE_TRACK_PROVIDER.THROTTLE_TRACK_3.getModel(true, true);
    var activeSpeed4Reverse =
        RailcraftModelTemplates.THROTTLE_TRACK_PROVIDER.THROTTLE_TRACK_4.getModel(true, true);

    var multiPartGenerator = MultiPartGenerator.multiPart(block)
        .with(
            Condition.condition().term(LockingTrackBlock.SHAPE, RailShape.NORTH_SOUTH),
            Variant.variant().with(VariantProperties.MODEL, trackModel)
        )
        .with(
            Condition.condition().term(LockingTrackBlock.SHAPE, RailShape.EAST_WEST),
            Variant.variant().with(VariantProperties.MODEL, trackModel)
                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90)
        );

    this.addSpeedMode(Locomotive.Speed.SLOWEST, speed1, activeSpeed1, false, multiPartGenerator);
    this.addSpeedMode(Locomotive.Speed.SLOWER, speed2, activeSpeed2, false, multiPartGenerator);
    this.addSpeedMode(Locomotive.Speed.NORMAL, speed3, activeSpeed3, false, multiPartGenerator);
    this.addSpeedMode(Locomotive.Speed.MAX, speed4, activeSpeed4, false, multiPartGenerator);

    this.addSpeedMode(Locomotive.Speed.SLOWEST, speed1Reverse, activeSpeed1Reverse, true, multiPartGenerator);
    this.addSpeedMode(Locomotive.Speed.SLOWER, speed2Reverse, activeSpeed2Reverse, true, multiPartGenerator);
    this.addSpeedMode(Locomotive.Speed.NORMAL, speed3Reverse, activeSpeed3Reverse, true, multiPartGenerator);
    this.addSpeedMode(Locomotive.Speed.MAX, speed4Reverse, activeSpeed4Reverse, true, multiPartGenerator);

    blockModels.blockStateOutput.accept(multiPartGenerator);
    blockModels.registerSimpleFlatItemModel(block.asItem());
  }

  private void addSpeedMode(Locomotive.Speed speed, ResourceLocation model,
      ResourceLocation poweredModel, boolean reverse, MultiPartGenerator multiPartGenerator) {
    multiPartGenerator
        .with(
            Condition.and(
                Condition.condition().term(ThrottleTrackBlock.LOCOMOTIVE_SPEED, speed),
                Condition.condition().term(ThrottleTrackBlock.REVERSE, reverse),
                Condition.condition().term(ThrottleTrackBlock.POWERED, false),
                Condition.condition().term(ThrottleTrackBlock.SHAPE, RailShape.NORTH_SOUTH)
            ),
            Variant.variant().with(VariantProperties.MODEL, model)
        )
        .with(
            Condition.and(
                Condition.condition().term(ThrottleTrackBlock.LOCOMOTIVE_SPEED, speed),
                Condition.condition().term(ThrottleTrackBlock.REVERSE, reverse),
                Condition.condition().term(ThrottleTrackBlock.POWERED, true),
                Condition.condition().term(ThrottleTrackBlock.SHAPE, RailShape.NORTH_SOUTH)
            ),
            Variant.variant().with(VariantProperties.MODEL, poweredModel)
        )
        .with(
            Condition.and(
                Condition.condition().term(ThrottleTrackBlock.LOCOMOTIVE_SPEED, speed),
                Condition.condition().term(ThrottleTrackBlock.REVERSE, reverse),
                Condition.condition().term(ThrottleTrackBlock.POWERED, false),
                Condition.condition().term(ThrottleTrackBlock.SHAPE, RailShape.EAST_WEST)
            ),
            Variant.variant().with(VariantProperties.MODEL, model)
                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90)
        )
        .with(
            Condition.and(
                Condition.condition().term(ThrottleTrackBlock.LOCOMOTIVE_SPEED, speed),
                Condition.condition().term(ThrottleTrackBlock.REVERSE, reverse),
                Condition.condition().term(ThrottleTrackBlock.POWERED, true),
                Condition.condition().term(ThrottleTrackBlock.SHAPE, RailShape.EAST_WEST)
            ),
            Variant.variant().with(VariantProperties.MODEL, poweredModel)
                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90)
        );
  }

  private void createBufferStopTrack(BlockModelGenerators blockModels, BufferStopTrackBlock block,
      ResourceLocation trackModel) {
    var bufferStop = modLocation("block/buffer_stop");

    blockModels.blockStateOutput.accept(
        MultiPartGenerator.multiPart(block)
            .with(
                Condition.and(
                    Condition.condition().term(OutfittedTrackBlock.SHAPE, RailShape.NORTH_SOUTH),
                    Condition.condition().term(ReversibleOutfittedTrackBlock.REVERSED, false)
                ),
                Variant.variant().with(VariantProperties.MODEL, bufferStop))
            .with(
                Condition.and(
                    Condition.condition().term(OutfittedTrackBlock.SHAPE, RailShape.NORTH_SOUTH),
                    Condition.condition().term(ReversibleOutfittedTrackBlock.REVERSED, true)
                ),
                Variant.variant().with(VariantProperties.MODEL, bufferStop)
                    .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180))
            .with(
                Condition.and(
                    Condition.condition().term(OutfittedTrackBlock.SHAPE, RailShape.EAST_WEST),
                    Condition.condition().term(ReversibleOutfittedTrackBlock.REVERSED, false)
                ),
                Variant.variant().with(VariantProperties.MODEL, bufferStop)
                    .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
            .with(
                Condition.and(
                    Condition.condition().term(OutfittedTrackBlock.SHAPE, RailShape.EAST_WEST),
                    Condition.condition().term(ReversibleOutfittedTrackBlock.REVERSED, true)
                ),
                Variant.variant().with(VariantProperties.MODEL, bufferStop)
                    .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270))
            .with(
                Condition.condition().term(OutfittedTrackBlock.SHAPE, RailShape.NORTH_SOUTH),
                Variant.variant().with(VariantProperties.MODEL, trackModel))
            .with(
                Condition.condition().term(OutfittedTrackBlock.SHAPE, RailShape.EAST_WEST),
                Variant.variant().with(VariantProperties.MODEL, trackModel)
                    .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
    );
    blockModels.registerSimpleFlatItemModel(block.asItem());
  }

  private void createTurnoutTrack(BlockModelGenerators blockModels, TurnoutTrackBlock block) {
    var northTexture = TextureMapping.getBlockTexture(block, "_north");
    var northSwitchedTexture = TextureMapping.getBlockTexture(block, "_north_switched");
    var southTexture = TextureMapping.getBlockTexture(block, "_south");
    var southSwitchedTexture = TextureMapping.getBlockTexture(block, "_south_switched");

    var northModel = ModelTemplates.RAIL_FLAT
        .extend().renderType(CUTOUT).build()
        .createWithSuffix(block, "_north",
            TextureMapping.singleSlot(TextureSlot.RAIL, northTexture), blockModels.modelOutput);
    var northSwitchedModel = ModelTemplates.RAIL_FLAT
        .extend().renderType(CUTOUT).build()
        .createWithSuffix(block, "_north_switched",
            TextureMapping.singleSlot(TextureSlot.RAIL, northSwitchedTexture), blockModels.modelOutput);
    var southModel = ModelTemplates.RAIL_FLAT
        .extend().renderType(CUTOUT).build()
        .createWithSuffix(block, "_south",
            TextureMapping.singleSlot(TextureSlot.RAIL, southTexture), blockModels.modelOutput);
    var southSwitchedModel = ModelTemplates.RAIL_FLAT
        .extend().renderType(CUTOUT).build()
        .createWithSuffix(block, "_south_switched",
            TextureMapping.singleSlot(TextureSlot.RAIL, southSwitchedTexture), blockModels.modelOutput);

    blockModels.blockStateOutput.accept(
        MultiVariantGenerator.multiVariant(block)
            .with(
                PropertyDispatch.properties(SwitchTrackBlock.SHAPE,
                        ReversibleOutfittedTrackBlock.REVERSED,
                        TurnoutTrackBlock.MIRRORED, SwitchTrackBlock.SWITCHED)
                    .generate((shape, reversed, mirrored, switched) -> {
                      if (shape == RailShape.NORTH_SOUTH) {
                        if (!reversed && !mirrored && !switched) {
                          return Variant.variant().with(VariantProperties.MODEL, northModel);
                        } else if (!reversed && !mirrored && switched) {
                          return Variant.variant().with(VariantProperties.MODEL, northSwitchedModel);
                        } else if (reversed && !mirrored && !switched) {
                          return Variant.variant().with(VariantProperties.MODEL, northModel)
                              .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180);
                        } else if (reversed && !mirrored && switched) {
                          return Variant.variant().with(VariantProperties.MODEL, northSwitchedModel)
                              .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180);
                        } else if (!reversed && mirrored && !switched) {
                          return Variant.variant().with(VariantProperties.MODEL, southModel)
                              .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180);
                        } else if (!reversed && mirrored && switched) {
                          return Variant.variant().with(VariantProperties.MODEL, southSwitchedModel)
                              .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180);
                        } else if (reversed && mirrored && !switched) {
                          return Variant.variant().with(VariantProperties.MODEL, southModel);
                        } else {
                          return Variant.variant().with(VariantProperties.MODEL, southSwitchedModel);
                        }
                      } else if (shape == RailShape.EAST_WEST) {
                        if (!reversed && !mirrored && !switched) {
                          return Variant.variant().with(VariantProperties.MODEL, northModel)
                              .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90);
                        } else if (!reversed && !mirrored && switched) {
                          return Variant.variant().with(VariantProperties.MODEL, northSwitchedModel)
                              .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90);
                        } else if (reversed && !mirrored && !switched) {
                          return Variant.variant().with(VariantProperties.MODEL, northModel)
                              .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270);
                        } else if (reversed && !mirrored && switched) {
                          return Variant.variant().with(VariantProperties.MODEL, northSwitchedModel)
                              .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270);
                        } else if (!reversed && mirrored && !switched) {
                          return Variant.variant().with(VariantProperties.MODEL, southModel)
                              .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270);
                        } else if (!reversed && mirrored && switched) {
                          return Variant.variant().with(VariantProperties.MODEL, southSwitchedModel)
                              .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270);
                        } else if (reversed && mirrored && !switched) {
                          return Variant.variant().with(VariantProperties.MODEL, southModel)
                              .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90);
                        } else {
                          return Variant.variant().with(VariantProperties.MODEL, southSwitchedModel)
                              .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90);
                        }
                      }
                      return Variant.variant();
                    })
            )
    );
    blockModels.registerSimpleFlatItemModel(block, "_north");
  }

  private void createWyeTrack(BlockModelGenerators blockModels, WyeTrackBlock block) {
    var eastTexture = TextureMapping.getBlockTexture(block, "_east");
    var eastSwitchedTexture = TextureMapping.getBlockTexture(block, "_east_switched");
    var westTexture = TextureMapping.getBlockTexture(block, "_west");
    var westSwitchedTexture = TextureMapping.getBlockTexture(block, "_west_switched");

    var eastModel = ModelTemplates.RAIL_FLAT
        .extend().renderType(CUTOUT).build()
        .createWithSuffix(block, "_east",
            TextureMapping.singleSlot(TextureSlot.RAIL, eastTexture), blockModels.modelOutput);
    var eastSwitchedModel = ModelTemplates.RAIL_FLAT
        .extend().renderType(CUTOUT).build()
        .createWithSuffix(block, "_east_switched",
            TextureMapping.singleSlot(TextureSlot.RAIL, eastSwitchedTexture), blockModels.modelOutput);
    var westModel = ModelTemplates.RAIL_FLAT
        .extend().renderType(CUTOUT).build()
        .createWithSuffix(block, "_west",
            TextureMapping.singleSlot(TextureSlot.RAIL, westTexture), blockModels.modelOutput);
    var westSwitchedModel = ModelTemplates.RAIL_FLAT
        .extend().renderType(CUTOUT).build()
        .createWithSuffix(block, "_west_switched",
            TextureMapping.singleSlot(TextureSlot.RAIL, westSwitchedTexture), blockModels.modelOutput);

    blockModels.blockStateOutput.accept(
        MultiVariantGenerator.multiVariant(block)
            .with(
                PropertyDispatch.properties(SwitchTrackBlock.SHAPE,
                        ReversibleOutfittedTrackBlock.REVERSED, SwitchTrackBlock.SWITCHED)
                    .generate((shape, reversed, switched) -> {
                      var facing = ReversibleOutfittedTrackBlock.getDirection(shape, reversed);
                      return switch (facing) {
                        case NORTH -> Variant.variant()
                            .with(VariantProperties.MODEL, switched ? eastSwitchedModel : eastModel)
                            .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90);
                        case SOUTH -> Variant.variant()
                            .with(VariantProperties.MODEL, switched ? westSwitchedModel : westModel)
                            .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90);
                        case EAST -> Variant.variant()
                            .with(VariantProperties.MODEL, switched ? westSwitchedModel : westModel);
                        case WEST -> Variant.variant()
                            .with(VariantProperties.MODEL, switched ? eastSwitchedModel :
                                eastModel);
                        default -> throw new UnsupportedOperationException();
                      };
                    })
            )
    );
    blockModels.registerSimpleFlatItemModel(block, "_east");
  }

  private void createJunctionTrack(BlockModelGenerators blockModels, JunctionTrackBlock block) {
    var model = ModelTemplates.RAIL_FLAT
        .extend().renderType(CUTOUT).build()
        .create(block, TextureMapping.rail(block), blockModels.modelOutput);

    blockModels.blockStateOutput.accept(BlockModelGenerators.createSimpleBlock(block, model));
    blockModels.registerSimpleFlatItemModel(block);
  }

  private record StraightTrackModelSet(
      ResourceLocation flatModel,
      ResourceLocation raisedNorthEastModel,
      ResourceLocation raisedSouthWestModel) {

    private Condition merge(Condition base, Condition... conditions) {
      if (conditions.length == 0) {
        return base;
      }
      var all = new ArrayList<Condition>();
      all.add(base);
      Collections.addAll(all, conditions);
      return Condition.and(all.toArray(Condition[]::new));
    }

    private void apply(MultiPartGenerator builder, Property<RailShape> shapeProperty,
        boolean includeRaised, boolean reversed, Condition... conditions) {
      builder
          .with(
              merge(
                  Condition.condition().term(shapeProperty, RailShape.NORTH_SOUTH),
                  conditions
              ),
              Variant.variant().with(VariantProperties.MODEL, this.flatModel)
                  .with(VariantProperties.Y_ROT, reversed ?
                      VariantProperties.Rotation.R180 : VariantProperties.Rotation.R0)
          )
          .with(
              merge(
                  Condition.condition().term(shapeProperty, RailShape.EAST_WEST),
                  conditions
              ),
              Variant.variant().with(VariantProperties.MODEL, this.flatModel)
                  .with(VariantProperties.Y_ROT, reversed ?
                      VariantProperties.Rotation.R270 : VariantProperties.Rotation.R90)
          );

      if (includeRaised) {
        builder
            .with(
                merge(
                    Condition.condition().term(shapeProperty, RailShape.ASCENDING_NORTH),
                    conditions
                ),
                Variant.variant().with(VariantProperties.MODEL, reversed ?
                        this.raisedSouthWestModel : this.raisedNorthEastModel)
                    .with(VariantProperties.Y_ROT, reversed ?
                        VariantProperties.Rotation.R180 : VariantProperties.Rotation.R0)
            )
            .with(
                merge(
                    Condition.condition().term(shapeProperty, RailShape.ASCENDING_SOUTH),
                    conditions
                ),
                Variant.variant().with(VariantProperties.MODEL, reversed ?
                        this.raisedNorthEastModel : this.raisedSouthWestModel)
                    .with(VariantProperties.Y_ROT, reversed ?
                        VariantProperties.Rotation.R180 : VariantProperties.Rotation.R0)
            )
            .with(
                merge(
                    Condition.condition().term(shapeProperty, RailShape.ASCENDING_EAST),
                    conditions
                ),
                Variant.variant().with(VariantProperties.MODEL, reversed ?
                        this.raisedSouthWestModel : this.raisedNorthEastModel)
                    .with(VariantProperties.Y_ROT, reversed ?
                        VariantProperties.Rotation.R270 : VariantProperties.Rotation.R90)
            )
            .with(
                merge(
                    Condition.condition().term(shapeProperty, RailShape.ASCENDING_WEST),
                    conditions
                ),
                Variant.variant().with(VariantProperties.MODEL, reversed ?
                        this.raisedNorthEastModel : this.raisedSouthWestModel)
                    .with(VariantProperties.Y_ROT, reversed ?
                        VariantProperties.Rotation.R270 : VariantProperties.Rotation.R90)
            );
      }
    }
  }

  @Override
  protected Stream<? extends Holder<Block>> getKnownBlocks() {
    return Stream.empty();
  }
}
