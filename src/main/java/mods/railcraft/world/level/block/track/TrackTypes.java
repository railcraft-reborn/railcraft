package mods.railcraft.world.level.block.track;

import javax.annotation.Nullable;
import mods.railcraft.Railcraft;
import mods.railcraft.api.tracks.TrackType;
import mods.railcraft.world.level.block.RailcraftBlocks;
import mods.railcraft.world.level.block.track.behaivor.CollisionHandler;
import mods.railcraft.world.level.block.track.behaivor.SpeedController;
import mods.railcraft.world.level.block.track.kit.TrackKits;
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
              .setEventHandler(SpeedController.ABANDONED)
              .setMaxSupportDistance(2)
              .build());

  public static final RegistryObject<TrackType> ELECTRIC =
      TRACK_TYPES.register("electric",
          () -> new TrackType.Builder(RailcraftBlocks.ELECTRIC_FLEX_TRACK)
              .setEventHandler(
                  new CompositeHandler(CollisionHandler.ELECTRIC, SpeedController.IRON))
              .setElectric(true)
              .build());

  public static final RegistryObject<TrackType> HIGH_SPEED =
      TRACK_TYPES.register("high_speed",
          () -> new TrackType.Builder(RailcraftBlocks.ELECTRIC_FLEX_TRACK)
              .setEventHandler(SpeedController.HIGH_SPEED)
              .setHighSpeed(true)
              .build());

  public static final RegistryObject<TrackType> HIGH_SPEED_ELECTRIC =
      TRACK_TYPES.register("high_speed_electric",
          () -> new TrackType.Builder(RailcraftBlocks.ELECTRIC_FLEX_TRACK)
              .setEventHandler(
                  new CompositeHandler(CollisionHandler.ELECTRIC, SpeedController.HIGH_SPEED))
              .setElectric(true)
              .setHighSpeed(true)
              .build());

  public static final RegistryObject<TrackType> STRAP_IRON =
      TRACK_TYPES.register("strap_iron",
          () -> new TrackType.Builder(RailcraftBlocks.STRAP_IRON_FLEX_TRACK)
              .setEventHandler(SpeedController.STRAP_IRON)
              .build());

  public static final RegistryObject<TrackType> REINFORCED =
      TRACK_TYPES.register("reinforced",
          () -> new TrackType.Builder(RailcraftBlocks.REINFORCED_FLEX_TRACK)
              .setEventHandler(SpeedController.REINFORCED)
              .build());

  public static final RegistryObject<TrackType> IRON =
      TRACK_TYPES.register("iron",
          () -> new TrackType.Builder(() -> (AbstractRailBlock) Blocks.RAIL)
              .addOutfittedBlock(TrackKits.TURNOUT, RailcraftBlocks.TURNOUT_TRACK)
              .addOutfittedBlock(TrackKits.WYE, RailcraftBlocks.WYE_TRACK)
              .setEventHandler(SpeedController.IRON)
              .build());

  private static class CompositeHandler implements TrackType.EventHandler {

    private final CollisionHandler collisionHandler;
    private final SpeedController speedController;

    public CompositeHandler(CollisionHandler collisionHandler, SpeedController speedController) {
      this.collisionHandler = collisionHandler;
      this.speedController = speedController;
    }

    @Override
    public void minecartPass(World level, AbstractMinecartEntity cart, BlockPos pos) {
      this.speedController.minecartPass(level, cart, pos);
    }

    @Override
    public void entityInside(World level, BlockPos pos, BlockState blockState, Entity entity) {
      this.collisionHandler.entityInside(level, pos, blockState, entity);
    }

    @Override
    @Nullable
    public RailShape getRailShapeOverride(IBlockReader level,
        BlockPos pos, BlockState blockState, @Nullable AbstractMinecartEntity cart) {
      return this.speedController.getRailShapeOverride(level, pos, blockState, cart);
    }

    @Override
    public float getMaxSpeed(World level, @Nullable AbstractMinecartEntity cart, BlockPos pos) {
      return this.speedController.getMaxSpeed(level, cart, pos);
    }
  }
}
