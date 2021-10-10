package mods.railcraft.world.level.block.entity.signal;

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
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

/**
 * @author CovertJaguar <https://www.railcraft.info/>
 */
public abstract class LockableSignalBoxBlockEntity extends AbstractSignalBoxBlockEntity
    implements Lockable {

  private Lock lock = Lock.UNLOCKED;

  public LockableSignalBoxBlockEntity(TileEntityType<?> type) {
    super(type);
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
  public CompoundNBT save(CompoundNBT data) {
    super.save(data);
    data.putString("lock", this.lock.getSerializedName());
    return data;
  }

  @Override
  public void load(BlockState state, CompoundNBT data) {
    super.load(state, data);
    this.lock = Lock.getByName(data.getString("lock")).orElse(Lock.UNLOCKED);
  }

  @Override
  public void writeSyncData(PacketBuffer data) {
    super.writeSyncData(data);
    data.writeEnum(this.lock);
  }

  @Override
  public void readSyncData(PacketBuffer data) {
    super.readSyncData(data);
    this.lock = data.readEnum(Lock.class);
  }

  public enum Lock implements ButtonState<Lock> {

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
    public ITextComponent getLabel() {
      return StringTextComponent.EMPTY;
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
