

addtask set_cgroupfs_config after do_compile


do_set_cgroupfs_config () {
    echo "cgroup_manager = \"cgroupfs\"" >> ${D}/etc/containers/libpod.conf
    echo "runtime = \"/usr/bin/crun\"" >> ${D}/etc/containers/libpod.conf
}

