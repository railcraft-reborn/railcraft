package mods.railcraft.world.level.block.track.outfitted;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;
import mods.railcraft.api.item.Crowbar;
import mods.railcraft.api.track.TrackType;
import mods.railcraft.client.gui.screen.EmbarkingTrackScreen;
import mods.railcraft.util.AABBFactory;
import mods.railcraft.util.HostEffects;
import mods.railcraft.world.entity.vehicle.CartTools;
import mods.railcraft.world.entity.vehicle.MinecartExtension;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

public class EmbarkingTrackBlock extends PoweredOutfittedTrackBlock {

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
      Minecraft.getInstance().setScreen(new EmbarkingTrackScreen(blockState, blockPos));
    }
    return true;
  }

  @Override
  public void onMinecartPass(BlockState blockState, Level level, BlockPos blockPos,
      AbstractMinecart cart) {
    super.onMinecartPass(blockState, level, blockPos, cart);
    var extension = MinecartExtension.getOrThrow(cart);
    if (isPowered(blockState) && cart.canBeRidden() && !cart.isVehicle()
        && extension.isMountable()) {
      int radius = getRadius(blockState);
      var box = AABBFactory.start().at(blockPos).build();
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
          HostEffects.INSTANCE.teleportEffect(entity, cart.position());
          CartTools.addPassenger(cart, entity);
        }
      }
    }
  }

  public static int getRadius(BlockState blockState) {
    return blockState.getValue(RADIUS);
  }

  public static BlockState setRadius(BlockState blockState, int radius) {
    return blockState.setValue(RADIUS, Mth.clamp(radius, MIN_RADIUS, MAX_RADIUS));
  }
}
