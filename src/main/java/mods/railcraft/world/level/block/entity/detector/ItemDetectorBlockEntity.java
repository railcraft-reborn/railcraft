package mods.railcraft.world.level.block.entity.detector;

import java.util.List;
import org.jetbrains.annotations.Nullable;
import mods.railcraft.Translations;
import mods.railcraft.api.container.manipulator.ContainerManipulator;
import mods.railcraft.api.container.manipulator.SlotAccessor;
import mods.railcraft.api.core.CompoundTagKeys;
import mods.railcraft.api.util.EnumUtil;
import mods.railcraft.util.container.StackFilter;
import mods.railcraft.world.inventory.detector.ItemDetectorMenu;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.redstone.Redstone;
import net.neoforged.neoforge.capabilities.Capabilities;

public class ItemDetectorBlockEntity extends FilterDetectorBlockEntity {

  private PrimaryMode primaryMode = PrimaryMode.ANYTHING;
  private FilterMode filterMode = FilterMode.AT_LEAST;

  public ItemDetectorBlockEntity(BlockPos blockPos, BlockState blockState) {
    super(RailcraftBlockEntityTypes.ITEM_DETECTOR.get(), blockPos, blockState, 9);
  }

  @Override
  protected int testCarts(List<AbstractMinecart> minecarts) {
    for (var cart : minecarts) {
      var itemHandler = cart.getCapability(Capabilities.ItemHandler.ENTITY);
      if (itemHandler != null) {
        var containerManipulator = ContainerManipulator.of(itemHandler);
        if (itemHandler.getSlots() > 0) {
          switch (primaryMode) {
            case ANYTHING:
              return Redstone.SIGNAL_MAX;
            case EMPTY:
              if (containerManipulator.hasNoItems())
                return Redstone.SIGNAL_MAX;
              continue;
            case FULL:
              if (containerManipulator.isFull())
                return Redstone.SIGNAL_MAX;
              continue;
            case FILTERED:
              if (matchesFilter(containerManipulator))
                return Redstone.SIGNAL_MAX;
              continue;
            case NOT_EMPTY:
              if (containerManipulator.hasItems())
                return Redstone.SIGNAL_MAX;
              continue;
            case ANALOG:
              return containerManipulator.calcRedstone();
          }
        }

      }
    }
    return Redstone.SIGNAL_NONE;
  }

  private boolean matchesFilter(ContainerManipulator<SlotAccessor> containerManipulator) {
    for (int i = 0; i < getContainerSize(); i++) {
      var filter = getItem(i);
      if (filter.isEmpty())
        continue;
      var stackFilter = StackFilter.anyMatch(filter);
      int amountFilter = stream()
          .filter(x -> x.matches(stackFilter))
          .map(x -> x.item().getCount())
          .reduce(Integer::sum)
          .orElse(0);
      int amountCart = containerManipulator.countItems(stackFilter);
      switch (filterMode) {
        case EXACTLY:
          if (amountCart != amountFilter)
            return false;
          break;
        case AT_LEAST:
          if (amountCart < amountFilter)
            return false;
          break;
        case AT_MOST:
          if (amountCart > amountFilter)
            return false;
          break;
        case GREATER_THAN:
          if (amountCart <= amountFilter)
            return false;
          break;
        case LESS_THAN:
          if (amountCart >= amountFilter)
            return false;
          break;
      }
    }
    return true;
  }

  @Override
  public void load(CompoundTag tag) {
    super.load(tag);
    this.primaryMode = PrimaryMode.fromName(tag.getString(CompoundTagKeys.PRIMARY_MODE));
    this.filterMode = FilterMode.fromName(tag.getString(CompoundTagKeys.FILTER_MODE));
  }

  @Override
  public void saveAdditional(CompoundTag tag) {
    super.saveAdditional(tag);
    tag.putString(CompoundTagKeys.PRIMARY_MODE, this.primaryMode.getSerializedName());
    tag.putString(CompoundTagKeys.FILTER_MODE, this.filterMode.getSerializedName());
  }

  @Override
  public void writeToBuf(FriendlyByteBuf out) {
    super.writeToBuf(out);
    out.writeEnum(this.primaryMode);
    out.writeEnum(this.filterMode);
  }

  @Override
  public void readFromBuf(FriendlyByteBuf in) {
    super.readFromBuf(in);
    this.primaryMode = in.readEnum(PrimaryMode.class);
    this.filterMode = in.readEnum(FilterMode.class);
  }

  @Nullable
  @Override
  public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
    return new ItemDetectorMenu(id, inventory, this);
  }

  public PrimaryMode getPrimaryMode() {
    return this.primaryMode;
  }

  public FilterMode getFilterMode() {
    return this.filterMode;
  }

  public void setPrimaryMode(PrimaryMode value) {
    this.primaryMode = value;
  }

  public void setFilterMode(FilterMode value) {
    this.filterMode = value;
  }

  public enum PrimaryMode implements StringRepresentable {

    EMPTY("empty"),
    FULL("full"),
    ANYTHING("anything"),
    FILTERED("filtered"),
    NOT_EMPTY("not_empty"),
    ANALOG("analog");

    private static final StringRepresentable.EnumCodec<PrimaryMode> CODEC =
        StringRepresentable.fromEnum(PrimaryMode::values);

    private final String name;

    PrimaryMode(String name) {
      this.name = name;
    }

    public PrimaryMode next() {
      return EnumUtil.next(this, values());
    }

    public PrimaryMode previous() {
      return EnumUtil.previous(this, values());
    }

    public Component getName() {
      return Component.translatable(
          Translations.makeKey("screen", "item_detector.primary_mode." + this.name));
    }

    @Override
    public String getSerializedName() {
      return this.name;
    }

    public static PrimaryMode fromName(String name) {
      return CODEC.byName(name, ANYTHING);
    }
  }

  public enum FilterMode implements StringRepresentable {
    AT_LEAST("at_least"),
    AT_MOST("at_most"),
    EXACTLY("exactly"),
    LESS_THAN("less_than"),
    GREATER_THAN("greater_than");

    private static final StringRepresentable.EnumCodec<FilterMode> CODEC =
        StringRepresentable.fromEnum(FilterMode::values);

    private final String name;

    FilterMode(String name) {
      this.name = name;
    }

    public FilterMode next() {
      return EnumUtil.next(this, values());
    }

    public FilterMode previous() {
      return EnumUtil.previous(this, values());
    }

    public Component getName() {
      return Component.translatable(
          Translations.makeKey("screen", "item_detector.filter_mode." + this.name));
    }

    @Override
    public String getSerializedName() {
      return this.name;
    }

    public static FilterMode fromName(String name) {
      return CODEC.byName(name, AT_LEAST);
    }
  }
}
