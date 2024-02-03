package mods.railcraft.world.level.block.entity;

import java.util.Arrays;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Optional;
import org.jetbrains.annotations.Nullable;
import com.mojang.authlib.GameProfile;
import com.mojang.datafixers.util.Either;
import mods.railcraft.Translations;
import mods.railcraft.api.carts.RollingStock;
import mods.railcraft.api.core.CompoundTagKeys;
import mods.railcraft.api.track.SwitchActuator;
import mods.railcraft.api.util.EnumUtil;
import mods.railcraft.client.gui.widget.button.ButtonTexture;
import mods.railcraft.client.gui.widget.button.TexturePosition;
import mods.railcraft.gui.button.ButtonState;
import mods.railcraft.util.container.AdvancedContainer;
import mods.railcraft.util.container.ForwardingContainer;
import mods.railcraft.util.routing.RouterBlockEntity;
import mods.railcraft.util.routing.RoutingLogic;
import mods.railcraft.util.routing.RoutingLogicException;
import mods.railcraft.world.inventory.SwitchTrackRouterMenu;
import mods.railcraft.world.level.block.track.actuator.SwitchTrackActuatorBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.Container;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.state.BlockState;

public class SwitchTrackRouterBlockEntity extends LockableSwitchTrackActuatorBlockEntity
    implements ForwardingContainer, MenuProvider, RouterBlockEntity, SwitchActuator {

  private final AdvancedContainer container;
  @Nullable
  private Either<RoutingLogic, RoutingLogicException> logic;
  private Railway railway = Railway.PUBLIC;
  private boolean powered;

  public SwitchTrackRouterBlockEntity(BlockPos blockPos, BlockState blockState) {
    super(RailcraftBlockEntityTypes.SWITCH_TRACK_ROUTER.get(), blockPos, blockState);
    this.container = new AdvancedContainer(1).listener((Container) this);
  }

  public void neighborChanged() {
    this.powered = this.level.hasNeighborSignal(this.getBlockPos());
    this.setChanged();
  }

  public Railway getRailway() {
    return this.railway;
  }

  public void setRailway(@Nullable GameProfile gameProfile) {
    this.railway = gameProfile == null ? Railway.PUBLIC : Railway.PRIVATE;
    if (!this.isLocked()) {
      this.setOwner(gameProfile);
    }
  }

  @Override
  public void setCustomName(@Nullable Component name) {
    super.setCustomName(name);
  }

  @Override
  protected void saveAdditional(CompoundTag tag) {
    super.saveAdditional(tag);
    tag.put(CompoundTagKeys.CONTAINER, this.container.createTag());
    tag.putString(CompoundTagKeys.RAILWAY, this.railway.getSerializedName());
    tag.putBoolean(CompoundTagKeys.POWERED, this.powered);
  }

  @Override
  public void load(CompoundTag tag) {
    super.load(tag);
    this.container.fromTag(tag.getList(CompoundTagKeys.CONTAINER, Tag.TAG_COMPOUND));
    this.railway = Railway.fromName(tag.getString(CompoundTagKeys.RAILWAY));
    this.powered = tag.getBoolean(CompoundTagKeys.POWERED);
  }

  @Override
  public void writeToBuf(FriendlyByteBuf data) {
    super.writeToBuf(data);
    data.writeEnum(this.railway);
    data.writeBoolean(this.powered);
  }

  @Override
  public void readFromBuf(FriendlyByteBuf data) {
    super.readFromBuf(data);
    this.railway = data.readEnum(Railway.class);
    this.powered = data.readBoolean();
  }

  public boolean isPowered() {
    return this.powered;
  }

  public Optional<Either<RoutingLogic, RoutingLogicException>> logicResult() {
    this.refreshLogic();
    return Optional.ofNullable(this.logic);
  }

  public Optional<RoutingLogic> logic() {
    return this.logicResult().flatMap(x -> x.left());
  }

  public Optional<RoutingLogicException> logicError() {
    return this.logicResult().flatMap(x -> x.right());
  }

  public void resetLogic() {
    this.logic = null;
  }

  private void refreshLogic() {
    if (this.logic == null && !this.container.getItem(0).isEmpty()) {
      var item = this.container.getItem(0);
      if (item.getTag() != null && item.getTag().contains("pages")) {
        var content = loadPages(item.getTag());
        try {
          this.logic = Either.left(RoutingLogic.parseTable(content));
        } catch (RoutingLogicException e) {
          this.logic = Either.right(e);
        }
      }
    }
  }

  @Override
  public boolean shouldSwitch(RollingStock rollingStock) {
    boolean shouldSwitch = this.logic()
        .map(logic -> logic.matches(this, rollingStock))
        .orElse(false);
    if (this.railway == Railway.PRIVATE) {
      shouldSwitch = rollingStock.owner()
          .filter(owner -> owner.equals(this.getOwnerOrThrow()))
          .isPresent();
    }
    SwitchTrackActuatorBlock.setSwitched(
        this.getBlockState(), this.level, this.getBlockPos(), shouldSwitch);
    return shouldSwitch;
  }

  private static Deque<String> loadPages(CompoundTag tag) {
    Deque<String> contents = new LinkedList<>();
    var pages = tag.getList("pages", Tag.TAG_STRING).copy();
    for (int i = 0; i < pages.size(); i++) {
      var page = pages.getString(i).split("\n");
      contents.addAll(Arrays.asList(page));
    }
    return contents;
  }

  @Override
  public Container container() {
    return this.container;
  }

  @Override
  public boolean stillValid(Player player) {
    return isStillValid(player);
  }

  @Nullable
  @Override
  public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
    return new SwitchTrackRouterMenu(id, inventory, this);
  }

  public enum Railway implements ButtonState<Railway>, StringRepresentable {

    PUBLIC("public"),
    PRIVATE("private");

    private static final StringRepresentable.EnumCodec<Railway> CODEC =
        StringRepresentable.fromEnum(Railway::values);

    private final String name;

    Railway(String name) {
      this.name = name;
    }

    @Override
    public Component label() {
      return Component.translatable(Translations.makeKey("screen",
          String.format("switch_track_router.%s_railway", this.name)));
    }

    @Override
    public TexturePosition texturePosition() {
      return ButtonTexture.SMALL_BUTTON;
    }

    @Override
    public Railway next() {
      return EnumUtil.next(this, values());
    }

    @Override
    public String getSerializedName() {
      return this.name;
    }

    public static Railway fromName(String name) {
      return CODEC.byName(name, PUBLIC);
    }
  }
}
