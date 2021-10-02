package mods.railcraft.world.item;

import java.util.List;
import java.util.function.Supplier;
import javax.annotation.Nullable;
import com.mojang.authlib.GameProfile;
import mods.railcraft.api.item.MinecartPlacer;
import mods.railcraft.carts.CartTools;
import mods.railcraft.util.inventory.InvTools;
import mods.railcraft.world.entity.AbstractFilteredMinecartEntity;
import net.minecraft.block.AbstractRailBlock;
import net.minecraft.block.DispenserBlock;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.item.MinecartItem;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

public class CartItem extends MinecartItem implements MinecartPlacer {

  private final Supplier<? extends EntityType<?>> type;

  public CartItem(Supplier<? extends EntityType<?>> type, Properties properties) {
    super(AbstractMinecartEntity.Type.RIDEABLE, properties);
    // Rest the dispense behaviour set by MinecartItem
    DispenserBlock.registerBehavior(this, new DefaultDispenseItemBehavior());

    this.type = type;
  }

  @Override
  public ActionResultType useOn(ItemUseContext context) {
    PlayerEntity player = context.getPlayer();
    Hand hand = context.getHand();
    World world = context.getLevel();
    BlockPos pos = context.getClickedPos();
    ItemStack stack = player.getItemInHand(hand);
    if (!AbstractRailBlock.isRail(world, pos))
      return ActionResultType.FAIL;
    if (!world.isClientSide()) {
      AbstractMinecartEntity placedCart =
          placeCart(player.getGameProfile(), stack, (ServerWorld) world, pos);
      if (placedCart != null) {
        InvTools.dec(stack);
      }
    }
    return ActionResultType.sidedSuccess(world.isClientSide());
  }

  @Override
  public boolean canBePlacedByNonPlayer(ItemStack cart) {
    return true;
  }

  @Override
  @Nullable
  public AbstractMinecartEntity placeCart(GameProfile owner, ItemStack cartStack, ServerWorld world,
      BlockPos pos) {
    return CartTools.placeCart(type.get(), owner, cartStack, world, pos);
  }

  @Override
  public void appendHoverText(ItemStack stack, @Nullable World world, List<ITextComponent> info,
      ITooltipFlag flag) {
    ItemStack filter = AbstractFilteredMinecartEntity.getFilterFromCartItem(stack);
    if (!filter.isEmpty()) {
      info.add(new TranslationTextComponent("gui.railcraft.filter").append(": ")
          .append(filter.getDisplayName()).withStyle(TextFormatting.BLUE));
    }
  }
}
