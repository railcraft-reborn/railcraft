package mods.railcraft.world.level.block.entity;

import it.unimi.dsi.fastutil.chars.CharList;
import java.util.List;
import javax.annotation.Nullable;
import mods.railcraft.Translations.Container;
import mods.railcraft.world.level.block.RailcraftBlocks;
import mods.railcraft.world.level.block.entity.multiblock.BlockPredicate;
import mods.railcraft.world.level.block.entity.multiblock.MultiblockBlockEntity;
import mods.railcraft.world.level.block.entity.multiblock.MultiblockPattern;
import mods.railcraft.world.module.CrusherModule;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class CrusherBlockEntity extends MultiblockBlockEntity<CrusherBlockEntity, Void> {

  private static final MultiblockPattern<Void> PATTERN = Util.make(() -> {
    final var bricks = BlockPredicate.of(RailcraftBlocks.CRUSHER);

    final var pattern = List.of(
        CharList.of('A', 'A', 'A'),
        CharList.of('A', 'A', 'A'));

    return MultiblockPattern.<Void>builder(2, 1, 2)
        .layer(pattern)
        .layer(pattern)
        .layer(pattern)
        .predicate('A', bricks)
        .build();
  });

  private final CrusherModule crusherModule;

  public CrusherBlockEntity(BlockPos blockPos, BlockState blockState) {
    super(RailcraftBlockEntityTypes.CRUSHER.get(), blockPos, blockState,
        CrusherBlockEntity.class, PATTERN);
    this.crusherModule = this.moduleDispatcher.registerModule("crusher",
        new CrusherModule(this));
  }

  public CrusherModule getCrusherModule() {
    return this.crusherModule;
  }

  @Override
  protected boolean isBlockEntity(MultiblockPattern.Element element) {
    return element.marker() == 'A';
  }

  @Override
  protected void membershipChanged(@Nullable Membership<CrusherBlockEntity> membership) {
    if (membership == null) {
      Containers.dropContents(this.level, this.getBlockPos(), this.crusherModule);
    }
  }

  @Override
  public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
    return null; //new CokeOvenMenu(id, inventory, this);
  }

  @Override
  public Component getDisplayName() {
    return Component.translatable(Container.CRUSHER);
  }

  public static void serverTick(Level level, BlockPos blockPos, BlockState blockState,
      CrusherBlockEntity blockEntity) {

    blockEntity.serverTick();

    blockEntity.moduleDispatcher.serverTick();

    /*blockEntity.getMembership()
        .map(Membership::master)
        .ifPresent(master -> {
          var lit = master.crusherModule.isProcessing();
          if (lit != blockState.getValue(CokeOvenBricksBlock.LIT)) {
            level.setBlockAndUpdate(blockPos,
                blockState.setValue(CokeOvenBricksBlock.LIT, lit));
          }
        });
*/
    if (blockEntity.isMaster()) {
      blockEntity.crusherModule.serverTick();
    }
  }
}
