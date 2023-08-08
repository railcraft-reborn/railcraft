package mods.railcraft.charge;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Deque;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.WeakHashMap;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import com.google.common.collect.ForwardingCollection;
import com.google.common.collect.ForwardingMap;
import com.google.common.collect.ForwardingSet;
import com.google.common.collect.Iterators;
import com.mojang.logging.LogUtils;
import mods.railcraft.RailcraftConfig;
import mods.railcraft.api.charge.Charge;
import mods.railcraft.api.charge.ChargeBlock;
import mods.railcraft.api.charge.ChargeProtectionItem;
import mods.railcraft.api.charge.ChargeStorage;
import mods.railcraft.util.ModEntitySelector;
import mods.railcraft.world.damagesource.RailcraftDamageSources;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.state.BlockState;

public class ChargeNetworkImpl implements Charge.Network {

  private static final Logger logger = LogUtils.getLogger();

  public static final int CHARGE_PER_DAMAGE = 1000;
  public static final Map<ChargeBlock.ConnectType, ConnectionMap> CONNECTION_MAPS =
      new EnumMap<>(ChargeBlock.ConnectType.class);
  private final ChargeGrid NULL_GRID = new NullGrid();
  private final Map<BlockPos, ChargeNode> nodes = new HashMap<>();
  private final Map<BlockPos, ChargeNode> queue = new LinkedHashMap<>();
  private final Set<ChargeNode> tickingNodes = new LinkedHashSet<>();
  private final Set<ChargeGrid> grids = Collections.newSetFromMap(new WeakHashMap<>());
  private final ChargeNode NULL_NODE = new NullNode();
  private final Charge network;
  private final ServerLevel level;
  private final ChargeSavedData chargeSavedData;

  public ChargeNetworkImpl(Charge network, ServerLevel level) {
    this.network = network;
    this.level = level;
    this.chargeSavedData = ChargeSavedData.getFor(network, level);
  }

  public void tick() {
    this.tickingNodes.removeIf(ChargeNode::checkUsageRecordingCompletion);

    // Process the queue of nodes waiting to be added/removed from the network
    Set<BlockPos> added = new HashSet<>();
    Iterator<Map.Entry<BlockPos, ChargeNode>> iterator = queue.entrySet().iterator();
    int count = 0;
    while (iterator.hasNext() && count < 500) {
      count++;
      Map.Entry<BlockPos, ChargeNode> action = iterator.next();
      if (action.getValue() == null) {
        removeNodeImpl(action.getKey());
      } else {
        addNodeImpl(action.getKey(), action.getValue());
        added.add(action.getKey());
      }
      iterator.remove();
    }

    // Search for connected nodes of recently added nodes and register them too
    // helps fill out the graph faster and more reliably
    Set<BlockPos> newNodes = new HashSet<>();
    added.forEach(pos -> forConnections(pos, (conPos, conState) -> {
      if (addNode(conPos, conState))
        newNodes.add(conPos);
    }));

    // Remove discarded grids and tick what's left
    grids.removeIf(g -> g.invalid);
    grids.forEach(ChargeGrid::tick);

    if (!newNodes.isEmpty()) {
      logger.debug("Nodes queued: {}", newNodes.size());
    }
  }

  private void forConnections(BlockPos pos, BiConsumer<BlockPos, BlockState> action) {
    if (this.level == null) {
      return;
    }
    var state = this.level.getBlockState(pos);
    var chargeSpec = this.getChargeSpec(state, pos);
    if (chargeSpec != null) {
      CONNECTION_MAPS.get(chargeSpec.connectType()).forEach((k, v) -> {
        var otherPos = pos.offset(k);
        var otherState = this.level.getBlockState(otherPos);
        var other = this.getChargeSpec(otherState, otherPos);
        if (other != null && CONNECTION_MAPS.get(other.connectType()).get(pos.subtract(otherPos))
            .contains(chargeSpec.connectType())) {
          action.accept(otherPos, otherState);
        }
      });
    }
  }

