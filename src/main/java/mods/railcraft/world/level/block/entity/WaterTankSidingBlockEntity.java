package mods.railcraft.world.level.block.entity;

import it.unimi.dsi.fastutil.chars.CharList;
import java.util.List;
import mods.railcraft.Translations.Container;
import mods.railcraft.world.inventory.WaterTankSidingMenu;
import mods.railcraft.world.level.block.RailcraftBlocks;
import mods.railcraft.world.level.block.entity.multiblock.BlockPredicate;
import mods.railcraft.world.level.block.entity.multiblock.MultiblockBlockEntity;
import mods.railcraft.world.level.block.entity.multiblock.MultiblockPattern;
import mods.railcraft.world.module.WaterCollectionModule;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.Nullable;

public class WaterTankSidingBlockEntity extends MultiblockBlockEntity<WaterTankSidingBlockEntity, Void> {

  private static final MultiblockPattern<Void> PATTERN = Util.make(() -> {
    final var bricks = BlockPredicate.of(RailcraftBlocks.WATER_TANK_SIDING);

    final var topAndBottom = List.of(
        CharList.of('B', 'B', 'B'),
        CharList.of('B', 'B', 'B'),
        CharList.of('B', 'B', 'B'));

    return MultiblockPattern.<Void>builder(2, 1, 2)
        .layer(topAndBottom)
        .layer(List.of(
            CharList.of('B', 'B', 'B'),
            CharList.of('B', 'A', 'B'),
            CharList.of('B', 'B', 'B')))
        .layer(topAndBottom)
        .predicate('A', BlockPredicate.AIR)
        .predicate('B', bricks)
        .build();
  });

  private final WaterCollectionModule waterCollectionModule;

  public WaterTankSidingBlockEntity(BlockPos blockPos, BlockState blockState) {
    super(RailcraftBlockEntityTypes.WATER_TANK_SIDING.get(), blockPos, blockState,
        WaterTankSidingBlockEntity.class, PATTERN);
    this.waterCollectionModule = this.moduleDispatcher.registerModule("water_tank_siding",
        new WaterCollectionModule(this));
  }

  public static void serverTick(Level level, BlockPos blockPos, BlockState blockState,
      WaterTankSidingBlockEntity blockEntity) {
    blockEntity.serverTick();
    blockEntity.moduleDispatcher.serverTick();
  }

  public WaterCollectionModule getModule() {
    return this.waterCollectionModule;
  }

  @Override
  protected boolean isBlockEntity(MultiblockPattern.Element element) {
    return element.marker() == 'B';
  }

  @Override
  protected void membershipChanged(@Nullable Membership<WaterTankSidingBlockEntity> membership) {
    if (membership == null) {
      Containers.dropContents(this.level, this.getBlockPos(), this.waterCollectionModule);
    }
  }

  @Override
  public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
    return new WaterTankSidingMenu(id, inventory, this);
  }

  @Override
  public Component getDisplayName() {
    return Component.translatable(Container.WATER_TANK_SIDING);
  }

  @Override
  public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
    if (cap == ForgeCapabilities.FLUID_HANDLER) {
      return this.getMasterBlockEntity()
          .map(WaterTankSidingBlockEntity::getModule)
          .map(WaterCollectionModule::getFluidHandler)
          .<LazyOptional<T>>map(LazyOptional::cast)
          .orElse(LazyOptional.empty());
    }
    return super.getCapability(cap, side);
  }
}
