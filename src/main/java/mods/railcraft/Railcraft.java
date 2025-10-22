package mods.railcraft;

import java.util.Objects;
import java.util.Optional;
import mods.railcraft.advancements.RailcraftCriteriaTriggers;
import mods.railcraft.api.carts.RollingStock;
import mods.railcraft.api.charge.Charge;
import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.attachment.RailcraftAttachmentTypes;
import mods.railcraft.charge.ChargeCartStorageImpl;
import mods.railcraft.charge.ChargeProviderImpl;
import mods.railcraft.charge.ZapEffectProviderImpl;
import mods.railcraft.client.ClientManager;
import mods.railcraft.data.RailcraftDataMapProvider;
import mods.railcraft.data.RailcraftLanguageProvider;
import mods.railcraft.data.RailcraftParticleProvider;
import mods.railcraft.data.RailcraftSoundsProvider;
import mods.railcraft.data.RailcraftSpriteSourceProvider;
import mods.railcraft.data.advancements.RailcraftAdvancementProvider;
import mods.railcraft.data.gametest.RailcraftGameTestInstances;
import mods.railcraft.data.gametest.RailcraftTestEnvironments;
import mods.railcraft.data.loot.RailcraftLootModifierProvider;
import mods.railcraft.data.loot.RailcraftLootTableProvider;
import mods.railcraft.data.models.RailcraftModelProvider;
import mods.railcraft.data.recipes.RailcraftRecipeProvider;
import mods.railcraft.data.recipes.providers.BlastFurnaceRecipeProvider;
import mods.railcraft.data.recipes.providers.CokeOvenRecipeProvider;
import mods.railcraft.data.recipes.providers.CrusherRecipeProvider;
import mods.railcraft.data.recipes.providers.RollingRecipeProvider;
import mods.railcraft.data.tags.RailcraftBlockTagsProvider;
import mods.railcraft.data.tags.RailcraftDamageTypeTagsProvider;
import mods.railcraft.data.tags.RailcraftFluidTagsProvider;
import mods.railcraft.data.tags.RailcraftItemTagsProvider;
import mods.railcraft.data.tags.RailcraftPoiTypeTagsProvider;
import mods.railcraft.data.worldgen.RailcraftBiomeModifiers;
import mods.railcraft.data.worldgen.RailcraftStructureSets;
import mods.railcraft.data.worldgen.RailcraftStructures;
import mods.railcraft.data.worldgen.features.RailcraftOreFeatures;
import mods.railcraft.data.worldgen.placements.RailcraftOrePlacements;
import mods.railcraft.datamaps.RailcraftDataMaps;
import mods.railcraft.loot.RailcraftLootModifiers;
import mods.railcraft.network.PacketHandler;
import mods.railcraft.network.RailcraftDataSerializers;
import mods.railcraft.network.to_client.LinkedCartsMessage;
import mods.railcraft.particle.RailcraftParticleTypes;
import mods.railcraft.sounds.RailcraftSoundEvents;
import mods.railcraft.util.EntitySearcher;
import mods.railcraft.world.damagesource.RailcraftDamageSources;
import mods.railcraft.world.damagesource.RailcraftDamageType;
import mods.railcraft.world.effect.RailcraftMobEffects;
import mods.railcraft.world.entity.RailcraftEntityTypes;
import mods.railcraft.world.entity.ai.village.poi.RailcraftPoiTypes;
import mods.railcraft.world.entity.npc.RailcraftVillagerProfession;
import mods.railcraft.world.entity.npc.RailcraftVillagerTrades;
import mods.railcraft.world.entity.vehicle.MinecartHandler;
import mods.railcraft.world.inventory.RailcraftMenuTypes;
import mods.railcraft.world.item.ChargeMeterItem;
import mods.railcraft.world.item.CrowbarHandler;
import mods.railcraft.world.item.RailcraftCreativeModeTabs;
import mods.railcraft.world.item.RailcraftItems;
import mods.railcraft.world.item.component.RailcraftDataComponents;
import mods.railcraft.world.item.crafting.RailcraftRecipeSerializers;
import mods.railcraft.world.item.crafting.RailcraftRecipeTypes;
import mods.railcraft.world.item.enchantment.RailcraftEnchantments;
import mods.railcraft.world.level.block.RailcraftBlocks;
import mods.railcraft.world.level.block.entity.BlastFurnaceBlockEntity;
import mods.railcraft.world.level.block.entity.CokeOvenBlockEntity;
import mods.railcraft.world.level.block.entity.CrusherBlockEntity;
import mods.railcraft.world.level.block.entity.FeedStationBlockEntity;
import mods.railcraft.world.level.block.entity.PoweredRollingMachineBlockEntity;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntityTypes;
import mods.railcraft.world.level.block.entity.SteamOvenBlockEntity;
import mods.railcraft.world.level.block.entity.SteamTurbineBlockEntity;
import mods.railcraft.world.level.block.entity.WaterTankSidingBlockEntity;
import mods.railcraft.world.level.block.entity.manipulator.CartDispenserBlockEntity;
import mods.railcraft.world.level.block.entity.manipulator.FluidLoaderBlockEntity;
import mods.railcraft.world.level.block.entity.manipulator.FluidUnloaderBlockEntity;
import mods.railcraft.world.level.block.entity.manipulator.ItemLoaderBlockEntity;
import mods.railcraft.world.level.block.entity.manipulator.ItemUnloaderBlockEntity;
import mods.railcraft.world.level.block.entity.manipulator.TrainDispenserBlockEntity;
import mods.railcraft.world.level.block.entity.steamboiler.SteamBoilerBlockEntity;
import mods.railcraft.world.level.block.entity.tank.IronTankBlockEntity;
import mods.railcraft.world.level.block.entity.tank.SteelTankBlockEntity;
import mods.railcraft.world.level.block.entity.worldspike.WorldSpikeBlockEntity;
import mods.railcraft.world.level.block.track.TrackTypes;
import mods.railcraft.world.level.gameevent.RailcraftGameEvents;
import mods.railcraft.world.level.levelgen.feature.RailcraftFeatures;
import mods.railcraft.world.level.levelgen.structure.ComponentWorkshop;
import mods.railcraft.world.level.levelgen.structure.RailcraftStructurePieces;
import mods.railcraft.world.level.levelgen.structure.RailcraftStructureTypes;
import mods.railcraft.world.level.material.RailcraftFluidTypes;
import mods.railcraft.world.level.material.RailcraftFluids;
import mods.railcraft.world.signal.TokenRingManager;
import net.minecraft.SharedConstants;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.world.chunk.RegisterTicketControllersEvent;
import net.neoforged.neoforge.common.world.chunk.TicketController;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.OnDatapackSyncEvent;
import net.neoforged.neoforge.event.entity.EntityLeaveLevelEvent;
import net.neoforged.neoforge.event.entity.living.LivingDropsEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.neoforge.event.level.LevelEvent;
import net.neoforged.neoforge.event.server.ServerAboutToStartEvent;
import net.neoforged.neoforge.event.server.ServerStartedEvent;
import net.neoforged.neoforge.event.tick.LevelTickEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.event.village.VillagerTradesEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.neoforged.neoforge.transfer.fluid.BucketResourceHandler;

