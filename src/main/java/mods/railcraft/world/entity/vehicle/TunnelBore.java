package mods.railcraft.world.entity.vehicle;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiFunction;
import org.jetbrains.annotations.Nullable;
import mods.railcraft.RailcraftConfig;
import mods.railcraft.api.carts.Linkable;
import mods.railcraft.api.carts.RollingStock;
import mods.railcraft.api.carts.Side;
import mods.railcraft.api.carts.Train;
import mods.railcraft.api.carts.TunnelBoreHead;
import mods.railcraft.api.container.manipulator.SlotAccessor;
import mods.railcraft.api.core.CompoundTagKeys;
import mods.railcraft.api.track.TrackUtil;
import mods.railcraft.tags.RailcraftTags;
import mods.railcraft.util.EntitySearcher;
import mods.railcraft.util.LevelUtil;
import mods.railcraft.util.ModEntitySelector;
import mods.railcraft.util.container.ContainerMapper;
import mods.railcraft.util.container.ContainerTools;
import mods.railcraft.util.container.StackFilter;
import mods.railcraft.world.damagesource.RailcraftDamageSources;
import mods.railcraft.world.entity.RailcraftEntityTypes;
import mods.railcraft.world.inventory.TunnelBoreMenu;
import mods.railcraft.world.item.RailcraftItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseRailBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.RailShape;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.Tags;
import net.minecraftforge.entity.PartEntity;
import net.minecraftforge.event.level.BlockEvent;

public class TunnelBore extends RailcraftMinecart implements Linkable {

  public static final float SPEED = 0.03F;
  public static final float LENGTH = 6.2f;
  public static final float WIDTH = 3f;
  public static final float HEIGHT = 3f;
  public static final int MAX_FILL_DEPTH = 10;
  public static final int FAIL_DELAY = 200;
  public static final int STANDARD_DELAY = 5;
  public static final int LAYER_DELAY = 40;
  public static final int BALLAST_DELAY = 10;
  public static final int FUEL_CONSUMPTION = 12;
  public static final float HARDNESS_MULTIPLIER = 8;

  public static final Set<BlockState> MINEABLE_STATES = new HashSet<>();
  public static final Set<Block> MINEABLE_BLOCKS = Set.of(
      Blocks.CLAY,
      Blocks.SNOW,
      Blocks.CACTUS,
      Blocks.CARROTS,
      Blocks.MOSSY_COBBLESTONE,
      Blocks.COCOA,
      Blocks.WHEAT,
      Blocks.DEAD_BUSH,
      Blocks.FIRE,
      Blocks.GLOWSTONE,
      Blocks.ICE,
      Blocks.MELON,
      Blocks.MELON_STEM,
      Blocks.BROWN_MUSHROOM,
      Blocks.BROWN_MUSHROOM_BLOCK,
      Blocks.RED_MUSHROOM,
      Blocks.RED_MUSHROOM_BLOCK,
      Blocks.MYCELIUM,
      Blocks.NETHER_WART,
      Blocks.POTATOES,
      Blocks.PUMPKIN,
      Blocks.PUMPKIN_STEM,
      Blocks.SAND,
      Blocks.SANDSTONE,
      Blocks.SOUL_SAND,
      Blocks.STONE,
      Blocks.FARMLAND,
      Blocks.TORCH,
      Blocks.VINE,
      Blocks.COBWEB,
      Blocks.END_STONE);

  public static final Set<TagKey<Block>> MINEABLE_TAGS = Set.of(
      Tags.Blocks.ORES,
      Tags.Blocks.NETHERRACK,
      Tags.Blocks.COBBLESTONE,
      Tags.Blocks.OBSIDIAN,
      Tags.Blocks.GRAVEL,
      BlockTags.DIRT,
      BlockTags.LEAVES,
      BlockTags.SAPLINGS,
      BlockTags.LOGS,
      BlockTags.FLOWERS,
      RailcraftTags.Blocks.MAGIC_ORE);

  public static final Set<Block> REPLACEABLE_BLOCKS = Set.of(Blocks.TORCH);

  public static final Set<TagKey<Block>> REPLACEABLE_TAGS = Set.of(BlockTags.FLOWERS);

  private static final EntityDataAccessor<Boolean> HAS_FUEL =
      SynchedEntityData.defineId(TunnelBore.class, EntityDataSerializers.BOOLEAN);
  private static final EntityDataAccessor<Boolean> MOVING =
      SynchedEntityData.defineId(TunnelBore.class, EntityDataSerializers.BOOLEAN);
  private static final EntityDataAccessor<Direction> FACING =
      SynchedEntityData.defineId(TunnelBore.class, EntityDataSerializers.DIRECTION);
  private static final EntityDataAccessor<ItemStack> BORE_HEAD =
      SynchedEntityData.defineId(TunnelBore.class, EntityDataSerializers.ITEM_STACK);

