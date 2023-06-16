package mods.railcraft.world.level.block.entity;

import java.util.List;
import java.util.function.Predicate;
import org.jetbrains.annotations.Nullable;
import it.unimi.dsi.fastutil.chars.CharList;
import mods.railcraft.Translations;
import mods.railcraft.api.charge.Charge;
import mods.railcraft.api.charge.ChargeStorage;
import mods.railcraft.util.EnergyUtil;
import mods.railcraft.world.inventory.SteamTurbineMenu;
import mods.railcraft.world.level.block.RailcraftBlocks;
import mods.railcraft.world.level.block.SteamTurbineBlock;
import mods.railcraft.world.level.block.entity.multiblock.BlockPredicate;
import mods.railcraft.world.level.block.entity.multiblock.MultiblockBlockEntity;
import mods.railcraft.world.level.block.entity.multiblock.MultiblockPattern;
import mods.railcraft.world.level.material.fluid.FluidTools;
import mods.railcraft.world.module.SteamTurbineModule;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidUtil;

public class SteamTurbineBlockEntity extends MultiblockBlockEntity<SteamTurbineBlockEntity, Void> {

  private static final int WATER_OUTPUT_RATE = 4;
  private static final int ENERGY_OUTPUT_RATE = 900;

  private static final BlockPredicate BLOCK_PREDICATE =
      BlockPredicate.of(RailcraftBlocks.STEAM_TURBINE);

  private static final MultiblockPattern<Void> pattern =
      MultiblockPattern.<Void>builder(BlockPos.ZERO)
          .layer(List.of(
              CharList.of('A', 'W', 'B'),
              CharList.of('B', 'W', 'A')))
          .layer(List.of(
              CharList.of('C', 'X', 'D'),
              CharList.of('D', 'X', 'C')))
          .predicate('A', BLOCK_PREDICATE)
          .predicate('B', BLOCK_PREDICATE)
          .predicate('C', BLOCK_PREDICATE)
          .predicate('D', BLOCK_PREDICATE)
          .predicate('X', BLOCK_PREDICATE)
          .predicate('W', BLOCK_PREDICATE)
          .build();

  private static final MultiblockPattern<Void> rotatedPattern = pattern.rotateClockwise();

  private static final List<MultiblockPattern<Void>> patterns = List.of(pattern, rotatedPattern);

  private final SteamTurbineModule module;

  // Used by renderer
  private float lastGaugeValue;
  private float masterOperatingRatio;

  private int syncTicks;

  public SteamTurbineBlockEntity(BlockPos blockPos, BlockState blockState) {
    super(RailcraftBlockEntityTypes.STEAM_TURBINE.get(), blockPos, blockState,
        SteamTurbineBlockEntity.class, patterns);
    this.module = this.moduleDispatcher.registerModule("steam_turbine",
        new SteamTurbineModule(this, Charge.distribution));
  }

  public SteamTurbineModule getSteamTurbineModule() {
    return this.module;
  }

  public static void serverTick(Level level, BlockPos blockPos, BlockState blockState,
      SteamTurbineBlockEntity blockEntity) {
    blockEntity.serverTick();
    blockEntity.moduleDispatcher.serverTick();

    blockEntity.masterOperatingRatio = 0;
    blockEntity.getMasterBlockEntity()
        .map(SteamTurbineBlockEntity::getSteamTurbineModule)
        .ifPresent(master -> {
          blockEntity.masterOperatingRatio = master.getOperatingRatio();

          Predicate<BlockEntity> filter = other -> !(other instanceof SteamTurbineBlockEntity tank)
              || !tank.getMembership().equals(blockEntity.getMembership());

          master.getEnergyStorage()
              .ifPresent(energyStorage -> EnergyUtil.pushToSides(level, blockPos, energyStorage,
                  ENERGY_OUTPUT_RATE, filter, Direction.values()));

          master.getFluidHandler().ifPresent(fluidHandler -> {
            var neighbors = FluidTools.findNeighbors(level, blockPos, filter,
                Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST);
            for (var neighbor : neighbors) {
              FluidUtil.tryFluidTransfer(neighbor, fluidHandler, WATER_OUTPUT_RATE, true);
            }
          });
        });

    if (blockEntity.syncTicks++ >= 4) {
      blockEntity.syncTicks = 0;
      blockEntity.syncToClient();
    }
  }

  public float getAndSmoothGaugeValue() {
    return this.lastGaugeValue = (this.lastGaugeValue * 14.0F + this.masterOperatingRatio) / 15.0F;
  }

  @Override
  public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
    return new SteamTurbineMenu(id, inventory, this);
  }

  @Override
  public Component getDisplayName() {
    return Component.translatable(Translations.Container.STEAM_TURBINE);
  }

  @Override
  public void writeToBuf(FriendlyByteBuf out) {
    super.writeToBuf(out);
    out.writeFloat(this.masterOperatingRatio);
  }

  @Override
  public void readFromBuf(FriendlyByteBuf in) {
    super.readFromBuf(in);
    this.masterOperatingRatio = in.readFloat();
  }

  @Override
  public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
    var masterModule = this.getMasterBlockEntity()
        .map(SteamTurbineBlockEntity::getSteamTurbineModule);
    if (cap == ForgeCapabilities.FLUID_HANDLER) {
      return masterModule
          .map(SteamTurbineModule::getFluidHandler)
          .<LazyOptional<T>>map(LazyOptional::cast)
          .orElse(LazyOptional.empty());
    }
    if (cap == ForgeCapabilities.ENERGY) {
      return masterModule
          .map(SteamTurbineModule::getEnergyStorage)
          .<LazyOptional<T>>map(LazyOptional::cast)
          .orElse(LazyOptional.empty());
    }
    return super.getCapability(cap, side);
  }

  @Override
  protected boolean isBlockEntity(MultiblockPattern.Element element) {
    return true;
  }

  @Override
  protected void membershipChanged(@Nullable Membership<SteamTurbineBlockEntity> membership) {
    if (membership == null) {
      this.module.storage().setState(ChargeStorage.State.DISABLED);

      this.level.setBlockAndUpdate(this.getBlockPos(),
          this.getBlockState().setValue(SteamTurbineBlock.TYPE, SteamTurbineBlock.Type.NONE));
      Containers.dropContents(this.level, this.getBlockPos(), this.module.getRotorContainer());
      return;
    }

    if (membership.master() == this) {
      this.module.storage().setState(ChargeStorage.State.SOURCE);
    }

    var type = switch (membership.patternElement().marker()) {
      case 'A' -> SteamTurbineBlock.Type.TOP_LEFT;
      case 'B' -> SteamTurbineBlock.Type.TOP_RIGHT;
      case 'C' -> SteamTurbineBlock.Type.BOTTOM_LEFT;
      case 'D' -> SteamTurbineBlock.Type.BOTTOM_RIGHT;
      case 'W' -> SteamTurbineBlock.Type.WINDOW;
      case 'X' -> SteamTurbineBlock.Type.NONE;
      default -> throw new IllegalArgumentException(
          "Unexpected value: " + membership.patternElement());
    };

    this.level.setBlockAndUpdate(this.getBlockPos(),
        this.getBlockState()
            .setValue(SteamTurbineBlock.TYPE, type)
            .setValue(SteamTurbineBlock.ROTATED,
                membership.master().getCurrentPattern().get() == rotatedPattern));
  }
}
