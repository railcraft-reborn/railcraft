package mods.railcraft;

import java.util.Set;
import mods.railcraft.advancements.RailcraftCriteriaTriggers;
import mods.railcraft.api.carts.CartUtil;
import mods.railcraft.api.carts.RollingStock;
import mods.railcraft.api.charge.Charge;
import mods.railcraft.api.fuel.FuelUtil;
import mods.railcraft.charge.ChargeProviderImpl;
import mods.railcraft.charge.ZapEffectProviderImpl;
import mods.railcraft.client.ClientManager;
import mods.railcraft.data.RailcraftBlockTagsProvider;
import mods.railcraft.data.RailcraftDamageTypeTagsProvider;
import mods.railcraft.data.RailcraftFluidTagsProvider;
import mods.railcraft.data.RailcraftItemTagsProvider;
import mods.railcraft.data.RailcraftLanguageProvider;
import mods.railcraft.data.RailcraftPoiTypeTagsProvider;
import mods.railcraft.data.RailcraftSoundsProvider;
import mods.railcraft.data.RailcraftSpriteSourceProvider;
import mods.railcraft.data.advancements.RailcraftAdvancementProvider;
import mods.railcraft.data.loot.RailcraftLootModifierProvider;
import mods.railcraft.data.loot.RailcraftLootTableProvider;
import mods.railcraft.loot.RailcraftLootModifiers;
import mods.railcraft.data.models.RailcraftBlockModelProvider;
import mods.railcraft.data.models.RailcraftItemModelProvider;
import mods.railcraft.data.recipes.RailcraftRecipeProvider;
import mods.railcraft.data.recipes.builders.BrewingRecipe;
import mods.railcraft.data.worldgen.RailcraftBiomeModifiers;
import mods.railcraft.data.worldgen.features.RailcraftOreFeatures;
import mods.railcraft.data.worldgen.placements.RailcraftOrePlacements;
import mods.railcraft.fuel.FuelManagerImpl;
import mods.railcraft.network.NetworkChannel;
import mods.railcraft.network.RailcraftDataSerializers;
import mods.railcraft.network.play.LinkedCartsMessage;
import mods.railcraft.particle.RailcraftParticleTypes;
import mods.railcraft.sounds.RailcraftSoundEvents;
import mods.railcraft.util.EntitySearcher;
import mods.railcraft.util.capability.CapabilityUtil;
import mods.railcraft.world.damagesource.RailcraftDamageSources;
import mods.railcraft.world.damagesource.RailcraftDamageType;
import mods.railcraft.world.effect.RailcraftMobEffects;
import mods.railcraft.world.entity.RailcraftEntityTypes;
import mods.railcraft.world.entity.ai.village.poi.RailcraftPoiTypes;
import mods.railcraft.world.entity.npc.RailcraftVillagerProfession;
import mods.railcraft.world.entity.npc.RailcraftVillagerTrades;
import mods.railcraft.world.entity.vehicle.MinecartHandler;
import mods.railcraft.world.entity.vehicle.RollingStockImpl;
import mods.railcraft.world.entity.vehicle.TrainTransferServiceImpl;
import mods.railcraft.world.inventory.RailcraftMenuTypes;
import mods.railcraft.world.item.CrowbarHandler;
import mods.railcraft.world.item.RailcraftCreativeModeTabs;
import mods.railcraft.world.item.RailcraftItems;
import mods.railcraft.world.item.alchemy.RailcraftPotions;
import mods.railcraft.world.item.crafting.RailcraftRecipeSerializers;
import mods.railcraft.world.item.crafting.RailcraftRecipeTypes;
import mods.railcraft.world.item.enchantment.RailcraftEnchantments;
import mods.railcraft.world.level.block.RailcraftBlocks;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntityTypes;
import mods.railcraft.world.level.block.track.TrackTypes;
import mods.railcraft.world.level.gameevent.RailcraftGameEvents;
import mods.railcraft.world.level.levelgen.structure.ComponentWorkshop;
import mods.railcraft.world.level.material.RailcraftFluidTypes;
import mods.railcraft.world.level.material.RailcraftFluids;
import mods.railcraft.world.signal.TokenRingManager;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataProvider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityLeaveLevelEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.event.server.ServerAboutToStartEvent;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.registries.ForgeRegistries;

@Mod(Railcraft.ID)
public class Railcraft {

  public static final String ID = "railcraft";
  public static final boolean BETA = true;

