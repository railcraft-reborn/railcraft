package mods.railcraft.client;

import org.apache.commons.lang3.tuple.Pair;

import mods.railcraft.IRailcraftDist;
import mods.railcraft.Railcraft;
import mods.railcraft.api.signals.SignalAspect;
import mods.railcraft.client.gui.screen.inventory.CreativeLocomotiveScreen;
import mods.railcraft.client.gui.screen.inventory.ElectricLocomotiveScreen;
import mods.railcraft.client.gui.screen.inventory.RollingTableScreen;
import mods.railcraft.client.gui.screen.inventory.SteamLocomotiveScreen;
import mods.railcraft.client.particle.ParticlePumpkin;
import mods.railcraft.client.particle.ParticleSpark;
import mods.railcraft.client.particle.ParticleSteam;
import mods.railcraft.client.renderer.blockentity.AbstractSignalBoxRenderer;
import mods.railcraft.client.renderer.blockentity.AbstractSignalRenderer;
import mods.railcraft.client.renderer.blockentity.DualSignalRenderer;
import mods.railcraft.client.renderer.blockentity.SignalCapacitorBoxRenderer;
import mods.railcraft.client.renderer.blockentity.SignalControllerBoxRenderer;
import mods.railcraft.client.renderer.blockentity.SignalReceiverBoxRenderer;
import mods.railcraft.client.renderer.blockentity.SignalRelayBoxRenderer;
import mods.railcraft.client.renderer.blockentity.SignalRenderer;
import mods.railcraft.client.renderer.entity.cart.ElectricLocomotiveRenderer;
import mods.railcraft.client.renderer.entity.cart.SteamLocomotiveRenderer;
import mods.railcraft.client.renderer.model.TextureReplacementModel;
import mods.railcraft.particle.RailcraftParticles;
import mods.railcraft.plugins.WorldPlugin;
import mods.railcraft.world.entity.RailcraftEntityTypes;
import mods.railcraft.world.inventory.RailcraftMenuTypes;
import mods.railcraft.world.item.LocomotiveItem;
import mods.railcraft.world.item.RailcraftItems;
import mods.railcraft.world.level.block.ForceTrackEmitterBlock;
import mods.railcraft.world.level.block.RailcraftBlocks;
import mods.railcraft.world.level.block.entity.ForceTrackEmitterBlockEntity;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntityTypes;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.GrassColors;
import net.minecraft.world.biome.BiomeColors;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class ClientDist implements IRailcraftDist {

  public static final ClientConfig clientConfig;
  public static final ForgeConfigSpec clientConfigSpec;

  static {
    final Pair<ClientConfig, ForgeConfigSpec> clientConfigPair =
        new ForgeConfigSpec.Builder().configure(ClientConfig::new);
    clientConfigSpec = clientConfigPair.getRight();
    clientConfig = clientConfigPair.getLeft();
  }

  private final Minecraft minecraft;

  public ClientDist() {
    ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, clientConfigSpec);

    IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
    modEventBus.addListener(this::handleClientSetup);
    modEventBus.addListener(this::handleItemColors);
    modEventBus.addListener(this::handleBlockColors);
    modEventBus.addListener(this::handleTextureStitch);
    modEventBus.addListener(this::handleModelRegistry);
    modEventBus.addListener(this::handleParticleRegistration);

    MinecraftForge.EVENT_BUS.register(this);

    this.minecraft = Minecraft.getInstance();
  }

  // ================================================================================
  // Client Mod Events
  // ================================================================================

  private void handleClientSetup(FMLClientSetupEvent event) {
    RenderTypeLookup.setRenderLayer(RailcraftBlocks.FIRESTONE.get(), RenderType.cutoutMipped());
    RenderTypeLookup.setRenderLayer(RailcraftBlocks.ELEVATOR_TRACK.get(), RenderType.cutout());
    RenderTypeLookup.setRenderLayer(RailcraftBlocks.REINFORCED_FLEX_TRACK.get(),
        RenderType.cutout());
    RenderTypeLookup.setRenderLayer(RailcraftBlocks.ABANDONED_FLEX_TRACK.get(),
        RenderType.cutout());
    RenderTypeLookup.setRenderLayer(RailcraftBlocks.ELECTRIC_FLEX_TRACK.get(),
        RenderType.cutout());
    RenderTypeLookup.setRenderLayer(RailcraftBlocks.HIGH_SPEED_FLEX_TRACK.get(),
        RenderType.cutout());
    RenderTypeLookup.setRenderLayer(RailcraftBlocks.HIGH_SPEED_ELECTRIC_FLEX_TRACK.get(),
        RenderType.cutout());
    RenderTypeLookup.setRenderLayer(RailcraftBlocks.STRAP_IRON_FLEX_TRACK.get(),
        RenderType.cutout());
    RenderTypeLookup.setRenderLayer(RailcraftBlocks.TURNOUT_TRACK.get(),
        RenderType.cutout());
    RenderTypeLookup.setRenderLayer(RailcraftBlocks.WYE_TRACK.get(),
        RenderType.cutout());
    RenderTypeLookup.setRenderLayer(RailcraftBlocks.FORCE_TRACK_EMITTER.get(),
        RenderType.cutout());
    RenderTypeLookup.setRenderLayer(RailcraftBlocks.SIGNAL.get(),
        RenderType.cutout());
    RenderTypeLookup.setRenderLayer(RailcraftBlocks.DISTANT_SIGNAL.get(),
        RenderType.cutout());
    RenderTypeLookup.setRenderLayer(RailcraftBlocks.TOKEN_SIGNAL.get(),
        RenderType.cutout());
    RenderTypeLookup.setRenderLayer(RailcraftBlocks.DUAL_SIGNAL.get(),
        RenderType.cutout());
    RenderTypeLookup.setRenderLayer(RailcraftBlocks.DUAL_DISTANT_SIGNAL.get(),
        RenderType.cutout());
    RenderTypeLookup.setRenderLayer(RailcraftBlocks.DUAL_TOKEN_SIGNAL.get(),
        RenderType.cutout());

    ClientRegistry.bindTileEntityRenderer(RailcraftBlockEntityTypes.SIGNAL.get(),
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
        SignalRelayBoxRenderer::new);

    ScreenManager.register(RailcraftMenuTypes.CREATIVE_LOCOMOTIVE.get(),
        CreativeLocomotiveScreen::new);
    ScreenManager.register(RailcraftMenuTypes.ELECTRIC_LOCOMOTIVE.get(),
        ElectricLocomotiveScreen::new);
    ScreenManager.register(RailcraftMenuTypes.STEAM_LOCOMOTIVE.get(), SteamLocomotiveScreen::new);

    ScreenManager.register(RailcraftMenuTypes.ROLLING_TABLE.get(), RollingTableScreen::new);

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
        .register((state, worldIn, pos, tintIndex) -> WorldPlugin
            .getTileEntity(worldIn, pos, ForceTrackEmitterBlockEntity.class)
            .map(ForceTrackEmitterBlockEntity::getColor)
            .orElse(ForceTrackEmitterBlock.DEFAULT_COLOR)
            .getColorValue(), RailcraftBlocks.FORCE_TRACK_EMITTER.get());

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
      event.addSprite(SignalRelayBoxRenderer.TEXTURE_LOCATION);
      event.addSprite(AbstractSignalBoxRenderer.BOTTOM_TEXTURE_LOCATION);
      event.addSprite(AbstractSignalBoxRenderer.CONNECTED_SIDE_TEXTURE_LOCATION);
      event.addSprite(AbstractSignalBoxRenderer.SIDE_TEXTURE_LOCATION);
    }
  }

  private void handleParticleRegistration(ParticleFactoryRegisterEvent event) {
    final ParticleManager particleEngine = this.minecraft.particleEngine;
    particleEngine.register(RailcraftParticles.STEAM.get(), ParticleSteam.SteamParticleFactory::new);
    particleEngine.register(RailcraftParticles.SPARK.get(), ParticleSpark.SparkParticleFactory::new);
    particleEngine.register(RailcraftParticles.PUMPKIN.get(), ParticlePumpkin.PumpkinParticleFactory::new);
  }

  // ================================================================================
  // Client Forge Events
  // ================================================================================

  @SubscribeEvent
  public void handleClientTick(TickEvent.ClientTickEvent event) {
    if (event.phase == Phase.START
      && (this.minecraft.level != null && !this.minecraft.isPaused())) {
        // switch this to a switch if we have more args to go about
        SignalAspect.tickBlinkState();
    }
  }
}
