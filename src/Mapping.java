package com.mycompany.cache_memory_simulator;

public class Mapping {
    public int memorySize;
    public int cacheSize;
    public int blockSize;
    
    public int numOfHits;
    public int numOfAccesses;
    
    public boolean write_hit;       //write through: true       write back: false
    public boolean write_miss;      //write allocate: true      no write allocate: false
    
    public void printStatistics() {
    double hitRatio = (numOfAccesses == 0) ? 0 : (double) numOfHits / numOfAccesses;
    System.out.println("==== Cache Statistics ====");
    System.out.println("Total Accesses: " + numOfAccesses);
    System.out.println("Total Hits:     " + numOfHits);
    System.out.println("Total misses:   " + (numOfAccesses - numOfHits));
    System.out.printf("Hit Ratio:      %.2f\n", hitRatio * 100);
    }
}
