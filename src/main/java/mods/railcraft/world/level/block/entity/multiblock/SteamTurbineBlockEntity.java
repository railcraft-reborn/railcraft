package mods.railcraft.world.level.block.entity.multiblock;

import java.util.Collection;
import java.util.List;
import it.unimi.dsi.fastutil.chars.CharList;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class SteamTurbineBlockEntity extends MultiblockBlockEntity<SteamTurbineBlockEntity> {

  private static final Collection<MultiblockPattern> PATTERNS;

  static {
    var topAndBottom = List.of(
        CharList.of('O', 'O', 'O', 'O', 'O'),
        CharList.of('O', 'O', 'O', 'O', 'O'),
        CharList.of('O', 'O', 'O', 'O', 'O'),
        CharList.of('O', 'O', 'O', 'O', 'O'));
    var topAndBottomSmall = List.of(
        CharList.of('O', 'O', 'O', 'O'),
        CharList.of('O', 'O', 'O', 'O'),
        CharList.of('O', 'O', 'O', 'O'),
        CharList.of('O', 'O', 'O', 'O'),
        CharList.of('O', 'O', 'O', 'O'));
    PATTERNS = List.of(
        MultiblockPattern.builder(BlockPos.ZERO)
            .layer(topAndBottomSmall)
            .layer(List.of(
                CharList.of('O', 'O', 'O', 'O'),
                CharList.of('O', 'B', 'B', 'O'),
                CharList.of('O', 'B', 'B', 'O'),
                CharList.of('O', 'B', 'B', 'O'),
                CharList.of('O', 'O', 'O', 'O')))
            .layer(List.of(
                CharList.of('O', 'O', 'O', 'O'),
                CharList.of('O', 'B', 'B', 'O'),
                CharList.of('O', 'W', 'W', 'O'),
                CharList.of('O', 'B', 'B', 'O'),
                CharList.of('O', 'O', 'O', 'O')))
            .layer(topAndBottomSmall)
            .build(),
        MultiblockPattern.builder(BlockPos.ZERO)
            .layer(topAndBottom)
            .layer(List.of(
                CharList.of('O', 'O', 'O', 'O', 'O'),
                CharList.of('O', 'B', 'B', 'B', 'O'),
                CharList.of('O', 'B', 'B', 'B', 'O'),
                CharList.of('O', 'O', 'O', 'O', 'O')))
            .layer(List.of(
                CharList.of('O', 'O', 'O', 'O', 'O'),
                CharList.of('O', 'B', 'W', 'B', 'O'),
                CharList.of('O', 'B', 'W', 'B', 'O'),
                CharList.of('O', 'O', 'O', 'O', 'O')))
            .layer(topAndBottom)
            .build());
  }

  public SteamTurbineBlockEntity(BlockPos blockPos, BlockState blockState) {
    super(RailcraftBlockEntityTypes.STEAM_TURBINE.get(), blockPos, blockState,
        SteamTurbineBlockEntity.class, PATTERNS);
  }

  public static void serverTick(Level level, BlockPos blockPos, BlockState blockState,
      SteamTurbineBlockEntity blockEntity) {
    blockEntity.serverTick();
    blockEntity.moduleDispatcher.serverTick();
  }

  @Override
  public AbstractContainerMenu createMenu(int containerId, Inventory inventory, Player player) {
    return null;
  }

  @Override
  protected boolean isBlockEntity(char marker) {
    return true;
  }

  @Override
  protected void membershipChanged(Membership<SteamTurbineBlockEntity> membership) {

  }
}
