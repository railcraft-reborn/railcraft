package mods.railcraft;

import java.util.Optional;
import org.slf4j.Logger;
import com.mojang.logging.LogUtils;
import mods.railcraft.advancements.RailcraftCriteriaTriggers;
import mods.railcraft.api.carts.RollingStock;
import mods.railcraft.api.charge.Charge;
import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.charge.ChargeCartStorageImpl;
import mods.railcraft.charge.ChargeProviderImpl;
import mods.railcraft.charge.ZapEffectProviderImpl;
import mods.railcraft.client.ClientManager;
import mods.railcraft.data.RailcraftBlockTagsProvider;
import mods.railcraft.data.RailcraftDamageTypeTagsProvider;
import mods.railcraft.data.RailcraftDataMapProvider;
import mods.railcraft.data.RailcraftDatapackProvider;
import mods.railcraft.data.RailcraftFluidTagsProvider;
import mods.railcraft.data.RailcraftItemTagsProvider;
import mods.railcraft.data.RailcraftLanguageProvider;
import mods.railcraft.data.RailcraftPoiTypeTagsProvider;
import mods.railcraft.data.RailcraftSoundsProvider;
import mods.railcraft.data.RailcraftSpriteSourceProvider;
import mods.railcraft.data.advancements.RailcraftAdvancementProvider;
import mods.railcraft.data.loot.RailcraftLootModifierProvider;
import mods.railcraft.data.loot.RailcraftLootTableProvider;
import mods.railcraft.data.models.RailcraftBlockModelProvider;
import mods.railcraft.data.models.RailcraftItemModelProvider;
import mods.railcraft.data.recipes.RailcraftRecipeProvider;
import mods.railcraft.data.recipes.builders.BrewingRecipe;
import mods.railcraft.loot.RailcraftLootModifiers;
import mods.railcraft.network.PacketHandler;
import mods.railcraft.network.RailcraftDataSerializers;
import mods.railcraft.network.to_client.LinkedCartsMessage;
import mods.railcraft.particle.RailcraftParticleTypes;
import mods.railcraft.sounds.RailcraftSoundEvents;
import mods.railcraft.util.EntitySearcher;
import mods.railcraft.util.attachment.RailcraftAttachmentTypes;
import mods.railcraft.util.datamaps.RailcraftDataMaps;
import mods.railcraft.util.fluids.CreosoteBottleWrapper;
import mods.railcraft.world.damagesource.RailcraftDamageSources;
import mods.railcraft.world.effect.RailcraftMobEffects;
import mods.railcraft.world.entity.RailcraftEntityTypes;
import mods.railcraft.world.entity.ai.village.poi.RailcraftPoiTypes;
import mods.railcraft.world.entity.npc.RailcraftVillagerProfession;
import mods.railcraft.world.entity.npc.RailcraftVillagerTrades;
import mods.railcraft.world.entity.vehicle.MinecartHandler;
import mods.railcraft.world.entity.vehicle.RollingStockImpl;
import mods.railcraft.world.inventory.RailcraftMenuTypes;
import mods.railcraft.world.item.ChargeMeterItem;
import mods.railcraft.world.item.CrowbarHandler;
import mods.railcraft.world.item.RailcraftCreativeModeTabs;
import mods.railcraft.world.item.RailcraftItems;
import mods.railcraft.world.item.alchemy.RailcraftPotions;
import mods.railcraft.world.item.crafting.RailcraftRecipeSerializers;
import mods.railcraft.world.item.crafting.RailcraftRecipeTypes;
import mods.railcraft.world.item.enchantment.RailcraftEnchantments;
import mods.railcraft.world.level.block.RailcraftBlocks;
import mods.railcraft.world.level.block.entity.BlastFurnaceBlockEntity;
import mods.railcraft.world.level.block.entity.CokeOvenBlockEntity;
import mods.railcraft.world.level.block.entity.CrusherBlockEntity;
import mods.railcraft.world.level.block.entity.FeedStationBlockEntity;
import mods.railcraft.world.level.block.entity.ForceTrackEmitterBlockEntity;
import mods.railcraft.world.level.block.entity.PoweredRollingMachineBlockEntity;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntityTypes;
import mods.railcraft.world.level.block.entity.SteamOvenBlockEntity;
import mods.railcraft.world.level.block.entity.SteamTurbineBlockEntity;
import mods.railcraft.world.level.block.entity.WaterTankSidingBlockEntity;
import mods.railcraft.world.level.block.entity.charge.BatteryBlockEntity;
import mods.railcraft.world.level.block.entity.detector.FilterDetectorBlockEntity;
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
import mods.railcraft.world.level.levelgen.structure.ComponentWorkshop;
import mods.railcraft.world.level.levelgen.structure.RailcraftStructurePieces;
import mods.railcraft.world.level.levelgen.structure.RailcraftStructureTypes;
import mods.railcraft.world.level.material.RailcraftFluidTypes;
import mods.railcraft.world.level.material.RailcraftFluids;
import mods.railcraft.world.signal.TokenRingManager;
import net.minecraft.SharedConstants;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.brewing.BrewingRecipeRegistry;
import net.neoforged.neoforge.common.world.chunk.RegisterTicketControllersEvent;
import net.neoforged.neoforge.common.world.chunk.TicketController;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.TickEvent;
import net.neoforged.neoforge.event.entity.EntityLeaveLevelEvent;
import net.neoforged.neoforge.event.entity.living.LivingDropsEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.neoforge.event.server.ServerAboutToStartEvent;
import net.neoforged.neoforge.event.server.ServerStartedEvent;
import net.neoforged.neoforge.event.village.VillagerTradesEvent;
import net.neoforged.neoforge.fluids.capability.wrappers.FluidBucketWrapper;

