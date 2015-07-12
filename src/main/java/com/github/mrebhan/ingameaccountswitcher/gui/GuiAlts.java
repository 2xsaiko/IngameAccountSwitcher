package com.github.mrebhan.ingameaccountswitcher.gui;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.lang3.StringUtils;
import org.lwjgl.input.Keyboard;

import com.github.mrebhan.ingameaccountswitcher.tools.alt.AccountData;
import com.github.mrebhan.ingameaccountswitcher.tools.alt.AltDatabase;
import com.github.mrebhan.ingameaccountswitcher.tools.alt.AltManager;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiSlot;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
/**
 * @author mrebhan
 * @author The_Fireplace
 */
public class GuiAlts extends GuiScreen {
	private int selectedAlt = 0;
	private Throwable failed;
	private ArrayList<AccountData> alts = AltDatabase.getInstance().getAlts();
	private GuiButton login;
	private GuiButton logino;
	private GuiButton del;
	private GuiAlts.List lg;

	@Override
	public void initGui() {
		lg = new GuiAlts.List(this.mc);
		lg.registerScrollButtons(5, 6);
		this.buttonList.add(new GuiButton(0, this.width / 2 + 4, this.height - 52, 160, 20, StatCollector.translateToLocal("ias.addaccount")));
		this.buttonList.add(login = new GuiButton(1, this.width / 2 - 154 - 10, this.height - 52, 160, 20, StatCollector.translateToLocal("ias.login")));
		this.buttonList.add(logino = new GuiButton(2, this.width / 2 - 154 - 10, this.height - 28, 110, 20, StatCollector.translateToLocal("ias.login")+" "+StatCollector.translateToLocal("ias.offline")));
		this.buttonList.add(new GuiButton(3, this.width / 2 + 4 + 50, this.height - 28, 110, 20, StatCollector.translateToLocal("gui.cancel")));
		this.buttonList.add(del = new GuiButton(4, this.width / 2 - 50, this.height - 28, 100, 20, StatCollector.translateToLocal("ias.delete")));
		login.enabled = false;
		logino.enabled = false;
		del.enabled = false;
	}
	@Override
	public void handleMouseInput() throws IOException
	{
		super.handleMouseInput();
		this.lg.handleMouseInput();
	}

	@Override
	public void drawScreen(int par1, int par2, float par3) {
		lg.drawScreen(par1, par2, par3);
		String s = "";
		try {
			s = alts.get(selectedAlt).alias;
		} catch (Exception e) {
			if (selectedAlt > 0) {
				selectedAlt--;
			}
		}
		this.drawCenteredString(fontRendererObj, StatCollector.translateToLocal("ias.selectaccount"), this.width / 2, 7, -1);
		if (Minecraft.getMinecraft().getSession().getToken().equals("0")) {
			this.drawCenteredString(fontRendererObj, StatCollector.translateToLocal("ias.offlinemode"), this.width / 2, this.height - 32, -1);
		}
		if (failed != null) {
			this.drawCenteredString(fontRendererObj, failed.getLocalizedMessage(), this.width / 2, this.height - 22, -1);
		}
		/*GL11.glPushMatrix();
		GL11.glTranslated(0, 13, 0);
		for (int i = 0; i < alts.size(); i++) {
			this.drawString(fontRendererObj, alts.get(i).alias, 2, i * 12 + 2, -1);
		}
		if(!alts.isEmpty())
			Tools.drawBorderedRect(0, selectedAlt * 12, fontRendererObj.getStringWidth(s) + 4, selectedAlt * 12 + 12, 1, 0xff444444, 0x00000000);
		GL11.glPopMatrix();*/
		super.drawScreen(par1, par2, par3);
	}

	@Override
	protected void actionPerformed(GuiButton button){
		if (button.enabled)
		{
			if(button.id == 3){
				mc.displayGuiScreen(null);
			}else if(button.id == 0){
				mc.displayGuiScreen(new GuiAddAlt());
			}else if(button.id == 4 && !alts.isEmpty()){
				AltDatabase.getInstance().getAlts().remove(selectedAlt);
			}else if(button.id == 1 && !alts.isEmpty()){
				login(selectedAlt);
			}else if(button.id == 2 && !alts.isEmpty()){
				AccountData data = alts.get(selectedAlt);
				AltManager.getInstance().setUserOffline(data.alias);
				failed = null;
				Minecraft.getMinecraft().displayGuiScreen(null);
			}else{
				lg.actionPerformed(button);
			}
		}
	}

	@Override
	protected void keyTyped(char par1, int par2) {
		if (par2 == Keyboard.KEY_UP) {
			if (selectedAlt > 0) {
				selectedAlt--;
			}
		}
		if (par2 == Keyboard.KEY_DOWN) {
			if (selectedAlt < alts.size() - 1) {
				selectedAlt++;
			}
		}
	}
	private void login(int selected){
		AccountData data = alts.get(selected);
		failed = AltManager.getInstance().setUser(data.user, data.pass);
		if (failed == null) {
			Minecraft.getMinecraft().displayGuiScreen(null);
		}
	}
	@SideOnly(Side.CLIENT)
	class List extends GuiSlot
	{
		public List(Minecraft mcIn)
		{
			super(mcIn, GuiAlts.this.width, GuiAlts.this.height, 32, GuiAlts.this.height - 64, 24);
		}

		@Override
		protected int getSize()
		{
			return GuiAlts.this.alts.size();
		}

		/**
		 * The element in the slot that was clicked, boolean for whether it was double clicked or not
		 */
		@Override
		protected void elementClicked(int slotIndex, boolean isDoubleClick, int mouseX, int mouseY)
		{
			GuiAlts.this.selectedAlt = slotIndex;
			boolean flag1 = GuiAlts.this.selectedAlt >= 0 && GuiAlts.this.selectedAlt < this.getSize();
			GuiAlts.this.login.enabled = flag1;
			GuiAlts.this.logino.enabled = flag1;
			GuiAlts.this.del.enabled = flag1;

			if (isDoubleClick && flag1)
			{
				GuiAlts.this.login(slotIndex);
			}
		}

		/**
		 * Returns true if the element passed in is currently selected
		 */
		@Override
		protected boolean isSelected(int slotIndex)
		{
			return slotIndex == GuiAlts.this.selectedAlt;
		}

		/**
		 * Return the height of the content being scrolled
		 */
		@Override
		protected int getContentHeight()
		{
			return GuiAlts.this.alts.size() * 36;
		}

		@Override
		protected void drawBackground()
		{
			GuiAlts.this.drawDefaultBackground();
		}

		@Override
		protected void drawSlot(int entryID, int p_180791_2_, int p_180791_3_, int p_180791_4_, int p_180791_5_, int p_180791_6_)
		{
			String s = GuiAlts.this.alts.get(entryID).alias;
			String s1 = GuiAlts.this.alts.get(entryID).user;
			if (StringUtils.isEmpty(s))
			{
				s = StatCollector.translateToLocal("ias.alt") + " " + (entryID + 1);
			}

			GuiAlts.this.drawString(GuiAlts.this.fontRendererObj, s, p_180791_2_ + 2, p_180791_3_ + 1, 16777215);
		}
	}
}
