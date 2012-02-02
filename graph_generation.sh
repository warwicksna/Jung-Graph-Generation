#!/bin/sh

for nodes in 100 200 300 400 500         
do
        java -cp '.:junglib/*:peerlib/*' GenerateGraph ${nodes} 10 true pop_size_${nodes}.pairing_count_10.directed.graphml       
        java -cp '.:junglib/*:peerlib/*' GenerateGraph ${nodes} 10 false pop_size_${nodes}.pairing_count_10.undirected.graphml      
done

for pairings in 3 5 10 15 20 25 30 50 100
do
        java -cp '.:junglib/*:peerlib/*' GenerateGraph 100 ${pairings} true pop_size_100.pairing_count_${pairings}.directed.graphml
        java -cp '.:junglib/*:peerlib/*' GenerateGraph 100 ${pairings} false pop_size_100.pairing_count_${pairings}.undirected.graphml
done