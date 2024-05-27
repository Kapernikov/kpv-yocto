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

* The [grub.cfg](./meta-kapernikov/recipes-core/images/image-kapernikov/grub.cfg) file has two entries and a state machine that keeps track of the active system. It will not change to the other active system unless `ustate` is set to 1 (which is done when `swupdate` gets an update). When `ustate` is set to 1, a counter will count down on every boot and when it reaches zero, it will fall back to the old system.

* A systemd unit (now dummy) and accompanying [script](./meta-kapernikov/recipes-core/images/image-kapernikov.bb) sets `ustate` to 0. This is now dummy, but in the real world this script should check for proper operation. If the script fails `ustate` remains 1 and the system will eventually fall back to the old system.

## Todos

* Now the `image-kapernikov` recipe is a bit hacky, and i need to extract much of the work (reconfigure-podman, but also the `resetustate` script) to a separate recipe, because now i get in trouble with the order of things.

* A third image that is never updated could run as a `rescue` image. This image could then wait for an update that rescues the system.

* The `resetustate` script could first verify the proper operation of `podman`.

* I went for podman because i tought that was going to be lightweight. It is not, i think i should switch to containerd.

* Test on real hardware, and remove the 'no security' stuff (now root log in works without password)

* CI build

## What i learned

* Amazing what yocto can do and how powerful bitbake is, and how flexible the whole yocto build system is: want a readonly rootfs ? check! want deb or rpm based package feed with updates ? check!. Want to know what you have to publish to comply with OSS licenses ? check! Want to build multiple images at once ? check.

* There are a lot of yocto recipes out there. If you need some software, chances are big that there is a recipe out there for the software.

* I really get now why one would want the added complexity of a system like yocto. However, using yocto for general purpose compute is probably not a good idea unless you want to create a CI build with a package feed for updates so you remain secure. The whole yocto flexibility is based on a rather interesting system of variable manipulation, patching and lazy evaluation. It is a good idea to spend some time understanding how this works before diving into recipes, you will understand everything much better if you do this first.

* Getting swupdate to work with grub was quite difficult. I think most users use swupdate with uboot (much easier to find examples using uboot).

* Grub can't do integer arithmetic

