package client;


import com.google.gson.Gson;
import com.google.gson.JsonObject;
import model.AuthData;
import model.GameData;
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
            throw new ResponseException("Unable to reach server");
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
           throw new ResponseException("Unable to reach server");
       }
   }


   public void logout(String authToken) throws ResponseException{
       HttpRequest request = HttpRequest.newBuilder()
               .uri(URI.create(serverURL + "/session")).header("authorization",authToken)
               .DELETE().header("authorization", authToken)
               .build();
        try{
            HttpResponse<String>response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if(response.statusCode() == 401){
                throw new ResponseException("Unauthorized");
            }
            if(response.statusCode() != 200){
                throw new ResponseException("Unable to logout");
            }
        } catch (IOException | InterruptedException e){
            throw new ResponseException("Unable to reach server");
        }
   }


   public int createGame(String authToken, String gameName) throws ResponseException{
       String json = new Gson().toJson(Map.of("gameName", gameName));
       HttpRequest request = HttpRequest.newBuilder()
               .uri(URI.create(serverURL + "/game"))
               .header("authorization",authToken)
               .POST(HttpRequest.BodyPublishers.ofString(json))
               .build();
       try{
           HttpResponse<String>response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
           if(response.statusCode() == 401){
               throw new ResponseException("You are not logged in");
           }
           if(response.statusCode() == 400){
               throw new ResponseException("Please enter a valid game name");
           }
           if(response.statusCode() != 200){
               throw new ResponseException("Unable to create game");
           }
           return new Gson().fromJson(response.body(), JsonObject.class).get("gameID").getAsInt();
       } catch (IOException | InterruptedException e){
           throw new ResponseException("Unable to reach server");
       }
   }

   public GameData[] listGames(String authToken) throws ResponseException{
       HttpRequest request = HttpRequest.newBuilder()
               .uri(URI.create(serverURL + "/game"))
               .header("authorization", authToken)
               .GET()
               .build();
       try {
           HttpResponse<String>response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
           if(response.statusCode() == 401){
               throw new ResponseException("You are not logged in");
           }
           if(response.statusCode() != 200){
               throw new ResponseException("Unable to list games");
           }
           JsonObject root = new Gson().fromJson(response.body(), JsonObject.class);
           return new Gson().fromJson(root.get("games"), GameData[].class);
       } catch (IOException | InterruptedException e){
           throw new ResponseException("Unable to reach server");
       }
   }







}



