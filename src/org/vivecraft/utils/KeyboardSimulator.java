package org.vivecraft.utils;

import java.awt.AWTException;
import java.awt.Robot;

import static java.awt.event.KeyEvent.*;
import static org.lwjgl.glfw.GLFW.*;


public class KeyboardSimulator {
	public static Robot robot;

	static{
        try {
			robot = new Robot();
		} catch (AWTException e) {
			e.printStackTrace();
		}		
	}

    public static void type(CharSequence characters) {
        int length = characters.length();
        for (int i = 0; i < length; i++) {
            char character = characters.charAt(i);
            type(character);
        }
    }

    public static void press(char character){
        int[] chars = getCodes(character);
        for (int i : chars) {
        	  robot.keyPress(i);
		}
    }
    
    public static void unpress(char character){
        int[] chars = getCodes(character);
        for (int i : chars) {
        	  robot.keyRelease(i);
		}
    }
    
	public static void type(char character) {
        int[] chars = getCodes(character);
        doType(chars, 0, chars.length);
	}
    
    public static int[] getCodes(char character) {
        switch (character) {
	        case 'a': return codes(VK_A); 
	        case 'b': return codes(VK_B); 
	        case 'c': return codes(VK_C); 
	        case 'd': return codes(VK_D); 
	        case 'e': return codes(VK_E); 
	        case 'f': return codes(VK_F); 
	        case 'g': return codes(VK_G); 
	        case 'h': return codes(VK_H); 
	        case 'i': return codes(VK_I); 
	        case 'j': return codes(VK_J); 
	        case 'k': return codes(VK_K); 
	        case 'l': return codes(VK_L); 
	        case 'm': return codes(VK_M); 
	        case 'n': return codes(VK_N); 
	        case 'o': return codes(VK_O); 
	        case 'p': return codes(VK_P); 
	        case 'q': return codes(VK_Q); 
	        case 'r': return codes(VK_R); 
	        case 's': return codes(VK_S); 
	        case 't': return codes(VK_T); 
	        case 'u': return codes(VK_U); 
	        case 'v': return codes(VK_V); 
	        case 'w': return codes(VK_W); 
	        case 'x': return codes(VK_X); 
	        case 'y': return codes(VK_Y); 
	        case 'z': return codes(VK_Z); 
	        case 'A': return codes(VK_SHIFT, VK_A); 
	        case 'B': return codes(VK_SHIFT, VK_B); 
	        case 'C': return codes(VK_SHIFT, VK_C); 
	        case 'D': return codes(VK_SHIFT, VK_D); 
	        case 'E': return codes(VK_SHIFT, VK_E); 
	        case 'F': return codes(VK_SHIFT, VK_F); 
	        case 'G': return codes(VK_SHIFT, VK_G); 
	        case 'H': return codes(VK_SHIFT, VK_H); 
	        case 'I': return codes(VK_SHIFT, VK_I); 
	        case 'J': return codes(VK_SHIFT, VK_J); 
	        case 'K': return codes(VK_SHIFT, VK_K); 
	        case 'L': return codes(VK_SHIFT, VK_L); 
	        case 'M': return codes(VK_SHIFT, VK_M); 
	        case 'N': return codes(VK_SHIFT, VK_N); 
	        case 'O': return codes(VK_SHIFT, VK_O); 
	        case 'P': return codes(VK_SHIFT, VK_P); 
	        case 'Q': return codes(VK_SHIFT, VK_Q); 
	        case 'R': return codes(VK_SHIFT, VK_R); 
	        case 'S': return codes(VK_SHIFT, VK_S); 
	        case 'T': return codes(VK_SHIFT, VK_T); 
	        case 'U': return codes(VK_SHIFT, VK_U); 
	        case 'V': return codes(VK_SHIFT, VK_V); 
	        case 'W': return codes(VK_SHIFT, VK_W); 
	        case 'X': return codes(VK_SHIFT, VK_X); 
	        case 'Y': return codes(VK_SHIFT, VK_Y); 
	        case 'Z': return codes(VK_SHIFT, VK_Z); 
	        case '`': return codes(VK_BACK_QUOTE); 
	        case '0': return codes(VK_0); 
	        case '1': return codes(VK_1); 
	        case '2': return codes(VK_2); 
	        case '3': return codes(VK_3); 
	        case '4': return codes(VK_4); 
	        case '5': return codes(VK_5); 
	        case '6': return codes(VK_6); 
	        case '7': return codes(VK_7); 
	        case '8': return codes(VK_8); 
	        case '9': return codes(VK_9); 
	        case '-': return codes(VK_MINUS); 
	        case '=': return codes(VK_EQUALS); 
	        case '~': return codes(VK_SHIFT,VK_BACK_QUOTE); 
	        case '!': return codes(VK_SHIFT,VK_1); 
	        case '@': return codes(VK_SHIFT,VK_2); 
	        case '#': return codes(VK_SHIFT,VK_3); 
	        case '$': return codes(VK_SHIFT,VK_4); 
	        case '%': return codes(VK_SHIFT, VK_5); 
	        case '^': return codes(VK_SHIFT,VK_6); 
	        case '&': return codes(VK_SHIFT,VK_7); 
	        case '*': return codes(VK_SHIFT,VK_8); 
	        case '(': return codes(VK_SHIFT,VK_9); 
	        case ')': return codes(VK_SHIFT,VK_0); 
	        case '_': return codes(VK_SHIFT,VK_MINUS); 
	        case '+': return codes(VK_SHIFT,VK_EQUALS); 
	        case '\t': return codes(VK_TAB); 
	        case '\n': return codes(VK_ENTER); 
	        case '[': return codes(VK_OPEN_BRACKET); 
	        case ']': return codes(VK_CLOSE_BRACKET); 
	        case '\\': return codes(VK_BACK_SLASH); 
	        case '{': return codes(VK_SHIFT, VK_OPEN_BRACKET); 
	        case '}': return codes(VK_SHIFT, VK_CLOSE_BRACKET); 
	        case '|': return codes(VK_SHIFT, VK_BACK_SLASH); 
	        case ';': return codes(VK_SEMICOLON); 
	        case ':': return codes(VK_SHIFT,VK_SEMICOLON); 
	        case '\'': return codes(VK_QUOTE); 
	        case '"': return codes(VK_SHIFT,VK_QUOTE); 
	        case ',': return codes(VK_COMMA); 
	        case '<': return codes(VK_SHIFT, VK_COMMA); 
	        case '.': return codes(VK_PERIOD); 
	        case '>': return codes(VK_SHIFT, VK_PERIOD); 
	        case '/': return codes(VK_SLASH); 
	        case '?': return codes(VK_SHIFT, VK_SLASH); 
	        case ' ': return codes(VK_SPACE); 
        	case '\b': return codes(VK_BACK_SPACE); 
        	case '\r': return codes(VK_ENTER); 
        	default: return codes();
	        //default: throw new IllegalArgumentException("Cannot type character " + character);
        }
    }
    
