package mods.railcraft;

import org.apache.commons.lang3.tuple.Pair;
import mods.railcraft.api.events.CartLinkEvent;
import mods.railcraft.api.signals.SignalTools;
import mods.railcraft.carts.Train;
import mods.railcraft.client.ClientDist;
import mods.railcraft.data.RailcraftBlockTagsProvider;
import mods.railcraft.event.MinecartInteractEvent;
import mods.railcraft.network.NetworkChannel;
import mods.railcraft.network.PacketBuilder;
import mods.railcraft.particle.RailcraftParticles;
import mods.railcraft.server.ServerDist;
import mods.railcraft.sounds.RailcraftSoundEvents;
import mods.railcraft.world.entity.LinkageHandler;
import mods.railcraft.world.entity.MinecartHandler;
import mods.railcraft.world.entity.RailcraftEntityTypes;
import mods.railcraft.world.inventory.RailcraftMenuTypes;
import mods.railcraft.world.item.CrowbarHandler;
import mods.railcraft.world.item.RailcraftItems;
import mods.railcraft.world.item.enchantment.RailcraftEnchantments;
import mods.railcraft.world.level.block.RailcraftBlocks;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntityTypes;
import mods.railcraft.world.level.block.track.TrackTypes;
import mods.railcraft.world.level.material.fluid.RailcraftFluids;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Railcraft.ID)
public class Railcraft {

  public static final String ID = "railcraft";

  public static final CommonConfig commonConfig;
  public static final ForgeConfigSpec commonConfigSpec;

  public static final ServerConfig serverConfig;
  public static final ForgeConfigSpec serverConfigSpec;

  static {
    final Pair<CommonConfig, ForgeConfigSpec> commonConfigPair =
        new ForgeConfigSpec.Builder().configure(CommonConfig::new);
    commonConfigSpec = commonConfigPair.getRight();
    commonConfig = commonConfigPair.getLeft();

    final Pair<ServerConfig, ForgeConfigSpec> serverConfigPair =
        new ForgeConfigSpec.Builder().configure(ServerConfig::new);
    serverConfigSpec = serverConfigPair.getRight();
    serverConfig = serverConfigPair.getLeft();
  }

  private static Railcraft instance;

  private final IRailcraftDist dist;

  private final MinecartHandler minecartHandler = new MinecartHandler();
  private final CrowbarHandler crowbarHandler = new CrowbarHandler();
  private final LinkageHandler linkageHandler = new LinkageHandler();

  public Railcraft() {
    instance = this;

    MinecraftForge.EVENT_BUS.register(this.minecartHandler);
    MinecraftForge.EVENT_BUS.register(this);

    this.dist = DistExecutor.safeRunForDist(() -> ClientDist::new, () -> ServerDist::new);

    NetworkChannel.registerAll();

    SignalTools.packetBuilder = PacketBuilder.instance();

    IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

    modEventBus.addListener(this::handleGatherData);

    RailcraftEntityTypes.ENTITY_TYPES.register(modEventBus);
    RailcraftBlocks.BLOCKS.register(modEventBus);
    RailcraftItems.ITEMS.register(modEventBus);
    RailcraftBlockEntityTypes.BLOCK_ENTITY_TYPES.register(modEventBus);
    TrackTypes.TRACK_TYPES.register(modEventBus);
    RailcraftFluids.FLUIDS.register(modEventBus);
    RailcraftMenuTypes.MENU_TYPES.register(modEventBus);
    RailcraftSoundEvents.SOUND_EVENTS.register(modEventBus);
    RailcraftEnchantments.ENCHANTMENTS.register(modEventBus);
    RailcraftParticles.PARTICLE_TYPES.register(modEventBus);
  }

  public LinkageHandler getLinkageHandler() {
    return this.linkageHandler;
  }

  public MinecartHandler getMinecartHandler() {
    return this.minecartHandler;
  }

  public IRailcraftDist getDist() {
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

  private void handleGatherData(GatherDataEvent event) {
    event.getGenerator().addProvider(
        new RailcraftBlockTagsProvider(event.getGenerator(), event.getExistingFileHelper()));
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
  public void handleMinecartInteract(MinecartInteractEvent event) {
    AbstractMinecartEntity cart = event.getCart();
    PlayerEntity player = event.getPlayer();
    Hand hand = event.getHand();
    event.setCanceled(this.minecartHandler.handleInteract(cart, player));
    event.setCanceled(this.crowbarHandler.handleInteract(cart, player, hand));
  }
}
