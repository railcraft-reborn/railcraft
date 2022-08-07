package mods.railcraft.util.container;

import mods.railcraft.util.container.manipulator.VanillaContainerManipulator;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class ContainerCopy implements Container, VanillaContainerManipulator {

    private final AdvancedContainer copy;

    public ContainerCopy(Container original) {
        this.copy = new AdvancedContainer(original.getContainerSize());
        for (int i = 0; i < original.getContainerSize(); i++) {
            var itemStack = original.getItem(i);
            if (!itemStack.isEmpty()) {
                copy.setItem(i, itemStack.copy());
            }
        }
    }

    @Override
    public Container getContainer() {
        return this;
    }

    @Override
    public int getContainerSize() {
        return copy.getContainerSize();
    }

    @Override
    public boolean isEmpty() {
        return copy.isEmpty();
    }

    @Override
    public ItemStack getItem(int slot) {
        return copy.getItem(slot);
    }

    @Override
    public ItemStack removeItem(int slot, int amount) {
        return copy.removeItem(slot, amount);
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        return copy.removeItemNoUpdate(slot);
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        copy.setItem(slot, stack);
    }

    @Override
    public void setChanged() {
        copy.setChanged();
    }

    @Override
    public boolean stillValid(Player player) {
        return copy.stillValid(player);
    }

    @Override
    public void clearContent() {
        copy.clearContent();
    }
}
