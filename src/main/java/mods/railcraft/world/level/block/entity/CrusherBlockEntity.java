package mods.railcraft.world.level.block.entity;

import java.util.List;
import org.jspecify.annotations.Nullable;
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
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Util;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.energy.EnergyHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.transaction.Transaction;

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

  @Override
  public void preRemoveSideEffects(BlockPos pos, BlockState state) {
    super.preRemoveSideEffects(pos, state);
    if (this.level instanceof ServerLevel serverLevel) {
      ((CrusherMultiblockBlock) state.getBlock()).deregisterNode(serverLevel, pos);
    }
  }

  public static void serverTick(Level level, BlockPos blockPos, BlockState blockState,
      CrusherBlockEntity blockEntity) {
    blockEntity.serverTick();
    blockEntity.moduleDispatcher.serverTick();
    var serverLevel = (ServerLevel) level;

    if (++blockEntity.tick % 8 == 0) {
      blockEntity.tick = 0;
      blockEntity.getMasterBlockEntity()
          .ifPresent(master -> {
            var target = blockPos.above();
            var energyCap = level
                .getCapability(Capabilities.Energy.BLOCK, master.getBlockPos(), null);
            EntitySearcher.findLiving()
                .at(target)
                .and(ModEntitySelector.KILLABLE)
                .list(level)
                .forEach(livingEntity -> {
                  if (energyCap == null) {
                    return;
                  }
                  if (energyCap.getAmountAsInt() < KILLING_POWER_COST) {
                    return;
                  }
                  var damageSource = RailcraftDamageSources.crusher(level.registryAccess());
                  livingEntity.hurtServer(serverLevel, damageSource, 5);
                  try (var tx = Transaction.openRoot()) {
                    energyCap.extract(KILLING_POWER_COST, tx);
                    tx.commit();
                  }
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

  @Nullable
  public ResourceHandler<ItemResource> getItemCap(@Nullable Direction side) {
    var masterModule = this.getMasterBlockEntity()
        .map(CrusherBlockEntity::getCrusherModule);
    return masterModule
        .map(CrusherModule::getItemHandler)
        .orElse(null);
  }

  @Nullable
  public EnergyHandler getEnergyCap(@Nullable Direction side) {
    var masterModule = this.getMasterBlockEntity()
        .map(CrusherBlockEntity::getCrusherModule);
    return masterModule
        .map(CrusherModule::getEnergyHandler)
        .orElse(null);
  }
}
