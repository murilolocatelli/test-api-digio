package br.com.digio.api.business.impl;

import br.com.digio.api.business.PurchaseBusiness;
import br.com.digio.api.dto.ClientPurchasesDto;
import br.com.digio.api.dto.ProductDto;
import br.com.digio.api.dto.ProductPurchaseDto;
import br.com.digio.api.dto.PurchaseDto;
import br.com.digio.api.exception.ValidationException;
import br.com.digio.api.service.ClientService;
import br.com.digio.api.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PurchaseBusinessImpl implements PurchaseBusiness {

    @Autowired
    private ClientService clientService;

    @Autowired
    private ProductService productService;

    @Override
    public List<PurchaseDto> getPurchasesOrderedByTotalPrice() {
        List<ClientPurchasesDto> clientPurchasesList = getClientPurchasesList();
        List<PurchaseDto> purchases = toPurchases(clientPurchasesList);

        purchases.sort(Comparator.comparingDouble(PurchaseDto::getTotalPrice));

        return purchases;
    }

    @Override
    public PurchaseDto getGreaterPurchase(Integer year) {
        List<ClientPurchasesDto> clientPurchasesList = getClientPurchasesList();
        List<PurchaseDto> purchases = toPurchases(clientPurchasesList);

        purchases = new ArrayList<>(purchases.stream()
            .filter(purchase -> purchase.getProduct().getPurchaseYear().equals(year))
            .toList());

        purchases.sort(Comparator.comparingDouble(PurchaseDto::getTotalPrice).reversed());

        return purchases.stream().findFirst().orElseThrow(() -> new ValidationException("Compra não encontrada"));
    }

    @Override
    public List<ClientPurchasesDto> getTop3LoyalClients() {
        List<ClientPurchasesDto> clientPurchasesList = getClientPurchasesList();

        clientPurchasesList.sort(Comparator.comparingInt(ClientPurchasesDto::getProductPurchasesSize).reversed());

        return clientPurchasesList.subList(0, Math.min(3, clientPurchasesList.size()));
    }

    @Override
    public ProductDto getWineRecommendation(String cpf) {
        List<ClientPurchasesDto> clientPurchasesList = getClientPurchasesList();

        ClientPurchasesDto clientPurchases = findClientByCpf(clientPurchasesList, cpf);

        Map<String, Long> wineTypesCounting = clientPurchases.getProductPurchases().stream()
            .collect(Collectors.groupingBy(productPurchase -> productPurchase.getProduct().getWineType(), Collectors.counting()));

        String preferredWineType = wineTypesCounting.entrySet().stream()
            .max(Map.Entry.comparingByValue())
            .map(Map.Entry::getKey)
            .orElseThrow(() -> new ValidationException("Cliente não possui compras anteriores"));

        return clientPurchases.getProductPurchases().stream()
            .map(ProductPurchaseDto::getProduct)
            .filter(product -> product.getWineType().equals(preferredWineType))
            .findFirst()
            .orElseThrow(() -> new ValidationException("Cliente não comprou vinho do tipo " + preferredWineType + " anteriormente"));
    }

    private List<ClientPurchasesDto> getClientPurchasesList() {
        List<ProductDto> products =
            Optional.ofNullable(productService.fetchProducts()).orElse(Collections.emptyList());

        List<ClientPurchasesDto> clientPurchasesList =
            Optional.ofNullable(clientService.fetchClientPurchasesList()).orElse(Collections.emptyList());

        clientPurchasesList.stream()
            .flatMap(client -> client.getProductPurchases().stream())
            .forEach(purchase -> purchase.setProduct(findProductByCode(products, Long.valueOf(purchase.getCode()))));

        return clientPurchasesList;
    }

    private List<PurchaseDto> toPurchases(List<ClientPurchasesDto> clientPurchasesList) {
        List<PurchaseDto> immutableList = clientPurchasesList.stream()
            .flatMap(client ->
                client.getProductPurchases().stream().map(productPurchase -> {
                    BigDecimal totalPrice = BigDecimal.valueOf(productPurchase.getProduct().getPrice() * productPurchase.getQuantity());
                    totalPrice = totalPrice.setScale(2, RoundingMode.HALF_UP);

                    return PurchaseDto.builder()
                        .name(client.getName())
                        .cpf(client.getCpf())
                        .product(productPurchase.getProduct())
                        .quantity(productPurchase.getQuantity())
                        .totalPrice(totalPrice.doubleValue())
                        .build();
                })
            )
            .toList();

        return new ArrayList<>(immutableList);
    }

    private ClientPurchasesDto findClientByCpf(List<ClientPurchasesDto> clientPurchasesList, String cpf) {
        return clientPurchasesList.stream()
            .filter(client -> client.getCpf().equals(cpf))
            .findFirst()
            .orElseThrow(() -> new ValidationException("Cliente não encontrado"));
    }

    private ProductDto findProductByCode(List<ProductDto> products, Long code) {
        return products.stream()
            .filter(product -> product.getCode().equals(code))
            .findFirst()
            .orElseThrow(() -> new ValidationException("Produto não encontrado"));
    }

}