  private final ContainerMapper fuelContainer =
      ContainerMapper.make(this, 1, 6).addFilters(StackFilter.FUEL);
  private final ContainerMapper ballastContainer =
      ContainerMapper.make(this, 7, 9).addFilters(StackFilter.BALLAST);
  private final ContainerMapper trackContainer =
      ContainerMapper.make(this, 16, 9).addFilters(StackFilter.TRACK);
  // protected static final int WATCHER_ID_BURN_TIME = 22;

  protected int delay;
  protected boolean placeRail;
  protected boolean placeBallast;
  protected boolean boreLayer;
  protected int boreRotationAngle;
  private boolean active;
  private int clock;
  private int burnTime;
  private int fuel;
  private final boolean constructed;
  private final TunnelBorePart[] parts;
  private final List<ContainerMapper> containers;

  public TunnelBore(EntityType<TunnelBore> type, Level level) {
    this(level, 0, 0, 0, Direction.SOUTH);
  }

  public TunnelBore(Level level, double x, double y, double z, Direction facing) {
    super(RailcraftEntityTypes.TUNNEL_BORE.get(), x, y, z, level);
    setFacing(facing);

    float headW = 1.5F;
    float headH = 2.6F;
    float headSO = 0.7F;
    this.parts = new TunnelBorePart[] {
        new TunnelBorePart(this, headW, headH, 1.85F, -headSO), // head1
        new TunnelBorePart(this, headW, headH, 1.85F, headSO), // head2
        new TunnelBorePart(this, headW, headH, 2.3F, -headSO), // head3
        new TunnelBorePart(this, headW, headH, 2.3F, headSO), // head4
        new TunnelBorePart(this, 2.0F, 1.9F, 0.6F), // body
        new TunnelBorePart(this, 1.6F, 1.4F, -1F), // tail1
        new TunnelBorePart(this, 1.6F, 1.4F, -2.2F), // tail2
    };
    this.constructed = true;
    this.containers = List.of(this.fuelContainer, this.ballastContainer, this.trackContainer);

    // Forge: Fix MC-158205: Make sure part ids are successors of parent mob id
    this.setId(ENTITY_COUNTER.getAndAdd(this.parts.length + 1) + 1);
  }

  @Override
  public void setId(int id) {
    super.setId(id);
    // Forge: Fix MC-158205: Set part ids to successor of parent mob id
    for (int i = 0; i < this.parts.length; i++)
      this.parts[i].setId(id + i + 1);
  }

  @Override
  public ItemStack getPickResult() {
    return RailcraftItems.TUNNEL_BORE.get().getDefaultInstance();
  }

  public static void addMineableBlock(Block block) {
    addMineableBlock(block.defaultBlockState());
  }

  public static void addMineableBlock(BlockState blockState) {
    MINEABLE_STATES.add(blockState);
  }

  public boolean canHeadHarvestBlock(ItemStack head, BlockState targetState) {
    return !head.isEmpty()
        && (!targetState.requiresCorrectToolForDrops() || head.isCorrectToolForDrops(targetState));
  }

  private boolean isMineableBlock(BlockState blockState) {
    return RailcraftConfig.SERVER.boreMinesAllBlocks.get()
        || MINEABLE_BLOCKS.contains(blockState.getBlock())
        || MINEABLE_STATES.contains(blockState)
        || MINEABLE_TAGS.stream().anyMatch(blockState::is);
  }

  @Override
  protected void defineSynchedData() {
    super.defineSynchedData();
    entityData.define(HAS_FUEL, false);
    entityData.define(MOVING, false);
    entityData.define(BORE_HEAD, ItemStack.EMPTY);
    entityData.define(FACING, Direction.NORTH);
    // entityData.define(WATCHER_ID_BURN_TIME, Integer.valueOf(0));
  }

  public boolean isMinecartPowered() {
    return entityData.get(HAS_FUEL);
  }

  public void setMinecartPowered(boolean powered) {
    entityData.set(HAS_FUEL, powered);
  }

