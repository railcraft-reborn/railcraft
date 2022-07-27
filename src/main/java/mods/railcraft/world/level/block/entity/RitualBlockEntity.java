package mods.railcraft.world.level.block.entity;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.Set;
import javax.annotation.Nullable;
import mods.railcraft.charge.HostEffects;
import mods.railcraft.world.item.RailcraftItems;
import mods.railcraft.world.level.material.fluid.FluidTools;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.Vec3;

/**
 * @author CovertJaguar <https://www.railcraft.info/>
 */
public class RitualBlockEntity extends RailcraftBlockEntity {

  public static final int[] REBUILD_DELAY = Util.make(new int[8], delay -> {
    delay[0] = 128;
    delay[1] = 256;
    delay[2] = 512;
    delay[3] = 1024;
    delay[4] = 2048;
    delay[5] = 4096;
    delay[6] = 8192;
    delay[7] = 16384;
  });

  private final Deque<BlockPos> queue = new ArrayDeque<>();
  private final Deque<BlockPos> lavaFound = new ArrayDeque<>();
  private final Set<BlockPos> visitedBlocks = new HashSet<>();
  public int charge;
  public long rotationYaw, preRotationYaw;
  public float yOffset = -2, preYOffset = -2;
  private int rebuildDelay;
  private Component itemName;

  private int rebuildDelayTicks;

  public RitualBlockEntity(BlockPos blockPos, BlockState blockState) {
    super(RailcraftBlockEntityTypes.RITUAL.get(), blockPos, blockState);
  }

  public static void clientTick(Level level, BlockPos blockPos, BlockState blockState,
      RitualBlockEntity blockEntity) {
    blockEntity.preRotationYaw = blockEntity.rotationYaw;
    blockEntity.rotationYaw += 5;
    if (blockEntity.rotationYaw >= 360) {
      blockEntity.rotationYaw = 0;
      blockEntity.preRotationYaw = blockEntity.rotationYaw;
    }
    blockEntity.preYOffset = blockEntity.yOffset;
    if (blockEntity.yOffset < 0) {
      blockEntity.yOffset += 0.0625F;
    }
  }

  public static void serverTick(Level level, BlockPos blockPos, BlockState blockState,
      RitualBlockEntity blockEntity) {
    var firestone = RailcraftItems.REFINED_FIRESTONE.get().getDefaultInstance();

    if (blockEntity.charge >= firestone.getMaxDamage()) {
      return;
    }

    if (blockEntity.rebuildDelayTicks++ >= REBUILD_DELAY[blockEntity.rebuildDelay]) {
      blockEntity.rebuildDelayTicks = 0;
      blockEntity.rebuildDelay++;
      if (blockEntity.rebuildDelay >= REBUILD_DELAY.length) {
        blockEntity.rebuildDelay = REBUILD_DELAY.length - 1;
      }
      blockEntity.rebuildQueue();
    }
    var index = blockEntity.getNextLavaBlock(true);

    if (index != null && blockEntity.coolLava(index)) {
      blockEntity.charge++;
      blockEntity.rebuildDelay = 0;
    }
  }

  // logical server
  private boolean coolLava(BlockPos pos) {
    FluidState fluid = this.level.getBlockState(pos).getFluidState();
    if (Fluids.LAVA == fluid.getType()) {
      boolean placed = this.level.setBlockAndUpdate(pos, Blocks.OBSIDIAN.defaultBlockState());
      if (placed) {
        Vec3 startPosition =
            new Vec3(pos.getX(), pos.getY(), pos.getZ()).add(0.5, 0.5, 0.5);
        Vec3 endPosition = new Vec3(this.worldPosition.getX(), this.worldPosition.getY(),
            this.worldPosition.getZ()).add(0.5, 0.8, 0.5);
        HostEffects.INSTANCE.fireSparkEffect(this.level, startPosition, endPosition);
        queueAdjacent(pos);
        expandQueue();
        return true;
      }
    }
    return false;
  }

  private @Nullable BlockPos getNextLavaBlock(boolean remove) {
    if (queue.isEmpty())
      return null;

    if (remove)
      return queue.pollFirst();
    return queue.peekFirst();
  }

  /**
   * Nasty expensive function, don't call if you don't have to.
   */
  void rebuildQueue() {
    queue.clear();
    visitedBlocks.clear();
    lavaFound.clear();

    queueAdjacent(getBlockPos());

    expandQueue();
  }

  private void expandQueue() {
    BlockPos next;
    while ((next = lavaFound.poll()) != null) {
      queueAdjacent(next);
    }
  }

  public void queueAdjacent(BlockPos pos) {
    // No idea if it matters which order these are added,
    // but I figured it best to keep them in the same order.
    for (Direction side : Direction.Plane.HORIZONTAL) {
      queueForFilling(pos.relative(side));
    }
    queueForFilling(pos.above());
    queueForFilling(pos.below());
  }

  // TODO: test
  public void queueForFilling(BlockPos index) {
    if (visitedBlocks.add(index)) {
      if ((index.getX() - this.worldPosition.getX()) * (index.getX() - this.worldPosition.getX())
          + (index.getZ() - this.worldPosition.getZ())
              * (index.getZ() - this.worldPosition.getZ()) > 64 * 64)
        return;

      BlockState state = this.level.getBlockState(index);
      if (state.getBlock() == Blocks.OBSIDIAN || Fluids.LAVA == FluidTools.getFluid(state)) {
        lavaFound.add(index);
        if (FluidTools.isFullFluidBlock(state, this.level, index))
          queue.addLast(index);
      }
    }
  }

  public Component getItemName() {
    return itemName;
  }

  public void setItemName(Component itemName) {
    this.itemName = itemName;
  }

  @Override
  protected void saveAdditional(CompoundTag tag) {
    super.saveAdditional(tag);
    tag.putShort("charge", (short) this.charge);
    tag.putByte("rebuildDelay", (byte) this.rebuildDelay);
    if (this.itemName != null) {
      tag.putString("itemName", Component.Serializer.toJson(this.itemName));
    }
  }

  @Override
  public void load(CompoundTag tag) {
    super.load(tag);
    this.charge = tag.getShort("charge");
    this.rebuildDelay = tag.getByte("rebuildDelay");
    if (tag.contains("itemName", Tag.TAG_STRING)) {
      this.itemName = Component.Serializer.fromJson(tag.getString("itemName"));
    }
  }
}
