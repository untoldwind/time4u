#!/bin/sh

CP=.

for i in lib/*.jar
do
CP=$CP:$i
done

java -cp $CP de.objectcode.time4u.migrator.server04.Migrator $*
