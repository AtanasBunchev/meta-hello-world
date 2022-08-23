# meta-hello-world

This layer contains the example bitbake recipe hello-world and simple vim patch that
addes the message "Modified version with Yocto and OpenEmbedded."


## Dependencies

This layer depends on:

 * URI: https://git.yoctoproject.org/poky
   * branch: kirkstone

Optional dependencies:

 * URI: https://git.yoctoproject.org/meta-raspberrypi/
   * branch: kirkstone


## Building

Assuming you're on a working linux distribution, preferably with at least 50 GB of space free and decent network connection and CPU.

1. Clone Poky

        git clone git://git.yoctoproject.org/poky -b kirkstone
        cd poky


2. (optional) Clone meta-raspberrypi

        git clone https://git.yoctoproject.org/meta-raspberrypi/ -b kirkstone


3. Clone this layer

        git clone https://github.com/AtanasBunchev/meta-hello-world


4. Prepare to build poky (the reference yocto distribution)

        source ./oe-init-build-env


5. Add the meta-hello-world and (optional) meta-raspberrypi layers

        bitbake-layers add-layer ../meta-hello-world
        bitbake-layers add-layer ../meta-raspberrypi


6. Set MACHINE in `conf/local.conf`

    The default machine is qemux86-64. You can pick different qemu machine or
    one of the [raspberrypi boards](https://meta-raspberrypi.readthedocs.io/en/latest/layer-contents.html#supported-machines), if you've added meta-raspberrypi.

        vim conf/local.conf


7. (optional) Enable UART on Raspberry Pi 0 WiFi and 3

    If you're using Raspberry Pi 0 wifi or Raspberry Pi 3 and want to connect
    over UART, you need to add `ENABLE_UART = "1"` to `conf/local.conf` [Details](https://meta-raspberrypi.readthedocs.io/en/latest/extra-build-config.html#enable-uart)


7. Building hello-world-image

    **Notice**: Building poky's base image requires 50GB of space and a lot of time.
    You should consider using download cache by setting [DL_DIR](https://docs.yoctoproject.org/ref-manual/variables.html#term-DL_DIR)
    in build/conf/local.conf and [Shared State Cache](https://docs.yoctoproject.org/overview-manual/concepts.html#shared-state-cache)
    by setting SSTATE_DIR

        bitbake hello-world-image

    hello-world-image is based on core-image-base
    and includes the hello-world application
    and vim.


8. Running QEMU

    If MACHINE is qemu, take a look at:
    https://docs.yoctoproject.org/dev-manual/qemu.html?highlight=qemu#using-the-quick-emulator-qemu

    Overall, it should be as simple as:

        runqemu qemux86-64

    Assuming MACHINE="qemux86-64"


    If you want to run the qemux86-64 image without the runqemu script:

        cd build/tmp/deploy/images/qemu*
        qemu-system-x86_64 -kernel ./bzImage \
        -append "oprofile.timer=1 tsc=reliable no_timer_check rcupdate.rcu_expedited=1 root=/dev/sda" \
        -drive file="./hello-world-image-qemux86-64.ext4",index=0,media=disk,format=raw


9. Running raspberrypi image

    If MACHINE is one of the raspberrypi boards, you should use bmaptool to copy the generated .wic.bz2 file to SD card:

        cd build/tmp/deploy/images/raspberry*
        sudo bmaptool copy ./hello-world-image-<board_name>.wic.bz2 /dev/sdx

    Insert the selected board at <board_name> and the selected SD-card instead /dev/sdx


10. Connecting to Raspberry over UART

    After you've connected the UART to USB cable, you can use `screen` to connect like this:

        sudo screen /dev/ttyUSB0 115200


11. Testing the image

    After running the qemu image or booting the raspberrypi with SD card,
    login as root if required (by default there is no password) and run:

        hello_world


    If target machine is qemu, hello_world will print:

        Hello world!
        Running on QEMU

    if target machine is raspberrypi board, hello_world will print:

        Hello world!
        Running on Raspberry Pi


    To see the vim patch you have to open vim without specifying a file.

        vim

    The message shown will contain "Modified version with Yocto and OpenEmbedded."
