package mods.railcraft.world.level.block;

import mods.railcraft.Railcraft;
import mods.railcraft.world.level.block.manipulator.*;
import mods.railcraft.world.level.block.post.PostBlock;
import mods.railcraft.world.level.block.signal.*;
import mods.railcraft.world.level.block.steamboiler.FluidFueledFireboxBlock;
import mods.railcraft.world.level.block.steamboiler.SolidFueledFireboxBlock;
import mods.railcraft.world.level.block.steamboiler.SteamBoilerTankBlock;
import mods.railcraft.world.level.block.tank.*;
import mods.railcraft.world.level.block.track.*;
import mods.railcraft.world.level.block.track.actuator.SwitchTrackActuatorBlock;
import mods.railcraft.world.level.block.track.actuator.SwitchTrackLeverBlock;
import mods.railcraft.world.level.block.track.actuator.SwitchTrackMotorBlock;
import mods.railcraft.world.level.block.track.outfitted.*;
import mods.railcraft.world.level.material.RailcraftMaterials;
import mods.railcraft.world.level.material.fluid.RailcraftFluids;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.HashSet;
import java.util.Set;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;

public class RailcraftBlocks {

  private static final DeferredRegister<Block> deferredRegister =
      DeferredRegister.create(ForgeRegistries.BLOCKS, Railcraft.ID);
  private static final Set<Block> REGISTERED_BLOCKS = new HashSet<>();

  public static void register(IEventBus modEventBus) {
    deferredRegister.register(modEventBus);
  }

  public static Set<Block> getRegisteredBlocks() {
    if(REGISTERED_BLOCKS.isEmpty()) {
      deferredRegister.getEntries()
        .stream()
        .map(RegistryObject::get)
        .collect(Collectors.toCollection(() -> REGISTERED_BLOCKS));
    }
    return REGISTERED_BLOCKS;
  }

  public static final RegistryObject<Block> WHITE_STRENGTHENED_GLASS =
      deferredRegister.register("white_strengthened_glass",
          () -> new StrengthenedGlassBlock(BlockBehaviour.Properties.of(Material.GLASS)
              .sound(SoundType.GLASS)
              .noOcclusion()
              .strength(1.0F, 5.0F)
              .isValidSpawn(RailcraftBlocks::never)
              .isRedstoneConductor(RailcraftBlocks::never)
              .isSuffocating(RailcraftBlocks::never)
              .isViewBlocking(RailcraftBlocks::never)));

  public static final RegistryObject<Block> ORANGE_STRENGTHENED_GLASS =
      deferredRegister.register("orange_strengthened_glass",
          () -> new StrengthenedGlassBlock(BlockBehaviour.Properties.of(Material.GLASS)
              .sound(SoundType.GLASS)
              .noOcclusion()
              .strength(1.0F, 5.0F)
              .isValidSpawn(RailcraftBlocks::never)
              .isRedstoneConductor(RailcraftBlocks::never)
              .isSuffocating(RailcraftBlocks::never)
              .isViewBlocking(RailcraftBlocks::never)));

  public static final RegistryObject<Block> MAGENTA_STRENGTHENED_GLASS =
      deferredRegister.register("magenta_strengthened_glass",
          () -> new StrengthenedGlassBlock(BlockBehaviour.Properties.of(Material.GLASS)
              .sound(SoundType.GLASS)
              .noOcclusion()
              .strength(1.0F, 5.0F)
              .isValidSpawn(RailcraftBlocks::never)
              .isRedstoneConductor(RailcraftBlocks::never)
              .isSuffocating(RailcraftBlocks::never)
              .isViewBlocking(RailcraftBlocks::never)));

  public static final RegistryObject<Block> LIGHT_BLUE_STRENGTHENED_GLASS =
      deferredRegister.register("light_blue_strengthened_glass",
          () -> new StrengthenedGlassBlock(BlockBehaviour.Properties.of(Material.GLASS)
              .sound(SoundType.GLASS)
              .noOcclusion()
              .strength(1.0F, 5.0F)
              .isValidSpawn(RailcraftBlocks::never)
              .isRedstoneConductor(RailcraftBlocks::never)
              .isSuffocating(RailcraftBlocks::never)
              .isViewBlocking(RailcraftBlocks::never)));

  public static final RegistryObject<Block> YELLOW_STRENGTHENED_GLASS =
      deferredRegister.register("yellow_strengthened_glass",
          () -> new StrengthenedGlassBlock(BlockBehaviour.Properties.of(Material.GLASS)
              .sound(SoundType.GLASS)
              .noOcclusion()
              .strength(1.0F, 5.0F)
              .isValidSpawn(RailcraftBlocks::never)
              .isRedstoneConductor(RailcraftBlocks::never)
              .isSuffocating(RailcraftBlocks::never)
              .isViewBlocking(RailcraftBlocks::never)));

  public static final RegistryObject<Block> LIME_STRENGTHENED_GLASS =
      deferredRegister.register("lime_strengthened_glass",
          () -> new StrengthenedGlassBlock(BlockBehaviour.Properties.of(Material.GLASS)
              .sound(SoundType.GLASS)
              .noOcclusion()
              .strength(1.0F, 5.0F)
              .isValidSpawn(RailcraftBlocks::never)
              .isRedstoneConductor(RailcraftBlocks::never)
              .isSuffocating(RailcraftBlocks::never)
              .isViewBlocking(RailcraftBlocks::never)));

  public static final RegistryObject<Block> PINK_STRENGTHENED_GLASS =
      deferredRegister.register("pink_strengthened_glass",
          () -> new StrengthenedGlassBlock(BlockBehaviour.Properties.of(Material.GLASS)
              .sound(SoundType.GLASS)
              .noOcclusion()
              .strength(1.0F, 5.0F)
              .isValidSpawn(RailcraftBlocks::never)
              .isRedstoneConductor(RailcraftBlocks::never)
              .isSuffocating(RailcraftBlocks::never)
              .isViewBlocking(RailcraftBlocks::never)));

  public static final RegistryObject<Block> GRAY_STRENGTHENED_GLASS =
      deferredRegister.register("gray_strengthened_glass",
          () -> new StrengthenedGlassBlock(BlockBehaviour.Properties.of(Material.GLASS)
              .sound(SoundType.GLASS)
              .noOcclusion()
              .strength(1.0F, 5.0F)
              .isValidSpawn(RailcraftBlocks::never)
              .isRedstoneConductor(RailcraftBlocks::never)
              .isSuffocating(RailcraftBlocks::never)
              .isViewBlocking(RailcraftBlocks::never)));

  public static final RegistryObject<Block> LIGHT_GRAY_STRENGTHENED_GLASS =
      deferredRegister.register("light_gray_strengthened_glass",
          () -> new StrengthenedGlassBlock(BlockBehaviour.Properties.of(Material.GLASS)
              .sound(SoundType.GLASS)
              .noOcclusion()
              .strength(1.0F, 5.0F)
              .isValidSpawn(RailcraftBlocks::never)
              .isRedstoneConductor(RailcraftBlocks::never)
              .isSuffocating(RailcraftBlocks::never)
              .isViewBlocking(RailcraftBlocks::never)));

  public static final RegistryObject<Block> CYAN_STRENGTHENED_GLASS =
      deferredRegister.register("cyan_strengthened_glass",
          () -> new StrengthenedGlassBlock(BlockBehaviour.Properties.of(Material.GLASS)
              .sound(SoundType.GLASS)
              .noOcclusion()
              .strength(1.0F, 5.0F)
              .isValidSpawn(RailcraftBlocks::never)
              .isRedstoneConductor(RailcraftBlocks::never)
              .isSuffocating(RailcraftBlocks::never)
              .isViewBlocking(RailcraftBlocks::never)));

  public static final RegistryObject<Block> PURPLE_STRENGTHENED_GLASS =
      deferredRegister.register("purple_strengthened_glass",
          () -> new StrengthenedGlassBlock(BlockBehaviour.Properties.of(Material.GLASS)
              .sound(SoundType.GLASS)
              .noOcclusion()
              .strength(1.0F, 5.0F)
              .isValidSpawn(RailcraftBlocks::never)
              .isRedstoneConductor(RailcraftBlocks::never)
              .isSuffocating(RailcraftBlocks::never)
              .isViewBlocking(RailcraftBlocks::never)));

  public static final RegistryObject<Block> BLUE_STRENGTHENED_GLASS =
      deferredRegister.register("blue_strengthened_glass",
          () -> new StrengthenedGlassBlock(BlockBehaviour.Properties.of(Material.GLASS)
              .sound(SoundType.GLASS)
              .noOcclusion()
              .strength(1.0F, 5.0F)
              .isValidSpawn(RailcraftBlocks::never)
              .isRedstoneConductor(RailcraftBlocks::never)
              .isSuffocating(RailcraftBlocks::never)
              .isViewBlocking(RailcraftBlocks::never)));