@Mod(RailcraftConstants.ID)
public class Railcraft {

  public static final boolean BETA = false;
  public static final TicketController CHUNK_CONTROLLER =
      new TicketController(RailcraftConstants.rl("ticket_controller"),
          new WorldSpikeBlockEntity.RailcraftValidationTicket());

  static {
    Charge._setZapEffectProvider(new ZapEffectProviderImpl());
    for (var value : ChargeProviderImpl.values()) {
      value.getCharge()._setProvider(value);
    }
  }

  private final MinecartHandler minecartHandler = new MinecartHandler();

  public Railcraft(ModContainer modContainer, Dist dist) {
    NeoForge.EVENT_BUS.register(this);

    RailcraftConfig.registerConfig(modContainer);

    var modEventBus = modContainer.getEventBus();
    modEventBus.addListener(this::handleRegisterCapabilities);
    modEventBus.addListener(this::buildContents);
    modEventBus.addListener(this::handleGatherData);
    modEventBus.addListener(this::registerChunkControllers);

    if (dist.isClient()) {
      ClientManager.init(modEventBus);
      modContainer.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }

    PacketHandler.register(modEventBus);
    RailcraftEntityTypes.register(modEventBus);
    RailcraftBlocks.register(modEventBus);
    RailcraftItems.register(modEventBus);
    RailcraftMobEffects.register(modEventBus);
    RailcraftCreativeModeTabs.register(modEventBus);
    RailcraftBlockEntityTypes.register(modEventBus);
    TrackTypes.register(modEventBus);
    RailcraftFluids.register(modEventBus);
    RailcraftFluidTypes.register(modEventBus);
    RailcraftMenuTypes.register(modEventBus);
    RailcraftSoundEvents.register(modEventBus);
    RailcraftParticleTypes.register(modEventBus);
    RailcraftRecipeSerializers.register(modEventBus);
    RailcraftRecipeTypes.register(modEventBus);
    RailcraftGameEvents.register(modEventBus);
    RailcraftDataSerializers.register(modEventBus);
    RailcraftPoiTypes.register(modEventBus);
    RailcraftVillagerProfession.register(modEventBus);
    RailcraftLootModifiers.register(modEventBus);
    RailcraftFeatures.register(modEventBus);
    RailcraftStructureTypes.register(modEventBus);
    RailcraftStructurePieces.register(modEventBus);
    RailcraftCriteriaTriggers.register(modEventBus);
    RailcraftAttachmentTypes.register(modEventBus);
    RailcraftDataMaps.register(modEventBus);
    RailcraftDataComponents.register(modEventBus);
    RailcraftGameTestInstances.register(modEventBus);
  }

