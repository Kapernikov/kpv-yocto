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

either use the run.sh that's included, this will use the qemu installed on your system (which might support graphical console). or use the yocto way:

```shell
kas shell
runqemu kvm
# or
runqemu kvm nographic 
```
