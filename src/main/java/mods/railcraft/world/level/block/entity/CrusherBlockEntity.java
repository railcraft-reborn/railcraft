package mods.railcraft.world.level.block.entity;

import it.unimi.dsi.fastutil.chars.CharList;
import java.util.List;
import javax.annotation.Nullable;
import mods.railcraft.Translations.Container;
import mods.railcraft.world.inventory.CrusherMenu;
import mods.railcraft.world.level.block.CrusherMultiblockBlock;
import mods.railcraft.world.level.block.CrusherMultiblockBlock.Type;
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
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.items.CapabilityItemHandler;

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

    private final CrusherModule crusherModule;

    public CrusherBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(RailcraftBlockEntityTypes.CRUSHER.get(), blockPos, blockState,
            CrusherBlockEntity.class, List.of(pattern, rotatedPattern));
        this.crusherModule = this.moduleDispatcher.registerModule("crusher",
            new CrusherModule(this));
    }

    public static void serverTick(Level level, BlockPos blockPos, BlockState blockState,
        CrusherBlockEntity blockEntity) {

        blockEntity.serverTick();
        blockEntity.moduleDispatcher.serverTick();
        if (blockEntity.isMaster()) {
            blockEntity.crusherModule.serverTick();
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
            this.level.setBlockAndUpdate(this.getBlockPos(),
                this.getBlockState()
                    .setValue(CrusherMultiblockBlock.TYPE, Type.NONE)
                    .setValue(CrusherMultiblockBlock.ROTATED, false)
                    .setValue(CrusherMultiblockBlock.OUTPUT, false));
            Containers.dropContents(this.level, this.getBlockPos(), this.crusherModule);
            return;
        }

        var type = switch (membership.patternElement().marker()) {
            case '0' -> Type.NORTH_WEST;
            case '1' -> Type.NORTH;
            case '2' -> Type.NORTH_EAST;
            case '3' -> Type.SOUTH_EAST;
            case '4' -> Type.SOUTH;
            case '5' -> Type.SOUTH_WEST;
            default -> Type.NONE;
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
        var masterModule = this.getMembership()
            .map(Membership::master)
            .map(CrusherBlockEntity::getCrusherModule);
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return masterModule
                .map(m -> {
                    if (this.getBlockState().getValue(CrusherMultiblockBlock.OUTPUT)) {
                        return m.getOutputHandler();
                    }
                    return m.getInputHandler();
                })
                .<LazyOptional<T>>map(LazyOptional::cast)
                .orElse(LazyOptional.empty());
        }
        if (cap == CapabilityEnergy.ENERGY) {
            return masterModule
                .map(CrusherModule::getEnergyHandler)
                .<LazyOptional<T>>map(LazyOptional::cast)
                .orElse(LazyOptional.empty());
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        this.crusherModule.invalidateCaps();
    }
}
