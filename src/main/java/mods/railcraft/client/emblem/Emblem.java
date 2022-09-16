package mods.railcraft.client.emblem;

import net.minecraft.world.item.Rarity;

/**
 *
 * @author CovertJaguar <https://www.railcraft.info/>
 */
public record Emblem(String id, String text, String displayName, Rarity rarity, boolean hasEffect) {}
