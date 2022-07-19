package mods.railcraft.world.item;

import com.google.common.collect.MapMaker;
import mods.railcraft.RailcraftConfig;
import mods.railcraft.advancements.RailcraftCriteriaTriggers;
import mods.railcraft.api.carts.LinkageHandler;
import mods.railcraft.api.item.Crowbar;
import mods.railcraft.world.entity.vehicle.*;
import mods.railcraft.world.item.enchantment.RailcraftEnchantments;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

import java.util.Map;

/**
 * @author CovertJaguar (https://www.railcraft.info)
 */
public class CrowbarHandler {

  public static final float SMACK_VELOCITY = 0.07f;

  private static final Map<Player, AbstractMinecart> linkMap =
      new MapMaker()
          .weakKeys()
          .weakValues()
          .makeMap();

  public InteractionResult handleInteract(AbstractMinecart cart, Player player,
      InteractionHand hand) {
    ItemStack stack = player.getItemInHand(hand);
    if (stack.isEmpty() || !(stack.getItem() instanceof Crowbar)) {
      return InteractionResult.PASS;
    }

    if (player.getLevel().isClientSide()) {
      return InteractionResult.SUCCESS;
    }

    var crowbar = (Crowbar) stack.getItem();

    if ((stack.getItem() instanceof SeasonsCrowbarItem)
        && cart instanceof SeasonalCart seasonalCart
        && RailcraftConfig.common.seasonsEnabled.get()) {
      var season = SeasonsCrowbarItem.getSeason(stack);
      seasonalCart.setSeason(season);
      RailcraftCriteriaTriggers.SEASON_SET.trigger((ServerPlayer) player, cart, season);
      return InteractionResult.CONSUME;
    }

    if (crowbar.canLink(player, hand, stack, cart)) {
      this.linkCart(player, hand, stack, cart, crowbar);
      return InteractionResult.CONSUME;
    } else if (crowbar.canBoost(player, hand, stack, cart)) {
      this.boostCart(player, hand, stack, cart, crowbar);
      return InteractionResult.CONSUME;
    }

    return InteractionResult.PASS;
  }

  private void linkCart(Player player, InteractionHand hand, ItemStack stack,
      AbstractMinecart cart, Crowbar crowbar) {
    if (cart instanceof LinkageHandler handler && !handler.isLinkable()) {
      return;
    }

    var last = linkMap.remove(player);
    if (last == null || !last.isAlive()) {
      linkMap.put(player, cart);
      player.displayClientMessage(Component.translatable("crowbar.link_started"), true);
      return;
    }

    if (LinkageManagerImpl.INSTANCE.areLinked(cart, last, false)) {
      LinkageManagerImpl.INSTANCE.breakLink(cart, last);
      player.displayClientMessage(Component.translatable("crowbar.link_broken"), true);
    } else {
      if (!LinkageManagerImpl.INSTANCE.createLink(last, cart)) {
        player.displayClientMessage(Component.translatable("crowbar.link_failed"), true);
        return;
      }

      if (!player.getLevel().isClientSide()) {
        RailcraftCriteriaTriggers.CART_LINK.trigger((ServerPlayer) player, last, cart);
      }

      player.displayClientMessage(Component.translatable("crowbar.link_created"), true);
    }

    crowbar.onLink(player, hand, stack, cart);
  }

  private void boostCart(Player player, InteractionHand hand, ItemStack stack,
      AbstractMinecart cart, Crowbar crowbar) {
    player.causeFoodExhaustion(.25F);

    if (player.getVehicle() != null) {
      // NOOP
    } else if (cart instanceof TunnelBore) {
      // NOOP
    } else if (cart instanceof IDirectionalCart) {
      ((IDirectionalCart) cart).reverse();
    } else if (cart instanceof TrackRemover) {
      TrackRemover trackRemover = (TrackRemover) cart;
      trackRemover.setMode(trackRemover.getMode().getNext());
    } else {
      int lvl = EnchantmentHelper.getItemEnchantmentLevel(RailcraftEnchantments.SMACK.get(), stack);
      if (lvl == 0) {
        CartTools.smackCart(cart, player, SMACK_VELOCITY);
      }

      Train.get(cart).ifPresent(train -> {
        float smackVelocity = SMACK_VELOCITY * (float) Math.pow(1.7, lvl);
        smackVelocity /= (float) Math.pow(train.size(), 1D / (1 + lvl));
        for (AbstractMinecart each : train) {
          CartTools.smackCart(cart, each, player, smackVelocity);
        }
      });
    }
    crowbar.onBoost(player, hand, stack, cart);
  }
}
