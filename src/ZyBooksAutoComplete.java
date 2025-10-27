import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import java.util.Vector;

import com.google.gson.Gson;

public class ZyBooksAutoComplete {
    private String code;
    private String JWT_USER_KEY;
    private ZyBooksAllAssignments assignments;
    private Vector<ZyBooksActivityMetadata> remainingQuestions = new Vector<ZyBooksActivityMetadata>();

    public ZyBooksAutoComplete(String zyBook_code, String PRIVATE_USER_KEY){
        code = zyBook_code;
        if (PRIVATE_USER_KEY == null){
            System.out.println("Please paste in your JWT user identifier");
            Scanner stdin = new Scanner(System.in);
            JWT_USER_KEY = stdin.nextLine();
            System.out.println(JWT_USER_KEY);
        } else
            JWT_USER_KEY = PRIVATE_USER_KEY;
        System.out.println("Fetching uncompleted ZyBooks questions...");
        assignments = fetchAssignments();
        int index = 0;
        for(ZyBooksAssignment assignment : assignments)
            for(ZyBooksActivityData activity : assignment)
                    for(ZyBooksActivity question : activity){
                        index += question.getMetadata().getParts();
                        remainingQuestions.add(question.getMetadata());
                    }
        System.out.println("Successfully fetched " + index + " uncompleted questions");
    }

    private String HOST = "https://zyserver.zybooks.com/v1/";

    private String bookURI() {return HOST + "zybook/" + code;}

    private String assignmentsURI() {return bookURI() + "/assignments";}

    private String authorizer() {return "Bearer " + JWT_USER_KEY;}

    private String contentURI(int contentID) {return HOST + "content_resource/" + contentID + "/activity";}

    private ZyBooksAllAssignments fetchAssignments(){
        try {
            URL url = new URL(assignmentsURI());
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("authorization", authorizer());
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer content = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();
            String response = content.toString();
            ZyBooksAllAssignments allAssignments = new Gson().fromJson(response, ZyBooksAllAssignments.class);
            return allAssignments;
        }
        catch (Exception e){
            System.out.println("An exception has occurred in method fetchAssignments");
            System.out.println(e);
            return null;
        }
    }

    public void completeEverything(){
        for (ZyBooksActivityMetadata question : remainingQuestions){
            if (question.getResourceType().equals("custom")){
                if(question.getTool().equals("zyAnimator")){
                    System.out.println(question);
                    autoCompleteAnimator(question);
                }
            }
        }
    }

    private void autoCompleteAnimator(ZyBooksActivityMetadata question){
        try {
            URL url = new URL(contentURI(question.getContentResourceID()));
            System.out.println(url);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setDoOutput(true);
            con.setRequestMethod("POST");
            con.setRequestProperty("authorization", authorizer());

            OutputStream bodyStream = con.getOutputStream();
            OutputStreamWriter bodyWriter = new OutputStreamWriter(bodyStream, "UTF-8");
            bodyWriter.write("{\"part\":0,\"complete\":true,\"metadata\":\"{\\\"event\\\":\\\"start clicked.\\\",\\\"isTrusted\\\":{\\\"isTrusted\\\":true},\\\"computerTime\\\":\\\"2025-10-27T05:12:33.571Z\\\"}\",\"zybook_code\":\"" + code + "\",\"timestamp\":\"2025-10-27T05:12:33.572Z\",\"__cs__\":\"c7c78430ea8cbeb0724a505f8366f31a\"}");
            //YouLikeKissingBoysDontYouuuuuBecauseYouAreABoyKisser
            bodyWriter.flush();
            bodyWriter.close();
            bodyStream.close();  //don't forget to close the OutputStream
            con.connect();
        }
        catch(Exception e){
            System.out.println("An exception has occurred in method autoCompleteAnimator");
            System.out.println(e);
        }
    }

}