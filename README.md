# FlightSimulatorSystem
This project is a flight simulator Application, connected to the Flight-Gear program as a projector. 
The project is written in Java, and divided into three parts:


*The Frontend* - written using the MVVM Design Pattern.

*The Agent* - written using the MVC Design Pattern.

<mark> The Backend </mark>- written using the MVC Design Pattern.


 Flight Simulator System.
 
 ## The architecture:


![image](https://user-images.githubusercontent.com/93862727/175337932-40ff33ee-b858-4f2c-a73b-bac76e3a4699.png)


![image](https://user-images.githubusercontent.com/93862727/175339350-0ee55686-7eaa-44fd-97c4-3cffa27d8c39.png)


![image](https://user-images.githubusercontent.com/93862727/175339404-4e5d069f-f460-4b6f-8cc2-97e024d78bd8.png)


## Frontend:


![image](https://user-images.githubusercontent.com/93862727/175351730-83c7a14b-f052-49a0-8079-b42db55a6438.png)



This part of the project devided into four tabs: Fleet Overview, Monitoring, Teleoperation, Time Capsule.


### Fleet Overview:
This tab presents all of the planes presented live on the map with graphs containing the fleet information below.
It includes a single plane information feature, double click feature, zooming the map and graphs refresh button.


### Monitoring:
In this tab you can easly pick a plane, select a feature and see the graph for the desired feature you picked. This tab also includes 

the pilot joystick (in disabled mode), Correlated graph (to the selected feature) and the Cockpit Clock Table.

### Teleoperation:
Through this tab you can control the plane.

Automatically - you can upload or manually enter a code to the text box.  

Manually - you can control the joystick by yourself!

Also here, you have the Clock Table view below.

### Time Capsule:
In this tab you can watch previous flights. Enter a flight index and choose desired plane feature. The joystick feature is in *disabled mode*.

You can also play, pause and change the video speed.


## Agent:



## Backend:
The Backend supports server-side application. It is built with MVC architecture and divided into two parts:

#### Controller - Manages all the information flow inside and outside of the application. 


In Inside aspect, the application and the information flow created by *Observer* Design Pattern. 


The Outside aspect divided into two parts: 

1. communicating with the agent and using sockets with TCP. 

2. comuunicating with the Frontend, by using HTTP server.


#### Model - has two main parts:


1. Interperter - Recives code block from the Frontend and deploys interpretation, by using "lexer", "parser" and commands pattern. It sends commands by the following flow: Interpreter -> Model -> Controller -> Agent.


2.  DB - In oreder to store our data, we used Mongo DB API. It stores information about the airplane fleet and enables information withdraw to the front side.


## Implements
* Java | XML | CSS

## Built With
* Intellij - Java IDE
* Scene Builder
* Mongo DB
* Maven
* Postman 

# Authors
Shahar Kozenyuk | Ofir Gur Cohen | Inon Angel | Guy Barzily | Roey Rachmany | Roy Ambar | Gal Levy | Yuval Kopelman | Or Ben Nun



