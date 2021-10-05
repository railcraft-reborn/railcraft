package mods.railcraft.world.entity;

import com.google.common.collect.Sets;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.BiFunction;

import javax.annotation.Nullable;

import mods.railcraft.RailcraftConfig;
import mods.railcraft.api.carts.CartUtil;
import mods.railcraft.api.carts.IBoreHead;
import mods.railcraft.api.carts.ILinkableCart;
import mods.railcraft.api.core.RailcraftFakePlayer;
import mods.railcraft.api.track.TrackUtil;
import mods.railcraft.carts.CartConstants;
import mods.railcraft.carts.CartTools;
import mods.railcraft.carts.LinkageManager;
import mods.railcraft.carts.Train;
import mods.railcraft.tags.RailcraftTags;
import mods.railcraft.util.AABBFactory;
import mods.railcraft.util.BallastRegistry;
import mods.railcraft.util.EntitySearcher;
import mods.railcraft.util.FuelUtil;
import mods.railcraft.util.HarvestUtil;
import mods.railcraft.util.LevelUtil;
import mods.railcraft.util.MiscTools;
import mods.railcraft.util.RCEntitySelectors;
import mods.railcraft.util.TrackTools;
import mods.railcraft.util.inventory.IExtInvSlot;
import mods.railcraft.util.inventory.IInvSlot;
import mods.railcraft.util.inventory.InvTools;
import mods.railcraft.util.inventory.InventoryIterator;
import mods.railcraft.util.inventory.filters.StackFilters;
import mods.railcraft.util.inventory.wrappers.InventoryMapper;
import mods.railcraft.world.damagesource.RailcraftDamageSource;
import mods.railcraft.world.item.RailcraftItems;
import net.minecraft.block.AbstractRailBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameters;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.state.properties.RailShape;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ITag;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.entity.PartEntity;
import net.minecraftforge.event.world.BlockEvent;

public class TunnelBoreEntity extends AbstractRailcraftMinecartEntity implements ILinkableCart {

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

  public static final Set<BlockState> mineableStates = new HashSet<>();
  public static final Set<Block> mineableBlocks = Sets.newHashSet(
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
      Blocks.SNOW,
      Blocks.STONE,
      Blocks.FARMLAND,
      Blocks.TORCH,
      Blocks.VINE,
      Blocks.COBWEB,
      Blocks.END_STONE);

  @SuppressWarnings("unchecked")
  public static final Set<ITag<Block>> mineableTags = Sets.newHashSet(
      Tags.Blocks.ORES,
      Tags.Blocks.NETHERRACK,
      Tags.Blocks.COBBLESTONE,
      Tags.Blocks.OBSIDIAN,
      Tags.Blocks.GRAVEL,
      Tags.Blocks.DIRT,
      BlockTags.LEAVES,
      BlockTags.SAPLINGS,
      BlockTags.LOGS,
      BlockTags.FLOWERS,
      RailcraftTags.Blocks.MAGIC_ORE);

  public static final Set<Block> replaceableBlocks = Sets.newHashSet(Blocks.TORCH);

  @SuppressWarnings("unchecked")
  public static final Set<ITag<Block>> replaceableTags = Sets.newHashSet(BlockTags.FLOWERS);

  private static final DataParameter<Boolean> HAS_FUEL =
      EntityDataManager.defineId(TunnelBoreEntity.class, DataSerializers.BOOLEAN);
  private static final DataParameter<Boolean> MOVING =
      EntityDataManager.defineId(TunnelBoreEntity.class, DataSerializers.BOOLEAN);
  private static final DataParameter<Direction> FACING =
      EntityDataManager.defineId(TunnelBoreEntity.class, DataSerializers.DIRECTION);
  private static final DataParameter<ItemStack> BORE_HEAD =
      EntityDataManager.defineId(TunnelBoreEntity.class, DataSerializers.ITEM_STACK);

  public final InventoryMapper invFuel =
      InventoryMapper.make(this, 1, 6).withFilters(StackFilters.FUEL);
  public final InventoryMapper invBallast =
      InventoryMapper.make(this, 7, 9).withFilters(StackFilters.BALLAST);
  public final InventoryMapper invRails =
      InventoryMapper.make(this, 16, 9).withFilters(StackFilters.TRACK);
  // protected static final int WATCHER_ID_BURN_TIME = 22;