    public static int[] getLWJGLCodes(char character) {
        switch (character) {
	        case 'a': return codes(GLFW_KEY_A);
	        case 'b': return codes(GLFW_KEY_B);
	        case 'c': return codes(GLFW_KEY_C);
	        case 'd': return codes(GLFW_KEY_D);
	        case 'e': return codes(GLFW_KEY_E);
	        case 'f': return codes(GLFW_KEY_F);
	        case 'g': return codes(GLFW_KEY_G);
	        case 'h': return codes(GLFW_KEY_H);
	        case 'i': return codes(GLFW_KEY_I);
	        case 'j': return codes(GLFW_KEY_J);
	        case 'k': return codes(GLFW_KEY_K);
	        case 'l': return codes(GLFW_KEY_L);
	        case 'm': return codes(GLFW_KEY_M);
	        case 'n': return codes(GLFW_KEY_N);
	        case 'o': return codes(GLFW_KEY_O);
	        case 'p': return codes(GLFW_KEY_P);
	        case 'q': return codes(GLFW_KEY_Q);
	        case 'r': return codes(GLFW_KEY_R);
	        case 's': return codes(GLFW_KEY_S);
	        case 't': return codes(GLFW_KEY_T);
	        case 'u': return codes(GLFW_KEY_U);
	        case 'v': return codes(GLFW_KEY_V);
	        case 'w': return codes(GLFW_KEY_W);
	        case 'x': return codes(GLFW_KEY_X);
	        case 'y': return codes(GLFW_KEY_Y);
	        case 'z': return codes(GLFW_KEY_Z);
	        case 'A': return codes(GLFW_KEY_LEFT_SHIFT, GLFW_KEY_A);
	        case 'B': return codes(GLFW_KEY_LEFT_SHIFT, GLFW_KEY_B);
	        case 'C': return codes(GLFW_KEY_LEFT_SHIFT, GLFW_KEY_C);
	        case 'D': return codes(GLFW_KEY_LEFT_SHIFT, GLFW_KEY_D);
	        case 'E': return codes(GLFW_KEY_LEFT_SHIFT, GLFW_KEY_E);
	        case 'F': return codes(GLFW_KEY_LEFT_SHIFT, GLFW_KEY_F);
	        case 'G': return codes(GLFW_KEY_LEFT_SHIFT, GLFW_KEY_G);
	        case 'H': return codes(GLFW_KEY_LEFT_SHIFT, GLFW_KEY_H);
	        case 'I': return codes(GLFW_KEY_LEFT_SHIFT, GLFW_KEY_I);
	        case 'J': return codes(GLFW_KEY_LEFT_SHIFT, GLFW_KEY_J);
	        case 'K': return codes(GLFW_KEY_LEFT_SHIFT, GLFW_KEY_K);
	        case 'L': return codes(GLFW_KEY_LEFT_SHIFT, GLFW_KEY_L);
	        case 'M': return codes(GLFW_KEY_LEFT_SHIFT, GLFW_KEY_M);
	        case 'N': return codes(GLFW_KEY_LEFT_SHIFT, GLFW_KEY_N);
	        case 'O': return codes(GLFW_KEY_LEFT_SHIFT, GLFW_KEY_O);
	        case 'P': return codes(GLFW_KEY_LEFT_SHIFT, GLFW_KEY_P);
	        case 'Q': return codes(GLFW_KEY_LEFT_SHIFT, GLFW_KEY_Q);
	        case 'R': return codes(GLFW_KEY_LEFT_SHIFT, GLFW_KEY_R);
	        case 'S': return codes(GLFW_KEY_LEFT_SHIFT, GLFW_KEY_S);
	        case 'T': return codes(GLFW_KEY_LEFT_SHIFT, GLFW_KEY_T);
	        case 'U': return codes(GLFW_KEY_LEFT_SHIFT, GLFW_KEY_U);
	        case 'V': return codes(GLFW_KEY_LEFT_SHIFT, GLFW_KEY_V);
	        case 'W': return codes(GLFW_KEY_LEFT_SHIFT, GLFW_KEY_W);
	        case 'X': return codes(GLFW_KEY_LEFT_SHIFT, GLFW_KEY_X);
	        case 'Y': return codes(GLFW_KEY_LEFT_SHIFT, GLFW_KEY_Y);
	        case 'Z': return codes(GLFW_KEY_LEFT_SHIFT, GLFW_KEY_Z);
	        case '`': return codes(GLFW_KEY_GRAVE_ACCENT);
	        case '0': return codes(GLFW_KEY_0);
	        case '1': return codes(GLFW_KEY_1);
	        case '2': return codes(GLFW_KEY_2);
	        case '3': return codes(GLFW_KEY_3);
	        case '4': return codes(GLFW_KEY_4);
	        case '5': return codes(GLFW_KEY_5);
	        case '6': return codes(GLFW_KEY_6);
	        case '7': return codes(GLFW_KEY_7);
	        case '8': return codes(GLFW_KEY_8);
	        case '9': return codes(GLFW_KEY_9);
	        case '-': return codes(GLFW_KEY_MINUS);
	        case '=': return codes(GLFW_KEY_EQUAL);
	        case '~': return codes(GLFW_KEY_LEFT_SHIFT, GLFW_KEY_B);
	        case '!': return codes(GLFW_KEY_LEFT_SHIFT, GLFW_KEY_1);
	        case '@': return codes(GLFW_KEY_LEFT_SHIFT, GLFW_KEY_2);
	        case '#': return codes(GLFW_KEY_LEFT_SHIFT, GLFW_KEY_3);
	        case '$': return codes(GLFW_KEY_LEFT_SHIFT, GLFW_KEY_4);
	        case '%': return codes(GLFW_KEY_LEFT_SHIFT, GLFW_KEY_5);
	        case '^': return codes(GLFW_KEY_LEFT_SHIFT, GLFW_KEY_6);
	        case '&': return codes(GLFW_KEY_LEFT_SHIFT, GLFW_KEY_7);
	        case '*': return codes(GLFW_KEY_LEFT_SHIFT, GLFW_KEY_8);
	        case '(': return codes(GLFW_KEY_LEFT_SHIFT, GLFW_KEY_9);
	        case ')': return codes(GLFW_KEY_LEFT_SHIFT, GLFW_KEY_0);
	        case '_': return codes(GLFW_KEY_LEFT_SHIFT, GLFW_KEY_MINUS);
	        case '+': return codes(GLFW_KEY_LEFT_SHIFT, GLFW_KEY_EQUAL);
	        case '\t': return codes(GLFW_KEY_TAB);
	        case '\n': return codes(GLFW_KEY_ENTER);
	        case '[': return codes(GLFW_KEY_LEFT_BRACKET);
	        case ']': return codes(GLFW_KEY_RIGHT_BRACKET);
	        case '\\': return codes(GLFW_KEY_BACKSLASH);
	        case '{': return codes(GLFW_KEY_LEFT_SHIFT, GLFW_KEY_LEFT_BRACKET);
	        case '}': return codes(GLFW_KEY_LEFT_SHIFT, GLFW_KEY_RIGHT_BRACKET);
	        case '|': return codes(GLFW_KEY_LEFT_SHIFT, GLFW_KEY_BACKSLASH);
	        case ';': return codes(GLFW_KEY_SEMICOLON);
	        case ':': return codes(GLFW_KEY_LEFT_SHIFT, GLFW_KEY_SEMICOLON);
	        case '\'': return codes(GLFW_KEY_APOSTROPHE);
	        case '"': return codes(GLFW_KEY_LEFT_SHIFT, GLFW_KEY_APOSTROPHE);
	        case ',': return codes(GLFW_KEY_COMMA);
	        case '<': return codes(GLFW_KEY_LEFT_SHIFT, GLFW_KEY_COMMA);
	        case '.': return codes(GLFW_KEY_PERIOD);
	        case '>': return codes(GLFW_KEY_LEFT_SHIFT, GLFW_KEY_PERIOD);
	        case '/': return codes(GLFW_KEY_SLASH);
	        case '?': return codes(GLFW_KEY_LEFT_SHIFT, GLFW_KEY_SLASH);
	        case ' ': return codes(GLFW_KEY_SPACE);
        	case '\b': return codes(GLFW_KEY_BACKSPACE);
        	case '\r': return codes(GLFW_KEY_ENTER);
        	default: return codes();
	        //default: throw new IllegalArgumentException("Cannot type character " + character);
        }
    }

