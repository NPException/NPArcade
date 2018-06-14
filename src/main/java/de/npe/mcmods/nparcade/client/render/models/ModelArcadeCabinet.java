package de.npe.mcmods.nparcade.client.render.models;

import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glRotatef;
import static org.lwjgl.opengl.GL11.glScalef;
import static org.lwjgl.opengl.GL11.glTranslatef;

import de.npe.mcmods.nparcade.client.arcade.ArcadeMachine;
import de.npe.mcmods.nparcade.client.render.Helper;
import de.npe.mcmods.nparcade.common.lib.Reference;
import de.npe.mcmods.nparcade.common.tileentities.TileArcadeCabinet;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.MinecraftForgeClient;

/**
 * Arcade - NPException
 * Created using Tabula 4.1.1
 */
@SideOnly(Side.CLIENT)
public final class ModelArcadeCabinet extends ModelBase {
	public static final ModelArcadeCabinet instance = new ModelArcadeCabinet();
	public static final ResourceLocation texture = new ResourceLocation(Reference.MOD_IDENTIFIER + "textures/models/nparcade_modelArcadeCabinet.png");

	private final ModelRenderer base;
	private final ModelRenderer stick;
	private final ModelRenderer buttonRed;
	private final ModelRenderer buttonGreen;
	private final ModelRenderer buttonBlue;
	private final ModelRenderer buttonYellow;

	private ModelArcadeCabinet() {
		textureWidth = 88;
		textureHeight = 57;

		ModelRenderer screen = new ModelRenderer(this, 58, 40);
		screen.setRotationPoint(0.0F, 8.0F, -2.0F);
		screen.addBox(-6.0F, -16.0F, 0.0F, 12, 15, 0, 0.0F);

		screen.rotateAngleX = (float) -0.5;
		screen.rotateAngleY = (float) 0.0;
		screen.rotateAngleZ = (float) 0.0;

		ModelRenderer backWall = new ModelRenderer(this, 0, 18);
		backWall.setRotationPoint(0.0F, 8.0F, 8.0F);
		backWall.addBox(-8.0F, -16.0F, -2.0F, 16, 15, 2, 0.0F);

		ModelRenderer lightBar = new ModelRenderer(this, 29, 53);
		lightBar.setRotationPoint(0.0F, -16.0F, -2.0F);
		lightBar.addBox(-6.0F, 0.0F, -2.0F, 12, 2, 2, 0.0F);

		ModelRenderer wallLeft1 = new ModelRenderer(this, 49, 0);
		wallLeft1.setRotationPoint(8.0F, 8.0F, 6.0F);
		wallLeft1.addBox(-2.0F, -4.0F, -10.0F, 2, 3, 10, 0.0F);
		ModelRenderer wallLeft2 = new ModelRenderer(this, 50, 18);
		wallLeft2.setRotationPoint(0.0F, -4.0F, 0.0F);
		wallLeft2.addBox(-2.0F, -4.0F, -8.0F, 2, 4, 8, 0.0F);
		ModelRenderer wallLeft3 = new ModelRenderer(this, 13, 36);
		wallLeft3.setRotationPoint(0.0F, -4.0F, 0.0F);
		wallLeft3.addBox(-2.0F, -4.0F, -6.0F, 2, 4, 6, 0.0F);
		ModelRenderer wallLeft4 = new ModelRenderer(this, 0, 37);
		wallLeft4.setRotationPoint(0.0F, -4.0F, 0.0F);
		wallLeft4.addBox(-2.0F, -4.0F, -4.0F, 2, 4, 4, 0.0F);

		ModelRenderer wallRight1 = new ModelRenderer(this, 64, 8);
		wallRight1.setRotationPoint(-6.0F, 8.0F, 6.0F);
		wallRight1.addBox(-2.0F, -4.0F, -10.0F, 2, 3, 10, 0.0F);
		ModelRenderer wallRight2 = new ModelRenderer(this, 37, 23);
		wallRight2.setRotationPoint(0.0F, -4.0F, 0.0F);
		wallRight2.addBox(-2.0F, -4.0F, -8.0F, 2, 4, 8, 0.0F);
		ModelRenderer wallRight3 = new ModelRenderer(this, 24, 41);
		wallRight3.setRotationPoint(0.0F, -4.0F, 0.0F);
		wallRight3.addBox(-2.0F, -4.0F, -6.0F, 2, 4, 6, 0.0F);
		ModelRenderer wallRight4 = new ModelRenderer(this, 0, 45);
		wallRight4.setRotationPoint(0.0F, -4.0F, 0.0F);
		wallRight4.addBox(-2.0F, -4.0F, -4.0F, 2, 4, 4, 0.0F);

		buttonRed = new ModelRenderer(this, 0, 2);
		buttonRed.setRotationPoint(-0.5F, 7.0F, -5.5F);
		buttonRed.addBox(-0.5F, -0.5F, -0.5F, 1, 1, 1, 0.0F);
		buttonGreen = new ModelRenderer(this, 0, 0);
		buttonGreen.setRotationPoint(0.5F, 7.0F, -4.5F);
		buttonGreen.addBox(-0.5F, -0.5F, -0.5F, 1, 1, 1, 0.0F);
		buttonBlue = new ModelRenderer(this, 4, 2);
		buttonBlue.setRotationPoint(2.5F, 7.0F, -5.5F);
		buttonBlue.addBox(-0.5F, -0.5F, -0.5F, 1, 1, 1, 0.0F);
		buttonYellow = new ModelRenderer(this, 4, 0);
		buttonYellow.setRotationPoint(3.5F, 7.0F, -4.5F);
		buttonYellow.addBox(-0.5F, -0.5F, -0.5F, 1, 1, 1, 0.0F);

		stick = new ModelRenderer(this, 0, 7);
		stick.setRotationPoint(-4.5F, 7.0F, -5.5F);
		stick.addBox(-0.5F, -1.5F, -0.5F, 1, 2, 1, 0.0F);

		base = new ModelRenderer(this, 0, 0);
		base.setRotationPoint(0.0F, 0.0F, 0.0F);
		base.addBox(-8.0F, 7.0F, -8.0F, 16, 1, 16, 0.0F);

		base.addChild(screen);
		base.addChild(backWall);
		backWall.addChild(lightBar);

		wallLeft3.addChild(wallLeft4);
		wallLeft2.addChild(wallLeft3);
		wallLeft1.addChild(wallLeft2);
		base.addChild(wallLeft1);

		wallRight3.addChild(wallRight4);
		wallRight2.addChild(wallRight3);
		wallRight1.addChild(wallRight2);
		base.addChild(wallRight1);

		base.addChild(buttonGreen);
		base.addChild(buttonRed);
		base.addChild(buttonYellow);
		base.addChild(buttonBlue);

		base.addChild(stick);
	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		base.render(f5);
	}

