package mods.railcraft.world.level.block.entity;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mods.railcraft.api.core.CompoundTagKeys;
import mods.railcraft.network.to_client.OpenLogBookScreen;
import mods.railcraft.util.EntitySearcher;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.network.PacketDistributor;

public class LogBookBlockEntity extends RailcraftBlockEntity {

  private static final float SEARCH_RADIUS = 16;
  private static final int BOOK_LINES_PER_PAGE = 13;
  private final Multimap<LocalDate, String> log = HashMultimap.create();
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
            .map(GameProfile::getName)
            .toList());
        if (isChanged) {
          blockEntity.setChanged();
        }
      }
    }
  }

  public void use(ServerPlayer player) {
    PacketDistributor.sendToPlayer(player, new OpenLogBookScreen(getPages(this.log)));
  }

  private static List<List<String>> getPages(Multimap<LocalDate, String> log) {
    var pages = new ArrayList<List<String>>();
    var days = new ArrayList<>(log.keySet());
    days.sort(Comparator.reverseOrder());
    for (var day : days) {
      var page = makePage(pages, day);
      for (var profile : log.get(day)) {
        if (page.size() > BOOK_LINES_PER_PAGE)
          page = makePage(pages, day);
        page.add(profile);
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
  protected void saveAdditional(ValueOutput output) {
    super.saveAdditional(output);
    output.store(CompoundTagKeys.LOG, DateEntry.CODEC.listOf(), convertMapToLog(this.log));
  }

  @Override
  protected void loadAdditional(ValueInput input) {
    super.loadAdditional(input);
    log.clear();
    input.read(CompoundTagKeys.LOG, DateEntry.CODEC.listOf())
        .ifPresent(dateEntries -> {
          log.putAll(convertLogToMap(dateEntries));
        });
  }

  private static List<DateEntry> convertMapToLog(Multimap<LocalDate, String> log) {
    var monthAgo = LocalDate.now().minusMonths(1);
    return log.asMap().entrySet().stream()
        .filter(entry -> !entry.getKey().isBefore(monthAgo))
        .map((entry) -> {
          var date = entry.getKey().toString();
          var players = new ArrayList<>(entry.getValue());
          return new DateEntry(date, players);
        })
        .toList();
  }

  private static Multimap<LocalDate, String> convertLogToMap(List<DateEntry> dateEntries) {
    Multimap<LocalDate, String> log = HashMultimap.create();
    var monthAgo = LocalDate.now().minusMonths(1);
    for (var dateEntry : dateEntries) {
      var date = LocalDate.parse(dateEntry.date);
      if (date.isBefore(monthAgo)) {
        continue;
      }
      var players = new HashSet<>(dateEntry.players);
      log.putAll(date, players);
    }
    return log;
  }

  private record DateEntry(String date, List<String> players) {
    public static final Codec<DateEntry> CODEC = RecordCodecBuilder.create(instance ->
        instance.group(
            Codec.STRING.fieldOf(CompoundTagKeys.DATE).forGetter(DateEntry::date),
            Codec.STRING.listOf().fieldOf(CompoundTagKeys.PLAYERS).forGetter(DateEntry::players)
        ).apply(instance, DateEntry::new));
  }
}
