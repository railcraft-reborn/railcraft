package mods.railcraft.world.level.block.entity;

import java.util.Deque;
import java.util.LinkedList;
import java.util.Optional;
import org.jetbrains.annotations.Nullable;
import mods.railcraft.util.container.AdvancedContainer;
import mods.railcraft.util.container.ForwardingContainer;
import mods.railcraft.util.routing.IBlockEntityRouting;
import mods.railcraft.util.routing.IRouter;
import mods.railcraft.util.routing.RoutingLogic;
import mods.railcraft.world.inventory.SwitchTrackRoutingMenu;
import mods.railcraft.world.level.block.track.actuator.SwitchTrackActuatorBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

public class SwitchTrackRoutingBlockEntity extends LockableSwitchTrackActuatorBlockEntity
    implements ForwardingContainer, MenuProvider, IBlockEntityRouting, IRouter {

  private AdvancedContainer container;

  @Nullable
  private RoutingLogic logic;

  private boolean redstoneTriggered;
  private boolean powered;

  public SwitchTrackRoutingBlockEntity(BlockPos blockPos, BlockState blockState) {
    super(RailcraftBlockEntityTypes.SWITCH_TRACK_ROUTING.get(), blockPos, blockState);
    this.container = new AdvancedContainer(1).listener((Container) this);
  }

  public void neighborChanged() {
    boolean lastPowered = this.powered;
    this.powered = this.level.hasNeighborSignal(this.getBlockPos());
    if (this.redstoneTriggered && lastPowered != this.powered) {
      this.updateSwitched();
    }
  }

  private void updateSwitched() {
    boolean switched = this.powered;
    SwitchTrackActuatorBlock.setSwitched(
        this.getBlockState(), this.level, this.getBlockPos(), switched);
  }

  @Override
  public void setCustomName(@Nullable Component name) {
    super.setCustomName(name);
  }


  @Override
  protected void saveAdditional(CompoundTag tag) {
    super.saveAdditional(tag);
    tag.put("container", this.container.createTag());
    tag.putBoolean("redstoneTriggered", this.redstoneTriggered);
  }

  @Override
  public void load(CompoundTag tag) {
    super.load(tag);
    this.container.fromTag(tag.getList("container", Tag.TAG_COMPOUND));
    this.redstoneTriggered = tag.getBoolean("redstoneTriggered");
  }

  @Override
  public void writeToBuf(FriendlyByteBuf data) {
    super.writeToBuf(data);
    data.writeBoolean(this.redstoneTriggered);
  }

  @Override
  public void readFromBuf(FriendlyByteBuf data) {
    super.readFromBuf(data);
    this.redstoneTriggered = data.readBoolean();
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
      if (item.getTag() != null && item.getTag().contains("pages", 8)) {
        var content = loadPages(item.getTag());
        logic = RoutingLogic.buildLogic(content);
      }
    }
  }

  private static Deque<String> loadPages(CompoundTag tag) {
    Deque<String> contents = new LinkedList<>();
    var listtag = tag.getList("pages", 8).copy();
    for(int i = 0; i < listtag.size(); i++) {
      contents.add(listtag.getString(i));
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
    return new SwitchTrackRoutingMenu(id, inventory, this);
  }
}
