package org.vivecraft.utils;

import net.minecraft.init.Items;
import net.minecraft.init.PotionTypes;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.TextComponentString;

public class ASMDelegator {
	public static boolean containerCreativeMouseDown(int eatTheStack) {
		//return Mouse.isButtonDown(0) || GuiScreen.mouseDown;
		return false;
	}

	public static void addCreativeItems(ItemGroup tab, NonNullList<ItemStack> list) {
		if (tab == ItemGroup.FOOD || tab == null) {
			ItemStack eatMe = new ItemStack(Items.PUMPKIN_PIE).setDisplayName(new TextComponentString("EAT ME"));
			ItemStack drinkMe = PotionUtils.addPotionToItemStack(new ItemStack(Items.POTION), PotionTypes.WATER).setDisplayName(new TextComponentString("DRINK ME"));
			list.add(eatMe);
			list.add(drinkMe);
		}
		if (tab == ItemGroup.TOOLS || tab == null) {
			ItemStack jumpBoots = new ItemStack(Items.LEATHER_BOOTS).setDisplayName(new TextComponentString("Jump Boots"));
			jumpBoots.getTag().setBoolean("Unbreakable", true);
			jumpBoots.getTag().setInteger("HideFlags", 4);
			ItemStack climbClaws = new ItemStack(Items.SHEARS).setDisplayName(new TextComponentString("Climb Claws"));
			climbClaws.getTag().setBoolean("Unbreakable", true);
			climbClaws.getTag().setInteger("HideFlags", 4);
			list.add(jumpBoots);
			list.add(climbClaws);
		}
	}

	public static void addCreativeSearch(String query, NonNullList<ItemStack> list) {
		NonNullList<ItemStack> myList = NonNullList.create();
		addCreativeItems(null, myList);
		for (ItemStack stack : myList) {
			if (query.isEmpty() || stack.getDisplayName().toString().toLowerCase().contains(query.toLowerCase()))
				list.add(stack);
		}
	}
	
	public static void dummy(float f) {
		// does nothing
	}
}
