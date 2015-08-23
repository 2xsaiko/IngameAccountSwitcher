package the_fireplace.ias.gui;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.lang3.StringUtils;
import org.lwjgl.input.Keyboard;

import com.github.mrebhan.ingameaccountswitcher.tools.Tools;
import com.github.mrebhan.ingameaccountswitcher.tools.alt.AccountData;
import com.github.mrebhan.ingameaccountswitcher.tools.alt.AltDatabase;
import com.github.mrebhan.ingameaccountswitcher.tools.alt.AltManager;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiSlot;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.StatCollector;
import the_fireplace.ias.account.AlreadyLoggedInException;
import the_fireplace.ias.account.ExtendedAccountData;
import the_fireplace.ias.config.ConfigValues;
import the_fireplace.ias.enums.EnumBool;
import the_fireplace.ias.tools.HttpTools;
import the_fireplace.ias.tools.JavaTools;
import the_fireplace.ias.tools.SkinTools;
import the_fireplace.iasencrypt.EncryptionTools;
/**
 * The GUI where you can log in to, add, and remove accounts
 * @author The_Fireplace
 */
public class GuiAccountSelector extends GuiScreen {
	private int selectedAccountIndex = 0;
	private Throwable loginfailed;
	private ArrayList<ExtendedAccountData> queriedaccounts = convertData();
	private GuiAccountSelector.List accountsgui;
	//Buttons that can be disabled need to be here
	private GuiButton login;
	private GuiButton loginoffline;
	private GuiButton delete;
	private GuiButton edit;
	//Search
	private String query;
	private GuiTextField search;

	@Override
	public void initGui() {
		Keyboard.enableRepeatEvents(true);
		accountsgui = new GuiAccountSelector.List(this.mc);
		accountsgui.registerScrollButtons(5, 6);
		query = StatCollector.translateToLocal("ias.search");
		this.buttonList.clear();
		//Top Row
		this.buttonList.add(new GuiButton(0, this.width / 2 + 4 + 40, this.height - 52, 120, 20, StatCollector.translateToLocal("ias.addaccount")));
		this.buttonList.add(login = new GuiButton(1, this.width / 2 - 154 - 10, this.height - 52, 120, 20, StatCollector.translateToLocal("ias.login")));
		this.buttonList.add(edit = new GuiButton(7, this.width / 2 - 40, this.height - 52, 80, 20, StatCollector.translateToLocal("ias.edit")));
		//Bottom Row
		this.buttonList.add(loginoffline = new GuiButton(2, this.width / 2 - 154 - 10, this.height - 28, 110, 20, StatCollector.translateToLocal("ias.login")+" "+StatCollector.translateToLocal("ias.offline")));
		this.buttonList.add(new GuiButton(3, this.width / 2 + 4 + 50, this.height - 28, 110, 20, StatCollector.translateToLocal("gui.cancel")));
		this.buttonList.add(delete = new GuiButton(4, this.width / 2 - 50, this.height - 28, 100, 20, StatCollector.translateToLocal("ias.delete")));
		search  = new GuiTextField(this.fontRendererObj, this.width / 2 - 80, 14, 160, 16);
		search.setText(query);
		updateButtons();
	}

	@Override
	public void updateScreen(){
		this.search.updateCursorCounter();
		updateText();
		updateButtons();
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton)
	{
		super.mouseClicked(mouseX, mouseY, mouseButton);
		boolean flag = search.isFocused();
		this.search.mouseClicked(mouseX, mouseY, mouseButton);
		if(!flag && search.isFocused()){
			query = "";
			updateText();
			updateQueried();
		}
	}

	private void updateText(){
		search.setText(query);
	}

	@Override
	public void onGuiClosed()
	{
		Keyboard.enableRepeatEvents(false);
	}

