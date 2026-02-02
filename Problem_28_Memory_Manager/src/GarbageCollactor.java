package Problem_28_Memory_Manager.src;
import java.util.Set;

public class GarbageCollector {

    private HeapManager heapManager;
    private RootSet rootSet;

    public GarbageCollector(HeapManager heapManager, RootSet rootSet) {
        this.heapManager = heapManager;
        this.rootSet = rootSet;
    }

    public void runGC() {
        System.out.println("--- Mark Phase ---");
        mark();
        System.out.println("--- Sweep Phase ---");
        sweep();
    }

    private void mark() {
        InStack stack = new InStack(heapManager.getHeap().length);

        int[] roots = rootSet.getRoots();
        int rootCount = rootSet.getRootCount();

        // Push all roots onto the stack
        for (int i = 0; i < rootCount; i++) {
            int rootIndex = roots[i];
            if (!heapManager.getHeap()[rootIndex].visited) {
                stack.push(rootIndex);
            }
        }

        while (!stack.isEmpty()) {
            int current = stack.pop();
            MemoryBlock block = heapManager.getHeap()[current];

            if (!block.visited) {
                block.visited = true;
                System.out.println("Visiting block " + current);

                for (int i = 0; i < block.refCount; i++) {
                    int ref = block.references[i];
                    if (!heapManager.getHeap()[ref].visited) {
                        stack.push(ref);
                    }
                }
            }
        }
    }


    private void sweep() {
        int freedCount = 0;
        MemoryBlock[] heap = heapManager.getHeap();

        for (int i = 0; i < heap.length; i++) {
            MemoryBlock block = heap[i];
            if (block.allocated && !block.visited) {
                heapManager.freeBlock(i);
                freedCount++;
                System.out.println("Block " + i + " is unreachable. FREED.");
            } else if (block.allocated) {
                block.visited = false;
            }
        }

        System.out.println("Freed " + freedCount + " block(s).");
    }
}
