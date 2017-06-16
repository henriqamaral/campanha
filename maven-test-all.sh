#! /bin/bash -e

for dir in *-service; do
	(cd $dir ; mvn test $*)
done