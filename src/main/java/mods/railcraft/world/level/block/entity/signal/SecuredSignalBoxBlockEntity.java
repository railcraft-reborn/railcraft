package mods.railcraft.world.level.block.entity.signal;

import com.mojang.authlib.GameProfile;
import mods.railcraft.gui.buttons.LockButtonState;
import mods.railcraft.gui.buttons.MultiButtonController;
import mods.railcraft.plugins.PlayerPlugin;
import mods.railcraft.util.ISecureObject;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntityType;

/**
 * @author CovertJaguar <http://www.railcraft.info/>
 */
public abstract class SecuredSignalBoxBlockEntity extends AbstractSignalBoxBlockEntity
    implements ISecureObject<LockButtonState> {

  private final MultiButtonController<LockButtonState> lockController =
      MultiButtonController.create(0, LockButtonState.VALUES);

  public SecuredSignalBoxBlockEntity(TileEntityType<?> type) {
    super(type);
  }

  @Override
  public MultiButtonController<LockButtonState> getLockController() {
    return lockController;
  }

  @Override
  public boolean isSecure() {
    return lockController.getButtonState() == LockButtonState.LOCKED;
  }

  protected boolean canAccess(GameProfile player) {
    return !isSecure() || PlayerPlugin.isOwnerOrOp(getOwner(), player);
  }

  @Override
  public CompoundNBT save(CompoundNBT data) {
    super.save(data);
    lockController.writeToNBT(data, "lock");
    return data;
  }

  @Override
  public void load(BlockState state, CompoundNBT data) {
    super.load(state, data);
    lockController.readFromNBT(data, "lock");
  }

  @Override
  public void writeSyncData(PacketBuffer data) {
    super.writeSyncData(data);
    data.writeByte(lockController.getCurrentState());
  }

  @Override
  public void readSyncData(PacketBuffer data) {
    super.readSyncData(data);
    lockController.setCurrentState(data.readByte());
  }
}