    private static int[] codes(int... keyCodes) {
        return keyCodes;
    }

    private static void doType(int[] keyCodes, int offset, int length) {
    	try {
	        if (length == 0) {
	            return;
	        }
	        robot.keyPress(keyCodes[offset]);
	        doType(keyCodes, offset + 1, length - 1);
	        robot.keyRelease(keyCodes[offset]);
		} catch (Exception e) {
			System.out.println("Cannot type keycode: " + keyCodes[offset]);
		}
    }

	public static int translateFromAWT(int code) {
	    switch (code) {
			case VK_ESCAPE: return GLFW_KEY_ESCAPE;
			case VK_1: return GLFW_KEY_1;
			case VK_2: return GLFW_KEY_2;
			case VK_3: return GLFW_KEY_3;
			case VK_4: return GLFW_KEY_4;
			case VK_5: return GLFW_KEY_5;
			case VK_6: return GLFW_KEY_6;
			case VK_7: return GLFW_KEY_7;
			case VK_8: return GLFW_KEY_8;
			case VK_9: return GLFW_KEY_9;
			case VK_0: return GLFW_KEY_0;
			case VK_MINUS: return GLFW_KEY_MINUS;
			case VK_EQUALS: return GLFW_KEY_EQUAL;
			case VK_BACK_SPACE: return GLFW_KEY_BACKSPACE;
			case VK_TAB: return GLFW_KEY_TAB;
			case VK_Q: return GLFW_KEY_Q;
			case VK_W: return GLFW_KEY_W;
			case VK_E: return GLFW_KEY_E;
			case VK_R: return GLFW_KEY_R;
			case VK_T: return GLFW_KEY_T;
			case VK_Y: return GLFW_KEY_Y;
			case VK_U: return GLFW_KEY_U;
			case VK_I: return GLFW_KEY_I;
			case VK_O: return GLFW_KEY_O;
			case VK_P: return GLFW_KEY_P;
			case VK_OPEN_BRACKET: return GLFW_KEY_LEFT_BRACKET;
			case VK_CLOSE_BRACKET: return GLFW_KEY_RIGHT_BRACKET;
			case VK_ENTER: return GLFW_KEY_ENTER;
			case VK_CONTROL: return GLFW_KEY_LEFT_CONTROL;
			case VK_A: return GLFW_KEY_A;
			case VK_S: return GLFW_KEY_S;
			case VK_D: return GLFW_KEY_D;
			case VK_F: return GLFW_KEY_F;
			case VK_G: return GLFW_KEY_G;
			case VK_H: return GLFW_KEY_H;
			case VK_J: return GLFW_KEY_J;
			case VK_K: return GLFW_KEY_K;
			case VK_L: return GLFW_KEY_L;
			case VK_SEMICOLON: return GLFW_KEY_SEMICOLON;
			case VK_QUOTE: return GLFW_KEY_APOSTROPHE;
			case VK_DEAD_GRAVE: return GLFW_KEY_GRAVE_ACCENT;
			case VK_SHIFT: return GLFW_KEY_LEFT_SHIFT;
			case VK_BACK_SLASH: return GLFW_KEY_BACKSLASH;
			case VK_Z: return GLFW_KEY_Z;
			case VK_X: return GLFW_KEY_X;
			case VK_C: return GLFW_KEY_C;
			case VK_V: return GLFW_KEY_V;
			case VK_B: return GLFW_KEY_B;
			case VK_N: return GLFW_KEY_N;
			case VK_M: return GLFW_KEY_M;
			case VK_COMMA: return GLFW_KEY_COMMA;
			case VK_PERIOD: return GLFW_KEY_PERIOD;
			case VK_SLASH: return GLFW_KEY_SLASH;
			case VK_MULTIPLY: return GLFW_KEY_KP_MULTIPLY;
			case VK_ALT: return GLFW_KEY_LEFT_ALT;
			case VK_SPACE: return GLFW_KEY_SPACE;
			case VK_CAPS_LOCK: return GLFW_KEY_CAPS_LOCK;
			case VK_F1: return GLFW_KEY_F1;
			case VK_F2: return GLFW_KEY_F2;
			case VK_F3: return GLFW_KEY_F3;
			case VK_F4: return GLFW_KEY_F4;
			case VK_F5: return GLFW_KEY_F5;
			case VK_F6: return GLFW_KEY_F6;
			case VK_F7: return GLFW_KEY_F7;
			case VK_F8: return GLFW_KEY_F8;
			case VK_F9: return GLFW_KEY_F9;
			case VK_F10: return GLFW_KEY_F10;
			case VK_NUM_LOCK: return GLFW_KEY_NUM_LOCK;
			case VK_SCROLL_LOCK: return GLFW_KEY_SCROLL_LOCK;
			case VK_NUMPAD7: return GLFW_KEY_KP_7;
			case VK_NUMPAD8: return GLFW_KEY_KP_8;
			case VK_NUMPAD9: return GLFW_KEY_KP_9;
			case VK_SUBTRACT: return GLFW_KEY_KP_SUBTRACT;
			case VK_NUMPAD4: return GLFW_KEY_KP_4;
			case VK_NUMPAD5: return GLFW_KEY_KP_5;
			case VK_NUMPAD6: return GLFW_KEY_KP_6;
			case VK_ADD: return GLFW_KEY_KP_ADD;
			case VK_NUMPAD1: return GLFW_KEY_KP_1;
			case VK_NUMPAD2: return GLFW_KEY_KP_2;
			case VK_NUMPAD3: return GLFW_KEY_KP_3;
			case VK_NUMPAD0: return GLFW_KEY_KP_0;
			case VK_DECIMAL: return GLFW_KEY_KP_DECIMAL;
			case VK_F11: return GLFW_KEY_F11;
			case VK_F12: return GLFW_KEY_F12;
			case VK_F13: return GLFW_KEY_F13;
			case VK_F14: return GLFW_KEY_F14;
			case VK_F15: return GLFW_KEY_F15;
			//case VK_KANA: return GLFW_KEY_KANA;
			//case VK_CONVERT: return GLFW_KEY_CONVERT;
			//case VK_NONCONVERT: return GLFW_KEY_NOCONVERT;
			//case VK_CIRCUMFLEX: return GLFW_KEY_CIRCUMFLEX;
			//case VK_AT: return GLFW_KEY_AT;
			//case VK_COLON: return GLFW_KEY_COLON;
			//case VK_UNDERSCORE: return GLFW_KEY_UNDERLINE;
			//case VK_KANJI: return GLFW_KEY_KANJI;
			//case VK_STOP: return GLFW_KEY_STOP;
			case VK_DIVIDE: return GLFW_KEY_KP_DIVIDE;
			case VK_PAUSE: return GLFW_KEY_PAUSE;
			case VK_HOME: return GLFW_KEY_HOME;
			case VK_UP: return GLFW_KEY_UP;
			case VK_PAGE_UP: return GLFW_KEY_PAGE_UP;
			case VK_LEFT: return GLFW_KEY_LEFT;
			case VK_RIGHT: return GLFW_KEY_RIGHT;
			case VK_END: return GLFW_KEY_END;
			case VK_DOWN: return GLFW_KEY_DOWN;
			case VK_PAGE_DOWN: return GLFW_KEY_PAGE_DOWN;
			case VK_INSERT: return GLFW_KEY_INSERT;
			case VK_DELETE: return GLFW_KEY_DELETE;
	    }
	    return GLFW_KEY_UNKNOWN;
	}
	
