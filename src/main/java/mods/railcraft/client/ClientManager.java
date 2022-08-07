package mods.railcraft.client;

import mods.railcraft.api.signal.SignalAspect;
import mods.railcraft.api.signal.SignalUtil;
import mods.railcraft.client.gui.screen.inventory.BlastFurnaceScreen;
import mods.railcraft.client.gui.screen.inventory.CokeOvenScreen;
import mods.railcraft.client.gui.screen.inventory.CreativeLocomotiveScreen;
import mods.railcraft.client.gui.screen.inventory.CrusherScreen;
import mods.railcraft.client.gui.screen.inventory.ElectricLocomotiveScreen;
import mods.railcraft.client.gui.screen.inventory.FeedStationScreen;
import mods.railcraft.client.gui.screen.inventory.FluidFueledSteamBoilerScreen;
import mods.railcraft.client.gui.screen.inventory.FluidManipulatorScreen;
import mods.railcraft.client.gui.screen.inventory.ItemManipulatorScreen;
import mods.railcraft.client.gui.screen.inventory.ManualRollingMachineScreen;
import mods.railcraft.client.gui.screen.inventory.SolidFueledSteamBoilerScreen;
import mods.railcraft.client.gui.screen.inventory.SteamLocomotiveScreen;
import mods.railcraft.client.gui.screen.inventory.SteamTurbineScreen;
import mods.railcraft.client.gui.screen.inventory.TankMinecartScreen;
import mods.railcraft.client.gui.screen.inventory.TankScreen;
import mods.railcraft.client.gui.screen.inventory.TunnelBoreScreen;
import mods.railcraft.client.model.RailcraftLayerDefinitions;
import mods.railcraft.client.particle.PumpkinParticle;
import mods.railcraft.client.particle.SparkParticle;
import mods.railcraft.client.particle.SteamParticle;
import mods.railcraft.client.particle.TuningAuraParticle;
import mods.railcraft.client.renderer.ShuntingAuraRenderer;
import mods.railcraft.client.renderer.blockentity.AbstractSignalBoxRenderer;
import mods.railcraft.client.renderer.blockentity.AbstractSignalRenderer;
import mods.railcraft.client.renderer.blockentity.BlockSignalRelayBoxRenderer;
import mods.railcraft.client.renderer.blockentity.FluidLoaderRenderer;
import mods.railcraft.client.renderer.blockentity.FluidManipulatorRenderer;
import mods.railcraft.client.renderer.blockentity.RailcraftBlockEntityRenderers;
import mods.railcraft.client.renderer.blockentity.SignalCapacitorBoxRenderer;
import mods.railcraft.client.renderer.blockentity.SignalControllerBoxRenderer;
import mods.railcraft.client.renderer.blockentity.SignalReceiverBoxRenderer;
import mods.railcraft.client.renderer.entity.RailcraftEntityRenderers;
import mods.railcraft.particle.RailcraftParticleTypes;
import mods.railcraft.world.inventory.RailcraftMenuTypes;
import mods.railcraft.world.item.LocomotiveItem;
import mods.railcraft.world.item.RailcraftItems;
import mods.railcraft.world.level.block.ForceTrackEmitterBlock;
import mods.railcraft.world.level.block.RailcraftBlocks;
import mods.railcraft.world.level.block.track.ForceTrackBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.GrassColor;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class ClientManager {

  private static ClientManager instance;

  private final Minecraft minecraft;

  private final ShuntingAuraRenderer shuntingAuraRenderer;

  public ClientManager() {
    instance = this;

    SignalUtil._setTuningAuraHandler(new TuningAuraHandlerImpl());

    var modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
    modEventBus.addListener(this::handleClientSetup);
    modEventBus.addListener(this::handleItemColors);
    modEventBus.addListener(this::handleBlockColors);
    modEventBus.addListener(this::handleTextureStitch);
    modEventBus.addListener(this::handleParticleRegistration);
    modEventBus.addListener(this::handleRegisterRenderers);
    modEventBus.addListener(this::handleRegisterLayerDefinitions);

    MinecraftForge.EVENT_BUS.register(this);

    this.minecraft = Minecraft.getInstance();
    this.shuntingAuraRenderer = new ShuntingAuraRenderer();
  }

  public ShuntingAuraRenderer getShuntingAuraRenderer() {
    return this.shuntingAuraRenderer;
  }

  // ================================================================================
  // Mod Events
  // ================================================================================

  private void handleClientSetup(FMLClientSetupEvent event) {
    RenderLayers.register();

    // === Menu Screens ===

    MenuScreens.register(RailcraftMenuTypes.SOLID_FUELED_STEAM_BOILER.get(),
        SolidFueledSteamBoilerScreen::new);
    MenuScreens.register(RailcraftMenuTypes.FLUID_FUELED_STEAM_BOILER.get(),
        FluidFueledSteamBoilerScreen::new);
    MenuScreens.register(RailcraftMenuTypes.STEAM_TURBINE.get(),
        SteamTurbineScreen::new);
    MenuScreens.register(RailcraftMenuTypes.TANK.get(),
        TankScreen::new);
    MenuScreens.register(RailcraftMenuTypes.BLAST_FURNACE.get(),
        BlastFurnaceScreen::new);
    MenuScreens.register(RailcraftMenuTypes.FEED_STATION.get(),
        FeedStationScreen::new);
    MenuScreens.register(RailcraftMenuTypes.CREATIVE_LOCOMOTIVE.get(),
        CreativeLocomotiveScreen::new);
    MenuScreens.register(RailcraftMenuTypes.ELECTRIC_LOCOMOTIVE.get(),
        ElectricLocomotiveScreen::new);
    MenuScreens.register(RailcraftMenuTypes.STEAM_LOCOMOTIVE.get(),
        SteamLocomotiveScreen::new);
    MenuScreens.register(RailcraftMenuTypes.MANUAL_ROLLING_MACHINE.get(),
        ManualRollingMachineScreen::new);
    MenuScreens.register(RailcraftMenuTypes.COKE_OVEN.get(),
        CokeOvenScreen::new);
    MenuScreens.register(RailcraftMenuTypes.CRUSHER.get(),
        CrusherScreen::new);
    MenuScreens.register(RailcraftMenuTypes.ITEM_MANIPULATOR.get(),
        ItemManipulatorScreen::new);
    MenuScreens.register(RailcraftMenuTypes.FLUID_MANIPULATOR.get(),
        FluidManipulatorScreen::new);
    MenuScreens.register(RailcraftMenuTypes.TANK_MINECART.get(),
        TankMinecartScreen::new);
    MenuScreens.register(RailcraftMenuTypes.TUNNEL_BORE.get(), TunnelBoreScreen::new);
  }

  private void handleItemColors(RegisterColorHandlersEvent.Item event) {
    event.register(
        (stack, tintIndex) -> switch (tintIndex) {
          case 0 -> LocomotiveItem.getPrimaryColor(stack).getMaterialColor().col;
          case 1 -> LocomotiveItem.getSecondaryColor(stack).getMaterialColor().col;
          default -> 0xFFFFFFFF;
        },
        RailcraftItems.CREATIVE_LOCOMOTIVE.get(),
        RailcraftItems.STEAM_LOCOMOTIVE.get(),
        RailcraftItems.ELECTRIC_LOCOMOTIVE.get());
  }

  private void handleBlockColors(RegisterColorHandlersEvent.Block event) {
    event.register(
        (state, level, pos,
            tintIndex) -> state.getValue(ForceTrackEmitterBlock.COLOR).getMaterialColor().col,
        RailcraftBlocks.FORCE_TRACK_EMITTER.get());

    event.register(
        (state, level, pos,
            tintIndex) -> state.getValue(ForceTrackBlock.COLOR).getMaterialColor().col,
        RailcraftBlocks.FORCE_TRACK.get());

    event.register(
        (state, level, pos, tintIndex) -> level != null && pos != null
            ? BiomeColors.getAverageGrassColor(level, pos)
            : GrassColor.get(0.5D, 1.0D),
        RailcraftBlocks.ABANDONED_TRACK.get());
  }

  private void handleTextureStitch(TextureStitchEvent.Pre event) {
    if (event.getAtlas().location().equals(InventoryMenu.BLOCK_ATLAS)) {
      AbstractSignalRenderer.ASPECT_TEXTURE_LOCATIONS.values().forEach(event::addSprite);
      AbstractSignalBoxRenderer.ASPECT_TEXTURE_LOCATIONS.values().forEach(event::addSprite);
      event.addSprite(SignalControllerBoxRenderer.TEXTURE_LOCATION);
      event.addSprite(SignalCapacitorBoxRenderer.TEXTURE_LOCATION);
      event.addSprite(SignalReceiverBoxRenderer.TEXTURE_LOCATION);
      event.addSprite(BlockSignalRelayBoxRenderer.TEXTURE_LOCATION);
      event.addSprite(AbstractSignalBoxRenderer.BOTTOM_TEXTURE_LOCATION);
      event.addSprite(AbstractSignalBoxRenderer.CONNECTED_SIDE_TEXTURE_LOCATION);
      event.addSprite(AbstractSignalBoxRenderer.SIDE_TEXTURE_LOCATION);
      event.addSprite(FluidManipulatorRenderer.INTERIOR_TEXTURE_LOCATION);
      event.addSprite(FluidLoaderRenderer.PIPE_END_TEXTURE_LOCATION);
      event.addSprite(FluidLoaderRenderer.PIPE_SIDE_TEXTURE_LOCATION);
    }
  }

  private void handleParticleRegistration(RegisterParticleProvidersEvent event) {
    event.register(RailcraftParticleTypes.STEAM.get(), SteamParticle.Provider::new);
    event.register(RailcraftParticleTypes.SPARK.get(), SparkParticle.Provider::new);
    event.register(RailcraftParticleTypes.PUMPKIN.get(), PumpkinParticle.Provider::new);
    event.register(RailcraftParticleTypes.TUNING_AURA.get(), TuningAuraParticle.Provider::new);
  }

  private void handleRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
    RailcraftEntityRenderers.register(event);
    RailcraftBlockEntityRenderers.register(event);
  }

  private void handleRegisterLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
    RailcraftLayerDefinitions.createRoots(event::registerLayerDefinition);
  }

  // ================================================================================
  // Forge Events
  // ================================================================================

  @SubscribeEvent
  public void handleClientTick(TickEvent.ClientTickEvent event) {
    if (event.phase == TickEvent.Phase.START
        && (this.minecraft.level != null && !this.minecraft.isPaused())) {
      SignalAspect.tickBlinkState();
    }
  }

  @SubscribeEvent
  public void handleRenderWorldLast(RenderLevelStageEvent event) {
    this.shuntingAuraRenderer.render(event.getPartialTick(), event.getPoseStack());
  }

  @SubscribeEvent
  public void handleClientLoggedOut(ClientPlayerNetworkEvent.LoggingOut event) {
    this.shuntingAuraRenderer.clearCarts();
  }

  // ================================================================================
  // Static Methods
  // ================================================================================

  public static ClientManager instance() {
    return instance;
  }
}
