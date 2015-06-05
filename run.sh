#/bin/bash

for port in `find /dev -name 'ttyACM*'`
do
    PORTS="$PORTS:$port"
done
for port in `find /dev -name 'ttyUSB*'`
do
    PORTS="$PORTS:$port"
done

#JAVA_OPT="--ports $PORTS --rxtxlib /usr/lib/jni"
JAVA_OPT="--ports $PORTS"
java -jar target/RXTX-0.0.1-SNAPSHOT.jar $JAVA_OPT
