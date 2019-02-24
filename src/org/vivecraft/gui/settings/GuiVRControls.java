/**
 * Copyright 2013 Mark Browning, StellaArtois
 * Licensed under the LGPL 3.0 or later (See LICENSE.md for details)
 */
package org.vivecraft.gui.settings;

import java.util.Set;

import org.vivecraft.control.ButtonTuple;
import org.vivecraft.control.ButtonType;
import org.vivecraft.control.ControllerType;
import org.vivecraft.control.TrackedControllerVive;
import org.vivecraft.control.TrackedControllerVive.TouchpadMode;
import org.vivecraft.control.VRButtonMapping;
import org.vivecraft.gui.framework.GuiVROptionsBase;
import org.vivecraft.provider.MCOpenVR;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.TextFormatting;

public class GuiVRControls extends GuiVROptionsBase {

	public VRButtonMapping mapping; 
	public Set<ButtonTuple> mappingButtons;
	public boolean selectionMode = false;
	public boolean pressMode = false;
	public boolean guiFilter = false;
	private boolean waitingForKey = false;
	private boolean keyboardHoldSelect = false;
	private boolean keyboardHold = false;
    
	private GuiVRControlsList guiList;
	private GuiKeyBindingSelection guiSelection;
    private GuiButton btnAddKey;
    private GuiButton btnKeyboardPress;
    private GuiButton btnKeyboardHold;
    private GuiButton btnLeftTouchpadMode;
    private GuiButton btnRightTouchpadMode;
	private GuiButton btnClearBinding;

	public GuiVRControls(GuiScreen par1GuiScreen) {
		super(par1GuiScreen);
	}

	@Override
	public boolean keyPressed(int key, int scanCode, int mods)
    {
    	
        if (waitingForKey)
        {
			String function = "keyboard";
			if (keyboardHold)
				function += "-hold_";
			else
				function += "-press_";
			function += key;

			if (!mc.vrSettings.buttonMappings.containsKey(function)) {
				mc.vrSettings.buttonMappings.put(function, new VRButtonMapping(function));
				guiList.buildList();
			}

			waitingForKey = false;
        	return true;
        }
        else
        {
        	return super.keyPressed(key, scanCode, mods);
        }
    }
   
	@Override
    public void initGui() {
        title = "VR Control Remapping";

        this.buttons.clear();
        this.eventListeners.clear();
        
    	this.guiList = new GuiVRControlsList(this, mc);
    	this.guiSelection = new GuiKeyBindingSelection(this);
        btnAddKey = (new GuiButton(100, this.width / 2 - 171, 16, 100, 20, "Add Keyboard Key") {
        	@Override
        	public void onClick(double mouseX, double mouseY) {
        		GuiVRControls.this.keyboardHoldSelect = true;
        	}
        });
        btnKeyboardPress = (new GuiButton(101, this.width / 2 - 105, this.height / 2,100,20, "Press") {
        	@Override
        	public void onClick(double mouseX, double mouseY) {
        		GuiVRControls.this.keyboardHold = false;
        		GuiVRControls.this.waitingForKey = true;
        		GuiVRControls.this.keyboardHoldSelect = false;
        	}
        });
        btnKeyboardHold = (new GuiButton(102, this.width / 2 + 5, this.height / 2,100,20, "Hold") {
        	@Override
        	public void onClick(double mouseX, double mouseY) {
        		GuiVRControls.this.keyboardHold = true;
        		GuiVRControls.this.waitingForKey = true;
        		GuiVRControls.this.keyboardHoldSelect = false;
        	}
        });
        btnLeftTouchpadMode = (new GuiButton(103, this.width / 2 - 171, 38, 170, 20, "") {
        	@Override
        	public void onClick(double mouseX, double mouseY) {
            	if (MCOpenVR.isVive()) {
            		TrackedControllerVive controller = (TrackedControllerVive)ControllerType.LEFT.getController();
            		TouchpadMode mode = controller.getTouchpadMode();
            		if (mode.ordinal() == TouchpadMode.values().length - 1)
            			mode = TouchpadMode.values()[0];
            		else mode = TouchpadMode.values()[mode.ordinal() + 1];
            		controller.setTouchpadMode(mode);
            		GuiVRControls.this.settings.leftTouchpadMode = mode;
            		GuiVRControls.this.settings.saveOptions();
            		GuiVRControls.this.guiSelection = new GuiKeyBindingSelection(GuiVRControls.this);
            	}
        	}
        });
        btnRightTouchpadMode = (new GuiButton(104, this.width / 2 + 1, 38, 170, 20, "") {
        	@Override
        	public void onClick(double mouseX, double mouseY) {
            	if (MCOpenVR.isVive()) {
            		TrackedControllerVive controller = (TrackedControllerVive)ControllerType.RIGHT.getController();
            		TouchpadMode mode = controller.getTouchpadMode();
            		if (mode.ordinal() == TouchpadMode.values().length - 1)
            			mode = TouchpadMode.values()[0];
            		else mode = TouchpadMode.values()[mode.ordinal() + 1];
            		controller.setTouchpadMode(mode);
            		GuiVRControls.this.settings.rightTouchpadMode = mode;
            		GuiVRControls.this.settings.saveOptions();
            		GuiVRControls.this.guiSelection = new GuiKeyBindingSelection(GuiVRControls.this);
            	}     		
        	}
        });
		btnClearBinding = new GuiButton(105, this.width / 2 - 40, 38, 80, 20, "Clear All") {
			@Override
			public void onClick(double mouseX, double mouseY) {
				if (mapping != null)
					mappingButtons.removeIf(tuple -> tuple.controller.getController().isButtonActive(tuple.button));
			}
		};

        this.addButton(btnAddKey);
        this.addButton(btnKeyboardPress);
        this.addButton(btnKeyboardHold);
        if (MCOpenVR.isVive()) {
            this.addButton(btnLeftTouchpadMode);
            this.addButton(btnRightTouchpadMode);
        }
		this.addButton(btnClearBinding);
        super.addDefaultButtons();
    }

