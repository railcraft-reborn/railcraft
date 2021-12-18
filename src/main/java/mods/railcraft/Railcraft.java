package mods.railcraft;

import java.io.IOException;
import java.util.jar.JarFile;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mods.railcraft.advancements.RailcraftCriteriaTriggers;
import mods.railcraft.api.carts.CartUtil;
import mods.railcraft.api.event.CartLinkEvent;
import mods.railcraft.client.ClientDist;
import mods.railcraft.data.RailcraftAdvancementProviders;
import mods.railcraft.data.RailcraftBlockTagsProvider;
import mods.railcraft.data.RailcraftItemTagsProvider;
import mods.railcraft.data.RailcraftLootTableProvider;
import mods.railcraft.data.models.RailcraftModelProvider;
import mods.railcraft.data.recipes.RailcraftRecipeProvider;
import mods.railcraft.data.worldgen.RailcraftOrePlacements;
import mods.railcraft.network.NetworkChannel;
import mods.railcraft.network.RailcraftDataSerializers;
import mods.railcraft.network.play.LinkedCartsMessage;
import mods.railcraft.particle.RailcraftParticleTypes;
import mods.railcraft.server.ServerDist;
import mods.railcraft.sounds.RailcraftSoundEvents;
import mods.railcraft.util.EntitySearcher;
import mods.railcraft.world.entity.RailcraftEntityTypes;
import mods.railcraft.world.entity.vehicle.LinkageHandler;
import mods.railcraft.world.entity.vehicle.LinkageManagerImpl;
import mods.railcraft.world.entity.vehicle.MinecartHandler;
import mods.railcraft.world.entity.vehicle.RailcraftTrainTransferHelper;
import mods.railcraft.world.entity.vehicle.Train;
import mods.railcraft.world.inventory.RailcraftMenuTypes;
import mods.railcraft.world.item.CrowbarHandler;
import mods.railcraft.world.item.RailcraftItems;
import mods.railcraft.world.item.crafting.RailcraftRecipeSerializers;
import mods.railcraft.world.item.enchantment.RailcraftEnchantments;
import mods.railcraft.world.level.block.RailcraftBlocks;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntityTypes;
import mods.railcraft.world.level.block.track.TrackTypes;
import mods.railcraft.world.level.material.fluid.RailcraftFluids;
import mods.railcraft.world.signal.TokenRingManager;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;
import net.minecraftforge.event.TickEvent.WorldTickEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.EntityLeaveWorldEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.registries.DataSerializerEntry;

@Mod(Railcraft.ID)
public class Railcraft {

  public static final String ID = "railcraft";

  private static Railcraft instance;

  private final RailcraftDist dist;

  private final MinecartHandler minecartHandler = new MinecartHandler();
  private final CrowbarHandler crowbarHandler = new CrowbarHandler();
  private final LinkageHandler linkageHandler = new LinkageHandler();

  static {
    CartUtil.linkageManager = LinkageManagerImpl.INSTANCE;
    CartUtil.transferHelper = RailcraftTrainTransferHelper.INSTANCE;
  }

  public static final Logger logger = LogManager.getLogger();

  public Railcraft() {
    instance = this;

    MinecraftForge.EVENT_BUS.register(this.minecartHandler);
    MinecraftForge.EVENT_BUS.register(this);
    // we have to register this all now; forge isn't dumb and will save server config in
    // the logical world's "serverconfig" dir
    var modLoadingContext = ModLoadingContext.get();
    modLoadingContext.registerConfig(ModConfig.Type.CLIENT, RailcraftConfig.clientSpec);
    modLoadingContext.registerConfig(ModConfig.Type.COMMON, RailcraftConfig.commonSpec);
    modLoadingContext.registerConfig(ModConfig.Type.SERVER, RailcraftConfig.serverSpec);

    this.dist = DistExecutor.safeRunForDist(() -> ClientDist::new, () -> ServerDist::new);

    NetworkChannel.registerAll();

    var modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

    modEventBus.addListener(this::handleGatherData);
    modEventBus.addListener(this::handleCommonSetup);
    modEventBus.addListener(this::handleConfigLoad);
    modEventBus.addGenericListener(DataSerializerEntry.class, RailcraftDataSerializers::register);

    RailcraftEntityTypes.ENTITY_TYPES.register(modEventBus);
    RailcraftBlocks.BLOCKS.register(modEventBus);
    RailcraftItems.ITEMS.register(modEventBus);
    RailcraftBlockEntityTypes.BLOCK_ENTITY_TYPES.register(modEventBus);
    TrackTypes.TRACK_TYPES.register(modEventBus);
    RailcraftFluids.FLUIDS.register(modEventBus);
    RailcraftMenuTypes.MENU_TYPES.register(modEventBus);
    RailcraftSoundEvents.SOUND_EVENTS.register(modEventBus);
    RailcraftEnchantments.ENCHANTMENTS.register(modEventBus);
    RailcraftParticleTypes.PARTICLE_TYPES.register(modEventBus);
    RailcraftRecipeSerializers.RECIPE_SERIALIZERS.register(modEventBus);
  }

  public LinkageHandler getLinkageHandler() {
    return this.linkageHandler;
  }

  public MinecartHandler getMinecartHandler() {
    return this.minecartHandler;
  }

  public RailcraftDist getDist() {
    return this.dist;
  }

  public ClientDist getClientDist() {
    if (this.dist instanceof ClientDist) {
      return (ClientDist) this.dist;
    }
    throw new IllegalStateException("Accessing client dist from wrong side");
  }

  public static Railcraft getInstance() {
    return instance;
  }

