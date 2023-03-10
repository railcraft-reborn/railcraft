package mods.railcraft.world.level.block.entity.tank;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.jetbrains.annotations.Nullable;
import com.google.common.collect.ImmutableList;
import it.unimi.dsi.fastutil.chars.CharList;
import mods.railcraft.RailcraftConfig;
import mods.railcraft.Translations;
import mods.railcraft.world.inventory.TankMenu;
import mods.railcraft.world.level.block.AbstractStrengthenedGlassBlock;
import mods.railcraft.world.level.block.entity.multiblock.BlockPredicate;
import mods.railcraft.world.level.block.entity.multiblock.MultiblockBlockEntity;
import mods.railcraft.world.level.block.entity.multiblock.MultiblockPattern;
import mods.railcraft.world.level.block.tank.TankGaugeBlock;
import mods.railcraft.world.level.block.tank.TankValveBlock;
import mods.railcraft.world.level.material.fluid.FluidTools;
import mods.railcraft.world.level.material.fluid.StandardTank;
import mods.railcraft.world.module.TankModule;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandler;

public abstract class TankBlockEntity extends MultiblockBlockEntity<TankBlockEntity, Void> {

  private static final int FLOW_RATE = FluidType.BUCKET_VOLUME;

  private final TankModule module;

  private int lastLight = -1;

  private int maxX;
  private int maxY;
  private int maxZ;

  private LazyOptional<IFluidHandler> fluidHandler = LazyOptional.empty();

  public TankBlockEntity(BlockEntityType<?> type, BlockPos blockPos, BlockState blockState,
      Collection<MultiblockPattern<Void>> patterns) {
    super(type, blockPos, blockState, TankBlockEntity.class, patterns);
    this.module = this.moduleDispatcher.registerModule("tank",
        new TankModule(this, StandardTank.ofCapacity(0)));
    this.module.getTank().changeCallback(this::tankChanged);
  }

  @Override
  protected void serverTick() {
    super.serverTick();
    this.fluidHandler.ifPresent(fluidHandler -> {
      var neighbors = FluidTools.findNeighbors(this.getLevel(), this.getBlockPos(),
          blockEntity -> !(blockEntity instanceof TankBlockEntity tank)
              || !tank.getMembership().equals(this.getMembership()),
          Direction.DOWN, Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST);
      for (var neighbor : neighbors) {
        FluidUtil.tryFluidTransfer(neighbor, fluidHandler, FLOW_RATE, true);
      }
    });
  }

  private void lightChanged(int light) {
    if (this.getBlockState().getBlock() instanceof TankGaugeBlock) {
      this.level.setBlock(this.getBlockPos(),
          this.getBlockState().setValue(TankGaugeBlock.LEVEL, light), Block.UPDATE_CLIENTS);
    }
  }

  protected void tankChanged() {
    if (!this.hasLevel() || this.level.isClientSide()) {
      return;
    }

    this.setChanged();
    this.syncToClient();

    var fluidStack = this.module.getTank().getFluid();
    var fluidType = this.module.getTank().getFluidType().getFluidType();
    var light = fluidType.getLightLevel(fluidStack);
    if (light != this.lastLight) {
      this.lastLight = light;
      this.streamMembers().forEach(member -> member.lightChanged(light));
    }
  }

