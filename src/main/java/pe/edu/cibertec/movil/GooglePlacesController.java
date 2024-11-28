package pe.edu.cibertec.movil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin("*")
public class GooglePlacesController {

    @Autowired
    private GooglePlacesService googlePlacesService;


    @GetMapping("/restaurantsWithPhoto")
    public List<RestaurantResponse> getRestaurantsWithPhoto(
            @RequestParam(defaultValue = "-12.121615") double latitude,
            @RequestParam(defaultValue = "-77.030663") double longitude,
            @RequestParam(defaultValue = "5000") int radius) {
        return googlePlacesService.getNearbyRestaurantsWithPhoto(latitude, longitude, radius);
    }

}
