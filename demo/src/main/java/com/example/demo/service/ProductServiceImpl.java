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

@Service
public class ProductServiceImpl implements ProductService {
	@Autowired
	private ProductRepository productRepository;


	@Override
	public Product saveProduct(Product product) {
		return productRepository.save(product);
	}

	@Override
	public List<Product> getAllProducts() {
		return productRepository.findAll();
	}

	@Override
	public Boolean deleteProduct(int id) {
		Product product = productRepository.findById(id).orElse(null);
		System.out.println(product + "product");
		if (!ObjectUtils.isEmpty(product)) {
			productRepository.delete(product);
			return true;
		}
		return false;
	}

	@Override
	public Product getProductById(Integer id) {
		Product product = productRepository.findById(id).orElse(null);
		return product;
	}

	@Override
	public Product updateProduct(Product product, MultipartFile file) {
		Product oldProduct = getProductById(product.getId());
		System.out.println(product.getId());
		String imagename = file.isEmpty() ? oldProduct.getImage() : file.getOriginalFilename();
		oldProduct.setTitle(product.getTitle());
		oldProduct.setDescription(product.getDescription());
		System.out.println(product.getDescription() + "desc");
		oldProduct.setCategory(product.getCategory());
		System.out.println(product.getCategory());
		oldProduct.setPrice(product.getPrice());
		oldProduct.setStock(product.getStock());
		oldProduct.setImage(imagename);
		oldProduct.setDiscount(product.getDiscount());
		// logic for discount price=100 =>100*5/(100)=5 100-5=95
		Double discount = product.getPrice() * (product.getDiscount() / 100.0);
		Double discountPrice = product.getPrice() - discount;
		System.out.println(discount + "discount in edit product");
		System.out.println(discountPrice + "discountPrice in edit product");
		oldProduct.setDiscountPrice(discountPrice);
		System.out.println(product.getDiscountPrice() + "DiscountPrice");
		Product updateProduct = productRepository.save(oldProduct);
		if (!ObjectUtils.isEmpty(updateProduct)) {
			if (!file.isEmpty()) {

				try (InputStream inputStream = file.getInputStream()) {
					Path path = Paths
							.get("C:\\Users\\DELL\\Desktop\\ecommerce\\demo\\src\\main\\resources\\static\\img"
									+ File.separator + "product_img" + File.separator
									+ file.getOriginalFilename());

					System.out.println(path);
					Files.copy(inputStream, path, StandardCopyOption.REPLACE_EXISTING);
				} catch (IOException ioe) {
					ioe.printStackTrace();
				}

			}
			return product;
		}
		return null;
	}

	@Override
	public List<Product> getAllActiveProducts(String category) {
		List<Product> products = null;
		if (ObjectUtils.isEmpty(category)) {
			System.out.println(category);
			products = productRepository.findByIsActiveTrue();
		} else {
			products = productRepository.findByCategory(category);
		}

		return products;
	}

	@Override
	public List<Product> searchProduct(String ch) {

		return productRepository.findByTitleContainingIgnoreCaseOrCategoryContainingIgnoreCase(ch, ch);

	}

	@Override
	public Page<Product> searchProductPagination(Integer pageNo,Integer pageSize,String ch){
        Pageable pageable=PageRequest.of(pageNo,pageSize);
		return productRepository.findByTitleContainingIgnoreCaseOrCategoryContainingIgnoreCase(ch,ch,pageable);
	}

	@Override
	public Page<Product> getAllActiveProductPagination(Integer pageNo, Integer PageSize, String category) {
        Pageable pageable = PageRequest.of(pageNo, PageSize);
		Page<Product> pageProduct = null;
		if (ObjectUtils.isEmpty(category)) {
			System.out.println(category);
			pageProduct = productRepository.findByIsActiveTrue(pageable);
		} else {
			pageProduct = productRepository.findByCategory(pageable, category);
		}
       return pageProduct;
	}
    @Override
	public Page<Product> getAllProducts(Integer pageNo, Integer pageSize){
		Pageable pageable=PageRequest.of(pageNo, pageSize);
		return productRepository.findAll(pageable);
	}
   


}
