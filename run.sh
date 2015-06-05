#/bin/bash

for port in `find /dev -name 'tty*'`
do
    PORTS="$PORTS:$port"
done
#JAVA_OPT="-Djava.library.path=/usr/lib/jni -Dgnu.io.rxtx.SerialPorts=$PORTS"
JAVA_OPT="-Dgnu.io.rxtx.SerialPorts=$PORTS"
java -jar $JAVA_OPT target/RXTX-0.0.1-SNAPSHOT.jar
