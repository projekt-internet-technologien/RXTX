# Get

`git clone git@git.dennis-boldt.de:arduino/rxtx.git`

# Install dependencies

## Ubuntu 14.04

`sudo apt-get install librxtx-java`

# Load Arduino application to Arduino

Upload the `arduino/SerialRead/SerialRead.ino` the the Arduino

# Run Java Application

## Options

* `--ports` - no default `/dev/ttyUSB0:/dev/ttyACM0`
* `--rxtxlib` - default is `usr/lib/jni`

## Manual 

```
java -jar target/RXTX-0.0.1-SNAPSHOT.jar --ports /dev/ttyUSB0:/dev/ttyACM0
```

## Script

```
-/run
```
