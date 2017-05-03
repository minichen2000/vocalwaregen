
package com.mfe.mfewordcard.vocalwaregen.Utils.MfeUnit;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "img",
    "update",
    "annotations"
})
public class Page {

    @JsonProperty("img")
    public String img;
    @JsonProperty("update")
    public String update;
    @JsonProperty("annotations")
    public List<Annotation> annotations = null;

}
