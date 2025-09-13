package com.mycompany.cache_memory_simulator;

import java.util.Scanner;

public class Cache_Memory_Simulator {

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        CacheSelection selecting = new CacheSelection();
        System.out.println("Select Cache memory mapping type:\n\t1. Direct\n\t2. Fully Associative\n\t3. Set Associative");
        int userInput = input.nextInt();
        switch (userInput) {
            case 1 -> selecting.DirectSelected();
            case 2 -> selecting.FullySelected();
            case 3 -> selecting.SetSelected();
            default -> System.out.println("Invalid input!");
        }
    }
}