  // Mod Events
  private void handleRegisterCapabilities(RegisterCapabilitiesEvent event) {
    for (var entityType : BuiltInRegistries.ENTITY_TYPE) {
      event.registerEntity(RollingStock.CAPABILITY, entityType,
          (entity, ctx) -> entity instanceof AbstractMinecart
              ? entity.getData(RailcraftAttachmentTypes.MINECART_ROLLING_STOCK.get())
              : null);
    }

    event.registerEntity(Capabilities.Fluid.ENTITY,
        RailcraftEntityTypes.TANK_MINECART.get(), (e, side) -> e.getTankManager());
    event.registerEntity(Capabilities.Energy.ENTITY,
        RailcraftEntityTypes.ENERGY_MINECART.get(), (e, side) -> e.getBatteryCart());
    event.registerEntity(Capabilities.Energy.ENTITY,
        RailcraftEntityTypes.ELECTRIC_LOCOMOTIVE.get(), (e, side) -> e.getBatteryCart());
    event.registerEntity(Capabilities.Fluid.ENTITY,
        RailcraftEntityTypes.STEAM_LOCOMOTIVE.get(), (e, side) -> e.getTankManager());
    event.registerEntity(Capabilities.Item.ENTITY_AUTOMATION,
        RailcraftEntityTypes.STEAM_LOCOMOTIVE.get(), (e, side) -> e.getFuelContainer());

    event.registerBlockEntity(Capabilities.Fluid.BLOCK,
        RailcraftBlockEntityTypes.WATER_TANK_SIDING.get(), WaterTankSidingBlockEntity::getFluidCap);
    event.registerBlockEntity(Capabilities.Item.BLOCK,
        RailcraftBlockEntityTypes.COKE_OVEN.get(), CokeOvenBlockEntity::getItemCap);
    event.registerBlockEntity(Capabilities.Fluid.BLOCK,
        RailcraftBlockEntityTypes.COKE_OVEN.get(), CokeOvenBlockEntity::getFluidCap);
    event.registerBlockEntity(Capabilities.Item.BLOCK,
        RailcraftBlockEntityTypes.STEAM_OVEN.get(), SteamOvenBlockEntity::getItemCap);
    event.registerBlockEntity(Capabilities.Fluid.BLOCK,
        RailcraftBlockEntityTypes.STEAM_OVEN.get(), SteamOvenBlockEntity::getFluidCap);
    event.registerBlockEntity(Capabilities.Item.BLOCK,
        RailcraftBlockEntityTypes.CRUSHER.get(), CrusherBlockEntity::getItemCap);
    event.registerBlockEntity(Capabilities.Energy.BLOCK,
        RailcraftBlockEntityTypes.CRUSHER.get(), CrusherBlockEntity::getEnergyCap);
    event.registerBlockEntity(Capabilities.Item.BLOCK,
        RailcraftBlockEntityTypes.BLAST_FURNACE.get(), BlastFurnaceBlockEntity::getItemCap);
    event.registerBlockEntity(Capabilities.Fluid.BLOCK,
        RailcraftBlockEntityTypes.STEAM_TURBINE.get(), SteamTurbineBlockEntity::getFluidCap);
    event.registerBlockEntity(Capabilities.Energy.BLOCK,
        RailcraftBlockEntityTypes.STEAM_TURBINE.get(), SteamTurbineBlockEntity::getEnergyCap);
    event.registerBlockEntity(Capabilities.Item.BLOCK,
        RailcraftBlockEntityTypes.STEAM_BOILER.get(), SteamBoilerBlockEntity::getItemCap);
    event.registerBlockEntity(Capabilities.Fluid.BLOCK,
        RailcraftBlockEntityTypes.STEAM_BOILER.get(), SteamBoilerBlockEntity::getFluidCap);

    event.registerBlockEntity(Capabilities.Item.BLOCK,
        RailcraftBlockEntityTypes.POWERED_ROLLING_MACHINE.get(),
        PoweredRollingMachineBlockEntity::getItemCap);

    event.registerBlockEntity(Capabilities.Fluid.BLOCK,
        RailcraftBlockEntityTypes.IRON_TANK.get(), IronTankBlockEntity::getFluidCap);
    event.registerBlockEntity(Capabilities.Fluid.BLOCK,
        RailcraftBlockEntityTypes.STEEL_TANK.get(), SteelTankBlockEntity::getFluidCap);

    event.registerBlockEntity(Capabilities.Item.BLOCK,
        RailcraftBlockEntityTypes.CART_DISPENSER.get(), CartDispenserBlockEntity::getItemCap);
    event.registerBlockEntity(Capabilities.Item.BLOCK,
        RailcraftBlockEntityTypes.TRAIN_DISPENSER.get(), TrainDispenserBlockEntity::getItemCap);
    event.registerBlockEntity(Capabilities.Item.BLOCK,
        RailcraftBlockEntityTypes.FEED_STATION.get(), FeedStationBlockEntity::getItemCap);
    event.registerBlockEntity(Capabilities.Item.BLOCK,
        RailcraftBlockEntityTypes.FLUID_LOADER.get(), FluidLoaderBlockEntity::getItemCap);
    event.registerBlockEntity(Capabilities.Fluid.BLOCK,
        RailcraftBlockEntityTypes.FLUID_LOADER.get(), FluidLoaderBlockEntity::getFluidCap);
    event.registerBlockEntity(Capabilities.Item.BLOCK,
        RailcraftBlockEntityTypes.FLUID_UNLOADER.get(), FluidUnloaderBlockEntity::getItemCap);
    event.registerBlockEntity(Capabilities.Fluid.BLOCK,
        RailcraftBlockEntityTypes.FLUID_UNLOADER.get(), FluidUnloaderBlockEntity::getFluidCap);
    event.registerBlockEntity(Capabilities.Item.BLOCK,
        RailcraftBlockEntityTypes.ITEM_LOADER.get(), ItemLoaderBlockEntity::getItemCap);
    event.registerBlockEntity(Capabilities.Item.BLOCK,
        RailcraftBlockEntityTypes.ITEM_UNLOADER.get(), ItemUnloaderBlockEntity::getItemCap);

    event.registerItem(Capabilities.Fluid.ITEM,
        (stack, ctx) -> new BucketResourceHandler(Objects.requireNonNull(ctx)), RailcraftItems.CREOSOTE_BUCKET);

    event.registerBlock(Capabilities.Energy.BLOCK, Charge.distribution,
        RailcraftBlocks.FORCE_TRACK_EMITTER.get(),
        RailcraftBlocks.NICKEL_ZINC_BATTERY.get(),
        RailcraftBlocks.NICKEL_IRON_BATTERY.get(),
        RailcraftBlocks.ZINC_CARBON_BATTERY.get(),
        RailcraftBlocks.ZINC_CARBON_BATTERY_EMPTY.get(),
        RailcraftBlocks.ZINC_SILVER_BATTERY.get(),
        RailcraftBlocks.ZINC_SILVER_BATTERY_EMPTY.get(),
        RailcraftBlocks.FRAME.get(),
        RailcraftBlocks.POWERED_ROLLING_MACHINE.get());
  }

