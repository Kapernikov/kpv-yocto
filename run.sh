sudo chmod -R og+rwX output
cd output/qemux86-64/
qemu-system-x86_64 -kernel bzImage -drive file=core-image-full-cmdline-qemux86-64.rootfs-20240515184528.ext4,format=raw -enable-kvm -m 1024 -net nic -net user -append "root=/dev/sda rw console=ttyS0" -nographic
