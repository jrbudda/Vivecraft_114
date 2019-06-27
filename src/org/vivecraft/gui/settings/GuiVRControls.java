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

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.TextFormatting;

public class GuiVRControls extends GuiVROptionsBase {

	public VRButtonMapping mapping; 
	public Set<ButtonTuple> mappingButtons;
	public int mappingModifiers;
	public boolean selectionMode = false;
	public boolean pressMode = false;
	public boolean guiFilter = false;
	private boolean waitingForKey = false;
	private boolean keyboardHoldSelect = false;
	private boolean keyboardHold = false;
    
	private GuiVRControlsList guiList;
	private GuiKeyBindingSelection guiSelection;
    private Button btnAddKey;
    private Button btnKeyboardPress;
    private Button btnKeyboardHold;
    private Button btnLeftTouchpadMode;
    private Button btnRightTouchpadMode;
	private Button btnClearBinding;
	private Button btnChangeModifiers;

	public GuiVRControls(Screen par1Screen) {
		super(par1Screen);
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

			if (!minecraft.vrSettings.buttonMappings.containsKey(function)) {
				minecraft.vrSettings.buttonMappings.put(function, new VRButtonMapping(function));
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
    public void init() {
        vrTitle = "VR Control Remapping";

        this.buttons.clear();
        this.children.clear();
        
    	this.guiList = new GuiVRControlsList(this, minecraft);
    	this.guiSelection = new GuiKeyBindingSelection(this);
        btnAddKey = (new Button( this.width / 2 - 171, 16, 100, 20, "Add Keyboard Key",(p)->{
        		GuiVRControls.this.keyboardHoldSelect = true;
        }));
        btnKeyboardPress = (new Button( this.width / 2 - 105, this.height / 2,100,20, "Press",(p)->{
        		GuiVRControls.this.keyboardHold = false;
        		GuiVRControls.this.waitingForKey = true;
        		GuiVRControls.this.keyboardHoldSelect = false;
        }));
        btnKeyboardHold = (new Button( this.width / 2 + 5, this.height / 2,100,20, "Hold",(p)->{
        		GuiVRControls.this.keyboardHold = true;
        		GuiVRControls.this.waitingForKey = true;
        		GuiVRControls.this.keyboardHoldSelect = false;
        }));
        btnLeftTouchpadMode = (new Button( this.width / 2 - 171, 38, 170, 20, "",(p)->{
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
        }));
        btnRightTouchpadMode = (new Button( this.width / 2 + 1, 38, 170, 20, "",(p)->{
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
        }));
		btnClearBinding = new Button( this.width / 2 + 5, 38, 100, 20, "Clear All",(p)->{
				if (mapping != null)
					mappingButtons.removeIf(tuple -> tuple.controller.getController().isButtonActive(tuple.button));
		});
		btnChangeModifiers = new Button( this.width / 2 - 105, 38, 100, 20, "",(p)->{
				mappingModifiers++;
				if (mappingModifiers >= 1 << MCOpenVR.MODIFIER_COUNT)
					mappingModifiers = 0;
		});

        this.addButton(btnAddKey);
        this.addButton(btnKeyboardPress);
        this.addButton(btnKeyboardHold);
        if (MCOpenVR.isVive()) {
            this.addButton(btnLeftTouchpadMode);
            this.addButton(btnRightTouchpadMode);
        }
		this.addButton(btnClearBinding);
        this.addButton(btnChangeModifiers);
        super.addDefaultButtons();
    }

	@Override
	public void tick() {
		this.visibleList = null;
    	if(waitingForKey){
    		vrTitle = "Press keyboard key...";
    		btnAddKey.visible = false;
    		btnKeyboardPress.visible = false;
    		btnKeyboardHold.visible = false;
    		btnLeftTouchpadMode.visible = false;
    		btnRightTouchpadMode.visible = false;
			btnClearBinding.visible = false;
			btnChangeModifiers.visible = false;
    	}else {
    		if(this.selectionMode && this.mapping != null){
    			btnAddKey.visible = false;
        		btnKeyboardPress.visible = false;
        		btnKeyboardHold.visible = false;
        		btnLeftTouchpadMode.visible = false;
        		btnRightTouchpadMode.visible = false;
				btnClearBinding.visible = true;
				btnChangeModifiers.visible = true;
				btnChangeModifiers.active = !mapping.isModifierBinding();

				StringBuilder sb = new StringBuilder();
				for (int i = 0; i < MCOpenVR.MODIFIER_COUNT; i++) {
					if ((mappingModifiers & (1 << i)) != 0) {
						if (sb.length() == 0)
							sb.append("Modifier ");
						else
							sb.append(" + ");

						sb.append(i + 1);
					}
				}
				btnChangeModifiers.setMessage(sb.length() > 0 ? sb.toString() : "No Modifier");

    			vrTitle = "Choose buttons for " + this.mapping.toReadableString();
    			this.visibleList = guiSelection;
    		}
    		else if (this.keyboardHoldSelect) {
    			btnAddKey.visible = false;
        		btnKeyboardPress.visible = true;
        		btnKeyboardHold.visible = true;
        		btnLeftTouchpadMode.visible = false;
        		btnRightTouchpadMode.visible = false;
				btnClearBinding.visible = false;
				btnChangeModifiers.visible = false;
    			vrTitle = "Choose keyboard key mode";
    		}
    		else{
    			btnAddKey.visible = true;
        		btnKeyboardPress.visible = false;
        		btnKeyboardHold.visible = false;
        		btnLeftTouchpadMode.visible = true;
        		btnRightTouchpadMode.visible = true;
				btnClearBinding.visible = false;
				btnChangeModifiers.visible = false;
    			if (MCOpenVR.isVive()) {
    				btnLeftTouchpadMode.setMessage("Left TP: " + ((TrackedControllerVive)ControllerType.LEFT.getController()).getTouchpadMode().friendlyName);
    				btnRightTouchpadMode.setMessage("Right TP: " + ((TrackedControllerVive)ControllerType.RIGHT.getController()).getTouchpadMode().friendlyName);
    			}
    			this.selectionMode = false;
    			vrTitle = "VR Control Remapping";
    			this.visibleList = guiList;
    			if (this.guiFilter) {
        			vrTitle = "VR GUI Control Remapping";
        			btnAddKey.visible = false;
    			}
    		}
    	}
	}

	@Override
	public void render(int mouseX, int mouseY, float partialTicks) {
		super.render(mouseX, mouseY, partialTicks);
		if (this.guiFilter)
			this.drawCenteredString(this.font, TextFormatting.RED + "Changing these wrongly can break GUI controller input. Tread carefully.", this.width / 2, 28, 16777215);
	}

	@Override
    protected void loadDefaults() {
    	if (this.selectionMode && this.mapping != null) { //
    		mappingButtons.clear();
    		mappingModifiers = 0;
			VRButtonMapping defaultBinding = minecraft.vrSettings.getBindingsDefaults().get(mapping.functionId);
			if (defaultBinding != null) {
				mappingButtons.addAll(defaultBinding.buttons);
				mappingModifiers = defaultBinding.modifiers;
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
			this.mapping.modifiers = mappingModifiers;
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
    protected void actionPerformed(Widget button) {
    	if (this.pressMode) return; //uhh
    }
          
    public void bindSingleButton(ButtonTuple button) {
    	if (this.pressMode && this.mapping != null) {
			mapping.buttons.removeIf(tuple -> tuple.controller.getController().isButtonActive(tuple.button));
    		mapping.buttons.add(button);
    		mapping.modifiers = 0;
    		this.pressMode = false;
    		this.mapping = null;
    		this.mappingButtons = null;
    	}
    }   
}
