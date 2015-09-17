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

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.FMLInjectionData;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
/**
 * Tools that have to do with Skins
 * @author The_Fireplace
 *
 */
@SideOnly(Side.CLIENT)
public class SkinTools {
	public static final File cachedir = new File((File)FMLInjectionData.data()[6], "cachedskins/");

	public static void drawSkinFront(String name, int x, int y, int width, int height){
		try {
			AbstractClientPlayer.getDownloadImageSkin(AbstractClientPlayer.getLocationSkin(name), name).loadTexture(Minecraft.getMinecraft().getResourceManager());
			Minecraft.getMinecraft().getTextureManager().bindTexture(AbstractClientPlayer.getLocationSkin(name));
			boolean slim;
			try{
				slim=isSkinSlim(ImageIO.read(new File(cachedir, name+".png")));
			}catch(IOException e){
				slim=DefaultPlayerSkin.getSkinType(EntityPlayer.getOfflineUUID(name)).equals("slim");
			}
			Tessellator tess = Tessellator.getInstance();
			WorldRenderer rend = tess.getWorldRenderer();
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
			rend.startDrawingQuads();
			rend.addVertexWithUV(x, y + h, 0, (float)u * 0.00390625F, (float)(v + fh) * 0.00390625F);
			rend.addVertexWithUV(x + w, y + h, 0, (float)(u + fw) * 0.00390625F, (float)(v + fh) * 0.00390625F);
			rend.addVertexWithUV(x + w, y, 0, (float)(u + fw) * 0.00390625F, (float)v * 0.00390625F);
			rend.addVertexWithUV(x, y, 0, (float)u * 0.00390625F, (float)v * 0.00390625F);
			tess.draw();
			// Hat
			u = 160;
			rend.startDrawingQuads();
			rend.addVertexWithUV(x, y + h, 0, (float)u * 0.00390625F, (float)(v + fh) * 0.00390625F);
			rend.addVertexWithUV(x + w, y + h, 0, (float)(u + fw) * 0.00390625F, (float)(v + fh) * 0.00390625F);
			rend.addVertexWithUV(x + w, y, 0, (float)(u + fw) * 0.00390625F, (float)v * 0.00390625F);
			rend.addVertexWithUV(x, y, 0, (float)u * 0.00390625F, (float)v * 0.00390625F);
			tess.draw();
			// Chest
			y = y + height / 4;
			h = height / 8 * 3;
			fh = 48;
			u = 80;
			v = 80;
			rend.startDrawingQuads();
			rend.addVertexWithUV(x, y + h, 0, (float)u * 0.00390625F, (float)(v + fh) * 0.00390625F);
			rend.addVertexWithUV(x + w, y + h, 0, (float)(u + fw) * 0.00390625F, (float)(v + fh) * 0.00390625F);
			rend.addVertexWithUV(x + w, y, 0, (float)(u + fw) * 0.00390625F, (float)v * 0.00390625F);
			rend.addVertexWithUV(x, y, 0, (float)u * 0.00390625F, (float)v * 0.00390625F);
			tess.draw();
			// Jacket
			v = 144;
			rend.startDrawingQuads();
			rend.addVertexWithUV(x, y + h, 0, (float)u * 0.00390625F, (float)(v + fh) * 0.00390625F);
			rend.addVertexWithUV(x + w, y + h, 0, (float)(u + fw) * 0.00390625F, (float)(v + fh) * 0.00390625F);
			rend.addVertexWithUV(x + w, (double)y + 0, 0, (float)(u + fw) * 0.00390625F, (float)v * 0.00390625F);
			rend.addVertexWithUV(x, y, 0, (float)u * 0.00390625F, (float)v * 0.00390625F);
			tess.draw();
			// Left Arm
			x = x - width / 16 * (slim ? 3 : 4);
			y = y + (slim ? height / 32 : 0);
			w = width / 16 * (slim ? 3 : 4);
			h = height / 8 * 3;
			fw = slim ? 12 : 16;
			fh = 48;
			u = 176;
			v = 80;
			rend.startDrawingQuads();
			rend.addVertexWithUV(x, y + h - 4, 0, (float)u * 0.00390625F, (float)(v + fh) * 0.00390625F);
			rend.addVertexWithUV(x + w, y + h - 4, 0, (float)(u + fw) * 0.00390625F, (float)(v + fh) * 0.00390625F);
			rend.addVertexWithUV(x + w, (double)y - 4, 0, (float)(u + fw) * 0.00390625F, (float)v * 0.00390625F);
			rend.addVertexWithUV(x, (double)y - 4, 0, (float)u * 0.00390625F, (float)v * 0.00390625F);
			tess.draw();
			// Left Sleeve
			v = 144;
			rend.startDrawingQuads();
			rend.addVertexWithUV(x, y + h - 4, 0, (float)u * 0.00390625F, (float)(v + fh) * 0.00390625F);
			rend.addVertexWithUV(x + w, y + h - 4, 0, (float)(u + fw) * 0.00390625F, (float)(v + fh) * 0.00390625F);
			rend.addVertexWithUV(x + w, (double)y - 4, 0, (float)(u + fw) * 0.00390625F, (float)v * 0.00390625F);
			rend.addVertexWithUV(x, (double)y - 4, 0, (float)u * 0.00390625F, (float)v * 0.00390625F);
			tess.draw();
			// Right Arm
			x = x + width / 16 * (slim ? 11 : 12);
			v = 80;
			rend.startDrawingQuads();
			rend.addVertexWithUV(x, y + h - 4, 0, (float)(u + fw) * 0.00390625F, (float)(v + fh) * 0.00390625F);
			rend.addVertexWithUV(x + w, y + h - 4, 0, (float)u * 0.00390625F, (float)(v + fh) * 0.00390625F);
			rend.addVertexWithUV(x + w, (double)y - 4, 0, (float)u * 0.00390625F, (float)v * 0.00390625F);
			rend.addVertexWithUV(x, (double)y - 4, 0, (float)(u + fw) * 0.00390625F, (float)v * 0.00390625F);
			tess.draw();
			// Right Sleeve
			v = 144;
			rend.startDrawingQuads();
			rend.addVertexWithUV(x, y + h - 4, 0, (float)(u + fw) * 0.00390625F, (float)(v + fh) * 0.00390625F);
			rend.addVertexWithUV(x + w, y + h - 4, 0, (float)u * 0.00390625F, (float)(v + fh) * 0.00390625F);
			rend.addVertexWithUV(x + w, (double)y - 4, 0, (float)u * 0.00390625F, (float)v * 0.00390625F);
			rend.addVertexWithUV(x, (double)y - 4, 0, (float)(u + fw) * 0.00390625F, (float)v * 0.00390625F);
			tess.draw();
			// Left Leg
			x = x - width / 2;
			y = y + height / 32 * (slim ? 11 : 12);
			w = width / 4;
			fw = 16;
			u = 16;
			v = 80;
			rend.startDrawingQuads();
			rend.addVertexWithUV(x, y + h, 0, (float)u * 0.00390625F, (float)(v + fh) * 0.00390625F);
			rend.addVertexWithUV(x + w, y + h, 0, (float)(u + fw) * 0.00390625F, (float)(v + fh) * 0.00390625F);
			rend.addVertexWithUV(x + w, y, 0, (float)(u + fw) * 0.00390625F, (float)v * 0.00390625F);
			rend.addVertexWithUV(x, y, 0, (float)u * 0.00390625F, (float)v * 0.00390625F);
			tess.draw();
			// Left Pants
			v = 144;
			rend.startDrawingQuads();
			rend.addVertexWithUV(x, y + h, 0, (float)u * 0.00390625F, (float)(v + fh) * 0.00390625F);
			rend.addVertexWithUV(x + w, y + h, 0, (float)(u + fw) * 0.00390625F, (float)(v + fh) * 0.00390625F);
			rend.addVertexWithUV(x + w, y, 0, (float)(u + fw) * 0.00390625F, (float)v * 0.00390625F);
			rend.addVertexWithUV(x, y, 0, (float)u * 0.00390625F, (float)v * 0.00390625F);
			tess.draw();
			// Right Leg
			x = x + width / 4;
			v = 80;
			rend.startDrawingQuads();
			rend.addVertexWithUV(x, y + h, 0, (float)(u + fw) * 0.00390625F, (float)(v + fh) * 0.00390625F);
			rend.addVertexWithUV(x + w, y + h, 0, (float)u * 0.00390625F, (float)(v + fh) * 0.00390625F);
			rend.addVertexWithUV(x + w, y, 0, (float)u * 0.00390625F, (float)v * 0.00390625F);
			rend.addVertexWithUV(x, y, 0, (float)(u + fw) * 0.00390625F, (float)v * 0.00390625F);
			tess.draw();
			// Right Pants
			v = 144;
			rend.startDrawingQuads();
			rend.addVertexWithUV(x, y + h, 0, (float)(u + fw) * 0.00390625F, (float)(v + fh) * 0.00390625F);
			rend.addVertexWithUV(x + w, y + h, 0, (float)u * 0.00390625F, (float)(v + fh) * 0.00390625F);
			rend.addVertexWithUV(x + w, y, 0, (float)u * 0.00390625F, (float)v * 0.00390625F);
			rend.addVertexWithUV(x, y, 0, (float)(u + fw) * 0.00390625F, (float)v * 0.00390625F);
			tess.draw();
			GL11.glDisable(GL11.GL_BLEND);
		} catch (IOException e) {
			//e.printStackTrace();
		}
	}
	public static boolean isSkinSlim(BufferedImage img){
		if(img == null)
			return false;
		return img.getRGB(54, 25)>>24 == 0x00;
	}
	public static void cacheSkins(){
		for(AccountData data : AltDatabase.getInstance().getAlts()){
			File file = new File(cachedir, data.alias+".png");
			if(!file.exists()){
				try{
					URL url = new URL(String.format("http://skins.minecraft.net/MinecraftSkins/%s.png", data.alias));
					InputStream is = url.openStream();
					file.createNewFile();
					OutputStream os = new FileOutputStream(file);

					byte[] b = new byte[2048];
					int length;

					while((length = is.read(b)) != -1){
						os.write(b, 0, length);
					}
					is.close();
					os.close();
				}catch(IOException e){
				}
			}else{
				try{
					URL url = new URL(String.format("http://skins.minecraft.net/MinecraftSkins/%s.png", data.alias));
					InputStream is = url.openStream();
					file.delete();
					file.createNewFile();
					OutputStream os = new FileOutputStream(file);

					byte[] b = new byte[2048];
					int length;

					while((length = is.read(b)) != -1){
						os.write(b, 0, length);
					}
					is.close();
					os.close();
				}catch(IOException e){
				}
			}
		}
	}
}
