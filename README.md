# BD-J Linux SDK
This is a set of tools to simplify building BD-J ISO images on GNU/Linux systems.
It is an adaptation of the Win32 [minimal BD-J toolkit for the PS3][ps3],
with an updated authoring tool from [the HD Cookbook][hdc]. For creating ISO
images, we use a [Linux port][makefs_termux] of [NetBSD makefs][makefs] ported
by [Andrew Randrianasulu][Randrianasulu].

## Install SDK
On Debian-flavored operating systems (seperti Ubuntu), you can invoke the following commands to
install dependencies, and compile the source code.

PERHATIAN: Ganti "akun" dengan user name di Ubuntu Anda !

```console
sudo apt update

sudo apt-get install build-essential libbsd-dev git pkg-config openjdk-8-jdk-headless openjdk-11-jdk-headless

git clone --recurse-submodules https://github.com/pees4/bdj-sdk

ln -s /usr/lib/jvm/java-8-openjdk-amd64 bdj-sdk/host/jdk8
ln -s /usr/lib/jvm/java-11-openjdk-amd64 bdj-sdk/host/jdk11
make -C bdj-sdk/host/src/makefs_termux
make -C bdj-sdk/host/src/makefs_termux install DESTDIR=$PWD/bdj-sdk/host
make -C bdj-sdk/target

# Folder aktif bdj-sdk/samples/BD-JB-1250/
cd bdj-sdk/samples/BD-JB-1250/
export BDJSDK_HOME="/home/akun/bdj-sdk"
export JAVA8_HOME="/usr/lib/jvm/java-8-openjdk-amd64"
make clean
cd

```

Untuk membuka folder tempat file yang mau diedit:
```console
explorer.exe .

```

## Cara membuat aiofix_USBpayload.elf
```console
rm -rf ps4-payload-dev/sdk
git clone https://github.com/ps4-payload-dev/sdk ~/ps4-payload-dev/sdk

# Folder aktif ps4-payload-dev/sdk/
cd ps4-payload-dev/sdk
sudo apt-get update && sudo apt-get upgrade
sudo apt-get install bash socat llvm clang lld
sudo apt-get install cmake meson pkg-config
sudo make DESTDIR=/opt/ps4-payload-sdk install

# Copy folder external dari BD-JB-1250-main/payloads/lapse/src/org/bdj/ ke ps4-payload-dev/sdk/samples/
export PS4_PAYLOAD_SDK=/opt/ps4-payload-sdk
make clean
rm samples/external/aiofix_USBpayload.elf
make -C samples/external
cp ~/ps4-payload-dev/sdk/samples/external/aiofix_USBpayload.elf ~/bdj-sdk/samples/BD-JB-1250/payloads/lapse/src/org/bdj/external/
cd

```

## Cara membuat payload.jar
```console
cd bdj-sdk/samples/BD-JB-1250/payloads/lapse
make clean
make
cp payload.jar /home/akun/bdj-sdk/samples/BD-JB-1250/src/org/bdj
cd

```

## Cara membuat Lapse.iso
```console

# Folder aktif bdj-sdk/samples/BD-JB-1250
# Copy payload ke dalam folder /bdj-sdk/resources/AVCHD

cd bdj-sdk/samples/BD-JB-1250/
make clean

chmod +x /home/akun/bdj-sdk/host/bin/bdsigner
ls -l /home/akun/bdj-sdk/resources/AVCHD/CERTIFICATE/bu.discroot.crt
make

# Jika ada perubahan, make clean lagi baru make.

```
### Jika semuanya dibangun dengan sukses, Anda akan menemukan file Lapse.iso BD-JB di
`bdj-sdk/samples/BD-JB-1250'
```console
explorer.exe .

```

Credits

* **[TheFlow](https://github.com/theofficialflow)**
* **[Gezine](https://github.com/Gezine)** 
* **[john-tornblom](https://github.com/john-tornblom)**