package org.vivecraft.gui.framework;

import java.util.ArrayList;
import java.util.List;

import org.vivecraft.gui.framework.VROptionLayout.Position;
import org.vivecraft.settings.VRSettings;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiListExtended;
import net.minecraft.client.gui.GuiOptionSlider;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiVideoSettings;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.math.Vec2f;
import net.optifine.gui.TooltipManager;
import org.lwjgl.glfw.GLFW;

public abstract class GuiVROptionsBase extends GuiScreen
{
	public static final int DONE_BUTTON = 200;
	public static final int DEFAULTS_BUTTON = 201;

	protected final GuiScreen lastScreen;
	protected final VRSettings settings;
	protected String title = "Generic Title";
	private TooltipManager tooltipManager = new TooltipManager(this, new TooltipProviderVROptions());

	protected boolean reinit;
	protected boolean drawDefaultButtons = true;
	protected GuiListExtended visibleList = null;
	private int nextButtonIndex = 0;

	private GuiButton btnDone;
	private GuiButton btnDefaults;

	public GuiVROptionsBase(GuiScreen lastScreen)
	{
		this.lastScreen = lastScreen;
		this.settings = Minecraft.getMinecraft().vrSettings;
	}

	protected void addDefaultButtons() {
		this.addButton(btnDone = new GuiButton(DONE_BUTTON, this.width / 2 + 5, this.height / 6 + 168, 150, 20, I18n.format("gui.back"))
		{
			public void onClick(double mouseX, double mouseY)
			{
				if (!GuiVROptionsBase.this.onDoneClicked()) {
					GuiVROptionsBase.this.mc.vrSettings.saveOptions();
					GuiVROptionsBase.this.mc.displayGuiScreen(GuiVROptionsBase.this.lastScreen);
				}
			}
		});
		this.addButton(btnDefaults = new GuiButton(DEFAULTS_BUTTON, this.width / 2 - 155, this.height / 6 + 168, 150, 20, "Load Defaults")
		{
			public void onClick(double mouseX, double mouseY)
			{
				GuiVROptionsBase.this.loadDefaults();
				GuiVROptionsBase.this.mc.vrSettings.saveOptions();
				GuiVROptionsBase.this.reinit = true;
			}
		});
	}

	protected boolean onDoneClicked() {
		return false;
	}

	/**
	 * Adds the buttons (and other controls) to the screen in question. Called when the GUI is displayed and when the
	 * window resizes, the buttonList is cleared beforehand.
	 */
	protected void initGui(VROptionLayout[] settings, boolean clear)
	{ // specify options and layout
		
		if (clear) {
	        this.buttons.clear();
	        this.eventListeners.clear();
		}	
		
		int i = 0;
		for (VROptionLayout layout : settings)
		{
			if (layout.getOption() !=null && layout.getOption().getEnumFloat())
			{ // Option Slider
				this.addButton(new GuiVROptionSlider(layout.getOrdinal(), layout.getX(this.width),layout.getY(this.height), layout.getOption(), layout.getOption().getValueMin(), layout.getOption().getValueMax()) {
					public void onClick(double mouseX, double mouseY) {
						if (layout.getCustomHandler() != null && layout.getCustomHandler().apply(this, new Vec2f((float)mouseX, (float)mouseY)))
							return;
						super.onClick(mouseX, mouseY);
					}
				});
			}
			else if (layout.getOption() != null)
			{ // Option Button
				this.addButton(new GuiVROptionButton(layout.getOrdinal(), layout.getX(this.width), layout.getY(this.height), layout.getOption(), layout.getButtonText())
				{
					public void onClick(double mouseX, double mouseY)
					{
						if (layout.getCustomHandler() != null && layout.getCustomHandler().apply(this, new Vec2f((float)mouseX, (float)mouseY)))
							return;
						GuiVROptionsBase.this.settings.setOptionValue(this.getOption());
						this.displayString = layout.getButtonText();
					}
				});
			}
			else if (layout.getScreen() != null)
			{ // Screen button
				this.addButton(new GuiVROptionButton(layout.getOrdinal(), layout.getX(this.width), layout.getY(this.height), layout.getButtonText()) {
					public void onClick(double mouseX, double mouseY) {
						try {
							if (layout.getCustomHandler() != null && layout.getCustomHandler().apply(this, new Vec2f((float)mouseX, (float)mouseY)))
								return;
							GuiVROptionsBase.this.settings.saveOptions();
							GuiVROptionsBase.this.mc.displayGuiScreen(layout.getScreen().getConstructor(GuiScreen.class).newInstance(GuiVROptionsBase.this));
						} catch (ReflectiveOperationException e) {
							e.printStackTrace();
						}
					}
				});
			}
			else if (layout.getCustomHandler() != null)
			{ // Custom click handler button
				this.addButton(new GuiVROptionButton(layout.getOrdinal(), layout.getX(this.width), layout.getY(this.height), layout.getButtonText()) {
					public void onClick(double mouseX, double mouseY) {
						layout.getCustomHandler().apply(this, new Vec2f((float)mouseX, (float)mouseY));
					}
				});
			}
			else { //just a button, do something with it on your own time.
				this.addButton(new GuiVROptionButton(layout.getOrdinal(), layout.getX(this.width), layout.getY(this.height), layout.getButtonText()) {
				});
			}
		}
		++i;
	}
	
