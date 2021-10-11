package mods.railcraft.client;

import mods.railcraft.Railcraft;
import mods.railcraft.RailcraftDist;
import mods.railcraft.api.signal.SignalAspect;
import mods.railcraft.client.gui.screen.ActionSignalBoxScreen;
import mods.railcraft.client.gui.screen.AnalogSignalControllerBoxScreen;
import mods.railcraft.client.gui.screen.SignalCapacitorBoxScreen;
import mods.railcraft.client.gui.screen.SignalControllerBoxScreen;
import mods.railcraft.client.gui.screen.SwitchTrackMotorScreen;
import mods.railcraft.client.gui.screen.inventory.CreativeLocomotiveScreen;
import mods.railcraft.client.gui.screen.inventory.ElectricLocomotiveScreen;
import mods.railcraft.client.gui.screen.inventory.ManualRollingMachineScreen;
import mods.railcraft.client.gui.screen.inventory.SteamLocomotiveScreen;
import mods.railcraft.client.particle.ParticlePumpkin;
import mods.railcraft.client.particle.ParticleSpark;
import mods.railcraft.client.particle.ParticleSteam;
import mods.railcraft.client.renderer.ShuntingAuraRenderer;
import mods.railcraft.client.renderer.blockentity.AbstractSignalBoxRenderer;
import mods.railcraft.client.renderer.blockentity.AbstractSignalRenderer;
import mods.railcraft.client.renderer.blockentity.AnalogSignalControllerBoxRenderer;
import mods.railcraft.client.renderer.blockentity.BlockSignalRelayBoxRenderer;
import mods.railcraft.client.renderer.blockentity.DualSignalRenderer;
import mods.railcraft.client.renderer.blockentity.SignalCapacitorBoxRenderer;
import mods.railcraft.client.renderer.blockentity.SignalControllerBoxRenderer;
import mods.railcraft.client.renderer.blockentity.SignalInterlockBoxRenderer;
import mods.railcraft.client.renderer.blockentity.SignalReceiverBoxRenderer;
import mods.railcraft.client.renderer.blockentity.SignalRenderer;
import mods.railcraft.client.renderer.blockentity.SignalSequencerBoxRenderer;
import mods.railcraft.client.renderer.entity.cart.ElectricLocomotiveRenderer;
import mods.railcraft.client.renderer.entity.cart.SteamLocomotiveRenderer;
import mods.railcraft.client.renderer.model.TextureReplacementModel;
import mods.railcraft.particle.RailcraftParticles;
import mods.railcraft.world.entity.RailcraftEntityTypes;
import mods.railcraft.world.inventory.RailcraftMenuTypes;
import mods.railcraft.world.item.LocomotiveItem;
import mods.railcraft.world.item.RailcraftItems;
import mods.railcraft.world.level.block.ForceTrackEmitterBlock;
import mods.railcraft.world.level.block.RailcraftBlocks;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntityTypes;
import mods.railcraft.world.level.block.entity.SwitchTrackMotorBlockEntity;
import mods.railcraft.world.level.block.entity.signal.ActionSignalBoxBlockEntity;
import mods.railcraft.world.level.block.entity.signal.AnalogSignalControllerBoxBlockEntity;
import mods.railcraft.world.level.block.entity.signal.SignalCapacitorBoxBlockEntity;
import mods.railcraft.world.level.block.entity.signal.SignalControllerBoxBlockEntity;
import mods.railcraft.world.level.block.track.ForceTrackBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.GrassColors;
import net.minecraft.world.biome.BiomeColors;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
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
    modEventBus.addListener(this::handleModelRegistry);
    modEventBus.addListener(this::handleParticleRegistration);

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
    RenderTypeLookup.setRenderLayer(RailcraftBlocks.FIRESTONE.get(), RenderType.cutoutMipped());
    RenderTypeLookup.setRenderLayer(RailcraftBlocks.ELEVATOR_TRACK.get(), RenderType.cutout());
    RenderTypeLookup.setRenderLayer(RailcraftBlocks.FORCE_TRACK.get(),
        RenderType.cutout());
    RenderTypeLookup.setRenderLayer(RailcraftBlocks.ABANDONED_FLEX_TRACK.get(),
        RenderType.cutout());
    RenderTypeLookup.setRenderLayer(RailcraftBlocks.ABANDONED_LOCKING_TRACK.get(),
        RenderType.cutout());
    RenderTypeLookup.setRenderLayer(RailcraftBlocks.ABANDONED_BUFFER_STOP_TRACK.get(),
        RenderType.cutout());
    RenderTypeLookup.setRenderLayer(RailcraftBlocks.ABANDONED_ACTIVATOR_TRACK.get(),
        RenderType.cutout());
    RenderTypeLookup.setRenderLayer(RailcraftBlocks.ABANDONED_BOOSTER_TRACK.get(),
        RenderType.cutout());
    RenderTypeLookup.setRenderLayer(RailcraftBlocks.ELECTRIC_FLEX_TRACK.get(),
        RenderType.cutout());
    RenderTypeLookup.setRenderLayer(RailcraftBlocks.ELECTRIC_LOCKING_TRACK.get(),
        RenderType.cutout());
    RenderTypeLookup.setRenderLayer(RailcraftBlocks.ELECTRIC_BUFFER_STOP_TRACK.get(),
        RenderType.cutout());
    RenderTypeLookup.setRenderLayer(RailcraftBlocks.ELECTRIC_ACTIVATOR_TRACK.get(),
        RenderType.cutout());
    RenderTypeLookup.setRenderLayer(RailcraftBlocks.ELECTRIC_BOOSTER_TRACK.get(),
        RenderType.cutout());
    RenderTypeLookup.setRenderLayer(RailcraftBlocks.HIGH_SPEED_FLEX_TRACK.get(),
        RenderType.cutout());
    RenderTypeLookup.setRenderLayer(RailcraftBlocks.HIGH_SPEED_LOCKING_TRACK.get(),
        RenderType.cutout());
    RenderTypeLookup.setRenderLayer(RailcraftBlocks.HIGH_SPEED_BUFFER_STOP_TRACK.get(),
        RenderType.cutout());
    RenderTypeLookup.setRenderLayer(RailcraftBlocks.HIGH_SPEED_ACTIVATOR_TRACK.get(),
        RenderType.cutout());
    RenderTypeLookup.setRenderLayer(RailcraftBlocks.HIGH_SPEED_BOOSTER_TRACK.get(),
        RenderType.cutout());
    RenderTypeLookup.setRenderLayer(RailcraftBlocks.HIGH_SPEED_ELECTRIC_FLEX_TRACK.get(),
        RenderType.cutout());
    RenderTypeLookup.setRenderLayer(RailcraftBlocks.HIGH_SPEED_ELECTRIC_LOCKING_TRACK.get(),
        RenderType.cutout());
    RenderTypeLookup.setRenderLayer(RailcraftBlocks.HIGH_SPEED_ELECTRIC_BUFFER_STOP_TRACK.get(),
        RenderType.cutout());
    RenderTypeLookup.setRenderLayer(RailcraftBlocks.HIGH_SPEED_ELECTRIC_ACTIVATOR_TRACK.get(),
        RenderType.cutout());
    RenderTypeLookup.setRenderLayer(RailcraftBlocks.HIGH_SPEED_ELECTRIC_BOOSTER_TRACK.get(),
        RenderType.cutout());
    RenderTypeLookup.setRenderLayer(RailcraftBlocks.IRON_LOCKING_TRACK.get(),
        RenderType.cutout());
    RenderTypeLookup.setRenderLayer(RailcraftBlocks.IRON_BUFFER_STOP_TRACK.get(),
        RenderType.cutout());
    RenderTypeLookup.setRenderLayer(RailcraftBlocks.IRON_ACTIVATOR_TRACK.get(),
        RenderType.cutout());
    RenderTypeLookup.setRenderLayer(RailcraftBlocks.IRON_BOOSTER_TRACK.get(),
        RenderType.cutout());
    RenderTypeLookup.setRenderLayer(RailcraftBlocks.REINFORCED_FLEX_TRACK.get(),
        RenderType.cutout());
    RenderTypeLookup.setRenderLayer(RailcraftBlocks.REINFORCED_LOCKING_TRACK.get(),
        RenderType.cutout());
    RenderTypeLookup.setRenderLayer(RailcraftBlocks.REINFORCED_BUFFER_STOP_TRACK.get(),
        RenderType.cutout());
    RenderTypeLookup.setRenderLayer(RailcraftBlocks.REINFORCED_ACTIVATOR_TRACK.get(),
        RenderType.cutout());
    RenderTypeLookup.setRenderLayer(RailcraftBlocks.REINFORCED_BOOSTER_TRACK.get(),
        RenderType.cutout());
    RenderTypeLookup.setRenderLayer(RailcraftBlocks.STRAP_IRON_FLEX_TRACK.get(),
        RenderType.cutout());
    RenderTypeLookup.setRenderLayer(RailcraftBlocks.STRAP_IRON_LOCKING_TRACK.get(),
        RenderType.cutout());
    RenderTypeLookup.setRenderLayer(RailcraftBlocks.STRAP_IRON_BUFFER_STOP_TRACK.get(),
        RenderType.cutout());
    RenderTypeLookup.setRenderLayer(RailcraftBlocks.STRAP_IRON_ACTIVATOR_TRACK.get(),
        RenderType.cutout());
    RenderTypeLookup.setRenderLayer(RailcraftBlocks.STRAP_IRON_BOOSTER_TRACK.get(),
        RenderType.cutout());
    RenderTypeLookup.setRenderLayer(RailcraftBlocks.TURNOUT_TRACK.get(),
        RenderType.cutout());
    RenderTypeLookup.setRenderLayer(RailcraftBlocks.WYE_TRACK.get(),
        RenderType.cutout());
    RenderTypeLookup.setRenderLayer(RailcraftBlocks.FORCE_TRACK_EMITTER.get(),
        RenderType.cutout());
    RenderTypeLookup.setRenderLayer(RailcraftBlocks.BLOCK_SIGNAL.get(),
        RenderType.cutout());
    RenderTypeLookup.setRenderLayer(RailcraftBlocks.DISTANT_SIGNAL.get(),
        RenderType.cutout());
    RenderTypeLookup.setRenderLayer(RailcraftBlocks.TOKEN_SIGNAL.get(),
        RenderType.cutout());
    RenderTypeLookup.setRenderLayer(RailcraftBlocks.DUAL_BLOCK_SIGNAL.get(),
        RenderType.cutout());
    RenderTypeLookup.setRenderLayer(RailcraftBlocks.DUAL_DISTANT_SIGNAL.get(),
        RenderType.cutout());
    RenderTypeLookup.setRenderLayer(RailcraftBlocks.DUAL_TOKEN_SIGNAL.get(),
        RenderType.cutout());

    ClientRegistry.bindTileEntityRenderer(RailcraftBlockEntityTypes.BLOCK_SIGNAL.get(),
        SignalRenderer::new);
    ClientRegistry.bindTileEntityRenderer(RailcraftBlockEntityTypes.DISTANT_SIGNAL.get(),
        SignalRenderer::new);
    ClientRegistry.bindTileEntityRenderer(RailcraftBlockEntityTypes.TOKEN_SIGNAL.get(),
        SignalRenderer::new);
    ClientRegistry.bindTileEntityRenderer(RailcraftBlockEntityTypes.DUAL_SIGNAL.get(),
        DualSignalRenderer::new);
    ClientRegistry.bindTileEntityRenderer(RailcraftBlockEntityTypes.DUAL_DISTANT_SIGNAL.get(),
        DualSignalRenderer::new);
    ClientRegistry.bindTileEntityRenderer(RailcraftBlockEntityTypes.DUAL_TOKEN_SIGNAL.get(),
        DualSignalRenderer::new);
    ClientRegistry.bindTileEntityRenderer(RailcraftBlockEntityTypes.SIGNAL_CONTROLLER_BOX.get(),
        SignalControllerBoxRenderer::new);
    ClientRegistry.bindTileEntityRenderer(RailcraftBlockEntityTypes.SIGNAL_CAPACITOR_BOX.get(),
        SignalCapacitorBoxRenderer::new);
    ClientRegistry.bindTileEntityRenderer(RailcraftBlockEntityTypes.SIGNAL_RECEIVER_BOX.get(),
        SignalReceiverBoxRenderer::new);
    ClientRegistry.bindTileEntityRenderer(RailcraftBlockEntityTypes.SIGNAL_RELAY_BOX.get(),
        BlockSignalRelayBoxRenderer::new);
    ClientRegistry.bindTileEntityRenderer(
        RailcraftBlockEntityTypes.ANALOG_SIGNAL_CONTROLLER_BOX.get(),
        AnalogSignalControllerBoxRenderer::new);
    ClientRegistry.bindTileEntityRenderer(RailcraftBlockEntityTypes.SIGNAL_SEQUENCER_BOX.get(),
        SignalSequencerBoxRenderer::new);
    ClientRegistry.bindTileEntityRenderer(RailcraftBlockEntityTypes.SIGNAL_INTERLOCK_BOX.get(),
        SignalInterlockBoxRenderer::new);
    ScreenManager.register(RailcraftMenuTypes.CREATIVE_LOCOMOTIVE.get(),
        CreativeLocomotiveScreen::new);
    ScreenManager.register(RailcraftMenuTypes.ELECTRIC_LOCOMOTIVE.get(),
        ElectricLocomotiveScreen::new);
    ScreenManager.register(RailcraftMenuTypes.STEAM_LOCOMOTIVE.get(), SteamLocomotiveScreen::new);

    ScreenManager.register(RailcraftMenuTypes.MANUAL_ROLLING_MACHINE.get(),
        ManualRollingMachineScreen::new);

    RenderingRegistry.registerEntityRenderingHandler(RailcraftEntityTypes.CREATIVE_LOCOMOTIVE.get(),
        ElectricLocomotiveRenderer::new);
    RenderingRegistry.registerEntityRenderingHandler(RailcraftEntityTypes.STEAM_LOCOMOTIVE.get(),
        SteamLocomotiveRenderer::new);
    RenderingRegistry.registerEntityRenderingHandler(RailcraftEntityTypes.ELECTRIC_LOCOMOTIVE.get(),
        ElectricLocomotiveRenderer::new);
  }

  private void handleModelRegistry(ModelRegistryEvent event) {
    ModelLoaderRegistry.registerLoader(new ResourceLocation(Railcraft.ID, "texture_replacement"),
        new TextureReplacementModel.Loader());
  }

  private void handleBlockColors(ColorHandlerEvent.Block event) {
    event.getBlockColors()
        .register((state, worldIn, pos, tintIndex) -> state.getValue(ForceTrackEmitterBlock.COLOR)
            .getColorValue(), RailcraftBlocks.FORCE_TRACK_EMITTER.get());

    event.getBlockColors()
        .register((state, worldIn, pos, tintIndex) -> state.getValue(ForceTrackBlock.COLOR)
            .getColorValue(), RailcraftBlocks.FORCE_TRACK.get());

    event.getBlockColors().register(
        (state, level, pos, tintIndex) -> level != null && pos != null
            ? BiomeColors.getAverageGrassColor(level, pos)
            : GrassColors.get(0.5D, 1.0D),
        RailcraftBlocks.ABANDONED_FLEX_TRACK.get());
  }

  private void handleItemColors(ColorHandlerEvent.Item event) {
    event.getItemColors().register(
        (stack, tintIndex) -> {
          switch (tintIndex) {
            case 0:
              return LocomotiveItem.getPrimaryColor(stack).getColorValue();
            case 1:
              return LocomotiveItem.getSecondaryColor(stack).getColorValue();
            default:
              return 0xFFFFFFFF;
          }
        },
        RailcraftItems.CREATIVE_LOCOMOTIVE.get(),
        RailcraftItems.STEAM_LOCOMOTIVE.get(),
        RailcraftItems.ELECTRIC_LOCOMOTIVE.get());
  }

  private void handleTextureStitch(TextureStitchEvent.Pre event) {
    if (event.getMap().location().equals(PlayerContainer.BLOCK_ATLAS)) {
      AbstractSignalRenderer.ASPECT_TEXTURE_LOCATIONS.values().forEach(event::addSprite);
      AbstractSignalBoxRenderer.ASPECT_TEXTURE_LOCATIONS.values().forEach(event::addSprite);
      event.addSprite(SignalControllerBoxRenderer.TEXTURE_LOCATION);
      event.addSprite(SignalCapacitorBoxRenderer.TEXTURE_LOCATION);
      event.addSprite(SignalReceiverBoxRenderer.TEXTURE_LOCATION);
      event.addSprite(BlockSignalRelayBoxRenderer.TEXTURE_LOCATION);
      event.addSprite(AbstractSignalBoxRenderer.BOTTOM_TEXTURE_LOCATION);
      event.addSprite(AbstractSignalBoxRenderer.CONNECTED_SIDE_TEXTURE_LOCATION);
      event.addSprite(AbstractSignalBoxRenderer.SIDE_TEXTURE_LOCATION);
    }
  }

  private void handleParticleRegistration(ParticleFactoryRegisterEvent event) {
    final ParticleManager particleEngine = this.minecraft.particleEngine;
    particleEngine.register(RailcraftParticles.STEAM.get(),
        ParticleSteam.SteamParticleFactory::new);
    particleEngine.register(RailcraftParticles.SPARK.get(),
        ParticleSpark.SparkParticleFactory::new);
    particleEngine.register(RailcraftParticles.PUMPKIN.get(),
        ParticlePumpkin.PumpkinParticleFactory::new);
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
  public void handleRenderWorldLast(RenderWorldLastEvent event) {
    this.shuntingAuraRenderer.render(event.getPartialTicks(), event.getMatrixStack());
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
