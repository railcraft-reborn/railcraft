package mods.railcraft.world.entity.vehicle;

import java.util.HashSet;
import java.util.Set;
import mods.railcraft.api.carts.RollingStock;
import mods.railcraft.api.track.RailShapeUtil;
import mods.railcraft.api.track.TrackUtil;
import mods.railcraft.util.container.ContainerTools;
import mods.railcraft.world.entity.RailcraftEntityTypes;
import mods.railcraft.world.inventory.TrackUndercutterMenu;
import mods.railcraft.world.item.RailcraftItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.RailShape;

public class TrackUndercutter extends MaintenancePatternMinecart {

  public static final Set<Block> EXCLUDED_BLOCKS = new HashSet<>();
  public static final int SLOT_STOCK_UNDER = 0;
  public static final int SLOT_STOCK_SIDE = 1;
  private static final int SLOT_EXIST_UNDER_A = 0;
  private static final int SLOT_EXIST_UNDER_B = 1;
  private static final int SLOT_EXIST_SIDE_A = 2;
  private static final int SLOT_EXIST_SIDE_B = 3;
  private static final int SLOT_REPLACE_UNDER = 4;
  private static final int SLOT_REPLACE_SIDE = 5;
  private static final int[] SLOTS = ContainerTools.buildSlotArray(0, 2);

  static {
    EXCLUDED_BLOCKS.add(Blocks.SAND);
  }

  public TrackUndercutter(EntityType<?> type, Level level) {
    super(type, level);
  }

  public TrackUndercutter(ItemStack itemStack, double x, double y, double z, ServerLevel level) {
    super(RailcraftEntityTypes.TRACK_UNDERCUTTER.get(), x, y, z, level);
  }

  public static boolean isValidBallast(ItemStack stack, TrackUndercutter trackUndercutter) {
    if (stack.isEmpty()) {
      return false;
    }
    var state = ContainerTools.getBlockStateFromStack(stack);
    if (EXCLUDED_BLOCKS.contains(state.getBlock())) {
      return false;
    }

    return state.isSuffocating(trackUndercutter.level(),
        BlockPos.containing(trackUndercutter.position()));
  }

  @Override
  public ItemStack getPickResult() {
    return RailcraftItems.TRACK_UNDERCUTTER.get().getDefaultInstance();
  }

  @Override
  public Item getDropItem() {
    return RailcraftItems.TRACK_UNDERCUTTER.get();
  }

  @Override
  public void tick() {
    super.tick();
    if (this.level().isClientSide()) {
      return;
    }
    if (this.mode() == Mode.OFF) {
      return;
    }
    stockItems(SLOT_REPLACE_UNDER, SLOT_STOCK_UNDER);
    stockItems(SLOT_REPLACE_SIDE, SLOT_STOCK_SIDE);

    var pos = BlockPos.containing(this.position());
    if (TrackUtil.isRailBlockAt(this.level(), pos.below())) {
      pos = pos.below();
    }

    var state = this.level().getBlockState(pos);
    var block = state.getBlock();

    if (TrackUtil.isRail(block)) {
      var railShape = TrackUtil.getTrackDirection(this.level(), pos, state, this);
      pos = pos.below();

      boolean slotANull = true;
      boolean slotBNull = true;
      if (!patternContainer.getItem(SLOT_EXIST_UNDER_A).isEmpty()) {
        replaceUnder(pos, SLOT_EXIST_UNDER_A);
        slotANull = false;
      }
      if (!patternContainer.getItem(SLOT_EXIST_UNDER_B).isEmpty()) {
        replaceUnder(pos, SLOT_EXIST_UNDER_B);
        slotBNull = false;
      }

      if (slotANull && slotBNull) {
        replaceUnder(pos, SLOT_EXIST_UNDER_A);
      }

      slotANull = true;
      slotBNull = true;
      if (!patternContainer.getItem(SLOT_EXIST_SIDE_A).isEmpty()) {
        replaceSide(pos, SLOT_EXIST_SIDE_A, railShape);
        slotANull = false;
      }
      if (!patternContainer.getItem(SLOT_EXIST_SIDE_B).isEmpty()) {
        replaceSide(pos, SLOT_EXIST_SIDE_B, railShape);
        slotBNull = false;
      }

      if (slotANull && slotBNull) {
        replaceSide(pos, SLOT_EXIST_SIDE_A, railShape);
      }
    }
  }