  protected int delay;
  protected boolean placeRail;
  protected boolean placeBallast;
  protected boolean boreLayer;
  protected int boreRotationAngle;
  private boolean active;
  private int clock = MiscTools.RANDOM.nextInt();
  private int burnTime;
  private int fuel;
  private final boolean hasInit;
  private final TunnelBorePartEntity[] partArray;

  public TunnelBoreEntity(EntityType<?> type, World world) {
    this(world, 0, 0, 0, Direction.SOUTH);
  }

  public TunnelBoreEntity(World world, double x, double y, double z) {
    this(world, x, y, z, Direction.SOUTH);
  }

  public TunnelBoreEntity(World world, double x, double y, double z, Direction f) {
    super(RailcraftEntityTypes.TUNNEL_BORE.get(), x, y, z, world);
    setFacing(f);
  }

  {
    float headW = 1.5F;
    float headH = 2.6F;
    float headSO = 0.7F;
    partArray = new TunnelBorePartEntity[] {
        // ------------------------------------- name, width, height, forwardOffset, sideOffset
        new TunnelBorePartEntity(this, "head1", headW, headH, 1.85F, -headSO),
        new TunnelBorePartEntity(this, "head2", headW, headH, 1.85F, headSO),
        new TunnelBorePartEntity(this, "head3", headW, headH, 2.3F, -headSO),
        new TunnelBorePartEntity(this, "head4", headW, headH, 2.3F, headSO),
        new TunnelBorePartEntity(this, "body", 2.0F, 1.9F, 0.6F),
        new TunnelBorePartEntity(this, "tail1", 1.6F, 1.4F, -1F),
        new TunnelBorePartEntity(this, "tail2", 1.6F, 1.4F, -2.2F),
    };
    hasInit = true;
    invMappers = Arrays.asList(invFuel, invBallast, invRails);
  }

  @Override
  public ItemStack getContents() {
    return ItemStack.EMPTY;
  }

  @Override
  public Item getItem() {
    return RailcraftItems.TUNNEL_BORE.get();
  }

  public static void addMineableBlock(Block block) {
    addMineableBlock(block.defaultBlockState());
  }

  public static void addMineableBlock(BlockState blockState) {
    mineableStates.add(blockState);
  }

  public boolean canHeadHarvestBlock(ItemStack head, BlockState targetState) {
    if (head.isEmpty()) {
      return false;
    }

    if (head.getItem() instanceof IBoreHead) {

      /*
       * boolean mappingExists = false;
       *
       * int blockHarvestLevel = HarvestPlugin.getHarvestLevel(targetState,
       * "pickaxe"); if (blockHarvestLevel > -1) { if (boreHead.getHarvestLevel() >=
       * blockHarvestLevel) return true; mappingExists = true; }
       *
       * blockHarvestLevel = HarvestPlugin.getHarvestLevel(targetState, "axe"); if
       * (blockHarvestLevel > -1) { if (boreHead.getHarvestLevel() >=
       * blockHarvestLevel) return true; mappingExists = true; }
       *
       * blockHarvestLevel = HarvestPlugin.getHarvestLevel(targetState, "shovel"); if
       * (blockHarvestLevel > -1) { if (boreHead.getHarvestLevel() >=
       * blockHarvestLevel) return true; mappingExists = true; }
       *
       * if (mappingExists) return false;
       */
      Item item = head.getItem();
      Set<ToolType> toolClasses = item.getToolTypes(head);
      PlayerEntity fakePlayer = RailcraftFakePlayer.get(
          (ServerWorld) this.level, this.getX(), this.getY(), this.getZ());

      return toolClasses.stream().anyMatch(tool -> item.getHarvestLevel(head, tool, fakePlayer,
          targetState) >= HarvestUtil.getHarvestLevel(targetState, tool));
    }

    return false;
  }

