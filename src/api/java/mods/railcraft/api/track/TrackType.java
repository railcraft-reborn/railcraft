/*------------------------------------------------------------------------------
 Copyright (c) CovertJaguar, 2011-2020

 This work (the API) is licensed under the "MIT" License,
 see LICENSE.md for details.
 -----------------------------------------------------------------------------*/

package mods.railcraft.api.track;

import java.util.List;
import java.util.function.Supplier;
import javax.annotation.Nullable;
import com.google.common.collect.ImmutableList;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseRailBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.RailShape;
import net.minecraftforge.registries.ForgeRegistryEntry;

/**
 * Created by CovertJaguar on 8/10/2016 for Railcraft.
 *
 * @author CovertJaguar <https://www.railcraft.info>
 */
public class TrackType extends ForgeRegistryEntry<TrackType> {

  private final Supplier<? extends BaseRailBlock> flexBlock;
  private final List<Supplier<? extends BaseRailBlock>> spikeMaulVariants;
  private final boolean highSpeed;
  private final boolean electric;
  private final int maxSupportDistance;
  private final EventHandler eventHandler;

  public TrackType(Supplier<? extends BaseRailBlock> flexBlock,
      List<Supplier<? extends BaseRailBlock>> spikeMaulVariants,
      boolean highSpeed, boolean electric, int maxSupportDistance, EventHandler eventHandler) {
    this.flexBlock = flexBlock;
    this.spikeMaulVariants = spikeMaulVariants;
    this.highSpeed = highSpeed;
    this.electric = electric;
    this.maxSupportDistance = maxSupportDistance;
    this.eventHandler = eventHandler;
  }

  public BaseRailBlock getFlexBlock() {
    return this.flexBlock.get();
  }

  public List<Supplier<? extends BaseRailBlock>> getSpikeMaulVariants() {
    return this.spikeMaulVariants;
  }

  public boolean isHighSpeed() {
    return this.highSpeed;
  }

  public boolean isElectric() {
    return this.electric;
  }

  public int getMaxSupportDistance() {
    return this.maxSupportDistance;
  }

  public EventHandler getEventHandler() {
    return this.eventHandler;
  }

  public ItemStack getItemStack() {
    return this.getItemStack(1);
  }

  public ItemStack getItemStack(int qty) {
    return new ItemStack(this.getFlexBlock(), qty);
  }

  public static final class Builder {

    private final Supplier<? extends BaseRailBlock> flexBlock;
    private final ImmutableList.Builder<Supplier<? extends BaseRailBlock>> spikeMaulVariants =
        ImmutableList.builder();
    private boolean highSpeed;
    private boolean electric;
    private int maxSupportDistance;
    private EventHandler eventHandler = new EventHandler() {};

    public Builder(Supplier<? extends BaseRailBlock> flexBlock) {
      this.flexBlock = flexBlock;
      this.spikeMaulVariants.add(flexBlock);
    }

    public Builder addSpikeMaulVariant(Supplier<? extends BaseRailBlock> spikeMaulVariant) {
      this.spikeMaulVariants.add(spikeMaulVariant);
      return this;
    }

    public Builder setHighSpeed(boolean highSpeed) {
      this.highSpeed = highSpeed;
      return this;
    }

    public Builder setElectric(boolean electric) {
      this.electric = electric;
      return this;
    }

    public Builder setMaxSupportDistance(int maxSupportDistance) {
      this.maxSupportDistance = maxSupportDistance;
      return this;
    }

    public Builder setEventHandler(EventHandler eventHandler) {
      this.eventHandler = eventHandler;
      return this;
    }

    public TrackType build() {
      return new TrackType(this.flexBlock, this.spikeMaulVariants.build(), this.highSpeed,
          this.electric, this.maxSupportDistance, this.eventHandler);
    }
  }

  /**
   * Event handler for tracks
   */
  public interface EventHandler {

    /**
     * Invokes after a minecart has passed over us
     *
     * @param level - the level.
     * @param cart - The {@link AbstractMinecartEntity} that passed us.
     * @param pos - our position.
     */
    default void minecartPass(Level level, AbstractMinecart cart, BlockPos pos) {}

    @Nullable
    default RailShape getRailShapeOverride(BlockGetter level, BlockPos pos,
        BlockState blockState, @Nullable AbstractMinecart cart) {
      return null;
    }

    /**
     * Event handler for when a mob collides with us over (this) rail
     *
     * @see mods.railcraft.world.level.block.track.behaivor.CollisionHandler CollisionHandler
     * @param level The world.
     * @param pos Block's position in world
     * @param state The state of the track
     * @param entity Entity colliding with us
     */
    default void entityInside(ServerLevel level, BlockPos pos, BlockState state, Entity entity) {}

    /**
     * Returns the max speed of the rail at the specified position.
     *
     * @see mods.railcraft.world.level.block.track.behaivor.SpeedController SpeedController
     * @param level The world.
     * @param cart The cart on the rail, may be null.
     * @param pos Block's position in world
     * @return The max speed of the current rail.
     */
    default double getMaxSpeed(Level level, @Nullable AbstractMinecart cart, BlockPos pos) {
      return 0.4D;
    }
  }
}
