package the_fireplace.ias.gui;

import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

import org.lwjgl.input.Keyboard;

import com.github.mrebhan.ingameaccountswitcher.tools.alt.AccountData;
import com.github.mrebhan.ingameaccountswitcher.tools.alt.AltDatabase;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.util.StatCollector;
/**
 * The GUI where the alt is added
 * @author The_Fireplace
 */
public class GuiAddAccount extends GuiScreen {

	private String user = "", pass = "", cover = "";
	private GuiTextField username;
	private GuiTextField password;
	private GuiButton addaccount;

	@Override
	public void initGui() {
		Keyboard.enableRepeatEvents(true);
		this.buttonList.clear();
		this.buttonList.add(addaccount = new GuiButton(2, this.width / 2 - 152, this.height - 28, 150, 20, StatCollector.translateToLocal("ias.addaccount")));
		this.buttonList.add(new GuiButton(3, this.width / 2 + 2, this.height - 28, 150, 20, StatCollector.translateToLocal("gui.cancel")));
		username = new GuiTextField(0, this.fontRendererObj, this.width / 2 - 100, 60, 200, 20);
		username.setFocused(true);
		username.setText(user);
		password = new GuiTextField(1, this.fontRendererObj, this.width / 2 - 100, 90, 200, 20);
		password.setText(cover);
		addaccount.enabled = username.getText().length() > 0;
	}

	@Override
	public void drawScreen(int par1, int par2, float par3) {
		this.drawDefaultBackground();
		this.drawCenteredString(fontRendererObj, StatCollector.translateToLocal("ias.addaccount"), this.width / 2, 7, -1);
		this.drawCenteredString(fontRendererObj, StatCollector.translateToLocal("ias.username"), this.width / 2 - 130, 66, -1);
		this.drawCenteredString(fontRendererObj, StatCollector.translateToLocal("ias.password"), this.width / 2 - 130, 96, -1);
		username.drawTextBox();
		password.drawTextBox();
		super.drawScreen(par1, par2, par3);
	}

	@Override
	protected void keyTyped(char character, int keyIndex) {
		if (keyIndex == Keyboard.KEY_ESCAPE) {
			escape();
		} else if (keyIndex == Keyboard.KEY_RETURN) {
			if(username.isFocused()){
				username.setFocused(false);
				password.setFocused(true);
			}else if(password.isFocused()){
				addAccount();
			}
		} else if (keyIndex == Keyboard.KEY_BACK) {
			if (username.isFocused() && user.length() > 0) {
				user = user.substring(0, user.length() - 1);
			} else if (password.isFocused() && pass.length() > 0) {
				pass = pass.substring(0, pass.length() - 1);
				cover = cover.substring(0, cover.length() - 1);
			}
			updateText();
		} else if(keyIndex == Keyboard.KEY_TAB){
			if(username.isFocused()){
				username.setFocused(false);
				password.setFocused(true);
			}else if(password.isFocused()){
				username.setFocused(true);
				password.setFocused(false);
			}
		} else if (keyIndex == Keyboard.KEY_V && (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) || Keyboard.isKeyDown(Keyboard.KEY_RCONTROL))){
			if(username.isFocused()){
				try {
					user += (String)Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
				} catch (HeadlessException e) {
					e.printStackTrace();
				} catch (UnsupportedFlavorException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}else if(password.isFocused()){
				try {
					pass += (String)Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
					for(int i=0;i<((String)Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor)).length();i++){
						cover += '*';
					}
				} catch (HeadlessException e) {
					e.printStackTrace();
				} catch (UnsupportedFlavorException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} else if (character != 0) {
			if (username.isFocused()) {
				user += character;
			} else if (password.isFocused()) {
				pass += character;
				cover += '*';
			}
			updateText();
		}
	}

	@Override
	public void updateScreen()
	{
		this.username.updateCursorCounter();
		this.password.updateCursorCounter();
		addaccount.enabled = username.getText().length() > 0;
		updateText();
	}

	@Override
	protected void actionPerformed(GuiButton button){
		if (button.enabled)
		{
			if(button.id == 2){
				addAccount();
			}else if(button.id == 3){
				escape();
			}
		}
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
	{
		super.mouseClicked(mouseX, mouseY, mouseButton);
		username.mouseClicked(mouseX, mouseY, mouseButton);
		password.mouseClicked(mouseX, mouseY, mouseButton);
	}

	@Override
	public void onGuiClosed()
	{
		Keyboard.enableRepeatEvents(false);
	}

	/**
	 * Return to the Account Selector
	 */
	private void escape(){
		mc.displayGuiScreen(new GuiAccountSelector());
	}

	/**
	 * Add account and return to account selector
	 */
	private void addAccount(){
		AltDatabase.getInstance().getAlts().add(new AccountData(user, pass, user));
		mc.displayGuiScreen(new GuiAccountSelector());
	}

	/**
	 * Updates the username and password
	 */
	private void updateText(){
		username.setText(user);
		password.setText(cover);
	}
}
