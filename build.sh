#!/bin/bash
mkdir -p output
mkdir -p sstate-cache
chmod og+rwX output sstate-cache
docker build -t kpv-embedded .
docker run -it --rm \
    --device /dev/kvm \
    -v ./sstate-cache:/yocto/poky/build/sstate-cache \
    -v ./output:/yocto/poky/build/tmp/deploy/images \
   kpv-embedded