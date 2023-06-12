package mods.railcraft.world.item;

import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
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
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class RefinedFirestoneItem extends FirestoneItem {

  public static final int CHARGES = 5000;
  public static final int HEAT = 250;

  protected int heat = HEAT;
  protected final RandomSource random;

  public RefinedFirestoneItem(Properties properties) {
    super(properties);
    this.random = RandomSource.create();
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
  public ItemStack getCraftingRemainingItem(ItemStack itemStack) {
    ItemStack newStack;
    double damageLevel = (double) itemStack.getDamageValue() / (double) itemStack.getMaxDamage();
    if (random.nextDouble() < damageLevel * 0.0001) {
      newStack = CrackedFirestoneItem.getItemEmpty();
      if (itemStack.hasCustomHoverName())
        newStack.setHoverName(itemStack.getHoverName());
    } else
      newStack = itemStack.copy();
    newStack.setCount(1);
    return newStack.hurt(1, random, null) ? ItemStack.EMPTY : newStack;
  }

  @Override
  public final int getBurnTime(ItemStack itemStack, RecipeType<?> recipeType) {
    return itemStack.getDamageValue() < itemStack.getMaxDamage() ? this.heat : 0;
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
    BlockState blockState = level.getBlockState(pos);
    RandomSource random = level.getRandom();

    if (level.isClientSide()) return InteractionResult.CONSUME;
    if (stack.getDamageValue() == stack.getMaxDamage()) return InteractionResult.PASS;

    ServerPlayer serverPlayer = (ServerPlayer) player;

    if (player.mayUseItemAt(pos, side, stack)) {
      if (blockState.getBlock() != Blocks.STONE) {
        var drops = Block.getDrops(blockState, (ServerLevel) level, pos, level.getBlockEntity(pos));
        if (drops.size() == 1 && !drops.get(0).isEmpty() && drops.get(0).getItem() instanceof BlockItem) {
          var cooked = cookedItem(level, drops.get(0));
          if (cooked.getItem() instanceof BlockItem) {
            var newState = ContainerTools.getBlockStateFromStack(cooked, level, pos);
            if (newState != null) {
              level.setBlockAndUpdate(pos, newState);
              player.playSound(SoundEvents.FIRECHARGE_USE, 1.0F, random.nextFloat() * 0.4F + 0.8F);
              stack.hurt(1, random, serverPlayer);
              return InteractionResult.SUCCESS;
            }
          }
        }
      }

      pos = pos.relative(side);

      if (player.mayUseItemAt(pos, side, stack) && level.getBlockState(pos).isAir()) {
        player.playSound(SoundEvents.FIRECHARGE_USE, 1.0F, random.nextFloat() * 0.4F + 0.8F);
        level.setBlockAndUpdate(pos, Blocks.FIRE.defaultBlockState());
        stack.hurt(1, random, serverPlayer);
        return InteractionResult.SUCCESS;
      }
    }
    return InteractionResult.CONSUME;
  }

  @NotNull
  private ItemStack cookedItem(Level level, ItemStack ingredient) {
    return level.getRecipeManager()
        .getRecipeFor(RecipeType.SMELTING, new SimpleContainer(ingredient), level)
        .map(x -> x.getResultItem(level.registryAccess()))
        .orElse(ItemStack.EMPTY);
  }

  @Override
  public InteractionResult interactLivingEntity(ItemStack itemStack, Player player,
      LivingEntity livingEntity, InteractionHand hand) {
    if (player instanceof ServerPlayer && !livingEntity.fireImmune()) {
      livingEntity.setSecondsOnFire(5);
      itemStack.hurtAndBreak(1, player, __ -> player.broadcastBreakEvent(hand));
      player.playSound(SoundEvents.FIRECHARGE_USE, 1.0F,
          player.getRandom().nextFloat() * 0.4F + 0.8F);
      player.swing(hand);
      player.level().setBlockAndUpdate(player.blockPosition(), Blocks.FIRE.defaultBlockState());
      return InteractionResult.SUCCESS;
    }
    return InteractionResult.CONSUME;
  }

  @Override
  @NotNull
  public FirestoneItemEntity createEntity(Level level, Entity entity, ItemStack itemStack) {
    var firestone = super.createEntity(level, entity, itemStack);
    firestone.setRefined(true);
    return firestone;
  }
}
