
package gestao.models.geocode;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "compound_code",
    "global_code"
})
public class PlusCode {

    @JsonProperty("compound_code")
    private String compoundCode;
    @JsonProperty("global_code")
    private String globalCode;

    @JsonProperty("compound_code")
    public String getCompoundCode() {
        return compoundCode;
    }

    @JsonProperty("compound_code")
    public void setCompoundCode(String compoundCode) {
        this.compoundCode = compoundCode;
    }

    @JsonProperty("global_code")
    public String getGlobalCode() {
        return globalCode;
    }

    @JsonProperty("global_code")
    public void setGlobalCode(String globalCode) {
        this.globalCode = globalCode;
    }

}
