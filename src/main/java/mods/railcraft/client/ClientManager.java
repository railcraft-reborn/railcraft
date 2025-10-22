package mods.railcraft.client;

import java.net.URI;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector4f;
import dev.lambdaurora.lambdynlights.api.DynamicLightHandlers;
import mods.railcraft.Railcraft;
import mods.railcraft.RailcraftConfig;
import mods.railcraft.Translations;
import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.api.signal.SignalAspect;
import mods.railcraft.api.signal.SignalUtil;
import mods.railcraft.client.color.item.LocomotiveColor;
import mods.railcraft.client.gui.screen.inventory.BlastFurnaceScreen;
import mods.railcraft.client.gui.screen.inventory.CartDispenserScreen;
import mods.railcraft.client.gui.screen.inventory.CokeOvenScreen;
import mods.railcraft.client.gui.screen.inventory.CreativeLocomotiveScreen;
import mods.railcraft.client.gui.screen.inventory.CrusherScreen;
import mods.railcraft.client.gui.screen.inventory.DumpingTrackScreen;
import mods.railcraft.client.gui.screen.inventory.ElectricLocomotiveScreen;
import mods.railcraft.client.gui.screen.inventory.EnergyMinecartScreen;
import mods.railcraft.client.gui.screen.inventory.FeedStationScreen;
import mods.railcraft.client.gui.screen.inventory.FluidFueledSteamBoilerScreen;
import mods.railcraft.client.gui.screen.inventory.FluidManipulatorScreen;
import mods.railcraft.client.gui.screen.inventory.ItemManipulatorScreen;
import mods.railcraft.client.gui.screen.inventory.ManualRollingMachineScreen;
import mods.railcraft.client.gui.screen.inventory.PoweredRollingMachineScreen;
import mods.railcraft.client.gui.screen.inventory.RoutingDetectorScreen;
import mods.railcraft.client.gui.screen.inventory.RoutingTrackScreen;
import mods.railcraft.client.gui.screen.inventory.SolidFueledSteamBoilerScreen;
import mods.railcraft.client.gui.screen.inventory.SteamLocomotiveScreen;
import mods.railcraft.client.gui.screen.inventory.SteamOvenScreen;
import mods.railcraft.client.gui.screen.inventory.SteamTurbineScreen;
import mods.railcraft.client.gui.screen.inventory.SwitchTrackRouterScreen;
import mods.railcraft.client.gui.screen.inventory.TankMinecartScreen;
import mods.railcraft.client.gui.screen.inventory.TankScreen;
import mods.railcraft.client.gui.screen.inventory.TrackLayerScreen;
import mods.railcraft.client.gui.screen.inventory.TrackRelayerScreen;
import mods.railcraft.client.gui.screen.inventory.TrackUndercutterScreen;
import mods.railcraft.client.gui.screen.inventory.TrainDispenserScreen;
import mods.railcraft.client.gui.screen.inventory.TunnelBoreScreen;
import mods.railcraft.client.gui.screen.inventory.WaterTankSidingScreen;
import mods.railcraft.client.gui.screen.inventory.detector.AdvancedDetectorScreen;
import mods.railcraft.client.gui.screen.inventory.detector.ItemDetectorScreen;
import mods.railcraft.client.gui.screen.inventory.detector.LocomotiveDetectorScreen;
import mods.railcraft.client.gui.screen.inventory.detector.SheepDetectorScreen;
import mods.railcraft.client.gui.screen.inventory.detector.TankDetectorScreen;
import mods.railcraft.client.model.RailcraftLayerDefinitions;
import mods.railcraft.client.particle.ChimneyParticle;
import mods.railcraft.client.particle.ChunkLoaderParticle;
import mods.railcraft.client.particle.FireSparkParticle;
import mods.railcraft.client.particle.ForceSpawnParticle;
import mods.railcraft.client.particle.PumpkinParticle;
import mods.railcraft.client.particle.SparkParticle;
import mods.railcraft.client.particle.SteamParticle;
import mods.railcraft.client.particle.TuningAuraParticle;
import mods.railcraft.client.renderer.ShuntingAuraRenderer;
import mods.railcraft.client.renderer.blockentity.RailcraftBlockEntityRenderers;
import mods.railcraft.client.renderer.entity.RailcraftEntityRenderers;
import mods.railcraft.integrations.jei.JeiRecipeSync;
import mods.railcraft.integrations.patchouli.Patchouli;
import mods.railcraft.network.to_server.SetLocomotiveByKeyMessage;
import mods.railcraft.particle.RailcraftParticleTypes;
import mods.railcraft.world.entity.RailcraftEntityTypes;
import mods.railcraft.world.inventory.RailcraftMenuTypes;
import mods.railcraft.world.item.GogglesItem;
import mods.railcraft.world.item.component.RailcraftDataComponents;
import mods.railcraft.world.item.crafting.RailcraftRecipeTypes;
import mods.railcraft.world.level.block.ForceTrackEmitterBlock;
import mods.railcraft.world.level.block.RailcraftBlocks;
import mods.railcraft.world.level.block.track.ForceTrackBlock;
import mods.railcraft.world.level.material.RailcraftFluidTypes;
import net.minecraft.ChatFormatting;
import net.minecraft.SharedConstants;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.client.renderer.fog.FogData;
import net.minecraft.client.renderer.fog.environment.FogEnvironment;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.vehicle.Minecart;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.GrassColor;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.HitResult;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.fml.VersionChecker;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.loading.moddiscovery.ModFileInfo;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.client.event.RecipesReceivedEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import net.neoforged.neoforge.client.extensions.common.IClientBlockExtensions;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;
import vazkii.patchouli.api.PatchouliAPI;

