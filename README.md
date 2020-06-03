 # Kalaha Game REST API
REST API for Kalaha game.

Operations covered,

| Operation       | Description    | [RequestType]:Request URL |
| :------------- | :----------: | -----------: |
| new game | Creates a new game for the player  |[POST]:http://domain-name/kalaha-api/v1/game    |
| join game   | Joins an existing game |[PUT]:http://domain-name/kalaha-api/v1/game/{game-id} |
| play   | Play/make move on the game for given player |[PUT]:http://domain-name/kalaha-api/v1/game/play/{game-id} |
| game state   | Gets the current game state and board for the given player|[PATCH]:http://domain-name/kalaha-api/v1/game/{game-id}/{player-id} |
| game info   | Gets the game info for the given game|[GET]:http://domain-name/kalaha-api/v1/game/{game-id}|

# Tools and Technologies used
  * [Spring Boot](https://spring.io/blog/2019/10/16/spring-boot-2-2-0)
  * [Java 8](https://docs.oracle.com/javase/8/docs/)
  * [Junit 5](https://junit.org/junit5/docs/current/user-guide/)
  * [Swagger 2.0](https://swagger.io/docs/specification/2-0/basic-structure/)
  * [Lombok](https://projectlombok.org/)
  * [MongoDB](https://www.mongodb.com/)

Note : For the purpose of reducing the external dependency with the database, embedded mongo db has been used for development purpose
  
# How to Run and Test application
   [maven command =>] mvn spring-boot:run
   
   Link : http://localhost:8080/kalaha-api/v1/game/
	
# Swagger Documentation 
  http://localhost:8080/kalaha-api/v1/swagger-ui.html#/

# To-Dos
    - AI implementation
    - User authentication
# Team and Mailing Lists
    - praveen.itian@gmail.com