  /**
   * Add the node to the network and clean up any node that used to exist there
   */
  private void addNodeImpl(BlockPos pos, ChargeNode node) {
    if (!this.needsNode(pos, node.chargeSpec))
      return;

    var oldNode = this.nodes.put(pos.immutable(), node);

    // update the battery in the save data tracker
    if (node.chargeBattery != null) {
      this.chargeSavedData.initBattery(node.chargeBattery);
    } else {
      this.chargeSavedData.removeBattery(pos);
    }

    // clean up any preexisting node
    if (oldNode != null) {
      oldNode.invalid = true;
      if (oldNode.chargeGrid.isActive()) {
        node.chargeGrid = oldNode.chargeGrid;
        node.chargeGrid.add(node);
      }
      oldNode.chargeGrid = NULL_GRID;
    }

    if (node.isGridNull()) {
      node.constructGrid();
    }
  }

  private void removeNodeImpl(BlockPos pos) {
    var chargeNode = this.nodes.remove(pos);
    if (chargeNode != null) {
      chargeNode.invalid = true;
      chargeNode.chargeGrid.destroy(true);
    }
    this.chargeSavedData.removeBattery(pos);
  }

  @Override
  public boolean addNode(BlockPos pos, BlockState state) {
    var chargeSpec = this.getChargeSpec(state, pos);
    if (chargeSpec != null && this.needsNode(pos, chargeSpec)) {
      pos = pos.immutable();
      logger.debug("Registering Node: {}->{}", pos, chargeSpec);
      this.queue.put(pos, new ChargeNode(pos, chargeSpec));
      return true;
    }
    return false;
  }

  private boolean needsNode(BlockPos pos, ChargeBlock.Spec chargeSpec) {
    var node = this.nodes.get(pos);
    return node == null || !node.isValid() || !Objects.equals(node.chargeSpec, chargeSpec);
  }

  @Nullable
  private ChargeBlock.Spec getChargeSpec(BlockState state, BlockPos pos) {
    if (this.level == null) {
      return null;
    }
    if (state.getBlock() instanceof ChargeBlock chargeBlock) {
      return chargeBlock.getChargeSpecs(state, this.level, pos).get(this.network);
    }
    return null;
  }

  @Override
  public void removeNode(BlockPos pos) {
    this.queue.put(pos.immutable(), null);
  }

  public ChargeGrid grid(BlockPos pos) {
    return this.access(pos).getGrid();
  }

  @Override
  public ChargeNode access(BlockPos pos) {
    var node = this.nodes.get(pos);
    if (node != null && !node.isValid()) {
      this.removeNodeImpl(pos);
      node = null;
    }
    if (node == null && this.level != null) {
      var state = this.level.getBlockState(pos);
      var chargeSpec = this.getChargeSpec(state, pos);
      if (chargeSpec != null) {
        pos = pos.immutable();
        node = new ChargeNode(pos, chargeSpec);
        this.addNodeImpl(pos, node);
      }
    }
    return node == null ? NULL_NODE : node;
  }

  public class ChargeGrid extends ForwardingSet<ChargeNode> {
    private final Set<ChargeNode> chargeNodes = new HashSet<>();
    private final List<ChargeStorageBlockImpl> batteries = new ArrayList<>();
    private boolean invalid;
    private float totalLosses;
    private int chargeUsedThisTick;
    private float averageUsagePerTick;

    @Override
    protected Set<ChargeNode> delegate() {
      return chargeNodes;
    }

    @Override
    public boolean add(ChargeNode chargeNode) {
      if (!chargeNode.isValid()) {
        return false;
      }

      var added = super.add(chargeNode);
      if (added) {
        totalLosses += chargeNode.chargeSpec.losses();
      }
      chargeNode.chargeGrid = this;
      this.batteries.removeIf(b -> b.getBlockPos().equals(chargeNode.pos));
      if (chargeNode.chargeBattery != null) {
        this.batteries.removeIf(b -> b.getBlockPos().equals(chargeNode.pos));
        this.batteries.add(chargeNode.chargeBattery);
        sortBatteries();
      } else {
        ChargeNetworkImpl.this.chargeSavedData.removeBattery(chargeNode.pos);
      }
      return added;
    }

