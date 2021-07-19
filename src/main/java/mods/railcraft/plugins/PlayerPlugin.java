package mods.railcraft.plugins;

import java.util.UUID;
import javax.annotation.Nullable;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
import com.mojang.authlib.GameProfile;
import mods.railcraft.api.core.RailcraftConstantsAPI;
import mods.railcraft.api.core.RailcraftFakePlayer;
import mods.railcraft.api.items.ActivationBlockingItem;
import mods.railcraft.util.Annotations;
import mods.railcraft.util.TrackTools;
import mods.railcraft.util.inventory.InvTools;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.OpEntry;
import net.minecraft.server.management.PlayerProfileCache;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.LogicalSidedProvider;

/**
 * @author CovertJaguar <http://www.railcraft.info/>
 */
public final class PlayerPlugin {

  public static void writeOwnerToNBT(CompoundNBT nbt, GameProfile owner) {
    if (owner.getName() != null)
      nbt.putString("owner", owner.getName());
    if (owner.getId() != null)
      nbt.putString("ownerId", owner.getId().toString());
  }

  public static GameProfile readOwnerFromNBT(CompoundNBT nbt) {
    String ownerName = RailcraftConstantsAPI.UNKNOWN_PLAYER;
    if (nbt.contains("owner", Constants.NBT.TAG_STRING))
      ownerName = nbt.getString("owner");
    UUID ownerUUID = null;
    if (nbt.hasUUID("ownerId"))
      ownerUUID = nbt.getUUID("ownerId");
    return new GameProfile(ownerUUID, ownerName);
  }

  public static @Nullable PlayerEntity getPlayer(World world, GameProfile gameProfile) {
    UUID playerId = gameProfile.getId();
    if (playerId != null) {
      PlayerEntity player = world.getPlayerByUUID(playerId);
      if (player != null)
        return player;
    }
    return null;
  }

  public static PlayerEntity getItemThrower(ItemEntity item) {
    UUID thrower = item.getThrower();
    PlayerEntity player = null;
    if (thrower != null)
      player = item.level.getPlayerByUUID(item.getThrower());
    if (player == null)
      player = RailcraftFakePlayer.get((ServerWorld) item.level, item.blockPosition());
    return player;
  }

  public static PlayerEntity getOwnerEntity(GameProfile owner, ServerWorld world, BlockPos pos) {
    PlayerEntity player = null;
    if (!RailcraftConstantsAPI.UNKNOWN_PLAYER.equals(owner.getName()))
      player = PlayerPlugin.getPlayer(world, owner);
    if (player == null)
      player = RailcraftFakePlayer.get(world, pos);
    return player;
  }

  public static ITextComponent getUsername(World world, GameProfile gameProfile) {
    UUID playerId = gameProfile.getId();
    if (playerId != null) {
      PlayerEntity player = world.getPlayerByUUID(playerId);
      if (player != null)
        return player.getDisplayName();
    }
    String username = gameProfile.getName();
    return new StringTextComponent(
        Strings.isEmpty(username) ? RailcraftConstantsAPI.UNKNOWN_PLAYER : username);
  }

  @SuppressWarnings("unused")
  public static ITextComponent getUsername(World world, @Nullable UUID playerId) {
    if (playerId != null) {
      PlayerEntity player = world.getPlayerByUUID(playerId);
      if (player != null)
        return player.getDisplayName();
    }
    return new StringTextComponent(RailcraftConstantsAPI.UNKNOWN_PLAYER);
  }

  public static boolean isOwnerOrOp(GameProfile owner, PlayerEntity player) {
    return isOwnerOrOp(owner, player.getGameProfile());
  }

  public static boolean isOwnerOrOp(@Nullable GameProfile owner, @Nullable GameProfile accessor) {
    return !(owner == null || accessor == null) && (owner.equals(accessor) || isPlayerOp(accessor));
  }

  public static boolean isSamePlayer(GameProfile a, GameProfile b) {
    if (a.getId() != null && b.getId() != null)
      return a.getId().equals(b.getId());
    return a.getName() != null && a.getName().equals(b.getName());
  }

  public static boolean isPlayerOp(GameProfile player) {
    return getPermissionLevel(player) >= 2;
  }

  public static int getPermissionLevel(GameProfile gameProfile) {
    MinecraftServer mcServer = getServer();
    if (mcServer != null && mcServer.getPlayerList().isOp(gameProfile)) {
      OpEntry opsEntry = mcServer.getPlayerList().getOps().get(gameProfile);
      return opsEntry != null ? opsEntry.getLevel() : 0;
    }
    return 0;
  }

  public static boolean isPlayerConnected(GameProfile player) {
    MinecraftServer mcServer = getServer();
    return mcServer != null
        && mcServer.getPlayerList().getPlayerByName(player.getName()) != null;
  }

  public static void swingArm(PlayerEntity player, Hand hand) {
    player.swing(hand);
  }

  public static boolean doesItemBlockActivation(PlayerEntity player, Hand hand) {
    if (player.isShiftKeyDown() || hand == Hand.OFF_HAND)
      return true;
    ItemStack heldItem = player.getItemInHand(hand);
    if (!InvTools.isEmpty(heldItem)) {
      return TrackTools.isRail(heldItem)
          || Annotations.isAnnotatedDeepSearch(ActivationBlockingItem.class, heldItem.getItem());
    }
    return false;
  }

  public static GameProfile fillGameProfile(GameProfile profile) {
    String name = profile.getName();
    UUID id = profile.getId();
    if (!StringUtils.isBlank(name) && id != null) {
      return profile;
    }
    PlayerProfileCache cache = getServer().getProfileCache();
    GameProfile filled = id == null ? cache.get(name) : cache.get(id);
    return filled == null ? profile : filled;
  }

  @Nullable
  private static MinecraftServer getServer() {
    return LogicalSidedProvider.INSTANCE.<MinecraftServer>get(LogicalSide.SERVER);
  }
}
