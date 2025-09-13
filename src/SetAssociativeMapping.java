package com.mycompany.cache_memory_simulator;

import java.util.Scanner;

public class SetAssociativeMapping extends Mapping{
    public int replacementPolicy;
    
    public Set[] sets;
    public int numOfSets;
    public int setNumber;
    public int lineNumber;
    public int blockNumber;
    public boolean hitOrMiss;
    
    public int blockOffset;
    public int setNo;
    public int tag;
    
    Scanner input = new Scanner(System.in);
    
    public SetAssociativeMapping(){
        numOfHits = 0;
        numOfAccesses = 0;
        
        System.out.print("Main memory size: ");
        memorySize = input.nextInt();
        System.out.print("Cache memory size: ");
        cacheSize = input.nextInt();
        System.out.print("Block size: ");
        blockSize = input.nextInt();
        System.out.print("Lines per set (K): ");
        Set.k_way = input.nextInt();
        
        numOfSets = (cacheSize / blockSize) / Set.k_way;
        blockOffset = (int) (Math.log(blockSize) / Math.log(2));
        setNo = (int) (Math.log(numOfSets) / Math.log(2));
        
        // Write-hit strategy
        int userInput;
        System.out.println("Write-Hit strategy:\n\t1. Write through\n\t2. Write back");
        userInput = input.nextInt();
        write_hit = (userInput == 1);
        
        // Write-miss strategy
        System.out.println("Write-Miss strategy:\n\t1. Write allocate\n\t2. No Write allocate");
        userInput = input.nextInt();
        write_miss = (userInput == 1);
        
        //Replacement policy selection
        System.out.println("Select one of these replacement policies:\n\t1. FIFO\n\t2.LRU\n\t3. LFU");
        replacementPolicy = input.nextInt();
        
        sets = new Set[numOfSets];
        for(int i = 0; i < numOfSets; i++){
            sets[i] = new Set();
        }
    }
    
    public void r_instruction(int address){
        numOfAccesses++;
        tag = address >> (setNo + blockOffset);
        blockNumber = address / blockSize;
        setNumber = blockNumber % numOfSets;
        lineNumber = sets[setNumber].checkHit(tag);

        hitOrMiss = (lineNumber != -1);

        if(hitOrMiss){
            System.out.println("Cache hit!");
            numOfHits++;
            sets[setNumber].Lines[lineNumber].frequency++;
            sets[setNumber].increaseLinesUsage();
            sets[setNumber].Lines[lineNumber].lastUsed = 0;

        } else {
            System.out.println("Cache miss!");
            
            if(sets[setNumber].fillingSet < Set.k_way){
                lineNumber = sets[setNumber].fillingSet;
                sets[setNumber].fillingSet++;
                sets[setNumber].Lines[lineNumber].tag = tag;
                sets[setNumber].Lines[lineNumber].frequency++;
                sets[setNumber].increaseLinesUsage();
                sets[setNumber].Lines[lineNumber].lastUsed = 0;
                sets[setNumber].Lines[lineNumber].inputOrder = sets[setNumber].lineOrder;

            } else {
                switch(replacementPolicy){
                    case 1 -> sets[setNumber].FIFO();
                    case 2 -> sets[setNumber].LRU();
                    case 3 -> sets[setNumber].LFU();
                }
                if(sets[setNumber].Lines[lineNumber].dirtyBit){
                    System.out.println("Write back");
                    sets[setNumber].Lines[lineNumber].dirtyBit = false;
                }
                sets[setNumber].Lines[lineNumber].fillLine(tag, sets[setNumber].lineOrder);
            }

            sets[setNumber].lineOrder++;
        }
    }

    
    public void w_instruction(int address){
        numOfAccesses++;
        tag = address >> (setNo + blockOffset);
        blockNumber = address / blockSize;
        setNumber = blockNumber % numOfSets;
        lineNumber = sets[setNumber].checkHit(tag);

        hitOrMiss = (lineNumber != -1);

        if(hitOrMiss){
            System.out.println("Cache hit!");
            numOfHits++;
            if(write_hit){
                System.out.println("Write through");
            } else {
                sets[setNumber].Lines[lineNumber].dirtyBit = true;
            }
            sets[setNumber].increaseLinesUsage();
            sets[setNumber].Lines[lineNumber].lastUsed = 0;
            sets[setNumber].Lines[lineNumber].frequency++;
        } else {
            System.out.println("Cache miss!");
            if(write_miss){
                System.out.println("Write allocate");
                if(sets[setNumber].fillingSet < Set.k_way){
                    lineNumber = sets[setNumber].fillingSet;
                    sets[setNumber].fillingSet++;
                    sets[setNumber].Lines[lineNumber].tag = tag;
                    sets[setNumber].Lines[lineNumber].frequency++;
                    sets[setNumber].increaseLinesUsage();
                    sets[setNumber].Lines[lineNumber].lastUsed = 0;
                    sets[setNumber].Lines[lineNumber].inputOrder = sets[setNumber].lineOrder;
                } else {
                    switch(replacementPolicy){
                        case 1 -> sets[setNumber].FIFO();
                        case 2 -> sets[setNumber].LRU();
                        case 3 -> sets[setNumber].LFU();
                    }
                    if(sets[setNumber].Lines[lineNumber].dirtyBit){
                        System.out.println("Write back");
                        sets[setNumber].Lines[lineNumber].dirtyBit = false;
                    }
                    sets[setNumber].Lines[lineNumber].fillLine(tag, sets[setNumber].lineOrder);
                }
                if(write_hit){
                    System.out.println("Write through");
                } else {
                    sets[setNumber].Lines[lineNumber].dirtyBit = true;
                }
                sets[setNumber].increaseLinesUsage();
                sets[setNumber].Lines[lineNumber].lastUsed = 0;
                sets[setNumber].Lines[lineNumber].frequency++;

            } else { // No write allocate
                System.out.println("No write allocate");
            }
            sets[setNumber].lineOrder++;
        }
    }
}
