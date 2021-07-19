package mods.railcraft.world.level.block.track;

import javax.annotation.Nullable;
import mods.railcraft.Railcraft;
import mods.railcraft.api.tracks.TrackKit;
import mods.railcraft.api.tracks.TrackType;
import mods.railcraft.world.level.block.RailcraftBlocks;
import mods.railcraft.world.level.block.track.behaivor.CollisionHandler;
import mods.railcraft.world.level.block.track.behaivor.SpeedController;
import net.minecraft.block.AbstractRailBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.state.properties.RailShape;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;

public class TrackTypes {

  public static final DeferredRegister<TrackType> TRACK_TYPES =
      DeferredRegister.create(TrackType.class, Railcraft.ID);

  public static final Lazy<IForgeRegistry<TrackType>> REGISTRY =
      Lazy.of(TRACK_TYPES.makeRegistry("track_types", RegistryBuilder::new));

  public static final RegistryObject<TrackType> ABANDONED =
      TRACK_TYPES.register("abandoned",
          () -> new TrackType.Builder(RailcraftBlocks.ABANDONED_FLEX_TRACK)
              .setEventHandler(new Handler(CollisionHandler.NULL, SpeedController.ABANDONED))
              .setMaxSupportDistance(2)
              .build());

  public static final RegistryObject<TrackType> ELECTRIC =
      TRACK_TYPES.register("electric",
          () -> new TrackType.Builder(RailcraftBlocks.ELECTRIC_FLEX_TRACK)
              .setEventHandler(new Handler(CollisionHandler.ELECTRIC, SpeedController.IRON))
              .setElectric(true)
              .build());

  public static final RegistryObject<TrackType> HIGH_SPEED =
      TRACK_TYPES.register("high_speed",
          () -> new TrackType.Builder(RailcraftBlocks.ELECTRIC_FLEX_TRACK)
              .setEventHandler(new Handler(CollisionHandler.NULL, SpeedController.HIGH_SPEED))
              .setHighSpeed(true)
              .build());

  public static final RegistryObject<TrackType> HIGH_SPEED_ELECTRIC =
      TRACK_TYPES.register("high_speed_electric",
          () -> new TrackType.Builder(RailcraftBlocks.ELECTRIC_FLEX_TRACK)
              .setEventHandler(new Handler(CollisionHandler.ELECTRIC, SpeedController.HIGH_SPEED))
              .setElectric(true)
              .setHighSpeed(true)
              .build());

  public static final RegistryObject<TrackType> STRAP_IRON =
      TRACK_TYPES.register("strap_iron",
          () -> new TrackType.Builder(RailcraftBlocks.ELECTRIC_FLEX_TRACK)
              .setEventHandler(new Handler(CollisionHandler.NULL, SpeedController.STRAP_IRON))
              .build());

  public static final RegistryObject<TrackType> REINFORCED =
      TRACK_TYPES.register("reinforced",
          () -> new TrackType.Builder(RailcraftBlocks.REINFORCED_FLEX_TRACK)
              .setEventHandler(new Handler(CollisionHandler.NULL, SpeedController.REINFORCED))
              .build());

  public static final RegistryObject<TrackType> IRON =
      TRACK_TYPES.register("iron",
          () -> new TrackType.Builder(() -> (AbstractRailBlock) Blocks.RAIL)
              .setEventHandler(new Handler(CollisionHandler.NULL, SpeedController.IRON))
              .build());

  private static class Handler extends TrackType.EventHandler {

    private final CollisionHandler collisionHandler;
    private final SpeedController speedController;

    public Handler(CollisionHandler collisionHandler, SpeedController speedController) {
      this.collisionHandler = collisionHandler;
      this.speedController = speedController;
    }

    @Override
    public void onMinecartPass(World worldIn, AbstractMinecartEntity cart, BlockPos pos,
        @Nullable TrackKit trackKit) {
      speedController.onMinecartPass(worldIn, cart, pos, trackKit);
    }

    @Override
    public void onEntityCollision(World world, BlockPos pos, BlockState state, Entity entity) {
      collisionHandler.onEntityCollision(world, pos, state, entity);
    }

    @Override
    public @Nullable RailShape getRailDirectionOverride(IBlockReader world,
        BlockPos pos, BlockState state, @Nullable AbstractMinecartEntity cart) {
      return speedController.getRailDirectionOverride(world, pos, state, cart);
    }

    @Override
    public double getMaxSpeed(World world, @Nullable AbstractMinecartEntity cart, BlockPos pos) {
      return speedController.getMaxSpeed(world, cart, pos);
    }
  }
}
