/*------------------------------------------------------------------------------
 Copyright (c) CovertJaguar, 2011-2020

 This work (the API) is licensed under the "MIT" License,
 see LICENSE.md for details.
 -----------------------------------------------------------------------------*/

package mods.railcraft.api.signal;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Stream;
import net.minecraft.util.math.BlockPos;

/**
 * This interface represent an object that can be paired with another object.
 *
 * Generally this applies to AbstractPair, but it is also used for creating TokenRings.
 *
 * Created by CovertJaguar on 7/26/2017 for Railcraft.
 *
 * @author CovertJaguar <https://www.railcraft.info>
 */
public interface SignalNetwork<T> {

  SignalAspect getSignalAspect();

  /**
   * If the signal network
   * 
   * @return
   */
  boolean isLinking();

  void startLinking();

  void stopLinking();

  Optional<T> getPeer(BlockPos blockPos);

  Collection<BlockPos> getPeers();

  default Stream<T> stream() {
    return this.getPeers().stream()
        .map(this::getPeer)
        .flatMap(optional -> optional.isPresent() ? Stream.of(optional.get()) : Stream.empty());
  }

  default int getPeerCount() {
    return this.getPeers().size();
  }

  default boolean hasPeers() {
    return this.getPeerCount() > 0;
  }

  default boolean isPeer(BlockPos blockPos) {
    return this.getPeers().contains(blockPos);
  }

  boolean addPeer(T peer);

  boolean removePeer(BlockPos peerPos);

  void refresh();

  void removed();
}