	@Override
	public void tick() {
		this.visibleList = null;
    	if(waitingForKey){
    		title = "Press keyboard key...";
    		btnAddKey.visible = false;
    		btnKeyboardPress.visible = false;
    		btnKeyboardHold.visible = false;
    		btnLeftTouchpadMode.visible = false;
    		btnRightTouchpadMode.visible = false;
			btnClearBinding.visible = false;
    	}else {
    		if(this.selectionMode && this.mapping != null){
    			btnAddKey.visible = false;
        		btnKeyboardPress.visible = false;
        		btnKeyboardHold.visible = false;
        		btnLeftTouchpadMode.visible = false;
        		btnRightTouchpadMode.visible = false;
				btnClearBinding.visible = true;
    			title = "Choose buttons for " + this.mapping.toReadableString();
    			this.visibleList = guiSelection;
    		}
    		else if (this.keyboardHoldSelect) {
    			btnAddKey.visible = false;
        		btnKeyboardPress.visible = true;
        		btnKeyboardHold.visible = true;
        		btnLeftTouchpadMode.visible = false;
        		btnRightTouchpadMode.visible = false;
				btnClearBinding.visible = false;
    			title = "Choose keyboard key mode";
    		}
    		else{
    			btnAddKey.visible = true;
        		btnKeyboardPress.visible = false;
        		btnKeyboardHold.visible = false;
        		btnLeftTouchpadMode.visible = true;
        		btnRightTouchpadMode.visible = true;
				btnClearBinding.visible = false;
    			if (MCOpenVR.isVive()) {
    				btnLeftTouchpadMode.displayString = "Left TP: " + ((TrackedControllerVive)ControllerType.LEFT.getController()).getTouchpadMode().friendlyName;
    				btnRightTouchpadMode.displayString = "Right TP: " + ((TrackedControllerVive)ControllerType.RIGHT.getController()).getTouchpadMode().friendlyName;
    			}
    			this.selectionMode = false;
    			title = "VR Control Remapping";
    			this.visibleList = guiList;
    			if (this.guiFilter) {
        			title = "VR GUI Control Remapping";
        			btnAddKey.visible = false;
    			}
    		}
    	}
	}

	@Override
	public void render(int mouseX, int mouseY, float partialTicks) {
		super.render(mouseX, mouseY, partialTicks);
		if (this.guiFilter)
			this.drawCenteredString(this.fontRenderer, TextFormatting.RED + "Changing these wrongly can break GUI controller input. Tread carefully.", this.width / 2, 28, 16777215);
	}

	@Override
    protected void loadDefaults() {
    	if (this.selectionMode && this.mapping != null) { //
    		for (ControllerType controller : ControllerType.values()) {
				for (ButtonType button : controller.getController().getActiveButtons()) {
					mappingButtons.remove(new ButtonTuple(button, controller));
				}
			}
    	} else {
    		this.settings.leftTouchpadMode = TouchpadMode.SPLIT_UD;
    		this.settings.rightTouchpadMode = TouchpadMode.SINGLE;
    		if (MCOpenVR.isVive()) {
    			((TrackedControllerVive)ControllerType.LEFT.getController()).setTouchpadMode(TouchpadMode.SPLIT_UD);
    			((TrackedControllerVive)ControllerType.RIGHT.getController()).setTouchpadMode(TouchpadMode.SINGLE);
    		}
    		this.settings.resetBindings();
        	this.guiList.buildList();
        	this.guiSelection = new GuiKeyBindingSelection(this);
    	}
    }
    
    @Override
    protected boolean onDoneClicked() {
		if (this.selectionMode && this.mapping != null) { //done in binding list
			if (this.mapping.functionId.equals("GUI Left Click")) { // Gross mess to stop people screwing themselves
				boolean bound = false;
				outer: for (ControllerType controller : ControllerType.values()) {
					for (ButtonType b : controller.getController().getActiveButtons()) {
						if (mappingButtons.contains(new ButtonTuple(b, controller))) {
							bound = true;
							break outer;
						}
					}
				}
				if (!bound) return true;
			}
			this.mapping.buttons.clear();
			this.mapping.buttons.addAll(mappingButtons);
			this.mappingButtons = null;
			this.selectionMode = false;
			return true;
		} else if (keyboardHoldSelect) {
			this.keyboardHoldSelect = false;
			return true;
		} else { //done for whole thing
			return false;
		}
    }
    
    @Override
    protected void actionPerformed(GuiButton button) {
    	if (this.pressMode) return; //uhh
    }
          
    public void bindSingleButton(ButtonTuple button) {
    	if (this.pressMode && this.mapping != null) {
    		for (ControllerType controller : ControllerType.values()) {
				for (ButtonType buttonType : controller.getController().getActiveButtons()) {
					mapping.buttons.remove(new ButtonTuple(buttonType, controller));
				}
			}
    		mapping.buttons.add(button);
    		this.pressMode = false;
    		this.mapping = null;
    		this.mappingButtons = null;
    	}
    }   
}
