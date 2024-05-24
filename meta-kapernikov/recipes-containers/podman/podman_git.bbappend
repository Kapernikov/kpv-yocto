

addtask set_cgroupfs_config after do_install before do_package

KAPERNIKOV_BBAPPEND_DONE = "myvalue3"

do_set_cgroupfs_config () {
    echo "debug: setting cgroupfs config ${KAPERNIKOV_BBAPPEND_DONE}"
	mkdir -p ${D}/etc/containers
	touch ${D}/etc/containers/kapernikov_bbappend_done
    echo "cgroup_manager = \"cgroupfs\"" >> ${D}/etc/containers/libpod.conf
    echo "runtime = \"/usr/bin/crun\"" >> ${D}/etc/containers/libpod.conf
}
