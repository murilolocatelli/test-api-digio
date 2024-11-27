package br.com.digio.api.business;

import br.com.digio.api.business.impl.PurchaseBusinessImpl;
import br.com.digio.api.dto.ClientPurchasesDto;
import br.com.digio.api.dto.ProductDto;
import br.com.digio.api.dto.ProductPurchaseDto;
import br.com.digio.api.dto.PurchaseDto;
import br.com.digio.api.exception.ValidationException;
import br.com.digio.api.service.ClientService;
import br.com.digio.api.service.ProductService;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;


@ExtendWith(SpringExtension.class)
public class PurchaseBusinessTest {

    @Mock
    private ProductService productService;

    @Mock
    private ClientService clientService;

    @InjectMocks
    private PurchaseBusinessImpl purchaseBusiness;

    private static String clientName1;
    private static String clientName2;
    private static String clientName3;
    private static String clientName4;

    private static String clientCpf1;
    private static String clientCpf2;
    private static String clientCpf3;
    private static String clientCpf4;

    @BeforeAll
    static void beforeAll() {
        Faker faker = new Faker();

        clientName1 = faker.name().name();
        clientName2 = faker.name().name();
        clientName3 = faker.name().name();
        clientName4 = faker.name().name();

        clientCpf1 = faker.number().digits(11);
        clientCpf2 = faker.number().digits(11);
        clientCpf3 = faker.number().digits(11);
        clientCpf4 = faker.number().digits(11);
    }

    @BeforeEach
    void beforeEach() {
        when(productService.fetchProducts()).thenReturn(products());
        when(clientService.fetchClientPurchasesList()).thenReturn(clientPurchasesList());
    }

    @Test
    void getPurchasesOrderedByTotalPriceWithSuccess() {
        List<PurchaseDto> result = purchaseBusiness.getPurchasesOrderedByTotalPrice();

        assertEquals(10, result.size());
        assertEquals(110.0, result.get(0).getTotalPrice());
        assertEquals(1200.0, result.get(result.size() - 1).getTotalPrice());
    }

    @Test
    void getGreaterPurchaseWithSuccess() {
        PurchaseDto result = purchaseBusiness.getGreaterPurchase(2023);

        assertNotNull(result);
        assertEquals(130.0, result.getTotalPrice());
    }

    @Test
    void getGreaterPurchaseWithExceptionThrown() {
        Exception exception = assertThrows(
            ValidationException.class, () -> purchaseBusiness.getGreaterPurchase(2019));

        assertEquals("Compra não encontrada", exception.getMessage());
    }

    @Test
    void getTop3LoyalClientsWithSuccess() {
        List<ClientPurchasesDto> result = purchaseBusiness.getTop3LoyalClients();

        assertEquals(3, result.size());
        assertEquals(clientName4, result.get(0).getName());
        assertEquals(clientName3, result.get(1).getName());
        assertEquals(clientName2, result.get(2).getName());
    }

    @Test
    void getWineRecommendationWithSuccess() {
        ProductDto result = purchaseBusiness.getWineRecommendation(clientCpf4);

        assertNotNull(result);
        assertEquals(4L, result.getCode());
    }

    @Test
    void getWineRecommendationWithExceptionThrown() {
        Exception exception = assertThrows(
            ValidationException.class, () -> purchaseBusiness.getWineRecommendation("90"));

        assertEquals("Cliente não encontrado", exception.getMessage());
    }

    private List<ProductDto> products() {
        return List.of(
            ProductDto.builder().code(1L).wineType("Branco").price(110.0).harvest("2020").purchaseYear(2021).build(),
            ProductDto.builder().code(2L).wineType("Tinto").price(120.0).harvest("2021").purchaseYear(2022).build(),
            ProductDto.builder().code(3L).wineType("Rosé").price(130.0).harvest("2022").purchaseYear(2023).build(),
            ProductDto.builder().code(4L).wineType("Espumante").price(140.0).harvest("2023").purchaseYear(2024).build()
        );
    }

    private List<ClientPurchasesDto> clientPurchasesList() {
        List<ProductPurchaseDto> productsCliente1 = List.of(
            productPurchase("1", 1)
        );

        List<ProductPurchaseDto> productsCliente2 = List.of(
            productPurchase("1", 1),
            productPurchase("2", 10)
        );

        List<ProductPurchaseDto> productsCliente3 = List.of(
            productPurchase("1", 1),
            productPurchase("2", 10),
            productPurchase("3", 1)
        );

        List<ProductPurchaseDto> productsCliente4 = List.of(
            productPurchase("1", 1),
            productPurchase("2", 10),
            productPurchase("4", 1),
            productPurchase("4", 1)
        );

        return new ArrayList<>(List.of(
            ClientPurchasesDto.builder().name(clientName1).cpf(clientCpf1).productPurchases(productsCliente1).build(),
            ClientPurchasesDto.builder().name(clientName2).cpf(clientCpf2).productPurchases(productsCliente2).build(),
            ClientPurchasesDto.builder().name(clientName3).cpf(clientCpf3).productPurchases(productsCliente3).build(),
            ClientPurchasesDto.builder().name(clientName4).cpf(clientCpf4).productPurchases(productsCliente4).build()
        ));
    }

    private ProductPurchaseDto productPurchase(String code, Integer quantity) {
        return ProductPurchaseDto.builder().code(code).quantity(quantity).build();
    }

}
