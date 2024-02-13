package mods.railcraft.world.level.block.entity;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.Set;
import org.jetbrains.annotations.Nullable;
import mods.railcraft.api.core.CompoundTagKeys;
import mods.railcraft.particle.FireSparkParticleOptions;
import mods.railcraft.util.fluids.FluidTools;
import mods.railcraft.world.item.RefinedFirestoneItem;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.Vec3;

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
  private int charge;

  private int rebuildDelay;
  private Component itemName;
  private int tick = 0;

  // Client only
  private long rotationYaw;
  private long preRotationYaw;
  private float yOffset = -2;
  private float preYOffset = -2;

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
    if (blockEntity.charge >= RefinedFirestoneItem.CHARGES) {
      blockEntity.tick = 0;
      return;
    }

    if (blockEntity.tick % REBUILD_DELAY[blockEntity.rebuildDelay] == 0) {
      blockEntity.rebuildDelay++;
      if (blockEntity.rebuildDelay >= REBUILD_DELAY.length) {
        blockEntity.rebuildDelay = REBUILD_DELAY.length - 1;
      }
      blockEntity.rebuildQueue();
    }
    var pos = blockEntity.getNextLavaBlock(true);

    if (pos != null && blockEntity.coolLava((ServerLevel) level, pos)) {
      blockEntity.charge++;
      blockEntity.rebuildDelay = 0;
    }
    blockEntity.tick++;
  }

  private boolean coolLava(ServerLevel level, BlockPos lavaPos) {
    var fluid = level.getBlockState(lavaPos).getFluidState();
    if (fluid.is(Fluids.LAVA)) {
      boolean placed = level.setBlockAndUpdate(lavaPos, Blocks.OBSIDIAN.defaultBlockState());
      if (placed) {
        var startPosition = Vec3.atLowerCornerWithOffset(lavaPos, 0.5, 0.5, 0.5);
        var endPosition = Vec3.atLowerCornerWithOffset(getBlockPos(), 0.5, 0.8, 0.5);
        fireSparkEffect(level, startPosition, endPosition);
        queueAdjacent(lavaPos);
        expandQueue();
        return true;
      }
    }
    return false;
  }

  private void fireSparkEffect(ServerLevel level, Vec3 start, Vec3 end) {
    level.sendParticles(new FireSparkParticleOptions(end),
        start.x, start.y, start.z, 1, 0, 0, 0, 0);
  }

  @Nullable
  private BlockPos getNextLavaBlock(boolean remove) {
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
    Direction.Plane.HORIZONTAL.forEach(side -> queueForFilling(pos.relative(side)));
    queueForFilling(pos.above());
    queueForFilling(pos.below());
  }

  public void queueForFilling(BlockPos index) {
    if (visitedBlocks.add(index)) {
      if ((index.getX() - this.worldPosition.getX()) * (index.getX() - this.worldPosition.getX())
          + (index.getZ() - this.worldPosition.getZ())
              * (index.getZ() - this.worldPosition.getZ()) > 64 * 64)
        return;

      var state = this.level.getBlockState(index);
      if (state.is(Blocks.OBSIDIAN) || FluidTools.getFluid(state).isSame(Fluids.LAVA)) {
        lavaFound.add(index);
        if (FluidTools.isFullFluidBlock(state, this.level, index))
          queue.addLast(index);
      }
    }
  }

  public void setItemName(Component itemName) {
    this.itemName = itemName;
  }

  public int charge() {
    return this.charge;
  }

  public void setCharge(int charge) {
    this.charge = charge;
  }

  public float getRotationYaw(float partialTick) {
    return Mth.lerp(partialTick, this.preRotationYaw, this.rotationYaw);
  }

  public float getYOffset(float partialTick) {
    return Mth.lerp(partialTick, this.preYOffset, this.yOffset);
  }

  @Override
  protected void saveAdditional(CompoundTag tag) {
    super.saveAdditional(tag);
    tag.putShort(CompoundTagKeys.CHARGE, (short) this.charge);
    tag.putByte(CompoundTagKeys.REBUILD_DELAY, (byte) this.rebuildDelay);
    if (this.itemName != null) {
      tag.putString(CompoundTagKeys.ITEM_NAME, Component.Serializer.toJson(this.itemName));
    }
  }

  @Override
  public void load(CompoundTag tag) {
    super.load(tag);
    this.charge = tag.getShort(CompoundTagKeys.CHARGE);
    this.rebuildDelay = tag.getByte(CompoundTagKeys.REBUILD_DELAY);
    if (tag.contains(CompoundTagKeys.ITEM_NAME, Tag.TAG_STRING)) {
      this.itemName = Component.Serializer.fromJson(tag.getString(CompoundTagKeys.ITEM_NAME));
    }
  }
}
