package mods.railcraft.network.to_server;

import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.network.RailcraftCustomPacketPayload;
import mods.railcraft.world.item.GogglesItem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;

public record UpdateAuraByKeyMessage(CompoundTag tag) implements RailcraftCustomPacketPayload {

  public static final ResourceLocation ID = RailcraftConstants.rl("update_aura_by_key");

  public static UpdateAuraByKeyMessage read(FriendlyByteBuf buf) {
    return new UpdateAuraByKeyMessage(buf.readNbt());
  }

  @Override
  public void write(FriendlyByteBuf buf) {
    buf.writeNbt(tag);
  }

  @Override
  public ResourceLocation id() {
    return ID;
  }

  @Override
  public void handle(PlayPayloadContext context) {
    context.player().ifPresent(player -> {
      var itemStack = player.getItemBySlot(EquipmentSlot.HEAD);
      if (itemStack.getItem() instanceof GogglesItem) {
        itemStack.setTag(tag);
      }
    });
  }
}
