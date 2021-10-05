package mods.railcraft.world.item;

import java.util.Map;
import com.google.common.collect.MapMaker;
import mods.railcraft.RailcraftConfig;
import mods.railcraft.advancements.criterion.RailcraftAdvancementTriggers;
import mods.railcraft.api.carts.ILinkableCart;
import mods.railcraft.api.item.Crowbar;
import mods.railcraft.carts.CartTools;
import mods.railcraft.carts.LinkageManager;
import mods.railcraft.carts.RailcraftCart;
import mods.railcraft.carts.Train;
import mods.railcraft.season.Season;
import mods.railcraft.world.entity.IDirectionalCart;
import mods.railcraft.world.entity.TrackRemoverMinecartEntity;
import mods.railcraft.world.entity.TunnelBoreEntity;
import mods.railcraft.world.item.enchantment.RailcraftEnchantments;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.text.TranslationTextComponent;

/**
 * @author CovertJaguar <http://www.railcraft.info>
 */
public class CrowbarHandler {

  public static final float SMACK_VELOCITY = 0.07f;

  private static final Map<PlayerEntity, AbstractMinecartEntity> linkMap =
      new MapMaker()
          .weakKeys()
          .weakValues()
          .makeMap();

  public boolean handleInteract(AbstractMinecartEntity cart, PlayerEntity player, Hand hand) {
    boolean cancel = false;

    ItemStack stack = player.getItemInHand(hand);
    if (stack.getItem() instanceof Crowbar)
      cancel = true;

    if (!stack.isEmpty() && stack.getItem() instanceof Crowbar) {
      player.swing(hand);
      cancel = true;
    } else
      return cancel;

    if (player.level.isClientSide())
      return cancel;

    Crowbar crowbar = (Crowbar) stack.getItem();

    if ((stack.getItem() instanceof SeasonCrowbarItem) && (cart instanceof RailcraftCart)
        && RailcraftConfig.COMMON.enableSeasons.get()) {
      Season season = SeasonCrowbarItem.getCurrentSeason(stack);
      ((RailcraftCart) cart).setSeason(season);
      RailcraftAdvancementTriggers.getInstance()
        .onSeasonSet((ServerPlayerEntity) player, cart, season);
      return true;
    }
    if (crowbar.canLink(player, hand, stack, cart)) {
      linkCart(player, hand, stack, cart, crowbar);
      return true;
    } else if (crowbar.canBoost(player, hand, stack, cart)) {
      boostCart(player, hand, stack, cart, crowbar);
      return true;
    }

    return cancel;
  }

  private void linkCart(PlayerEntity player, Hand hand, ItemStack stack,
      AbstractMinecartEntity cart, Crowbar crowbar) {
    boolean used = false;
    boolean linkable = cart instanceof ILinkableCart;
    if (!linkable || ((ILinkableCart) cart).isLinkable()) {
      AbstractMinecartEntity last = linkMap.remove(player);
      if (last != null && last.isAlive()) {
        LinkageManager lm = LinkageManager.INSTANCE;
        if (lm.areLinked(cart, last, false)) {
          lm.breakLink(cart, last);
          used = true;
          player.displayClientMessage(
              new TranslationTextComponent("message.link.broken"), false);
          LinkageManager.printDebug("Reason For Broken Link: User removed link.");
        } else {
          used = lm.createLink(last, cart);
          if (used)
            player.displayClientMessage(
                new TranslationTextComponent("message.link.created"), false);
        }
        if (!used)
          player.displayClientMessage(
              new TranslationTextComponent("message.link.failed"), false);
      } else {
        linkMap.put(player, cart);
        player.displayClientMessage(
            new TranslationTextComponent("message.link.started"), false);
      }
    }
    if (used)
      crowbar.onLink(player, hand, stack, cart);
  }

  private void boostCart(PlayerEntity player, Hand hand, ItemStack stack,
      AbstractMinecartEntity cart,
      Crowbar crowbar) {
    player.causeFoodExhaustion(.25F);

    if (player.getVehicle() != null) {
      // NOOP
    } else if (cart instanceof TunnelBoreEntity) {
      // NOOP
    } else if (cart instanceof IDirectionalCart)
      ((IDirectionalCart) cart).reverse();
    else if (cart instanceof TrackRemoverMinecartEntity) {
      ((TrackRemoverMinecartEntity) cart)
          .setMode(((TrackRemoverMinecartEntity) cart).getOtherMode());
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
