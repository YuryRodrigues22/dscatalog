package com.br.dscatalog.services;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.br.dscatalog.dto.ProductDTO;
import com.br.dscatalog.entities.Category;
import com.br.dscatalog.entities.Product;
import com.br.dscatalog.repositories.CategoryRepository;
import com.br.dscatalog.repositories.ProductRepository;
import com.br.dscatalog.tests.Factory;
import com.br.dscatalog.services.exceptions.DatabaseException;
import com.br.dscatalog.services.exceptions.ResourceNotFoundException;

@ExtendWith(SpringExtension.class)
public class ProductServiceTests {

	@InjectMocks
	private ProductService service;

	@Mock
	private ProductRepository repository;

	@Mock
	private CategoryRepository categoryRepository;

	private Long existindId;
	private Long nonExistingId;
	private Long dependentId;
	private PageImpl<Product> page;
	private Product product;
	private Category category;
	private ProductDTO productDTO;

	@BeforeEach
	void setup() throws Exception {

		existindId = 1L;
		nonExistingId = 2L;
		dependentId = 3L;
		category = Factory.createCategory();
		product = Factory.createProduct();
		productDTO = Factory.createProductDTO();
		page = new PageImpl<>(List.of(product));

		Mockito.doNothing().when(repository).deleteById(existindId);
		Mockito.doThrow(EmptyResultDataAccessException.class).when(repository).deleteById(nonExistingId);
		Mockito.doThrow(DataIntegrityViolationException.class).when(repository).deleteById(dependentId);

		Mockito.when(repository.findAll((Pageable) ArgumentMatchers.any())).thenReturn(page);

		Mockito.when(repository.save(ArgumentMatchers.any())).thenReturn(product);

		Mockito.when(repository.findById(existindId)).thenReturn(Optional.of(product));
		Mockito.when(repository.findById(nonExistingId)).thenReturn(Optional.empty());

		Mockito.when(repository.getOne(existindId)).thenReturn(product);
		Mockito.when(repository.getOne(nonExistingId)).thenThrow(EntityNotFoundException.class);

		Mockito.when(categoryRepository.getOne(existindId)).thenReturn(category);
		Mockito.when(categoryRepository.getOne(nonExistingId)).thenThrow(EntityNotFoundException.class);
	}

	@Test
	public void updateShouldReturnProductDTOWhenIdExists() {

		ProductDTO result = service.update(existindId, productDTO);

		Assertions.assertNotNull(result);

	}

	@Test
	public void updateShouldThrowResourceNotFoundExceptionWhenIdDoesNotExists() {

		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			service.update(nonExistingId, productDTO);
		});

	}

	@Test
	public void findByIdShouldReturnProductDTOWhenIdExists() {

		ProductDTO result = service.findById(existindId);

		Assertions.assertNotNull(result);

	}

	@Test
	public void findByIdShouldThrowResourceNotFoundExceptionWhenIdDoesNotExists() {

		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			service.findById(nonExistingId);
		});

	}

	@Test
	public void findAllPagedShouldReturnPage() {

		Pageable pageable = PageRequest.of(0, 10);

		Page<ProductDTO> result = service.findAllPaged(pageable);

		Assertions.assertNotNull(result);
		Mockito.verify(repository).findAll(pageable);
	}

	@Test
	public void deleteShouldDoNothingWhenIdExists() {

		Assertions.assertDoesNotThrow(() -> {
			service.delete(existindId);
		});

		Mockito.verify(repository, Mockito.times(1)).deleteById(existindId);
		;
	}

	@Test
	public void deleteShouldThrowResourceNotFoundExceptionwhenIdDoesNotExist() {

		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			service.delete(nonExistingId);
		});

		Mockito.verify(repository, Mockito.times(1)).deleteById(nonExistingId);
		;
	}

	@Test
	public void deleteShouldThrowDataBaseExceptionWhenDependentId() {

		Assertions.assertThrows(DatabaseException.class, () -> {
			service.delete(dependentId);
		});

		Mockito.verify(repository, Mockito.times(1)).deleteById(dependentId);
	}
}
