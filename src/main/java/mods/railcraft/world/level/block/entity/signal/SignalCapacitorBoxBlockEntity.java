package mods.railcraft.world.level.block.entity.signal;

import java.util.Optional;
import mods.railcraft.Translations;
import mods.railcraft.api.core.CompoundTagKeys;
import mods.railcraft.api.signal.SignalAspect;
import mods.railcraft.api.util.EnumUtil;
import mods.railcraft.client.gui.widget.button.ButtonTexture;
import mods.railcraft.client.gui.widget.button.TexturePosition;
import mods.railcraft.gui.button.ButtonState;
import mods.railcraft.season.Season;
import mods.railcraft.util.RedstoneUtil;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.redstone.Redstone;

public class SignalCapacitorBoxBlockEntity extends AbstractSignalBoxBlockEntity {

  private short ticksPowered;
  private short ticksToPower = 200;
  private SignalAspect signalAspect = SignalAspect.OFF;
  private Mode mode = Mode.RISING_EDGE;

  public SignalCapacitorBoxBlockEntity(BlockPos blockPos, BlockState blockState) {
    super(RailcraftBlockEntityTypes.SIGNAL_CAPACITOR_BOX.get(), blockPos, blockState);
  }

  public short getTicksToPower() {
    return this.ticksToPower;
  }

  public void setTicksToPower(short ticksToPower) {
    this.ticksToPower = ticksToPower;
    this.setChanged();
  }

  public Mode getMode() {
    return this.mode;
  }

  public void setMode(Mode mode) {
    this.mode = mode;
    this.setChanged();
  }

  public static void serverTick(Level level, BlockPos blockPos, BlockState blockState,
      SignalCapacitorBoxBlockEntity blockEntity) {
    if (blockEntity.ticksPowered-- > 0) {
      if (blockEntity.mode == Mode.FALLING_EDGE) {
        var signalAspect = SignalAspect.GREEN;
        var powered = RedstoneUtil.hasRepeaterSignal(level, blockPos);
        for (var direction : Direction.Plane.HORIZONTAL) {
          var neighbor = level.getBlockEntity(blockPos.relative(direction));
          if (neighbor instanceof AbstractSignalBoxBlockEntity box) {
            if (box.getRedstoneSignal(direction.getOpposite()) > 0) {
              powered = true;
              signalAspect = SignalAspect.mostRestrictive(signalAspect,
                  box.getSignalAspect(direction.getOpposite()));
            }
          }
        }
        if (powered) {
          blockEntity.ticksPowered = blockEntity.ticksToPower;
          if (blockEntity.signalAspect != signalAspect) {
            // change to the most restrictive aspect found// above.
            blockEntity.signalAspect = signalAspect;
            blockEntity.setChanged();
          }
        }
      }

      if (blockEntity.ticksPowered <= 0) {
        blockEntity.setChanged();
      }
    }
  }

  @Override
  public void setChanged() {
    super.setChanged();
    this.syncToClient();
  }

  @Override
  public void neighborChanged() {
    if (this.level.isClientSide()) {
      return;
    }
    var powered = RedstoneUtil.hasRepeaterSignal(this.level, this.getBlockPos());
    if (this.ticksPowered <= 0 && powered) {
      this.ticksPowered = this.ticksToPower;
      if (this.mode == Mode.RISING_EDGE) {
        this.signalAspect = SignalAspect.GREEN;
      }
      this.setChanged();
    }
  }

  @Override
  public void neighborSignalBoxChanged(AbstractSignalBoxBlockEntity neighbor,
      Direction direction, boolean removed) {
    if (neighbor.getRedstoneSignal(direction) > 0) {
      this.ticksPowered = this.ticksToPower;
      if (this.mode == Mode.RISING_EDGE) {
        this.signalAspect = neighbor.getSignalAspect(direction);
      }
      this.setChanged();
    }
  }

  @Override
  public int getRedstoneSignal(Direction direction) {
    var blockEntity =
        this.level.getBlockEntity(this.getBlockPos().relative(direction.getOpposite()));
    return blockEntity instanceof AbstractSignalBoxBlockEntity || this.ticksPowered <= 0
        ? Redstone.SIGNAL_NONE
        : Redstone.SIGNAL_MAX;
  }

  @Override
  protected void saveAdditional(CompoundTag tag) {
    super.saveAdditional(tag);
    tag.putShort(CompoundTagKeys.TICKS_POWERED, this.ticksPowered);
    tag.putShort(CompoundTagKeys.TICKS_TO_POWER, this.ticksToPower);
    tag.putString(CompoundTagKeys.SIGNAL_ASPECT, this.signalAspect.getSerializedName());
    tag.putInt(CompoundTagKeys.MODE, this.mode.ordinal());
  }

  @Override
  public void load(CompoundTag tag) {
    super.load(tag);
    this.ticksPowered = tag.getShort(CompoundTagKeys.TICKS_POWERED);
    this.ticksToPower = tag.getShort(CompoundTagKeys.TICKS_TO_POWER);
    this.signalAspect =
        SignalAspect.fromName(tag.getString(CompoundTagKeys.SIGNAL_ASPECT)).orElse(SignalAspect.OFF);
    // TODO: 1.20.4+ use Mode.fromName(tag.getString("CompoundTagKeys.MODE"));
    this.mode = Mode.values()[tag.getInt(CompoundTagKeys.MODE)];
  }

  @Override
  public void writeToBuf(FriendlyByteBuf data) {
    super.writeToBuf(data);
    data.writeBoolean(this.ticksPowered > 0);
    data.writeShort(this.ticksToPower);
    data.writeEnum(this.signalAspect);
    data.writeEnum(this.mode);
  }

  @Override
  public void readFromBuf(FriendlyByteBuf data) {
    super.readFromBuf(data);
    this.ticksPowered = (short) (data.readBoolean() ? 1 : 0);
    this.ticksToPower = data.readShort();
    this.signalAspect = data.readEnum(SignalAspect.class);
    this.mode = data.readEnum(Mode.class);
  }

  @Override
  public SignalAspect getSignalAspect(Direction direction) {
    return this.ticksPowered > 0 ? this.signalAspect : SignalAspect.RED;
  }

  public enum Mode implements ButtonState<Mode>, StringRepresentable {

    RISING_EDGE("rising_edge"),
    FALLING_EDGE("falling_edge");

    private static final StringRepresentable.EnumCodec<Mode> CODEC =
        StringRepresentable.fromEnum(Mode::values);

    private final String name;

    Mode(String name) {
      this.name = name;
    }

    @Override
    public Component label() {
      return Component.translatable(this.getTranslationKey());
    }

    @Override
    public Optional<Component> tooltip() {
      return Optional.of(Component.translatable(this.getDescriptionKey()));
    }

    public String getTranslationKey() {
      return Translations.makeKey("signal", "capacitor." + this.name);
    }

    public String getDescriptionKey() {
      return this.getTranslationKey() + ".desc";
    }

    @Override
    public String getSerializedName() {
      return this.name;
    }

    @Override
    public TexturePosition texturePosition() {
      return ButtonTexture.SMALL_BUTTON;
    }

    @Override
    public Mode next() {
      return EnumUtil.next(this, values());
    }

    public static Mode fromName(String name) {
      return CODEC.byName(name, RISING_EDGE);
    }
  }
}