  @Override
  public boolean hurt(DamageSource source, float damage) {
    if (!this.level().isClientSide() && !this.isRemoved()) {
      if (this.isInvulnerableTo(source)) {
        return false;
      } else {
        this.setHurtDir(-this.getHurtDir());
        this.setHurtTime(10);
        this.markHurt();
        this.setDamage(this.getDamage() + damage * 10);
        this.gameEvent(GameEvent.ENTITY_DAMAGE, source.getEntity());
        boolean flag =
            (source.getEntity() instanceof Player player) && player.getAbilities().instabuild;
        if (flag || this.getDamage() > 120) {
          this.ejectPassengers();
          if (flag && !this.hasCustomName()) {
            this.remove(RemovalReason.KILLED);
          } else {
            this.destroy(source);
          }
        }
        return true;
      }
    } else {
      return true;
    }
  }

  private void setYaw() {
    var yaw = switch (this.getFacing()) {
      case NORTH -> 180;
      case EAST -> 270;
      case SOUTH -> 0;
      case WEST -> 90;
      default -> 0;
    };
    this.setRot(yaw, this.getXRot());
  }

  @Override
  public int getContainerSize() {
    return 25;
  }

  @Override
  public boolean isPushable() {
    return false;
  }

  @Override
  public void setPos(double x, double y, double z) {
    if (!this.constructed) {
      super.setPos(x, y, z);
      return;
    }

    this.setPosRaw(x, y, z);

    double halfWidth = WIDTH / 2.0;
    double height = this.getBbHeight();
    double len = LENGTH / 2.0;
    double minX = x;
    double maxX = x;
    double minZ = z;
    double maxZ = z;
    if (this.getFacing() == Direction.WEST || this.getFacing() == Direction.EAST) {
      minX -= len;
      maxX += len;
      minZ -= halfWidth;
      maxZ += halfWidth;
    } else {
      minX -= halfWidth;
      maxX += halfWidth;
      minZ -= len;
      maxZ += len;
    }

    this.setBoundingBox(new AABB(minX, y, minZ, maxX, y + height, maxZ));
  }

  @Override
  public void tick() {
    clock++;

    if (!this.level().isClientSide()) {
      if (clock % 64 == 0) {
        forceUpdateBoreHead();
        setMinecartPowered(false);
        setMoving(false);
      }

      stockBallast();
      stockTracks();
    }

    super.tick();

    for (Entity part : parts) {
      part.tick();
    }

    if (!this.level().isClientSide()) {

      updateFuel();
      // if(update % 64 == 0){
      // System.out.println("bore tick");
      // }

      if (hasFuel() && getDelay() == 0) {
        setActive(true);
        // System.out.println("Yaw = " + MathHelper.floor_double(this.yRot));

        RailShape dir = RailShape.NORTH_SOUTH;
        if (getFacing() == Direction.WEST || getFacing() == Direction.EAST) {
          dir = RailShape.EAST_WEST;
        }

        if (getDelay() == 0) {
          float offset = 1.5f;
          BlockPos targetPos = new BlockPos(getPositionAhead(offset)).below();

          if (placeBallast) {
            boolean placed = placeBallast(targetPos);
            if (placed) {
              setDelay(STANDARD_DELAY);
            } else {
              setDelay(FAIL_DELAY);
              setActive(false);
            }
            placeBallast = false;
          } else if (!Block.canSupportRigidBlock(this.level(), targetPos)) {
            placeBallast = true;
            setDelay(BALLAST_DELAY);
          }
        }

        if (getDelay() == 0) {
          float offset = 0.8f;
          BlockPos targetPos = new BlockPos(getPositionAhead(offset));
          BlockState existingState = this.level().getBlockState(targetPos);

          if (placeRail) {
            boolean placed = placeTrack(targetPos, existingState, dir);
            if (placed) {
              setDelay(STANDARD_DELAY);
            } else {
              setDelay(FAIL_DELAY);
              setActive(false);
            }
            placeRail = false;
          } else if (BaseRailBlock.isRail(existingState)) {
            if (dir != TrackUtil.getTrackDirection(this.level(), targetPos, this)) {
              TrackUtil.setRailShape(this.level(), targetPos, dir);
              setDelay(STANDARD_DELAY);
            }
          } else if (existingState.isAir()
              || REPLACEABLE_BLOCKS.contains(existingState.getBlock())) {
            placeRail = true;
            setDelay(STANDARD_DELAY);
          } else {
            setDelay(FAIL_DELAY);
            setActive(false);
          }
        }

        if (getDelay() == 0) {
          float offset = 3.3f;
          BlockPos targetPos = new BlockPos(getPositionAhead(offset));

          if (boreLayer) {
            boolean bored = boreLayer(targetPos, dir);
            if (bored) {
              setDelay(LAYER_DELAY);
            } else {
              setDelay(FAIL_DELAY);
              setActive(false);
            }
            boreLayer = false;
          } else if (checkForLava(targetPos, dir)) {
            setDelay(FAIL_DELAY);
            setActive(false);
          } else {
            setDelay(Mth.ceil(getLayerHardness(targetPos, dir)));
            if (getDelay() != 0) {
              boreLayer = true;
            }
          }
        }
      }

      if (isMinecartPowered()) {
        var headPos = getPositionAhead(3.3);
        double size = 0.8;
        var entities = EntitySearcher.findLiving()
            .and(ModEntitySelector.KILLABLE)
            .box(builder -> builder
                .setBoundsToPoint(headPos)
                .inflateHorizontally(size)
                .raiseCeiling(2)
                .build())
            .list(this.level());
        entities
            .forEach(e -> e.hurt(RailcraftDamageSources.bore(this.level().registryAccess()), 2));

        ItemStack head = getItem(0);
        if (!head.isEmpty()) {
          head.hurt(entities.size(), this.random, MinecartUtil.getFakePlayer(this));
        }
      }

      setMoving(hasFuel() && getDelay() == 0);

      if (getDelay() > 0) {
        setDelay(getDelay() - 1);
      }
    }

    Vec3 motion = this.getDeltaMovement();
    if (isMoving()) {
      float factorX = -Mth.sin((float) Math.toRadians(this.getYRot()));
      float factorZ = Mth.cos((float) Math.toRadians(this.getYRot()));
      this.setDeltaMovement(SPEED * factorX, motion.y(), SPEED * factorZ);
    } else {
      this.setDeltaMovement(0.0D, motion.y(), 0.0D);
    }

    emitParticles();

    if (isMinecartPowered()) {
      boreRotationAngle += 5;
    }
  }

