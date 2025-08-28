package mods.railcraft.world.entity.vehicle;

import mods.railcraft.Translations;
import mods.railcraft.api.carts.RollingStock;
import mods.railcraft.api.core.CompoundTagKeys;
import mods.railcraft.api.track.TrackUtil;
import mods.railcraft.api.util.EnumUtil;
import mods.railcraft.attachment.RailcraftAttachmentTypes;
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
import net.minecraft.world.phys.Vec3;

public abstract class MaintenanceMinecart extends RailcraftMinecart {

  private static final EntityDataAccessor<Integer> BLINK =
      SynchedEntityData.defineId(MaintenanceMinecart.class, EntityDataSerializers.INT);
  protected static final double DRAG_FACTOR = 0.9;
  private static final int BLINK_DURATION = 3;
  private static final EntityDataAccessor<Mode> MODE =
      SynchedEntityData.defineId(MaintenanceMinecart.class, RailcraftDataSerializers.MAINTENANCE_MODE);

  protected MaintenanceMinecart(EntityType<?> type, Level level) {
    super(type, level);
  }

  protected MaintenanceMinecart(ItemStack itemStack, EntityType<?> type, Level level,
      double x, double y, double z) {
    super(itemStack, type, level, x, y, z);
  }

  @Override
  protected void defineSynchedData(SynchedEntityData.Builder builder) {
    super.defineSynchedData(builder);
    builder.define(BLINK, 0);
    builder.define(MODE, Mode.ON);
  }

  public Mode mode() {
    return this.entityData.get(MODE);
  }

  public void setMode(Mode mode) {
    this.entityData.set(MODE, mode);
    this.setData(RailcraftAttachmentTypes.MAX_CART_SPEED_ON_RAIL, mode.speed());
  }

  @Override
  public int getContainerSize() {
    return 0;
  }

  protected void blink() {
    this.entityData.set(BLINK, BLINK_DURATION);
  }

  protected void setBlink(int blink) {
    this.entityData.set(BLINK, blink);
  }

  protected int getBlink() {
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
      this.setBlink(this.getBlink() - 1);
    }
  }

  @Override
  public Vec3 applyNaturalSlowdown(Vec3 speed) {
    return speed.multiply(DRAG_FACTOR, 1.0D, DRAG_FACTOR);
  }

  @Override
  protected void addAdditionalSaveData(CompoundTag tag) {
    super.addAdditionalSaveData(tag);
    tag.store(CompoundTagKeys.MODE, Mode.CODEC, this.mode());
  }

  @Override
  protected void readAdditionalSaveData(CompoundTag tag) {
    super.readAdditionalSaveData(tag);
    this.setMode(tag.read(CompoundTagKeys.MODE, Mode.CODEC).orElse(Mode.ON));
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
  }
}
