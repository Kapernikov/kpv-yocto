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

do_reconfigure_podman() {
    # reconfigure podman to use the data directory
    bbnote "Reconfiguring podman to use /data/podman ${MYCOUNTER}"
    sed -i -e "s|/var/lib/containers|/data/podman/var/lib/containers|g" ${IMAGE_ROOTFS}${sysconfdir}/containers/storage.conf
    # reconfigure /run/containers too
    sed -i -e "s|/run/containers|/data/podman/run/containers|g" ${IMAGE_ROOTFS}${sysconfdir}/containers/storage.conf
}

do_add_resetustate_service() {
    install -d ${IMAGE_ROOTFS}${systemd_unitdir}/system
cat << EOF > ${IMAGE_ROOTFS}${systemd_unitdir}/system/resetustate.service
[Unit]
Description=Reset ustate to 0 after the system boots
Wants=network-online.target
After=multi-user.target network-online.target

[Service]
Type=oneshot
ExecStart=/usr/bin/resetustate
RemainAfterExit=yes

[Install]
WantedBy=multi-user.target
EOF

cat << EOF > ${IMAGE_ROOTFS}/usr/bin/resetustate
#!/bin/sh
if ! mount | grep boot; then
		mount /dev/disk/by-partlabel/bootA /boot
fi
grub-editenv /boot/EFI/BOOT/grub.env set ustate=0
EOF
chmod +x ${IMAGE_ROOTFS}/usr/bin/resetustate
}

do_image_wic[depfiles] += "${FILE_DIRNAME}/image-kapernikov/grub.cfg"

GRUB_ENV ?= "${S}/grub.env"


do_before_wic() {
    grub-editenv ${GRUB_ENV} create
    grub-editenv ${GRUB_ENV} set rootfs="B"
    grub-editenv ${GRUB_ENV} set ustate="0"

    bbwarn "grub env created in ${GRUB_ENV}"
}



IMAGE_EFI_BOOT_FILES:append = " ${GRUB_ENV};EFI/BOOT/grub.env"

addtask create_data_dir before do_rootfs
addtask reconfigure_podman after do_rootfs before do_image
addtask add_resetustate_service after do_rootfs before do_image
addtask before_wic before do_image_wic after do_rootfs
WKS_FILE = "kpv.wks.in"