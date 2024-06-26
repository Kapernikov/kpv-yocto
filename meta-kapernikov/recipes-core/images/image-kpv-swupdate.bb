DESCRIPTION = "SWUpdate image for kapernikov"

LICENSE = "CLOSED"

inherit swupdate

SRC_URI = "\
    file://emmcsetup.lua \
    file://sw-description \
"

# images to build before building swupdate image
IMAGE_DEPENDS = "image-kapernikov"

MYCOUNTER = "2"
# images and files that will be included in the .swu image
SWUPDATE_IMAGES = "image-kapernikov"

SWUPDATE_IMAGES_FSTYPES[image-kapernikov] = "-${MACHINE}.rootfs.ext4.gz"