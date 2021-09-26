/*------------------------------------------------------------------------------
 Copyright (c) CovertJaguar, 2011-2020

 This work (the API) is licensed under the "MIT" License,
 see LICENSE.md for details.
 -----------------------------------------------------------------------------*/

package mods.railcraft.api.tracks;

import java.util.function.Supplier;
import javax.annotation.Nullable;
import mods.railcraft.api.core.ILocalizedObject;
import net.minecraft.block.AbstractRailBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.properties.RailShape;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistryEntry;

/**
 * Created by CovertJaguar on 8/10/2016 for Railcraft.
 *
 * @author CovertJaguar <http://www.railcraft.info>
 */
public class TrackType extends ForgeRegistryEntry<TrackType>
    implements IStringSerializable, ILocalizedObject {

  public static final String NBT_TAG = "rail";

  private final Supplier<? extends AbstractRailBlock> baseBlock;
  private final boolean highSpeed;
  private final boolean electric;
  private final int maxSupportDistance;
  private final EventHandler eventHandler;

  public TrackType(Supplier<? extends AbstractRailBlock> baseBlock, boolean highSpeed,
      boolean electric, int maxSupportDistance,
      EventHandler eventHandler) {
    this.baseBlock = baseBlock;
    this.highSpeed = highSpeed;
    this.electric = electric;
    this.maxSupportDistance = maxSupportDistance;
    this.eventHandler = eventHandler;
  }

  public boolean isHighSpeed() {
    return highSpeed;
  }

  public boolean isElectric() {
    return electric;
  }

  @Override
  public final String getSerializedName() {
    return getRegistryName().toString().replaceAll("[.:]", "_");
  }

  @Override
  public String getLocalizationTag() {
    return "track_type.railcraft." + getSerializedName() + ".name";
  }

  public AbstractRailBlock getBaseBlock() {
    return this.baseBlock.get();
  }

  public ItemStack getFlexStack() {
    return getFlexStack(1);
  }

  public ItemStack getFlexStack(int qty) {
    return new ItemStack(getBaseBlock(), qty);
  }

  public int getMaxSupportDistance() {
    return maxSupportDistance;
  }

  public EventHandler getEventHandler() {
    return eventHandler;
  }

  @Override
  public String toString() {
    return "TrackType{" + getSerializedName() + "}";
  }

  public static final class Builder {

    private final Supplier<? extends AbstractRailBlock> baseBlock;
    private boolean highSpeed;
    private boolean electric;
    private int maxSupportDistance;
    private EventHandler eventHandler;

    public Builder(Supplier<? extends AbstractRailBlock> baseBlock) {
      this.baseBlock = baseBlock;
    }

    public TrackType build() {
      if (eventHandler == null)
        eventHandler = new EventHandler();
      return new TrackType(baseBlock, highSpeed, electric, maxSupportDistance, eventHandler);
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
  }

  /**
   * Event handler for tracks
   * @see mods.railcraft.world.level.block.track.TrackTypes.Handler TrackTypes Handler
   */
  public static class EventHandler {

    /**
     * Invokes after a minecart has passed over us
     * @param worldIn The world.
     * @param cart The minecart entity that passed us.
     * @param pos Our position in the world
     * @param trackKit Track kit installed on us, if any.
     */
    public void onMinecartPass(World worldIn, AbstractMinecartEntity cart, BlockPos pos,
        @Nullable TrackKit trackKit) {}

    public @Nullable RailShape getRailDirectionOverride(IBlockReader world, BlockPos pos,
        BlockState state, @Nullable AbstractMinecartEntity cart) {
      return null;
    }

    /**
     * Event handler for when a mob collides with us over (this) rail
     * @see mods.railcraft.world.level.block.track.behaivor.CollisionHandler CollisionHandler
     * @param world The world.
     * @param pos Block's position in world
     * @param state The state of the track
     * @param entity Entity colliding with us
     */
    public void onEntityCollision(World world, BlockPos pos, BlockState state, Entity entity) {}

    /**
     * Returns the max speed of the rail at the specified position.
     * @see mods.railcraft.world.level.block.track.behaivor.SpeedController SpeedController
     * @param world The world.
     * @param cart The cart on the rail, may be null.
     * @param pos Block's position in world
     * @return The max speed of the current rail.
     */
    public double getMaxSpeed(World world, @Nullable AbstractMinecartEntity cart, BlockPos pos) {
      return 0.4d;
    }
  }
}
