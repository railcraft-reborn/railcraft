package mods.railcraft.world.level.block.entity;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.Set;
import javax.annotation.Nullable;
import mods.railcraft.api.core.CollectionToolsAPI;
import mods.railcraft.util.HostEffects;
import mods.railcraft.world.item.RailcraftItems;
import mods.railcraft.world.level.material.fluid.FluidTools;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.common.util.Constants;

/**
 * @author CovertJaguar <http://www.railcraft.info/>
 */
public class RitualBlockEntity extends RailcraftTickableBlockEntity {

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

  private final Deque<BlockPos> queue = CollectionToolsAPI.blockPosDeque(ArrayDeque::new);
  private final Deque<BlockPos> lavaFound = CollectionToolsAPI.blockPosDeque(ArrayDeque::new);
  private final Set<BlockPos> visitedBlocks = CollectionToolsAPI.blockPosSet(HashSet::new);
  public int charge;
  public long rotationYaw, preRotationYaw;
  public float yOffset = -2, preYOffset = -2;
  private int rebuildDelay;
  private ITextComponent itemName;

  public RitualBlockEntity() {
    super(RailcraftBlockEntityTypes.RITUAL.get());
  }

  @Override
  public void tick() {
    super.tick();
    if (this.level.isClientSide()) {
      preRotationYaw = rotationYaw;
      rotationYaw += 5;
      if (rotationYaw >= 360) {
        rotationYaw = 0;
        preRotationYaw = rotationYaw;
      }
      preYOffset = yOffset;
      if (yOffset < 0)
        yOffset += 0.0625F;
      return;
    }

    ItemStack firestone = RailcraftItems.REFINED_FIRESTONE.get().getDefaultInstance();

    if (charge >= firestone.getMaxDamage())
      return;

    // if (clock % 4 == 0) {
    if (this.clock(REBUILD_DELAY[rebuildDelay])) {
      rebuildDelay++;
      if (rebuildDelay >= REBUILD_DELAY.length)
        rebuildDelay = REBUILD_DELAY.length - 1;
      rebuildQueue();
    }
    BlockPos index = getNextLavaBlock(true);

    if (index != null && coolLava(index)) {
      charge++;
      rebuildDelay = 0;
    }
    // }
  }

  // logical server
  private boolean coolLava(BlockPos pos) {
    FluidState fluid = this.level.getBlockState(pos).getFluidState();
    if (Fluids.LAVA == fluid.getType()) {
      boolean placed = this.level.setBlockAndUpdate(pos, Blocks.OBSIDIAN.defaultBlockState());
      if (placed) {
        Vector3d startPosition =
            new Vector3d(pos.getX(), pos.getY(), pos.getZ()).add(0.5, 0.5, 0.5);
        Vector3d endPosition = new Vector3d(this.worldPosition.getX(), this.worldPosition.getY(),
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

  public ITextComponent getItemName() {
    return itemName;
  }

  public void setItemName(ITextComponent itemName) {
    this.itemName = itemName;
  }

  @Override
  public CompoundNBT save(CompoundNBT data) {
    super.save(data);
    data.putShort("charge", (short) charge);
    data.putByte("rebuildDelay", (byte) rebuildDelay);
    if (itemName != null)
      data.putString("itemName", ITextComponent.Serializer.toJson(itemName));
    return data;
  }

  @Override
  public void load(BlockState state, CompoundNBT data) {
    super.load(state, data);
    charge = data.getShort("charge");
    rebuildDelay = data.getByte("rebuildDelay");
    if (data.contains("itemName", Constants.NBT.TAG_STRING))
      itemName = ITextComponent.Serializer.fromJson(data.getString("itemName"));
  }
}
