LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_URI = "file://main.c"

PV = "0.5"
PACKAGE_ARCH = "${MACHINE_ARCH}"
S = "${WORKDIR}"

NAME="hello_world"

FILES:${PN} = "${bindir}/${NAME}"

SYSTEM_NAME = "your_system"
SYSTEM_NAME:qemuall = "QEMU"
SYSTEM_NAME:rpi = "Raspberry Pi"

do_compile () {
	${CC} ${LDFLAGS} -DSYSTEM_NAME=\"${SYSTEM_NAME}\" -o ${NAME} main.c
}

do_install () {
	install -d ${D}${bindir}
	install -m 755 ${NAME} ${D}${bindir}
}
