package org.vivecraft.control;

import net.minecraft.client.settings.KeyBinding;

public enum VRInputActionSet {
	GLOBAL("/actions/global", "Global", "leftright"),
	INGAME("/actions/ingame", "In-Game", "leftright"),
	GUI("/actions/gui", "GUI", "leftright"),
	KEYBOARD("/actions/keyboard", "Keyboard", "single"),
	CLIMBEY("/actions/climbey", "Climbey Motion", "single"),
	MIXED_REALITY("/actions/mixedreality", "Mixed Reality", "single");

	public final String name;
	public final String localizedName;
	public final String usage;

	VRInputActionSet(String name, String localizedName, String usage) {
		this.name = name;
		this.localizedName = localizedName;
		this.usage = usage;
	}

	public static VRInputActionSet fromKeyBinding(KeyBinding keyBinding) {
		switch (keyBinding.getKeyCategory()) {
			case "Vivecraft GUI":
				return GUI;
			case "Vivecraft Climbey":
				return CLIMBEY;
			case "Vivecraft Keyboard":
				return KEYBOARD;
			default:
				return INGAME;
		}
	}
}
