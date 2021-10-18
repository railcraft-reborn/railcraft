package mods.railcraft.world.item;

import mods.railcraft.api.item.MinecartFactory;
import net.minecraft.block.AbstractRailBlock;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

public class CartItem extends Item {

  private final MinecartFactory minecartFactory;

  public CartItem(MinecartFactory minecartFactory, Properties properties) {
    super(properties);
    this.minecartFactory = minecartFactory;
  }

  @Override
  public ActionResultType useOn(ItemUseContext context) {
    PlayerEntity player = context.getPlayer();
    Hand hand = context.getHand();
    World level = context.getLevel();
    BlockPos pos = context.getClickedPos();
    ItemStack itemStack = player.getItemInHand(hand);
    if (!AbstractRailBlock.isRail(level, pos)) {
      return ActionResultType.FAIL;
    }
    if (!level.isClientSide()) {
      AbstractMinecartEntity minecart = this.minecartFactory.createMinecart(
          itemStack, pos.getX(), pos.getY(), pos.getZ(), (ServerWorld) level);
      if (minecart != null) {
        minecart.yRot = context.getHorizontalDirection().toYRot();
        level.addFreshEntity(minecart);
        itemStack.shrink(1);
      }
    }
    return ActionResultType.sidedSuccess(level.isClientSide());
  }
}