@Mod(RailcraftConstants.ID)
public class Railcraft {

  public static final boolean BETA = true;
  private static final Logger LOGGER = LogUtils.getLogger();

  static {
    Charge._setZapEffectProvider(new ZapEffectProviderImpl());
    for (var value : ChargeProviderImpl.values()) {
      value.getCharge()._setProvider(value);
    }
  }

  private final MinecartHandler minecartHandler = new MinecartHandler();

  public static final TicketController CHUNK_CONTROLLER =
      new TicketController(RailcraftConstants.rl("default"), (level, ticketHelper) -> {
        for (var entry : ticketHelper.getBlockTickets().entrySet()) {
          var key = entry.getKey();
          var value = entry.getValue();
          int ticketCount = value.nonTicking().size();
          int tickingTicketCount = value.ticking().size();
          var be = level.getBlockEntity(key);
          if (be instanceof WorldSpikeBlockEntity) {
            LOGGER.info("Allowing {} chunk tickets and {} ticking chunk tickets to be reinstated for position: {}.", ticketCount, tickingTicketCount, key);
          } else {
            ticketHelper.removeAllTickets(key);
            LOGGER.info("Removing {} chunk tickets and {} ticking chunk tickets for no longer valid position: {}.", ticketCount, tickingTicketCount, key);
          }
        }
        for (var entry : ticketHelper.getEntityTickets().entrySet()) {
          var key = entry.getKey();
          var value = entry.getValue();
          int ticketCount = value.nonTicking().size();
          int tickingTicketCount = value.ticking().size();
          LOGGER.info("Allowing {} chunk tickets and {} ticking chunk tickets to be reinstated for entity: {}.", ticketCount, tickingTicketCount, key);
        }
      });

