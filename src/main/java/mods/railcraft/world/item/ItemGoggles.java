package mods.railcraft.world.item;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

/**
 * @author CovertJaguar <http://www.railcraft.info>
 */
public class ItemGoggles extends ArmorItem {

  public ItemGoggles(Properties properties) {
    super(RailcraftArmorMaterial.GOGGLES, EquipmentSlotType.HEAD, properties);
  }

  @Override
  public ActionResult<ItemStack> use(World level, PlayerEntity player, Hand hand) {
    ItemStack itemStack = player.getItemInHand(hand);
    if (!level.isClientSide()) {
      incrementAura(itemStack);
      Aura aura = getAura(itemStack);
      player.displayClientMessage(aura.getDescriptionText(), false);
    }
    return ActionResult.sidedSuccess(itemStack, level.isClientSide());
  }

  @Override
  public void appendHoverText(ItemStack itemStack, @Nullable World level,
      List<ITextComponent> lines, ITooltipFlag adv) {
    lines.add(getAura(itemStack).getDescriptionText());
    lines.add(new TranslationTextComponent("gui.railcraft.goggles.tips"));
  }

  public static Aura getAura(ItemStack itemStack) {
    if (itemStack.getItem() instanceof ItemGoggles) {
      Optional.ofNullable(itemStack.getTag())
          .filter(tag -> tag.contains("aura", Constants.NBT.TAG_STRING))
          .map(tag -> tag.getString("aura"))
          .flatMap(Aura::getByName)
          .orElse(Aura.NONE);
    }
    return Aura.NONE;
  }

  public static void incrementAura(ItemStack itemStack) {
    if (itemStack.getItem() instanceof ItemGoggles) {
      Aura aura = Optional.of(itemStack.getOrCreateTag())
          .filter(tag -> tag.contains("aura", Constants.NBT.TAG_STRING))
          .map(tag -> tag.getString("aura"))
          .flatMap(Aura::getByName)
          .orElse(Aura.NONE)
          .getNext();
      if (aura == Aura.TRACKING)
        aura.getNext();
    }
  }

  public enum Aura implements IStringSerializable {

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
    private final ITextComponent displayName;

    private Aura(String name) {
      this.name = name;
      this.displayName = new TranslationTextComponent("gui.railcraft.goggles.aura." + name);
    }

    public ITextComponent getDisplayName() {
      return this.displayName;
    }

    public String getName() {
      return this.name;
    }

    @Override
    public String getSerializedName() {
      return this.name;
    }

    public ITextComponent getDescriptionText() {
      return new TranslationTextComponent("gui.railcraft.goggles.mode",
          this.displayName.copy().withStyle(TextFormatting.DARK_PURPLE));
    }

    public Aura getNext() {
      return values()[this.ordinal() % values().length];
    }

    public static Optional<Aura> getByName(String name) {
      return Optional.ofNullable(byName.get(name));
    }
  }
}
