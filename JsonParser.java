import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import org.json.JSONObject;

public class JsonParser {

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
                String response = reader.lines().collect(StringBuilder::new, StringBuilder::append, StringBuilder::append).toString();
                reader.close();

                // Parse the JSON data
                JSONObject jsonObject = new JSONObject(response);

                // Check the actual structure of the JSON response
                if (jsonObject.has("products")) {
                    Object productsObject = jsonObject.get("products");

                    if (productsObject instanceof JSONObject) {
                        JSONObject products = (JSONObject) productsObject;

                        // Iterate through the entries in the products object
                        for (Map.Entry<String, Object> entry : products.toMap().entrySet()) {
                            String productId = entry.getKey();
                            Map<String, Object> productDetailsMap = (Map<String, Object>) entry.getValue();
                            JSONObject productDetails = new JSONObject(productDetailsMap);

                            System.out.println("Product ID: " + productId);
                            System.out.println("Title: " + productDetails.getString("title"));
                            System.out.println("Price: " + productDetails.getDouble("price"));
                            System.out.println("Popularity: " + productDetails.getInt("popularity"));
                            System.out.println("Subcategory: " + productDetails.getString("subcategory"));
                            System.out.println("------------------------------");
                        }
                    } else {
                        System.out.println("Unexpected structure: 'products' is not a JSONObject.");
                    }
                } else {
                    System.out.println("The response does not contain 'products' field.");
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
