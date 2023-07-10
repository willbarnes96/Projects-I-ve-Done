/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package pkg.presidioproject;


import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.apache.http.client.methods.HttpUriRequest;
import org.json.JSONArray;
import org.json.JSONObject;
/**
 *
 * @author willbarnes
 */
public class PresidioProject {
    
    public static String apiKey = "AIzaSyBhJuQsh-r8nJRb7VTOiebfHahwCDIvFPE";
    public static String searchEngineId = "97849fec2db774282";
    
    private static final Map<String, String> reservedCharacters;

    static {
        reservedCharacters = new HashMap<>();
        reservedCharacters.put("!", "%21");
        reservedCharacters.put("#", "%23");
        reservedCharacters.put("$", "%24");
        reservedCharacters.put("&", "%26");
        reservedCharacters.put("'", "%27");
        reservedCharacters.put("(", "%28");
        reservedCharacters.put(")", "%29");
        reservedCharacters.put("*", "%2A");
        reservedCharacters.put("+", "%2B");
        reservedCharacters.put(",", "%2C");
        reservedCharacters.put("/", "%2F");
        reservedCharacters.put(":", "%3A");
        reservedCharacters.put(";", "%3B");
        reservedCharacters.put("=", "%3D");
        reservedCharacters.put("?", "%3F");
        reservedCharacters.put("@", "%40");
        reservedCharacters.put("[", "%5B");
        reservedCharacters.put("]", "%5D");
        reservedCharacters.put(" ", "%20");
    }
    
    
    //using reservedCharacters to properly format the query 
    public static String formatQuery(String query) {
        
        String formattedQuery="";
        
        String decodedValue;
        String encodedValue;
        
        for (int i = 0; i < query.length(); i++) {
            if (!Character.isLetter(query.charAt(i))) {
                //System.out.println(query.charAt(i) + " is not a letter.");
                
                decodedValue = Character.toString(query.charAt(i));
                encodedValue = reservedCharacters.get(decodedValue);
                //System.out.println("Encoded Value: " + encodedValue);
                
//                query.charAt(i) = (ch)encodedValue;

                formattedQuery += encodedValue;
                
            } else {
                //System.out.println(query.charAt(i) + " is a letter.");
                
                formattedQuery += query.charAt(i);
            }
        }
        
        //System.out.println(formattedQuery + " final\n");
        return formattedQuery;
        
    }
    
    
    public static void searchQuery(String query) throws IOException {
        
        String url = "https://www.googleapis.com/customsearch/v1?key=" + apiKey +
                     "&cx=" + searchEngineId + "&q=" + query;

        HttpClient client = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(url);
        HttpResponse response = client.execute(request);

        HttpEntity entity = response.getEntity();
        String jsonString = EntityUtils.toString(entity);

        JSONObject json = new JSONObject(jsonString);
        JSONArray items = json.getJSONArray("items");

        for (int i = 0; i < items.length(); i++) {
            JSONObject item = items.getJSONObject(i);
            String title = item.getString("title");
            String link = item.getString("link");
            System.out.println(title + " - " + link);
            
            // Check if the result has a featured snippet
            if (item.has("pagemap") && item.getJSONObject("pagemap").has("metatags") &&
                item.getJSONObject("pagemap").getJSONArray("metatags").getJSONObject(0).has("og:description")) {
                String featuredSnippet = item.getJSONObject("pagemap")
                        .getJSONArray("metatags")
                        .getJSONObject(0)
                        .getString("og:description");

                System.out.println("Featured Snippet: " + featuredSnippet);
            }
            
            System.out.println("");
        }
    }
    

    public static void main(String[] args) throws Exception {

        
        int choice = 0;
        String query = "";
        
        //to activate other choices later
        boolean queryMade = false;
        
        do {
            System.out.println("*****************Menu*****************");
            System.out.println("1: Make New Query. ");
            
            if (queryMade) {
                System.out.println("2: Ask Further Question");
            }
            
            System.out.println("5: Exit. ");
            System.out.println("");
            System.out.print("Enter choice: ");
            Scanner sc = new Scanner(System.in);
            choice = sc.nextInt();
            //System.out.println("");
            
            switch(choice) {
                
                //initial search
                case 1:
                    System.out.println("");
                    Scanner sc2 = new Scanner(System.in);
                    System.out.println("Enter Query: ");
                    
                    query = sc2.nextLine();
                    query = formatQuery(query);
                    searchQuery(query);
                    queryMade = true;
                    break;
                    
                //further question
                case 2:
                    System.out.println("");
                    System.out.println("Enter Your Further Question: ");
                    Scanner sc3 = new Scanner(System.in);
                    String question = sc3.nextLine();
                    
                    //further formatting...
                    query = query.concat(" ");
                    question = query.concat(question);
                    System.out.println(question);
                    question = formatQuery(question);
                    searchQuery(question);
                    break;
                    
                case 5:
                    System.exit(0);
                
            }
                
            
        } while (choice != 5);

        
        
    }
}
