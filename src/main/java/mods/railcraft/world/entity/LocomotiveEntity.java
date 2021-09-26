package mods.railcraft.world.entity;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import javax.annotation.Nullable;
import org.apache.commons.lang3.StringUtils;
import com.mojang.authlib.GameProfile;
import mods.railcraft.NBTPlugin;
import mods.railcraft.Railcraft;
import mods.railcraft.advancements.criterion.RailcraftAdvancementTriggers;
import mods.railcraft.api.carts.CartToolsAPI;
import mods.railcraft.api.carts.ILinkableCart;
import mods.railcraft.api.carts.IMinecart;
import mods.railcraft.api.carts.IPaintedCart;
import mods.railcraft.api.carts.IRoutableCart;
import mods.railcraft.carts.CartTools;
import mods.railcraft.carts.LinkageManager;
import mods.railcraft.carts.LinkageManager.LinkType;
import mods.railcraft.carts.Train;
import mods.railcraft.client.ClientEffects;
import mods.railcraft.event.MinecartInteractEvent;
import mods.railcraft.gui.buttons.ButtonTextureSet;
import mods.railcraft.gui.buttons.IButtonTextureSet;
import mods.railcraft.gui.buttons.IMultiButtonState;
import mods.railcraft.gui.buttons.MultiButtonController;
import mods.railcraft.plugins.DataManagerPlugin;
import mods.railcraft.plugins.PlayerPlugin;
import mods.railcraft.plugins.SeasonPlugin;
import mods.railcraft.util.ISecureObject;
import mods.railcraft.util.MathTools;
import mods.railcraft.util.MiscTools;
import mods.railcraft.util.RCEntitySelectors;
import mods.railcraft.util.collections.Streams;
import mods.railcraft.util.inventory.InvTools;
import mods.railcraft.world.damagesource.RailcraftDamageSource;
import mods.railcraft.world.entity.LocomotiveEntity.LocoLockButtonState;
import mods.railcraft.world.item.ItemTicket;
import mods.railcraft.world.item.LocomotiveItem;
import mods.railcraft.world.item.RailcraftItems;
import mods.railcraft.world.level.block.track.behaivor.HighSpeedTools;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.DyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.ITextProperties;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.network.NetworkHooks;

/**
 * @author CovertJaguar <http://www.railcraft.info>
 */
