package com.mfe.mfewordcard.vocalwaregen.servlet;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "url",
        "method",
        "contentType",
        "acceptContentType",
        "data"
})
public class OpModel {

    @JsonProperty("url")
    public String url;
    @JsonProperty("method")
    public String method;
    @JsonProperty("contentType")
    public String contentType;
    @JsonProperty("acceptContentType")
    public String acceptContentType;
    @JsonProperty("data")
    public String data;

}