  @Override
  public float getMaxCartSpeedOnRail() {
    return SPEED;
  }

  @Override
  public Item getDropItem() {
    return RailcraftItems.TUNNEL_BORE.get();
  }

  private void updateFuel() {
    if (!this.level().isClientSide()) {
      if (isMinecartPowered()) {
        spendFuel();
      }
      stockFuel();
      if (outOfFuel()) {
        addFuel();
      }
      setMinecartPowered(hasFuel() && isActive());
    }
  }

  protected BlockPos getPositionAhead(double offset) {
    double x = this.getX();
    double z = this.getZ();

    if (getFacing() == Direction.EAST) {
      x += offset;
    } else if (getFacing() == Direction.WEST) {
      x -= offset;
    }

    if (getFacing() == Direction.NORTH) {
      z -= offset;
    } else if (getFacing() == Direction.SOUTH) {
      z += offset;
    }
    return BlockPos.containing(x, this.getY(), z);
  }

  protected double getOffsetX(double x, double forwardOffset, double sideOffset) {
    return switch (getFacing()) {
      case NORTH -> x + sideOffset;
      case SOUTH -> x - sideOffset;
      case EAST -> x + forwardOffset;
      case WEST -> x - forwardOffset;
      default -> x;
    };
  }

  protected double getOffsetZ(double z, double forwardOffset, double sideOffset) {
    return switch (getFacing()) {
      case NORTH -> z - forwardOffset;
      case SOUTH -> z + forwardOffset;
      case EAST -> z - sideOffset;
      case WEST -> z + sideOffset;
      default -> z;
    };
  }

