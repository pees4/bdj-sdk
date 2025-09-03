# BD-J Linux SDK
This is a set of tools to simplify building BD-J ISO images on GNU/Linux systems.
It is an adaptation of the Win32 [minimal BD-J toolkit for the PS3][ps3],
with an updated authoring tool from [the HD Cookbook][hdc]. For creating ISO
images, we use a [Linux port][makefs_termux] of [NetBSD makefs][makefs] ported
by [Andrew Randrianasulu][Randrianasulu].

## Building
On Debian-flavored operating systems, you can invoke the following commands to
install dependencies, and compile the source code.

```console
sudo apt update

rm -rf bdj-sdk
sudo apt-get install build-essential libbsd-dev git pkg-config openjdk-8-jdk-headless openjdk-11-jdk-headless
git clone --recurse-submodules https://github.com/pees4/bdj-sdk
ln -s /usr/lib/jvm/java-8-openjdk-amd64 bdj-sdk/host/jdk8
ln -s /usr/lib/jvm/java-11-openjdk-amd64 bdj-sdk/host/jdk11
make -C bdj-sdk/host/src/makefs_termux
make -C bdj-sdk/host/src/makefs_termux install DESTDIR=$PWD/bdj-sdk/host
make -C bdj-sdk/target
cd bdj-sdk/samples/BD-JB-1250/
export BDJSDK_HOME="/home/akun/bdj-sdk"
export JAVA8_HOME="/usr/lib/jvm/java-8-openjdk-amd64"
make clean
cd ../../

```

Untuk membuat payload.jar
```console
cd samples/BD-JB-1250/payloads/lapse
rm samples/BD-JB-1250/payloads/lapse/payload.jar
make
cp payload.jar /home/akun/bdj-sdk/samples/BD-JB-1250/src/org/bdj
cd ../../../../

```

## Usage example
```console
cd samples/BD-JB-1250/
make clean
cd ../../
chmod +x /home/akun/bdj-sdk/host/bin/bdsigner
rm samples/BD-JB-1250/RemoteJarLoader.iso
make -C samples/BD-JB-1250

```
If everything was built successfully, you will find an BD-RE iso file
`bdj-sdk/samples/BD-JB-1250
```console
explorer.exe .

```

[ps3]: https://ps3.brewology.com/downloads/download.php?id=2171&mcid=4
[hdc]: http://oliverlietz.github.io/bd-j/hdcookbook.html
[makefs_termux]: https://github.com/Randrianasulu/makefs_termux
[makefs]: https://man.netbsd.org/makefs.8
[Randrianasulu]: https://github.com/Randrianasulu
