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

MYCOUNTER = "10"

inherit core-image

do_create_data_dir() {
    mkdir -p "${IMAGE_ROOTFS}/data"
}

do_reconfigure_podman() {
    # reconfigure podman to use the data directory
    bbnote "Reconfiguring podman to use /data/podman ${MYCOUNTER}"
    sed -i -e "s|/var/lib/containers|/data/podman/var/lib/containers|g" ${IMAGE_ROOTFS}${sysconfdir}/containers/storage.conf
    # reconfigure /run/containers too
    sed -i -e "s|/run/containers|/data/podman/run/containers|g" ${IMAGE_ROOTFS}${sysconfdir}/containers/storage.conf
}


GRUB_ENV ?= "${S}/grub.env"


do_before_wic() {
    grub-editenv ${GRUB_ENV} create
    grub-editenv ${GRUB_ENV} set copy=1
    bbwarn "grub env created in ${GRUB_ENV}"
}


IMAGE_EFI_BOOT_FILES:append = " ${GRUB_ENV};EFI/BOOT/grub.env"

addtask create_data_dir before do_rootfs
addtask reconfigure_podman after do_rootfs before do_image
addtask before_wic before do_image_wic after do_rootfs
WKS_FILE = "kpv.wks.in"