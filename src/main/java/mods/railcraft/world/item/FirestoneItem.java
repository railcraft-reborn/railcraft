package mods.railcraft.world.item;

import java.util.Random;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import mods.railcraft.plugins.WorldPlugin;
import mods.railcraft.util.MiscTools;
import mods.railcraft.util.inventory.InvTools;
import mods.railcraft.world.entity.FirestoneItemEntity;
import mods.railcraft.world.level.block.RailcraftBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * @author CovertJaguar <http://www.railcraft.info/>
 */
public class FirestoneItem extends Item {

  public static final Predicate<ItemStack> SPAWNS_FIRE = stack -> {
    if (stack.isEmpty())
      return false;
    if (RailcraftItems.RAW_FIRESTONE.get() == stack.getItem())
      return true;
    if (RailcraftItems.CUT_FIRESTONE.get() == stack.getItem())
      return true;
    if (RailcraftItems.CRACKED_FIRESTONE.get() == stack.getItem())
      return true;
    return InvTools.isStackEqualToBlock(stack, RailcraftBlocks.FIRESTONE.get());
  };

  public FirestoneItem(Properties properties) {
    super(properties);
  }

  /**
   * Determines if this Item has a special entity for when they are in the world. Is called when a
   * EntityItem is spawned in the world, if true and Item#createCustomEntity returns non null, the
   * EntityItem will be destroyed and the new Entity will be added to the world.
   *
   * @param stack The current item stack
   * @return True of the item has a custom entity, If true, Item#createCustomEntity will be called
   */
  @Override
  public boolean hasCustomEntity(ItemStack stack) {
    return true;
  }

  /**
   * This function should return a new entity to replace the dropped item. Returning null here will
   * not kill the EntityItem and will leave it to function normally. Called when the item it placed
   * in a world.
   *
   * @param world The world object
   * @param original The EntityItem object, useful for getting the position of the entity
   * @param stack The current item stack
   * @return A new Entity object to spawn or null
   */
  @Override
  public FirestoneItemEntity createEntity(World world, Entity original, ItemStack stack) {
    return createEntityItem(world, original, stack);
  }

  /**
   * Called by CraftingManager to determine if an item is reparable.
   *
   * @return Always returns false for ItemFirestoneBase
   */
  @Override
  public boolean isRepairable(ItemStack itemStack) {
    return false;
  }

  public static FirestoneItemEntity createEntityItem(World world, Entity original,
      ItemStack stack) {
    FirestoneItemEntity entity =
        new FirestoneItemEntity(original.getX(), original.getY(), original.getZ(), world, stack);
    entity.setThrower(((ItemEntity) original).getThrower());
    entity.setDeltaMovement(original.getDeltaMovement());
    entity.setDefaultPickUpDelay();
    return entity;
  }

  public static boolean trySpawnFire(World world, BlockPos pos, ItemStack stack,
      PlayerEntity holder) {
    if (stack.isEmpty() || !SPAWNS_FIRE.test(stack))
      return false;
    boolean spawnedFire = false;
    for (int i = 0; i < stack.getCount(); i++) {
      spawnedFire |= spawnFire(world, pos, holder);
    }
    if (spawnedFire && stack.isDamageableItem()
        && stack.getDamageValue() < stack.getMaxDamage() - 1)
      stack.hurtAndBreak(1, holder, __ -> {
      });
    return spawnedFire;
  }

  public static boolean spawnFire(World world, BlockPos pos, @Nullable PlayerEntity holder) {
    Random rnd = MiscTools.RANDOM;
    int x = pos.getX() - 5 + rnd.nextInt(12);
    int y = pos.getY() - 5 + rnd.nextInt(12);
    int z = pos.getZ() - 5 + rnd.nextInt(12);

    if (y < 1)
      y = 1;
    if (y > world.getHeight())
      y = world.getHeight() - 2;

    BlockPos firePos = new BlockPos(x, y, z);
    return canBurn(world, firePos)
        && WorldPlugin.setBlockState(world, firePos, Blocks.FIRE.defaultBlockState(), holder);
  }

  @SuppressWarnings("deprecation")
  private static boolean canBurn(World world, BlockPos pos) {
    if (world.getBlockState(pos).isAir(world, pos))
      return false;
    for (Direction side : Direction.values()) {
      BlockPos offset = pos.relative(side);
      BlockState offsetBlockState = world.getBlockState(offset);
      if (!offsetBlockState.isAir(world, offset)
          && offsetBlockState.getMaterial() != Material.FIRE) {
        return true;
      }
    }
    return false;
  }
}
