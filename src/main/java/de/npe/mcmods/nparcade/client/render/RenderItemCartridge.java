package de.npe.mcmods.nparcade.client.render;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import de.npe.api.nparcade.util.Size;
import de.npe.mcmods.nparcade.arcade.ArcadeGameRegistry;
import de.npe.mcmods.nparcade.arcade.ArcadeGameWrapper;
import de.npe.mcmods.nparcade.client.render.models.ModelCartridge;
import de.npe.mcmods.nparcade.common.lib.Strings;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import static org.lwjgl.opengl.GL11.*;

/**
 * Created by NPException (2015)
 */
@SideOnly(Side.CLIENT)
public class RenderItemCartridge extends AbstractItemRenderer {

	@Override
	public void renderItem(ItemRenderType type, ItemStack stack, Object... data) {
		glPushMatrix();

		if (type == ItemRenderType.INVENTORY) {
			glScalef(2.375f, 2.375f, 2.375f);
		}

		if (type == ItemRenderType.ENTITY) {
			glRotatef(90F, 0F, 1F, 0F);
			glTranslatef(-0.5F, -0.34F, -0.5F);
		}

		glTranslatef(0.5f, 0.625f, 0.5f);
		glRotatef(180f, 1f, 0f, 0f);
		if (type == ItemRenderType.EQUIPPED_FIRST_PERSON) {
			glRotatef(71F, 0F, 1F, 0F);
			glTranslatef(-0.1F, -0.8F, -0.7F);
		}

		NBTTagCompound tag = stack.getTagCompound();
		ArcadeGameWrapper wrapper = (tag == null || !tag.hasKey(Strings.NBT_GAME)) ? null : ArcadeGameRegistry.gameForID(tag.getString(Strings.NBT_GAME));

		if (wrapper != null && wrapper.hasColor()) {
			glColor3f(wrapper.colorRed(), wrapper.colorGreen(), wrapper.colorBlue());
		} else {
			glColor3f(0.51F, 0.51F, 0.47F);
		}

		glPushMatrix();
		bindTexture(ModelCartridge.texture);
		ModelCartridge.instance.render(null, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
		glPopMatrix();

		if (wrapper != null && wrapper.hasLabel()) {
			glPushMatrix();
			renderSticker(wrapper);
			glPopMatrix();
		}

		glPopMatrix();
	}

	private void renderSticker(ArcadeGameWrapper wrapper) {
		glScalef(0.0625F, 0.0625F, 0.0625F);
		glTranslatef(-1.3F, 0.2F, -0.76F);
		glScalef(0.9F, 0.9F, 0.9F);

		glEnable (GL_BLEND);
		glBlendFunc (GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		glColor3f(1.0F, 1.0F, 1.0F);
		Size size = wrapper.labelSize();
		glBindTexture(GL_TEXTURE_2D, wrapper.prepareLabelTexture());
		Helper.renderRectInBounds(4, 5, size.width, size.height, 0, 0, 1, 1, Helper.Alignment.U);
	}
}