  public Railcraft(IEventBus modEventBus, Dist dist) {
    NeoForge.EVENT_BUS.register(this);

    RailcraftConfig.registerConfig(ModLoadingContext.get());

    modEventBus.addListener(this::handleCommonSetup);
    modEventBus.addListener(this::handleRegisterCapabilities);
    modEventBus.addListener(this::buildContents);
    modEventBus.addListener(this::handleGatherData);
    modEventBus.addListener(this::registerChunkControllers);

    if (dist.isClient()) {
      ClientManager.init(modEventBus);
    }

    PacketHandler.register(modEventBus);
    RailcraftEntityTypes.register(modEventBus);
    RailcraftBlocks.register(modEventBus);
    RailcraftItems.register(modEventBus);
    RailcraftPotions.register(modEventBus);
    RailcraftMobEffects.register(modEventBus);
    RailcraftCreativeModeTabs.register(modEventBus);
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
    RailcraftPoiTypes.register(modEventBus);
    RailcraftVillagerProfession.register(modEventBus);
    RailcraftLootModifiers.register(modEventBus);
    RailcraftStructureTypes.register(modEventBus);
    RailcraftStructurePieces.register(modEventBus);
    RailcraftCriteriaTriggers.register(modEventBus);
    RailcraftAttachmentTypes.register(modEventBus);
    RailcraftDataMaps.register(modEventBus);
  }

  // Mod Events
  private void handleCommonSetup(FMLCommonSetupEvent event) {
    event.enqueueWork(() -> {
      BrewingRecipeRegistry.addRecipe(new BrewingRecipe(Potions.AWKWARD,
          RailcraftItems.CREOSOTE_BOTTLE.get(), RailcraftPotions.CREOSOTE.get()));
    });
  }

