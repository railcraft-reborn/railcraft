package mods.railcraft.world.item;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;
import javax.annotation.Nullable;
import mods.railcraft.Translations.Tips;
import mods.railcraft.util.container.ContainerTools;
import mods.railcraft.world.entity.FirestoneItemEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.loot.LootContext;

/**
 * @author CovertJaguar <https://www.railcraft.info/>
 */
public class RefinedFirestoneItem extends FirestoneItem {

  public static final int CHARGES = 5000;
  public static final int HEAT = 250;

  protected int heat = HEAT;

  public RefinedFirestoneItem(Properties properties) {
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
  public boolean hasCraftingRemainingItem(ItemStack stack) {
    return true;
  }

  @Override
  public ItemStack getCraftingRemainingItem(ItemStack stack) {
    ItemStack newStack;
    double damageLevel = (double) stack.getDamageValue() / (double) stack.getMaxDamage();
    if (ThreadLocalRandom.current().nextDouble() < damageLevel * 0.0001) {
      newStack = CrackedFirestoneItem.getItemEmpty();
      if (stack.hasCustomHoverName())
        newStack.setHoverName(stack.getHoverName());
    } else
      newStack = stack.copy();
    newStack.setCount(1);
    return newStack.hurt(1, RandomSource.create(), null) ? ItemStack.EMPTY : newStack;
  }

  @Override
  public final int getBurnTime(ItemStack stack, RecipeType<?> recipeType) {
    return stack.getDamageValue() < stack.getMaxDamage() ? this.heat : 0;
  }

  @Override
  public void appendHoverText(ItemStack itemStack, @Nullable Level level,
      List<Component> lines, TooltipFlag adv) {
    MutableComponent component;
    if(itemStack.getDamageValue() >= itemStack.getMaxDamage() - 5) {
      component = Component.translatable(Tips.FIRESTONE_EMPTY);
    } else {
      component = Component.translatable(Tips.FIRESTONE_CHARGED);
    }
    lines.add(component.withStyle(ChatFormatting.GRAY));
  }

  @Override
  public InteractionResult useOn(UseOnContext context) {
    Player player = context.getPlayer();
    ItemStack stack = context.getItemInHand();
    Level level = context.getLevel();
    BlockPos pos = context.getClickedPos();
    Direction side = context.getClickedFace();

    if (!level.isClientSide() && player.mayUseItemAt(pos, side, stack)) {
      var blockState = level.getBlockState(pos);
      if (blockState.getBlock() != Blocks.STONE) {
        var drops = blockState.getDrops(new LootContext.Builder((ServerLevel) level));
        if (drops.size() == 1 && !drops.get(0).isEmpty()
            && drops.get(0).getItem() instanceof BlockItem) {
          var cooked = level.getRecipeManager()
              .getRecipeFor(RecipeType.SMELTING, new SimpleContainer(drops.get(0)), level)
              .map(SmeltingRecipe::getResultItem)
              .orElse(ItemStack.EMPTY);
          if (cooked.getItem() instanceof BlockItem) {
            var newState = ContainerTools.getBlockStateFromStack(cooked, level, pos);
            if (newState != null) {
              level.setBlockAndUpdate(pos, newState);
              player.playSound(SoundEvents.FIRECHARGE_USE, 1.0F,
                  player.getRandom().nextFloat() * 0.4F + 0.8F);
              stack.hurt(1, player.getRandom(), (ServerPlayer) player);
              return InteractionResult.SUCCESS;
            }
          }
        }
      }

      pos = pos.relative(side);

      if (player.mayUseItemAt(pos, side, stack) && level.getBlockState(pos).isAir()) {
        player.playSound(SoundEvents.FIRECHARGE_USE, 1.0F,
            player.getRandom().nextFloat() * 0.4F + 0.8F);
        level.setBlockAndUpdate(pos, Blocks.FIRE.defaultBlockState());
        stack.hurt(1, player.getRandom(), (ServerPlayer) player);
        return InteractionResult.SUCCESS;
      }
    }

    return InteractionResult.CONSUME;
  }

  @Override
  public InteractionResult interactLivingEntity(ItemStack stack, Player player,
      LivingEntity target, InteractionHand hand) {
    if (player instanceof ServerPlayer && !target.fireImmune()) {
      target.setSecondsOnFire(5);
      stack.hurtAndBreak(1, player, __ -> player.broadcastBreakEvent(hand));
      player.playSound(SoundEvents.FIRECHARGE_USE, 1.0F,
          player.getRandom().nextFloat() * 0.4F + 0.8F);
      player.swing(hand);
      player.level.setBlockAndUpdate(player.blockPosition(), Blocks.FIRE.defaultBlockState());
      return InteractionResult.SUCCESS;
    }
    return InteractionResult.CONSUME;
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
  public FirestoneItemEntity createEntity(Level world, Entity location, ItemStack stack) {
    FirestoneItemEntity entity = super.createEntity(world, location, stack);
    Objects.requireNonNull(entity).setRefined(true);
    return entity;
  }
}
