# ♕ BYU CS 240 Chess

This project demonstrates mastery of proper software design, client/server architecture, networking using HTTP and WebSocket, database persistence, unit testing, serialization, and security.

## 10k Architecture Overview

The application implements a multiplayer chess server and a command line chess client.

[![Sequence Diagram](10k-architecture.png)](https://sequencediagram.org/index.html#initialData=C4S2BsFMAIGEAtIGckCh0AcCGAnUBjEbAO2DnBElIEZVs8RCSzYKrgAmO3AorU6AGVIOAG4jUAEyzAsAIyxIYAERnzFkdKgrFIuaKlaUa0ALQA+ISPE4AXNABWAexDFoAcywBbTcLEizS1VZBSVbbVc9HGgnADNYiN19QzZSDkCrfztHFzdPH1Q-Gwzg9TDEqJj4iuSjdmoMopF7LywAaxgvJ3FC6wCLaFLQyHCdSriEseSm6NMBurT7AFcMaWAYOSdcSRTjTka+7NaO6C6emZK1YdHI-Qma6N6ss3nU4Gpl1ZkNrZwdhfeByy9hwyBA7mIT2KAyGGhuSWi9wuc0sAI49nyMG6ElQQA)

## Modules

The application has three modules.

- **Client**: The command line program used to play a game of chess over the network.
- **Server**: The command line program that listens for network requests from the client and manages users and games.
- **Shared**: Code that is used by both the client and the server. This includes the rules of chess and tracking the state of a game.

## Starter Code

As you create your chess application you will move through specific phases of development. This starts with implementing the moves of chess and finishes with sending game moves over the network between your client and server. You will start each phase by copying course provided [starter-code](starter-code/) for that phase into the source code of the project. Do not copy a phases' starter code before you are ready to begin work on that phase.

## IntelliJ Support

Open the project directory in IntelliJ in order to develop, run, and debug your code using an IDE.

## Maven Support

You can use the following commands to build, test, package, and run your code.

| Command                    | Description                                     |
| -------------------------- | ----------------------------------------------- |
| `mvn compile`              | Builds the code                                 |
| `mvn package`              | Run the tests and build an Uber jar file        |
| `mvn package -DskipTests`  | Build an Uber jar file                          |
| `mvn install`              | Installs the packages into the local repository |
| `mvn test`                 | Run all the tests                               |
| `mvn -pl shared test`      | Run all the shared tests                        |
| `mvn -pl client exec:java` | Build and run the client `Main`                 |
| `mvn -pl server exec:java` | Build and run the server `Main`                 |

These commands are configured by the `pom.xml` (Project Object Model) files. There is a POM file in the root of the project, and one in each of the modules. The root POM defines any global dependencies and references the module POM files.

## Running the program using Java

Once you have compiled your project into an uber jar, you can execute it with the following command.

```sh
java -jar client/target/client-jar-with-dependencies.jar

♕ 240 Chess Client: chess.ChessPiece@7852e922
```

## Chess Server Design Link

https://sequencediagram.org/index.html?presentationMode=readOnly#initialData=IYYwLg9gTgBAwgGwJYFMB2YBQAHYUxIhK4YwDKKUAbpTngUSWDABLBoAmCtu+hx7ZhWqEUdPo0EwAIsDDAAgiBAoAzqswc5wAEbBVKGBx2ZM6MFACeq3ETQBzGAAYAdAE5M9qBACu2AMQALADMABwATG4gMP7I9gAWYDoIPoYASij2SKoWckgQaJiIqKQAtAB85JQ0UABcMADaAAoA8mQAKgC6MAD0PgZQADpoAN4ARP2UaMAAtihjtWMwYwA0y7jqAO7QHAtLq8soM8BICHvLAL6YwjUwFazsXJT145NQ03PnB2MbqttQu0WyzWYyOJzOQLGVzYnG4sHuN1E9SgmWyYEoAAoMlkcpQMgBHVI5ACU12qojulVk8iUKnU9XsKDAAFUBhi3h8UKTqYplGpVJSjDpagAxJCcGCsyg8mA6SwwDmzMQ6FHAADWkoGME2SDA8QVA05MGACFVHHlKAAHmiNDzafy7gjySp6lKoDyySIVI7KjdnjAFKaUMBze11egAKKWlTYAgFT23Ur3YrmeqBJzBYbjObqYCMhbLCNQbx1A1TJXGoMh+XyNXoKFmTiYO189Q+qpelD1NA+BAIBMU+4tumqWogVXot3sgY87nae1t+7GWoKDgcTXS7QD71D+et0fj4PohQ+PUY4Cn+Kz5t7keC5er9cnvUexE7+4wp6l7FovFqXtYJ+cLtn6pavIaSpLPU+wgheertBAdZoFByyXAmlDtimGD1OEThOFmEwQZ8MDQcCyxwfECFISh+xXOgHCmF4vgBNA7CMjEIpwBG0hwAoMAADIQFkhRYcwTrUP6zRtF0vQGOo+RoFmipzGsvz-BwVygYKQH+uB5afIs3xqTsewNjp8K+s6XYwAgQnihignCQSRJgKSKrBhqACy2SqOKjjKYY0AwMZALboYSaWRJpYAEIhs5ahgFGMZxoUWkRfAyCpjA6b4aMYw5qoebzNBRYlvUejriihIJQ29FhYKw78gyTJTgFc40vu97CjAYoSm6MpymW7xKpg7nqjAAByEBDTAABmvgSjqeowHqchDZyN4dXe6VvtZPZ9vVO3VP6zLTJe0BIAAXigHBJSgsYKeh8LJpl2HZU4ACMBEFUVBZjKV0D1D4Z16hd127HRTaNYu4lUEiG7uluY0ak0+h-DsRgQGoMBoBAzDHGAIDxIdsMnSD8Rgzdd0PfGaUvSUYBpp9338r9JXFoDCrk5TEONgxu0NbeTUwIecgoM+8Tnpe17QwKS7dY+AbS1uAvpeZ9SOeKGSqABmDmSBx1gYR+nFWM3wUVR9aGZphuYa9jMwLhuV6cNBlm7Bl6W8h1t84x3h+P4XgoOgMRxIkQch45vhYKJgqgfUDTSBG-ERu0EbdD0cm+QUwwW4h6BPdpjxwvUedIXrxcYbD8O2fY0dS-B+doKSqtUkL9IwIyYASw3lFN+1vLbZUy49eKT7K-IsrymXBfI5N00SzK83A+u82wDPhSywbnbdr2-aq6TpanRRPPUylhfpaJTNfXlP35uzZVcyfUBXTdtVQ+3cvVy6SsvirVmCy2sLDgKBuDHkvL3L2A8Fxy2Ht1aQoCmSGEXv-Ts7Z1YwB1vuCusIq6HxeDbKKdsGY4TwlmSGDFPD+wCCidc-hsDig1PxNEMAADiSoNCx3wY0VhqcM72CVLnT2TcL6VAwRvHBX445WXqMgHI7CcyQP7mFCostahdx7hvaB+4KjLl6uPP+k9BoSLnlNX+V5tBzQWqvIKEjZYRV2rUfa+8rIOMNrUY+50X7gyjPdc+dNKhX3ejfbMrN76Fg5qWYGz9X68zqlvI6O9zGvlceUNRciwAKLUBibRI5dHCn0WwpUHp7EVEcVklJnYIrqwyVk1Q2tdbmTcVFWoIxlgCJzAWBo4wOkoAAJLSALB9cIwRAggk2PEXUKA3SclMiCZIoA1QzMgoZEEvSJpKmtjATohCagRSvk7LM7SOFdJ6UqAZQyRljOWBMqZyy3bfAWSAJZRFTbfHWZss2FxtkUL9sxfwHAADsbgnAoCcDECMwQ4BcQAGzwAnIYLJMAij22kS0xorQOj8MEeTL2WYPlzF2VXMRldSwbzWCMAlKBoSkrRXDH+ot0RZKUUhClVKLgt1SWojRECtGbUHvyfJoox7JMscY4R5dTELwnsAKxK8rHrwlQXUp5RHHOJUWU9xnjQbeKppaPxj0AkZRIcElmuZwn-UiUDbmuq4kfyATDSK9LrIoPkKNVUGozG9JxiNA+Tr-SxQ4PFHIZ9DW20vvbJmzt8phOKhEx+FUYBVRcu-fmXLP5jgRRU7QGJKVKg2YS3JgrygPjXEUuYlTnXVNJeopUcAEUKAaQgQCpLmk1FaccuYFz6jDNGTAIlUB9mRsOaMTt-TBk9quf232VD-mWFAbZTYockAJDAPOvsEAl0ACkIDinLYYfwTy1QooZnSySTRmQyR6L0oRjckJZmwAgYA86oBwAgLZKAaxekDIHUXXBZKlVoApesJ9L630fr2AAdRYH0tOPRor8QUHAAA0u885E6YC9sCNO-W39rIACtd1oGZeSmAbSfigcoOB6AUGYNwYQ0h1Daz0OXL7RylRaSM08rPHy+xJaCkitdbK8Vd7Z4evnqKyey8JRr2NIB-lMC21InVbtNtzxtUU1tb4mmqVw30yyumEJMbzVxstY-aJXjYmpoU51RJzr6hCY49ypkzLv3SCLeoIVhS3Puo8hJtzcr7UCs85qneKnXGhfbYG4NiV9U6Yvvpt6OUzWFQtQDUsibk01V+Sq8pxSkbieo+vKs5oVrhl02guz-pAxmksGGJCobaZ6cCZG96mZR3GdS6Z9LpcSs1nK0sX5rcOz2dlD4U464-BaHROubNk8lr6jQCgJdj7n0YRDCAtNlW24OtHAqbA02UDMqm2LDgc3gAedgUKeoGRVvenOwqzu6GNXlHVoR8UWSm0tv-WpuoIwB1DpISO-7M7TCzoDl4Z9y7V2Q-lIgYM69sCPsIHkAoyKuH+tLInZOqd069GMKIh4-76ggG4HgJNKBqo5EkcBPDJOydQByYdHbwW9uk4R26VQTOElwPqAgsBhgTQIARpWwcLOYH04R3U7nn8up88QeiSswvzvM5kBmkWDOe6XblzIBXgu+ySeACTElxPME+Gwbh7hIOjVBOB78oAA
