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
public class ModelCartridge extends ModelBase {
	public static final ModelCartridge instance = new ModelCartridge();
	public static final ResourceLocation texture = new ResourceLocation(Reference.MOD_IDENTIFIER + "textures/models/nparcade_modelCartridge_new.png");

	public ModelRenderer base;
	public ModelRenderer left;
	public ModelRenderer right;
	public ModelRenderer coverTop;
	public ModelRenderer coverBottom;
	public ModelRenderer stripe;

	public ModelCartridge() {
		this.textureWidth = 23;
		this.textureHeight = 18;
		this.base = new ModelRenderer(this, 0, 0);
		this.base.setRotationPoint(0.0F, 8.0F, 0.0F);
		this.base.addBox(-2.5F, -8.0F, 0.0F, 5, 8, 1, 0.0F);
		this.left = new ModelRenderer(this, 7, 10);
		this.left.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.left.addBox(2.5F, -8.0F, -1.0F, 1, 6, 2, 0.0F);
		this.right = new ModelRenderer(this, 0, 10);
		this.right.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.right.addBox(-3.5F, -8.0F, -1.0F, 1, 6, 2, 0.0F);
		this.coverTop = new ModelRenderer(this, 13, 0);
		this.coverTop.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.coverTop.addBox(-1.5F, -7.9999F, -0.75F, 4, 5, 1, 0.0F);
		this.coverBottom = new ModelRenderer(this, 13, 5);
		this.coverBottom.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.coverBottom.addBox(-1.5F, -3.0F, -1.0F, 4, 3, 1, 0.0F);
		this.stripe = new ModelRenderer(this, 14, 10);
		this.stripe.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.stripe.addBox(-2.5F, -7.0F, -1.0F, 1, 7, 1, 0.0F);

		this.base.addChild(this.left);
		this.base.addChild(this.right);
		this.base.addChild(this.coverTop);
		this.base.addChild(this.stripe);
		this.base.addChild(this.coverBottom);
	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		this.base.render(f5);
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
