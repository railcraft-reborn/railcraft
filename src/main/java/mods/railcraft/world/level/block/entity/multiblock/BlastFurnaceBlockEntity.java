package mods.railcraft.world.level.block.entity.multiblock;

import java.util.List;
import javax.annotation.Nullable;
import it.unimi.dsi.fastutil.chars.CharList;
import mods.railcraft.util.container.CompositeContainerAdaptor;
import mods.railcraft.util.container.wrappers.ContainerMapper;
import mods.railcraft.world.inventory.BlastFurnaceMenu;
import mods.railcraft.world.level.block.CokeOvenBricksBlock;
import mods.railcraft.world.level.block.RailcraftBlocks;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntityTypes;
import mods.railcraft.world.level.block.entity.module.BlastFurnaceModule;
import mods.railcraft.world.level.block.entity.module.ModuleProvider;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class BlastFurnaceBlockEntity extends MultiblockBlockEntity<BlastFurnaceBlockEntity> {

  private static final Component MENU_TITLE =
      new TranslatableComponent("container.blast_furnace");

  private static final MultiblockPattern PATTERN = Util.make(() -> {
    final var bricks = BlockPredicate.of(RailcraftBlocks.BLAST_FURNACE_BRICKS);
    final var lava = BlockPredicate.ofFluidTag(FluidTags.LAVA);

    final var middle = List.of(
        CharList.of('B', 'B', 'B'),
        CharList.of('B', 'A', 'B'),
        CharList.of('B', 'B', 'B'));

    return MultiblockPattern.builder(new BlockPos(2, 1, 2))
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

  private final BlastFurnaceModule logic;

  private final ContainerMapper fuelContainerMapper;

  private int fuelMoveTicks;

  public BlastFurnaceBlockEntity(BlockPos blockPos, BlockState blockState) {
    super(RailcraftBlockEntityTypes.BLAST_FURNACE.get(), blockPos, blockState,
        BlastFurnaceBlockEntity.class, PATTERN);
    this.logic = this.moduleDispatcher.registerCapabilityModule("blast_furnace",
        new BlastFurnaceModule(ModuleProvider.of(this)));
    this.fuelContainerMapper = ContainerMapper.make(
        this.logic, BlastFurnaceModule.SLOT_FUEL, 1);
  }

  public BlastFurnaceModule getLogic() {
    return this.logic;
  }

  @Override
  protected boolean isBlockEntity(char id) {
    return id == 'B' || id == 'W';
  }

  @Override
  protected void membershipChanged(@Nullable Membership<BlastFurnaceBlockEntity> membership) {
    if (membership == null) {
      this.level.setBlock(this.getBlockPos(),
          this.getBlockState()
              .setValue(CokeOvenBricksBlock.WINDOW, false)
              .setValue(CokeOvenBricksBlock.LIT, false),
          Block.UPDATE_ALL);
    } else {
      this.level.setBlock(this.getBlockPos(),
          this.getBlockState().setValue(CokeOvenBricksBlock.WINDOW, membership.marker() == 'W'),
          Block.UPDATE_ALL);
    }
  }

  public static void serverTick(Level level, BlockPos blockPos, BlockState blockState,
      BlastFurnaceBlockEntity blockEntity) {
    blockEntity.serverTick();

    blockEntity.getMembership()
        .map(Membership::master)
        .ifPresent(master -> {
          boolean lit = master.logic.isProcessing();
          if (lit != blockState.getValue(CokeOvenBricksBlock.LIT)) {
            level.setBlockAndUpdate(blockPos,
                blockState.setValue(CokeOvenBricksBlock.LIT, lit));
          }

          if (blockEntity.fuelMoveTicks++ >= 128) {
            blockEntity.fuelMoveTicks = 0;
            CompositeContainerAdaptor.of(blockEntity.getAdjacentContainers())
                .moveOneItemTo(master.fuelContainerMapper,
                    item -> master.logic.getItemBurnTime(item) > 0);
          }
        });

    if (blockEntity.isMaster()) {
      blockEntity.logic.serverTick();
    }
  }

  @Override
  public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
    return new BlastFurnaceMenu(id, inventory, this);
  }

  @Override
  public Component getDisplayName() {
    return MENU_TITLE;
  }
}
