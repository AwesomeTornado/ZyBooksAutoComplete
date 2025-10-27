import java.util.Arrays;
import java.util.Iterator;
import java.util.function.Consumer;

public class ZyBooksAssignment implements Iterable<ZyBooksActivityData>{
    private int assignment_id;
    private int creator_user_id;
    private ZyBookSection[] sections;
    private String due_dates;
    private Object lti_consumer_resources;
    private String title;
    private boolean submission_link_available;
    private boolean visible;
    private int zybook_id;
    private Object points_recorded_in_lms;

    private ZyBooksActivityData[] activity_data;

    @Override
    public Iterator<ZyBooksActivityData> iterator() {
        return Arrays.stream(activity_data).iterator();
    }

    @Override
    public void forEach(Consumer action) {
        Iterable.super.forEach(action);
    }


    public String toString(){
        return title;
    }


    //public abstract void completeQuestion();
}
