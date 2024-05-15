FROM ubuntu:24.04 AS base

# Set up dependencies that change less often
RUN apt-get update && apt-get install -y gawk wget git diffstat unzip texinfo gcc build-essential chrpath socat cpio python3 python3-pip python3-pexpect xz-utils debianutils iputils-ping python3-git python3-jinja2 python3-subunit zstd liblz4-tool file locales libacl1 sudo && \
    locale-gen en_US.UTF-8

RUN groupadd -r yocto && useradd -r -g yocto yocto && mkdir -p /yocto && chown -R yocto.yocto /yocto && echo "yocto ALL=(ALL) NOPASSWD: ALL" >> /etc/sudoers
USER yocto

# Clone the poky repository
ARG POKY_BRANCH=scarthgap
RUN git clone -b ${POKY_BRANCH} git://git.yoctoproject.org/poky /yocto/poky


# Set up the build environment and default build directory
WORKDIR /yocto/poky
RUN bash -c '. oe-init-build-env'

FROM base as stage1

# Add your layers (change more frequently)
COPY ./layers /yocto/layers

# Pre-download sources
#RUN bitbake-layers add-layer /yocto/layers
RUN bash -c '. oe-init-build-env && bitbake core-image-full-cmdline --runall=fetch'

# Add configuration files
COPY ./conf/bblayers.conf /yocto/build/conf/
COPY ./conf/local.conf /yocto/build/conf/

# Build the image (change very frequently)
CMD bash -c 'sudo chown -R yocto:yocto /yocto/poky/build/tmp/deploy && source oe-init-build-env && bitbake core-image-full-cmdline'
