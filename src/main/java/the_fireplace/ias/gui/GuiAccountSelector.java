package the_fireplace.ias.gui;

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
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.util.StatCollector;
import the_fireplace.ias.account.ExtendedAccountData;
import the_fireplace.ias.config.ConfigValues;
import the_fireplace.ias.enums.EnumBool;
import the_fireplace.ias.tools.HttpTools;
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
		search  = new GuiTextField(8, this.fontRendererObj, this.width / 2 - 80, 14, 160, 16);
		search.setText(query);
		login.enabled = !queriedaccounts.isEmpty();
		loginoffline.enabled = !queriedaccounts.isEmpty();
		delete.enabled = !queriedaccounts.isEmpty();
		edit.enabled = !queriedaccounts.isEmpty();
	}
	@Override
	public void handleMouseInput() throws IOException
	{
		super.handleMouseInput();
		this.accountsgui.handleMouseInput();
	}

	@Override
	public void updateScreen(){
		this.search.updateCursorCounter();
		updateText();
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
	{
		super.mouseClicked(mouseX, mouseY, mouseButton);
		boolean flag = search.isFocused();
		this.search.mouseClicked(mouseX, mouseY, mouseButton);
		if(!flag && search.isFocused()){
			query = "";
			updateText();
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
		if (Minecraft.getMinecraft().getSession().getToken().equals("0")) {
			this.drawCenteredString(fontRendererObj, StatCollector.translateToLocal("ias.offlinemode"), this.width / 2, 14, -1);//TODO: Relocate, it is behind the search field
		}
		if (loginfailed != null) {
			this.drawCenteredString(fontRendererObj, loginfailed.getLocalizedMessage(), this.width / 2, 24, 16737380);//TODO: Relocate, it is behind the search field
		}
		search.drawTextBox();
		super.drawScreen(par1, par2, par3);
		SkinTools.drawSkinFront(queriedaccounts.get(selectedAccountIndex).alias, 8, height/2-64-16, 64, 128);
	}

	@Override
	protected void actionPerformed(GuiButton button){
		if (button.enabled)
		{
			if(button.id == 3){
				escape();
			}else if(button.id == 0){
				add();
			}else if(button.id == 4 && !queriedaccounts.isEmpty()){
				delete();
			}else if(button.id == 1 && !queriedaccounts.isEmpty()){
				login(selectedAccountIndex);
			}else if(button.id == 2 && !queriedaccounts.isEmpty()){
				logino(selectedAccountIndex);
			}else if(button.id == 7 && !queriedaccounts.isEmpty()){
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
		System.out.println("Delete called on "+queriedaccounts.get(selectedAccountIndex));
		AltDatabase.getInstance().getAlts().remove(queriedaccounts.get(selectedAccountIndex));
		updateQueried();
		if(queriedaccounts.isEmpty()){
			login.enabled = false;
			loginoffline.enabled = false;
			delete.enabled = false;
			edit.enabled = false;
		}
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
		getCurrentAsEditable().useCount++;
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
			getCurrentAsEditable().premium=EnumBool.TRUE;
		}else if(HttpTools.ping("authserver.mojang.com")){
			getCurrentAsEditable().premium=EnumBool.FALSE;
		}
		getCurrentAsEditable().useCount++;
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
	}

	@Override
	protected void keyTyped(char character, int keyIndex) {
		if (keyIndex == Keyboard.KEY_UP) {
			if (selectedAccountIndex > 0) {
				selectedAccountIndex--;
			}
		} else if (keyIndex == Keyboard.KEY_DOWN) {
			if (selectedAccountIndex < queriedaccounts.size() - 1) {
				selectedAccountIndex++;
			}
		} else if(keyIndex == Keyboard.KEY_ESCAPE){
			escape();
		} else if(keyIndex == Keyboard.KEY_DELETE){
			delete();
		} else if(character == '+'){
			add();
		} else if(character == '/'){
			edit();
		} else if(keyIndex == Keyboard.KEY_RETURN && !search.isFocused()){
			if(Keyboard.isKeyDown(Keyboard.KEY_RSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)){
				logino(selectedAccountIndex);
			}else{
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
			boolean flag = GuiAccountSelector.this.selectedAccountIndex >= 0 && GuiAccountSelector.this.selectedAccountIndex < this.getSize();
			GuiAccountSelector.this.login.enabled = flag;
			GuiAccountSelector.this.loginoffline.enabled = flag;
			GuiAccountSelector.this.delete.enabled = flag;
			GuiAccountSelector.this.edit.enabled = flag;

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
			return GuiAccountSelector.this.queriedaccounts.size() * 14;
		}

		@Override
		protected void drawBackground()
		{
			GuiAccountSelector.this.drawDefaultBackground();
		}

		@Override
		protected void drawSlot(int entryID, int p_180791_2_, int p_180791_3_, int p_180791_4_, int p_180791_5_, int p_180791_6_)
		{
			String s = GuiAccountSelector.this.queriedaccounts.get(entryID).alias;
			if (StringUtils.isEmpty(s))
			{
				s = StatCollector.translateToLocal("ias.alt") + " " + (entryID + 1);
			}

			GuiAccountSelector.this.drawString(GuiAccountSelector.this.fontRendererObj, s, p_180791_2_ + 2, p_180791_3_ + 1, 16777215);
		}
	}
}
