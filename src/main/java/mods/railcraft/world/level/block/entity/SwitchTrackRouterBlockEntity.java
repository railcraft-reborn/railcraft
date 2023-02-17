package mods.railcraft.world.level.block.entity;

import java.util.Arrays;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.jetbrains.annotations.Nullable;
import com.mojang.authlib.GameProfile;
import mods.railcraft.Translations;
import mods.railcraft.api.util.EnumUtil;
import mods.railcraft.client.gui.widget.button.ButtonTexture;
import mods.railcraft.client.gui.widget.button.TexturePosition;
import mods.railcraft.gui.button.ButtonState;
import mods.railcraft.util.container.AdvancedContainer;
import mods.railcraft.util.container.ForwardingContainer;
import mods.railcraft.util.routing.IBlockEntityRouting;
import mods.railcraft.util.routing.IRouter;
import mods.railcraft.util.routing.RoutingLogic;
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
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

public class SwitchTrackRouterBlockEntity extends LockableSwitchTrackActuatorBlockEntity
    implements ForwardingContainer, MenuProvider, IBlockEntityRouting, IRouter {

  private AdvancedContainer container;

  @Nullable
  private RoutingLogic logic;

  private Railway railway = Railway.PUBLIC;

  private boolean powered;

  public SwitchTrackRouterBlockEntity(BlockPos blockPos, BlockState blockState) {
    super(RailcraftBlockEntityTypes.SWITCH_TRACK_ROUTER.get(), blockPos, blockState);
    this.container = new AdvancedContainer(1).listener((Container) this);
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

  private void updateSwitched() {
    SwitchTrackActuatorBlock.setSwitched(
        this.getBlockState(), this.level, this.getBlockPos(), this.powered);
  }

  @Override
  public void setCustomName(@Nullable Component name) {
    super.setCustomName(name);
  }

  @Override
  protected void saveAdditional(CompoundTag tag) {
    super.saveAdditional(tag);
    tag.put("container", this.container.createTag());
    tag.putString("railway", this.railway.getSerializedName());
  }

  @Override
  public void load(CompoundTag tag) {
    super.load(tag);
    this.container.fromTag(tag.getList("container", Tag.TAG_COMPOUND));
    this.railway = Railway.getByName(tag.getString("railway")).orElse(Railway.PUBLIC);
  }

  @Override
  public void writeToBuf(FriendlyByteBuf data) {
    super.writeToBuf(data);
    data.writeEnum(this.railway);
  }

  @Override
  public void readFromBuf(FriendlyByteBuf data) {
    super.readFromBuf(data);
    this.railway = data.readEnum(Railway.class);
  }

  @Override
  public ItemStack getRoutingTable() {
    return this.container.getItem(0);
  }

  @Override
  public void setRoutingTable(ItemStack stack) {
    this.container.setItem(0, stack);
  }

  @Override
  public boolean isPowered() {
    return this.powered;
  }

  @Override
  public Optional<RoutingLogic> getLogic() {
    refreshLogic();
    return Optional.ofNullable(logic);
  }

  @Override
  public void resetLogic() {
    logic = null;
  }

  private void refreshLogic() {
    if (logic == null && !container.getItem(0).isEmpty()) {
      var item = container.getItem(0);
      if (item.getTag() != null && item.getTag().contains("pages")) {
        var content = loadPages(item.getTag());
        logic = RoutingLogic.buildLogic(content);
      }
    }
  }

  private static Deque<String> loadPages(CompoundTag tag) {
    Deque<String> contents = new LinkedList<>();
    var pages = tag.getList("pages", Tag.TAG_STRING).copy();
    for(int i = 0; i < pages.size(); i++) {
      var page = pages.getString(i).split("\n");
      contents.addAll(Arrays.asList(page));
    }
    return contents;
  }

  @Override
  public Container getContainer() {
    return this.container;
  }

  @Nullable
  @Override
  public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
    return new SwitchTrackRouterMenu(id, inventory, this);
  }

  public enum Railway implements ButtonState<Railway>, StringRepresentable {

    PUBLIC("public"),
    PRIVATE("private");

    private static final Map<String, Railway> BY_NAME = Arrays.stream(values())
        .collect(Collectors.toUnmodifiableMap(Railway::getSerializedName, Function.identity()));

    private final String name;

    private Railway(String name) {
      this.name = name;
    }

    @Override
    public Component getLabel() {
      return Component.translatable(Translations.makeKey("screen",
          String.format("switch_track_routing.%s_railway", this.name)));
    }

    @Override
    public TexturePosition getTexturePosition() {
      return ButtonTexture.SMALL_BUTTON;
    }

    @Override
    public Railway getNext() {
      return EnumUtil.next(this, values());
    }

    @Override
    public String getSerializedName() {
      return this.name;
    }

    public static Optional<Railway> getByName(String name) {
      return Optional.ofNullable(BY_NAME.get(name));
    }
  }
}
