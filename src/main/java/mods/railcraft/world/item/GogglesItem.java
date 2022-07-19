package mods.railcraft.world.item;

import net.minecraft.ChatFormatting;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import net.minecraft.world.item.Item.Properties;

/**
 * @author CovertJaguar <https://www.railcraft.info>
 */
public class GogglesItem extends ArmorItem {

  public GogglesItem(Properties properties) {
    super(RailcraftArmorMaterial.GOGGLES, EquipmentSlot.HEAD, properties);
  }

  @Override
  public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
    ItemStack itemStack = player.getItemInHand(hand);
    if (!level.isClientSide()) {
      incrementAura(itemStack);
      Aura aura = getAura(itemStack);
      player.displayClientMessage(getDescriptionText(aura.getDisplayName()), true);
    }
    return InteractionResultHolder.sidedSuccess(itemStack, level.isClientSide());
  }

  @Override
  public void appendHoverText(ItemStack itemStack, @Nullable Level level,
      List<Component> lines, TooltipFlag adv) {
    lines.add(getDescriptionText(getAura(itemStack).getDisplayName()));
    lines.add(Component.translatable("goggles.description"));
  }

  public static Component getDescriptionText(Component displayName) {
    return Component.translatable("goggles.aura",
        displayName.copy().withStyle(ChatFormatting.DARK_PURPLE));
  }

  public static Aura getAura(ItemStack itemStack) {
    return Optional.ofNullable(itemStack.getTag())
        .filter(tag -> tag.contains("aura", Tag.TAG_STRING))
        .map(tag -> tag.getString("aura"))
        .flatMap(Aura::getByName)
        .orElse(Aura.NONE);
  }

  public static void incrementAura(ItemStack itemStack) {
    Aura aura = getAura(itemStack).getNext();
    if (aura == Aura.TRACKING) {
      aura.getNext();
    }
    itemStack.getOrCreateTag().putString("aura", aura.getSerializedName());
  }

  public enum Aura implements StringRepresentable {

    NONE("none"),
    TRACKING("tracking"),
    TUNING("tuning"),
    SHUNTING("shunting"),
    SIGNALLING("signalling"),
    SURVEYING("surveying"),
    WORLDSPIKE("worldspike");

    private static final Map<String, Aura> byName =
        Arrays.stream(values()).collect(Collectors.toMap(Aura::getName, Function.identity()));

    private String name;
    private final Component displayName;

    private Aura(String name) {
      this.name = name;
      this.displayName = Component.translatable("goggles.aura." + name);
    }

    public Component getDisplayName() {
      return this.displayName;
    }

    public String getName() {
      return this.name;
    }

    @Override
    public String getSerializedName() {
      return this.name;
    }

    public Aura getNext() {
      return values()[(this.ordinal() + 1) % values().length];
    }

    public static Optional<Aura> getByName(String name) {
      return Optional.ofNullable(byName.get(name));
    }
  }
}
