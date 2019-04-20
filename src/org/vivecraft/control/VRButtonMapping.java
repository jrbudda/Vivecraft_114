package org.vivecraft.control;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.vivecraft.provider.MCOpenVR;
import org.vivecraft.utils.InputSimulator;
import org.vivecraft.utils.MCReflection;

import com.google.common.base.Joiner;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings;
import net.optifine.reflect.Reflector;
import org.lwjgl.glfw.GLFW;

public class VRButtonMapping implements Comparable<VRButtonMapping> {
	public final String functionId;
	public final String functionDesc;
	public final int functionExt;
	public KeyBinding keyBinding;
	public Set<ButtonTuple> buttons;
	public int modifiers;
	protected int unpress;
	protected boolean pressed;
	private int priority = 0;
	private ArrayList<KeyListener> listeners=new ArrayList<>();
	
	public VRButtonMapping(String functionId, ButtonTuple... buttons) {
		this.functionId = functionId;
		String[] split = functionId.split("_");
		if (split.length == 1 || !functionId.startsWith("keyboard")) {
			this.functionDesc = functionId;
            this.functionExt = -1;
        } else {
        	this.functionDesc = split[0];
            this.functionExt = Integer.parseInt(split[1]);
        }
		this.buttons = new HashSet<>(Arrays.asList(buttons));
	}
	
	public VRButtonMapping(String functionDesc, int functionExt, ButtonTuple... buttons) {
		this.functionId = functionDesc + (functionExt != 0 ? "_" + functionExt : "");
		this.functionDesc = functionDesc;
		this.functionExt = functionExt;
		this.buttons = new HashSet<>(Arrays.asList(buttons));
	}
	
	@Override
	public String toString() {
		return "vrmapping_" + functionId + ":" + (!buttons.isEmpty() ? (modifiers != 0 ? "mods_" + modifiers + "," : "") + Joiner.on(',').join(buttons) : "none");
	}

	public String toReadableString() {
		if (this.keyBinding != null)
			return I18n.format(this.keyBinding.getKeyDescription());
		if (this.functionExt != -1) {
			if (functionDesc.contains("-hold"))
				return "Keyboard (Hold) " + InputMappings.getInputByCode(functionExt, 0).getName();
			if (functionDesc.contains("-press"))
				return "Keyboard (Press) " + InputMappings.getInputByCode(functionExt, 0).getName();
		}
		return this.functionId;
	}
	
	public int getPriority(){
		int prio=priority;
		if (isGUIBinding() || isKeyboardBinding())
			prio+=50;
		return prio;
	}
	
	public void setPriority(int priority) {
		this.priority = priority;
	}
	
	public void registerListener(KeyListener listener){
		listeners.add(listener);
	}
	
	public void unregisterListener(KeyListener listener){
		listeners.remove(listener);
	}
	
	public boolean notifyListeners(boolean pressed){
		boolean consume=false;
		for(KeyListener listener: listeners){
			if(pressed){
				consume=consume || listener.onPressed();
			}else{
				listener.onUnpressed();
			}
		}
		return consume;
	}
	
	public boolean conflictsWith(VRButtonMapping other) {
		if (this.functionId.equals(other.functionId))
			return false;
		if (this.isGUIBinding() != other.isGUIBinding())
			return false;
		if (this.modifiers != other.modifiers && !this.isModifierBinding() && !other.isModifierBinding())
			return false;
		if(this.getPriority() != other.getPriority())
			return false;

		for (ButtonTuple button : this.buttons) {
			if (button.controller.getController().isButtonActive(button.button)) {
				if (other.buttons.contains(button))
					return true;
			}
		}

		return false;
	}

	public boolean conflictsWith(Set<ButtonTuple> otherButtons, int otherModifiers, boolean otherIsModifier) {
		if (this.modifiers != otherModifiers && !this.isModifierBinding() && !otherIsModifier)
			return false;

		for (ButtonTuple button : this.buttons) {
			if (button.controller.getController().isButtonActive(button.button)) {
				if (otherButtons.contains(button))
					return true;
			}
		}

		return false;
	}

	@Override
	public int compareTo(VRButtonMapping other) {
		if (keyBinding != null && other.keyBinding != null)
			return keyBinding.compareTo(other.keyBinding);
		if (isKeyboardBinding() && !other.isKeyboardBinding())
			return 1;
		if (!isKeyboardBinding() && other.isKeyboardBinding())
			return -1;
		if (keyBinding != null)
			return I18n.format(keyBinding.getKeyDescription()).compareTo(other.functionId);
		if (other.keyBinding != null)
			return functionId.compareTo(I18n.format(other.keyBinding.getKeyDescription()));
		return functionId.compareTo(other.functionId);
	}
	
	public boolean isGUIBinding() {
		if (keyBinding == Minecraft.getMinecraft().gameSettings.keyBindInventory)
			return Minecraft.getMinecraft().currentScreen instanceof GuiContainer; // dirty hack
		return keyBinding != null && keyBinding.getKeyCategory().startsWith("Vivecraft") && keyBinding.getKeyDescription().startsWith("GUI");
	}
	
	public boolean isKeyboardBinding() {
		return functionExt != -1 && keyBinding == null;
	}

