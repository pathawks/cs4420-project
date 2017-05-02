#!/bin/bash

pushd data
rm results.csv

for size in $(ls -d */);
do
    pushd $size
    for moves in {0..20};
    do
        pushd $moves
        for board in $(ls *.txt);
        do
            scala ../../../Project.jar $board astar manhattan >> ../../results.csv
        done
        popd
    done
    popd
done
popd
