
package com.mfe.mfewordcard.vocalwaregen.Utils.MfeUnit;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "title",
    "phonetic",
    "chinese"
})
public class Vocabulary {

    @JsonProperty("title")
    public String title;
    @JsonProperty("phonetic")
    public String phonetic;
    @JsonProperty("chinese")
    public String chinese;

}
