package strhercules.chickens.client.render;

import strhercules.chickens.ChickensMod;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;

/**
 * Modern port of Hatchery's {@code ModelRooster}. The geometry and texture
 * coordinates mirror the original model so the legacy rooster texture maps
 * cleanly onto the 1.21.1 model system.
 */
public class RoosterModel extends EntityModel<RoosterRenderState> {
    public static final ModelLayerLocation LAYER_LOCATION =
            new ModelLayerLocation(Identifier.fromNamespaceAndPath(ChickensMod.MOD_ID, "rooster"), "main");

    private final ModelPart head;
    private final ModelPart rightLeg;
    private final ModelPart chin;
    private final ModelPart bill;
    private final ModelPart leftLeg;
    private final ModelPart leftWing;
    private final ModelPart rightWing;
    private final ModelPart tailBottom;
    private final ModelPart crest;
    private final ModelPart tailMain;
    private final ModelPart tailTop;
    private final ModelPart body;
    private final ModelPart neck;

    public RoosterModel(ModelPart root) {
        super(root);
        this.head = root.getChild("head");
        this.rightLeg = root.getChild("right_leg");
        this.chin = root.getChild("chin");
        this.bill = root.getChild("bill");
        this.leftLeg = root.getChild("left_leg");
        this.leftWing = root.getChild("left_wing");
        this.rightWing = root.getChild("right_wing");
        this.tailBottom = root.getChild("tail_bottom");
        this.crest = root.getChild("crest");
        this.tailMain = root.getChild("tail_main");
        this.tailTop = root.getChild("tail_top");
        this.body = root.getChild("body");
        this.neck = root.getChild("neck");
    }

    /**
     * Rebuilds the original rooster cuboids using the 1.21.1 model layer API.
     * Texture offsets and dimensions are copied directly from Hatchery's
     * {@code ModelRooster}.
     */
    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        // Left wing
        root.addOrReplaceChild("left_wing",
                CubeListBuilder.create().texOffs(24, 13)
                        .addBox(-1.0F, 0.0F, -3.0F, 1.0F, 4.0F, 6.0F),
                PartPose.offset(4.0F, 13.0F, 0.0F));

        // Tail main plume
        root.addOrReplaceChild("tail_main",
                CubeListBuilder.create().texOffs(39, 13)
                        .addBox(0.0F, -2.3F, -1.5F, 1.0F, 4.0F, 6.0F),
                PartPose.offsetAndRotation(-0.5F, 14.0F, 4.0F,
                        0.72850043F, 0.0F, 0.0F));

        // Tail bottom feather
        root.addOrReplaceChild("tail_bottom",
                CubeListBuilder.create().texOffs(39, 16)
                        .addBox(0.0F, 1.5F, 1.5F, 1.0F, 1.0F, 1.0F),
                PartPose.offsetAndRotation(-0.5F, 14.0F, 4.0F,
                        0.72850043F, 0.0F, 0.0F));

        // Head
        root.addOrReplaceChild("head",
                CubeListBuilder.create().texOffs(0, 0)
                        .addBox(-2.0F, -6.0F, -2.0F, 4.0F, 7.0F, 3.0F),
                PartPose.offset(0.0F, 14.0F, -4.0F));

        // Body
        root.addOrReplaceChild("body",
                CubeListBuilder.create().texOffs(0, 10)
                        .addBox(-3.0F, -4.0F, -3.0F, 6.0F, 8.0F, 6.0F),
                PartPose.offsetAndRotation(0.0F, 16.0F, 0.0F,
                        1.3089969F, 0.0F, 0.0F));

        // Right leg
        CubeListBuilder leg = CubeListBuilder.create().texOffs(26, 0)
                .addBox(-1.0F, 0.0F, -3.0F, 3.0F, 5.0F, 3.0F);
        root.addOrReplaceChild("right_leg", leg, PartPose.offset(-2.0F, 19.0F, 1.0F));
        root.addOrReplaceChild("left_leg", leg, PartPose.offset(1.0F, 19.0F, 1.0F));

        // Crest
        root.addOrReplaceChild("crest",
                CubeListBuilder.create().texOffs(18, 7)
                        .addBox(-0.5F, -8.0F, -3.0F, 1.0F, 4.0F, 5.0F),
                PartPose.offset(0.0F, 15.0F, -4.0F));

        // Chin / wattle
        root.addOrReplaceChild("chin",
                CubeListBuilder.create().texOffs(14, 4)
                        .addBox(-1.0F, -2.0F, -3.0F, 2.0F, 2.0F, 2.0F),
                PartPose.offset(0.0F, 14.0F, -4.0F));

        // Right wing
        root.addOrReplaceChild("right_wing",
                CubeListBuilder.create().texOffs(24, 13)
                        .addBox(0.0F, 0.0F, -3.0F, 1.0F, 4.0F, 6.0F),
                PartPose.offset(-4.0F, 13.0F, 0.0F));

        // Tail top feather
        root.addOrReplaceChild("tail_top",
                CubeListBuilder.create().texOffs(39, 13)
                        .addBox(0.0F, 1.5F, 3.5F, 1.0F, 1.0F, 1.0F),
                PartPose.offsetAndRotation(-0.5F, 14.0F, 4.0F,
                        0.72850043F, 0.0F, 0.0F));

        // Beak
        root.addOrReplaceChild("bill",
                CubeListBuilder.create().texOffs(14, 0)
                        .addBox(-2.0F, -4.0F, -4.0F, 4.0F, 2.0F, 2.0F),
                PartPose.offset(0.0F, 14.0F, -4.0F));

        // Neck
        root.addOrReplaceChild("neck",
                CubeListBuilder.create().texOffs(33, 3)
                        .addBox(-2.5F, -5.0F, -4.5F, 5.0F, 3.0F, 6.0F),
                PartPose.offsetAndRotation(0.0F, 16.0F, 0.0F,
                        0.7853982F, 0.0F, 0.0F));

        return LayerDefinition.create(mesh, 64, 32);
    }

    @Override
    public void setupAnim(RoosterRenderState state) {
        super.setupAnim(state);
        float limbSwing = state.walkAnimationPos;
        float limbSwingAmount = state.walkAnimationSpeed;
        float flapAngle = (Mth.sin(state.flap) + 1.0F) * state.flapSpeed;
        this.head.xRot = state.xRot * 0.017453292F;
        this.head.yRot = state.yRot * 0.017453292F;
        this.bill.xRot = this.head.xRot;
        this.bill.yRot = this.head.yRot;
        this.chin.xRot = this.head.xRot;
        this.chin.yRot = this.head.yRot;
        this.crest.xRot = this.head.xRot;
        this.crest.yRot = this.head.yRot;

        this.rightLeg.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
        this.leftLeg.xRot = Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount;
        this.rightWing.zRot = flapAngle;
        this.leftWing.zRot = -flapAngle;
    }
}

