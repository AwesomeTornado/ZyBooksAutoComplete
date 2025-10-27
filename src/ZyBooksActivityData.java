import java.util.Arrays;
import java.util.Iterator;
import java.util.function.Consumer;

public class ZyBooksActivityData implements Iterable<ZyBooksActivity> {
    private int chapter_number;
    private int section_number;
    private long section_id;
    private ZyBooksActivity[] section_activity_data;

    @Override
    public Iterator<ZyBooksActivity> iterator() {
        return Arrays.stream(section_activity_data).iterator();
    }

    @Override
    public void forEach(Consumer action) {
        Iterable.super.forEach(action);
    }
}
