package mods.railcraft.world.level.block.entity.steamboiler;

import mods.railcraft.client.Translations;
import mods.railcraft.world.inventory.SolidFueledSteamBoilerMenu;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntityTypes;
import mods.railcraft.world.module.SolidFueledSteamBoilerModule;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.state.BlockState;

public class SolidFueledSteamBoilerBlockEntity extends SteamBoilerBlockEntity {

  public SolidFueledSteamBoilerBlockEntity(BlockPos blockPos, BlockState blockState) {
    super(RailcraftBlockEntityTypes.SOLID_FUELED_STEAM_BOILER.get(), blockPos, blockState);
    this.moduleDispatcher.registerModule("solid_fueled_steam_boiler",
        new SolidFueledSteamBoilerModule(this));
  }

  @Override
  public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
    return new SolidFueledSteamBoilerMenu(id, inventory, this);
  }

  @Override
  public Component getDisplayName() {
    return Component.translatable(Translations.Container.SOLID_FUELED_STEAM_BOILER);
  }
}
