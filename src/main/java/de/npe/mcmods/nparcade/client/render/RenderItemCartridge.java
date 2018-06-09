package de.npe.mcmods.nparcade.client.render;

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glRotatef;
import static org.lwjgl.opengl.GL11.glScalef;
import static org.lwjgl.opengl.GL11.glTranslatef;

import de.npe.api.nparcade.util.Size;
import de.npe.mcmods.nparcade.arcade.ArcadeGameRegistry;
import de.npe.mcmods.nparcade.arcade.ArcadeGameWrapper;
import de.npe.mcmods.nparcade.arcade.DummyGames;
import de.npe.mcmods.nparcade.client.render.models.ModelCartridge;
import de.npe.mcmods.nparcade.common.lib.Strings;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

/**
 * Created by NPException (2015)
 */
@SideOnly(Side.CLIENT)
public class RenderItemCartridge extends AbstractItemRenderer {

	private static final Size identiconSize = new Size(30, 30);

	@Override
	public void renderItem(ItemRenderType type, ItemStack stack, Object... data) {
		glPushMatrix();
		{
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
			String gameID = (tag == null || !tag.hasKey(Strings.NBT_GAME)) ? null : tag.getString(Strings.NBT_GAME);
			ArcadeGameWrapper wrapper = gameID == null ? null : ArcadeGameRegistry.gameForID(gameID);

			if (wrapper != null && wrapper.hasColor()) {
				glColor3f(wrapper.colorRed(), wrapper.colorGreen(), wrapper.colorBlue());
			} else {
				glColor3f(0.51F, 0.51F, 0.47F);
			}

			glPushMatrix();
			{
				bindTexture(ModelCartridge.texture);
				ModelCartridge.instance.render(null, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
			}
			glPopMatrix();

			if (wrapper != null) {
				renderSticker(wrapper, gameID);
			}
		}
		glPopMatrix();
	}

	/*
	 * I need to pass in the gameID, because in case of the UNKNOWN_GAME_WRAPPER
	 * I want the itemstack's gameID and not the one of the wrapper.
	 */
	private void renderSticker(ArcadeGameWrapper wrapper, String gameID) {
		glPushMatrix();
		{
			glScalef(0.0625F, 0.0625F, 0.0625F);
			// -1.5F & 0.0F -> both + 0.2 to offset from border of the label recess
			glTranslatef(-1.3F, 0.2F, -0.76F);

			glEnable(GL_BLEND);
			glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
			glColor3f(1.0F, 1.0F, 1.0F);

			Size size;
			int textureID;

			if (wrapper != DummyGames.UNKNOWN_GAME_WRAPPER && wrapper.hasLabel()) {
				size = wrapper.labelSize();
				textureID = wrapper.prepareLabelTexture();
			} else {
				size = identiconSize;
				textureID = IdentIconUtil.prepareIdentIconTexture(gameID, size.width);
			}

			glBindTexture(GL_TEXTURE_2D, textureID);
			// 4F & 5F -> both - 0.4 to scale down and offset from border of the label recess
			Helper.renderRectInBounds(3.6F, 4.6F, size.width, size.height, 0, 0, 1, 1, Helper.Alignment.U);
			glBindTexture(GL_TEXTURE_2D, 0);
		}
		glPopMatrix();
	}
}
