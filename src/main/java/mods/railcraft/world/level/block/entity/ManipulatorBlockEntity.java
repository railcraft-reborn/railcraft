package mods.railcraft.world.level.block.entity;

import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import com.google.common.collect.ImmutableList;
import mods.railcraft.api.carts.CartUtil;
import mods.railcraft.client.gui.widget.button.ButtonTexture;
import mods.railcraft.client.gui.widget.button.TexturePosition;
import mods.railcraft.gui.button.ButtonState;
import mods.railcraft.util.EntitySearcher;
import mods.railcraft.util.inventory.InventoryAdvanced;
import mods.railcraft.util.inventory.filters.StackFilters;
import mods.railcraft.world.level.block.ManipulatorBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.ITextProperties;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.util.Constants;

public abstract class ManipulatorBlockEntity extends InventoryBlockEntity
    implements ITickableTileEntity, INamedContainerProvider {

  public static final float STOP_VELOCITY = 0.02f;
  public static final int PAUSE_DELAY = 4;

  private static final Set<RedstoneMode> SUPPORTED_REDSTONE_MODES =
      Collections.unmodifiableSet(EnumSet.allOf(RedstoneMode.class));

  private final InventoryAdvanced cartFiltersInventory =
      new InventoryAdvanced(2).callbackInv(this).phantom();
  private RedstoneMode redstoneMode = RedstoneMode.COMPLETE;
  @Nullable
  protected AbstractMinecartEntity currentCart;
  private boolean sendCartGateAction;
  private boolean processing;
  private int pause;
  protected int resetTimer;

  public ManipulatorBlockEntity(TileEntityType<?> type) {
    super(type);
  }

  public RedstoneMode getRedstoneMode() {
    return this.redstoneMode;
  }

  public void setRedstoneMode(RedstoneMode redstoneMode) {
    if (this.isAllowedRedstoneMode(redstoneMode)) {
      this.redstoneMode = redstoneMode;
    }
  }

  public boolean isAllowedRedstoneMode(RedstoneMode redstoneMode) {
    return this.getSupportedRedstoneModes().contains(redstoneMode);
  }

  public Set<RedstoneMode> getSupportedRedstoneModes() {
    return SUPPORTED_REDSTONE_MODES;
  }

  protected Direction getFacing() {
    BlockState blockState = this.getBlockState();
    return ((ManipulatorBlock<?>) blockState.getBlock()).getFacing(blockState);
  }

  @Nullable
  public AbstractMinecartEntity getCart() {
    return EntitySearcher.findMinecarts()
        .around(this.getBlockPos().relative(this.getFacing()))
        .outTo(-0.1F)
        .in(this.level)
        .any();
  }

  public boolean canHandleCart(AbstractMinecartEntity cart) {
    if (this.isSendCartGateAction()) {
      return false;
    }
    ItemStack filterStack1 = this.getCartFilters().getItem(0);
    ItemStack filterStack2 = this.getCartFilters().getItem(1);
    if (!filterStack1.isEmpty() || !filterStack2.isEmpty()) {
      Predicate<ItemStack> matcher = StackFilters.isCart(cart);
      return matcher.test(filterStack1) || matcher.test(filterStack2);
    }
    return true;
  }

  protected void setCurrentCart(@Nullable AbstractMinecartEntity newCart) {
    if (newCart != this.currentCart) {
      this.reset();
      this.setPowered(false);
      this.currentCart = newCart;
      this.cartWasSent();
    }
  }

  protected void reset() {
    this.resetTimer = 0;
  }

  protected final void setProcessing(boolean processing) {
    this.processing = processing;
  }

  protected final boolean isProcessing() {
    return this.processing;
  }

  public boolean isManualMode() {
    return this.redstoneMode == RedstoneMode.MANUAL;
  }

  protected final void trySendCart(AbstractMinecartEntity cart) {
    if (this.redstoneMode != RedstoneMode.MANUAL && !this.isPowered()
        && !this.hasWorkForCart(cart)) {
      sendCart(cart);
    }
  }

  protected abstract boolean hasWorkForCart(AbstractMinecartEntity cart);

  protected void sendCart(AbstractMinecartEntity cart) {
    if (this.isManualMode()) {
      return;
    }
    if (CartUtil.cartVelocityIsLessThan(cart.getDeltaMovement(), STOP_VELOCITY)
        || cart.isPoweredCart()) {
      this.setPowered(true);
    }
  }

  public final boolean isPowered() {
    return ManipulatorBlock.isPowered(this.getBlockState());
  }

  protected void setPowered(boolean powered) {
    if (powered) {
      this.setProcessing(false);
    }
    if (this.isManualMode()) {
      powered = false;
    }
    if (this.hasLevel() && ManipulatorBlock.isPowered(this.getBlockState()) != powered) {
      this.level.setBlockAndUpdate(this.getBlockPos(),
          this.getBlockState().setValue(ManipulatorBlock.POWERED, powered));
    }
  }

  public final InventoryAdvanced getCartFilters() {
    return this.cartFiltersInventory;
  }

  public boolean isSendCartGateAction() {
    return this.sendCartGateAction;
  }

  public void cartWasSent() {
    this.sendCartGateAction = false;
  }

  public boolean isPaused() {
    return this.pause > 0;
  }

  protected void setResetTimer(int ticks) {
    this.resetTimer = ticks;
  }

  protected void waitForReset(@Nullable AbstractMinecartEntity cart) {
    this.sendCart(cart);
  }

  protected void onNoCart() {}

  @Override
  public final void tick() {
    if (this.level.isClientSide()) {
      return;
    }

    this.upkeep();

    if (this.pause > 0) {
      this.pause--;
    }

    boolean lastProcessing = this.isProcessing();

    this.setProcessing(false);

    // Find cart to play with
    AbstractMinecartEntity cart = this.getCart();

    this.setCurrentCart(cart);

    // Wait for reset timer (used by loaders that trickle fill forever)
    if (this.resetTimer > 0) {
      this.resetTimer--;
    }

    if (this.resetTimer > 0) {
      this.waitForReset(cart);
      return;
    }

    // We are alone
    if (cart == null) {
      this.onNoCart();
      return;
    }

    // We only like some carts
    if (!this.canHandleCart(cart)) {
      this.sendCart(cart);
      return;
    }

    // Time out
    if (this.isPaused()) {
      return;
    }

    // Play time!
    this.processCart(cart);

    // We did something!
    if (this.isProcessing()) {
      this.setPowered(false);
    } else {
      // Are we done?
      this.trySendCart(cart);
    }

    // Tell our twin
    if (this.isProcessing() != lastProcessing) {
      this.syncToClient();
    }
  }

  protected void upkeep() {}

  protected abstract void processCart(AbstractMinecartEntity cart);

  @Override
  public void writeSyncData(PacketBuffer data) {
    super.writeSyncData(data);
    data.writeEnum(this.redstoneMode);
  }

  @Override
  public void readSyncData(PacketBuffer data) {
    super.readSyncData(data);
    this.redstoneMode = data.readEnum(RedstoneMode.class);
  }

  @Override
  public CompoundNBT save(CompoundNBT data) {
    super.save(data);
    data.putString("redstoneMode", this.redstoneMode.getSerializedName());
    data.put("cartFilters", this.getCartFilters().serializeNBT());
    return data;
  }

  @Override
  public void load(BlockState blockState, CompoundNBT data) {
    super.load(blockState, data);
    this.setPowered(ManipulatorBlock.isPowered(blockState));
    this.redstoneMode =
        RedstoneMode.getByName(data.getString("redstoneMode")).orElse(RedstoneMode.COMPLETE);
    this.getCartFilters().deserializeNBT(data.getList("cartFilters", Constants.NBT.TAG_COMPOUND));
  }

  public enum TransferMode implements ButtonState<TransferMode>, IStringSerializable {

    ALL("all", new StringTextComponent("\u27a7\u27a7\u27a7")),
    EXCESS("excess", new StringTextComponent("#\u27a7\u27a7")),
    STOCK("stock", new StringTextComponent("\u27a7\u27a7#")),
    TRANSFER("transfer", new StringTextComponent("\u27a7#\u27a7"));

    private static final Map<String, TransferMode> byName = Arrays.stream(values())
        .collect(Collectors.toMap(TransferMode::getSerializedName, Function.identity()));

    private final String name;
    private final ITextComponent label;
    private final List<ITextProperties> tooltip;

    private TransferMode(String name, ITextComponent label) {
      this.name = name;
      this.label = label;
      final String translationKey = "manipulator.transfer_mode." + name;
      this.tooltip = ImmutableList.of(
          label.copy().withStyle(TextFormatting.WHITE),
          new TranslationTextComponent(translationKey).withStyle(TextFormatting.DARK_GREEN),
          new TranslationTextComponent(translationKey + ".description"));
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
    public List<? extends ITextProperties> getTooltip() {
      return this.tooltip;
    }

    @Override
    public TransferMode getNext() {
      return values()[(this.ordinal() + 1) % values().length];
    }

    @Override
    public String getSerializedName() {
      return this.name;
    }

    public static Optional<TransferMode> getByName(String name) {
      return Optional.ofNullable(byName.get(name));
    }
  }

  public enum RedstoneMode implements ButtonState<RedstoneMode>, IStringSerializable {

    COMPLETE("complete", new StringTextComponent("\u2714")),
    IMMEDIATE("immediate", new StringTextComponent("\u2762")),
    MANUAL("manual", new StringTextComponent("\u2718")),
    PARTIAL("partial", new StringTextComponent("\u27a7"));

    private static final Map<String, RedstoneMode> byName = Arrays.stream(values())
        .collect(Collectors.toMap(RedstoneMode::getSerializedName, Function.identity()));

    private final String name;
    private final ITextComponent label;
    private final List<ITextProperties> tooltip;

    private RedstoneMode(String name, ITextComponent label) {
      this.name = name;
      this.label = label;
      final String translationKey = "manipulator.redstone_mode." + name;
      this.tooltip = ImmutableList.of(
          label.copy().withStyle(TextFormatting.WHITE),
          new TranslationTextComponent(translationKey).withStyle(TextFormatting.DARK_GREEN),
          new TranslationTextComponent(translationKey + ".description"));
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
    public List<? extends ITextProperties> getTooltip() {
      return this.tooltip;
    }

    @Override
    public RedstoneMode getNext() {
      return values()[(this.ordinal() + 1) % values().length];
    }

    @Override
    public String getSerializedName() {
      return this.name;
    }

    public static Optional<RedstoneMode> getByName(String name) {
      return Optional.ofNullable(byName.get(name));
    }
  }
}
