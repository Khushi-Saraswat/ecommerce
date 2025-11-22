package com.example.demo.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.model.Product;
import com.example.demo.repository.ProductRepository;
import com.example.demo.service.methods.ProductService;

@Service
public class ProductServiceImpl implements ProductService {
	@Autowired
	private ProductRepository productRepository;

	// this method is used to save a product in database
	@Override
	public Product saveProduct(Product product) {
		return productRepository.save(product);
	}

	// this method is responsible for returning all products ...
	@Override
	public List<Product> getAllProducts() {
		return productRepository.findAll();
	}

	// this method is responsible for deleting a particular product by id.
	@Override
	public Boolean deleteProduct(int id) {

		Product product = productRepository.findById(id).orElse(null);
		productRepository.delete(product);

		if (product != null)
			return true;

		else
			return false;

	}

	// this method is responsible for getting a product by id.
	@Override
	public Product getProductById(Integer id) {
		Product product = productRepository.findById(id).orElse(null);
		return product;
	}

	// this method is responsible for updating product
	@Override
	public Product updateProduct(Product product, MultipartFile file) {
		// find oldproduct by id..
		Product oldProduct = getProductById(product.getId());
		System.out.println(product.getId());
		// get imageName .....
		String imagename = file.isEmpty() ? oldProduct.getImage() : file.getOriginalFilename();
		oldProduct.setTitle(product.getTitle());
		oldProduct.setDescription(product.getDescription());
		oldProduct.setCategory(product.getCategory());
		oldProduct.setPrice(product.getPrice());
		oldProduct.setStock(product.getStock());
		oldProduct.setImage(imagename);
		oldProduct.setDiscount(product.getDiscount());
		// logic for discount price=100 =>100*5/(100)=5 100-5=95
		Double discount = product.getPrice() * (product.getDiscount() / 100.0);
		Double discountPrice = product.getPrice() - discount;
		oldProduct.setDiscountPrice(discountPrice);
		Product updateProduct = productRepository.save(oldProduct);
		// if updateProduct is not null this logic is for saving image at desktop side
		if (!ObjectUtils.isEmpty(updateProduct)) {
			// file is not empty
			if (!file.isEmpty()) {

				// convert file into inputstream
				try (InputStream inputStream = file.getInputStream()) {
					// create the path
					Path path = Paths
							.get("C:\\Users\\DELL\\Desktop\\ecommerce\\demo\\src\\main\\resources\\static\\img"
									+ File.separator + "product_img" + File.separator
									+ file.getOriginalFilename());

					System.out.println(path);
					// and save the file at a desired location
					Files.copy(inputStream, path, StandardCopyOption.REPLACE_EXISTING);
				} catch (IOException ioe) {
					ioe.printStackTrace();
				}

			}

		}
		return product;
	}

	// this method is responsible for getting all the active products.
	@Override
	public List<Product> getAllActiveProducts(String category) {
		List<Product> products = null;
		if (ObjectUtils.isEmpty(category)) {
			products = productRepository.findByIsActiveTrue();
		} else {
			products = productRepository.findByCategory(category);
		}

		return products;
	}

	@Override
	public List<Product> searchProduct(String ch) {

		// The title has the keyword ORThe category has the keyword and it doesn’t
		// matter if the text is uppercase or lowercase.
		return productRepository.findByTitleContainingIgnoreCaseOrCategoryContainingIgnoreCase(ch, ch);

	}

	// same above logic for searching the product but along with pagination
	@Override
	public Page<Product> searchProductPagination(Integer pageNo, Integer pageSize, String ch) {
		Pageable pageable = PageRequest.of(pageNo, pageSize);
		return productRepository.findByTitleContainingIgnoreCaseOrCategoryContainingIgnoreCase(ch, ch, pageable);
	}

	// get all the active products by admin but with pagination.
	@Override
	public Page<Product> getAllActiveProductPagination(Integer pageNo, Integer PageSize, String category) {
		Pageable pageable = PageRequest.of(pageNo, PageSize);
		Page<Product> pageProduct = null;
		if (ObjectUtils.isEmpty(category)) {

			pageProduct = productRepository.findByIsActiveTrue(pageable);
		} else {
			pageProduct = productRepository.findByCategory(pageable, category);
		}
		return pageProduct;
	}

	// get all products with pagination
	@Override
	public Page<Product> getAllProducts(Integer pageNo, Integer pageSize) {
		Pageable pageable = PageRequest.of(pageNo, pageSize);
		return productRepository.findAll(pageable);
	}

}

/*
 * Real-World Example to Understand
 * 
 * Imagine your product table:
 * 
 * ID Title Category
 * 1 Blue Shirt Clothing
 * 2 Laptop Bag Accessories
 * 3 T-Shirt Clothing
 * 
 * Now you call:
 * 
 * findByTitleContainingIgnoreCaseOrCategoryContainingIgnoreCase("shirt",
 * "shirt")
 * 
 * 
 * What happens?
 * 
 * Check Title:
 * 
 * "Blue Shirt" → contains "shirt" → MATCH
 * 
 * "Laptop Bag" → does not contain → NO
 * 
 * "T-Shirt" → contains → MATCH
 * 
 * Check Category:
 * 
 * "Clothing" → no
 * 
 * "Accessories" → no
 * 
 * "Clothing" → no
 * 
 * Results returned:
 * 
 * Blue Shirt
 * 
 * T-Shirt
 */