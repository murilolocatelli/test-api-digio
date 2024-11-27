package br.com.digio.api.dto;

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
public class ProductDto {

    @JsonProperty("codigo")
    private Long code;

    @JsonProperty("tipo_vinho")
    private String wineType;

    @JsonProperty("preco")
    private Double price;

    @JsonProperty("safra")
    private String harvest;

    @JsonProperty("ano_compra")
    private Integer purchaseYear;

}
