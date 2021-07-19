/*------------------------------------------------------------------------------
 Copyright (c) CovertJaguar, 2011-2020

 This work (the API) is licensed under the "MIT" License,
 see LICENSE.md for details.
 -----------------------------------------------------------------------------*/

package mods.railcraft.api.core;

import com.google.common.collect.ForwardingDeque;
import com.google.common.collect.ForwardingList;
import com.google.common.collect.ForwardingMap;
import com.google.common.collect.ForwardingSet;
import net.minecraft.util.math.BlockPos;

import java.util.*;
import java.util.function.Supplier;

/**
 * Created by CovertJaguar on 12/4/2018 for Railcraft.
 *
 * @author CovertJaguar <http://www.railcraft.info>
 */
public class CollectionToolsAPI {

  public static List<BlockPos> blockPosList(Supplier<List<BlockPos>> delegate) {
    return blockPosList(delegate.get());
  }

  public static List<BlockPos> blockPosList(List<BlockPos> delegate) {
    return new BlockPosList(delegate);
  }

  private static class BlockPosList extends ForwardingList<BlockPos> {
    private final List<BlockPos> delegate;

    private BlockPosList(List<BlockPos> delegate) {
      this.delegate = delegate;
    }

    @Override
    protected List<BlockPos> delegate() {
      return delegate;
    }

    @Override
    public boolean add(BlockPos element) {
      return super.add(element.immutable());
    }

    @Override
    public boolean addAll(Collection<? extends BlockPos> collection) {
      return standardAddAll(collection);
    }

  }

  public static Deque<BlockPos> blockPosDeque(Supplier<Deque<BlockPos>> delegate) {
    return blockPosDeque(delegate.get());
  }

  public static Deque<BlockPos> blockPosDeque(Deque<BlockPos> delegate) {
    return new BlockPosDeque(delegate);
  }

  private static class BlockPosDeque extends ForwardingDeque<BlockPos> {
    private final Deque<BlockPos> delegate;

    private BlockPosDeque(Deque<BlockPos> delegate) {
      this.delegate = delegate;
    }

    @Override
    protected Deque<BlockPos> delegate() {
      return delegate;
    }

    @Override
    public boolean add(BlockPos element) {
      return super.add(element.immutable());
    }

    @Override
    public boolean addAll(Collection<? extends BlockPos> collection) {
      return standardAddAll(collection);
    }

  }

  public static Set<BlockPos> blockPosSet(Supplier<Set<BlockPos>> delegate) {
    return blockPosSet(delegate.get());
  }

  public static Set<BlockPos> blockPosSet(Set<BlockPos> delegate) {
    return new BlockPosSet(delegate);
  }

  private static class BlockPosSet extends ForwardingSet<BlockPos> {
    private final Set<BlockPos> delegate;

    private BlockPosSet(Set<BlockPos> delegate) {
      this.delegate = delegate;
    }

    @Override
    protected Set<BlockPos> delegate() {
      return delegate;
    }

    @Override
    public boolean add(BlockPos element) {
      return super.add(element.immutable());
    }

    @Override
    public boolean addAll(Collection<? extends BlockPos> collection) {
      return standardAddAll(collection);
    }

  }

  public static <V> Map<BlockPos, V> blockPosMap(Map<BlockPos, V> delegate) {
    return new BlockPosMap<>(delegate);
  }

  private static class BlockPosMap<V> extends ForwardingMap<BlockPos, V> {
    private final Map<BlockPos, V> delegate;

    private BlockPosMap(Map<BlockPos, V> delegate) {
      this.delegate = delegate;
    }

    @Override
    protected Map<BlockPos, V> delegate() {
      return delegate;
    }

    @Override
    public V put(BlockPos key, V value) {
      return super.put(key.immutable(), value);
    }
  }
}