  private void replaceUnder(BlockPos pos, int slotExist) {
    replaceWith(pos, slotExist, SLOT_STOCK_UNDER);
  }

  private void replaceSide(BlockPos pos, int slotExist, RailShape railShape) {
    if (RailShapeUtil.isEastWest(railShape)) {
      replaceWith(pos.north(), slotExist, SLOT_STOCK_SIDE);
      replaceWith(pos.south(), slotExist, SLOT_STOCK_SIDE);
    } else if (RailShapeUtil.isNorthSouth(railShape)) {
      replaceWith(pos.east(), slotExist, SLOT_STOCK_SIDE);
      replaceWith(pos.west(), slotExist, SLOT_STOCK_SIDE);
    }
  }

  private void replaceWith(BlockPos pos, int slotExist, int slotStock) {
    var exist = patternContainer.getItem(slotExist);
    var stock = getItem(slotStock);

    if (!isValidBallast(stock, this)) {
      return;
    }

    var oldState = this.level().getBlockState(pos);

    if (!blockMatches(oldState, exist)) {
      return;
    }

    if (safeToReplace(pos)) {
      var stockBlock = ContainerTools.getBlockStateFromStack(stock, this.level(), pos);
      var drops = Block.getDrops(oldState, (ServerLevel) this.level(), pos,
          this.level().getBlockEntity(pos));
      if (stockBlock != null && level().setBlockAndUpdate(pos, stockBlock)) {
        this.level().playSound(null, pos, stockBlock.getSoundType().getPlaceSound(),
            SoundSource.AMBIENT, (1f + 1.0F) / 2.0F, 0.8F);

        this.removeItem(slotStock, 1);
        for (var stack : drops) {
          RollingStock.getOrThrow(this).offerOrDropItem(stack);
        }
        blink();
      }
    }
  }


  private boolean blockMatches(BlockState state, ItemStack stack) {
    if (stack.isEmpty()) {
      return true;
    }
    if (stack.getItem() instanceof BlockItem) {
      var stackBlock = ContainerTools.getBlockStateFromStack(stack);
      return (state.equals(stackBlock) || state.is(stackBlock.getBlock())) ||
          state.is(Blocks.GRASS) && stackBlock.is(Blocks.DIRT);
    }
    return false;
  }

  @SuppressWarnings("deprecation")
  private boolean safeToReplace(BlockPos pos) {
    var state = level().getBlockState(pos);
    if (state.isAir()) {
      return false;
    }
    if (state.liquid()) {
      return false;
    }
    if (state.getDestroySpeed(this.level(), pos) < 0) {
      return false;
    }
    return !TunnelBore.REPLACEABLE_BLOCKS.contains(state.getBlock());
  }

  @Override
  public int[] getSlotsForFace(Direction side) {
    return SLOTS;
  }

  @Override
  public int getContainerSize() {
    return 2;
  }

  @Override
  public boolean canPlaceItem(int slot, ItemStack stack) {
    if (slot == SLOT_REPLACE_UNDER) {
      var trackReplace = patternContainer.getItem(SLOT_REPLACE_UNDER);
      return ItemStack.isSameItem(stack, trackReplace);
    }
    if (slot == SLOT_REPLACE_SIDE) {
      var trackReplace = patternContainer.getItem(SLOT_REPLACE_SIDE);
      return ItemStack.isSameItem(stack, trackReplace);
    }
    return false;
  }

  @Override
  protected AbstractContainerMenu createMenu(int id, Inventory inventory) {
    return new TrackUndercutterMenu(id, inventory, this);
  }
}
