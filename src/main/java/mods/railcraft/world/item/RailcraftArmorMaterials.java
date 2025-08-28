package mods.railcraft.world.item;

import java.util.Map;
import com.google.common.collect.Maps;
import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.tags.RailcraftTags;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.world.item.equipment.EquipmentAssets;
import net.neoforged.neoforge.common.Tags;

public interface RailcraftArmorMaterials {

  ArmorMaterial OVERALLS = new ArmorMaterial(5, makeDefense(1, 2, 3, 1, 3), 8,
      SoundEvents.ARMOR_EQUIP_LEATHER, 0, 0, Tags.Items.DYED_BLUE,
      ResourceKey.create(EquipmentAssets.ROOT_ID, RailcraftConstants.rl("overalls")));

  ArmorMaterial GOGGLES = new ArmorMaterial(5, makeDefense(1, 2, 3, 1, 3), 8,
      SoundEvents.ARMOR_EQUIP_LEATHER, 0, 0, RailcraftTags.Items.STEEL_INGOT,
      ResourceKey.create(EquipmentAssets.ROOT_ID, RailcraftConstants.rl("goggles")));

  ArmorMaterial STEEL = new ArmorMaterial(15, makeDefense(2, 5, 6, 2, 5), 8,
      SoundEvents.ARMOR_EQUIP_IRON, 0.8F, 0, RailcraftTags.Items.STEEL_INGOT,
      ResourceKey.create(EquipmentAssets.ROOT_ID, RailcraftConstants.rl("steel")));

  private static Map<ArmorType, Integer> makeDefense(int boots, int leggings, int chestplate, int helmet, int body) {
    return Maps.newEnumMap(Map.of(
        ArmorType.BOOTS, boots,
        ArmorType.LEGGINGS, leggings,
        ArmorType.CHESTPLATE, chestplate,
        ArmorType.HELMET, helmet,
        ArmorType.BODY, body));
  }
}