  protected void emitParticles() {
    if (isMinecartPowered()) {
      double randomFactor = 0.125;

      double forwardOffset = -0.35;
      double smokeYOffset = 2.8;
      double flameYOffset = 1.1;
      double smokeSideOffset = 0.92;
      double flameSideOffset = 1.14;
      double smokeX1 = this.getX();
      double smokeX2 = this.getX();
      double smokeZ1 = this.getZ();
      double smokeZ2 = this.getZ();

      double flameX1 = this.getX();
      double flameX2 = this.getX();
      double flameZ1 = this.getZ();
      double flameZ2 = this.getZ();
      if (getFacing() == Direction.NORTH) {
        smokeX1 += smokeSideOffset;
        smokeX2 -= smokeSideOffset;
        smokeZ1 += forwardOffset;
        smokeZ2 += forwardOffset;

        flameX1 += flameSideOffset;
        flameX2 -= flameSideOffset;
        flameZ1 += forwardOffset + (this.random.nextGaussian() * randomFactor);
        flameZ2 += forwardOffset + (this.random.nextGaussian() * randomFactor);
      } else if (getFacing() == Direction.EAST) {
        smokeX1 -= forwardOffset;
        smokeX2 -= forwardOffset;
        smokeZ1 += smokeSideOffset;
        smokeZ2 -= smokeSideOffset;

        flameX1 -= forwardOffset + (this.random.nextGaussian() * randomFactor);
        flameX2 -= forwardOffset + (this.random.nextGaussian() * randomFactor);
        flameZ1 += flameSideOffset;
        flameZ2 -= flameSideOffset;
      } else if (getFacing() == Direction.SOUTH) {
        smokeX1 += smokeSideOffset;
        smokeX2 -= smokeSideOffset;
        smokeZ1 -= forwardOffset;
        smokeZ2 -= forwardOffset;

        flameX1 += flameSideOffset;
        flameX2 -= flameSideOffset;
        flameZ1 -= forwardOffset + (this.random.nextGaussian() * randomFactor);
        flameZ2 -= forwardOffset + (this.random.nextGaussian() * randomFactor);
      } else if (getFacing() == Direction.WEST) {
        smokeX1 += forwardOffset;
        smokeX2 += forwardOffset;
        smokeZ1 += smokeSideOffset;
        smokeZ2 -= smokeSideOffset;

        flameX1 += forwardOffset + (this.random.nextGaussian() * randomFactor);
        flameX2 += forwardOffset + (this.random.nextGaussian() * randomFactor);
        flameZ1 += flameSideOffset;
        flameZ2 -= flameSideOffset;
      }

      if (this.random.nextInt(4) == 0) {
        this.level().addParticle(ParticleTypes.LARGE_SMOKE,
            smokeX1, this.getY() + smokeYOffset, smokeZ1,
            0.0D, 0.0D, 0.0D);
        this.level().addParticle(ParticleTypes.FLAME, flameX1,
            this.getY() + flameYOffset + (this.random.nextGaussian() * randomFactor), flameZ1,
            0.0D, 0.0D, 0.0D);
      }
      if (this.random.nextInt(4) == 0) {
        this.level().addParticle(ParticleTypes.LARGE_SMOKE,
            smokeX2, this.getY() + smokeYOffset, smokeZ2,
            0.0D, 0.0D, 0.0D);
        this.level().addParticle(ParticleTypes.FLAME, flameX2,
            this.getY() + flameYOffset + (this.random.nextGaussian() * randomFactor), flameZ2,
            0.0D, 0.0D, 0.0D);
      }
    }
  }

  protected void stockBallast() {
    var stack = RollingStock.getOrThrow(this).pullItem(this.ballastContainer::canFit);
    if (!stack.isEmpty()) {
      this.ballastContainer.insert(stack);
    }
  }

  @SuppressWarnings("deprecation")
  protected boolean placeBallast(BlockPos targetPos) {
    if (!Block.canSupportRigidBlock(this.level(), targetPos)) {
      return this.ballastContainer.stream()
          .filter(slot -> slot.hasItem()
              && slot.item().getItem() instanceof BlockItem blockItem
              && blockItem.getBlock().builtInRegistryHolder().is(RailcraftTags.Blocks.BALLAST))
          .findFirst()
          .map(slot -> {
            var searchPos = targetPos.mutable();
            for (int i = 0; i < MAX_FILL_DEPTH; i++) {
              searchPos.move(Direction.DOWN);
              if (Block.canSupportRigidBlock(this.level(), searchPos)) {
                // Fill ballast
                var state =
                    ContainerTools.getBlockStateFromStack(slot.item(), this.level(), targetPos);
                if (state != null) {
                  slot.extract();
                  this.level().setBlockAndUpdate(targetPos, state);
                  return true;
                }
              } else {
                BlockState state = this.level().getBlockState(searchPos);
                if (!state.isAir() && !state.liquid()) {
                  // Break other blocks first
                  LevelUtil.playerRemoveBlock(this.level(), searchPos.immutable(),
                      MinecartUtil.getFakePlayer(this),
                      this.level().getGameRules().getBoolean(GameRules.RULE_DOBLOCKDROPS)
                          && !RailcraftConfig.SERVER.boreDestroysBlocks.get());
                }
              }
            }
            return false;
          })
          .orElse(false);
    }
    return false;
  }

  protected void stockTracks() {
    var stack = RollingStock.getOrThrow(this).pullItem(this.trackContainer::canFit);
    if (!stack.isEmpty()) {
      this.trackContainer.insert(stack);
    }
  }

