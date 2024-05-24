# Yocto experiment

* [ ] poky-based distro with apt
* [ ] podman
* [ ] swupdate

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
