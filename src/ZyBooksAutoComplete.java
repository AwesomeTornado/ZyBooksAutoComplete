import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Scanner;
import java.util.Vector;
import java.security.MessageDigest;

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

    //https://stackoverflow.com/questions/9655181/java-convert-a-byte-array-to-a-hex-string
    private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();
    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars);
    }

    private String get_buildkey(){
        return "45242603-99c8-4df4-e466-df5f14656d0c";
        //fuch this shit this is so hard to extract.
        //resolveRegistration("config:environment")["APP"]["BUILDKEY"]
    }

    private String checksum(int contentID, String ts){
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            String path = "content_resource/" + contentID + "/activity" + ts + JWT_USER_KEY + contentID + "0" + "true" + get_buildkey();
            return bytesToHex(md.digest((path).getBytes())).toLowerCase();
            //data = f"content_resource/{act_id}/activity{ts}{auth}{act_id}{part}true{get_buildkey()}"
        }
        catch (Exception e){
            System.out.println("An exception has occurred in method checksum");
            System.out.println(e);
            return null;
        }
    }

    private void autoCompleteAnimator(ZyBooksActivityMetadata question){
        try {

            LocalDateTime currentDateTime = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
            //2025-10-27T05:12:33.571Z
            String timestamp = currentDateTime.format(formatter);
            timestamp = timestamp.replace(" ", "T");
            System.out.println("Current Timestamp: " + timestamp);
            String checksum = checksum(question.getContentResourceID(), timestamp);
            System.out.println("Current Checksum: " + checksum);

            URL url = new URL(contentURI(question.getContentResourceID()));
            System.out.println(url);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setDoOutput(true);
            con.setRequestMethod("POST");
            con.setRequestProperty("authorization", authorizer());

            OutputStream bodyStream = con.getOutputStream();
            OutputStreamWriter bodyWriter = new OutputStreamWriter(bodyStream, "UTF-8");
            String body = "{\"part\":0,\"complete\":true,\"metadata\":\"{\\\"event\\\":\\\"start clicked." +
                    "\\\",\\\"isTrusted\\\":{\\\"isTrusted\\\":true},\\\"computerTime\\\":\\\"" + timestamp +
                    "\\\"}\",\"zybook_code\":\"" + code + "\",\"timestamp\":\"" + timestamp +
                    "\",\"__cs__\":\""+ checksum + "\"}";
            bodyWriter.write(body);
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