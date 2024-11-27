package br.com.digio.api.service.impl;

import br.com.digio.api.dto.ClientPurchasesDto;
import br.com.digio.api.dto.ProductDto;
import br.com.digio.api.service.ProductService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;
import java.util.Objects;

@Component
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ObjectMapper objectMapper;

    // Como está sendo usado um mock estático, essa variável foi criada para ser usada como um cache,
    // evitando que cada requisição chame o mock novamente.
    // Em um ambiente real, precisariamos chamar a api real para trabalhar com dados em tempo real,
    // e se necessário utilizar alguma forma mais aprimorada de cache.
    public static List<ProductDto> cacheProducts;

    private static final String apiUrl =
        "https://rgr3viiqdl8sikgv.public.blob.vercel-storage.com/produtos-mnboX5IPl6VgG390FECTKqHsD9SkLS.json";

    @Override
    public List<ProductDto> fetchProducts() {
        if (!Objects.isNull(cacheProducts)) {
            return cacheProducts;
        }

        HttpClient client = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10))
            .build();
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(apiUrl))
            .timeout(Duration.ofSeconds(10))
            .GET()
            .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                String responseBody = response.body();
                cacheProducts = objectMapper.readValue(responseBody, new TypeReference<>() { });
                return cacheProducts;
            } else {
                throw new RuntimeException("Erro ao chamar a API para listar produtos. StatusCode : " + response.statusCode());
            }
        } catch (Exception e) {
            throw new RuntimeException("Erro ao chamar a API para listar produtos", e);
        }
    }

}
