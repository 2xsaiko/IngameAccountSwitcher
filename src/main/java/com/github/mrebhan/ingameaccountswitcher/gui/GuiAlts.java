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
		login.enabled = !alts.isEmpty();
		logino.enabled = !alts.isEmpty();
		del.enabled = !alts.isEmpty();
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
		this.drawCenteredString(fontRendererObj, StatCollector.translateToLocal("ias.selectaccount"), this.width / 2, 4, -1);
		if (Minecraft.getMinecraft().getSession().getToken().equals("0")) {
			this.drawCenteredString(fontRendererObj, StatCollector.translateToLocal("ias.offlinemode"), this.width / 2, 14, -1);
		}
		if (failed != null) {
			this.drawCenteredString(fontRendererObj, failed.getLocalizedMessage(), this.width / 2, 24, 16737380);
		}
		super.drawScreen(par1, par2, par3);
	}

	@Override
	protected void actionPerformed(GuiButton button){
		if (button.enabled)
		{
			if(button.id == 3){
				escape();
			}else if(button.id == 0){
				add();
			}else if(button.id == 4 && !alts.isEmpty()){
				delete();
			}else if(button.id == 1 && !alts.isEmpty()){
				login(selectedAlt);
			}else if(button.id == 2 && !alts.isEmpty()){
				logino(selectedAlt);
			}else{
				lg.actionPerformed(button);
			}
		}
	}

	/**
	 * Used to ensure that the alt list here stays synced with the main alt list
	 */
	private void refreshAlts(){
		alts = AltDatabase.getInstance().getAlts();
	}

	private void escape(){
		mc.displayGuiScreen(null);
	}

	private void delete(){
		AltDatabase.getInstance().getAlts().remove(selectedAlt);
		refreshAlts();
		if(alts.isEmpty()){
			login.enabled = false;
			logino.enabled = false;
			del.enabled = false;
		}
	}

	private void add(){
		mc.displayGuiScreen(new GuiAddAlt());
	}

	private void logino(int selected){
		AccountData data = alts.get(selected);
		AltManager.getInstance().setUserOffline(data.alias);
		failed = null;
		Minecraft.getMinecraft().displayGuiScreen(null);
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
		if(par2 == Keyboard.KEY_ESCAPE){
			escape();
		}
		if(par2 == Keyboard.KEY_DELETE){
			delete();
		}
		if(par1 == '+'){
			add();
		}
		if(par2 == Keyboard.KEY_RETURN){
			if(Keyboard.isKeyDown(Keyboard.KEY_RSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)){
				logino(selectedAlt);
			}else{
				login(selectedAlt);
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
			super(mcIn, GuiAlts.this.width, GuiAlts.this.height, 32, GuiAlts.this.height - 64, 14);
		}

		@Override
		protected int getSize()
		{
			return GuiAlts.this.alts.size();
		}

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

		@Override
		protected boolean isSelected(int slotIndex)
		{
			return slotIndex == GuiAlts.this.selectedAlt;
		}

		@Override
		protected int getContentHeight()
		{
			return GuiAlts.this.alts.size() * 14;
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
			if (StringUtils.isEmpty(s))
			{
				s = StatCollector.translateToLocal("ias.alt") + " " + (entryID + 1);
			}

			GuiAlts.this.drawString(GuiAlts.this.fontRendererObj, s, p_180791_2_ + 2, p_180791_3_ + 1, 16777215);
		}
	}
}
