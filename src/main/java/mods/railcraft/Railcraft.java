package mods.railcraft;

import mods.railcraft.advancements.RailcraftCriteriaTriggers;
import mods.railcraft.api.carts.CartUtil;
import mods.railcraft.api.charge.Charge;
import mods.railcraft.api.event.CartLinkEvent;
import mods.railcraft.api.fuel.FuelUtil;
import mods.railcraft.charge.ChargeProviderImpl;
import mods.railcraft.charge.ZapEffectProviderImpl;
import mods.railcraft.client.ClientManager;
import mods.railcraft.data.RailcraftAdvancementProviders;
import mods.railcraft.data.RailcraftBlockTagsProvider;
import mods.railcraft.data.RailcraftFluidTagsProvider;
import mods.railcraft.data.RailcraftItemTagsProvider;
import mods.railcraft.data.RailcraftLanguageProvider;
import mods.railcraft.data.RailcraftLootTableProvider;
import mods.railcraft.data.models.RailcraftModelProvider;
import mods.railcraft.data.recipes.RailcraftRecipeProvider;
import mods.railcraft.fuel.FuelManagerImpl;
import mods.railcraft.network.NetworkChannel;
import mods.railcraft.network.RailcraftDataSerializers;
import mods.railcraft.network.play.LinkedCartsMessage;
import mods.railcraft.particle.RailcraftParticleTypes;
import mods.railcraft.sounds.RailcraftSoundEvents;
import mods.railcraft.util.EntitySearcher;
import mods.railcraft.world.entity.RailcraftEntityTypes;
import mods.railcraft.world.entity.vehicle.LinkageManagerImpl;
import mods.railcraft.world.entity.vehicle.MinecartExtension;
import mods.railcraft.world.entity.vehicle.MinecartHandler;
import mods.railcraft.world.entity.vehicle.Train;
import mods.railcraft.world.entity.vehicle.TrainTransferHelperImpl;
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
import mods.railcraft.world.level.material.fluid.RailcraftFluidTypes;
import mods.railcraft.world.level.material.fluid.RailcraftFluids;
import mods.railcraft.world.signal.TokenRingManager;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityLeaveLevelEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.network.NetworkDirection;

@Mod(Railcraft.ID)
public class Railcraft {

  public static final String ID = "railcraft";

  private final CrowbarHandler crowbarHandler = new CrowbarHandler();
  private final MinecartHandler minecartHandler = new MinecartHandler();

  static {
    FuelUtil._setFuelManager(FuelManagerImpl.INSTANCE);
    CartUtil._setLinkageManager(LinkageManagerImpl.INSTANCE);
    CartUtil._setTransferHelper(TrainTransferHelperImpl.INSTANCE);
    Charge._setZapEffectProvider(new ZapEffectProviderImpl());
    for (var value : ChargeProviderImpl.values()) {
      value.getCharge()._setProvider(value);
    }
  }

  public Railcraft() {
    MinecraftForge.EVENT_BUS.register(this.minecartHandler);
    MinecraftForge.EVENT_BUS.register(this);

    var context = ModLoadingContext.get();
    context.registerConfig(ModConfig.Type.CLIENT, RailcraftConfig.clientSpec);
    context.registerConfig(ModConfig.Type.COMMON, RailcraftConfig.commonSpec);
    context.registerConfig(ModConfig.Type.SERVER, RailcraftConfig.serverSpec);

    var modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
    modEventBus.addListener(this::handleCommonSetup);
    modEventBus.addListener(this::handleRegisterCapabilities);
    modEventBus.addListener(this::handleGatherData);

    DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> ClientManager::new);

