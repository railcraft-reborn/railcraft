package mods.railcraft.world.level.block.entity.signal;

import com.mojang.authlib.GameProfile;
import mods.railcraft.gui.button.ButtonState;
import mods.railcraft.gui.button.ButtonTextureSet;
import mods.railcraft.gui.button.MultiButtonController;
import mods.railcraft.gui.button.SimpleButtonTextureSet;
import mods.railcraft.plugins.PlayerPlugin;
import mods.railcraft.util.ISecureObject;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

/**
 * @author CovertJaguar <http://www.railcraft.info/>
 */
public abstract class SecuredSignalBoxBlockEntity extends AbstractSignalBoxBlockEntity
    implements ISecureObject<SecuredSignalBoxBlockEntity.LockButtonState> {

  private final MultiButtonController<LockButtonState> lockController =
      MultiButtonController.create(0, LockButtonState.values());

  public SecuredSignalBoxBlockEntity(TileEntityType<?> type) {
    super(type);
  }

  @Override
  public MultiButtonController<LockButtonState> getLockController() {
    return this.lockController;
  }

  @Override
  public boolean isSecure() {
    return this.lockController.getCurrentState() == LockButtonState.LOCKED;
  }

  protected boolean canAccess(GameProfile player) {
    return !this.isSecure() || PlayerPlugin.isOwnerOrOp(getOwner(), player);
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
    data.writeByte(this.lockController.getCurrentStateIndex());
  }

  @Override
  public void readSyncData(PacketBuffer data) {
    super.readSyncData(data);
    this.lockController.setCurrentState(data.readByte());
  }

  public enum LockButtonState implements ButtonState {

    UNLOCKED("unlocked", new SimpleButtonTextureSet(224, 0, 16, 16)),
    LOCKED("locked", new SimpleButtonTextureSet(240, 0, 16, 16));

    private final String name;
    private final ButtonTextureSet texture;

    private LockButtonState(String name, ButtonTextureSet texture) {
      this.name = name;
      this.texture = texture;
    }

    @Override
    public ITextComponent getLabel() {
      return StringTextComponent.EMPTY;
    }

    @Override
    public ButtonTextureSet getTextureSet() {
      return this.texture;
    }

    @Override
    public String getSerializedName() {
      return this.name;
    }
  }
}
