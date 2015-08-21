package de.npe.mcmods.nparcade.client.render.models;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import de.npe.mcmods.nparcade.common.lib.Reference;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

/**
 * Arcade - NPException
 * Created using Tabula 4.1.1
 */
@SideOnly(Side.CLIENT)
public class ModelArcadeCabinet extends ModelBase {
	public static final ModelArcadeCabinet instance = new ModelArcadeCabinet();
	public static final ResourceLocation texture = new ResourceLocation(Reference.MOD_IDENTIFIER + "textures/models/nparcade_modelArcadeCabinet.png");

	public ModelRenderer base;
	public ModelRenderer stick;
	public ModelRenderer buttonRed;
	public ModelRenderer buttonGreen;
	public ModelRenderer buttonBlue;
	public ModelRenderer buttonYellow;
	public ModelRenderer wallLeft1;
	public ModelRenderer wallRight1;
	public ModelRenderer backWall;
	public ModelRenderer screen;
	public ModelRenderer wallLeft2;
	public ModelRenderer wallLeft3;
	public ModelRenderer wallLeft4;
	public ModelRenderer wallRight2;
	public ModelRenderer wallRight3;
	public ModelRenderer wallRight4;
	public ModelRenderer lightBar;

	public ModelArcadeCabinet() {
		this.textureWidth = 88;
		this.textureHeight = 57;

		this.screen = new ModelRenderer(this, 58, 40);
		this.screen.setRotationPoint(0.0F, 8.0F, -2.0F);
		this.screen.addBox(-6.0F, -16.0F, 0.0F, 12, 15, 0, 0.0F);
		this.setRotateAngle(screen, -0.5F, 0.0F, 0.0F);

		this.backWall = new ModelRenderer(this, 0, 18);
		this.backWall.setRotationPoint(0.0F, 8.0F, 8.0F);
		this.backWall.addBox(-8.0F, -16.0F, -2.0F, 16, 15, 2, 0.0F);

		this.lightBar = new ModelRenderer(this, 29, 53);
		this.lightBar.setRotationPoint(0.0F, -16.0F, -2.0F);
		this.lightBar.addBox(-6.0F, 0.0F, -2.0F, 12, 2, 2, 0.0F);

		this.wallLeft1 = new ModelRenderer(this, 49, 0);
		this.wallLeft1.setRotationPoint(8.0F, 8.0F, 6.0F);
		this.wallLeft1.addBox(-2.0F, -4.0F, -10.0F, 2, 3, 10, 0.0F);
		this.wallLeft2 = new ModelRenderer(this, 50, 18);
		this.wallLeft2.setRotationPoint(0.0F, -4.0F, 0.0F);
		this.wallLeft2.addBox(-2.0F, -4.0F, -8.0F, 2, 4, 8, 0.0F);
		this.wallLeft3 = new ModelRenderer(this, 13, 36);
		this.wallLeft3.setRotationPoint(0.0F, -4.0F, 0.0F);
		this.wallLeft3.addBox(-2.0F, -4.0F, -6.0F, 2, 4, 6, 0.0F);
		this.wallLeft4 = new ModelRenderer(this, 0, 37);
		this.wallLeft4.setRotationPoint(0.0F, -4.0F, 0.0F);
		this.wallLeft4.addBox(-2.0F, -4.0F, -4.0F, 2, 4, 4, 0.0F);

		this.wallRight1 = new ModelRenderer(this, 64, 8);
		this.wallRight1.setRotationPoint(-6.0F, 8.0F, 6.0F);
		this.wallRight1.addBox(-2.0F, -4.0F, -10.0F, 2, 3, 10, 0.0F);
		this.wallRight2 = new ModelRenderer(this, 37, 23);
		this.wallRight2.setRotationPoint(0.0F, -4.0F, 0.0F);
		this.wallRight2.addBox(-2.0F, -4.0F, -8.0F, 2, 4, 8, 0.0F);
		this.wallRight3 = new ModelRenderer(this, 24, 41);
		this.wallRight3.setRotationPoint(0.0F, -4.0F, 0.0F);
		this.wallRight3.addBox(-2.0F, -4.0F, -6.0F, 2, 4, 6, 0.0F);
		this.wallRight4 = new ModelRenderer(this, 0, 45);
		this.wallRight4.setRotationPoint(0.0F, -4.0F, 0.0F);
		this.wallRight4.addBox(-2.0F, -4.0F, -4.0F, 2, 4, 4, 0.0F);

		this.buttonGreen = new ModelRenderer(this, 0, 0);
		this.buttonGreen.setRotationPoint(0.5F, 7.0F, -4.5F);
		this.buttonGreen.addBox(-0.5F, -0.5F, -0.5F, 1, 1, 1, 0.0F);
		this.buttonRed = new ModelRenderer(this, 0, 2);
		this.buttonRed.setRotationPoint(-0.5F, 7.0F, -5.5F);
		this.buttonRed.addBox(-0.5F, -0.5F, -0.5F, 1, 1, 1, 0.0F);
		this.buttonYellow = new ModelRenderer(this, 4, 0);
		this.buttonYellow.setRotationPoint(3.5F, 7.0F, -4.5F);
		this.buttonYellow.addBox(-0.5F, -0.5F, -0.5F, 1, 1, 1, 0.0F);
		this.buttonBlue = new ModelRenderer(this, 4, 2);
		this.buttonBlue.setRotationPoint(2.5F, 7.0F, -5.5F);
		this.buttonBlue.addBox(-0.5F, -0.5F, -0.5F, 1, 1, 1, 0.0F);

		this.stick = new ModelRenderer(this, 0, 7);
		this.stick.setRotationPoint(-4.5F, 7.0F, -5.5F);
		this.stick.addBox(-0.5F, -1.5F, -0.5F, 1, 2, 1, 0.0F);

		this.base = new ModelRenderer(this, 0, 0);
		this.base.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.base.addBox(-8.0F, 7.0F, -8.0F, 16, 1, 16, 0.0F);

		this.base.addChild(this.screen);
		this.base.addChild(this.backWall);
		this.backWall.addChild(this.lightBar);

		this.wallLeft3.addChild(this.wallLeft4);
		this.wallLeft2.addChild(this.wallLeft3);
		this.wallLeft1.addChild(this.wallLeft2);
		this.base.addChild(this.wallLeft1);

		this.wallRight3.addChild(this.wallRight4);
		this.wallRight2.addChild(this.wallRight3);
		this.wallRight1.addChild(this.wallRight2);
		this.base.addChild(this.wallRight1);

		this.base.addChild(this.buttonGreen);
		this.base.addChild(this.buttonRed);
		this.base.addChild(this.buttonYellow);
		this.base.addChild(this.buttonBlue);

		this.base.addChild(this.stick);
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
