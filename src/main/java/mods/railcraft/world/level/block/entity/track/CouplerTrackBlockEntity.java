package mods.railcraft.world.level.block.entity.track;

import mods.railcraft.Translations.Tips;
import mods.railcraft.world.entity.vehicle.LinkageManagerImpl;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntityTypes;
import mods.railcraft.world.level.block.track.outfitted.CouplerTrackBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

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

    COUPLER(8) {
      @Override
      protected void minecartPassed(CouplerTrackBlockEntity track, AbstractMinecart cart) {
        if (track.pendingCoupling != null) {
          LinkageManagerImpl.INSTANCE.createLink(track.pendingCoupling, cart);
        }
        track.pendingCoupling = cart;
      }
    },
    DECOUPLER(0) {
      @Override
      protected void minecartPassed(CouplerTrackBlockEntity track, AbstractMinecart cart) {
        LinkageManagerImpl.INSTANCE.breakLinks(cart);
      }
    },
    AUTO_COUPLER(0) {
      @Override
      protected void minecartPassed(CouplerTrackBlockEntity track, AbstractMinecart cart) {
        LinkageManagerImpl.INSTANCE.setAutoLink(cart,
            CouplerTrackBlock.isPowered(track.getBlockState()));
      }
    };

    private final int powerPropagation;

    Mode(int powerPropagation) {
      this.powerPropagation = powerPropagation;
    }

    public Mode next() {
      return values()[(this.ordinal() + 1) % values().length];
    }

    public Mode previous() {
      return values()[(this.ordinal() + values().length - 1) % values().length];
    }

    public Component getDisplayName() {
      return Component.translatable(getTip());
    }

    @Override
    public String getSerializedName() {
      var name = getTip().split("\\.");
      return name[name.length - 1];
    }

    private String getTip() {
      return switch (this.ordinal()) {
        case 0 -> Tips.COUPLER_TRACK_COUPLER;
        case 1 -> Tips.COUPLER_TRACK_DECOUPLER;
        case 2 -> Tips.COUPLER_TRACK_AUTO_COUPLER;
        default -> "translation.not.implemented";
      };
    }

    public int getPowerPropagation() {
      return this.powerPropagation;
    }

    protected abstract void minecartPassed(CouplerTrackBlockEntity track, AbstractMinecart cart);
  }
}
