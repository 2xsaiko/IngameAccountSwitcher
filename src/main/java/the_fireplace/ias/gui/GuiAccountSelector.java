package the_fireplace.ias.gui;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.lang3.StringUtils;
import org.lwjgl.input.Keyboard;

import com.github.mrebhan.ingameaccountswitcher.gui.GuiAddAlt;
import com.github.mrebhan.ingameaccountswitcher.tools.alt.AccountData;
import com.github.mrebhan.ingameaccountswitcher.tools.alt.AltDatabase;
import com.github.mrebhan.ingameaccountswitcher.tools.alt.AltManager;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiSlot;
import net.minecraft.util.StatCollector;
/**
 * The GUI where you can log in to, add, and remove accounts
 * @author The_Fireplace
 */
public class GuiAccountSelector extends GuiScreen {
	private int selectedAccountIndex = 0;
	private Throwable loginfailed;
	private ArrayList<AccountData> accounts = AltDatabase.getInstance().getAlts();
	private GuiAccountSelector.List accountsgui;
	//Buttons that can be disabled need to be here
	private GuiButton login;
	private GuiButton loginoffline;
	private GuiButton delete;

	@Override
	public void initGui() {
		accountsgui = new GuiAccountSelector.List(this.mc);
		accountsgui.registerScrollButtons(5, 6);
		this.buttonList.add(new GuiButton(0, this.width / 2 + 4, this.height - 52, 160, 20, StatCollector.translateToLocal("ias.addaccount")));
		this.buttonList.add(login = new GuiButton(1, this.width / 2 - 154 - 10, this.height - 52, 160, 20, StatCollector.translateToLocal("ias.login")));
		this.buttonList.add(loginoffline = new GuiButton(2, this.width / 2 - 154 - 10, this.height - 28, 110, 20, StatCollector.translateToLocal("ias.login")+" "+StatCollector.translateToLocal("ias.offline")));
		this.buttonList.add(new GuiButton(3, this.width / 2 + 4 + 50, this.height - 28, 110, 20, StatCollector.translateToLocal("gui.cancel")));
		this.buttonList.add(delete = new GuiButton(4, this.width / 2 - 50, this.height - 28, 100, 20, StatCollector.translateToLocal("ias.delete")));
		login.enabled = !accounts.isEmpty();
		loginoffline.enabled = !accounts.isEmpty();
		delete.enabled = !accounts.isEmpty();
	}
	@Override
	public void handleMouseInput() throws IOException
	{
		super.handleMouseInput();
		this.accountsgui.handleMouseInput();
	}

