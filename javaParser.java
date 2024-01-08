import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONArray;
import org.json.JSONObject;

public class JavaParser {

    public static void main(String[] args) {
        try {
            // Fetch the JSON data from the API
            String url = "https://s3.amazonaws.com/open-to-cors/assignment.json";
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("GET");

            // Check if the request was successful (status code 200)
            if (connection.getResponseCode() == 200) {
                // Read the JSON data
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                // Parse the JSON data
                JSONObject jsonObject = new JSONObject(response.toString());
                JSONArray products = jsonObject.getJSONArray("products");

                // Sort the products based on descending popularity
                products.sort((a, b) -> Integer.compare(((JSONObject) b).getInt("popularity"), ((JSONObject) a).getInt("popularity")));

                // Display the data with Title, Price ordered based on descending popularity
                for (Object product : products) {
                    JSONObject productJson = (JSONObject) product;
                    System.out.println("Title: " + productJson.getString("title"));
                    System.out.println("Price: " + productJson.getDouble("price"));
                    System.out.println("Popularity: " + productJson.getInt("popularity"));
                    System.out.println("Subcategory: " + productJson.getString("subcategory"));
                    System.out.println("------------------------------");
                }
            } else {
                System.out.println("Failed to fetch data. Status code: " + connection.getResponseCode());
            }

            connection.disconnect();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
