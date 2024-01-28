package mods.railcraft.world.level.block.entity.steamboiler;

import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.Nullable;
import com.google.common.collect.ImmutableList;
import it.unimi.dsi.fastutil.chars.CharArrayList;
import it.unimi.dsi.fastutil.chars.CharList;
import mods.railcraft.world.level.block.RailcraftBlocks;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntityTypes;
import mods.railcraft.world.level.block.entity.multiblock.BlockPredicate;
import mods.railcraft.world.level.block.entity.multiblock.MultiblockBlockEntity;
import mods.railcraft.world.level.block.entity.multiblock.MultiblockPattern;
import mods.railcraft.world.level.block.steamboiler.FireboxBlock;
import mods.railcraft.world.level.block.steamboiler.SteamBoilerTankBlock;
import mods.railcraft.world.level.material.steam.SteamConstants;
import mods.railcraft.world.module.SteamBoilerModule;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.IItemHandler;

public class SteamBoilerBlockEntity
    extends MultiblockBlockEntity<SteamBoilerBlockEntity, SteamBoilerBlockEntity.Metadata> {

  public static final int TRANSFER_RATE = FluidType.BUCKET_VOLUME;
  public static final int TICKS_LOW = 16;
  public static final int TICKS_HIGH = 8;
  public static final int STEAM_LOW = 16;
  public static final int STEAM_HIGH = 32;
  public static final float HEAT_LOW = SteamConstants.MAX_HEAT_LOW;
  public static final float HEAT_HIGH = SteamConstants.MAX_HEAT_HIGH;

  private static final List<MultiblockPattern<Metadata>> PATTERNS = Util.make(() -> {
    var patterns = ImmutableList.<MultiblockPattern<Metadata>>builder();
    patterns.addAll(buildPatterns(BlockPredicate.of(RailcraftBlocks.SOLID_FUELED_FIREBOX)));
    patterns.addAll(buildPatterns(BlockPredicate.of(RailcraftBlocks.FLUID_FUELED_FIREBOX)));
    return patterns.build();
  });

  private LazyOptional<IFluidHandler> fluidHandler = LazyOptional.empty();
  private LazyOptional<IItemHandler> itemHandler = LazyOptional.empty();

  public SteamBoilerBlockEntity(BlockPos blockPos, BlockState blockState) {
    this(RailcraftBlockEntityTypes.STEAM_BOILER.get(), blockPos, blockState);
  }

  public SteamBoilerBlockEntity(BlockEntityType<?> type, BlockPos blockPos, BlockState blockState) {
    super(type, blockPos, blockState, SteamBoilerBlockEntity.class, PATTERNS);
  }

  public static void serverTick(Level level, BlockPos blockPos, BlockState blockState,
      SteamBoilerBlockEntity blockEntity) {
    blockEntity.serverTick();
    blockEntity.moduleDispatcher.serverTick();
  }

  @Override
  public InteractionResult use(ServerPlayer player, InteractionHand hand) {
    return FluidUtil.interactWithFluidHandler(player, hand,
        this.getModule(SteamBoilerModule.class).get().getTankManager())
            ? InteractionResult.CONSUME
            : super.use(player, hand);
  }

  @Override
  public <T> LazyOptional<T> getCapability(Capability<T> capability,
      @Nullable Direction direction) {
    if (capability == ForgeCapabilities.FLUID_HANDLER) {
      return this.fluidHandler.cast();
    }

    if (capability == ForgeCapabilities.ITEM_HANDLER) {
      return this.itemHandler.cast();
    }

    return LazyOptional.empty();
  }

  @Override
  public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
    throw new UnsupportedOperationException("Not a master block.");
  }

  @Override
  protected boolean isBlockEntity(MultiblockPattern.Element element) {
    return element.marker() != 'O';
  }

  @Override
  protected void membershipChanged(@Nullable Membership<SteamBoilerBlockEntity> membership) {
    if (membership == null) {
      this.fluidHandler = LazyOptional.empty();
      this.itemHandler = LazyOptional.empty();
      this.getModule(SteamBoilerModule.class)
          .ifPresent(module -> Containers.dropContents(this.level, this.getBlockPos(), module));
    } else {
      SteamBoilerModule<?> module = membership.master().getModule(SteamBoilerModule.class).get();

      if (membership.master() == this) {
        module.update(this.getCurrentPattern().get().getMetadata());
      }

      this.fluidHandler = module.getFluidHandler();
      this.itemHandler = module.getItemHandler();
    }

    if (this.getBlockState().getBlock() instanceof FireboxBlock && membership == null) {
      this.level.setBlockAndUpdate(this.getBlockPos(), this.getBlockState()
          .setValue(FireboxBlock.LIT, false));
    } else if (this.getBlockState().getBlock() instanceof SteamBoilerTankBlock) {
      if (membership == null) {
        this.level.setBlockAndUpdate(this.getBlockPos(), this.getBlockState()
            .setValue(SteamBoilerTankBlock.CONNECTION_TYPE,
                SteamBoilerTankBlock.ConnectionType.NONE));
        return;
      }

      var patternPos = membership.patternElement().relativePos();
      var marker = membership.patternElement().marker();
      var pattern = membership.master().getCurrentPattern().get();

      var north = pattern.getMarkerOrDefault(patternPos.north(), 'O') == marker;
      var south = pattern.getMarkerOrDefault(patternPos.south(), 'O') == marker;
      var east = pattern.getMarkerOrDefault(patternPos.east(), 'O') == marker;
      var west = pattern.getMarkerOrDefault(patternPos.west(), 'O') == marker;

      SteamBoilerTankBlock.ConnectionType type = null;
      if (!north && !south && !east && !west) {
        type = SteamBoilerTankBlock.ConnectionType.NONE;
      } else if (north && south && east && west) {
        type = SteamBoilerTankBlock.ConnectionType.ALL;
      } else if (north && south && east) {
        type = SteamBoilerTankBlock.ConnectionType.NORTH_SOUTH_EAST;
      } else if (south && east && west) {
        type = SteamBoilerTankBlock.ConnectionType.SOUTH_EAST_WEST;
      } else if (north && east && west) {
        type = SteamBoilerTankBlock.ConnectionType.NORTH_EAST_WEST;
      } else if (north && south && west) {
        type = SteamBoilerTankBlock.ConnectionType.NORTH_SOUTH_WEST;
      } else if (north && east) {
        type = SteamBoilerTankBlock.ConnectionType.NORTH_EAST;
      } else if (south && east) {
        type = SteamBoilerTankBlock.ConnectionType.SOUTH_EAST;
      } else if (south && west) {
        type = SteamBoilerTankBlock.ConnectionType.SOUTH_WEST;
      } else if (north && west) {
        type = SteamBoilerTankBlock.ConnectionType.NORTH_WEST;
      }

      if (type == null) {
        throw new IllegalStateException("Something's not right here...");
      }

      this.level.setBlockAndUpdate(this.getBlockPos(), this.getBlockState()
          .setValue(SteamBoilerTankBlock.CONNECTION_TYPE, type));
    }
  }

  private static List<MultiblockPattern<Metadata>> buildPatterns(BlockPredicate fireboxPredicate) {
    return List.of(buildPattern(3, 4, 2, TICKS_HIGH, HEAT_HIGH, STEAM_HIGH,
        BlockPredicate.of(RailcraftBlocks.HIGH_PRESSURE_STEAM_BOILER_TANK), fireboxPredicate),
        buildPattern(3, 3, 2, TICKS_HIGH, HEAT_HIGH, STEAM_HIGH,
            BlockPredicate.of(RailcraftBlocks.HIGH_PRESSURE_STEAM_BOILER_TANK), fireboxPredicate),
        buildPattern(3, 2, 2, TICKS_HIGH, HEAT_HIGH, STEAM_HIGH,
            BlockPredicate.of(RailcraftBlocks.HIGH_PRESSURE_STEAM_BOILER_TANK), fireboxPredicate),

        buildPattern(2, 3, 1, TICKS_HIGH, HEAT_HIGH, STEAM_HIGH,
            BlockPredicate.of(RailcraftBlocks.HIGH_PRESSURE_STEAM_BOILER_TANK), fireboxPredicate),
        buildPattern(2, 2, 1, TICKS_HIGH, HEAT_HIGH, STEAM_HIGH,
            BlockPredicate.of(RailcraftBlocks.HIGH_PRESSURE_STEAM_BOILER_TANK), fireboxPredicate),

        buildPattern(1, 1, 1, TICKS_HIGH, HEAT_HIGH, STEAM_HIGH,
            BlockPredicate.of(RailcraftBlocks.HIGH_PRESSURE_STEAM_BOILER_TANK), fireboxPredicate),

        buildPattern(3, 4, 2, TICKS_LOW, HEAT_LOW, STEAM_LOW,
            BlockPredicate.of(RailcraftBlocks.LOW_PRESSURE_STEAM_BOILER_TANK), fireboxPredicate),
        buildPattern(3, 3, 2, TICKS_LOW, HEAT_LOW, STEAM_LOW,
            BlockPredicate.of(RailcraftBlocks.LOW_PRESSURE_STEAM_BOILER_TANK), fireboxPredicate),
        buildPattern(3, 2, 2, TICKS_LOW, HEAT_LOW, STEAM_LOW,
            BlockPredicate.of(RailcraftBlocks.LOW_PRESSURE_STEAM_BOILER_TANK), fireboxPredicate),

        buildPattern(2, 3, 1, TICKS_LOW, HEAT_LOW, STEAM_LOW,
            BlockPredicate.of(RailcraftBlocks.LOW_PRESSURE_STEAM_BOILER_TANK), fireboxPredicate),
        buildPattern(2, 2, 1, TICKS_LOW, HEAT_LOW, STEAM_LOW,
            BlockPredicate.of(RailcraftBlocks.LOW_PRESSURE_STEAM_BOILER_TANK), fireboxPredicate),

        buildPattern(1, 1, 1, TICKS_LOW, HEAT_LOW, STEAM_LOW,
            BlockPredicate.of(RailcraftBlocks.LOW_PRESSURE_STEAM_BOILER_TANK), fireboxPredicate));
  }

  private static MultiblockPattern<Metadata> buildPattern(int width, int tankHeight, int offset,
      int ticks, float heat, int capacity, BlockPredicate tankPredicate,
      BlockPredicate fireboxPredicate) {
    var builder = MultiblockPattern.<Metadata>builder(offset, 0, offset)
        .metadata(new Metadata(width * width * tankHeight, ticks, heat, capacity))
        .predicate('T', tankPredicate)
        .predicate('F', fireboxPredicate)
        .predicate('O', tankPredicate.or(fireboxPredicate).negate());

    // Top air layer
    var layer = new ArrayList<CharList>();
    for (int i = 0; i < width + 2; i++) {
      var list = new CharArrayList();
      for (int j = 0; j < width + 2; j++) {
        list.add('O');
      }
      layer.add(list);
    }
    builder.layer(layer);

    // Tank layer
    for (int i = 0; i < tankHeight; i++) {
      layer = new ArrayList<CharList>();
      for (int j = 0; j < width + 2; j++) {
        var list = new CharArrayList();
        for (int k = 0; k < width + 2; k++) {
          list.add(j == 0 || k == 0 || j == width + 1 || k == width + 1 ? 'O' : 'T');
        }
        layer.add(list);
      }
      builder.layer(layer);
    }

    // Firebox layer
    layer = new ArrayList<CharList>();
    for (int i = 0; i < width + 2; i++) {
      var list = new CharArrayList();
      for (int j = 0; j < width + 2; j++) {
        list.add(i == 0 || j == 0 || i == width + 1 || j == width + 1 ? 'O' : 'F');
      }
      layer.add(list);
    }
    builder.layer(layer);

    return builder.build();
  }

  public record Metadata(int tanks, int ticksPerCycle, float maxTemperature,
      int steamCapacityPerTank) {}
}
