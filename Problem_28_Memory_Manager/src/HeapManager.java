package Problem_28_Memory_Manager.src;

public class HeapManager {


    public void freeBlock(int index) {
        if (index >= 0 && index < size && heap[index].allocated) {
            heap[index].allocated = false;
            heap[index].visited = false;
            heap[index].clearReferences(); // fixed
        }
    }

    public void addReference(int fromIndex, int toIndex) {
        if (fromIndex >= 0 && fromIndex < heap.length &&
                toIndex >= 0 && toIndex < heap.length) {

            if (heap[fromIndex].allocated && heap[toIndex].allocated) {
                heap[fromIndex].addReference(toIndex);
                System.out.println("Reference added: Block " + fromIndex + " -> Block " + toIndex);
            } else {
                System.out.println("Both blocks must be allocated to create a reference.");
            }

        } else {
            System.out.println("Invalid block indices.");
        }
    }


}



