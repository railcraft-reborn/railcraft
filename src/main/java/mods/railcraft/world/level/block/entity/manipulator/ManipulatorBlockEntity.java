package mods.railcraft.world.level.block.entity.manipulator;

import java.util.EnumSet;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import org.jetbrains.annotations.Nullable;
import mods.railcraft.Translations;
import mods.railcraft.api.carts.CartUtil;
import mods.railcraft.api.util.EnumUtil;
import mods.railcraft.client.gui.widget.button.ButtonTexture;
import mods.railcraft.client.gui.widget.button.TexturePosition;
import mods.railcraft.gui.button.ButtonState;
import mods.railcraft.util.EntitySearcher;
import mods.railcraft.util.container.AdvancedContainer;
import mods.railcraft.util.container.StackFilter;
import mods.railcraft.world.level.block.entity.ContainerBlockEntity;
import mods.railcraft.world.level.block.manipulator.ManipulatorBlock;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
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
      Set.copyOf(EnumSet.allOf(RedstoneMode.class));

  private final AdvancedContainer cartFiltersInventory =
      new AdvancedContainer(2).listener((Container) this).phantom();
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

  public Optional<AbstractMinecart> findCart() {
    return EntitySearcher.findMinecarts()
        .at(this.getBlockPos().relative(this.getFacing()))
        .inflate(-0.1F)
        .stream(this.level)
        .findAny();
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
    var cart = blockEntity.findCart().orElse(null);

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
    tag.putInt("redstoneMode", this.redstoneMode.ordinal());
    tag.put("cartFilters", this.getCartFilters().createTag());
  }

  @Override
  public void load(CompoundTag tag) {
    super.load(tag);
    this.setPowered(ManipulatorBlock.isPowered(this.getBlockState()));
    this.redstoneMode = RedstoneMode.values()[tag.getInt("redstoneMode")];
    this.getCartFilters().fromTag(tag.getList("cartFilters", Tag.TAG_COMPOUND));
  }

  public enum TransferMode implements ButtonState<TransferMode> {

    ALL("all", "➧➧➧"),
    EXCESS("excess", "#➧➧"),
    STOCK("stock", "➧➧#"),
    TRANSFER("transfer", "➧#➧");

    private final String name;
    private final Component label;

    private TransferMode(String name, String label) {
      this.name = name;
      this.label = Component.literal(label);
    }

    @Override
    public Component getLabel() {
      return this.label;
    }

    @Override
    public Optional<Component> getTooltip() {
      return Optional.of(CommonComponents.joinLines(
          this.getLabel().copy()
              .withStyle(ChatFormatting.WHITE),
          Component.translatable(this.getTranslationKey())
              .withStyle(ChatFormatting.DARK_GREEN),
          Component.translatable(this.getDescriptionKey())));
    }

    public String getTranslationKey() {
      return Translations.makeKey("tips", "manipulator.transfer_mode." + this.name);
    }

    public String getDescriptionKey() {
      return this.getTranslationKey() + ".desc";
    }

    @Override
    public TexturePosition getTexturePosition() {
      return ButtonTexture.SMALL_BUTTON;
    }

    @Override
    public TransferMode getNext() {
      return EnumUtil.next(this, values());
    }
  }

  public enum RedstoneMode implements ButtonState<RedstoneMode> {

    COMPLETE("complete", '✓'),
    IMMEDIATE("immediate", '❢'),
    MANUAL("manual", '✘'),
    PARTIAL("partial", '➧');

    private final String name;
    private final Component label;

    private RedstoneMode(String name, char icon) {
      this.name = name;
      this.label = Component.literal(String.valueOf(icon));
    }

    @Override
    public Component getLabel() {
      return this.label;
    }

    @Override
    public Optional<Component> getTooltip() {
      return Optional.of(CommonComponents.joinLines(
          this.getLabel().copy()
              .withStyle(ChatFormatting.WHITE),
          Component.translatable(this.getTranslationKey())
              .withStyle(ChatFormatting.DARK_GREEN),
          Component.translatable(this.getDescriptionKey())));
    }

    public String getTranslationKey() {
      return Translations.makeKey("tips", "manipulator.redstone_mode." + this.name);
    }

    public String getDescriptionKey() {
      return this.getTranslationKey() + ".desc";
    }

    @Override
    public TexturePosition getTexturePosition() {
      return ButtonTexture.SMALL_BUTTON;
    }

    @Override
    public RedstoneMode getNext() {
      return EnumUtil.next(this, values());
    }
  }
}