    RailcraftEntityTypes.register(modEventBus);
    RailcraftBlocks.register(modEventBus);
    RailcraftItems.register(modEventBus);
    RailcraftBlockEntityTypes.register(modEventBus);
    TrackTypes.register(modEventBus);
    RailcraftFluids.register(modEventBus);
    RailcraftFluidTypes.register(modEventBus);
    RailcraftMenuTypes.register(modEventBus);
    RailcraftSoundEvents.register(modEventBus);
    RailcraftEnchantments.register(modEventBus);
    RailcraftParticleTypes.register(modEventBus);
    RailcraftRecipeSerializers.register(modEventBus);
    RailcraftRecipeTypes.register(modEventBus);
    RailcraftGameEvents.register(modEventBus);
    RailcraftDataSerializers.register(modEventBus);
  }

  // ================================================================================
  // Mod Events
  // ================================================================================

  private void handleCommonSetup(FMLCommonSetupEvent event) {
    NetworkChannel.registerAll();

    event.enqueueWork(RailcraftCriteriaTriggers::register);
    FuelUtil.fuelManager().addFuel(RailcraftFluids.CREOSOTE.get(), 4800);
  }

  private void handleRegisterCapabilities(RegisterCapabilitiesEvent event) {
    event.register(MinecartExtension.class);
  }

  private void handleGatherData(GatherDataEvent event) {
    var generator = event.getGenerator();
    var fileHelper = event.getExistingFileHelper();
    var blockTags = new RailcraftBlockTagsProvider(generator, fileHelper);
    generator.addProvider(event.includeServer(), blockTags);
    generator.addProvider(event.includeServer(),
        new RailcraftItemTagsProvider(generator, blockTags, fileHelper));
    generator.addProvider(event.includeServer(),
        new RailcraftFluidTagsProvider(generator, fileHelper));
    generator.addProvider(event.includeServer(), new RailcraftLootTableProvider(generator));
    generator.addProvider(event.includeServer(),
        new RailcraftAdvancementProviders(generator, fileHelper));
    generator.addProvider(event.includeServer(), new RailcraftRecipeProvider(generator));
    generator.addProvider(event.includeClient(), new RailcraftModelProvider(generator));
    generator.addProvider(event.includeClient(), new RailcraftLanguageProvider(generator));
  }

  // ================================================================================
  // Forge Events
  // ================================================================================

  @SubscribeEvent
  public void handleAttachEntityCapabilities(AttachCapabilitiesEvent<Entity> event) {
    if (event.getObject() instanceof AbstractMinecart minecart) {
      event.addCapability(MinecartExtension.KEY, new ICapabilitySerializable<CompoundTag>() {

        private final LazyOptional<MinecartExtension> instance =
            LazyOptional.of(() -> MinecartExtension.create(minecart));

        @Override
        public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
          return cap == MinecartExtension.CAPABILITY ? this.instance.cast() : LazyOptional.empty();
        }

        @Override
        public CompoundTag serializeNBT() {
          return this.instance.map(MinecartExtension::serializeNBT).orElseGet(CompoundTag::new);
        }

        @Override
        public void deserializeNBT(CompoundTag tag) {
          this.instance.ifPresent(cap -> cap.deserializeNBT(tag));
        }
      });
    }
  }

  @SubscribeEvent
  public void handleLevelTick(TickEvent.LevelTickEvent event) {
    if (event.level instanceof ServerLevel level && event.phase == TickEvent.Phase.END) {
      for (var provider : ChargeProviderImpl.values()) {
        provider.network(level).tick();
      }
      TokenRingManager.get(level).tick(level);
      if (level.getServer().getTickCount() % 32 == 0) {
        Train.getManager(level).tick();
      }
    }
  }

  @SubscribeEvent
  public void handlePlayerTick(TickEvent.PlayerTickEvent event) {
    if (event.player instanceof ServerPlayer player && event.player.tickCount % 20 == 0) {
      var linkedCarts = EntitySearcher.findMinecarts()
          .around(player)
          .inflate(32F)
          .search(player.getLevel())
          .stream()
          .map(MinecartExtension::getOrThrow)
          .map(LinkedCartsMessage.LinkedCart::new)
          .toList();
      NetworkChannel.GAME.simpleChannel().sendTo(new LinkedCartsMessage(linkedCarts),
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
      var player = event.getEntity();
      var hand = event.getHand();
      event.setCanceled(this.minecartHandler.handleInteract(cart, player));
      var crowbarActionResult = this.crowbarHandler.handleInteract(cart, player, hand);
      if (crowbarActionResult.consumesAction()) {
        event.setCanceled(true);
        event.setCancellationResult(crowbarActionResult);
      }
    }
  }

  @SubscribeEvent
  public void handleEntityLeaveWorld(EntityLeaveLevelEvent event) {
    if (event.getEntity() instanceof AbstractMinecart cart
        && !event.getEntity().getLevel().isClientSide()
        && event.getEntity().getRemovalReason() != null
        && event.getEntity().getRemovalReason().shouldDestroy()) {
      Train.killTrain(cart);
      LinkageManagerImpl.INSTANCE.breakLinks(cart);
    }
  }

  @SubscribeEvent
  public void handleNeighborNotify(BlockEvent.NeighborNotifyEvent event) {
    event.getLevel().gameEvent(null, RailcraftGameEvents.NEIGHBOR_NOTIFY.get(), event.getPos());
  }
}
