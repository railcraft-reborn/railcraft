package mods.railcraft.world.item;

import java.util.function.Consumer;
import org.jspecify.annotations.Nullable;
import mods.railcraft.Translations;
import mods.railcraft.world.level.block.RailcraftBlocks;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.gamerules.GameRules;

public class FirestoneOreBlockItem extends BlockItem {

  public FirestoneOreBlockItem(Properties properties) {
    super(RailcraftBlocks.FIRESTONE_ORE.get(), properties);
  }

  @Override
  public void inventoryTick(ItemStack stack, ServerLevel level, Entity entity,
      @Nullable EquipmentSlot slot) {
    if (level.getGameRules().get(GameRules.FIRE_SPREAD_RADIUS_AROUND_PLAYER) != 0
        && entity instanceof Player player
        && level.getRandom().nextInt(12) % 4 == 0) {
      FirestoneItem.trySpawnFire(level, player.blockPosition(), stack, player);
    }
  }

  @Override
  public void appendHoverText(ItemStack stack, TooltipContext context,
      TooltipDisplay tooltipDisplay, Consumer<Component> tooltipAdder, TooltipFlag flag) {
    tooltipAdder.accept(Component.translatable(Translations.Tips.FIRESTONE_ORE)
        .withStyle(ChatFormatting.GRAY));
  }
}
