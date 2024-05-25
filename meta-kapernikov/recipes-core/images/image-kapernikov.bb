SUMMARY = "Kapernikov image"

IMAGE_FEATURES += "splash ssh-server-openssh read-only-rootfs overlayfs-etc"

OVERLAYFS_ETC_MOUNT_POINT = "/data"
OVERLAYFS_ETC_DEVICE = "/dev/disk/by-label/data"
OVERLAYFS_ETC_FSTYPE ?= "btrfs"
OVERLAYFS_ETC_INIT_TEMPLATE = "${FILE_DIRNAME}/image-kapernikov/overlayfs-etc-preinit-kpv.sh.in"
IMAGE_INSTALL = "\
    packagegroup-core-boot \
    packagegroup-core-full-cmdline \
    ${CORE_IMAGE_EXTRA_INSTALL} \
    "

MYCOUNTER = "6"

inherit core-image

do_create_data_dir() {
    mkdir -p "${D}/data"
}


addtask create_data_dir before do_rootfs

WKS_FILE = "kpv.wks.in"