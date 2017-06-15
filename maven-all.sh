#! /bin/bash -e

for dir in *-service discovery gateway monitoring ; do
	(cd $dir ; mvn clean install $*)
done