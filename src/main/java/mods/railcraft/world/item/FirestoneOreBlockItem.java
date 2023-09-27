package mods.railcraft.world.item;

import java.util.List;
import org.jetbrains.annotations.Nullable;
import mods.railcraft.Translations;
import mods.railcraft.world.level.block.RailcraftBlocks;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;

public class FirestoneOreBlockItem extends BlockItem {

  public FirestoneOreBlockItem(Properties properties) {
    super(RailcraftBlocks.FIRESTONE_ORE.get(), properties);
  }

  @Override
  public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId,
      boolean isSelected) {
    if (level.getGameRules().getBoolean(GameRules.RULE_DOFIRETICK)
        && entity instanceof Player player
        && level.getRandom().nextInt(12) % 4 == 0) {
      FirestoneItem.trySpawnFire(player.level(), player.blockPosition(), stack, player);
    }
  }

  @Override
  public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip,
      TooltipFlag flag) {
    tooltip.add(Component.translatable(Translations.Tips.FIRESTONE_ORE)
        .withStyle(ChatFormatting.GRAY));
  }
}
