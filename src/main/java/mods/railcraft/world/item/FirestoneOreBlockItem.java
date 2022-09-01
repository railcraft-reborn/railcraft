package mods.railcraft.world.item;

import mods.railcraft.world.level.block.RailcraftBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class FirestoneOreBlockItem extends BlockItem {

    public FirestoneOreBlockItem(Properties properties) {
        super(RailcraftBlocks.FIRESTONE_ORE.get(), properties);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId,
        boolean isSelected) {
        if (entity instanceof Player player) {
            if (level.getRandom().nextInt(12) % 4 == 0) {
                var pos = new BlockPos(player.position());
                FirestoneItem.trySpawnFire(player.level, pos, stack, player);
            }
        }
    }
}
