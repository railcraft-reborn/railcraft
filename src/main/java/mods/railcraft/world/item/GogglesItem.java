package mods.railcraft.world.item;

import java.util.List;
import org.jetbrains.annotations.Nullable;
import mods.railcraft.Translations;
import mods.railcraft.api.util.EnumUtil;
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

public class GogglesItem extends ArmorItem {

  public GogglesItem(Properties properties) {
    super(RailcraftArmorMaterial.GOGGLES, Type.HELMET, properties);
  }

  @Override
  public InteractionResultHolder<ItemStack> use(Level level, Player player,
      InteractionHand hand) {
    ItemStack itemStack = player.getItemInHand(hand);
    if (!level.isClientSide()) {
      incrementAura(itemStack);
      Aura aura = getAura(itemStack);
      player.displayClientMessage(getDescriptionText(aura.getDisplayName(), false), true);
    }
    return InteractionResultHolder.sidedSuccess(itemStack, level.isClientSide());
  }

  @Override
  public void appendHoverText(ItemStack itemStack, @Nullable Level level,
      List<Component> lines, TooltipFlag adv) {
    lines.add(getDescriptionText(getAura(itemStack).getDisplayName(), true));
    lines
        .add(Component.translatable(Translations.Tips.GOOGLES_DESC).withStyle(ChatFormatting.GRAY));
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
    var title = Component.translatable(Translations.Tips.GOOGLES_AURA);
    if (tooltip) {
      title.withStyle(ChatFormatting.GRAY);
    }
    return title.append(" ").append(value.withStyle(ChatFormatting.DARK_PURPLE));
  }

  public enum Aura {

    NONE(Translations.Tips.GOOGLES_AURA_NONE),
    TRACKING(Translations.Tips.GOOGLES_AURA_TRACKING),
    TUNING(Translations.Tips.GOOGLES_AURA_TUNING),
    SHUNTING(Translations.Tips.GOOGLES_AURA_SHUNTING),
    SIGNALLING(Translations.Tips.GOOGLES_AURA_SIGNALLING),
    SURVEYING(Translations.Tips.GOOGLES_AURA_SURVEYING),
    WORLDSPIKE(Translations.Tips.GOOGLES_AURA_WORLDSPIKE);

    private final String translationKey;

    private Aura(String translationKey) {
      this.translationKey = translationKey;
    }

    public MutableComponent getDisplayName() {
      return Component.translatable(this.translationKey);
    }

    public Aura getNext() {
      return EnumUtil.next(this, values());
    }
  }
}
