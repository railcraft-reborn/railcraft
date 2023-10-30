package mods.railcraft.world.level.block.entity;

import java.util.List;
import org.jetbrains.annotations.Nullable;
import it.unimi.dsi.fastutil.chars.CharList;
import mods.railcraft.Translations.Container;
import mods.railcraft.api.charge.Charge;
import mods.railcraft.api.charge.ChargeStorage;
import mods.railcraft.util.EntitySearcher;
import mods.railcraft.util.ModEntitySelector;
import mods.railcraft.world.damagesource.RailcraftDamageSources;
import mods.railcraft.world.inventory.CrusherMenu;
import mods.railcraft.world.level.block.CrusherMultiblockBlock;
import mods.railcraft.world.level.block.RailcraftBlocks;
import mods.railcraft.world.level.block.entity.multiblock.BlockPredicate;
import mods.railcraft.world.level.block.entity.multiblock.MultiblockBlockEntity;
import mods.railcraft.world.level.block.entity.multiblock.MultiblockPattern;
import mods.railcraft.world.module.CrusherModule;
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
import net.neoforged.neoforge.common.capabilities.Capabilities;
import net.neoforged.neoforge.common.capabilities.Capability;
import net.neoforged.neoforge.common.util.LazyOptional;

public class CrusherBlockEntity extends MultiblockBlockEntity<CrusherBlockEntity, Void> {

  private static final MultiblockPattern<Void> pattern = Util.make(() -> {
    final var bricks = BlockPredicate.of(RailcraftBlocks.CRUSHER);  

    return MultiblockPattern.<Void>builder(BlockPos.ZERO)
        .layer(List.of(
            CharList.of('0', '1', '2'),
            CharList.of('5', '4', '3')))
        .layer(List.of(
            CharList.of('A', 'O', 'A'),
            CharList.of('A', 'O', 'A')))
        .predicate('A', bricks)
        .predicate('O', bricks)
        .predicate('0', bricks)
        .predicate('1', bricks)
        .predicate('2', bricks)
        .predicate('3', bricks)
        .predicate('4', bricks)
        .predicate('5', bricks)
        .build();
  });

  private static final MultiblockPattern<Void> rotatedPattern = pattern.rotateClockwise();
  private static final int KILLING_POWER_COST = 5000;

  private final CrusherModule crusherModule;
  private int tick = 0;

  public CrusherBlockEntity(BlockPos blockPos, BlockState blockState) {
    super(RailcraftBlockEntityTypes.CRUSHER.get(), blockPos, blockState,
        CrusherBlockEntity.class, List.of(pattern, rotatedPattern));
    this.crusherModule = this.moduleDispatcher.registerModule("crusher",
        new CrusherModule(this, Charge.distribution));
  }

  public static void serverTick(Level level, BlockPos blockPos, BlockState blockState,
      CrusherBlockEntity blockEntity) {
    blockEntity.serverTick();
    blockEntity.moduleDispatcher.serverTick();
    if (blockEntity.isMaster()) {
      blockEntity.crusherModule.serverTick();
    }

    if (++blockEntity.tick % 8 == 0) {
      blockEntity.tick = 0;
      blockEntity.getMasterBlockEntity()
          .ifPresent(master -> {
            var target = blockPos.above();
            var energyCap = master.getCapability(Capabilities.ENERGY);
            EntitySearcher.findLiving()
                .at(target)
                .and(ModEntitySelector.KILLABLE)
                .list(level)
                .forEach(livingEntity -> {
                  energyCap.ifPresent(energyStorage -> {
                    if (energyStorage.getEnergyStored() >= KILLING_POWER_COST) {
                      livingEntity.hurt(RailcraftDamageSources.crusher(level.registryAccess()), 5);
                      energyStorage.extractEnergy(KILLING_POWER_COST, false);
                    }
                  });
                });
          });
    }
  }

  public CrusherModule getCrusherModule() {
    return this.crusherModule;
  }

  @Override
  protected boolean isBlockEntity(MultiblockPattern.Element element) {
    return true;
  }

  @Override
  protected void membershipChanged(@Nullable Membership<CrusherBlockEntity> membership) {
    if (membership == null) {
      this.crusherModule.storage()
          .ifPresent(storage -> storage.setState(ChargeStorage.State.DISABLED));
      this.level.setBlockAndUpdate(this.getBlockPos(),
          this.getBlockState()
              .setValue(CrusherMultiblockBlock.TYPE, CrusherMultiblockBlock.Type.NONE)
              .setValue(CrusherMultiblockBlock.ROTATED, false)
              .setValue(CrusherMultiblockBlock.OUTPUT, false));
      Containers.dropContents(this.level, this.getBlockPos(), this.crusherModule);
      return;
    }
    if (membership.master() == this) {
      this.crusherModule.storage()
          .ifPresent(storage -> storage.setState(ChargeStorage.State.RECHARGEABLE));
    }

    var type = switch (membership.patternElement().marker()) {
      case '0' -> CrusherMultiblockBlock.Type.NORTH_WEST;
      case '1' -> CrusherMultiblockBlock.Type.NORTH;
      case '2' -> CrusherMultiblockBlock.Type.NORTH_EAST;
      case '3' -> CrusherMultiblockBlock.Type.SOUTH_EAST;
      case '4' -> CrusherMultiblockBlock.Type.SOUTH;
      case '5' -> CrusherMultiblockBlock.Type.SOUTH_WEST;
      default -> CrusherMultiblockBlock.Type.NONE;
    };

    this.level.setBlockAndUpdate(this.getBlockPos(),
        this.getBlockState()
            .setValue(CrusherMultiblockBlock.TYPE, type)
            .setValue(CrusherMultiblockBlock.OUTPUT,
                membership.patternElement().marker() == 'O')
            .setValue(CrusherMultiblockBlock.ROTATED,
                membership.master().getCurrentPattern().get() == rotatedPattern));
  }

  @Override
  public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
    return new CrusherMenu(id, inventory, this);
  }

  @Override
  public Component getDisplayName() {
    return Component.translatable(Container.CRUSHER);
  }

  @Override
  public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
    var masterModule = this.getMasterBlockEntity()
        .map(CrusherBlockEntity::getCrusherModule);
    if (cap == ForgeCapabilities.ITEM_HANDLER) {
      return masterModule
          .map(CrusherModule::getItemHandler)
          .<LazyOptional<T>>map(LazyOptional::cast)
          .orElse(LazyOptional.empty());
    }
    if (cap == ForgeCapabilities.ENERGY) {
      return masterModule
          .map(CrusherModule::getEnergyHandler)
          .<LazyOptional<T>>map(LazyOptional::cast)
          .orElse(LazyOptional.empty());
    }
    return super.getCapability(cap, side);
  }
}