	public static int translateToAWT(int code) {
	    switch (code) {
			case GLFW_KEY_ESCAPE: return VK_ESCAPE;
			case GLFW_KEY_1: return VK_1;
			case GLFW_KEY_2: return VK_2;
			case GLFW_KEY_3: return VK_3;
			case GLFW_KEY_4: return VK_4;
			case GLFW_KEY_5: return VK_5;
			case GLFW_KEY_6: return VK_6;
			case GLFW_KEY_7: return VK_7;
			case GLFW_KEY_8: return VK_8;
			case GLFW_KEY_9: return VK_9;
			case GLFW_KEY_0: return VK_0;
			case GLFW_KEY_MINUS: return VK_MINUS;
			case GLFW_KEY_EQUAL: return VK_EQUALS;
			case GLFW_KEY_BACKSPACE: return VK_BACK_SPACE;
			case GLFW_KEY_TAB: return VK_TAB;
			case GLFW_KEY_Q: return VK_Q;
			case GLFW_KEY_W: return VK_W;
			case GLFW_KEY_E: return VK_E;
			case GLFW_KEY_R: return VK_R;
			case GLFW_KEY_T: return VK_T;
			case GLFW_KEY_Y: return VK_Y;
			case GLFW_KEY_U: return VK_U;
			case GLFW_KEY_I: return VK_I;
			case GLFW_KEY_O: return VK_O;
			case GLFW_KEY_P: return VK_P;
			case GLFW_KEY_LEFT_BRACKET: return VK_OPEN_BRACKET;
			case GLFW_KEY_RIGHT_BRACKET: return VK_CLOSE_BRACKET;
			case GLFW_KEY_ENTER: return VK_ENTER;
			//case GLFW_KEY_LEFT_CONTROL: return VK_CONTROL;
			case GLFW_KEY_A: return VK_A;
			case GLFW_KEY_S: return VK_S;
			case GLFW_KEY_D: return VK_D;
			case GLFW_KEY_F: return VK_F;
			case GLFW_KEY_G: return VK_G;
			case GLFW_KEY_H: return VK_H;
			case GLFW_KEY_J: return VK_J;
			case GLFW_KEY_K: return VK_K;
			case GLFW_KEY_L: return VK_L;
			case GLFW_KEY_SEMICOLON: return VK_SEMICOLON;
			case GLFW_KEY_APOSTROPHE: return VK_QUOTE;
			case GLFW_KEY_GRAVE_ACCENT: return VK_BACK_QUOTE;
			//case GLFW_KEY_LEFT_SHIFT: return VK_SHIFT;
			case GLFW_KEY_BACKSLASH: return VK_BACK_SLASH;
			case GLFW_KEY_Z: return VK_Z;
			case GLFW_KEY_X: return VK_X;
			case GLFW_KEY_C: return VK_C;
			case GLFW_KEY_V: return VK_V;
			case GLFW_KEY_B: return VK_B;
			case GLFW_KEY_N: return VK_N;
			case GLFW_KEY_M: return VK_M;
			case GLFW_KEY_COMMA: return VK_COMMA;
			case GLFW_KEY_PERIOD: return VK_PERIOD;
			case GLFW_KEY_SLASH: return VK_SLASH;
			case GLFW_KEY_KP_MULTIPLY: return VK_MULTIPLY;
			//case GLFW_KEY_LMENU: return VK_ALT;
			case GLFW_KEY_SPACE: return VK_SPACE;
			case GLFW_KEY_CAPS_LOCK: return VK_CAPS_LOCK;
			case GLFW_KEY_F1: return VK_F1;
			case GLFW_KEY_F2: return VK_F2;
			case GLFW_KEY_F3: return VK_F3;
			case GLFW_KEY_F4: return VK_F4;
			case GLFW_KEY_F5: return VK_F5;
			case GLFW_KEY_F6: return VK_F6;
			case GLFW_KEY_F7: return VK_F7;
			case GLFW_KEY_F8: return VK_F8;
			case GLFW_KEY_F9: return VK_F9;
			case GLFW_KEY_F10: return VK_F10;
			case GLFW_KEY_NUM_LOCK: return VK_NUM_LOCK;
			case GLFW_KEY_SCROLL_LOCK: return VK_SCROLL_LOCK;
			case GLFW_KEY_KP_7: return VK_NUMPAD7;
			case GLFW_KEY_KP_8: return VK_NUMPAD8;
			case GLFW_KEY_KP_9: return VK_NUMPAD9;
			case GLFW_KEY_KP_SUBTRACT: return VK_SUBTRACT;
			case GLFW_KEY_KP_4: return VK_NUMPAD4;
			case GLFW_KEY_KP_5: return VK_NUMPAD5;
			case GLFW_KEY_KP_6: return VK_NUMPAD6;
			case GLFW_KEY_KP_ADD: return VK_ADD;
			case GLFW_KEY_KP_1: return VK_NUMPAD1;
			case GLFW_KEY_KP_2: return VK_NUMPAD2;
			case GLFW_KEY_KP_3: return VK_NUMPAD3;
			case GLFW_KEY_KP_0: return VK_NUMPAD0;
			case GLFW_KEY_KP_DECIMAL: return VK_DECIMAL;
			case GLFW_KEY_F11: return VK_F11;
			case GLFW_KEY_F12: return VK_F12;
			case GLFW_KEY_F13: return VK_F13;
			case GLFW_KEY_F14: return VK_F14;
			case GLFW_KEY_F15: return VK_F15;
			//case GLFW_KEY_KANA: return VK_KANA;
			//case GLFW_KEY_CONVERT: return VK_CONVERT;
			//case GLFW_KEY_NOCONVERT: return VK_NONCONVERT;
			//case GLFW_KEY_CIRCUMFLEX: return VK_CIRCUMFLEX;
			//case GLFW_KEY_AT: return VK_AT;
			//case GLFW_KEY_COLON: return VK_COLON;
			//case GLFW_KEY_UNDERLINE: return VK_UNDERSCORE;
			//case GLFW_KEY_KANJI: return VK_KANJI;
			//case GLFW_KEY_STOP: return VK_STOP;
			case GLFW_KEY_KP_DIVIDE: return VK_DIVIDE;
			case GLFW_KEY_PAUSE: return VK_PAUSE;
			case GLFW_KEY_HOME: return VK_HOME;
			case GLFW_KEY_UP: return VK_UP;
			case GLFW_KEY_PAGE_UP: return VK_PAGE_UP;
			case GLFW_KEY_LEFT: return VK_LEFT;
			case GLFW_KEY_RIGHT: return VK_RIGHT;
			case GLFW_KEY_END: return VK_END;
			case GLFW_KEY_DOWN: return VK_DOWN;
			case GLFW_KEY_PAGE_DOWN: return VK_PAGE_DOWN;
			case GLFW_KEY_INSERT: return VK_INSERT;
			case GLFW_KEY_DELETE: return VK_DELETE;
	    }
	    return VK_UNDEFINED;
	}

}

