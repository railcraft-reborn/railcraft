package mods.railcraft.world.signal;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.common.util.Constants;

/**
 * Created by CovertJaguar on 7/26/2016 for Railcraft.
 *
 * @author CovertJaguar <http://www.railcraft.info>
 */
public class TokenManager extends WorldSavedData {

  public static final String DATA_TAG = "railcraft.tokens";

  private final Map<UUID, TokenRingImpl> tokenRings = new HashMap<>();
  private int clock;

  public TokenManager() {
      super(DATA_TAG);
    }

  @Override
  public void load(CompoundNBT data) {
    List<INBT> tokenRingList = data.getList("tokenRings", Constants.NBT.TAG_COMPOUND);
    for (INBT nbt : tokenRingList) {
      CompoundNBT entry = (CompoundNBT) nbt;
      UUID id = entry.getUUID("id");
      TokenRingImpl tokenRing = new TokenRingImpl(this, id);
      this.tokenRings.put(id, tokenRing);
      List<INBT> signalList = entry.getList("signals", Constants.NBT.TAG_COMPOUND);
      Set<BlockPos> signalPositions = signalList.stream()
          .map(CompoundNBT.class::cast)
          .map(NBTUtil::readBlockPos)
          .filter(Objects::nonNull)
          .collect(Collectors.toSet());
      tokenRing.loadSignals(signalPositions);
      List<INBT> cartList = entry.getList("carts", Constants.NBT.TAG_COMPOUND);
      Set<UUID> carts = cartList.stream()
          .map(CompoundNBT.class::cast)
          .map(signal -> signal.getUUID("cart"))
          .filter(Objects::nonNull)
          .collect(Collectors.toSet());
      tokenRing.loadCarts(carts);
    }
  }

  @Override
  public CompoundNBT save(CompoundNBT data) {
    ListNBT tokenRingList = new ListNBT();
    for (TokenRingImpl tokenRing : tokenRings.values()) {
      CompoundNBT tokenData = new CompoundNBT();
      tokenData.putUUID("id", tokenRing.getId());
      ListNBT signalList = new ListNBT();
      for (BlockPos pos : tokenRing.getPeers()) {
        signalList.add(NBTUtil.writeBlockPos(pos));
      }
      tokenData.put("signals", signalList);
      ListNBT cartList = new ListNBT();
      for (UUID uuid : tokenRing.getTrackedCarts()) {
        CompoundNBT cart = new CompoundNBT();
        cart.putUUID("cart", uuid);
        cartList.add(cart);
      }
      tokenData.put("carts", cartList);
      tokenRingList.add(tokenData);
    }
    data.put("tokenRings", tokenRingList);
    return data;
  }

  public void tick(World level) {
    this.clock++;
    if (this.clock >= 32) {
      this.clock = 0;
      if (this.tokenRings.entrySet().removeIf(e -> e.getValue().isOrphaned(level)))
        this.setDirty();
      this.tokenRings.values().forEach(t -> t.tick(level));
    }
  }

  public TokenRingImpl getTokenRing(UUID id, BlockPos origin) {
    return this.tokenRings.computeIfAbsent(id, __ -> new TokenRingImpl(this, id, origin));
  }

  public Collection<TokenRingImpl> getTokenRings() {
    return this.tokenRings.values();
  }
  
  public static TokenManager get(ServerWorld level) {
    return level.getDataStorage().computeIfAbsent(TokenManager::new, DATA_TAG);
  }
}