  public static final RegistryObject<Block> BROWN_STRENGTHENED_GLASS =
      deferredRegister.register("brown_strengthened_glass",
          () -> new StrengthenedGlassBlock(BlockBehaviour.Properties.of(Material.GLASS)
              .sound(SoundType.GLASS)
              .noOcclusion()
              .strength(1.0F, 5.0F)
              .isValidSpawn(RailcraftBlocks::never)
              .isRedstoneConductor(RailcraftBlocks::never)
              .isSuffocating(RailcraftBlocks::never)
              .isViewBlocking(RailcraftBlocks::never)));

  public static final RegistryObject<Block> GREEN_STRENGTHENED_GLASS =
      deferredRegister.register("green_strengthened_glass",
          () -> new StrengthenedGlassBlock(BlockBehaviour.Properties.of(Material.GLASS)
              .sound(SoundType.GLASS)
              .noOcclusion()
              .strength(1.0F, 5.0F)
              .isValidSpawn(RailcraftBlocks::never)
              .isRedstoneConductor(RailcraftBlocks::never)
              .isSuffocating(RailcraftBlocks::never)
              .isViewBlocking(RailcraftBlocks::never)));

  public static final RegistryObject<Block> RED_STRENGTHENED_GLASS =
      deferredRegister.register("red_strengthened_glass",
          () -> new StrengthenedGlassBlock(BlockBehaviour.Properties.of(Material.GLASS)
              .sound(SoundType.GLASS)
              .noOcclusion()
              .strength(1.0F, 5.0F)
              .isValidSpawn(RailcraftBlocks::never)
              .isRedstoneConductor(RailcraftBlocks::never)
              .isSuffocating(RailcraftBlocks::never)
              .isViewBlocking(RailcraftBlocks::never)));

  public static final RegistryObject<Block> BLACK_STRENGTHENED_GLASS =
      deferredRegister.register("black_strengthened_glass",
          () -> new StrengthenedGlassBlock(BlockBehaviour.Properties.of(Material.GLASS)
              .sound(SoundType.GLASS)
              .noOcclusion()
              .strength(1.0F, 5.0F)
              .isValidSpawn(RailcraftBlocks::never)
              .isRedstoneConductor(RailcraftBlocks::never)
              .isSuffocating(RailcraftBlocks::never)
              .isViewBlocking(RailcraftBlocks::never)));

