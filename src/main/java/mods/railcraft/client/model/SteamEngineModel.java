package mods.railcraft.client.model;

import java.util.EnumMap;
import java.util.Map;
import mods.railcraft.world.level.block.entity.SteamEngineBlockEntity.EnergyStage;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.rendertype.RenderTypes;

/**
 * Steam engine pointing up. The frame moves up and down following the piston stroke and the
 * piston rings fill the gap between the base and the frame.
 */
public class SteamEngineModel extends Model<SteamEngineModel.State> {

  private static final float MAX_STROKE = 7.99F;
  private static final int PISTON_SPACING = 2;
  private static final int PISTON_COUNT = 5;
  private static final float PISTON_OFFSET = -0.16F;

  private final ModelPart frame;
  private final ModelPart[] pistons = new ModelPart[PISTON_COUNT];
  private final Map<EnergyStage, ModelPart> trunks = new EnumMap<>(EnergyStage.class);

  public SteamEngineModel(ModelPart root) {
    super(root, RenderTypes::entityCutout);
    this.frame = root.getChild("frame");
    for (int i = 0; i < PISTON_COUNT; i++) {
      this.pistons[i] = root.getChild("piston_" + i);
    }
    for (var stage : EnergyStage.values()) {
      if (stage != EnergyStage.OVERHEAT) {
        this.trunks.put(stage, root.getChild(trunkName(stage)));
      }
    }
  }

  private static String trunkName(EnergyStage stage) {
    return "trunk_" + stage.getSerializedName();
  }

  public static LayerDefinition createBodyLayer() {
    var mesh = new MeshDefinition();
    var root = mesh.getRoot();
    var pose = PartPose.offset(8.0F, 8.0F, 8.0F);

    root.addOrReplaceChild("base", CubeListBuilder.create()
        .texOffs(1, 1).addBox(-8F, -8F, -8F, 16, 4, 16), pose);

    root.addOrReplaceChild("frame", CubeListBuilder.create()
        .texOffs(65, 1).addBox(-6F, -4F, -6F, 12, 4, 12)
        .texOffs(1, 23).addBox(-3F, -4F, -8F, 6, 4, 16)
        .texOffs(1, 45).addBox(-8F, -4F, -3F, 16, 4, 6), pose);

    for (int i = 0; i < PISTON_COUNT; i++) {
      root.addOrReplaceChild("piston_" + i, CubeListBuilder.create()
          .texOffs(32, 24)
          .addBox(-5F, -4F, -5F, 10, 2, 10)
          .addBox(-7F, -4F, -1F, 1, 2, 2)
          .addBox(-1F, -4F, -7F, 2, 2, 1)
          .addBox(6F, -4F, -1F, 1, 2, 2)
          .addBox(-1F, -4F, 6F, 2, 2, 1),
          PartPose.offset(8.0F, 8.0F + PISTON_OFFSET + i * PISTON_SPACING, 8.0F));
    }

    addTrunk(root, EnergyStage.BLUE, 1, 57);
    addTrunk(root, EnergyStage.GREEN, 35, 57);
    addTrunk(root, EnergyStage.YELLOW, 69, 57);
    addTrunk(root, EnergyStage.ORANGE, 1, 79);
    addTrunk(root, EnergyStage.RED, 35, 79);

    return LayerDefinition.create(mesh, 128, 128);
  }

  private static void addTrunk(PartDefinition root, EnergyStage stage, int u, int v) {
    root.addOrReplaceChild(trunkName(stage), CubeListBuilder.create()
        .texOffs(u, v).addBox(-4F, -4F, -4F, 8, 12, 8), PartPose.offset(8.0F, 8.0F, 8.0F));
  }

  @Override
  public void setupAnim(State state) {
    super.setupAnim(state);
    // Overheat shows the red trunk
    var visibleTrunk = state.stage() == EnergyStage.OVERHEAT ? EnergyStage.RED : state.stage();
    this.trunks.forEach((stage, trunk) -> trunk.visible = stage == visibleTrunk);

    float progress = state.progress();
    float step = progress > 0.5F
        ? MAX_STROKE - (progress - 0.5F) * 2.0F * MAX_STROKE
        : progress * 2.0F * MAX_STROKE;
    this.frame.y += step;
    for (int i = 0; i < PISTON_COUNT; i++) {
      this.pistons[i].visible = i * PISTON_SPACING <= step + 2;
    }
  }

  /**
   * @param stage energy stage, selects the trunk colour.
   * @param progress piston progress of the current stroke, 0 to 1.
   */
  public record State(EnergyStage stage, float progress) {}
}
