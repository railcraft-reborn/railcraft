package mods.railcraft.world.item;

import java.util.List;
import javax.annotation.Nullable;
import mods.railcraft.Translations.Tips;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

/**
 * @author CovertJaguar <https://www.railcraft.info>
 */
public class GogglesItem extends ArmorItem {

    public GogglesItem(Properties properties) {
        super(RailcraftArmorMaterial.GOGGLES, EquipmentSlot.HEAD, properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player,
        InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        if (!level.isClientSide()) {
            incrementAura(itemStack);
            Aura aura = getAura(itemStack);
            player.displayClientMessage(getDescriptionText(aura.getTranslation(), false), true);
        }
        return InteractionResultHolder.sidedSuccess(itemStack, level.isClientSide());
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level level,
        List<Component> lines, TooltipFlag adv) {
        lines.add(getDescriptionText(getAura(itemStack).getTranslation(), true));
        lines.add(Component.translatable(Tips.GOOGLES_DESC).withStyle(ChatFormatting.GRAY));
    }

    public static Aura getAura(ItemStack itemStack) {
        if (itemStack.hasTag()) {
            var tag = itemStack.getTag();
            if (tag.contains("aura")) {
                return Aura.values()[tag.getInt("aura")];
            }
        }
        return Aura.NONE;
    }

    private static void incrementAura(ItemStack itemStack) {
        Aura aura = getAura(itemStack).getNext();
        if (aura == Aura.TRACKING) {
            aura.getNext();
        }
        itemStack.getOrCreateTag().putInt("aura", aura.ordinal());
    }

    public static boolean isGoggleAuraActive(Player player, Aura aura) {
        var itemStack = player.getItemBySlot(EquipmentSlot.HEAD);
        return itemStack.getItem() instanceof GogglesItem && getAura(itemStack) == aura;
    }

    private static Component getDescriptionText(MutableComponent value, boolean tooltip) {
        var title = Component.translatable(Tips.GOOGLES_AURA);
        if(tooltip)
            title.withStyle(ChatFormatting.GRAY);
        return title.append(value.withStyle(ChatFormatting.DARK_PURPLE));
    }

    public enum Aura {

        NONE,
        TRACKING,
        TUNING,
        SHUNTING,
        SIGNALLING,
        SURVEYING,
        WORLDSPIKE;

        public MutableComponent getTranslation() {
            return Component.translatable(switch (this.ordinal()) {
                case 0 -> Tips.GOOGLES_AURA_NONE;
                case 1 -> Tips.GOOGLES_AURA_TRACKING;
                case 2 -> Tips.GOOGLES_AURA_TUNING;
                case 3 -> Tips.GOOGLES_AURA_SHUNTING;
                case 4 -> Tips.GOOGLES_AURA_SIGNALLING;
                case 5 -> Tips.GOOGLES_AURA_SURVEYING;
                case 6 -> Tips.GOOGLES_AURA_WORLDSPIKE;
                default -> "translation.not.implemented";
            });
        }

        public Aura getNext() {
            return values()[(this.ordinal() + 1) % values().length];
        }
    }
}
