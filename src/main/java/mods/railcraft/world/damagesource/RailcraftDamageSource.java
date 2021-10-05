package mods.railcraft.world.damagesource;

import mods.railcraft.util.MiscTools;
import mods.railcraft.util.inventory.InvTools;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

/**
 *
 * @author CovertJaguar <https://www.railcraft.info/>
 */
public class RailcraftDamageSource extends DamageSource {

  public static final RailcraftDamageSource BORE = new RailcraftDamageSource("bore");
  public static final RailcraftDamageSource CRUSHER = new RailcraftDamageSource("crusher", 8);
  public static final RailcraftDamageSource ELECTRIC = new RailcraftDamageSource("electric");
  public static final RailcraftDamageSource STEAM = new RailcraftDamageSource("steam");
  public static final RailcraftDamageSource TRACK_ELECTRIC =
      new RailcraftDamageSource("track.electric");
  public static final RailcraftDamageSource TRAIN = new RailcraftDamageSource("train");
  public static final RailcraftDamageSource CREOSOTE = new RailcraftDamageSource("creosote");

  static {
    BORE.bypassArmor();
    ELECTRIC.bypassArmor();
    TRACK_ELECTRIC.bypassArmor();
    TRAIN.bypassArmor();
    CREOSOTE.bypassArmor();
  }

  private final int numMessages;

  private RailcraftDamageSource(String tag) {
    this(tag, 6);
  }

  private RailcraftDamageSource(String tag, int numMessages) {
    super(tag);
    this.numMessages = numMessages;
  }

  @Override
  public ITextComponent getLocalizedDeathMessage(LivingEntity entity) {
    String locTag =
        "death.railcraft." + this.msgId + "." + (MiscTools.RANDOM.nextInt(this.numMessages) + 1);
    return new TranslationTextComponent(locTag, entity.getName());
  }

  public static final EventHandler EVENT_HANDLER = new EventHandler();

  public static class EventHandler {

    @SubscribeEvent
    public void modifyDrops(LivingDropsEvent event) {
      if (event.getSource() == STEAM)
        for (ItemEntity entityItem : event.getDrops()) {
          ItemStack drop = entityItem.getItem();
          World level = event.getEntityLiving().level;
          ItemStack cooked = level.getRecipeManager()
              .getRecipeFor(IRecipeType.SMELTING, new Inventory(drop), level)
              .map(FurnaceRecipe::getResultItem)
              .orElse(ItemStack.EMPTY);
          if (!cooked.isEmpty() && MiscTools.RANDOM.nextDouble() < 0.5) {
            cooked = cooked.copy();
            InvTools.setSize(cooked, drop.getCount());
            entityItem.setItem(cooked);
          }
        }
    }
  }
}