public abstract class LocomotiveEntity extends AbstractRailcraftMinecartEntity
    implements IDirectionalCart, ILinkableCart, IMinecart, ISecureObject<LocoLockButtonState>,
    IPaintedCart, IRoutableCart, IEntityAdditionalSpawnData {

  private static final DataParameter<Boolean> HAS_FUEL =
      EntityDataManager.defineId(LocomotiveEntity.class, DataSerializers.BOOLEAN);
  private static final DataParameter<Byte> LOCOMOTIVE_MODE =
      EntityDataManager.defineId(LocomotiveEntity.class, DataSerializers.BYTE);
  private static final DataParameter<Byte> LOCOMOTIVE_SPEED =
      EntityDataManager.defineId(LocomotiveEntity.class, DataSerializers.BYTE);
  private static final DataParameter<Boolean> REVERSE =
      EntityDataManager.defineId(LocomotiveEntity.class, DataSerializers.BOOLEAN);
  private static final DataParameter<Integer> PRIMARY_COLOR =
      EntityDataManager.defineId(LocomotiveEntity.class, DataSerializers.INT);
  private static final DataParameter<Integer> SECONDARY_COLOR =
      EntityDataManager.defineId(LocomotiveEntity.class, DataSerializers.INT);
  private static final DataParameter<String> EMBLEM =
      EntityDataManager.defineId(LocomotiveEntity.class, DataSerializers.STRING);
  private static final DataParameter<String> DEST =
      EntityDataManager.defineId(LocomotiveEntity.class, DataSerializers.STRING);

  private static final double DRAG_FACTOR = 0.9;
  private static final float HS_FORCE_BONUS = 3.5F;
  private static final byte FUEL_USE_INTERVAL = 8;
  private static final byte KNOCKBACK = 1;
  private static final int WHISTLE_INTERVAL = 256;
  private static final int WHISTLE_DELAY = 160;
  private static final int WHISTLE_CHANCE = 4;

  private final MultiButtonController<LocoLockButtonState> lockController =
      MultiButtonController.create(0, LocoLockButtonState.VALUES);
  protected float renderYaw;
  private int fuel;
  private int update = MiscTools.RANDOM.nextInt();
  private int whistleDelay;
  private int tempIdle;
  private float whistlePitch = getNewWhistlePitch();
  private EnumSet<Mode> allowedModes = EnumSet.allOf(Mode.class);
  private Speed maxReverseSpeed = Speed.NORMAL;

  private String clientOwnerName;

  protected LocomotiveEntity(EntityType<?> type, World world) {
    super(type, world);
  }

  protected LocomotiveEntity(EntityType<?> type, double x, double y, double z, World world) {
    super(type, x, y, z, world);
  }

  @Override
  protected void defineSynchedData() {
    super.defineSynchedData();
    this.entityData.define(HAS_FUEL, false);
    this.entityData.define(PRIMARY_COLOR, this.getDefaultPrimaryColor());
    this.entityData.define(SECONDARY_COLOR, this.getDefaultSecondaryColor());
    this.entityData.define(LOCOMOTIVE_MODE, (byte) Mode.SHUTDOWN.ordinal());
    this.entityData.define(LOCOMOTIVE_SPEED, (byte) Speed.NORMAL.ordinal());
    this.entityData.define(REVERSE, false);
    this.entityData.define(EMBLEM, "");
    this.entityData.define(DEST, "");
  }

  protected abstract int getDefaultPrimaryColor();

  protected abstract int getDefaultSecondaryColor();

  public String getClientOwnerName() {
    return this.clientOwnerName;
  }

  @Override
  public void initEntityFromItem(ItemStack item) {
    CompoundNBT nbt = item.getTag();
    if (nbt == null)
      return;

    setPrimaryColor(LocomotiveItem.getPrimaryColor(item).getColorValue());
    setSecondaryColor(LocomotiveItem.getSecondaryColor(item).getColorValue());

    if (nbt.contains("whistlePitch"))
      whistlePitch = nbt.getFloat("whistlePitch");
    if (nbt.contains("owner")) {
      GameProfile ownerProfile = PlayerPlugin.readOwnerFromNBT(nbt);
      CartToolsAPI.setCartOwner(this, ownerProfile);
      setSecurityState(LocoLockButtonState.LOCKED);
    }
    if (nbt.contains("security"))
      setSecurityState(LocoLockButtonState.VALUES[nbt.getByte("security")]);
    if (nbt.contains("emblem"))
      setEmblem(nbt.getString("emblem"));
  }

  @Override
  public MultiButtonController<LocoLockButtonState> getLockController() {
    return lockController;
  }

  @Override
  public GameProfile getOwner() {
    return CartToolsAPI.getCartOwner(this);
  }

  private float getNewWhistlePitch() {
    return 1f + (float) this.random.nextGaussian() * 0.2f;
  }

  @Override
  public ItemStack createCartItem(AbstractMinecartEntity cart) {
    ItemStack item = this.getCartItem();
    if (isSecure() && CartToolsAPI.doesCartHaveOwner(this))
      LocomotiveItem.setOwnerData(item, CartToolsAPI.getCartOwner(this));
    LocomotiveItem.setItemWhistleData(item, whistlePitch);
    LocomotiveItem.setEmblem(item, getEmblem());
    if (hasCustomName())
      item.setHoverName(this.getCustomName());
    return item;
  }

  @Override
  public ActionResultType interact(PlayerEntity player, Hand hand) {
    if (MinecraftForge.EVENT_BUS.post(new MinecartInteractEvent(this, player, hand))) {
      return ActionResultType.CONSUME;
    }
    ItemStack stack = player.getItemInHand(hand);
    if (!level.isClientSide()) {
      if (!stack.isEmpty() && stack.getItem() == RailcraftItems.WHISTLE_TUNER.get()) {
        if (whistleDelay <= 0) {
          whistlePitch = getNewWhistlePitch();
          whistle();
          stack.hurt(1, this.random, (ServerPlayerEntity) player);
        }
        return ActionResultType.CONSUME;
      }
      if (!isPrivate() || PlayerPlugin.isOwnerOrOp(getOwner(), player.getGameProfile()))
        super.interact(player, hand); // open gui
    }
    return ActionResultType.CONSUME;
  }

  @Override
  public boolean isSecure() {
    return getSecurityState() == LocoLockButtonState.LOCKED || isPrivate();
  }

  public boolean isPrivate() {
    return getSecurityState() == LocoLockButtonState.PRIVATE;
  }

  public boolean canControl(GameProfile user) {
    return !isPrivate() || PlayerPlugin.isOwnerOrOp(getOwner(), user);
  }

  public LocoLockButtonState getSecurityState() {
    return lockController.getButtonState();
  }

  public void setSecurityState(LocoLockButtonState state) {
    lockController.setCurrentState(state);
  }

  public String getEmblem() {
    return this.getEntityData().get(EMBLEM);
  }

  public void setEmblem(String emblem) {
    if (!getEmblem().equals(emblem))
      this.getEntityData().set(EMBLEM, emblem);
  }

  public ItemStack getDestItem() {
    return getTicketInventory().getItem(1);
  }

  @Override
  public String getDestination() {
    return StringUtils.defaultIfBlank(this.getEntityData().get(DEST), "");
  }

  public void setDestString(String dest) {
    if (!StringUtils.equals(getDestination(), dest))
      this.getEntityData().set(DEST, dest);
  }

  public Mode getMode() {
    return DataManagerPlugin.readEnum(this.getEntityData(), LOCOMOTIVE_MODE, Mode.VALUES);
  }

  public void setMode(Mode mode) {
    if (!allowedModes.contains(mode))
      mode = Mode.SHUTDOWN;
    if (getMode() != mode)
      DataManagerPlugin.writeEnum(this.getEntityData(), LOCOMOTIVE_MODE, mode);
  }

  public EnumSet<Mode> getAllowedModes() {
    return allowedModes;
  }

  protected final void setAllowedModes(EnumSet<Mode> allowedModes) {
    this.allowedModes = allowedModes;
  }

  public Speed getSpeed() {
    return DataManagerPlugin.readEnum(this.getEntityData(), LOCOMOTIVE_SPEED, Speed.VALUES);
  }

  public void setSpeed(Speed speed) {
    if (getSpeed() != speed)
      DataManagerPlugin.writeEnum(this.getEntityData(), LOCOMOTIVE_SPEED, speed);
  }

  public Speed getMaxReverseSpeed() {
    return maxReverseSpeed;
  }

  protected final void setMaxReverseSpeed(Speed speed) {
    this.maxReverseSpeed = speed;
  }

  public void increaseSpeed() {
    Speed speed = getSpeed();
    speed = speed.shiftUp();
    setSpeed(speed);
  }

  public void decreaseSpeed() {
    Speed speed = getSpeed();
    speed = speed.shiftDown();
    setSpeed(speed);
  }

  public boolean hasFuel() {
    return this.getEntityData().get(HAS_FUEL);
  }

  public void setHasFuel(boolean powered) {
    this.getEntityData().set(HAS_FUEL, powered);
  }

  public boolean isReverse() {
    return this.getEntityData().get(REVERSE);
  }

  public void setReverse(boolean reverse) {
    this.getEntityData().set(REVERSE, reverse);
  }

  public boolean isRunning() {
    return fuel > 0 && getMode() == Mode.RUNNING && !(isIdle() || isShutdown());
  }

  public boolean isIdle() {
    return !isShutdown() && (tempIdle > 0 || getMode() == Mode.IDLE
        || Train.get(this).map(Train::isIdle).orElse(false));
  }

  public boolean isShutdown() {
    return getMode() == Mode.SHUTDOWN || Train.get(this).map(Train::isStopped).orElse(false);
  }

  public void forceIdle(int ticks) {
    tempIdle = Math.max(tempIdle, ticks);
  }

  @Override
  public void reverse() {
    this.yRot += 180;
    this.setDeltaMovement(this.getDeltaMovement().multiply(-1.0D, 1.0D, -1.0D));
  }

  @Override
  public void setRenderYaw(float yaw) {
    renderYaw = yaw;
  }

  public abstract SoundEvent getWhistle();

  public final void whistle() {
    if (whistleDelay <= 0) {
      this.level.playSound(null, getEntity(), getWhistle(), getSoundSource(), 1.0F, 1.0F);
      whistleDelay = WHISTLE_DELAY;
    }
  }

  @Override
  public void tick() {
    super.tick();
    update++;

    if (level.isClientSide()) {
      if (SeasonPlugin.isPolarExpress(this)
          && (!MathTools.nearZero(this.getDeltaMovement().x())
              || !MathTools.nearZero(this.getDeltaMovement().z())))
        ClientEffects.INSTANCE.snowEffect(this.level, this, getBoundingBox().minY - this.getY());
      return;
    }

    // {
    // boolean reverse = ObfuscationReflectionHelper.getPrivateValue(AbstractMinecartEntity.class,
    // this,
    // IS_REVERSED_INDEX);
    // if (reverse != preReverse || prevRotationYaw != rotationYaw) {
    // preReverse = reverse;
    // Game.log(Level.INFO, "tick={0}, reverse={1}, yaw={2}", world.getTotalWorldTime(), reverse,
    // rotationYaw);
    // }
    // }

    processTicket();
    updateFuel();

    // if (getEntityData().getBoolean("HighSpeed"))
    // System.out.println(CartToolsAPI.getCartSpeedUncapped(this));
    if (whistleDelay > 0)
      whistleDelay--;

    if (tempIdle > 0)
      tempIdle--;

    if (update % WHISTLE_INTERVAL == 0 && isRunning() && this.random.nextInt(WHISTLE_CHANCE) == 0)
      whistle();
  }

  @Override
  public boolean setDestination(ItemStack ticket) {
    if (ticket.getItem() instanceof ItemTicket) {
      if (isSecure() && !ItemTicket.matchesOwnerOrOp(ticket, CartToolsAPI.getCartOwner(this)))
        return false;
      String dest = ItemTicket.getDestination(ticket);
      if (!dest.equals(getDestination())) {
        setDestString(dest);
        getTicketInventory().setItem(1, ItemTicket.copyTicket(ticket));
        return true;
      }
    }
    return false;
  }

  protected abstract IInventory getTicketInventory();

  private void processTicket() {
    IInventory invTicket = getTicketInventory();
    ItemStack stack = invTicket.getItem(0);
    if (stack.getItem() instanceof ItemTicket) {
      if (setDestination(stack))
        invTicket.setItem(0, InvTools.depleteItem(stack));
    } else
      invTicket.setItem(0, ItemStack.EMPTY);
  }

  @Override
  protected void applyNaturalSlowdown() {
    this.setDeltaMovement(this.getDeltaMovement().multiply(getDrag(), 0.0D, getDrag()));

    if (isReverse() && getSpeed().getLevel() > getMaxReverseSpeed().getLevel()) {
      setSpeed(getMaxReverseSpeed());
    }

    Speed speed = getSpeed();
    if (isRunning()) {
      double force = Railcraft.serverConfig.locomotiveHorsepower.get() * 0.01F;
      if (isReverse())
        force = -force;
      switch (speed) {
        case MAX:
          boolean highSpeed = HighSpeedTools.isTravellingHighSpeed(this);
          if (highSpeed)
            force *= HS_FORCE_BONUS;
          break;
        default:
          break;
      }
      double yaw = this.yRot * Math.PI / 180D;
      this.setDeltaMovement(
          this.getDeltaMovement().add(Math.cos(yaw) * force, 0, Math.sin(yaw) * force));
    }

    if (speed != Speed.MAX) {
      float limit = 0.4f;
      switch (speed) {
        case SLOWEST:
          limit = 0.1f;
          break;
        case SLOWER:
          limit = 0.2f;
          break;
        case NORMAL:
          limit = 0.3f;
          break;
        default:
          break;
      }

      Vector3d motion = this.getDeltaMovement();

      this.setDeltaMovement(Math.copySign(Math.min(Math.abs(motion.x()), limit), motion.x()),
          motion.y(), Math.copySign(Math.min(Math.abs(motion.z()), limit), motion.z()));
    }
  }

  private int getFuelUse() {
    if (isRunning()) {
      Speed speed = getSpeed();
      switch (speed) {
        case SLOWEST:
          return 2;
        case SLOWER:
          return 4;
        case NORMAL:
          return 6;
        default:
          return 8;
      }
    } else if (isIdle())
      return getIdleFuelUse();
    return 0;
  }

  protected int getIdleFuelUse() {
    return 1;
  }

  protected void updateFuel() {
    if (update % FUEL_USE_INTERVAL == 0 && fuel > 0) {
      fuel -= getFuelUse();
      if (fuel < 0)
        fuel = 0;
    }
    while (fuel <= FUEL_USE_INTERVAL && !isShutdown()) {
      int newFuel = getMoreGoJuice();
      if (newFuel <= 0)
        break;
      fuel += newFuel;
    }
    setHasFuel(fuel > 0);
  }

  private boolean cartVelocityIsGreaterThan(float vel) {
    return Math.abs(this.getDeltaMovement().x()) > vel
        || Math.abs(this.getDeltaMovement().z()) > vel;
  }

  public int getDamageToRoadKill(LivingEntity entity) {
    if (entity instanceof PlayerEntity) {
      ItemStack pants = entity.getItemBySlot(EquipmentSlotType.LEGS);
      if (RailcraftItems.OVERALLS.get() == pants.getItem()) {
        pants.hurtAndBreak(5, entity, __ -> entity.broadcastBreakEvent(EquipmentSlotType.LEGS));
        return 4;
      }
    }
    return 25;
  }

  @Override
  public void push(Entity entity) {
    if (!this.level.isClientSide()) {
      if (!entity.isAlive())
        return;
      if (Train.streamCarts(this).noneMatch(t -> t.hasPassenger(entity))
          && (cartVelocityIsGreaterThan(0.2f) || HighSpeedTools.isTravellingHighSpeed(this))
          && RCEntitySelectors.KILLABLE.test(entity)) {
        LivingEntity living = (LivingEntity) entity;
        if (Railcraft.serverConfig.locomotiveDamageMobs.get())
          living.hurt(RailcraftDamageSource.TRAIN, getDamageToRoadKill(living));
        if (living.getHealth() > 0) {
          float yaw = (this.yRot - 90) * (float) Math.PI / 180.0F;
          this.setDeltaMovement(
              this.getDeltaMovement().add(-MathHelper.sin(yaw) * KNOCKBACK * 0.5F, 0.2D,
                  MathHelper.cos(yaw) * KNOCKBACK * 0.5F));
        } else {
          if (living instanceof ServerPlayerEntity)
            RailcraftAdvancementTriggers.getInstance()
                .onKilledByLocomotive((ServerPlayerEntity) living, this);
        }
        return;
      }
      if (collidedWithOtherLocomotive(entity)) {
        LocomotiveEntity otherLoco = (LocomotiveEntity) entity;
        explode();
        if (otherLoco.isAlive())
          otherLoco.explode();
        return;
      }
    }
    super.push(entity);
  }

  private boolean collidedWithOtherLocomotive(Entity entity) {
    if (!(entity instanceof LocomotiveEntity))
      return false;
    LocomotiveEntity otherLoco = (LocomotiveEntity) entity;
    if (getUUID().equals(entity.getUUID()))
      return false;
    if (Train.areInSameTrain(this, otherLoco))
      return false;

    Vector3d motion = this.getDeltaMovement();
    Vector3d otherMotion = entity.getDeltaMovement();
    return cartVelocityIsGreaterThan(0.2f) && otherLoco.cartVelocityIsGreaterThan(0.2f)
        && (Math.abs(motion.x() - otherMotion.x()) > 0.3f
            || Math.abs(motion.z() - otherMotion.z()) > 0.3f);
  }

  @Override
  public void killAndDrop(AbstractMinecartEntity cart) {
    getTicketInventory().setItem(1, ItemStack.EMPTY);
    super.killAndDrop(cart);
  }

  @Override
  public void remove() {
    getTicketInventory().setItem(1, ItemStack.EMPTY);
    super.remove();
  }

  public void explode() {
    CartTools.explodeCart(this);
    remove();
  }

  public abstract int getMoreGoJuice();

  public double getDrag() {
    return DRAG_FACTOR;
  }

  @Override
  public void addAdditionalSaveData(CompoundNBT data) {
    super.addAdditionalSaveData(data);

    data.putBoolean("flipped", this.flipped);

    data.putString("emblem", getEmblem());

    data.putString("dest", StringUtils.defaultIfBlank(getDestination(), ""));

    data.putByte("locoMode", (byte) getMode().ordinal());
    data.putByte("locoSpeed", (byte) getSpeed().ordinal());

    data.putInt("primaryColor", getPrimaryColor());
    data.putInt("secondaryColor", getSecondaryColor());

    data.putFloat("whistlePitch", whistlePitch);

    data.putInt("fuel", fuel);

    data.putBoolean("reverse", this.getEntityData().get(REVERSE));

    lockController.writeToNBT(data, "lock");
  }

  @Override
  public void readAdditionalSaveData(CompoundNBT data) {
    super.readAdditionalSaveData(data);

    this.flipped = data.getBoolean("flipped");

    setEmblem(data.getString("emblem"));

    setDestString(data.getString("dest"));

    setMode(Mode.VALUES[data.getByte("locoMode")]);
    setSpeed(NBTPlugin.readEnumOrdinal(data, "locoSpeed", Speed.VALUES, Speed.NORMAL));

    setPrimaryColor(data.contains("primaryColor", Constants.NBT.TAG_INT)
        ? data.getInt("primaryColor")
        : DyeColor.BLACK.getColorValue());
    setSecondaryColor(data.contains("secondaryColor", Constants.NBT.TAG_INT)
        ? data.getInt("secondaryColor")
        : DyeColor.RED.getColorValue());

    whistlePitch = data.getFloat("whistlePitch");

    fuel = data.getInt("fuel");

    if (data.contains("reverse", Constants.NBT.TAG_BYTE))
      this.getEntityData().set(REVERSE, data.getBoolean("reverse"));

    lockController.readFromNBT(data, "lock");
  }

  @Override
  public void writeSpawnData(PacketBuffer data) {
    if (this.hasCustomName()) {
      data.writeBoolean(true);
      data.writeComponent(getName());
    } else {
      data.writeBoolean(false);
    }
    data.writeUtf(this.getOwner().getName());
  }

  @Override
  public void readSpawnData(PacketBuffer data) {
    if (data.readBoolean())
      setCustomName(data.readComponent());
    this.clientOwnerName = data.readUtf();
  }

  public static void applyAction(GameProfile gameProfile, AbstractMinecartEntity cart,
      boolean single,
      Consumer<LocomotiveEntity> action) {
    Stream<LocomotiveEntity> locos = Train.streamCarts(cart)
        .flatMap(Streams.ofType(LocomotiveEntity.class))
        .filter(loco -> loco.canControl(gameProfile));
    if (single) {
      locos.findAny().ifPresent(action);
    } else {
      locos.forEach(action);
    }
  }

  @Override
  public boolean canBeRidden() {
    return false;
  }

  @Override
  public int getContainerSize() {
    return 0;
  }

  @Override
  public boolean isPoweredCart() {
    return true;
  }

  @Override
  public boolean isLinkable() {
    return true;
  }

  @Override
  public boolean canLink(AbstractMinecartEntity cart) {
    if (isExemptFromLinkLimits(cart))
      return true;

    LinkageManager lm = LinkageManager.INSTANCE;

    if (StreamSupport.stream(lm.linkIterator(this, LinkType.LINK_A).spliterator(), false)
        .anyMatch(linked -> !isExemptFromLinkLimits(linked))) {
      return false;
    }
    return StreamSupport.stream(lm.linkIterator(this, LinkType.LINK_B).spliterator(), false)
        .allMatch(this::isExemptFromLinkLimits);
  }

  private boolean isExemptFromLinkLimits(AbstractMinecartEntity cart) {
    return cart instanceof LocomotiveEntity || cart instanceof AbstractMaintenanceMinecartEntity;
  }

  @Override
  public float getLinkageDistance(AbstractMinecartEntity cart) {
    return LinkageManager.LINKAGE_DISTANCE;
  }

  @Override
  public float getOptimalDistance(AbstractMinecartEntity cart) {
    return 0.9f;
  }

  @Override
  public void onLinkCreated(AbstractMinecartEntity cart) {
    // Moved from linkage manager - this should not be there
    if (getSpeed().compareTo(Speed.SLOWEST) > 0)
      setSpeed(Speed.SLOWEST);
  }

  @Override
  public boolean canPassItemRequests(ItemStack stack) {
    return true;
  }

  @Override
  public final int getPrimaryColor() {
    return this.entityData.get(PRIMARY_COLOR);
  }

  public final void setPrimaryColor(int color) {
    this.entityData.set(PRIMARY_COLOR, color);
  }

  @Override
  public final int getSecondaryColor() {
    return this.entityData.get(SECONDARY_COLOR);
  }

  public final void setSecondaryColor(int color) {
    this.entityData.set(SECONDARY_COLOR, color);
  }

  public enum Mode implements IStringSerializable {

    SHUTDOWN("shutdown"), IDLE("idle"), RUNNING("running");

    public static final Mode[] VALUES = values();

    private final String name;

    private Mode(String name) {
      this.name = name;
    }

    @Override
    public String getSerializedName() {
      return this.name;
    }
  }

  public enum Speed implements IStringSerializable {

    SLOWEST("slowest", 1, 1, 0),
    SLOWER("slower", 2, 1, -1),
    NORMAL("normal", 3, 1, -1),
    MAX("max", 4, 0, -1);

    public static final Speed[] VALUES = values();

    private static final Map<String, Speed> BY_NAME = Arrays.stream(values())
        .collect(Collectors.toMap(Speed::getSerializedName, Function.identity()));

    private final String name;
    private final int shiftUp;
    private final int shiftDown;
    private final int level;

    private Speed(String name, int level, int shiftUp, int shiftDown) {
      this.name = name;
      this.level = level;
      this.shiftUp = shiftUp;
      this.shiftDown = shiftDown;
    }

    public static Speed fromName(String name) {
      return BY_NAME.get(name);
    }

    public static Speed fromLevel(int level) {
      return VALUES[level - 1];
    }

    public int getLevel() {
      return level;
    }

    @Override
    public String getSerializedName() {
      return this.name;
    }

    public Speed shiftUp() {
      return Speed.VALUES[ordinal() + shiftUp];
    }

    public Speed shiftDown() {
      return Speed.VALUES[ordinal() + shiftDown];
    }
  }

  public enum LocoLockButtonState implements IMultiButtonState {

    UNLOCKED(new ButtonTextureSet(224, 0, 16, 16)),
    LOCKED(new ButtonTextureSet(240, 0, 16, 16)),
    PRIVATE(new ButtonTextureSet(240, 48, 16, 16));

    public static final LocoLockButtonState[] VALUES = values();
    private final IButtonTextureSet texture;

    LocoLockButtonState(IButtonTextureSet texture) {
      this.texture = texture;
    }

    @Override
    public ITextComponent getLabel() {
      return StringTextComponent.EMPTY;
    }

    @Override
    public IButtonTextureSet getTextureSet() {
      return texture;
    }

    @Override
    public @Nullable List<? extends ITextProperties> getTooltip() {
      return null;
    }
  }

  @Override
  public IPacket<?> getAddEntityPacket() {
    return NetworkHooks.getEntitySpawningPacket(this);
  }
}
