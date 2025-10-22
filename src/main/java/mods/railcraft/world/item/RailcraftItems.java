package mods.railcraft.world.item;

import java.util.Collection;
import org.jetbrains.annotations.Nullable;
import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.util.VariantRegistrar;
import mods.railcraft.world.entity.vehicle.EnergyMinecart;
import mods.railcraft.world.entity.vehicle.TankMinecart;
import mods.railcraft.world.entity.vehicle.locomotive.CreativeLocomotive;
import mods.railcraft.world.entity.vehicle.locomotive.ElectricLocomotive;
import mods.railcraft.world.entity.vehicle.locomotive.SteamLocomotive;
import mods.railcraft.world.item.creativetabs.RailcraftTabs;
import mods.railcraft.world.item.tunnelbore.BronzeTunnelBoreHeadItem;
import mods.railcraft.world.item.tunnelbore.DiamondTunnelBoreHeadItem;
import mods.railcraft.world.item.tunnelbore.IronTunnelBoreHeadItem;
import mods.railcraft.world.item.tunnelbore.SteelTunnelBoreHeadItem;
import mods.railcraft.world.item.tunnelbore.TunnelBoreItem;
import mods.railcraft.world.level.block.RailcraftBlocks;
import mods.railcraft.world.level.block.track.TrackTypes;
import mods.railcraft.world.level.material.RailcraftFluids;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.ShearsItem;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fluids.capability.wrappers.FluidBucketWrapper;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class RailcraftItems {

  private static final DeferredRegister<Item> deferredRegister =
      DeferredRegister.create(ForgeRegistries.ITEMS, RailcraftConstants.ID);

  public static void register(IEventBus modEventBus) {
    deferredRegister.register(modEventBus);
  }

  public static Collection<RegistryObject<Item>> entries() {
    return deferredRegister.getEntries();
  }

  public static final VariantRegistrar<DyeColor, BlockItem> STRENGTHENED_GLASS
      = registerBlockVariants(RailcraftBlocks.STRENGTHENED_GLASS, RailcraftTabs.DECORATIVE);
  public static final VariantRegistrar<DyeColor, BlockItem> POST =
      registerBlockVariants(RailcraftBlocks.POST, RailcraftTabs.DECORATIVE);
  public static final VariantRegistrar<DyeColor, BlockItem> IRON_TANK_GAUGE =
      registerBlockVariants(RailcraftBlocks.IRON_TANK_GAUGE, RailcraftTabs.MAIN);
  public static final VariantRegistrar<DyeColor, BlockItem> IRON_TANK_VALVE =
      registerBlockVariants(RailcraftBlocks.IRON_TANK_VALVE, RailcraftTabs.MAIN);
  public static final VariantRegistrar<DyeColor, BlockItem> IRON_TANK_WALL =
      registerBlockVariants(RailcraftBlocks.IRON_TANK_WALL, RailcraftTabs.MAIN);
  public static final VariantRegistrar<DyeColor, BlockItem> STEEL_TANK_GAUGE =
      registerBlockVariants(RailcraftBlocks.STEEL_TANK_GAUGE, RailcraftTabs.MAIN);
  public static final VariantRegistrar<DyeColor, BlockItem> STEEL_TANK_VALVE =
      registerBlockVariants(RailcraftBlocks.STEEL_TANK_VALVE, RailcraftTabs.MAIN);
  public static final VariantRegistrar<DyeColor, BlockItem> STEEL_TANK_WALL =
      registerBlockVariants(RailcraftBlocks.STEEL_TANK_WALL, RailcraftTabs.MAIN);

  public static final RegistryObject<PressureBoilerTankBlockItem> LOW_PRESSURE_STEAM_BOILER_TANK =
      deferredRegister.register("low_pressure_steam_boiler_tank",
          () -> new PressureBoilerTankBlockItem(
              RailcraftBlocks.LOW_PRESSURE_STEAM_BOILER_TANK.get(),
              new Item.Properties().tab(RailcraftTabs.MAIN)));

  public static final RegistryObject<PressureBoilerTankBlockItem> HIGH_PRESSURE_STEAM_BOILER_TANK =
      deferredRegister.register("high_pressure_steam_boiler_tank",
          () -> new PressureBoilerTankBlockItem(
              RailcraftBlocks.HIGH_PRESSURE_STEAM_BOILER_TANK.get(),
              new Item.Properties().tab(RailcraftTabs.MAIN)));

  public static final RegistryObject<FueledFireboxBlockItem> SOLID_FUELED_FIREBOX =
      deferredRegister.register("solid_fueled_firebox",
          () -> new FueledFireboxBlockItem(RailcraftBlocks.SOLID_FUELED_FIREBOX.get(),
              new Item.Properties().tab(RailcraftTabs.MAIN)));

  public static final RegistryObject<FueledFireboxBlockItem> FLUID_FUELED_FIREBOX =
      deferredRegister.register("fluid_fueled_firebox",
          () -> new FueledFireboxBlockItem(RailcraftBlocks.FLUID_FUELED_FIREBOX.get(),
              new Item.Properties().tab(RailcraftTabs.MAIN)));

  public static final RegistryObject<SignalLabelItem> SIGNAL_LABEL =
      deferredRegister.register("signal_label",
          () -> new SignalLabelItem(new Item.Properties().tab(RailcraftTabs.MAIN)));

  public static final RegistryObject<Item> TURBINE_BLADE = registerBasic("turbine_blade", RailcraftTabs.MAIN);

  public static final RegistryObject<Item> TURBINE_DISK = registerBasic("turbine_disk", RailcraftTabs.MAIN);

  public static final RegistryObject<TurbineRotorItem> TURBINE_ROTOR =
      deferredRegister.register("turbine_rotor",
          () -> new TurbineRotorItem(new Item.Properties().stacksTo(1).tab(RailcraftTabs.MAIN)));

  public static final RegistryObject<BlockItem> STEAM_TURBINE =
      blockItem(RailcraftBlocks.STEAM_TURBINE, RailcraftTabs.MAIN);


  public static final RegistryObject<BlockItem> BLAST_FURNACE_BRICKS =
      blockItem(RailcraftBlocks.BLAST_FURNACE_BRICKS, RailcraftTabs.MAIN);

  public static final RegistryObject<BlockItem> FEED_STATION =
      blockItem(RailcraftBlocks.FEED_STATION, RailcraftTabs.MAIN);

  public static final RegistryObject<BlockItem> LOGBOOK = blockItem(RailcraftBlocks.LOGBOOK, RailcraftTabs.MAIN);

  public static final RegistryObject<BlockItem> FRAME_BLOCK = blockItem(RailcraftBlocks.FRAME, RailcraftTabs.MAIN);

  public static final RegistryObject<ChargeMeterItem> CHARGE_METER =
      deferredRegister.register("charge_meter",
          () -> new ChargeMeterItem(new Item.Properties()
              .durability(0)
              .stacksTo(1)
              .tab(RailcraftTabs.MAIN)));

  public static final RegistryObject<BlockItem> NICKEL_ZINC_BATTERY =
      blockItem(RailcraftBlocks.NICKEL_ZINC_BATTERY, RailcraftTabs.MAIN);

  public static final RegistryObject<BlockItem> NICKEL_IRON_BATTERY =
      blockItem(RailcraftBlocks.NICKEL_IRON_BATTERY, RailcraftTabs.MAIN);

  public static final RegistryObject<BlockItem> ZINC_CARBON_BATTERY =
      blockItem(RailcraftBlocks.ZINC_CARBON_BATTERY, RailcraftTabs.MAIN);

  public static final RegistryObject<BlockItem> ZINC_CARBON_BATTERY_EMPTY =
      blockItem(RailcraftBlocks.ZINC_CARBON_BATTERY_EMPTY, RailcraftTabs.MAIN);

  public static final RegistryObject<BlockItem> ZINC_SILVER_BATTERY =
      blockItem(RailcraftBlocks.ZINC_SILVER_BATTERY, RailcraftTabs.MAIN);

  public static final RegistryObject<BlockItem> ZINC_SILVER_BATTERY_EMPTY =
      blockItem(RailcraftBlocks.ZINC_SILVER_BATTERY_EMPTY, RailcraftTabs.MAIN);

  public static final RegistryObject<BlockItem> STEEL_ANVIL =
      blockItem(RailcraftBlocks.STEEL_ANVIL, RailcraftTabs.MAIN);

  public static final RegistryObject<BlockItem> CHIPPED_STEEL_ANVIL =
      blockItem(RailcraftBlocks.CHIPPED_STEEL_ANVIL, RailcraftTabs.MAIN);

  public static final RegistryObject<BlockItem> DAMAGED_STEEL_ANVIL =
      blockItem(RailcraftBlocks.DAMAGED_STEEL_ANVIL, RailcraftTabs.MAIN);

  public static final RegistryObject<BlockItem> STEEL_BLOCK =
      blockItem(RailcraftBlocks.STEEL_BLOCK, RailcraftTabs.MAIN);

  public static final RegistryObject<BlockItem> BRASS_BLOCK =
      blockItem(RailcraftBlocks.BRASS_BLOCK, RailcraftTabs.MAIN);

  public static final RegistryObject<BlockItem> BRONZE_BLOCK =
      blockItem(RailcraftBlocks.BRONZE_BLOCK, RailcraftTabs.MAIN);

  public static final RegistryObject<BlockItem> INVAR_BLOCK =
      blockItem(RailcraftBlocks.INVAR_BLOCK, RailcraftTabs.MAIN);

  public static final RegistryObject<BlockItem> LEAD_BLOCK =
      blockItem(RailcraftBlocks.LEAD_BLOCK, RailcraftTabs.MAIN);

  public static final RegistryObject<BlockItem> NICKEL_BLOCK =
      blockItem(RailcraftBlocks.NICKEL_BLOCK, RailcraftTabs.MAIN);

  public static final RegistryObject<BlockItem> SILVER_BLOCK =
      blockItem(RailcraftBlocks.SILVER_BLOCK, RailcraftTabs.MAIN);

  public static final RegistryObject<BlockItem> TIN_BLOCK =
      blockItem(RailcraftBlocks.TIN_BLOCK, RailcraftTabs.MAIN);

  public static final RegistryObject<BlockItem> ZINC_BLOCK =
      blockItem(RailcraftBlocks.ZINC_BLOCK, RailcraftTabs.MAIN);

  public static final RegistryObject<BlockItem> LEAD_ORE =
      blockItem(RailcraftBlocks.LEAD_ORE, RailcraftTabs.MAIN);

  public static final RegistryObject<BlockItem> NICKEL_ORE =
      blockItem(RailcraftBlocks.NICKEL_ORE, RailcraftTabs.MAIN);

  public static final RegistryObject<BlockItem> SILVER_ORE =
      blockItem(RailcraftBlocks.SILVER_ORE, RailcraftTabs.MAIN);

  public static final RegistryObject<BlockItem> TIN_ORE =
      blockItem(RailcraftBlocks.TIN_ORE, RailcraftTabs.MAIN);

  public static final RegistryObject<BlockItem> ZINC_ORE =
      blockItem(RailcraftBlocks.ZINC_ORE, RailcraftTabs.MAIN);

  public static final RegistryObject<BlockItem> DEEPSLATE_LEAD_ORE =
      blockItem(RailcraftBlocks.DEEPSLATE_LEAD_ORE, RailcraftTabs.MAIN);

  public static final RegistryObject<BlockItem> DEEPSLATE_NICKEL_ORE =
      blockItem(RailcraftBlocks.DEEPSLATE_NICKEL_ORE, RailcraftTabs.MAIN);

  public static final RegistryObject<BlockItem> DEEPSLATE_SILVER_ORE =
      blockItem(RailcraftBlocks.DEEPSLATE_SILVER_ORE, RailcraftTabs.MAIN);

  public static final RegistryObject<BlockItem> DEEPSLATE_TIN_ORE =
      blockItem(RailcraftBlocks.DEEPSLATE_TIN_ORE, RailcraftTabs.MAIN);

  public static final RegistryObject<BlockItem> DEEPSLATE_ZINC_ORE =
      blockItem(RailcraftBlocks.DEEPSLATE_ZINC_ORE, RailcraftTabs.MAIN);

  public static final RegistryObject<BlockItem> SULFUR_ORE =
      blockItem(RailcraftBlocks.SULFUR_ORE, RailcraftTabs.MAIN);

  public static final RegistryObject<BlockItem> DEEPSLATE_SULFUR_ORE =
      blockItem(RailcraftBlocks.DEEPSLATE_SULFUR_ORE, RailcraftTabs.MAIN);

  public static final RegistryObject<BlockItem> SALTPETER_ORE =
      blockItem(RailcraftBlocks.SALTPETER_ORE, RailcraftTabs.MAIN);

  public static final RegistryObject<CoalCokeBlockItem> COAL_COKE_BLOCK =
      deferredRegister.register("coal_coke_block",
          () -> new CoalCokeBlockItem(new Properties().tab(RailcraftTabs.MAIN)));

  public static final RegistryObject<ShearsItem> STEEL_SHEARS =
      deferredRegister.register("steel_shears",
          () -> new ShearsItem(new Item.Properties().durability(500).tab(CreativeModeTab.TAB_TOOLS)));

  public static final RegistryObject<SwordItem> STEEL_SWORD =
      deferredRegister.register("steel_sword",
          () -> new SwordItem(RailcraftItemTier.STEEL, 3, -2.4F,
              new Item.Properties().tab(CreativeModeTab.TAB_COMBAT)));

  public static final RegistryObject<ShovelItem> STEEL_SHOVEL =
      deferredRegister.register("steel_shovel",
          () -> new ShovelItem(RailcraftItemTier.STEEL, 1.5F, -3.0F,
              new Item.Properties().tab(CreativeModeTab.TAB_TOOLS)));

  public static final RegistryObject<PickaxeItem> STEEL_PICKAXE =
      deferredRegister.register("steel_pickaxe",
          () -> new PickaxeItem(RailcraftItemTier.STEEL, 1, -2.8F,
              new Item.Properties().tab(CreativeModeTab.TAB_TOOLS)));

  public static final RegistryObject<AxeItem> STEEL_AXE =
      deferredRegister.register("steel_axe",
          () -> new AxeItem(RailcraftItemTier.STEEL, 5.5F, -3.0F,
              new Item.Properties().tab(CreativeModeTab.TAB_TOOLS)));

  public static final RegistryObject<HoeItem> STEEL_HOE =
      deferredRegister.register("steel_hoe",
          () -> new HoeItem(RailcraftItemTier.STEEL, -2, -0.5F,
              new Item.Properties().tab(CreativeModeTab.TAB_TOOLS)));

  public static final RegistryObject<ArmorItem> STEEL_BOOTS =
      deferredRegister.register("steel_boots",
          () -> new ArmorItem(RailcraftArmorMaterial.STEEL, EquipmentSlot.FEET,
              new Item.Properties().tab(CreativeModeTab.TAB_COMBAT)));

  public static final RegistryObject<ArmorItem> STEEL_CHESTPLATE =
      deferredRegister.register("steel_chestplate",
          () -> new ArmorItem(RailcraftArmorMaterial.STEEL, EquipmentSlot.CHEST,
              new Item.Properties().tab(CreativeModeTab.TAB_COMBAT)));

  public static final RegistryObject<ArmorItem> STEEL_HELMET =
      deferredRegister.register("steel_helmet",
          () -> new ArmorItem(RailcraftArmorMaterial.STEEL, EquipmentSlot.HEAD,
              new Item.Properties().tab(CreativeModeTab.TAB_COMBAT)));

  public static final RegistryObject<ArmorItem> STEEL_LEGGINGS =
      deferredRegister.register("steel_leggings",
          () -> new ArmorItem(RailcraftArmorMaterial.STEEL, EquipmentSlot.LEGS,
              new Item.Properties().tab(CreativeModeTab.TAB_COMBAT)));

  public static final RegistryObject<IronTunnelBoreHeadItem> IRON_TUNNEL_BORE_HEAD =
      deferredRegister.register("iron_tunnel_bore_head",
          () -> new IronTunnelBoreHeadItem(new Item.Properties().durability(1500).tab(RailcraftTabs.MAIN)));

  public static final RegistryObject<BronzeTunnelBoreHeadItem> BRONZE_TUNNEL_BORE_HEAD =
      deferredRegister.register("bronze_tunnel_bore_head",
          () -> new BronzeTunnelBoreHeadItem(new Item.Properties().durability(1200).tab(RailcraftTabs.MAIN)));

  public static final RegistryObject<SteelTunnelBoreHeadItem> STEEL_TUNNEL_BORE_HEAD =
      deferredRegister.register("steel_tunnel_bore_head",
          () -> new SteelTunnelBoreHeadItem(new Item.Properties().durability(3000).tab(RailcraftTabs.MAIN)));

  public static final RegistryObject<DiamondTunnelBoreHeadItem> DIAMOND_TUNNEL_BORE_HEAD =
      deferredRegister.register("diamond_tunnel_bore_head",
          () -> new DiamondTunnelBoreHeadItem(new Item.Properties().durability(6000).tab(RailcraftTabs.MAIN)));

  public static final RegistryObject<BlockItem> FLUID_LOADER =
      blockItem(RailcraftBlocks.FLUID_LOADER, RailcraftTabs.MAIN);

  public static final RegistryObject<BlockItem> FLUID_UNLOADER =
      blockItem(RailcraftBlocks.FLUID_UNLOADER, RailcraftTabs.MAIN);

  public static final RegistryObject<BlockItem> ADVANCED_ITEM_LOADER =
      blockItem(RailcraftBlocks.ADVANCED_ITEM_LOADER, RailcraftTabs.MAIN);

  public static final RegistryObject<BlockItem> ADVANCED_ITEM_UNLOADER =
      blockItem(RailcraftBlocks.ADVANCED_ITEM_UNLOADER, RailcraftTabs.MAIN);

  public static final RegistryObject<BlockItem> ITEM_LOADER =
      blockItem(RailcraftBlocks.ITEM_LOADER, RailcraftTabs.MAIN);

  public static final RegistryObject<BlockItem> ITEM_UNLOADER =
      blockItem(RailcraftBlocks.ITEM_UNLOADER, RailcraftTabs.MAIN);

  public static final RegistryObject<BlockItem> CART_DISPENSER =
      blockItem(RailcraftBlocks.CART_DISPENSER, RailcraftTabs.MAIN);

  public static final RegistryObject<BlockItem> TRAIN_DISPENSER =
      blockItem(RailcraftBlocks.TRAIN_DISPENSER, RailcraftTabs.MAIN);

  public static final RegistryObject<BlockItem> ADVANCED_DETECTOR =
      blockItem(RailcraftBlocks.ADVANCED_DETECTOR, RailcraftTabs.MAIN);

  public static final RegistryObject<BlockItem> AGE_DETECTOR =
      blockItem(RailcraftBlocks.AGE_DETECTOR, RailcraftTabs.MAIN);

  public static final RegistryObject<BlockItem> ANIMAL_DETECTOR =
      blockItem(RailcraftBlocks.ANIMAL_DETECTOR, RailcraftTabs.MAIN);

  public static final RegistryObject<BlockItem> ANY_DETECTOR =
      blockItem(RailcraftBlocks.ANY_DETECTOR, RailcraftTabs.MAIN);

  public static final RegistryObject<BlockItem> EMPTY_DETECTOR =
      blockItem(RailcraftBlocks.EMPTY_DETECTOR, RailcraftTabs.MAIN);

  public static final RegistryObject<BlockItem> ITEM_DETECTOR =
      blockItem(RailcraftBlocks.ITEM_DETECTOR, RailcraftTabs.MAIN);

  public static final RegistryObject<BlockItem> LOCOMOTIVE_DETECTOR =
      blockItem(RailcraftBlocks.LOCOMOTIVE_DETECTOR, RailcraftTabs.MAIN);

  public static final RegistryObject<BlockItem> MOB_DETECTOR =
      blockItem(RailcraftBlocks.MOB_DETECTOR, RailcraftTabs.MAIN);

  public static final RegistryObject<BlockItem> PLAYER_DETECTOR =
      blockItem(RailcraftBlocks.PLAYER_DETECTOR, RailcraftTabs.MAIN);

  public static final RegistryObject<BlockItem> ROUTING_DETECTOR =
      blockItem(RailcraftBlocks.ROUTING_DETECTOR, RailcraftTabs.MAIN);

  public static final RegistryObject<BlockItem> SHEEP_DETECTOR =
      blockItem(RailcraftBlocks.SHEEP_DETECTOR, RailcraftTabs.MAIN);

  public static final RegistryObject<BlockItem> TANK_DETECTOR =
      blockItem(RailcraftBlocks.TANK_DETECTOR, RailcraftTabs.MAIN);

  public static final RegistryObject<BlockItem> TRAIN_DETECTOR =
      blockItem(RailcraftBlocks.TRAIN_DETECTOR, RailcraftTabs.MAIN);

  public static final RegistryObject<BlockItem> VILLAGER_DETECTOR =
      blockItem(RailcraftBlocks.VILLAGER_DETECTOR, RailcraftTabs.MAIN);

  public static final RegistryObject<SpikeMaulItem> IRON_SPIKE_MAUL =
      deferredRegister.register("iron_spike_maul",
          () -> new SpikeMaulItem(11.0F, -3.5F, Tiers.IRON,
              new Item.Properties().tab(RailcraftTabs.MAIN)));

  public static final RegistryObject<SpikeMaulItem> STEEL_SPIKE_MAUL =
      deferredRegister.register("steel_spike_maul",
          () -> new SpikeMaulItem(11.0F, -3.4F, RailcraftItemTier.STEEL,
              new Item.Properties().tab(RailcraftTabs.MAIN)));

  public static final RegistryObject<SpikeMaulItem> DIAMOND_SPIKE_MAUL =
      deferredRegister.register("diamond_spike_maul",
          () -> new SpikeMaulItem(11.0F, -3.3F, Tiers.DIAMOND,
              new Item.Properties().tab(RailcraftTabs.MAIN)));

  public static final RegistryObject<BlockItem> SWITCH_TRACK_LEVER =
      blockItem(RailcraftBlocks.SWITCH_TRACK_LEVER, CreativeModeTab.TAB_TRANSPORTATION);

  public static final RegistryObject<BlockItem> SWITCH_TRACK_MOTOR =
      blockItem(RailcraftBlocks.SWITCH_TRACK_MOTOR, CreativeModeTab.TAB_TRANSPORTATION);

  public static final RegistryObject<BlockItem> SWITCH_TRACK_ROUTER =
      blockItem(RailcraftBlocks.SWITCH_TRACK_ROUTER, CreativeModeTab.TAB_TRANSPORTATION);

  public static final RegistryObject<SignalTunerItem> SIGNAL_TUNER =
      deferredRegister.register("signal_tuner",
          () -> new SignalTunerItem(new Item.Properties().stacksTo(1).tab(RailcraftTabs.MAIN)));

  public static final RegistryObject<SignalBlockSurveyorItem> SIGNAL_BLOCK_SURVEYOR =
      deferredRegister.register("signal_block_surveyor",
          () -> new SignalBlockSurveyorItem(new Item.Properties().stacksTo(1).tab(RailcraftTabs.MAIN)));

  public static final RegistryObject<BlockItem> ANALOG_SIGNAL_CONTROLLER_BOX =
      blockItem(RailcraftBlocks.ANALOG_SIGNAL_CONTROLLER_BOX, CreativeModeTab.TAB_TRANSPORTATION);

  public static final RegistryObject<BlockItem> SIGNAL_SEQUENCER_BOX =
      blockItem(RailcraftBlocks.SIGNAL_SEQUENCER_BOX, CreativeModeTab.TAB_TRANSPORTATION);

  public static final RegistryObject<BlockItem> SIGNAL_CAPACITOR_BOX =
      blockItem(RailcraftBlocks.SIGNAL_CAPACITOR_BOX, CreativeModeTab.TAB_TRANSPORTATION);

  public static final RegistryObject<BlockItem> SIGNAL_INTERLOCK_BOX =
      blockItem(RailcraftBlocks.SIGNAL_INTERLOCK_BOX, CreativeModeTab.TAB_TRANSPORTATION);

  public static final RegistryObject<BlockItem> SIGNAL_BLOCK_RELAY_BOX =
      blockItem(RailcraftBlocks.SIGNAL_BLOCK_RELAY_BOX, CreativeModeTab.TAB_TRANSPORTATION);

  public static final RegistryObject<BlockItem> SIGNAL_RECEIVER_BOX =
      blockItem(RailcraftBlocks.SIGNAL_RECEIVER_BOX, CreativeModeTab.TAB_TRANSPORTATION);

  public static final RegistryObject<BlockItem> SIGNAL_CONTROLLER_BOX =
      blockItem(RailcraftBlocks.SIGNAL_CONTROLLER_BOX, CreativeModeTab.TAB_TRANSPORTATION);

  public static final RegistryObject<BlockItem> TOKEN_SIGNAL_BOX =
      blockItem(RailcraftBlocks.TOKEN_SIGNAL_BOX, CreativeModeTab.TAB_TRANSPORTATION);

  public static final RegistryObject<BlockItem> DUAL_BLOCK_SIGNAL =
      blockItem(RailcraftBlocks.DUAL_BLOCK_SIGNAL, CreativeModeTab.TAB_TRANSPORTATION);

  public static final RegistryObject<BlockItem> DUAL_DISTANT_SIGNAL =
      blockItem(RailcraftBlocks.DUAL_DISTANT_SIGNAL, CreativeModeTab.TAB_TRANSPORTATION);

  public static final RegistryObject<BlockItem> DUAL_TOKEN_SIGNAL =
      blockItem(RailcraftBlocks.DUAL_TOKEN_SIGNAL, CreativeModeTab.TAB_TRANSPORTATION);

  public static final RegistryObject<BlockItem> BLOCK_SIGNAL =
      blockItem(RailcraftBlocks.BLOCK_SIGNAL, CreativeModeTab.TAB_TRANSPORTATION);

  public static final RegistryObject<BlockItem> DISTANT_SIGNAL =
      blockItem(RailcraftBlocks.DISTANT_SIGNAL, CreativeModeTab.TAB_TRANSPORTATION);

  public static final RegistryObject<BlockItem> TOKEN_SIGNAL =
      blockItem(RailcraftBlocks.TOKEN_SIGNAL, CreativeModeTab.TAB_TRANSPORTATION);

  public static final RegistryObject<TrackRemoverCartItem> TRACK_REMOVER =
      deferredRegister.register("track_remover",
          () -> new TrackRemoverCartItem(new Item.Properties()
              .rarity(Rarity.UNCOMMON)
              .stacksTo(1)));

  public static final RegistryObject<TrackLayerCartItem> TRACK_LAYER =
      deferredRegister.register("track_layer",
          () -> new TrackLayerCartItem(new Item.Properties()
              .rarity(Rarity.UNCOMMON)
              .stacksTo(1)
              .tab(CreativeModeTab.TAB_TRANSPORTATION)));

  public static final RegistryObject<TrackRelayerCartItem> TRACK_RELAYER =
      deferredRegister.register("track_relayer",
          () -> new TrackRelayerCartItem(new Item.Properties()
              .rarity(Rarity.UNCOMMON)
              .stacksTo(1)
              .tab(CreativeModeTab.TAB_TRANSPORTATION)));

  public static final RegistryObject<TrackUndercutterCartItem> TRACK_UNDERCUTTER =
      deferredRegister.register("track_undercutter",
          () -> new TrackUndercutterCartItem(new Item.Properties()
              .rarity(Rarity.UNCOMMON)
              .stacksTo(1)
              .tab(CreativeModeTab.TAB_TRANSPORTATION)));

  public static final RegistryObject<CartItem> TANK_MINECART =
      deferredRegister.register("tank_minecart",
          () -> new CartItem(TankMinecart::new, new Item.Properties().stacksTo(1)
              .tab(CreativeModeTab.TAB_TRANSPORTATION)));

  public static final RegistryObject<CartItem> ENERGY_MINECART =
      deferredRegister.register("energy_minecart",
          () -> new CartItem(EnergyMinecart::new, new Item.Properties().stacksTo(1)
              .tab(CreativeModeTab.TAB_TRANSPORTATION)));

  public static final RegistryObject<WorldSpikeMinecartItem> WORLD_SPIKE_MINECART =
      deferredRegister.register("world_spike_minecart",
          () -> new WorldSpikeMinecartItem(new Item.Properties()
              .stacksTo(1)
              .rarity(Rarity.UNCOMMON)
              .tab(CreativeModeTab.TAB_TRANSPORTATION)));

  public static final RegistryObject<TunnelBoreItem> TUNNEL_BORE =
      deferredRegister.register("tunnel_bore",
          () -> new TunnelBoreItem(new Item.Properties()
              .rarity(Rarity.UNCOMMON)
              .stacksTo(1)
              .tab(CreativeModeTab.TAB_TRANSPORTATION)));

  public static final RegistryObject<LocomotiveItem> CREATIVE_LOCOMOTIVE =
      deferredRegister.register("creative_locomotive",
          () -> new LocomotiveItem(CreativeLocomotive::new, DyeColor.BLACK, DyeColor.MAGENTA,
              new Item.Properties().stacksTo(1).tab(CreativeModeTab.TAB_TRANSPORTATION)));

  public static final RegistryObject<LocomotiveItem> ELECTRIC_LOCOMOTIVE =
      deferredRegister.register("electric_locomotive",
          () -> new LocomotiveItem(ElectricLocomotive::new, DyeColor.YELLOW, DyeColor.BLACK,
              new Item.Properties().stacksTo(1).tab(CreativeModeTab.TAB_TRANSPORTATION)));

  public static final RegistryObject<LocomotiveItem> STEAM_LOCOMOTIVE =
      deferredRegister.register("steam_locomotive",
          () -> new LocomotiveItem(SteamLocomotive::new, DyeColor.LIGHT_GRAY, DyeColor.GRAY,
              new Item.Properties().stacksTo(1).tab(CreativeModeTab.TAB_TRANSPORTATION)));

  public static final RegistryObject<Item> WHISTLE_TUNER =
      deferredRegister.register("whistle_tuner",
          () -> new Item(new Item.Properties().durability(250).tab(RailcraftTabs.MAIN)));

  public static final RegistryObject<GoldenTicketItem> GOLDEN_TICKET =
      deferredRegister.register("golden_ticket",
          () -> new GoldenTicketItem(new Item.Properties().rarity(Rarity.UNCOMMON).tab(RailcraftTabs.MAIN)));

  public static final RegistryObject<TicketItem> TICKET =
      deferredRegister.register("ticket", () -> new TicketItem(new Item.Properties().tab(RailcraftTabs.MAIN)));

  public static final RegistryObject<RoutingTableBookItem> ROUTING_TABLE_BOOK =
      deferredRegister.register("routing_table_book",
          () -> new RoutingTableBookItem(new Item.Properties().tab(RailcraftTabs.MAIN)));

  public static final RegistryObject<OverallsItem> OVERALLS =
      deferredRegister.register("overalls",
          () -> new OverallsItem(new Item.Properties().tab(RailcraftTabs.MAIN)));

  public static final RegistryObject<FirestoneOreBlockItem> FIRESTONE_ORE =
      deferredRegister.register("firestone_ore",
          () -> new FirestoneOreBlockItem(new Item.Properties().tab(RailcraftTabs.MAIN)));

  public static final RegistryObject<FirestoneItem> RAW_FIRESTONE =
      deferredRegister.register("raw_firestone",
          () -> new FirestoneItem(true,
              new Item.Properties().rarity(Rarity.RARE)));

  public static final RegistryObject<RefinedFirestoneItem> REFINED_FIRESTONE =
      deferredRegister.register("refined_firestone",
          () -> new RefinedFirestoneItem(false, new Item.Properties()
              .stacksTo(1)
              .durability(RefinedFirestoneItem.CHARGES)
              .rarity(Rarity.RARE)));

  public static final RegistryObject<CrackedFirestoneItem> CRACKED_FIRESTONE =
      deferredRegister.register("cracked_firestone",
          () -> new CrackedFirestoneItem(new Item.Properties()
              .stacksTo(1)
              .durability(RefinedFirestoneItem.CHARGES)
              .rarity(Rarity.RARE)));

  public static final RegistryObject<FirestoneItem> CUT_FIRESTONE =
      deferredRegister.register("cut_firestone",
          () -> new FirestoneItem(true,
              new Item.Properties()
                  .stacksTo(1)
                  .durability(RefinedFirestoneItem.CHARGES)
                  .rarity(Rarity.RARE)));

  public static final RegistryObject<BlockItem> FORCE_TRACK_EMITTER =
      blockItem(RailcraftBlocks.FORCE_TRACK_EMITTER, RailcraftTabs.MAIN);

  public static final RegistryObject<BlockItem> ABANDONED_TRACK =
      blockItem(RailcraftBlocks.ABANDONED_TRACK, CreativeModeTab.TAB_TRANSPORTATION);

  public static final RegistryObject<BlockItem> ABANDONED_LOCKING_TRACK =
      blockItem(RailcraftBlocks.ABANDONED_LOCKING_TRACK, RailcraftTabs.OUTFITTED_TRACKS);

  public static final RegistryObject<BlockItem> ABANDONED_BUFFER_STOP_TRACK =
      blockItem(RailcraftBlocks.ABANDONED_BUFFER_STOP_TRACK, RailcraftTabs.OUTFITTED_TRACKS);

  public static final RegistryObject<BlockItem> ABANDONED_ACTIVATOR_TRACK =
      blockItem(RailcraftBlocks.ABANDONED_ACTIVATOR_TRACK, RailcraftTabs.OUTFITTED_TRACKS);

  public static final RegistryObject<BlockItem> ABANDONED_BOOSTER_TRACK =
      blockItem(RailcraftBlocks.ABANDONED_BOOSTER_TRACK, RailcraftTabs.OUTFITTED_TRACKS);

  public static final RegistryObject<BlockItem> ABANDONED_CONTROL_TRACK =
      blockItem(RailcraftBlocks.ABANDONED_CONTROL_TRACK, RailcraftTabs.OUTFITTED_TRACKS);

  public static final RegistryObject<BlockItem> ABANDONED_GATED_TRACK =
      blockItem(RailcraftBlocks.ABANDONED_GATED_TRACK, RailcraftTabs.OUTFITTED_TRACKS);

  public static final RegistryObject<BlockItem> ABANDONED_DETECTOR_TRACK =
      blockItem(RailcraftBlocks.ABANDONED_DETECTOR_TRACK, RailcraftTabs.OUTFITTED_TRACKS);

  public static final RegistryObject<BlockItem> ABANDONED_COUPLER_TRACK =
      blockItem(RailcraftBlocks.ABANDONED_COUPLER_TRACK, RailcraftTabs.OUTFITTED_TRACKS);

  public static final RegistryObject<BlockItem> ABANDONED_EMBARKING_TRACK =
      blockItem(RailcraftBlocks.ABANDONED_EMBARKING_TRACK, RailcraftTabs.OUTFITTED_TRACKS);

  public static final RegistryObject<BlockItem> ABANDONED_DISEMBARKING_TRACK =
      blockItem(RailcraftBlocks.ABANDONED_DISEMBARKING_TRACK, RailcraftTabs.OUTFITTED_TRACKS);

  public static final RegistryObject<BlockItem> ABANDONED_DUMPING_TRACK =
      blockItem(RailcraftBlocks.ABANDONED_DUMPING_TRACK, RailcraftTabs.OUTFITTED_TRACKS);

  public static final RegistryObject<BlockItem> ABANDONED_WYE_TRACK =
      blockItem(RailcraftBlocks.ABANDONED_WYE_TRACK, RailcraftTabs.OUTFITTED_TRACKS);

  public static final RegistryObject<BlockItem> ABANDONED_TURNOUT_TRACK =
      blockItem(RailcraftBlocks.ABANDONED_TURNOUT_TRACK, RailcraftTabs.OUTFITTED_TRACKS);

  public static final RegistryObject<BlockItem> ABANDONED_JUNCTION_TRACK =
      blockItem(RailcraftBlocks.ABANDONED_JUNCTION_TRACK, RailcraftTabs.OUTFITTED_TRACKS);

  public static final RegistryObject<BlockItem> ABANDONED_LAUNCHER_TRACK =
      blockItem(RailcraftBlocks.ABANDONED_LAUNCHER_TRACK, RailcraftTabs.OUTFITTED_TRACKS);

  public static final RegistryObject<BlockItem> ABANDONED_ONE_WAY_TRACK =
      blockItem(RailcraftBlocks.ABANDONED_ONE_WAY_TRACK, RailcraftTabs.OUTFITTED_TRACKS);

  public static final RegistryObject<BlockItem> ABANDONED_WHISTLE_TRACK =
      blockItem(RailcraftBlocks.ABANDONED_WHISTLE_TRACK, RailcraftTabs.OUTFITTED_TRACKS);

  public static final RegistryObject<BlockItem> ABANDONED_LOCOMOTIVE_TRACK =
      blockItem(RailcraftBlocks.ABANDONED_LOCOMOTIVE_TRACK, RailcraftTabs.OUTFITTED_TRACKS);

  public static final RegistryObject<BlockItem> ABANDONED_THROTTLE_TRACK =
      blockItem(RailcraftBlocks.ABANDONED_THROTTLE_TRACK, RailcraftTabs.OUTFITTED_TRACKS);

  public static final RegistryObject<BlockItem> ABANDONED_ROUTING_TRACK =
      blockItem(RailcraftBlocks.ABANDONED_ROUTING_TRACK, RailcraftTabs.OUTFITTED_TRACKS);

  public static final RegistryObject<BlockItem> ELECTRIC_TRACK =
      blockItem(RailcraftBlocks.ELECTRIC_TRACK, CreativeModeTab.TAB_TRANSPORTATION);

  public static final RegistryObject<BlockItem> ELECTRIC_LOCKING_TRACK =
      blockItem(RailcraftBlocks.ELECTRIC_LOCKING_TRACK, RailcraftTabs.OUTFITTED_TRACKS);

  public static final RegistryObject<BlockItem> ELECTRIC_BUFFER_STOP_TRACK =
      blockItem(RailcraftBlocks.ELECTRIC_BUFFER_STOP_TRACK, RailcraftTabs.OUTFITTED_TRACKS);

  public static final RegistryObject<BlockItem> ELECTRIC_ACTIVATOR_TRACK =
      blockItem(RailcraftBlocks.ELECTRIC_ACTIVATOR_TRACK, RailcraftTabs.OUTFITTED_TRACKS);

  public static final RegistryObject<BlockItem> ELECTRIC_BOOSTER_TRACK =
      blockItem(RailcraftBlocks.ELECTRIC_BOOSTER_TRACK, RailcraftTabs.OUTFITTED_TRACKS);

  public static final RegistryObject<BlockItem> ELECTRIC_CONTROL_TRACK =
      blockItem(RailcraftBlocks.ELECTRIC_CONTROL_TRACK, RailcraftTabs.OUTFITTED_TRACKS);

  public static final RegistryObject<BlockItem> ELECTRIC_GATED_TRACK =
      blockItem(RailcraftBlocks.ELECTRIC_GATED_TRACK, RailcraftTabs.OUTFITTED_TRACKS);

  public static final RegistryObject<BlockItem> ELECTRIC_DETECTOR_TRACK =
      blockItem(RailcraftBlocks.ELECTRIC_DETECTOR_TRACK, RailcraftTabs.OUTFITTED_TRACKS);

  public static final RegistryObject<BlockItem> ELECTRIC_COUPLER_TRACK =
      blockItem(RailcraftBlocks.ELECTRIC_COUPLER_TRACK, RailcraftTabs.OUTFITTED_TRACKS);

  public static final RegistryObject<BlockItem> ELECTRIC_EMBARKING_TRACK =
      blockItem(RailcraftBlocks.ELECTRIC_EMBARKING_TRACK, RailcraftTabs.OUTFITTED_TRACKS);

  public static final RegistryObject<BlockItem> ELECTRIC_DISEMBARKING_TRACK =
      blockItem(RailcraftBlocks.ELECTRIC_DISEMBARKING_TRACK, RailcraftTabs.OUTFITTED_TRACKS);

  public static final RegistryObject<BlockItem> ELECTRIC_DUMPING_TRACK =
      blockItem(RailcraftBlocks.ELECTRIC_DUMPING_TRACK, RailcraftTabs.OUTFITTED_TRACKS);

  public static final RegistryObject<BlockItem> ELECTRIC_WYE_TRACK =
      blockItem(RailcraftBlocks.ELECTRIC_WYE_TRACK, RailcraftTabs.OUTFITTED_TRACKS);

  public static final RegistryObject<BlockItem> ELECTRIC_TURNOUT_TRACK =
      blockItem(RailcraftBlocks.ELECTRIC_TURNOUT_TRACK, RailcraftTabs.OUTFITTED_TRACKS);

  public static final RegistryObject<BlockItem> ELECTRIC_JUNCTION_TRACK =
      blockItem(RailcraftBlocks.ELECTRIC_JUNCTION_TRACK, RailcraftTabs.OUTFITTED_TRACKS);

  public static final RegistryObject<BlockItem> ELECTRIC_LAUNCHER_TRACK =
      blockItem(RailcraftBlocks.ELECTRIC_LAUNCHER_TRACK, RailcraftTabs.OUTFITTED_TRACKS);

  public static final RegistryObject<BlockItem> ELECTRIC_ONE_WAY_TRACK =
      blockItem(RailcraftBlocks.ELECTRIC_ONE_WAY_TRACK, RailcraftTabs.OUTFITTED_TRACKS);

  public static final RegistryObject<BlockItem> ELECTRIC_WHISTLE_TRACK =
      blockItem(RailcraftBlocks.ELECTRIC_WHISTLE_TRACK, RailcraftTabs.OUTFITTED_TRACKS);

  public static final RegistryObject<BlockItem> ELECTRIC_LOCOMOTIVE_TRACK =
      blockItem(RailcraftBlocks.ELECTRIC_LOCOMOTIVE_TRACK, RailcraftTabs.OUTFITTED_TRACKS);

  public static final RegistryObject<BlockItem> ELECTRIC_THROTTLE_TRACK =
      blockItem(RailcraftBlocks.ELECTRIC_THROTTLE_TRACK, RailcraftTabs.OUTFITTED_TRACKS);

  public static final RegistryObject<BlockItem> ELECTRIC_ROUTING_TRACK =
      blockItem(RailcraftBlocks.ELECTRIC_ROUTING_TRACK, RailcraftTabs.OUTFITTED_TRACKS);

  public static final RegistryObject<BlockItem> HIGH_SPEED_TRACK =
      blockItem(RailcraftBlocks.HIGH_SPEED_TRACK, CreativeModeTab.TAB_TRANSPORTATION);

  public static final RegistryObject<BlockItem> HIGH_SPEED_TRANSITION_TRACK =
      blockItem(RailcraftBlocks.HIGH_SPEED_TRANSITION_TRACK, RailcraftTabs.OUTFITTED_TRACKS);

  public static final RegistryObject<BlockItem> HIGH_SPEED_LOCKING_TRACK =
      blockItem(RailcraftBlocks.HIGH_SPEED_LOCKING_TRACK, RailcraftTabs.OUTFITTED_TRACKS);

  public static final RegistryObject<BlockItem> HIGH_SPEED_ACTIVATOR_TRACK =
      blockItem(RailcraftBlocks.HIGH_SPEED_ACTIVATOR_TRACK, RailcraftTabs.OUTFITTED_TRACKS);

  public static final RegistryObject<BlockItem> HIGH_SPEED_BOOSTER_TRACK =
      blockItem(RailcraftBlocks.HIGH_SPEED_BOOSTER_TRACK, RailcraftTabs.OUTFITTED_TRACKS);

  public static final RegistryObject<BlockItem> HIGH_SPEED_DETECTOR_TRACK =
      blockItem(RailcraftBlocks.HIGH_SPEED_DETECTOR_TRACK, RailcraftTabs.OUTFITTED_TRACKS);

  public static final RegistryObject<BlockItem> HIGH_SPEED_WYE_TRACK =
      blockItem(RailcraftBlocks.HIGH_SPEED_WYE_TRACK, RailcraftTabs.OUTFITTED_TRACKS);

  public static final RegistryObject<BlockItem> HIGH_SPEED_TURNOUT_TRACK =
      blockItem(RailcraftBlocks.HIGH_SPEED_TURNOUT_TRACK, RailcraftTabs.OUTFITTED_TRACKS);

  public static final RegistryObject<BlockItem> HIGH_SPEED_JUNCTION_TRACK =
      blockItem(RailcraftBlocks.HIGH_SPEED_JUNCTION_TRACK, RailcraftTabs.OUTFITTED_TRACKS);

  public static final RegistryObject<BlockItem> HIGH_SPEED_WHISTLE_TRACK =
      blockItem(RailcraftBlocks.HIGH_SPEED_WHISTLE_TRACK, RailcraftTabs.OUTFITTED_TRACKS);

  public static final RegistryObject<BlockItem> HIGH_SPEED_LOCOMOTIVE_TRACK =
      blockItem(RailcraftBlocks.HIGH_SPEED_LOCOMOTIVE_TRACK, RailcraftTabs.OUTFITTED_TRACKS);

  public static final RegistryObject<BlockItem> HIGH_SPEED_THROTTLE_TRACK =
      blockItem(RailcraftBlocks.HIGH_SPEED_THROTTLE_TRACK, RailcraftTabs.OUTFITTED_TRACKS);

  public static final RegistryObject<BlockItem> HIGH_SPEED_ELECTRIC_TRACK =
      blockItem(RailcraftBlocks.HIGH_SPEED_ELECTRIC_TRACK, CreativeModeTab.TAB_TRANSPORTATION);

  public static final RegistryObject<BlockItem> HIGH_SPEED_ELECTRIC_TRANSITION_TRACK =
      blockItem(RailcraftBlocks.HIGH_SPEED_ELECTRIC_TRANSITION_TRACK, RailcraftTabs.OUTFITTED_TRACKS);

  public static final RegistryObject<BlockItem> HIGH_SPEED_ELECTRIC_LOCKING_TRACK =
      blockItem(RailcraftBlocks.HIGH_SPEED_ELECTRIC_LOCKING_TRACK, RailcraftTabs.OUTFITTED_TRACKS);

  public static final RegistryObject<BlockItem> HIGH_SPEED_ELECTRIC_ACTIVATOR_TRACK =
      blockItem(RailcraftBlocks.HIGH_SPEED_ELECTRIC_ACTIVATOR_TRACK, RailcraftTabs.OUTFITTED_TRACKS);

  public static final RegistryObject<BlockItem> HIGH_SPEED_ELECTRIC_BOOSTER_TRACK =
      blockItem(RailcraftBlocks.HIGH_SPEED_ELECTRIC_BOOSTER_TRACK, RailcraftTabs.OUTFITTED_TRACKS);

  public static final RegistryObject<BlockItem> HIGH_SPEED_ELECTRIC_DETECTOR_TRACK =
      blockItem(RailcraftBlocks.HIGH_SPEED_ELECTRIC_DETECTOR_TRACK, RailcraftTabs.OUTFITTED_TRACKS);

  public static final RegistryObject<BlockItem> HIGH_SPEED_ELECTRIC_WYE_TRACK =
      blockItem(RailcraftBlocks.HIGH_SPEED_ELECTRIC_WYE_TRACK, RailcraftTabs.OUTFITTED_TRACKS);

  public static final RegistryObject<BlockItem> HIGH_SPEED_ELECTRIC_TURNOUT_TRACK =
      blockItem(RailcraftBlocks.HIGH_SPEED_ELECTRIC_TURNOUT_TRACK, RailcraftTabs.OUTFITTED_TRACKS);

  public static final RegistryObject<BlockItem> HIGH_SPEED_ELECTRIC_JUNCTION_TRACK =
      blockItem(RailcraftBlocks.HIGH_SPEED_ELECTRIC_JUNCTION_TRACK, RailcraftTabs.OUTFITTED_TRACKS);

  public static final RegistryObject<BlockItem> HIGH_SPEED_ELECTRIC_WHISTLE_TRACK =
      blockItem(RailcraftBlocks.HIGH_SPEED_ELECTRIC_WHISTLE_TRACK, RailcraftTabs.OUTFITTED_TRACKS);

  public static final RegistryObject<BlockItem> HIGH_SPEED_ELECTRIC_LOCOMOTIVE_TRACK =
      blockItem(RailcraftBlocks.HIGH_SPEED_ELECTRIC_LOCOMOTIVE_TRACK, RailcraftTabs.OUTFITTED_TRACKS);

  public static final RegistryObject<BlockItem> HIGH_SPEED_ELECTRIC_THROTTLE_TRACK =
      blockItem(RailcraftBlocks.HIGH_SPEED_ELECTRIC_THROTTLE_TRACK, RailcraftTabs.OUTFITTED_TRACKS);

  public static final RegistryObject<BlockItem> IRON_LOCKING_TRACK =
      blockItem(RailcraftBlocks.IRON_LOCKING_TRACK, RailcraftTabs.OUTFITTED_TRACKS);

  public static final RegistryObject<BlockItem> IRON_BUFFER_STOP_TRACK =
      blockItem(RailcraftBlocks.IRON_BUFFER_STOP_TRACK, RailcraftTabs.OUTFITTED_TRACKS);

  public static final RegistryObject<BlockItem> IRON_ACTIVATOR_TRACK =
      blockItem(RailcraftBlocks.IRON_ACTIVATOR_TRACK, RailcraftTabs.OUTFITTED_TRACKS);

  public static final RegistryObject<BlockItem> IRON_BOOSTER_TRACK =
      blockItem(RailcraftBlocks.IRON_BOOSTER_TRACK, RailcraftTabs.OUTFITTED_TRACKS);

  public static final RegistryObject<BlockItem> IRON_CONTROL_TRACK =
      blockItem(RailcraftBlocks.IRON_CONTROL_TRACK, RailcraftTabs.OUTFITTED_TRACKS);

  public static final RegistryObject<BlockItem> IRON_GATED_TRACK =
      blockItem(RailcraftBlocks.IRON_GATED_TRACK, RailcraftTabs.OUTFITTED_TRACKS);

  public static final RegistryObject<BlockItem> IRON_DETECTOR_TRACK =
      blockItem(RailcraftBlocks.IRON_DETECTOR_TRACK, RailcraftTabs.OUTFITTED_TRACKS);

  public static final RegistryObject<BlockItem> IRON_COUPLER_TRACK =
      blockItem(RailcraftBlocks.IRON_COUPLER_TRACK, RailcraftTabs.OUTFITTED_TRACKS);

  public static final RegistryObject<BlockItem> IRON_EMBARKING_TRACK =
      blockItem(RailcraftBlocks.IRON_EMBARKING_TRACK, RailcraftTabs.OUTFITTED_TRACKS);

  public static final RegistryObject<BlockItem> IRON_DISEMBARKING_TRACK =
      blockItem(RailcraftBlocks.IRON_DISEMBARKING_TRACK, RailcraftTabs.OUTFITTED_TRACKS);

  public static final RegistryObject<BlockItem> IRON_DUMPING_TRACK =
      blockItem(RailcraftBlocks.IRON_DUMPING_TRACK, RailcraftTabs.OUTFITTED_TRACKS);

  public static final RegistryObject<BlockItem> IRON_WYE_TRACK =
      blockItem(RailcraftBlocks.IRON_WYE_TRACK, RailcraftTabs.OUTFITTED_TRACKS);

  public static final RegistryObject<BlockItem> IRON_TURNOUT_TRACK =
      blockItem(RailcraftBlocks.IRON_TURNOUT_TRACK, RailcraftTabs.OUTFITTED_TRACKS);

  public static final RegistryObject<BlockItem> IRON_JUNCTION_TRACK =
      blockItem(RailcraftBlocks.IRON_JUNCTION_TRACK, RailcraftTabs.OUTFITTED_TRACKS);

  public static final RegistryObject<BlockItem> IRON_LAUNCHER_TRACK =
      blockItem(RailcraftBlocks.IRON_LAUNCHER_TRACK, RailcraftTabs.OUTFITTED_TRACKS);

  public static final RegistryObject<BlockItem> IRON_ONE_WAY_TRACK =
      blockItem(RailcraftBlocks.IRON_ONE_WAY_TRACK, RailcraftTabs.OUTFITTED_TRACKS);

  public static final RegistryObject<BlockItem> IRON_WHISTLE_TRACK =
      blockItem(RailcraftBlocks.IRON_WHISTLE_TRACK, RailcraftTabs.OUTFITTED_TRACKS);

  public static final RegistryObject<BlockItem> IRON_LOCOMOTIVE_TRACK =
      blockItem(RailcraftBlocks.IRON_LOCOMOTIVE_TRACK, RailcraftTabs.OUTFITTED_TRACKS);

  public static final RegistryObject<BlockItem> IRON_THROTTLE_TRACK =
      blockItem(RailcraftBlocks.IRON_THROTTLE_TRACK, RailcraftTabs.OUTFITTED_TRACKS);

  public static final RegistryObject<BlockItem> IRON_ROUTING_TRACK =
      blockItem(RailcraftBlocks.IRON_ROUTING_TRACK, RailcraftTabs.OUTFITTED_TRACKS);

  public static final RegistryObject<BlockItem> REINFORCED_TRACK =
      blockItem(RailcraftBlocks.REINFORCED_TRACK, CreativeModeTab.TAB_TRANSPORTATION);

  public static final RegistryObject<BlockItem> REINFORCED_LOCKING_TRACK =
      blockItem(RailcraftBlocks.REINFORCED_LOCKING_TRACK, RailcraftTabs.OUTFITTED_TRACKS);

  public static final RegistryObject<BlockItem> REINFORCED_BUFFER_STOP_TRACK =
      blockItem(RailcraftBlocks.REINFORCED_BUFFER_STOP_TRACK, RailcraftTabs.OUTFITTED_TRACKS);

  public static final RegistryObject<BlockItem> REINFORCED_ACTIVATOR_TRACK =
      blockItem(RailcraftBlocks.REINFORCED_ACTIVATOR_TRACK, RailcraftTabs.OUTFITTED_TRACKS);

  public static final RegistryObject<BlockItem> REINFORCED_BOOSTER_TRACK =
      blockItem(RailcraftBlocks.REINFORCED_BOOSTER_TRACK, RailcraftTabs.OUTFITTED_TRACKS);

  public static final RegistryObject<BlockItem> REINFORCED_CONTROL_TRACK =
      blockItem(RailcraftBlocks.REINFORCED_CONTROL_TRACK, RailcraftTabs.OUTFITTED_TRACKS);

  public static final RegistryObject<BlockItem> REINFORCED_GATED_TRACK =
      blockItem(RailcraftBlocks.REINFORCED_GATED_TRACK, RailcraftTabs.OUTFITTED_TRACKS);

  public static final RegistryObject<BlockItem> REINFORCED_DETECTOR_TRACK =
      blockItem(RailcraftBlocks.REINFORCED_DETECTOR_TRACK, RailcraftTabs.OUTFITTED_TRACKS);

  public static final RegistryObject<BlockItem> REINFORCED_COUPLER_TRACK =
      blockItem(RailcraftBlocks.REINFORCED_COUPLER_TRACK, RailcraftTabs.OUTFITTED_TRACKS);

  public static final RegistryObject<BlockItem> REINFORCED_EMBARKING_TRACK =
      blockItem(RailcraftBlocks.REINFORCED_EMBARKING_TRACK, RailcraftTabs.OUTFITTED_TRACKS);

  public static final RegistryObject<BlockItem> REINFORCED_DISEMBARKING_TRACK =
      blockItem(RailcraftBlocks.REINFORCED_DISEMBARKING_TRACK, RailcraftTabs.OUTFITTED_TRACKS);

  public static final RegistryObject<BlockItem> REINFORCED_DUMPING_TRACK =
      blockItem(RailcraftBlocks.REINFORCED_DUMPING_TRACK, RailcraftTabs.OUTFITTED_TRACKS);

  public static final RegistryObject<BlockItem> REINFORCED_WYE_TRACK =
      blockItem(RailcraftBlocks.REINFORCED_WYE_TRACK, RailcraftTabs.OUTFITTED_TRACKS);

  public static final RegistryObject<BlockItem> REINFORCED_TURNOUT_TRACK =
      blockItem(RailcraftBlocks.REINFORCED_TURNOUT_TRACK, RailcraftTabs.OUTFITTED_TRACKS);

  public static final RegistryObject<BlockItem> REINFORCED_JUNCTION_TRACK =
      blockItem(RailcraftBlocks.REINFORCED_JUNCTION_TRACK, RailcraftTabs.OUTFITTED_TRACKS);

  public static final RegistryObject<BlockItem> REINFORCED_LAUNCHER_TRACK =
      blockItem(RailcraftBlocks.REINFORCED_LAUNCHER_TRACK, RailcraftTabs.OUTFITTED_TRACKS);

  public static final RegistryObject<BlockItem> REINFORCED_ONE_WAY_TRACK =
      blockItem(RailcraftBlocks.REINFORCED_ONE_WAY_TRACK, RailcraftTabs.OUTFITTED_TRACKS);

  public static final RegistryObject<BlockItem> REINFORCED_WHISTLE_TRACK =
      blockItem(RailcraftBlocks.REINFORCED_WHISTLE_TRACK, RailcraftTabs.OUTFITTED_TRACKS);

  public static final RegistryObject<BlockItem> REINFORCED_LOCOMOTIVE_TRACK =
      blockItem(RailcraftBlocks.REINFORCED_LOCOMOTIVE_TRACK, RailcraftTabs.OUTFITTED_TRACKS);

  public static final RegistryObject<BlockItem> REINFORCED_THROTTLE_TRACK =
      blockItem(RailcraftBlocks.REINFORCED_THROTTLE_TRACK, RailcraftTabs.OUTFITTED_TRACKS);

  public static final RegistryObject<BlockItem> REINFORCED_ROUTING_TRACK =
      blockItem(RailcraftBlocks.REINFORCED_ROUTING_TRACK, RailcraftTabs.OUTFITTED_TRACKS);

  public static final RegistryObject<BlockItem> STRAP_IRON_TRACK =
      blockItem(RailcraftBlocks.STRAP_IRON_TRACK, CreativeModeTab.TAB_TRANSPORTATION);

  public static final RegistryObject<BlockItem> STRAP_IRON_LOCKING_TRACK =
      blockItem(RailcraftBlocks.STRAP_IRON_LOCKING_TRACK, RailcraftTabs.OUTFITTED_TRACKS);

  public static final RegistryObject<BlockItem> STRAP_IRON_BUFFER_STOP_TRACK =
      blockItem(RailcraftBlocks.STRAP_IRON_BUFFER_STOP_TRACK, RailcraftTabs.OUTFITTED_TRACKS);

  public static final RegistryObject<BlockItem> STRAP_IRON_ACTIVATOR_TRACK =
      blockItem(RailcraftBlocks.STRAP_IRON_ACTIVATOR_TRACK, RailcraftTabs.OUTFITTED_TRACKS);

  public static final RegistryObject<BlockItem> STRAP_IRON_BOOSTER_TRACK =
      blockItem(RailcraftBlocks.STRAP_IRON_BOOSTER_TRACK, RailcraftTabs.OUTFITTED_TRACKS);

  public static final RegistryObject<BlockItem> STRAP_IRON_CONTROL_TRACK =
      blockItem(RailcraftBlocks.STRAP_IRON_CONTROL_TRACK, RailcraftTabs.OUTFITTED_TRACKS);

  public static final RegistryObject<BlockItem> STRAP_IRON_GATED_TRACK =
      blockItem(RailcraftBlocks.STRAP_IRON_GATED_TRACK, RailcraftTabs.OUTFITTED_TRACKS);

  public static final RegistryObject<BlockItem> STRAP_IRON_DETECTOR_TRACK =
      blockItem(RailcraftBlocks.STRAP_IRON_DETECTOR_TRACK, RailcraftTabs.OUTFITTED_TRACKS);

  public static final RegistryObject<BlockItem> STRAP_IRON_COUPLER_TRACK =
      blockItem(RailcraftBlocks.STRAP_IRON_COUPLER_TRACK, RailcraftTabs.OUTFITTED_TRACKS);

  public static final RegistryObject<BlockItem> STRAP_IRON_EMBARKING_TRACK =
      blockItem(RailcraftBlocks.STRAP_IRON_EMBARKING_TRACK, RailcraftTabs.OUTFITTED_TRACKS);

  public static final RegistryObject<BlockItem> STRAP_IRON_DISEMBARKING_TRACK =
      blockItem(RailcraftBlocks.STRAP_IRON_DISEMBARKING_TRACK, RailcraftTabs.OUTFITTED_TRACKS);

  public static final RegistryObject<BlockItem> STRAP_IRON_DUMPING_TRACK =
      blockItem(RailcraftBlocks.STRAP_IRON_DUMPING_TRACK, RailcraftTabs.OUTFITTED_TRACKS);

  public static final RegistryObject<BlockItem> STRAP_IRON_WYE_TRACK =
      blockItem(RailcraftBlocks.STRAP_IRON_WYE_TRACK, RailcraftTabs.OUTFITTED_TRACKS);

  public static final RegistryObject<BlockItem> STRAP_IRON_TURNOUT_TRACK =
      blockItem(RailcraftBlocks.STRAP_IRON_TURNOUT_TRACK, RailcraftTabs.OUTFITTED_TRACKS);

  public static final RegistryObject<BlockItem> STRAP_IRON_JUNCTION_TRACK =
      blockItem(RailcraftBlocks.STRAP_IRON_JUNCTION_TRACK, RailcraftTabs.OUTFITTED_TRACKS);

  public static final RegistryObject<BlockItem> STRAP_IRON_LAUNCHER_TRACK =
      blockItem(RailcraftBlocks.STRAP_IRON_LAUNCHER_TRACK, RailcraftTabs.OUTFITTED_TRACKS);

  public static final RegistryObject<BlockItem> STRAP_IRON_ONE_WAY_TRACK =
      blockItem(RailcraftBlocks.STRAP_IRON_ONE_WAY_TRACK, RailcraftTabs.OUTFITTED_TRACKS);

  public static final RegistryObject<BlockItem> STRAP_IRON_WHISTLE_TRACK =
      blockItem(RailcraftBlocks.STRAP_IRON_WHISTLE_TRACK, RailcraftTabs.OUTFITTED_TRACKS);

  public static final RegistryObject<BlockItem> STRAP_IRON_LOCOMOTIVE_TRACK =
      blockItem(RailcraftBlocks.STRAP_IRON_LOCOMOTIVE_TRACK, RailcraftTabs.OUTFITTED_TRACKS);

  public static final RegistryObject<BlockItem> STRAP_IRON_THROTTLE_TRACK =
      blockItem(RailcraftBlocks.STRAP_IRON_THROTTLE_TRACK, RailcraftTabs.OUTFITTED_TRACKS);

  public static final RegistryObject<BlockItem> STRAP_IRON_ROUTING_TRACK =
      blockItem(RailcraftBlocks.STRAP_IRON_ROUTING_TRACK, RailcraftTabs.OUTFITTED_TRACKS);

  public static final RegistryObject<BlockItem> ELEVATOR_TRACK =
      blockItem(RailcraftBlocks.ELEVATOR_TRACK, CreativeModeTab.TAB_TRANSPORTATION);

  public static final RegistryObject<CrowbarItem> IRON_CROWBAR =
      deferredRegister.register("iron_crowbar",
          () -> new CrowbarItem(2.5F, -2.8F, Tiers.IRON,
              new Item.Properties().tab(RailcraftTabs.MAIN)));

  public static final RegistryObject<CrowbarItem> STEEL_CROWBAR =
      deferredRegister.register("steel_crowbar",
          () -> new CrowbarItem(2.5F, -2.7F, RailcraftItemTier.STEEL,
              new Item.Properties().tab(RailcraftTabs.MAIN)));

  public static final RegistryObject<CrowbarItem> DIAMOND_CROWBAR =
      deferredRegister.register("diamond_crowbar",
          () -> new CrowbarItem(2.5F, -2.4F, Tiers.DIAMOND,
              new Item.Properties().tab(RailcraftTabs.MAIN)));

  public static final RegistryObject<SeasonsCrowbarItem> SEASONS_CROWBAR =
      deferredRegister.register("seasons_crowbar",
          () -> new SeasonsCrowbarItem(new Item.Properties().tab(RailcraftTabs.MAIN)));

  public static final RegistryObject<Item> TRACK_PARTS = registerBasic("track_parts", RailcraftTabs.MAIN);

  public static final RegistryObject<TrackKitItem> TRANSITION_TRACK_KIT =
      deferredRegister.register("transition_track_kit",
          () -> new TrackKitItem(new TrackKitItem.Properties()
              .tab(CreativeModeTab.TAB_TRANSPORTATION)
              .setAllowedOnSlopes(true)
              .addOutfittedBlock(TrackTypes.HIGH_SPEED, RailcraftBlocks.HIGH_SPEED_TRANSITION_TRACK)
              .addOutfittedBlock(TrackTypes.HIGH_SPEED_ELECTRIC,
                  RailcraftBlocks.HIGH_SPEED_ELECTRIC_TRANSITION_TRACK)
          ));

  public static final RegistryObject<TrackKitItem> LOCKING_TRACK_KIT =
      deferredRegister.register("locking_track_kit",
          () -> new TrackKitItem(new TrackKitItem.Properties()
              .tab(CreativeModeTab.TAB_TRANSPORTATION)
              .addOutfittedBlock(TrackTypes.ABANDONED, RailcraftBlocks.ABANDONED_LOCKING_TRACK)
              .addOutfittedBlock(TrackTypes.ELECTRIC, RailcraftBlocks.ELECTRIC_LOCKING_TRACK)
              .addOutfittedBlock(TrackTypes.HIGH_SPEED, RailcraftBlocks.HIGH_SPEED_LOCKING_TRACK)
              .addOutfittedBlock(TrackTypes.HIGH_SPEED_ELECTRIC,
                  RailcraftBlocks.HIGH_SPEED_ELECTRIC_LOCKING_TRACK)
              .addOutfittedBlock(TrackTypes.IRON, RailcraftBlocks.IRON_LOCKING_TRACK)
              .addOutfittedBlock(TrackTypes.REINFORCED, RailcraftBlocks.REINFORCED_LOCKING_TRACK)
              .addOutfittedBlock(TrackTypes.STRAP_IRON, RailcraftBlocks.STRAP_IRON_LOCKING_TRACK)));

  public static final RegistryObject<TrackKitItem> BUFFER_STOP_TRACK_KIT =
      deferredRegister.register("buffer_stop_track_kit",
          () -> new TrackKitItem(new TrackKitItem.Properties()
              .tab(CreativeModeTab.TAB_TRANSPORTATION)
              .addOutfittedBlock(TrackTypes.ABANDONED, RailcraftBlocks.ABANDONED_BUFFER_STOP_TRACK)
              .addOutfittedBlock(TrackTypes.ELECTRIC, RailcraftBlocks.ELECTRIC_BUFFER_STOP_TRACK)
              .addOutfittedBlock(TrackTypes.IRON, RailcraftBlocks.IRON_BUFFER_STOP_TRACK)
              .addOutfittedBlock(TrackTypes.REINFORCED,
                  RailcraftBlocks.REINFORCED_BUFFER_STOP_TRACK)
              .addOutfittedBlock(TrackTypes.STRAP_IRON,
                  RailcraftBlocks.STRAP_IRON_BUFFER_STOP_TRACK)));

  public static final RegistryObject<TrackKitItem> ACTIVATOR_TRACK_KIT =
      deferredRegister.register("activator_track_kit",
          () -> new TrackKitItem(new TrackKitItem.Properties()
              .tab(CreativeModeTab.TAB_TRANSPORTATION)
              .setAllowedOnSlopes(true)
              .addOutfittedBlock(TrackTypes.ABANDONED, RailcraftBlocks.ABANDONED_ACTIVATOR_TRACK)
              .addOutfittedBlock(TrackTypes.ELECTRIC, RailcraftBlocks.ELECTRIC_ACTIVATOR_TRACK)
              .addOutfittedBlock(TrackTypes.HIGH_SPEED, RailcraftBlocks.HIGH_SPEED_ACTIVATOR_TRACK)
              .addOutfittedBlock(TrackTypes.HIGH_SPEED_ELECTRIC,
                  RailcraftBlocks.HIGH_SPEED_ELECTRIC_ACTIVATOR_TRACK)
              .addOutfittedBlock(TrackTypes.IRON, RailcraftBlocks.IRON_ACTIVATOR_TRACK)
              .addOutfittedBlock(TrackTypes.REINFORCED, RailcraftBlocks.REINFORCED_ACTIVATOR_TRACK)
              .addOutfittedBlock(TrackTypes.STRAP_IRON,
                  RailcraftBlocks.STRAP_IRON_ACTIVATOR_TRACK)));

  public static final RegistryObject<TrackKitItem> BOOSTER_TRACK_KIT =
      deferredRegister.register("booster_track_kit",
          () -> new TrackKitItem(new TrackKitItem.Properties()
              .tab(CreativeModeTab.TAB_TRANSPORTATION)
              .setAllowedOnSlopes(true)
              .addOutfittedBlock(TrackTypes.ABANDONED, RailcraftBlocks.ABANDONED_BOOSTER_TRACK)
              .addOutfittedBlock(TrackTypes.ELECTRIC, RailcraftBlocks.ELECTRIC_BOOSTER_TRACK)
              .addOutfittedBlock(TrackTypes.HIGH_SPEED, RailcraftBlocks.HIGH_SPEED_BOOSTER_TRACK)
              .addOutfittedBlock(TrackTypes.HIGH_SPEED_ELECTRIC,
                  RailcraftBlocks.HIGH_SPEED_ELECTRIC_BOOSTER_TRACK)
              .addOutfittedBlock(TrackTypes.IRON, RailcraftBlocks.IRON_BOOSTER_TRACK)
              .addOutfittedBlock(TrackTypes.REINFORCED, RailcraftBlocks.REINFORCED_BOOSTER_TRACK)
              .addOutfittedBlock(TrackTypes.STRAP_IRON, RailcraftBlocks.STRAP_IRON_BOOSTER_TRACK)));

  public static final RegistryObject<TrackKitItem> CONTROL_TRACK_KIT =
      deferredRegister.register("control_track_kit",
          () -> new TrackKitItem(new TrackKitItem.Properties()
              .tab(CreativeModeTab.TAB_TRANSPORTATION)
              .setAllowedOnSlopes(true)
              .addOutfittedBlock(TrackTypes.ABANDONED, RailcraftBlocks.ABANDONED_CONTROL_TRACK)
              .addOutfittedBlock(TrackTypes.ELECTRIC, RailcraftBlocks.ELECTRIC_CONTROL_TRACK)
              .addOutfittedBlock(TrackTypes.IRON, RailcraftBlocks.IRON_CONTROL_TRACK)
              .addOutfittedBlock(TrackTypes.REINFORCED, RailcraftBlocks.REINFORCED_CONTROL_TRACK)
              .addOutfittedBlock(TrackTypes.STRAP_IRON, RailcraftBlocks.STRAP_IRON_CONTROL_TRACK)));

  public static final RegistryObject<TrackKitItem> GATED_TRACK_KIT =
      deferredRegister.register("gated_track_kit",
          () -> new TrackKitItem(new TrackKitItem.Properties()
              .tab(CreativeModeTab.TAB_TRANSPORTATION)
              .setAllowedOnSlopes(true)
              .addOutfittedBlock(TrackTypes.ABANDONED, RailcraftBlocks.ABANDONED_GATED_TRACK)
              .addOutfittedBlock(TrackTypes.ELECTRIC, RailcraftBlocks.ELECTRIC_GATED_TRACK)
              .addOutfittedBlock(TrackTypes.IRON, RailcraftBlocks.IRON_GATED_TRACK)
              .addOutfittedBlock(TrackTypes.REINFORCED, RailcraftBlocks.REINFORCED_GATED_TRACK)
              .addOutfittedBlock(TrackTypes.STRAP_IRON, RailcraftBlocks.STRAP_IRON_GATED_TRACK)));

  public static final RegistryObject<TrackKitItem> DETECTOR_TRACK_KIT =
      deferredRegister.register("detector_track_kit",
          () -> new TrackKitItem(new TrackKitItem.Properties()
              .tab(CreativeModeTab.TAB_TRANSPORTATION)
              .setAllowedOnSlopes(true)
              .addOutfittedBlock(TrackTypes.ABANDONED, RailcraftBlocks.ABANDONED_DETECTOR_TRACK)
              .addOutfittedBlock(TrackTypes.ELECTRIC, RailcraftBlocks.ELECTRIC_DETECTOR_TRACK)
              .addOutfittedBlock(TrackTypes.HIGH_SPEED, RailcraftBlocks.HIGH_SPEED_DETECTOR_TRACK)
              .addOutfittedBlock(TrackTypes.HIGH_SPEED_ELECTRIC,
                  RailcraftBlocks.HIGH_SPEED_ELECTRIC_DETECTOR_TRACK)
              .addOutfittedBlock(TrackTypes.IRON, RailcraftBlocks.IRON_DETECTOR_TRACK)
              .addOutfittedBlock(TrackTypes.REINFORCED, RailcraftBlocks.REINFORCED_DETECTOR_TRACK)
              .addOutfittedBlock(TrackTypes.STRAP_IRON,
                  RailcraftBlocks.STRAP_IRON_DETECTOR_TRACK)));

  public static final RegistryObject<TrackKitItem> COUPLER_TRACK_KIT =
      deferredRegister.register("coupler_track_kit",
          () -> new TrackKitItem(new TrackKitItem.Properties()
              .tab(CreativeModeTab.TAB_TRANSPORTATION)
              .setAllowedOnSlopes(true)
              .addOutfittedBlock(TrackTypes.ABANDONED, RailcraftBlocks.ABANDONED_COUPLER_TRACK)
              .addOutfittedBlock(TrackTypes.ELECTRIC, RailcraftBlocks.ELECTRIC_COUPLER_TRACK)
              .addOutfittedBlock(TrackTypes.IRON, RailcraftBlocks.IRON_COUPLER_TRACK)
              .addOutfittedBlock(TrackTypes.REINFORCED, RailcraftBlocks.REINFORCED_COUPLER_TRACK)
              .addOutfittedBlock(TrackTypes.STRAP_IRON, RailcraftBlocks.STRAP_IRON_COUPLER_TRACK)));

  public static final RegistryObject<TrackKitItem> EMBARKING_TRACK_KIT =
      deferredRegister.register("embarking_track_kit",
          () -> new TrackKitItem(new TrackKitItem.Properties()
              .tab(CreativeModeTab.TAB_TRANSPORTATION)
              .setAllowedOnSlopes(true)
              .addOutfittedBlock(TrackTypes.ABANDONED, RailcraftBlocks.ABANDONED_EMBARKING_TRACK)
              .addOutfittedBlock(TrackTypes.ELECTRIC, RailcraftBlocks.ELECTRIC_EMBARKING_TRACK)
              .addOutfittedBlock(TrackTypes.IRON, RailcraftBlocks.IRON_EMBARKING_TRACK)
              .addOutfittedBlock(TrackTypes.REINFORCED, RailcraftBlocks.REINFORCED_EMBARKING_TRACK)
              .addOutfittedBlock(TrackTypes.STRAP_IRON,
                  RailcraftBlocks.STRAP_IRON_EMBARKING_TRACK)));

  public static final RegistryObject<TrackKitItem> DISEMBARKING_TRACK_KIT =
      deferredRegister.register("disembarking_track_kit",
          () -> new TrackKitItem(new TrackKitItem.Properties()
              .tab(CreativeModeTab.TAB_TRANSPORTATION)
              .setAllowedOnSlopes(true)
              .addOutfittedBlock(TrackTypes.ABANDONED, RailcraftBlocks.ABANDONED_DISEMBARKING_TRACK)
              .addOutfittedBlock(TrackTypes.ELECTRIC, RailcraftBlocks.ELECTRIC_DISEMBARKING_TRACK)
              .addOutfittedBlock(TrackTypes.IRON, RailcraftBlocks.IRON_DISEMBARKING_TRACK)
              .addOutfittedBlock(TrackTypes.REINFORCED,
                  RailcraftBlocks.REINFORCED_DISEMBARKING_TRACK)
              .addOutfittedBlock(TrackTypes.STRAP_IRON,
                  RailcraftBlocks.STRAP_IRON_DISEMBARKING_TRACK)));

  public static final RegistryObject<TrackKitItem> DUMPING_TRACK_KIT =
      deferredRegister.register("dumping_track_kit",
          () -> new TrackKitItem(new TrackKitItem.Properties()
              .tab(CreativeModeTab.TAB_TRANSPORTATION)
              .setAllowedOnSlopes(true)
              .addOutfittedBlock(TrackTypes.ABANDONED, RailcraftBlocks.ABANDONED_DUMPING_TRACK)
              .addOutfittedBlock(TrackTypes.ELECTRIC, RailcraftBlocks.ELECTRIC_DUMPING_TRACK)
              .addOutfittedBlock(TrackTypes.IRON, RailcraftBlocks.IRON_DUMPING_TRACK)
              .addOutfittedBlock(TrackTypes.REINFORCED,
                  RailcraftBlocks.REINFORCED_DUMPING_TRACK)
              .addOutfittedBlock(TrackTypes.STRAP_IRON,
                  RailcraftBlocks.STRAP_IRON_DUMPING_TRACK)));

  public static final RegistryObject<TrackKitItem> LAUNCHER_TRACK_KIT =
      deferredRegister.register("launcher_track_kit",
          () -> new TrackKitItem(new TrackKitItem.Properties()
              .tab(CreativeModeTab.TAB_TRANSPORTATION)
              .addOutfittedBlock(TrackTypes.ABANDONED, RailcraftBlocks.ABANDONED_LAUNCHER_TRACK)
              .addOutfittedBlock(TrackTypes.ELECTRIC, RailcraftBlocks.ELECTRIC_LAUNCHER_TRACK)
              .addOutfittedBlock(TrackTypes.IRON, RailcraftBlocks.IRON_LAUNCHER_TRACK)
              .addOutfittedBlock(TrackTypes.REINFORCED,
                  RailcraftBlocks.REINFORCED_LAUNCHER_TRACK)
              .addOutfittedBlock(TrackTypes.STRAP_IRON,
                  RailcraftBlocks.STRAP_IRON_LAUNCHER_TRACK)));

  public static final RegistryObject<TrackKitItem> ONE_WAY_TRACK_KIT =
      deferredRegister.register("one_way_track_kit",
          () -> new TrackKitItem(new TrackKitItem.Properties()
              .tab(CreativeModeTab.TAB_TRANSPORTATION)
              .addOutfittedBlock(TrackTypes.ABANDONED, RailcraftBlocks.ABANDONED_ONE_WAY_TRACK)
              .addOutfittedBlock(TrackTypes.ELECTRIC, RailcraftBlocks.ELECTRIC_ONE_WAY_TRACK)
              .addOutfittedBlock(TrackTypes.IRON, RailcraftBlocks.IRON_ONE_WAY_TRACK)
              .addOutfittedBlock(TrackTypes.REINFORCED,
                  RailcraftBlocks.REINFORCED_ONE_WAY_TRACK)
              .addOutfittedBlock(TrackTypes.STRAP_IRON,
                  RailcraftBlocks.STRAP_IRON_ONE_WAY_TRACK)));

  public static final RegistryObject<TrackKitItem> WHISTLE_TRACK_KIT =
      deferredRegister.register("whistle_track_kit",
          () -> new TrackKitItem(new TrackKitItem.Properties()
              .tab(CreativeModeTab.TAB_TRANSPORTATION)
              .addOutfittedBlock(TrackTypes.ABANDONED, RailcraftBlocks.ABANDONED_WHISTLE_TRACK)
              .addOutfittedBlock(TrackTypes.ELECTRIC, RailcraftBlocks.ELECTRIC_WHISTLE_TRACK)
              .addOutfittedBlock(TrackTypes.HIGH_SPEED, RailcraftBlocks.HIGH_SPEED_WHISTLE_TRACK)
              .addOutfittedBlock(TrackTypes.HIGH_SPEED_ELECTRIC,
                  RailcraftBlocks.HIGH_SPEED_ELECTRIC_WHISTLE_TRACK)
              .addOutfittedBlock(TrackTypes.IRON, RailcraftBlocks.IRON_WHISTLE_TRACK)
              .addOutfittedBlock(TrackTypes.REINFORCED, RailcraftBlocks.REINFORCED_WHISTLE_TRACK)
              .addOutfittedBlock(TrackTypes.STRAP_IRON, RailcraftBlocks.STRAP_IRON_WHISTLE_TRACK)));

  public static final RegistryObject<TrackKitItem> LOCOMOTIVE_TRACK_KIT =
      deferredRegister.register("locomotive_track_kit",
          () -> new TrackKitItem(new TrackKitItem.Properties()
              .tab(CreativeModeTab.TAB_TRANSPORTATION)
              .addOutfittedBlock(TrackTypes.ABANDONED, RailcraftBlocks.ABANDONED_LOCOMOTIVE_TRACK)
              .addOutfittedBlock(TrackTypes.ELECTRIC, RailcraftBlocks.ELECTRIC_LOCOMOTIVE_TRACK)
              .addOutfittedBlock(TrackTypes.HIGH_SPEED, RailcraftBlocks.HIGH_SPEED_LOCOMOTIVE_TRACK)
              .addOutfittedBlock(TrackTypes.HIGH_SPEED_ELECTRIC,
                  RailcraftBlocks.HIGH_SPEED_ELECTRIC_LOCOMOTIVE_TRACK)
              .addOutfittedBlock(TrackTypes.IRON, RailcraftBlocks.IRON_LOCOMOTIVE_TRACK)
              .addOutfittedBlock(TrackTypes.REINFORCED, RailcraftBlocks.REINFORCED_LOCOMOTIVE_TRACK)
              .addOutfittedBlock(TrackTypes.STRAP_IRON,
                  RailcraftBlocks.STRAP_IRON_LOCOMOTIVE_TRACK)));

  public static final RegistryObject<TrackKitItem> THROTTLE_TRACK_KIT =
      deferredRegister.register("throttle_track_kit",
          () -> new TrackKitItem(new TrackKitItem.Properties()
              .tab(CreativeModeTab.TAB_TRANSPORTATION)
              .addOutfittedBlock(TrackTypes.ABANDONED, RailcraftBlocks.ABANDONED_THROTTLE_TRACK)
              .addOutfittedBlock(TrackTypes.ELECTRIC, RailcraftBlocks.ELECTRIC_THROTTLE_TRACK)
              .addOutfittedBlock(TrackTypes.HIGH_SPEED, RailcraftBlocks.HIGH_SPEED_THROTTLE_TRACK)
              .addOutfittedBlock(TrackTypes.HIGH_SPEED_ELECTRIC,
                  RailcraftBlocks.HIGH_SPEED_ELECTRIC_THROTTLE_TRACK)
              .addOutfittedBlock(TrackTypes.IRON, RailcraftBlocks.IRON_THROTTLE_TRACK)
              .addOutfittedBlock(TrackTypes.REINFORCED, RailcraftBlocks.REINFORCED_THROTTLE_TRACK)
              .addOutfittedBlock(TrackTypes.STRAP_IRON,
                  RailcraftBlocks.STRAP_IRON_THROTTLE_TRACK)));

  public static final RegistryObject<TrackKitItem> ROUTING_TRACK_KIT =
      deferredRegister.register("routing_track_kit",
          () -> new TrackKitItem(new TrackKitItem.Properties()
              .tab(CreativeModeTab.TAB_TRANSPORTATION)
              .addOutfittedBlock(TrackTypes.ABANDONED, RailcraftBlocks.ABANDONED_ROUTING_TRACK)
              .addOutfittedBlock(TrackTypes.ELECTRIC, RailcraftBlocks.ELECTRIC_ROUTING_TRACK)
              .addOutfittedBlock(TrackTypes.IRON, RailcraftBlocks.IRON_ROUTING_TRACK)
              .addOutfittedBlock(TrackTypes.REINFORCED, RailcraftBlocks.REINFORCED_ROUTING_TRACK)
              .addOutfittedBlock(TrackTypes.STRAP_IRON, RailcraftBlocks.STRAP_IRON_ROUTING_TRACK)));

  public static final RegistryObject<GogglesItem> GOGGLES =
      deferredRegister.register("goggles", () -> new GogglesItem(new Item.Properties().tab(RailcraftTabs.MAIN)));

  public static final RegistryObject<BlockItem> MANUAL_ROLLING_MACHINE =
      blockItem(RailcraftBlocks.MANUAL_ROLLING_MACHINE, RailcraftTabs.MAIN);

  public static final RegistryObject<BlockItem> POWERED_ROLLING_MACHINE =
      blockItem(RailcraftBlocks.POWERED_ROLLING_MACHINE, RailcraftTabs.MAIN);

  public static final RegistryObject<BlockItem> CRUSHER = blockItem(RailcraftBlocks.CRUSHER, RailcraftTabs.MAIN);

  public static final RegistryObject<BlockItem> COKE_OVEN_BRICKS =
      blockItem(RailcraftBlocks.COKE_OVEN_BRICKS, RailcraftTabs.MAIN);

  public static final RegistryObject<BlockItem> STEAM_OVEN = blockItem(RailcraftBlocks.STEAM_OVEN, RailcraftTabs.MAIN);

  public static final RegistryObject<BlockItem> CRUSHED_OBSIDIAN =
      blockItem(RailcraftBlocks.CRUSHED_OBSIDIAN, RailcraftTabs.MAIN);

  // ================================================================================
  // Crafting Materials
  // ================================================================================

  public static final RegistryObject<CoalCokeItem> COAL_COKE =
      deferredRegister.register("coal_coke", () -> new CoalCokeItem(new Item.Properties().tab(RailcraftTabs.MAIN)));

  public static final RegistryObject<Item> STEEL_PLATE = registerBasic("steel_plate", RailcraftTabs.MAIN);
  public static final RegistryObject<Item> IRON_PLATE = registerBasic("iron_plate", RailcraftTabs.MAIN);
  public static final RegistryObject<Item> TIN_PLATE = registerBasic("tin_plate", RailcraftTabs.MAIN);
  public static final RegistryObject<Item> GOLD_PLATE = registerBasic("gold_plate", RailcraftTabs.MAIN);
  public static final RegistryObject<Item> LEAD_PLATE = registerBasic("lead_plate", RailcraftTabs.MAIN);
  public static final RegistryObject<Item> ZINC_PLATE = registerBasic("zinc_plate", RailcraftTabs.MAIN);
  public static final RegistryObject<Item> BRASS_PLATE = registerBasic("brass_plate", RailcraftTabs.MAIN);
  public static final RegistryObject<Item> INVAR_PLATE = registerBasic("invar_plate", RailcraftTabs.MAIN);
  public static final RegistryObject<Item> BRONZE_PLATE = registerBasic("bronze_plate", RailcraftTabs.MAIN);
  public static final RegistryObject<Item> COPPER_PLATE = registerBasic("copper_plate", RailcraftTabs.MAIN);
  public static final RegistryObject<Item> NICKEL_PLATE = registerBasic("nickel_plate", RailcraftTabs.MAIN);
  public static final RegistryObject<Item> SILVER_PLATE = registerBasic("silver_plate", RailcraftTabs.MAIN);

  public static final RegistryObject<Item> STEEL_INGOT = registerBasic("steel_ingot", RailcraftTabs.MAIN);
  public static final RegistryObject<Item> TIN_INGOT = registerBasic("tin_ingot", RailcraftTabs.MAIN);
  public static final RegistryObject<Item> ZINC_INGOT = registerBasic("zinc_ingot", RailcraftTabs.MAIN);
  public static final RegistryObject<Item> BRASS_INGOT = registerBasic("brass_ingot", RailcraftTabs.MAIN);
  public static final RegistryObject<Item> BRONZE_INGOT = registerBasic("bronze_ingot", RailcraftTabs.MAIN);
  public static final RegistryObject<Item> NICKEL_INGOT = registerBasic("nickel_ingot", RailcraftTabs.MAIN);
  public static final RegistryObject<Item> INVAR_INGOT = registerBasic("invar_ingot", RailcraftTabs.MAIN);
  public static final RegistryObject<Item> LEAD_INGOT = registerBasic("lead_ingot", RailcraftTabs.MAIN);
  public static final RegistryObject<Item> SILVER_INGOT = registerBasic("silver_ingot", RailcraftTabs.MAIN);

  public static final RegistryObject<Item> SALTPETER_DUST = registerBasic("saltpeter_dust", RailcraftTabs.MAIN);
  public static final RegistryObject<Item> COAL_DUST = registerBasic("coal_dust", RailcraftTabs.MAIN);
  public static final RegistryObject<Item> CHARCOAL_DUST = registerBasic("charcoal_dust", RailcraftTabs.MAIN);
  public static final RegistryObject<Item> SLAG = registerBasic("slag", RailcraftTabs.MAIN);
  public static final RegistryObject<Item> ENDER_DUST = registerBasic("ender_dust", RailcraftTabs.MAIN);
  public static final RegistryObject<Item> SULFUR_DUST = registerBasic("sulfur_dust", RailcraftTabs.MAIN);
  public static final RegistryObject<Item> OBSIDIAN_DUST = registerBasic("obsidian_dust", RailcraftTabs.MAIN);

  public static final RegistryObject<Item> STEEL_NUGGET = registerBasic("steel_nugget", RailcraftTabs.MAIN);
  public static final RegistryObject<Item> TIN_NUGGET = registerBasic("tin_nugget", RailcraftTabs.MAIN);
  public static final RegistryObject<Item> ZINC_NUGGET = registerBasic("zinc_nugget", RailcraftTabs.MAIN);
  public static final RegistryObject<Item> BRASS_NUGGET = registerBasic("brass_nugget", RailcraftTabs.MAIN);
  public static final RegistryObject<Item> BRONZE_NUGGET = registerBasic("bronze_nugget", RailcraftTabs.MAIN);
  public static final RegistryObject<Item> NICKEL_NUGGET = registerBasic("nickel_nugget", RailcraftTabs.MAIN);
  public static final RegistryObject<Item> INVAR_NUGGET = registerBasic("invar_nugget", RailcraftTabs.MAIN);
  public static final RegistryObject<Item> SILVER_NUGGET = registerBasic("silver_nugget", RailcraftTabs.MAIN);
  public static final RegistryObject<Item> LEAD_NUGGET = registerBasic("lead_nugget", RailcraftTabs.MAIN);

  public static final RegistryObject<Item> TIN_RAW = registerBasic("tin_raw", RailcraftTabs.MAIN);
  public static final RegistryObject<Item> ZINC_RAW = registerBasic("zinc_raw", RailcraftTabs.MAIN);
  public static final RegistryObject<Item> NICKEL_RAW = registerBasic("nickel_raw", RailcraftTabs.MAIN);
  public static final RegistryObject<Item> SILVER_RAW = registerBasic("silver_raw", RailcraftTabs.MAIN);
  public static final RegistryObject<Item> LEAD_RAW = registerBasic("lead_raw", RailcraftTabs.MAIN);

  public static final RegistryObject<Item> BUSHING_GEAR = registerBasic("bushing_gear", RailcraftTabs.MAIN);
  public static final RegistryObject<Item> TIN_GEAR = registerBasic("tin_gear", RailcraftTabs.MAIN);
  public static final RegistryObject<Item> GOLD_GEAR = registerBasic("gold_gear", RailcraftTabs.MAIN);
  public static final RegistryObject<Item> IRON_GEAR = registerBasic("iron_gear", RailcraftTabs.MAIN);
  public static final RegistryObject<Item> LEAD_GEAR = registerBasic("lead_gear", RailcraftTabs.MAIN);
  public static final RegistryObject<Item> ZINC_GEAR = registerBasic("zinc_gear", RailcraftTabs.MAIN);
  public static final RegistryObject<Item> BRASS_GEAR = registerBasic("brass_gear", RailcraftTabs.MAIN);
  public static final RegistryObject<Item> INVAR_GEAR = registerBasic("invar_gear", RailcraftTabs.MAIN);
  public static final RegistryObject<Item> STEEL_GEAR = registerBasic("steel_gear", RailcraftTabs.MAIN);
  public static final RegistryObject<Item> BRONZE_GEAR = registerBasic("bronze_gear", RailcraftTabs.MAIN);
  public static final RegistryObject<Item> COPPER_GEAR = registerBasic("copper_gear", RailcraftTabs.MAIN);
  public static final RegistryObject<Item> NICKEL_GEAR = registerBasic("nickel_gear", RailcraftTabs.MAIN);
  public static final RegistryObject<Item> SILVER_GEAR = registerBasic("silver_gear", RailcraftTabs.MAIN);

  public static final RegistryObject<Item> TIN_ELECTRODE = registerBasic("tin_electrode", RailcraftTabs.MAIN);
  public static final RegistryObject<Item> GOLD_ELECTRODE = registerBasic("gold_electrode", RailcraftTabs.MAIN);
  public static final RegistryObject<Item> IRON_ELECTRODE = registerBasic("iron_electrode", RailcraftTabs.MAIN);
  public static final RegistryObject<Item> LEAD_ELECTRODE = registerBasic("lead_electrode", RailcraftTabs.MAIN);
  public static final RegistryObject<Item> ZINC_ELECTRODE = registerBasic("zinc_electrode", RailcraftTabs.MAIN);
  public static final RegistryObject<Item> BRASS_ELECTRODE = registerBasic("brass_electrode", RailcraftTabs.MAIN);
  public static final RegistryObject<Item> INVAR_ELECTRODE = registerBasic("invar_electrode", RailcraftTabs.MAIN);
  public static final RegistryObject<Item> STEEL_ELECTRODE = registerBasic("steel_electrode", RailcraftTabs.MAIN);
  public static final RegistryObject<Item> BRONZE_ELECTRODE = registerBasic("bronze_electrode", RailcraftTabs.MAIN);
  public static final RegistryObject<Item> CARBON_ELECTRODE = registerBasic("carbon_electrode", RailcraftTabs.MAIN);
  public static final RegistryObject<Item> COPPER_ELECTRODE = registerBasic("copper_electrode", RailcraftTabs.MAIN);
  public static final RegistryObject<Item> NICKEL_ELECTRODE = registerBasic("nickel_electrode", RailcraftTabs.MAIN);
  public static final RegistryObject<Item> SILVER_ELECTRODE = registerBasic("silver_electrode", RailcraftTabs.MAIN);

  public static final RegistryObject<Item> CONTROLLER_CIRCUIT =
      registerBasic("controller_circuit", RailcraftTabs.MAIN);
  public static final RegistryObject<Item> RECEIVER_CIRCUIT = registerBasic("receiver_circuit", RailcraftTabs.MAIN);
  public static final RegistryObject<Item> SIGNAL_CIRCUIT = registerBasic("signal_circuit", RailcraftTabs.MAIN);
  public static final RegistryObject<Item> RADIO_CIRCUIT = registerBasic("radio_circuit", RailcraftTabs.MAIN);

  public static final RegistryObject<Item> WOODEN_RAIL = registerBasic("wooden_rail", RailcraftTabs.MAIN);
  public static final RegistryObject<Item> STANDARD_RAIL = registerBasic("standard_rail", RailcraftTabs.MAIN);
  public static final RegistryObject<Item> ADVANCED_RAIL = registerBasic("advanced_rail", RailcraftTabs.MAIN);
  public static final RegistryObject<Item> REINFORCED_RAIL = registerBasic("reinforced_rail", RailcraftTabs.MAIN);
  public static final RegistryObject<Item> HIGH_SPEED_RAIL = registerBasic("high_speed_rail", RailcraftTabs.MAIN);
  public static final RegistryObject<Item> ELECTRIC_RAIL = registerBasic("electric_rail", RailcraftTabs.MAIN);

  public static final RegistryObject<Item> BAG_OF_CEMENT = registerBasic("bag_of_cement", RailcraftTabs.MAIN);

  public static final RegistryObject<Item> WOODEN_TIE = registerBasic("wooden_tie", RailcraftTabs.MAIN);
  public static final RegistryObject<Item> STONE_TIE = registerBasic("stone_tie", RailcraftTabs.MAIN);

  public static final RegistryObject<Item> REBAR = registerBasic("rebar", RailcraftTabs.MAIN);
  public static final RegistryObject<Item> WOODEN_RAILBED = registerBasic("wooden_railbed", RailcraftTabs.MAIN);
  public static final RegistryObject<Item> STONE_RAILBED = registerBasic("stone_railbed", RailcraftTabs.MAIN);

  public static final RegistryObject<Item> SIGNAL_LAMP = registerBasic("signal_lamp", RailcraftTabs.MAIN);

  public static final RegistryObject<Item> CHARGE_SPOOL_LARGE =
      registerBasic("charge_spool_large", RailcraftTabs.MAIN);
  public static final RegistryObject<Item> CHARGE_SPOOL_MEDIUM =
      registerBasic("charge_spool_medium", RailcraftTabs.MAIN);
  public static final RegistryObject<Item> CHARGE_SPOOL_SMALL =
      registerBasic("charge_spool_small", RailcraftTabs.MAIN);

  public static final RegistryObject<Item> CHARGE_MOTOR = registerBasic("charge_motor", RailcraftTabs.MAIN);

  public static final RegistryObject<Item> CHARGE_COIL = registerBasic("charge_coil", RailcraftTabs.MAIN);

  public static final RegistryObject<Item> CHARGE_TERMINAL = registerBasic("charge_terminal", RailcraftTabs.MAIN);

  public static final RegistryObject<BlockItem> WATER_TANK_SIDING =
      blockItem(RailcraftBlocks.WATER_TANK_SIDING, RailcraftTabs.MAIN);

  public static final RegistryObject<BlockItem> QUARRIED_STONE =
      blockItem(RailcraftBlocks.QUARRIED_STONE, RailcraftTabs.DECORATIVE);

  public static final RegistryObject<BlockItem> QUARRIED_COBBLESTONE =
      blockItem(RailcraftBlocks.QUARRIED_COBBLESTONE, RailcraftTabs.DECORATIVE);

  public static final RegistryObject<BlockItem> POLISHED_QUARRIED_STONE =
      blockItem(RailcraftBlocks.POLISHED_QUARRIED_STONE, RailcraftTabs.DECORATIVE);

  public static final RegistryObject<BlockItem> CHISELED_QUARRIED_STONE =
      blockItem(RailcraftBlocks.CHISELED_QUARRIED_STONE, RailcraftTabs.DECORATIVE);

  public static final RegistryObject<BlockItem> ETCHED_QUARRIED_STONE =
      blockItem(RailcraftBlocks.ETCHED_QUARRIED_STONE, RailcraftTabs.DECORATIVE);

  public static final RegistryObject<BlockItem> QUARRIED_BRICKS =
      blockItem(RailcraftBlocks.QUARRIED_BRICKS, RailcraftTabs.DECORATIVE);

  public static final RegistryObject<BlockItem> QUARRIED_BRICK_STAIRS =
      blockItem(RailcraftBlocks.QUARRIED_BRICK_STAIRS, RailcraftTabs.DECORATIVE);

  public static final RegistryObject<BlockItem> QUARRIED_BRICK_SLAB =
      blockItem(RailcraftBlocks.QUARRIED_BRICK_SLAB, RailcraftTabs.DECORATIVE);

  public static final RegistryObject<BlockItem> QUARRIED_PAVER =
      blockItem(RailcraftBlocks.QUARRIED_PAVER, RailcraftTabs.DECORATIVE);

  public static final RegistryObject<BlockItem> QUARRIED_PAVER_STAIRS =
      blockItem(RailcraftBlocks.QUARRIED_PAVER_STAIRS, RailcraftTabs.DECORATIVE);

  public static final RegistryObject<BlockItem> QUARRIED_PAVER_SLAB =
      blockItem(RailcraftBlocks.QUARRIED_PAVER_SLAB, RailcraftTabs.DECORATIVE);

  public static final RegistryObject<BlockItem> ABYSSAL_STONE =
      blockItem(RailcraftBlocks.ABYSSAL_STONE, RailcraftTabs.DECORATIVE);

  public static final RegistryObject<BlockItem> ABYSSAL_COBBLESTONE =
      blockItem(RailcraftBlocks.ABYSSAL_COBBLESTONE, RailcraftTabs.DECORATIVE);

  public static final RegistryObject<BlockItem> POLISHED_ABYSSAL_STONE =
      blockItem(RailcraftBlocks.POLISHED_ABYSSAL_STONE, RailcraftTabs.DECORATIVE);

  public static final RegistryObject<BlockItem> CHISELED_ABYSSAL_STONE =
      blockItem(RailcraftBlocks.CHISELED_ABYSSAL_STONE, RailcraftTabs.DECORATIVE);

  public static final RegistryObject<BlockItem> ETCHED_ABYSSAL_STONE =
      blockItem(RailcraftBlocks.ETCHED_ABYSSAL_STONE, RailcraftTabs.DECORATIVE);

  public static final RegistryObject<BlockItem> ABYSSAL_BRICKS =
      blockItem(RailcraftBlocks.ABYSSAL_BRICKS, RailcraftTabs.DECORATIVE);

  public static final RegistryObject<BlockItem> ABYSSAL_BRICK_STAIRS =
      blockItem(RailcraftBlocks.ABYSSAL_BRICK_STAIRS, RailcraftTabs.DECORATIVE);

  public static final RegistryObject<BlockItem> ABYSSAL_BRICK_SLAB =
      blockItem(RailcraftBlocks.ABYSSAL_BRICK_SLAB, RailcraftTabs.DECORATIVE);

  public static final RegistryObject<BlockItem> ABYSSAL_PAVER =
      blockItem(RailcraftBlocks.ABYSSAL_PAVER, RailcraftTabs.DECORATIVE);

  public static final RegistryObject<BlockItem> ABYSSAL_PAVER_STAIRS =
      blockItem(RailcraftBlocks.ABYSSAL_PAVER_STAIRS, RailcraftTabs.DECORATIVE);

  public static final RegistryObject<BlockItem> ABYSSAL_PAVER_SLAB =
      blockItem(RailcraftBlocks.ABYSSAL_PAVER_SLAB, RailcraftTabs.DECORATIVE);

  public static final RegistryObject<BlockItem> WORLD_SPIKE =
      blockItem(RailcraftBlocks.WORLD_SPIKE, RailcraftTabs.MAIN);

  public static final RegistryObject<BlockItem> PERSONAL_WORLD_SPIKE =
      blockItem(RailcraftBlocks.PERSONAL_WORLD_SPIKE, RailcraftTabs.MAIN);

  // ================================================================================
  // Buckets
  // ================================================================================

  public static final RegistryObject<BucketItem> CREOSOTE_BUCKET =
      deferredRegister.register("creosote_bucket",
          () -> new BucketItem(RailcraftFluids.CREOSOTE,
              new Item.Properties()
                  .stacksTo(1)
                  .craftRemainder(Items.BUCKET)
                  .tab(RailcraftTabs.MAIN)) {
            @Override
            public ICapabilityProvider initCapabilities(ItemStack stack,
                                                        @Nullable CompoundTag nbt) {
              return new FluidBucketWrapper(stack);
            }

            @Override
            public int getBurnTime(ItemStack itemStack, @Nullable RecipeType<?> recipeType) {
              return 800;
            }
          });

  public static final RegistryObject<FluidBottleItem> CREOSOTE_BOTTLE =
      deferredRegister.register("creosote_bottle",
          () -> new FluidBottleItem(RailcraftFluids.CREOSOTE,
              new Item.Properties()
                  .stacksTo(16)
                  .craftRemainder(Items.GLASS_BOTTLE)
                  .tab(RailcraftTabs.MAIN)));

  // ================================================================================
  // Utils
  // ================================================================================

  private static RegistryObject<Item> registerBasic(String name, CreativeModeTab tab) {
    return deferredRegister.register(name, () -> new Item(new Item.Properties().tab(tab)));
  }

  private static RegistryObject<BlockItem> blockItem(RegistryObject<? extends Block> block, CreativeModeTab tab) {
    var name = block.getId().getPath();
    return deferredRegister.register(name, () -> new BlockItem(block.get(), new Properties().tab(tab)));
  }

  private static <B extends Block> VariantRegistrar<DyeColor, BlockItem> registerBlockVariants(
      VariantRegistrar<DyeColor, B> blockRegistrar, CreativeModeTab tab) {
    var itemRegistrar = VariantRegistrar.<DyeColor, BlockItem>from(DyeColor.class, deferredRegister);
    itemRegistrar.registerUsing(blockRegistrar, block -> {
      return new BlockItem(block, new Properties().tab(tab));
    });
    return itemRegistrar;
  }
}
