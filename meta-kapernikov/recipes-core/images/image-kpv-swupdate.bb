DESCRIPTION = "SWUpdate image for kapernikov"

LICENSE = "MIT"

inherit swupdate

SRC_URI = "\
    file://emmcsetup.lua \
    file://sw-description \
"

# images to build before building swupdate image
IMAGE_DEPENDS = "image-kapernikov"

# images and files that will be included in the .swu image
SWUPDATE_IMAGES = "image-kapernikov"

SWUPDATE_IMAGES_FSTYPES[core-image-full-cmdline] = ".ext4.gz"