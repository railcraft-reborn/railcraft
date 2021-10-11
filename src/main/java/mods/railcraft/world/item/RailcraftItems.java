package mods.railcraft.world.item;

import mods.railcraft.Railcraft;
import mods.railcraft.world.entity.cart.CreativeLocomotiveEntity;
import mods.railcraft.world.entity.cart.ElectricLocomotiveEntity;
import mods.railcraft.world.entity.cart.SteamLocomotiveEntity;
import mods.railcraft.world.entity.cart.TrackLayerMinecartEntity;
import mods.railcraft.world.entity.cart.TrackRemoverMinecartEntity;
import mods.railcraft.world.level.block.RailcraftBlocks;
import mods.railcraft.world.level.block.track.TrackTypes;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.BlockItem;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTier;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class RailcraftItems {

  public static final DeferredRegister<Item> ITEMS =
      DeferredRegister.create(ForgeRegistries.ITEMS, Railcraft.ID);

  /**
   * Railcraft's creative tab.
   */
  public static final ItemGroup TAB = new ItemGroup(Railcraft.ID) {

    @Override
    public ItemStack makeIcon() {
      return new ItemStack(IRON_CROWBAR.get());
    }
  };

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
          () -> new CartItem(TrackRemoverMinecartEntity::new,
              new Item.Properties().tab(TAB)));

  public static final RegistryObject<Item> TRACK_LAYER =
      ITEMS.register("track_layer",
          () -> new CartItem(TrackLayerMinecartEntity::new,
              new Item.Properties().tab(TAB)));

  public static final RegistryObject<Item> TUNNEL_BORE =
      ITEMS.register("tunnel_bore",
          () -> new TunnelBoreItem(new Item.Properties()
              .stacksTo(1)
              .tab(TAB)));

  public static final RegistryObject<Item> CREATIVE_LOCOMOTIVE =
      ITEMS.register("creative_locomotive",
          () -> new LocomotiveItem(CreativeLocomotiveEntity::new,
              DyeColor.BLACK, DyeColor.MAGENTA,
              new Item.Properties()
                  .stacksTo(1)
                  .tab(TAB)));

  public static final RegistryObject<Item> ELECTRIC_LOCOMOTIVE =
      ITEMS.register("electric_locomotive",
          () -> new LocomotiveItem(ElectricLocomotiveEntity::new,
              DyeColor.YELLOW, DyeColor.BLACK,
              new Item.Properties()
                  .stacksTo(1)
                  .tab(TAB)));

  public static final RegistryObject<Item> STEAM_LOCOMOTIVE =
      ITEMS.register("steam_locomotive",
          () -> new LocomotiveItem(SteamLocomotiveEntity::new,
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
          () -> new ArmorItem(RailcraftArmorMaterial.OVERALLS, EquipmentSlotType.LEGS,
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

  public static final RegistryObject<Item> ABANDONED_FLEX_TRACK =
      ITEMS.register("abandoned_flex_track",
          () -> new BlockItem(RailcraftBlocks.ABANDONED_FLEX_TRACK.get(),
              new Item.Properties().tab(ItemGroup.TAB_TRANSPORTATION)));

  public static final RegistryObject<Item> ABANDONED_LOCKING_TRACK =
      ITEMS.register("abandoned_locking_track",
          () -> new BlockItem(RailcraftBlocks.ABANDONED_LOCKING_TRACK.get(),
              new Item.Properties().tab(ItemGroup.TAB_TRANSPORTATION)));

  public static final RegistryObject<Item> ABANDONED_BUFFER_STOP_TRACK =
      ITEMS.register("abandoned_buffer_stop_track",
          () -> new BlockItem(RailcraftBlocks.ABANDONED_BUFFER_STOP_TRACK.get(),
              new Item.Properties().tab(ItemGroup.TAB_TRANSPORTATION)));

  public static final RegistryObject<Item> ABANDONED_ACTIVATOR_TRACK =
      ITEMS.register("abandoned_activator_track",
          () -> new BlockItem(RailcraftBlocks.ABANDONED_ACTIVATOR_TRACK.get(),
              new Item.Properties().tab(ItemGroup.TAB_TRANSPORTATION)));

  public static final RegistryObject<Item> ABANDONED_BOOSTER_TRACK =
      ITEMS.register("abandoned_booster_track",
          () -> new BlockItem(RailcraftBlocks.ABANDONED_BOOSTER_TRACK.get(),
              new Item.Properties().tab(ItemGroup.TAB_TRANSPORTATION)));

  public static final RegistryObject<Item> ABANDONED_CONTROL_TRACK =
      ITEMS.register("abandoned_control_track",
          () -> new BlockItem(RailcraftBlocks.ABANDONED_CONTROL_TRACK.get(),
              new Item.Properties().tab(ItemGroup.TAB_TRANSPORTATION)));

  public static final RegistryObject<Item> ELECTRIC_FLEX_TRACK =
      ITEMS.register("electric_flex_track",
          () -> new BlockItem(RailcraftBlocks.ELECTRIC_FLEX_TRACK.get(),
              new Item.Properties().tab(ItemGroup.TAB_TRANSPORTATION)));

  public static final RegistryObject<Item> ELECTRIC_LOCKING_TRACK =
      ITEMS.register("electric_locking_track",
          () -> new BlockItem(RailcraftBlocks.ELECTRIC_LOCKING_TRACK.get(),
              new Item.Properties().tab(ItemGroup.TAB_TRANSPORTATION)));

  public static final RegistryObject<Item> ELECTRIC_BUFFER_STOP_TRACK =
      ITEMS.register("electric_buffer_stop_track",
          () -> new BlockItem(RailcraftBlocks.ELECTRIC_BUFFER_STOP_TRACK.get(),
              new Item.Properties().tab(ItemGroup.TAB_TRANSPORTATION)));

  public static final RegistryObject<Item> ELECTRIC_ACTIVATOR_TRACK =
      ITEMS.register("electric_activator_track",
          () -> new BlockItem(RailcraftBlocks.ELECTRIC_ACTIVATOR_TRACK.get(),
              new Item.Properties().tab(ItemGroup.TAB_TRANSPORTATION)));

  public static final RegistryObject<Item> ELECTRIC_BOOSTER_TRACK =
      ITEMS.register("electric_booster_track",
          () -> new BlockItem(RailcraftBlocks.ELECTRIC_BOOSTER_TRACK.get(),
              new Item.Properties().tab(ItemGroup.TAB_TRANSPORTATION)));

  public static final RegistryObject<Item> ELECTRIC_CONTROL_TRACK =
      ITEMS.register("electric_control_track",
          () -> new BlockItem(RailcraftBlocks.ELECTRIC_CONTROL_TRACK.get(),
              new Item.Properties().tab(ItemGroup.TAB_TRANSPORTATION)));

  public static final RegistryObject<Item> HIGH_SPEED_FLEX_TRACK =
      ITEMS.register("high_speed_flex_track",
          () -> new BlockItem(RailcraftBlocks.HIGH_SPEED_FLEX_TRACK.get(),
              new Item.Properties().tab(ItemGroup.TAB_TRANSPORTATION)));

  public static final RegistryObject<Item> HIGH_SPEED_LOCKING_TRACK =
      ITEMS.register("high_speed_locking_track",
          () -> new BlockItem(RailcraftBlocks.HIGH_SPEED_LOCKING_TRACK.get(),
              new Item.Properties().tab(ItemGroup.TAB_TRANSPORTATION)));

  public static final RegistryObject<Item> HIGH_SPEED_BUFFER_STOP_TRACK =
      ITEMS.register("high_speed_buffer_stop_track",
          () -> new BlockItem(RailcraftBlocks.HIGH_SPEED_BUFFER_STOP_TRACK.get(),
              new Item.Properties().tab(ItemGroup.TAB_TRANSPORTATION)));

  public static final RegistryObject<Item> HIGH_SPEED_ACTIVATOR_TRACK =
      ITEMS.register("high_speed_activator_track",
          () -> new BlockItem(RailcraftBlocks.HIGH_SPEED_ACTIVATOR_TRACK.get(),
              new Item.Properties().tab(ItemGroup.TAB_TRANSPORTATION)));

  public static final RegistryObject<Item> HIGH_SPEED_BOOSTER_TRACK =
      ITEMS.register("high_speed_booster_track",
          () -> new BlockItem(RailcraftBlocks.HIGH_SPEED_BOOSTER_TRACK.get(),
              new Item.Properties().tab(ItemGroup.TAB_TRANSPORTATION)));

  public static final RegistryObject<Item> HIGH_SPEED_CONTROL_TRACK =
      ITEMS.register("high_speed_control_track",
          () -> new BlockItem(RailcraftBlocks.HIGH_SPEED_CONTROL_TRACK.get(),
              new Item.Properties().tab(ItemGroup.TAB_TRANSPORTATION)));

  public static final RegistryObject<Item> HIGH_SPEED_ELECTRIC_FLEX_TRACK =
      ITEMS.register("high_speed_electric_flex_track",
          () -> new BlockItem(RailcraftBlocks.HIGH_SPEED_ELECTRIC_FLEX_TRACK.get(),
              new Item.Properties().tab(ItemGroup.TAB_TRANSPORTATION)));

  public static final RegistryObject<Item> HIGH_SPEED_ELECTRIC_LOCKING_TRACK =
      ITEMS.register("high_speed_electric_locking_track",
          () -> new BlockItem(RailcraftBlocks.HIGH_SPEED_ELECTRIC_LOCKING_TRACK.get(),
              new Item.Properties().tab(ItemGroup.TAB_TRANSPORTATION)));

  public static final RegistryObject<Item> HIGH_SPEED_ELECTRIC_BUFFER_STOP_TRACK =
      ITEMS.register("high_speed_electric_buffer_stop_track",
          () -> new BlockItem(RailcraftBlocks.HIGH_SPEED_ELECTRIC_BUFFER_STOP_TRACK.get(),
              new Item.Properties().tab(ItemGroup.TAB_TRANSPORTATION)));

  public static final RegistryObject<Item> HIGH_SPEED_ELECTRIC_ACTIVATOR_TRACK =
      ITEMS.register("high_speed_electric_activator_track",
          () -> new BlockItem(RailcraftBlocks.HIGH_SPEED_ELECTRIC_ACTIVATOR_TRACK.get(),
              new Item.Properties().tab(ItemGroup.TAB_TRANSPORTATION)));

  public static final RegistryObject<Item> HIGH_SPEED_ELECTRIC_BOOSTER_TRACK =
      ITEMS.register("high_speed_electric_booster_track",
          () -> new BlockItem(RailcraftBlocks.HIGH_SPEED_ELECTRIC_BOOSTER_TRACK.get(),
              new Item.Properties().tab(ItemGroup.TAB_TRANSPORTATION)));

  public static final RegistryObject<Item> HIGH_SPEED_ELECTRIC_CONTROL_TRACK =
      ITEMS.register("high_speed_electric_control_track",
          () -> new BlockItem(RailcraftBlocks.HIGH_SPEED_ELECTRIC_CONTROL_TRACK.get(),
              new Item.Properties().tab(ItemGroup.TAB_TRANSPORTATION)));

  public static final RegistryObject<Item> IRON_LOCKING_TRACK =
      ITEMS.register("iron_locking_track",
          () -> new BlockItem(RailcraftBlocks.IRON_LOCKING_TRACK.get(),
              new Item.Properties().tab(ItemGroup.TAB_TRANSPORTATION)));

  public static final RegistryObject<Item> IRON_BUFFER_STOP_TRACK =
      ITEMS.register("iron_buffer_stop_track",
          () -> new BlockItem(RailcraftBlocks.IRON_BUFFER_STOP_TRACK.get(),
              new Item.Properties().tab(ItemGroup.TAB_TRANSPORTATION)));

  public static final RegistryObject<Item> IRON_ACTIVATOR_TRACK =
      ITEMS.register("iron_activator_track",
          () -> new BlockItem(RailcraftBlocks.IRON_ACTIVATOR_TRACK.get(),
              new Item.Properties().tab(ItemGroup.TAB_TRANSPORTATION)));

  public static final RegistryObject<Item> IRON_BOOSTER_TRACK =
      ITEMS.register("iron_booster_track",
          () -> new BlockItem(RailcraftBlocks.IRON_BOOSTER_TRACK.get(),
              new Item.Properties().tab(ItemGroup.TAB_TRANSPORTATION)));

  public static final RegistryObject<Item> IRON_CONTROL_TRACK =
      ITEMS.register("iron_control_track",
          () -> new BlockItem(RailcraftBlocks.IRON_CONTROL_TRACK.get(),
              new Item.Properties().tab(ItemGroup.TAB_TRANSPORTATION)));

  public static final RegistryObject<Item> REINFORCED_FLEX_TRACK =
      ITEMS.register("reinforced_flex_track",
          () -> new BlockItem(RailcraftBlocks.REINFORCED_FLEX_TRACK.get(),
              new Item.Properties().tab(ItemGroup.TAB_TRANSPORTATION)));

  public static final RegistryObject<Item> REINFORCED_LOCKING_TRACK =
      ITEMS.register("reinforced_locking_track",
          () -> new BlockItem(RailcraftBlocks.REINFORCED_LOCKING_TRACK.get(),
              new Item.Properties().tab(ItemGroup.TAB_TRANSPORTATION)));

  public static final RegistryObject<Item> REINFORCED_BUFFER_STOP_TRACK =
      ITEMS.register("reinforced_buffer_stop_track",
          () -> new BlockItem(RailcraftBlocks.REINFORCED_BUFFER_STOP_TRACK.get(),
              new Item.Properties().tab(ItemGroup.TAB_TRANSPORTATION)));

  public static final RegistryObject<Item> REINFORCED_ACTIVATOR_TRACK =
      ITEMS.register("reinforced_activator_track",
          () -> new BlockItem(RailcraftBlocks.REINFORCED_ACTIVATOR_TRACK.get(),
              new Item.Properties().tab(ItemGroup.TAB_TRANSPORTATION)));

  public static final RegistryObject<Item> REINFORCED_BOOSTER_TRACK =
      ITEMS.register("reinforced_booster_track",
          () -> new BlockItem(RailcraftBlocks.REINFORCED_BOOSTER_TRACK.get(),
              new Item.Properties().tab(ItemGroup.TAB_TRANSPORTATION)));

  public static final RegistryObject<Item> REINFORCED_CONTROL_TRACK =
      ITEMS.register("reinforced_control_track",
          () -> new BlockItem(RailcraftBlocks.REINFORCED_CONTROL_TRACK.get(),
              new Item.Properties().tab(ItemGroup.TAB_TRANSPORTATION)));

  public static final RegistryObject<Item> STRAP_IRON_FLEX_TRACK =
      ITEMS.register("strap_iron_flex_track",
          () -> new BlockItem(RailcraftBlocks.STRAP_IRON_FLEX_TRACK.get(),
              new Item.Properties().tab(ItemGroup.TAB_TRANSPORTATION)));

  public static final RegistryObject<Item> STRAP_IRON_LOCKING_TRACK =
      ITEMS.register("strap_iron_locking_track",
          () -> new BlockItem(RailcraftBlocks.STRAP_IRON_LOCKING_TRACK.get(),
              new Item.Properties().tab(ItemGroup.TAB_TRANSPORTATION)));

  public static final RegistryObject<Item> STRAP_IRON_BUFFER_STOP_TRACK =
      ITEMS.register("strap_iron_buffer_stop_track",
          () -> new BlockItem(RailcraftBlocks.STRAP_IRON_BUFFER_STOP_TRACK.get(),
              new Item.Properties().tab(ItemGroup.TAB_TRANSPORTATION)));

  public static final RegistryObject<Item> STRAP_IRON_ACTIVATOR_TRACK =
      ITEMS.register("strap_iron_activator_track",
          () -> new BlockItem(RailcraftBlocks.STRAP_IRON_ACTIVATOR_TRACK.get(),
              new Item.Properties().tab(ItemGroup.TAB_TRANSPORTATION)));

  public static final RegistryObject<Item> STRAP_IRON_BOOSTER_TRACK =
      ITEMS.register("strap_iron_booster_track",
          () -> new BlockItem(RailcraftBlocks.STRAP_IRON_BOOSTER_TRACK.get(),
              new Item.Properties().tab(ItemGroup.TAB_TRANSPORTATION)));

  public static final RegistryObject<Item> STRAP_IRON_CONTROL_TRACK =
      ITEMS.register("strap_iron_control_track",
          () -> new BlockItem(RailcraftBlocks.STRAP_IRON_CONTROL_TRACK.get(),
              new Item.Properties().tab(ItemGroup.TAB_TRANSPORTATION)));

  public static final RegistryObject<Item> ELEVATOR_TRACK =
      ITEMS.register("elevator_track",
          () -> new BlockItem(RailcraftBlocks.ELEVATOR_TRACK.get(),
              new Item.Properties().tab(ItemGroup.TAB_TRANSPORTATION)));

  public static final RegistryObject<Item> WYE_TRACK =
      ITEMS.register("wye_track",
          () -> new BlockItem(RailcraftBlocks.WYE_TRACK.get(),
              new Item.Properties().tab(ItemGroup.TAB_TRANSPORTATION)));

  public static final RegistryObject<Item> TURNOUT_TRACK =
      ITEMS.register("turnout_track",
          () -> new BlockItem(RailcraftBlocks.TURNOUT_TRACK.get(),
              new Item.Properties().tab(ItemGroup.TAB_TRANSPORTATION)));

  public static final RegistryObject<Item> IRON_CROWBAR =
      ITEMS.register("iron_crowbar",
          () -> new CrowbarItem(2.5F, -2.8F, ItemTier.IRON,
              new Item.Properties().tab(TAB)));

  public static final RegistryObject<Item> STEEL_CROWBAR =
      ITEMS.register("steel_crowbar",
          () -> new CrowbarItem(2.5F, -2.7F, ItemTier.IRON,
              new Item.Properties().tab(TAB)));

  public static final RegistryObject<Item> DIAMOND_CROWBAR =
      ITEMS.register("diamond_crowbar",
          () -> new CrowbarItem(2.5F, -2.4F, ItemTier.DIAMOND,
              new Item.Properties().tab(TAB)));

  public static final RegistryObject<Item> SEASONS_CROWBAR =
      ITEMS.register("seasons_crowbar",
          () -> new SeasonsCrowbarItem(new Item.Properties().tab(TAB)));

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
              .addOutfittedBlock(TrackTypes.HIGH_SPEED,
                  RailcraftBlocks.HIGH_SPEED_BUFFER_STOP_TRACK)
              .addOutfittedBlock(TrackTypes.HIGH_SPEED_ELECTRIC,
                  RailcraftBlocks.HIGH_SPEED_ELECTRIC_BUFFER_STOP_TRACK)
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
              .addOutfittedBlock(TrackTypes.HIGH_SPEED, RailcraftBlocks.HIGH_SPEED_CONTROL_TRACK)
              .addOutfittedBlock(TrackTypes.HIGH_SPEED_ELECTRIC,
                  RailcraftBlocks.HIGH_SPEED_ELECTRIC_CONTROL_TRACK)
              .addOutfittedBlock(TrackTypes.IRON, RailcraftBlocks.IRON_CONTROL_TRACK)
              .addOutfittedBlock(TrackTypes.REINFORCED, RailcraftBlocks.REINFORCED_CONTROL_TRACK)
              .addOutfittedBlock(TrackTypes.STRAP_IRON,
                  RailcraftBlocks.STRAP_IRON_CONTROL_TRACK)
              .tab(TAB)));


  public static final RegistryObject<Item> HIGH_SPEED_TRANSITION_TRACK_KIT =
      ITEMS.register("high_speed_transition_track_kit",
          () -> new Item(new Item.Properties().tab(TAB)));

  public static final RegistryObject<Item> GOGGLES =
      ITEMS.register("goggles",
          () -> new GogglesItem(new Item.Properties().tab(TAB)));

  public static final RegistryObject<BlockItem> MANUAL_ROLLING_MACHINE =
      ITEMS.register("manual_rolling_machine",
          () -> new BlockItem(RailcraftBlocks.MANUAL_ROLLING_MACHINE.get(),
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

  // BLOCK
  // public static final RegistryObject<BlockItem> STEEL_BLOCK =
  // ITEMS.register("steel_block",
  // () -> new BlockItem(new Item.Properties().tab(TAB)));

  // public static final RegistryObject<BlockItem> COPPER_BLOCK =
  // ITEMS.register("copper_block",
  // () -> new BlockItem(new Item.Properties().tab(TAB)));

  // public static final RegistryObject<BlockItem> TIN_BLOCK =
  // ITEMS.register("tin_block",
  // () -> new BlockItem(new Item.Properties().tab(TAB)));

  // public static final RegistryObject<BlockItem> ZINC_BLOCK =
  // ITEMS.register("zinc_block",
  // () -> new BlockItem(new Item.Properties().tab(TAB)));

  // public static final RegistryObject<BlockItem> BRASS_BLOCK =
  // ITEMS.register("brass_block",
  // () -> new BlockItem(new Item.Properties().tab(TAB)));

  // public static final RegistryObject<BlockItem> BRONZE_BLOCK =
  // ITEMS.register("bronze_block",
  // () -> new BlockItem(new Item.Properties().tab(TAB)));

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
