#!/bin/sh

THE_CLASSPATH=
for i in `ls libs/*.jar`
do
    THE_CLASSPATH=${THE_CLASSPATH}:${i}
done

java \
-Dfile.encoding=UTF-8 \
-classpath ".:conf/paypal:${THE_CLASSPATH}" \
net.cworks.wowserver.WowServer