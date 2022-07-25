package mods.railcraft;

import mods.railcraft.api.carts.CartUtil;
import mods.railcraft.api.fuel.FuelUtil;
import mods.railcraft.charge.ChargeProviderImpl;
import mods.railcraft.fuel.FuelManagerImpl;
import mods.railcraft.network.RailcraftDataSerializers;
import mods.railcraft.particle.RailcraftParticleTypes;
import mods.railcraft.setup.ClientSetup;
import mods.railcraft.setup.ModSetup;
import mods.railcraft.sounds.RailcraftSoundEvents;
import mods.railcraft.world.entity.RailcraftEntityTypes;
import mods.railcraft.world.entity.vehicle.LinkageManagerImpl;
import mods.railcraft.world.entity.vehicle.MinecartExtension;
import mods.railcraft.world.entity.vehicle.TrainTransferHelperImpl;
import mods.railcraft.world.inventory.RailcraftMenuTypes;
import mods.railcraft.world.item.RailcraftItems;
import mods.railcraft.world.item.crafting.RailcraftRecipeSerializers;
import mods.railcraft.world.item.crafting.RailcraftRecipeTypes;
import mods.railcraft.world.item.enchantment.RailcraftEnchantments;
import mods.railcraft.world.level.block.RailcraftBlocks;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntityTypes;
import mods.railcraft.world.level.block.track.TrackTypes;
import mods.railcraft.world.level.gameevent.RailcraftGameEvents;
import mods.railcraft.world.level.material.fluid.RailcraftFluids;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Railcraft.ID)
public class Railcraft {

  public static final String ID = "railcraft";

  static {
    FuelUtil._setFuelManager(FuelManagerImpl.INSTANCE);
    CartUtil._setLinkageManager(LinkageManagerImpl.INSTANCE);
    CartUtil._setTransferHelper(TrainTransferHelperImpl.INSTANCE);
    for (var value : ChargeProviderImpl.values()) {
      value.getCharge()._setProvider(value);
    }
  }

  public Railcraft() {

    MinecraftForge.EVENT_BUS.register(ModSetup.MINECART_HANDLER);
    MinecraftForge.EVENT_BUS.register(this);

    var context = ModLoadingContext.get();
    context.registerConfig(ModConfig.Type.CLIENT, RailcraftConfig.clientSpec);
    context.registerConfig(ModConfig.Type.COMMON, RailcraftConfig.commonSpec);
    context.registerConfig(ModConfig.Type.SERVER, RailcraftConfig.serverSpec);

    var modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
    modEventBus.addListener(ModSetup::init);
    modEventBus.addListener(ClientSetup::init);
    modEventBus.addListener(this::handleRegisterCapabilities);

    DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
      MinecraftForge.EVENT_BUS.addListener(ClientSetup::handleClientTick);
      MinecraftForge.EVENT_BUS.addListener(ClientSetup::handleRenderWorldLast);
      MinecraftForge.EVENT_BUS.addListener(ClientSetup::handleClientLoggedOut);
    });

    RailcraftEntityTypes.register(modEventBus);
    RailcraftBlocks.register(modEventBus);
    RailcraftItems.register(modEventBus);
    RailcraftBlockEntityTypes.register(modEventBus);
    TrackTypes.register(modEventBus);
    RailcraftFluids.register(modEventBus);
    RailcraftMenuTypes.register(modEventBus);
    RailcraftSoundEvents.register(modEventBus);
    RailcraftEnchantments.register(modEventBus);
    RailcraftParticleTypes.register(modEventBus);
    RailcraftRecipeSerializers.register(modEventBus);
    RailcraftRecipeTypes.register(modEventBus);
    RailcraftGameEvents.register(modEventBus);
    RailcraftDataSerializers.register(modEventBus);
  }

  private void handleRegisterCapabilities(RegisterCapabilitiesEvent event) {
    event.register(MinecartExtension.class);
  }
}