	@Override
	public void drawScreen(int par1, int par2, float par3) {
		accountsgui.drawScreen(par1, par2, par3);
		this.drawCenteredString(fontRendererObj, StatCollector.translateToLocal("ias.selectaccount"), this.width / 2, 4, -1);
		if (loginfailed != null) {
			this.drawCenteredString(fontRendererObj, loginfailed.getLocalizedMessage(), this.width / 2, this.height - 62, 16737380);
		}
		search.drawTextBox();
		super.drawScreen(par1, par2, par3);
		if(!queriedaccounts.isEmpty()){
			//SkinTools.drawSkinFront(queriedaccounts.get(selectedAccountIndex).alias, 8, height/2-64-16, 64, 128);
			this.drawString(fontRendererObj, StatCollector.translateToLocal("ias.nopreview1"), 2, height/2-10, -1);
			this.drawString(fontRendererObj, StatCollector.translateToLocal("ias.nopreview2"), 2, height/2, -1);
			this.drawString(fontRendererObj, StatCollector.translateToLocal("ias.nopreview3"), 2, height/2+10, -1);
			Tools.drawBorderedRect(width-8-64, height/2-64-16, width-8, height/2+64-16, 2, -5855578, -13421773);
			if(queriedaccounts.get(selectedAccountIndex).premium == EnumBool.TRUE)
				this.drawString(fontRendererObj, StatCollector.translateToLocal("ias.premium"), width-8-61, height/2-64-13, 6618980);
			else if(queriedaccounts.get(selectedAccountIndex).premium == EnumBool.FALSE)
				this.drawString(fontRendererObj, StatCollector.translateToLocal("ias.notpremium"), width-8-61, height/2-64-13, 16737380);
			this.drawString(fontRendererObj, StatCollector.translateToLocal("ias.timesused"), width-8-61, height/2-64-15+12, -1);
			this.drawString(fontRendererObj, String.valueOf(queriedaccounts.get(selectedAccountIndex).useCount), width-8-61, height/2-64-15+21, -1);
			if(queriedaccounts.get(selectedAccountIndex).useCount > 0){
				this.drawString(fontRendererObj, StatCollector.translateToLocal("ias.lastused"), width-8-61, height/2-64-15+30, -1);
				this.drawString(fontRendererObj, JavaTools.getJavaCompat().getFormattedDate(), width-8-61, height/2-64-15+39, -1);
			}
		}
	}

	@Override
	protected void actionPerformed(GuiButton button){
		if (button.enabled)
		{
			if(button.id == 3){
				escape();
			}else if(button.id == 0){
				add();
			}else if(button.id == 4){
				delete();
			}else if(button.id == 1){
				login(selectedAccountIndex);
			}else if(button.id == 2){
				logino(selectedAccountIndex);
			}else if(button.id == 7){
				edit();
			}else{
				accountsgui.actionPerformed(button);
			}
		}
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
		AltDatabase.getInstance().getAlts().remove(getCurrentAsEditable());
		if(selectedAccountIndex > 0)
			selectedAccountIndex--;
		updateQueried();
		updateButtons();
	}
	/**
	 * Add an account
	 */
	private void add(){
		mc.displayGuiScreen(new GuiAddAccount());
	}
	/**
	 * Login to the account in offline mode, then return to main menu
	 * @param selected
	 * 		The index of the account to log in to
	 */
	private void logino(int selected){
		ExtendedAccountData data = queriedaccounts.get(selected);
		AltManager.getInstance().setUserOffline(data.alias);
		loginfailed = null;
		Minecraft.getMinecraft().displayGuiScreen(null);
		ExtendedAccountData current = getCurrentAsEditable();
		current.useCount++;
		current.lastused=JavaTools.getJavaCompat().getDate();
	}
	/**
	 * Attempt login to the account, then return to main menu if successful
	 * @param selected
	 * 		The index of the account to log in to
	 */
	private void login(int selected){
		ExtendedAccountData data = queriedaccounts.get(selected);
		loginfailed = AltManager.getInstance().setUser(data.user, data.pass);
		if (loginfailed == null) {
			Minecraft.getMinecraft().displayGuiScreen(null);
			ExtendedAccountData current = getCurrentAsEditable();
			current.premium=EnumBool.TRUE;
			current.useCount++;
			current.lastused=JavaTools.getJavaCompat().getDate();
		}else if(loginfailed instanceof AlreadyLoggedInException){
			getCurrentAsEditable().lastused=JavaTools.getJavaCompat().getDate();
		}else if(HttpTools.ping("http://minecraft.net")){
			getCurrentAsEditable().premium=EnumBool.FALSE;
		}
	}
	/**
	 * Edits the current account's information
	 */
	private void edit(){
		mc.displayGuiScreen(new GuiEditAccount(selectedAccountIndex));
	}

	private void updateQueried(){
		queriedaccounts = convertData();
		if(query != StatCollector.translateToLocal("ias.search") && query != ""){
			for(int i=0;i<queriedaccounts.size();i++){
				if(!queriedaccounts.get(i).alias.contains(query) && ConfigValues.CASESENSITIVE){
					queriedaccounts.remove(i);
					i--;
				}else if(!queriedaccounts.get(i).alias.toLowerCase().contains(query.toLowerCase()) && !ConfigValues.CASESENSITIVE){
					queriedaccounts.remove(i);
					i--;
				}
			}
		}
		if(!queriedaccounts.isEmpty()){
			while(selectedAccountIndex >= queriedaccounts.size()){
				selectedAccountIndex--;
			}
		}
	}

