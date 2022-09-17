package mods.railcraft.world.entity.vehicle;

import com.google.common.collect.ForwardingMap;
import com.google.common.collect.MapMaker;
import com.mojang.logging.LogUtils;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.jetbrains.annotations.Nullable;
import mods.railcraft.util.CompositeFluidHandler;
import mods.railcraft.util.FunctionalUtil;
import mods.railcraft.world.entity.vehicle.locomotive.Locomotive;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;
import org.slf4j.Logger;

/**
 * @author CovertJaguar (https://www.railcraft.info)
 */
public final class Train implements Iterable<AbstractMinecart> {

  public static final String TRAIN_NBT = "rcTrain";

  private static final Logger logger = LogUtils.getLogger();

  private static final Map<ServerLevel, Manager> managers = new MapMaker().weakKeys().makeMap();

  private final UUID id;
  private final Deque<UUID> carts = new LinkedList<>();
  private final Collection<UUID> safeCarts = Collections.unmodifiableCollection(this.carts);
  private final Set<UUID> locks = new HashSet<>();
  @Nullable
  private Level level;
  private State state;
  private boolean dirty = true;
  private boolean dead;

  private Train(AbstractMinecart cart) {
    this(UUID.randomUUID(),
        State.NORMAL,
        Collections.singleton(cart.getUUID()),
        Collections.emptySet());
    this.level = cart.level;
    rebuild(cart);
  }

  private Train(UUID id, State state, Collection<UUID> carts, Set<UUID> locks) {
    this.id = id;
    this.state = state;
    this.carts.addAll(carts);
    this.locks.addAll(locks);
  }

  public static Manager getManager(ServerLevel level) {
    return managers.computeIfAbsent(level, Manager::new);
  }

  /**
   * Finds and returns a Train object.
   *
   * If one is not found, it will create a new instance.
   *
   * This function is NOT thread safe and will throw an error if called outside the server thread.
   */
  public static Optional<Train> get(@Nullable AbstractMinecart cart) {
    if (cart == null || cart.level.isClientSide()) {
      return Optional.empty();
    }
    Manager manager = getManager((ServerLevel) cart.level);
    Train train = manager.get(getTrainUUID(cart));
    if (train == null) {
      train = new Train(cart);
      manager.put(train.id, train);
      logger.debug("Creating new train object: {}", train);
    } else {
      train.level = cart.level;
      if (train.dead || !train.contains(cart) || train.isInvalid()) {
        train.kill();
        return Optional.empty();
      }
    }
    return Optional.of(train);
  }

  /**
   * Finds and returns a Train object only if one has already been created.
   *
   * It will not create a new Train instance.
   *
   * This function is thread safe.
   */
  public static Optional<Train> getExisting(AbstractMinecart cart) {
    return Optional.ofNullable(getManager((ServerLevel) cart.level).get(getTrainUUID(cart)));
  }

  @Override
  public String toString() {
    return String.format("Train{id=%s,n=%d}", id, size());
  }

  /**
   * Will stream all carts in the train if on the server, or just the passed in cart if on the
   * client.
   */
  public static Stream<AbstractMinecart> streamCarts(AbstractMinecart cart) {
    return get(cart).map(Train::stream).orElseGet(() -> Stream.of(cart));
  }

  @Nullable
  public static UUID getTrainUUID(AbstractMinecart cart) {
    CompoundTag nbt = cart.getPersistentData();
    return nbt.hasUUID(TRAIN_NBT) ? nbt.getUUID(TRAIN_NBT) : null;
  }

  public static boolean isPartOfTrain(AbstractMinecart cart) {
    return Train.get(cart).map(t -> t.size() > 1).orElse(false);
  }

  public static boolean areInSameTrain(@Nullable AbstractMinecart cart1,
      @Nullable AbstractMinecart cart2) {
    if (cart1 == null || cart2 == null) {
      return false;
    }
    if (cart1 == cart2) {
      return true;
    }

    UUID train1 = getTrainUUID(cart1);
    UUID train2 = getTrainUUID(cart2);

    return train1 != null && Objects.equals(train1, train2);
  }

