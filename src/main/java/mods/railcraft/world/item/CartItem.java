package mods.railcraft.world.item;

import mods.railcraft.api.item.MinecartFactory;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseRailBlock;

public class CartItem extends Item {

  private final MinecartFactory minecartFactory;

  public CartItem(MinecartFactory minecartFactory, Properties properties) {
    super(properties);
    this.minecartFactory = minecartFactory;
  }

  @Override
  public InteractionResult useOn(UseOnContext context) {
    Player player = context.getPlayer();
    InteractionHand hand = context.getHand();
    Level level = context.getLevel();
    BlockPos pos = context.getClickedPos();
    ItemStack itemStack = player.getItemInHand(hand);
    if (!BaseRailBlock.isRail(level, pos)) {
      return InteractionResult.FAIL;
    }
    if (!level.isClientSide()) {
      AbstractMinecart minecart = this.minecartFactory.createMinecart(
          itemStack, pos.getX(), pos.getY(), pos.getZ(), (ServerLevel) level);
      if (minecart != null) {
        minecart.setYRot(context.getHorizontalDirection().toYRot());
        level.addFreshEntity(minecart);
        itemStack.shrink(1);
      }
    }
    return InteractionResult.sidedSuccess(level.isClientSide());
  }
}
