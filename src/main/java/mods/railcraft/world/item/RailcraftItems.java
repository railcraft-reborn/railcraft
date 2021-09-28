package mods.railcraft.world.item;

import mods.railcraft.Railcraft;
import mods.railcraft.world.entity.RailcraftEntityTypes;
import mods.railcraft.world.level.block.RailcraftBlocks;
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
          () -> new BlockItem(RailcraftBlocks.SWITCH_TRACK_LEVER.get(), new Item.Properties()));

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

  public static final RegistryObject<Item> SIGNAL_RELAY_BOX =
      ITEMS.register("signal_relay_box",
          () -> new BlockItem(
              RailcraftBlocks.SIGNAL_RELAY_BOX.get(),
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

  public static final RegistryObject<Item> DUAL_SIGNAL =
      ITEMS.register("dual_signal",
          () -> new BlockItem(
              RailcraftBlocks.DUAL_SIGNAL.get(),
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

  public static final RegistryObject<Item> SIGNAL =
      ITEMS.register("signal",
          () -> new BlockItem(
              RailcraftBlocks.SIGNAL.get(),
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

  public static final RegistryObject<Item> MOW_TRACK_REMOVER =
      ITEMS.register("mow_track_remover",
          () -> new CartItem(
              RailcraftEntityTypes.MOW_TRACK_REMOVER,
              new Item.Properties().tab(TAB)));

  public static final RegistryObject<Item> MOW_TRACK_LAYER =
      ITEMS.register("mow_track_layer",
          () -> new CartItem(
              RailcraftEntityTypes.MOW_TRACK_LAYER,
              new Item.Properties().tab(TAB)));

  public static final RegistryObject<Item> TUNNEL_BORE =
      ITEMS.register("tunnel_bore",
          () -> new TunnelBoreItem(
              RailcraftEntityTypes.TUNNEL_BORE,
              new Item.Properties()
                  .stacksTo(1)
                  .tab(TAB)));

  public static final RegistryObject<Item> CREATIVE_LOCOMOTIVE =
      ITEMS.register("creative_locomotive",
          () -> new LocomotiveItem(
              RailcraftEntityTypes.CREATIVE_LOCOMOTIVE,
              DyeColor.BLACK, DyeColor.MAGENTA,
              new Item.Properties()
                  .stacksTo(1)
                  .tab(TAB)));

  public static final RegistryObject<Item> ELECTRIC_LOCOMOTIVE =
      ITEMS.register("electric_locomotive",
          () -> new LocomotiveItem(
              RailcraftEntityTypes.ELECTRIC_LOCOMOTIVE,
              DyeColor.YELLOW, DyeColor.BLACK,
              new Item.Properties()
                  .stacksTo(1)
                  .tab(TAB)));

  public static final RegistryObject<Item> STEAM_LOCOMOTIVE =
      ITEMS.register("steam_locomotive",
          () -> new LocomotiveItem(RailcraftEntityTypes.STEAM_LOCOMOTIVE,
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

  public static final RegistryObject<BlockItem> FIRESTONE_ORE =
      ITEMS.register("firestone",
          () -> new BlockItem(RailcraftBlocks.FIRESTONE.get(),
              new Item.Properties().tab(TAB)));

  public static final RegistryObject<BlockItem> FORCE_TRACK_EMITTER =
      ITEMS.register("force_track_emitter",
          () -> new BlockItem(
              RailcraftBlocks.FORCE_TRACK_EMITTER.get(),
              new Item.Properties().tab(TAB)));

  public static final RegistryObject<BlockItem> FORCE_TRACK =
      ITEMS.register("force_track",
          () -> new BlockItem(RailcraftBlocks.FORCE_TRACK.get(),
              new Item.Properties().tab(ItemGroup.TAB_TRANSPORTATION)));

  public static final RegistryObject<Item> REINFORCED_FLEX_TRACK =
      ITEMS.register("reinforced_flex_track",
          () -> new BlockItem(RailcraftBlocks.REINFORCED_FLEX_TRACK.get(),
              new Item.Properties().tab(ItemGroup.TAB_TRANSPORTATION)));

  public static final RegistryObject<Item> ELECTRIC_FLEX_TRACK =
      ITEMS.register("electric_flex_track",
          () -> new BlockItem(RailcraftBlocks.ELECTRIC_FLEX_TRACK.get(),
              new Item.Properties().tab(ItemGroup.TAB_TRANSPORTATION)));

  public static final RegistryObject<Item> HIGH_SPEED_FLEX_TRACK =
      ITEMS.register("high_speed_flex_track",
          () -> new BlockItem(RailcraftBlocks.HIGH_SPEED_FLEX_TRACK.get(),
              new Item.Properties().tab(ItemGroup.TAB_TRANSPORTATION)));

  public static final RegistryObject<Item> HIGH_SPEED_ELECTRIC_FLEX_TRACK =
      ITEMS.register("high_speed_electric_flex_track",
          () -> new BlockItem(RailcraftBlocks.HIGH_SPEED_ELECTRIC_FLEX_TRACK.get(),
              new Item.Properties().tab(ItemGroup.TAB_TRANSPORTATION)));

  public static final RegistryObject<Item> ABANDONED_FLEX_TRACK =
      ITEMS.register("abandoned_flex_track",
          () -> new BlockItem(RailcraftBlocks.ABANDONED_FLEX_TRACK.get(),
              new Item.Properties().tab(ItemGroup.TAB_TRANSPORTATION)));

  public static final RegistryObject<Item> STRAP_IRON_FLEX_TRACK =
      ITEMS.register("strap_iron_flex_track",
          () -> new BlockItem(RailcraftBlocks.STRAP_IRON_FLEX_TRACK.get(),
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
          () -> new CrowbarItem(2.5F, -2.4F, ItemTier.IRON,
              new Item.Properties().tab(TAB)));

  public static final RegistryObject<Item> ACTIVATOR_TRACK_KIT =
      ITEMS.register("activator_track_kit",
          () -> new Item(new Item.Properties().tab(TAB)));

  public static final RegistryObject<Item> BOOSTER_TRACK_KIT =
      ITEMS.register("booster_track_kit",
          () -> new Item(new Item.Properties().tab(TAB)));

  public static final RegistryObject<Item> HIGH_SPEED_TRANSITION_TRACK_KIT =
      ITEMS.register("high_speed_transition_track_kit",
          () -> new Item(new Item.Properties().tab(TAB)));

  public static final RegistryObject<Item> GOGGLES =
      ITEMS.register("goggles",
          () -> new Item(new Item.Properties().tab(TAB)));
}
