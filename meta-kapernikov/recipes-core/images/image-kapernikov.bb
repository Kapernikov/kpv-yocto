SUMMARY = "Kapernikov image"

IMAGE_FEATURES += "splash ssh-server-openssh package-management"

IMAGE_INSTALL = "\
    packagegroup-core-boot \
    packagegroup-core-full-cmdline \
    ${CORE_IMAGE_EXTRA_INSTALL} \
    "

MYCOUNTER = "6"

inherit core-image

WKS_FILE = "kpv.wks.in"