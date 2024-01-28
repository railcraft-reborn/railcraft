package mods.railcraft.world.entity.vehicle;

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
import net.minecraft.world.level.block.BaseRailBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.RailShape;

public class TrackUndercutter extends MaintenancePatternMinecart {

  private static final Set<Block> EXCLUDED_BLOCKS = Set.of(Blocks.SAND);

  public static final int SLOT_STOCK_UNDER = 0;
  public static final int SLOT_STOCK_SIDE = 1;
  private static final int SLOT_EXIST_UNDER_A = 0;
  private static final int SLOT_EXIST_UNDER_B = 1;
  private static final int SLOT_EXIST_SIDE_A = 2;
  private static final int SLOT_EXIST_SIDE_B = 3;
  private static final int SLOT_REPLACE_UNDER = 4;
  private static final int SLOT_REPLACE_SIDE = 5;
  private static final int[] SLOTS = ContainerTools.buildSlotArray(0, 2);

  public TrackUndercutter(EntityType<?> type, Level level) {
    super(type, level);
  }

  public TrackUndercutter(ItemStack itemStack, double x, double y, double z, ServerLevel level) {
    super(RailcraftEntityTypes.TRACK_UNDERCUTTER.get(), x, y, z, level);
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
    this.stockItems(SLOT_REPLACE_UNDER, SLOT_STOCK_UNDER);
    this.stockItems(SLOT_REPLACE_SIDE, SLOT_STOCK_SIDE);

    var pos = BlockPos.containing(this.position());
    if (BaseRailBlock.isRail(this.level(), pos.below())) {
      pos = pos.below();
    }

    var blockState = this.level().getBlockState(pos);
    if (!BaseRailBlock.isRail(blockState)) {
      return;
    }

    var railShape = TrackUtil.getTrackDirection(this.level(), pos, blockState, this);
    pos = pos.below();

    var slotAEmpty = true;
    var slotBEmpty = true;
    if (!this.patternContainer.getItem(SLOT_EXIST_UNDER_A).isEmpty()) {
      this.replaceUnder(pos, SLOT_EXIST_UNDER_A);
      slotAEmpty = false;
    }
    if (!this.patternContainer.getItem(SLOT_EXIST_UNDER_B).isEmpty()) {
      this.replaceUnder(pos, SLOT_EXIST_UNDER_B);
      slotBEmpty = false;
    }

    if (slotAEmpty && slotBEmpty) {
      this.replaceUnder(pos, SLOT_EXIST_UNDER_A);
    }

    slotAEmpty = true;
    slotBEmpty = true;
    if (!this.patternContainer.getItem(SLOT_EXIST_SIDE_A).isEmpty()) {
      this.replaceSide(pos, SLOT_EXIST_SIDE_A, railShape);
      slotAEmpty = false;
    }
    if (!this.patternContainer.getItem(SLOT_EXIST_SIDE_B).isEmpty()) {
      this.replaceSide(pos, SLOT_EXIST_SIDE_B, railShape);
      slotBEmpty = false;
    }

    if (slotAEmpty && slotBEmpty) {
      this.replaceSide(pos, SLOT_EXIST_SIDE_A, railShape);
    }
  }

  private void replaceUnder(BlockPos pos, int slotExist) {
    this.replaceWith(pos, slotExist, SLOT_STOCK_UNDER);
  }

  private void replaceSide(BlockPos pos, int slotExist, RailShape railShape) {
    if (RailShapeUtil.isEastWest(railShape)) {
      this.replaceWith(pos.north(), slotExist, SLOT_STOCK_SIDE);
      this.replaceWith(pos.south(), slotExist, SLOT_STOCK_SIDE);
    } else if (RailShapeUtil.isNorthSouth(railShape)) {
      this.replaceWith(pos.east(), slotExist, SLOT_STOCK_SIDE);
      this.replaceWith(pos.west(), slotExist, SLOT_STOCK_SIDE);
    }
  }

  private void replaceWith(BlockPos pos, int existingSlot, int stockSlot) {
    var existingTrack = this.patternContainer.getItem(existingSlot);
    var stockTrack = this.getItem(stockSlot);

    if (!isValidBallast(stockTrack, this)) {
      return;
    }

    var oldState = this.level().getBlockState(pos);

    if (!blockMatches(oldState, existingTrack)) {
      return;
    }

    if (this.safeToReplace(pos)) {
      var stockBlock = ContainerTools.getBlockStateFromStack(stockTrack, this.level(), pos);
      var drops = Block.getDrops(oldState, (ServerLevel) this.level(), pos,
          this.level().getBlockEntity(pos));
      if (stockBlock != null && this.level().setBlockAndUpdate(pos, stockBlock)) {
        this.level().playSound(null, pos, stockBlock.getSoundType().getPlaceSound(),
            SoundSource.AMBIENT, 1, 0.8F);

        this.removeItem(stockSlot, 1);
        var rollingStock = RollingStock.getOrThrow(this);
        for (var stack : drops) {
          rollingStock.offerOrDropItem(stack);
        }
        this.blink();
      }
    }
  }

  @SuppressWarnings("deprecation")
  private boolean safeToReplace(BlockPos pos) {
    var blockState = this.level().getBlockState(pos);
    if (blockState.isAir()) {
      return false;
    }
    if (blockState.liquid()) {
      return false;
    }
    if (blockState.getDestroySpeed(this.level(), pos) < 0) {
      return false;
    }
    return !TunnelBore.REPLACEABLE_BLOCKS.contains(blockState.getBlock());
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
    return switch (slot) {
      case SLOT_REPLACE_UNDER -> ItemStack.isSameItem(stack,
          this.patternContainer.getItem(SLOT_REPLACE_UNDER));
      case SLOT_REPLACE_SIDE -> ItemStack.isSameItem(stack,
          this.patternContainer.getItem(SLOT_REPLACE_SIDE));
      default -> false;
    };
  }

  @Override
  protected AbstractContainerMenu createMenu(int id, Inventory inventory) {
    return new TrackUndercutterMenu(id, inventory, this);
  }

  private static boolean blockMatches(BlockState state, ItemStack stack) {
    if (stack.isEmpty()) {
      return true;
    }
    if (stack.getItem() instanceof BlockItem item) {
      return state.is(item.getBlock()) || state.is(Blocks.GRASS) && item.getBlock() == Blocks.DIRT;
    }
    return false;
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
        trackUndercutter.blockPosition());
  }
}
