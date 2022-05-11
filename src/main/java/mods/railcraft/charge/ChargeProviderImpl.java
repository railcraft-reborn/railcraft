package mods.railcraft.charge;

import java.util.Map;
import com.google.common.collect.MapMaker;
import mods.railcraft.api.charge.Charge;
import net.minecraft.server.level.ServerLevel;

/**
 * Created by CovertJaguar on 7/26/2016 for Railcraft.
 *
 * @author CovertJaguar <http://www.railcraft.info>
 */
public enum ChargeProviderImpl implements Charge.Provider {

  DISTRIBUTION(Charge.distribution);

  private final Charge charge;

  private final Map<ServerLevel, ChargeNetworkImpl> networks = new MapMaker().weakKeys().makeMap();

  private ChargeProviderImpl(Charge charge) {
    this.charge = charge;
  }

  public Charge getCharge() {
    return this.charge;
  }

  @Override
  public ChargeNetworkImpl network(ServerLevel level) {
    return this.networks.computeIfAbsent(level, __ -> new ChargeNetworkImpl(this.charge, level));
  }
}
