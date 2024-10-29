Wheather Application

- Description: 

The Weather Application is a Java program designed to provide users with instant, real-time weather data for selected locations. Weather information is obtained through an external API and displayed in a graphical user interface (GUI). Users enter a location, and the application gathers and presents relevant weather details, including temperature, weather conditions, humidity, and wind speed. This documentation outlines the project’s architecture, implemented technologies, and describes the role of each class within the application.

•	Links to the sources needed to carry out the project:  

1.	JSON JAR: https://code.google.com/archive/p/json-simple/downloads
2.	Weather Forecast API: https://open-meteo.com/en/docs#latitude=33.767&longitude=-118.1892
3.	Geolocation API: https://open-meteo.com/en/docs/geocoding-api
4.	Images used for the Project are located in the „scr-Icons”

![Screenshot 2024-10-29 141322](https://github.com/user-attachments/assets/418835f0-efd1-44b2-9d3c-1af66388fbbb)

•	Technologies Used: 

-	Java 18
- JSON Simple - Used to parse and read through JSON data
- HTTPURLConnection - Java's built-in library for making HTTP requests to fetch data from external APIs.

•	Overview of classes

-	Main
Description: The Main class functions as the initial access point of the Weather Application. It is tasked with launching the graphical interface and displaying the primary application window.

-	AppInterface

Description: The AppInterface class defines the graphical user interface (GUI) layout and manages the visual display of weather information for user-specified locations.

Summary: This class organizes and renders the GUI components, including fields for input, labels, buttons, and images. It provides the interface for users to enter location information and refreshes the weather display according to user selections.

-	 AppFunctionability
  
Description: The AppFunctionability class manages the backend operations required to retrieve weather data from an external API. It gathers geographical coordinates for specific locations, pulls corresponding weather data, and offers functions for translating weather codes.

Summary: Serving as the core logic component, this class encompasses methods for obtaining weather data, converting location information, interpreting weather codes into readable formats, and managing API requests. It functions as the intermediary between the GUI and the external data source, ensuring accurate weather data retrieval and display.

Here are some pictures of some tests of the app:

![Screenshot 2024-10-29 132859](https://github.com/user-attachments/assets/c5979f5e-e877-4eee-a026-dad805d1acfa)

![Screenshot 2024-10-29 132911](https://github.com/user-attachments/assets/323a3b4d-26a1-4d28-8c98-0224e4854a47)

![Screenshot 2024-10-29 133028](https://github.com/user-attachments/assets/c705eb59-b5e3-42ed-8d74-1bf16885aee9)