  private void buildContents(BuildCreativeModeTabContentsEvent event) {
    if (event.getTabKey() == CreativeModeTabs.TOOLS_AND_UTILITIES) {
      RailcraftCreativeModeTabs.addToolsAndUtilities(event);
    } else if (event.getTabKey() == CreativeModeTabs.COMBAT) {
      RailcraftCreativeModeTabs.addCombat(event);
    }
  }

  private void handleGatherData(GatherDataEvent.Client event) {
    event.createBlockAndItemTags(RailcraftBlockTagsProvider::new, RailcraftItemTagsProvider::new);
    event.createProvider(RailcraftFluidTagsProvider::new);
    event.createProvider(RailcraftLootTableProvider::new);
    event.createProvider(RailcraftAdvancementProvider::new);
    event.createProvider(RailcraftRecipeProvider.Runner::new);
    event.createProvider(BlastFurnaceRecipeProvider.Runner::new);
    event.createProvider(CokeOvenRecipeProvider.Runner::new);
    event.createProvider(CrusherRecipeProvider.Runner::new);
    event.createProvider(RollingRecipeProvider.Runner::new);
    event.createProvider(RailcraftPoiTypeTagsProvider::new);
    event.createProvider(RailcraftLootModifierProvider::new);
    event.createProvider(RailcraftDamageTypeTagsProvider::new);
    event.createProvider(RailcraftDataMapProvider::new);
    event.createProvider(RailcraftModelProvider::new);
    event.createProvider(RailcraftLanguageProvider::new);
    event.createProvider(RailcraftSoundsProvider::new);
    event.createProvider(RailcraftSpriteSourceProvider::new);
    event.createProvider(RailcraftParticleProvider::new);
    event.createDatapackRegistryObjects(new RegistrySetBuilder()
        .add(Registries.CONFIGURED_FEATURE, RailcraftOreFeatures::bootstrap)
        .add(Registries.PLACED_FEATURE, RailcraftOrePlacements::bootstrap)
        .add(NeoForgeRegistries.Keys.BIOME_MODIFIERS, RailcraftBiomeModifiers::bootstrap)
        .add(Registries.DAMAGE_TYPE, RailcraftDamageType::bootstrap)
        .add(Registries.STRUCTURE, RailcraftStructures::bootstrap)
        .add(Registries.STRUCTURE_SET, RailcraftStructureSets::bootstrap)
        .add(Registries.ENCHANTMENT, RailcraftEnchantments::bootstrap)
        .add(Registries.TEST_INSTANCE, RailcraftGameTestInstances::bootstrap)
        .add(Registries.TEST_ENVIRONMENT, RailcraftTestEnvironments::bootstrap));
  }

