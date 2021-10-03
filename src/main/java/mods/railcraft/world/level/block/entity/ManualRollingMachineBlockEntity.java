package mods.railcraft.world.level.block.entity;

import java.util.function.Consumer;
import mods.railcraft.world.item.crafting.ManualRollingMachineMenu;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.LockableTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.IIntArray;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class ManualRollingMachineBlockEntity extends LockableTileEntity
    implements ITickableTileEntity {

  private static final ITextComponent MENU_TITLE =
      new TranslationTextComponent("container.manual_rolling_machine");

  private int recipieRequiredTime = 12222222;
  private int currentTick = 0;
  private Consumer<Void> callback;
  private boolean shouldFire = false;
  // KEY INFO:
  // 1. required time | 2. currentTick (UNSETTABLE)
  // 3. shouldFire - 1 == true
  protected final IIntArray data = new IIntArray() {
    public int get(int key) {
      switch (key) {
        case 0:
          return ManualRollingMachineBlockEntity.this.recipieRequiredTime;
        case 1:
          return ManualRollingMachineBlockEntity.this.currentTick;
        case 2:
          return ManualRollingMachineBlockEntity.this.shouldFire ? 1 : 0;
        default:
          return 0;
      }
    }

    public void set(int key, int value) {
      switch (key) {
        case 0:
          ManualRollingMachineBlockEntity.this.recipieRequiredTime = value;
          break;
        case 1:
          break;
        case 2:
          if (value != 1) {
            ManualRollingMachineBlockEntity.this.resetProgress();
            break;
          }
          ManualRollingMachineBlockEntity.this.shouldFire = true;
          break;
        default:
          break;
      }
    }

    public int getCount() {
      return 3;
    }
  };

  public ManualRollingMachineBlockEntity() {
    super(RailcraftBlockEntityTypes.MANUAL_ROLLING_MACHINE.get());
  }

  public ManualRollingMachineBlockEntity(TileEntityType<?> type) {
    super(type);
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
    return Math.max(Math.min((float) currentTick / (float) recipieRequiredTime, 1F), 0.0F);
  }

  public void resetProgress() {
    this.shouldFire = false;
    this.currentTick = 0;
  }

  @Override
  public void tick() {
    if (!this.shouldFire) {
      return;
    }
    this.currentTick++;
    this.updateRollingStatus();
  }

  @Override
  public int getContainerSize() {
    return 10;
  }

  @Override
  public boolean isEmpty() {
    return true;
  }

  // todo: implement this painful proc at rollingtable powered variant
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
  public boolean stillValid(PlayerEntity playerEntity) {
    return true;
  }

  @Override
  public void clearContent() {
    // nothing
  }

  @Override
  protected ITextComponent getDefaultName() {
    return MENU_TITLE;
  }

  @Override
  protected Container createMenu(int containerProvider, PlayerInventory playerInventory) {
    return new ManualRollingMachineMenu(containerProvider, playerInventory, this.data, this);
  }
}
