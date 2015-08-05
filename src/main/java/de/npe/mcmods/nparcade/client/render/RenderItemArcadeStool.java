package de.npe.mcmods.nparcade.client.render;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import de.npe.mcmods.nparcade.client.render.models.ModelArcadeStool;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;

import static org.lwjgl.opengl.GL11.*;

/**
 * Created by NPException (2015)
 */
@SideOnly(Side.CLIENT)
public class RenderItemArcadeStool implements IItemRenderer {

	@Override
	public boolean handleRenderType(ItemStack item, ItemRenderType type) {
		return true;
	}

	@Override
	public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
		return true;
	}

	@Override
	public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
		glPushMatrix();

		if (type == ItemRenderType.INVENTORY) {
			glTranslatef(0.0F, 0.2F, 0.0F);
		}

		if (type == ItemRenderType.ENTITY) {
			glTranslatef(-0.5F, -0.0F, -0.5F);
		}

		glTranslatef(0.5f, 0.625f, 0.5f);
		glRotatef(180f, 1f, 0f, 0f);
		glScalef(1.625f, 1.625f, 1.625f);
		glColor3f(1.0F, 1.0F, 1.0F);
		bindTexture(ModelArcadeStool.texture);
		ModelArcadeStool.instance.render(null, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
		glPopMatrix();
	}

	public static void bindTexture(ResourceLocation texture) {
		Minecraft.getMinecraft().renderEngine.bindTexture(texture);
	}
}
