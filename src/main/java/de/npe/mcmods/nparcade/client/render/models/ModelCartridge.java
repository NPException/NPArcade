package de.npe.mcmods.nparcade.client.render.models;

import de.npe.mcmods.nparcade.common.lib.Reference;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

/**
 * Cartridge - NPException
 * Created using Tabula 4.1.1
 */
public final class ModelCartridge extends ModelBase {
	public static final ModelCartridge instance = new ModelCartridge();
	public static final ResourceLocation texture = new ResourceLocation(Reference.MOD_IDENTIFIER + "textures/models/nparcade_modelCartridge_new.png");

	private final ModelRenderer base;

	private ModelCartridge() {
		textureWidth = 23;
		textureHeight = 18;

		base = new ModelRenderer(this, 0, 0);
		base.setRotationPoint(0.0F, 8.0F, 0.0F);
		base.addBox(-2.5F, -8.0F, 0.0F, 5, 8, 1, 0.0F);

		ModelRenderer left = new ModelRenderer(this, 7, 10);
		left.setRotationPoint(0.0F, 0.0F, 0.0F);
		left.addBox(2.5F, -8.0F, -1.0F, 1, 6, 2, 0.0F);

		ModelRenderer right = new ModelRenderer(this, 0, 10);
		right.setRotationPoint(0.0F, 0.0F, 0.0F);
		right.addBox(-3.5F, -8.0F, -1.0F, 1, 6, 2, 0.0F);

		ModelRenderer coverTop = new ModelRenderer(this, 13, 0);
		coverTop.setRotationPoint(0.0F, 0.0F, 0.0F);
		coverTop.addBox(-1.5F, -7.9999F, -0.75F, 4, 5, 1, 0.0F);

		ModelRenderer coverBottom = new ModelRenderer(this, 13, 5);
		coverBottom.setRotationPoint(0.0F, 0.0F, 0.0F);
		coverBottom.addBox(-1.5F, -3.0F, -1.0F, 4, 3, 1, 0.0F);

		ModelRenderer stripe = new ModelRenderer(this, 14, 10);
		stripe.setRotationPoint(0.0F, 0.0F, 0.0F);
		stripe.addBox(-2.5F, -7.0F, -1.0F, 1, 7, 1, 0.0F);

		base.addChild(left);
		base.addChild(right);
		base.addChild(coverTop);
		base.addChild(stripe);
		base.addChild(coverBottom);
	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		base.render(f5);
	}
}