	@Override
	public void drawScreen(int par1, int par2, float par3) {
		accountsgui.drawScreen(par1, par2, par3);
		this.drawCenteredString(fontRendererObj, StatCollector.translateToLocal("ias.selectaccount"), this.width / 2, 4, -1);
		if (Minecraft.getMinecraft().getSession().getToken().equals("0")) {
			this.drawCenteredString(fontRendererObj, StatCollector.translateToLocal("ias.offlinemode"), this.width / 2, 14, -1);
		}
		if (loginfailed != null) {
			this.drawCenteredString(fontRendererObj, loginfailed.getLocalizedMessage(), this.width / 2, 24, 16737380);
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
			}else if(button.id == 4 && !accounts.isEmpty()){
				delete();
			}else if(button.id == 1 && !accounts.isEmpty()){
				login(selectedAccountIndex);
			}else if(button.id == 2 && !accounts.isEmpty()){
				logino(selectedAccountIndex);
			}else{
				accountsgui.actionPerformed(button);
			}
		}
	}

	/**
	 * Used to ensure that the alt list here stays in sync with the main alt list
	 */
	private void refreshAlts(){
		accounts = AltDatabase.getInstance().getAlts();
	}
	/**
	 * Leave the gui
	 */
	private void escape(){
		mc.displayGuiScreen(null);
	}
	/**
	 * Delete the selected account
	 */
	private void delete(){
		AltDatabase.getInstance().getAlts().remove(selectedAccountIndex);
		refreshAlts();
		if(accounts.isEmpty()){
			login.enabled = false;
			loginoffline.enabled = false;
			delete.enabled = false;
		}
	}
	/**
	 * Add an account
	 */
	private void add(){
		mc.displayGuiScreen(new GuiAddAlt());
	}
	/**
	 * Login to the account in offline mode, then return to main menu
	 * @param selected
	 * 		The index of the account to log in to
	 */
	private void logino(int selected){
		AccountData data = accounts.get(selected);
		AltManager.getInstance().setUserOffline(data.alias);
		loginfailed = null;
		Minecraft.getMinecraft().displayGuiScreen(null);
	}
	/**
	 * Attempt login to the account, then return to main menu if successful
	 * @param selected
	 * 		The index of the account to log in to
	 */
	private void login(int selected){
		AccountData data = accounts.get(selected);
		loginfailed = AltManager.getInstance().setUser(data.user, data.pass);
		if (loginfailed == null) {
			Minecraft.getMinecraft().displayGuiScreen(null);
		}
	}

	@Override
	protected void keyTyped(char character, int keyIndex) {
		if (keyIndex == Keyboard.KEY_UP) {
			if (selectedAccountIndex > 0) {
				selectedAccountIndex--;
			}
		}
		if (keyIndex == Keyboard.KEY_DOWN) {
			if (selectedAccountIndex < accounts.size() - 1) {
				selectedAccountIndex++;
			}
		}
		if(keyIndex == Keyboard.KEY_ESCAPE){
			escape();
		}
		if(keyIndex == Keyboard.KEY_DELETE){
			delete();
		}
		if(character == '+'){
			add();
		}
		if(keyIndex == Keyboard.KEY_RETURN){
			if(Keyboard.isKeyDown(Keyboard.KEY_RSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)){
				logino(selectedAccountIndex);
			}else{
				login(selectedAccountIndex);
			}
		}
	}
	class List extends GuiSlot
	{
		public List(Minecraft mcIn)
		{
			super(mcIn, GuiAccountSelector.this.width, GuiAccountSelector.this.height, 32, GuiAccountSelector.this.height - 64, 14);
		}

		@Override
		protected int getSize()
		{
			return GuiAccountSelector.this.accounts.size();
		}

		@Override
		protected void elementClicked(int slotIndex, boolean isDoubleClick, int mouseX, int mouseY)
		{
			GuiAccountSelector.this.selectedAccountIndex = slotIndex;
			boolean flag = GuiAccountSelector.this.selectedAccountIndex >= 0 && GuiAccountSelector.this.selectedAccountIndex < this.getSize();
			GuiAccountSelector.this.login.enabled = flag;
			GuiAccountSelector.this.loginoffline.enabled = flag;
			GuiAccountSelector.this.delete.enabled = flag;

			if (isDoubleClick && flag)
			{
				GuiAccountSelector.this.login(slotIndex);
			}
		}

		@Override
		protected boolean isSelected(int slotIndex)
		{
			return slotIndex == GuiAccountSelector.this.selectedAccountIndex;
		}

		@Override
		protected int getContentHeight()
		{
			return GuiAccountSelector.this.accounts.size() * 14;
		}

		@Override
		protected void drawBackground()
		{
			GuiAccountSelector.this.drawDefaultBackground();
		}

		@Override
		protected void drawSlot(int entryID, int p_180791_2_, int p_180791_3_, int p_180791_4_, int p_180791_5_, int p_180791_6_)
		{
			String s = GuiAccountSelector.this.accounts.get(entryID).alias;
			if (StringUtils.isEmpty(s))
			{
				s = StatCollector.translateToLocal("ias.alt") + " " + (entryID + 1);
			}

			GuiAccountSelector.this.drawString(GuiAccountSelector.this.fontRendererObj, s, p_180791_2_ + 2, p_180791_3_ + 1, 16777215);
		}
	}
}
