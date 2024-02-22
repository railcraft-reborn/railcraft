package mods.railcraft.world.item;

import java.util.List;
import org.jetbrains.annotations.Nullable;
import mods.railcraft.Translations;
import mods.railcraft.api.util.EnumUtil;
import mods.railcraft.network.PacketHandler;
import mods.railcraft.network.to_server.UpdateAuraByKeyMessage;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.StringRepresentable;
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

  public static Aura getAura(ItemStack itemStack) {
    if (itemStack.hasTag()) {
      return Aura.fromName(itemStack.getTag().getString("aura"));
    }
    return Aura.NONE;
  }

  public static Aura incrementAura(ItemStack itemStack) {
    var aura = getAura(itemStack).getNext();
    if (aura == Aura.TRACKING) {
      aura.getNext();
    }
    itemStack.getOrCreateTag().putString("aura", aura.getSerializedName());
    return aura;
  }

  public static void changeAuraByKey(LocalPlayer player) {
    var itemStack = player.getItemBySlot(EquipmentSlot.HEAD);
    if (itemStack.isEmpty()) {
      return;
    }
    var aura = incrementAura(itemStack);
    player.displayClientMessage(getDescriptionText(aura.getDisplayName(), false), true);
    PacketHandler.sendToServer(new UpdateAuraByKeyMessage(itemStack.getTag()));
  }

  public static boolean isGoggleAuraActive(Aura aura) {
    var player = Minecraft.getInstance().player;
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

  @Override
  public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
    var itemStack = player.getItemInHand(hand);
    if (!level.isClientSide()) {
      var aura = incrementAura(itemStack);
      player.displayClientMessage(getDescriptionText(aura.getDisplayName(), false), true);
    }
    return InteractionResultHolder.sidedSuccess(itemStack, level.isClientSide());
  }

  @Override
  public void appendHoverText(ItemStack itemStack, @Nullable Level level,
      List<Component> lines, TooltipFlag adv) {
    lines.add(getDescriptionText(getAura(itemStack).getDisplayName(), true));
    lines.add(Component.translatable(Translations.Tips.GOOGLES_DESC)
        .withStyle(ChatFormatting.GRAY));
  }

  public enum Aura implements StringRepresentable {

    NONE("none"),
    TRACKING("tracking"),
    TUNING("tuning"),
    SHUNTING("shunting"),
    SIGNALLING("signalling"),
    SURVEYING("surveying"),
    WORLDSPIKE("worldspike");

    private static final StringRepresentable.EnumCodec<Aura> CODEC =
        StringRepresentable.fromEnum(Aura::values);

    private final String name;

    Aura(String name) {
      this.name = name;
    }

    public MutableComponent getDisplayName() {
      return Component.translatable(this.getTranslationKey());
    }

    private String getTranslationKey() {
      return Translations.makeKey("tips", "googles.aura." + this.name);
    }

    @Override
    public String getSerializedName() {
      return this.name;
    }

    public Aura getNext() {
      return EnumUtil.next(this, values());
    }

    public static Aura fromName(String name) {
      return CODEC.byName(name, NONE);
    }
  }
}
