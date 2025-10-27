import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;
import java.util.Map;
import com.google.gson.Gson;

public class ZyBooksBook {
    private String code;
    private String JWT_USER_KEY;
    private ZyBooksAllAssignments remainingProblems;

    public ZyBooksBook(String zyBook_code, String PRIVATE_USER_KEY){
        code = zyBook_code;
        if (PRIVATE_USER_KEY == null){
            System.out.println("Please paste in your JWT user identifier");
            Scanner stdin = new Scanner(System.in);
            JWT_USER_KEY = stdin.nextLine();
            System.out.println(JWT_USER_KEY);
        } else
            JWT_USER_KEY = PRIVATE_USER_KEY;
        remainingProblems = fetchAllAssignments();
        int index = 0;
        for(ZyBooksAssignment a : remainingProblems)
            for(ZyBooksActivityData b : a)
                    for(ZyBooksActivity c : b){
                            System.out.println(index++);
                            System.out.println(c.getMetadata().toString());
                        }



    }

    private String bookURI() {return "https://zyserver.zybooks.com" + pathURI();}

    private String assignmentsURI() {return bookURI() + "/assignments";}

    private String pathURI() {return "/v1/zybook/" + code;}

    private String authorizer() {return "Bearer " + JWT_USER_KEY;}

    private ZyBooksAllAssignments fetchAllAssignments(){
        try {
            URL url = new URL(assignmentsURI());
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("authorization", authorizer());
            con.setRequestProperty("path", pathURI() + "/assignments");
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer content = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();
            String response = content.toString();
            ZyBooksAllAssignments allAssignments = new Gson().fromJson(response, ZyBooksAllAssignments.class);
            System.out.println(allAssignments.getAssignment(0).toString());
            return allAssignments;
        }
        catch (Exception e){
            System.out.println("An exception has occurred");
            System.out.println(e);
            return null;
        }
    }


}