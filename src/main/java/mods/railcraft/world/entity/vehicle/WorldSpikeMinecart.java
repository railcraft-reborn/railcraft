package mods.railcraft.world.entity.vehicle;

import mods.railcraft.world.entity.RailcraftEntityTypes;
import mods.railcraft.world.item.RailcraftItems;
import mods.railcraft.world.level.block.RailcraftBlocks;
import mods.railcraft.world.level.block.entity.worldspike.WorldSpikeBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class WorldSpikeMinecart extends RailcraftMinecart {

  public WorldSpikeMinecart(EntityType<?> type, Level level) {
    super(type, level);
  }

  public WorldSpikeMinecart(ItemStack itemStack, double x, double y, double z, Level level) {
    super(RailcraftEntityTypes.WORLD_SPIKE.get(), x, y, z, level);
  }

  @Override
  public BlockState getDefaultDisplayBlockState() {
    return RailcraftBlocks.WORLD_SPIKE.get().defaultBlockState();
  }

  @Override
  public int getDefaultDisplayOffset() {
    return 6;
  }

  @Override
  public void tick() {
    super.tick();

    if (this.level().isClientSide()) {
      return;
    }
    WorldSpikeBlockEntity.spawnParticle((ServerLevel) this.level(),
        BlockPos.containing(this.getX(), this.getY(), this.getZ()));
  }

  @Override
  public int getContainerSize() {
    return 0;
  }

  @Override
  protected boolean hasMenu() {
    return false;
  }

  @Override
  protected AbstractContainerMenu createMenu(int i, Inventory inventory) {
    return null;
  }

  @Override
  public ItemStack getPickResult() {
    return RailcraftItems.WORLD_SPIKE_MINECART.get().getDefaultInstance();
  }

  @Override
  public Item getDropItem() {
    return RailcraftItems.WORLD_SPIKE_MINECART.get();
  }
}
