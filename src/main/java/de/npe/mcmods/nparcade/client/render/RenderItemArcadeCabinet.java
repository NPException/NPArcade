package de.npe.mcmods.nparcade.client.render;

import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glRotatef;
import static org.lwjgl.opengl.GL11.glTranslatef;

import de.npe.mcmods.nparcade.client.render.models.ModelArcadeCabinet;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.item.ItemStack;

/**
 * Created by NPException (2015)
 */
@SideOnly(Side.CLIENT)
public class RenderItemArcadeCabinet extends AbstractItemRenderer {
	@Override
	public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
		glPushMatrix();
		{
			if (type == ItemRenderType.INVENTORY) {
				glTranslatef(0.0F, -0.2165F, 0.0F);
			}

			if (type == ItemRenderType.ENTITY) {
				glRotatef(180F, 0F, 1F, 0F);
				glTranslatef(-0.5F, -0.625F, -0.5F);
			}

			glTranslatef(0.5f, 0.625f, 0.5f);

			if (type == ItemRenderType.INVENTORY) {
				glRotatef(90F, 0F, 1F, 0F);
			} else if (type == ItemRenderType.EQUIPPED_FIRST_PERSON) {
				glTranslatef(0F, 0.5F, 0F);
				glRotatef(270F, 0F, 1F, 0F);
			}

			glRotatef(180f, 1f, 0f, 0f);
			glColor3f(1.0F, 1.0F, 1.0F);
			bindTexture(ModelArcadeCabinet.texture);
			ModelArcadeCabinet.instance.render(null, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
		}
		glPopMatrix();
	}
}
