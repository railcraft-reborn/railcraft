package mods.railcraft.world.level.block.entity.multiblock;

import java.util.List;
import javax.annotation.Nullable;
import it.unimi.dsi.fastutil.chars.CharList;
import mods.railcraft.world.inventory.CokeOvenMenu;
import mods.railcraft.world.level.block.CokeOvenBricksBlock;
import mods.railcraft.world.level.block.RailcraftBlocks;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntityTypes;
import mods.railcraft.world.level.block.entity.module.CokeOvenModule;
import mods.railcraft.world.level.block.entity.module.ModuleProvider;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class CokeOvenBlockEntity extends MultiblockBlockEntity<CokeOvenBlockEntity> {

  private static final Component MENU_TITLE =
      new TranslatableComponent("container.coke_oven");

  private static final MultiblockPattern PATTERN = Util.make(() -> {
    final var bricks = BlockPredicate.of(RailcraftBlocks.COKE_OVEN_BRICKS);

    final var topAndBottom = List.of(
        CharList.of('B', 'B', 'B'),
        CharList.of('B', 'B', 'B'),
        CharList.of('B', 'B', 'B'));

    return MultiblockPattern.builder(new BlockPos(2, 1, 2))
        .layer(topAndBottom)
        .layer(List.of(
            CharList.of('B', 'W', 'B'),
            CharList.of('W', 'A', 'W'),
            CharList.of('B', 'W', 'B')))
        .layer(topAndBottom)
        .predicate('B', bricks)
        .predicate('W', bricks)
        .predicate('A', BlockPredicate.AIR)
        .build();
  });

  private final CokeOvenModule module;

  public CokeOvenBlockEntity(BlockPos blockPos, BlockState blockState) {
    super(RailcraftBlockEntityTypes.COKE_OVEN.get(), blockPos, blockState,
        CokeOvenBlockEntity.class, PATTERN);
    this.module = this.moduleDispatcher.registerCapabilityModule("coke_oven",
        new CokeOvenModule(ModuleProvider.of(this)));
  }

  public CokeOvenModule getLogic() {
    return this.module;
  }

  @Override
  protected boolean isBlockEntity(char id) {
    return id == 'B' || id == 'W';
  }

  @Override
  protected void membershipChanged(@Nullable Membership<CokeOvenBlockEntity> membership) {
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

  @Override
  public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
    return new CokeOvenMenu(id, inventory, this);
  }

  @Override
  public Component getDisplayName() {
    return MENU_TITLE;
  }

  public static void serverTick(Level level, BlockPos blockPos, BlockState blockState,
      CokeOvenBlockEntity blockEntity) {

    blockEntity.serverTick();

    blockEntity.getMembership()
        .map(Membership::master)
        .ifPresent(master -> {
          boolean lit = master.module.isProcessing();
          if (lit != blockState.getValue(CokeOvenBricksBlock.LIT)) {
            level.setBlockAndUpdate(blockPos,
                blockState.setValue(CokeOvenBricksBlock.LIT, lit));
          }
        });

    if (blockEntity.isMaster()) {
      blockEntity.module.serverTick();
    }
  }

  @Override
  public void setRemoved() {
    if (this.getLevel().isClientSide()) {
      super.setRemoved();
      return; // do not run deletion clientside.
    }
    Containers.dropContents(this.getLevel(), this.getBlockPos(), this.module);
    super.setRemoved(); // at this point, the block itself is null
  }
}
