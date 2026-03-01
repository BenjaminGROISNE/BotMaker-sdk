package com.botmaker.sdk.internal.capture.linux;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.PointerByReference;

import java.util.Arrays;
import java.util.List;

/**
 * JNA bindings for X11 library (libX11)
 * Used for window management, querying, and screen capture on Linux
 */
public interface X11 extends Library {
	X11 INSTANCE = Native.load("X11", X11.class);

	// Constants
	int None = 0;
	int AnyPropertyType = 0;
	int Success = 0;
	int InputFocus = 1;
	int RevertToParent = 2;

	int ZPixmap = 2;
	int AllPlanes = 0xFFFFFFFF;

	int IsUnmapped = 0;
	int IsUnviewable = 1;
	int IsViewable = 2;

	// Display management
	Pointer XOpenDisplay(String displayName);
	int XCloseDisplay(Pointer display);
	Pointer XDefaultRootWindow(Pointer display);
	int XDefaultScreen(Pointer display);
	Pointer XRootWindow(Pointer display, int screenNumber);
	int XFlush(Pointer display);
	int XSync(Pointer display, boolean discard);

	// Window queries
	int XQueryTree(Pointer display, Pointer window,
				   PointerByReference rootReturn,
				   PointerByReference parentReturn,
				   PointerByReference childrenReturn,
				   IntByReference nChildrenReturn);

	int XGetWindowProperty(Pointer display, Pointer window,
						   Pointer property, long longOffset, long longLength,
						   boolean delete, Pointer reqType,
						   PointerByReference actualTypeReturn,
						   IntByReference actualFormatReturn,
						   IntByReference nItemsReturn,
						   IntByReference bytesAfterReturn,
						   PointerByReference propReturn);

	// Window attributes
	int XGetWindowAttributes(Pointer display, Pointer window, XWindowAttributes attributes);

	int XGetGeometry(Pointer display, Pointer drawable,
					 PointerByReference rootReturn,
					 IntByReference xReturn, IntByReference yReturn,
					 IntByReference widthReturn, IntByReference heightReturn,
					 IntByReference borderWidthReturn,
					 IntByReference depthReturn);

	int XTranslateCoordinates(Pointer display, Pointer srcWindow, Pointer destWindow,
							  int srcX, int srcY,
							  IntByReference destXReturn, IntByReference destYReturn,
							  PointerByReference childReturn);

	// Window focus
	int XGetInputFocus(Pointer display, PointerByReference focusReturn, IntByReference revertToReturn);
	int XSetInputFocus(Pointer display, Pointer focus, int revertTo, long time);

	// Atoms
	Pointer XInternAtom(Pointer display, String atomName, boolean onlyIfExists);
	int XGetAtomName(Pointer display, Pointer atom, PointerByReference nameReturn);

	// Image capture
	Pointer XGetImage(Pointer display, Pointer drawable,
					  int x, int y,
					  int width, int height,
					  long planeMask, int format);

	int XDestroyImage(Pointer image);

	// Screen info
	int XScreenCount(Pointer display);
	int XDisplayWidth(Pointer display, int screenNumber);
	int XDisplayHeight(Pointer display, int screenNumber);

	// Memory management
	int XFree(Pointer data);

	// Utility
	int XFetchName(Pointer display, Pointer window, PointerByReference nameReturn);

	/**
	 * XWindowAttributes structure
	 */
	class XWindowAttributes extends Structure {
		public int x, y;
		public int width, height;
		public int border_width;
		public int depth;
		public Pointer visual;
		public Pointer root;
		public int c_class;
		public int bit_gravity;
		public int win_gravity;
		public int backing_store;
		public long backing_planes;
		public long backing_pixel;
		public boolean save_under;
		public Pointer colormap;
		public boolean map_installed;
		public int map_state;
		public long all_event_masks;
		public long your_event_mask;
		public long do_not_propagate_mask;
		public boolean override_redirect;
		public Pointer screen;

		@Override
		protected List<String> getFieldOrder() {
			return Arrays.asList("x", "y", "width", "height", "border_width", "depth",
				"visual", "root", "c_class", "bit_gravity", "win_gravity",
				"backing_store", "backing_planes", "backing_pixel",
				"save_under", "colormap", "map_installed", "map_state",
				"all_event_masks", "your_event_mask", "do_not_propagate_mask",
				"override_redirect", "screen");
		}
	}

	/**
	 * XImage structure (simplified)
	 */
	class XImage extends Structure {
		public int width;
		public int height;
		public int xoffset;
		public int format;
		public Pointer data;
		public int byte_order;
		public int bitmap_unit;
		public int bitmap_bit_order;
		public int bitmap_pad;
		public int depth;
		public int bytes_per_line;
		public int bits_per_pixel;
		public long red_mask;
		public long green_mask;
		public long blue_mask;
		public Pointer obdata;

		@Override
		protected List<String> getFieldOrder() {
			return Arrays.asList("width", "height", "xoffset", "format", "data",
				"byte_order", "bitmap_unit", "bitmap_bit_order",
				"bitmap_pad", "depth", "bytes_per_line", "bits_per_pixel",
				"red_mask", "green_mask", "blue_mask", "obdata");
		}

		public XImage() {
			super();
		}

		public XImage(Pointer p) {
			super(p);
			read();
		}
	}
}