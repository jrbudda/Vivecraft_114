package org.vivecraft.gui.framework;

import javax.annotation.Nullable;

import org.vivecraft.settings.VRSettings;

import net.minecraft.client.gui.GuiButton;

public abstract class GuiVROptionButton extends GuiButton
{
    @Nullable
    protected final VRSettings.VrOptions enumOptions;

    public GuiVROptionButton(int id, int x, int y, String text)
    {
        this(id, x, y, (VRSettings.VrOptions)null, text);
    }

    public GuiVROptionButton(int id, int x, int y, @Nullable VRSettings.VrOptions option, String text)
    {
        this(id, x, y, 150, 20, option, text);
    }

    public GuiVROptionButton(int id, int x, int y, int width, int height, @Nullable VRSettings.VrOptions option, String text)
    {
        super(id, x, y, width, height, text);
        this.enumOptions = option;
    }

    @Nullable
    public VRSettings.VrOptions getOption()
    {
        return this.enumOptions;
    }
    
    public String[] getToolTip() {
    	if(this.enumOptions == null) {
    		return null;
    	}
    	return this.enumOptions.getToolTip();
    }
}
