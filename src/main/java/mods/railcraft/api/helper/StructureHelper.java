/*------------------------------------------------------------------------------
 Copyright (c) CovertJaguar, 2011-2020

 This work (the API) is licensed under the "MIT" License,
 see LICENSE.md for details.
 -----------------------------------------------------------------------------*/

package mods.railcraft.api.helper;

import java.util.List;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;

/**
 * @author CovertJaguar <http://www.railcraft.info/>
 */
public interface StructureHelper {

  void placeBlastFurnace(World world, BlockPos pos, ItemStack input, ItemStack output,
      ItemStack secondOutput, ItemStack fuel);

  void placeCokeOven(World world, BlockPos pos, int creosote, ItemStack input, ItemStack output);

  void placeFluidBoiler(World world, BlockPos pos, int width, int height, boolean highPressure,
      int water, FluidStack fuel);

  void placeIronTank(World world, BlockPos pos, int patternIndex, FluidStack fluid);

  void placeRockCrusher(World world, BlockPos pos, int patternIndex, List<ItemStack> input,
      List<ItemStack> output);

  void placeSolidBoiler(World world, BlockPos pos, int width, int height, boolean highPressure,
      int water, List<ItemStack> fuel);

  void placeSteamOven(World world, BlockPos pos, List<ItemStack> input, List<ItemStack> output);

  void placeSteelTank(World world, BlockPos pos, int patternIndex, FluidStack fluid);

  void placeWaterTank(World world, BlockPos pos, int water);

  void placeFluxTransformer(World world, BlockPos pos);
}
