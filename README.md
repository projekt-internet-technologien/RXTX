# Get

```
git clone git@git.dennis-boldt.de:arduino/rxtx.git
```

# Install dependencies

## Ubuntu 14.04

```
sudo apt-get install librxtx-java
```

# Load Arduino application to Arduino

Upload the `arduino/SerialRead/SerialRead.ino` the the Arduino

# Run Java Application

## Options

* `--ports` - no default `/dev/ttyUSB0:/dev/ttyUSB1:/dev/ttyACM0:/dev/ttyACM1`
* `--rxtxlib` - default is `/usr/lib/jni`

## Manual 

```
java -jar target/RXTX-0.0.1-SNAPSHOT.jar
java -jar target/RXTX-0.0.1-SNAPSHOT.jar --ports /dev/ttyUSB0:/dev/ttyACM0
java -jar target/RXTX-0.0.1-SNAPSHOT.jar --ports /dev/ttyUSB0:/dev/ttyACM0 --rxtxlib /usr/lib
```

## Script

```
./run
```

# Sources

* http://angryelectron.com/rxtx-on-raspbian/
* http://eclipsesource.com/blogs/2012/10/17/serial-communication-in-java-with-raspberry-pi-and-rxtx/
* http://blog.cedarsoft.com/2010/11/setting-java-library-path-programmatically/
* http://rxtx.qbang.org/wiki/index.php/Two_way_communcation_with_the_serial_port
* http://stackoverflow.com/a/11380638/605890
