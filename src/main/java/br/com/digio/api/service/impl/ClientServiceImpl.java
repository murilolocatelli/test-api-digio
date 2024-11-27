package br.com.digio.api.service.impl;

import br.com.digio.api.dto.ClientPurchasesDto;
import br.com.digio.api.service.ClientService;
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
public class ClientServiceImpl implements ClientService {

    @Autowired
    private ObjectMapper objectMapper;

    // Como está sendo usado um mock estático, essa variável foi criada para ser usada como um cache,
    // evitando que cada requisição chame o mock novamente.
    // Em um ambiente real, precisariamos chamar a api real para trabalhar com dados em tempo real,
    // e se necessário utilizar alguma forma mais aprimorada de cache.
    public static List<ClientPurchasesDto> cacheClientPurchasesList;

    private static final String apiUrl =
        "https://rgr3viiqdl8sikgv.public.blob.vercel-storage.com/clientes-Vz1U6aR3GTsjb3W8BRJhcNKmA81pVh.json";

    @Override
    public List<ClientPurchasesDto> fetchClientPurchasesList() {
        if (!Objects.isNull(cacheClientPurchasesList)) {
            return cacheClientPurchasesList;
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
                cacheClientPurchasesList = objectMapper.readValue(responseBody, new TypeReference<>() { });
                return cacheClientPurchasesList;
            } else {
                throw new RuntimeException("Erro ao chamar a API para listar clientes e compras. StatusCode : " + response.statusCode());
            }
        } catch (Exception e) {
            throw new RuntimeException("Erro ao chamar a API para listar clientes e compras", e);
        }
    }

}