  protected boolean placeTrack(BlockPos targetPos, BlockState oldState, RailShape shape) {
    Player owner = MinecartUtil.getFakePlayer(this);

    if (REPLACEABLE_BLOCKS.contains(oldState.getBlock())) {
      LevelUtil.destroyBlock(this.level(), targetPos, owner, true);
    }

    if (oldState.isAir()
        && Block.canSupportRigidBlock(this.level(), targetPos.below())) {
      return this.trackContainer.stream()
          .filter(SlotAccessor::hasItem)
          .peek(slot -> {
            var placed =
                TrackUtil.placeRailAt(slot.item(), (ServerLevel) this.level(), targetPos, shape);
            if (placed) {
              slot.extract();
            }
          })
          .findFirst()
          .isPresent();
    }
    return false;
  }

  protected boolean checkForLava(BlockPos targetPos, RailShape dir) {
    int xStart = targetPos.getX() - 1;
    int zStart = targetPos.getZ() - 1;
    int xEnd = targetPos.getX() + 1;
    int zEnd = targetPos.getZ() + 1;
    if (dir == RailShape.NORTH_SOUTH) {
      xStart--;
      xEnd++;
    } else {
      zStart--;
      zEnd++;
    }

    int y = targetPos.getY();

    for (BlockPos blockPos : BlockPos.betweenClosed(xStart, y, zStart, xEnd, y + 3, zEnd)) {
      Fluid fluid = this.level().getFluidState(blockPos).getType();
      if (fluid == Fluids.LAVA || fluid == Fluids.FLOWING_LAVA) {
        return true;
      }
    }

    return false;
  }

  private <T> T layerAction(BlockPos targetPos, RailShape trackShape, T initialValue,
      BiFunction<BlockPos, RailShape, T> action, BiFunction<T, T, T> sum) {
    T returnValue = initialValue;

    int x = targetPos.getX();
    int y = targetPos.getY();
    int z = targetPos.getZ();
    for (int jj = y; jj < y + 3; jj++) {
      returnValue = sum.apply(returnValue, action.apply(new BlockPos(x, jj, z), trackShape));
    }

    if (trackShape == RailShape.NORTH_SOUTH) {
      x--;
    } else {
      z--;
    }
    for (int jj = y; jj < y + 3; jj++) {
      returnValue = sum.apply(returnValue, action.apply(new BlockPos(x, jj, z), trackShape));
    }

    x = targetPos.getX();
    z = targetPos.getZ();
    if (trackShape == RailShape.NORTH_SOUTH) {
      x++;
    } else {
      z++;
    }
    for (int jj = y; jj < y + 3; jj++) {
      returnValue = sum.apply(returnValue, action.apply(new BlockPos(x, jj, z), trackShape));
    }
    return returnValue;
  }

  protected boolean boreLayer(BlockPos targetPos, RailShape dir) {
    return layerAction(targetPos, dir, true, this::mineBlock, (s, r) -> s && r);
  }

  /**
   * Mines the block forward, returns true if it's clear. Now 1.17 compliant!
   *
   * @return true if the target block is clear
   */
  protected boolean mineBlock(BlockPos targetPos, RailShape preferredShape) {
    BlockState targetState = this.level().getBlockState(targetPos);
    if (targetState.isAir()) {
      return true;
    }

    if (BaseRailBlock.isRail(targetState)) {
      var targetShape =
          TrackUtil.getTrackDirection(this.level(), targetPos, targetState, this);
      if (preferredShape == targetShape) {
        return true;
      }
    } else if (targetState.getBlock() == Blocks.TORCH) {
      return true;
    }

    ItemStack head = getItem(0);
    if (head.isEmpty()) {
      return false;
    }

    if (!canMineBlock(targetPos, targetState)) {
      return false;
    }

    ServerPlayer fakePlayer = MinecartUtil.getFakePlayerWith(this, head);

    // Fires break event within; harvest handled separately
    BlockEvent.BreakEvent breakEvent =
        new BlockEvent.BreakEvent(this.level(), targetPos, targetState, fakePlayer);
    MinecraftForge.EVENT_BUS.post(breakEvent);

    if (breakEvent.isCanceled()) {
      return false;
    }

    if (!RailcraftConfig.SERVER.boreDestroysBlocks.get()
        && this.level().getGameRules().getBoolean(GameRules.RULE_DOBLOCKDROPS)) {
      targetState
          .getDrops(new LootParams.Builder((ServerLevel) this.level())
              .withParameter(LootContextParams.TOOL, head)
              .withParameter(LootContextParams.KILLER_ENTITY, this)
              .withParameter(LootContextParams.ORIGIN, this.position()))
          .forEach(stack -> {
            if (StackFilter.FUEL.test(stack)) {
              stack = this.fuelContainer.insert(stack);
            }

            if (!stack.isEmpty() && ContainerTools.isItemStackBlock(stack, Blocks.GRAVEL)) {
              stack = this.ballastContainer.insert(stack);
            }

            if (!stack.isEmpty()) {
              stack = RollingStock.getOrThrow(this).pushItem(stack);
            }

            if (!stack.isEmpty()) {
              Block.popResource(this.level(), this.blockPosition(), stack);
            }
          });
    }

    LevelUtil.setAir(this.level(), targetPos);

    if (head.hurt(1, this.random, fakePlayer)) {
      this.setItem(0, ItemStack.EMPTY);
    }

    return true;
  }