	protected void loadDefaults() {
		
	}
	
	protected void initGui(VROptionEntry[] settings, boolean clear)
	{ //auto-layout a list of options.
		
		if (clear) {
	        this.buttons.clear();
	        this.eventListeners.clear();
	        this.nextButtonIndex = 0;
		}	
		
		ArrayList<VROptionLayout> layouts = new ArrayList<>();
		if (this.nextButtonIndex < this.buttons.size())
			this.nextButtonIndex = this.buttons.size();
		int j = this.nextButtonIndex;
		for (int i = 0; i < settings.length; i++) {
			Position pos = settings[i].center ? Position.POS_CENTER : (j % 2 == 0 ? Position.POS_LEFT : Position.POS_RIGHT);
			if (settings[i].center && j % 2 != 0) ++j;
			if (settings[i].option != null) {
				if (settings[i].option != VRSettings.VrOptions.DUMMY) {
					layouts.add(new VROptionLayout(settings[i].option, settings[i].customHandler, pos, (float) Math.floor(j / 2f), VROptionLayout.ENABLED, settings[i].title));
				}
			} else if (settings[i].customHandler != null) {
				layouts.add(new VROptionLayout(settings[i].customHandler, pos, (float) Math.floor(j / 2f), VROptionLayout.ENABLED, settings[i].title));
			}
			if (settings[i].center) ++j;
			++j;
		}
		this.nextButtonIndex = j;

		this.initGui(layouts.toArray(new VROptionLayout[0]), false);
	}

	protected void initGui(VRSettings.VrOptions[] settings, boolean clear) {
		VROptionEntry[] entries = new VROptionEntry[settings.length];
		for (int i = 0; i < settings.length; i++) {
			entries[i] = new VROptionEntry(settings[i]);
		}

		this.initGui(entries, clear);
	}

	public void close()
	{
		super.close();
	}

	@Override
	public void render(int mouseX, int mouseY, float partialTicks)
	{
		if (reinit) {
			this.reinit = false;
			this.initGui();
		}
		this.drawDefaultBackground();
		if (visibleList != null)
			visibleList.drawScreen(mouseX, mouseY, partialTicks);
		this.drawCenteredString(this.fontRenderer, this.title, this.width / 2, 15, 16777215);

		if (btnDefaults != null)
			btnDefaults.visible = drawDefaultButtons;
		if (btnDone != null)
			btnDone.visible = drawDefaultButtons;

		super.render(mouseX, mouseY, partialTicks);
		this.tooltipManager.drawTooltips(mouseX, mouseY, buttons);
	}
    protected void actionPerformed(GuiButton button)
    {
    }

    protected void actionPerformedRightClick(GuiButton button)
    {
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int mouseButton)
    {
    	boolean flag = super.mouseClicked(mouseX, mouseY, mouseButton);
        GuiButton guibutton = getSelectedButton((int)mouseX, (int)mouseY, this.buttons);

        if (guibutton != null && guibutton.enabled)
        {
            if (!(guibutton instanceof GuiOptionSlider))
            {
                guibutton.playPressSound(this.mc.getSoundHandler());
            }

            if (mouseButton == 0)
            {
                this.actionPerformed(guibutton);
            }
            else if (mouseButton == 1)
            {
                this.actionPerformedRightClick(guibutton);
            }
        }
        else
        {
        	if (visibleList!=null)
        		return visibleList.mouseClicked(mouseX, mouseY, mouseButton);
        }       
        return flag;

    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
    	if (visibleList != null)
    		return visibleList.mouseReleased(mouseX, mouseY, button);
    	return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
    	if (visibleList != null)
    		return visibleList.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    	return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    @Override
    public boolean mouseScrolled(double p_mouseScrolled_1_) {
    	if (visibleList != null)
    		visibleList.mouseScrolled(p_mouseScrolled_1_);
    	return super.mouseScrolled(p_mouseScrolled_1_);
    }

    private GuiButton getSelectedButton(int x, int y, List<GuiButton> listButtons)
    {
        for (int i = 0; i < listButtons.size(); ++i)
        {
            GuiButton guibutton = listButtons.get(i);

            if (guibutton.visible)
            {
                int j = GuiVideoSettings.getButtonWidth(guibutton);
                int k = GuiVideoSettings.getButtonHeight(guibutton);

                if (x >= guibutton.x && y >= guibutton.y && x < guibutton.x + j && y < guibutton.y + k)
                {
                    return guibutton;
                }
            }
        }

        return null;
    }

    @Override
    public boolean keyPressed(int key, int action, int mods)
    {
        if (key == GLFW.GLFW_KEY_ESCAPE) //esc
		{
			if(this.onDoneClicked()) {
				this.mc.vrSettings.saveOptions();
				this.mc.displayGuiScreen(GuiVROptionsBase.this.lastScreen);
			}
			return true;
        }
        else
        {
            return super.keyPressed(key, action, mods);
        }
    }
}
