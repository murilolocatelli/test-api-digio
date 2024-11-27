package br.com.digio.api.service;

import br.com.digio.api.dto.ClientPurchasesDto;

import java.util.List;

public interface ClientService {

    List<ClientPurchasesDto> fetchClientPurchasesList();

}