  private boolean canMineBlock(BlockPos targetPos, BlockState existingState) {
    ItemStack head = getItem(0);
    if (existingState.getDestroySpeed(this.level(), targetPos) < 0) {
      return false;
    }
    return isMineableBlock(existingState) && canHeadHarvestBlock(head, existingState);
  }

  protected double getLayerHardness(BlockPos targetPos, RailShape dir) {
    double hardness = layerAction(targetPos, dir, 0F, this::getBlockHardness, Float::sum);
    hardness *= HARDNESS_MULTIPLIER;

    var boreSlot = this.getItem(0);
    if (!boreSlot.isEmpty() && boreSlot.getItem() instanceof TunnelBoreHead head) {
      double dig = head.getDigModifier();
      hardness /= dig;
      int e = boreSlot.getEnchantmentLevel(Enchantments.BLOCK_EFFICIENCY);
      hardness /= (e * e * 0.2d + 1);
    }

    hardness /= RailcraftConfig.SERVER.boreMiningSpeedMultiplier.get();

    return hardness;
  }

  protected float getBlockHardness(BlockPos pos, RailShape dir) {
    var blockState = this.level().getBlockState(pos);

    if (blockState.isAir()) {
      return 0;
    }

    if (BaseRailBlock.isRail(blockState)) {
      var trackMeta = TrackUtil.getTrackDirection(this.level(), pos, blockState, this);
      if (dir == trackMeta) {
        return 0;
      }
    }

    if (blockState.getBlock() == Blocks.TORCH) {
      return 0;
    }

    if (blockState.getBlock() == Blocks.OBSIDIAN) {
      return 15;
    }

    if (!canMineBlock(pos, blockState)) {
      return 0.1f;
    }

    float hardness = blockState.getDestroySpeed(this.level(), pos);
    if (hardness <= 0) {
      hardness = 0.1f;
    }
    return hardness;
  }

  @Override
  public boolean canCollideWith(Entity other) {
    return other instanceof LivingEntity;
  }

  public float getBoreRotationAngle() {
    return (float) Math.toRadians(boreRotationAngle);
  }

  @Override
  protected void addAdditionalSaveData(CompoundTag tag) {
    super.addAdditionalSaveData(tag);
    tag.putInt(CompoundTagKeys.FACING, getFacing().get3DDataValue());
    tag.putInt(CompoundTagKeys.DELAY, getDelay());
    tag.putBoolean(CompoundTagKeys.ACTIVE, isActive());
    tag.putInt(CompoundTagKeys.BURN_TIME, getBurnTime());
    tag.putInt(CompoundTagKeys.FUEL, this.fuel);
  }

  @Override
  protected void readAdditionalSaveData(CompoundTag tag) {
    super.readAdditionalSaveData(tag);
    setFacing(Direction.from3DDataValue(tag.getInt(CompoundTagKeys.FACING)));
    setDelay(tag.getInt(CompoundTagKeys.DELAY));
    setActive(tag.getBoolean(CompoundTagKeys.ACTIVE));
    setBurnTime(tag.getInt(CompoundTagKeys.BURN_TIME));
    setFuel(tag.getInt(CompoundTagKeys.FUEL));
  }

  protected int getDelay() {
    return delay;
    // return entityData.get(WATCHER_ID_DELAY);
  }

  protected void setDelay(int i) {
    delay = i;
    // entityData.set(WATCHER_ID_DELAY, Integer.valueOf(i));
  }

  protected boolean isActive() {
    return active;
    // return entityData.get(WATCHER_ID_ACTIVE) != 0;
  }

  protected void setActive(boolean active) {
    this.active = active;
    var state = active ? Train.State.STOPPED : Train.State.NORMAL;
    if (!this.level().isClientSide()) {
      RollingStock.getOrThrow(this).train().setState(state);
    }
    // entityData.set(WATCHER_ID_ACTIVE, Byte.valueOf((byte)(active ? 1 : 0)));
  }

  protected boolean isMoving() {
    return this.entityData.get(MOVING);
  }

