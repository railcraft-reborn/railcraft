package mods.railcraft.client;

import mods.railcraft.RailcraftDist;
import mods.railcraft.api.signal.SignalAspect;
import mods.railcraft.client.gui.screen.ActionSignalBoxScreen;
import mods.railcraft.client.gui.screen.AnalogSignalControllerBoxScreen;
import mods.railcraft.client.gui.screen.SignalCapacitorBoxScreen;
import mods.railcraft.client.gui.screen.SignalControllerBoxScreen;
import mods.railcraft.client.gui.screen.SwitchTrackMotorScreen;
import mods.railcraft.client.gui.screen.inventory.CokeOvenMenuScreen;
import mods.railcraft.client.gui.screen.inventory.CreativeLocomotiveScreen;
import mods.railcraft.client.gui.screen.inventory.ElectricLocomotiveScreen;
import mods.railcraft.client.gui.screen.inventory.FluidManipulatorScreen;
import mods.railcraft.client.gui.screen.inventory.ItemManipulatorScreen;
import mods.railcraft.client.gui.screen.inventory.ManualRollingMachineScreen;
import mods.railcraft.client.gui.screen.inventory.SteamLocomotiveScreen;
import mods.railcraft.client.gui.screen.inventory.TankMinecartScreen;
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
import mods.railcraft.world.level.block.entity.SwitchTrackMotorBlockEntity;
import mods.railcraft.world.level.block.entity.signal.ActionSignalBoxBlockEntity;
import mods.railcraft.world.level.block.entity.signal.AnalogSignalControllerBoxBlockEntity;
import mods.railcraft.world.level.block.entity.signal.SignalCapacitorBoxBlockEntity;
import mods.railcraft.world.level.block.entity.signal.SignalControllerBoxBlockEntity;
import mods.railcraft.world.level.block.track.ForceTrackBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.GrassColor;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.client.event.RenderLevelLastEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class ClientDist implements RailcraftDist {

  private final Minecraft minecraft;
  private final ShuntingAuraRenderer shuntingAuraRenderer;

  public ClientDist() {
    IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
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
  // Client Mod Events
  // ================================================================================

  private void handleClientSetup(FMLClientSetupEvent event) {
    RenderLayers.register();

    // === Menu Screens ===

    MenuScreens.register(RailcraftMenuTypes.CREATIVE_LOCOMOTIVE.get(),
        CreativeLocomotiveScreen::new);
    MenuScreens.register(RailcraftMenuTypes.ELECTRIC_LOCOMOTIVE.get(),
        ElectricLocomotiveScreen::new);
    MenuScreens.register(RailcraftMenuTypes.STEAM_LOCOMOTIVE.get(),
        SteamLocomotiveScreen::new);
    MenuScreens.register(RailcraftMenuTypes.MANUAL_ROLLING_MACHINE.get(),
        ManualRollingMachineScreen::new);
    MenuScreens.register(RailcraftMenuTypes.COKE_OVEN.get(),
        CokeOvenMenuScreen::new);
    MenuScreens.register(RailcraftMenuTypes.ITEM_MANIPULATOR.get(),
        ItemManipulatorScreen::new);
    MenuScreens.register(RailcraftMenuTypes.FLUID_MANIPULATOR.get(),
        FluidManipulatorScreen::new);
    MenuScreens.register(RailcraftMenuTypes.TANK_MINECART.get(),
        TankMinecartScreen::new);
    MenuScreens.register(RailcraftMenuTypes.TUNNEL_BORE.get(), TunnelBoreScreen::new);
  }

  private void handleBlockColors(ColorHandlerEvent.Block event) {
    BlockColors blockColors = event.getBlockColors();
    blockColors.register(
        (state, worldIn, pos,
            tintIndex) -> state.getValue(ForceTrackEmitterBlock.COLOR).getMaterialColor().col,
        RailcraftBlocks.FORCE_TRACK_EMITTER.get());

    blockColors.register(
        (state, worldIn, pos,
            tintIndex) -> state.getValue(ForceTrackBlock.COLOR).getMaterialColor().col,
        RailcraftBlocks.FORCE_TRACK.get());

    blockColors.register(
        (state, level, pos, tintIndex) -> level != null && pos != null
            ? BiomeColors.getAverageGrassColor(level, pos)
            : GrassColor.get(0.5D, 1.0D),
        RailcraftBlocks.ABANDONED_TRACK.get());
  }

  private void handleItemColors(ColorHandlerEvent.Item event) {
    event.getItemColors().register(
        (stack, tintIndex) -> {
          switch (tintIndex) {
            case 0:
              return LocomotiveItem.getPrimaryColor(stack).getMaterialColor().col;
            case 1:
              return LocomotiveItem.getSecondaryColor(stack).getMaterialColor().col;
            default:
              return 0xFFFFFFFF;
          }
        },
        RailcraftItems.CREATIVE_LOCOMOTIVE.get(),
        RailcraftItems.STEAM_LOCOMOTIVE.get(),
        RailcraftItems.ELECTRIC_LOCOMOTIVE.get());
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

  private void handleParticleRegistration(ParticleFactoryRegisterEvent event) {
    var particleEngine = this.minecraft.particleEngine;
    particleEngine.register(RailcraftParticleTypes.STEAM.get(),
        SteamParticle.Provider::new);
    particleEngine.register(RailcraftParticleTypes.SPARK.get(),
        SparkParticle.Provider::new);
    particleEngine.register(RailcraftParticleTypes.PUMPKIN.get(),
        PumpkinParticle.Provider::new);
    particleEngine.register(RailcraftParticleTypes.TUNING_AURA.get(),
        TuningAuraParticle.Provider::new);
  }

  private void handleRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
    RailcraftEntityRenderers.register(event);
    RailcraftBlockEntityRenderers.register(event);
  }

  private void handleRegisterLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
    RailcraftLayerDefinitions.createRoots(event::registerLayerDefinition);
  }

  // ================================================================================
  // Client Forge Events
  // ================================================================================

  @SubscribeEvent
  public void handleClientTick(TickEvent.ClientTickEvent event) {
    if (event.phase == Phase.START
        && (this.minecraft.level != null && !this.minecraft.isPaused())) {
      SignalAspect.tickBlinkState();
    }
  }

  @SubscribeEvent
  public void handleRenderWorldLast(RenderLevelLastEvent event) {
    this.shuntingAuraRenderer.render(event.getPartialTick(), event.getPoseStack());
  }

  @SubscribeEvent
  public void handleClientLoggedOut(ClientPlayerNetworkEvent.LoggedOutEvent event) {
    this.shuntingAuraRenderer.clearCarts();
  }

  // ================================================================================
  // Bouncer Methods (required to avoid loading client only classes on wrong dist)
  // ================================================================================

  public static void openSignalControllerBoxScreen(SignalControllerBoxBlockEntity signalBox) {
    Minecraft.getInstance().setScreen(new SignalControllerBoxScreen(signalBox));
  }

  public static void openAnalogSignalControllerBoxScreen(
      AnalogSignalControllerBoxBlockEntity signalBox) {
    Minecraft.getInstance().setScreen(new AnalogSignalControllerBoxScreen(signalBox));
  }

  public static void openSignalCapacitorBoxScreen(SignalCapacitorBoxBlockEntity signalBox) {
    Minecraft.getInstance().setScreen(new SignalCapacitorBoxScreen(signalBox));
  }

  public static void openActionSignalBoxScreen(ActionSignalBoxBlockEntity signalBox) {
    Minecraft.getInstance().setScreen(new ActionSignalBoxScreen(signalBox));
  }

  public static void openSwitchTrackMotorScreen(SwitchTrackMotorBlockEntity switchTrackMotor) {
    Minecraft.getInstance().setScreen(new SwitchTrackMotorScreen(switchTrackMotor));
  }
}
