# Exec helper
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://3rdparty/luawrapper/LICENSE.md;md5=e2c021c3bb7bf4b68141714431d46aff \
                    file://LICENSE;md5=e4abc2c1968f9b556e285966085f3f5a"

SRC_URI = "git://github.com/bverhagen/exec-helper;protocol=https;branch=master"

# Modify these as desired
PV = "0.6.0"
SRCREV = "8869989a59b352f340406ae8859958bf343be776"

S = "${WORKDIR}/git"


inherit cmake

# Specify any options you want to pass to cmake using EXTRA_OECMAKE:
EXTRA_OECMAKE = "-DUSE_SYSTEM_YAML_CPP=ON -DUSE_SYSTEM_LUAJIT=ON -DUSE_SYSTEM_GSL=ON -DENABLE_TESTING=OFF -DBUILD_MAN_DOCUMENTATION=OFF -DBUILD_HTML_DOCUMENTATION=OFF -DBUILD_XML_DOCUMENTATION=OFF -DBUILD_USAGE_DOCUMENTATION=OFF -DBUILD_API_DOCUMENTATION=OFF"

DEPENDS = "boost yaml-cpp luajit microsoft-gsl"
RDEPENDS_${PN} = "boost-program-options boost-log"

## this one does not compile yet on musl libc
# | In file included from /home/kervel/projects/yocto/kas/build/tmp-musl/work/core2-64-oe-linux-musl/exec-helper/0.6.0/git/src/config/include/config/environment.h:8,
# |                  from /home/kervel/projects/yocto/kas/build/tmp-musl/work/core2-64-oe-linux-musl/exec-helper/0.6.0/git/src/core/include/core/task.h:9,
# |                  from /home/kervel/projects/yocto/kas/build/tmp-musl/work/core2-64-oe-linux-musl/exec-helper/0.6.0/git/src/core/include/core/posixShell.h:7,
# |                  from /home/kervel/projects/yocto/kas/build/tmp-musl/work/core2-64-oe-linux-musl/exec-helper/0.6.0/git/src/core/src/posixShell.cpp:2:
# | /home/kervel/projects/yocto/kas/build/tmp-musl/work/core2-64-oe-linux-musl/exec-helper/0.6.0/recipe-sysroot/usr/include/gsl/string_span:390:97: error: template argument 1 is invalid
# |   390 | constexpr basic_string_span<const byte, details::calculate_byte_size<ElementType, Extent>::value>
# |       |                                                                                                 ^
# | /home/kervel/projects/yocto/kas/build/tmp-musl/work/core2-64-oe-linux-musl/exec-helper/0.6.0/recipe-sysroot/usr/include/gsl/string_span: In function 'constexpr int gsl::as_bytes(basic_string_span<CharT, Extent>)':
# | /home/kervel/projects/yocto/kas/build/tmp-musl/work/core2-64-oe-linux-musl/exec-helper/0.6.0/recipe-sysroot/usr/include/gsl/string_span:396:36: error: expected '>' before 'byte'
# |   396 |     return {reinterpret_cast<const byte*>(s.data()), s.size_bytes()};
# |       |                                    ^~~~
# | /home/kervel/projects/yocto/kas/build/tmp-musl/work/core2-64-oe-linux-musl/exec-helper/0.6.0/recipe-sysroot/usr/include/gsl/string_span:396:36: error: expected '(' before 'byte'
# |   396 |     return {reinterpret_cast<const byte*>(s.data()), s.size_bytes()};
# |       |                                    ^~~~
# |       |                                    (
# | /home/kervel/projects/yocto/kas/build/tmp-musl/work/core2-64-oe-linux-musl/exec-helper/0.6.0/recipe-sysroot/usr/include/gsl/string_span:396:36: error: 'byte' was not declared in this scope; did you mean 'std::byte'?
# |   396 |     return {reinterpret_cast<const byte*>(s.data()), s.size_bytes()};
# |       |                                    ^~~~
# |       |                                    std::byte
# | In file included from /home/kervel/projects/yocto/kas/build/tmp-musl/work/core2-64-oe-linux-musl/exec-helper/0.6.0/git/src/core/src/posixShell.cpp:21:
# | /home/kervel/projects/yocto/kas/build/tmp-musl/work/core2-64-oe-linux-musl/exec-helper/0.6.0/git/src/core/include/core/logger.h:9:14: error: 'gsl::czstring' {aka 'const char*'} is not a template
# |     9 | static const gsl::czstring<> LOG_CHANNEL = "core";
# |       |              ^~~
# | /home/kervel/projects/yocto/kas/build/tmp-musl/work/core2-64-oe-linux-musl/exec-helper/0.6.0/git/src/core/src/posixShell.cpp: In static member function 'static execHelper::core::TaskCollection execHelper::core::PosixShell::wordExpand(const execHelper::core::Task&)':
# | /home/kervel/projects/yocto/kas/build/tmp-musl/work/core2-64-oe-linux-musl/exec-helper/0.6.0/git/src/core/src/posixShell.cpp:147:18: error: 'gsl::zstring' {aka 'char*'} is not a template
# |   147 |             span<zstring<>> w(p.we_wordv, p.we_wordc);
# |       |                  ^~~~~~~
# | ninja: build stopped: subcommand failed.