	public boolean isModifierBinding() {
		return keyBinding != null && keyBinding.getKeyCategory().startsWith("Vivecraft") && keyBinding.getKeyDescription().startsWith("Modifier");
	}

	public boolean hasModifier(int modifier) {
		return (modifiers & (1 << modifier)) != 0;
	}
	
	public void tick() {
		if (this.unpress > 0) {
			this.unpress -= 1;
			if(this.unpress <= 0)
				actuallyUnpress();
		}
	}
	
	public boolean isPressed() {
		return this.pressed;
	}
	
	
	/**
	 * Called when one of the buttons is pressed.
	 * Returns true if the event was consumed or false if it can propagate to other mappings
	 * */
	public boolean press(){
		this.unpress = 0;
		if (this.pressed) return false;
		
		boolean consume=false;
		if(Minecraft.getMinecraft().currentScreen != null && (isGUIBinding() || isKeyboardBinding())){
			consume=true;
		}
		if (keyBinding != null) {
			pressKey(keyBinding);
			this.pressed = true;
			consume=consume || notifyListeners(true);
			return consume;
		}
		if (functionExt != -1){
			if (functionDesc.contains("-hold")) {
				InputSimulator.pressKey(functionExt);
			} else if (functionDesc.contains("-press")) {
				InputSimulator.pressKey(functionExt);
				InputSimulator.releaseKey(functionExt);
			}
			this.pressed = true;
			consume=consume || notifyListeners(true);
			return consume;
		}
		
		return false;
	}
	
	public void scheduleUnpress(int unpressInTicks){
		this.unpress = unpressInTicks;
	}
	
	public void actuallyUnpress() {
		if (!this.pressed) return;
		this.pressed = false;
		notifyListeners(false);
		if (keyBinding != null) {
			unpressKey(keyBinding);
			return;
		}
		if (functionExt != -1){
			if (functionDesc.contains("-hold")){
				InputSimulator.releaseKey(functionExt);
			} else if (functionDesc.contains("-press")) {
				//nothing
			}
			return;
		}
	}

    public static void setKeyBindState(KeyBinding kb, boolean pressed) {
        if (kb != null) {
			MCReflection.KeyBinding_pressed.set(kb, pressed); //kb.pressed = pressed;
			MCReflection.KeyBinding_pressTime.set(kb, (Integer)MCReflection.KeyBinding_pressTime.get(kb) + 1); //++kb.pressTime;
        }       
    }
    
    public static void pressKey(KeyBinding kb) {
		InputMappings.Input input = (InputMappings.Input)MCReflection.KeyBinding_keyCode.get(kb);

		if (input.getKeyCode() != GLFW.GLFW_KEY_UNKNOWN && !MCOpenVR.isVivecraftBinding(kb) && (!Reflector.forgeExists() || Reflector.call(kb, Reflector.ForgeKeyBinding_getKeyModifier) == Reflector.getFieldValue(Reflector.KeyModifier_NONE))) {
			if (input.getType() == InputMappings.Type.KEYSYM) {
				//System.out.println("InputSimulator pressKey: " + kb.getKeyDescription() + ", input type: " + input.getType().name() + ", key code: " + input.getKeyCode());
				InputSimulator.pressKey(input.getKeyCode());
				return;
			} else if (input.getType() == InputMappings.Type.MOUSE) {
				//System.out.println("InputSimulator pressMouse: " + kb.getKeyDescription() + ", input type: " + input.getType().name() + ", key code: " + input.getKeyCode());
				InputSimulator.pressMouse(input.getKeyCode());
				return;
			}
		}

		// If all else fails, just press the binding directly
		//System.out.println("setKeyBindState true: " + kb.getKeyDescription() + ", input type: " + input.getType().name() + ", key code: " + input.getKeyCode());
		setKeyBindState(kb, true);
    }
    
    public static void unpressKey(KeyBinding kb) {
		InputMappings.Input input = (InputMappings.Input)MCReflection.KeyBinding_keyCode.get(kb);

		if (input.getKeyCode() != GLFW.GLFW_KEY_UNKNOWN && !MCOpenVR.isVivecraftBinding(kb) && (!Reflector.forgeExists() || Reflector.call(kb, Reflector.ForgeKeyBinding_getKeyModifier) == Reflector.getFieldValue(Reflector.KeyModifier_NONE))) {
			if (input.getType() == InputMappings.Type.KEYSYM) {
				//System.out.println("InputSimulator releaseKey: " + kb.getKeyDescription() + ", input type: " + input.getType().name() + ", key code: " + input.getKeyCode());
				InputSimulator.releaseKey(input.getKeyCode());
				return;
			} else if (input.getType() == InputMappings.Type.MOUSE) {
				//System.out.println("InputSimulator releaseMouse: " + kb.getKeyDescription() + ", input type: " + input.getType().name() + ", key code: " + input.getKeyCode());
				InputSimulator.releaseMouse(input.getKeyCode());
				return;
			}
		}

    	// If all else fails, just press the binding directly
		//System.out.println("unpressKey: " + kb.getKeyDescription() + ", input type: " + input.getType().name() + ", key code: " + input.getKeyCode());
		MCReflection.KeyBinding_unpressKey.invoke(kb);
    }
	
	public interface KeyListener{
		public boolean onPressed();
		public void onUnpressed();
	}
}
