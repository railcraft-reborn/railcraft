package mods.railcraft;

import java.util.List;
import java.util.UUID;
import mods.railcraft.advancements.criterion.RailcraftCriteriaTriggers;
import mods.railcraft.api.carts.CartUtil;
import mods.railcraft.api.event.CartLinkEvent;
import mods.railcraft.client.ClientDist;
import mods.railcraft.data.RailcraftAdvancementProviders;
import mods.railcraft.data.RailcraftBlockStateProvider;
import mods.railcraft.data.RailcraftBlockTagsProvider;
import mods.railcraft.data.RailcraftItemTagsProvider;
import mods.railcraft.data.RailcraftLootTableProvider;
import mods.railcraft.data.RailcraftRecipiesProvider;
import mods.railcraft.network.NetworkChannel;
import mods.railcraft.network.RailcraftDataSerializers;
import mods.railcraft.network.play.LinkedCartsMessage;
import mods.railcraft.particle.RailcraftParticleTypes;
import mods.railcraft.server.ServerDist;
import mods.railcraft.sounds.RailcraftSoundEvents;
import mods.railcraft.util.EntitySearcher;
import mods.railcraft.world.entity.RailcraftEntityTypes;
import mods.railcraft.world.entity.vehicle.LinkageHandler;
import mods.railcraft.world.entity.vehicle.MinecartHandler;
import mods.railcraft.world.entity.vehicle.RailcraftLinkageManager;
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
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.EntityLeaveWorldEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.LogicalSide;
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
    CartUtil.linkageManager = RailcraftLinkageManager.INSTANCE;
    CartUtil.transferHelper = RailcraftTrainTransferHelper.INSTANCE;
  }

  public Railcraft() {
    instance = this;

    MinecraftForge.EVENT_BUS.register(this.minecartHandler);
    MinecraftForge.EVENT_BUS.register(this);
    // we have to register this all now; forge isn't dumb and will save server config in
    // the logical world's "serverconfig" dir
    ModLoadingContext modLoadingContext = ModLoadingContext.get();
    modLoadingContext.registerConfig(ModConfig.Type.CLIENT, RailcraftConfig.clientSpec);
    modLoadingContext.registerConfig(ModConfig.Type.COMMON, RailcraftConfig.commonSpec);
    modLoadingContext.registerConfig(ModConfig.Type.SERVER, RailcraftConfig.serverSpec);

    this.dist = DistExecutor.safeRunForDist(() -> ClientDist::new, () -> ServerDist::new);

    NetworkChannel.registerAll();

    IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

    modEventBus.addListener(this::handleGatherData);
    modEventBus.addListener(this::handleCommonSetup);
    modEventBus.addGenericListener(DataSerializerEntry.class, RailcraftDataSerializers::register);

    RailcraftEntityTypes.ENTITY_TYPES.register(modEventBus);
    RailcraftBlocks.BLOCKS.register(modEventBus);
    RailcraftItems.ITEMS.register(modEventBus);
    RailcraftBlockEntityTypes.BLOCK_ENTITY_TYPES.register(modEventBus);
    TrackTypes.trackTypes.register(modEventBus);
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
    generator.addProvider(new RailcraftRecipiesProvider(generator));
    generator.addProvider(new RailcraftBlockStateProvider(generator));
  }

  @SubscribeEvent
  public void handleWorldTick(TickEvent.WorldTickEvent event) {
    if (event.side == LogicalSide.SERVER && event.phase == TickEvent.Phase.END) {
      ServerLevel level = (ServerLevel) event.world;
      TokenRingManager.get(level).tick(level);
      if (level.getServer().getTickCount() % 32 == 0) {
        Train.getManager(level).tick();
      }
    }
  }

  @SubscribeEvent
  public void handlePlayerTick(PlayerTickEvent event) {
    if ((event.player instanceof ServerPlayer) && (event.player.tickCount % 20) == 0) {
      ServerPlayer player = (ServerPlayer) event.player;
      List<AbstractMinecart> carts = EntitySearcher.findMinecarts()
          .around(player)
          .outTo(32F)
          .in(player.level);

      LinkedCartsMessage.LinkedCart[] linkedCarts = new LinkedCartsMessage.LinkedCart[carts.size()];
      for (int i = 0; i < linkedCarts.length; i++) {
        AbstractMinecart cart = carts.get(i);
        UUID trainId = Train.getTrainUUID(cart);
        linkedCarts[i] = new LinkedCartsMessage.LinkedCart(
            cart.getId(), trainId,
            RailcraftLinkageManager.INSTANCE.getLinkA(cart),
            RailcraftLinkageManager.INSTANCE.getLinkB(cart));
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
    if (event.getTarget() instanceof AbstractMinecart) {
      AbstractMinecart cart = (AbstractMinecart) event.getTarget();
      Player player = event.getPlayer();
      InteractionHand hand = event.getHand();
      event.setCanceled(this.minecartHandler.handleInteract(cart, player));
      InteractionResult crowbarActionResult =
          this.crowbarHandler.handleInteract(cart, player, hand);
      if (crowbarActionResult.consumesAction()) {
        event.setCanceled(true);
        event.setCancellationResult(crowbarActionResult);
      }
    }
  }

  @SubscribeEvent(priority = EventPriority.HIGHEST)
  public void handleEntityJoinWorld(EntityJoinWorldEvent event) {
    if (event.getEntity() instanceof AbstractMinecart) {
      event.setCanceled(this.minecartHandler.handleSpawn(event.getWorld(),
          (AbstractMinecart) event.getEntity()));
    }
  }

  @SubscribeEvent
  public void handleEntityLeaveWorld(EntityLeaveWorldEvent event) {
    if (event.getEntity() instanceof AbstractMinecart
        && !event.getEntity().level.isClientSide()) {
      AbstractMinecart cart = (AbstractMinecart) event.getEntity();
      Train.killTrain(cart);
      RailcraftLinkageManager.INSTANCE.breakLinks(cart);
    }
  }
}
