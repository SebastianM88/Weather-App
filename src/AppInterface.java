import org.json.simple.JSONObject;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class AppInterface extends JFrame {

    private JSONObject weatherData;
    private JLabel weatherConditionImage;

    // We define the constructor of our class
    public AppInterface() {

        // We define the dimensions and some properties
        super("Whether App");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(450, 650);
        setLocationRelativeTo(null);
        setLayout(null);
        setResizable(false);

        // we create a personalized panel
        BackgroundPanel backgroundPanel = new BackgroundPanel("src/Icons/gradient.png");
        backgroundPanel.setLayout(null);
        setContentPane(backgroundPanel);

        addGUIComponents();
    }

    // We create a method that will contain all the details of our interface
    private void addGUIComponents() {
        JTextField searchTextField = new JTextField();
        searchTextField.setBounds(15, 15, 351, 45);
        searchTextField.setFont(new Font("Dialog", Font.PLAIN, 24));
        add(searchTextField);

        // Add the cloudy image
        ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("Icons\\cloudy.png"));
        Image i2 = i1.getImage().getScaledInstance(250, 200, Image.SCALE_DEFAULT);
        ImageIcon i3 = new ImageIcon(i2);
        weatherConditionImage = new JLabel(i3);
        weatherConditionImage.setBounds(0, 85, 450, 217);
        add(weatherConditionImage);

        // Set a possible temperatureText
        JLabel temperatureText = new JLabel("8 C");
        temperatureText.setBounds(0, 300, 450, 54);
        temperatureText.setFont(new Font("Dialog", Font.BOLD, 48));
        temperatureText.setHorizontalAlignment(SwingConstants.CENTER);
        add(temperatureText);

        // Set a possible weather condition
        JLabel weatherConditionDesc = new JLabel("Cloudy");
        weatherConditionDesc.setBounds(0, 350, 450, 36);
        weatherConditionDesc.setFont(new Font("Dialog", Font.PLAIN, 32));
        weatherConditionDesc.setHorizontalAlignment(SwingConstants.CENTER);
        add(weatherConditionDesc);

        // Add the humidity image
        ImageIcon i4 = new ImageIcon(ClassLoader.getSystemResource("Icons\\humidity.png"));
        Image i5 = i4.getImage().getScaledInstance(68, 68, Image.SCALE_DEFAULT);
        ImageIcon i6 = new ImageIcon(i5);
        JLabel image1 = new JLabel(i6);
        image1.setBounds(15, 500, 74, 66);
        add(image1);

        // Set the text for humidity
        JLabel humidityText = new JLabel("<html><b>Humidity</b> 100%</html>");
        humidityText.setBounds(90, 515, 85, 55);
        humidityText.setFont(new Font("Dialog", Font.PLAIN, 16));
        add(humidityText);

        // Add the wind speed image
        ImageIcon i7 = new ImageIcon(ClassLoader.getSystemResource("Icons\\wind speed.png"));
        Image i8 = i7.getImage().getScaledInstance(65, 65, Image.SCALE_DEFAULT);
        ImageIcon i9 = new ImageIcon(i8);
        JLabel image2 = new JLabel(i9);
        image2.setBounds(230, 510, 85, 55);
        add(image2);

        // Set the text for wind speed
        JLabel windspeedText = new JLabel("<html><b>Windspeed</b> 15km/h</html>");
        windspeedText.setBounds(320, 520, 85, 55);
        humidityText.setFont(new Font("Dialog", Font.PLAIN, 16));
        add(windspeedText);


        // Set a search button and also add an image to match the search function
        JButton searchButton = new JButton(loadImage("src\\Icons\\search.png"));

        // change the cursor to a hand cursor when hovering over this button
        searchButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        searchButton.setBounds(370, 15, 47, 44);
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                // get the location from user
                String userInput = searchTextField.getText();

                // validate input and also remove whiteSpace to ensure empty the text
                if (userInput.replaceAll("\\s", "").length() <= 0) {
                    return;
                }

                // retrieve weather data
                weatherData = AppFunctionality.getWeatherData(userInput);

                if (weatherData == null) {
                    System.out.println("Error: weather data is null.");
                    return;
                }

                // update weather image
                String weatherCondition = (String) weatherData.get("weather_condition");
                if (weatherCondition == null) {
                    System.out.println("Error: weather condition is null.");
                    return;
                }

                // depending on the condition, we will update the weather
                // image corresponds with the condition
                switch (weatherCondition) {
                    case "Clear":
                        weatherConditionImage.setIcon(loadImage("src/Icons/clear.png"));
                        break;
                    case "Cloudy":
                        weatherConditionImage.setIcon(loadImage("src/Icons/cloudy.png"));
                        break;
                    case "Rain":
                        weatherConditionImage.setIcon(loadImage("src/Icons/rain.png"));
                        break;
                    case "Snow":
                        weatherConditionImage.setIcon(loadImage("src/Icons/snow.png"));
                        break;
                }

                // update temperatureText text
                Double temperature = (Double) weatherData.get("temperature");
                if (temperature == null) {
                    System.out.println("Error: temperature is null.");
                    return;
                }
                temperatureText.setText(temperature + " C");

                // update humidity text
                Long humidity = (Long) weatherData.get("humidity");
                if (humidity == null) {
                    System.out.println("Error: humidity is null.");
                    return;
                }
                humidityText.setText("<html><b>Humidity</b> " + humidity + "%</html>");

                // update windspeed text
                Double windspeed = (Double) weatherData.get("windspeed");
                if (windspeed == null) {
                    System.out.println("Error: windspeed is null.");
                    return;
                }
                windspeedText.setText("<html><b>Windspeed</b> " + windspeed + "km/h</html>");
            }
        });
        add(searchButton);
    }

    // Sett the method to add the image inside the button and make both work
    private ImageIcon loadImage(String resourcePath) {

        try {
            BufferedImage image = ImageIO.read(new File(resourcePath));
            return new ImageIcon(image);

        } catch (IOException e) {

            e.printStackTrace();
        }

        System.out.println("Could not find resource");
        return null;
    }

    // We create a custom JPanel to draw the background image
    private class BackgroundPanel extends JPanel {

        private Image backgroundImage;

        public BackgroundPanel(String filePath) {
            try {

                backgroundImage = ImageIO.read(new File(filePath));

            } catch (IOException e) {

                e.printStackTrace();
            }
        }

        // We create an override method to take the desired length and
        // width to be transposed to our interface
        @Override
        protected void paintComponent(Graphics g) {

            super.paintComponent(g);
            if (backgroundImage != null) {

                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        }
    }
}
