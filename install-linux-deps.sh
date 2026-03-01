#!/bin/bash

# Installation script for Linux screen capture dependencies
# This script installs the required X11 development libraries

set -e

echo "==================================="
echo "Linux Screen Capture Setup Script"
echo "==================================="
echo ""

# Detect Linux distribution
if [ -f /etc/os-release ]; then
    . /etc/os-release
    DISTRO=$ID
else
    echo "Error: Cannot detect Linux distribution"
    exit 1
fi

echo "Detected distribution: $DISTRO"
echo ""

# Install based on distribution
case $DISTRO in
    ubuntu|debian|pop|linuxmint)
        echo "Installing packages for Debian/Ubuntu-based system..."
        sudo apt-get update
        sudo apt-get install -y libx11-dev libxtst-dev libxext-dev
        ;;

    fedora|rhel|centos|rocky|almalinux)
        echo "Installing packages for Fedora/RHEL-based system..."
        sudo dnf install -y libX11-devel libXtst-devel libXext-devel
        ;;

    arch|manjaro)
        echo "Installing packages for Arch-based system..."
        sudo pacman -S --noconfirm libx11 libxtst libxext
        ;;

    opensuse*)
        echo "Installing packages for openSUSE..."
        sudo zypper install -y libX11-devel libXtst-devel libXext-devel
        ;;

    *)
        echo "Error: Unsupported distribution: $DISTRO"
        echo ""
        echo "Please install the following packages manually:"
        echo "  - libx11 (development files)"
        echo "  - libxtst (development files)"
        echo "  - libxext (development files)"
        exit 1
        ;;
esac

echo ""
echo "==================================="
echo "Verifying installation..."
echo "==================================="

# Verify libraries are installed
check_library() {
    if ldconfig -p | grep -q "$1"; then
        echo "✓ $1 found"
        return 0
    else
        echo "✗ $1 NOT found"
        return 1
    fi
}

ALL_FOUND=true

if ! check_library "libX11.so"; then
    ALL_FOUND=false
fi

if ! check_library "libXtst.so"; then
    ALL_FOUND=false
fi

if ! check_library "libXext.so"; then
    ALL_FOUND=false
fi

echo ""

# Check X11 environment
echo "Checking X11 environment..."
if [ -n "$DISPLAY" ]; then
    echo "✓ DISPLAY is set to: $DISPLAY"
else
    echo "✗ DISPLAY is not set"
    echo "  Set it with: export DISPLAY=:0"
    ALL_FOUND=false
fi

if [ -n "$XDG_SESSION_TYPE" ]; then
    echo "✓ Session type: $XDG_SESSION_TYPE"
    if [ "$XDG_SESSION_TYPE" = "wayland" ]; then
        echo "  ⚠ Note: Running Wayland. X11 features will use XWayland."
    fi
else
    echo "  Session type: unknown"
fi

echo ""
echo "==================================="

if $ALL_FOUND; then
    echo "✓ Installation successful!"
    echo ""
    echo "You can now run the test suite:"
    echo "  java -cp target/classes com.botmaker.sdk.internal.capture.linux.LinuxControllerTest"
else
    echo "✗ Installation incomplete"
    echo ""
    echo "Please resolve the issues above and try again."
    exit 1
fi

echo "==================================="