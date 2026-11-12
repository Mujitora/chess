package client;


import com.google.gson.Gson;
import model.AuthData;
import model.UserData;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

public class ServerFacade {
   private String serverURL;

   public ServerFacade(int port){
       serverURL = "http://localhost:" + port;
   }


   private HttpClient httpClient = HttpClient.newHttpClient();

   public void clear() throws ResponseException{
       HttpRequest request = HttpRequest.newBuilder()
               .uri(URI.create(serverURL + "/db"))
               .DELETE()
               .build();
       try{
           HttpResponse<String>response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
           if (response.statusCode() !=200){
               throw new ResponseException("could not clear database");
           }
       }  catch(IOException | InterruptedException e){
           throw new ResponseException("could not access server");
       }
   }

   public AuthData register(String username, String password, String email) throws ResponseException{
        String json = new Gson().toJson(new UserData(username, password, email));
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(serverURL + "/user"))
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
        try{
            HttpResponse<String>response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 403){
                throw new ResponseException("Username already taken");
            }
            if (response.statusCode() != 200){
                throw new ResponseException("Could not register");
            }
            return new Gson().fromJson(response.body(), AuthData.class);
        } catch(IOException | InterruptedException e) {
            throw new ResponseException("Could not reach the server");
        }
   }

   public AuthData login(String username, String password) throws ResponseException{
       String json = new Gson().toJson(Map.of("username", username, "password", password));
       HttpRequest request = HttpRequest.newBuilder()
               .uri(URI.create(serverURL + "/session"))
               .POST(HttpRequest.BodyPublishers.ofString(json))
               .build();
       try{
           HttpResponse<String>response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
           if(response.statusCode() == 401){
               throw new ResponseException("Username or Password is incorrect");
           }
           if (response.statusCode() != 200){
               throw new ResponseException("Unable to login");
           }
           return new Gson().fromJson(response.body(), AuthData.class);
       } catch (IOException | InterruptedException e){
           throw new ResponseException("Could not reach the server");
       }
   }



}



