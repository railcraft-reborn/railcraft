package mods.railcraft.world.item;

import mods.railcraft.api.item.MinecartFactory;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.BaseRailBlock;

public class CartItem extends Item {

  private final MinecartFactory minecartFactory;

  public CartItem(MinecartFactory minecartFactory, Properties properties) {
    super(properties);
    this.minecartFactory = minecartFactory;
  }

  @Override
  public InteractionResult useOn(UseOnContext context) {
    var player = context.getPlayer();
    var hand = context.getHand();
    var level = context.getLevel();
    var pos = context.getClickedPos();
    var itemStack = player.getItemInHand(hand);
    if (!BaseRailBlock.isRail(level, pos)) {
      return InteractionResult.FAIL;
    }
    if (level instanceof ServerLevel serverLevel) {
      var minecart = this.minecartFactory.createMinecart(
          itemStack, pos.getX(), pos.getY(), pos.getZ(), serverLevel);
      if (minecart != null) {
        minecart.setYRot(context.getHorizontalDirection().toYRot());
        level.addFreshEntity(minecart);
        itemStack.shrink(1);
      }
    }
    return InteractionResult.sidedSuccess(level.isClientSide());
  }
}
