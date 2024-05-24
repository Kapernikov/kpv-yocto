qemu-system-x86_64 -object rng-random,filename=/dev/urandom,id=rng0 -device virtio-rng-pci,rng=rng0 \
	 -drive file=/home/kervel/projects/yocto/kas/build/tmp-musl/deploy/images/qemux86-64/core-image-full-cmdline-qemux86-64.rootfs.ext4,if=virtio,format=raw -usb -device usb-tablet -usb -device usb-kbd   -cpu IvyBridge -smp 4 -enable-kvm -m 256 -serial mon:vc -serial null -device virtio-vga  -display gtk,show-cursor=on \
	 -kernel /home/kervel/projects/yocto/kas/build/tmp-musl/deploy/images/qemux86-64/bzImage -append 'root=/dev/vda rw'
