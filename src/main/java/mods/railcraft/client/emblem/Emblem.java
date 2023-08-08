package mods.railcraft.client.emblem;

import net.minecraft.world.item.Rarity;

public record Emblem(String id, String text, String displayName, Rarity rarity, boolean hasEffect) {}
