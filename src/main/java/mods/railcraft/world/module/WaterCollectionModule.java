package mods.railcraft.world.module;

import java.util.Collection;
import java.util.Objects;
import mods.railcraft.RailcraftConfig;
import mods.railcraft.world.level.block.entity.WaterTankSidingBlockEntity;
import mods.railcraft.world.level.material.fluid.FluidItemHelper;
import mods.railcraft.world.level.material.fluid.FluidTools;
import mods.railcraft.world.level.material.fluid.FluidTools.ProcessType;
import mods.railcraft.world.level.material.fluid.tank.StandardTank;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandler;

public class WaterCollectionModule extends ContainerModule<BlockModuleProvider> {

  public static final int SLOT_INPUT = 0;
  public static final int SLOT_PROCESS = 1;
  public static final int SLOT_OUTPUT = 2;
  private static final int REFILL_INTERVAL = 20;
  private static final float REFILL_PENALTY_FROZEN = 0.5F;
  private static final float REFILL_BOOST_RAIN = 3.0F;

  private final WaterTankSidingBlockEntity blockEntity;
  private final StandardTank tank;
  private final LazyOptional<IFluidHandler> fluidHandler;

  private State state;
  private int refillTicks;

  private FluidTools.ProcessState processState = FluidTools.ProcessState.RESET;
  private int processTicks;

  public WaterCollectionModule(WaterTankSidingBlockEntity provider) {
    super(provider, 3);
    blockEntity = provider;
    var capacity = RailcraftConfig.server.tankCapacityPerBlock.get() * FluidType.BUCKET_VOLUME;
    this.tank = new StandardTank(capacity * 26);
    this.fluidHandler = LazyOptional.of(() -> this.tank);
  }

  public State getState() {
    return this.state;
  }

  @Override
  public void serverTick() {
    var level = this.provider.level();
    if (this.refillTicks++ >= REFILL_INTERVAL) {
      this.refillTicks = 0;
      var above = this.provider.blockPos().above();
      this.state = State.create(level, above);
      int rate = this.state.calculateRate(calculateMultiplier());
      if (rate > 0) {
        this.tank.fill(new FluidStack(Fluids.WATER, rate),
            IFluidHandler.FluidAction.EXECUTE);
      } else {
        this.tank.drain(new FluidStack(Fluids.WATER, Math.abs(rate)),
            IFluidHandler.FluidAction.EXECUTE);
      }
    }
    if (this.processTicks++ >= FluidTools.BUCKET_FILL_TIME) {
      this.processTicks = 0;
      this.processState = FluidTools.processContainer(this, this.tank,
          ProcessType.DRAIN_THEN_FILL, this.processState);
    }
  }

  @Override
  public boolean canPlaceItem(int slot, ItemStack stack) {
    if (!super.canPlaceItem(slot, stack)) {
      return false;
    }
    if (slot == SLOT_INPUT) {
      return (!this.tank.isEmpty()
          && FluidItemHelper.isRoomInContainer(stack, this.tank.getFluidType()))
          || FluidUtil.getFluidContained(stack).isPresent();
    }
    return false;
  }

  private int calculateMultiplier() {
    if (this.blockEntity.getMembers().isPresent()) {
     return (int) this.blockEntity.getMembers().stream()
          .flatMap(Collection::stream)
          .flatMap(member -> member.getModule(WaterCollectionModule.class).stream())
          .map(WaterCollectionModule::getState)
          .filter(Objects::nonNull)
          .filter(State::skyVisible)
          .count();
    }
    return 1;
  }

  public StandardTank getTank() {
    return this.tank;
  }

  public LazyOptional<IFluidHandler> getFluidHandler() {
    return this.fluidHandler;
  }

  @Override
  public CompoundTag serializeNBT() {
    var tag = super.serializeNBT();
    tag.put("tank", this.tank.writeToNBT(new CompoundTag()));
    tag.putString("processState", this.processState.getSerializedName());
    return tag;
  }

  @Override
  public void deserializeNBT(CompoundTag tag) {
    super.deserializeNBT(tag);
    this.tank.readFromNBT(tag.getCompound("tank"));
    this.processState = FluidTools.ProcessState.getByName(tag.getString("processState"))
        .orElse(FluidTools.ProcessState.RESET);
  }

  @Override
  public void writeToBuf(FriendlyByteBuf out) {
    super.writeToBuf(out);
    out.writeVarInt(this.tank.getCapacity());
    out.writeFluidStack(this.tank.getFluid());
  }

  @Override
  public void readFromBuf(FriendlyByteBuf in) {
    super.readFromBuf(in);
    this.tank.setCapacity(in.readVarInt());
    this.tank.setFluid(in.readFluidStack());
  }

  public record State(boolean skyVisible, double temperaturePenalty,
                      double humidityMultiplier, double precipitationMultiplier) {

    public int calculateRate(int size) {
      return Mth.floor(
          (RailcraftConfig.server.waterCollectionRate.get()
              * size
              * this.humidityMultiplier
              * this.precipitationMultiplier)
              - this.temperaturePenalty);
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
