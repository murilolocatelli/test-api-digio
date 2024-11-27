package br.com.digio.api.controller;

import br.com.digio.api.dto.PurchaseDto;
import br.com.digio.api.business.PurchaseBusiness;
import br.com.digio.api.dto.ClientPurchasesDto;
import br.com.digio.api.dto.ProductDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
public class PurchaseController {

    @Autowired
    private PurchaseBusiness purchaseBusiness;

    @GetMapping("/compras")
    public List<PurchaseDto> getPurchasesOrderedByTotalPrice() {
        return purchaseBusiness.getPurchasesOrderedByTotalPrice();
    }

    @GetMapping("/maior-compra/{ano}")
    public PurchaseDto getGreaterPurchase(@PathVariable("ano") Integer year) {
        return purchaseBusiness.getGreaterPurchase(year);
    }

    @GetMapping("/clientes-fieis")
    public List<ClientPurchasesDto> getTop3LoyalClients() {
        return purchaseBusiness.getTop3LoyalClients();
    }

    @GetMapping("/recomendacao/{cpf}/tipo")
    public ProductDto getWineRecommendation(@PathVariable("cpf") String cpf) {
        return purchaseBusiness.getWineRecommendation(cpf);
    }

}
