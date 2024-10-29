import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

// Class meant to bring the data from an experiment site inside
// our application and to correlate them as much as they can offer a result.
public class AppFunctionality {

    // code used for brought the data for given location
    public static JSONObject getWeatherData(String locationName) {

        // get location coordinates using the geolocation API
        JSONArray locationData = (JSONArray) getLocationData(locationName);

        // Extract de Longitude and Latitude data
        JSONObject location = (JSONObject) locationData.get(0);
        double latitude = (double) location.get("latitude");
        double longitude = (double) location.get("longitude");

        // Build the API which request the URL with the location coordinates
        // You are going to find the APY I used in the GitHub-Description
        // there is the place where you will find the APY url.
        String urlString = "https://api.open-meteo.com/v1/forecast?" +
                "latitude=" + latitude + "&longitude=" + longitude +
                "&hourly=temperature_2m,relative_humidity_2m,weather_code,wind_speed_10m";

        try {

            // call the API response
            HttpURLConnection conn = fetchApiResponse(urlString);

            // check the status response
            if (conn.getResponseCode() != 200) {

                System.out.println("Error: Could not connect to API");
                return null;
            }

            // Create a StringBuilder to store the resulting JSON data
            StringBuilder resultJson = new StringBuilder();
            Scanner scanner = new Scanner(conn.getInputStream());

            while (scanner.hasNext()){
                // Read the results and store it in StringBuilder
                resultJson.append(scanner.nextLine());
            }

            // close the scanner
            scanner.close();

            // close the url connection
            conn.disconnect();

            JSONParser parser = new JSONParser();
            JSONObject resultJsonObj = (JSONObject) parser.parse(String.valueOf(resultJson));

            // receive hourly data
            JSONObject hourly = (JSONObject) resultJsonObj.get("hourly");

            // Check if all required data is present
            // get temperature, Weather Code, Humidity and windspeed
            JSONArray temperatureData = (JSONArray) hourly.get("temperature_2m");
            JSONArray weatherCode = (JSONArray) hourly.get("weather_code");
            JSONArray relativeHumidity = (JSONArray) hourly.get("relative_humidity_2m");
            JSONArray windspeedData = (JSONArray) hourly.get("wind_speed_10m");

            // we verify if a values of ours is null and return a message if is the case
            if (temperatureData == null || weatherCode == null || relativeHumidity == null || windspeedData == null) {
                System.out.println("Error: Required data not found.");
                return null;
            }

            //  the method which return the current time
            JSONArray time = (JSONArray) hourly.get("time");
            int index = findIndexOfCurrentTime(time);

            // the code has the role to extract the temperature, weatherCondition,
            // humidity and windspeed and to convert them all using the method convertWeatherCOde()
            double temperature = (double) temperatureData.get(index);
            String weatherCondition = convertWeatherCode((long) weatherCode.get(index));
            long humidity = (long) relativeHumidity.get(index);
            double windspeed = (double) windspeedData.get(index);

            // build the weather JSON data object that we are going to access in our frontend
            JSONObject weatherData = new JSONObject();
            weatherData.put("temperature", temperature);
            weatherData.put("weather_condition", weatherCondition);
            weatherData.put("humidity", humidity);
            weatherData.put("windspeed", windspeed);

            return weatherData;

        } catch (Exception e) {

            e.printStackTrace();
        }
        return null;
    }

    // retrieves geographic coordinates for given location name
    public static JSONArray getLocationData(String locationName){

        // replace any whitespace in location name to + to adhere to APIs request format
        locationName = locationName.replaceAll(" ", "+");

        // build API url with location parameter
        String urlString = "https://geocoding-api.open-meteo.com/v1/search?name=" +
                locationName + "&count=10&language=en&format=json";

        try{
            // call api and get a response
            HttpURLConnection conn = fetchApiResponse(urlString);

            // check response status
            // 200 means successful a connection
            if(conn.getResponseCode() != 200){

                System.out.println("Error: Could not connect to API");
                return null;

            }else{
                // create a StringBuilder to store the API results
                StringBuilder resultJson = new StringBuilder();
                Scanner scanner = new Scanner(conn.getInputStream());

                // read and store the resulting json data into our string builder
                while(scanner.hasNext()){
                    resultJson.append(scanner.nextLine());
                }

                // close scanner
                scanner.close();

                // close url connection
                conn.disconnect();

                // parse the JSON string into a JSON obj
                JSONParser parser = new JSONParser();
                JSONObject resultsJsonObj = (JSONObject) parser.parse(String.valueOf(resultJson));

                // get the list of location data the API generated from the location name
                JSONArray locationData = (JSONArray) resultsJsonObj.get("results");
                return locationData;
            }

        }catch(Exception e){

            e.printStackTrace();
        }

        // couldn't find location
        return null;
    }

    // Define a method named fetchApiResponse which receive a
    // string named urlString and which return an object HttpURLConnection
    private static HttpURLConnection fetchApiResponse(String urlString) {

        try {

            // The code for attempting to create a connection to URL
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            // set a request method
            conn.setRequestMethod("GET");

            // connect to our API
            conn.connect();
            return conn;

        } catch (Exception e) {

            e.printStackTrace();
        }
        return null;
    }

    private static int findIndexOfCurrentTime(JSONArray timeList) {

        String currentTime = getCurrentTime();

        // iterate through the list and see which one matches our current time
        for (int i = 0; i < timeList.size(); i++) {

            String time = (String) timeList.get(i);

            if (time.equalsIgnoreCase(currentTime)) {
                // return our index
                return i;
            }
        }

        return 0;
    }

    public static String getCurrentTime() {

        // get current data time
        LocalDateTime currentDataTime = LocalDateTime.now();

        // format of data has to be 2024-10-25T00:00 (this is how is written is API)
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH':00'");

        // format and print the current data and time
        String formatterDataTime = currentDataTime.format(formatter);

        return formatterDataTime;
    }


    // convert the weather code to something more readable
    // Here we will use the Api decoder of the weather application, where we will compare
    // the code from our application and if it fits we will categorize it
    // the decoder is in the "GitHub-Description"
    private static String convertWeatherCode(long weatherCode) {

        String weatherCondition = "";

        if (weatherCode == 0L) {
            //clear
            weatherCondition = "Clear";

        } else if (weatherCode > 0L && weatherCode <= 3L) {

            //cloudy
            weatherCondition = "Cloudy";

        } else if ((weatherCode >= 51L && weatherCode <= 67L)
                || (weatherCode >= 80L && weatherCode <= 99L)){

            // rain
            weatherCondition = "Rain";
        } else if (weatherCode >= 71L && weatherCode <= 77L) {

            // snow
            weatherCondition = "Snow";
        }

        return weatherCondition;
    }
}
