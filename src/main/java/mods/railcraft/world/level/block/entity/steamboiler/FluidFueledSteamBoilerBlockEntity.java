package mods.railcraft.world.level.block.entity.steamboiler;

import mods.railcraft.client.Translations;
import mods.railcraft.world.inventory.FluidFueledSteamBoilerMenu;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntityTypes;
import mods.railcraft.world.module.FluidFueledSteamBoilerModule;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.state.BlockState;

public class FluidFueledSteamBoilerBlockEntity extends SteamBoilerBlockEntity {

  public FluidFueledSteamBoilerBlockEntity(BlockPos blockPos, BlockState blockState) {
    super(RailcraftBlockEntityTypes.FLUID_FUELED_STEAM_BOILER.get(), blockPos, blockState);
    this.moduleDispatcher.registerModule("fluid_fueled_steam_boiler",
        new FluidFueledSteamBoilerModule(this));
  }

  @Override
  public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
    return new FluidFueledSteamBoilerMenu(id, inventory, this);
  }

  @Override
  public Component getDisplayName() {
    return Component.translatable(Translations.Container.FLUID_FUELED_STEAM_BOILER);
  }
}
