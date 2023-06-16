package mods.railcraft.world.level.block.track.outfitted;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;
import mods.railcraft.Translations;
import mods.railcraft.api.carts.RollingStock;
import mods.railcraft.api.item.Crowbar;
import mods.railcraft.api.track.TrackType;
import mods.railcraft.client.ScreenFactories;
import mods.railcraft.util.BoxBuilder;
import mods.railcraft.world.entity.vehicle.CartTools;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.Vec3;

public class EmbarkingTrackBlock extends PoweredOutfittedTrackBlock {

  public static final short TELEPORT_PARTICLES = 64;

  public static final int MIN_RADIUS = 1;
  public static final int MAX_RADIUS = 5;

  public static final Set<EntityType<?>> excludedEntities = new HashSet<>();

  static {
    excludedEntities.add(EntityType.IRON_GOLEM);
    excludedEntities.add(EntityType.ENDER_DRAGON);
    excludedEntities.add(EntityType.WITHER);
    excludedEntities.add(EntityType.BLAZE);
    excludedEntities.add(EntityType.MAGMA_CUBE);
    excludedEntities.add(EntityType.SQUID);
    excludedEntities.add(EntityType.BAT);
  }

  public static final IntegerProperty RADIUS =
      IntegerProperty.create("radius", MIN_RADIUS, MAX_RADIUS);

  public EmbarkingTrackBlock(Supplier<? extends TrackType> trackType, Properties properties) {
    super(trackType, properties);
  }

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
    super.createBlockStateDefinition(builder);
    builder.add(RADIUS);
  }

  @Override
  protected BlockState buildDefaultState(BlockState blockState) {
    return super.buildDefaultState(blockState)
        .setValue(RADIUS, 2);
  }

  @Override
  protected boolean crowbarWhack(BlockState blockState, Level level, BlockPos blockPos,
      Player player, InteractionHand hand, ItemStack itemStack) {
    if (level.isClientSide()) {
      ScreenFactories.openEmbarkingTrackScreen(blockState, blockPos);
    }
    return true;
  }

  @Override
  public void onMinecartPass(BlockState blockState, Level level, BlockPos blockPos,
      AbstractMinecart cart) {
    super.onMinecartPass(blockState, level, blockPos, cart);
    var extension = RollingStock.getOrThrow(cart);
    if (isPowered(blockState) && cart.canBeRidden() && !cart.isVehicle()
        && extension.isMountable()) {
      int radius = getRadius(blockState);
      var box = BoxBuilder.create().at(blockPos).build();
      box = box.inflate(radius, radius, radius);
      var entities = level.getEntitiesOfClass(LivingEntity.class, box);

      if (!entities.isEmpty()) {
        var entity = entities.get(level.getRandom().nextInt(entities.size()));

        if (entity instanceof Player player) {
          if (player.isCrouching()) {
            return;
          }

          ItemStack current = player.getMainHandItem();
          if (!current.isEmpty() && current.getItem() instanceof Crowbar) {
            return;
          }
        } else if (excludedEntities.contains(entity.getType()) || entity instanceof WaterAnimal) {
          return;
        } else if (entity instanceof Slime slime && slime.getSize() >= 100) {
          return;
        }

        if (!entity.isPassenger()) {
          teleportEffect(entity, cart.position());
          CartTools.addPassenger(cart, entity);
        }
      }
    }
  }

  private static void teleportEffect(Entity entity, Vec3 destination) {
    var level = entity.getLevel();
    if (level.isClientSide()) {
      return;
    }

    var random = level.getRandom();
    var start = entity.position();
    for (int i = 0; i < TELEPORT_PARTICLES; i++) {
      var travel = (double) i / ((double) TELEPORT_PARTICLES - 1.0D);
      var vX = (random.nextFloat() - 0.5F) * 0.2F;
      var vY = (random.nextFloat() - 0.5F) * 0.2F;
      var vZ = (random.nextFloat() - 0.5F) * 0.2F;
      var pX = start.x + (destination.x - start.x) * travel + (random.nextDouble() - 0.5D) * 2.0D;
      var pY = start.y + (destination.y - start.y) * travel + (random.nextDouble() - 0.5D) * 2.0D;
      var pZ = start.z + (destination.z - start.z) * travel + (random.nextDouble() - 0.5D) * 2.0D;
      level.addParticle(ParticleTypes.PORTAL, pX, pY, pZ, vX, vY, vZ);
    }

    level.playSound(
        null, entity, SoundEvents.ENDERMAN_TELEPORT, SoundSource.BLOCKS, 0.25F, 1.0F);
  }

  public static int getRadius(BlockState blockState) {
    return blockState.getValue(RADIUS);
  }

  public static BlockState setRadius(BlockState blockState, int radius) {
    return blockState.setValue(RADIUS, Mth.clamp(radius, MIN_RADIUS, MAX_RADIUS));
  }

  @Override
  public void appendHoverText(ItemStack stack, BlockGetter level, List<Component> lines,
      TooltipFlag flag) {
    lines.add(Component.translatable(Translations.Tips.EMBARKING_TRACK)
        .withStyle(ChatFormatting.GRAY));
    lines.add(Component.translatable(Translations.Tips.HIT_CROWBAR_TO_CHANGE_RANGE)
        .withStyle(ChatFormatting.BLUE));
    lines.add(Component.translatable(Translations.Tips.APPLY_REDSTONE_TO_ENABLE)
        .withStyle(ChatFormatting.RED));
  }
}
