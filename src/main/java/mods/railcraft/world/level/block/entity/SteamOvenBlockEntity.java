package mods.railcraft.world.level.block.entity;

import java.util.List;
import org.jetbrains.annotations.Nullable;
import it.unimi.dsi.fastutil.chars.CharList;
import mods.railcraft.Translations.Container;
import mods.railcraft.world.inventory.SteamOvenMenu;
import mods.railcraft.world.level.block.RailcraftBlocks;
import mods.railcraft.world.level.block.SteamOvenBlock;
import mods.railcraft.world.level.block.entity.multiblock.BlockPredicate;
import mods.railcraft.world.level.block.entity.multiblock.MultiblockBlockEntity;
import mods.railcraft.world.level.block.entity.multiblock.MultiblockPattern;
import mods.railcraft.world.module.SteamOvenModule;
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
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.items.IItemHandler;

public class SteamOvenBlockEntity extends MultiblockBlockEntity<SteamOvenBlockEntity, Void> {

  private static final MultiblockPattern<Void> pattern = Util.make(() -> {
    final var bricks = BlockPredicate.of(RailcraftBlocks.STEAM_OVEN);

    return MultiblockPattern.<Void>builder(BlockPos.ZERO)
        .layer(List.of(
            CharList.of('A', 'B'),
            CharList.of('C', 'D')))
        .layer(List.of(
            CharList.of('E', 'F'),
            CharList.of('G', 'H')))
        .predicate('A', bricks)
        .predicate('B', bricks)
        .predicate('C', bricks)
        .predicate('D', bricks)
        .predicate('E', bricks)
        .predicate('F', bricks)
        .predicate('G', bricks)
        .predicate('H', bricks)
        .build();
  });

  private final SteamOvenModule steamOvenModule;

  public SteamOvenBlockEntity(BlockPos blockPos, BlockState blockState) {
    super(RailcraftBlockEntityTypes.STEAM_OVEN.get(), blockPos, blockState,
        SteamOvenBlockEntity.class, List.of(pattern));
    this.steamOvenModule = this.moduleDispatcher.registerModule("steam_oven",
        new SteamOvenModule(this));
  }

  public static void serverTick(Level level, BlockPos blockPos, BlockState blockState,
      SteamOvenBlockEntity blockEntity) {
    blockEntity.serverTick();
    blockEntity.moduleDispatcher.serverTick();
  }

  public SteamOvenModule getSteamOvenModule() {
    return this.steamOvenModule;
  }

  @Override
  protected boolean isBlockEntity(MultiblockPattern.Element element) {
    return true;
  }

  @Override
  protected void membershipChanged(@Nullable Membership<SteamOvenBlockEntity> membership) {
    if (membership == null) {
      this.level.setBlockAndUpdate(this.getBlockPos(),
          this.getBlockState()
              .setValue(SteamOvenBlock.TYPE, SteamOvenBlock.Type.DEFAULT));
      Containers.dropContents(this.level, this.getBlockPos(), this.steamOvenModule);
      return;
    }

    var facing = this.getBlockState().getValue(SteamOvenBlock.FACING);
    var type = switch (facing) {
      case SOUTH -> {
        switch (membership.patternElement().marker()) {
          case 'A', 'C': yield SteamOvenBlock.Type.DOOR_TOP_LEFT;
          case 'B', 'D': yield SteamOvenBlock.Type.DOOR_TOP_RIGHT;
          case 'E', 'G': yield SteamOvenBlock.Type.DOOR_BOTTOM_LEFT;
          case 'F', 'H': yield SteamOvenBlock.Type.DOOR_BOTTOM_RIGHT;
          default: yield SteamOvenBlock.Type.DEFAULT;
        }
      }
      case NORTH -> {
        switch (membership.patternElement().marker()) {
          case 'A', 'C': yield SteamOvenBlock.Type.DOOR_TOP_RIGHT;
          case 'B', 'D': yield SteamOvenBlock.Type.DOOR_TOP_LEFT;
          case 'E', 'G': yield SteamOvenBlock.Type.DOOR_BOTTOM_RIGHT;
          case 'F', 'H': yield SteamOvenBlock.Type.DOOR_BOTTOM_LEFT;
          default: yield SteamOvenBlock.Type.DEFAULT;
        }
      }
      case EAST -> {
        switch (membership.patternElement().marker()) {
          case 'A', 'B': yield SteamOvenBlock.Type.DOOR_TOP_RIGHT;
          case 'C', 'D': yield SteamOvenBlock.Type.DOOR_TOP_LEFT;
          case 'E', 'F': yield SteamOvenBlock.Type.DOOR_BOTTOM_RIGHT;
          case 'G', 'H': yield SteamOvenBlock.Type.DOOR_BOTTOM_LEFT;
          default: yield SteamOvenBlock.Type.DEFAULT;
        }
      }
      case WEST -> {
        switch (membership.patternElement().marker()) {
          case 'A', 'B': yield SteamOvenBlock.Type.DOOR_TOP_LEFT;
          case 'C', 'D': yield SteamOvenBlock.Type.DOOR_TOP_RIGHT;
          case 'E', 'F': yield SteamOvenBlock.Type.DOOR_BOTTOM_LEFT;
          case 'G', 'H': yield SteamOvenBlock.Type.DOOR_BOTTOM_RIGHT;
          default: yield SteamOvenBlock.Type.DEFAULT;
        }
      }
      default -> SteamOvenBlock.Type.DEFAULT;
    };

    this.level.setBlockAndUpdate(this.getBlockPos(),
        this.getBlockState()
            .setValue(SteamOvenBlock.TYPE, type));
  }

  @Override
  public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
    return new SteamOvenMenu(id, inventory, this);
  }

  @Override
  public Component getDisplayName() {
    return Component.translatable(Container.STEAM_OVEN);
  }

  public IItemHandler getItemCap(Direction side) {
    var masterModule = this.getMasterBlockEntity()
        .map(SteamOvenBlockEntity::getSteamOvenModule);
    return masterModule
        .map(SteamOvenModule::getItemHandler)
        .orElse(null);
  }

  public IFluidHandler getFluidCap(Direction side) {
    var masterModule = this.getMasterBlockEntity()
        .map(SteamOvenBlockEntity::getSteamOvenModule);
    return masterModule
        .map(SteamOvenModule::getSteamTank)
        .orElse(null);
  }
}
