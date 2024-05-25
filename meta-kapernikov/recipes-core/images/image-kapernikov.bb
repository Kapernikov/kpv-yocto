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

MYCOUNTER = "9"

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


addtask create_data_dir before do_rootfs
addtask reconfigure_podman after do_rootfs before do_image

WKS_FILE = "kpv.wks.in"