# Cache Memory Simulator 🖥️
#### Video Demo: [https://youtu.be/DT_A1G98zvs](https://youtu.be/DT_A1G98zvs)
#### Description:

This project implements a **Cache Memory Simulator** in Java, designed to model the fundamental mapping strategies used in modern computer architecture. The simulator provides support for **Direct Mapping**, **Fully Associative Mapping**, and **Set-Associative Mapping**, while also incorporating **replacement policies** (FIFO, LRU, LFU) and **write policies** (Write-Through vs. Write-Back, Write-Allocate vs. No Write-Allocate).  

The project was built as a hands-on way to understand how memory access patterns interact with cache organization, and how performance tradeoffs emerge from different architectural choices. By simulating cache hits and misses for read and write instructions, the program provides detailed statistics on memory access efficiency, helping visualize the critical role of caches in bridging the performance gap between CPU and main memory.  

This README will walk through the structure of the project, explain the purpose of each file, discuss the algorithms used, and highlight the design decisions made during implementation.

---

## Project Structure

### `Cache_Memory_Simulator.java`
This is the **main class** that initiats the project by asking the user for the cache mapping type and ending up by calling one of the cache mapping selecting methods.

---

### `CacheSelection.java`
This class starts to build the cache memory by building the selected cache mapping type and asking the user for the instruction to do and keep going until the user input is to exit the program and displaying cache statistics.

---

### `Mapping.java`
This is the **base class** for mapping types that contains shared attributes and behaviors across all cache mapping techniques. Variables such as `memorySize`, `cacheSize`, `blockSize`, `numOfAccesses`, and `numOfHits` are defined here to avoid redundancy. Write policy flags (`write_hit` and `write_miss`) are also included so that all mapping types consistently apply the same write strategies. By using inheritance, we reduced code duplication and made it easier to maintain consistency across the different mapping techniques.

---

### `Line.java`
This class represents a **single cache line**. Each line stores important metadata:
- **Tag**: to identify which block is currently stored.
- **Dirty Bit**: to track whether the block has been modified (relevant for Write-Back).
- **Frequency**: for LFU replacement.
- **Last Used**: for LRU replacement.
- **Input Order**: for FIFO replacement.

It also includes helper methods such as `fillLine()`, which resets metadata when a block is placed in the cache. Abstracting the line logic here ensures that all mapping techniques can reuse this structure.

---

### `Set.java`
This class models a **set of lines** for Set-Associative Mapping. Each set contains `k` lines (where `k` is the associativity level chosen by the user). It tracks:
- `Lines[]`: the actual cache lines within the set.
- `fillingSet`: the number of lines currently used in the set.
- `lineOrder`: a counter used for FIFO replacement.

It also contains the replacement policies (`FIFO`, `LRU`, `LFU`) applied locally within a set, ensuring that competition is restricted to lines belonging to the same set.

---

### `DirectMapping.java`
This file implements the simplest mapping strategy: **Direct Mapping**.  
- Each block from main memory maps to exactly one cache line (using the modulo operation).  
- No replacement policy is needed since there is only one possible location for a block.  
- Read and write instructions update statistics and apply write policies directly.  

This module served as the starting point of the simulator since its logic is straightforward and builds the foundation for more complex mappings.

---

### `FullyAssociativeMapping.java`
This file implements **Fully Associative Mapping**, where any memory block can be stored in any cache line.  
- All lines are candidates for replacement.  
- Replacement policies (FIFO, LRU, LFU) determine which line is evicted once the cache is full.  
- Both read (`r_instruction`) and write (`w_instruction`) methods handle hits, misses, write strategies, and dirty bit management.

Design-wise, this mapping type highlighted the importance of metadata tracking. For example, maintaining `lastUsed` counters for LRU and `frequency` counters for LFU required careful updating on every access.

---

### `SetAssociativeMapping.java`
This file bridges the gap between Direct and Fully Associative Mapping.  
- The cache is divided into multiple sets, and each block maps to one specific set (using the block number modulo the number of sets).  
- Within a set, replacement policies (FIFO, LRU, LFU) are applied to select a victim line when necessary.  
- Its logic is nearly identical to Fully Associative Mapping, except that all decisions are constrained to a specific set instead of the whole cache.

Implementing this module forced a deeper appreciation for the relationship between **tag, set index, and block offset**. Careful bit manipulation was required to extract the tag and determine the correct set.

---

## Replacement Policies

Three replacement policies were implemented consistently across Fully and Set-Associative Mapping:

1. **FIFO (First In First Out):** Evicts the line that has been in the cache the longest, tracked via `inputOrder`.  
2. **LRU (Least Recently Used):** Evicts the line that has gone the longest without being accessed, tracked via `lastUsed`.  
3. **LFU (Least Frequently Used):** Evicts the line with the lowest access frequency, tracked via `frequency`.  

The decision to implement all three policies gave flexibility and highlighted the tradeoffs: FIFO is simple but may evict heavily used lines, LRU is intuitive but requires careful updating, and LFU better reflects usage patterns but can suffer from ties.

---

## Write Policies

Both write-hit and write-miss strategies were included:

- **Write-Hit:**  
  - *Write-Through:* updates main memory immediately.  
  - *Write-Back:* marks the line dirty, deferring the update until eviction.  

- **Write-Miss:**  
  - *Write-Allocate:* loads the block into the cache before writing.  
  - *No Write-Allocate:* updates memory directly without caching the block.  

This combination ensured realism in the simulator, as modern processors use different strategies depending on performance and consistency needs.

---

## Statistics and Output

At the end of simulation, the program prints detailed statistics:
- Total accesses  
- Total hits  
- Total misses  
- Hit ratio  

This output quantifies how different mapping and replacement strategies affect cache efficiency. It allows students and developers to test various scenarios and observe how cache design choices impact performance.

---

## Design Decisions

Several important design choices shaped this project:

1. **Inheritance over Duplication:**  
   By creating a base `Mapping` class, shared variables and statistics were centralized, reducing redundancy across Direct, Fully, and Set-Associative implementations.  

2. **Separation of Concerns:**  
   `Line` and `Set` classes abstracted low-level details, keeping the mapping classes focused on algorithms and control flow.  

3. **User-Driven Configuration:**  
   Instead of hardcoding parameters, the simulator prompts users for memory size, cache size, block size, associativity, write policies, and replacement policy. This makes the tool flexible for experiments.  

4. **Faithfulness to Theory:**  
   The implementation closely follows textbook definitions of cache mapping and replacement policies, ensuring it can serve as a teaching tool.  

---

## Conclusion

This Cache Memory Simulator provides a comprehensive model of how caches work under different mapping strategies and policies. It balances accuracy, modular design, and flexibility, allowing users to explore the nuanced tradeoffs in computer architecture.  

By simulating memory access patterns and observing statistics, this project not only demonstrates practical programming skills but also deepens theoretical understanding of performance-critical hardware concepts.  

The code structure reflects careful design decisions, and the simulator can be extended further (e.g., prefetching, victim caches, or multi-level caches). Overall, the project is a robust foundation for anyone studying or teaching cache organization in computer architecture.

