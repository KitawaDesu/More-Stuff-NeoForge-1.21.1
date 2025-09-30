package net.kitawa.more_stuff.experimentals.entities.models;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.kitawa.more_stuff.MoreStuff;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public class ThrownJavelinModel extends Model {
    public static final ModelLayerLocation SPEAR_LAYER =
            new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(MoreStuff.MOD_ID, "thrown_spear"), "main");

    private final ModelPart body;
    private final ModelPart middleSpike3;
    private final ModelPart middleSpike4;

    /** Per-part visibility */
    private final Map<String, Boolean> visibility = new HashMap<>();

    public ThrownJavelinModel(ModelPart root) {
        super(RenderType::entityCutout);

        this.body = root.getChild("body");
        this.middleSpike3 = root.getChild("middle_spike3");
        this.middleSpike4 = root.getChild("middle_spike4");

        visibility.put("body", true);
        visibility.put("middle_spike3", true);
        visibility.put("middle_spike4", true);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer,
                               int packedLight, int packedOverlay, int color) {
        if (visibility.getOrDefault("body", true)) {
            body.render(poseStack, buffer, packedLight, packedOverlay);
        }
        if (visibility.getOrDefault("middle_spike3", true)) {
            middleSpike3.render(poseStack, buffer, packedLight, packedOverlay);
        }
        if (visibility.getOrDefault("middle_spike4", true)) {
            middleSpike4.render(poseStack, buffer, packedLight, packedOverlay);
        }
    }

    /** Create the layer with parts matching your Blockbench JSON */
    public static LayerDefinition createLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        root.addOrReplaceChild(
                "body",
                CubeListBuilder.create()
                        .texOffs(0, 4)
                        .addBox(-0.5F, 0F, -0.5F, 1, 27, 1),
                PartPose.ZERO
        );

        root.addOrReplaceChild(
                "middle_spike3",
                CubeListBuilder.create()
                        .texOffs(4, 0)
                        .addBox(-2.5F, -4F, 0F, 5, 5, 0),
                PartPose.ZERO
        );

        root.addOrReplaceChild(
                "middle_spike4",
                CubeListBuilder.create()
                        .texOffs(4, -5)
                        .addBox(0F, -4F, -2.5F, 0, 5, 5),
                PartPose.ZERO
        );

        return LayerDefinition.create(mesh, 32, 32);
    }
}