  private void handleConfigLoad(ModConfigEvent.Loading event) {
    if (event.getConfig().getModId() != ID) {
      return;
    }
    RailcraftOrePlacements.TIN_ORE.handleConfigSpecLoad();
  }

  private void handleCommonSetup(FMLCommonSetupEvent event) {
    event.enqueueWork(RailcraftCriteriaTriggers::register);

    logger.info("Starting Railcraft");

    try (var jar = new JarFile(this.getClass()
        .getProtectionDomain()
        .getCodeSource()
        .getLocation()
        .getPath())) {
      var mf = jar.getManifest().getMainAttributes();
      var gsource = mf.getValue("Build-GitSource");

      if (gsource == "PR") {
        logger.error("THIS RAILCRAFT BUILD IS FROM A PULL REQUEST!"
          + "DO **NOT** POST ISSUES ABOUT THIS VERSION ON ISSUES TAB, INSTEAD REPORT ON THE PR ITSELF AT "
          + "https://github.com/ hey configure this /pulls/id");
        logger.error("RC Commit: " + mf.getValue("Build-Commit"));
        logger.error("RC Source: PR");
        jar.close();
        return;
      }
      logger.info("RC Commit: " + mf.getValue("Build-Commit"));
      logger.info("RC Source: " + (gsource != null ?  gsource : "Localy built."));
      jar.close();
    }
    catch (IOException e){
      logger.error("Railcraft manifest fetching failed! This must be in development.");
    }
  }

  private void handleGatherData(GatherDataEvent event) {
    var generator = event.getGenerator();
    var fileHelper = event.getExistingFileHelper();
    var blockTags = new RailcraftBlockTagsProvider(generator, fileHelper);
    generator.addProvider(blockTags);
    generator.addProvider(new RailcraftItemTagsProvider(generator, blockTags, fileHelper));
    generator.addProvider(new RailcraftLootTableProvider(generator));
    generator.addProvider(new RailcraftAdvancementProviders(generator, fileHelper));
    generator.addProvider(new RailcraftRecipeProvider(generator));
    generator.addProvider(new RailcraftModelProvider(generator));
  }

  @SubscribeEvent(priority = EventPriority.HIGHEST)
  public void handleBiomeLoading(BiomeLoadingEvent event) {
    if (!RailcraftConfig.common.enableOreGeneration.get()) {
      return;
    }
    // BOILERPLATE! Will maybe turn into its own registry so we can iterate and such
    if (event.getName() != null && RailcraftOrePlacements.TIN_ORE.bannedBiomesList().contains(event.getName().toString())) {
      return;
    }
    event.getGeneration().addFeature(
        GenerationStep.Decoration.UNDERGROUND_ORES,
        RailcraftOrePlacements.TIN_ORE.placedFeature());
  }

  @SubscribeEvent
  public void handleWorldTick(WorldTickEvent event) {
    if (event.world instanceof ServerLevel level && event.phase == TickEvent.Phase.END) {
      TokenRingManager.get(level).tick(level);
      if (level.getServer().getTickCount() % 32 == 0) {
        Train.getManager(level).tick();
      }
    }
  }

  @SubscribeEvent
  public void handlePlayerTick(PlayerTickEvent event) {
    if (event.player instanceof ServerPlayer player && event.player.tickCount % 20 == 0) {
      var carts = EntitySearcher.findMinecarts()
          .around(player)
          .outTo(32F)
          .in(player.level);

      var linkedCarts = new LinkedCartsMessage.LinkedCart[carts.size()];
      for (int i = 0; i < linkedCarts.length; i++) {
        var cart = carts.get(i);
        var trainId = Train.getTrainUUID(cart);
        linkedCarts[i] = new LinkedCartsMessage.LinkedCart(
            cart.getId(), trainId,
            LinkageManagerImpl.INSTANCE.getLinkA(cart),
            LinkageManagerImpl.INSTANCE.getLinkB(cart));
      }
      NetworkChannel.GAME.getSimpleChannel().sendTo(new LinkedCartsMessage(linkedCarts),
          player.connection.getConnection(), NetworkDirection.PLAY_TO_CLIENT);
    }
  }

  @SubscribeEvent(priority = EventPriority.HIGHEST)
  public void handleCartLink(CartLinkEvent.Link event) {
    Train.repairTrain(event.getCartOne(), event.getCartTwo());
  }

  @SubscribeEvent(priority = EventPriority.HIGHEST)
  public void handleCartUnlink(CartLinkEvent.Unlink event) {
    Train.killTrain(event.getCartOne());
    Train.killTrain(event.getCartTwo());
  }

  @SubscribeEvent
  public void handleMinecartInteract(PlayerInteractEvent.EntityInteract event) {
    if (event.getTarget()instanceof AbstractMinecart cart) {
      var player = event.getPlayer();
      var hand = event.getHand();
      event.setCanceled(this.minecartHandler.handleInteract(cart, player));
      var crowbarActionResult = this.crowbarHandler.handleInteract(cart, player, hand);
      if (crowbarActionResult.consumesAction()) {
        event.setCanceled(true);
        event.setCancellationResult(crowbarActionResult);
      }
    }
  }

  @SubscribeEvent(priority = EventPriority.HIGHEST)
  public void handleEntityJoinWorld(EntityJoinWorldEvent event) {
    if (event.getEntity()instanceof AbstractMinecart cart) {
      event.setCanceled(this.minecartHandler.handleSpawn(event.getWorld(), cart));
    }
  }

  @SubscribeEvent
  public void handleEntityLeaveWorld(EntityLeaveWorldEvent event) {
    if (event.getEntity()instanceof AbstractMinecart cart
        && !event.getEntity().level.isClientSide()) {
      Train.killTrain(cart);
      LinkageManagerImpl.INSTANCE.breakLinks(cart);
    }
  }
}
