
package com.mfe.mfewordcard.vocalwaregen.Utils.MfeUnit;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "press",
    "version",
    "shortName",
    "unitNumber",
    "unitName",
    "unitCName",
    "vocabulary",
    "sentence",
    "pages"
})
public class MfeUnit {

    @JsonProperty("press")
    public String press;
    @JsonProperty("version")
    public Integer version;
    @JsonProperty("shortName")
    public String shortName;
    @JsonProperty("unitNumber")
    public Integer unitNumber;
    @JsonProperty("unitName")
    public String unitName;
    @JsonProperty("unitCName")
    public String unitCName;
    @JsonProperty("vocabulary")
    public List<Vocabulary> vocabulary = null;
    @JsonProperty("sentence")
    public List<Sentence> sentence = null;
    @JsonProperty("pages")
    public List<Page> pages = null;

}
