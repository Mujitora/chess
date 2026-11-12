package client;

import model.AuthData;
import org.junit.jupiter.api.*;
import server.Server;


public class ServerFacadeTests {

    private static Server server;
    static ServerFacade facade;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        facade = new ServerFacade(port);
    }
    @BeforeEach
    public void setup() throws ResponseException {
        facade.clear();
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @Test
    public void sampleTest() {
        Assertions.assertTrue(true);
    }



    @Test
    public void registerPositive() throws ResponseException{
        AuthData auth = facade.register("bob", "monkey", "bobmonkey@gmail.com");
        Assertions.assertEquals("bob", auth.username());
        Assertions.assertNotNull(auth.authToken());
        Assertions.assertFalse(auth.authToken().isEmpty());
    }

    @Test
    public void registerAlreadyTakenUsername() throws ResponseException{
        facade.register("bob", "monkey", "bobmonkey@gmail.com");
        Assertions.assertThrows(ResponseException.class, () ->
                facade.register("bob", "monkey", "bobmonkey@gmail.com"));
    }



}
