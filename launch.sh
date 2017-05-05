#!/bin/bash

export _JAVA_OPTIONS="-Xms4g -Xmn4g -Xmx10g"
pushd data

for size in $(ls -d */);
do
    pushd $size
    for moves in {1..20};
    do
        pushd $moves
        for board in $(ls -U | tail -n25);
        do
            scala ../../../Project.jar $board astar manhattan             >> ../../results/manhattan.csv
            scala ../../../Project.jar $board astar linearConflict        >> ../../results/linearConflict.csv
            scala ../../../Project.jar $board astar NMaxSwap              >> ../../results/NMaxSwap.csv
            scala ../../../Project.jar $board astar nonAdditiveFringe     >> ../../results/nonAdditiveFringe.csv
#           scala ../../../Project.jar $board astar nonAdditiveCorner     >> ../../results/nonAdditiveCorner.csv
            scala ../../../Project.jar $board astar nonAdditiveMax        >> ../../results/nonAdditiveMax.csv
            scala ../../../Project.jar $board astar disjointPDBVertical   >> ../../results/disjointPDBVertical.csv
            scala ../../../Project.jar $board astar disjointPDBHorizontal >> ../../results/disjointPDBHorizontal.csv
            scala ../../../Project.jar $board astar disjointPDBMax        >> ../../results/disjointPDBMax.csv
            scala ../../../Project.jar $board ida   linearConflict        >> ../../results/ida-linearConflict.csv
        done
        popd
    done
    popd
done
popd
