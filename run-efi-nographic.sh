#!/bin/bash
qemu-system-x86_64  \
        -bios /usr/share/ovmf/OVMF.fd \
	-object rng-random,filename=/dev/urandom,id=rng0 \
	-device virtio-rng-pci,rng=rng0 \
	-drive file=build/tmp-glibc/deploy/images/qemux86-64/image-kapernikov-qemux86-64.rootfs.wic \
	 -cpu IvyBridge -smp 4 -enable-kvm \
	 -m 256 -nographic