  private static Optional<Train> getLongerTrain(AbstractMinecart cart1,
      AbstractMinecart cart2) {
    Optional<Train> train1 = getExisting(cart1);
    Optional<Train> train2 = getExisting(cart2);

    if (train1.equals(train2)) {
      return train1;
    }
    if (!train1.isPresent()) {
      return train2;
    }
    if (!train2.isPresent()) {
      return train1;
    }

    if (train1.get().size() >= train2.get().size()) {
      return train1;
    }
    return train2;
  }

  public static void repairTrain(AbstractMinecart cart1, AbstractMinecart cart2) {
    getLongerTrain(cart1, cart2).ifPresent(t -> t.rebuild(cart1));
  }

  public static void removeTrainTag(AbstractMinecart cart) {
    cart.getPersistentData().remove(TRAIN_NBT);
  }

  public void addTrainTag(AbstractMinecart cart) {
    UUID trainId = this.getId();
    cart.getPersistentData().put(TRAIN_NBT, NbtUtils.createUUID(trainId));
  }

  @Nullable
  private AbstractMinecart getCart(UUID cartID) {
    Objects.requireNonNull(this.level);
    return CartTools.getCartFromUUID(this.level, cartID);
  }

  private void rebuild(AbstractMinecart first) {
    this.clear();
    this.rebuild(null, first);
    this.markDirty();
  }

  private void rebuild(@Nullable AbstractMinecart prev, AbstractMinecart next) {
    if (prev == null || this.carts.getFirst() == prev.getUUID()) {
      this.carts.addFirst(next.getUUID());
    } else if (carts.getLast() == prev.getUUID()) {
      this.carts.addLast(next.getUUID());
    } else {
      throw new IllegalStateException("Passed a non-null prev value on an empty train!");
    }

    getExisting(next).filter(t -> t != this).ifPresent(Train::kill);
    this.addTrainTag(next);

    var linkA = LinkageManagerImpl.INSTANCE.getLinkedCartA(next).orElse(null);
    var linkB = LinkageManagerImpl.INSTANCE.getLinkedCartB(next).orElse(null);

    if (linkA != null && linkA != prev && !this.contains(linkA)) {
      this.rebuild(next, linkA);
    }

    if (linkB != null && linkB != prev && !this.contains(linkB)) {
      this.rebuild(next, linkB);
    }
  }

  private boolean isInvalid() {
    return this.isEmpty() || this.carts.stream().anyMatch(this::isCartInvalid);
  }

  private boolean isCartInvalid(UUID cartID) {
    AbstractMinecart cart = getCart(cartID);
    return cart != null && !this.id.equals(getTrainUUID(cart));
  }

  /**
   * Only marks the train for removal, it isn't removed until the next world tick.
   *
   * This is done for thread safety reasons.
   */
  public static void killTrain(AbstractMinecart cart) {
    // Game.log(Level.WARN, "Thread: " + Thread.currentThread().getName());
    getExisting(cart).ifPresent(Train::kill);
    removeTrainTag(cart);
  }

  public void kill() {
    this.dead = true;
  }

  private void clear() {
    this.forEach(Train::removeTrainTag);
    this.carts.clear();
    this.markDirty();
  }

  public UUID getId() {
    return this.id;
  }

  public boolean contains(@Nullable AbstractMinecart cart) {
    return cart != null && this.carts.contains(cart.getUUID());
  }

  public boolean contains(@Nullable UUID cart) {
    return cart != null && this.carts.contains(cart);
  }

  public boolean isTrainEnd(@Nullable AbstractMinecart cart) {
    return cart != null && this.getEnds().contains(cart.getUUID());
  }

  public Collection<UUID> getEnds() {
    Set<UUID> ends = new HashSet<>();
    if (!this.carts.isEmpty()) {
      ends.add(this.carts.getFirst());
      ends.add(this.carts.getLast());
    }
    return ends;
  }

  public Optional<Locomotive> getHeadLocomotive() {
    return getEnds().stream().map(this::getCart).flatMap(FunctionalUtil.ofType(Locomotive.class))
        .findFirst();
  }

