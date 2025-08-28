package mods.railcraft.world.level.block.charge;

import java.util.Collections;
import java.util.Map;
import mods.railcraft.api.charge.Charge;
import mods.railcraft.integrations.jei.JeiSearchable;
import mods.railcraft.util.BoxBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.InsideBlockEffectApplier;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public abstract class BatteryBlock extends ChargeBlock implements JeiSearchable {

  private static final VoxelShape SHAPE = Shapes.create(BoxBuilder.create()
      .box()
      .raiseCeiling(-0.0625D)
      .build());

  public BatteryBlock(Properties properties) {
    super(properties);
  }

  @Override
  public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos,
      CollisionContext context) {
    return SHAPE;
  }

  @Override
  protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity,
      InsideBlockEffectApplier effectApplier) {
    super.entityInside(state, level, pos, entity, effectApplier);
    if (level instanceof ServerLevel serverLevel) {
      Charge.distribution.network(serverLevel).access(pos)
          .zap(entity, Charge.DamageOrigin.BLOCK, 1F);
    }
  }

  protected abstract Spec getChargeSpec();

  @Override
  public Map<Charge, Spec> getChargeSpecs(BlockState state, ServerLevel level, BlockPos pos) {
    return Collections.singletonMap(Charge.distribution, getChargeSpec());
  }

  @Override
  public RenderShape getRenderShape(BlockState state) {
    return RenderShape.MODEL;
  }
}
