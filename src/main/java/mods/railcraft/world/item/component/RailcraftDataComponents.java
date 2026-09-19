package mods.railcraft.world.item.component;

import com.mojang.serialization.Codec;
import mods.railcraft.api.core.RailcraftConstants;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.fluids.SimpleFluidContent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class RailcraftDataComponents {

  private static final DeferredRegister.DataComponents deferredRegister =
      DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, RailcraftConstants.ID);

  public static void register(IEventBus modEventBus) {
    deferredRegister.register(modEventBus);
  }

  public static final DeferredHolder<DataComponentType<?>, DataComponentType<LocomotiveColorComponent>> LOCOMOTIVE_COLOR =
      deferredRegister.registerComponentType("locomotive_color", builder ->
          builder
              .persistent(LocomotiveColorComponent.CODEC)
              .networkSynchronized(LocomotiveColorComponent.STREAM_CODEC));

  public static final DeferredHolder<DataComponentType<?>, DataComponentType<LocomotiveLockComponent>> LOCOMOTIVE_LOCK =
      deferredRegister.registerComponentType("locomotive_lock", builder ->
          builder
              .persistent(LocomotiveLockComponent.CODEC)
              .networkSynchronized(LocomotiveLockComponent.STREAM_CODEC));

  public static final DeferredHolder<DataComponentType<?>, DataComponentType<LocomotiveWhistlePitchComponent>> LOCOMOTIVE_WHISTLE_PITCH =
      deferredRegister.registerComponentType("locomotive_whistle_pitch", builder ->
          builder
              .persistent(LocomotiveWhistlePitchComponent.CODEC)
              .networkSynchronized(LocomotiveWhistlePitchComponent.STREAM_CODEC));

  public static final DeferredHolder<DataComponentType<?>, DataComponentType<LocomotiveOwnerComponent>> LOCOMOTIVE_OWNER =
      deferredRegister.registerComponentType("locomotive_owner", builder ->
          builder
              .persistent(LocomotiveOwnerComponent.CODEC)
              .networkSynchronized(LocomotiveOwnerComponent.STREAM_CODEC));

  public static final DeferredHolder<DataComponentType<?>, DataComponentType<LocomotiveEnergyComponent>> LOCOMOTIVE_ENERGY =
      deferredRegister.registerComponentType("locomotive_energy", builder ->
          builder
              .persistent(LocomotiveEnergyComponent.CODEC)
              .networkSynchronized(LocomotiveEnergyComponent.STREAM_CODEC));

  public static final DeferredHolder<DataComponentType<?>, DataComponentType<TicketComponent>> TICKET =
      deferredRegister.registerComponentType("ticket", builder ->
          builder
              .persistent(TicketComponent.CODEC)
              .networkSynchronized(TicketComponent.STREAM_CODEC));

  public static final DeferredHolder<DataComponentType<?>, DataComponentType<AuraComponent>> AURA =
      deferredRegister.registerComponentType("aura", builder ->
          builder
              .persistent(AuraComponent.CODEC)
              .networkSynchronized(AuraComponent.STREAM_CODEC));

  public static final DeferredHolder<DataComponentType<?>, DataComponentType<PairToolComponent>> PAIR_TOOL =
      deferredRegister.registerComponentType("pair_tool", builder ->
          builder
              .persistent(PairToolComponent.CODEC)
              .networkSynchronized(PairToolComponent.STREAM_CODEC));

  public static final DeferredHolder<DataComponentType<?>, DataComponentType<SeasonComponent>> SEASON =
      deferredRegister.registerComponentType("season", builder ->
          builder
              .persistent(SeasonComponent.CODEC)
              .networkSynchronized(SeasonComponent.STREAM_CODEC));

  public static final DeferredHolder<DataComponentType<?>, DataComponentType<Boolean>> CLICK_TO_CRAFT =
      deferredRegister.registerComponentType("click_to_craft", builder ->
          builder
              .persistent(Codec.BOOL)
              .networkSynchronized(ByteBufCodecs.BOOL));

  public static final DeferredHolder<DataComponentType<?>, DataComponentType<RoutingTableBookContent>> ROUTING_TABLE_BOOK =
      deferredRegister.registerComponentType("routing_table_book", builder ->
          builder
              .persistent(RoutingTableBookContent.CODEC)
              .networkSynchronized(RoutingTableBookContent.STREAM_CODEC));

  public static final DeferredHolder<DataComponentType<?>, DataComponentType<SimpleFluidContent>> FLUID =
      deferredRegister.registerComponentType("simple_fluid_content", builder ->
          builder
              .persistent(SimpleFluidContent.CODEC)
              .networkSynchronized(SimpleFluidContent.STREAM_CODEC));

  public static final DeferredHolder<DataComponentType<?>, DataComponentType<BatteryComponent>> BATTERY =
      deferredRegister.registerComponentType("battery", builder ->
          builder
              .persistent(BatteryComponent.CODEC)
              .networkSynchronized(BatteryComponent.STREAM_CODEC));
}
