package mods.railcraft.world.entity;

import java.util.List;
import mods.railcraft.api.carts.CartUtil;
import mods.railcraft.api.track.TrackUtil;
import mods.railcraft.client.gui.widget.button.ButtonTexture;
import mods.railcraft.client.gui.widget.button.TexturePosition;
import mods.railcraft.gui.button.ButtonState;
import mods.railcraft.gui.button.MultiButtonController;
import mods.railcraft.network.RailcraftDataSerializers;
import mods.railcraft.util.TrackTools;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameters;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.state.properties.RailShape;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

/**
 * @author CovertJaguar <https://www.railcraft.info>
 */
public abstract class AbstractMaintenanceMinecartEntity extends AbstractRailcraftMinecartEntity {

  private static final DataParameter<Byte> BLINK =
      EntityDataManager.defineId(AbstractMaintenanceMinecartEntity.class, DataSerializers.BYTE);
  protected static final double DRAG_FACTOR = 0.9;
  private static final int BLINK_DURATION = 3;
  public static final DataParameter<Byte> CART_MODE =
      EntityDataManager.defineId(AbstractMaintenanceMinecartEntity.class, DataSerializers.BYTE);
  private final MultiButtonController<CartModeButtonState> modeController =
      MultiButtonController.create(0, CartModeButtonState.VALUES);

  @Override
  public float getMaxCartSpeedOnRail() {
    return getMode().speed;
  }

  public enum CartMode implements IStringSerializable {

    SERVICE("service", 0.1f),
    TRANSPORT("transport", 0.4f);

    public static final TrackLayerMinecartEntity.CartMode[] VALUES = values();
    private final String name;
    public final float speed;

    private CartMode(String name, float speed) {
      this.name = name;
      this.speed = speed;
    }

    @Override
    public String getSerializedName() {
      return this.name;
    }
  }

  public MultiButtonController<CartModeButtonState> getModeController() {
    return modeController;
  }

  public enum CartModeButtonState implements ButtonState {

    SERVICE("service"),
    TRANSPORT("transport");

    public static final CartModeButtonState[] VALUES = values();
    private final String name;
    private final ITextComponent label;

    private CartModeButtonState(String name) {
      this.name = name;
      this.label = new TranslationTextComponent("gui.railcraft.cart.maintenance.mode." + name);
    }

    @Override
    public ITextComponent getLabel() {
      return this.label;
    }

    @Override
    public TexturePosition getTexturePosition() {
      return ButtonTexture.SMALL_BUTTON;
    }

    @Override
    public String getSerializedName() {
      return this.name;
    }
  }

  public CartMode getMode() {
    return RailcraftDataSerializers.getEnum(this.entityData, CART_MODE, CartMode.VALUES);
  }

  public void setMode(CartMode mode) {
    if (getMode() != mode)
      RailcraftDataSerializers.setEnum(this.entityData, CART_MODE, mode);
  }

  public CartMode getOtherMode() {
    if (getMode() == CartMode.SERVICE)
      return CartMode.TRANSPORT;
    else if (getMode() == CartMode.TRANSPORT)
      return CartMode.SERVICE;
    return CartMode.SERVICE;
  }

  protected AbstractMaintenanceMinecartEntity(EntityType<?> type, World world) {
    super(type, world);
  }

  protected AbstractMaintenanceMinecartEntity(EntityType<?> type, double x, double y, double z,
      World world) {
    super(type, x, y, z, world);
  }

  @Override
  protected void defineSynchedData() {
    super.defineSynchedData();
    this.entityData.define(BLINK, (byte) 0);
    this.entityData.define(CART_MODE, (byte) CartMode.SERVICE.ordinal());
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
    if (this.level.isClientSide())
      return;

    if (isBlinking())
      setBlink((byte) (getBlink() - 1));
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
    if (!trackStock.isEmpty())
      if (TrackUtil.placeRailAt(trackStock, (ServerWorld) this.level, pos)) {
        this.removeItem(slotStock, 1);
        blink();
        return true;
      }
    return false;
  }

  protected RailShape removeOldTrack(BlockPos pos, BlockState state) {
    List<ItemStack> drops = state.getDrops(new LootContext.Builder((ServerWorld) this.level)
        .withParameter(LootParameters.ORIGIN, Vector3d.atCenterOf(pos)));

    for (ItemStack stack : drops) {
      CartUtil.transferHelper().offerOrDropItem(this, stack);
    }
    RailShape trackShape = TrackTools.getTrackDirectionRaw(state);
    this.level.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
    return trackShape;
  }
}
