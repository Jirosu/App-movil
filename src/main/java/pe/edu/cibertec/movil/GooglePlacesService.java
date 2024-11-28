package pe.edu.cibertec.movil;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class GooglePlacesService {

    @Value("${google.api.key}") // Para que puedas configurar la clave en tu archivo application.properties
    private String googleApiKey;


    private final RestTemplate restTemplate;

    public GooglePlacesService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<RestaurantResponse> getNearbyRestaurantsWithPhoto(double latitude, double longitude, int radius) {
        // Obtener restaurantes cercanos
        String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location="
                + latitude + "," + longitude
                + "&radius=" + radius
                + "&type=tourist_attraction"
                + "&key=" + googleApiKey;

        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        // Procesar respuesta JSON
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode;
        List<RestaurantResponse> restaurantResponses = new ArrayList<>();

        try {
            rootNode = objectMapper.readTree(response.getBody());
            for (JsonNode resultNode : rootNode.path("results")) {
                RestaurantResponse restaurantResponse = new RestaurantResponse();

                // Extract name, vicinity, and price_level
                restaurantResponse.setName(resultNode.path("name").asText());
                restaurantResponse.setVicinity(resultNode.path("vicinity").asText());
                restaurantResponse.setRating(resultNode.path("rating").asInt());
                restaurantResponse.setUserRatingsTotal(resultNode.path("user_ratings_total").asInt());

                if (resultNode.has("price_level")) {
                    restaurantResponse.setPriceLevel(resultNode.path("price_level").asInt());
                }

                // Get photo URL if available
                if (resultNode.has("photos")) {
                    String photoReference = resultNode.path("photos").get(0).path("photo_reference").asText();
                    String photoUrl = getPlacePhotoUrl(photoReference, 400);  // Max width set to 100
                    restaurantResponse.setPhoto(photoUrl);
                }

                restaurantResponses.add(restaurantResponse);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return restaurantResponses;
    }

    private String getPlacePhotoUrl(String photoReference, int maxWidth) {
        // Generar la URL para la API de fotos de Google Places
        return "https://maps.googleapis.com/maps/api/place/photo?maxwidth=" + maxWidth
                + "&photo_reference=" + photoReference
                + "&key=" + googleApiKey;
    }
}
