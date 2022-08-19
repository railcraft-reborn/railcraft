package mods.railcraft.data.models;

import static net.minecraftforge.client.model.generators.ModelProvider.BLOCK_FOLDER;

import mods.railcraft.Railcraft;
import mods.railcraft.world.level.block.AbstractStrengthenedGlassBlock;
import mods.railcraft.world.level.block.RailcraftBlocks;
import mods.railcraft.world.level.block.tank.IronTankGaugeBlock;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.models.model.TextureMapping;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;

public class RailcraftBlockModelProvider extends BlockStateProvider {
    
    private static final String CUTOUT = "cutout";

    public RailcraftBlockModelProvider(DataGenerator generator,
        ExistingFileHelper existingFileHelper) {
        super(generator, Railcraft.ID, existingFileHelper);
    }

    private ResourceLocation key(Block block) {
        return ForgeRegistries.BLOCKS.getKey(block);
    }

    private String name(Block block) {
        return key(block).getPath();
    }
    private String name(Block block, String suffix) {
        return name(block) + suffix;
    }

    @Override
    protected void registerStatesAndModels() {
        for (DyeColor dyeColor : DyeColor.values()) {
            createStrengthenedGlass(RailcraftBlocks.STRENGTHENED_GLASS.variantFor(dyeColor).get());
            createStrengthenedGlass(RailcraftBlocks.IRON_TANK_GAUGE.variantFor(dyeColor).get());
            createStrengthenedGlass(RailcraftBlocks.STEEL_TANK_GAUGE.variantFor(dyeColor).get());
        }

    }

    private void createStrengthenedGlass(Block block) {
        var endTexture = TextureMapping.getBlockTexture(block, "_top");
        
        var singleModel = models().cubeAll(name(block, "_single"), endTexture)
            .renderType(CUTOUT);
        var topModel = models().cubeColumn(name(block, "_top"),
            TextureMapping.getBlockTexture(block, "_side_top"), endTexture)
            .renderType(CUTOUT);
        var centerModel = models().cubeColumn(name(block, "_center"),
            TextureMapping.getBlockTexture(block, "_side_center"), endTexture)
            .renderType(CUTOUT);
        var bottomModel = models().cubeColumn(name(block, "_bottom"),
            TextureMapping.getBlockTexture(block, "_side_bottom"), endTexture)
            .renderType(CUTOUT);

        getVariantBuilder(block)
            .forAllStatesExcept(blockState -> ConfiguredModel.builder()
                .modelFile(switch (blockState.getValue(AbstractStrengthenedGlassBlock.TYPE)) {
                case SINGLE -> singleModel;
                case TOP -> topModel;
                case CENTER -> centerModel;
                case BOTTOM -> bottomModel;
            }).build(), IronTankGaugeBlock.LEVEL);

        itemModels().withExistingParent(name(block),
            modLoc(BLOCK_FOLDER + "/" + name(block) + "_single"));
    }
}
