package mods.railcraft.world.item;

import javax.annotation.Nullable;
import mods.railcraft.Railcraft;
import mods.railcraft.world.entity.vehicle.TankMinecart;
import mods.railcraft.world.entity.vehicle.TrackLayer;
import mods.railcraft.world.entity.vehicle.TrackRemover;
import mods.railcraft.world.entity.vehicle.locomotive.CreativeLocomotive;
import mods.railcraft.world.entity.vehicle.locomotive.ElectricLocomotive;
import mods.railcraft.world.entity.vehicle.locomotive.SteamLocomotive;
import mods.railcraft.world.level.block.RailcraftBlocks;
import mods.railcraft.world.level.block.track.TrackTypes;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.ShearsItem;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class RailcraftItems {

  public static final DeferredRegister<Item> ITEMS =
      DeferredRegister.create(ForgeRegistries.ITEMS, Railcraft.ID);

  /**
   * Railcraft's creative tab.
   */
  public static final CreativeModeTab TAB = new CreativeModeTab(Railcraft.ID) {
    @Override
    public ItemStack makeIcon() {
      return new ItemStack(IRON_CROWBAR.get());
    }
  };

  public static final CreativeModeTab OUTFITTED_TRACKS_TAB =
      new CreativeModeTab(Railcraft.ID + "_outfitted_tracks") {
        @Override
        public ItemStack makeIcon() {
          return new ItemStack(IRON_DETECTOR_TRACK.get());
        }
      };

  public static final RegistryObject<Item> FEED_STATION =
      ITEMS.register("feed_station",
          () -> new BlockItem(RailcraftBlocks.FEED_STATION.get(), new Item.Properties().tab(TAB)));

  public static final RegistryObject<Item> STEEL_ANVIL =
      ITEMS.register("steel_anvil",
          () -> new BlockItem(RailcraftBlocks.STEEL_ANVIL.get(), new Item.Properties().tab(TAB)));

  public static final RegistryObject<Item> CHIPPED_STEEL_ANVIL =
      ITEMS.register("chipped_steel_anvil",
          () -> new BlockItem(RailcraftBlocks.CHIPPED_STEEL_ANVIL.get(),
              new Item.Properties().tab(TAB)));

  public static final RegistryObject<Item> DAMAGED_STEEL_ANVIL =
      ITEMS.register("damaged_steel_anvil",
          () -> new BlockItem(RailcraftBlocks.DAMAGED_STEEL_ANVIL.get(),
              new Item.Properties().tab(TAB)));

  public static final RegistryObject<Item> STEEL_BLOCK =
      ITEMS.register("steel_block",
          () -> new BlockItem(RailcraftBlocks.STEEL_BLOCK.get(), new Item.Properties().tab(TAB)));

  public static final RegistryObject<Item> STEEL_SHEARS =
      ITEMS.register("steel_shears",
          () -> new ShearsItem(new Item.Properties()
              .durability(500)
              .tab(TAB)));

  public static final RegistryObject<Item> STEEL_SWORD =
      ITEMS.register("steel_sword",
          () -> new SwordItem(RailcraftItemTier.STEEL, 3, -2.4F,
              new Item.Properties().tab(TAB)));

  public static final RegistryObject<Item> STEEL_SHOVEL =
      ITEMS.register("steel_shovel",
          () -> new ShovelItem(RailcraftItemTier.STEEL, 1.5F, -3.0F,
              new Item.Properties().tab(TAB)));

  public static final RegistryObject<Item> STEEL_PICKAXE =
      ITEMS.register("steel_pickaxe",
          () -> new PickaxeItem(RailcraftItemTier.STEEL, 1, -2.8F,
              new Item.Properties().tab(TAB)));

  public static final RegistryObject<Item> STEEL_AXE =
      ITEMS.register("steel_axe",
          () -> new AxeItem(RailcraftItemTier.STEEL, 8.0F, -3F,
              new Item.Properties().tab(TAB)));

  public static final RegistryObject<Item> STEEL_HOE =
      ITEMS.register("steel_hoe",
          () -> new HoeItem(RailcraftItemTier.STEEL, -2, -0.5F,
              new Item.Properties().tab(TAB)));

  public static final RegistryObject<Item> STEEL_BOOTS =
      ITEMS.register("steel_boots",
          () -> new ArmorItem(RailcraftArmorMaterial.STEEL, EquipmentSlot.FEET,
              new Item.Properties().tab(TAB)));

  public static final RegistryObject<Item> STEEL_CHESTPLATE =
      ITEMS.register("steel_chestplate",
          () -> new ArmorItem(RailcraftArmorMaterial.STEEL, EquipmentSlot.CHEST,
              new Item.Properties().tab(TAB)));

  public static final RegistryObject<Item> STEEL_HELMET =
      ITEMS.register("steel_helmet",
          () -> new ArmorItem(RailcraftArmorMaterial.STEEL, EquipmentSlot.HEAD,
              new Item.Properties().tab(TAB)));

  public static final RegistryObject<Item> STEEL_LEGGINGS =
      ITEMS.register("steel_leggings",
          () -> new ArmorItem(RailcraftArmorMaterial.STEEL, EquipmentSlot.LEGS,
              new Item.Properties().tab(TAB)));

  public static final RegistryObject<Item> IRON_TUNNEL_BORE_HEAD =
      ITEMS.register("iron_tunnel_bore_head",
          () -> new IronTunnelBoreHeadItem(new Item.Properties()
              .tab(TAB)
              .durability(1500)));

  public static final RegistryObject<Item> BRONZE_TUNNEL_BORE_HEAD =
      ITEMS.register("bronze_tunnel_bore_head",
          () -> new BronzeTunnelBoreHeadItem(new Item.Properties()
              .tab(TAB)
              .durability(1200)));

  public static final RegistryObject<Item> STEEL_TUNNEL_BORE_HEAD =
      ITEMS.register("steel_tunnel_bore_head",
          () -> new SteelTunnelBoreHeadItem(
              new Item.Properties()
                  .tab(TAB)
                  .durability(3000)));

  public static final RegistryObject<Item> DIAMOND_TUNNEL_BORE_HEAD =
      ITEMS.register("diamond_tunnel_bore_head",
          () -> new DiamondTunnelBoreHeadItem(new Item.Properties()
              .tab(TAB)
              .durability(6000)));

  public static final RegistryObject<Item> TANK_MINECART =
      ITEMS.register("tank_minecart",
          () -> new CartItem(TankMinecart::new,
              new Item.Properties().tab(TAB)));

  public static final RegistryObject<Item> FLUID_LOADER =
      ITEMS.register("fluid_loader",
          () -> new BlockItem(RailcraftBlocks.FLUID_LOADER.get(),
              new Item.Properties().tab(TAB)));

  public static final RegistryObject<Item> FLUID_UNLOADER =
      ITEMS.register("fluid_unloader",
          () -> new BlockItem(RailcraftBlocks.FLUID_UNLOADER.get(),
              new Item.Properties().tab(TAB)));

  public static final RegistryObject<Item> ADVANCED_ITEM_LOADER =
      ITEMS.register("advanced_item_loader",
          () -> new BlockItem(RailcraftBlocks.ADVANCED_ITEM_LOADER.get(),
              new Item.Properties().tab(TAB)));

  public static final RegistryObject<Item> ADVANCED_ITEM_UNLOADER =
      ITEMS.register("advanced_item_unloader",
          () -> new BlockItem(RailcraftBlocks.ADVANCED_ITEM_UNLOADER.get(),
              new Item.Properties().tab(TAB)));

  public static final RegistryObject<Item> ITEM_LOADER =
      ITEMS.register("item_loader",
          () -> new BlockItem(RailcraftBlocks.ITEM_LOADER.get(), new Item.Properties().tab(TAB)));

  public static final RegistryObject<Item> ITEM_UNLOADER =
      ITEMS.register("item_unloader",
          () -> new BlockItem(RailcraftBlocks.ITEM_UNLOADER.get(), new Item.Properties().tab(TAB)));

  public static final RegistryObject<Item> IRON_SPIKE_MAUL =
      ITEMS.register("iron_spike_maul",
          () -> new SpikeMaulItem(11.0F, -3.5F, Tiers.IRON,
              new Item.Properties().tab(TAB)));

  public static final RegistryObject<Item> STEEL_SPIKE_MAUL =
      ITEMS.register("steel_spike_maul",
          () -> new SpikeMaulItem(11.0F, -3.4F, RailcraftItemTier.STEEL,
              new Item.Properties().tab(TAB)));

  public static final RegistryObject<Item> DIAMOND_SPIKE_MAUL =
      ITEMS.register("diamond_spike_maul",
          () -> new SpikeMaulItem(11.0F, -3.3F, Tiers.DIAMOND,
              new Item.Properties().tab(TAB)));

  public static final RegistryObject<Item> SWITCH_TRACK_LEVER =
      ITEMS.register("switch_track_lever",
          () -> new BlockItem(RailcraftBlocks.SWITCH_TRACK_LEVER.get(),
              new Item.Properties().tab(TAB)));

  public static final RegistryObject<Item> SWITCH_TRACK_MOTOR =
      ITEMS.register("switch_track_motor",
          () -> new BlockItem(RailcraftBlocks.SWITCH_TRACK_MOTOR.get(),
              new Item.Properties().tab(TAB)));

  public static final RegistryObject<Item> SIGNAL_TUNER =
      ITEMS.register("signal_tuner",
          () -> new SignalTunerItem(
              new Item.Properties()
                  .stacksTo(1)
                  .tab(TAB)));

  public static final RegistryObject<Item> SIGNAL_BLOCK_SURVEYOR =
      ITEMS.register("signal_block_surveyor",
          () -> new SignalBlockSurveyorItem(new Item.Properties()
              .stacksTo(1)
              .tab(TAB)));

  public static final RegistryObject<Item> ANALOG_SIGNAL_CONTROLLER_BOX =
      ITEMS.register("analog_signal_controller_box",
          () -> new BlockItem(
              RailcraftBlocks.ANALOG_SIGNAL_CONTROLLER_BOX.get(),
              new Item.Properties().tab(TAB)));

  public static final RegistryObject<Item> SIGNAL_SEQUENCER_BOX =
      ITEMS.register("signal_sequencer_box",
          () -> new BlockItem(
              RailcraftBlocks.SIGNAL_SEQUENCER_BOX.get(),
              new Item.Properties().tab(TAB)));

  public static final RegistryObject<Item> SIGNAL_CAPACITOR_BOX =
      ITEMS.register("signal_capacitor_box",
          () -> new BlockItem(
              RailcraftBlocks.SIGNAL_CAPACITOR_BOX.get(),
              new Item.Properties().tab(TAB)));

  public static final RegistryObject<Item> SIGNAL_INTERLOCK_BOX =
      ITEMS.register("signal_interlock_box",
          () -> new BlockItem(
              RailcraftBlocks.SIGNAL_INTERLOCK_BOX.get(),
              new Item.Properties().tab(TAB)));

  public static final RegistryObject<Item> BLOCK_SIGNAL_RELAY_BOX =
      ITEMS.register("block_signal_relay_box",
          () -> new BlockItem(
              RailcraftBlocks.BLOCK_SIGNAL_RELAY_BOX.get(),
              new Item.Properties().tab(TAB)));

  public static final RegistryObject<Item> SIGNAL_RECEIVER_BOX =
      ITEMS.register("signal_receiver_box",
          () -> new BlockItem(
              RailcraftBlocks.SIGNAL_RECEIVER_BOX.get(),
              new Item.Properties().tab(TAB)));

  public static final RegistryObject<Item> SIGNAL_CONTROLLER_BOX =
      ITEMS.register("signal_controller_box",
          () -> new BlockItem(
              RailcraftBlocks.SIGNAL_CONTROLLER_BOX.get(),
              new Item.Properties().tab(TAB)));

  public static final RegistryObject<Item> DUAL_BLOCK_SIGNAL =
      ITEMS.register("dual_block_signal",
          () -> new BlockItem(
              RailcraftBlocks.DUAL_BLOCK_SIGNAL.get(),
              new Item.Properties().tab(TAB)));

  public static final RegistryObject<Item> DUAL_DISTANT_SIGNAL =
      ITEMS.register("dual_distant_signal",
          () -> new BlockItem(
              RailcraftBlocks.DUAL_DISTANT_SIGNAL.get(),
              new Item.Properties().tab(TAB)));

  public static final RegistryObject<Item> DUAL_TOKEN_SIGNAL =
      ITEMS.register("dual_token_signal",
          () -> new BlockItem(
              RailcraftBlocks.DUAL_TOKEN_SIGNAL.get(),
              new Item.Properties().tab(TAB)));

  public static final RegistryObject<Item> BLOCK_SIGNAL =
      ITEMS.register("block_signal",
          () -> new BlockItem(
              RailcraftBlocks.BLOCK_SIGNAL.get(),
              new Item.Properties().tab(TAB)));

  public static final RegistryObject<Item> DISTANT_SIGNAL =
      ITEMS.register("distant_signal",
          () -> new BlockItem(
              RailcraftBlocks.DISTANT_SIGNAL.get(),
              new Item.Properties().tab(TAB)));

  public static final RegistryObject<Item> TOKEN_SIGNAL =
      ITEMS.register("token_signal",
          () -> new BlockItem(
              RailcraftBlocks.TOKEN_SIGNAL.get(),
              new Item.Properties().tab(TAB)));

  public static final RegistryObject<Item> TRACK_REMOVER =
      ITEMS.register("track_remover",
          () -> new CartItem(TrackRemover::new,
              new Item.Properties().tab(TAB)));

  public static final RegistryObject<Item> TRACK_LAYER =
      ITEMS.register("track_layer",
          () -> new CartItem(TrackLayer::new,
              new Item.Properties().tab(TAB)));

  public static final RegistryObject<Item> TUNNEL_BORE =
      ITEMS.register("tunnel_bore",
          () -> new TunnelBoreItem(new Item.Properties()
              .stacksTo(1)
              .tab(TAB)));

  public static final RegistryObject<Item> CREATIVE_LOCOMOTIVE =
      ITEMS.register("creative_locomotive",
          () -> new LocomotiveItem(CreativeLocomotive::new,
              DyeColor.BLACK, DyeColor.MAGENTA,
              new Item.Properties()
                  .stacksTo(1)
                  .tab(TAB)));

  public static final RegistryObject<Item> ELECTRIC_LOCOMOTIVE =
      ITEMS.register("electric_locomotive",
          () -> new LocomotiveItem(ElectricLocomotive::new,
              DyeColor.YELLOW, DyeColor.BLACK,
              new Item.Properties()
                  .stacksTo(1)
                  .tab(TAB)));

  public static final RegistryObject<Item> STEAM_LOCOMOTIVE =
      ITEMS.register("steam_locomotive",
          () -> new LocomotiveItem(SteamLocomotive::new,
              DyeColor.LIGHT_GRAY, DyeColor.GRAY,
              new Item.Properties()
                  .stacksTo(1)
                  .tab(TAB)));

  public static final RegistryObject<Item> WHISTLE_TUNER =
      ITEMS.register("whistle_tuner",
          () -> new Item(new Item.Properties().durability(250).tab(TAB)));

  public static final RegistryObject<Item> TICKET =
      ITEMS.register("ticket",
          () -> new Item(new Item.Properties().tab(TAB)));

  public static final RegistryObject<Item> OVERALLS =
      ITEMS.register("overalls",
          () -> new ArmorItem(RailcraftArmorMaterial.OVERALLS, EquipmentSlot.LEGS,
              new Item.Properties().tab(TAB)));

  public static final RegistryObject<Item> CRACKED_FIRESTONE =
      ITEMS.register("cracked_firestone",
          () -> new CrackedFirestoneItem(new Item.Properties()
              .stacksTo(1)
              .durability(RefinedFirestoneItem.CHARGES)
              .tab(TAB)));

  public static final RegistryObject<Item> RAW_FIRESTONE =
      ITEMS.register("raw_firestone", () -> new FirestoneItem(
          new Item.Properties().tab(TAB)));

  public static final RegistryObject<Item> CUT_FIRESTONE =
      ITEMS.register("cut_firestone", () -> new FirestoneItem(
          new Item.Properties().tab(TAB)));

  public static final RegistryObject<Item> REFINED_FIRESTONE =
      ITEMS.register("refined_firestone",
          () -> new RefinedFirestoneItem(new Item.Properties()
              .stacksTo(1)
              .durability(RefinedFirestoneItem.CHARGES)
              .tab(TAB)));

  public static final RegistryObject<BlockItem> FORCE_TRACK_EMITTER =
      ITEMS.register("force_track_emitter",
          () -> new BlockItem(
              RailcraftBlocks.FORCE_TRACK_EMITTER.get(),
              new Item.Properties().tab(TAB)));

  public static final RegistryObject<Item> ABANDONED_TRACK =
      ITEMS.register("abandoned_track",
          () -> new BlockItem(RailcraftBlocks.ABANDONED_TRACK.get(),
              new Item.Properties().tab(CreativeModeTab.TAB_TRANSPORTATION)));

  public static final RegistryObject<Item> ABANDONED_LOCKING_TRACK =
      ITEMS.register("abandoned_locking_track",
          () -> new BlockItem(RailcraftBlocks.ABANDONED_LOCKING_TRACK.get(),
              new Item.Properties().tab(OUTFITTED_TRACKS_TAB)));

  public static final RegistryObject<Item> ABANDONED_BUFFER_STOP_TRACK =
      ITEMS.register("abandoned_buffer_stop_track",
          () -> new BlockItem(RailcraftBlocks.ABANDONED_BUFFER_STOP_TRACK.get(),
              new Item.Properties().tab(OUTFITTED_TRACKS_TAB)));

  public static final RegistryObject<Item> ABANDONED_ACTIVATOR_TRACK =
      ITEMS.register("abandoned_activator_track",
          () -> new BlockItem(RailcraftBlocks.ABANDONED_ACTIVATOR_TRACK.get(),
              new Item.Properties().tab(OUTFITTED_TRACKS_TAB)));

  public static final RegistryObject<Item> ABANDONED_BOOSTER_TRACK =
      ITEMS.register("abandoned_booster_track",
          () -> new BlockItem(RailcraftBlocks.ABANDONED_BOOSTER_TRACK.get(),
              new Item.Properties().tab(OUTFITTED_TRACKS_TAB)));

  public static final RegistryObject<Item> ABANDONED_CONTROL_TRACK =
      ITEMS.register("abandoned_control_track",
          () -> new BlockItem(RailcraftBlocks.ABANDONED_CONTROL_TRACK.get(),
              new Item.Properties().tab(OUTFITTED_TRACKS_TAB)));

  public static final RegistryObject<Item> ABANDONED_GATED_TRACK =
      ITEMS.register("abandoned_gated_track",
          () -> new BlockItem(RailcraftBlocks.ABANDONED_GATED_TRACK.get(),
              new Item.Properties().tab(OUTFITTED_TRACKS_TAB)));

  public static final RegistryObject<Item> ABANDONED_DETECTOR_TRACK =
      ITEMS.register("abandoned_detector_track",
          () -> new BlockItem(RailcraftBlocks.ABANDONED_DETECTOR_TRACK.get(),
              new Item.Properties().tab(OUTFITTED_TRACKS_TAB)));

  public static final RegistryObject<Item> ABANDONED_COUPLER_TRACK =
      ITEMS.register("abandoned_coupler_track",
          () -> new BlockItem(RailcraftBlocks.ABANDONED_COUPLER_TRACK.get(),
              new Item.Properties().tab(OUTFITTED_TRACKS_TAB)));

  public static final RegistryObject<Item> ABANDONED_EMBARKING_TRACK =
      ITEMS.register("abandoned_embarking_track",
          () -> new BlockItem(RailcraftBlocks.ABANDONED_EMBARKING_TRACK.get(),
              new Item.Properties().tab(OUTFITTED_TRACKS_TAB)));

  public static final RegistryObject<Item> ABANDONED_DISEMBARKING_TRACK =
      ITEMS.register("abandoned_disembarking_track",
          () -> new BlockItem(RailcraftBlocks.ABANDONED_DISEMBARKING_TRACK.get(),
              new Item.Properties().tab(OUTFITTED_TRACKS_TAB)));

  public static final RegistryObject<Item> ABANDONED_WYE_TRACK =
      ITEMS.register("abandoned_wye_track",
          () -> new BlockItem(RailcraftBlocks.ABANDONED_WYE_TRACK.get(),
              new Item.Properties().tab(OUTFITTED_TRACKS_TAB)));

  public static final RegistryObject<Item> ABANDONED_TURNOUT_TRACK =
      ITEMS.register("abandoned_turnout_track",
          () -> new BlockItem(RailcraftBlocks.ABANDONED_TURNOUT_TRACK.get(),
              new Item.Properties().tab(OUTFITTED_TRACKS_TAB)));

  public static final RegistryObject<Item> ABANDONED_JUNCTION_TRACK =
      ITEMS.register("abandoned_junction_track",
          () -> new BlockItem(RailcraftBlocks.ABANDONED_JUNCTION_TRACK.get(),
              new Item.Properties().tab(OUTFITTED_TRACKS_TAB)));

  public static final RegistryObject<Item> ELECTRIC_TRACK =
      ITEMS.register("electric_track",
          () -> new BlockItem(RailcraftBlocks.ELECTRIC_TRACK.get(),
              new Item.Properties().tab(CreativeModeTab.TAB_TRANSPORTATION)));

  public static final RegistryObject<Item> ELECTRIC_LOCKING_TRACK =
      ITEMS.register("electric_locking_track",
          () -> new BlockItem(RailcraftBlocks.ELECTRIC_LOCKING_TRACK.get(),
              new Item.Properties().tab(OUTFITTED_TRACKS_TAB)));

  public static final RegistryObject<Item> ELECTRIC_BUFFER_STOP_TRACK =
      ITEMS.register("electric_buffer_stop_track",
          () -> new BlockItem(RailcraftBlocks.ELECTRIC_BUFFER_STOP_TRACK.get(),
              new Item.Properties().tab(OUTFITTED_TRACKS_TAB)));

  public static final RegistryObject<Item> ELECTRIC_ACTIVATOR_TRACK =
      ITEMS.register("electric_activator_track",
          () -> new BlockItem(RailcraftBlocks.ELECTRIC_ACTIVATOR_TRACK.get(),
              new Item.Properties().tab(OUTFITTED_TRACKS_TAB)));

  public static final RegistryObject<Item> ELECTRIC_BOOSTER_TRACK =
      ITEMS.register("electric_booster_track",
          () -> new BlockItem(RailcraftBlocks.ELECTRIC_BOOSTER_TRACK.get(),
              new Item.Properties().tab(OUTFITTED_TRACKS_TAB)));

  public static final RegistryObject<Item> ELECTRIC_CONTROL_TRACK =
      ITEMS.register("electric_control_track",
          () -> new BlockItem(RailcraftBlocks.ELECTRIC_CONTROL_TRACK.get(),
              new Item.Properties().tab(OUTFITTED_TRACKS_TAB)));

  public static final RegistryObject<Item> ELECTRIC_GATED_TRACK =
      ITEMS.register("electric_gated_track",
          () -> new BlockItem(RailcraftBlocks.ELECTRIC_GATED_TRACK.get(),
              new Item.Properties().tab(OUTFITTED_TRACKS_TAB)));

  public static final RegistryObject<Item> ELECTRIC_DETECTOR_TRACK =
      ITEMS.register("electric_detector_track",
          () -> new BlockItem(RailcraftBlocks.ELECTRIC_DETECTOR_TRACK.get(),
              new Item.Properties().tab(OUTFITTED_TRACKS_TAB)));

  public static final RegistryObject<Item> ELECTRIC_COUPLER_TRACK =
      ITEMS.register("electric_coupler_track",
          () -> new BlockItem(RailcraftBlocks.ELECTRIC_COUPLER_TRACK.get(),
              new Item.Properties().tab(OUTFITTED_TRACKS_TAB)));

  public static final RegistryObject<Item> ELECTRIC_EMBARKING_TRACK =
      ITEMS.register("electric_embarking_track",
          () -> new BlockItem(RailcraftBlocks.ELECTRIC_EMBARKING_TRACK.get(),
              new Item.Properties().tab(OUTFITTED_TRACKS_TAB)));

  public static final RegistryObject<Item> ELECTRIC_DISEMBARKING_TRACK =
      ITEMS.register("electric_disembarking_track",
          () -> new BlockItem(RailcraftBlocks.ELECTRIC_DISEMBARKING_TRACK.get(),
              new Item.Properties().tab(OUTFITTED_TRACKS_TAB)));

  public static final RegistryObject<Item> ELECTRIC_WYE_TRACK =
      ITEMS.register("electric_wye_track",
          () -> new BlockItem(RailcraftBlocks.ELECTRIC_WYE_TRACK.get(),
              new Item.Properties().tab(OUTFITTED_TRACKS_TAB)));

  public static final RegistryObject<Item> ELECTRIC_TURNOUT_TRACK =
      ITEMS.register("electric_turnout_track",
          () -> new BlockItem(RailcraftBlocks.ELECTRIC_TURNOUT_TRACK.get(),
              new Item.Properties().tab(OUTFITTED_TRACKS_TAB)));

  public static final RegistryObject<Item> ELECTRIC_JUNCTION_TRACK =
      ITEMS.register("electric_junction_track",
          () -> new BlockItem(RailcraftBlocks.ELECTRIC_JUNCTION_TRACK.get(),
              new Item.Properties().tab(OUTFITTED_TRACKS_TAB)));

  public static final RegistryObject<Item> HIGH_SPEED_TRACK =
      ITEMS.register("high_speed_track",
          () -> new BlockItem(RailcraftBlocks.HIGH_SPEED_TRACK.get(),
              new Item.Properties().tab(CreativeModeTab.TAB_TRANSPORTATION)));

  public static final RegistryObject<Item> HIGH_SPEED_TRANSITION_TRACK =
      ITEMS.register("high_speed_transition_track",
          () -> new BlockItem(RailcraftBlocks.HIGH_SPEED_TRANSITION_TRACK.get(),
              new Item.Properties().tab(OUTFITTED_TRACKS_TAB)));

  public static final RegistryObject<Item> HIGH_SPEED_LOCKING_TRACK =
      ITEMS.register("high_speed_locking_track",
          () -> new BlockItem(RailcraftBlocks.HIGH_SPEED_LOCKING_TRACK.get(),
              new Item.Properties().tab(OUTFITTED_TRACKS_TAB)));

  public static final RegistryObject<Item> HIGH_SPEED_ACTIVATOR_TRACK =
      ITEMS.register("high_speed_activator_track",
          () -> new BlockItem(RailcraftBlocks.HIGH_SPEED_ACTIVATOR_TRACK.get(),
              new Item.Properties().tab(OUTFITTED_TRACKS_TAB)));

  public static final RegistryObject<Item> HIGH_SPEED_BOOSTER_TRACK =
      ITEMS.register("high_speed_booster_track",
          () -> new BlockItem(RailcraftBlocks.HIGH_SPEED_BOOSTER_TRACK.get(),
              new Item.Properties().tab(OUTFITTED_TRACKS_TAB)));

  public static final RegistryObject<Item> HIGH_SPEED_DETECTOR_TRACK =
      ITEMS.register("high_speed_detector_track",
          () -> new BlockItem(RailcraftBlocks.HIGH_SPEED_DETECTOR_TRACK.get(),
              new Item.Properties().tab(OUTFITTED_TRACKS_TAB)));

  public static final RegistryObject<Item> HIGH_SPEED_WYE_TRACK =
      ITEMS.register("high_speed_wye_track",
          () -> new BlockItem(RailcraftBlocks.HIGH_SPEED_WYE_TRACK.get(),
              new Item.Properties().tab(OUTFITTED_TRACKS_TAB)));

  public static final RegistryObject<Item> HIGH_SPEED_TURNOUT_TRACK =
      ITEMS.register("high_speed_turnout_track",
          () -> new BlockItem(RailcraftBlocks.HIGH_SPEED_TURNOUT_TRACK.get(),
              new Item.Properties().tab(OUTFITTED_TRACKS_TAB)));

  public static final RegistryObject<Item> HIGH_SPEED_JUNCTION_TRACK =
      ITEMS.register("high_speed_junction_track",
          () -> new BlockItem(RailcraftBlocks.HIGH_SPEED_JUNCTION_TRACK.get(),
              new Item.Properties().tab(OUTFITTED_TRACKS_TAB)));

  public static final RegistryObject<Item> HIGH_SPEED_ELECTRIC_TRACK =
      ITEMS.register("high_speed_electric_track",
          () -> new BlockItem(RailcraftBlocks.HIGH_SPEED_ELECTRIC_TRACK.get(),
              new Item.Properties().tab(CreativeModeTab.TAB_TRANSPORTATION)));

  public static final RegistryObject<Item> HIGH_SPEED_ELECTRIC_TRANSITION_TRACK =
      ITEMS.register("high_speed_electric_transition_track",
          () -> new BlockItem(RailcraftBlocks.HIGH_SPEED_ELECTRIC_TRANSITION_TRACK.get(),
              new Item.Properties().tab(OUTFITTED_TRACKS_TAB)));

  public static final RegistryObject<Item> HIGH_SPEED_ELECTRIC_LOCKING_TRACK =
      ITEMS.register("high_speed_electric_locking_track",
          () -> new BlockItem(RailcraftBlocks.HIGH_SPEED_ELECTRIC_LOCKING_TRACK.get(),
              new Item.Properties().tab(OUTFITTED_TRACKS_TAB)));

  public static final RegistryObject<Item> HIGH_SPEED_ELECTRIC_ACTIVATOR_TRACK =
      ITEMS.register("high_speed_electric_activator_track",
          () -> new BlockItem(RailcraftBlocks.HIGH_SPEED_ELECTRIC_ACTIVATOR_TRACK.get(),
              new Item.Properties().tab(OUTFITTED_TRACKS_TAB)));

  public static final RegistryObject<Item> HIGH_SPEED_ELECTRIC_BOOSTER_TRACK =
      ITEMS.register("high_speed_electric_booster_track",
          () -> new BlockItem(RailcraftBlocks.HIGH_SPEED_ELECTRIC_BOOSTER_TRACK.get(),
              new Item.Properties().tab(OUTFITTED_TRACKS_TAB)));

  public static final RegistryObject<Item> HIGH_SPEED_ELECTRIC_DETECTOR_TRACK =
      ITEMS.register("high_speed_electric_detector_track",
          () -> new BlockItem(RailcraftBlocks.HIGH_SPEED_ELECTRIC_DETECTOR_TRACK.get(),
              new Item.Properties().tab(OUTFITTED_TRACKS_TAB)));

  public static final RegistryObject<Item> HIGH_SPEED_ELECTRIC_WYE_TRACK =
      ITEMS.register("high_speed_electric_wye_track",
          () -> new BlockItem(RailcraftBlocks.HIGH_SPEED_ELECTRIC_WYE_TRACK.get(),
              new Item.Properties().tab(OUTFITTED_TRACKS_TAB)));

  public static final RegistryObject<Item> HIGH_SPEED_ELECTRIC_TURNOUT_TRACK =
      ITEMS.register("high_speed_electric_turnout_track",
          () -> new BlockItem(RailcraftBlocks.HIGH_SPEED_ELECTRIC_TURNOUT_TRACK.get(),
              new Item.Properties().tab(OUTFITTED_TRACKS_TAB)));

  public static final RegistryObject<Item> HIGH_SPEED_ELECTRIC_JUNCTION_TRACK =
      ITEMS.register("high_speed_electric_junction_track",
          () -> new BlockItem(RailcraftBlocks.HIGH_SPEED_ELECTRIC_JUNCTION_TRACK.get(),
              new Item.Properties().tab(OUTFITTED_TRACKS_TAB)));

  public static final RegistryObject<Item> IRON_LOCKING_TRACK =
      ITEMS.register("iron_locking_track",
          () -> new BlockItem(RailcraftBlocks.IRON_LOCKING_TRACK.get(),
              new Item.Properties().tab(OUTFITTED_TRACKS_TAB)));

  public static final RegistryObject<Item> IRON_BUFFER_STOP_TRACK =
      ITEMS.register("iron_buffer_stop_track",
          () -> new BlockItem(RailcraftBlocks.IRON_BUFFER_STOP_TRACK.get(),
              new Item.Properties().tab(OUTFITTED_TRACKS_TAB)));

  public static final RegistryObject<Item> IRON_ACTIVATOR_TRACK =
      ITEMS.register("iron_activator_track",
          () -> new BlockItem(RailcraftBlocks.IRON_ACTIVATOR_TRACK.get(),
              new Item.Properties().tab(OUTFITTED_TRACKS_TAB)));

  public static final RegistryObject<Item> IRON_BOOSTER_TRACK =
      ITEMS.register("iron_booster_track",
          () -> new BlockItem(RailcraftBlocks.IRON_BOOSTER_TRACK.get(),
              new Item.Properties().tab(OUTFITTED_TRACKS_TAB)));

  public static final RegistryObject<Item> IRON_CONTROL_TRACK =
      ITEMS.register("iron_control_track",
          () -> new BlockItem(RailcraftBlocks.IRON_CONTROL_TRACK.get(),
              new Item.Properties().tab(OUTFITTED_TRACKS_TAB)));

  public static final RegistryObject<Item> IRON_GATED_TRACK =
      ITEMS.register("iron_gated_track",
          () -> new BlockItem(RailcraftBlocks.IRON_GATED_TRACK.get(),
              new Item.Properties().tab(OUTFITTED_TRACKS_TAB)));

  public static final RegistryObject<Item> IRON_DETECTOR_TRACK =
      ITEMS.register("iron_detector_track",
          () -> new BlockItem(RailcraftBlocks.IRON_DETECTOR_TRACK.get(),
              new Item.Properties().tab(OUTFITTED_TRACKS_TAB)));

  public static final RegistryObject<Item> IRON_COUPLER_TRACK =
      ITEMS.register("iron_coupler_track",
          () -> new BlockItem(RailcraftBlocks.IRON_COUPLER_TRACK.get(),
              new Item.Properties().tab(OUTFITTED_TRACKS_TAB)));

  public static final RegistryObject<Item> IRON_EMBARKING_TRACK =
      ITEMS.register("iron_embarking_track",
          () -> new BlockItem(RailcraftBlocks.IRON_EMBARKING_TRACK.get(),
              new Item.Properties().tab(OUTFITTED_TRACKS_TAB)));

  public static final RegistryObject<Item> IRON_DISEMBARKING_TRACK =
      ITEMS.register("iron_disembarking_track",
          () -> new BlockItem(RailcraftBlocks.IRON_DISEMBARKING_TRACK.get(),
              new Item.Properties().tab(OUTFITTED_TRACKS_TAB)));

  public static final RegistryObject<Item> IRON_WYE_TRACK =
      ITEMS.register("iron_wye_track",
          () -> new BlockItem(RailcraftBlocks.IRON_WYE_TRACK.get(),
              new Item.Properties().tab(OUTFITTED_TRACKS_TAB)));

  public static final RegistryObject<Item> IRON_TURNOUT_TRACK =
      ITEMS.register("iron_turnout_track",
          () -> new BlockItem(RailcraftBlocks.IRON_TURNOUT_TRACK.get(),
              new Item.Properties().tab(OUTFITTED_TRACKS_TAB)));

  public static final RegistryObject<Item> IRON_JUNCTION_TRACK =
      ITEMS.register("iron_junction_track",
          () -> new BlockItem(RailcraftBlocks.IRON_JUNCTION_TRACK.get(),
              new Item.Properties().tab(OUTFITTED_TRACKS_TAB)));

  public static final RegistryObject<Item> REINFORCED_TRACK =
      ITEMS.register("reinforced_track",
          () -> new BlockItem(RailcraftBlocks.REINFORCED_TRACK.get(),
              new Item.Properties().tab(CreativeModeTab.TAB_TRANSPORTATION)));

  public static final RegistryObject<Item> REINFORCED_LOCKING_TRACK =
      ITEMS.register("reinforced_locking_track",
          () -> new BlockItem(RailcraftBlocks.REINFORCED_LOCKING_TRACK.get(),
              new Item.Properties().tab(OUTFITTED_TRACKS_TAB)));

  public static final RegistryObject<Item> REINFORCED_BUFFER_STOP_TRACK =
      ITEMS.register("reinforced_buffer_stop_track",
          () -> new BlockItem(RailcraftBlocks.REINFORCED_BUFFER_STOP_TRACK.get(),
              new Item.Properties().tab(OUTFITTED_TRACKS_TAB)));

  public static final RegistryObject<Item> REINFORCED_ACTIVATOR_TRACK =
      ITEMS.register("reinforced_activator_track",
          () -> new BlockItem(RailcraftBlocks.REINFORCED_ACTIVATOR_TRACK.get(),
              new Item.Properties().tab(OUTFITTED_TRACKS_TAB)));

  public static final RegistryObject<Item> REINFORCED_BOOSTER_TRACK =
      ITEMS.register("reinforced_booster_track",
          () -> new BlockItem(RailcraftBlocks.REINFORCED_BOOSTER_TRACK.get(),
              new Item.Properties().tab(OUTFITTED_TRACKS_TAB)));

  public static final RegistryObject<Item> REINFORCED_CONTROL_TRACK =
      ITEMS.register("reinforced_control_track",
          () -> new BlockItem(RailcraftBlocks.REINFORCED_CONTROL_TRACK.get(),
              new Item.Properties().tab(OUTFITTED_TRACKS_TAB)));

  public static final RegistryObject<Item> REINFORCED_GATED_TRACK =
      ITEMS.register("reinforced_gated_track",
          () -> new BlockItem(RailcraftBlocks.REINFORCED_GATED_TRACK.get(),
              new Item.Properties().tab(OUTFITTED_TRACKS_TAB)));

  public static final RegistryObject<Item> REINFORCED_DETECTOR_TRACK =
      ITEMS.register("reinforced_detector_track",
          () -> new BlockItem(RailcraftBlocks.REINFORCED_DETECTOR_TRACK.get(),
              new Item.Properties().tab(OUTFITTED_TRACKS_TAB)));

  public static final RegistryObject<Item> REINFORCED_COUPLER_TRACK =
      ITEMS.register("reinforced_coupler_track",
          () -> new BlockItem(RailcraftBlocks.REINFORCED_COUPLER_TRACK.get(),
              new Item.Properties().tab(OUTFITTED_TRACKS_TAB)));

  public static final RegistryObject<Item> REINFORCED_EMBARKING_TRACK =
      ITEMS.register("reinforced_embarking_track",
          () -> new BlockItem(RailcraftBlocks.REINFORCED_EMBARKING_TRACK.get(),
              new Item.Properties().tab(OUTFITTED_TRACKS_TAB)));

  public static final RegistryObject<Item> REINFORCED_DISEMBARKING_TRACK =
      ITEMS.register("reinforced_disembarking_track",
          () -> new BlockItem(RailcraftBlocks.REINFORCED_DISEMBARKING_TRACK.get(),
              new Item.Properties().tab(OUTFITTED_TRACKS_TAB)));

  public static final RegistryObject<Item> REINFORCED_WYE_TRACK =
      ITEMS.register("reinforced_wye_track",
          () -> new BlockItem(RailcraftBlocks.REINFORCED_WYE_TRACK.get(),
              new Item.Properties().tab(OUTFITTED_TRACKS_TAB)));

  public static final RegistryObject<Item> REINFORCED_TURNOUT_TRACK =
      ITEMS.register("reinforced_turnout_track",
          () -> new BlockItem(RailcraftBlocks.REINFORCED_TURNOUT_TRACK.get(),
              new Item.Properties().tab(OUTFITTED_TRACKS_TAB)));

  public static final RegistryObject<Item> REINFORCED_JUNCTION_TRACK =
      ITEMS.register("reinforced_junction_track",
          () -> new BlockItem(RailcraftBlocks.REINFORCED_JUNCTION_TRACK.get(),
              new Item.Properties().tab(OUTFITTED_TRACKS_TAB)));

  public static final RegistryObject<Item> STRAP_IRON_TRACK =
      ITEMS.register("strap_iron_track",
          () -> new BlockItem(RailcraftBlocks.STRAP_IRON_TRACK.get(),
              new Item.Properties().tab(CreativeModeTab.TAB_TRANSPORTATION)));

  public static final RegistryObject<Item> STRAP_IRON_LOCKING_TRACK =
      ITEMS.register("strap_iron_locking_track",
          () -> new BlockItem(RailcraftBlocks.STRAP_IRON_LOCKING_TRACK.get(),
              new Item.Properties().tab(OUTFITTED_TRACKS_TAB)));

  public static final RegistryObject<Item> STRAP_IRON_BUFFER_STOP_TRACK =
      ITEMS.register("strap_iron_buffer_stop_track",
          () -> new BlockItem(RailcraftBlocks.STRAP_IRON_BUFFER_STOP_TRACK.get(),
              new Item.Properties().tab(OUTFITTED_TRACKS_TAB)));

  public static final RegistryObject<Item> STRAP_IRON_ACTIVATOR_TRACK =
      ITEMS.register("strap_iron_activator_track",
          () -> new BlockItem(RailcraftBlocks.STRAP_IRON_ACTIVATOR_TRACK.get(),
              new Item.Properties().tab(OUTFITTED_TRACKS_TAB)));

  public static final RegistryObject<Item> STRAP_IRON_BOOSTER_TRACK =
      ITEMS.register("strap_iron_booster_track",
          () -> new BlockItem(RailcraftBlocks.STRAP_IRON_BOOSTER_TRACK.get(),
              new Item.Properties().tab(OUTFITTED_TRACKS_TAB)));

  public static final RegistryObject<Item> STRAP_IRON_CONTROL_TRACK =
      ITEMS.register("strap_iron_control_track",
          () -> new BlockItem(RailcraftBlocks.STRAP_IRON_CONTROL_TRACK.get(),
              new Item.Properties().tab(OUTFITTED_TRACKS_TAB)));

  public static final RegistryObject<Item> STRAP_IRON_GATED_TRACK =
      ITEMS.register("strap_iron_gated_track",
          () -> new BlockItem(RailcraftBlocks.STRAP_IRON_GATED_TRACK.get(),
              new Item.Properties().tab(OUTFITTED_TRACKS_TAB)));

  public static final RegistryObject<Item> STRAP_IRON_DETECTOR_TRACK =
      ITEMS.register("strap_iron_detector_track",
          () -> new BlockItem(RailcraftBlocks.STRAP_IRON_DETECTOR_TRACK.get(),
              new Item.Properties().tab(OUTFITTED_TRACKS_TAB)));

  public static final RegistryObject<Item> STRAP_IRON_COUPLER_TRACK =
      ITEMS.register("strap_iron_coupler_track",
          () -> new BlockItem(RailcraftBlocks.STRAP_IRON_COUPLER_TRACK.get(),
              new Item.Properties().tab(OUTFITTED_TRACKS_TAB)));

  public static final RegistryObject<Item> STRAP_IRON_EMBARKING_TRACK =
      ITEMS.register("strap_iron_embarking_track",
          () -> new BlockItem(RailcraftBlocks.STRAP_IRON_EMBARKING_TRACK.get(),
              new Item.Properties().tab(OUTFITTED_TRACKS_TAB)));

  public static final RegistryObject<Item> STRAP_IRON_DISEMBARKING_TRACK =
      ITEMS.register("strap_iron_disembarking_track",
          () -> new BlockItem(RailcraftBlocks.STRAP_IRON_DISEMBARKING_TRACK.get(),
              new Item.Properties().tab(OUTFITTED_TRACKS_TAB)));

  public static final RegistryObject<Item> STRAP_IRON_WYE_TRACK =
      ITEMS.register("strap_iron_wye_track",
          () -> new BlockItem(RailcraftBlocks.STRAP_IRON_WYE_TRACK.get(),
              new Item.Properties().tab(OUTFITTED_TRACKS_TAB)));

  public static final RegistryObject<Item> STRAP_IRON_TURNOUT_TRACK =
      ITEMS.register("strap_iron_turnout_track",
          () -> new BlockItem(RailcraftBlocks.STRAP_IRON_TURNOUT_TRACK.get(),
              new Item.Properties().tab(OUTFITTED_TRACKS_TAB)));

  public static final RegistryObject<Item> STRAP_IRON_JUNCTION_TRACK =
      ITEMS.register("strap_iron_junction_track",
          () -> new BlockItem(RailcraftBlocks.STRAP_IRON_JUNCTION_TRACK.get(),
              new Item.Properties().tab(OUTFITTED_TRACKS_TAB)));

  public static final RegistryObject<Item> ELEVATOR_TRACK =
      ITEMS.register("elevator_track",
          () -> new BlockItem(RailcraftBlocks.ELEVATOR_TRACK.get(),
              new Item.Properties().tab(CreativeModeTab.TAB_TRANSPORTATION)));

  public static final RegistryObject<Item> IRON_CROWBAR =
      ITEMS.register("iron_crowbar",
          () -> new CrowbarItem(2.5F, -2.8F, Tiers.IRON,
              new Item.Properties().tab(TAB)));

  public static final RegistryObject<Item> STEEL_CROWBAR =
      ITEMS.register("steel_crowbar",
          () -> new CrowbarItem(2.5F, -2.7F, RailcraftItemTier.STEEL,
              new Item.Properties().tab(TAB)));

  public static final RegistryObject<Item> DIAMOND_CROWBAR =
      ITEMS.register("diamond_crowbar",
          () -> new CrowbarItem(2.5F, -2.4F, Tiers.DIAMOND,
              new Item.Properties().tab(TAB)));

  public static final RegistryObject<Item> SEASONS_CROWBAR =
      ITEMS.register("seasons_crowbar",
          () -> new SeasonsCrowbarItem(new Item.Properties().tab(TAB)));

  public static final RegistryObject<Item> TRANSITION_TRACK_KIT =
      ITEMS.register("transition_track_kit",
          () -> new TrackKitItem((TrackKitItem.Properties) new TrackKitItem.Properties()
              .setAllowedOnSlopes(true)
              .addOutfittedBlock(TrackTypes.HIGH_SPEED, RailcraftBlocks.HIGH_SPEED_TRANSITION_TRACK)
              .addOutfittedBlock(TrackTypes.HIGH_SPEED_ELECTRIC,
                  RailcraftBlocks.HIGH_SPEED_ELECTRIC_TRANSITION_TRACK)
              .tab(TAB)));

  public static final RegistryObject<Item> LOCKING_TRACK_KIT =
      ITEMS.register("locking_track_kit",
          () -> new TrackKitItem((TrackKitItem.Properties) new TrackKitItem.Properties()
              .addOutfittedBlock(TrackTypes.ABANDONED, RailcraftBlocks.ABANDONED_LOCKING_TRACK)
              .addOutfittedBlock(TrackTypes.ELECTRIC, RailcraftBlocks.ELECTRIC_LOCKING_TRACK)
              .addOutfittedBlock(TrackTypes.HIGH_SPEED, RailcraftBlocks.HIGH_SPEED_LOCKING_TRACK)
              .addOutfittedBlock(TrackTypes.HIGH_SPEED_ELECTRIC,
                  RailcraftBlocks.HIGH_SPEED_ELECTRIC_LOCKING_TRACK)
              .addOutfittedBlock(TrackTypes.IRON, RailcraftBlocks.IRON_LOCKING_TRACK)
              .addOutfittedBlock(TrackTypes.REINFORCED, RailcraftBlocks.REINFORCED_LOCKING_TRACK)
              .addOutfittedBlock(TrackTypes.STRAP_IRON, RailcraftBlocks.STRAP_IRON_LOCKING_TRACK)
              .tab(TAB)));

  public static final RegistryObject<Item> BUFFER_STOP_TRACK_KIT =
      ITEMS.register("buffer_stop_track_kit",
          () -> new TrackKitItem((TrackKitItem.Properties) new TrackKitItem.Properties()
              .addOutfittedBlock(TrackTypes.ABANDONED, RailcraftBlocks.ABANDONED_BUFFER_STOP_TRACK)
              .addOutfittedBlock(TrackTypes.ELECTRIC, RailcraftBlocks.ELECTRIC_BUFFER_STOP_TRACK)
              .addOutfittedBlock(TrackTypes.IRON, RailcraftBlocks.IRON_BUFFER_STOP_TRACK)
              .addOutfittedBlock(TrackTypes.REINFORCED,
                  RailcraftBlocks.REINFORCED_BUFFER_STOP_TRACK)
              .addOutfittedBlock(TrackTypes.STRAP_IRON,
                  RailcraftBlocks.STRAP_IRON_BUFFER_STOP_TRACK)
              .tab(TAB)));

  public static final RegistryObject<Item> ACTIVATOR_TRACK_KIT =
      ITEMS.register("activator_track_kit",
          () -> new TrackKitItem((TrackKitItem.Properties) new TrackKitItem.Properties()
              .setAllowedOnSlopes(true)
              .addOutfittedBlock(TrackTypes.ABANDONED, RailcraftBlocks.ABANDONED_ACTIVATOR_TRACK)
              .addOutfittedBlock(TrackTypes.ELECTRIC, RailcraftBlocks.ELECTRIC_ACTIVATOR_TRACK)
              .addOutfittedBlock(TrackTypes.HIGH_SPEED, RailcraftBlocks.HIGH_SPEED_ACTIVATOR_TRACK)
              .addOutfittedBlock(TrackTypes.HIGH_SPEED_ELECTRIC,
                  RailcraftBlocks.HIGH_SPEED_ELECTRIC_ACTIVATOR_TRACK)
              .addOutfittedBlock(TrackTypes.IRON, RailcraftBlocks.IRON_ACTIVATOR_TRACK)
              .addOutfittedBlock(TrackTypes.REINFORCED, RailcraftBlocks.REINFORCED_ACTIVATOR_TRACK)
              .addOutfittedBlock(TrackTypes.STRAP_IRON, RailcraftBlocks.STRAP_IRON_ACTIVATOR_TRACK)
              .tab(TAB)));

  public static final RegistryObject<Item> BOOSTER_TRACK_KIT =
      ITEMS.register("booster_track_kit",
          () -> new TrackKitItem((TrackKitItem.Properties) new TrackKitItem.Properties()
              .setAllowedOnSlopes(true)
              .addOutfittedBlock(TrackTypes.ABANDONED, RailcraftBlocks.ABANDONED_BOOSTER_TRACK)
              .addOutfittedBlock(TrackTypes.ELECTRIC, RailcraftBlocks.ELECTRIC_BOOSTER_TRACK)
              .addOutfittedBlock(TrackTypes.HIGH_SPEED, RailcraftBlocks.HIGH_SPEED_BOOSTER_TRACK)
              .addOutfittedBlock(TrackTypes.HIGH_SPEED_ELECTRIC,
                  RailcraftBlocks.HIGH_SPEED_ELECTRIC_BOOSTER_TRACK)
              .addOutfittedBlock(TrackTypes.IRON, RailcraftBlocks.IRON_BOOSTER_TRACK)
              .addOutfittedBlock(TrackTypes.REINFORCED, RailcraftBlocks.REINFORCED_BOOSTER_TRACK)
              .addOutfittedBlock(TrackTypes.STRAP_IRON, RailcraftBlocks.STRAP_IRON_BOOSTER_TRACK)
              .tab(TAB)));

  public static final RegistryObject<Item> CONTROL_TRACK_KIT =
      ITEMS.register("control_track_kit",
          () -> new TrackKitItem((TrackKitItem.Properties) new TrackKitItem.Properties()
              .setAllowedOnSlopes(true)
              .addOutfittedBlock(TrackTypes.ABANDONED, RailcraftBlocks.ABANDONED_CONTROL_TRACK)
              .addOutfittedBlock(TrackTypes.ELECTRIC, RailcraftBlocks.ELECTRIC_CONTROL_TRACK)
              .addOutfittedBlock(TrackTypes.IRON, RailcraftBlocks.IRON_CONTROL_TRACK)
              .addOutfittedBlock(TrackTypes.REINFORCED, RailcraftBlocks.REINFORCED_CONTROL_TRACK)
              .addOutfittedBlock(TrackTypes.STRAP_IRON, RailcraftBlocks.STRAP_IRON_CONTROL_TRACK)
              .tab(TAB)));

  public static final RegistryObject<Item> GATED_TRACK_KIT =
      ITEMS.register("gated_track_kit",
          () -> new TrackKitItem((TrackKitItem.Properties) new TrackKitItem.Properties()
              .setAllowedOnSlopes(true)
              .addOutfittedBlock(TrackTypes.ABANDONED, RailcraftBlocks.ABANDONED_GATED_TRACK)
              .addOutfittedBlock(TrackTypes.ELECTRIC, RailcraftBlocks.ELECTRIC_GATED_TRACK)
              .addOutfittedBlock(TrackTypes.IRON, RailcraftBlocks.IRON_GATED_TRACK)
              .addOutfittedBlock(TrackTypes.REINFORCED, RailcraftBlocks.REINFORCED_GATED_TRACK)
              .addOutfittedBlock(TrackTypes.STRAP_IRON, RailcraftBlocks.STRAP_IRON_GATED_TRACK)
              .tab(TAB)));

  public static final RegistryObject<Item> DETECTOR_TRACK_KIT =
      ITEMS.register("detector_track_kit",
          () -> new TrackKitItem((TrackKitItem.Properties) new TrackKitItem.Properties()
              .setAllowedOnSlopes(true)
              .addOutfittedBlock(TrackTypes.ABANDONED, RailcraftBlocks.ABANDONED_DETECTOR_TRACK)
              .addOutfittedBlock(TrackTypes.ELECTRIC, RailcraftBlocks.ELECTRIC_DETECTOR_TRACK)
              .addOutfittedBlock(TrackTypes.HIGH_SPEED, RailcraftBlocks.HIGH_SPEED_DETECTOR_TRACK)
              .addOutfittedBlock(TrackTypes.HIGH_SPEED_ELECTRIC,
                  RailcraftBlocks.HIGH_SPEED_ELECTRIC_DETECTOR_TRACK)
              .addOutfittedBlock(TrackTypes.IRON, RailcraftBlocks.IRON_DETECTOR_TRACK)
              .addOutfittedBlock(TrackTypes.REINFORCED, RailcraftBlocks.REINFORCED_DETECTOR_TRACK)
              .addOutfittedBlock(TrackTypes.STRAP_IRON, RailcraftBlocks.STRAP_IRON_DETECTOR_TRACK)
              .tab(TAB)));

  public static final RegistryObject<Item> COUPLER_TRACK_KIT =
      ITEMS.register("coupler_track_kit",
          () -> new TrackKitItem((TrackKitItem.Properties) new TrackKitItem.Properties()
              .setAllowedOnSlopes(true)
              .addOutfittedBlock(TrackTypes.ABANDONED, RailcraftBlocks.ABANDONED_COUPLER_TRACK)
              .addOutfittedBlock(TrackTypes.ELECTRIC, RailcraftBlocks.ELECTRIC_COUPLER_TRACK)
              .addOutfittedBlock(TrackTypes.IRON, RailcraftBlocks.IRON_COUPLER_TRACK)
              .addOutfittedBlock(TrackTypes.REINFORCED, RailcraftBlocks.REINFORCED_COUPLER_TRACK)
              .addOutfittedBlock(TrackTypes.STRAP_IRON, RailcraftBlocks.STRAP_IRON_COUPLER_TRACK)
              .tab(TAB)));

  public static final RegistryObject<Item> EMBARKING_TRACK_KIT =
      ITEMS.register("embarking_track_kit",
          () -> new TrackKitItem((TrackKitItem.Properties) new TrackKitItem.Properties()
              .setAllowedOnSlopes(true)
              .addOutfittedBlock(TrackTypes.ABANDONED, RailcraftBlocks.ABANDONED_EMBARKING_TRACK)
              .addOutfittedBlock(TrackTypes.ELECTRIC, RailcraftBlocks.ELECTRIC_EMBARKING_TRACK)
              .addOutfittedBlock(TrackTypes.IRON, RailcraftBlocks.IRON_EMBARKING_TRACK)
              .addOutfittedBlock(TrackTypes.REINFORCED, RailcraftBlocks.REINFORCED_EMBARKING_TRACK)
              .addOutfittedBlock(TrackTypes.STRAP_IRON, RailcraftBlocks.STRAP_IRON_EMBARKING_TRACK)
              .tab(TAB)));

  public static final RegistryObject<Item> DISEMBARKING_TRACK_KIT =
      ITEMS.register("disembarking_track_kit",
          () -> new TrackKitItem((TrackKitItem.Properties) new TrackKitItem.Properties()
              .setAllowedOnSlopes(true)
              .addOutfittedBlock(TrackTypes.ABANDONED, RailcraftBlocks.ABANDONED_DISEMBARKING_TRACK)
              .addOutfittedBlock(TrackTypes.ELECTRIC, RailcraftBlocks.ELECTRIC_DISEMBARKING_TRACK)
              .addOutfittedBlock(TrackTypes.IRON, RailcraftBlocks.IRON_DISEMBARKING_TRACK)
              .addOutfittedBlock(TrackTypes.REINFORCED,
                  RailcraftBlocks.REINFORCED_DISEMBARKING_TRACK)
              .addOutfittedBlock(TrackTypes.STRAP_IRON,
                  RailcraftBlocks.STRAP_IRON_DISEMBARKING_TRACK)
              .tab(TAB)));

  public static final RegistryObject<Item> GOGGLES =
      ITEMS.register("goggles",
          () -> new GogglesItem(new Item.Properties().tab(TAB)));

  public static final RegistryObject<BlockItem> MANUAL_ROLLING_MACHINE =
      ITEMS.register("manual_rolling_machine",
          () -> new BlockItem(RailcraftBlocks.MANUAL_ROLLING_MACHINE.get(),
              new Item.Properties().tab(TAB)));

  public static final RegistryObject<BlockItem> COKE_OVEN_BRICKS =
      ITEMS.register("coke_oven_bricks",
          () -> new BlockItem(RailcraftBlocks.COKE_OVEN_BRICKS.get(),
              new Item.Properties().tab(TAB)));

  public static final RegistryObject<Item> BLACK_POST =
      ITEMS.register("black_post",
          () -> new BlockItem(RailcraftBlocks.BLACK_POST.get(),
              new Item.Properties().tab(TAB)));

  public static final RegistryObject<Item> RED_POST =
      ITEMS.register("red_post",
          () -> new BlockItem(RailcraftBlocks.RED_POST.get(),
              new Item.Properties().tab(TAB)));

  public static final RegistryObject<Item> GREEN_POST =
      ITEMS.register("green_post",
          () -> new BlockItem(RailcraftBlocks.GREEN_POST.get(),
              new Item.Properties().tab(TAB)));

  public static final RegistryObject<Item> BROWN_POST =
      ITEMS.register("brown_post",
          () -> new BlockItem(RailcraftBlocks.BROWN_POST.get(),
              new Item.Properties().tab(TAB)));

  public static final RegistryObject<Item> BLUE_POST =
      ITEMS.register("blue_post",
          () -> new BlockItem(RailcraftBlocks.BLUE_POST.get(),
              new Item.Properties().tab(TAB)));

  public static final RegistryObject<Item> PURPLE_POST =
      ITEMS.register("purple_post",
          () -> new BlockItem(RailcraftBlocks.PURPLE_POST.get(),
              new Item.Properties().tab(TAB)));

  public static final RegistryObject<Item> CYAN_POST =
      ITEMS.register("cyan_post",
          () -> new BlockItem(RailcraftBlocks.CYAN_POST.get(),
              new Item.Properties().tab(TAB)));

  public static final RegistryObject<Item> LIGHT_GRAY_POST =
      ITEMS.register("light_gray_post",
          () -> new BlockItem(RailcraftBlocks.LIGHT_GRAY_POST.get(),
              new Item.Properties().tab(TAB)));

  public static final RegistryObject<Item> GRAY_POST =
      ITEMS.register("gray_post",
          () -> new BlockItem(RailcraftBlocks.GRAY_POST.get(),
              new Item.Properties().tab(TAB)));

  public static final RegistryObject<Item> PINK_POST =
      ITEMS.register("pink_post",
          () -> new BlockItem(RailcraftBlocks.PINK_POST.get(),
              new Item.Properties().tab(TAB)));

  public static final RegistryObject<Item> LIME_POST =
      ITEMS.register("lime_post",
          () -> new BlockItem(RailcraftBlocks.LIME_POST.get(),
              new Item.Properties().tab(TAB)));

  public static final RegistryObject<Item> YELLOW_POST =
      ITEMS.register("yellow_post",
          () -> new BlockItem(RailcraftBlocks.YELLOW_POST.get(),
              new Item.Properties().tab(TAB)));

  public static final RegistryObject<Item> LIGHT_BLUE_POST =
      ITEMS.register("light_blue_post",
          () -> new BlockItem(RailcraftBlocks.LIGHT_BLUE_POST.get(),
              new Item.Properties().tab(TAB)));

  public static final RegistryObject<Item> MAGENTA_POST =
      ITEMS.register("magenta_post",
          () -> new BlockItem(RailcraftBlocks.MAGENTA_POST.get(),
              new Item.Properties().tab(TAB)));

  public static final RegistryObject<Item> ORANGE_POST =
      ITEMS.register("orange_post",
          () -> new BlockItem(RailcraftBlocks.ORANGE_POST.get(),
              new Item.Properties().tab(TAB)));

  public static final RegistryObject<Item> WHITE_POST =
      ITEMS.register("white_post",
          () -> new BlockItem(RailcraftBlocks.WHITE_POST.get(),
              new Item.Properties().tab(TAB)));

  /* ===== CRAFTING MATERIALS ===== */
  public static final RegistryObject<Item> COAL_COKE =
      ITEMS.register("coal_coke",
          () -> new Item(new Item.Properties().tab(TAB)) {
            @Override
            public int getBurnTime(ItemStack itemStack, @Nullable RecipeType<?> recipeType) {
              return 3200;
            }
          });

  public static final RegistryObject<Item> STEEL_INGOT =
      ITEMS.register("steel_ingot",
          () -> new Item(new Item.Properties().tab(TAB)));

  public static final RegistryObject<Item> COPPER_INGOT =
      ITEMS.register("copper_ingot",
          () -> new Item(new Item.Properties().tab(TAB)));

  public static final RegistryObject<Item> TIN_INGOT =
      ITEMS.register("tin_ingot",
          () -> new Item(new Item.Properties().tab(TAB)));

  public static final RegistryObject<Item> ZINC_INGOT =
      ITEMS.register("zinc_ingot",
          () -> new Item(new Item.Properties().tab(TAB)));

  public static final RegistryObject<Item> BRASS_INGOT =
      ITEMS.register("brass_ingot",
          () -> new Item(new Item.Properties().tab(TAB)));

  public static final RegistryObject<Item> BRONZE_INGOT =
      ITEMS.register("bronze_ingot",
          () -> new Item(new Item.Properties().tab(TAB)));

  // NUGGET
  public static final RegistryObject<Item> STEEL_NUGGET =
      ITEMS.register("steel_nugget",
          () -> new Item(new Item.Properties().tab(TAB)));

  public static final RegistryObject<Item> COPPER_NUGGET =
      ITEMS.register("copper_nugget",
          () -> new Item(new Item.Properties().tab(TAB)));

  public static final RegistryObject<Item> TIN_NUGGET =
      ITEMS.register("tin_nugget",
          () -> new Item(new Item.Properties().tab(TAB)));

  public static final RegistryObject<Item> ZINC_NUGGET =
      ITEMS.register("zinc_nugget",
          () -> new Item(new Item.Properties().tab(TAB)));

  public static final RegistryObject<Item> BRASS_NUGGET =
      ITEMS.register("brass_nugget",
          () -> new Item(new Item.Properties().tab(TAB)));

  public static final RegistryObject<Item> BRONZE_NUGGET =
      ITEMS.register("bronze_nugget",
          () -> new Item(new Item.Properties().tab(TAB)));

  // circuits
  public static final RegistryObject<Item> CONTROLLER_CIRCUIT =
      ITEMS.register("controller_circuit",
          () -> new Item(new Item.Properties().tab(TAB)));

  public static final RegistryObject<Item> RECEIVER_CIRCUIT =
      ITEMS.register("receiver_circuit",
          () -> new Item(new Item.Properties().tab(TAB)));

  public static final RegistryObject<Item> SIGNAL_CIRCUIT =
      ITEMS.register("signal_circuit",
          () -> new Item(new Item.Properties().tab(TAB)));

  // rails
  public static final RegistryObject<Item> WOODEN_RAIL =
      ITEMS.register("wooden_rail",
          () -> new Item(new Item.Properties().tab(TAB)));

  public static final RegistryObject<Item> STANDARD_RAIL =
      ITEMS.register("standard_rail",
          () -> new Item(new Item.Properties().tab(TAB)));

  public static final RegistryObject<Item> ADVANCED_RAIL =
      ITEMS.register("advanced_rail",
          () -> new Item(new Item.Properties().tab(TAB)));

  public static final RegistryObject<Item> REINFORCED_RAIL =
      ITEMS.register("reinforced_rail",
          () -> new Item(new Item.Properties().tab(TAB)));

  public static final RegistryObject<Item> HIGH_SPEED_RAIL =
      ITEMS.register("high_speed_rail",
          () -> new Item(new Item.Properties().tab(TAB)));

  public static final RegistryObject<Item> ELECTRIC_RAIL =
      ITEMS.register("electric_rail",
          () -> new Item(new Item.Properties().tab(TAB)));

  // misc crafting units
  public static final RegistryObject<Item> WOODEN_TIE =
      ITEMS.register("wooden_tie",
          () -> new Item(new Item.Properties().tab(TAB)));

  public static final RegistryObject<Item> STONE_TIE =
      ITEMS.register("stone_tie",
          () -> new Item(new Item.Properties().tab(TAB)));

  public static final RegistryObject<Item> REBAR =
      ITEMS.register("rebar",
          () -> new Item(new Item.Properties().tab(TAB)));

  public static final RegistryObject<Item> WOODEN_RAILBED =
      ITEMS.register("wooden_railbed",
          () -> new Item(new Item.Properties().tab(TAB)));

  public static final RegistryObject<Item> STONE_RAILBED =
      ITEMS.register("stone_railbed",
          () -> new Item(new Item.Properties().tab(TAB)));

  /* ===== BUCKETS ===== */

  public static final RegistryObject<Item> CREOSOTE_BUCKET =
      ITEMS.register("creosote_bucket",
          () -> new Item(new Item.Properties().tab(TAB)));
}
