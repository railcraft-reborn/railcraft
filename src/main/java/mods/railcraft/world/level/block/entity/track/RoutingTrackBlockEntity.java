package mods.railcraft.world.level.block.entity.track;

import org.jetbrains.annotations.Nullable;
import mods.railcraft.api.carts.Routable;
import mods.railcraft.util.container.AdvancedContainer;
import mods.railcraft.util.container.ForwardingContainer;
import mods.railcraft.world.inventory.RoutingTrackMenu;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntity;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntityTypes;
import mods.railcraft.world.level.block.track.outfitted.PoweredOutfittedTrackBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.state.BlockState;

public class RoutingTrackBlockEntity extends LockableTrackBlockEntity implements ForwardingContainer,
    MenuProvider {

  private final AdvancedContainer container;

  public RoutingTrackBlockEntity(BlockPos blockPos, BlockState blockState) {
    super(RailcraftBlockEntityTypes.ROUTING_TRACK.get(), blockPos, blockState);
    this.container = new AdvancedContainer(1).listener((Container) this);
  }

  public void minecartPassed(AbstractMinecart cart) {
    if (PoweredOutfittedTrackBlock.isPowered(this.getBlockState())) {
      var stack = container.getItem(0);
      if (!stack.isEmpty() && cart instanceof Routable routable) {
        routable.setDestination(stack);
      }
    }
  }

  @Override
  protected void saveAdditional(CompoundTag tag) {
    super.saveAdditional(tag);
    tag.put("container", this.container.createTag());
  }

  @Override
  public void load(CompoundTag tag) {
    super.load(tag);
    this.container.fromTag(tag.getList("container", Tag.TAG_COMPOUND));
  }

  @Override
  public Container container() {
    return this.container;
  }

  @Override
  public boolean stillValid(Player player) {
    return RailcraftBlockEntity.stillValid(this, player);
  }

  @Override
  public Component getName() {
    return this.getDisplayName();
  }

  @Override
  public Component getDisplayName() {
    return this.getBlockState().getBlock().getName();
  }

  @Nullable
  @Override
  public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
    return new RoutingTrackMenu(id, inventory, this);
  }
}
