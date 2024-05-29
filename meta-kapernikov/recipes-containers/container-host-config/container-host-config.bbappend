

do_install:append() {
    sed -i -e "s|/var/lib/containers|/data/podman/var/lib/containers|g" ${D}${sysconfdir}/containers/storage.conf
    # reconfigure /run/containers too
    sed -i -e "s|/run/containers|/data/podman/run/containers|g" ${D}${sysconfdir}/containers/storage.conf
}
