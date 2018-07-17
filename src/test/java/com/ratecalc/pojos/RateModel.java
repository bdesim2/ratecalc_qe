package com.ratecalc.pojos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Pojo for the rate request payload
 *
 * @Author Brian DeSimone
 * @Date 07/16/2018
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RateModel {

    @JsonProperty
    private String startRate;
    @JsonProperty
    private String endRate;

    public RateModel(String startRate, String endRate){
        this.startRate = startRate;
        this.endRate = endRate;
    }

}