  @Override
  public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
    return new TankMenu(id, inventory, this);
  }

  @Override
  public Component getDisplayName() {
    return Component.translatable(Translations.Container.TANK);
  }

  public TankModule getModule() {
    return this.module;
  }

  public int getMaxX() {
    return this.maxX;
  }

  public int getMaxY() {
    return this.maxY;
  }

  public int getMaxZ() {
    return this.maxZ;
  }

  @Override
  public InteractionResult use(ServerPlayer player, InteractionHand hand) {
    return FluidUtil.interactWithFluidHandler(player, hand, this.module.getTank())
        ? InteractionResult.CONSUME
        : super.use(player, hand);
  }

  protected abstract int getCapacityPerBlock();

  @Override
  protected boolean isBlockEntity(MultiblockPattern.Element element) {
    return element.marker() == 'W' || element.marker() == 'B';
  }

  @Override
  protected void membershipChanged(@Nullable Membership<TankBlockEntity> membership) {
    this.getCurrentPattern().ifPresent(pattern -> {
      this.module.getTank().setCapacity(this.getCapacityPerBlock() * pattern.getArea());
      this.maxX = pattern.getXSize();
      this.maxY = pattern.getYSize();
      this.maxZ = pattern.getZSize();
      this.syncToClient();
    });

    if (membership == null) {
      this.getModule().getTank().setFluid(FluidStack.EMPTY);
      this.lightChanged(BlockStateProperties.MIN_LEVEL);
      this.fluidHandler = LazyOptional.empty();
    } else if (this.getBlockState().getBlock() instanceof TankValveBlock) {
      this.fluidHandler = LazyOptional.of(() -> new ValveFluidHandler(this, membership.master()));
    }

    if (this.getBlockState().getBlock() instanceof TankGaugeBlock block) {
      var type = membership == null
          ? AbstractStrengthenedGlassBlock.Type.SINGLE
          : AbstractStrengthenedGlassBlock.Type.determine(this.getBlockPos(), this.level, block);
      if (this.getBlockState().getValue(AbstractStrengthenedGlassBlock.TYPE) != type) {
        this.level.setBlockAndUpdate(this.getBlockPos(),
            this.getBlockState().setValue(AbstractStrengthenedGlassBlock.TYPE, type));
      }
    }
  }

  @Override
  public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction side) {
    if (capability == ForgeCapabilities.FLUID_HANDLER) {
      return this.fluidHandler.cast();
    }

    return super.getCapability(capability, side);
  }

  @Override
  public void writeToBuf(FriendlyByteBuf out) {
    super.writeToBuf(out);
    out.writeVarInt(this.maxX);
    out.writeVarInt(this.maxY);
    out.writeVarInt(this.maxZ);
  }

  @Override
  public void readFromBuf(FriendlyByteBuf in) {
    super.readFromBuf(in);
    this.maxX = in.readVarInt();
    this.maxY = in.readVarInt();
    this.maxZ = in.readVarInt();
  }

  protected static Collection<MultiblockPattern<Void>> buildPatterns(
      TagKey<Block> wallBlock, TagKey<Block> gaugeBlock, TagKey<Block> valveBlock) {

    var wallPredicate = BlockPredicate.ofTag(wallBlock);
    var wallGaugeValvePredicate = wallPredicate
        .or(BlockPredicate.ofTag(gaugeBlock))
        .or(BlockPredicate.ofTag(valveBlock));

    var patterns = ImmutableList.<MultiblockPattern<Void>>builder();

    // 3x3

    var bottom = List.of(
        CharList.of('B', 'B', 'B'),
        CharList.of('B', 'W', 'B'),
        CharList.of('B', 'B', 'B'));

    var middle = List.of(
        CharList.of('B', 'W', 'B'),
        CharList.of('W', 'A', 'W'),
        CharList.of('B', 'W', 'B'));

    var top = List.of(
        CharList.of('B', 'B', 'B'),
        CharList.of('B', 'W', 'B'),
        CharList.of('B', 'B', 'B'));

    for (int i = 4; i <= 8; i++) {
      var pattern = createTank(i, bottom, middle, top);
      var entityCheck = new AABB(0, 1, 0, 1, i - 1, 1);
      patterns.add(buildPattern(pattern, wallPredicate, wallGaugeValvePredicate, entityCheck));
    }

    // 5x5
    if (RailcraftConfig.server.maxTankSize.get() >= 5) {
      bottom = List.of(
          CharList.of('B', 'B', 'B', 'B', 'B'),
          CharList.of('B', 'W', 'W', 'W', 'B'),
          CharList.of('B', 'W', 'W', 'W', 'B'),
          CharList.of('B', 'W', 'W', 'W', 'B'),
          CharList.of('B', 'B', 'B', 'B', 'B'));

      middle = List.of(
          CharList.of('B', 'W', 'W', 'W', 'B'),
          CharList.of('W', 'A', 'A', 'A', 'W'),
          CharList.of('W', 'A', 'A', 'A', 'W'),
          CharList.of('W', 'A', 'A', 'A', 'W'),
          CharList.of('B', 'W', 'W', 'W', 'B'));

      top = List.of(
          CharList.of('B', 'B', 'B', 'B', 'B'),
          CharList.of('B', 'W', 'W', 'W', 'B'),
          CharList.of('B', 'W', 'W', 'W', 'B'),
          CharList.of('B', 'W', 'W', 'W', 'B'),
          CharList.of('B', 'B', 'B', 'B', 'B'));

      for (int i = 4; i <= 8; i++) {
        var map = createTank(i, bottom, middle, top);
        var entityCheck = new AABB(-1, 1, -1, 2, i - 1, 2);
        patterns.add(buildPattern(map, wallPredicate, wallGaugeValvePredicate, entityCheck));
      }
    }

    // 7x7
    if (RailcraftConfig.server.maxTankSize.get() >= 7) {
      bottom = List.of(
          CharList.of('B', 'B', 'B', 'B', 'B', 'B', 'B'),
          CharList.of('B', 'W', 'W', 'W', 'W', 'W', 'B'),
          CharList.of('B', 'W', 'W', 'W', 'W', 'W', 'B'),
          CharList.of('B', 'W', 'W', 'W', 'W', 'W', 'B'),
          CharList.of('B', 'W', 'W', 'W', 'W', 'W', 'B'),
          CharList.of('B', 'W', 'W', 'W', 'W', 'W', 'B'),
          CharList.of('B', 'B', 'B', 'B', 'B', 'B', 'B'));

      middle = List.of(
          CharList.of('B', 'W', 'W', 'W', 'W', 'W', 'B'),
          CharList.of('W', 'A', 'A', 'A', 'A', 'A', 'W'),
          CharList.of('W', 'A', 'A', 'A', 'A', 'A', 'W'),
          CharList.of('W', 'A', 'A', 'A', 'A', 'A', 'W'),
          CharList.of('W', 'A', 'A', 'A', 'A', 'A', 'W'),
          CharList.of('W', 'A', 'A', 'A', 'A', 'A', 'W'),
          CharList.of('B', 'W', 'W', 'W', 'W', 'W', 'B'));

      top = List.of(
          CharList.of('B', 'B', 'B', 'B', 'B', 'B', 'B'),
          CharList.of('B', 'W', 'W', 'W', 'W', 'W', 'B'),
          CharList.of('B', 'W', 'W', 'W', 'W', 'W', 'B'),
          CharList.of('B', 'W', 'W', 'W', 'W', 'W', 'B'),
          CharList.of('B', 'W', 'W', 'W', 'W', 'W', 'B'),
          CharList.of('B', 'W', 'W', 'W', 'W', 'W', 'B'),
          CharList.of('B', 'B', 'B', 'B', 'B', 'B', 'B'));

      for (int i = 4; i <= 8; i++) {
        var map = createTank(i, bottom, middle, top);
        var entityCheck = new AABB(-2, 1, -2, 3, i - 1, 3);
        patterns.add(buildPattern(map, wallPredicate, wallGaugeValvePredicate, entityCheck));
      }
    }

    // 9x9
    if (RailcraftConfig.server.maxTankSize.get() >= 9) {
      bottom = List.of(
          CharList.of('B', 'B', 'B', 'B', 'B', 'B', 'B', 'B', 'B'),
          CharList.of('B', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'B'),
          CharList.of('B', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'B'),
          CharList.of('B', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'B'),
          CharList.of('B', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'B'),
          CharList.of('B', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'B'),
          CharList.of('B', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'B'),
          CharList.of('B', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'B'),
          CharList.of('B', 'B', 'B', 'B', 'B', 'B', 'B', 'B', 'B'));

      middle = List.of(
          CharList.of('B', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'B'),
          CharList.of('W', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'W'),
          CharList.of('W', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'W'),
          CharList.of('W', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'W'),
          CharList.of('W', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'W'),
          CharList.of('W', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'W'),
          CharList.of('W', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'W'),
          CharList.of('W', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'W'),
          CharList.of('B', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'B'));

      top = List.of(
          CharList.of('B', 'B', 'B', 'B', 'B', 'B', 'B', 'B', 'B'),
          CharList.of('B', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'B'),
          CharList.of('B', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'B'),
          CharList.of('B', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'B'),
          CharList.of('B', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'B'),
          CharList.of('B', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'B'),
          CharList.of('B', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'B'),
          CharList.of('B', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'B'),
          CharList.of('B', 'B', 'B', 'B', 'B', 'B', 'B', 'B', 'B'));

      for (int i = 4; i <= 8; i++) {
        var map = createTank(i, bottom, middle, top);
        var entityCheck = new AABB(-3, 1, -3, 4, i - 1, 4);
        patterns.add(buildPattern(map, wallPredicate, wallGaugeValvePredicate, entityCheck));
      }
    }

    return patterns.build();
  }

  private static MultiblockPattern<Void> buildPattern(
      Collection<List<CharList>> pattern, BlockPredicate wallPredicate,
      BlockPredicate wallGaugeValvePredicate, AABB entityCheckBounds) {
    return MultiblockPattern.<Void>builder(1, 0, 1)
        .pattern(pattern)
        .entityCheckBounds(entityCheckBounds)
        .predicate('B', wallPredicate)
        .predicate('W', wallGaugeValvePredicate)
        .predicate('A', BlockPredicate.AIR)
        .build();
  }

  private static List<List<CharList>> createTank(int height, List<CharList> bottom,
      List<CharList> middle, List<CharList> top) {
    List<List<CharList>> pattern = new ArrayList<>(height);
    pattern.add(top);
    for (int i = 0; i < height - 2; i++) {
      pattern.add(middle);
    }
    pattern.add(bottom);
    return pattern;
  }

  @FunctionalInterface
  public interface LightCallback {

    void lightChanged(BlockState blockState, Level level, BlockPos blockPos, int light);
  }
}
