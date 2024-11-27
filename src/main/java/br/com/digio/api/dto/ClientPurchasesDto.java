package br.com.digio.api.dto;

import java.util.List;

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
public class ClientPurchasesDto {

    @JsonProperty("nome")
    private String name;

    @JsonProperty("cpf")
    private String cpf;

    @JsonProperty("compras")
    private List<ProductPurchaseDto> productPurchases;

    @JsonProperty("quantidade_compras")
    public Integer getProductPurchasesSize() {
        return productPurchases.size();
    }

}
