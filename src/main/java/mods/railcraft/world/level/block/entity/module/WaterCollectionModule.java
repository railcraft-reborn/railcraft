package mods.railcraft.world.level.block.entity.module;

import mods.railcraft.RailcraftConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fluids.capability.IFluidHandler;

/**
 * Created by CovertJaguar on 1/28/2019 for Railcraft.
 *
 * @author CovertJaguar <http://www.railcraft.info>
 */
public class WaterCollectionModule extends BaseModule {

  private static final int REFILL_INTERVAL = 20;
  private static final float REFILL_PENALTY_FROZEN = 0.5F;
  private static final float REFILL_BOOST_RAIN = 3.0F;

  private final IFluidTank tank;

  private State state;
  private int refillTicks;

  public WaterCollectionModule(ModuleProvider provider, IFluidTank tank) {
    super(provider);
    this.tank = tank;
  }

  public State getState() {
    return this.state;
  }

  @Override
  public void serverTick() {
    var level = this.provider.getLevel();
    if (this.refillTicks++ >= REFILL_INTERVAL) {
      this.refillTicks = 0;
      var above = this.provider.getBlockPos().above();
      this.state = State.create(level, above);
      var rate = this.state.calculateRate();
      if (rate > 0) {
        this.tank.fill(new FluidStack(Fluids.WATER, rate),
            IFluidHandler.FluidAction.EXECUTE);
      } else {
        this.tank.drain(new FluidStack(Fluids.WATER, rate),
            IFluidHandler.FluidAction.EXECUTE);
      }
    }
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

  public record State(boolean skyVisible, double temperaturePenalty,
      double humidityMultiplier, double precipitationMultiplier) {

    public int calculateRate() {
      return Mth.abs(Mth.floor(
          (RailcraftConfig.server.waterCollectionRate.get()
              * this.humidityMultiplier
              * this.precipitationMultiplier)
              - this.temperaturePenalty));
    }

    public static State create(Level level, BlockPos pos) {
      if (level.canSeeSky(pos)) {
        var biome = level.getBiome(pos).value();
        var humidityMultiplier = biome.getDownfall();

        var precipitationMultiplier = 1.0D;
        if (biome.coldEnoughToSnow(pos)) {
          precipitationMultiplier = REFILL_PENALTY_FROZEN;
        } else if (level.isRainingAt(pos)) {
          precipitationMultiplier = REFILL_BOOST_RAIN;
        }

        var temperaturePenalty = 0.0D;
        @SuppressWarnings("deprecation")
        var temperature = biome.getTemperature(pos);
        if (temperature > 1.0D) {
          temperaturePenalty = temperature - 1.0D;
        }

        return new State(true, temperaturePenalty, humidityMultiplier,
            precipitationMultiplier);
      } else {
        return new State(false, 0.0D, 1.0D, 1.0D);
      }
    }
  }
}
