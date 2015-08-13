package de.npe.mcmods.nparcade.client.render.models;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import de.npe.mcmods.nparcade.common.lib.Reference;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

/**
 * Created by NPException (2015)
 * Created using Tabula 4.1.1
 */
@SideOnly(Side.CLIENT)
public class ModelArcadeStool extends ModelBase {
	public static final ModelArcadeStool instance = new ModelArcadeStool();
	public static final ResourceLocation texture = new ResourceLocation(Reference.MOD_IDENTIFIER + "textures/blocks/nparcade_modelStool.png");

	public ModelRenderer seat;
	public ModelRenderer seatBottom;
	public ModelRenderer seatEdge1;
	public ModelRenderer seatEdge2;
	public ModelRenderer seatEdge3;
	public ModelRenderer seatEdge4;
	public ModelRenderer legRed;
	public ModelRenderer legBlue;
	public ModelRenderer legGreen;
	public ModelRenderer legYellow;

	public ModelArcadeStool() {
		this.textureWidth = 32;
		this.textureHeight = 38;
		this.seatEdge2 = new ModelRenderer(this, 0, 30);
		this.seatEdge2.setRotationPoint(0.0F, 0.0F, 4.0F);
		this.seatEdge2.addBox(-3.0F, 0.0F, 0.0F, 6, 1, 1, 0.0F);
		this.legBlue = new ModelRenderer(this, 24, 11);
		this.legBlue.setRotationPoint(2.0F, 2.0F, 2.0F);
		this.legBlue.addBox(-1.0F, 0.0F, -1.0F, 2, 8, 2, 0.0F);
		this.legGreen = new ModelRenderer(this, 15, 22);
		this.legGreen.setRotationPoint(-2.0F, 2.0F, -2.0F);
		this.legGreen.addBox(-1.0F, 0.0F, -1.0F, 2, 8, 2, 0.0F);
		this.seatBottom = new ModelRenderer(this, 0, 33);
		this.seatBottom.setRotationPoint(0.0F, 2.0F, 0.0F);
		this.seatBottom.addBox(-2.0F, 0.0F, -2.0F, 4, 1, 4, 0.0F);
		this.legRed = new ModelRenderer(this, 15, 11);
		this.legRed.setRotationPoint(-2.0F, 2.0F, 2.0F);
		this.legRed.addBox(-1.0F, 0.0F, -1.0F, 2, 8, 2, 0.0F);
		this.seatEdge1 = new ModelRenderer(this, 0, 19);
		this.seatEdge1.setRotationPoint(0.0F, 0.0F, -4.0F);
		this.seatEdge1.addBox(-3.0F, 0.0F, -1.0F, 6, 1, 1, 0.0F);
		this.seatEdge4 = new ModelRenderer(this, 0, 22);
		this.seatEdge4.setRotationPoint(-4.0F, 0.0F, 0.0F);
		this.seatEdge4.addBox(-1.0F, 0.0F, -3.0F, 1, 1, 6, 0.0F);
		this.legYellow = new ModelRenderer(this, 24, 22);
		this.legYellow.setRotationPoint(2.0F, 2.0F, -2.0F);
		this.legYellow.addBox(-1.0F, 0.0F, -1.0F, 2, 8, 2, 0.0F);
		this.seat = new ModelRenderer(this, 0, 0);
		this.seat.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.seat.addBox(-4.0F, 0.0F, -4.0F, 8, 2, 8, 0.0F);
		this.seatEdge3 = new ModelRenderer(this, 0, 11);
		this.seatEdge3.setRotationPoint(4.0F, 0.0F, 0.0F);
		this.seatEdge3.addBox(0.0F, 0.0F, -3.0F, 1, 1, 6, 0.0F);
		this.seat.addChild(this.seatEdge2);
		this.seat.addChild(this.legBlue);
		this.seat.addChild(this.legGreen);
		this.seat.addChild(this.seatBottom);
		this.seat.addChild(this.legRed);
		this.seat.addChild(this.seatEdge1);
		this.seat.addChild(this.seatEdge4);
		this.seat.addChild(this.legYellow);
		this.seat.addChild(this.seatEdge3);
	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		this.seat.render(f5);
	}

	/**
	 * This is a helper function from Tabula to set the rotation of model parts
	 */
	public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}
}
