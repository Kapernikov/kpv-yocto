SUMMARY = "Post update health check"
DESCRIPTION = "A health check script that confirms a swupdate update has been carried out succesfully"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"


inherit systemd
inherit evaluate-in-template

BOOT_PARTITION ??= "/dev/disk/by-partlabel/bootA"
BOOTENV_FILE ??= "/boot/EFI/BOOT/grub.env"

SRC_URI = "file://post-swupdate-resetustate.in \
           file://post-swupdate-resetustate.service \
          "

S = "${WORKDIR}"



do_install() {
    install -Dm 0644 ${WORKDIR}/post-swupdate-resetustate.service  ${D}/${systemd_unitdir}/system/post-swupdate-resetustate.service
    install -Dm 0755 ${WORKDIR}/post-swupdate-resetustate ${D}/${bindir}/post-swupdate-resetustate
}

FILES:${PN} += "${bindir}/post-swupdate-resetustate"
## FILES:${PN} += ${systemd_unitdir}/system/post-swupdate-resetustate.service"


SYSTEMD_SERVICE:${PN} = "post-swupdate-resetustate.service"
SYSTEMD_AUTO_ENABLE:${PN} = "enable"
