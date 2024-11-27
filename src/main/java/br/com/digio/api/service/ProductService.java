package br.com.digio.api.service;

import br.com.digio.api.dto.ProductDto;

import java.util.List;

public interface ProductService {

    List<ProductDto> fetchProducts();

}
