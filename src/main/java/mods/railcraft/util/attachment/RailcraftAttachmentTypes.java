package mods.railcraft.util.attachment;

import mods.railcraft.api.core.RailcraftConstants;
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

  public static final DeferredHolder<AttachmentType<?>, AttachmentType<RollingStockDataAttachment>> ROLLING_STOCK_DATA =
      deferredRegister.register("rolling_stock_data",
          () -> AttachmentType.serializable(RollingStockDataAttachment::new).build());
}