  private void registerChunkControllers(RegisterTicketControllersEvent event) {
    event.register(CHUNK_CONTROLLER);
  }

  // NeoForge Events
  @SubscribeEvent
  public void handleServerAboutToStart(ServerAboutToStartEvent event) {
    ComponentWorkshop.addVillageStructures(event.getServer().registryAccess());
  }

  @SubscribeEvent
  public void handleServerStarted(ServerStartedEvent event) {
    if (RailcraftConfig.SERVER.solidCarts.get()) {
      //AbstractMinecart.registerCollisionHandler(this.minecartHandler);
    }
  }

  @SubscribeEvent
  public void handleLevelTick(LevelTickEvent.Post event) {
    if (event.getLevel() instanceof ServerLevel level) {
      for (var provider : ChargeProviderImpl.values()) {
        provider.network(level).tick();
      }
      TokenRingManager.get(level).tick(level);
    }
  }

  @SubscribeEvent
  public void handlePlayerTick(PlayerTickEvent.Post event) {
    if (event.getEntity() instanceof ServerPlayer player
        && player.tickCount % SharedConstants.TICKS_PER_SECOND == 0) {
      var linkedCarts = EntitySearcher.findMinecarts()
          .around(player)
          .inflate(32F)
          .stream(player.level())
          .map(RollingStock::getOrThrow)
          .map(LinkedCartsMessage.LinkedCart::new)
          .toList();
      PacketDistributor.sendToPlayer(player, new LinkedCartsMessage(linkedCarts));
    }
  }