public class ClientManager {

  private static final ShuntingAuraRenderer shuntingAuraRenderer = new ShuntingAuraRenderer();

  public static void init(IEventBus modEventBus) {
    modEventBus.addListener(ClientManager::handleRegisterMenuScreens);
    modEventBus.addListener(ClientManager::handleClientSetup);
    modEventBus.addListener(ClientManager::registerItemTintSources);
    modEventBus.addListener(ClientManager::handleBlockColors);
    modEventBus.addListener(ClientManager::handleParticleRegistration);
    modEventBus.addListener(ClientManager::handleRegisterRenderers);
    modEventBus.addListener(ClientManager::handleRegisterLayerDefinitions);
    modEventBus.addListener(ClientManager::handleKeyRegister);
    modEventBus.addListener(ClientManager::handleClientExtensions);
    NeoForge.EVENT_BUS.register(ClientManager.class);

    SignalUtil._setTuningAuraHandler(new TuningAuraHandlerImpl());
  }

  public static ShuntingAuraRenderer getShuntingAuraRenderer() {
    return shuntingAuraRenderer;
  }

  // ================================================================================
  // Mod Events
  // ================================================================================

  private static void handleRegisterMenuScreens(RegisterMenuScreensEvent event) {
    event.register(RailcraftMenuTypes.SOLID_FUELED_STEAM_BOILER.get(),
        SolidFueledSteamBoilerScreen::new);
    event.register(RailcraftMenuTypes.FLUID_FUELED_STEAM_BOILER.get(),
        FluidFueledSteamBoilerScreen::new);
    event.register(RailcraftMenuTypes.STEAM_TURBINE.get(), SteamTurbineScreen::new);
    event.register(RailcraftMenuTypes.TANK.get(), TankScreen::new);
    event.register(RailcraftMenuTypes.WATER_TANK_SIDING.get(), WaterTankSidingScreen::new);
    event.register(RailcraftMenuTypes.TRACK_LAYER.get(), TrackLayerScreen::new);
    event.register(RailcraftMenuTypes.TRACK_RELAYER.get(), TrackRelayerScreen::new);
    event.register(RailcraftMenuTypes.TRACK_UNDERCUTTER.get(), TrackUndercutterScreen::new);
    event.register(RailcraftMenuTypes.BLAST_FURNACE.get(), BlastFurnaceScreen::new);
    event.register(RailcraftMenuTypes.FEED_STATION.get(), FeedStationScreen::new);
    event.register(RailcraftMenuTypes.CREATIVE_LOCOMOTIVE.get(), CreativeLocomotiveScreen::new);
    event.register(RailcraftMenuTypes.ELECTRIC_LOCOMOTIVE.get(), ElectricLocomotiveScreen::new);
    event.register(RailcraftMenuTypes.STEAM_LOCOMOTIVE.get(), SteamLocomotiveScreen::new);
    event.register(RailcraftMenuTypes.MANUAL_ROLLING_MACHINE.get(),
        ManualRollingMachineScreen::new);
    event.register(RailcraftMenuTypes.POWERED_ROLLING_MACHINE.get(),
        PoweredRollingMachineScreen::new);
    event.register(RailcraftMenuTypes.COKE_OVEN.get(), CokeOvenScreen::new);
    event.register(RailcraftMenuTypes.CRUSHER.get(), CrusherScreen::new);
    event.register(RailcraftMenuTypes.STEAM_OVEN.get(), SteamOvenScreen::new);
    event.register(RailcraftMenuTypes.ITEM_MANIPULATOR.get(), ItemManipulatorScreen::new);
    event.register(RailcraftMenuTypes.FLUID_MANIPULATOR.get(), FluidManipulatorScreen::new);
    event.register(RailcraftMenuTypes.CART_DISPENSER.get(), CartDispenserScreen::new);
    event.register(RailcraftMenuTypes.TRAIN_DISPENSER.get(), TrainDispenserScreen::new);
    event.register(RailcraftMenuTypes.TANK_MINECART.get(), TankMinecartScreen::new);
    event.register(RailcraftMenuTypes.ENERGY_MINECART.get(), EnergyMinecartScreen::new);
    event.register(RailcraftMenuTypes.SWITCH_TRACK_ROUTER.get(), SwitchTrackRouterScreen::new);
    event.register(RailcraftMenuTypes.TUNNEL_BORE.get(), TunnelBoreScreen::new);
    event.register(RailcraftMenuTypes.ROUTING_TRACK.get(), RoutingTrackScreen::new);
    event.register(RailcraftMenuTypes.DUMPING_TRACK.get(), DumpingTrackScreen::new);
    event.register(RailcraftMenuTypes.SHEEP_DETECTOR.get(), SheepDetectorScreen::new);
    event.register(RailcraftMenuTypes.LOCOMOTIVE_DETECTOR.get(), LocomotiveDetectorScreen::new);
    event.register(RailcraftMenuTypes.TANK_DETECTOR.get(), TankDetectorScreen::new);
    event.register(RailcraftMenuTypes.ADVANCED_DETECTOR.get(), AdvancedDetectorScreen::new);
    event.register(RailcraftMenuTypes.ITEM_DETECTOR.get(), ItemDetectorScreen::new);
    event.register(RailcraftMenuTypes.ROUTING_DETECTOR.get(), RoutingDetectorScreen::new);
  }

