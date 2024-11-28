package pe.edu.cibertec.movil;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RestaurantResponse {
    private String name;
    private String vicinity;
    private Integer priceLevel;
    private String photo;
    private Integer rating;
    private Integer userRatingsTotal;

}
