package mods.railcraft.world.level.block;

import java.util.function.ToIntFunction;
import mods.railcraft.Railcraft;
import mods.railcraft.world.level.block.post.PostBlock;
import mods.railcraft.world.level.block.signal.AnalogSignalControllerBoxBlock;
import mods.railcraft.world.level.block.signal.BlockSignalBlock;
import mods.railcraft.world.level.block.signal.BlockSignalRelayBoxBlock;
import mods.railcraft.world.level.block.signal.DistantSignalBlock;
import mods.railcraft.world.level.block.signal.DualBlockSignalBlock;
import mods.railcraft.world.level.block.signal.DualDistantSignalBlock;
import mods.railcraft.world.level.block.signal.DualSignalBlock;
import mods.railcraft.world.level.block.signal.DualTokenSignalBlock;
import mods.railcraft.world.level.block.signal.SelfAttachableSignalBoxBlock;
import mods.railcraft.world.level.block.signal.SignalBoxBlock;
import mods.railcraft.world.level.block.signal.SignalCapacitorBoxBlock;
import mods.railcraft.world.level.block.signal.SignalControllerBoxBlock;
import mods.railcraft.world.level.block.signal.SignalInterlockBoxBlock;
import mods.railcraft.world.level.block.signal.SignalReceiverBoxBlock;
import mods.railcraft.world.level.block.signal.SingleSignalBlock;
import mods.railcraft.world.level.block.signal.TokenSignalBlock;
import mods.railcraft.world.level.block.track.AbandonedTrackBlock;
import mods.railcraft.world.level.block.track.ElevatorTrackBlock;
import mods.railcraft.world.level.block.track.ForceTrackBlock;
import mods.railcraft.world.level.block.track.TrackBlock;
import mods.railcraft.world.level.block.track.TrackConstants;
import mods.railcraft.world.level.block.track.TrackTypes;
import mods.railcraft.world.level.block.track.actuator.SwitchTrackActuatorBlock;
import mods.railcraft.world.level.block.track.actuator.SwitchTrackLeverBlock;
import mods.railcraft.world.level.block.track.actuator.SwitchTrackMotorBlock;
import mods.railcraft.world.level.block.track.outfitted.ActivatorTrackBlock;
import mods.railcraft.world.level.block.track.outfitted.BoosterTrackBlock;
import mods.railcraft.world.level.block.track.outfitted.BufferStopTrackBlock;
import mods.railcraft.world.level.block.track.outfitted.ControlTrackBlock;
import mods.railcraft.world.level.block.track.outfitted.CouplerTrackBlock;
import mods.railcraft.world.level.block.track.outfitted.DetectorTrackBlock;
import mods.railcraft.world.level.block.track.outfitted.DisembarkingTrackBlock;
import mods.railcraft.world.level.block.track.outfitted.EmbarkingTrackBlock;
import mods.railcraft.world.level.block.track.outfitted.GatedTrackBlock;
import mods.railcraft.world.level.block.track.outfitted.JunctionTrackBlock;
import mods.railcraft.world.level.block.track.outfitted.LauncherTrackBlock;
import mods.railcraft.world.level.block.track.outfitted.LockingTrackBlock;
import mods.railcraft.world.level.block.track.outfitted.TransitionTrackBlock;
import mods.railcraft.world.level.block.track.outfitted.TurnoutTrackBlock;
import mods.railcraft.world.level.block.track.outfitted.WyeTrackBlock;
import mods.railcraft.world.level.material.RailcraftMaterials;
import mods.railcraft.world.level.material.fluid.RailcraftFluids;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.AnvilBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LightBlock;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.OreBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class RailcraftBlocks {

  public static final DeferredRegister<Block> BLOCKS =
      DeferredRegister.create(ForgeRegistries.BLOCKS, Railcraft.ID);


  public static final RegistryObject<Block> WHITE_STRENGTHENED_GLASS =
      BLOCKS.register("white_strengthened_glass",
          () -> new StrengthenedGlassBlock(BlockBehaviour.Properties.of(Material.GLASS)
              .sound(SoundType.GLASS)
              .noOcclusion()
              .strength(1.0F, 5.0F)
              .isValidSpawn(RailcraftBlocks::never)
              .isRedstoneConductor(RailcraftBlocks::never)
              .isSuffocating(RailcraftBlocks::never)
              .isViewBlocking(RailcraftBlocks::never)));

  public static final RegistryObject<Block> ORANGE_STRENGTHENED_GLASS =
      BLOCKS.register("orange_strengthened_glass",
          () -> new StrengthenedGlassBlock(BlockBehaviour.Properties.of(Material.GLASS)
              .sound(SoundType.GLASS)
              .noOcclusion()
              .strength(1.0F, 5.0F)
              .isValidSpawn(RailcraftBlocks::never)
              .isRedstoneConductor(RailcraftBlocks::never)
              .isSuffocating(RailcraftBlocks::never)
              .isViewBlocking(RailcraftBlocks::never)));

  public static final RegistryObject<Block> MAGENTA_STRENGTHENED_GLASS =
      BLOCKS.register("magenta_strengthened_glass",
          () -> new StrengthenedGlassBlock(BlockBehaviour.Properties.of(Material.GLASS)
              .sound(SoundType.GLASS)
              .noOcclusion()
              .strength(1.0F, 5.0F)
              .isValidSpawn(RailcraftBlocks::never)
              .isRedstoneConductor(RailcraftBlocks::never)
              .isSuffocating(RailcraftBlocks::never)
              .isViewBlocking(RailcraftBlocks::never)));

  public static final RegistryObject<Block> LIGHT_BLUE_STRENGTHENED_GLASS =
      BLOCKS.register("light_blue_strengthened_glass",
          () -> new StrengthenedGlassBlock(BlockBehaviour.Properties.of(Material.GLASS)
              .sound(SoundType.GLASS)
              .noOcclusion()
              .strength(1.0F, 5.0F)
              .isValidSpawn(RailcraftBlocks::never)
              .isRedstoneConductor(RailcraftBlocks::never)
              .isSuffocating(RailcraftBlocks::never)
              .isViewBlocking(RailcraftBlocks::never)));

  public static final RegistryObject<Block> YELLOW_STRENGTHENED_GLASS =
      BLOCKS.register("yellow_strengthened_glass",
          () -> new StrengthenedGlassBlock(BlockBehaviour.Properties.of(Material.GLASS)
              .sound(SoundType.GLASS)
              .noOcclusion()
              .strength(1.0F, 5.0F)
              .isValidSpawn(RailcraftBlocks::never)
              .isRedstoneConductor(RailcraftBlocks::never)
              .isSuffocating(RailcraftBlocks::never)
              .isViewBlocking(RailcraftBlocks::never)));

  public static final RegistryObject<Block> LIME_STRENGTHENED_GLASS =
      BLOCKS.register("lime_strengthened_glass",
          () -> new StrengthenedGlassBlock(BlockBehaviour.Properties.of(Material.GLASS)
              .sound(SoundType.GLASS)
              .noOcclusion()
              .strength(1.0F, 5.0F)
              .isValidSpawn(RailcraftBlocks::never)
              .isRedstoneConductor(RailcraftBlocks::never)
              .isSuffocating(RailcraftBlocks::never)
              .isViewBlocking(RailcraftBlocks::never)));

  public static final RegistryObject<Block> PINK_STRENGTHENED_GLASS =
      BLOCKS.register("pink_strengthened_glass",
          () -> new StrengthenedGlassBlock(BlockBehaviour.Properties.of(Material.GLASS)
              .sound(SoundType.GLASS)
              .noOcclusion()
              .strength(1.0F, 5.0F)
              .isValidSpawn(RailcraftBlocks::never)
              .isRedstoneConductor(RailcraftBlocks::never)
              .isSuffocating(RailcraftBlocks::never)
              .isViewBlocking(RailcraftBlocks::never)));

  public static final RegistryObject<Block> GRAY_STRENGTHENED_GLASS =
      BLOCKS.register("gray_strengthened_glass",
          () -> new StrengthenedGlassBlock(BlockBehaviour.Properties.of(Material.GLASS)
              .sound(SoundType.GLASS)
              .noOcclusion()
              .strength(1.0F, 5.0F)
              .isValidSpawn(RailcraftBlocks::never)
              .isRedstoneConductor(RailcraftBlocks::never)
              .isSuffocating(RailcraftBlocks::never)
              .isViewBlocking(RailcraftBlocks::never)));

  public static final RegistryObject<Block> LIGHT_GRAY_STRENGTHENED_GLASS =
      BLOCKS.register("light_gray_strengthened_glass",
          () -> new StrengthenedGlassBlock(BlockBehaviour.Properties.of(Material.GLASS)
              .sound(SoundType.GLASS)
              .noOcclusion()
              .strength(1.0F, 5.0F)
              .isValidSpawn(RailcraftBlocks::never)
              .isRedstoneConductor(RailcraftBlocks::never)
              .isSuffocating(RailcraftBlocks::never)
              .isViewBlocking(RailcraftBlocks::never)));

  public static final RegistryObject<Block> CYAN_STRENGTHENED_GLASS =
      BLOCKS.register("cyan_strengthened_glass",
          () -> new StrengthenedGlassBlock(BlockBehaviour.Properties.of(Material.GLASS)
              .sound(SoundType.GLASS)
              .noOcclusion()
              .strength(1.0F, 5.0F)
              .isValidSpawn(RailcraftBlocks::never)
              .isRedstoneConductor(RailcraftBlocks::never)
              .isSuffocating(RailcraftBlocks::never)
              .isViewBlocking(RailcraftBlocks::never)));

  public static final RegistryObject<Block> PURPLE_STRENGTHENED_GLASS =
      BLOCKS.register("purple_strengthened_glass",
          () -> new StrengthenedGlassBlock(BlockBehaviour.Properties.of(Material.GLASS)
              .sound(SoundType.GLASS)
              .noOcclusion()
              .strength(1.0F, 5.0F)
              .isValidSpawn(RailcraftBlocks::never)
              .isRedstoneConductor(RailcraftBlocks::never)
              .isSuffocating(RailcraftBlocks::never)
              .isViewBlocking(RailcraftBlocks::never)));

  public static final RegistryObject<Block> BLUE_STRENGTHENED_GLASS =
      BLOCKS.register("blue_strengthened_glass",
          () -> new StrengthenedGlassBlock(BlockBehaviour.Properties.of(Material.GLASS)
              .sound(SoundType.GLASS)
              .noOcclusion()
              .strength(1.0F, 5.0F)
              .isValidSpawn(RailcraftBlocks::never)
              .isRedstoneConductor(RailcraftBlocks::never)
              .isSuffocating(RailcraftBlocks::never)
              .isViewBlocking(RailcraftBlocks::never)));

  public static final RegistryObject<Block> BROWN_STRENGTHENED_GLASS =
      BLOCKS.register("brown_strengthened_glass",
          () -> new StrengthenedGlassBlock(BlockBehaviour.Properties.of(Material.GLASS)
              .sound(SoundType.GLASS)
              .noOcclusion()
              .strength(1.0F, 5.0F)
              .isValidSpawn(RailcraftBlocks::never)
              .isRedstoneConductor(RailcraftBlocks::never)
              .isSuffocating(RailcraftBlocks::never)
              .isViewBlocking(RailcraftBlocks::never)));

  public static final RegistryObject<Block> GREEN_STRENGTHENED_GLASS =
      BLOCKS.register("green_strengthened_glass",
          () -> new StrengthenedGlassBlock(BlockBehaviour.Properties.of(Material.GLASS)
              .sound(SoundType.GLASS)
              .noOcclusion()
              .strength(1.0F, 5.0F)
              .isValidSpawn(RailcraftBlocks::never)
              .isRedstoneConductor(RailcraftBlocks::never)
              .isSuffocating(RailcraftBlocks::never)
              .isViewBlocking(RailcraftBlocks::never)));

  public static final RegistryObject<Block> RED_STRENGTHENED_GLASS =
      BLOCKS.register("red_strengthened_glass",
          () -> new StrengthenedGlassBlock(BlockBehaviour.Properties.of(Material.GLASS)
              .sound(SoundType.GLASS)
              .noOcclusion()
              .strength(1.0F, 5.0F)
              .isValidSpawn(RailcraftBlocks::never)
              .isRedstoneConductor(RailcraftBlocks::never)
              .isSuffocating(RailcraftBlocks::never)
              .isViewBlocking(RailcraftBlocks::never)));

  public static final RegistryObject<Block> BLACK_STRENGTHENED_GLASS =
      BLOCKS.register("black_strengthened_glass",
          () -> new StrengthenedGlassBlock(BlockBehaviour.Properties.of(Material.GLASS)
              .sound(SoundType.GLASS)
              .noOcclusion()
              .strength(1.0F, 5.0F)
              .isValidSpawn(RailcraftBlocks::never)
              .isRedstoneConductor(RailcraftBlocks::never)
              .isSuffocating(RailcraftBlocks::never)
              .isViewBlocking(RailcraftBlocks::never)));

  public static final RegistryObject<Block> WHITE_IRON_TANK_GAUGE =
      BLOCKS.register("white_iron_tank_gauge",
          () -> new IronTankGaugeBlock(BlockBehaviour.Properties.of(Material.GLASS)
              .sound(SoundType.GLASS)
              .noOcclusion()
              .strength(1.0F, 5.0F)
              .isValidSpawn(RailcraftBlocks::never)
              .isRedstoneConductor(RailcraftBlocks::never)
              .isSuffocating(RailcraftBlocks::never)
              .isViewBlocking(RailcraftBlocks::never)
              .lightLevel(LightBlock.LIGHT_EMISSION)));

  public static final RegistryObject<Block> ORANGE_IRON_TANK_GAUGE =
      BLOCKS.register("orange_iron_tank_gauge",
          () -> new IronTankGaugeBlock(BlockBehaviour.Properties.of(Material.GLASS)
              .sound(SoundType.GLASS)
              .noOcclusion()
              .strength(1.0F, 5.0F)
              .isValidSpawn(RailcraftBlocks::never)
              .isRedstoneConductor(RailcraftBlocks::never)
              .isSuffocating(RailcraftBlocks::never)
              .isViewBlocking(RailcraftBlocks::never)
              .lightLevel(LightBlock.LIGHT_EMISSION)));

  public static final RegistryObject<Block> MAGENTA_IRON_TANK_GAUGE =
      BLOCKS.register("magenta_iron_tank_gauge",
          () -> new IronTankGaugeBlock(BlockBehaviour.Properties.of(Material.GLASS)
              .sound(SoundType.GLASS)
              .noOcclusion()
              .strength(1.0F, 5.0F)
              .isValidSpawn(RailcraftBlocks::never)
              .isRedstoneConductor(RailcraftBlocks::never)
              .isSuffocating(RailcraftBlocks::never)
              .isViewBlocking(RailcraftBlocks::never)
              .lightLevel(LightBlock.LIGHT_EMISSION)));

  public static final RegistryObject<Block> LIGHT_BLUE_IRON_TANK_GAUGE =
      BLOCKS.register("light_blue_iron_tank_gauge",
          () -> new IronTankGaugeBlock(BlockBehaviour.Properties.of(Material.GLASS)
              .sound(SoundType.GLASS)
              .noOcclusion()
              .strength(1.0F, 5.0F)
              .isValidSpawn(RailcraftBlocks::never)
              .isRedstoneConductor(RailcraftBlocks::never)
              .isSuffocating(RailcraftBlocks::never)
              .isViewBlocking(RailcraftBlocks::never)
              .lightLevel(LightBlock.LIGHT_EMISSION)));

  public static final RegistryObject<Block> YELLOW_IRON_TANK_GAUGE =
      BLOCKS.register("yellow_iron_tank_gauge",
          () -> new IronTankGaugeBlock(BlockBehaviour.Properties.of(Material.GLASS)
              .sound(SoundType.GLASS)
              .noOcclusion()
              .strength(1.0F, 5.0F)
              .isValidSpawn(RailcraftBlocks::never)
              .isRedstoneConductor(RailcraftBlocks::never)
              .isSuffocating(RailcraftBlocks::never)
              .isViewBlocking(RailcraftBlocks::never)
              .lightLevel(LightBlock.LIGHT_EMISSION)));

  public static final RegistryObject<Block> LIME_IRON_TANK_GAUGE =
      BLOCKS.register("lime_iron_tank_gauge",
          () -> new IronTankGaugeBlock(BlockBehaviour.Properties.of(Material.GLASS)
              .sound(SoundType.GLASS)
              .noOcclusion()
              .strength(1.0F, 5.0F)
              .isValidSpawn(RailcraftBlocks::never)
              .isRedstoneConductor(RailcraftBlocks::never)
              .isSuffocating(RailcraftBlocks::never)
              .isViewBlocking(RailcraftBlocks::never)
              .lightLevel(LightBlock.LIGHT_EMISSION)));

  public static final RegistryObject<Block> PINK_IRON_TANK_GAUGE =
      BLOCKS.register("pink_iron_tank_gauge",
          () -> new IronTankGaugeBlock(BlockBehaviour.Properties.of(Material.GLASS)
              .sound(SoundType.GLASS)
              .noOcclusion()
              .strength(1.0F, 5.0F)
              .isValidSpawn(RailcraftBlocks::never)
              .isRedstoneConductor(RailcraftBlocks::never)
              .isSuffocating(RailcraftBlocks::never)
              .isViewBlocking(RailcraftBlocks::never)
              .lightLevel(LightBlock.LIGHT_EMISSION)));

  public static final RegistryObject<Block> GRAY_IRON_TANK_GAUGE =
      BLOCKS.register("gray_iron_tank_gauge",
          () -> new IronTankGaugeBlock(BlockBehaviour.Properties.of(Material.GLASS)
              .sound(SoundType.GLASS)
              .noOcclusion()
              .strength(1.0F, 5.0F)
              .isValidSpawn(RailcraftBlocks::never)
              .isRedstoneConductor(RailcraftBlocks::never)
              .isSuffocating(RailcraftBlocks::never)
              .isViewBlocking(RailcraftBlocks::never)
              .lightLevel(LightBlock.LIGHT_EMISSION)));

  public static final RegistryObject<Block> LIGHT_GRAY_IRON_TANK_GAUGE =
      BLOCKS.register("light_gray_iron_tank_gauge",
          () -> new IronTankGaugeBlock(BlockBehaviour.Properties.of(Material.GLASS)
              .sound(SoundType.GLASS)
              .noOcclusion()
              .strength(1.0F, 5.0F)
              .isValidSpawn(RailcraftBlocks::never)
              .isRedstoneConductor(RailcraftBlocks::never)
              .isSuffocating(RailcraftBlocks::never)
              .isViewBlocking(RailcraftBlocks::never)
              .lightLevel(LightBlock.LIGHT_EMISSION)));

  public static final RegistryObject<Block> CYAN_IRON_TANK_GAUGE =
      BLOCKS.register("cyan_iron_tank_gauge",
          () -> new IronTankGaugeBlock(BlockBehaviour.Properties.of(Material.GLASS)
              .sound(SoundType.GLASS)
              .noOcclusion()
              .strength(1.0F, 5.0F)
              .isValidSpawn(RailcraftBlocks::never)
              .isRedstoneConductor(RailcraftBlocks::never)
              .isSuffocating(RailcraftBlocks::never)
              .isViewBlocking(RailcraftBlocks::never)
              .lightLevel(LightBlock.LIGHT_EMISSION)));

  public static final RegistryObject<Block> PURPLE_IRON_TANK_GAUGE =
      BLOCKS.register("purple_iron_tank_gauge",
          () -> new IronTankGaugeBlock(BlockBehaviour.Properties.of(Material.GLASS)
              .sound(SoundType.GLASS)
              .noOcclusion()
              .strength(1.0F, 5.0F)
              .isValidSpawn(RailcraftBlocks::never)
              .isRedstoneConductor(RailcraftBlocks::never)
              .isSuffocating(RailcraftBlocks::never)
              .isViewBlocking(RailcraftBlocks::never)
              .lightLevel(LightBlock.LIGHT_EMISSION)));

  public static final RegistryObject<Block> BLUE_IRON_TANK_GAUGE =
      BLOCKS.register("blue_iron_tank_gauge",
          () -> new IronTankGaugeBlock(BlockBehaviour.Properties.of(Material.GLASS)
              .sound(SoundType.GLASS)
              .noOcclusion()
              .strength(1.0F, 5.0F)
              .isValidSpawn(RailcraftBlocks::never)
              .isRedstoneConductor(RailcraftBlocks::never)
              .isSuffocating(RailcraftBlocks::never)
              .isViewBlocking(RailcraftBlocks::never)
              .lightLevel(LightBlock.LIGHT_EMISSION)));

  public static final RegistryObject<Block> BROWN_IRON_TANK_GAUGE =
      BLOCKS.register("brown_iron_tank_gauge",
          () -> new IronTankGaugeBlock(BlockBehaviour.Properties.of(Material.GLASS)
              .sound(SoundType.GLASS)
              .noOcclusion()
              .strength(1.0F, 5.0F)
              .isValidSpawn(RailcraftBlocks::never)
              .isRedstoneConductor(RailcraftBlocks::never)
              .isSuffocating(RailcraftBlocks::never)
              .isViewBlocking(RailcraftBlocks::never)
              .lightLevel(LightBlock.LIGHT_EMISSION)));

  public static final RegistryObject<Block> GREEN_IRON_TANK_GAUGE =
      BLOCKS.register("green_iron_tank_gauge",
          () -> new IronTankGaugeBlock(BlockBehaviour.Properties.of(Material.GLASS)
              .sound(SoundType.GLASS)
              .noOcclusion()
              .strength(1.0F, 5.0F)
              .isValidSpawn(RailcraftBlocks::never)
              .isRedstoneConductor(RailcraftBlocks::never)
              .isSuffocating(RailcraftBlocks::never)
              .isViewBlocking(RailcraftBlocks::never)
              .lightLevel(LightBlock.LIGHT_EMISSION)));

  public static final RegistryObject<Block> RED_IRON_TANK_GAUGE =
      BLOCKS.register("red_iron_tank_gauge",
          () -> new IronTankGaugeBlock(BlockBehaviour.Properties.of(Material.GLASS)
              .sound(SoundType.GLASS)
              .noOcclusion()
              .strength(1.0F, 5.0F)
              .isValidSpawn(RailcraftBlocks::never)
              .isRedstoneConductor(RailcraftBlocks::never)
              .isSuffocating(RailcraftBlocks::never)
              .isViewBlocking(RailcraftBlocks::never)
              .lightLevel(LightBlock.LIGHT_EMISSION)));

  public static final RegistryObject<Block> BLACK_IRON_TANK_GAUGE =
      BLOCKS.register("black_iron_tank_gauge",
          () -> new IronTankGaugeBlock(BlockBehaviour.Properties.of(Material.GLASS)
              .sound(SoundType.GLASS)
              .noOcclusion()
              .strength(1.0F, 5.0F)
              .isValidSpawn(RailcraftBlocks::never)
              .isRedstoneConductor(RailcraftBlocks::never)
              .isSuffocating(RailcraftBlocks::never)
              .isViewBlocking(RailcraftBlocks::never)
              .lightLevel(LightBlock.LIGHT_EMISSION)));

  public static final RegistryObject<Block> WHITE_IRON_TANK_VALVE =
      BLOCKS.register("white_iron_tank_valve",
          () -> new IronTankValveBlock(BlockBehaviour.Properties.of(Material.METAL)
              .sound(SoundType.METAL)
              .noOcclusion()
              .explosionResistance(12)));

  public static final RegistryObject<Block> ORANGE_IRON_TANK_VALVE =
      BLOCKS.register("orange_iron_tank_valve",
          () -> new IronTankValveBlock(BlockBehaviour.Properties.of(Material.METAL)
              .sound(SoundType.METAL)
              .noOcclusion()
              .explosionResistance(12)));

  public static final RegistryObject<Block> MAGENTA_IRON_TANK_VALVE =
      BLOCKS.register("magenta_iron_tank_valve",
          () -> new IronTankValveBlock(BlockBehaviour.Properties.of(Material.METAL)
              .sound(SoundType.METAL)
              .noOcclusion()
              .explosionResistance(12)));

  public static final RegistryObject<Block> LIGHT_BLUE_IRON_TANK_VALVE =
      BLOCKS.register("light_blue_iron_tank_valve",
          () -> new IronTankValveBlock(BlockBehaviour.Properties.of(Material.METAL)
              .sound(SoundType.METAL)
              .noOcclusion()
              .explosionResistance(12)));

  public static final RegistryObject<Block> YELLOW_IRON_TANK_VALVE =
      BLOCKS.register("yellow_iron_tank_valve",
          () -> new IronTankValveBlock(BlockBehaviour.Properties.of(Material.METAL)
              .sound(SoundType.METAL)
              .noOcclusion()
              .explosionResistance(12)));

  public static final RegistryObject<Block> LIME_IRON_TANK_VALVE =
      BLOCKS.register("lime_iron_tank_valve",
          () -> new IronTankValveBlock(BlockBehaviour.Properties.of(Material.METAL)
              .sound(SoundType.METAL)
              .noOcclusion()
              .explosionResistance(12)));

  public static final RegistryObject<Block> PINK_IRON_TANK_VALVE =
      BLOCKS.register("pink_iron_tank_valve",
          () -> new IronTankValveBlock(BlockBehaviour.Properties.of(Material.METAL)
              .sound(SoundType.METAL)
              .noOcclusion()
              .explosionResistance(12)));

  public static final RegistryObject<Block> GRAY_IRON_TANK_VALVE =
      BLOCKS.register("gray_iron_tank_valve",
          () -> new IronTankValveBlock(BlockBehaviour.Properties.of(Material.METAL)
              .sound(SoundType.METAL)
              .noOcclusion()
              .explosionResistance(12)));

  public static final RegistryObject<Block> LIGHT_GRAY_IRON_TANK_VALVE =
      BLOCKS.register("light_gray_iron_tank_valve",
          () -> new IronTankValveBlock(BlockBehaviour.Properties.of(Material.METAL)
              .sound(SoundType.METAL)
              .noOcclusion()
              .explosionResistance(12)));

  public static final RegistryObject<Block> CYAN_IRON_TANK_VALVE =
      BLOCKS.register("cyan_iron_tank_valve",
          () -> new IronTankValveBlock(BlockBehaviour.Properties.of(Material.METAL)
              .sound(SoundType.METAL)
              .noOcclusion()
              .explosionResistance(12)));

  public static final RegistryObject<Block> PURPLE_IRON_TANK_VALVE =
      BLOCKS.register("purple_iron_tank_valve",
          () -> new IronTankValveBlock(BlockBehaviour.Properties.of(Material.METAL)
              .sound(SoundType.METAL)
              .noOcclusion()
              .explosionResistance(12)));

  public static final RegistryObject<Block> BLUE_IRON_TANK_VALVE =
      BLOCKS.register("blue_iron_tank_valve",
          () -> new IronTankValveBlock(BlockBehaviour.Properties.of(Material.METAL)
              .sound(SoundType.METAL)
              .noOcclusion()
              .explosionResistance(12)));

  public static final RegistryObject<Block> BROWN_IRON_TANK_VALVE =
      BLOCKS.register("brown_iron_tank_valve",
          () -> new IronTankValveBlock(BlockBehaviour.Properties.of(Material.METAL)
              .sound(SoundType.METAL)
              .noOcclusion()
              .explosionResistance(12)));

  public static final RegistryObject<Block> GREEN_IRON_TANK_VALVE =
      BLOCKS.register("green_iron_tank_valve",
          () -> new IronTankValveBlock(BlockBehaviour.Properties.of(Material.METAL)
              .sound(SoundType.METAL)
              .noOcclusion()
              .explosionResistance(12)));

  public static final RegistryObject<Block> RED_IRON_TANK_VALVE =
      BLOCKS.register("red_iron_tank_valve",
          () -> new IronTankValveBlock(BlockBehaviour.Properties.of(Material.METAL)
              .sound(SoundType.METAL)
              .noOcclusion()
              .explosionResistance(12)));

  public static final RegistryObject<Block> BLACK_IRON_TANK_VALVE =
      BLOCKS.register("black_iron_tank_valve",
          () -> new IronTankValveBlock(BlockBehaviour.Properties.of(Material.METAL)
              .sound(SoundType.METAL)
              .noOcclusion()
              .explosionResistance(12)));

  public static final RegistryObject<Block> WHITE_IRON_TANK_WALL =
      BLOCKS.register("white_iron_tank_wall",
          () -> new IronTankWallBlock(BlockBehaviour.Properties.of(Material.METAL)
              .sound(SoundType.METAL)
              .noOcclusion()
              .explosionResistance(12)));

  public static final RegistryObject<Block> ORANGE_IRON_TANK_WALL =
      BLOCKS.register("orange_iron_tank_wall",
          () -> new IronTankWallBlock(BlockBehaviour.Properties.of(Material.METAL)
              .sound(SoundType.METAL)
              .noOcclusion()
              .explosionResistance(12)));

  public static final RegistryObject<Block> MAGENTA_IRON_TANK_WALL =
      BLOCKS.register("magenta_iron_tank_wall",
          () -> new IronTankWallBlock(BlockBehaviour.Properties.of(Material.METAL)
              .sound(SoundType.METAL)
              .noOcclusion()
              .explosionResistance(12)));

  public static final RegistryObject<Block> LIGHT_BLUE_IRON_TANK_WALL =
      BLOCKS.register("light_blue_iron_tank_wall",
          () -> new IronTankWallBlock(BlockBehaviour.Properties.of(Material.METAL)
              .sound(SoundType.METAL)
              .noOcclusion()
              .explosionResistance(12)));

  public static final RegistryObject<Block> YELLOW_IRON_TANK_WALL =
      BLOCKS.register("yellow_iron_tank_wall",
          () -> new IronTankWallBlock(BlockBehaviour.Properties.of(Material.METAL)
              .sound(SoundType.METAL)
              .noOcclusion()
              .explosionResistance(12)));

  public static final RegistryObject<Block> LIME_IRON_TANK_WALL =
      BLOCKS.register("lime_iron_tank_wall",
          () -> new IronTankWallBlock(BlockBehaviour.Properties.of(Material.METAL)
              .sound(SoundType.METAL)
              .noOcclusion()
              .explosionResistance(12)));

  public static final RegistryObject<Block> PINK_IRON_TANK_WALL =
      BLOCKS.register("pink_iron_tank_wall",
          () -> new IronTankWallBlock(BlockBehaviour.Properties.of(Material.METAL)
              .sound(SoundType.METAL)
              .noOcclusion()
              .explosionResistance(12)));

  public static final RegistryObject<Block> GRAY_IRON_TANK_WALL =
      BLOCKS.register("gray_iron_tank_wall",
          () -> new IronTankWallBlock(BlockBehaviour.Properties.of(Material.METAL)
              .sound(SoundType.METAL)
              .noOcclusion()
              .explosionResistance(12)));

  public static final RegistryObject<Block> LIGHT_GRAY_IRON_TANK_WALL =
      BLOCKS.register("light_gray_iron_tank_wall",
          () -> new IronTankWallBlock(BlockBehaviour.Properties.of(Material.METAL)
              .sound(SoundType.METAL)
              .noOcclusion()
              .explosionResistance(12)));

  public static final RegistryObject<Block> CYAN_IRON_TANK_WALL =
      BLOCKS.register("cyan_iron_tank_wall",
          () -> new IronTankWallBlock(BlockBehaviour.Properties.of(Material.METAL)
              .sound(SoundType.METAL)
              .noOcclusion()
              .explosionResistance(12)));

  public static final RegistryObject<Block> PURPLE_IRON_TANK_WALL =
      BLOCKS.register("purple_iron_tank_wall",
          () -> new IronTankWallBlock(BlockBehaviour.Properties.of(Material.METAL)
              .sound(SoundType.METAL)
              .noOcclusion()
              .explosionResistance(12)));

  public static final RegistryObject<Block> BLUE_IRON_TANK_WALL =
      BLOCKS.register("blue_iron_tank_wall",
          () -> new IronTankWallBlock(BlockBehaviour.Properties.of(Material.METAL)
              .sound(SoundType.METAL)
              .noOcclusion()
              .explosionResistance(12)));

  public static final RegistryObject<Block> BROWN_IRON_TANK_WALL =
      BLOCKS.register("brown_iron_tank_wall",
          () -> new IronTankWallBlock(BlockBehaviour.Properties.of(Material.METAL)
              .sound(SoundType.METAL)
              .noOcclusion()
              .explosionResistance(12)));

  public static final RegistryObject<Block> GREEN_IRON_TANK_WALL =
      BLOCKS.register("green_iron_tank_wall",
          () -> new IronTankWallBlock(BlockBehaviour.Properties.of(Material.METAL)
              .sound(SoundType.METAL)
              .noOcclusion()
              .explosionResistance(12)));

  public static final RegistryObject<Block> RED_IRON_TANK_WALL =
      BLOCKS.register("red_iron_tank_wall",
          () -> new IronTankWallBlock(BlockBehaviour.Properties.of(Material.METAL)
              .sound(SoundType.METAL)
              .noOcclusion()
              .explosionResistance(12)));

  public static final RegistryObject<Block> BLACK_IRON_TANK_WALL =
      BLOCKS.register("black_iron_tank_wall",
          () -> new IronTankWallBlock(BlockBehaviour.Properties.of(Material.METAL)
              .sound(SoundType.METAL)
              .noOcclusion()
              .explosionResistance(12)));

  public static final RegistryObject<Block> WHITE_STEEL_TANK_GAUGE =
      BLOCKS.register("white_steel_tank_gauge",
          () -> new SteelTankGaugeBlock(BlockBehaviour.Properties.of(Material.GLASS)
              .sound(SoundType.GLASS)
              .noOcclusion()
              .strength(1.0F, 5.0F)
              .isValidSpawn(RailcraftBlocks::never)
              .isRedstoneConductor(RailcraftBlocks::never)
              .isSuffocating(RailcraftBlocks::never)
              .isViewBlocking(RailcraftBlocks::never)
              .lightLevel(LightBlock.LIGHT_EMISSION)));

  public static final RegistryObject<Block> ORANGE_STEEL_TANK_GAUGE =
      BLOCKS.register("orange_steel_tank_gauge",
          () -> new SteelTankGaugeBlock(BlockBehaviour.Properties.of(Material.GLASS)
              .sound(SoundType.GLASS)
              .noOcclusion()
              .strength(1.0F, 5.0F)
              .isValidSpawn(RailcraftBlocks::never)
              .isRedstoneConductor(RailcraftBlocks::never)
              .isSuffocating(RailcraftBlocks::never)
              .isViewBlocking(RailcraftBlocks::never)
              .lightLevel(LightBlock.LIGHT_EMISSION)));

  public static final RegistryObject<Block> MAGENTA_STEEL_TANK_GAUGE =
      BLOCKS.register("magenta_steel_tank_gauge",
          () -> new SteelTankGaugeBlock(BlockBehaviour.Properties.of(Material.GLASS)
              .sound(SoundType.GLASS)
              .noOcclusion()
              .strength(1.0F, 5.0F)
              .isValidSpawn(RailcraftBlocks::never)
              .isRedstoneConductor(RailcraftBlocks::never)
              .isSuffocating(RailcraftBlocks::never)
              .isViewBlocking(RailcraftBlocks::never)
              .lightLevel(LightBlock.LIGHT_EMISSION)));

  public static final RegistryObject<Block> LIGHT_BLUE_STEEL_TANK_GAUGE =
      BLOCKS.register("light_blue_steel_tank_gauge",
          () -> new SteelTankGaugeBlock(BlockBehaviour.Properties.of(Material.GLASS)
              .sound(SoundType.GLASS)
              .noOcclusion()
              .strength(1.0F, 5.0F)
              .isValidSpawn(RailcraftBlocks::never)
              .isRedstoneConductor(RailcraftBlocks::never)
              .isSuffocating(RailcraftBlocks::never)
              .isViewBlocking(RailcraftBlocks::never)
              .lightLevel(LightBlock.LIGHT_EMISSION)));

  public static final RegistryObject<Block> YELLOW_STEEL_TANK_GAUGE =
      BLOCKS.register("yellow_steel_tank_gauge",
          () -> new SteelTankGaugeBlock(BlockBehaviour.Properties.of(Material.GLASS)
              .sound(SoundType.GLASS)
              .noOcclusion()
              .strength(1.0F, 5.0F)
              .isValidSpawn(RailcraftBlocks::never)
              .isRedstoneConductor(RailcraftBlocks::never)
              .isSuffocating(RailcraftBlocks::never)
              .isViewBlocking(RailcraftBlocks::never)
              .lightLevel(LightBlock.LIGHT_EMISSION)));

  public static final RegistryObject<Block> LIME_STEEL_TANK_GAUGE =
      BLOCKS.register("lime_steel_tank_gauge",
          () -> new SteelTankGaugeBlock(BlockBehaviour.Properties.of(Material.GLASS)
              .sound(SoundType.GLASS)
              .noOcclusion()
              .strength(1.0F, 5.0F)
              .isValidSpawn(RailcraftBlocks::never)
              .isRedstoneConductor(RailcraftBlocks::never)
              .isSuffocating(RailcraftBlocks::never)
              .isViewBlocking(RailcraftBlocks::never)
              .lightLevel(LightBlock.LIGHT_EMISSION)));

  public static final RegistryObject<Block> PINK_STEEL_TANK_GAUGE =
      BLOCKS.register("pink_steel_tank_gauge",
          () -> new SteelTankGaugeBlock(BlockBehaviour.Properties.of(Material.GLASS)
              .sound(SoundType.GLASS)
              .noOcclusion()
              .strength(1.0F, 5.0F)
              .isValidSpawn(RailcraftBlocks::never)
              .isRedstoneConductor(RailcraftBlocks::never)
              .isSuffocating(RailcraftBlocks::never)
              .isViewBlocking(RailcraftBlocks::never)
              .lightLevel(LightBlock.LIGHT_EMISSION)));

  public static final RegistryObject<Block> GRAY_STEEL_TANK_GAUGE =
      BLOCKS.register("gray_steel_tank_gauge",
          () -> new SteelTankGaugeBlock(BlockBehaviour.Properties.of(Material.GLASS)
              .sound(SoundType.GLASS)
              .noOcclusion()
              .strength(1.0F, 5.0F)
              .isValidSpawn(RailcraftBlocks::never)
              .isRedstoneConductor(RailcraftBlocks::never)
              .isSuffocating(RailcraftBlocks::never)
              .isViewBlocking(RailcraftBlocks::never)
              .lightLevel(LightBlock.LIGHT_EMISSION)));

  public static final RegistryObject<Block> LIGHT_GRAY_STEEL_TANK_GAUGE =
      BLOCKS.register("light_gray_steel_tank_gauge",
          () -> new SteelTankGaugeBlock(BlockBehaviour.Properties.of(Material.GLASS)
              .sound(SoundType.GLASS)
              .noOcclusion()
              .strength(1.0F, 5.0F)
              .isValidSpawn(RailcraftBlocks::never)
              .isRedstoneConductor(RailcraftBlocks::never)
              .isSuffocating(RailcraftBlocks::never)
              .isViewBlocking(RailcraftBlocks::never)
              .lightLevel(LightBlock.LIGHT_EMISSION)));

  public static final RegistryObject<Block> CYAN_STEEL_TANK_GAUGE =
      BLOCKS.register("cyan_steel_tank_gauge",
          () -> new SteelTankGaugeBlock(BlockBehaviour.Properties.of(Material.GLASS)
              .sound(SoundType.GLASS)
              .noOcclusion()
              .strength(1.0F, 5.0F)
              .isValidSpawn(RailcraftBlocks::never)
              .isRedstoneConductor(RailcraftBlocks::never)
              .isSuffocating(RailcraftBlocks::never)
              .isViewBlocking(RailcraftBlocks::never)
              .lightLevel(LightBlock.LIGHT_EMISSION)));

  public static final RegistryObject<Block> PURPLE_STEEL_TANK_GAUGE =
      BLOCKS.register("purple_steel_tank_gauge",
          () -> new SteelTankGaugeBlock(BlockBehaviour.Properties.of(Material.GLASS)
              .sound(SoundType.GLASS)
              .noOcclusion()
              .strength(1.0F, 5.0F)
              .isValidSpawn(RailcraftBlocks::never)
              .isRedstoneConductor(RailcraftBlocks::never)
              .isSuffocating(RailcraftBlocks::never)
              .isViewBlocking(RailcraftBlocks::never)
              .lightLevel(LightBlock.LIGHT_EMISSION)));

  public static final RegistryObject<Block> BLUE_STEEL_TANK_GAUGE =
      BLOCKS.register("blue_steel_tank_gauge",
          () -> new SteelTankGaugeBlock(BlockBehaviour.Properties.of(Material.GLASS)
              .sound(SoundType.GLASS)
              .noOcclusion()
              .strength(1.0F, 5.0F)
              .isValidSpawn(RailcraftBlocks::never)
              .isRedstoneConductor(RailcraftBlocks::never)
              .isSuffocating(RailcraftBlocks::never)
              .isViewBlocking(RailcraftBlocks::never)
              .lightLevel(LightBlock.LIGHT_EMISSION)));

  public static final RegistryObject<Block> BROWN_STEEL_TANK_GAUGE =
      BLOCKS.register("brown_steel_tank_gauge",
          () -> new SteelTankGaugeBlock(BlockBehaviour.Properties.of(Material.GLASS)
              .sound(SoundType.GLASS)
              .noOcclusion()
              .strength(1.0F, 5.0F)
              .isValidSpawn(RailcraftBlocks::never)
              .isRedstoneConductor(RailcraftBlocks::never)
              .isSuffocating(RailcraftBlocks::never)
              .isViewBlocking(RailcraftBlocks::never)
              .lightLevel(LightBlock.LIGHT_EMISSION)));

  public static final RegistryObject<Block> GREEN_STEEL_TANK_GAUGE =
      BLOCKS.register("green_steel_tank_gauge",
          () -> new SteelTankGaugeBlock(BlockBehaviour.Properties.of(Material.GLASS)
              .sound(SoundType.GLASS)
              .noOcclusion()
              .strength(1.0F, 5.0F)
              .isValidSpawn(RailcraftBlocks::never)
              .isRedstoneConductor(RailcraftBlocks::never)
              .isSuffocating(RailcraftBlocks::never)
              .isViewBlocking(RailcraftBlocks::never)
              .lightLevel(LightBlock.LIGHT_EMISSION)));

  public static final RegistryObject<Block> RED_STEEL_TANK_GAUGE =
      BLOCKS.register("red_steel_tank_gauge",
          () -> new SteelTankGaugeBlock(BlockBehaviour.Properties.of(Material.GLASS)
              .sound(SoundType.GLASS)
              .noOcclusion()
              .strength(1.0F, 5.0F)
              .isValidSpawn(RailcraftBlocks::never)
              .isRedstoneConductor(RailcraftBlocks::never)
              .isSuffocating(RailcraftBlocks::never)
              .isViewBlocking(RailcraftBlocks::never)
              .lightLevel(LightBlock.LIGHT_EMISSION)));

  public static final RegistryObject<Block> BLACK_STEEL_TANK_GAUGE =
      BLOCKS.register("black_steel_tank_gauge",
          () -> new SteelTankGaugeBlock(BlockBehaviour.Properties.of(Material.GLASS)
              .sound(SoundType.GLASS)
              .noOcclusion()
              .strength(1.0F, 5.0F)
              .isValidSpawn(RailcraftBlocks::never)
              .isRedstoneConductor(RailcraftBlocks::never)
              .isSuffocating(RailcraftBlocks::never)
              .isViewBlocking(RailcraftBlocks::never)
              .lightLevel(LightBlock.LIGHT_EMISSION)));

  public static final RegistryObject<Block> WHITE_STEEL_TANK_VALVE =
      BLOCKS.register("white_steel_tank_valve",
          () -> new SteelTankValveBlock(BlockBehaviour.Properties.of(Material.METAL)
              .sound(SoundType.METAL)
              .noOcclusion()
              .explosionResistance(15)));

  public static final RegistryObject<Block> ORANGE_STEEL_TANK_VALVE =
      BLOCKS.register("orange_steel_tank_valve",
          () -> new SteelTankValveBlock(BlockBehaviour.Properties.of(Material.METAL)
              .sound(SoundType.METAL)
              .noOcclusion()
              .explosionResistance(15)));

  public static final RegistryObject<Block> MAGENTA_STEEL_TANK_VALVE =
      BLOCKS.register("magenta_steel_tank_valve",
          () -> new SteelTankValveBlock(BlockBehaviour.Properties.of(Material.METAL)
              .sound(SoundType.METAL)
              .noOcclusion()
              .explosionResistance(15)));

  public static final RegistryObject<Block> LIGHT_BLUE_STEEL_TANK_VALVE =
      BLOCKS.register("light_blue_steel_tank_valve",
          () -> new SteelTankValveBlock(BlockBehaviour.Properties.of(Material.METAL)
              .sound(SoundType.METAL)
              .noOcclusion()
              .explosionResistance(15)));

  public static final RegistryObject<Block> YELLOW_STEEL_TANK_VALVE =
      BLOCKS.register("yellow_steel_tank_valve",
          () -> new SteelTankValveBlock(BlockBehaviour.Properties.of(Material.METAL)
              .sound(SoundType.METAL)
              .noOcclusion()
              .explosionResistance(15)));

  public static final RegistryObject<Block> LIME_STEEL_TANK_VALVE =
      BLOCKS.register("lime_steel_tank_valve",
          () -> new SteelTankValveBlock(BlockBehaviour.Properties.of(Material.METAL)
              .sound(SoundType.METAL)
              .noOcclusion()
              .explosionResistance(15)));

  public static final RegistryObject<Block> PINK_STEEL_TANK_VALVE =
      BLOCKS.register("pink_steel_tank_valve",
          () -> new SteelTankValveBlock(BlockBehaviour.Properties.of(Material.METAL)
              .sound(SoundType.METAL)
              .noOcclusion()
              .explosionResistance(15)));

  public static final RegistryObject<Block> GRAY_STEEL_TANK_VALVE =
      BLOCKS.register("gray_steel_tank_valve",
          () -> new SteelTankValveBlock(BlockBehaviour.Properties.of(Material.METAL)
              .sound(SoundType.METAL)
              .noOcclusion()
              .explosionResistance(15)));

  public static final RegistryObject<Block> LIGHT_GRAY_STEEL_TANK_VALVE =
      BLOCKS.register("light_gray_steel_tank_valve",
          () -> new SteelTankValveBlock(BlockBehaviour.Properties.of(Material.METAL)
              .sound(SoundType.METAL)
              .noOcclusion()
              .explosionResistance(15)));

  public static final RegistryObject<Block> CYAN_STEEL_TANK_VALVE =
      BLOCKS.register("cyan_steel_tank_valve",
          () -> new SteelTankValveBlock(BlockBehaviour.Properties.of(Material.METAL)
              .sound(SoundType.METAL)
              .noOcclusion()
              .explosionResistance(15)));

  public static final RegistryObject<Block> PURPLE_STEEL_TANK_VALVE =
      BLOCKS.register("purple_steel_tank_valve",
          () -> new SteelTankValveBlock(BlockBehaviour.Properties.of(Material.METAL)
              .sound(SoundType.METAL)
              .noOcclusion()
              .explosionResistance(15)));

  public static final RegistryObject<Block> BLUE_STEEL_TANK_VALVE =
      BLOCKS.register("blue_steel_tank_valve",
          () -> new SteelTankValveBlock(BlockBehaviour.Properties.of(Material.METAL)
              .sound(SoundType.METAL)
              .noOcclusion()
              .explosionResistance(15)));

  public static final RegistryObject<Block> BROWN_STEEL_TANK_VALVE =
      BLOCKS.register("brown_steel_tank_valve",
          () -> new SteelTankValveBlock(BlockBehaviour.Properties.of(Material.METAL)
              .sound(SoundType.METAL)
              .noOcclusion()
              .explosionResistance(15)));

  public static final RegistryObject<Block> GREEN_STEEL_TANK_VALVE =
      BLOCKS.register("green_steel_tank_valve",
          () -> new SteelTankValveBlock(BlockBehaviour.Properties.of(Material.METAL)
              .sound(SoundType.METAL)
              .noOcclusion()
              .explosionResistance(15)));

  public static final RegistryObject<Block> RED_STEEL_TANK_VALVE =
      BLOCKS.register("red_steel_tank_valve",
          () -> new SteelTankValveBlock(BlockBehaviour.Properties.of(Material.METAL)
              .sound(SoundType.METAL)
              .noOcclusion()
              .explosionResistance(15)));

  public static final RegistryObject<Block> BLACK_STEEL_TANK_VALVE =
      BLOCKS.register("black_steel_tank_valve",
          () -> new SteelTankValveBlock(BlockBehaviour.Properties.of(Material.METAL)
              .sound(SoundType.METAL)
              .noOcclusion()
              .explosionResistance(15)));

  public static final RegistryObject<Block> WHITE_STEEL_TANK_WALL =
      BLOCKS.register("white_steel_tank_wall",
          () -> new SteelTankWallBlock(BlockBehaviour.Properties.of(Material.METAL)
              .sound(SoundType.METAL)
              .noOcclusion()
              .explosionResistance(15)));

  public static final RegistryObject<Block> ORANGE_STEEL_TANK_WALL =
      BLOCKS.register("orange_steel_tank_wall",
          () -> new SteelTankWallBlock(BlockBehaviour.Properties.of(Material.METAL)
              .sound(SoundType.METAL)
              .noOcclusion()
              .explosionResistance(15)));

  public static final RegistryObject<Block> MAGENTA_STEEL_TANK_WALL =
      BLOCKS.register("magenta_steel_tank_wall",
          () -> new SteelTankWallBlock(BlockBehaviour.Properties.of(Material.METAL)
              .sound(SoundType.METAL)
              .noOcclusion()
              .explosionResistance(15)));

  public static final RegistryObject<Block> LIGHT_BLUE_STEEL_TANK_WALL =
      BLOCKS.register("light_blue_steel_tank_wall",
          () -> new SteelTankWallBlock(BlockBehaviour.Properties.of(Material.METAL)
              .sound(SoundType.METAL)
              .noOcclusion()
              .explosionResistance(15)));

  public static final RegistryObject<Block> YELLOW_STEEL_TANK_WALL =
      BLOCKS.register("yellow_steel_tank_wall",
          () -> new SteelTankWallBlock(BlockBehaviour.Properties.of(Material.METAL)
              .sound(SoundType.METAL)
              .noOcclusion()
              .explosionResistance(15)));

  public static final RegistryObject<Block> LIME_STEEL_TANK_WALL =
      BLOCKS.register("lime_steel_tank_wall",
          () -> new SteelTankWallBlock(BlockBehaviour.Properties.of(Material.METAL)
              .sound(SoundType.METAL)
              .noOcclusion()
              .explosionResistance(15)));

  public static final RegistryObject<Block> PINK_STEEL_TANK_WALL =
      BLOCKS.register("pink_steel_tank_wall",
          () -> new SteelTankWallBlock(BlockBehaviour.Properties.of(Material.METAL)
              .sound(SoundType.METAL)
              .noOcclusion()
              .explosionResistance(15)));

  public static final RegistryObject<Block> GRAY_STEEL_TANK_WALL =
      BLOCKS.register("gray_steel_tank_wall",
          () -> new SteelTankWallBlock(BlockBehaviour.Properties.of(Material.METAL)
              .sound(SoundType.METAL)
              .noOcclusion()
              .explosionResistance(15)));

  public static final RegistryObject<Block> LIGHT_GRAY_STEEL_TANK_WALL =
      BLOCKS.register("light_gray_steel_tank_wall",
          () -> new SteelTankWallBlock(BlockBehaviour.Properties.of(Material.METAL)
              .sound(SoundType.METAL)
              .noOcclusion()
              .explosionResistance(15)));

  public static final RegistryObject<Block> CYAN_STEEL_TANK_WALL =
      BLOCKS.register("cyan_steel_tank_wall",
          () -> new SteelTankWallBlock(BlockBehaviour.Properties.of(Material.METAL)
              .sound(SoundType.METAL)
              .noOcclusion()
              .explosionResistance(15)));

  public static final RegistryObject<Block> PURPLE_STEEL_TANK_WALL =
      BLOCKS.register("purple_steel_tank_wall",
          () -> new SteelTankWallBlock(BlockBehaviour.Properties.of(Material.METAL)
              .sound(SoundType.METAL)
              .noOcclusion()
              .explosionResistance(15)));

  public static final RegistryObject<Block> BLUE_STEEL_TANK_WALL =
      BLOCKS.register("blue_steel_tank_wall",
          () -> new SteelTankWallBlock(BlockBehaviour.Properties.of(Material.METAL)
              .sound(SoundType.METAL)
              .noOcclusion()
              .explosionResistance(15)));

  public static final RegistryObject<Block> BROWN_STEEL_TANK_WALL =
      BLOCKS.register("brown_steel_tank_wall",
          () -> new SteelTankWallBlock(BlockBehaviour.Properties.of(Material.METAL)
              .sound(SoundType.METAL)
              .noOcclusion()
              .explosionResistance(15)));

  public static final RegistryObject<Block> GREEN_STEEL_TANK_WALL =
      BLOCKS.register("green_steel_tank_wall",
          () -> new SteelTankWallBlock(BlockBehaviour.Properties.of(Material.METAL)
              .sound(SoundType.METAL)
              .noOcclusion()
              .explosionResistance(15)));

  public static final RegistryObject<Block> RED_STEEL_TANK_WALL =
      BLOCKS.register("red_steel_tank_wall",
          () -> new SteelTankWallBlock(BlockBehaviour.Properties.of(Material.METAL)
              .sound(SoundType.METAL)
              .noOcclusion()
              .explosionResistance(15)));

  public static final RegistryObject<Block> BLACK_STEEL_TANK_WALL =
      BLOCKS.register("black_steel_tank_wall",
          () -> new SteelTankWallBlock(BlockBehaviour.Properties.of(Material.METAL)
              .sound(SoundType.METAL)
              .noOcclusion()
              .explosionResistance(15)));

  public static final RegistryObject<Block> BLAST_FURNACE_BRICKS =
      BLOCKS.register("blast_furnace_bricks",
          () -> new BlastFurnaceBricksBlock(BlockBehaviour.Properties.of(Material.STONE)
              .lightLevel(litBlockEmission(13))
              .sound(SoundType.STONE)));

  public static final RegistryObject<Block> FEED_STATION =
      BLOCKS.register("feed_station",
          () -> new FeedStationBlock(
              BlockBehaviour.Properties.of(Material.STONE, MaterialColor.WOOD)
                  .sound(SoundType.WOOD)));

  public static final RegistryObject<Block> STEEL_ANVIL =
      BLOCKS.register("steel_anvil",
          () -> new AnvilBlock(
              BlockBehaviour.Properties.of(Material.HEAVY_METAL, MaterialColor.METAL)
                  .requiresCorrectToolForDrops()
                  .strength(5.0F, 2000.0F)
                  .sound(SoundType.ANVIL)));

  public static final RegistryObject<Block> CHIPPED_STEEL_ANVIL =
      BLOCKS.register("chipped_steel_anvil",
          () -> new AnvilBlock(
              BlockBehaviour.Properties.of(Material.HEAVY_METAL, MaterialColor.METAL)
                  .requiresCorrectToolForDrops()
                  .strength(5.0F, 2000.0F)
                  .sound(SoundType.ANVIL)));

  public static final RegistryObject<Block> DAMAGED_STEEL_ANVIL =
      BLOCKS.register("damaged_steel_anvil",
          () -> new AnvilBlock(
              BlockBehaviour.Properties.of(Material.HEAVY_METAL, MaterialColor.METAL)
                  .requiresCorrectToolForDrops()
                  .strength(5.0F, 2000.0F)
                  .sound(SoundType.ANVIL)));

  public static final RegistryObject<Block> STEEL_BLOCK =
      BLOCKS.register("steel_block",
          () -> new Block(BlockBehaviour.Properties.of(Material.METAL)
              .strength(5.0F, 15.0F)
              .sound(SoundType.METAL)));

  public static final RegistryObject<FluidLoaderBlock> FLUID_LOADER =
      BLOCKS.register("fluid_loader",
          () -> new FluidLoaderBlock(BlockBehaviour.Properties.of(Material.STONE)
              .sound(SoundType.STONE)
              .noOcclusion()));

  public static final RegistryObject<FluidUnloaderBlock> FLUID_UNLOADER =
      BLOCKS.register("fluid_unloader",
          () -> new FluidUnloaderBlock(BlockBehaviour.Properties.of(Material.STONE)
              .sound(SoundType.STONE)
              .noOcclusion()));

  public static final RegistryObject<AdvancedItemLoaderBlock> ADVANCED_ITEM_LOADER =
      BLOCKS.register("advanced_item_loader",
          () -> new AdvancedItemLoaderBlock(BlockBehaviour.Properties.of(Material.STONE)
              .sound(SoundType.STONE)));

  public static final RegistryObject<AdvancedItemUnloaderBlock> ADVANCED_ITEM_UNLOADER =
      BLOCKS.register("advanced_item_unloader",
          () -> new AdvancedItemUnloaderBlock(BlockBehaviour.Properties.of(Material.STONE)
              .sound(SoundType.STONE)));

  public static final RegistryObject<ItemLoaderBlock> ITEM_LOADER =
      BLOCKS.register("item_loader",
          () -> new ItemLoaderBlock(BlockBehaviour.Properties.of(Material.STONE)
              .sound(SoundType.STONE)));

  public static final RegistryObject<ItemUnloaderBlock> ITEM_UNLOADER =
      BLOCKS.register("item_unloader",
          () -> new ItemUnloaderBlock(BlockBehaviour.Properties.of(Material.STONE)
              .sound(SoundType.STONE)));

  public static final RegistryObject<SwitchTrackActuatorBlock> SWITCH_TRACK_LEVER =
      BLOCKS.register("switch_track_lever",
          () -> new SwitchTrackLeverBlock(BlockBehaviour.Properties.of(Material.DECORATION)
              .strength(8.0F, 50.0F)
              .sound(SoundType.METAL)
              .noOcclusion()));

  public static final RegistryObject<SwitchTrackActuatorBlock> SWITCH_TRACK_MOTOR =
      BLOCKS.register("switch_track_motor",
          () -> new SwitchTrackMotorBlock(BlockBehaviour.Properties.of(Material.DECORATION)
              .strength(8.0F, 50.0F)
              .sound(SoundType.METAL)
              .noOcclusion()));

  public static final RegistryObject<SignalBoxBlock> ANALOG_SIGNAL_CONTROLLER_BOX =
      BLOCKS.register("analog_signal_controller_box",
          () -> new AnalogSignalControllerBoxBlock(
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .strength(8.0F, 50.0F)
                  .sound(SoundType.METAL)
                  .noOcclusion()));

  public static final RegistryObject<SignalBoxBlock> SIGNAL_SEQUENCER_BOX =
      BLOCKS.register("signal_sequencer_box",
          () -> new SelfAttachableSignalBoxBlock(BlockBehaviour.Properties.of(Material.DECORATION)
              .strength(8.0F, 50.0F)
              .sound(SoundType.METAL)
              .noOcclusion()));

  public static final RegistryObject<SignalBoxBlock> SIGNAL_CAPACITOR_BOX =
      BLOCKS.register("signal_capacitor_box",
          () -> new SignalCapacitorBoxBlock(BlockBehaviour.Properties.of(Material.DECORATION)
              .strength(8.0F, 50.0F)
              .sound(SoundType.METAL)
              .noOcclusion()));

  public static final RegistryObject<SignalBoxBlock> SIGNAL_INTERLOCK_BOX =
      BLOCKS.register("signal_interlock_box",
          () -> new SignalInterlockBoxBlock(BlockBehaviour.Properties.of(Material.DECORATION)
              .strength(8.0F, 50.0F)
              .sound(SoundType.METAL)
              .noOcclusion()));

  public static final RegistryObject<SignalBoxBlock> BLOCK_SIGNAL_RELAY_BOX =
      BLOCKS.register("block_signal_relay_box",
          () -> new BlockSignalRelayBoxBlock(BlockBehaviour.Properties.of(Material.DECORATION)
              .strength(8.0F, 50.0F)
              .sound(SoundType.METAL)
              .noOcclusion()));

  public static final RegistryObject<SignalBoxBlock> SIGNAL_RECEIVER_BOX =
      BLOCKS.register("signal_receiver_box",
          () -> new SignalReceiverBoxBlock(BlockBehaviour.Properties.of(Material.DECORATION)
              .strength(8.0F, 50.0F)
              .sound(SoundType.METAL)
              .noOcclusion()));

  public static final RegistryObject<SignalBoxBlock> SIGNAL_CONTROLLER_BOX =
      BLOCKS.register("signal_controller_box",
          () -> new SignalControllerBoxBlock(BlockBehaviour.Properties.of(Material.DECORATION)
              .strength(8.0F, 50.0F)
              .sound(SoundType.METAL)
              .noOcclusion()));

  public static final RegistryObject<DualSignalBlock> DUAL_BLOCK_SIGNAL =
      BLOCKS.register("dual_block_signal",
          () -> new DualBlockSignalBlock(BlockBehaviour.Properties.of(Material.DECORATION)
              .strength(8.0F, 50.0F)
              .noOcclusion()));

  public static final RegistryObject<DualSignalBlock> DUAL_DISTANT_SIGNAL =
      BLOCKS.register("dual_distant_signal",
          () -> new DualDistantSignalBlock(BlockBehaviour.Properties.of(Material.DECORATION)
              .strength(8.0F, 50.0F)
              .noOcclusion()));

  public static final RegistryObject<DualSignalBlock> DUAL_TOKEN_SIGNAL =
      BLOCKS.register("dual_token_signal",
          () -> new DualTokenSignalBlock(BlockBehaviour.Properties.of(Material.DECORATION)
              .strength(8.0F, 50.0F)
              .noOcclusion()));

  public static final RegistryObject<SingleSignalBlock> BLOCK_SIGNAL =
      BLOCKS.register("block_signal",
          () -> new BlockSignalBlock(BlockBehaviour.Properties.of(Material.DECORATION)
              .strength(8.0F, 50.0F)
              .noOcclusion()));

  public static final RegistryObject<SingleSignalBlock> DISTANT_SIGNAL =
      BLOCKS.register("distant_signal",
          () -> new DistantSignalBlock(BlockBehaviour.Properties.of(Material.DECORATION)
              .strength(8.0F, 50.0F)
              .noOcclusion()));

  public static final RegistryObject<SingleSignalBlock> TOKEN_SIGNAL =
      BLOCKS.register("token_signal",
          () -> new TokenSignalBlock(BlockBehaviour.Properties.of(Material.DECORATION)
              .strength(8.0F, 50.0F)
              .noOcclusion()));

  public static final RegistryObject<ForceTrackBlock> FORCE_TRACK =
      BLOCKS.register("force_track",
          () -> new ForceTrackBlock(BlockBehaviour.Properties.of(Material.DECORATION)
              .sound(SoundType.GLASS)
              .instabreak()
              .noCollission()
              .randomTicks()));

  public static final RegistryObject<ForceTrackEmitterBlock> FORCE_TRACK_EMITTER =
      BLOCKS.register("force_track_emitter",
          () -> new ForceTrackEmitterBlock(BlockBehaviour.Properties.of(Material.METAL)
              .sound(SoundType.METAL)
              .randomTicks()));

  public static final RegistryObject<TrackBlock> ABANDONED_TRACK =
      BLOCKS.register("abandoned_track",
          () -> new AbandonedTrackBlock(TrackTypes.ABANDONED,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> ABANDONED_LOCKING_TRACK =
      BLOCKS.register("abandoned_locking_track",
          () -> new LockingTrackBlock(TrackTypes.ABANDONED,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> ABANDONED_BUFFER_STOP_TRACK =
      BLOCKS.register("abandoned_buffer_stop_track",
          () -> new BufferStopTrackBlock(TrackTypes.ABANDONED,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> ABANDONED_ACTIVATOR_TRACK =
      BLOCKS.register("abandoned_activator_track",
          () -> new ActivatorTrackBlock(TrackTypes.ABANDONED,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> ABANDONED_BOOSTER_TRACK =
      BLOCKS.register("abandoned_booster_track",
          () -> new BoosterTrackBlock(TrackTypes.ABANDONED,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> ABANDONED_CONTROL_TRACK =
      BLOCKS.register("abandoned_control_track",
          () -> new ControlTrackBlock(TrackTypes.ABANDONED,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> ABANDONED_GATED_TRACK =
      BLOCKS.register("abandoned_gated_track",
          () -> new GatedTrackBlock(TrackTypes.ABANDONED,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> ABANDONED_DETECTOR_TRACK =
      BLOCKS.register("abandoned_detector_track",
          () -> new DetectorTrackBlock(TrackTypes.ABANDONED,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> ABANDONED_COUPLER_TRACK =
      BLOCKS.register("abandoned_coupler_track",
          () -> new CouplerTrackBlock(TrackTypes.ABANDONED,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> ABANDONED_EMBARKING_TRACK =
      BLOCKS.register("abandoned_embarking_track",
          () -> new EmbarkingTrackBlock(TrackTypes.ABANDONED,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> ABANDONED_DISEMBARKING_TRACK =
      BLOCKS.register("abandoned_disembarking_track",
          () -> new DisembarkingTrackBlock(TrackTypes.ABANDONED,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> ABANDONED_WYE_TRACK =
      BLOCKS.register("abandoned_wye_track",
          () -> new WyeTrackBlock(TrackTypes.ABANDONED,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> ABANDONED_TURNOUT_TRACK =
      BLOCKS.register("abandoned_turnout_track",
          () -> new TurnoutTrackBlock(TrackTypes.ABANDONED,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> ABANDONED_JUNCTION_TRACK =
      BLOCKS.register("abandoned_junction_track",
          () -> new JunctionTrackBlock(TrackTypes.ABANDONED,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> ABANDONED_LAUNCHER_TRACK =
      BLOCKS.register("abandoned_launcher_track",
          () -> new LauncherTrackBlock(TrackTypes.ABANDONED,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> ELECTRIC_TRACK =
      BLOCKS.register("electric_track",
          () -> new TrackBlock(TrackTypes.ELECTRIC,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .randomTicks()
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> ELECTRIC_LOCKING_TRACK =
      BLOCKS.register("electric_locking_track",
          () -> new LockingTrackBlock(TrackTypes.ELECTRIC,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .randomTicks()
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> ELECTRIC_BUFFER_STOP_TRACK =
      BLOCKS.register("electric_buffer_stop_track",
          () -> new BufferStopTrackBlock(TrackTypes.ELECTRIC,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .randomTicks()
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> ELECTRIC_ACTIVATOR_TRACK =
      BLOCKS.register("electric_activator_track",
          () -> new ActivatorTrackBlock(TrackTypes.ELECTRIC,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .randomTicks()
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> ELECTRIC_BOOSTER_TRACK =
      BLOCKS.register("electric_booster_track",
          () -> new BoosterTrackBlock(TrackTypes.ELECTRIC,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .randomTicks()
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> ELECTRIC_CONTROL_TRACK =
      BLOCKS.register("electric_control_track",
          () -> new ControlTrackBlock(TrackTypes.ELECTRIC,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .randomTicks()
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, 3.5F)));

  public static final RegistryObject<TrackBlock> ELECTRIC_GATED_TRACK =
      BLOCKS.register("electric_gated_track",
          () -> new GatedTrackBlock(TrackTypes.ELECTRIC,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .randomTicks()
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> ELECTRIC_DETECTOR_TRACK =
      BLOCKS.register("electric_detector_track",
          () -> new DetectorTrackBlock(TrackTypes.ELECTRIC,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .randomTicks()
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> ELECTRIC_COUPLER_TRACK =
      BLOCKS.register("electric_coupler_track",
          () -> new CouplerTrackBlock(TrackTypes.ELECTRIC,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> ELECTRIC_EMBARKING_TRACK =
      BLOCKS.register("electric_embarking_track",
          () -> new EmbarkingTrackBlock(TrackTypes.ELECTRIC,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> ELECTRIC_DISEMBARKING_TRACK =
      BLOCKS.register("electric_disembarking_track",
          () -> new DisembarkingTrackBlock(TrackTypes.ELECTRIC,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> ELECTRIC_WYE_TRACK =
      BLOCKS.register("electric_wye_track",
          () -> new WyeTrackBlock(TrackTypes.ELECTRIC,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> ELECTRIC_TURNOUT_TRACK =
      BLOCKS.register("electric_turnout_track",
          () -> new TurnoutTrackBlock(TrackTypes.ELECTRIC,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> ELECTRIC_JUNCTION_TRACK =
      BLOCKS.register("electric_junction_track",
          () -> new JunctionTrackBlock(TrackTypes.ELECTRIC,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> ELECTRIC_LAUNCHER_TRACK =
      BLOCKS.register("electric_launcher_track",
          () -> new LauncherTrackBlock(TrackTypes.ELECTRIC,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> HIGH_SPEED_TRACK =
      BLOCKS.register("high_speed_track",
          () -> new TrackBlock(TrackTypes.HIGH_SPEED,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> HIGH_SPEED_TRANSITION_TRACK =
      BLOCKS.register("high_speed_transition_track",
          () -> new TransitionTrackBlock(TrackTypes.HIGH_SPEED,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> HIGH_SPEED_LOCKING_TRACK =
      BLOCKS.register("high_speed_locking_track",
          () -> new LockingTrackBlock(TrackTypes.HIGH_SPEED,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> HIGH_SPEED_ACTIVATOR_TRACK =
      BLOCKS.register("high_speed_activator_track",
          () -> new ActivatorTrackBlock(TrackTypes.HIGH_SPEED,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> HIGH_SPEED_BOOSTER_TRACK =
      BLOCKS.register("high_speed_booster_track",
          () -> new BoosterTrackBlock(TrackTypes.HIGH_SPEED,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> HIGH_SPEED_DETECTOR_TRACK =
      BLOCKS.register("high_speed_detector_track",
          () -> new DetectorTrackBlock(TrackTypes.HIGH_SPEED,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> HIGH_SPEED_WYE_TRACK =
      BLOCKS.register("high_speed_wye_track",
          () -> new WyeTrackBlock(TrackTypes.HIGH_SPEED,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> HIGH_SPEED_TURNOUT_TRACK =
      BLOCKS.register("high_speed_turnout_track",
          () -> new TurnoutTrackBlock(TrackTypes.HIGH_SPEED,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> HIGH_SPEED_JUNCTION_TRACK =
      BLOCKS.register("high_speed_junction_track",
          () -> new JunctionTrackBlock(TrackTypes.HIGH_SPEED,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> HIGH_SPEED_ELECTRIC_TRACK =
      BLOCKS.register("high_speed_electric_track",
          () -> new TrackBlock(TrackTypes.HIGH_SPEED_ELECTRIC,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .randomTicks()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> HIGH_SPEED_ELECTRIC_TRANSITION_TRACK =
      BLOCKS.register("high_speed_electric_transition_track",
          () -> new TransitionTrackBlock(TrackTypes.HIGH_SPEED_ELECTRIC,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .randomTicks()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> HIGH_SPEED_ELECTRIC_LOCKING_TRACK =
      BLOCKS.register("high_speed_electric_locking_track",
          () -> new LockingTrackBlock(TrackTypes.HIGH_SPEED_ELECTRIC,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .randomTicks()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> HIGH_SPEED_ELECTRIC_ACTIVATOR_TRACK =
      BLOCKS.register("high_speed_electric_activator_track",
          () -> new ActivatorTrackBlock(TrackTypes.HIGH_SPEED_ELECTRIC,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .randomTicks()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> HIGH_SPEED_ELECTRIC_BOOSTER_TRACK =
      BLOCKS.register("high_speed_electric_booster_track",
          () -> new BoosterTrackBlock(TrackTypes.HIGH_SPEED_ELECTRIC,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .randomTicks()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> HIGH_SPEED_ELECTRIC_DETECTOR_TRACK =
      BLOCKS.register("high_speed_electric_detector_track",
          () -> new DetectorTrackBlock(TrackTypes.HIGH_SPEED_ELECTRIC,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .randomTicks()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> HIGH_SPEED_ELECTRIC_WYE_TRACK =
      BLOCKS.register("high_speed_electric_wye_track",
          () -> new WyeTrackBlock(TrackTypes.HIGH_SPEED_ELECTRIC,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .randomTicks()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> HIGH_SPEED_ELECTRIC_TURNOUT_TRACK =
      BLOCKS.register("high_speed_electric_turnout_track",
          () -> new TurnoutTrackBlock(TrackTypes.HIGH_SPEED_ELECTRIC,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .randomTicks()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> HIGH_SPEED_ELECTRIC_JUNCTION_TRACK =
      BLOCKS.register("high_speed_electric_junction_track",
          () -> new JunctionTrackBlock(TrackTypes.HIGH_SPEED_ELECTRIC,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .randomTicks()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> IRON_LOCKING_TRACK =
      BLOCKS.register("iron_locking_track",
          () -> new LockingTrackBlock(TrackTypes.IRON,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .randomTicks()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> IRON_BUFFER_STOP_TRACK =
      BLOCKS.register("iron_buffer_stop_track",
          () -> new BufferStopTrackBlock(TrackTypes.IRON,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .randomTicks()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> IRON_ACTIVATOR_TRACK =
      BLOCKS.register("iron_activator_track",
          () -> new ActivatorTrackBlock(TrackTypes.IRON,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .randomTicks()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> IRON_BOOSTER_TRACK =
      BLOCKS.register("iron_booster_track",
          () -> new BoosterTrackBlock(TrackTypes.IRON,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .randomTicks()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> IRON_CONTROL_TRACK =
      BLOCKS.register("iron_control_track",
          () -> new ControlTrackBlock(TrackTypes.IRON,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .randomTicks()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> IRON_GATED_TRACK =
      BLOCKS.register("iron_gated_track",
          () -> new GatedTrackBlock(TrackTypes.IRON,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .randomTicks()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> IRON_DETECTOR_TRACK =
      BLOCKS.register("iron_detector_track",
          () -> new DetectorTrackBlock(TrackTypes.IRON,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .randomTicks()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> IRON_COUPLER_TRACK =
      BLOCKS.register("iron_coupler_track",
          () -> new CouplerTrackBlock(TrackTypes.IRON,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> IRON_EMBARKING_TRACK =
      BLOCKS.register("iron_embarking_track",
          () -> new EmbarkingTrackBlock(TrackTypes.IRON,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> IRON_DISEMBARKING_TRACK =
      BLOCKS.register("iron_disembarking_track",
          () -> new DisembarkingTrackBlock(TrackTypes.IRON,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> IRON_WYE_TRACK =
      BLOCKS.register("iron_wye_track",
          () -> new WyeTrackBlock(TrackTypes.IRON,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> IRON_TURNOUT_TRACK =
      BLOCKS.register("iron_turnout_track",
          () -> new TurnoutTrackBlock(TrackTypes.IRON,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> IRON_JUNCTION_TRACK =
      BLOCKS.register("iron_junction_track",
          () -> new JunctionTrackBlock(TrackTypes.IRON,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> IRON_LAUNCHER_TRACK =
      BLOCKS.register("iron_launcher_track",
          () -> new LauncherTrackBlock(TrackTypes.IRON,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> REINFORCED_TRACK =
      BLOCKS.register("reinforced_track",
          () -> new TrackBlock(TrackTypes.REINFORCED,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.REINFORCED_RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> REINFORCED_LOCKING_TRACK =
      BLOCKS.register("reinforced_locking_track",
          () -> new LockingTrackBlock(TrackTypes.REINFORCED,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.REINFORCED_RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> REINFORCED_BUFFER_STOP_TRACK =
      BLOCKS.register("reinforced_buffer_stop_track",
          () -> new BufferStopTrackBlock(TrackTypes.REINFORCED,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.REINFORCED_RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> REINFORCED_ACTIVATOR_TRACK =
      BLOCKS.register("reinforced_activator_track",
          () -> new ActivatorTrackBlock(TrackTypes.REINFORCED,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.REINFORCED_RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> REINFORCED_BOOSTER_TRACK =
      BLOCKS.register("reinforced_booster_track",
          () -> new BoosterTrackBlock(TrackTypes.REINFORCED,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.REINFORCED_RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> REINFORCED_CONTROL_TRACK =
      BLOCKS.register("reinforced_control_track",
          () -> new ControlTrackBlock(TrackTypes.REINFORCED,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.REINFORCED_RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> REINFORCED_GATED_TRACK =
      BLOCKS.register("reinforced_gated_track",
          () -> new GatedTrackBlock(TrackTypes.REINFORCED,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.REINFORCED_RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> REINFORCED_DETECTOR_TRACK =
      BLOCKS.register("reinforced_detector_track",
          () -> new DetectorTrackBlock(TrackTypes.REINFORCED,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.REINFORCED_RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> REINFORCED_COUPLER_TRACK =
      BLOCKS.register("reinforced_coupler_track",
          () -> new CouplerTrackBlock(TrackTypes.REINFORCED,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.REINFORCED_RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> REINFORCED_EMBARKING_TRACK =
      BLOCKS.register("reinforced_embarking_track",
          () -> new EmbarkingTrackBlock(TrackTypes.REINFORCED,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.REINFORCED_RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> REINFORCED_DISEMBARKING_TRACK =
      BLOCKS.register("reinforced_disembarking_track",
          () -> new DisembarkingTrackBlock(TrackTypes.REINFORCED,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.REINFORCED_RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> REINFORCED_WYE_TRACK =
      BLOCKS.register("reinforced_wye_track",
          () -> new WyeTrackBlock(TrackTypes.REINFORCED,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.REINFORCED_RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> REINFORCED_TURNOUT_TRACK =
      BLOCKS.register("reinforced_turnout_track",
          () -> new TurnoutTrackBlock(TrackTypes.REINFORCED,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.REINFORCED_RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> REINFORCED_JUNCTION_TRACK =
      BLOCKS.register("reinforced_junction_track",
          () -> new JunctionTrackBlock(TrackTypes.REINFORCED,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.REINFORCED_RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> REINFORCED_LAUNCHER_TRACK =
      BLOCKS.register("reinforced_launcher_track",
          () -> new LauncherTrackBlock(TrackTypes.REINFORCED,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.REINFORCED_RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> STRAP_IRON_TRACK =
      BLOCKS.register("strap_iron_track",
          () -> new TrackBlock(TrackTypes.STRAP_IRON,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> STRAP_IRON_LOCKING_TRACK =
      BLOCKS.register("strap_iron_locking_track",
          () -> new LockingTrackBlock(TrackTypes.STRAP_IRON,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> STRAP_IRON_BUFFER_STOP_TRACK =
      BLOCKS.register("strap_iron_buffer_stop_track",
          () -> new BufferStopTrackBlock(TrackTypes.STRAP_IRON,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> STRAP_IRON_ACTIVATOR_TRACK =
      BLOCKS.register("strap_iron_activator_track",
          () -> new ActivatorTrackBlock(TrackTypes.STRAP_IRON,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> STRAP_IRON_BOOSTER_TRACK =
      BLOCKS.register("strap_iron_booster_track",
          () -> new BoosterTrackBlock(TrackTypes.STRAP_IRON,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> STRAP_IRON_CONTROL_TRACK =
      BLOCKS.register("strap_iron_control_track",
          () -> new ControlTrackBlock(TrackTypes.STRAP_IRON,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> STRAP_IRON_GATED_TRACK =
      BLOCKS.register("strap_iron_gated_track",
          () -> new GatedTrackBlock(TrackTypes.STRAP_IRON,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> STRAP_IRON_DETECTOR_TRACK =
      BLOCKS.register("strap_iron_detector_track",
          () -> new DetectorTrackBlock(TrackTypes.STRAP_IRON,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> STRAP_IRON_COUPLER_TRACK =
      BLOCKS.register("strap_iron_coupler_track",
          () -> new CouplerTrackBlock(TrackTypes.STRAP_IRON,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> STRAP_IRON_EMBARKING_TRACK =
      BLOCKS.register("strap_iron_embarking_track",
          () -> new EmbarkingTrackBlock(TrackTypes.STRAP_IRON,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> STRAP_IRON_DISEMBARKING_TRACK =
      BLOCKS.register("strap_iron_disembarking_track",
          () -> new DisembarkingTrackBlock(TrackTypes.STRAP_IRON,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> STRAP_IRON_WYE_TRACK =
      BLOCKS.register("strap_iron_wye_track",
          () -> new WyeTrackBlock(TrackTypes.STRAP_IRON,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> STRAP_IRON_TURNOUT_TRACK =
      BLOCKS.register("strap_iron_turnout_track",
          () -> new TurnoutTrackBlock(TrackTypes.STRAP_IRON,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> STRAP_IRON_JUNCTION_TRACK =
      BLOCKS.register("strap_iron_junction_track",
          () -> new JunctionTrackBlock(TrackTypes.STRAP_IRON,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> STRAP_IRON_LAUNCHER_TRACK =
      BLOCKS.register("strap_iron_launcher_track",
          () -> new LauncherTrackBlock(TrackTypes.STRAP_IRON,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<Block> ELEVATOR_TRACK =
      BLOCKS.register("elevator_track",
          () -> new ElevatorTrackBlock(BlockBehaviour.Properties.of(RailcraftMaterials.ELEVATOR)
              .noCollission()
              .strength(1.05F)
              .sound(SoundType.METAL)));

  // firestone (ORE)
  public static final RegistryObject<Block> FIRESTONE =
      BLOCKS.register("firestone",
          () -> new FirestoneBlock(BlockBehaviour.Properties.of(Material.STONE)
              .lightLevel(__ -> 15)
              .strength(3, 5)));

  public static final RegistryObject<Block> RITUAL =
      BLOCKS.register("ritual",
          () -> new RitualBlock(BlockBehaviour.Properties.of(Material.STONE)
              .lightLevel(__ -> 1)
              .noOcclusion()));

  public static final RegistryObject<Block> MANUAL_ROLLING_MACHINE =
      BLOCKS.register("manual_rolling_machine",
          () -> new ManualRollingMachineBlock(BlockBehaviour.Properties.of(Material.WOOD)
              .sound(SoundType.WOOD)
              .strength(2.0F)));

  public static final RegistryObject<Block> COKE_OVEN_BRICKS =
      BLOCKS.register("coke_oven_bricks",
          () -> new CokeOvenBricksBlock(BlockBehaviour.Properties.of(Material.STONE)
              .sound(SoundType.STONE)
              .lightLevel(litBlockEmission(13))
              .strength(2F, 6.0F)));

  public static final RegistryObject<LiquidBlock> CREOSOTE =
      BLOCKS.register("creosote",
          () -> new LiquidBlock(RailcraftFluids.CREOSOTE,
              BlockBehaviour.Properties.of(Material.WATER)
                  .noCollission().strength(50.0F).noDrops()));

  /* == ORES == */

  // UNUSED! ONLY FOR 1.16 BACKPORT
  // public static final RegistryObject<OreBlock> COPPER_ORE =
  //     BLOCKS.register("copper_ore",
  //       () -> new OreBlock(
  //         BlockBehaviour.Properties.of(Material.STONE)
  //             .requiresCorrectToolForDrops()
  //             .strength(3.0F, 3.0F)));

  public static final RegistryObject<OreBlock> TIN_ORE =
      BLOCKS.register("tin_ore",
        () -> new OreBlock(
          BlockBehaviour.Properties.of(Material.STONE)
              .requiresCorrectToolForDrops()
              .strength(3.0F, 3.0F)));

  public static final RegistryObject<OreBlock> DEEPSLATE_TIN_ORE =
      BLOCKS.register("deepslate_tin_ore",
        () -> new OreBlock(
          BlockBehaviour.Properties.copy(TIN_ORE.get())
              .color(MaterialColor.DEEPSLATE)
              .strength(4.5F, 3.0F)
              .sound(SoundType.DEEPSLATE)));

  // public static final Block GOLD_ORE = register("gold_ore", new OreBlock(BlockBehaviour.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(3.0F, 3.0F)));
  // public static final Block DEEPSLATE_GOLD_ORE = register("deepslate_gold_ore", new OreBlock(BlockBehaviour.Properties.copy(GOLD_ORE).color(MaterialColor.DEEPSLATE).strength(4.5F, 3.0F).sound(SoundType.DEEPSLATE)));

  public static final RegistryObject<Block> BLACK_POST =
      BLOCKS.register("black_post",
          () -> new PostBlock(BlockBehaviour.Properties.of(Material.METAL)
              .sound(SoundType.METAL)));

  public static final RegistryObject<Block> RED_POST =
      BLOCKS.register("red_post",
          () -> new PostBlock(BlockBehaviour.Properties.of(Material.METAL)
              .sound(SoundType.METAL)));

  public static final RegistryObject<Block> GREEN_POST =
      BLOCKS.register("green_post",
          () -> new PostBlock(BlockBehaviour.Properties.of(Material.METAL)
              .sound(SoundType.METAL)));

  public static final RegistryObject<Block> BROWN_POST =
      BLOCKS.register("brown_post",
          () -> new PostBlock(BlockBehaviour.Properties.of(Material.METAL)
              .sound(SoundType.METAL)));

  public static final RegistryObject<Block> BLUE_POST =
      BLOCKS.register("blue_post",
          () -> new PostBlock(BlockBehaviour.Properties.of(Material.METAL)
              .sound(SoundType.METAL)));

  public static final RegistryObject<Block> PURPLE_POST =
      BLOCKS.register("purple_post",
          () -> new PostBlock(BlockBehaviour.Properties.of(Material.METAL)
              .sound(SoundType.METAL)));

  public static final RegistryObject<Block> CYAN_POST =
      BLOCKS.register("cyan_post",
          () -> new PostBlock(BlockBehaviour.Properties.of(Material.METAL)
              .sound(SoundType.METAL)));

  public static final RegistryObject<Block> LIGHT_GRAY_POST =
      BLOCKS.register("light_gray_post",
          () -> new PostBlock(BlockBehaviour.Properties.of(Material.METAL)
              .sound(SoundType.METAL)));

  public static final RegistryObject<Block> GRAY_POST =
      BLOCKS.register("gray_post",
          () -> new PostBlock(BlockBehaviour.Properties.of(Material.METAL)
              .sound(SoundType.METAL)));

  public static final RegistryObject<Block> PINK_POST =
      BLOCKS.register("pink_post",
          () -> new PostBlock(BlockBehaviour.Properties.of(Material.METAL)
              .sound(SoundType.METAL)));

  public static final RegistryObject<Block> LIME_POST =
      BLOCKS.register("lime_post",
          () -> new PostBlock(BlockBehaviour.Properties.of(Material.METAL)
              .sound(SoundType.METAL)));

  public static final RegistryObject<Block> YELLOW_POST =
      BLOCKS.register("yellow_post",
          () -> new PostBlock(BlockBehaviour.Properties.of(Material.METAL)
              .sound(SoundType.METAL)));

  public static final RegistryObject<Block> LIGHT_BLUE_POST =
      BLOCKS.register("light_blue_post",
          () -> new PostBlock(BlockBehaviour.Properties.of(Material.METAL)
              .sound(SoundType.METAL)));

  public static final RegistryObject<Block> MAGENTA_POST =
      BLOCKS.register("magenta_post",
          () -> new PostBlock(BlockBehaviour.Properties.of(Material.METAL)
              .sound(SoundType.METAL)));

  public static final RegistryObject<Block> ORANGE_POST =
      BLOCKS.register("orange_post",
          () -> new PostBlock(BlockBehaviour.Properties.of(Material.METAL)
              .sound(SoundType.METAL)));

  public static final RegistryObject<Block> WHITE_POST =
      BLOCKS.register("white_post",
          () -> new PostBlock(BlockBehaviour.Properties.of(Material.METAL)
              .sound(SoundType.METAL)));

  private static ToIntFunction<BlockState> litBlockEmission(int light) {
    return blockState -> blockState.getValue(BlockStateProperties.LIT) ? light : 0;
  }

  private static Boolean never(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos,
      EntityType<?> entityType) {
    return false;
  }

  private static boolean never(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos) {
    return false;
  }

}