  public Stream<AbstractMinecart> stream() {
    return safeCarts.stream().map(this::getCart).filter(Objects::nonNull);
  }

  public <T extends AbstractMinecart> Stream<T> stream(Class<T> cartClass) {
    return stream().flatMap(FunctionalUtil.ofType(cartClass));
  }

  @Override
  public Iterator<AbstractMinecart> iterator() {
    return stream().iterator();
  }

  public int getNumRunningLocomotives() {
    return (int) stream(Locomotive.class).filter(Locomotive::isRunning).count();
  }

  public <T extends AbstractMinecart> List<T> getCarts(Class<T> cartClass) {
    return stream(cartClass).collect(Collectors.toList());
  }

  public Collection<UUID> getCarts() {
    return this.safeCarts;
  }

  public Optional<IItemHandlerModifiable> getItemHandler() {
    List<IItemHandlerModifiable> cartHandlers = stream()
        .flatMap(cart -> cart.getCapability(ForgeCapabilities.ITEM_HANDLER)
            .map(Stream::of)
            .orElse(Stream.empty()))
        .flatMap(FunctionalUtil.ofType(IItemHandlerModifiable.class))
        .collect(Collectors.toList());
    return cartHandlers.isEmpty()
        ? Optional.empty()
        : Optional.of(new CombinedInvWrapper(cartHandlers.toArray(new IItemHandlerModifiable[0])));
  }

  public Optional<IFluidHandler> getFluidHandler() {
    List<IFluidHandler> cartHandlers = stream()
        .flatMap(cart -> cart.getCapability(ForgeCapabilities.FLUID_HANDLER)
            .map(Stream::of)
            .orElse(Stream.empty()))
        .collect(Collectors.toList());
    return cartHandlers.isEmpty()
        ? Optional.empty()
        : Optional.of(new CompositeFluidHandler(cartHandlers));
  }

  public int size() {
    return carts.size();
  }

  public boolean isEmpty() {
    return carts.isEmpty();
  }

  public void refreshMaxSpeed() {
    setMaxSpeed(calculateMaxSpeed());
  }

  private float calculateMaxSpeed() {
    double locoBoost = Math.max(0.0, getNumRunningLocomotives() - 1.0) * 0.075;
    return (float) (double) stream()
        .mapToDouble(c -> Math.min(c.getMaxCartSpeedOnRail(), softMaxSpeed(c) + locoBoost))
        .min().orElse(1.2F);
  }

  private float softMaxSpeed(AbstractMinecart cart) {
    if (cart instanceof WeightedCart) {
      return ((WeightedCart) cart).softMaxSpeed();
    }
    return cart.getMaxCartSpeedOnRail();
  }

  private void setMaxSpeed(float trainSpeed) {
    for (AbstractMinecart c : this) {
      c.setCurrentCartSpeedCapOnRail(trainSpeed);
    }
  }

  public boolean isTrainLockedDown() {
    return !locks.isEmpty();
  }

  public void addLock(UUID lock) {
    locks.add(lock);
    markDirty();
  }

  public void removeLock(UUID lock) {
    locks.remove(lock);
    markDirty();
  }

  public boolean isIdle() {
    return state == State.IDLE || isTrainLockedDown();
  }

  public boolean isStopped() {
    return state == State.STOPPED;
  }

  public void setTrainState(State state) {
    if (this.state != state) {
      this.state = state;
      markDirty();
    }
  }

  public enum State implements StringRepresentable {

    STOPPED("stopped"), IDLE("idle"), NORMAL("normal");

    private static final Map<String, State> byName = Arrays.stream(values())
        .collect(Collectors.toUnmodifiableMap(State::getSerializedName, Function.identity()));

    private final String name;

    private State(String name) {
      this.name = name;
    }

    @Override
    public String getSerializedName() {
      return this.name;
    }

