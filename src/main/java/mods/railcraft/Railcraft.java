package mods.railcraft;

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
import mods.railcraft.world.item.crafting.RailcraftRecipeTypes;
import mods.railcraft.world.item.enchantment.RailcraftEnchantments;
import mods.railcraft.world.level.block.RailcraftBlocks;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntityTypes;
import mods.railcraft.world.level.block.track.TrackTypes;
import mods.railcraft.world.level.gameevent.RailcraftGameEvents;
import mods.railcraft.world.level.material.fluid.RailcraftFluids;
import mods.railcraft.world.signal.TokenRingManager;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.EntityLeaveWorldEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
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

  public Railcraft() {
    instance = this;

    MinecraftForge.EVENT_BUS.register(this.minecartHandler);
    MinecraftForge.EVENT_BUS.register(this);

    var context = ModLoadingContext.get();
    context.registerConfig(ModConfig.Type.CLIENT, RailcraftConfig.clientSpec);
    context.registerConfig(ModConfig.Type.COMMON, RailcraftConfig.commonSpec);
    context.registerConfig(ModConfig.Type.SERVER, RailcraftConfig.serverSpec);

    this.dist = DistExecutor.safeRunForDist(() -> ClientDist::new, () -> ServerDist::new);

    NetworkChannel.registerAll();

    var modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

    modEventBus.addListener(this::handleGatherData);
    modEventBus.addListener(this::handleCommonSetup);
    modEventBus.addGenericListener(DataSerializerEntry.class, RailcraftDataSerializers::register);
    modEventBus.addGenericListener(RecipeSerializer.class,
        (RegistryEvent.Register<RecipeSerializer<?>> event) -> {
          // Static init as Minecraft now freezes its registries
          RailcraftRecipeTypes.init();
          RailcraftGameEvents.init();
        });

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

  private void handleCommonSetup(FMLCommonSetupEvent event) {
    event.enqueueWork(RailcraftCriteriaTriggers::register);
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

  @SubscribeEvent
  public void handleWorldTick(TickEvent.WorldTickEvent event) {
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
    if (event.getTarget() instanceof AbstractMinecart cart) {
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
    if (event.getEntity() instanceof AbstractMinecart cart) {
      event.setCanceled(this.minecartHandler.handleSpawn(event.getWorld(), cart));
    }
  }

  @SubscribeEvent
  public void handleEntityLeaveWorld(EntityLeaveWorldEvent event) {
    if (event.getEntity() instanceof AbstractMinecart cart
        && !event.getEntity().getLevel().isClientSide()) {
      Train.killTrain(cart);
      LinkageManagerImpl.INSTANCE.breakLinks(cart);
    }
  }

  @SubscribeEvent
  public void handleNeighborNotify(BlockEvent.NeighborNotifyEvent event) {
    event.getWorld().gameEvent(RailcraftGameEvents.NEIGHBOR_NOTIFY, event.getPos());
  }
}
