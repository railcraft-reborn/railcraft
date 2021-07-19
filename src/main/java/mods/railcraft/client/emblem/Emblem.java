package mods.railcraft.client.emblem;

import net.minecraft.item.Rarity;

/**
 *
 * @author CovertJaguar <http://www.railcraft.info/>
 */
public class Emblem {

  public final String textureFile;
  public final String identifier;
  public final String displayName;
  public final Rarity rarity;
  public final boolean hasEffect;

  public Emblem(String ident, String text, String display, Rarity rarity, boolean hasEffect) {
    this.identifier = ident;
    this.textureFile = text;
    this.displayName = display;
    this.rarity = rarity;
    this.hasEffect = hasEffect;
  }

  @Override
  public String toString() {
    return String.format("Emblem - \"%s\" - %s", displayName, identifier);
  }
}