    public static Optional<State> getByName(String name) {
      return Optional.ofNullable(byName.get(name));
    }
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof Train)) {
      return false;
    }
    Train other = (Train) obj;
    return id.equals(other.id);
  }

  @Override
  public int hashCode() {
    return id.hashCode();
  }

  @Nullable
  static Train readFromNBT(CompoundTag tag) {
    if (!tag.hasUUID("id")) {
      return null;
    }
    UUID id = tag.getUUID("id");
    State state = State.getByName(tag.getString("state")).orElse(State.NORMAL);
    List<UUID> carts =
        tag.getList("carts", Tag.TAG_INT_ARRAY).stream().map(NbtUtils::loadUUID)
            .collect(Collectors.toList());
    Set<UUID> locks =
        tag.getList("locks", Tag.TAG_INT_ARRAY).stream().map(NbtUtils::loadUUID)
            .collect(Collectors.toSet());
    return new Train(id, state, carts, locks);
  }

  void writeToNBT(CompoundTag tag) {
    tag.putUUID("id", this.id);
    tag.putString("state", this.state.getSerializedName());
    ListTag listTag = new ListTag();
    for (UUID id : this.carts) {
      listTag.add(NbtUtils.createUUID(id));
    }
    tag.put("carts", listTag);

    ListTag locks = new ListTag();
    for (UUID uuid : this.locks) {
      locks.add(NbtUtils.createUUID(uuid));
    }
    tag.put("locks", locks);
  }

  void markDirty() {
    setDirty(true);
  }

  boolean isDirty() {
    return dirty;
  }

  void setDirty(boolean dirty) {
    this.dirty = dirty;
  }

  public static final class Manager extends ForwardingMap<UUID, Train> {

    final Level level;
    final TrainSavedData data;

    private Manager(ServerLevel level) {
      this.level = level;
      this.data = level.getDataStorage().computeIfAbsent(tag -> {
        var savedData = new TrainSavedData();
        savedData.load(tag);
        return savedData;
      }, TrainSavedData::new, TrainSavedData.ID);
    }

    public static void clearTrains() {
      managers.values().forEach(ForwardingMap::clear);
    }

    @Override
    protected Map<UUID, Train> delegate() {
      return this.data.trains;
    }

    public void tick() {
      Iterator<Train> it = values().iterator();
      while (it.hasNext()) {
        Train train = it.next();
        train.level = level;
        if (train.dead || train.isInvalid()) {
          train.clear();
          it.remove();
          data.setDirty();
        }
      }
    }
  }

  public static final class TrainSavedData extends SavedData {

    private static final String ID = "railcraft.trains";

    private static final Logger logger = LogUtils.getLogger();

    final Map<UUID, Train> trains = new ForwardingMap<UUID, Train>() {
      private final Map<UUID, Train> trains = new HashMap<>();

      @Override
      protected Map<UUID, Train> delegate() {
        return trains;
      }

      @Override
      public Train put(UUID key, Train value) {
        setDirty();
        return super.put(key, value);
      }

      @Override
      public void putAll(Map<? extends UUID, ? extends Train> map) {
        standardPutAll(map);
      }

      @Override
      public Train remove(Object key) {
        setDirty();
        return super.remove(key);
      }

      @Override
      public void clear() {
        super.clear();
        setDirty();
      }
    };

    private void load(CompoundTag tag) {
      trains.clear();
      for (Tag each : tag.getList("trains", Tag.TAG_COMPOUND)) {
        Train train = Train.readFromNBT((CompoundTag) each);
        if (train != null) {
          trains.put(train.getId(), train);
        }
      }
      logger.debug("Loaded {} trains", trains.size());
    }

    @Override
    public CompoundTag save(CompoundTag tag) {
      logger.debug("Saving {} trains", trains.size());
      ListTag trainsTag = new ListTag();
      for (Train train : trains.values()) {
        CompoundTag trainTag = new CompoundTag();
        train.writeToNBT(trainTag);
        trainsTag.add(trainTag);
        train.setDirty(false);
      }
      tag.put("trains", trainsTag);
      return tag;
    }

    @Override
    public boolean isDirty() {
      return super.isDirty() || trains.values().stream().anyMatch(Train::isDirty);
    }
  }
}
