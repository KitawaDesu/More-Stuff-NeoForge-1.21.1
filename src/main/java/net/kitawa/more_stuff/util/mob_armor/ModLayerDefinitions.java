package net.kitawa.more_stuff.util.mob_armor;

import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ModLayerDefinitions {
    private static final CubeDeformation cubeDeformation = new CubeDeformation(0.2F);

    public static LayerDefinition HoglinArmor() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        CubeDeformation expand = new CubeDeformation(0.2F); // Avoids Z-fighting

        PartDefinition partdefinition1 = partdefinition.addOrReplaceChild(
                "body",
                CubeListBuilder.create().texOffs(1, 1)
                        .addBox(-8.0F, -7.0F, -13.0F, 16.0F, 14.0F, 26.0F, expand),
                PartPose.offset(0.0F, 7.0F, 0.0F)
        );

        partdefinition1.addOrReplaceChild(
                "mane",
                CubeListBuilder.create().texOffs(90, 33)
                        .addBox(0.0F, 0.0F, -9.0F, 0.0F, 10.0F, 19.0F, new CubeDeformation(0.201F)), // Slightly more to prevent overlap
                PartPose.offset(0.0F, -14.0F, -5.0F)
        );

        PartDefinition partdefinition2 = partdefinition.addOrReplaceChild(
                "head",
                CubeListBuilder.create().texOffs(61, 1)
                        .addBox(-7.0F, -3.0F, -19.0F, 14.0F, 6.0F, 19.0F, expand),
                PartPose.offsetAndRotation(0.0F, 2.0F, -12.0F, 0.87266463F, 0.0F, 0.0F)
        );

        partdefinition2.addOrReplaceChild(
                "right_ear",
                CubeListBuilder.create().texOffs(1, 1)
                        .addBox(-6.0F, -1.0F, -2.0F, 6.0F, 1.0F, 4.0F, expand),
                PartPose.offsetAndRotation(-6.0F, -2.0F, -3.0F, 0.0F, 0.0F, (float) (-Math.PI * 2.0 / 9.0))
        );

        partdefinition2.addOrReplaceChild(
                "left_ear",
                CubeListBuilder.create().texOffs(1, 6)
                        .addBox(0.0F, -1.0F, -2.0F, 6.0F, 1.0F, 4.0F, expand),
                PartPose.offsetAndRotation(6.0F, -2.0F, -3.0F, 0.0F, 0.0F, (float) (Math.PI * 2.0 / 9.0))
        );

        partdefinition2.addOrReplaceChild(
                "right_horn",
                CubeListBuilder.create().texOffs(10, 13)
                        .addBox(-1.0F, -11.0F, -1.0F, 2.0F, 11.0F, 2.0F, expand),
                PartPose.offset(-7.0F, 2.0F, -12.0F)
        );

        partdefinition2.addOrReplaceChild(
                "left_horn",
                CubeListBuilder.create().texOffs(1, 13)
                        .addBox(-1.0F, -11.0F, -1.0F, 2.0F, 11.0F, 2.0F, expand),
                PartPose.offset(7.0F, 2.0F, -12.0F)
        );

        partdefinition.addOrReplaceChild(
                "right_front_leg",
                CubeListBuilder.create().texOffs(66, 42)
                        .addBox(-3.0F, 0.0F, -3.0F, 6.0F, 14.0F, 6.0F, expand),
                PartPose.offset(-4.0F, 10.0F, -8.5F)
        );

        partdefinition.addOrReplaceChild(
                "left_front_leg",
                CubeListBuilder.create().texOffs(41, 42)
                        .addBox(-3.0F, 0.0F, -3.0F, 6.0F, 14.0F, 6.0F, expand),
                PartPose.offset(4.0F, 10.0F, -8.5F)
        );

        partdefinition.addOrReplaceChild(
                "right_hind_leg",
                CubeListBuilder.create().texOffs(21, 45)
                        .addBox(-2.5F, 0.0F, -2.5F, 5.0F, 11.0F, 5.0F, expand),
                PartPose.offset(-5.0F, 13.0F, 10.0F)
        );

        partdefinition.addOrReplaceChild(
                "left_hind_leg",
                CubeListBuilder.create().texOffs(0, 45)
                        .addBox(-2.5F, 0.0F, -2.5F, 5.0F, 11.0F, 5.0F, expand),
                PartPose.offset(5.0F, 13.0F, 10.0F)
        );

        return LayerDefinition.create(meshdefinition, 128, 64);
    }

}
