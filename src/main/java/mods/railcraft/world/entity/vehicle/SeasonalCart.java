package mods.railcraft.world.entity.vehicle;

import mods.railcraft.season.Season;

public interface SeasonalCart {

  Season getSeason();

  void setSeason(Season season);
}
