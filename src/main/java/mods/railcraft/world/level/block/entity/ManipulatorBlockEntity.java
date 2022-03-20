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
import mods.railcraft.util.container.AdvancedContainer;
import mods.railcraft.util.container.StackFilter;
import mods.railcraft.world.level.block.ManipulatorBlock;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.Container;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public abstract class ManipulatorBlockEntity extends ContainerBlockEntity implements MenuProvider {

  public static final float STOP_VELOCITY = 0.02f;
  public static final int PAUSE_DELAY = 4;

  private static final Set<RedstoneMode> SUPPORTED_REDSTONE_MODES =
      Collections.unmodifiableSet(EnumSet.allOf(RedstoneMode.class));

  private final AdvancedContainer cartFiltersInventory =
      new AdvancedContainer(2).callback((Container) this).phantom();
  private RedstoneMode redstoneMode = RedstoneMode.COMPLETE;
  @Nullable
  protected AbstractMinecart currentCart;
  private boolean sendCartGateAction;
  private boolean processing;
  private int pause;
  protected int resetTimer;

  public ManipulatorBlockEntity(BlockEntityType<?> type, BlockPos blockPos, BlockState blockState) {
    super(type, blockPos, blockState);
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
  public AbstractMinecart getCart() {
    return EntitySearcher.findMinecarts()
        .around(this.getBlockPos().relative(this.getFacing()))
        .inflate(-0.1F)
        .search(this.level)
        .any();
  }

  public boolean canHandleCart(AbstractMinecart cart) {
    if (this.isSendCartGateAction()) {
      return false;
    }
    ItemStack filterStack1 = this.getCartFilters().getItem(0);
    ItemStack filterStack2 = this.getCartFilters().getItem(1);
    if (!filterStack1.isEmpty() || !filterStack2.isEmpty()) {
      Predicate<ItemStack> matcher = StackFilter.isCart(cart);
      return matcher.test(filterStack1) || matcher.test(filterStack2);
    }
    return true;
  }

  protected void setCurrentCart(@Nullable AbstractMinecart newCart) {
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

  protected final void trySendCart(AbstractMinecart cart) {
    if (this.redstoneMode != RedstoneMode.MANUAL && !this.isPowered()
        && !this.hasWorkForCart(cart)) {
      sendCart(cart);
    }
  }

  protected abstract boolean hasWorkForCart(AbstractMinecart cart);

  protected void sendCart(AbstractMinecart cart) {
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

  public final AdvancedContainer getCartFilters() {
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

  protected void waitForReset(@Nullable AbstractMinecart cart) {
    this.sendCart(cart);
  }

  protected void onNoCart() {}

  public static void serverTick(Level level, BlockPos blockPos, BlockState blockState,
      ManipulatorBlockEntity blockEntity) {
    blockEntity.upkeep();

    if (blockEntity.pause > 0) {
      blockEntity.pause--;
    }

    boolean lastProcessing = blockEntity.isProcessing();

    blockEntity.setProcessing(false);

    // Find cart to play with
    AbstractMinecart cart = blockEntity.getCart();

    blockEntity.setCurrentCart(cart);

    // Wait for reset timer (used by loaders that trickle fill forever)
    if (blockEntity.resetTimer > 0) {
      blockEntity.resetTimer--;
    }

    if (blockEntity.resetTimer > 0) {
      blockEntity.waitForReset(cart);
      return;
    }

    // We are alone
    if (cart == null) {
      blockEntity.onNoCart();
      return;
    }

    // We only like some carts
    if (!blockEntity.canHandleCart(cart)) {
      blockEntity.sendCart(cart);
      return;
    }

    // Time out
    if (blockEntity.isPaused()) {
      return;
    }

    // Play time!
    blockEntity.processCart(cart);

    // We did something!
    if (blockEntity.isProcessing()) {
      blockEntity.setPowered(false);
    } else {
      // Are we done?
      blockEntity.trySendCart(cart);
    }

    // Tell our twin
    if (blockEntity.isProcessing() != lastProcessing) {
      blockEntity.syncToClient();
    }
  }

  protected void upkeep() {}

  protected abstract void processCart(AbstractMinecart cart);

  @Override
  public void writeToBuf(FriendlyByteBuf data) {
    super.writeToBuf(data);
    data.writeEnum(this.redstoneMode);
  }

  @Override
  public void readFromBuf(FriendlyByteBuf data) {
    super.readFromBuf(data);
    this.redstoneMode = data.readEnum(RedstoneMode.class);
  }

  @Override
  protected void saveAdditional(CompoundTag tag) {
    super.saveAdditional(tag);
    tag.putString("redstoneMode", this.redstoneMode.getSerializedName());
    tag.put("cartFilters", this.getCartFilters().createTag());
  }

  @Override
  public void load(CompoundTag tag) {
    super.load(tag);
    this.setPowered(ManipulatorBlock.isPowered(this.getBlockState()));
    this.redstoneMode =
        RedstoneMode.getByName(tag.getString("redstoneMode")).orElse(RedstoneMode.COMPLETE);
    this.getCartFilters().fromTag(tag.getList("cartFilters", Tag.TAG_COMPOUND));
  }

  public enum TransferMode implements ButtonState<TransferMode>, StringRepresentable {

    ALL("all", new TextComponent("\u27a7\u27a7\u27a7")),
    EXCESS("excess", new TextComponent("#\u27a7\u27a7")),
    STOCK("stock", new TextComponent("\u27a7\u27a7#")),
    TRANSFER("transfer", new TextComponent("\u27a7#\u27a7"));

    private static final Map<String, TransferMode> byName = Arrays.stream(values())
        .collect(Collectors.toMap(TransferMode::getSerializedName, Function.identity()));

    private final String name;
    private final Component label;
    private final List<Component> tooltip;

    private TransferMode(String name, Component label) {
      this.name = name;
      this.label = label;
      final String translationKey = "manipulator.transfer_mode." + name;
      this.tooltip = ImmutableList.of(
          label.copy().withStyle(ChatFormatting.WHITE),
          new TranslatableComponent(translationKey).withStyle(ChatFormatting.DARK_GREEN),
          new TranslatableComponent(translationKey + ".description"));
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
    public List<Component> getTooltip() {
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

  public enum RedstoneMode implements ButtonState<RedstoneMode>, StringRepresentable {

    COMPLETE("complete", new TextComponent("\u2714")),
    IMMEDIATE("immediate", new TextComponent("\u2762")),
    MANUAL("manual", new TextComponent("\u2718")),
    PARTIAL("partial", new TextComponent("\u27a7"));

    private static final Map<String, RedstoneMode> byName = Arrays.stream(values())
        .collect(Collectors.toMap(RedstoneMode::getSerializedName, Function.identity()));

    private final String name;
    private final Component label;
    private final List<Component> tooltip;

    private RedstoneMode(String name, Component label) {
      this.name = name;
      this.label = label;
      final String translationKey = "manipulator.redstone_mode." + name;
      this.tooltip = ImmutableList.of(
          label.copy().withStyle(ChatFormatting.WHITE),
          new TranslatableComponent(translationKey).withStyle(ChatFormatting.DARK_GREEN),
          new TranslatableComponent(translationKey + ".description"));
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
    public List<Component> getTooltip() {
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
