# Rick and Morty App
Simple app that uses the [Rick and Mory API](https://rickandmortyapi.com) to collect Rick and Morty characters data, which than can be accessed.
Characters data is stored in the database, which is updated scheduled, with a certain time interval

# Functionality
Scheduled data update:
Retrieving data from API and updating internal database every day on 8:00 AM.

## Methods

`GET /movie-characters/random` - to get random character data. Responds with single [character](#movie-character-model) 

`GET /movie-characters/by-name?name={name}` - to find [characters] by part of their name. Responds with list of [characters](#movie-character-model)

Also you can get info about application endpoints and test it on `GET /swagger-ui/index.html`

# Project structure

Classic Spring Boot MVC based application architecrture.

## Movie Character model

    {
      id      integer
      name    string
      status  string
      gender  string 
    }
    
# Technologies
 Spring Boot 3.0.2 - starters:
    Web, Data Jpa, Validation, Test
 
 Liquibase
 
 Apache Http Client 4.5.10
 
 PostgreSQL
 
 Project Lombok
 
 Mockito 5.1.0
 
 Swagger
