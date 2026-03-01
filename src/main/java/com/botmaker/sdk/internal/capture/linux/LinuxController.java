package com.botmaker.sdk.internal.capture.linux;

import com.botmaker.sdk.internal.capture.core.GenericWindow;
import com.botmaker.sdk.internal.capture.core.NativeController;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class LinuxController implements NativeController {

	@Override
	public GenericWindow getForegroundWindow() {
		System.out.println("[Linux] getForegroundWindow is not yet implemented.");
		return new GenericWindow(-1, "Mock Linux Window", new Rectangle(0, 0, 800, 600));
	}

	@Override
	public List<GenericWindow> getChildWindows(GenericWindow parent) {
		System.out.println("[Linux] getChildWindows is not yet implemented.");
		return new ArrayList<>();
	}

	@Override
	public List<GenericWindow> getAllWindows() {
		System.out.println("[Linux] getAllWindows is not yet implemented.");
		return new ArrayList<>();
	}

	@Override
	public BufferedImage captureWindow(GenericWindow window) {
		System.out.println("[Linux] captureWindow falling back to Robot desktop capture.");
		return captureDesktop(); // Temporary fallback
	}

	@Override
	public BufferedImage captureDesktop() {
		try {
			return new Robot().createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
		} catch (AWTException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public void postLeftClick(GenericWindow window, int relativeX, int relativeY) {
		System.out.println("[Linux] postLeftClick is not yet implemented.");
	}

	@Override
	public void postLeftClickScreen(int xAbs, int yAbs) {
		try {
			Robot robot = new Robot();
			robot.mouseMove(xAbs, yAbs);
			// Simulate click
			// robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
			// robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
		} catch (AWTException e) {
			e.printStackTrace();
		}
	}
}