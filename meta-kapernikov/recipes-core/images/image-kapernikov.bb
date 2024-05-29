SUMMARY = "Kapernikov image"

IMAGE_FEATURES += "splash ssh-server-openssh read-only-rootfs overlayfs-etc"

DEPENDS:append = " grub-native"
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
    mkdir -p "${IMAGE_ROOTFS}/data"
}


do_image_wic[depfiles] += "${FILE_DIRNAME}/image-kapernikov/grub.cfg"

GRUB_ENV ?= "${S}/grub.env"


do_before_wic() {
    grub-editenv ${GRUB_ENV} create
    grub-editenv ${GRUB_ENV} set rootfs="B"
    grub-editenv ${GRUB_ENV} set ustate="0"
}



IMAGE_EFI_BOOT_FILES:append = " ${GRUB_ENV};EFI/BOOT/grub.env"

addtask create_data_dir before do_rootfs
addtask before_wic before do_image_wic after do_rootfs
WKS_FILE = "kpv.wks.in"