  @SubscribeEvent
  public void handleEntityInteract(PlayerInteractEvent.EntityInteract event) {
    if (event.getTarget() instanceof AbstractMinecart cart) {
      var player = event.getEntity();
      var hand = event.getHand();
      var stack = event.getItemStack();

      if (!stack.isEmpty() && stack.is(RailcraftItems.CHARGE_METER.get())) {
        player.swing(hand);
        if (!player.level().isClientSide()) {
          Optional.ofNullable(cart.getCapability(Capabilities.Energy.ENTITY, null))
              .filter(ChargeCartStorageImpl.class::isInstance)
              .map(ChargeCartStorageImpl.class::cast)
              .ifPresent(battery -> {
                ChargeMeterItem.sendCartStat(player, cart.getDisplayName(), battery);
                event.setCanceled(true);
                event.setCancellationResult(InteractionResult.SUCCESS);
              });
        }
      } else {
        event.setCanceled(this.minecartHandler.handleInteract(cart, player));
        var crowbarActionResult = CrowbarHandler.handleInteract(cart, player, hand);
        if (crowbarActionResult.consumesAction()) {
          event.setCanceled(true);
          event.setCancellationResult(crowbarActionResult);
        }
      }
    }
  }

  @SubscribeEvent
  public void handleEntityLeaveWorld(EntityLeaveLevelEvent event) {
    if (event.getEntity() instanceof AbstractMinecart cart
        && !cart.level().isClientSide() && cart.isRemoved()) {
      RollingStock.getOrThrow(cart).removed(cart.getRemovalReason());
    }
  }

  @SubscribeEvent
  public void handleLevelUnload(LevelEvent.Unload event) {
    if (event.getLevel() instanceof ServerLevel level) {
      ChargeProviderImpl.DISTRIBUTION.removeChargeNetwork(level);
    }
  }

  @SubscribeEvent
  public void modifyDrops(LivingDropsEvent event) {
    var level = (ServerLevel) event.getEntity().level();
    var registryAccess = level.registryAccess();
    if (event.getSource().equals(RailcraftDamageSources.steam(registryAccess))) {
      var recipeManager = level.recipeAccess();
      for (var entityItem : event.getDrops()) {
        var drop = entityItem.getItem();
        var cooked = recipeManager
            .getRecipeFor(RecipeType.SMELTING, new SingleRecipeInput(drop), level)
            .map(x -> x.value().assemble(null, registryAccess))
            .orElse(ItemStack.EMPTY);
        if (!cooked.isEmpty() && level.getRandom().nextBoolean()) {
          entityItem.setItem(new ItemStack(cooked.getItem(), drop.getCount()));
        }
      }
    }
  }

  @SubscribeEvent
  public void addCustomTrades(VillagerTradesEvent event) {
    if (event.getType() == RailcraftVillagerProfession.TRACKMAN.getKey()) {
      RailcraftVillagerTrades.addTradeForTrackman(event.getTrades());
    } else if (event.getType() == RailcraftVillagerProfession.CARTMAN.getKey()) {
      RailcraftVillagerTrades.addTradeForCartman(event.getTrades());
    } else if (event.getType() == VillagerProfession.ARMORER) {
      RailcraftVillagerTrades.addTradeForArmorer(event.getTrades());
    } else if (event.getType() == VillagerProfession.TOOLSMITH) {
      RailcraftVillagerTrades.addTradeForToolSmith(event.getTrades());
    }
  }

  @SubscribeEvent
  public void handleNeighborNotify(BlockEvent.NeighborNotifyEvent event) {
    event.getLevel().gameEvent(null, RailcraftGameEvents.NEIGHBOR_NOTIFY, event.getPos());
  }

  @SubscribeEvent
  public void handleDatapackSync(OnDatapackSyncEvent event) {
    event.sendRecipes(RailcraftRecipeTypes.BLASTING.get());
    event.sendRecipes(RailcraftRecipeTypes.ROLLING.get());
    event.sendRecipes(RailcraftRecipeTypes.CRUSHING.get());
    event.sendRecipes(RailcraftRecipeTypes.COKING.get());
  }
}
