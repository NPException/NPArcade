package de.npe.mcmods.nparcade.client.render;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
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
			glRotatef(71F, 0F,1F,0F);
			glTranslatef(-0.1F, -0.8F, -0.7F);
		}
		glColor3f(1.0F, 1.0F, 1.0F);
		bindTexture(ModelCartridge.texture);
		ModelCartridge.instance.render(null, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);

		renderSticker(stack);

		glPopMatrix();
	}

	private void renderSticker(ItemStack stack) {
		NBTTagCompound tag = stack.getTagCompound();
		if (tag == null || !tag.hasKey(Strings.NBT_GAME)) {
			return;
		}
		ArcadeGameWrapper wrapper = ArcadeGameRegistry.gameForID(tag.getString(Strings.NBT_GAME));
		if (wrapper == null) {
			// TODO: bindTexture(ModelCartridge.unknownGameStickerTexture);
		} else {
			// TODO: translate and scale, bind game texture, Helper.renderRectInBounds(...)
		}
	}
}