  private static void handleClientSetup(FMLClientSetupEvent event) {
    if (ModList.get().isLoaded(PatchouliAPI.MOD_ID)) {
      Patchouli.setup();
    }
    if (ModList.get().isLoaded("sodiumdynamiclights")) {
      DynamicLightHandlers.registerDynamicLightHandler(RailcraftEntityTypes.CREATIVE_LOCOMOTIVE.get(),
          loco -> loco.isRunning() ? 12 : 0);
      DynamicLightHandlers.registerDynamicLightHandler(RailcraftEntityTypes.ELECTRIC_LOCOMOTIVE.get(),
          loco -> loco.isRunning() ? Math.round(loco.getLightLevel()) : 0);
    }
  }

  private static void registerItemTintSources(RegisterColorHandlersEvent.ItemTintSources event) {
    event.register(RailcraftConstants.rl("locomotive_color"), LocomotiveColor.MAP_CODEC);
  }

  private static void handleBlockColors(RegisterColorHandlersEvent.Block event) {
    event.register((state, level, pos, tintIndex) ->
            state.getValue(ForceTrackEmitterBlock.COLOR).getMapColor().col,
        RailcraftBlocks.FORCE_TRACK_EMITTER.get());

    event.register((state, level, pos, tintIndex) ->
            state.getValue(ForceTrackBlock.COLOR).getMapColor().col,
        RailcraftBlocks.FORCE_TRACK.get());

    event.register((state, level, pos, tintIndex) -> level != null && pos != null
            ? BiomeColors.getAverageGrassColor(level, pos)
            : GrassColor.get(0.5D, 1.0D),
        RailcraftBlocks.ABANDONED_TRACK.get());
  }

