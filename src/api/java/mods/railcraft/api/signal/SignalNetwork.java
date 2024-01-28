/*------------------------------------------------------------------------------
 Copyright (c) Railcraft Reborn, 2023+

 This work (the API) is licensed under the "MIT" License,
 see LICENSE.md for details.
 -----------------------------------------------------------------------------*/
package mods.railcraft.api.signal;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Stream;
import net.minecraft.core.BlockPos;

/**
 * Provides access to a signal network.
 * 
 * @author Sm0keySa1m0n
 *
 * @param <T> - node type
 */
public interface SignalNetwork<T> {

  SignalAspect aspect();

  /**
   * If the signal network
   *
   * @return
   */
  boolean isLinking();

  void startLinking();

  void stopLinking();

  Optional<T> peerAt(BlockPos blockPos);

  Collection<BlockPos> peers();

  default Stream<T> stream() {
    return this.peers().stream()
        .map(this::peerAt)
        .flatMap(Optional::stream);
  }

  default int peerCount() {
    return this.peers().size();
  }

  default boolean hasPeers() {
    return this.peerCount() > 0;
  }

  default boolean isPeer(BlockPos blockPos) {
    return this.peers().contains(blockPos);
  }

  boolean addPeer(T peer);

  boolean removePeer(BlockPos peerPos);

  void refresh();

  void destroy();
}
