package mods.railcraft.world.level.block.entity.track;

import java.util.UUID;
import org.jetbrains.annotations.Nullable;
import mods.railcraft.api.core.CompoundTagKeys;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntity;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntityTypes;
import mods.railcraft.world.level.block.track.outfitted.PoweredOutfittedTrackBlock;
import net.minecraft.SharedConstants;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundSetActionBarTextPacket;
import net.minecraft.network.protocol.game.ClientboundSetSubtitleTextPacket;
import net.minecraft.network.protocol.game.ClientboundSetTitleTextPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.level.block.state.BlockState;

public class MessengerTrackBlockEntity extends RailcraftBlockEntity {

  public static final int MAX_LENGTH = 128;
  private static final int RESEND_INTERVAL = 5 * SharedConstants.TICKS_PER_SECOND;

  private String title = "";
  private String subtitle = "";
  private String actionbar = "";

  @Nullable
  private UUID lastCartId;
  private long lastMessageTime;

  public MessengerTrackBlockEntity(BlockPos blockPos, BlockState blockState) {
    super(RailcraftBlockEntityTypes.MESSENGER_TRACK.get(), blockPos, blockState);
  }

  public void minecartPassed(AbstractMinecart cart) {
    if (!PoweredOutfittedTrackBlock.isPowered(this.getBlockState())) {
      return;
    }
    var time = this.level.getGameTime();
    if (cart.getUUID().equals(this.lastCartId)
        && time - this.lastMessageTime < RESEND_INTERVAL) {
      return;
    }
    this.lastCartId = cart.getUUID();
    this.lastMessageTime = time;
    this.sendMessage(cart);
  }

  private void sendMessage(AbstractMinecart cart) {
    var showTitle = !this.title.isEmpty() || !this.subtitle.isEmpty();
    var showActionbar = !this.actionbar.isEmpty();
    if (!showTitle && !showActionbar) {
      return;
    }
    for (var passenger : cart.getIndirectPassengers()) {
      if (passenger instanceof ServerPlayer player) {
        if (showActionbar) {
          player.connection.send(
              new ClientboundSetActionBarTextPacket(Component.literal(this.actionbar)));
        }
        if (showTitle) {
          player.connection.send(
              new ClientboundSetSubtitleTextPacket(Component.literal(this.subtitle)));
          player.connection.send(
              new ClientboundSetTitleTextPacket(Component.literal(this.title)));
        }
      }
    }
  }

  public String getTitle() {
    return this.title;
  }

  public String getSubtitle() {
    return this.subtitle;
  }

  public String getActionbar() {
    return this.actionbar;
  }

  public void setMessages(String title, String subtitle, String actionbar) {
    this.title = sanitize(title);
    this.subtitle = sanitize(subtitle);
    this.actionbar = sanitize(actionbar);
  }

  private static String sanitize(String text) {
    var trimmed = text.strip();
    return trimmed.length() > MAX_LENGTH ? trimmed.substring(0, MAX_LENGTH) : trimmed;
  }

  @Override
  protected void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {
    super.saveAdditional(tag, provider);
    tag.putString(CompoundTagKeys.TITLE, this.title);
    tag.putString(CompoundTagKeys.SUBTITLE, this.subtitle);
    tag.putString(CompoundTagKeys.ACTIONBAR, this.actionbar);
  }

  @Override
  public void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {
    super.loadAdditional(tag, provider);
    this.setMessages(tag.getString(CompoundTagKeys.TITLE),
        tag.getString(CompoundTagKeys.SUBTITLE),
        tag.getString(CompoundTagKeys.ACTIONBAR));
  }

  @Override
  public void writeToBuf(RegistryFriendlyByteBuf out) {
    super.writeToBuf(out);
    out.writeUtf(this.title, MAX_LENGTH);
    out.writeUtf(this.subtitle, MAX_LENGTH);
    out.writeUtf(this.actionbar, MAX_LENGTH);
  }

  @Override
  public void readFromBuf(RegistryFriendlyByteBuf in) {
    super.readFromBuf(in);
    this.title = in.readUtf(MAX_LENGTH);
    this.subtitle = in.readUtf(MAX_LENGTH);
    this.actionbar = in.readUtf(MAX_LENGTH);
  }
}
