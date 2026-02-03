

public class HeapManager {

    private MemoryBlock[] heap;
    private int size;
    private FreeList freeList;

    public HeapManager(int size) {
        this.size = size;
        heap = new MemoryBlock[size];
        freeList = new FreeList();

        // initialize heap and add all indices to free list
        for (int i = 0; i < size; i++) {
            heap[i] = new MemoryBlock();
            freeList.add(i);
        }
    }


    public int allocateBlock() {
        int index = freeList.get(); // get a free block index
        if (index == -1) {
            System.out.println("No free block available!");
            return -1;
        }

        heap[index].allocated = true;
        heap[index].visited = false;
        heap[index].clearReferences();
        System.out.println("Block allocated at index " + index);
        return index;
    }

    public void freeBlock(int index) {
        if (index >= 0 && index < size && heap[index].allocated) {
            heap[index].allocated = false;
            heap[index].visited = false;
            heap[index].clearReferences();
            freeList.add(index); // add back to free list
            System.out.println("Block " + index + " freed");
        } else {
            System.out.println("Invalid or already free block: " + index);
        }
    }


    public void addReference(int fromIndex, int toIndex) {
        if (fromIndex >= 0 && fromIndex < size &&
                toIndex >= 0 && toIndex < size) {

            if (heap[fromIndex].allocated && heap[toIndex].allocated) {
                heap[fromIndex].addReference(toIndex);
                System.out.println("Reference added: Block " + fromIndex + " -> Block " + toIndex);
            } else {
                System.out.println("Both blocks must be allocated.");
            }
        } else {
            System.out.println("Invalid indices.");
        }
    }

    public MemoryBlock[] getHeap() {
        return heap;
    }


    public void printHeapStatus() {
        System.out.println("Heap Status:");
        for (int i = 0; i < size; i++) {
            MemoryBlock block = heap[i];
            String status = block.allocated ? "ALLOCATED" : "FREE";
            System.out.print("Index " + i + ": " + status + ", refs: [");
            for (int j = 0; j < block.refCount; j++) {
                System.out.print(block.references[j]);
                if (j < block.refCount - 1) System.out.print(", ");
            }
            System.out.println("]");
        }
    }

}
