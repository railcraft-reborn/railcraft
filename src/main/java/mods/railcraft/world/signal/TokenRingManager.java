package mods.railcraft.world.signal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mods.railcraft.api.core.CompoundTagKeys;
import net.minecraft.core.BlockPos;
import net.minecraft.core.UUIDUtil;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;

public class TokenRingManager extends SavedData {

  private static final Codec<Map<String, TokenRingData>> MAP_CODEC =
      Codec.unboundedMap(Codec.STRING, TokenRingData.CODEC);

  private static final SavedDataType<TokenRingManager> TYPE = new SavedDataType<>(
      "railcraft.tokens",
      TokenRingManager::new,
      ctx -> RecordCodecBuilder.create(instance -> instance.group(
          RecordCodecBuilder.point(ctx.levelOrThrow()),
          MAP_CODEC.fieldOf(CompoundTagKeys.TOKEN_RINGS).forGetter(manager -> from(manager.tokenRings))
      ).apply(instance, TokenRingManager::new))
  );

  private final ServerLevel level;
  private final Map<UUID, SimpleTokenRing> tokenRings = new HashMap<>();
  private int clock;

  private TokenRingManager(Context context) {
    this.level = context.levelOrThrow();
  }

  private TokenRingManager(ServerLevel level, Map<String, TokenRingData> tokenRings) {
    this.level = level;
    for (var entry : tokenRings.entrySet()) {
      var id = UUID.fromString(entry.getKey());
      var tokenRing = new SimpleTokenRing(this.level, this, id);
      tokenRing.loadSignals(entry.getValue().signals);
      tokenRing.loadCarts(entry.getValue().carts);
      this.tokenRings.put(id, tokenRing);
    }
  }

  private record TokenRingData(
      UUID id,
      List<BlockPos> signals,
      List<UUID> carts) {
    public static final Codec<TokenRingData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        UUIDUtil.CODEC.fieldOf(CompoundTagKeys.ID).forGetter(TokenRingData::id),
        BlockPos.CODEC.listOf().fieldOf(CompoundTagKeys.SIGNALS).forGetter(TokenRingData::signals),
        UUIDUtil.CODEC.listOf().fieldOf(CompoundTagKeys.CARTS).forGetter(TokenRingData::carts)
    ).apply(instance, TokenRingData::new));
  }

  private static Map<String, TokenRingData> from(Map<UUID, SimpleTokenRing> tokenRings) {
    var result = new HashMap<String, TokenRingData>();
    for (var entry : tokenRings.entrySet()) {
      var id = entry.getKey();
      var simpleTokenRing = entry.getValue();
      var signals = new ArrayList<>(simpleTokenRing.peers());
      var carts = new ArrayList<>(simpleTokenRing.getTrackedCarts());
      result.put(id.toString(), new TokenRingData(id, signals, carts));
    }
    return result;
  }

  public void tick(ServerLevel level) {
    this.clock++;
    if (this.clock >= 32) {
      this.clock = 0;
      if (this.tokenRings.entrySet().removeIf(e -> e.getValue().isOrphaned(level))) {
        this.setDirty();
      }
      this.tokenRings.values().forEach(SimpleTokenRing::tick);
    }
  }

  public SimpleTokenRing getTokenRingNetwork(UUID id, BlockPos origin) {
    return this.tokenRings.computeIfAbsent(id,
        __ -> new SimpleTokenRing(this.level, this, id, origin));
  }

  public static TokenRingManager get(ServerLevel level) {
    return level.getDataStorage().computeIfAbsent(TYPE);
  }
}
