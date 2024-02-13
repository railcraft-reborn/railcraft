package mods.railcraft.world.entity.vehicle;

import mods.railcraft.Translations;
import mods.railcraft.api.carts.RollingStock;
import mods.railcraft.api.core.CompoundTagKeys;
import mods.railcraft.api.track.TrackUtil;
import mods.railcraft.api.util.EnumUtil;
import mods.railcraft.client.gui.widget.button.ButtonTexture;
import mods.railcraft.client.gui.widget.button.TexturePosition;
import mods.railcraft.gui.button.ButtonState;
import mods.railcraft.network.RailcraftDataSerializers;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.RailShape;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;

public abstract class MaintenanceMinecart extends RailcraftMinecart {

  private static final EntityDataAccessor<Byte> BLINK =
      SynchedEntityData.defineId(MaintenanceMinecart.class, EntityDataSerializers.BYTE);
  protected static final double DRAG_FACTOR = 0.9;
  private static final int BLINK_DURATION = 3;
  private static final EntityDataAccessor<Byte> MODE =
      SynchedEntityData.defineId(MaintenanceMinecart.class, EntityDataSerializers.BYTE);

  protected MaintenanceMinecart(EntityType<?> type, Level level) {
    super(type, level);
  }

  protected MaintenanceMinecart(EntityType<?> type, double x, double y, double z, Level level) {
    super(type, x, y, z, level);
  }

  @Override
  protected void defineSynchedData() {
    super.defineSynchedData();
    this.entityData.define(BLINK, (byte) 0);
    this.entityData.define(MODE, (byte) Mode.ON.ordinal());
  }

  @Override
  public float getMaxCartSpeedOnRail() {
    return this.mode().speed();
  }

  public Mode mode() {
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
    if (this.level().isClientSide()) {
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

  @Override
  protected void addAdditionalSaveData(CompoundTag tag) {
    super.addAdditionalSaveData(tag);
    tag.putString("mode", this.mode().getSerializedName());
  }

  @Override
  protected void readAdditionalSaveData(CompoundTag tag) {
    super.readAdditionalSaveData(tag);
    this.setMode(Mode.fromName(tag.getString(CompoundTagKeys.MODE)));
  }

  protected boolean placeNewTrack(BlockPos pos, int slotStock, RailShape railShape) {
    ItemStack trackStack = getItem(slotStock);
    if (!trackStack.isEmpty()) {
      if (TrackUtil.placeRailAt(trackStack, (ServerLevel) this.level(), pos, railShape)) {
        this.removeItem(slotStock, 1);
        this.blink();
        return true;
      }
    }
    return false;
  }

  protected RailShape removeOldTrack(BlockPos pos, BlockState state) {
    var drops = state.getDrops(new LootParams.Builder((ServerLevel) this.level())
        .withParameter(LootContextParams.TOOL, ItemStack.EMPTY)
        .withParameter(LootContextParams.ORIGIN, pos.getCenter()));

    var rollingStock = RollingStock.getOrThrow(this);
    for (var stack : drops) {
      rollingStock.offerOrDropItem(stack);
    }
    var trackShape = TrackUtil.getRailShapeRaw(state);
    this.level().setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
    return trackShape;
  }

  public enum Mode implements ButtonState<Mode>, StringRepresentable {

    ON("on", 0.1F),
    OFF("off", 0.4F);

    private static final StringRepresentable.EnumCodec<Mode> CODEC =
        StringRepresentable.fromEnum(Mode::values);

    private final String name;
    private final float speed;

    Mode(String name, float speed) {
      this.name = name;
      this.speed = speed;
    }

    public float speed() {
      return this.speed;
    }

    @Override
    public Component label() {
      return Component.translatable(this.getTranslationKey());
    }

    public String getTranslationKey() {
      return Translations.makeKey("screen", "cart.maintenance.mode." + this.name);
    }

    public String getTipsKey() {
      return Translations.makeKey("tips", "cart.maintenance.mode." + this.name);
    }

    @Override
    public TexturePosition texturePosition() {
      return ButtonTexture.SMALL_BUTTON;
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
      return CODEC.byName(name, ON);
    }
  }
}