  private static void handleParticleRegistration(RegisterParticleProvidersEvent event) {
    event.registerSpriteSet(RailcraftParticleTypes.STEAM.get(), SteamParticle.Provider::new);
    event.registerSpriteSet(RailcraftParticleTypes.SPARK.get(), SparkParticle.Provider::new);
    event.registerSpriteSet(RailcraftParticleTypes.PUMPKIN.get(), PumpkinParticle.Provider::new);
    event.registerSpriteSet(RailcraftParticleTypes.TUNING_AURA.get(),
        TuningAuraParticle.Provider::new);
    event.registerSpriteSet(RailcraftParticleTypes.FIRE_SPARK.get(),
        FireSparkParticle.Provider::new);
    event.registerSpriteSet(RailcraftParticleTypes.FORCE_SPAWN.get(),
        ForceSpawnParticle.Provider::new);
    event.registerSpriteSet(RailcraftParticleTypes.CHIMNEY.get(),
        ChimneyParticle.Provider::new);
    event.registerSpriteSet(RailcraftParticleTypes.CHUNK_LOADER.get(),
        ChunkLoaderParticle.Provider::new);
  }

  private static void handleRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
    RailcraftEntityRenderers.register(event);
    RailcraftBlockEntityRenderers.register(event);
  }

  private static void handleRegisterLayerDefinitions(
      EntityRenderersEvent.RegisterLayerDefinitions event) {
    RailcraftLayerDefinitions.createRoots(event::registerLayerDefinition);
  }

  private static void handleKeyRegister(RegisterKeyMappingsEvent event) {
    for (var keyBinding : KeyBinding.values()) {
      event.register(keyBinding.getKeyMapping());
    }
    event.registerCategory(RailcraftKeyMappingCategory.DEFAULT);
  }

  private static void handleClientExtensions(RegisterClientExtensionsEvent event) {
    event.registerBlock(new IClientBlockExtensions() {
      @Override
      public boolean addDestroyEffects(BlockState state, Level level, BlockPos pos,
          ParticleEngine particleEngine) {
        return true;
      }

      @Override
      public boolean addHitEffects(BlockState state, Level level, @Nullable HitResult result,
          ParticleEngine particleEngine) {
        return true;
      }
    }, RailcraftBlocks.RITUAL.get());

    event.registerFluidType(new IClientFluidTypeExtensions() {
      private static final ResourceLocation STILL_TEXTURE =
          RailcraftConstants.rl("block/steam_still");

      @Override
      public int getTintColor() {
        return 0xFFF5F5F5;
      }

      @Override
      public ResourceLocation getStillTexture() {
        return STILL_TEXTURE;
      }

      @Override
      public ResourceLocation getFlowingTexture() {
        return STILL_TEXTURE;
      }
    }, RailcraftFluidTypes.STEAM.get());

    event.registerFluidType(new IClientFluidTypeExtensions() {
      private static final ResourceLocation STILL_TEXTURE =
          ResourceLocation.withDefaultNamespace("block/water_still");
      private static final ResourceLocation FLOW_TEXTURE =
          ResourceLocation.withDefaultNamespace("block/water_flow");

      @Override
      public int getTintColor() {
        return 0xFF6A6200;
      }

      @Override
      public ResourceLocation getStillTexture() {
        return STILL_TEXTURE;
      }

      @Override
      public ResourceLocation getFlowingTexture() {
        return FLOW_TEXTURE;
      }

      @Override
      public Vector4f modifyFogColor(Camera camera, float partialTick,
          ClientLevel level, int renderDistance, float darkenWorldAmount, Vector4f fluidFogColor) {
        var x = Integer.parseInt("6A", 16) / 255f;
        var y = Integer.parseInt("62", 16) / 255f;
        var z = Integer.parseInt("00", 16) / 255f;
        return new Vector4f(x, y, z, fluidFogColor.w());
      }

      @Override
      public void modifyFogRender(Camera camera, @Nullable FogEnvironment environment,
          float renderDistance, float partialTick, FogData fogData) {
        fogData.renderDistanceStart = 0;
        fogData.renderDistanceEnd = 3f;
      }
    }, RailcraftFluidTypes.CREOSOTE.get());
  }

  // ================================================================================
  // NeoForge Events
  // ================================================================================

  @SubscribeEvent
  static void handleClientTick(ClientTickEvent.Pre event) {
    if (Minecraft.getInstance().level != null && !Minecraft.getInstance().isPaused()) {
      SignalAspect.tickBlinkState();
    }
  }

  @SubscribeEvent
  static void handleRenderWorldLast(RenderLevelStageEvent.AfterEntities event) {
    shuntingAuraRenderer.render(event.getPoseStack(), event.getLevelRenderState().cameraRenderState,
        Minecraft.getInstance().getDeltaTracker().getGameTimeDeltaPartialTick(false));
  }

  @SubscribeEvent
  static void handleClientLoggedOut(ClientPlayerNetworkEvent.LoggingOut event) {
    shuntingAuraRenderer.clearCarts();
    JeiRecipeSync.clearAll();
  }

  @SuppressWarnings("unused")
  @SubscribeEvent
  static void handleClientLoggedIn(ClientPlayerNetworkEvent.LoggingIn event) {
    var modInfo = ModList.get().getModFileById(RailcraftConstants.ID).getMods().getFirst();
    var result = VersionChecker.getResult(modInfo);
    var versionStatus = result.status();

    if (versionStatus.shouldDraw()) {
      var newVersion = result.target().toString();
      var modUrl = modInfo.getModURL().orElseThrow().toString();
      var message = Component.literal(RailcraftConstants.NAME + ": ").withStyle(ChatFormatting.GREEN)
          .append(Component.literal(
              "A new version (%s) is available to download.".formatted(newVersion))
              .withStyle(style -> style
                  .withColor(ChatFormatting.WHITE)
                  .withUnderlined(true)
                  .withClickEvent(new ClickEvent.OpenUrl(URI.create(modUrl)))));
      event.getPlayer().displayClientMessage(message, false);
    }

    var qualifier = modInfo.getVersion().getQualifier();
    boolean isSnapshot = qualifier != null && qualifier.equals("snapshot");
    boolean showMessageBeta = Railcraft.BETA && RailcraftConfig.CLIENT.showBetaMessage.get();
    if (SharedConstants.IS_RUNNING_IN_IDE || isSnapshot || showMessageBeta) {
      var type = isSnapshot ? "development" : "beta";
      var issueUrl = ((ModFileInfo) (modInfo.getOwningFile())).getIssueURL().toString();
      var message = CommonComponents.joinLines(
          Component.literal("You are using a %s version of %s.".formatted(type, RailcraftConstants.NAME))
              .withStyle(ChatFormatting.RED),
        /*Component.literal("- World saves are not stable and may break between versions.")
            .withStyle(ChatFormatting.GRAY),*/
          Component.literal("- Features might be missing or only partially implemented.")
              .withStyle(ChatFormatting.GRAY),
        /*Component.literal("You have been warned.")
            .withStyle(ChatFormatting.RED, ChatFormatting.ITALIC),*/
          Component.literal("Bug reports are welcome at our issue tracker.")
              .withStyle(style -> style
                  .withColor(ChatFormatting.GREEN)
                  .withUnderlined(true)
                  .withClickEvent(new ClickEvent.OpenUrl(URI.create(issueUrl)))),
          Component.literal("- Sm0keySa1m0n, Edivad99")
              .withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
      event.getPlayer().displayClientMessage(message, false);
    }

    var minecartImprovedFeatureFlag =
        event.getPlayer().level().enabledFeatures().contains(FeatureFlags.MINECART_IMPROVEMENTS);
    if (minecartImprovedFeatureFlag) {
      var message = CommonComponents.joinLines(
          Component.literal("The 'Minecart Improvements' experiment is enabled.")
              .withStyle(ChatFormatting.GOLD, ChatFormatting.UNDERLINE),
          Component.literal("Some Railcraft features may not work as intended.")
              .withStyle(ChatFormatting.YELLOW)
      );
      event.getPlayer().displayClientMessage(message, false);
    }
  }

  @SubscribeEvent
  static void handleItemTooltip(ItemTooltipEvent event) {
    var itemStack = event.getItemStack();
    var clickToCraft = itemStack.getOrDefault(RailcraftDataComponents.CLICK_TO_CRAFT, false);
    if (clickToCraft) {
      event.getToolTip().add(Component.translatable(Translations.Tips.CLICK_TO_CRAFT)
          .withStyle(ChatFormatting.YELLOW));
    }
  }

  @SubscribeEvent
  static void handleRecipesReceived(RecipesReceivedEvent event) {
    JeiRecipeSync.setBlastFurnaceRecipes(
        event.getRecipeMap().byType(RailcraftRecipeTypes.BLASTING.get()));
    JeiRecipeSync.setRollingRecipes(
        event.getRecipeMap().byType(RailcraftRecipeTypes.ROLLING.get()));
    JeiRecipeSync.setCokingRecipes(
        event.getRecipeMap().byType(RailcraftRecipeTypes.COKING.get()));
    JeiRecipeSync.setCrushingRecipes(
        event.getRecipeMap().byType(RailcraftRecipeTypes.CRUSHING.get()));
  }

  @SubscribeEvent
  static void handleKeyInput(InputEvent.Key event) {
    var player = Minecraft.getInstance().player;
    if (player == null) {
      return;
    }
    if (Minecraft.getInstance().screen instanceof ChatScreen) {
      return;
    }

    if (KeyBinding.CHANGE_AURA.consumeClick()) {
      GogglesItem.changeAuraByKey(player);
    }
    // Locomotive Keybindings
    if (!(player.getVehicle() instanceof Minecart)) {
      return;
    }
    if (KeyBinding.REVERSE.consumeClick()) {
      ClientPacketDistributor.sendToServer(
          new SetLocomotiveByKeyMessage(SetLocomotiveByKeyMessage.LocomotiveKeyBinding.REVERSE));
    }
    if (KeyBinding.FASTER.consumeClick()) {
      ClientPacketDistributor.sendToServer(
          new SetLocomotiveByKeyMessage(SetLocomotiveByKeyMessage.LocomotiveKeyBinding.FASTER));
    }
    if (KeyBinding.SLOWER.consumeClick()) {
      ClientPacketDistributor.sendToServer(
          new SetLocomotiveByKeyMessage(SetLocomotiveByKeyMessage.LocomotiveKeyBinding.SLOWER));
    }
    if (KeyBinding.MODE_CHANGE.consumeClick()) {
      ClientPacketDistributor.sendToServer(
          new SetLocomotiveByKeyMessage(SetLocomotiveByKeyMessage.LocomotiveKeyBinding.MODE_CHANGE));
    }
    if (KeyBinding.WHISTLE.consumeClick()) {
      ClientPacketDistributor.sendToServer(
          new SetLocomotiveByKeyMessage(SetLocomotiveByKeyMessage.LocomotiveKeyBinding.WHISTLE));
    }
  }
}
