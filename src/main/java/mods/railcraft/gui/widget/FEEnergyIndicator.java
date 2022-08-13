package mods.railcraft.gui.widget;

import java.util.ArrayList;
import java.util.List;
import mods.railcraft.util.HumanReadableNumberFormatter;
import net.minecraft.network.chat.Component;
import net.minecraftforge.energy.IEnergyStorage;

public class FEEnergyIndicator implements Gauge {

    private int charge;
    private final IEnergyStorage battery;

    private final List<Component> tooltip = new ArrayList<>(1);

    public FEEnergyIndicator(IEnergyStorage battery) {
        this.battery = battery;
    }

    @Override
    public void refresh() {
        int capacity = this.battery.getMaxEnergyStored();
        int current = Math.min(this.charge, capacity);
        this.tooltip.clear();

        var currentFormatter = HumanReadableNumberFormatter.format(current) + "FE";
        var capacityFormatter = HumanReadableNumberFormatter.format(capacity) + "FE";
        this.tooltip.add(Component.literal(currentFormatter + "/" + capacityFormatter));
    }

    @Override
    public List<Component> getTooltip() {
        return this.tooltip;
    }

    @Override
    public float getMeasurement() {
        int capacity = this.battery.getMaxEnergyStored();
        if (capacity <= 0) {
            return 0.0F;
        }
        return (float)this.charge / (float)capacity;
    }

    @Override
    public float getServerValue() {
        return (float)this.battery.getEnergyStored();
    }

    @Override
    public float getClientValue() {
        return this.charge;
    }

    @Override
    public void setClientValue(float value) {
        this.charge = (int)value;
    }
}
