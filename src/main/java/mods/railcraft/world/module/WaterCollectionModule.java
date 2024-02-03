package mods.railcraft.world.module;

import java.util.Collection;
import mods.railcraft.RailcraftConfig;
import mods.railcraft.api.core.CompoundTagKeys;
import mods.railcraft.util.fluids.FluidTools;
import mods.railcraft.util.fluids.FluidTools.ProcessType;
import mods.railcraft.world.level.block.entity.WaterTankSidingBlockEntity;
import mods.railcraft.world.level.material.FluidItemHelper;
import mods.railcraft.world.level.material.StandardTank;
import net.minecraft.SharedConstants;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidUtil;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

public class WaterCollectionModule extends ContainerModule<BlockModuleProvider> {

  public static final int SLOT_INPUT = 0;
  public static final int SLOT_PROCESS = 1;
  public static final int SLOT_OUTPUT = 2;

  private static final int REFILL_INTERVAL = SharedConstants.TICKS_PER_SECOND;
  private static final float REFILL_PENALTY_FROZEN = 0.5F;
  private static final float REFILL_BOOST_RAIN = 3.0F;

  private final WaterTankSidingBlockEntity blockEntity;
  private final StandardTank tank;

  private State state = State.INACTIVE;
  private int refillTicks;

  private FluidTools.ProcessState processState = FluidTools.ProcessState.RESET;
  private int processTicks;

  public WaterCollectionModule(WaterTankSidingBlockEntity provider) {
    super(provider, 3);
    this.blockEntity = provider;
    this.tank = StandardTank.ofBuckets(RailcraftConfig.SERVER.tankCapacityPerBlock.get() * 26);
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
      int rate = this.state.calculateRate(this.calculateMultiplier());
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
    return switch (slot) {
      case SLOT_INPUT -> (!this.tank.isEmpty()
          && FluidItemHelper.isRoomInContainer(stack, this.tank.getFluid().getFluid()))
          || FluidUtil.getFluidContained(stack).isPresent();
      case SLOT_PROCESS, SLOT_OUTPUT -> true;
      default -> false;
    } && super.canPlaceItem(slot, stack);
  }

  private int calculateMultiplier() {
    if (this.blockEntity.getMembers().isEmpty()) {
      return 1;
    }
    return (int) this.blockEntity.getMembers().stream()
        .flatMap(Collection::stream)
        .flatMap(member -> member.getModule(WaterCollectionModule.class).stream())
        .map(WaterCollectionModule::getState)
        .filter(State::skyVisible)
        .count();
  }

  public StandardTank getTank() {
    return this.tank;
  }

  @Override
  public CompoundTag serializeNBT() {
    var tag = super.serializeNBT();
    tag.put(CompoundTagKeys.TANK, this.tank.writeToNBT(new CompoundTag()));
    tag.putString(CompoundTagKeys.PROCESS_STATE, this.processState.getSerializedName());
    return tag;
  }

  @Override
  public void deserializeNBT(CompoundTag tag) {
    super.deserializeNBT(tag);
    this.tank.readFromNBT(tag.getCompound(CompoundTagKeys.TANK));
    this.processState = FluidTools.ProcessState.fromTag(tag);
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

    public static final State INACTIVE = new State(false, 0.0D, 1.0D, 1.0D);

    public int calculateRate(int size) {
      return Mth.floor(
          (RailcraftConfig.SERVER.waterCollectionRate.get()
              * size
              * this.humidityMultiplier
              * this.precipitationMultiplier)
              - this.temperaturePenalty);
    }

    public static State create(Level level, BlockPos pos) {
      if (!level.canSeeSky(pos)) {
        return INACTIVE;
      }

      var biome = level.getBiome(pos).value();
      var humidityMultiplier = biome.getModifiedClimateSettings().downfall();

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
    }
  }
}