  private void handleRegisterCapabilities(RegisterCapabilitiesEvent event) {
    for (var entityType : BuiltInRegistries.ENTITY_TYPE) {
      event.registerEntity(RollingStock.CAPABILITY, entityType, (entity, ctx) ->
          entity instanceof AbstractMinecart minecart ? new RollingStockImpl(minecart) : null);
    }

    event.registerEntity(Capabilities.FluidHandler.ENTITY,
        RailcraftEntityTypes.TANK_MINECART.get(), (e, side) -> e.getTankManager());
    event.registerEntity(Capabilities.EnergyStorage.ENTITY,
        RailcraftEntityTypes.ELECTRIC_LOCOMOTIVE.get(), (e, side) -> e.getBatteryCart());
    event.registerEntity(Capabilities.FluidHandler.ENTITY,
        RailcraftEntityTypes.STEAM_LOCOMOTIVE.get(), (e, side) -> e.getTankManager());

    event.registerBlockEntity(Capabilities.EnergyStorage.BLOCK,
        RailcraftBlockEntityTypes.BATTERY.get(), BatteryBlockEntity::getEnergyCap);
    event.registerBlockEntity(Capabilities.EnergyStorage.BLOCK,
        RailcraftBlockEntityTypes.FORCE_TRACK_EMITTER.get(),
        ForceTrackEmitterBlockEntity::getEnergyCap);
    event.registerBlockEntity(Capabilities.FluidHandler.BLOCK,
        RailcraftBlockEntityTypes.WATER_TANK_SIDING.get(), WaterTankSidingBlockEntity::getFluidCap);
    event.registerBlockEntity(Capabilities.ItemHandler.BLOCK,
        RailcraftBlockEntityTypes.COKE_OVEN.get(), CokeOvenBlockEntity::getItemCap);
    event.registerBlockEntity(Capabilities.FluidHandler.BLOCK,
        RailcraftBlockEntityTypes.COKE_OVEN.get(), CokeOvenBlockEntity::getFluidCap);
    event.registerBlockEntity(Capabilities.ItemHandler.BLOCK,
        RailcraftBlockEntityTypes.STEAM_OVEN.get(), SteamOvenBlockEntity::getItemCap);
    event.registerBlockEntity(Capabilities.FluidHandler.BLOCK,
        RailcraftBlockEntityTypes.STEAM_OVEN.get(), SteamOvenBlockEntity::getFluidCap);
    event.registerBlockEntity(Capabilities.ItemHandler.BLOCK,
        RailcraftBlockEntityTypes.CRUSHER.get(), CrusherBlockEntity::getItemCap);
    event.registerBlockEntity(Capabilities.EnergyStorage.BLOCK,
        RailcraftBlockEntityTypes.CRUSHER.get(), CrusherBlockEntity::getEnergyCap);
    event.registerBlockEntity(Capabilities.ItemHandler.BLOCK,
        RailcraftBlockEntityTypes.BLAST_FURNACE.get(), BlastFurnaceBlockEntity::getItemCap);
    event.registerBlockEntity(Capabilities.FluidHandler.BLOCK,
        RailcraftBlockEntityTypes.STEAM_TURBINE.get(), SteamTurbineBlockEntity::getFluidCap);
    event.registerBlockEntity(Capabilities.EnergyStorage.BLOCK,
        RailcraftBlockEntityTypes.STEAM_TURBINE.get(), SteamTurbineBlockEntity::getEnergyCap);
    event.registerBlockEntity(Capabilities.ItemHandler.BLOCK,
        RailcraftBlockEntityTypes.STEAM_BOILER.get(), SteamBoilerBlockEntity::getItemCap);
    event.registerBlockEntity(Capabilities.FluidHandler.BLOCK,
        RailcraftBlockEntityTypes.STEAM_BOILER.get(), SteamBoilerBlockEntity::getFluidCap);

    event.registerBlockEntity(Capabilities.ItemHandler.BLOCK,
        RailcraftBlockEntityTypes.POWERED_ROLLING_MACHINE.get(),
        PoweredRollingMachineBlockEntity::getItemCap);
    event.registerBlockEntity(Capabilities.EnergyStorage.BLOCK,
        RailcraftBlockEntityTypes.POWERED_ROLLING_MACHINE.get(),
        PoweredRollingMachineBlockEntity::getEnergyCap);

    event.registerBlockEntity(Capabilities.FluidHandler.BLOCK,
        RailcraftBlockEntityTypes.IRON_TANK.get(), IronTankBlockEntity::getFluidCap);
    event.registerBlockEntity(Capabilities.FluidHandler.BLOCK,
        RailcraftBlockEntityTypes.STEEL_TANK.get(), SteelTankBlockEntity::getFluidCap);

    event.registerBlockEntity(Capabilities.ItemHandler.BLOCK,
        RailcraftBlockEntityTypes.CART_DISPENSER.get(), CartDispenserBlockEntity::getItemCap);
    event.registerBlockEntity(Capabilities.ItemHandler.BLOCK,
        RailcraftBlockEntityTypes.TRAIN_DISPENSER.get(), TrainDispenserBlockEntity::getItemCap);
    event.registerBlockEntity(Capabilities.ItemHandler.BLOCK,
        RailcraftBlockEntityTypes.FEED_STATION.get(), FeedStationBlockEntity::getItemCap);
    event.registerBlockEntity(Capabilities.ItemHandler.BLOCK,
        RailcraftBlockEntityTypes.FLUID_LOADER.get(), FluidLoaderBlockEntity::getItemCap);
    event.registerBlockEntity(Capabilities.FluidHandler.BLOCK,
        RailcraftBlockEntityTypes.FLUID_LOADER.get(), FluidLoaderBlockEntity::getFluidCap);
    event.registerBlockEntity(Capabilities.ItemHandler.BLOCK,
        RailcraftBlockEntityTypes.FLUID_UNLOADER.get(), FluidUnloaderBlockEntity::getItemCap);
    event.registerBlockEntity(Capabilities.FluidHandler.BLOCK,
        RailcraftBlockEntityTypes.FLUID_UNLOADER.get(), FluidUnloaderBlockEntity::getFluidCap);
    event.registerBlockEntity(Capabilities.ItemHandler.BLOCK,
        RailcraftBlockEntityTypes.ITEM_LOADER.get(), ItemLoaderBlockEntity::getItemCap);
    event.registerBlockEntity(Capabilities.ItemHandler.BLOCK,
        RailcraftBlockEntityTypes.ITEM_UNLOADER.get(), ItemUnloaderBlockEntity::getItemCap);
    event.registerBlockEntity(Capabilities.ItemHandler.BLOCK,
        RailcraftBlockEntityTypes.ADVANCED_DETECTOR.get(), FilterDetectorBlockEntity::getItemCap);
    event.registerBlockEntity(Capabilities.ItemHandler.BLOCK,
        RailcraftBlockEntityTypes.ITEM_DETECTOR.get(), FilterDetectorBlockEntity::getItemCap);
    event.registerBlockEntity(Capabilities.ItemHandler.BLOCK,
        RailcraftBlockEntityTypes.LOCOMOTIVE_DETECTOR.get(), FilterDetectorBlockEntity::getItemCap);
    event.registerBlockEntity(Capabilities.ItemHandler.BLOCK,
        RailcraftBlockEntityTypes.SHEEP_DETECTOR.get(), FilterDetectorBlockEntity::getItemCap);
    event.registerBlockEntity(Capabilities.ItemHandler.BLOCK,
        RailcraftBlockEntityTypes.TANK_DETECTOR.get(), FilterDetectorBlockEntity::getItemCap);

    event.registerItem(Capabilities.FluidHandler.ITEM,
        (stack, ctx) -> new CreosoteBottleWrapper(stack), Items.GLASS_BOTTLE);
    event.registerItem(Capabilities.FluidHandler.ITEM,
        (stack, ctx) -> new CreosoteBottleWrapper(stack), RailcraftItems.CREOSOTE_BOTTLE);
    event.registerItem(Capabilities.FluidHandler.ITEM,
        (stack, ctx) -> new FluidBucketWrapper(stack), RailcraftItems.CREOSOTE_BUCKET);
  }

