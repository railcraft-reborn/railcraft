package mods.railcraft.world.level.block.entity;

import mods.railcraft.util.AABBFactory;
import mods.railcraft.util.container.CompositeContainerAdaptor;
import mods.railcraft.util.container.filters.StackFilters;
import mods.railcraft.world.inventory.FeedStationMenu;
import mods.railcraft.world.level.block.FeedStationBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class FeedStationBlockEntity extends ContainerBlockEntity implements MenuProvider {

  private static final int AREA = 3;
  private static final int MIN_FEED_INTERVAL = 128;
  private static final int FEED_VARIANCE = 256;
  private static final byte ANIMALS_PER_FOOD = 2;
  private int feedTime;
  private byte feedCounter;

  private int processingTicks;

  public FeedStationBlockEntity(BlockPos blockPos, BlockState blockState) {
    super(RailcraftBlockEntityTypes.FEED_STATION.get(), blockPos, blockState, 1);
  }

  public static void serverTick(Level level, BlockPos blockPos, BlockState blockState,
      FeedStationBlockEntity blockEntity) {

    ItemStack feed = blockEntity.getItem(0);

    if (blockEntity.processingTicks++ >= (MIN_FEED_INTERVAL / 4)
        && (feed.isEmpty() || feed.getCount() < feed.getMaxStackSize())) {
      var chests = CompositeContainerAdaptor.of(blockEntity.getAdjacentContainers());
      chests.moveOneItemTo(blockEntity, StackFilters.FEED);
      blockEntity.setChanged();
    }

    feed = blockEntity.getItem(0);

    blockEntity.feedTime--;

    var powered = blockState.getValue(FeedStationBlock.POWERED);

    if (!powered && !feed.isEmpty() && blockEntity.feedTime <= 0) {
      blockEntity.feedTime = MIN_FEED_INTERVAL + level.getRandom().nextInt(FEED_VARIANCE);
      var box = AABBFactory.start()
          .at(blockPos)
          .raiseFloor(-1)
          .raiseCeiling(2)
          .expandHorizontally(AREA)
          .build();
      var animals = level.getEntitiesOfClass(Animal.class, box);
      for (var target : animals) {
        if (target.isFood(blockEntity.getItem(0)) && blockEntity.feedAnimal(target)) {
          if (blockEntity.feedCounter <= 0) {
            feed.shrink(1);
            blockEntity.feedCounter = ANIMALS_PER_FOOD;
          }
          blockEntity.feedCounter--;
          blockEntity.setChanged();
          break;
        }
      }
    }
  }

  private boolean feedAnimal(Animal animal) {
    if (animal.getAge() == 0 && animal.canFallInLove()) {
      var player = this.level instanceof ServerLevel serverLevel
          ? this.getOwner()
              .<Player>map(
                  profile -> serverLevel.getServer().getPlayerList().getPlayer(profile.getId()))
              .orElse(null)
          : null;

      animal.setInLove(player);

      this.level.addParticle(ParticleTypes.HAPPY_VILLAGER, animal.getRandomX(1.0D),
          animal.getRandomY() + 0.5D, animal.getRandomZ(1.0D), 0.0D, 0.0D, 0.0D);

      return true;
    }

    return false;
  }

  @Override
  public void load(CompoundTag tag) {
    super.load(tag);
    this.feedCounter = tag.getByte("feedCounter");
  }

  @Override
  public void saveAdditional(CompoundTag tag) {
    super.saveAdditional(tag);
    tag.putByte("feedCounter", this.feedCounter);
  }

  @Override
  public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
    return new FeedStationMenu(id, inventory, this);
  }
}
