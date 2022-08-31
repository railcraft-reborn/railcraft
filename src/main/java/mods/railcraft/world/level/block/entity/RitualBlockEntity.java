package mods.railcraft.world.level.block.entity;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.Set;
import mods.railcraft.particle.FireSparkParticleOptions;
import mods.railcraft.world.item.RefinedFirestoneItem;
import mods.railcraft.world.level.material.fluid.FluidTools;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

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

  private int tick = 0;

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

    if(blockEntity.tick % REBUILD_DELAY[blockEntity.rebuildDelay] == 0) {
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
    FluidState fluid = level.getBlockState(lavaPos).getFluidState();
    if (fluid.is(Fluids.LAVA)) {
      boolean placed = level.setBlockAndUpdate(lavaPos, Blocks.OBSIDIAN.defaultBlockState());
      if (placed) {
        var startPosition = lavaPos.offset(0.5, 0.5, 0.5);
        var endPosition = getBlockPos().offset(0.5, 0.8, 0.5);
        fireSparkEffect(level, startPosition, endPosition);
        queueAdjacent(lavaPos);
        expandQueue();
        return true;
      }
    }
    return false;
  }

  private void fireSparkEffect(ServerLevel level, BlockPos start, BlockPos end) {
    level.sendParticles(new FireSparkParticleOptions(Vec3.atCenterOf(end)),
        start.getX(), start.getY(), start.getZ(),
        1, 0, 0, 0, 0
    );
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
