package mods.railcraft.world.item;

import java.util.Map;
import com.google.common.collect.MapMaker;
import mods.railcraft.RailcraftConfig;
import mods.railcraft.advancements.criterion.RailcraftCriteriaTriggers;
import mods.railcraft.api.carts.ILinkableCart;
import mods.railcraft.api.item.Crowbar;
import mods.railcraft.season.Season;
import mods.railcraft.world.entity.cart.CartTools;
import mods.railcraft.world.entity.cart.IDirectionalCart;
import mods.railcraft.world.entity.cart.RailcraftLinkageManager;
import mods.railcraft.world.entity.cart.SeasonalCart;
import mods.railcraft.world.entity.cart.TrackRemoverMinecartEntity;
import mods.railcraft.world.entity.cart.Train;
import mods.railcraft.world.entity.cart.TunnelBoreEntity;
import mods.railcraft.world.item.enchantment.RailcraftEnchantments;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.text.TranslationTextComponent;

/**
 * @author CovertJaguar (https://www.railcraft.info)
 */
public class CrowbarHandler {

  public static final float SMACK_VELOCITY = 0.07f;

  private static final Map<PlayerEntity, AbstractMinecartEntity> linkMap =
      new MapMaker()
          .weakKeys()
          .weakValues()
          .makeMap();

  public ActionResultType handleInteract(AbstractMinecartEntity cart, PlayerEntity player,
      Hand hand) {
    ItemStack stack = player.getItemInHand(hand);
    if (stack.isEmpty() || !(stack.getItem() instanceof Crowbar)) {
      return ActionResultType.PASS;
    }

    if (player.level.isClientSide()) {
      return ActionResultType.SUCCESS;
    }

    Crowbar crowbar = (Crowbar) stack.getItem();

    if ((stack.getItem() instanceof SeasonsCrowbarItem) && (cart instanceof SeasonalCart)
        && RailcraftConfig.common.enableSeasons.get()) {
      Season season = SeasonsCrowbarItem.getSeason(stack);
      ((SeasonalCart) cart).setSeason(season);
      RailcraftCriteriaTriggers.SEASON_SET.trigger((ServerPlayerEntity) player, cart, season);
      return ActionResultType.CONSUME;
    }

    if (crowbar.canLink(player, hand, stack, cart)) {
      this.linkCart(player, hand, stack, cart, crowbar);
      return ActionResultType.CONSUME;
    } else if (crowbar.canBoost(player, hand, stack, cart)) {
      this.boostCart(player, hand, stack, cart, crowbar);
      return ActionResultType.CONSUME;
    }

    return ActionResultType.PASS;
  }

  private void linkCart(PlayerEntity player, Hand hand, ItemStack stack,
      AbstractMinecartEntity cart, Crowbar crowbar) {
    boolean used = false;
    boolean linkable = cart instanceof ILinkableCart;

    if (!linkable || ((ILinkableCart) cart).isLinkable()) {
      AbstractMinecartEntity last = linkMap.remove(player);
      if (last != null && last.isAlive()) {
        RailcraftLinkageManager lm = RailcraftLinkageManager.INSTANCE;
        if (lm.areLinked(cart, last, false)) {
          lm.breakLink(cart, last);
          used = true;
          player.displayClientMessage(new TranslationTextComponent("crowbar.link_broken"), true);
          RailcraftLinkageManager.printDebug("Reason For Broken Link: User removed link.");
        } else {
          used = lm.createLink(last, cart);
          if (used) {
            if (!player.level.isClientSide()) {
              RailcraftCriteriaTriggers.CART_LINK.trigger((ServerPlayerEntity) player, last, cart);
            }
            player.displayClientMessage(new TranslationTextComponent("crowbar.link_created"), true);
          }
        }
        if (!used) {
          player.displayClientMessage(new TranslationTextComponent("crowbar.link_failed"), true);
        }
      } else {
        linkMap.put(player, cart);
        player.displayClientMessage(new TranslationTextComponent("crowbar.link_started"), true);
      }
    }
    if (used) {
      crowbar.onLink(player, hand, stack, cart);
    }
  }

  private void boostCart(PlayerEntity player, Hand hand, ItemStack stack,
      AbstractMinecartEntity cart, Crowbar crowbar) {
    player.causeFoodExhaustion(.25F);

    if (player.getVehicle() != null) {
      // NOOP
    } else if (cart instanceof TunnelBoreEntity) {
      // NOOP
    } else if (cart instanceof IDirectionalCart) {
      ((IDirectionalCart) cart).reverse();
    } else if (cart instanceof TrackRemoverMinecartEntity) {
      TrackRemoverMinecartEntity trackRemover = (TrackRemoverMinecartEntity) cart;
      trackRemover.setMode(trackRemover.getMode().getNext());
    } else {
      int lvl = EnchantmentHelper.getItemEnchantmentLevel(RailcraftEnchantments.SMACK.get(), stack);
      if (lvl == 0) {
        CartTools.smackCart(cart, player, SMACK_VELOCITY);
      }

      Train.get(cart).ifPresent(train -> {
        float smackVelocity = SMACK_VELOCITY * (float) Math.pow(1.7, lvl);
        smackVelocity /= (float) Math.pow(train.size(), 1D / (1 + lvl));
        for (AbstractMinecartEntity each : train) {
          CartTools.smackCart(cart, each, player, smackVelocity);
        }
      });
    }
    crowbar.onBoost(player, hand, stack, cart);
  }
}
