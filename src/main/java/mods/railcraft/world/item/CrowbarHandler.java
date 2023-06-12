package mods.railcraft.world.item;

import java.util.Map;
import com.google.common.collect.MapMaker;
import mods.railcraft.RailcraftConfig;
import mods.railcraft.Translations;
import mods.railcraft.advancements.RailcraftCriteriaTriggers;
import mods.railcraft.api.carts.RollingStock;
import mods.railcraft.api.item.Crowbar;
import mods.railcraft.world.entity.vehicle.CartTools;
import mods.railcraft.world.entity.vehicle.Directional;
import mods.railcraft.world.entity.vehicle.SeasonalCart;
import mods.railcraft.world.entity.vehicle.TrackRemover;
import mods.railcraft.world.entity.vehicle.TunnelBore;
import mods.railcraft.world.item.enchantment.RailcraftEnchantments;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.item.ItemStack;

/**
 * @author CovertJaguar (https://www.railcraft.info)
 */
public class CrowbarHandler {

  public static final float SMACK_VELOCITY = 0.07f;

  private static final Map<Player, RollingStock> linkMap =
      new MapMaker()
          .weakKeys()
          .weakValues()
          .makeMap();

  public InteractionResult handleInteract(AbstractMinecart cart, Player player,
      InteractionHand hand) {
    ItemStack stack = player.getItemInHand(hand);
    if (stack.isEmpty() || !(stack.getItem() instanceof Crowbar crowbar)) {
      return InteractionResult.PASS;
    }

    if (player.level().isClientSide()) {
      return InteractionResult.SUCCESS;
    }

    if ((stack.getItem() instanceof SeasonsCrowbarItem)
        && cart instanceof SeasonalCart seasonalCart
        && RailcraftConfig.COMMON.seasonsEnabled.get()) {
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
    var extension = RollingStock.getOrThrow(cart);
    var last = linkMap.remove(player);
    if (last == null || !last.entity().isAlive()) {
      linkMap.put(player, extension);
      var message = Component.translatable(Translations.Tips.CROWBAR_LINK_STARTED)
          .withStyle(ChatFormatting.LIGHT_PURPLE);
      player.displayClientMessage(message, true);
      return;
    }

    if (extension.unlink(last)) {
      var message = Component.translatable(Translations.Tips.CROWBAR_LINK_BROKEN)
          .withStyle(ChatFormatting.LIGHT_PURPLE);
      player.displayClientMessage(message, true);
    } else {
      if (!last.link(extension)) {
        var message = Component.translatable(Translations.Tips.CROWBAR_LINK_FAILED)
            .withStyle(ChatFormatting.RED);
        player.displayClientMessage(message, true);
        return;
      }

      if (!player.level().isClientSide()) {
        RailcraftCriteriaTriggers.CART_LINK.trigger((ServerPlayer) player, last.entity(), cart);
      }

      var message = Component.translatable(Translations.Tips.CROWBAR_LINK_CREATED)
          .withStyle(ChatFormatting.GREEN);
      player.displayClientMessage(message, true);
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
    } else if (cart instanceof Directional directional) {
      directional.reverse();
    } else if (cart instanceof TrackRemover trackRemover) {
      trackRemover.setMode(trackRemover.getMode().getNext());
    } else {
      int lvl = stack.getEnchantmentLevel(RailcraftEnchantments.SMACK.get());
      if (lvl == 0) {
        CartTools.smackCart(cart, player, SMACK_VELOCITY);
      }
      var extension = RollingStock.getOrThrow(cart);
      var train = extension.train();
      var smackVelocity = (SMACK_VELOCITY * (float) Math.pow(1.7, lvl))
          / (float) Math.pow(train.size(), 1D / (1 + lvl));
      train.stream().forEach(
          each -> CartTools.smackCart(cart, each.entity(), player, smackVelocity));
    }
    crowbar.onBoost(player, hand, stack, cart);
  }
}
