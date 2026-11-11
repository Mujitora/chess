package client;


import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ServerFacade {
   private String serverURL;

   public ServerFacade(int port){
       serverURL = "http://localhost:" + port;
   }


   private HttpClient httpClient = HttpClient.newHttpClient();

   public void clear() throws ResponseExeption{
       HttpRequest request = HttpRequest.newBuilder()
               .uri(URI.create(serverURL + "/db"))
               .DELETE()
               .build();
       try{
           HttpResponse<String>response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
           if (response.statusCode() !=200){
               throw new ResponseExeption("could not clear database");
           }
       }  catch(IOException | InterruptedException e){
           throw new ResponseExeption("could not access server");
       }
   }

}



