package mods.railcraft.world.level.block.entity;

import it.unimi.dsi.fastutil.chars.CharList;
import java.util.List;
import org.jetbrains.annotations.Nullable;
import mods.railcraft.Translations;
import mods.railcraft.world.inventory.BlastFurnaceMenu;
import mods.railcraft.world.level.block.FurnaceMultiblockBlock;
import mods.railcraft.world.level.block.RailcraftBlocks;
import mods.railcraft.world.level.block.entity.multiblock.BlockPredicate;
import mods.railcraft.world.level.block.entity.multiblock.MultiblockBlockEntity;
import mods.railcraft.world.level.block.entity.multiblock.MultiblockPattern;
import mods.railcraft.world.module.BlastFurnaceModule;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;

public class BlastFurnaceBlockEntity extends MultiblockBlockEntity<BlastFurnaceBlockEntity, Void> {

  private static final MultiblockPattern<Void> PATTERN = Util.make(() -> {
    final var bricks = BlockPredicate.of(RailcraftBlocks.BLAST_FURNACE_BRICKS);
    final var lava = BlockPredicate.ofFluidTag(FluidTags.LAVA);

    final var middle = List.of(
        CharList.of('B', 'B', 'B'),
        CharList.of('B', 'A', 'B'),
        CharList.of('B', 'B', 'B'));

    return MultiblockPattern.<Void>builder(2, 1, 2)
        .layer(List.of(
            CharList.of('B', 'B', 'B'),
            CharList.of('B', 'B', 'B'),
            CharList.of('B', 'B', 'B')))
        .layer(middle)
        .layer(middle)
        .layer(List.of(
            CharList.of('B', 'W', 'B'),
            CharList.of('W', 'B', 'W'),
            CharList.of('B', 'W', 'B')))
        .predicate('B', bricks)
        .predicate('W', bricks)
        .predicate('A', lava.or(BlockPredicate.AIR))
        .build();
  });

  private final BlastFurnaceModule blastFurnaceModule;

  private int fuelMoveTicks;

  public BlastFurnaceBlockEntity(BlockPos blockPos, BlockState blockState) {
    super(RailcraftBlockEntityTypes.BLAST_FURNACE.get(), blockPos, blockState,
        BlastFurnaceBlockEntity.class, PATTERN);
    this.blastFurnaceModule = this.moduleDispatcher.registerModule("blast_furnace",
        new BlastFurnaceModule(this));
  }

  public static void serverTick(Level level, BlockPos blockPos, BlockState blockState,
      BlastFurnaceBlockEntity blockEntity) {
    blockEntity.serverTick();

    blockEntity.moduleDispatcher.serverTick();

    blockEntity.getMasterBlockEntity()
        .ifPresent(master -> {
          var lit = master.blastFurnaceModule.isBurning();
          if (lit != blockState.getValue(FurnaceMultiblockBlock.LIT)) {
            level.setBlockAndUpdate(blockPos,
                blockState.setValue(FurnaceMultiblockBlock.LIT, lit));
          }

          if (blockEntity.fuelMoveTicks++ >= 128) {
            blockEntity.fuelMoveTicks = 0;
            blockEntity.findAdjacentContainers().moveOneItemTo(
                master.getBlastFurnaceModule().getFuelContainer(),
                master.blastFurnaceModule::isFuel);
          }
        });
  }

  public BlastFurnaceModule getBlastFurnaceModule() {
    return this.blastFurnaceModule;
  }

  @Override
  protected boolean isBlockEntity(MultiblockPattern.Element element) {
    return element.marker() == 'B' || element.marker() == 'W';
  }

  @Override
  protected void membershipChanged(@Nullable Membership<BlastFurnaceBlockEntity> membership) {
    if (membership == null) {
      this.level.setBlock(this.getBlockPos(),
          this.getBlockState()
              .setValue(FurnaceMultiblockBlock.WINDOW, false)
              .setValue(FurnaceMultiblockBlock.LIT, false),
          Block.UPDATE_ALL);
      Containers.dropContents(this.level, this.getBlockPos(), this.blastFurnaceModule);
    } else {
      this.level.setBlock(this.getBlockPos(),
          this.getBlockState().setValue(FurnaceMultiblockBlock.WINDOW,
              membership.patternElement().marker() == 'W'),
          Block.UPDATE_ALL);
    }
  }

  @Override
  public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
    return new BlastFurnaceMenu(id, inventory, this);
  }

  @Override
  public Component getDisplayName() {
    return Component.translatable(Translations.Container.BLAST_FURNACE);
  }

  @Override
  public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
    if (cap == ForgeCapabilities.ITEM_HANDLER) {
      return this.getMasterBlockEntity()
          .map(BlastFurnaceBlockEntity::getBlastFurnaceModule)
          .map(BlastFurnaceModule::getItemHandler)
          .<LazyOptional<T>>map(LazyOptional::cast)
          .orElse(LazyOptional.empty());
    }
    return super.getCapability(cap, side);
  }
}
