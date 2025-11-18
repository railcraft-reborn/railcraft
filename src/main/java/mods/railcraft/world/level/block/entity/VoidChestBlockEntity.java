package mods.railcraft.world.level.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.entity.ChestLidController;
import net.minecraft.world.level.block.entity.ContainerOpenersCounter;
import net.minecraft.world.level.block.entity.LidBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class VoidChestBlockEntity extends ContainerBlockEntity implements LidBlockEntity,
    MenuProvider {

  private static final int EVENT_SET_OPEN_COUNT = 1;
  private final ContainerOpenersCounter openersCounter = new ContainerOpenersCounter() {
    @Override
    protected void onOpen(Level level, BlockPos pos, BlockState state) {
      playSound(level, pos, state, SoundEvents.CHEST_OPEN);
    }

    @Override
    protected void onClose(Level level, BlockPos pos, BlockState state) {
      playSound(level, pos, state, SoundEvents.CHEST_CLOSE);
    }

    @Override
    protected void openerCountChanged(Level level, BlockPos pos, BlockState state, int count, int openCount) {
      VoidChestBlockEntity.this.signalOpenCount(level, pos, state, count, openCount);
    }

    @Override
    protected boolean isOwnContainer(Player player) {
      if (player.containerMenu instanceof ChestMenu chestMenu) {
        return chestMenu.getContainer() == VoidChestBlockEntity.this;
      }
      return false;
    }
  };

  private final ChestLidController chestLidController = new ChestLidController();
  private int tick = 0;

  public VoidChestBlockEntity(BlockPos blockPos, BlockState blockState) {
    super(RailcraftBlockEntityTypes.VOID_CHEST.get(), blockPos, blockState, 27);
  }

  public static void clientTick(Level level, BlockPos pos, BlockState state,
      VoidChestBlockEntity blockEntity) {
    blockEntity.chestLidController.tickLid();
  }

  public static void serverTick(Level level, BlockPos pos, BlockState state,
      VoidChestBlockEntity blockEntity) {
    if (++blockEntity.tick >= 8) {
      blockEntity.container().clearContent();
      blockEntity.tick = 0;
    }
  }

  private static void playSound(Level level, BlockPos pos, BlockState state, SoundEvent sound) {
    double x = (double)pos.getX() + 0.5;
    double y = (double)pos.getY() + 0.5;
    double z = (double)pos.getZ() + 0.5;
    level.playSound(null, x, y, z, sound, SoundSource.BLOCKS, 0.5F, level.random.nextFloat() * 0.1F + 0.9F);
  }

  @Override
  public boolean triggerEvent(int id, int type) {
    if (id == EVENT_SET_OPEN_COUNT) {
      this.chestLidController.shouldBeOpen(type > 0);
      return true;
    } else {
      return super.triggerEvent(id, type);
    }
  }

  @Override
  public void startOpen(Player player) {
    if (!this.remove && !player.isSpectator()) {
      this.openersCounter.incrementOpeners(player, this.getLevel(), this.getBlockPos(), this.getBlockState());
    }
  }

  @Override
  public void stopOpen(Player player) {
    if (!this.remove && !player.isSpectator()) {
      this.openersCounter.decrementOpeners(player, this.getLevel(), this.getBlockPos(), this.getBlockState());
    }
  }

  @Override
  public float getOpenNess(float partialTicks) {
    return this.chestLidController.getOpenness(partialTicks);
  }

  public static int getOpenCount(BlockGetter level, BlockPos pos) {
    var blockstate = level.getBlockState(pos);
    if (blockstate.hasBlockEntity()) {
      if (level.getBlockEntity(pos) instanceof VoidChestBlockEntity voidChestBlockEntity) {
        return voidChestBlockEntity.openersCounter.getOpenerCount();
      }
    }

    return 0;
  }

  @Override
  public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
    return ChestMenu.threeRows(id, inventory, this);
  }

  @Override
  public void setBlockState(BlockState blockState) {
    var oldState = getBlockState();
    super.setBlockState(blockState);
    // Neo: Chest state change might change the chest item handler -> invalidate
    if (oldState.getValue(ChestBlock.FACING) != blockState.getValue(ChestBlock.FACING)) {
      this.invalidateCapabilities();
    }
  }

  public void recheckOpen() {
    if (!this.remove) {
      this.openersCounter.recheckOpeners(this.getLevel(), this.getBlockPos(), this.getBlockState());
    }
  }

  protected void signalOpenCount(Level level, BlockPos pos, BlockState state, int count, int openCount) {
    level.blockEvent(pos, state.getBlock(), EVENT_SET_OPEN_COUNT, openCount);
  }
}
