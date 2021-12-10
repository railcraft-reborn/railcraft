package mods.railcraft.world.entity.cart;

import java.util.List;
import mods.railcraft.api.carts.CartUtil;
import mods.railcraft.api.track.TrackUtil;
import mods.railcraft.client.gui.widget.button.ButtonTexture;
import mods.railcraft.client.gui.widget.button.TexturePosition;
import mods.railcraft.gui.button.ButtonState;
import mods.railcraft.network.RailcraftDataSerializers;
import mods.railcraft.util.TrackTools;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.level.block.state.properties.RailShape;
import net.minecraft.util.StringRepresentable;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;

/**
 * @author CovertJaguar <https://www.railcraft.info>
 */
public abstract class MaintenanceMinecartEntity extends RailcraftMinecartEntity {

  private static final EntityDataAccessor<Byte> BLINK =
      SynchedEntityData.defineId(MaintenanceMinecartEntity.class, EntityDataSerializers.BYTE);
  protected static final double DRAG_FACTOR = 0.9;
  private static final int BLINK_DURATION = 3;
  public static final EntityDataAccessor<Byte> MODE =
      SynchedEntityData.defineId(MaintenanceMinecartEntity.class, EntityDataSerializers.BYTE);

  protected MaintenanceMinecartEntity(EntityType<?> type, Level world) {
    super(type, world);
  }

  protected MaintenanceMinecartEntity(EntityType<?> type, double x, double y, double z,
      Level world) {
    super(type, x, y, z, world);
  }

  @Override
  protected void defineSynchedData() {
    super.defineSynchedData();
    this.entityData.define(BLINK, (byte) 0);
    this.entityData.define(MODE, (byte) Mode.SERVICE.ordinal());
  }

  @Override
  public float getMaxCartSpeedOnRail() {
    return this.getMode().getSpeed();
  }

  public Mode getMode() {
    return RailcraftDataSerializers.getEnum(this.entityData, MODE, Mode.values());
  }

  public void setMode(Mode mode) {
    RailcraftDataSerializers.setEnum(this.entityData, MODE, mode);
  }

  @Override
  public int getContainerSize() {
    return 0;
  }

  protected void blink() {
    this.entityData.set(BLINK, (byte) BLINK_DURATION);
  }

  protected void setBlink(byte blink) {
    this.entityData.set(BLINK, blink);
  }

  protected byte getBlink() {
    return this.entityData.get(BLINK);
  }

  public boolean isBlinking() {
    return this.getBlink() > 0;
  }

  @Override
  public void tick() {
    super.tick();
    if (this.level.isClientSide()) {
      return;
    }

    if (this.isBlinking()) {
      this.setBlink((byte) (this.getBlink() - 1));
    }
  }

  @Override
  public boolean canBeRidden() {
    return false;
  }

  @Override
  protected void applyNaturalSlowdown() {
    super.applyNaturalSlowdown();
    this.setDeltaMovement(this.getDeltaMovement().multiply(DRAG_FACTOR, 1.0D, DRAG_FACTOR));
  }

  protected boolean placeNewTrack(BlockPos pos, int slotStock,
      RailShape trackShape) {
    ItemStack trackStock = getItem(slotStock);
    if (!trackStock.isEmpty()) {
      if (TrackUtil.placeRailAt(trackStock, (ServerLevel) this.level, pos)) {
        this.removeItem(slotStock, 1);
        this.blink();
        return true;
      }
    }
    return false;
  }

  protected RailShape removeOldTrack(BlockPos pos, BlockState state) {
    List<ItemStack> drops = state.getDrops(new LootContext.Builder((ServerLevel) this.level)
        .withParameter(LootContextParams.TOOL, ItemStack.EMPTY)
        .withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(pos)));

    for (ItemStack stack : drops) {
      CartUtil.transferHelper().offerOrDropItem(this, stack);
    }
    RailShape trackShape = TrackTools.getRailShapeRaw(state);
    this.level.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
    return trackShape;
  }

  public enum Mode implements ButtonState<Mode>, StringRepresentable {

    SERVICE("service", 0.1F),
    TRANSPORT("transport", 0.4F);

    private final String name;
    private final float speed;
    private final Component label;

    private Mode(String name, float speed) {
      this.name = name;
      this.speed = speed;
      this.label = new TranslatableComponent("gui.railcraft.cart.maintenance.mode." + name);
    }

    public float getSpeed() {
      return this.speed;
    }

    @Override
    public Component getLabel() {
      return this.label;
    }

    @Override
    public TexturePosition getTexturePosition() {
      return ButtonTexture.SMALL_BUTTON;
    }

    @Override
    public Mode getNext() {
      return values()[(this.ordinal() + 1) % values().length];
    }

    @Override
    public String getSerializedName() {
      return this.name;
    }
  }
}
