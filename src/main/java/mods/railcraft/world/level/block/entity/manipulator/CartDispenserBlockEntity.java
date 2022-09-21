package mods.railcraft.world.level.block.entity.manipulator;

import mods.railcraft.util.container.manipulator.ContainerManipulator;
import mods.railcraft.world.inventory.CartDispenserMenu;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class CartDispenserBlockEntity extends ManipulatorBlockEntity implements MenuProvider {

  protected boolean powered;
  protected int timeSinceLastSpawn;

  public CartDispenserBlockEntity(BlockPos pos, BlockState state) {
    super(RailcraftBlockEntityTypes.CART_DISPENSER.get(), pos, state);
  }

  /*public static void serverTick(Level level, BlockPos blockPos, BlockState blockState,
      CartDispenserBlockEntity blockEntity) {
    if (blockEntity.timeSinceLastSpawn < Integer.MAX_VALUE) {
      blockEntity.timeSinceLastSpawn++;
    }
  }*/



  @Override
  protected boolean hasWorkForCart(AbstractMinecart cart) {
    return false;
  }

  @Override
  protected void processCart(AbstractMinecart cart) {

  }

  @Nullable
  @Override
  public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
    return new CartDispenserMenu(id, inventory, this);
  }

  @Override
  protected void saveAdditional(CompoundTag tag) {
    super.saveAdditional(tag);
    tag.putBoolean("powered", this.powered);
    tag.putInt("timeSinceLastSpawn", this.timeSinceLastSpawn);
  }

  @Override
  public void load(CompoundTag tag) {
    super.load(tag);
    this.powered = tag.getBoolean("powered");
    this.timeSinceLastSpawn = tag.getInt("timeSinceLastSpawn");
  }

  @Override
  public void writeToBuf(FriendlyByteBuf data) {
    super.writeToBuf(data);
    data.writeBoolean(this.powered);
    data.writeInt(this.timeSinceLastSpawn);
  }

  @Override
  public void readFromBuf(FriendlyByteBuf data) {
    super.readFromBuf(data);
    this.powered = data.readBoolean();
    this.timeSinceLastSpawn = data.readInt();
  }
}
