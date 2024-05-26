# Yocto experiment

* [x] poky-based distro with apt
* [x] podman
* [x] swupdate with atomic updates and A/B partition

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

