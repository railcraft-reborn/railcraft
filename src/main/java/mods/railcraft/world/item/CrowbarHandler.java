package mods.railcraft.world.item;

import java.util.Map;
import com.google.common.collect.MapMaker;
import mods.railcraft.Railcraft;
import mods.railcraft.advancements.criterion.RailcraftAdvancementTriggers;
import mods.railcraft.api.carts.ILinkableCart;
import mods.railcraft.api.items.IToolCrowbar;
import mods.railcraft.carts.CartTools;
import mods.railcraft.carts.IRailcraftCart;
import mods.railcraft.carts.LinkageManager;
import mods.railcraft.carts.Train;
import mods.railcraft.plugins.SeasonPlugin;
import mods.railcraft.util.inventory.InvTools;
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
    if (stack.getItem() instanceof IToolCrowbar)
      cancel = true;

    if (!InvTools.isEmpty(stack) && stack.getItem() instanceof IToolCrowbar) {
      player.swing(hand);
      cancel = true;
    } else
      return cancel;

    if (player.level.isClientSide())
      return cancel;

    IToolCrowbar crowbar = (IToolCrowbar) stack.getItem();

    if (stack.getItem() instanceof SeasonCrowbarItem && cart instanceof IRailcraftCart
        && Railcraft.commonConfig.enableSeasons.get()) {
      cancel = true;
      SeasonPlugin.Season season = SeasonCrowbarItem.getCurrentSeason(stack);
      ((IRailcraftCart) cart).setSeason(season);
      RailcraftAdvancementTriggers.getInstance().onSeasonSet((ServerPlayerEntity) player, cart,
          season);
    } else if (crowbar.canLink(player, hand, stack, cart)) {
      cancel = true;
      linkCart(player, hand, stack, cart, crowbar);
    } else if (crowbar.canBoost(player, hand, stack, cart)) {
      cancel = true;
      boostCart(player, hand, stack, cart, crowbar);
    }

    return cancel;
  }

  private void linkCart(PlayerEntity player, Hand hand, ItemStack stack,
      AbstractMinecartEntity cart, IToolCrowbar crowbar) {
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
      IToolCrowbar crowbar) {
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
