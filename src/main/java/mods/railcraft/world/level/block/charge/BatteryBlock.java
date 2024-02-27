package mods.railcraft.world.level.block.charge;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.jetbrains.annotations.Nullable;
import mods.railcraft.Translations;
import mods.railcraft.api.charge.Charge;
import mods.railcraft.api.charge.ChargeStorage;
import mods.railcraft.integrations.jei.JeiSearchable;
import mods.railcraft.util.BoxBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
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

  @SuppressWarnings("deprecation")
  @Override
  public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
    super.entityInside(state, level, pos, entity);
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

  @Override
  public void appendHoverText(ItemStack stack, @Nullable BlockGetter level,
      List<Component> tooltip, TooltipFlag flag) {
    tooltip.add(Component.translatable(Translations.Tips.CHARGE_NETWORK_BATTERY)
        .withStyle(ChatFormatting.BLUE));

    var spec = getChargeSpec();
    var isRechargeable = spec.storageSpec().initialState()
        .equals(ChargeStorage.State.RECHARGEABLE);
    var capacity = spec.storageSpec().capacity() / 1000;
    var maxDraw = spec.storageSpec().maxDraw();
    var loss = spec.losses();
    var efficiency = (int) (spec.storageSpec().efficiency() * 100);

    tooltip.add(Component.translatable(isRechargeable
        ? Translations.Tips.TYPE_RECHARGEABLE
        : Translations.Tips.TYPE_DISPOSABLE)
        .withStyle(ChatFormatting.BLUE));
    tooltip.add(Component.translatable(Translations.Tips.CAPACITY, capacity)
        .withStyle(ChatFormatting.GRAY));
    tooltip.add(Component.translatable(Translations.Tips.MAX_DRAW, maxDraw)
        .withStyle(ChatFormatting.GRAY));
    tooltip.add(Component.translatable(Translations.Tips.LOSS, loss)
        .withStyle(ChatFormatting.GRAY));
    tooltip.add(Component.translatable(Translations.Tips.EFFICIENCY, efficiency)
        .withStyle(ChatFormatting.GRAY));
  }
}
