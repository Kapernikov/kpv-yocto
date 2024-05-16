#!/bin/bash
mkdir -p output
mkdir -p sstate-cache
chmod og+rwX output sstate-cache
docker build -t kpv-embedded .
rm -f  output/deploy/images/qemux86-64/*
docker run -it --rm \
    --device /dev/kvm \
    -v ./sstate-cache:/yocto/poky/build/sstate-cache \
    -v ./output:/yocto/poky/build/tmp/ \
   kpv-embedded