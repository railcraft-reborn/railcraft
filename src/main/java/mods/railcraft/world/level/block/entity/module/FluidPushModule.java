package mods.railcraft.world.level.block.entity.module;

import java.util.function.Predicate;
import java.util.function.Supplier;
import mods.railcraft.world.level.material.fluid.FluidTools;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandler;

/**
 * Created by CovertJaguar on 1/28/2019 for Railcraft.
 *
 * @author CovertJaguar <http://www.railcraft.info>
 */
public class FluidPushModule extends BaseModule {

  private final Supplier<LazyOptional<? extends IFluidHandler>> fluidHandler;
  private final int outputRate;
  private final Direction[] directions;
  private final Predicate<BlockEntity> filter;

  public FluidPushModule(ModuleProvider provider,
      Supplier<LazyOptional<? extends IFluidHandler>> fluidHandler, int outputRate,
      Predicate<BlockEntity> filter, Direction... directions) {
    super(provider);
    this.fluidHandler = fluidHandler;
    this.outputRate = outputRate;
    this.directions = directions;
    this.filter = filter;
  }

  @Override
  public void serverTick() {
    this.fluidHandler.get().ifPresent(fluidHandler -> {
      var neighbors = FluidTools.findNeighbors(this.provider.getLevel(),
          this.provider.getBlockPos(), this.filter, this.directions);
      for (var neighbor : neighbors) {
        FluidUtil.tryFluidTransfer(neighbor, fluidHandler, this.outputRate, true);
      }
    });
  }

  @Override
  public void writeToBuf(FriendlyByteBuf out) {}

  @Override
  public void readFromBuf(FriendlyByteBuf in) {}

  @Override
  public CompoundTag serializeNBT() {
    return new CompoundTag();
  }

  @Override
  public void deserializeNBT(CompoundTag nbt) {}
}