	@Override
	protected void keyTyped(char character, int keyIndex) {
		if (keyIndex == Keyboard.KEY_UP && !queriedaccounts.isEmpty()) {
			if (selectedAccountIndex > 0) {
				selectedAccountIndex--;
			}
		} else if (keyIndex == Keyboard.KEY_DOWN && !queriedaccounts.isEmpty()) {
			if (selectedAccountIndex < queriedaccounts.size() - 1) {
				selectedAccountIndex++;
			}
		} else if(keyIndex == Keyboard.KEY_ESCAPE){
			escape();
		} else if(keyIndex == Keyboard.KEY_DELETE && delete.enabled){
			delete();
		} else if(character == '+'){
			add();
		} else if(character == '/' && edit.enabled){
			edit();
		} else if(keyIndex == Keyboard.KEY_RETURN && !search.isFocused() && (login.enabled || loginoffline.enabled)){
			if((Keyboard.isKeyDown(Keyboard.KEY_RSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) && loginoffline.enabled){
				logino(selectedAccountIndex);
			}else{
				if(login.enabled)
					login(selectedAccountIndex);
			}
		} else if(keyIndex == Keyboard.KEY_BACK){
			if (search.isFocused() && query.length() > 0) {
				query = query.substring(0, query.length()-1);
				updateText();
				updateQueried();
			}
		} else if (character != 0) {
			if (search.isFocused()) {
				if(keyIndex == Keyboard.KEY_RETURN){
					search.setFocused(false);
					updateText();
					updateQueried();
					return;
				}
				query += character;
				updateText();
				updateQueried();
			}
		}
	}
	private ArrayList<ExtendedAccountData> convertData(){
		ArrayList<AccountData> tmp = (ArrayList<AccountData>) AltDatabase.getInstance().getAlts().clone();
		ArrayList<ExtendedAccountData> converted = new ArrayList();
		int index=0;
		for(AccountData data : tmp){
			if(data instanceof ExtendedAccountData){
				converted.add((ExtendedAccountData) data);
			}else{
				converted.add(new ExtendedAccountData(EncryptionTools.decode(data.user), EncryptionTools.decode(data.pass), data.alias));
				AltDatabase.getInstance().getAlts().set(index, new ExtendedAccountData(EncryptionTools.decode(data.user), EncryptionTools.decode(data.pass), data.alias));
			}
			index++;
		}
		return converted;
	}
	private ArrayList<AccountData> getAccountList(){
		return AltDatabase.getInstance().getAlts();
	}
	private ExtendedAccountData getCurrentAsEditable(){
		for(AccountData dat : getAccountList()){
			if(dat instanceof ExtendedAccountData){
				if(((ExtendedAccountData)dat).equals(queriedaccounts.get(selectedAccountIndex))){
					return (ExtendedAccountData) dat;
				}
			}
		}
		return null;
	}
	private void updateButtons(){
		login.enabled = !queriedaccounts.isEmpty() && !EncryptionTools.decode(queriedaccounts.get(selectedAccountIndex).pass).equals("");
		loginoffline.enabled = !queriedaccounts.isEmpty();
		delete.enabled = !queriedaccounts.isEmpty();
		edit.enabled = !queriedaccounts.isEmpty();
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
			return GuiAccountSelector.this.queriedaccounts.size();
		}

		@Override
		protected void elementClicked(int slotIndex, boolean isDoubleClick, int mouseX, int mouseY)
		{
			GuiAccountSelector.this.selectedAccountIndex = slotIndex;
			GuiAccountSelector.this.updateButtons();

			if (isDoubleClick && GuiAccountSelector.this.login.enabled)
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
			return GuiAccountSelector.this.queriedaccounts.size() * 14;
		}

		@Override
		protected void drawBackground()
		{
			GuiAccountSelector.this.drawDefaultBackground();
		}

		@Override
		protected void drawSlot(int entryID, int par2, int par3, int p_148126_4_, Tessellator p_148126_5_, int p_148126_6_, int p_148126_7_) {
			String s = GuiAccountSelector.this.queriedaccounts.get(entryID).alias;
			if (StringUtils.isEmpty(s))
			{
				s = StatCollector.translateToLocal("ias.alt") + " " + (entryID + 1);
			}

			GuiAccountSelector.this.drawString(GuiAccountSelector.this.fontRendererObj, s, par2 + 2, par3 + 1, 16777215);
		}
	}
}