	public void renderCabinet(TileArcadeCabinet tile, float tick, float scale) {
		glColor3f(1.0F, 1.0F, 1.0F);

		ArcadeMachine am = tile.arcadeMachine();

		stick.offsetX = am.stickOffsetX * scale * 0.7F;
		stick.offsetZ = -am.stickOffsetY * scale * 0.7F;

		buttonRed.offsetY = am.buttonRedOffset * scale * 0.3F;
		buttonGreen.offsetY = am.buttonGreenOffset * scale * 0.3F;
		buttonBlue.offsetY = am.buttonBlueOffset * scale * 0.3F;
		buttonYellow.offsetY = am.buttonYellowOffset * scale * 0.3F;

		base.render(scale);
		renderScreen(tile, tick);

		// render cartridge if available
		ItemStack cartridgeItem = tile.generateCurrentGameCartridge();
		if (cartridgeItem != null) {
			IItemRenderer renderer = MinecraftForgeClient.getItemRenderer(cartridgeItem, IItemRenderer.ItemRenderType.ENTITY);
			if (renderer != null) {
				glPushMatrix();
				{
					glTranslatef(0.4375F, 0.39F, -0.37F);
					glRotatef(180F, 0F, 0F, 1F);
					glScalef(0.5F, 0.5F, 0.5F);
					renderer.renderItem(IItemRenderer.ItemRenderType.ENTITY, cartridgeItem);
				}
				glPopMatrix();
			}
		}
	}

	private static final float _16th = 1F / 16F;
	private static final float screenShiftX = -(_16th * 5F) + 0.2025F * _16th;
	private static final float screenShiftY = -(_16th * 6F) + 0.4025F * _16th;
	private static final float screenShiftZ = _16th * 2.36F;

	private static void renderScreen(TileArcadeCabinet tile, float tick) {
		ArcadeMachine arcade = tile.arcadeMachine();
		if (arcade == null) {
			return;
		}

		glPushMatrix();
		{
			// set max brightness
			Helper.pushMaxBrightness();

			glTranslatef(0F, 0F, screenShiftZ);
			glRotatef(-0.5F * (180F / (float) Math.PI), 1.0F, 0.0F, 0.0F);
			glTranslatef(screenShiftX, screenShiftY, 0F);
			glScalef(0.006F, 0.006F, 0.006F);

			arcade.doRenderScreen(tick);

			// reset brightness
			Helper.popMaxBrightness();
		}
		glPopMatrix();
	}
}