  protected void setMoving(boolean moving) {
    this.entityData.set(MOVING, moving);
  }

  public int getBurnTime() {
    return this.burnTime;
  }

  public void setBurnTime(int burnTime) {
    this.burnTime = burnTime;
  }

  public int getFuel() {
    return this.fuel;
  }

  public void setFuel(int fuel) {
    this.fuel = fuel;
  }

  public boolean outOfFuel() {
    return this.getFuel() <= FUEL_CONSUMPTION;
  }

  public boolean hasFuel() {
    return this.getFuel() > 0;
  }

  protected void stockFuel() {
    var stack = RollingStock.getOrThrow(this).pullItem(this.fuelContainer::canFit);
    if (!stack.isEmpty()) {
      this.fuelContainer.insert(stack);
    }
  }

  protected void addFuel() {
    int burn = 0;
    for (int slot = 0; slot < this.fuelContainer.getContainerSize(); slot++) {
      var stack = this.fuelContainer.getItem(slot);
      if (!stack.isEmpty()) {
        burn = ForgeHooks.getBurnTime(stack, null);
        if (burn > 0) {
          if (stack.getItem().hasCraftingRemainingItem(stack)) {
            this.fuelContainer.setItem(slot, stack.getItem().getCraftingRemainingItem(stack));
          } else {
            this.fuelContainer.removeItem(slot, 1);
          }
          break;
        }
      }
    }
    if (burn > 0) {
      this.setBurnTime(burn + this.getFuel());
      this.setFuel(this.getFuel() + burn);
    }
  }

  public int getBurnProgressScaled(int i) {
    int burn = this.getBurnTime();
    if (burn == 0) {
      return 0;
    }

    return this.getFuel() * i / burn;
  }

  protected void spendFuel() {
    this.setFuel(this.getFuel() - FUEL_CONSUMPTION);
  }

  protected void forceUpdateBoreHead() {
    var boreStack = this.getItem(0);
    if (!boreStack.isEmpty()) {
      boreStack = boreStack.copy();
    }
    this.entityData.set(BORE_HEAD, boreStack);
  }

  @Nullable
  public TunnelBoreHead getBoreHead() {
    var boreStack = this.entityData.get(BORE_HEAD);
    if (boreStack.getItem() instanceof TunnelBoreHead head) {
      return head;
    }
    return null;
  }

  @Override
  protected void applyNaturalSlowdown() {
    this.setDeltaMovement(this.getDeltaMovement()
        .multiply(CartConstants.STANDARD_DRAG, 0.0D, CartConstants.STANDARD_DRAG));
  }

  @Override
  public boolean isPoweredCart() {
    return true;
  }

  @Override
  public void setChanged() {
    if (!this.isActive()) {
      this.setDelay(STANDARD_DELAY);
    }
  }

  public final Direction getFacing() {
    return this.entityData.get(FACING);
  }

  protected final void setFacing(Direction facing) {
    this.entityData.set(FACING, facing);
    this.setYaw();
  }

  @Override
  public Optional<Side> disabledSide() {
    return Optional.of(Side.FRONT);
  }

  @Override
  public boolean isLinkableWith(RollingStock cart) {
    var pos = getPositionAhead(-LENGTH / 2.0);
    float dist = RollingStock.MAX_LINK_DISTANCE * 2;
    dist = dist * dist;
    return cart.entity().distanceToSqr(pos.getX(), pos.getY(), pos.getZ()) < dist;
  }

  @Override
  public float getLinkageDistance(RollingStock cart) {
    return 4f;
  }

  @Override
  public float getOptimalDistance(RollingStock cart) {
    return 3.1f;
  }

  @Override
  public void linked(RollingStock cart) {}

  @Override
  public void unlinked(RollingStock cart) {}

  @Override
  public boolean canBeAdjusted(RollingStock cart) {
    return !isActive();
  }

  @Override
  public boolean shouldDoRailFunctions() {
    return false;
  }

  @Override
  public PartEntity<?>[] getParts() {
    return this.parts;
  }

  @Override
  public boolean isMultipartEntity() {
    return true;
  }

  @Override
  public boolean canPlaceItem(int index, ItemStack stack) {
    return this.containers.stream()
        .filter(m -> m.containsSlot(index))
        .allMatch(m -> m.filter().test(stack));
  }

  public boolean attackEntityFromPart(TunnelBorePart part, DamageSource damageSource,
      float damage) {
    return hurt(damageSource, damage);
  }

  @Override
  protected AbstractContainerMenu createMenu(int id, Inventory inventory) {
    return new TunnelBoreMenu(id, inventory, this);
  }
}
