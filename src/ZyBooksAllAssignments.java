import java.util.Arrays;
import java.util.Iterator;
import java.util.function.Consumer;

public class ZyBooksAllAssignments implements Iterable<ZyBooksAssignment>{
    private boolean success;
    private ZyBooksAssignment[] assignments;
    public ZyBooksAssignment getAssignment(int i){
        return assignments[i];
    }

    @Override
    public Iterator<ZyBooksAssignment> iterator() {
        return Arrays.stream(assignments).iterator();
    }

    @Override
    public void forEach(Consumer action) {
        Iterable.super.forEach(action);
    }
}