  public static final RegistryObject<Block> WHITE_IRON_TANK_GAUGE =
      deferredRegister.register("white_iron_tank_gauge",
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
      deferredRegister.register("orange_iron_tank_gauge",
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
      deferredRegister.register("magenta_iron_tank_gauge",
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
      deferredRegister.register("light_blue_iron_tank_gauge",
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
      deferredRegister.register("yellow_iron_tank_gauge",
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
      deferredRegister.register("lime_iron_tank_gauge",
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
      deferredRegister.register("pink_iron_tank_gauge",
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
      deferredRegister.register("gray_iron_tank_gauge",
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
      deferredRegister.register("light_gray_iron_tank_gauge",
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
      deferredRegister.register("cyan_iron_tank_gauge",
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
      deferredRegister.register("purple_iron_tank_gauge",
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
      deferredRegister.register("blue_iron_tank_gauge",
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
      deferredRegister.register("brown_iron_tank_gauge",
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
      deferredRegister.register("green_iron_tank_gauge",
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
      deferredRegister.register("red_iron_tank_gauge",
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
      deferredRegister.register("black_iron_tank_gauge",
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
      deferredRegister.register("white_iron_tank_valve",
          () -> new IronTankValveBlock(BlockBehaviour.Properties.of(Material.METAL)
              .sound(SoundType.METAL)
              .noOcclusion()
              .explosionResistance(12)));

  public static final RegistryObject<Block> ORANGE_IRON_TANK_VALVE =
      deferredRegister.register("orange_iron_tank_valve",
          () -> new IronTankValveBlock(BlockBehaviour.Properties.of(Material.METAL)
              .sound(SoundType.METAL)
              .noOcclusion()
              .explosionResistance(12)));

  public static final RegistryObject<Block> MAGENTA_IRON_TANK_VALVE =
      deferredRegister.register("magenta_iron_tank_valve",
          () -> new IronTankValveBlock(BlockBehaviour.Properties.of(Material.METAL)
              .sound(SoundType.METAL)
              .noOcclusion()
              .explosionResistance(12)));

  public static final RegistryObject<Block> LIGHT_BLUE_IRON_TANK_VALVE =
      deferredRegister.register("light_blue_iron_tank_valve",
          () -> new IronTankValveBlock(BlockBehaviour.Properties.of(Material.METAL)
              .sound(SoundType.METAL)
              .noOcclusion()
              .explosionResistance(12)));

  public static final RegistryObject<Block> YELLOW_IRON_TANK_VALVE =
      deferredRegister.register("yellow_iron_tank_valve",
          () -> new IronTankValveBlock(BlockBehaviour.Properties.of(Material.METAL)
              .sound(SoundType.METAL)
              .noOcclusion()
              .explosionResistance(12)));

  public static final RegistryObject<Block> LIME_IRON_TANK_VALVE =
      deferredRegister.register("lime_iron_tank_valve",
          () -> new IronTankValveBlock(BlockBehaviour.Properties.of(Material.METAL)
              .sound(SoundType.METAL)
              .noOcclusion()
              .explosionResistance(12)));

  public static final RegistryObject<Block> PINK_IRON_TANK_VALVE =
      deferredRegister.register("pink_iron_tank_valve",
          () -> new IronTankValveBlock(BlockBehaviour.Properties.of(Material.METAL)
              .sound(SoundType.METAL)
              .noOcclusion()
              .explosionResistance(12)));

  public static final RegistryObject<Block> GRAY_IRON_TANK_VALVE =
      deferredRegister.register("gray_iron_tank_valve",
          () -> new IronTankValveBlock(BlockBehaviour.Properties.of(Material.METAL)
              .sound(SoundType.METAL)
              .noOcclusion()
              .explosionResistance(12)));

  public static final RegistryObject<Block> LIGHT_GRAY_IRON_TANK_VALVE =
      deferredRegister.register("light_gray_iron_tank_valve",
          () -> new IronTankValveBlock(BlockBehaviour.Properties.of(Material.METAL)
              .sound(SoundType.METAL)
              .noOcclusion()
              .explosionResistance(12)));

  public static final RegistryObject<Block> CYAN_IRON_TANK_VALVE =
      deferredRegister.register("cyan_iron_tank_valve",
          () -> new IronTankValveBlock(BlockBehaviour.Properties.of(Material.METAL)
              .sound(SoundType.METAL)
              .noOcclusion()
              .explosionResistance(12)));

  public static final RegistryObject<Block> PURPLE_IRON_TANK_VALVE =
      deferredRegister.register("purple_iron_tank_valve",
          () -> new IronTankValveBlock(BlockBehaviour.Properties.of(Material.METAL)
              .sound(SoundType.METAL)
              .noOcclusion()
              .explosionResistance(12)));

  public static final RegistryObject<Block> BLUE_IRON_TANK_VALVE =
      deferredRegister.register("blue_iron_tank_valve",
          () -> new IronTankValveBlock(BlockBehaviour.Properties.of(Material.METAL)
              .sound(SoundType.METAL)
              .noOcclusion()
              .explosionResistance(12)));

  public static final RegistryObject<Block> BROWN_IRON_TANK_VALVE =
      deferredRegister.register("brown_iron_tank_valve",
          () -> new IronTankValveBlock(BlockBehaviour.Properties.of(Material.METAL)
              .sound(SoundType.METAL)
              .noOcclusion()
              .explosionResistance(12)));

  public static final RegistryObject<Block> GREEN_IRON_TANK_VALVE =
      deferredRegister.register("green_iron_tank_valve",
          () -> new IronTankValveBlock(BlockBehaviour.Properties.of(Material.METAL)
              .sound(SoundType.METAL)
              .noOcclusion()
              .explosionResistance(12)));

  public static final RegistryObject<Block> RED_IRON_TANK_VALVE =
      deferredRegister.register("red_iron_tank_valve",
          () -> new IronTankValveBlock(BlockBehaviour.Properties.of(Material.METAL)
              .sound(SoundType.METAL)
              .noOcclusion()
              .explosionResistance(12)));

  public static final RegistryObject<Block> BLACK_IRON_TANK_VALVE =
      deferredRegister.register("black_iron_tank_valve",
          () -> new IronTankValveBlock(BlockBehaviour.Properties.of(Material.METAL)
              .sound(SoundType.METAL)
              .noOcclusion()
              .explosionResistance(12)));

  public static final RegistryObject<Block> WHITE_IRON_TANK_WALL =
      deferredRegister.register("white_iron_tank_wall",
          () -> new IronTankWallBlock(BlockBehaviour.Properties.of(Material.METAL)
              .sound(SoundType.METAL)
              .noOcclusion()
              .explosionResistance(12)));

  public static final RegistryObject<Block> ORANGE_IRON_TANK_WALL =
      deferredRegister.register("orange_iron_tank_wall",
          () -> new IronTankWallBlock(BlockBehaviour.Properties.of(Material.METAL)
              .sound(SoundType.METAL)
              .noOcclusion()
              .explosionResistance(12)));

  public static final RegistryObject<Block> MAGENTA_IRON_TANK_WALL =
      deferredRegister.register("magenta_iron_tank_wall",
          () -> new IronTankWallBlock(BlockBehaviour.Properties.of(Material.METAL)
              .sound(SoundType.METAL)
              .noOcclusion()
              .explosionResistance(12)));

  public static final RegistryObject<Block> LIGHT_BLUE_IRON_TANK_WALL =
      deferredRegister.register("light_blue_iron_tank_wall",
          () -> new IronTankWallBlock(BlockBehaviour.Properties.of(Material.METAL)
              .sound(SoundType.METAL)
              .noOcclusion()
              .explosionResistance(12)));

  public static final RegistryObject<Block> YELLOW_IRON_TANK_WALL =
      deferredRegister.register("yellow_iron_tank_wall",
          () -> new IronTankWallBlock(BlockBehaviour.Properties.of(Material.METAL)
              .sound(SoundType.METAL)
              .noOcclusion()
              .explosionResistance(12)));

  public static final RegistryObject<Block> LIME_IRON_TANK_WALL =
      deferredRegister.register("lime_iron_tank_wall",
          () -> new IronTankWallBlock(BlockBehaviour.Properties.of(Material.METAL)
              .sound(SoundType.METAL)
              .noOcclusion()
              .explosionResistance(12)));

  public static final RegistryObject<Block> PINK_IRON_TANK_WALL =
      deferredRegister.register("pink_iron_tank_wall",
          () -> new IronTankWallBlock(BlockBehaviour.Properties.of(Material.METAL)
              .sound(SoundType.METAL)
              .noOcclusion()
              .explosionResistance(12)));

  public static final RegistryObject<Block> GRAY_IRON_TANK_WALL =
      deferredRegister.register("gray_iron_tank_wall",
          () -> new IronTankWallBlock(BlockBehaviour.Properties.of(Material.METAL)
              .sound(SoundType.METAL)
              .noOcclusion()
              .explosionResistance(12)));

  public static final RegistryObject<Block> LIGHT_GRAY_IRON_TANK_WALL =
      deferredRegister.register("light_gray_iron_tank_wall",
          () -> new IronTankWallBlock(BlockBehaviour.Properties.of(Material.METAL)
              .sound(SoundType.METAL)
              .noOcclusion()
              .explosionResistance(12)));

  public static final RegistryObject<Block> CYAN_IRON_TANK_WALL =
      deferredRegister.register("cyan_iron_tank_wall",
          () -> new IronTankWallBlock(BlockBehaviour.Properties.of(Material.METAL)
              .sound(SoundType.METAL)
              .noOcclusion()
              .explosionResistance(12)));

  public static final RegistryObject<Block> PURPLE_IRON_TANK_WALL =
      deferredRegister.register("purple_iron_tank_wall",
          () -> new IronTankWallBlock(BlockBehaviour.Properties.of(Material.METAL)
              .sound(SoundType.METAL)
              .noOcclusion()
              .explosionResistance(12)));

  public static final RegistryObject<Block> BLUE_IRON_TANK_WALL =
      deferredRegister.register("blue_iron_tank_wall",
          () -> new IronTankWallBlock(BlockBehaviour.Properties.of(Material.METAL)
              .sound(SoundType.METAL)
              .noOcclusion()
              .explosionResistance(12)));

  public static final RegistryObject<Block> BROWN_IRON_TANK_WALL =
      deferredRegister.register("brown_iron_tank_wall",
          () -> new IronTankWallBlock(BlockBehaviour.Properties.of(Material.METAL)
              .sound(SoundType.METAL)
              .noOcclusion()
              .explosionResistance(12)));

  public static final RegistryObject<Block> GREEN_IRON_TANK_WALL =
      deferredRegister.register("green_iron_tank_wall",
          () -> new IronTankWallBlock(BlockBehaviour.Properties.of(Material.METAL)
              .sound(SoundType.METAL)
              .noOcclusion()
              .explosionResistance(12)));

  public static final RegistryObject<Block> RED_IRON_TANK_WALL =
      deferredRegister.register("red_iron_tank_wall",
          () -> new IronTankWallBlock(BlockBehaviour.Properties.of(Material.METAL)
              .sound(SoundType.METAL)
              .noOcclusion()
              .explosionResistance(12)));

  public static final RegistryObject<Block> BLACK_IRON_TANK_WALL =
      deferredRegister.register("black_iron_tank_wall",
          () -> new IronTankWallBlock(BlockBehaviour.Properties.of(Material.METAL)
              .sound(SoundType.METAL)
              .noOcclusion()
              .explosionResistance(12)));

  public static final RegistryObject<Block> WHITE_STEEL_TANK_GAUGE =
      deferredRegister.register("white_steel_tank_gauge",
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
      deferredRegister.register("orange_steel_tank_gauge",
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
      deferredRegister.register("magenta_steel_tank_gauge",
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
      deferredRegister.register("light_blue_steel_tank_gauge",
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
      deferredRegister.register("yellow_steel_tank_gauge",
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
      deferredRegister.register("lime_steel_tank_gauge",
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
      deferredRegister.register("pink_steel_tank_gauge",
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
      deferredRegister.register("gray_steel_tank_gauge",
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
      deferredRegister.register("light_gray_steel_tank_gauge",
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
      deferredRegister.register("cyan_steel_tank_gauge",
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
      deferredRegister.register("purple_steel_tank_gauge",
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
      deferredRegister.register("blue_steel_tank_gauge",
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
      deferredRegister.register("brown_steel_tank_gauge",
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
      deferredRegister.register("green_steel_tank_gauge",
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
      deferredRegister.register("red_steel_tank_gauge",
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
      deferredRegister.register("black_steel_tank_gauge",
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
      deferredRegister.register("white_steel_tank_valve",
          () -> new SteelTankValveBlock(BlockBehaviour.Properties.of(Material.METAL)
              .sound(SoundType.METAL)
              .noOcclusion()
              .explosionResistance(15)));

  public static final RegistryObject<Block> ORANGE_STEEL_TANK_VALVE =
      deferredRegister.register("orange_steel_tank_valve",
          () -> new SteelTankValveBlock(BlockBehaviour.Properties.of(Material.METAL)
              .sound(SoundType.METAL)
              .noOcclusion()
              .explosionResistance(15)));

  public static final RegistryObject<Block> MAGENTA_STEEL_TANK_VALVE =
      deferredRegister.register("magenta_steel_tank_valve",
          () -> new SteelTankValveBlock(BlockBehaviour.Properties.of(Material.METAL)
              .sound(SoundType.METAL)
              .noOcclusion()
              .explosionResistance(15)));

  public static final RegistryObject<Block> LIGHT_BLUE_STEEL_TANK_VALVE =
      deferredRegister.register("light_blue_steel_tank_valve",
          () -> new SteelTankValveBlock(BlockBehaviour.Properties.of(Material.METAL)
              .sound(SoundType.METAL)
              .noOcclusion()
              .explosionResistance(15)));

  public static final RegistryObject<Block> YELLOW_STEEL_TANK_VALVE =
      deferredRegister.register("yellow_steel_tank_valve",
          () -> new SteelTankValveBlock(BlockBehaviour.Properties.of(Material.METAL)
              .sound(SoundType.METAL)
              .noOcclusion()
              .explosionResistance(15)));

  public static final RegistryObject<Block> LIME_STEEL_TANK_VALVE =
      deferredRegister.register("lime_steel_tank_valve",
          () -> new SteelTankValveBlock(BlockBehaviour.Properties.of(Material.METAL)
              .sound(SoundType.METAL)
              .noOcclusion()
              .explosionResistance(15)));

  public static final RegistryObject<Block> PINK_STEEL_TANK_VALVE =
      deferredRegister.register("pink_steel_tank_valve",
          () -> new SteelTankValveBlock(BlockBehaviour.Properties.of(Material.METAL)
              .sound(SoundType.METAL)
              .noOcclusion()
              .explosionResistance(15)));

  public static final RegistryObject<Block> GRAY_STEEL_TANK_VALVE =
      deferredRegister.register("gray_steel_tank_valve",
          () -> new SteelTankValveBlock(BlockBehaviour.Properties.of(Material.METAL)
              .sound(SoundType.METAL)
              .noOcclusion()
              .explosionResistance(15)));

  public static final RegistryObject<Block> LIGHT_GRAY_STEEL_TANK_VALVE =
      deferredRegister.register("light_gray_steel_tank_valve",
          () -> new SteelTankValveBlock(BlockBehaviour.Properties.of(Material.METAL)
              .sound(SoundType.METAL)
              .noOcclusion()
              .explosionResistance(15)));

  public static final RegistryObject<Block> CYAN_STEEL_TANK_VALVE =
      deferredRegister.register("cyan_steel_tank_valve",
          () -> new SteelTankValveBlock(BlockBehaviour.Properties.of(Material.METAL)
              .sound(SoundType.METAL)
              .noOcclusion()
              .explosionResistance(15)));

  public static final RegistryObject<Block> PURPLE_STEEL_TANK_VALVE =
      deferredRegister.register("purple_steel_tank_valve",
          () -> new SteelTankValveBlock(BlockBehaviour.Properties.of(Material.METAL)
              .sound(SoundType.METAL)
              .noOcclusion()
              .explosionResistance(15)));

  public static final RegistryObject<Block> BLUE_STEEL_TANK_VALVE =
      deferredRegister.register("blue_steel_tank_valve",
          () -> new SteelTankValveBlock(BlockBehaviour.Properties.of(Material.METAL)
              .sound(SoundType.METAL)
              .noOcclusion()
              .explosionResistance(15)));

  public static final RegistryObject<Block> BROWN_STEEL_TANK_VALVE =
      deferredRegister.register("brown_steel_tank_valve",
          () -> new SteelTankValveBlock(BlockBehaviour.Properties.of(Material.METAL)
              .sound(SoundType.METAL)
              .noOcclusion()
              .explosionResistance(15)));

  public static final RegistryObject<Block> GREEN_STEEL_TANK_VALVE =
      deferredRegister.register("green_steel_tank_valve",
          () -> new SteelTankValveBlock(BlockBehaviour.Properties.of(Material.METAL)
              .sound(SoundType.METAL)
              .noOcclusion()
              .explosionResistance(15)));

  public static final RegistryObject<Block> RED_STEEL_TANK_VALVE =
      deferredRegister.register("red_steel_tank_valve",
          () -> new SteelTankValveBlock(BlockBehaviour.Properties.of(Material.METAL)
              .sound(SoundType.METAL)
              .noOcclusion()
              .explosionResistance(15)));

  public static final RegistryObject<Block> BLACK_STEEL_TANK_VALVE =
      deferredRegister.register("black_steel_tank_valve",
          () -> new SteelTankValveBlock(BlockBehaviour.Properties.of(Material.METAL)
              .sound(SoundType.METAL)
              .noOcclusion()
              .explosionResistance(15)));

  public static final RegistryObject<Block> WHITE_STEEL_TANK_WALL =
      deferredRegister.register("white_steel_tank_wall",
          () -> new SteelTankWallBlock(BlockBehaviour.Properties.of(Material.METAL)
              .sound(SoundType.METAL)
              .noOcclusion()
              .explosionResistance(15)));

  public static final RegistryObject<Block> ORANGE_STEEL_TANK_WALL =
      deferredRegister.register("orange_steel_tank_wall",
          () -> new SteelTankWallBlock(BlockBehaviour.Properties.of(Material.METAL)
              .sound(SoundType.METAL)
              .noOcclusion()
              .explosionResistance(15)));

  public static final RegistryObject<Block> MAGENTA_STEEL_TANK_WALL =
      deferredRegister.register("magenta_steel_tank_wall",
          () -> new SteelTankWallBlock(BlockBehaviour.Properties.of(Material.METAL)
              .sound(SoundType.METAL)
              .noOcclusion()
              .explosionResistance(15)));

  public static final RegistryObject<Block> LIGHT_BLUE_STEEL_TANK_WALL =
      deferredRegister.register("light_blue_steel_tank_wall",
          () -> new SteelTankWallBlock(BlockBehaviour.Properties.of(Material.METAL)
              .sound(SoundType.METAL)
              .noOcclusion()
              .explosionResistance(15)));

  public static final RegistryObject<Block> YELLOW_STEEL_TANK_WALL =
      deferredRegister.register("yellow_steel_tank_wall",
          () -> new SteelTankWallBlock(BlockBehaviour.Properties.of(Material.METAL)
              .sound(SoundType.METAL)
              .noOcclusion()
              .explosionResistance(15)));

  public static final RegistryObject<Block> LIME_STEEL_TANK_WALL =
      deferredRegister.register("lime_steel_tank_wall",
          () -> new SteelTankWallBlock(BlockBehaviour.Properties.of(Material.METAL)
              .sound(SoundType.METAL)
              .noOcclusion()
              .explosionResistance(15)));

  public static final RegistryObject<Block> PINK_STEEL_TANK_WALL =
      deferredRegister.register("pink_steel_tank_wall",
          () -> new SteelTankWallBlock(BlockBehaviour.Properties.of(Material.METAL)
              .sound(SoundType.METAL)
              .noOcclusion()
              .explosionResistance(15)));

  public static final RegistryObject<Block> GRAY_STEEL_TANK_WALL =
      deferredRegister.register("gray_steel_tank_wall",
          () -> new SteelTankWallBlock(BlockBehaviour.Properties.of(Material.METAL)
              .sound(SoundType.METAL)
              .noOcclusion()
              .explosionResistance(15)));

  public static final RegistryObject<Block> LIGHT_GRAY_STEEL_TANK_WALL =
      deferredRegister.register("light_gray_steel_tank_wall",
          () -> new SteelTankWallBlock(BlockBehaviour.Properties.of(Material.METAL)
              .sound(SoundType.METAL)
              .noOcclusion()
              .explosionResistance(15)));

  public static final RegistryObject<Block> CYAN_STEEL_TANK_WALL =
      deferredRegister.register("cyan_steel_tank_wall",
          () -> new SteelTankWallBlock(BlockBehaviour.Properties.of(Material.METAL)
              .sound(SoundType.METAL)
              .noOcclusion()
              .explosionResistance(15)));

  public static final RegistryObject<Block> PURPLE_STEEL_TANK_WALL =
      deferredRegister.register("purple_steel_tank_wall",
          () -> new SteelTankWallBlock(BlockBehaviour.Properties.of(Material.METAL)
              .sound(SoundType.METAL)
              .noOcclusion()
              .explosionResistance(15)));

  public static final RegistryObject<Block> BLUE_STEEL_TANK_WALL =
      deferredRegister.register("blue_steel_tank_wall",
          () -> new SteelTankWallBlock(BlockBehaviour.Properties.of(Material.METAL)
              .sound(SoundType.METAL)
              .noOcclusion()
              .explosionResistance(15)));

  public static final RegistryObject<Block> BROWN_STEEL_TANK_WALL =
      deferredRegister.register("brown_steel_tank_wall",
          () -> new SteelTankWallBlock(BlockBehaviour.Properties.of(Material.METAL)
              .sound(SoundType.METAL)
              .noOcclusion()
              .explosionResistance(15)));

  public static final RegistryObject<Block> GREEN_STEEL_TANK_WALL =
      deferredRegister.register("green_steel_tank_wall",
          () -> new SteelTankWallBlock(BlockBehaviour.Properties.of(Material.METAL)
              .sound(SoundType.METAL)
              .noOcclusion()
              .explosionResistance(15)));

  public static final RegistryObject<Block> RED_STEEL_TANK_WALL =
      deferredRegister.register("red_steel_tank_wall",
          () -> new SteelTankWallBlock(BlockBehaviour.Properties.of(Material.METAL)
              .sound(SoundType.METAL)
              .noOcclusion()
              .explosionResistance(15)));

  public static final RegistryObject<Block> BLACK_STEEL_TANK_WALL =
      deferredRegister.register("black_steel_tank_wall",
          () -> new SteelTankWallBlock(BlockBehaviour.Properties.of(Material.METAL)
              .sound(SoundType.METAL)
              .noOcclusion()
              .explosionResistance(15)));

  public static final RegistryObject<Block> LOW_PRESSURE_STEAM_BOILER_TANK =
      deferredRegister.register("low_pressure_steam_boiler_tank",
          () -> new SteamBoilerTankBlock(BlockBehaviour.Properties.of(Material.METAL)
              .noOcclusion()
              .sound(SoundType.METAL)));

  public static final RegistryObject<Block> HIGH_PRESSURE_STEAM_BOILER_TANK =
      deferredRegister.register("high_pressure_steam_boiler_tank",
          () -> new SteamBoilerTankBlock(BlockBehaviour.Properties.of(Material.METAL)
              .noOcclusion()
              .sound(SoundType.METAL)));

  public static final RegistryObject<Block> SOLID_FUELED_FIREBOX =
      deferredRegister.register("solid_fueled_firebox",
          () -> new SolidFueledFireboxBlock(BlockBehaviour.Properties.of(Material.STONE)
              .lightLevel(litBlockEmission(13))
              .sound(SoundType.STONE)));

  public static final RegistryObject<Block> FLUID_FUELED_FIREBOX =
      deferredRegister.register("fluid_fueled_firebox",
          () -> new FluidFueledFireboxBlock(BlockBehaviour.Properties.of(Material.STONE)
              .lightLevel(litBlockEmission(13))
              .sound(SoundType.METAL)));

  public static final RegistryObject<Block> STEAM_TURBINE =
      deferredRegister.register("steam_turbine",
          () -> new SteamTurbineBlock(BlockBehaviour.Properties.of(Material.METAL)
              .randomTicks()
              .sound(SoundType.METAL)));

  public static final RegistryObject<Block> BLAST_FURNACE_BRICKS =
      deferredRegister.register("blast_furnace_bricks",
          () -> new BlastFurnaceBricksBlock(BlockBehaviour.Properties.of(Material.STONE)
              .lightLevel(litBlockEmission(13))
              .sound(SoundType.STONE)));

  public static final RegistryObject<Block> FEED_STATION =
      deferredRegister.register("feed_station",
          () -> new FeedStationBlock(
              BlockBehaviour.Properties.of(Material.STONE, MaterialColor.WOOD)
                  .sound(SoundType.WOOD)));

  public static final RegistryObject<Block> STEEL_ANVIL =
      deferredRegister.register("steel_anvil",
          () -> new AnvilBlock(
              BlockBehaviour.Properties.of(Material.HEAVY_METAL, MaterialColor.METAL)
                  .requiresCorrectToolForDrops()
                  .strength(5.0F, 2000.0F)
                  .sound(SoundType.ANVIL)));

  public static final RegistryObject<Block> CHIPPED_STEEL_ANVIL =
      deferredRegister.register("chipped_steel_anvil",
          () -> new AnvilBlock(
              BlockBehaviour.Properties.of(Material.HEAVY_METAL, MaterialColor.METAL)
                  .requiresCorrectToolForDrops()
                  .strength(5.0F, 2000.0F)
                  .sound(SoundType.ANVIL)));

  public static final RegistryObject<Block> DAMAGED_STEEL_ANVIL =
      deferredRegister.register("damaged_steel_anvil",
          () -> new AnvilBlock(
              BlockBehaviour.Properties.of(Material.HEAVY_METAL, MaterialColor.METAL)
                  .requiresCorrectToolForDrops()
                  .strength(5.0F, 2000.0F)
                  .sound(SoundType.ANVIL)));

  public static final RegistryObject<Block> STEEL_BLOCK =
      deferredRegister.register("steel_block",
          () -> new Block(BlockBehaviour.Properties.of(Material.METAL)
              .strength(5.0F, 15.0F)
              .sound(SoundType.METAL)));

  public static final RegistryObject<FluidLoaderBlock> FLUID_LOADER =
      deferredRegister.register("fluid_loader",
          () -> new FluidLoaderBlock(BlockBehaviour.Properties.of(Material.STONE)
              .sound(SoundType.STONE)
              .noOcclusion()));

  public static final RegistryObject<FluidUnloaderBlock> FLUID_UNLOADER =
      deferredRegister.register("fluid_unloader",
          () -> new FluidUnloaderBlock(BlockBehaviour.Properties.of(Material.STONE)
              .sound(SoundType.STONE)
              .noOcclusion()));

  public static final RegistryObject<AdvancedItemLoaderBlock> ADVANCED_ITEM_LOADER =
      deferredRegister.register("advanced_item_loader",
          () -> new AdvancedItemLoaderBlock(BlockBehaviour.Properties.of(Material.STONE)
              .sound(SoundType.STONE)));

  public static final RegistryObject<AdvancedItemUnloaderBlock> ADVANCED_ITEM_UNLOADER =
      deferredRegister.register("advanced_item_unloader",
          () -> new AdvancedItemUnloaderBlock(BlockBehaviour.Properties.of(Material.STONE)
              .sound(SoundType.STONE)));

  public static final RegistryObject<ItemLoaderBlock> ITEM_LOADER =
      deferredRegister.register("item_loader",
          () -> new ItemLoaderBlock(BlockBehaviour.Properties.of(Material.STONE)
              .sound(SoundType.STONE)));

  public static final RegistryObject<ItemUnloaderBlock> ITEM_UNLOADER =
      deferredRegister.register("item_unloader",
          () -> new ItemUnloaderBlock(BlockBehaviour.Properties.of(Material.STONE)
              .sound(SoundType.STONE)));

  public static final RegistryObject<SwitchTrackActuatorBlock> SWITCH_TRACK_LEVER =
      deferredRegister.register("switch_track_lever",
          () -> new SwitchTrackLeverBlock(BlockBehaviour.Properties.of(Material.DECORATION)
              .strength(8.0F, 50.0F)
              .sound(SoundType.METAL)
              .noOcclusion()));

  public static final RegistryObject<SwitchTrackActuatorBlock> SWITCH_TRACK_MOTOR =
      deferredRegister.register("switch_track_motor",
          () -> new SwitchTrackMotorBlock(BlockBehaviour.Properties.of(Material.DECORATION)
              .strength(8.0F, 50.0F)
              .sound(SoundType.METAL)
              .noOcclusion()));

  public static final RegistryObject<SignalBoxBlock> ANALOG_SIGNAL_CONTROLLER_BOX =
      deferredRegister.register("analog_signal_controller_box",
          () -> new AnalogSignalControllerBoxBlock(
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .strength(8.0F, 50.0F)
                  .sound(SoundType.METAL)
                  .noOcclusion()));

  public static final RegistryObject<SignalBoxBlock> SIGNAL_SEQUENCER_BOX =
      deferredRegister.register("signal_sequencer_box",
          () -> new SignalSequencerBoxBlock(BlockBehaviour.Properties.of(Material.DECORATION)
              .strength(8.0F, 50.0F)
              .sound(SoundType.METAL)
              .noOcclusion()));

  public static final RegistryObject<SignalBoxBlock> SIGNAL_CAPACITOR_BOX =
      deferredRegister.register("signal_capacitor_box",
          () -> new SignalCapacitorBoxBlock(BlockBehaviour.Properties.of(Material.DECORATION)
              .strength(8.0F, 50.0F)
              .sound(SoundType.METAL)
              .noOcclusion()));

  public static final RegistryObject<SignalBoxBlock> SIGNAL_INTERLOCK_BOX =
      deferredRegister.register("signal_interlock_box",
          () -> new SignalInterlockBoxBlock(BlockBehaviour.Properties.of(Material.DECORATION)
              .strength(8.0F, 50.0F)
              .sound(SoundType.METAL)
              .noOcclusion()));

  public static final RegistryObject<SignalBoxBlock> BLOCK_SIGNAL_RELAY_BOX =
      deferredRegister.register("block_signal_relay_box",
          () -> new BlockSignalRelayBoxBlock(BlockBehaviour.Properties.of(Material.DECORATION)
              .strength(8.0F, 50.0F)
              .sound(SoundType.METAL)
              .noOcclusion()));

  public static final RegistryObject<SignalBoxBlock> SIGNAL_RECEIVER_BOX =
      deferredRegister.register("signal_receiver_box",
          () -> new SignalReceiverBoxBlock(BlockBehaviour.Properties.of(Material.DECORATION)
              .strength(8.0F, 50.0F)
              .sound(SoundType.METAL)
              .noOcclusion()));

  public static final RegistryObject<SignalBoxBlock> SIGNAL_CONTROLLER_BOX =
      deferredRegister.register("signal_controller_box",
          () -> new SignalControllerBoxBlock(BlockBehaviour.Properties.of(Material.DECORATION)
              .strength(8.0F, 50.0F)
              .sound(SoundType.METAL)
              .noOcclusion()));

  public static final RegistryObject<DualSignalBlock> DUAL_BLOCK_SIGNAL =
      deferredRegister.register("dual_block_signal",
          () -> new DualBlockSignalBlock(BlockBehaviour.Properties.of(Material.DECORATION)
              .strength(8.0F, 50.0F)
              .noOcclusion()));

  public static final RegistryObject<DualSignalBlock> DUAL_DISTANT_SIGNAL =
      deferredRegister.register("dual_distant_signal",
          () -> new DualDistantSignalBlock(BlockBehaviour.Properties.of(Material.DECORATION)
              .strength(8.0F, 50.0F)
              .noOcclusion()));

  public static final RegistryObject<DualSignalBlock> DUAL_TOKEN_SIGNAL =
      deferredRegister.register("dual_token_signal",
          () -> new DualTokenSignalBlock(BlockBehaviour.Properties.of(Material.DECORATION)
              .strength(8.0F, 50.0F)
              .noOcclusion()));

  public static final RegistryObject<SingleSignalBlock> BLOCK_SIGNAL =
      deferredRegister.register("block_signal",
          () -> new BlockSignalBlock(BlockBehaviour.Properties.of(Material.DECORATION)
              .strength(8.0F, 50.0F)
              .noOcclusion()));

  public static final RegistryObject<SingleSignalBlock> DISTANT_SIGNAL =
      deferredRegister.register("distant_signal",
          () -> new DistantSignalBlock(BlockBehaviour.Properties.of(Material.DECORATION)
              .strength(8.0F, 50.0F)
              .noOcclusion()));

  public static final RegistryObject<SingleSignalBlock> TOKEN_SIGNAL =
      deferredRegister.register("token_signal",
          () -> new TokenSignalBlock(BlockBehaviour.Properties.of(Material.DECORATION)
              .strength(8.0F, 50.0F)
              .noOcclusion()));

  public static final RegistryObject<ForceTrackBlock> FORCE_TRACK =
      deferredRegister.register("force_track",
          () -> new ForceTrackBlock(BlockBehaviour.Properties.of(Material.DECORATION)
              .sound(SoundType.GLASS)
              .instabreak()
              .noCollission()
              .randomTicks()));

  public static final RegistryObject<ForceTrackEmitterBlock> FORCE_TRACK_EMITTER =
      deferredRegister.register("force_track_emitter",
          () -> new ForceTrackEmitterBlock(BlockBehaviour.Properties.of(Material.METAL)
              .sound(SoundType.METAL)
              .randomTicks()));

  public static final RegistryObject<TrackBlock> ABANDONED_TRACK =
      deferredRegister.register("abandoned_track",
          () -> new AbandonedTrackBlock(TrackTypes.ABANDONED,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> ABANDONED_LOCKING_TRACK =
      deferredRegister.register("abandoned_locking_track",
          () -> new LockingTrackBlock(TrackTypes.ABANDONED,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> ABANDONED_BUFFER_STOP_TRACK =
      deferredRegister.register("abandoned_buffer_stop_track",
          () -> new BufferStopTrackBlock(TrackTypes.ABANDONED,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> ABANDONED_ACTIVATOR_TRACK =
      deferredRegister.register("abandoned_activator_track",
          () -> new ActivatorTrackBlock(TrackTypes.ABANDONED,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> ABANDONED_BOOSTER_TRACK =
      deferredRegister.register("abandoned_booster_track",
          () -> new BoosterTrackBlock(TrackTypes.ABANDONED,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> ABANDONED_CONTROL_TRACK =
      deferredRegister.register("abandoned_control_track",
          () -> new ControlTrackBlock(TrackTypes.ABANDONED,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> ABANDONED_GATED_TRACK =
      deferredRegister.register("abandoned_gated_track",
          () -> new GatedTrackBlock(TrackTypes.ABANDONED,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> ABANDONED_DETECTOR_TRACK =
      deferredRegister.register("abandoned_detector_track",
          () -> new DetectorTrackBlock(TrackTypes.ABANDONED,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> ABANDONED_COUPLER_TRACK =
      deferredRegister.register("abandoned_coupler_track",
          () -> new CouplerTrackBlock(TrackTypes.ABANDONED,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> ABANDONED_EMBARKING_TRACK =
      deferredRegister.register("abandoned_embarking_track",
          () -> new EmbarkingTrackBlock(TrackTypes.ABANDONED,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> ABANDONED_DISEMBARKING_TRACK =
      deferredRegister.register("abandoned_disembarking_track",
          () -> new DisembarkingTrackBlock(TrackTypes.ABANDONED,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> ABANDONED_WYE_TRACK =
      deferredRegister.register("abandoned_wye_track",
          () -> new WyeTrackBlock(TrackTypes.ABANDONED,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> ABANDONED_TURNOUT_TRACK =
      deferredRegister.register("abandoned_turnout_track",
          () -> new TurnoutTrackBlock(TrackTypes.ABANDONED,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> ABANDONED_JUNCTION_TRACK =
      deferredRegister.register("abandoned_junction_track",
          () -> new JunctionTrackBlock(TrackTypes.ABANDONED,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> ABANDONED_LAUNCHER_TRACK =
      deferredRegister.register("abandoned_launcher_track",
          () -> new LauncherTrackBlock(TrackTypes.ABANDONED,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> ELECTRIC_TRACK =
      deferredRegister.register("electric_track",
          () -> new TrackBlock(TrackTypes.ELECTRIC,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .randomTicks()
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> ELECTRIC_LOCKING_TRACK =
      deferredRegister.register("electric_locking_track",
          () -> new LockingTrackBlock(TrackTypes.ELECTRIC,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .randomTicks()
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> ELECTRIC_BUFFER_STOP_TRACK =
      deferredRegister.register("electric_buffer_stop_track",
          () -> new BufferStopTrackBlock(TrackTypes.ELECTRIC,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .randomTicks()
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> ELECTRIC_ACTIVATOR_TRACK =
      deferredRegister.register("electric_activator_track",
          () -> new ActivatorTrackBlock(TrackTypes.ELECTRIC,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .randomTicks()
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> ELECTRIC_BOOSTER_TRACK =
      deferredRegister.register("electric_booster_track",
          () -> new BoosterTrackBlock(TrackTypes.ELECTRIC,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .randomTicks()
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> ELECTRIC_CONTROL_TRACK =
      deferredRegister.register("electric_control_track",
          () -> new ControlTrackBlock(TrackTypes.ELECTRIC,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .randomTicks()
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, 3.5F)));

  public static final RegistryObject<TrackBlock> ELECTRIC_GATED_TRACK =
      deferredRegister.register("electric_gated_track",
          () -> new GatedTrackBlock(TrackTypes.ELECTRIC,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .randomTicks()
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> ELECTRIC_DETECTOR_TRACK =
      deferredRegister.register("electric_detector_track",
          () -> new DetectorTrackBlock(TrackTypes.ELECTRIC,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .randomTicks()
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> ELECTRIC_COUPLER_TRACK =
      deferredRegister.register("electric_coupler_track",
          () -> new CouplerTrackBlock(TrackTypes.ELECTRIC,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> ELECTRIC_EMBARKING_TRACK =
      deferredRegister.register("electric_embarking_track",
          () -> new EmbarkingTrackBlock(TrackTypes.ELECTRIC,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> ELECTRIC_DISEMBARKING_TRACK =
      deferredRegister.register("electric_disembarking_track",
          () -> new DisembarkingTrackBlock(TrackTypes.ELECTRIC,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> ELECTRIC_WYE_TRACK =
      deferredRegister.register("electric_wye_track",
          () -> new WyeTrackBlock(TrackTypes.ELECTRIC,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> ELECTRIC_TURNOUT_TRACK =
      deferredRegister.register("electric_turnout_track",
          () -> new TurnoutTrackBlock(TrackTypes.ELECTRIC,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> ELECTRIC_JUNCTION_TRACK =
      deferredRegister.register("electric_junction_track",
          () -> new JunctionTrackBlock(TrackTypes.ELECTRIC,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> ELECTRIC_LAUNCHER_TRACK =
      deferredRegister.register("electric_launcher_track",
          () -> new LauncherTrackBlock(TrackTypes.ELECTRIC,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> HIGH_SPEED_TRACK =
      deferredRegister.register("high_speed_track",
          () -> new TrackBlock(TrackTypes.HIGH_SPEED,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> HIGH_SPEED_TRANSITION_TRACK =
      deferredRegister.register("high_speed_transition_track",
          () -> new TransitionTrackBlock(TrackTypes.HIGH_SPEED,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> HIGH_SPEED_LOCKING_TRACK =
      deferredRegister.register("high_speed_locking_track",
          () -> new LockingTrackBlock(TrackTypes.HIGH_SPEED,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> HIGH_SPEED_ACTIVATOR_TRACK =
      deferredRegister.register("high_speed_activator_track",
          () -> new ActivatorTrackBlock(TrackTypes.HIGH_SPEED,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> HIGH_SPEED_BOOSTER_TRACK =
      deferredRegister.register("high_speed_booster_track",
          () -> new BoosterTrackBlock(TrackTypes.HIGH_SPEED,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> HIGH_SPEED_DETECTOR_TRACK =
      deferredRegister.register("high_speed_detector_track",
          () -> new DetectorTrackBlock(TrackTypes.HIGH_SPEED,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> HIGH_SPEED_WYE_TRACK =
      deferredRegister.register("high_speed_wye_track",
          () -> new WyeTrackBlock(TrackTypes.HIGH_SPEED,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> HIGH_SPEED_TURNOUT_TRACK =
      deferredRegister.register("high_speed_turnout_track",
          () -> new TurnoutTrackBlock(TrackTypes.HIGH_SPEED,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> HIGH_SPEED_JUNCTION_TRACK =
      deferredRegister.register("high_speed_junction_track",
          () -> new JunctionTrackBlock(TrackTypes.HIGH_SPEED,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> HIGH_SPEED_ELECTRIC_TRACK =
      deferredRegister.register("high_speed_electric_track",
          () -> new TrackBlock(TrackTypes.HIGH_SPEED_ELECTRIC,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .randomTicks()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> HIGH_SPEED_ELECTRIC_TRANSITION_TRACK =
      deferredRegister.register("high_speed_electric_transition_track",
          () -> new TransitionTrackBlock(TrackTypes.HIGH_SPEED_ELECTRIC,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .randomTicks()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> HIGH_SPEED_ELECTRIC_LOCKING_TRACK =
      deferredRegister.register("high_speed_electric_locking_track",
          () -> new LockingTrackBlock(TrackTypes.HIGH_SPEED_ELECTRIC,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .randomTicks()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> HIGH_SPEED_ELECTRIC_ACTIVATOR_TRACK =
      deferredRegister.register("high_speed_electric_activator_track",
          () -> new ActivatorTrackBlock(TrackTypes.HIGH_SPEED_ELECTRIC,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .randomTicks()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> HIGH_SPEED_ELECTRIC_BOOSTER_TRACK =
      deferredRegister.register("high_speed_electric_booster_track",
          () -> new BoosterTrackBlock(TrackTypes.HIGH_SPEED_ELECTRIC,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .randomTicks()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> HIGH_SPEED_ELECTRIC_DETECTOR_TRACK =
      deferredRegister.register("high_speed_electric_detector_track",
          () -> new DetectorTrackBlock(TrackTypes.HIGH_SPEED_ELECTRIC,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .randomTicks()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> HIGH_SPEED_ELECTRIC_WYE_TRACK =
      deferredRegister.register("high_speed_electric_wye_track",
          () -> new WyeTrackBlock(TrackTypes.HIGH_SPEED_ELECTRIC,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .randomTicks()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> HIGH_SPEED_ELECTRIC_TURNOUT_TRACK =
      deferredRegister.register("high_speed_electric_turnout_track",
          () -> new TurnoutTrackBlock(TrackTypes.HIGH_SPEED_ELECTRIC,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .randomTicks()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> HIGH_SPEED_ELECTRIC_JUNCTION_TRACK =
      deferredRegister.register("high_speed_electric_junction_track",
          () -> new JunctionTrackBlock(TrackTypes.HIGH_SPEED_ELECTRIC,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .randomTicks()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> IRON_LOCKING_TRACK =
      deferredRegister.register("iron_locking_track",
          () -> new LockingTrackBlock(TrackTypes.IRON,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .randomTicks()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> IRON_BUFFER_STOP_TRACK =
      deferredRegister.register("iron_buffer_stop_track",
          () -> new BufferStopTrackBlock(TrackTypes.IRON,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .randomTicks()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> IRON_ACTIVATOR_TRACK =
      deferredRegister.register("iron_activator_track",
          () -> new ActivatorTrackBlock(TrackTypes.IRON,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .randomTicks()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> IRON_BOOSTER_TRACK =
      deferredRegister.register("iron_booster_track",
          () -> new BoosterTrackBlock(TrackTypes.IRON,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .randomTicks()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> IRON_CONTROL_TRACK =
      deferredRegister.register("iron_control_track",
          () -> new ControlTrackBlock(TrackTypes.IRON,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .randomTicks()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> IRON_GATED_TRACK =
      deferredRegister.register("iron_gated_track",
          () -> new GatedTrackBlock(TrackTypes.IRON,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .randomTicks()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> IRON_DETECTOR_TRACK =
      deferredRegister.register("iron_detector_track",
          () -> new DetectorTrackBlock(TrackTypes.IRON,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .randomTicks()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> IRON_COUPLER_TRACK =
      deferredRegister.register("iron_coupler_track",
          () -> new CouplerTrackBlock(TrackTypes.IRON,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> IRON_EMBARKING_TRACK =
      deferredRegister.register("iron_embarking_track",
          () -> new EmbarkingTrackBlock(TrackTypes.IRON,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> IRON_DISEMBARKING_TRACK =
      deferredRegister.register("iron_disembarking_track",
          () -> new DisembarkingTrackBlock(TrackTypes.IRON,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> IRON_WYE_TRACK =
      deferredRegister.register("iron_wye_track",
          () -> new WyeTrackBlock(TrackTypes.IRON,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> IRON_TURNOUT_TRACK =
      deferredRegister.register("iron_turnout_track",
          () -> new TurnoutTrackBlock(TrackTypes.IRON,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> IRON_JUNCTION_TRACK =
      deferredRegister.register("iron_junction_track",
          () -> new JunctionTrackBlock(TrackTypes.IRON,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> IRON_LAUNCHER_TRACK =
      deferredRegister.register("iron_launcher_track",
          () -> new LauncherTrackBlock(TrackTypes.IRON,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> REINFORCED_TRACK =
      deferredRegister.register("reinforced_track",
          () -> new TrackBlock(TrackTypes.REINFORCED,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.REINFORCED_RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> REINFORCED_LOCKING_TRACK =
      deferredRegister.register("reinforced_locking_track",
          () -> new LockingTrackBlock(TrackTypes.REINFORCED,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.REINFORCED_RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> REINFORCED_BUFFER_STOP_TRACK =
      deferredRegister.register("reinforced_buffer_stop_track",
          () -> new BufferStopTrackBlock(TrackTypes.REINFORCED,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.REINFORCED_RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> REINFORCED_ACTIVATOR_TRACK =
      deferredRegister.register("reinforced_activator_track",
          () -> new ActivatorTrackBlock(TrackTypes.REINFORCED,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.REINFORCED_RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> REINFORCED_BOOSTER_TRACK =
      deferredRegister.register("reinforced_booster_track",
          () -> new BoosterTrackBlock(TrackTypes.REINFORCED,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.REINFORCED_RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> REINFORCED_CONTROL_TRACK =
      deferredRegister.register("reinforced_control_track",
          () -> new ControlTrackBlock(TrackTypes.REINFORCED,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.REINFORCED_RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> REINFORCED_GATED_TRACK =
      deferredRegister.register("reinforced_gated_track",
          () -> new GatedTrackBlock(TrackTypes.REINFORCED,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.REINFORCED_RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> REINFORCED_DETECTOR_TRACK =
      deferredRegister.register("reinforced_detector_track",
          () -> new DetectorTrackBlock(TrackTypes.REINFORCED,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.REINFORCED_RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> REINFORCED_COUPLER_TRACK =
      deferredRegister.register("reinforced_coupler_track",
          () -> new CouplerTrackBlock(TrackTypes.REINFORCED,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.REINFORCED_RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> REINFORCED_EMBARKING_TRACK =
      deferredRegister.register("reinforced_embarking_track",
          () -> new EmbarkingTrackBlock(TrackTypes.REINFORCED,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.REINFORCED_RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> REINFORCED_DISEMBARKING_TRACK =
      deferredRegister.register("reinforced_disembarking_track",
          () -> new DisembarkingTrackBlock(TrackTypes.REINFORCED,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.REINFORCED_RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> REINFORCED_WYE_TRACK =
      deferredRegister.register("reinforced_wye_track",
          () -> new WyeTrackBlock(TrackTypes.REINFORCED,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.REINFORCED_RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> REINFORCED_TURNOUT_TRACK =
      deferredRegister.register("reinforced_turnout_track",
          () -> new TurnoutTrackBlock(TrackTypes.REINFORCED,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.REINFORCED_RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> REINFORCED_JUNCTION_TRACK =
      deferredRegister.register("reinforced_junction_track",
          () -> new JunctionTrackBlock(TrackTypes.REINFORCED,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.REINFORCED_RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> REINFORCED_LAUNCHER_TRACK =
      deferredRegister.register("reinforced_launcher_track",
          () -> new LauncherTrackBlock(TrackTypes.REINFORCED,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.REINFORCED_RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> STRAP_IRON_TRACK =
      deferredRegister.register("strap_iron_track",
          () -> new TrackBlock(TrackTypes.STRAP_IRON,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> STRAP_IRON_LOCKING_TRACK =
      deferredRegister.register("strap_iron_locking_track",
          () -> new LockingTrackBlock(TrackTypes.STRAP_IRON,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> STRAP_IRON_BUFFER_STOP_TRACK =
      deferredRegister.register("strap_iron_buffer_stop_track",
          () -> new BufferStopTrackBlock(TrackTypes.STRAP_IRON,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> STRAP_IRON_ACTIVATOR_TRACK =
      deferredRegister.register("strap_iron_activator_track",
          () -> new ActivatorTrackBlock(TrackTypes.STRAP_IRON,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> STRAP_IRON_BOOSTER_TRACK =
      deferredRegister.register("strap_iron_booster_track",
          () -> new BoosterTrackBlock(TrackTypes.STRAP_IRON,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> STRAP_IRON_CONTROL_TRACK =
      deferredRegister.register("strap_iron_control_track",
          () -> new ControlTrackBlock(TrackTypes.STRAP_IRON,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> STRAP_IRON_GATED_TRACK =
      deferredRegister.register("strap_iron_gated_track",
          () -> new GatedTrackBlock(TrackTypes.STRAP_IRON,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> STRAP_IRON_DETECTOR_TRACK =
      deferredRegister.register("strap_iron_detector_track",
          () -> new DetectorTrackBlock(TrackTypes.STRAP_IRON,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> STRAP_IRON_COUPLER_TRACK =
      deferredRegister.register("strap_iron_coupler_track",
          () -> new CouplerTrackBlock(TrackTypes.STRAP_IRON,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> STRAP_IRON_EMBARKING_TRACK =
      deferredRegister.register("strap_iron_embarking_track",
          () -> new EmbarkingTrackBlock(TrackTypes.STRAP_IRON,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> STRAP_IRON_DISEMBARKING_TRACK =
      deferredRegister.register("strap_iron_disembarking_track",
          () -> new DisembarkingTrackBlock(TrackTypes.STRAP_IRON,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> STRAP_IRON_WYE_TRACK =
      deferredRegister.register("strap_iron_wye_track",
          () -> new WyeTrackBlock(TrackTypes.STRAP_IRON,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> STRAP_IRON_TURNOUT_TRACK =
      deferredRegister.register("strap_iron_turnout_track",
          () -> new TurnoutTrackBlock(TrackTypes.STRAP_IRON,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> STRAP_IRON_JUNCTION_TRACK =
      deferredRegister.register("strap_iron_junction_track",
          () -> new JunctionTrackBlock(TrackTypes.STRAP_IRON,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> STRAP_IRON_LAUNCHER_TRACK =
      deferredRegister.register("strap_iron_launcher_track",
          () -> new LauncherTrackBlock(TrackTypes.STRAP_IRON,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<Block> ELEVATOR_TRACK =
      deferredRegister.register("elevator_track",
          () -> new ElevatorTrackBlock(BlockBehaviour.Properties.of(RailcraftMaterials.ELEVATOR)
              .noCollission()
              .strength(1.05F)
              .sound(SoundType.METAL)));

  // firestone (ORE)
  public static final RegistryObject<Block> FIRESTONE =
      deferredRegister.register("firestone",
          () -> new FirestoneBlock(BlockBehaviour.Properties.of(Material.STONE)
              .lightLevel(__ -> 15)
              .strength(3, 5)));

  public static final RegistryObject<Block> RITUAL =
      deferredRegister.register("ritual",
          () -> new RitualBlock(BlockBehaviour.Properties.of(Material.STONE)
              .lightLevel(__ -> 1)
              .noOcclusion()));

  public static final RegistryObject<Block> MANUAL_ROLLING_MACHINE =
      deferredRegister.register("manual_rolling_machine",
          () -> new ManualRollingMachineBlock(BlockBehaviour.Properties.of(Material.WOOD)
              .sound(SoundType.WOOD)
              .strength(2.0F)));

  public static final RegistryObject<Block> COKE_OVEN_BRICKS =
      deferredRegister.register("coke_oven_bricks",
          () -> new CokeOvenBricksBlock(BlockBehaviour.Properties.of(Material.STONE)
              .sound(SoundType.STONE)
              .lightLevel(litBlockEmission(13))
              .strength(2F, 6.0F)));

  public static final RegistryObject<LiquidBlock> CREOSOTE =
      deferredRegister.register("creosote",
          () -> new LiquidBlock(RailcraftFluids.CREOSOTE,
              BlockBehaviour.Properties.of(Material.WATER)
                  .noCollission().strength(50.0F).noLootTable()));

  public static final RegistryObject<Block> BLACK_POST =
      deferredRegister.register("black_post",
          () -> new PostBlock(BlockBehaviour.Properties.of(Material.METAL)
              .sound(SoundType.METAL)));

  public static final RegistryObject<Block> RED_POST =
      deferredRegister.register("red_post",
          () -> new PostBlock(BlockBehaviour.Properties.of(Material.METAL)
              .sound(SoundType.METAL)));

  public static final RegistryObject<Block> GREEN_POST =
      deferredRegister.register("green_post",
          () -> new PostBlock(BlockBehaviour.Properties.of(Material.METAL)
              .sound(SoundType.METAL)));

  public static final RegistryObject<Block> BROWN_POST =
      deferredRegister.register("brown_post",
          () -> new PostBlock(BlockBehaviour.Properties.of(Material.METAL)
              .sound(SoundType.METAL)));

  public static final RegistryObject<Block> BLUE_POST =
      deferredRegister.register("blue_post",
          () -> new PostBlock(BlockBehaviour.Properties.of(Material.METAL)
              .sound(SoundType.METAL)));

  public static final RegistryObject<Block> PURPLE_POST =
      deferredRegister.register("purple_post",
          () -> new PostBlock(BlockBehaviour.Properties.of(Material.METAL)
              .sound(SoundType.METAL)));

  public static final RegistryObject<Block> CYAN_POST =
      deferredRegister.register("cyan_post",
          () -> new PostBlock(BlockBehaviour.Properties.of(Material.METAL)
              .sound(SoundType.METAL)));

  public static final RegistryObject<Block> LIGHT_GRAY_POST =
      deferredRegister.register("light_gray_post",
          () -> new PostBlock(BlockBehaviour.Properties.of(Material.METAL)
              .sound(SoundType.METAL)));

  public static final RegistryObject<Block> GRAY_POST =
      deferredRegister.register("gray_post",
          () -> new PostBlock(BlockBehaviour.Properties.of(Material.METAL)
              .sound(SoundType.METAL)));

  public static final RegistryObject<Block> PINK_POST =
      deferredRegister.register("pink_post",
          () -> new PostBlock(BlockBehaviour.Properties.of(Material.METAL)
              .sound(SoundType.METAL)));

  public static final RegistryObject<Block> LIME_POST =
      deferredRegister.register("lime_post",
          () -> new PostBlock(BlockBehaviour.Properties.of(Material.METAL)
              .sound(SoundType.METAL)));

  public static final RegistryObject<Block> YELLOW_POST =
      deferredRegister.register("yellow_post",
          () -> new PostBlock(BlockBehaviour.Properties.of(Material.METAL)
              .sound(SoundType.METAL)));

  public static final RegistryObject<Block> LIGHT_BLUE_POST =
      deferredRegister.register("light_blue_post",
          () -> new PostBlock(BlockBehaviour.Properties.of(Material.METAL)
              .sound(SoundType.METAL)));

  public static final RegistryObject<Block> MAGENTA_POST =
      deferredRegister.register("magenta_post",
          () -> new PostBlock(BlockBehaviour.Properties.of(Material.METAL)
              .sound(SoundType.METAL)));

  public static final RegistryObject<Block> ORANGE_POST =
      deferredRegister.register("orange_post",
          () -> new PostBlock(BlockBehaviour.Properties.of(Material.METAL)
              .sound(SoundType.METAL)));

  public static final RegistryObject<Block> WHITE_POST =
      deferredRegister.register("white_post",
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
