package mods.railcraft.world.item;

import java.util.List;
import java.util.Objects;
import javax.annotation.Nullable;
import mods.railcraft.plugins.WorldPlugin;
import mods.railcraft.util.MiscTools;
import mods.railcraft.util.inventory.InvTools;
import mods.railcraft.world.entity.FirestoneItemEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.item.crafting.FurnaceRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.loot.LootContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

/**
 * @author CovertJaguar <http://www.railcraft.info/>
 */
public class ItemFirestoneRefined extends ItemFirestone {

  public static final int CHARGES = 5000;
  public static final int HEAT = 250;

  protected int heat = HEAT;

  public ItemFirestoneRefined(Properties properties) {
    super(properties);
  }

  public static ItemStack getItemCharged() {
    return RailcraftItems.REFINED_FIRESTONE.get().getDefaultInstance();
  }

  public static ItemStack getItemEmpty() {
    ItemStack itemStack = RailcraftItems.REFINED_FIRESTONE.get().getDefaultInstance();
    itemStack.setDamageValue(CHARGES - 1);
    return itemStack;
  }

  @Override
  public boolean hasContainerItem(ItemStack stack) {
    return true;
  }

  @Override
  public ItemStack getContainerItem(ItemStack stack) {
    ItemStack newStack;
    double damageLevel = (double) stack.getDamageValue() / (double) stack.getMaxDamage();
    if (MiscTools.RANDOM.nextDouble() < damageLevel * 0.0001) {
      newStack = ItemFirestoneCracked.getItemEmpty();
      if (stack.hasCustomHoverName())
        newStack.setHoverName(stack.getHoverName());
    } else
      newStack = stack.copy();
    InvTools.setSize(newStack, 1);
    return newStack.hurt(1, random, null) ? ItemStack.EMPTY : newStack;
  }

  @Override
  public final int getBurnTime(ItemStack stack) {
    return stack.getDamageValue() < stack.getMaxDamage() ? this.heat : 0;
  }

  @Override
  public void appendHoverText(
      ItemStack stack, @Nullable World world, List<ITextComponent> info, ITooltipFlag adv) {
    info.add(new TranslationTextComponent(stack.getDamageValue() >= stack.getMaxDamage() - 5
        ? "item.railcraft.firestone.empty"
        : "item.railcraft.firestone.charged"));
  }

  @Override
  public ActionResultType useOn(ItemUseContext context) {
    PlayerEntity player = context.getPlayer();
    ItemStack stack = context.getItemInHand();
    World world = context.getLevel();
    BlockPos pos = context.getClickedPos();
    Direction side = context.getClickedFace();

    if (!world.isClientSide()) {
      if (player.mayUseItemAt(pos, side, stack)) {
        BlockState blockState = world.getBlockState(pos);
        if (blockState.getBlock() != Blocks.STONE) {
          List<ItemStack> drops =
              blockState.getDrops(new LootContext.Builder((ServerWorld) world));
          if (drops.size() == 1 && !drops.get(0).isEmpty()
              && drops.get(0).getItem() instanceof BlockItem) {
            ItemStack cooked = world.getRecipeManager()
                .getRecipeFor(IRecipeType.SMELTING, new Inventory(drops.get(0)), world)
                .map(FurnaceRecipe::getResultItem)
                .orElse(ItemStack.EMPTY);
            if (cooked.getItem() instanceof BlockItem) {
              BlockState newState = InvTools.getBlockStateFromStack(cooked, world, pos);
              if (newState != null) {
                world.setBlockAndUpdate(pos, newState);
                player.playSound(SoundEvents.FIRECHARGE_USE, 1.0F,
                    random.nextFloat() * 0.4F + 0.8F);
                stack.hurt(1, random, (ServerPlayerEntity) player);
                return ActionResultType.SUCCESS;
              }
            }
          }
        }
      }

      pos = pos.relative(side);

      if (player.mayUseItemAt(pos, side, stack) && WorldPlugin.isAir(world, pos)) {
        player.playSound(SoundEvents.FIRECHARGE_USE, 1.0F, random.nextFloat() * 0.4F + 0.8F);
        world.setBlockAndUpdate(pos, Blocks.FIRE.defaultBlockState());
        stack.hurt(1, random, (ServerPlayerEntity) player);
        return ActionResultType.SUCCESS;
      }
    }

    return ActionResultType.CONSUME;
  }

  @Override
  public ActionResultType interactLivingEntity(ItemStack stack, PlayerEntity playerIn,
      LivingEntity target, Hand hand) {
    if (playerIn instanceof ServerPlayerEntity && !target.fireImmune()) {
      target.setSecondsOnFire(5);
      stack.hurtAndBreak(1, playerIn, __ -> playerIn.broadcastBreakEvent(hand));
      playerIn.playSound(SoundEvents.FIRECHARGE_USE, 1.0F, random.nextFloat() * 0.4F + 0.8F);
      playerIn.swing(hand);
      playerIn.level.setBlockAndUpdate(playerIn.blockPosition(), Blocks.FIRE.defaultBlockState());
      return ActionResultType.SUCCESS;
    }
    return ActionResultType.CONSUME;
  }

  /**
   * This function should return a new entity to replace the dropped item. Returning null here will
   * not kill the EntityItem and will leave it to function normally. Called when the item it placed
   * in a world.
   *
   * @param world The world object
   * @param location The EntityItem object, useful for getting the position of the entity
   * @param stack The current item stack
   * @return A new Entity object to spawn or null
   */
  @Override
  public FirestoneItemEntity createEntity(World world, Entity location, ItemStack stack) {
    FirestoneItemEntity entity = super.createEntity(world, location, stack);
    Objects.requireNonNull(entity).setRefined(true);
    return entity;
  }
}
