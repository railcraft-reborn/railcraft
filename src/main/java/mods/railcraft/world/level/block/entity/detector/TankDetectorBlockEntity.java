package mods.railcraft.world.level.block.entity.detector;

import java.util.List;
import java.util.Optional;
import org.jetbrains.annotations.Nullable;
import mods.railcraft.Translations;
import mods.railcraft.api.core.CompoundTagKeys;
import mods.railcraft.api.util.EnumUtil;
import mods.railcraft.client.gui.widget.button.ButtonTexture;
import mods.railcraft.client.gui.widget.button.TexturePosition;
import mods.railcraft.gui.button.ButtonState;
import mods.railcraft.util.fluids.AdvancedFluidHandler;
import mods.railcraft.world.inventory.detector.TankDetectorMenu;
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
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidUtil;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

public class TankDetectorBlockEntity extends FilterDetectorBlockEntity {

  private Mode mode = Mode.VOID;

  public TankDetectorBlockEntity(BlockPos blockPos, BlockState blockState) {
    super(RailcraftBlockEntityTypes.TANK_DETECTOR.get(), blockPos, blockState, 1);
  }

  public FluidStack getFilterFluid() {
    return FluidUtil.getFluidContained(this.getItem(0)).orElse(FluidStack.EMPTY);
  }

  @Override
  protected int testCarts(List<AbstractMinecart> minecarts) {
    for (var cart : minecarts) {
      var fluidHandler = cart.getCapability(Capabilities.FluidHandler.ENTITY, null);
      if (fluidHandler != null) {
        var tank = new AdvancedFluidHandler(fluidHandler);
        boolean liquidMatches = false;
        var filterFluid = this.getFilterFluid();
        var tankLiquid = tank.drain(1, IFluidHandler.FluidAction.SIMULATE);

        if (filterFluid.isEmpty())
          liquidMatches = true;
        else if (filterFluid.isFluidEqual(tankLiquid))
          liquidMatches = true;
        else if (tank.canPutFluid(new FluidStack(filterFluid, 1)))
          liquidMatches = true;
        boolean quantityMatches = false;
        switch (mode) {
          case VOID:
            quantityMatches = true;
            break;
          case EMPTY:
            if (!filterFluid.isEmpty() && tank.isTankEmpty(filterFluid))
              quantityMatches = true;
            else if (filterFluid.isEmpty() && tank.areTanksEmpty())
              quantityMatches = true;
            break;
          case NOT_EMPTY:
            if (!filterFluid.isEmpty() && tank.getFluidQty(filterFluid) > 0)
              quantityMatches = true;
            else if (filterFluid.isEmpty() && tank.isFluidInTank())
              quantityMatches = true;
            break;
          case FULL:
            if (!filterFluid.isEmpty() && tank.isTankFull(filterFluid))
              quantityMatches = true;
            else if (filterFluid.isEmpty() && tank.areTanksFull())
              quantityMatches = true;
            break;
          default:
            float level = !filterFluid.isEmpty() ? tank.getFluidLevel(filterFluid) : tank.getFluidLevel();
            if (mode == Mode.ANALOG) {
              return (int) (Redstone.SIGNAL_MAX * level);
            }
            quantityMatches = switch (mode) {
              case QUARTER -> level >= 0.25f;
              case HALF -> level >= 0.5f;
              case MOST -> level >= 0.75f;
              case LESS_THAN_QUARTER -> level < 0.25f;
              case LESS_THAN_HALF -> level < 0.5f;
              case LESS_THAN_MOST -> level < 0.75f;
              case LESS_THAN_FULL -> level < 1f;
              default -> false;
            };
        }
        return liquidMatches && quantityMatches ? Redstone.SIGNAL_MAX : Redstone.SIGNAL_NONE;
      }
    }
    return Redstone.SIGNAL_NONE;
  }

  @Override
  public void saveAdditional(CompoundTag tag) {
    super.saveAdditional(tag);
    tag.putString(CompoundTagKeys.MODE, this.mode.getSerializedName());
  }

  @Override
  public void load(CompoundTag tag) {
    super.load(tag);
    this.mode = Mode.fromName(tag.getString(CompoundTagKeys.MODE));
  }

  @Override
  public void writeToBuf(FriendlyByteBuf data) {
    super.writeToBuf(data);
    data.writeEnum(this.mode);
  }

  @Override
  public void readFromBuf(FriendlyByteBuf data) {
    super.readFromBuf(data);
    this.mode = data.readEnum(Mode.class);
  }

  @Nullable
  @Override
  public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
    return new TankDetectorMenu(id, inventory, this);
  }

  public Mode getMode() {
    return this.mode;
  }

  public void setMode(Mode mode) {
    this.mode = mode;
  }

  public enum Mode implements ButtonState<Mode>, StringRepresentable {

    VOID("void", "L = *"),
    EMPTY("empty", "L = 0%"),
    NOT_EMPTY("not_empty", "L > 0%"),
    FULL("full", "L = 100%"),
    QUARTER("quarter", "L >= 25%"),
    HALF("half", "L >= 50%"),
    MOST("most", "L >= 75%"),
    LESS_THAN_QUARTER("less_than_quarter", "L < 25%"),
    LESS_THAN_HALF("less_than_half", "L < 50%"),
    LESS_THAN_MOST("less_than_most", "L < 75%"),
    LESS_THAN_FULL("less_than_full", "L < 100%"),
    ANALOG("analog", "L = ~");

    private static final StringRepresentable.EnumCodec<Mode> CODEC =
        StringRepresentable.fromEnum(Mode::values);

    private final String name;
    private final String label;

    Mode(String name, String label) {
      this.name = name;
      this.label = label;
    }

    @Override
    public Component label() {
      return Component.literal(this.label);
    }

    @Override
    public TexturePosition texturePosition() {
      return ButtonTexture.LARGE_BUTTON;
    }

    @Override
    public Optional<Component> tooltip() {
      return Optional.of(Component.translatable(
          Translations.makeKey("screen", "tank_detector." + name)));
    }

    @Override
    public Mode next() {
      return EnumUtil.next(this, values());
    }

    @Override
    public String getSerializedName() {
      return this.name;
    }

    public static Mode fromName(String name) {
      return CODEC.byName(name, VOID);
    }
  }
}
