
package com.mfe.mfewordcard.vocalwaregen.Utils.MfeUnit;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "x",
    "y",
    "w",
    "h",
    "words",
    "audio",
    "areaid"
})
public class Annotation {

    @JsonProperty("x")
    public Integer x;
    @JsonProperty("y")
    public Integer y;
    @JsonProperty("w")
    public Integer w;
    @JsonProperty("h")
    public Integer h;
    @JsonProperty("words")
    public String words;
    @JsonProperty("audio")
    public String audio;
    @JsonProperty("areaid")
    public Integer areaid;

}
