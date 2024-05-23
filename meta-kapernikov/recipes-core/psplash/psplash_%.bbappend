# https://dev.to/makame/customize-boot-splash-screen-in-yocto-3bip
SPLASH_IMAGES:forcevariable = "file://logo.png;outsuffix=default"
FILESEXTRAPATHS:prepend := "${THISDIR}/files:"
