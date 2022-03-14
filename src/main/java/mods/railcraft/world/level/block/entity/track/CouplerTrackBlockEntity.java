package mods.railcraft.world.level.block.entity.track;

import javax.annotation.Nullable;
import mods.railcraft.world.entity.vehicle.LinkageManagerImpl;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntityTypes;
import mods.railcraft.world.level.block.track.outfitted.CouplerTrackBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class CouplerTrackBlockEntity extends BlockEntity {

  @Nullable
  private AbstractMinecart pendingCoupling;

  public CouplerTrackBlockEntity(BlockPos blockPos, BlockState blockState) {
    super(RailcraftBlockEntityTypes.COUPLER_TRACK.get(), blockPos, blockState);
  }

  // Called by block
  public void minecartPassed(AbstractMinecart cart) {
    CouplerTrackBlock.getMode(this.getBlockState()).minecartPassed(this, cart);
  }

  public enum Mode implements StringRepresentable {

    COUPLER("coupler", 8) {
      @Override
      protected void minecartPassed(CouplerTrackBlockEntity track, AbstractMinecart cart) {
        if (track.pendingCoupling != null) {
          LinkageManagerImpl.INSTANCE.createLink(track.pendingCoupling, cart);
        }
        track.pendingCoupling = cart;
      }
    },
    DECOUPLER("decoupler", 0) {
      @Override
      protected void minecartPassed(CouplerTrackBlockEntity track, AbstractMinecart cart) {
        LinkageManagerImpl.INSTANCE.breakLinks(cart);
      }
    },
    AUTO_COUPLER("auto_coupler", 0) {
      @Override
      protected void minecartPassed(CouplerTrackBlockEntity track, AbstractMinecart cart) {
        LinkageManagerImpl.INSTANCE.setAutoLink(cart,
            CouplerTrackBlock.isPowered(track.getBlockState()));
      }
    };

    private final String name;
    private final Component displayName;
    private final int powerPropagation;

    private Mode(String name, int powerPropagation) {
      this.name = name;
      this.displayName = new TranslatableComponent("coupler_track.mode." + name);
      this.powerPropagation = powerPropagation;
    }

    public Mode next() {
      return values()[(this.ordinal() + 1) % values().length];
    }

    public Mode previous() {
      return values()[(this.ordinal() + values().length - 1) % values().length];
    }

    @Override
    public String getSerializedName() {
      return this.name;
    }

    public Component getDisplayName() {
      return this.displayName;
    }

    public int getPowerPropagation() {
      return this.powerPropagation;
    }

    protected abstract void minecartPassed(CouplerTrackBlockEntity track, AbstractMinecart cart);
  }
}
