package mods.railcraft.data.models;

import static net.minecraftforge.client.model.generators.ModelProvider.BLOCK_FOLDER;

import mods.railcraft.Railcraft;
import mods.railcraft.world.level.block.AbstractStrengthenedGlassBlock;
import mods.railcraft.world.level.block.RailcraftBlocks;
import mods.railcraft.world.level.block.SteamTurbineBlock;
import mods.railcraft.world.level.block.SteamTurbineBlock.Type;
import mods.railcraft.world.level.block.manipulator.AdvancedItemLoaderBlock;
import mods.railcraft.world.level.block.steamboiler.FireboxBlock;
import mods.railcraft.world.level.block.tank.IronTankGaugeBlock;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.models.model.TextureMapping;
import net.minecraft.data.models.model.TextureSlot;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.client.model.generators.BlockModelBuilder;
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

    private BlockModelBuilder cube(String name, ResourceLocation parent, ResourceLocation down,
        ResourceLocation up,
        ResourceLocation north,
        ResourceLocation south, ResourceLocation east, ResourceLocation west,
        ResourceLocation particle) {
        return models().withExistingParent(name, parent)
            .texture("down", down)
            .texture("up", up)
            .texture("north", north)
            .texture("south", south)
            .texture("east", east)
            .texture("west", west)
            .texture("particle", particle);
    }

    @Override
    protected void registerStatesAndModels() {
        for (DyeColor dyeColor : DyeColor.values()) {
            createStrengthenedGlass(RailcraftBlocks.STRENGTHENED_GLASS.variantFor(dyeColor).get());
            createStrengthenedGlass(RailcraftBlocks.IRON_TANK_GAUGE.variantFor(dyeColor).get());
            createStrengthenedGlass(RailcraftBlocks.STEEL_TANK_GAUGE.variantFor(dyeColor).get());

            createTankValve(RailcraftBlocks.IRON_TANK_VALVE.variantFor(dyeColor).get(),
                RailcraftBlocks.IRON_TANK_WALL.variantFor(dyeColor).get());
            createTankValve(RailcraftBlocks.STEEL_TANK_VALVE.variantFor(dyeColor).get(),
                RailcraftBlocks.STEEL_TANK_WALL.variantFor(dyeColor).get());
            createCubeColumnBlock(RailcraftBlocks.IRON_TANK_WALL.variantFor(dyeColor).get());
            createCubeColumnBlock(RailcraftBlocks.STEEL_TANK_WALL.variantFor(dyeColor).get());
        }


        simpleBlock(RailcraftBlocks.STEEL_BLOCK.get());
        simpleBlock(RailcraftBlocks.BRASS_BLOCK.get());
        simpleBlock(RailcraftBlocks.BRONZE_BLOCK.get());
        simpleBlock(RailcraftBlocks.INVAR_BLOCK.get());
        simpleBlock(RailcraftBlocks.LEAD_BLOCK.get());
        simpleBlock(RailcraftBlocks.NICKEL_BLOCK.get());
        simpleBlock(RailcraftBlocks.SILVER_BLOCK.get());
        simpleBlock(RailcraftBlocks.TIN_BLOCK.get());
        simpleBlock(RailcraftBlocks.ZINC_BLOCK.get());
        simpleBlock(RailcraftBlocks.COKE_BLOCK.get());
        simpleBlock(RailcraftBlocks.CRUSHED_OBSIDIAN.get());

        createCubeColumnBlock(RailcraftBlocks.FEED_STATION.get());

        createFluidManipulator(RailcraftBlocks.FLUID_LOADER.get());
        createFluidManipulator(RailcraftBlocks.FLUID_UNLOADER.get());
        createManipulator(RailcraftBlocks.ITEM_LOADER.get());
        createManipulator(RailcraftBlocks.ITEM_UNLOADER.get());
        createDirectionalManipulator(RailcraftBlocks.ADVANCED_ITEM_LOADER.get());
        createDirectionalManipulator(RailcraftBlocks.ADVANCED_ITEM_UNLOADER.get());

        createFirebox(RailcraftBlocks.SOLID_FUELED_FIREBOX.get());
        createFirebox(RailcraftBlocks.FLUID_FUELED_FIREBOX.get());

        createSteamTurbine(RailcraftBlocks.STEAM_TURBINE.get());
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
            modLoc(BLOCK_FOLDER + "/" + name(block, "_single")));
    }

    private void createFluidManipulator(Block block) {
        var texture = TextureMapping.cubeBottomTop(block);
        var model = models().cubeBottomTop(name(block), texture.get(TextureSlot.SIDE),
                texture.get(TextureSlot.BOTTOM), texture.get(TextureSlot.TOP))
            .renderType(CUTOUT);

        simpleBlock(block, model);

        var side = new ResourceLocation(Railcraft.ID, "block/fluid_manipulator_side_inventory");
        var bottom = TextureMapping.getBlockTexture(block, "_bottom");
        var top = TextureMapping.getBlockTexture(block, "_top");
        models().cubeBottomTop(name(block, "_inventory"), side, bottom, top);

        itemModels().withExistingParent(name(block),
            modLoc(BLOCK_FOLDER + "/" + name(block, "_inventory")));
    }

    private void createManipulator(Block block) {
        var texture = TextureMapping.cubeBottomTop(block);
        var model = models().cubeBottomTop(name(block), texture.get(TextureSlot.SIDE),
            texture.get(TextureSlot.BOTTOM), texture.get(TextureSlot.TOP));

        simpleBlock(block, model);
    }

    private void createDirectionalManipulator(Block block) {

        var horizontalTexture = TextureMapping.orientableCubeOnlyTop(block);
        var horizontalModel = models().orientable(name(block),
            horizontalTexture.get(TextureSlot.SIDE), horizontalTexture.get(TextureSlot.FRONT),
            horizontalTexture.get(TextureSlot.TOP));

        var side = TextureMapping.getBlockTexture(block, "_side");
        var front = TextureMapping.getBlockTexture(block, "_front");
        var top = TextureMapping.getBlockTexture(block, "_top");
        var upModel = models().cubeBottomTop(name(block, "_up"), side, top, front);
        var downModel = models().cubeBottomTop(name(block, "_down"), side, front, top);

        getVariantBuilder(block)
            .forAllStatesExcept(blockState -> {
                var facing = blockState.getValue(AdvancedItemLoaderBlock.FACING);
                int yRot = 0;

                switch (facing) {
                    case SOUTH : yRot = 180; break;
                    case EAST : yRot = 90; break;
                    case WEST : yRot = 270; break;
                    case UP : return ConfiguredModel.builder().modelFile(upModel).build();
                    case DOWN: return ConfiguredModel.builder().modelFile(downModel).build();
                    default: break;
                }

                return ConfiguredModel.builder()
                    .modelFile(horizontalModel)
                    .rotationX(0)
                    .rotationY(yRot)
                    .build();
            }, AdvancedItemLoaderBlock.POWERED);
    }

    private void createFirebox(Block block) {
        var endTexture = TextureMapping.getBlockTexture(block, "_end");
        var sideTexture = TextureMapping.getBlockTexture(block, "_side");
        var sideLitTexture = TextureMapping.getBlockTexture(block, "_side_lit");

        var model = models().cubeColumn(name(block), sideTexture, endTexture);
        var litModel = models().cubeColumn(name(block, "_lit"), sideLitTexture, endTexture);

        getVariantBuilder(block)
            .forAllStates(blockState -> {
                var lit = blockState.getValue(FireboxBlock.LIT);
                return ConfiguredModel.builder().modelFile(lit ? litModel : model).build();
            });
    }

    private void createSteamTurbine(Block block) {
        var sideTexture = TextureMapping.getBlockTexture(block, "_side");

        createSteamTurbineModel(block, sideTexture, "_inventory", false);
        itemModels().withExistingParent(name(block),
            modLoc(BLOCK_FOLDER + "/" + name(block, "_inventory")));

        var noneVariant = models().cubeAll(name(block, "_side"), sideTexture);

        getVariantBuilder(block)
            .forAllStates(blockState -> {
                var type = blockState.getValue(SteamTurbineBlock.TYPE);
                var rotated = blockState.getValue(SteamTurbineBlock.ROTATED);

                if(type == Type.NONE) {
                    return ConfiguredModel.builder().modelFile(noneVariant).build();
                } else {
                    var model = this.createSteamTurbineModel(block, sideTexture, "_" + type.getSerializedName(),
                        type != SteamTurbineBlock.Type.WINDOW);
                    return ConfiguredModel.builder()
                        .modelFile(model)
                        .rotationY(rotated ? 90 : 0)
                        .build();
                }
            });
    }

    private BlockModelBuilder createSteamTurbineModel(Block block, ResourceLocation sideTexture,
        String suffix, boolean rotated) {
        var frontTexture = TextureMapping.getBlockTexture(block, suffix);
        var parent = modLoc(BLOCK_FOLDER + "/template_mirrored_cube");
        return cube(name(block, suffix), parent,
            sideTexture,
            sideTexture,
            rotated ? sideTexture : frontTexture,
            rotated ? sideTexture : frontTexture,
            rotated ? frontTexture : sideTexture,
            rotated ? frontTexture : sideTexture,
            sideTexture);
    }

    private void createTankValve(Block block, Block wallBlock) {
        var verticalModel = cube(name(block), mcLoc("cube"),
            TextureMapping.getBlockTexture(block, "_top"),
            TextureMapping.getBlockTexture(block, "_top"),
            TextureMapping.getBlockTexture(wallBlock, "_side"),
            TextureMapping.getBlockTexture(wallBlock, "_side"),
            TextureMapping.getBlockTexture(wallBlock, "_side"),
            TextureMapping.getBlockTexture(wallBlock, "_side"),
            TextureMapping.getBlockTexture(wallBlock, "_top"));

        var horizontalModel = cube(name(block, "_horizontal"), mcLoc("cube"),
            TextureMapping.getBlockTexture(wallBlock, "_top"),
            TextureMapping.getBlockTexture(wallBlock, "_top"),
            TextureMapping.getBlockTexture(block, "_front"),
            TextureMapping.getBlockTexture(block, "_front"),
            TextureMapping.getBlockTexture(wallBlock, "_side"),
            TextureMapping.getBlockTexture(wallBlock, "_side"),
            TextureMapping.getBlockTexture(wallBlock, "_top"));

        getVariantBuilder(block)
            .forAllStates(blockState -> {
                var axis = blockState.getValue(BlockStateProperties.AXIS);
                return switch (axis) {
                    case Y -> ConfiguredModel.builder().modelFile(verticalModel).build();
                    case Z -> ConfiguredModel.builder().modelFile(horizontalModel).build();
                    case X -> ConfiguredModel.builder().modelFile(horizontalModel).rotationY(90).build();
                };
            });
    }

    private void createCubeColumnBlock(Block block) {
        var model = models().cubeColumn(name(block), TextureMapping.getBlockTexture(block, "_side"),
            TextureMapping.getBlockTexture(block, "_top"));
        simpleBlock(block, model);
    }
}
