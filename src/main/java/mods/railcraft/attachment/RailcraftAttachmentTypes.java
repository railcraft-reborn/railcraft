package mods.railcraft.attachment;

import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.world.entity.vehicle.RollingStockImpl;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
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
              .serializable(holder -> new RollingStockImpl((AbstractMinecart) holder))
              .build());
}
