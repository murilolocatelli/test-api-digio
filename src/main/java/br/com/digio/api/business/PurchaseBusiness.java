package br.com.digio.api.business;

import br.com.digio.api.dto.PurchaseDto;
import br.com.digio.api.dto.ClientPurchasesDto;
import br.com.digio.api.dto.ProductDto;

import java.util.List;

public interface PurchaseBusiness {

    List<PurchaseDto> getPurchasesOrderedByTotalPrice();

    PurchaseDto getGreaterPurchase(Integer year);

    List<ClientPurchasesDto> getTop3LoyalClients();

    ProductDto getWineRecommendation(String cpf);

}
