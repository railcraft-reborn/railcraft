package mods.railcraft.world.signal;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;

/**
 * Created by CovertJaguar on 7/26/2016 for Railcraft.
 *
 * @author CovertJaguar <https://www.railcraft.info>
 */
public class TokenRingManager extends SavedData {

  public static final String DATA_TAG = "railcraft.tokens";

  private final ServerLevel level;
  private final Map<UUID, SimpleTokenRing> tokenRings = new HashMap<>();
  private int clock;

  public TokenRingManager(ServerLevel level) {
    this.level = level;
  }

  private void load(CompoundTag data) {
    List<Tag> tokenRingList = data.getList("tokenRings", Tag.TAG_COMPOUND);
    for (Tag nbt : tokenRingList) {
      CompoundTag entry = (CompoundTag) nbt;
      UUID id = entry.getUUID("id");
      SimpleTokenRing tokenRing = new SimpleTokenRing(this.level, this, id);
      this.tokenRings.put(id, tokenRing);
      List<Tag> signalList = entry.getList("signals", Tag.TAG_COMPOUND);
      Set<BlockPos> signalPositions = signalList.stream()
          .map(CompoundTag.class::cast)
          .map(NbtUtils::readBlockPos)
          .filter(Objects::nonNull)
          .collect(Collectors.toSet());
      tokenRing.loadSignals(signalPositions);
      List<Tag> cartList = entry.getList("carts", Tag.TAG_COMPOUND);
      Set<UUID> carts = cartList.stream()
          .map(CompoundTag.class::cast)
          .map(signal -> signal.getUUID("cart"))
          .filter(Objects::nonNull)
          .collect(Collectors.toSet());
      tokenRing.loadCarts(carts);
    }
  }

  @Override
  public CompoundTag save(CompoundTag data) {
    ListTag tokenRingList = new ListTag();
    for (SimpleTokenRing tokenRing : tokenRings.values()) {
      CompoundTag tokenData = new CompoundTag();
      tokenData.putUUID("id", tokenRing.getId());
      ListTag signalList = new ListTag();
      for (BlockPos pos : tokenRing.getPeers()) {
        signalList.add(NbtUtils.writeBlockPos(pos));
      }
      tokenData.put("signals", signalList);
      ListTag cartList = new ListTag();
      for (UUID uuid : tokenRing.getTrackedCarts()) {
        CompoundTag cart = new CompoundTag();
        cart.putUUID("cart", uuid);
        cartList.add(cart);
      }
      tokenData.put("carts", cartList);
      tokenRingList.add(tokenData);
    }
    data.put("tokenRings", tokenRingList);
    return data;
  }

  public void tick(ServerLevel level) {
    this.clock++;
    if (this.clock >= 32) {
      this.clock = 0;
      if (this.tokenRings.entrySet().removeIf(e -> e.getValue().isOrphaned(level)))
        this.setDirty();
      this.tokenRings.values().forEach(SimpleTokenRing::tick);
    }
  }

  public SimpleTokenRing getTokenRingNetwork(UUID id, BlockPos origin) {
    return this.tokenRings.computeIfAbsent(id,
        __ -> new SimpleTokenRing(this.level, this, id, origin));
  }

  public Collection<SimpleTokenRing> getTokenRings() {
    return this.tokenRings.values();
  }

  public static TokenRingManager get(ServerLevel level) {
    return level.getDataStorage().computeIfAbsent(tag -> {
      var manager = new TokenRingManager(level);
      manager.load(tag);
      return manager;

    }, () -> new TokenRingManager(level), DATA_TAG);
  }
}
