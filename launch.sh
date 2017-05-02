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
            output=$(echo "astar, linearConflict, 3, 3, 2.01")
            (printf "%s,%9d, %s\n" ${size%%/} $(echo $board | sed "s/\.txt//") $output) >> ../../results.csv
        done
        popd
    done
    popd
done
popd
