package de.npe.mcmods.nparcade.client.render.models;

import de.npe.mcmods.nparcade.common.lib.Reference;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
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
	public static final ResourceLocation texture = new ResourceLocation(Reference.MOD_IDENTIFIER + "textures/models/nparcade_modelStool.png");

	private final ModelRenderer seat;

	private ModelArcadeStool() {
		textureWidth = 32;
		textureHeight = 38;

		ModelRenderer seatEdge2 = new ModelRenderer(this, 0, 30);
		seatEdge2.setRotationPoint(0.0F, 0.0F, 4.0F);
		seatEdge2.addBox(-3.0F, 0.0F, 0.0F, 6, 1, 1, 0.0F);

		ModelRenderer legBlue = new ModelRenderer(this, 24, 11);
		legBlue.setRotationPoint(2.0F, 2.0F, 2.0F);
		legBlue.addBox(-1.0F, 0.0F, -1.0F, 2, 8, 2, 0.0F);

		ModelRenderer legGreen = new ModelRenderer(this, 15, 22);
		legGreen.setRotationPoint(-2.0F, 2.0F, -2.0F);
		legGreen.addBox(-1.0F, 0.0F, -1.0F, 2, 8, 2, 0.0F);

		ModelRenderer seatBottom = new ModelRenderer(this, 0, 33);
		seatBottom.setRotationPoint(0.0F, 2.0F, 0.0F);
		seatBottom.addBox(-2.0F, 0.0F, -2.0F, 4, 1, 4, 0.0F);

		ModelRenderer legRed = new ModelRenderer(this, 15, 11);
		legRed.setRotationPoint(-2.0F, 2.0F, 2.0F);
		legRed.addBox(-1.0F, 0.0F, -1.0F, 2, 8, 2, 0.0F);

		ModelRenderer seatEdge1 = new ModelRenderer(this, 0, 19);
		seatEdge1.setRotationPoint(0.0F, 0.0F, -4.0F);
		seatEdge1.addBox(-3.0F, 0.0F, -1.0F, 6, 1, 1, 0.0F);

		ModelRenderer seatEdge4 = new ModelRenderer(this, 0, 22);
		seatEdge4.setRotationPoint(-4.0F, 0.0F, 0.0F);
		seatEdge4.addBox(-1.0F, 0.0F, -3.0F, 1, 1, 6, 0.0F);

		ModelRenderer legYellow = new ModelRenderer(this, 24, 22);
		legYellow.setRotationPoint(2.0F, 2.0F, -2.0F);
		legYellow.addBox(-1.0F, 0.0F, -1.0F, 2, 8, 2, 0.0F);
		seat = new ModelRenderer(this, 0, 0);
		seat.setRotationPoint(0.0F, 0.0F, 0.0F);
		seat.addBox(-4.0F, 0.0F, -4.0F, 8, 2, 8, 0.0F);

		ModelRenderer seatEdge3 = new ModelRenderer(this, 0, 11);
		seatEdge3.setRotationPoint(4.0F, 0.0F, 0.0F);
		seatEdge3.addBox(0.0F, 0.0F, -3.0F, 1, 1, 6, 0.0F);

		seat.addChild(seatEdge2);
		seat.addChild(legBlue);
		seat.addChild(legGreen);
		seat.addChild(seatBottom);
		seat.addChild(legRed);
		seat.addChild(seatEdge1);
		seat.addChild(seatEdge4);
		seat.addChild(legYellow);
		seat.addChild(seatEdge3);
	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		seat.render(f5);
	}
}
