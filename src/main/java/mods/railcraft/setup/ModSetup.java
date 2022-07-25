package mods.railcraft.setup;

import mods.railcraft.Railcraft;
import mods.railcraft.advancements.RailcraftCriteriaTriggers;
import mods.railcraft.api.event.CartLinkEvent;
import mods.railcraft.api.fuel.FuelUtil;
import mods.railcraft.charge.ChargeProviderImpl;
import mods.railcraft.network.NetworkChannel;
import mods.railcraft.network.play.LinkedCartsMessage;
import mods.railcraft.util.EntitySearcher;
import mods.railcraft.world.entity.vehicle.LinkageManagerImpl;
import mods.railcraft.world.entity.vehicle.MinecartExtension;
import mods.railcraft.world.entity.vehicle.MinecartHandler;
import mods.railcraft.world.entity.vehicle.Train;
import mods.railcraft.world.item.CrowbarHandler;
import mods.railcraft.world.level.gameevent.RailcraftGameEvents;
import mods.railcraft.world.level.material.fluid.RailcraftFluids;
import mods.railcraft.world.signal.TokenRingManager;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityLeaveLevelEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.network.NetworkDirection;

@Mod.EventBusSubscriber(modid = Railcraft.ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ModSetup {

  private static final CrowbarHandler CROWBAR_HANDLER = new CrowbarHandler();
  public static final MinecartHandler MINECART_HANDLER = new MinecartHandler();

  public static void init(FMLCommonSetupEvent event) {
    NetworkChannel.registerAll();

    event.enqueueWork(RailcraftCriteriaTriggers::register);
    FuelUtil.fuelManager().addFuel(RailcraftFluids.CREOSOTE.get(), 4800);
  }

  @SubscribeEvent
  public static void handleAttachEntityCapabilities(AttachCapabilitiesEvent<Entity> event) {
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
  public static void handleLevelTick(TickEvent.LevelTickEvent event) {
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
  public static void handlePlayerTick(TickEvent.PlayerTickEvent event) {
    if (event.player instanceof ServerPlayer player && event.player.tickCount % 20 == 0) {
      var linkedCarts = EntitySearcher.findMinecarts()
        .around(player)
        .inflate(32F)
        .search(player.getLevel())
        .stream()
        .map(MinecartExtension::getOrThrow)
        .map(LinkedCartsMessage.LinkedCart::new)
        .toList();
      NetworkChannel.GAME.getSimpleChannel().sendTo(new LinkedCartsMessage(linkedCarts),
        player.connection.getConnection(), NetworkDirection.PLAY_TO_CLIENT);
    }
  }

  @SubscribeEvent(priority = EventPriority.HIGHEST)
  public static void handleCartLink(CartLinkEvent.Link event) {
    Train.repairTrain(event.getCartOne(), event.getCartTwo());
  }

  @SubscribeEvent(priority = EventPriority.HIGHEST)
  public static void handleCartUnlink(CartLinkEvent.Unlink event) {
    Train.killTrain(event.getCartOne());
    Train.killTrain(event.getCartTwo());
  }

  @SubscribeEvent
  public static void handleMinecartInteract(PlayerInteractEvent.EntityInteract event) {
    if (event.getTarget() instanceof AbstractMinecart cart) {
      var player = event.getEntity();
      var hand = event.getHand();
      event.setCanceled(MINECART_HANDLER.handleInteract(cart, player));
      var crowbarActionResult = CROWBAR_HANDLER.handleInteract(cart, player, hand);
      if (crowbarActionResult.consumesAction()) {
        event.setCanceled(true);
        event.setCancellationResult(crowbarActionResult);
      }
    }
  }

  @SubscribeEvent
  public static void handleEntityLeaveWorld(EntityLeaveLevelEvent event) {
    if (event.getEntity() instanceof AbstractMinecart cart
      && !event.getEntity().getLevel().isClientSide()
      && event.getEntity().getRemovalReason() != null
      && event.getEntity().getRemovalReason().shouldDestroy()) {
      Train.killTrain(cart);
      LinkageManagerImpl.INSTANCE.breakLinks(cart);
    }
  }

  @SubscribeEvent
  public static void handleNeighborNotify(BlockEvent.NeighborNotifyEvent event) {
    event.getLevel().gameEvent(null, RailcraftGameEvents.NEIGHBOR_NOTIFY.get(), event.getPos());
  }
}
