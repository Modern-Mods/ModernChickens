package strhercules.chickens.client.render;

import strhercules.chickens.ChickensMod;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;

/** 3x Java model conversion of vanilla chicken geometry and UVs. */
public final class MegaChickenModel extends EntityModel<MegaChickenRenderState> {
    private static final CubeDeformation PLANAR_ELEMENT = new CubeDeformation(0.01F);
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
            Identifier.fromNamespaceAndPath(ChickensMod.MOD_ID, "mega_chicken"), "main");

    private final ModelPart head;
    private final ModelPart beak;
    private final ModelPart redThing;
    private final ModelPart body;
    private final ModelPart rightLeg;
    private final ModelPart leftLeg;
    private final ModelPart rightWing;
    private final ModelPart leftWing;
    private final ModelPart saddle;
    private final ModelPart chests;
    private final ModelPart rightChest;
    private final ModelPart leftChest;

    public MegaChickenModel(ModelPart root) {
        super(root);
        this.head = root.getChild("head");
        this.beak = root.getChild("beak");
        this.redThing = root.getChild("red_thing");
        this.body = root.getChild("body");
        this.rightLeg = root.getChild("right_leg");
        this.leftLeg = root.getChild("left_leg");
        this.rightWing = root.getChild("right_wing");
        this.leftWing = root.getChild("left_wing");
        this.saddle = root.getChild("saddle");
        this.chests = root.getChild("chests");
        this.rightChest = this.chests.getChild("right_chest");
        this.leftChest = this.chests.getChild("left_chest");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        // The supplied Blockbench model is 3x vanilla. The 48-pixel Y offset
        // aligns its 72-pixel feet with the standard renderer's 24-pixel floor.
        root.addOrReplaceChild("head",
                CubeListBuilder.create().texOffs(0, 0)
                        .addBox(-6.0F, -18.0F, -6.0F, 12.0F, 18.0F, 9.0F),
                PartPose.offset(0.0F, -3.0F, -12.0F));
        root.addOrReplaceChild("beak",
                CubeListBuilder.create().texOffs(42, 0)
                        .addBox(-6.0F, -12.0F, -12.0F, 12.0F, 6.0F, 6.0F),
                PartPose.offset(0.0F, -3.0F, -12.0F));
        root.addOrReplaceChild("red_thing",
                CubeListBuilder.create().texOffs(42, 12)
                        .addBox(-3.0F, -6.0F, -9.0F, 6.0F, 6.0F, 6.0F),
                PartPose.offset(0.0F, -3.0F, -12.0F));
        root.addOrReplaceChild("body",
                CubeListBuilder.create().texOffs(0, 27)
                        .addBox(-9.0F, -12.0F, -9.0F, 18.0F, 24.0F, 18.0F),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -Mth.HALF_PI, 0.0F, 0.0F));

        CubeListBuilder leg = CubeListBuilder.create().texOffs(78, 0)
                .addBox(-3.0F, 0.0F, -9.0F, 9.0F, 15.0F, 9.0F);
        root.addOrReplaceChild("right_leg", leg, PartPose.offset(-6.0F, 9.0F, 3.0F));
        root.addOrReplaceChild("left_leg", leg, PartPose.offset(3.0F, 9.0F, 3.0F));

        root.addOrReplaceChild("right_wing",
                CubeListBuilder.create().texOffs(72, 39)
                        .addBox(0.0F, 0.0F, -9.0F, 3.0F, 12.0F, 18.0F),
                PartPose.offset(-12.0F, -9.0F, 0.0F));
        root.addOrReplaceChild("left_wing",
                CubeListBuilder.create().texOffs(72, 39)
                        .addBox(-3.0F, 0.0F, -9.0F, 3.0F, 12.0F, 18.0F),
                PartPose.offset(12.0F, -9.0F, 0.0F));

        PartDefinition saddle = root.addOrReplaceChild("saddle", CubeListBuilder.create(),
                PartPose.offset(0.0F, 24.0F, 0.0F));
        saddle.addOrReplaceChild("top", CubeListBuilder.create().texOffs(130, 2)
                        .addBox(-1.0F, 0.0F, -1.0F, 18.0F, 0.0F, 16.0F, PLANAR_ELEMENT),
                PartPose.offset(-8.0F, -33.0F, -8.0F));
        saddle.addOrReplaceChild("right_side", CubeListBuilder.create().texOffs(161, 2)
                        .addBox(-1.0F, -3.0F, -1.0F, 0.0F, 3.0F, 7.0F, PLANAR_ELEMENT),
                PartPose.offsetAndRotation(10.0F, -27.0F, 0.0F, Mth.HALF_PI, 0.0F, 0.0F));
        saddle.addOrReplaceChild("left_side", CubeListBuilder.create().texOffs(161, 2)
                        .addBox(-1.0F, -3.0F, -1.0F, 0.0F, 3.0F, 7.0F, PLANAR_ELEMENT),
                PartPose.offsetAndRotation(-10.0F, -27.0F, -3.0F,
                        Mth.HALF_PI, -Mth.PI, 0.0F));

        PartDefinition chests = root.addOrReplaceChild("chests", CubeListBuilder.create(),
                PartPose.offset(0.0F, 24.0F, 0.0F));
        PartDefinition rightChest = chests.addOrReplaceChild("right_chest", CubeListBuilder.create(),
                PartPose.ZERO);
        rightChest.addOrReplaceChild("face", CubeListBuilder.create().texOffs(152, 56)
                        .addBox(-1.0F, -8.0F, -1.0F, 8.0F, 8.0F, 0.0F, PLANAR_ELEMENT),
                PartPose.offset(1.0F, -24.0F, 15.0F));
        rightChest.addOrReplaceChild("near_side", CubeListBuilder.create().texOffs(161, 56)
                        .addBox(-1.0F, -8.0F, -1.0F, 2.0F, 8.0F, 0.0F, PLANAR_ELEMENT),
                PartPose.offsetAndRotation(1.0F, -24.0F, 13.0F, 0.0F, Mth.HALF_PI, 0.0F));
        rightChest.addOrReplaceChild("far_side", CubeListBuilder.create().texOffs(163, 56)
                        .addBox(-1.0F, -8.0F, -1.0F, 2.0F, 8.0F, 0.0F, PLANAR_ELEMENT),
                PartPose.offsetAndRotation(9.0F, -24.0F, 13.0F, 0.0F, Mth.HALF_PI, 0.0F));
        rightChest.addOrReplaceChild("top", CubeListBuilder.create().texOffs(152, 54)
                        .addBox(-1.0F, -2.0F, -1.0F, 8.0F, 2.0F, 0.0F, PLANAR_ELEMENT),
                PartPose.offsetAndRotation(1.0F, -33.0F, 14.0F, Mth.HALF_PI, 0.0F, 0.0F));
        rightChest.addOrReplaceChild("bottom", CubeListBuilder.create().texOffs(152, 54)
                        .addBox(-1.0F, -2.0F, -1.0F, 8.0F, 2.0F, 0.0F, PLANAR_ELEMENT),
                PartPose.offsetAndRotation(1.0F, -25.0F, 14.0F, Mth.HALF_PI, 0.0F, 0.0F));

        PartDefinition leftChest = chests.addOrReplaceChild("left_chest", CubeListBuilder.create(),
                PartPose.offset(-8.0F, 0.0F, 0.0F));
        leftChest.addOrReplaceChild("face", CubeListBuilder.create().texOffs(152, 56)
                        .addBox(-1.0F, -8.0F, -1.0F, 8.0F, 8.0F, 0.0F, PLANAR_ELEMENT),
                PartPose.offset(1.0F, -24.0F, 15.0F));
        leftChest.addOrReplaceChild("near_side", CubeListBuilder.create().texOffs(161, 56)
                        .addBox(-1.0F, -8.0F, -1.0F, 2.0F, 8.0F, 0.0F, PLANAR_ELEMENT),
                PartPose.offsetAndRotation(1.0F, -24.0F, 13.0F, 0.0F, Mth.HALF_PI, 0.0F));
        leftChest.addOrReplaceChild("far_side", CubeListBuilder.create().texOffs(163, 56)
                        .addBox(-1.0F, -8.0F, -1.0F, 2.0F, 8.0F, 0.0F, PLANAR_ELEMENT),
                PartPose.offsetAndRotation(9.0F, -24.0F, 13.0F, 0.0F, Mth.HALF_PI, 0.0F));
        leftChest.addOrReplaceChild("top", CubeListBuilder.create().texOffs(152, 54)
                        .addBox(-1.0F, -2.0F, -1.0F, 8.0F, 2.0F, 0.0F, PLANAR_ELEMENT),
                PartPose.offsetAndRotation(1.0F, -33.0F, 14.0F, Mth.HALF_PI, 0.0F, 0.0F));
        leftChest.addOrReplaceChild("bottom", CubeListBuilder.create().texOffs(152, 54)
                        .addBox(-1.0F, -2.0F, -1.0F, 8.0F, 2.0F, 0.0F, PLANAR_ELEMENT),
                PartPose.offsetAndRotation(1.0F, -25.0F, 14.0F, Mth.HALF_PI, 0.0F, 0.0F));

        return LayerDefinition.create(mesh, 192, 96);
    }

    @Override
    public void setupAnim(MegaChickenRenderState state) {
        super.setupAnim(state);
        float limbSwing = state.walkAnimationPos;
        float limbSwingAmount = state.walkAnimationSpeed;
        float ageInTicks = state.bob;
        float headPitch = state.xRot;
        float netHeadYaw = state.yRot;

        this.saddle.visible = state.tamed && state.saddled;
        this.rightChest.visible = state.tamed && state.rightChest;
        this.leftChest.visible = state.tamed && state.leftChest;

        this.head.xRot = headPitch * Mth.DEG_TO_RAD;
        this.head.yRot = netHeadYaw * Mth.DEG_TO_RAD;
        this.beak.xRot = this.head.xRot;
        this.beak.yRot = this.head.yRot;
        this.redThing.xRot = this.head.xRot;
        this.redThing.yRot = this.head.yRot;

        boolean airborne = state.airborne;
        float legSwing = airborne ? ageInTicks * 0.6662F : limbSwing * 0.6662F;
        float legSwingAmount = airborne ? 0.7F : Mth.clamp(limbSwingAmount, 0.0F, 1.0F);
        this.rightLeg.xRot = Mth.cos(legSwing) * 1.4F * legSwingAmount;
        this.leftLeg.xRot = Mth.cos(legSwing + Mth.PI) * 1.4F * legSwingAmount;
        float wingFlap = airborne ? Mth.sin(ageInTicks * 0.45F) * 0.75F : 0.0F;
        this.rightWing.zRot = wingFlap;
        this.leftWing.zRot = -wingFlap;
    }
}
