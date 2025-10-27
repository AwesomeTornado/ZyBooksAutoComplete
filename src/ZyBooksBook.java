import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class ZyBooksBook {
    private String code;
    private String JWT_USER_KEY;
    private ZyBooksAssignment[] remainingProblems;

    public ZyBooksBook(String zyBook_code, String PRIVATE_USER_KEY){
        code = zyBook_code;
        if (PRIVATE_USER_KEY == null){
            Scanner stdin = new Scanner(System.in);
            JWT_USER_KEY = stdin.nextLine();
        } else
            JWT_USER_KEY = PRIVATE_USER_KEY;
        remainingProblems = fetchAllAssignments();
    }

    private String bookURI() {return "https://zyserver.zybooks.com/v1/zybook/" + code + "/";}

    private String assignmentsURI() {return bookURI() + "assignments/";}

    private String authorizer() {return "Bearer " + JWT_USER_KEY;}

    private ZyBooksAssignment[] fetchAllAssignments() throws Exception {
        URL url = new URL(assignmentsURI());
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("authorization", authorizer());
        throw new Exception("AAA");
    }


}
