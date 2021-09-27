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
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;

/**
 * Created by CovertJaguar on 7/26/2016 for Railcraft.
 *
 * @author CovertJaguar <http://www.railcraft.info>
 */
public class TokenManager {

  public static final String DATA_TAG = "railcraft.tokens";

  public static TokenWorldManager getManager(ServerWorld world) {
    return world.getDataStorage().computeIfAbsent(TokenWorldManager::new, DATA_TAG);
  }

  public static TokenManager getEventListener() {
    return new TokenManager();
  }

  @SubscribeEvent
  public void tick(TickEvent.WorldTickEvent event) {
    if (event.side == LogicalSide.SERVER && event.phase == TickEvent.Phase.END)
      getManager((ServerWorld) event.world).tick(event.world);
  }

  public static class TokenWorldManager extends WorldSavedData {

    private final Map<UUID, TokenRing> tokenRings = new HashMap<>();
    private int clock;

    public TokenWorldManager() {
      super(DATA_TAG);
    }

    @Override
    public void load(CompoundNBT data) {
      List<INBT> tokenRingList = data.getList("tokenRings", Constants.NBT.TAG_COMPOUND);
      for (INBT nbt : tokenRingList) {
        CompoundNBT entry = (CompoundNBT) nbt;
        UUID uuid = entry.getUUID("uuid");
        if (uuid != null) {
          TokenRing tokenRing = new TokenRing(this, uuid);
          tokenRings.put(uuid, tokenRing);
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
    }

    @Override
    public CompoundNBT save(CompoundNBT data) {
      ListNBT tokenRingList = new ListNBT();
      for (TokenRing tokenRing : tokenRings.values()) {
        CompoundNBT tokenData = new CompoundNBT();
        tokenData.putUUID("uuid", tokenRing.getUUID());
        ListNBT signalList = new ListNBT();
        for (BlockPos pos : tokenRing.getSignals()) {
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

    public void tick(World world) {
      clock++;
      if (clock % 32 == 0) {
        if (tokenRings.entrySet().removeIf(e -> e.getValue().isOrphaned(world)))
          this.setDirty();

        tokenRings.values().forEach(t -> t.tick(world));
      }
    }

    public TokenRing getTokenRing(UUID uuid, BlockPos origin) {
      return tokenRings.computeIfAbsent(uuid, k -> new TokenRing(this, uuid, origin));
    }

    public Collection<TokenRing> getTokenRings() {
      return tokenRings.values();
    }
  }
}