  static {
    FuelUtil._setFuelManager(FuelManagerImpl.INSTANCE);
    CartUtil._setTransferService(TrainTransferServiceImpl.INSTANCE);
    Charge._setZapEffectProvider(new ZapEffectProviderImpl());
    for (var value : ChargeProviderImpl.values()) {
      value.getCharge()._setProvider(value);
    }
  }

  private final CrowbarHandler crowbarHandler = new CrowbarHandler();
  private final MinecartHandler minecartHandler = new MinecartHandler();

  public Railcraft() {
    MinecraftForge.EVENT_BUS.register(this);

    RailcraftConfig.registerConfig(ModLoadingContext.get());

    var modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
    modEventBus.addListener(this::handleCommonSetup);
    modEventBus.addListener(this::handleRegisterCapabilities);
    modEventBus.addListener(this::buildContents);
    modEventBus.addListener(this::handleGatherData);

    DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> ClientManager::new);

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
  }

  // ================================================================================
  // Mod Events
  // ================================================================================

  private void handleCommonSetup(FMLCommonSetupEvent event) {
    NetworkChannel.registerAll();

    event.enqueueWork(() -> {
      RailcraftCriteriaTriggers.register();
      BrewingRecipeRegistry.addRecipe(new BrewingRecipe(Potions.AWKWARD,
          RailcraftItems.CREOSOTE_BOTTLE.get(), RailcraftPotions.CREOSOTE.get()));
    });
    FuelUtil.fuelManager().addFuel(RailcraftFluids.CREOSOTE.get(), 4800);
  }

  private void handleRegisterCapabilities(RegisterCapabilitiesEvent event) {
    event.register(RollingStock.class);
  }

  public void buildContents(BuildCreativeModeTabContentsEvent event) {
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
    generator.addProvider(event.includeClient(),
        new RailcraftItemModelProvider(packOutput, fileHelper));
    generator.addProvider(event.includeClient(),
        new RailcraftBlockModelProvider(packOutput, fileHelper));
    generator.addProvider(event.includeClient(), new RailcraftLanguageProvider(packOutput));
    generator.addProvider(event.includeClient(),
        new RailcraftSoundsProvider(packOutput, fileHelper));
    generator.addProvider(event.includeClient(),
        new RailcraftSpriteSourceProvider(packOutput, fileHelper));

    var builder = new RegistrySetBuilder()
        .add(Registries.CONFIGURED_FEATURE, RailcraftOreFeatures::bootstrap)
        .add(Registries.PLACED_FEATURE, RailcraftOrePlacements::bootstrap)
        .add(ForgeRegistries.Keys.BIOME_MODIFIERS, RailcraftBiomeModifiers::bootstrap)
        .add(Registries.DAMAGE_TYPE, RailcraftDamageType::bootstrap);

    generator.addProvider(event.includeServer(),
        (DataProvider.Factory<DatapackBuiltinEntriesProvider>) output ->
            new DatapackBuiltinEntriesProvider(output, lookupProvider, builder, Set.of(ID)));

    generator.addProvider(event.includeServer(),
        new RailcraftDamageTypeTagsProvider(packOutput, lookupProvider.thenApply(provider ->
            builder.buildPatch(RegistryAccess
                .fromRegistryOfRegistries(BuiltInRegistries.REGISTRY), provider)), fileHelper));
  }

  // ================================================================================
  // Forge Events
  // ================================================================================

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
  public void handleAttachEntityCapabilities(AttachCapabilitiesEvent<Entity> event) {
    if (event.getObject() instanceof AbstractMinecart minecart) {
      event.addCapability(RollingStockImpl.KEY,
          CapabilityUtil.serializableProvider(
              CompoundTag::new,
              () -> new RollingStockImpl(minecart),
              RollingStock.CAPABILITY));
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
    if (event.player instanceof ServerPlayer player && player.tickCount % 20 == 0) {
      var linkedCarts = EntitySearcher.findMinecarts()
          .around(player)
          .inflate(32F)
          .stream(player.level())
          .map(RollingStock::getOrThrow)
          .map(LinkedCartsMessage.LinkedCart::new)
          .toList();
      NetworkChannel.GAME.simpleChannel().sendTo(new LinkedCartsMessage(linkedCarts),
          player.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
    }
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
            .map(x -> x.getResultItem(registryAccess))
            .orElse(ItemStack.EMPTY);
        if (!cooked.isEmpty() && level.getRandom().nextDouble() < 0.5) {
          cooked = cooked.copy();
          cooked.setCount(drop.getCount());
          entityItem.setItem(cooked);
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
