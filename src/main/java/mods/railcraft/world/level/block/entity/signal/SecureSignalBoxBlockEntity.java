package mods.railcraft.world.level.block.entity.signal;

import com.mojang.authlib.GameProfile;
import mods.railcraft.api.core.Secure;
import mods.railcraft.client.gui.widget.button.ButtonTexture;
import mods.railcraft.client.gui.widget.button.TexturePosition;
import mods.railcraft.gui.button.ButtonState;
import mods.railcraft.gui.button.MultiButtonController;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

/**
 * @author CovertJaguar <https://www.railcraft.info/>
 */
public abstract class SecureSignalBoxBlockEntity extends AbstractSignalBoxBlockEntity
    implements Secure<SecureSignalBoxBlockEntity.Lock> {

  private final MultiButtonController<Lock> lockController =
      MultiButtonController.create(0, Lock.values());

  public SecureSignalBoxBlockEntity(TileEntityType<?> type) {
    super(type);
  }

  @Override
  public MultiButtonController<Lock> getLockController() {
    return this.lockController;
  }

  @Override
  public boolean isLocked() {
    return this.lockController.getCurrentState() == Lock.LOCKED;
  }

  public boolean canAccess(GameProfile gameProfile) {
    return !this.isLocked() || this.isOwnerOrOperator(gameProfile);
  }

  @Override
  public CompoundNBT save(CompoundNBT data) {
    super.save(data);
    data.put("lock", this.lockController.serializeNBT());
    return data;
  }

  @Override
  public void load(BlockState state, CompoundNBT data) {
    super.load(state, data);
    this.lockController.deserializeNBT(data.getCompound("lock"));
  }

  @Override
  public void writeSyncData(PacketBuffer data) {
    super.writeSyncData(data);
    data.writeEnum(this.lockController.getCurrentState());
  }

  @Override
  public void readSyncData(PacketBuffer data) {
    super.readSyncData(data);
    this.lockController.setCurrentState(data.readEnum(Lock.class));
  }

  public enum Lock implements ButtonState {

    UNLOCKED("unlocked", ButtonTexture.UNLOCKED_BUTTON),
    LOCKED("locked", ButtonTexture.LOCKED_BUTTON);

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
    public String getSerializedName() {
      return this.name;
    }
  }
}
