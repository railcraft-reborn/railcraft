/*------------------------------------------------------------------------------
 Copyright (c) Railcraft Reborn, 2023+

 This work (the API) is licensed under the "MIT" License,
 see LICENSE.md for details.
 -----------------------------------------------------------------------------*/

package mods.railcraft.api.helper;

import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.fluids.FluidStack;

public interface StructureHelper {

  void placeBlastFurnace(Level world, BlockPos pos, ItemStack input, ItemStack output,
      ItemStack secondOutput, ItemStack fuel);

  void placeCokeOven(Level world, BlockPos pos, int creosote, ItemStack input, ItemStack output);

  void placeFluidBoiler(Level world, BlockPos pos, int width, int height, boolean highPressure,
      int water, FluidStack fuel);

  void placeIronTank(Level world, BlockPos pos, int patternIndex, FluidStack fluid);

  void placeRockCrusher(Level world, BlockPos pos, int patternIndex, List<ItemStack> input,
      List<ItemStack> output);

  void placeSolidBoiler(Level world, BlockPos pos, int width, int height, boolean highPressure,
      int water, List<ItemStack> fuel);

  void placeSteamOven(Level world, BlockPos pos, List<ItemStack> input, List<ItemStack> output);

  void placeSteelTank(Level world, BlockPos pos, int patternIndex, FluidStack fluid);

  void placeWaterTank(Level world, BlockPos pos, int water);

  void placeFluxTransformer(Level world, BlockPos pos);
}
