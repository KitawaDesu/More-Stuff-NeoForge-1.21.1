package net.kitawa.more_stuff.entities.monster.renderers.models;

import com.google.common.collect.ImmutableList;
import net.kitawa.more_stuff.entities.monster.ZombieWolf;
import net.minecraft.client.model.ColorableAgeableListModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.animal.Wolf;

public class ZombieWolfModel<T extends ZombieWolf> extends ColorableAgeableListModel<T> {
    private static final String REAL_HEAD = "real_head";
    private static final String UPPER_BODY = "upper_body";
    private static final String REAL_TAIL = "real_tail";

    private final ModelPart head;
    private final ModelPart realHead;
    private final ModelPart body;
    private final ModelPart upperBody;
    private final ModelPart rightHindLeg;
    private final ModelPart leftHindLeg;
    private final ModelPart rightFrontLeg;
    private final ModelPart leftFrontLeg;
    private final ModelPart tail;
    private final ModelPart realTail;

    private static final int LEG_SIZE = 8;

    public ZombieWolfModel(ModelPart root) {
        this.head = root.getChild("head");
        this.realHead = this.head.getChild(REAL_HEAD);
        this.body = root.getChild("body");
        this.upperBody = root.getChild(UPPER_BODY);
        this.rightHindLeg = root.getChild("right_hind_leg");
        this.leftHindLeg = root.getChild("left_hind_leg");
        this.rightFrontLeg = root.getChild("right_front_leg");
        this.leftFrontLeg = root.getChild("left_front_leg");
        this.tail = root.getChild("tail");
        this.realTail = this.tail.getChild(REAL_TAIL);
    }

    @Override
    protected Iterable<ModelPart> headParts() {
        return ImmutableList.of(this.head);
    }

    @Override
    protected Iterable<ModelPart> bodyParts() {
        return ImmutableList.of(this.body, this.upperBody, this.rightHindLeg, this.leftHindLeg,
                this.rightFrontLeg, this.leftFrontLeg, this.tail);
    }

    @Override
    public void prepareMobModel(T entity, float limbSwing, float limbSwingAmount, float partialTick) {
        // Tail wagging
        if (entity.isAngry()) {
            this.tail.yRot = 0.0F;
        } else {
            this.tail.yRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
        }
            this.body.setPos(0.0F, 14.0F, 2.0F);
            this.body.xRot = (float) (Math.PI / 2);
            this.upperBody.setPos(-1.0F, 14.0F, -3.0F);
            this.upperBody.xRot = this.body.xRot;
            this.tail.setPos(-1.0F, 12.0F, 8.0F);

            this.rightHindLeg.setPos(-2.5F, 16.0F, 7.0F);
            this.leftHindLeg.setPos(0.5F, 16.0F, 7.0F);
            this.rightFrontLeg.setPos(-2.5F, 16.0F, -4.0F);
            this.leftFrontLeg.setPos(0.5F, 16.0F, -4.0F);

            this.rightHindLeg.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
            this.leftHindLeg.xRot = Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount;
            this.rightFrontLeg.xRot = Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount;
            this.leftFrontLeg.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.head.xRot = (float) (headPitch * ((float) Math.PI / 180.0));
        this.head.yRot = (float) (netHeadYaw * ((float) Math.PI / 180.0));
        // No ageInTicks tail rotation — tail only moves via prepareMobModel
    }
}


