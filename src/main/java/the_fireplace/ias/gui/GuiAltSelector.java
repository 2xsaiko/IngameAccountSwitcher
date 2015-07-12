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
 * The GUI with all of the alts on it
 * @author mrebhan
 * @author The_Fireplace
 */
public class GuiAltSelector extends GuiScreen {
	private int selectedAlt = 0;
	private Throwable failed;
	private ArrayList<AccountData> alts = AltDatabase.getInstance().getAlts();
	private GuiAltSelector.List lg;
	//Buttons that can be disabled need to be here
	private GuiButton login;
	private GuiButton logino;
	private GuiButton del;

	@Override
	public void initGui() {
		lg = new GuiAltSelector.List(this.mc);
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
	 * Used to ensure that the alt list here stays in sync with the main alt list
	 */
	private void refreshAlts(){
		alts = AltDatabase.getInstance().getAlts();
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
		AltDatabase.getInstance().getAlts().remove(selectedAlt);
		refreshAlts();
		if(alts.isEmpty()){
			login.enabled = false;
			logino.enabled = false;
			del.enabled = false;
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
		AccountData data = alts.get(selected);
		AltManager.getInstance().setUserOffline(data.alias);
		failed = null;
		Minecraft.getMinecraft().displayGuiScreen(null);
	}
	/**
	 * Attempt login to the account, then return to main menu if successful
	 * @param selected
	 * 		The index of the account to log in to
	 */
	private void login(int selected){
		AccountData data = alts.get(selected);
		failed = AltManager.getInstance().setUser(data.user, data.pass);
		if (failed == null) {
			Minecraft.getMinecraft().displayGuiScreen(null);
		}
	}

	@Override
	protected void keyTyped(char character, int keyIndex) {
		if (keyIndex == Keyboard.KEY_UP) {
			if (selectedAlt > 0) {
				selectedAlt--;
			}
		}
		if (keyIndex == Keyboard.KEY_DOWN) {
			if (selectedAlt < alts.size() - 1) {
				selectedAlt++;
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
				logino(selectedAlt);
			}else{
				login(selectedAlt);
			}
		}
	}
	class List extends GuiSlot
	{
		public List(Minecraft mcIn)
		{
			super(mcIn, GuiAltSelector.this.width, GuiAltSelector.this.height, 32, GuiAltSelector.this.height - 64, 14);
		}

		@Override
		protected int getSize()
		{
			return GuiAltSelector.this.alts.size();
		}

		@Override
		protected void elementClicked(int slotIndex, boolean isDoubleClick, int mouseX, int mouseY)
		{
			GuiAltSelector.this.selectedAlt = slotIndex;
			boolean flag = GuiAltSelector.this.selectedAlt >= 0 && GuiAltSelector.this.selectedAlt < this.getSize();
			GuiAltSelector.this.login.enabled = flag;
			GuiAltSelector.this.logino.enabled = flag;
			GuiAltSelector.this.del.enabled = flag;

			if (isDoubleClick && flag)
			{
				GuiAltSelector.this.login(slotIndex);
			}
		}

		@Override
		protected boolean isSelected(int slotIndex)
		{
			return slotIndex == GuiAltSelector.this.selectedAlt;
		}

		@Override
		protected int getContentHeight()
		{
			return GuiAltSelector.this.alts.size() * 14;
		}

		@Override
		protected void drawBackground()
		{
			GuiAltSelector.this.drawDefaultBackground();
		}

		@Override
		protected void drawSlot(int entryID, int p_180791_2_, int p_180791_3_, int p_180791_4_, int p_180791_5_, int p_180791_6_)
		{
			String s = GuiAltSelector.this.alts.get(entryID).alias;
			if (StringUtils.isEmpty(s))
			{
				s = StatCollector.translateToLocal("ias.alt") + " " + (entryID + 1);
			}

			GuiAltSelector.this.drawString(GuiAltSelector.this.fontRendererObj, s, p_180791_2_ + 2, p_180791_3_ + 1, 16777215);
		}
	}
}
