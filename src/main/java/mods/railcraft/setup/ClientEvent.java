package mods.railcraft.setup;

import mods.railcraft.Railcraft;
import mods.railcraft.api.charge.Charge;
import mods.railcraft.api.signal.SignalAspect;
import mods.railcraft.api.signal.SignalTools;
import mods.railcraft.client.ClientEffects;
import mods.railcraft.client.renderer.ShuntingAuraRenderer;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Railcraft.ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ClientEvent {

  @SubscribeEvent
  public static void handleClientTick(TickEvent.ClientTickEvent event) {
    if (event.phase == TickEvent.Phase.START
      && (Minecraft.getInstance().level != null && !Minecraft.getInstance().isPaused())) {
      SignalAspect.tickBlinkState();
    }
  }

  private static ShuntingAuraRenderer SHUNTING_AURA_RENDERER;
  static {
    SHUNTING_AURA_RENDERER = new ShuntingAuraRenderer();
    SignalTools._setTuningAuraProvider(ClientEffects.INSTANCE);
    Charge._setZapEffectProvider(ClientEffects.INSTANCE);
  }

  public static ShuntingAuraRenderer getShuntingAuraRenderer() {
    return SHUNTING_AURA_RENDERER;
  }

  @SubscribeEvent
  public static void handleRenderWorldLast(RenderLevelStageEvent event) {
    SHUNTING_AURA_RENDERER.render(event.getPartialTick(), event.getPoseStack());
  }

  @SubscribeEvent
  public static void handleClientLoggedOut(ClientPlayerNetworkEvent.LoggingOut event) {
    SHUNTING_AURA_RENDERER.clearCarts();
  }
}