  private void buildContents(BuildCreativeModeTabContentsEvent event) {
    if (event.getTabKey() == CreativeModeTabs.TOOLS_AND_UTILITIES) {
      RailcraftCreativeModeTabs.addToolsAndUtilities(event.getEntries());
    } else if (event.getTabKey() == CreativeModeTabs.COMBAT) {
      RailcraftCreativeModeTabs.addCombat(event.getEntries());
    }
  }

  private void handleGatherData(GatherDataEvent event) {
    var generator = event.getGenerator();
    var packOutput = generator.getPackOutput();
    var lookupProvider = event.getLookupProvider();
    var fileHelper = event.getExistingFileHelper();

    var blockTags = new RailcraftBlockTagsProvider(packOutput, lookupProvider, fileHelper);
    var blockTagsLookup = blockTags.contentsGetter();
    generator.addProvider(event.includeServer(), blockTags);
    generator.addProvider(event.includeServer(),
        new RailcraftItemTagsProvider(packOutput, lookupProvider, blockTagsLookup, fileHelper));
    generator.addProvider(event.includeServer(),
        new RailcraftFluidTagsProvider(packOutput, lookupProvider, fileHelper));
    generator.addProvider(event.includeServer(), new RailcraftLootTableProvider(packOutput));
    generator.addProvider(event.includeServer(),
        new RailcraftAdvancementProvider(packOutput, lookupProvider, fileHelper));
    generator.addProvider(event.includeServer(), new RailcraftRecipeProvider(packOutput));
    generator.addProvider(event.includeServer(),
        new RailcraftPoiTypeTagsProvider(packOutput, lookupProvider, fileHelper));
    generator.addProvider(event.includeServer(), new RailcraftLootModifierProvider(packOutput));
    generator.addProvider(event.includeServer(),
        new RailcraftDamageTypeTagsProvider(packOutput, lookupProvider, fileHelper));
    generator.addProvider(event.includeServer(),
        new RailcraftDatapackProvider(packOutput, lookupProvider));
    generator.addProvider(event.includeServer(),
        new RailcraftDataMapProvider(packOutput, lookupProvider));
    generator.addProvider(event.includeClient(),
        new RailcraftItemModelProvider(packOutput, fileHelper));
    generator.addProvider(event.includeClient(),
        new RailcraftBlockModelProvider(packOutput, fileHelper));
    generator.addProvider(event.includeClient(), new RailcraftLanguageProvider(packOutput));
    generator.addProvider(event.includeClient(),
        new RailcraftSoundsProvider(packOutput, fileHelper));
    generator.addProvider(event.includeClient(),
        new RailcraftSpriteSourceProvider(packOutput, lookupProvider, fileHelper));
  }

