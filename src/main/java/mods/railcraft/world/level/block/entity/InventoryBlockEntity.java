package mods.railcraft.world.level.block.entity;

import java.util.EnumMap;
import java.util.Map;
import javax.annotation.Nullable;
import mods.railcraft.util.inventory.ForwardingInventory;
import mods.railcraft.util.inventory.InventoryAdvanced;
import mods.railcraft.util.inventory.ItemHandlerFactory;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

public abstract class InventoryBlockEntity extends RailcraftBlockEntity
    implements ForwardingInventory {

  private InventoryAdvanced inventory;
  private LazyOptional<IItemHandler> itemHandler = LazyOptional.of(() -> new InvWrapper(this));
  private Map<Direction, LazyOptional<IItemHandler>> directionalItemHandlers =
      new EnumMap<>(Direction.class);

  public InventoryBlockEntity(TileEntityType<?> type) {
    super(type);
    this.inventory = new InventoryAdvanced(0).callbackInv(this);

  }

  protected InventoryBlockEntity(TileEntityType<?> type, int size) {
    super(type);
    this.inventory = new InventoryAdvanced(size).callbackInv(this);
  }

  protected void setInventorySize(int size) {
    this.inventory = new InventoryAdvanced(size).callbackInv(this);
  }

  @Override
  public boolean stillValid(PlayerEntity player) {
    return RailcraftBlockEntity.stillValid(this, player);
  }

  @Override
  public IInventory getInventory() {
    return this.inventory;
  }

  @Override
  public <T> LazyOptional<T> getCapability(Capability<T> capability,
      @Nullable Direction direction) {
    if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
      return direction == null
          ? this.itemHandler.cast()
          : this.directionalItemHandlers.computeIfAbsent(direction,
              __ -> LazyOptional.of(() -> ItemHandlerFactory.wrap(this, direction))).cast();
    }
    return super.getCapability(capability, direction);
  }

  @Override
  public void load(BlockState blockState, CompoundNBT data) {
    super.load(blockState, data);
    data.put("inventory", this.inventory.serializeNBT());
  }

  @Override
  public CompoundNBT save(CompoundNBT data) {
    super.save(data);
    this.inventory.deserializeNBT(data.getList("inventory", Constants.NBT.TAG_COMPOUND));
    return data;
  }
}
