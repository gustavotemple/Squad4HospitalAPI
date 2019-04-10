
package gestao.models.geocode;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "location",
    "location_type",
    "viewport"
})
public class Geometry {

    @JsonProperty("location")
    private Location location;
    @JsonProperty("location_type")
    private String locationType;
    @JsonProperty("viewport")
    private Viewport viewport;

  
	@JsonProperty("location")
    public Location getLocation() {
        return location;
    }

    @JsonProperty("location")
    public void setLocation(Location location) {
        this.location = location;
    }

    @JsonProperty("location_type")
    public String getLocationType() {
        return locationType;
    }

    @JsonProperty("location_type")
    public void setLocationType(String locationType) {
        this.locationType = locationType;
    }

    @JsonProperty("viewport")
    public Viewport getViewport() {
        return viewport;
    }

    @JsonProperty("viewport")
    public void setViewport(Viewport viewport) {
        this.viewport = viewport;
    }

}
