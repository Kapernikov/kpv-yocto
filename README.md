# Yocto experiment

* [x] readonly rootFS with an overlay on `/etc/`
* [x] poky-based distro based on glibc (trivially changeable to musl by editing [the distro config](./meta-kapernikov/conf/distro/kapernikov.conf))
* [x] podman container runtime with storage on a data partition
* [x] swupdate with atomic updates and A/B partition, using grub.

I am not sure if grub was the best way to go. It was hard to figure out (swupdate doesn't have a lot of grub docu), uboot is better documented, but grub still seems the way to go for x86_64 based systems.

## building

```shell
pip install kas
kas build
```

## running in qemu

either use the `run-efi.sh` that's included, this will use the qemu installed on your system (which might support graphical console). or use the yocto way:

```shell
kas shell
runqemu kvm
# or
runqemu kvm nographic 
```

The system has an A and a B system with an overlay on `/etc/`. I'm not yet sure if that's actually a good idea: a messed up `/etc/` can make the system unbootable, with no way of recovery.

## Testing the atomic software update

Go to `http://localhost:8080/` and upload the generated `swu` file. The system will reboot and the new system will be active.

This is how it works:

* An [image](./meta-kapernikov/recipes-core/images/image-kapernikov.bb) is build, with `wic` to have GPT partitions. I avoid using the block device names for refering to these partitions, and use the FS labels and partition labels instead. The labels get destroyed when swupdate updates a filesystem, so i make grub remember the block devices. The reason for avoiding the block device names is that they are not stable: on qemu they would be `/dev/sda1` and `/dev/sda2`, but on a real system they could be `/dev/nvme0n1p1` and `/dev/nvme0n1p2` or `/dev/mmcblk0p1` ...

* The [grub.cfg](./meta-kapernikov/recipes-core/images/image-kapernikov/grub.cfg) file has two entries and a state machine that keeps track of the active system. It will not change to the other active system unless `ustate` is set to 1 (which is done when `swupdate` gets an update). When `ustate` is set to 1, a counter will count down on every boot and when it reaches zero, it will fall back to the old system.

* A systemd unit and accompanying [script](./meta-kapernikov/recipes-support/psh/post-swupdate-healthcheck/post-swupdate-resetustate.in) sets `ustate` to 0. This is now dummy, but in the real world this script should check for proper operation. If the script fails `ustate` remains 1 and the system will eventually fall back to the old system.

## Todos

* A third image that is never updated could run as a `rescue` image. This image could then wait for an update that rescues the system.

* use bitbakes built in support for kconfig parsing for the swupdate config instead of putting my own defconfig file.

* The [post-swupdate-healthcheck](./meta-kapernikov/recipes-support/psh/post-swupdate-healthcheck.bb) package could first verify the proper operation of `podman`.

* I went for podman because i tought that was going to be lightweight. It is not, i think i should switch to containerd.

* Test on real hardware, and remove the 'no security' stuff (now root log in works without password)

* CI build

