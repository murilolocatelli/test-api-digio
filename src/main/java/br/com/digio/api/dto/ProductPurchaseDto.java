package br.com.digio.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
@Getter
@ToString
public class ProductPurchaseDto {

    @JsonProperty("codigo")
    private String code;

    @JsonProperty("quantidade")
    private Integer quantity;

    @JsonIgnore
    private ProductDto product;

}
