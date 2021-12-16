package mods.railcraft.world.level.block.entity;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import com.mojang.authlib.GameProfile;
import mods.railcraft.api.core.Lockable;
import mods.railcraft.client.gui.widget.button.ButtonTexture;
import mods.railcraft.client.gui.widget.button.TexturePosition;
import mods.railcraft.gui.button.ButtonState;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class LockableSwitchTrackActuatorBlockEntity extends RailcraftBlockEntity
    implements Lockable {

  private Lock lock = Lock.UNLOCKED;

  public LockableSwitchTrackActuatorBlockEntity(BlockEntityType<?> type, BlockPos blockPos,
      BlockState blockState) {
    super(type, blockPos, blockState);
  }

  public Lock getLock() {
    return this.lock;
  }

  public void setLock(Lock lock) {
    this.lock = lock;
  }

  @Override
  public boolean isLocked() {
    return this.lock == Lock.LOCKED;
  }

  public boolean canAccess(GameProfile gameProfile) {
    return !this.isLocked() || this.isOwnerOrOperator(gameProfile);
  }

  @Override
  protected void saveAdditional(CompoundTag tag) {
    super.saveAdditional(tag);
    tag.putString("lock", this.lock.getSerializedName());
  }

  @Override
  public void load( CompoundTag tag) {
    super.load( tag);
    this.lock = Lock.getByName(tag.getString("lock")).orElse(Lock.UNLOCKED);
  }

  @Override
  public void writeToBuf(FriendlyByteBuf data) {
    super.writeToBuf(data);
    data.writeEnum(this.lock);
  }

  @Override
  public void readFromBuf(FriendlyByteBuf data) {
    super.readFromBuf(data);
    this.lock = data.readEnum(Lock.class);
  }

  public enum Lock implements ButtonState<Lock>, StringRepresentable {

    UNLOCKED("unlocked", ButtonTexture.UNLOCKED_BUTTON),
    LOCKED("locked", ButtonTexture.LOCKED_BUTTON);

    private static final Map<String, Lock> byName = Arrays.stream(values())
        .collect(Collectors.toMap(Lock::getSerializedName, Function.identity()));

    private final String name;
    private final TexturePosition texture;

    private Lock(String name, TexturePosition texture) {
      this.name = name;
      this.texture = texture;
    }

    @Override
    public Component getLabel() {
      return TextComponent.EMPTY;
    }

    @Override
    public TexturePosition getTexturePosition() {
      return this.texture;
    }

    @Override
    public Lock getNext() {
      return values()[(this.ordinal() + 1) % values().length];
    }

    @Override
    public String getSerializedName() {
      return this.name;
    }

    public static Optional<Lock> getByName(String name) {
      return Optional.ofNullable(byName.get(name));
    }
  }
}
