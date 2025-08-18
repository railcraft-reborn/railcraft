package mods.railcraft.attachment;

import java.util.Optional;
import java.util.function.Supplier;
import com.mojang.serialization.Codec;
import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.world.entity.vehicle.RollingStockImpl;
import net.minecraft.util.ExtraCodecs;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class RailcraftAttachmentTypes {

  private static final DeferredRegister<AttachmentType<?>> deferredRegister =
      DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, RailcraftConstants.ID);

  public static void register(IEventBus modEventBus) {
    deferredRegister.register(modEventBus);
  }

  public static final DeferredHolder<AttachmentType<?>, AttachmentType<RollingStockImpl>> MINECART_ROLLING_STOCK =
      deferredRegister.register("minecart_rolling_stock",
          () -> AttachmentType
              .serializable(RollingStockImpl::new)
              .build());

  public static final Supplier<AttachmentType<Boolean>> CAN_USE_RAIL =
      deferredRegister.register("can_use_rail",
          () -> AttachmentType.builder(() -> true)
              .serialize(Codec.BOOL)
              .build());

  public static final Supplier<AttachmentType<Boolean>> SHOULD_DO_RAIL_FUNCTIONS =
      deferredRegister.register("should_do_rail_functions",
          () -> AttachmentType.builder(() -> true)
              .serialize(Codec.BOOL)
              .build());

  public static final Supplier<AttachmentType<Float>> CURRENT_SPEED_CAP_ON_RAIL =
      deferredRegister.register("current_speed_cap_on_rail",
          () -> AttachmentType.builder(() -> 1.2F)
              .serialize(Codec.FLOAT)
              .build());

  public static final Supplier<AttachmentType<Float>> MAX_CART_SPEED_ON_RAIL =
      deferredRegister.register("get_max_cart_speed_on_rail",
          () -> AttachmentType.builder(() -> 1.2F)
              .serialize(Codec.FLOAT)
              .build());

  public static final Supplier<AttachmentType<Optional<Float>>> MAX_SPEED_AIR_LATERAL =
      deferredRegister.register("max_speed_air_lateral",
          () -> AttachmentType.builder(Optional::<Float>empty)
              .serialize(ExtraCodecs.optionalEmptyMap(Codec.FLOAT))
              .build());

  public static final Supplier<AttachmentType<Float>> MAX_SPEED_AIR_VERTICAL =
      deferredRegister.register("max_speed_air_vertical",
          () -> AttachmentType.builder(() -> RailcraftConstants.DEFAULT_MAX_SPEED_AIR_VERTICAL)
              .serialize(Codec.FLOAT)
              .build());

  public static final Supplier<AttachmentType<Float>> AIR_DRAG =
      deferredRegister.register("air_drag",
          () -> AttachmentType.builder(() -> RailcraftConstants.DEFAULT_AIR_DRAG)
              .serialize(Codec.FLOAT)
              .build());
}
