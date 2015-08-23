package the_fireplace.ias.tools;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

import javax.imageio.ImageIO;

import org.lwjgl.opengl.GL11;

import com.github.mrebhan.ingameaccountswitcher.tools.alt.AccountData;
import com.github.mrebhan.ingameaccountswitcher.tools.alt.AltDatabase;

import cpw.mods.fml.relauncher.FMLInjectionData;
import cpw.mods.fml.relauncher.SideOnly;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.entity.player.EntityPlayer;
/**
 * Tools that have to do with Skins
 * @author The_Fireplace
 *
 */
@SideOnly(Side.CLIENT)
public class SkinTools {
	public static void drawSkinFront(String name, int x, int y, int width, int height){//TODO: Fix
		/*try {
			AbstractClientPlayer.getDownloadImageSkin(AbstractClientPlayer.getLocationSkin(name), name).loadTexture(Minecraft.getMinecraft().getResourceManager());
			Minecraft.getMinecraft().getTextureManager().bindTexture(AbstractClientPlayer.getLocationSkin(name));
			Tessellator tess = Tessellator.instance;
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			// Face
			x = x + width / 4;
			double w = width / 2;
			double h = height / 4;
			double fw = 32;
			double fh = 32;
			double u = 32;
			double v = 32;
			tess.startDrawingQuads();
			tess.addVertexWithUV(x, y + h, 0, (float)u * 0.00390625F, (float)(v + fh) * 0.00390625F);
			tess.addVertexWithUV(x + w, y + h, 0, (float)(u + fw) * 0.00390625F, (float)(v + fh) * 0.00390625F);
			tess.addVertexWithUV(x + w, y, 0, (float)(u + fw) * 0.00390625F, (float)v * 0.00390625F);
			tess.addVertexWithUV(x, y, 0, (float)u * 0.00390625F, (float)v * 0.00390625F);
			tess.draw();
			// Hat
			u = 160;
			tess.startDrawingQuads();
			tess.addVertexWithUV(x, y + h, 0, (float)u * 0.00390625F, (float)(v + fh) * 0.00390625F);
			tess.addVertexWithUV(x + w, y + h, 0, (float)(u + fw) * 0.00390625F, (float)(v + fh) * 0.00390625F);
			tess.addVertexWithUV(x + w, y, 0, (float)(u + fw) * 0.00390625F, (float)v * 0.00390625F);
			tess.addVertexWithUV(x, y, 0, (float)u * 0.00390625F, (float)v * 0.00390625F);
			tess.draw();
			// Chest
			y = y + height / 4;
			h = height / 8 * 3;
			fh = 48;
			u = 80;
			v = 80;
			tess.startDrawingQuads();
			tess.addVertexWithUV(x, y + h, 0, (float)u * 0.00390625F, (float)(v + fh) * 0.00390625F);
			tess.addVertexWithUV(x + w, y + h, 0, (float)(u + fw) * 0.00390625F, (float)(v + fh) * 0.00390625F);
			tess.addVertexWithUV(x + w, y, 0, (float)(u + fw) * 0.00390625F, (float)v * 0.00390625F);
			tess.addVertexWithUV(x, y, 0, (float)u * 0.00390625F, (float)v * 0.00390625F);
			tess.draw();
			// Jacket
			v = 144;
			tess.startDrawingQuads();
			tess.addVertexWithUV(x, y + h, 0, (float)u * 0.00390625F, (float)(v + fh) * 0.00390625F);
			tess.addVertexWithUV(x + w, y + h, 0, (float)(u + fw) * 0.00390625F, (float)(v + fh) * 0.00390625F);
			tess.addVertexWithUV(x + w, (double)y + 0, 0, (float)(u + fw) * 0.00390625F, (float)v * 0.00390625F);
			tess.addVertexWithUV(x, y, 0, (float)u * 0.00390625F, (float)v * 0.00390625F);
			tess.draw();
			// Left Arm
			x = x - width / 16 * 4;
			w = width / 16 * 4;
			h = height / 8 * 3;
			fw = 16;
			fh = 48;
			u = 176;
			v = 80;
			tess.startDrawingQuads();
			tess.addVertexWithUV(x, y + h - 4, 0, (float)u * 0.00390625F, (float)(v + fh) * 0.00390625F);
			tess.addVertexWithUV(x + w, y + h - 4, 0, (float)(u + fw) * 0.00390625F, (float)(v + fh) * 0.00390625F);
			tess.addVertexWithUV(x + w, (double)y - 4, 0, (float)(u + fw) * 0.00390625F, (float)v * 0.00390625F);
			tess.addVertexWithUV(x, (double)y - 4, 0, (float)u * 0.00390625F, (float)v * 0.00390625F);
			tess.draw();
			// Left Sleeve
			v = 144;
			tess.startDrawingQuads();
			tess.addVertexWithUV(x, y + h - 4, 0, (float)u * 0.00390625F, (float)(v + fh) * 0.00390625F);
			tess.addVertexWithUV(x + w, y + h - 4, 0, (float)(u + fw) * 0.00390625F, (float)(v + fh) * 0.00390625F);
			tess.addVertexWithUV(x + w, (double)y - 4, 0, (float)(u + fw) * 0.00390625F, (float)v * 0.00390625F);
			tess.addVertexWithUV(x, (double)y - 4, 0, (float)u * 0.00390625F, (float)v * 0.00390625F);
			tess.draw();
			// Right Arm
			x = x + width / 16 * 12;
			v = 80;
			tess.startDrawingQuads();
			tess.addVertexWithUV(x, y + h - 4, 0, (float)(u + fw) * 0.00390625F, (float)(v + fh) * 0.00390625F);
			tess.addVertexWithUV(x + w, y + h - 4, 0, (float)u * 0.00390625F, (float)(v + fh) * 0.00390625F);
			tess.addVertexWithUV(x + w, (double)y - 4, 0, (float)u * 0.00390625F, (float)v * 0.00390625F);
			tess.addVertexWithUV(x, (double)y - 4, 0, (float)(u + fw) * 0.00390625F, (float)v * 0.00390625F);
			tess.draw();
			// Right Sleeve
			v = 144;
			tess.startDrawingQuads();
			tess.addVertexWithUV(x, y + h - 4, 0, (float)(u + fw) * 0.00390625F, (float)(v + fh) * 0.00390625F);
			tess.addVertexWithUV(x + w, y + h - 4, 0, (float)u * 0.00390625F, (float)(v + fh) * 0.00390625F);
			tess.addVertexWithUV(x + w, (double)y - 4, 0, (float)u * 0.00390625F, (float)v * 0.00390625F);
			tess.addVertexWithUV(x, (double)y - 4, 0, (float)(u + fw) * 0.00390625F, (float)v * 0.00390625F);
			tess.draw();
			// Left Leg
			x = x - width / 2;
			y = y + height / 32 * 12;
			w = width / 4;
			fw = 16;
			u = 16;
			v = 80;
			tess.startDrawingQuads();
			tess.addVertexWithUV(x, y + h, 0, (float)u * 0.00390625F, (float)(v + fh) * 0.00390625F);
			tess.addVertexWithUV(x + w, y + h, 0, (float)(u + fw) * 0.00390625F, (float)(v + fh) * 0.00390625F);
			tess.addVertexWithUV(x + w, y, 0, (float)(u + fw) * 0.00390625F, (float)v * 0.00390625F);
			tess.addVertexWithUV(x, y, 0, (float)u * 0.00390625F, (float)v * 0.00390625F);
			tess.draw();
			// Left Pants
			v = 144;
			tess.startDrawingQuads();
			tess.addVertexWithUV(x, y + h, 0, (float)u * 0.00390625F, (float)(v + fh) * 0.00390625F);
			tess.addVertexWithUV(x + w, y + h, 0, (float)(u + fw) * 0.00390625F, (float)(v + fh) * 0.00390625F);
			tess.addVertexWithUV(x + w, y, 0, (float)(u + fw) * 0.00390625F, (float)v * 0.00390625F);
			tess.addVertexWithUV(x, y, 0, (float)u * 0.00390625F, (float)v * 0.00390625F);
			tess.draw();
			// Right Leg
			x = x + width / 4;
			v = 80;
			tess.startDrawingQuads();
			tess.addVertexWithUV(x, y + h, 0, (float)(u + fw) * 0.00390625F, (float)(v + fh) * 0.00390625F);
			tess.addVertexWithUV(x + w, y + h, 0, (float)u * 0.00390625F, (float)(v + fh) * 0.00390625F);
			tess.addVertexWithUV(x + w, y, 0, (float)u * 0.00390625F, (float)v * 0.00390625F);
			tess.addVertexWithUV(x, y, 0, (float)(u + fw) * 0.00390625F, (float)v * 0.00390625F);
			tess.draw();
			// Right Pants
			v = 144;
			tess.startDrawingQuads();
			tess.addVertexWithUV(x, y + h, 0, (float)(u + fw) * 0.00390625F, (float)(v + fh) * 0.00390625F);
			tess.addVertexWithUV(x + w, y + h, 0, (float)u * 0.00390625F, (float)(v + fh) * 0.00390625F);
			tess.addVertexWithUV(x + w, y, 0, (float)u * 0.00390625F, (float)v * 0.00390625F);
			tess.addVertexWithUV(x, y, 0, (float)(u + fw) * 0.00390625F, (float)v * 0.00390625F);
			tess.draw();
			GL11.glDisable(GL11.GL_BLEND);
		} catch (IOException e) {
		}*/
	}
}
