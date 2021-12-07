# Project job4j_chat
[![Build Status](https://app.travis-ci.com/EDGE775/job4j_chat.svg?branch=master)](https://app.travis-ci.com/EDGE775/job4j_chat)
[![codecov](https://codecov.io/gh/EDGE775/job4j_chat/branch/master/graph/badge.svg?token=T2XUH6XQUL)](https://codecov.io/gh/EDGE775/job4j_chat)

> REST API for chat

## Table of contents
* [General info](#general-info)
* [Technologies](#technologies)
* [Installation](#installation)
* [Usage](#usage)
* [Status](#status)
* [Contact](#contact)

## General info
This project is a Spring Boot RESTfull API using Spring Security and JWT for authentication and authorization.
This is a simple service that implements the server side of an online chat.
It uses simple CRUD operations.

## Technologies
* Java 17
* Spring Boot (Web, Data, Security)
* REST API
* JWT
* PostgreSQL, H2 (for tests)
* Maven
* Junit

## Installation
* Create Data Base in PostgreSQL named: "chat"
* Execute the script ```db/schema.sql```
* Execute ```mvn install```
* Execute ```java -jar target/job4j_chat-1.0.jar```
* Default server address: http://localhost:8080/

## Usage
* Registration
  ```
  POST http://localhost:8080/person/sign-up
  Content-Type: application/json
  Request body: 
  {
    "name":"user",
    "email":"user@mail",
    "password":"12345",
    "roleId":"1"
  }
  ```
* Authentication
  ```
  POST http://localhost:8080/person/sign-up
  Content-Type: application/json
  Request body: 
  {
    "name":"user",
    "email":"user@mail",
    "password":"12345",
  }
  ```
  ```
  Response header: Authorization: Bearer ___jwtToken___
  ```
* Get all users
  ```
  GET http://localhost:8080/person
  Header: Authorization: Bearer ___jwtToken___
  ```
* Delete user
  ```
  DELETE http://localhost:8080/person/3
  Header: Authorization: Bearer ___jwtToken___
  ```
* Create room
  ```
  POST http://localhost:8080/person/1/room
  Content-Type: application/json
  Request body: 
  {
    "name":"job4j chat"
  }
  Header: Authorization: Bearer ___jwtToken___
  ```
* Update room
  ```
  PUT http://localhost:8080/person/1/room/2
  Content-Type: application/json
  Request body: 
  {
    "name":"job4j chat upd"
  }
  Header: Authorization: Bearer ___jwtToken___
  ```
* Get room by id
  ```
  GET http://localhost:8080/person/2/room/all
  Header: Authorization: Bearer ___jwtToken___
  ```
* Get all rooms
  ```
  GET http://localhost:8080/person/1/room/2
  Header: Authorization: Bearer ___jwtToken___
  ```  
* Add a person to room
  ```
  PUT http://localhost:8080/person/2/room/1/add
  Header: Authorization: Bearer ___jwtToken___
  ```
* Delete a person from room
  ```
  DELETE http://localhost:8080/person/2/room/1/delete
  Header: Authorization: Bearer ___jwtToken___
  ```
* Delete a room
  ```
  DELETE http://localhost:8080/person/1/room/1
  Header: Authorization: Bearer ___jwtToken___
  ```
* Create a message
  ```
  POST http://localhost:8080/person/1/room/1/message
  Content-Type: application/json
  Request body: 
  {
    "text":"Привет!"
  }
  Header: Authorization: Bearer ___jwtToken___
  ```
* Get all messages from room
  ```
  GET http://localhost:8080/person/1/room/1/message
  Header: Authorization: Bearer ___jwtToken___
  ```  
* Delete a message
  ```
  DELETE http://localhost:8080/person/1/room/1/message?id=3
  Header: Authorization: Bearer ___jwtToken___
  ```

## Status
Project is: _in progress_

## Contact
Created by Khlapov Dmitry - feel free to contact me!
