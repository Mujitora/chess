package client;

public class ServerFacade {
   private String serverURL;

   public ServerFacade(int port){
       serverURL = "http://localhost:" + port;
   }
}
