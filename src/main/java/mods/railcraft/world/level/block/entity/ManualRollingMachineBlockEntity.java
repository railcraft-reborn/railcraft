package mods.railcraft.world.level.block.entity;

import mods.railcraft.client.Translations;
import mods.railcraft.world.inventory.ManualRollingMachineMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Consumer;

public class ManualRollingMachineBlockEntity extends BaseContainerBlockEntity {

  private int recipieRequiredTime = 12222222;
  private int currentTick = 0;
  private Consumer<Void> callback;
  private boolean shouldFire = false;
  // KEY INFO:
  // 1. required time | 2. currentTick (UNSETTABLE)
  // 3. shouldFire - 1 == true
  protected final ContainerData data = new ContainerData() {
    public int get(int key) {
      return switch (key) {
        case 0 -> ManualRollingMachineBlockEntity.this.recipieRequiredTime;
        case 1 -> ManualRollingMachineBlockEntity.this.currentTick;
        case 2 -> ManualRollingMachineBlockEntity.this.shouldFire ? 1 : 0;
        default -> 0;
      };
    }

    public void set(int key, int value) {
      switch (key) {
        case 0 -> ManualRollingMachineBlockEntity.this.recipieRequiredTime = value;
        case 2 -> {
          if (value != 1) {
            ManualRollingMachineBlockEntity.this.resetProgress();
            break;
          }
          ManualRollingMachineBlockEntity.this.shouldFire = true;
        }
        default -> {
        }
      }
    }

    public int getCount() {
      return 3;
    }
  };

  public ManualRollingMachineBlockEntity(BlockPos blockPos, BlockState blockState) {
    super(RailcraftBlockEntityTypes.MANUAL_ROLLING_MACHINE.get(), blockPos, blockState);
  }

  public ManualRollingMachineBlockEntity(BlockEntityType<?> type, BlockPos blockPos,
      BlockState blockState) {
    super(type, blockPos, blockState);
  }

  public void setRequiredTime(int requiredTime) {
    this.recipieRequiredTime = requiredTime;
  }

  public boolean updateRollingStatus() {
    if (this.rollingProgress() == 1F) {
      this.shouldFire = false;
      if (callback != null) {
        callback.accept(null);
      }
      return true;
    }
    return false;
  }

  public void setOnFinishedCallback(Consumer<Void> callback) {
    this.callback = callback;
  }

  /**
   * Progress of the current recipie, in "float percent" ie: 10% == 0.1, 50% = 0.5%
   *
   * @return The progress, used by
   *         {@link mods.railcraft.client.gui.screen.inventory.ManualRollingMachineScreen
   *         RollingTableScreen}
   */
  public float rollingProgress() {
    return Mth.clamp((float) this.currentTick / (float) this.recipieRequiredTime, 0.0F, 1.0F);
  }

  public void resetProgress() {
    this.shouldFire = false;
    this.currentTick = 0;
  }

  public static void serverTick(Level level, BlockPos blockPos, BlockState blockState,
      ManualRollingMachineBlockEntity blockEntity) {
    if (!blockEntity.shouldFire) {
      return;
    }
    blockEntity.currentTick++;
    blockEntity.updateRollingStatus();
  }

  @Override
  public int getContainerSize() {
    return 10;
  }

  @Override
  public boolean isEmpty() {
    return true;
  }

  @Override
  public ItemStack getItem(int slotID) {
    return ItemStack.EMPTY;
  }

  @Override
  public ItemStack removeItem(int slotID, int count) {
    return ItemStack.EMPTY;
  }

  @Override
  public ItemStack removeItemNoUpdate(int slotID) {
    return ItemStack.EMPTY;
  }

  @Override
  public void setItem(int slotID, ItemStack stack) {
    // nothing here.
  }

  @Override
  public boolean stillValid(Player playerEntity) {
    if (this.level.getBlockEntity(this.worldPosition) != this) {
      return false;
    } else {
      return playerEntity.distanceToSqr(
          (double) this.worldPosition.getX() + 0.5D,
          (double) this.worldPosition.getY() + 0.5D,
          (double) this.worldPosition.getZ() + 0.5D) <= 64.0D;
    }
  }

  @Override
  public void clearContent() {
    // nothing
  }

  @Override
  protected Component getDefaultName() {
    return Component.translatable(Translations.Container.MANUAL_ROLLING_MACHINE);
  }

  @Override
  protected AbstractContainerMenu createMenu(int containerProvider, Inventory playerInventory) {
    return new ManualRollingMachineMenu(containerProvider, playerInventory, this.data, this);
  }
}