    private void sortBatteries() {
      this.batteries.sort(Comparator.comparing(ChargeStorage::getState)
          .thenComparing(Comparator.comparing(ChargeStorage::getEfficiency).reversed()));
    }

    @Override
    public boolean addAll(Collection<? extends ChargeNode> collection) {
      return standardAddAll(collection);
    }

    @Override
    public boolean remove(Object object) {
      throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeAll(Collection<?> collection) {
      throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeIf(Predicate<? super ChargeNode> filter) {
      throw new UnsupportedOperationException();
    }

    @Override
    public boolean retainAll(Collection<?> collection) {
      throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<ChargeNode> iterator() {
      return Iterators.unmodifiableIterator(super.iterator());
    }

    protected void destroy(boolean touchNodes) {
      logger.debug("Destroying grid: {}", this);
      invalid = true;
      totalLosses = 0.0F;
      if (touchNodes) {
        forEach(n -> n.chargeGrid = NULL_GRID);
      }
      batteries.clear();
      chargeNodes.clear();
      grids.remove(this);
    }

    @Override
    public void clear() {
      throw new UnsupportedOperationException();
    }

    private void tick() {
      this.sortBatteries();

      this.removeCharge(Mth.floor(this.getLosses()), false);

      // balance the charge in all the rechargeable batteries in the grid
      var rechargeable =
          this.batteries(ChargeStorage.State.RECHARGEABLE).collect(Collectors.toSet());

      var capacity = rechargeable.stream().mapToInt(ChargeStorage::getMaxEnergyStored).sum();
      if (capacity > 0) {
        var charge = rechargeable.stream().mapToInt(ChargeStorage::getEnergyStored).sum();
        final var neededCharge = capacity - charge;
        if (neededCharge > 0) {
          charge += this.removeCharge(this.batteries(ChargeStorage.State.SOURCE).toList(),
              neededCharge, false);
        }
        final var chargeLevel = charge / (float) capacity;
        rechargeable.forEach(bat -> bat.setEnergyStored(
            Mth.floor(chargeLevel * bat.getMaxEnergyStored())));
      }

      batteries.forEach(bat -> {
        bat.tick();
        chargeSavedData.updateBatteryRecord(bat);
      });

      // track usage patterns
      averageUsagePerTick = (averageUsagePerTick * 49.0F + chargeUsedThisTick) / 50.0F;
      chargeUsedThisTick = 0;
    }

    private Stream<ChargeStorageBlockImpl> batteries(ChargeStorage.State... state) {
      List<ChargeStorage.State> list = Arrays.asList(state);
      return batteries.stream().filter(b -> list.contains(b.getState()));
    }

    private Stream<ChargeStorageBlockImpl> activeBatteries() {
      return batteries.stream().filter(b -> b.getState() != ChargeStorage.State.DISABLED);
    }

    public int getCharge() {
      return activeBatteries().mapToInt(ChargeStorage::getEnergyStored).sum();
    }

    public int getCapacity() {
      return activeBatteries().mapToInt(ChargeStorage::getMaxEnergyStored).sum();
    }

    public float getChargeLevel() {
      return (float) getCharge() / (float) getCapacity();
    }

    public int getAvailableCharge() {
      return activeBatteries().mapToInt(ChargeStorage::getAvailableCharge).sum();
    }

    public int getPotentialDraw() {
      return activeBatteries().mapToInt(ChargeStorage::getPotentialDraw).sum();
    }

    public int getMaxDraw() {
      return activeBatteries().mapToInt(ChargeStorage::getMaxDraw).sum();
    }

    public float getEfficiency() {
      return (float) this.activeBatteries()
          .mapToDouble(ChargeStorage::getEfficiency)
          .average()
          .orElse(1.0);
    }

    public int getComparatorOutput() {
      double capacity = getCapacity();
      if (capacity <= 0.0)
        return 0;
      double level = getCharge() / capacity;
      return Math.round((float) (15.0 * level));
    }

    public float getLosses() {
      return totalLosses * RailcraftConfig.SERVER.lossMultiplier.get().floatValue();
    }

    public float getAverageUsagePerTick() {
      return this.averageUsagePerTick;
    }

    public float getUtilization() {
      if (this.isInfinite()) {
        return 0.0F;
      }
      var potentialDraw = this.getPotentialDraw();
      if (potentialDraw <= 0.0F) {
        return 1.0F;
      }
      return Math.min(this.averageUsagePerTick / potentialDraw, 1.0F);
    }

    public boolean isInfinite() {
      return batteries.stream().anyMatch(b -> b.getState() == ChargeStorage.State.INFINITE);
    }

    public boolean isActive() {
      return !isNull();
    }

    public boolean isNull() {
      return false;
    }

    /**
     * Remove the requested amount of charge if possible and return whether sufficient charge was
     * available to perform the operation.
     *
     * @return true if charge could be removed in full
     */
    public boolean useCharge(int amount, boolean simulate) {
      if (this.hasCapacity(amount)) {
        this.removeCharge(this.activeBatteries().toList(), amount, simulate);
        return true;
      }
      return false;
    }

    /**
     * Determines if the grid is capable of providing the required charge. The amount of charge that
     * can be withdraw from the grid is dependant on the overall efficiency of the grid and its
     * available charge.
     *
     * @param amount the requested charge
     * @return true if the grid can provide the power
     */
    public boolean hasCapacity(int amount) {
      return this.getAvailableCharge() >= amount;
    }

    /**
     * Remove up to the requested amount of charge and returns the amount removed.
     *
     * @return charge removed
     */
    public int removeCharge(int desiredAmount, boolean simulate) {
      return this.removeCharge(this.activeBatteries().toList(), desiredAmount, simulate);
    }

    /**
     * Remove up to the requested amount of charge and returns the amount removed.
     *
     * @return charge removed
     */
    private int removeCharge(List<ChargeStorageBlockImpl> batteries, int desiredAmount,
        boolean simulate) {
      var amountNeeded = desiredAmount;
      for (var battery : batteries) {
        amountNeeded -= battery.extractEnergy(amountNeeded, simulate);
        if (!simulate) {
          chargeSavedData.updateBatteryRecord(battery);
        }
        if (amountNeeded <= 0) {
          break;
        }
      }
      var chargeRemoved = desiredAmount - amountNeeded;
      if (!simulate) {
        this.chargeUsedThisTick += chargeRemoved;
      }
      return chargeRemoved;
    }

    @Override
    public String toString() {
      return String.format("ChargeGrid{id=%s,s=%d,b=%d}", "@" + System.identityHashCode(this),
          size(), batteries.size());
    }
  }

  private class NullGrid extends ChargeGrid {
    @Override
    protected Set<ChargeNode> delegate() {
      return Collections.emptySet();
    }

    @Override
    protected void destroy(boolean touchNodes) {}

    @Override
    public boolean isNull() {
      return true;
    }

    @Override
    public String toString() {
      return "ChargeGrid{NullGrid}";
    }
  }

  private class UsageRecorder {
    private final int ticksToRecord;
    private final Consumer<Double> usageConsumer;

    private double chargeUsed;
    private int ticksRecorded;

    public UsageRecorder(int ticksToRecord, Consumer<Double> usageConsumer) {
      this.ticksToRecord = ticksToRecord;
      this.usageConsumer = usageConsumer;
    }

    public void useCharge(double amount) {
      chargeUsed += amount;
    }

    public Boolean run() {
      ticksRecorded++;
      if (ticksRecorded > ticksToRecord) {
        usageConsumer.accept(chargeUsed / ticksToRecord);
        return false;
      }
      return true;
    }
  }

  @FunctionalInterface
  public interface ChargeListener {

    void chargeRemoved(ChargeNode node, int amount);
  }

  public class ChargeNode implements Charge.Access {

    protected final ChargeStorageBlockImpl chargeBattery;
    private final BlockPos pos;
    private final ChargeBlock.Spec chargeSpec;
    private ChargeGrid chargeGrid = NULL_GRID;
    private boolean invalid;
    private Optional<UsageRecorder> usageRecorder = Optional.empty();
    private final Collection<ChargeListener> listeners = new LinkedHashSet<>();

    private ChargeNode(BlockPos pos, ChargeBlock.Spec chargeSpec) {
      this.pos = pos.immutable();
      this.chargeSpec = chargeSpec;
      this.chargeBattery = chargeSpec.storageSpec() == null ? null
          : new ChargeStorageBlockImpl(this.pos, chargeSpec.storageSpec());
    }

    public ChargeBlock.Spec getChargeSpec() {
      return chargeSpec;
    }

    private void forConnections(Consumer<ChargeNode> action) {
      CONNECTION_MAPS.get(chargeSpec.connectType()).forEach((k, v) -> {
        BlockPos otherPos = pos.offset(k);
        ChargeNode other = nodes.get(otherPos);
        if (other != null && v.contains(other.chargeSpec.connectType())
            && CONNECTION_MAPS.get(other.chargeSpec.connectType()).get(pos.subtract(otherPos))
                .contains(chargeSpec.connectType())) {
          action.accept(other);
        }
      });
    }

    public ChargeGrid getGrid() {
      if (chargeGrid.isActive())
        return chargeGrid;
      constructGrid();
      return chargeGrid;
    }

    public void addListener(ChargeListener listener) {
      listeners.add(listener);
    }

    public void removeListener(ChargeListener listener) {
      listeners.remove(listener);
    }

    public void startUsageRecording(int ticksToRecord, Consumer<Double> usageConsumer) {
      usageRecorder = Optional.of(new UsageRecorder(ticksToRecord, usageConsumer));
      tickingNodes.add(this);
    }

    public boolean checkUsageRecordingCompletion() {
      usageRecorder = usageRecorder.filter(UsageRecorder::run);
      return !usageRecorder.isPresent();
    }

    @Override
    public boolean hasCapacity(int amount) {
      return chargeGrid.hasCapacity(amount);
    }

    @Override
    public boolean useCharge(int amount, boolean simulate) {
      boolean removed = this.chargeGrid.useCharge(amount, simulate);
      if (removed && !simulate) {
        this.listeners.forEach(c -> c.chargeRemoved(this, amount));
        this.usageRecorder.ifPresent(r -> r.useCharge(amount));
      }
      return removed;
    }

    @Override
    public int removeCharge(int desiredAmount, boolean simulate) {
      var removed = this.chargeGrid.removeCharge(desiredAmount, simulate);
      if (!simulate) {
        this.listeners.forEach(c -> c.chargeRemoved(this, removed));
        this.usageRecorder.ifPresent(r -> r.useCharge(removed));
      }
      return removed;
    }

    public boolean isValid() {
      return !this.invalid;
    }

    public boolean isGridNull() {
      return this.chargeGrid.isNull();
    }

    protected void constructGrid() {
      Set<ChargeNode> visitedNodes = new HashSet<>();
      visitedNodes.add(this);
      Set<ChargeNode> nullNodes = new HashSet<>();
      if (this.isGridNull()) {
        nullNodes.add(this);
      }
      Deque<ChargeNode> nodeQueue = new ArrayDeque<>();
      nodeQueue.add(this);
      TreeSet<ChargeGrid> seenGrids =
          new TreeSet<>(Comparator.comparingInt(ForwardingCollection::size));
      seenGrids.add(this.chargeGrid);
      ChargeNode nextNode;
      while ((nextNode = nodeQueue.poll()) != null) {
        nextNode.forConnections(connection -> {
          if (visitedNodes.contains(connection)) {
            return;
          }
          visitedNodes.add(connection);

          if (connection.isGridNull()) {
            nullNodes.add(connection);
            nodeQueue.addLast(connection);
          } else {
            seenGrids.add(connection.chargeGrid);
          }
        });
      }
      this.chargeGrid = Objects.requireNonNull(seenGrids.pollLast());
      if (this.chargeGrid.isNull()) {
        this.chargeGrid = new ChargeGrid();
        ChargeNetworkImpl.this.grids.add(this.chargeGrid);
      }
      int originalSize = this.chargeGrid.size();
      this.chargeGrid.addAll(nullNodes);
      seenGrids.forEach(grid -> {
        this.chargeGrid.addAll(grid);
        grid.destroy(false);
      });
      logger.debug("Constructing Grid: {}->{} Added {} nodes", this.pos, this.chargeGrid,
          this.chargeGrid.size() - originalSize);
    }

    @Override
    public void zap(Entity entity, Charge.DamageOrigin origin, float damage) {
      if (entity.level().isClientSide()) {
        return;
      }
      // logical server
      if (!ModEntitySelector.KILLABLE.test(entity)) {
        return;
      }

      int chargeCost = Mth.floor(damage) * CHARGE_PER_DAMAGE;

      if (this.hasCapacity(chargeCost)) {
        var remainingDamage = damage;
        if (entity instanceof LivingEntity livingEntity) {
          Map<EquipmentSlot, ChargeProtectionItem> protections = new EnumMap<>(EquipmentSlot.class);
          for (var slot : EquipmentSlot.values()) {
            var protection = getChargeProtection(livingEntity, slot);
            if (protection != null) {
              protections.put(slot, protection);
            }
          }
          for (var entry : protections.entrySet()) {
            if (remainingDamage > 0.1F) {
              var result = entry.getValue()
                  .zap(livingEntity.getItemBySlot(entry.getKey()), livingEntity, remainingDamage);
              entity.setItemSlot(entry.getKey(), result.stack());
              remainingDamage -= result.damagePrevented();
            } else {
              break;
            }
          }
        }
        if (remainingDamage > 0.1F
            && entity.hurt(origin == Charge.DamageOrigin.BLOCK
                ? RailcraftDamageSources.electric(level.registryAccess())
                : RailcraftDamageSources.trackElectric(level.registryAccess()), remainingDamage)) {
          this.removeCharge(chargeCost, false);
          Charge.zapEffectProvider().zapEffectDeath(entity.level(),
              entity.getX(), entity.getY(), entity.getZ());
        }
      }
    }

    @Nullable
    private ChargeProtectionItem getChargeProtection(LivingEntity entity, EquipmentSlot slot) {
      var stack = entity.getItemBySlot(slot);
      var item = stack.getItem();
      if (item instanceof ChargeProtectionItem protectionItem
          && protectionItem.isZapProtectionActive(stack, entity)) {
        return protectionItem;
      }
      return null;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o)
        return true;
      if (o == null || getClass() != o.getClass())
        return false;

      ChargeNode that = (ChargeNode) o;

      return pos.equals(that.pos);
    }

    @Override
    public int hashCode() {
      return pos.hashCode();
    }

    @Override
    public Optional<ChargeStorageBlockImpl> storage() {
      return Optional.ofNullable(this.chargeBattery);
    }

    @Override
    public int getComparatorOutput() {
      return getGrid().getComparatorOutput();
    }

    @Override
    public String toString() {
      String string = String.format("ChargeNode{%s}|%s", pos, chargeSpec.toString());
      if (this.chargeBattery != null) {
        string += "|State: " + this.chargeBattery.getState();
      }
      return string;
    }
  }

  public class NullNode extends ChargeNode {
    public NullNode() {
      super(new BlockPos(0, 0, 0),
          new ChargeBlock.Spec(ChargeBlock.ConnectType.BLOCK, 0));
    }

    @Override
    public Optional<ChargeStorageBlockImpl> storage() {
      return Optional.empty();
    }

    @Override
    public boolean isValid() {
      return false;
    }

    @Override
    public ChargeGrid getGrid() {
      return NULL_GRID;
    }

    @Override
    public boolean hasCapacity(int amount) {
      return false;
    }

    @Override
    public boolean useCharge(int amount, boolean simulate) {
      return false;
    }

    @Override
    public int removeCharge(int desiredAmount, boolean simulate) {
      return 0;
    }

    @Override
    protected void constructGrid() {}

    @Override
    public String toString() {
      return "ChargeNode{NullNode}";
    }
  }

  static {
    EnumSet<ChargeBlock.ConnectType> any = EnumSet.allOf(ChargeBlock.ConnectType.class);
    EnumSet<ChargeBlock.ConnectType> notWire =
        EnumSet.complementOf(EnumSet.of(ChargeBlock.ConnectType.WIRE));
    EnumSet<ChargeBlock.ConnectType> track = EnumSet.of(ChargeBlock.ConnectType.TRACK);
    EnumSet<ChargeBlock.ConnectType> notFlat = EnumSet
        .complementOf(EnumSet.of(ChargeBlock.ConnectType.TRACK, ChargeBlock.ConnectType.SLAB));
    ConnectionMap positions;

    // BLOCK

    positions = new ConnectionMap();
    for (Direction facing : Direction.values()) {
      positions.put(facing.getNormal(), any);
    }
    CONNECTION_MAPS.put(ChargeBlock.ConnectType.BLOCK, positions);

    // SLAB
    positions = new ConnectionMap();

    positions.put(new BlockPos(+1, 0, 0), notWire);
    positions.put(new BlockPos(-1, 0, 0), notWire);

    positions.put(new BlockPos(0, -1, 0), any);

    positions.put(new BlockPos(0, 0, +1), notWire);
    positions.put(new BlockPos(0, 0, -1), notWire);

    CONNECTION_MAPS.put(ChargeBlock.ConnectType.SLAB, positions);

    // TRACK
    positions = new ConnectionMap();

    positions.put(new BlockPos(+1, 0, 0), notWire);
    positions.put(new BlockPos(-1, 0, 0), notWire);

    positions.put(new BlockPos(+1, +1, 0), track);
    positions.put(new BlockPos(+1, -1, 0), track);

    positions.put(new BlockPos(-1, +1, 0), track);
    positions.put(new BlockPos(-1, -1, 0), track);

    positions.put(new BlockPos(0, -1, 0), any);

    positions.put(new BlockPos(0, 0, +1), notWire);
    positions.put(new BlockPos(0, 0, -1), notWire);

    positions.put(new BlockPos(0, +1, +1), track);
    positions.put(new BlockPos(0, -1, +1), track);

    positions.put(new BlockPos(0, +1, -1), track);
    positions.put(new BlockPos(0, -1, -1), track);

    CONNECTION_MAPS.put(ChargeBlock.ConnectType.TRACK, positions);

    // WIRE
    positions = new ConnectionMap();

    positions.put(new BlockPos(+1, 0, 0), notFlat);
    positions.put(new BlockPos(-1, 0, 0), notFlat);
    positions.put(new BlockPos(0, +1, 0), any);
    positions.put(new BlockPos(0, -1, 0), notFlat);
    positions.put(new BlockPos(0, 0, +1), notFlat);
    positions.put(new BlockPos(0, 0, -1), notFlat);

    CONNECTION_MAPS.put(ChargeBlock.ConnectType.WIRE, positions);
  }

  private static class ConnectionMap
      extends ForwardingMap<Vec3i, Set<ChargeBlock.ConnectType>> {

    private final Map<Vec3i, Set<ChargeBlock.ConnectType>> delegate;

    public ConnectionMap() {
      delegate = new HashMap<>();
    }

    @Override
    protected Map<Vec3i, Set<ChargeBlock.ConnectType>> delegate() {
      return delegate;
    }

    @Override
    public Set<ChargeBlock.ConnectType> get(@Nullable Object key) {
      var ret = super.get(key);
      return ret == null ? EnumSet.noneOf(ChargeBlock.ConnectType.class) : ret;
    }
  }
}
