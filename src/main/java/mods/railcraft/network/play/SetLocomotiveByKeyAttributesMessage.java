package mods.railcraft.network.play;

import java.util.function.Supplier;
import mods.railcraft.world.entity.vehicle.locomotive.Locomotive;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.vehicle.Minecart;
import net.minecraftforge.network.NetworkEvent;

public record SetLocomotiveByKeyAttributesMessage(LocomotiveKeyBinding keyBinding) {

  public void encode(FriendlyByteBuf out) {
    out.writeEnum(this.keyBinding);
  }

  public static SetLocomotiveByKeyAttributesMessage decode(FriendlyByteBuf in) {
    return new SetLocomotiveByKeyAttributesMessage(
        in.readEnum(LocomotiveKeyBinding.class));
  }

  public boolean handle(Supplier<NetworkEvent.Context> ctx) {
    var player = ctx.get().getSender();
    if (player.getVehicle() instanceof Minecart minecart) {
      switch (this.keyBinding) {
        case REVERSE -> Locomotive.applyAction(player, minecart, false, loco -> loco.setReverse(!loco.isReverse()));
        case FASTER -> Locomotive.applyAction(player, minecart, false, Locomotive::increaseSpeed);
        case SLOWER -> Locomotive.applyAction(player, minecart, false, Locomotive::decreaseSpeed);
        case WHISTLE -> Locomotive.applyAction(player, minecart, false, Locomotive::whistle);
        case MODE_CHANGE -> Locomotive.applyAction(player, minecart, false, loco -> {
          var mode = loco.getMode();
          do {
            mode = mode.next();
          } while (!loco.isAllowedMode(mode));
          loco.setMode(mode);
        });
      }
    }
    return true;
  }

  public enum LocomotiveKeyBinding {
    REVERSE,
    FASTER,
    SLOWER,
    MODE_CHANGE,
    WHISTLE;
  }
}
