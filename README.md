# RXTX

This RXTX project is a maven project, which can be used as a dependency in other maven projects to handle a serial communication, e.g. an USB communication:

```
<dependency>
  <groupId>de.dennis_boldt</groupId>
  <artifactId>RXTX</artifactId>
  <version>0.0.2-SNAPSHOT</version>
</dependency>
```

In parallel, there is the [RXTX-examples project](https://github.com/projekt-internet-technologien/RXTX-examples), which provides some examples.

## Dependencies and configuration

### Ubuntu 14.04

```
sudo apt-get install maven
sudo apt-get install librxtx-java
```

Make sure, the folder `/usr/lib/jni` contains a `librxtxSerial.so`.

Add the user to the group `dialout`:

```
sudo usermod -a -G dialout $USER
```
## Get and install RXTX

Please clone this repository:

```
git clone git@github.com:projekt-internet-technologien/RXTX.git
```

Next, install RXTX into the local maven repository:

```
mvn install
```

You can import this project as maven project into Eclipse.

