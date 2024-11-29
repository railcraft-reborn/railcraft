package mods.railcraft.world.item;

import java.util.EnumMap;
import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.tags.RailcraftTags;
import net.minecraft.Util;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorType;
import net.neoforged.neoforge.common.Tags;

public interface RailcraftArmorMaterials {

  ArmorMaterial OVERALLS = new ArmorMaterial(5, Util.make(new EnumMap<>(ArmorType.class), defense -> {
    defense.put(ArmorType.BOOTS, 1);
    defense.put(ArmorType.LEGGINGS, 2);
    defense.put(ArmorType.CHESTPLATE, 3);
    defense.put(ArmorType.HELMET, 1);
    defense.put(ArmorType.BODY, 3);
  }), 8, SoundEvents.ARMOR_EQUIP_LEATHER, 0, 0, Tags.Items.DYED_BLUE, RailcraftConstants.rl("overalls"));

  ArmorMaterial GOGGLES = new ArmorMaterial(5, Util.make(new EnumMap<>(ArmorType.class), defense -> {
    defense.put(ArmorType.BOOTS, 1);
    defense.put(ArmorType.LEGGINGS, 2);
    defense.put(ArmorType.CHESTPLATE, 3);
    defense.put(ArmorType.HELMET, 1);
    defense.put(ArmorType.BODY, 3);
  }), 8, SoundEvents.ARMOR_EQUIP_LEATHER, 0, 0, RailcraftTags.Items.STEEL_INGOT, RailcraftConstants.rl("goggles"));

  ArmorMaterial STEEL = new ArmorMaterial(15, Util.make(new EnumMap<>(ArmorType.class), defense -> {
    defense.put(ArmorType.BOOTS, 2);
    defense.put(ArmorType.LEGGINGS, 5);
    defense.put(ArmorType.CHESTPLATE, 6);
    defense.put(ArmorType.HELMET, 2);
    defense.put(ArmorType.BODY, 5);
  }), 8, SoundEvents.ARMOR_EQUIP_IRON, 0.8F, 0, RailcraftTags.Items.STEEL_INGOT, RailcraftConstants.rl("steel"));
}
