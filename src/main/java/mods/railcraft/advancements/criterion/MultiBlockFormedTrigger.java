package mods.railcraft.advancements.criterion;

import static java.util.Objects.requireNonNull;
import java.util.Collection;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import com.google.gson.JsonObject;
import com.mojang.authlib.GameProfile;
import mods.railcraft.api.core.RailcraftConstantsAPI;
import mods.railcraft.event.MultiBlockEvent;
import mods.railcraft.util.Conditions;
import mods.railcraft.util.JsonTools;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntity;
import net.minecraft.advancements.ICriterionInstance;
import net.minecraft.advancements.PlayerAdvancements;
import net.minecraft.advancements.criterion.NBTPredicate;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.loot.ConditionArrayParser;
import net.minecraft.loot.ConditionArraySerializer;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.ForgeRegistries;

final class MultiBlockFormedTrigger extends BaseTrigger<MultiBlockFormedTrigger.Instance> {

  static final ResourceLocation ID = RailcraftConstantsAPI.locationOf("multiblock_formed");

  MultiBlockFormedTrigger() {
    MinecraftForge.EVENT_BUS.register(this);
  }

  @Override
  public ResourceLocation getId() {
    return ID;
  }

  @Override
  public Instance createInstance(JsonObject json, ConditionArrayParser parser) {
    TileEntityType<?> type;
    if (json.has("type")) {
      type = ForgeRegistries.TILE_ENTITIES.getValue(
          new ResourceLocation(json.get("type").getAsString()));
    } else {
      type = null;
    }
    NBTPredicate nbt =
        JsonTools.whenPresent(json, "nbt", NBTPredicate::fromJson, NBTPredicate.ANY);

    return new Instance(type, nbt);
  }

  @SubscribeEvent
  public void onMultiBlockForm(MultiBlockEvent.Form event) {
    RailcraftBlockEntity tile = event.getMaster();
    GameProfile owner = tile.getOwner();
    MinecraftServer server = requireNonNull(((ServerWorld) tile.getLevel()).getServer());
    ServerPlayerEntity player = server.getPlayerList().getPlayer(owner.getId());
    if (player == null) {
      return; // Offline
    }
    PlayerAdvancements advancements = player.getAdvancements();
    Collection<Listener<MultiBlockFormedTrigger.Instance>> done = manager.get(advancements).stream()
        .filter(listener -> listener.getTriggerInstance().matches(tile))
        .collect(Collectors.toList());
    for (Listener<Instance> listener : done) {
      listener.run(advancements);
    }
  }

  static final class Instance implements ICriterionInstance {

    final @Nullable TileEntityType<?> type;
    final NBTPredicate nbt;

    Instance(@Nullable TileEntityType<?> type, NBTPredicate nbtPredicate) {
      this.type = type;
      this.nbt = nbtPredicate;
    }

    boolean matches(RailcraftBlockEntity tile) {
      return Conditions.check(this.type, tile.getType())
          && nbt.matches(tile.save(new CompoundNBT()));
    }

    @Override
    public ResourceLocation getCriterion() {
      return ID;
    }

    @Override
    public JsonObject serializeToJson(ConditionArraySerializer serializer) {
      JsonObject json = new JsonObject();
      if (this.type != null) {
        json.addProperty("type", this.type.getRegistryName().toString());
      }
      json.add("nbt", this.nbt.serializeToJson());
      return json;
    }
  }
}
