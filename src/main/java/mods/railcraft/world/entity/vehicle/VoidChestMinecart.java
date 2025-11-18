package mods.railcraft.world.entity.vehicle;

import mods.railcraft.api.carts.RollingStock;
import mods.railcraft.world.entity.RailcraftEntityTypes;
import mods.railcraft.world.item.RailcraftItems;
import mods.railcraft.world.level.block.RailcraftBlocks;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.piglin.PiglinAi;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.state.BlockState;

public class VoidChestMinecart extends RailcraftMinecart {

  private int tick;

  public VoidChestMinecart(EntityType<?> type, Level level) {
    super(type, level);
  }

  public VoidChestMinecart(ItemStack itemStack, double x, double y, double z, Level level) {
    super(itemStack, RailcraftEntityTypes.VOID_CHEST_MINECART.get(), x, y, z, level);
  }

  @Override
  public void tick() {
    super.tick();

    if (this.level().isClientSide()) {
      return;
    }

    if (++this.tick >= 8) {
      this.clearContent();
      this.tick = 0;
    }
  }

  @Override
  protected Item getDropItem() {
    return RailcraftItems.VOID_CHEST_MINECART.get();
  }

  @Override
  public int getContainerSize() {
    return 27;
  }

  @Override
  public BlockState getDefaultDisplayBlockState() {
    return RailcraftBlocks.VOID_CHEST.get().defaultBlockState().setValue(ChestBlock.FACING, Direction.NORTH);
  }

  @Override
  public int getDefaultDisplayOffset() {
    return 8;
  }

  @Override
  protected AbstractContainerMenu createMenu(int id, Inventory playerInventory) {
    return ChestMenu.threeRows(id, playerInventory, this);
  }

  @Override
  public InteractionResult interact(Player player, InteractionHand hand) {
    var result = this.interactWithContainerVehicle(player);
    if (result.consumesAction()) {
      PiglinAi.angerNearbyPiglins(player, true);
    }
    return result;
  }

  @Override
  public boolean canPassItemRequests(ItemStack stack) {
    return true;
  }

  @Override
  public boolean canAcceptPushedItem(RollingStock requester, ItemStack stack) {
    return true;
  }

  @Override
  public boolean canProvidePulledItem(RollingStock requester, ItemStack stack) {
    return true;
  }
}
