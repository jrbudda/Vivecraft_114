package org.vivecraft.control;

import net.minecraft.client.settings.KeyBinding;

public enum VRInputActionSet {
	GLOBAL("/actions/global", "Global", "leftright", false),
	INGAME("/actions/ingame", "In-Game", "leftright", false),
	GUI("/actions/gui", "GUI", "leftright", false),
	KEYBOARD("/actions/keyboard", "Keyboard", "single", true),
	CLIMBEY("/actions/climbey", "Climbey Motion", "single", true),
	MIXED_REALITY("/actions/mixedreality", "Mixed Reality", "single", true);

	public final String name;
	public final String localizedName;
	public final String usage;
	public final boolean advanced;
	
	VRInputActionSet(String name, String localizedName, String usage, boolean advanced) {
		this.name = name;
		this.localizedName = localizedName;
		this.usage = usage;
		this.advanced = advanced;
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