  private boolean isMineableBlock(BlockState blockState) {
    return RailcraftConfig.server.boreMinesAllBlocks.get()
        || mineableBlocks.contains(blockState.getBlock())
        || mineableStates.contains(blockState)
        || mineableTags.stream().anyMatch(tag -> tag.contains(blockState.getBlock()));
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
    if (!this.level.isClientSide() && this.isAlive()) {
      if (isInvulnerableTo(source)) {
        return false;
      } else {
        setHurtDir(-getHurtDir());
        setHurtTime(10);
        markHurt();
        setDamage(getDamage() + damage * 10);
        boolean flag = (source.getEntity() instanceof PlayerEntity)
            && ((PlayerEntity) source.getEntity()).isCreative();

        if (flag || getDamage() > 120) {
          ejectPassengers();

          if (flag && !hasCustomName()) {
            this.remove();
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
    float yaw = 0;
    switch (getFacing()) {
      case NORTH:
        yaw = 180;
        break;
      case EAST:
        yaw = 270;
        break;
      case SOUTH:
        yaw = 0;
        break;
      case WEST:
        yaw = 90;
        break;
      default:
        break;
    }
    this.setRot(yaw, this.xRot);
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
    if (!hasInit) {
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
    if (getFacing() == Direction.WEST || getFacing() == Direction.EAST) {
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

    this.setBoundingBox(new AxisAlignedBB(minX, y, minZ, maxX, y + height, maxZ));
  }

  @SuppressWarnings("deprecation")
  @Override
  public void tick() {
    clock++;

    if (!this.level.isClientSide()) {
      if (clock % 64 == 0) {
        forceUpdateBoreHead();
        setMinecartPowered(false);
        setMoving(false);
      }

      stockBallast();
      stockTracks();
    }

    super.tick();

    for (Entity part : partArray) {
      part.tick();
    }

    if (!this.level.isClientSide()) {

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
          } else if (!Block.canSupportRigidBlock(this.level, targetPos)) {
            placeBallast = true;
            setDelay(BALLAST_DELAY);
          }
        }

        if (getDelay() == 0) {
          float offset = 0.8f;
          BlockPos targetPos = new BlockPos(getPositionAhead(offset));
          BlockState existingState = this.level.getBlockState(targetPos);

          if (placeRail) {
            boolean placed = placeTrack(targetPos, existingState, dir);
            if (placed) {
              setDelay(STANDARD_DELAY);
            } else {
              setDelay(FAIL_DELAY);
              setActive(false);
            }
            placeRail = false;
          } else if (AbstractRailBlock.isRail(existingState)) {
            if (dir != TrackTools.getTrackDirection(this.level, targetPos, this)) {
              TrackTools.setTrackDirection(this.level, targetPos, dir);
              setDelay(STANDARD_DELAY);
            }
          } else if (existingState.isAir(this.level, targetPos)
              || replaceableBlocks.contains(existingState.getBlock())) {
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
            setDelay((int) Math.ceil(getLayerHardness(targetPos, dir)));
            if (getDelay() != 0) {
              boreLayer = true;
            }
          }
        }
      }

      if (isMinecartPowered()) {
        Vector3d headPos = getPositionAhead(3.3);
        double size = 0.8;
        AxisAlignedBB entitySearchBox = AABBFactory.start()
            .setBoundsToPoint(headPos)
            .expandHorizontally(size)
            .raiseCeiling(2).build();
        List<LivingEntity> entities = EntitySearcher.findLiving().and(RCEntitySelectors.KILLABLE)
            .around(entitySearchBox).in(this.level);
        entities.forEach(e -> e.hurt(RailcraftDamageSource.BORE, 2));

        ItemStack head = getItem(0);
        if (!head.isEmpty()) {
          head.hurt(entities.size(), this.random, CartTools.getFakePlayer(this));
        }
      }

      setMoving(hasFuel() && getDelay() == 0);

      if (getDelay() > 0) {
        setDelay(getDelay() - 1);
      }
    }

    Vector3d motion = this.getDeltaMovement();
    if (isMoving()) {
      float factorX = -MathHelper.sin((float) Math.toRadians(this.yRot));
      float factorZ = MathHelper.cos((float) Math.toRadians(this.yRot));
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

  private void updateFuel() {
    if (!this.level.isClientSide()) {
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

  protected Vector3d getPositionAhead(double offset) {
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

    return new Vector3d(x, this.getY(), z);
  }

  protected double getOffsetX(double x, double forwardOffset, double sideOffset) {
    switch (getFacing()) {
      case NORTH:
        return x + sideOffset;
      case SOUTH:
        return x - sideOffset;
      case EAST:
        return x + forwardOffset;
      case WEST:
        return x - forwardOffset;
      default:
        break;
    }
    return x;
  }

  protected double getOffsetZ(double z, double forwardOffset, double sideOffset) {
    switch (getFacing()) {
      case NORTH:
        return z - forwardOffset;
      case SOUTH:
        return z + forwardOffset;
      case EAST:
        return z - sideOffset;
      case WEST:
        return z + sideOffset;
      default:
        break;
    }
    return z;
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
        this.level.addParticle(ParticleTypes.LARGE_SMOKE,
            smokeX1, this.getY() + smokeYOffset, smokeZ1,
            0.0D, 0.0D, 0.0D);
        this.level.addParticle(ParticleTypes.FLAME, flameX1,
            this.getY() + flameYOffset + (this.random.nextGaussian() * randomFactor), flameZ1,
            0.0D, 0.0D, 0.0D);
      }
      if (this.random.nextInt(4) == 0) {
        this.level.addParticle(ParticleTypes.LARGE_SMOKE,
            smokeX2, this.getY() + smokeYOffset, smokeZ2,
            0.0D, 0.0D, 0.0D);
        this.level.addParticle(ParticleTypes.FLAME, flameX2,
            this.getY() + flameYOffset + (this.random.nextGaussian() * randomFactor), flameZ2,
            0.0D, 0.0D, 0.0D);
      }
    }
  }

  protected void stockBallast() {
    ItemStack stack = CartUtil.transferHelper().pullStack(this, StackFilters.roomIn(invBallast));
    if (!stack.isEmpty()) {
      invBallast.addStack(stack);
    }
  }

  @SuppressWarnings("deprecation")
  protected boolean placeBallast(BlockPos targetPos) {
    if (!Block.canSupportRigidBlock(this.level, targetPos)) {
      for (IExtInvSlot slot : InventoryIterator.get(invBallast)) {
        ItemStack stack = slot.getStack();
        if (!stack.isEmpty() && BallastRegistry.isItemBallast(stack)) {
          BlockPos.Mutable searchPos = targetPos.mutable();
          for (int i = 0; i < MAX_FILL_DEPTH; i++) {
            searchPos.move(Direction.DOWN);
            if (Block.canSupportRigidBlock(this.level, searchPos)) {
              // Fill ballast
              BlockState state = InvTools.getBlockStateFromStack(stack, this.level, targetPos);
              if (state != null) {
                slot.decreaseStack();
                this.level.setBlockAndUpdate(targetPos, state);
                return true;
              }
            } else {
              BlockState state = this.level.getBlockState(searchPos);
              if (!state.isAir(this.level, searchPos) && !state.getMaterial().isLiquid()) {
                // Break other blocks first
                LevelUtil.playerRemoveBlock(this.level, searchPos.immutable(),
                    CartTools.getFakePlayer(this),
                    this.level.getGameRules().getBoolean(GameRules.RULE_DOBLOCKDROPS)
                        && !RailcraftConfig.server.boreDestorysBlocks.get());
              }
            }
          }
          return false;
        }
      }
    }
    return false;
  }

  protected void stockTracks() {
    ItemStack stack = CartUtil.transferHelper().pullStack(this, StackFilters.roomIn(invRails));
    if (!stack.isEmpty()) {
      invRails.addStack(stack);
    }
  }

  @SuppressWarnings("deprecation")
  protected boolean placeTrack(BlockPos targetPos, BlockState oldState, RailShape shape) {
    PlayerEntity owner = CartTools.getFakePlayer(this);

    if (replaceableBlocks.contains(oldState.getBlock())) {
      LevelUtil.destroyBlock(this.level, targetPos, owner, true);
    }

    if (oldState.isAir(this.level, targetPos)
        && Block.canSupportRigidBlock(this.level, targetPos.below())) {
      for (IInvSlot slot : InventoryIterator.get(invRails)) {
        ItemStack stack = slot.getStack();
        if (!stack.isEmpty()) {
          boolean placed = TrackUtil.placeRailAt(stack, (ServerWorld) this.level, targetPos, shape);
          if (placed) {
            slot.decreaseStack();
          }
          return placed;
        }
      }
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
      Fluid fluid = this.level.getFluidState(blockPos).getType();
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
   * @return true if the target block is clear
   */
  protected boolean mineBlock(BlockPos targetPos, RailShape preferredShape) {
    BlockState targetState = this.level.getBlockState(targetPos);
    if (targetState.getBlock().isAir(targetState, this.level, targetPos)) {
      return true;
    }

    if (AbstractRailBlock.isRail(targetState)) {
      RailShape targetShape =
          TrackTools.getTrackDirection(this.level, targetPos, targetState, this);
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

    ServerPlayerEntity fakePlayer = CartTools.getFakePlayerWith(this, head);

    // Fires break event within; harvest handled separately
    BlockEvent.BreakEvent breakEvent =
        new BlockEvent.BreakEvent(this.level, targetPos, targetState, fakePlayer);
    MinecraftForge.EVENT_BUS.post(breakEvent);

    if (breakEvent.isCanceled()) {
      return false;
    }

    if (!RailcraftConfig.server.boreDestorysBlocks.get()
        && this.level.getGameRules().getBoolean(GameRules.RULE_DOBLOCKDROPS)) {
      targetState
          .getDrops(new LootContext.Builder((ServerWorld) this.level)
              .withParameter(LootParameters.TOOL, head)
              .withParameter(LootParameters.KILLER_ENTITY, this)
              .withParameter(LootParameters.ORIGIN, this.position()))
          .forEach(stack -> {
            if (StackFilters.FUEL.test(stack)) {
              stack = invFuel.addStack(stack);
            }

            if (!stack.isEmpty() && InvTools.isStackEqualToBlock(stack, Blocks.GRAVEL)) {
              stack = invBallast.addStack(stack);
            }

            if (!stack.isEmpty()) {
              stack = CartUtil.transferHelper().pushStack(this, stack);
            }

            if (!stack.isEmpty()) {
              Block.popResource(this.level, this.blockPosition(), stack);
            }
          });
    }

    LevelUtil.setAir(this.level, targetPos);

    if (head.hurt(1, this.random, fakePlayer)) {
      this.setItem(0, ItemStack.EMPTY);
    }

    return true;
  }

  private boolean canMineBlock(BlockPos targetPos, BlockState existingState) {
    ItemStack head = getItem(0);
    if (existingState.getDestroySpeed(this.level, targetPos) < 0) {
      return false;
    }
    return isMineableBlock(existingState) && canHeadHarvestBlock(head, existingState);
  }

  protected double getLayerHardness(BlockPos targetPos, RailShape dir) {
    double hardness = layerAction(targetPos, dir, 0F, this::getBlockHardness, (s, r) -> s + r);
    hardness *= HARDNESS_MULTIPLIER;

    ItemStack boreSlot = getItem(0);
    if (!boreSlot.isEmpty() && boreSlot.getItem() instanceof IBoreHead) {
      IBoreHead head = (IBoreHead) boreSlot.getItem();
      double dig = head.getDigModifier();
      hardness /= dig;
      int e = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.BLOCK_EFFICIENCY, boreSlot);
      hardness /= (e * e * 0.2d + 1);
    }

    hardness /= RailcraftConfig.server.boreMininigSpeedMultiplier.get();

    return hardness;
  }

  @SuppressWarnings("deprecation")
  protected float getBlockHardness(BlockPos pos, RailShape dir) {
    if (this.level.getBlockState(pos).isAir(this.level, pos)) {
      return 0;
    }

    BlockState blockState = this.level.getBlockState(pos);
    if (AbstractRailBlock.isRail(blockState)) {
      RailShape trackMeta = TrackTools.getTrackDirection(this.level, pos, blockState, this);
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

    float hardness = blockState.getDestroySpeed(this.level, pos);
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
  protected void addAdditionalSaveData(CompoundNBT data) {
    // fuel = getFuel();
    super.addAdditionalSaveData(data);
    data.putInt("facing", getFacing().get3DDataValue());
    data.putInt("delay", getDelay());
    data.putBoolean("active", isActive());
    data.putInt("burnTime", getBurnTime());
    data.putInt("fuel", fuel);
  }

  @Override
  protected void readAdditionalSaveData(CompoundNBT data) {
    super.readAdditionalSaveData(data);
    setFacing(Direction.from3DDataValue(data.getInt("facing")));
    setDelay(data.getInt("delay"));
    setActive(data.getBoolean("active"));
    setBurnTime(data.getInt("burnTime"));
    setFuel(data.getInt("fuel"));
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
    Train.State state = active ? Train.State.STOPPED : Train.State.NORMAL;
    Train.get(this).ifPresent(t -> t.setTrainState(state));
    // entityData.set(WATCHER_ID_ACTIVE, Byte.valueOf((byte)(active ? 1 : 0)));
  }

  protected boolean isMoving() {
    return entityData.get(MOVING);
  }

  protected void setMoving(boolean move) {
    entityData.set(MOVING, move);
  }

  public int getBurnTime() {
    return burnTime;
    // return entityData.get(WATCHER_ID_BURN_TIME);
  }

  public void setBurnTime(int burnTime) {
    this.burnTime = burnTime;
    // entityData.set(WATCHER_ID_BURN_TIME, Integer.valueOf(burnTime));
  }

  public int getFuel() {
    return fuel;
    // return entityData.get(WATCHER_ID_FUEL);
  }

  public void setFuel(int i) {
    fuel = i;
    // entityData.set(WATCHER_ID_FUEL, Integer.valueOf(i));
  }

  public boolean outOfFuel() {
    return getFuel() <= FUEL_CONSUMPTION;
  }

  public boolean hasFuel() {
    return getFuel() > 0;
  }

  protected void stockFuel() {
    ItemStack stack = CartUtil.transferHelper().pullStack(this, StackFilters.roomIn(invFuel));
    if (!stack.isEmpty()) {
      invFuel.addStack(stack);
    }
  }

  protected void addFuel() {
    int burn = 0;
    for (int slot = 0; slot < invFuel.getContainerSize(); slot++) {
      ItemStack stack = invFuel.getItem(slot);
      if (!stack.isEmpty()) {
        burn = FuelUtil.getBurnTime(stack);
        if (burn > 0) {
          if (stack.getItem().hasContainerItem(stack)) {
            invFuel.setItem(slot, stack.getItem().getContainerItem(stack));
          } else {
            invFuel.removeItem(slot, 1);
          }
          break;
        }
      }
    }
    if (burn > 0) {
      setBurnTime(burn + getFuel());
      setFuel(getFuel() + burn);
    }
  }

  public int getBurnProgressScaled(int i) {
    int burn = getBurnTime();
    if (burn == 0) {
      return 0;
    }

    return getFuel() * i / burn;
  }

  protected void spendFuel() {
    setFuel(getFuel() - FUEL_CONSUMPTION);
  }

  protected void forceUpdateBoreHead() {
    ItemStack boreStack = getItem(0);
    if (!boreStack.isEmpty()) {
      boreStack = boreStack.copy();
    }
    entityData.set(BORE_HEAD, boreStack);
  }

  public @Nullable IBoreHead getBoreHead() {
    ItemStack boreStack = entityData.get(BORE_HEAD);
    if (boreStack.getItem() instanceof IBoreHead) {
      return (IBoreHead) boreStack.getItem();
    }
    return null;
  }

  @Override
  protected void applyNaturalSlowdown() {
    this.setDeltaMovement(
        this.getDeltaMovement()
            .multiply(CartConstants.STANDARD_DRAG, 0.0D, CartConstants.STANDARD_DRAG));
  }

  @Override
  public boolean isPoweredCart() {
    return true;
  }

  @Override
  public void setChanged() {
    if (!isActive()) {
      setDelay(STANDARD_DELAY);
    }
  }

  public final Direction getFacing() {
    return entityData.get(FACING);
  }

  protected final void setFacing(Direction facing) {
    entityData.set(FACING, facing);

    setYaw();
  }

  @Override
  public boolean isLinkable() {
    return true;
  }

  @Override
  public boolean canLink(AbstractMinecartEntity cart) {
    Vector3d pos = getPositionAhead(-LENGTH / 2.0);
    float dist = LinkageManager.LINKAGE_DISTANCE * 2;
    dist = dist * dist;
    return cart.distanceToSqr(pos.x, pos.y, pos.z) < dist;
  }

  @Override
  public boolean hasTwoLinks() {
    return false;
  }

  @Override
  public float getLinkageDistance(AbstractMinecartEntity cart) {
    return 4f;
  }

  @Override
  public float getOptimalDistance(AbstractMinecartEntity cart) {
    return 3.1f;
  }

  @Override
  public void onLinkCreated(AbstractMinecartEntity cart) {}

  @Override
  public void onLinkBroken(AbstractMinecartEntity cart) {}

  @Override
  public boolean canBeAdjusted(AbstractMinecartEntity cart) {
    return !isActive();
  }

  @Override
  public boolean shouldDoRailFunctions() {
    return false;
  }

  @Override
  public PartEntity<?>[] getParts() {
    return this.partArray;
  }

  public boolean attackEntityFromPart(TunnelBorePartEntity part, DamageSource damageSource,
      float damage) {
    return hurt(damageSource, damage);
  }

  @Override
  protected Container createMenu(int containerProvider, PlayerInventory playerInventory) {
    return null;
  }
}