  private void registerChunkControllers(RegisterTicketControllersEvent event) {
    event.register(CHUNK_CONTROLLER);
  }

  // Forge Events
  @SubscribeEvent
  public void handleServerAboutToStart(ServerAboutToStartEvent event) {
    ComponentWorkshop.addVillageStructures(event.getServer().registryAccess());
  }

  @SubscribeEvent
  public void handleServerStarted(ServerStartedEvent event) {
    if (RailcraftConfig.SERVER.solidCarts.get()) {
      AbstractMinecart.registerCollisionHandler(this.minecartHandler);
    }
  }

  @SubscribeEvent
  public void handleLevelTick(TickEvent.LevelTickEvent event) {
    if (event.level instanceof ServerLevel level && event.phase == TickEvent.Phase.END) {
      for (var provider : ChargeProviderImpl.values()) {
        provider.network(level).tick();
      }
      TokenRingManager.get(level).tick(level);
    }
  }

  @SubscribeEvent
  public void handlePlayerTick(TickEvent.PlayerTickEvent event) {
    if (event.player instanceof ServerPlayer player && player.tickCount % SharedConstants.TICKS_PER_SECOND == 0) {
      var linkedCarts = EntitySearcher.findMinecarts()
          .around(player)
          .inflate(32F)
          .stream(player.level())
          .map(RollingStock::getOrThrow)
          .map(LinkedCartsMessage.LinkedCart::new)
          .toList();
      PacketHandler.sendTo(player, new LinkedCartsMessage(linkedCarts));
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
          Optional.ofNullable(cart.getCapability(Capabilities.EnergyStorage.ENTITY, null))
              .filter(ChargeCartStorageImpl.class::isInstance)
              .map(ChargeCartStorageImpl.class::cast)
              .ifPresent(battery -> {
                ChargeMeterItem.sendChat(player, Translations.ChargeMeter.CART,
                    battery.getEnergyStored(), battery.getDraw(), battery.getLosses());
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
  public void modifyDrops(LivingDropsEvent event) {
    var level = event.getEntity().level();
    var registryAccess = level.registryAccess();
    if (event.getSource().equals(RailcraftDamageSources.steam(registryAccess))) {
      var recipeManager = level.getRecipeManager();
      for (var entityItem : event.getDrops()) {
        var drop = entityItem.getItem();
        var cooked = recipeManager
            .getRecipeFor(RecipeType.SMELTING, new SimpleContainer(drop), level)
            .map(x -> x.value().getResultItem(registryAccess))
            .orElse(ItemStack.EMPTY);
        if (!cooked.isEmpty() && level.getRandom().nextBoolean()) {
          entityItem.setItem(new ItemStack(cooked.getItem(), drop.getCount()));
        }
      }
    }
  }

  @SubscribeEvent
  public void addCustomTrades(VillagerTradesEvent event) {
    if (event.getType() == RailcraftVillagerProfession.TRACKMAN.get()) {
      RailcraftVillagerTrades.addTradeForTrackman(event.getTrades());
    } else if (event.getType() == RailcraftVillagerProfession.CARTMAN.get()) {
      RailcraftVillagerTrades.addTradeForCartman(event.getTrades());
    } else if (event.getType() == VillagerProfession.ARMORER) {
      RailcraftVillagerTrades.addTradeForArmorer(event.getTrades());
    } else if (event.getType() == VillagerProfession.TOOLSMITH) {
      RailcraftVillagerTrades.addTradeForToolSmith(event.getTrades());
    }
  }

  @SubscribeEvent
  public void handleNeighborNotify(BlockEvent.NeighborNotifyEvent event) {
    event.getLevel().gameEvent(null, RailcraftGameEvents.NEIGHBOR_NOTIFY.get(), event.getPos());
  }
}
