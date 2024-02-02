package mods.railcraft.world.level.block.entity;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.mojang.authlib.GameProfile;
import mods.railcraft.api.core.CompoundTagKeys;
import mods.railcraft.network.NetworkChannel;
import mods.railcraft.network.play.OpenLogBookScreen;
import mods.railcraft.util.EntitySearcher;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class LogBookBlockEntity extends RailcraftBlockEntity {

  private static final float SEARCH_RADIUS = 16;
  private static final int BOOK_LINES_PER_PAGE = 13;
  private final Multimap<LocalDate, GameProfile> log = HashMultimap.create();
  private int clock = 0;

  public LogBookBlockEntity(BlockPos blockPos, BlockState blockState) {
    super(RailcraftBlockEntityTypes.LOGBOOK.get(), blockPos, blockState);
  }

  public static void serverTick(Level level, BlockPos blockPos, BlockState blockState,
      LogBookBlockEntity blockEntity) {
    if (++blockEntity.clock % 32 == 0) {
      var players =
          EntitySearcher
              .find(Player.class)
              .at(blockPos)
              .inflateHorizontally(SEARCH_RADIUS)
              .list(level);
      if (!players.isEmpty()) {
        var date = LocalDate.now();
        var isChanged = blockEntity.log.putAll(date, players.stream()
            .map(Player::getGameProfile)
            .toList());
        if (isChanged) {
          blockEntity.setChanged();
        }
      }
    }
  }

  public static CompoundTag convertLogToTag(Multimap<LocalDate, GameProfile> log) {
    var tag = new CompoundTag();
    var monthAgo = LocalDate.now().minusMonths(1);

    var logList = new ListTag();
    for (var entry : log.asMap().entrySet()) {
      if (entry.getKey().isBefore(monthAgo)) {
        continue;
      }
      var dateEntry = new CompoundTag();
      var players = new ListTag();
      for (var player : entry.getValue()) {
        var playerTag = NbtUtils.writeGameProfile(new CompoundTag(), player);
        players.add(playerTag);
      }
      dateEntry.putString(CompoundTagKeys.DATE, entry.getKey().toString());
      dateEntry.put(CompoundTagKeys.PLAYERS, players);
      logList.add(dateEntry);
    }
    tag.put(CompoundTagKeys.ENTRIES, logList);
    return tag;
  }

  public static Multimap<LocalDate, GameProfile> convertLogFromTag(CompoundTag tag) {
    Multimap<LocalDate, GameProfile> log = HashMultimap.create();

    var monthAgo = LocalDate.now().minusMonths(1);

    ListTag logList = tag.getList(CompoundTagKeys.ENTRIES, Tag.TAG_COMPOUND);
    for (int i = 0; i < logList.size(); i++) {
      var compound = logList.getCompound(i);
      var date = LocalDate.parse(compound.getString(CompoundTagKeys.DATE));
      try {
        if (date.isBefore(monthAgo)) {
          continue;
        }
        var playerList = compound.getList(CompoundTagKeys.PLAYERS, Tag.TAG_COMPOUND);
        var players = new HashSet<GameProfile>();
        for (int j = 0; j < playerList.size(); j++) {
          var playerCompound = playerList.getCompound(i);
          players.add(NbtUtils.readGameProfile(playerCompound));
        }
        log.putAll(date, players);
      } catch (DateTimeParseException ignored) {
      }
    }
    return log;
  }

  public void use(ServerPlayer player) {
    NetworkChannel.GAME.sendTo(new OpenLogBookScreen(getPages(this.log)), player);
  }

  private static List<List<String>> getPages(Multimap<LocalDate, GameProfile> log) {
    var pages = new ArrayList<List<String>>();
    var days = new ArrayList<>(log.keySet());
    days.sort(Comparator.reverseOrder());
    for (var day : days) {
      var page = makePage(pages, day);
      for (var profile : log.get(day)) {
        if (page.size() > BOOK_LINES_PER_PAGE)
          page = makePage(pages, day);
        page.add(profile.getName());
      }
    }
    return pages;
  }

  private static List<String> makePage(List<List<String>> pages, LocalDate date) {
    var page = new LinkedList<String>();
    page.add(date.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT)));
    page.add(StringUtils.repeat('-', 34));
    pages.add(page);
    return page;
  }

  @Override
  protected void saveAdditional(CompoundTag tag) {
    super.saveAdditional(tag);
    tag.put(CompoundTagKeys.LOG, convertLogToTag(log));
  }

  @Override
  public void load(CompoundTag tag) {
    super.load(tag);
    log.clear();
    log.putAll(convertLogFromTag(tag.getCompound(CompoundTagKeys.LOG)));
  }
}
