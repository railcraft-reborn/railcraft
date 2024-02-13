package mods.railcraft.world.signal;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import mods.railcraft.api.core.CompoundTagKeys;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;

public class TokenRingManager extends SavedData {

  private static final String DATA_TAG = "railcraft.tokens";

  private final ServerLevel level;
  private final Map<UUID, SimpleTokenRing> tokenRings = new HashMap<>();
  private int clock;

  public TokenRingManager(ServerLevel level) {
    this.level = level;
  }

  private void load(CompoundTag tag) {
    var tokenRingList = tag.getList(CompoundTagKeys.TOKEN_RINGS, Tag.TAG_COMPOUND);
    for (int i = 0; i < tokenRingList.size(); i++) {
      var entry = tokenRingList.getCompound(i);
      var id = entry.getUUID(CompoundTagKeys.ID);
      var tokenRing = new SimpleTokenRing(this.level, this, id);
      this.tokenRings.put(id, tokenRing);
      var signalList = entry.getList(CompoundTagKeys.SIGNALS, Tag.TAG_COMPOUND);
      var signalPositions = signalList.stream()
          .map(CompoundTag.class::cast)
          .map(NbtUtils::readBlockPos)
          .collect(Collectors.toSet());
      tokenRing.loadSignals(signalPositions);
      var cartList = entry.getList(CompoundTagKeys.CARTS, Tag.TAG_COMPOUND);
      var carts = cartList.stream()
          .map(CompoundTag.class::cast)
          .map(signal -> signal.getUUID(CompoundTagKeys.CART))
          .collect(Collectors.toSet());
      tokenRing.loadCarts(carts);
    }
  }

  @Override
  public CompoundTag save(CompoundTag tag) {
    var tokenRingList = new ListTag();
    for (var tokenRing : tokenRings.values()) {
      var tokenData = new CompoundTag();
      tokenData.putUUID(CompoundTagKeys.ID, tokenRing.getId());
      var signalList = new ListTag();
      for (var pos : tokenRing.peers()) {
        signalList.add(NbtUtils.writeBlockPos(pos));
      }
      tokenData.put(CompoundTagKeys.SIGNALS, signalList);
      var cartList = new ListTag();
      for (var uuid : tokenRing.getTrackedCarts()) {
        var cart = new CompoundTag();
        cart.putUUID(CompoundTagKeys.CART, uuid);
        cartList.add(cart);
      }
      tokenData.put(CompoundTagKeys.CARTS, cartList);
      tokenRingList.add(tokenData);
    }
    tag.put(CompoundTagKeys.TOKEN_RINGS, tokenRingList);
    return tag;
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
    return level.getDataStorage().computeIfAbsent(tag -> {
      var manager = new TokenRingManager(level);
      manager.load(tag);
      return manager;
    }, () -> new TokenRingManager(level), DATA_TAG);
  }
}
