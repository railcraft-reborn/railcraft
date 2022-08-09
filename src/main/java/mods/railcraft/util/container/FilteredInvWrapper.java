package mods.railcraft.util.container;

import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.wrapper.InvWrapper;
import org.jetbrains.annotations.NotNull;

public class FilteredInvWrapper extends InvWrapper {

    private final boolean allowInsert;
    private final boolean allowExtract;

    public FilteredInvWrapper(Container inv, boolean allowInsert, boolean allowExtract) {
        super(inv);
        this.allowInsert = allowInsert;
        this.allowExtract = allowExtract;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        var that = (FilteredInvWrapper) o;

        return super.equals(o) && that.allowInsert == allowInsert && that.allowExtract
            == allowExtract;
    }

    @Override
    @NotNull
    public ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
        if(!allowInsert)
            return ItemStack.EMPTY;
        return super.insertItem(slot, stack, simulate);
    }

    @Override
    @NotNull
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        if(!allowExtract)
            return ItemStack.EMPTY;
        return super.extractItem(slot, amount, simulate);
    }

    @Override
    public void setStackInSlot(int slot, @NotNull ItemStack stack) {
        if(!allowInsert)
            return;
        super.setStackInSlot(slot, stack);
    }

    @Override
    public boolean isItemValid(int slot, @NotNull ItemStack stack) {
        if(!allowInsert)
            return false;
        return super.isItemValid(slot, stack);
    }
}
