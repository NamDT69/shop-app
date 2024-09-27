package com.project.shopapp.controller;

import com.github.javafaker.Faker;
import com.project.shopapp.dto.ProductDto;
import com.project.shopapp.dto.ProductImageDto;
import com.project.shopapp.model.Product;
import com.project.shopapp.model.ProductImage;
import com.project.shopapp.response.BaseResponse;
import com.project.shopapp.response.DetailProductResponse;
import com.project.shopapp.response.ProductResponse;
import com.project.shopapp.response.ProductsResponse;
import com.project.shopapp.service.IProductImage;
import com.project.shopapp.service.IProductService;
import com.project.shopapp.service.ProductImageService;
import com.project.shopapp.service.ProductService;
import jakarta.activation.MimetypesFileTypeMap;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@RestController
@RequestMapping("${api.prefix}/products")
@RequiredArgsConstructor
public class ProductController {
    private final IProductService productService;
    private final IProductImage productImageService;
    private final ModelMapper mapper;
    @GetMapping("")
    public ResponseEntity<ProductsResponse> getProducts(
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(defaultValue = "0", name = "category_id") Integer categoryId,
            @RequestParam("page") int page,
            @RequestParam("limit") int limit
    ){
        PageRequest pageRequest = PageRequest.of(page, limit, Sort.by("createAt").descending());
        Page<ProductResponse> productPage = productService.getAllProducts(keyword, categoryId, pageRequest);
        int totalPages = productPage.getTotalPages();
        List<ProductResponse> products = productPage.getContent().stream().map(product -> mapper.map(product, ProductResponse.class)).toList();
        return ResponseEntity.ok(ProductsResponse.builder()
                        .productResponses(products)
                        .totalPage(totalPages)
                        .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProduct(
            @PathVariable int id
    ){
        try {
            DetailProductResponse product = productService.getProductById(id);
            return ResponseEntity.ok().body(BaseResponse.builder().data(product).build());
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping(value = "")
    public ResponseEntity<?> insertProduct(
            @RequestBody @Valid ProductDto productDto,
            BindingResult bindingResult
    ) throws IOException {
        if (bindingResult.hasErrors()){
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(fieldError -> errors.put(fieldError.getField(), fieldError.getDefaultMessage()));
            String errorMsg = "";
            for (String key: errors.keySet()){
                errorMsg+= "Lỗi ở: " + key + ", lí do: " + errors.get(key) + "\n";
            }
            return ResponseEntity.status(400).body(errorMsg);
        }
        Product newProduct = productService.createProduct(productDto);

        return ResponseEntity.ok().body(newProduct);
    }
    @Transactional
    @PostMapping(value = "/uploads/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadImage(@PathVariable("id") Integer productId,
                                         @RequestParam(value = "files")  ArrayList<MultipartFile> multipartFiles
    ){
        try{
            List<ProductImageDto> productImageDtos = new ArrayList<>();
            // check product existing
            DetailProductResponse product = productService.getProductById(productId);
            int numberImages = productImageService.getProductImageByProductId(productId).size();
            if (multipartFiles.size() +  numberImages > 5){
                return ResponseEntity.badRequest().body(String.format("Number images remain %d", 5 -numberImages));
            }
            for (MultipartFile file: multipartFiles){
                if (!file.isEmpty()){
                    if(file.getSize() > 10*1024*1024){
                        return ResponseEntity.status(400).body("file large");
                    }
                }else {
                    break;
                }
                if (isValidImage(file.getInputStream())) {
                    return ResponseEntity.status(400).body("required image");
                }
                String imageUrl = upload(file);
                ProductImage productImage = productService.createProductImage(ProductImageDto.builder()
                        .productId(productId)
                        .imageUrl(imageUrl)
                        .build());
                ProductImageDto productImageDto = mapper.map(productImage, ProductImageDto.class);
                productImageDtos.add(productImageDto);
            }
            return ResponseEntity.ok().body(productImageDtos);

        }catch(Exception e){
            return ResponseEntity.badRequest().body("Upload unsuccessfully");
        }
    }

    @GetMapping("/images/{imageName}")
    public ResponseEntity<?> viewImages(@PathVariable String imageName){
        try {
            Path imagePath = Paths.get("uploads/" + imageName);
            UrlResource resource = new UrlResource(imagePath.toUri());
            if (resource.exists()){
                return ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_JPEG)
                        .body(resource);
            }else{
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable int id){
        try {
            if(productService.getProductById(id) == null){
                return ResponseEntity.badRequest().body("Product not existing");
            }
            productService.deleteProduct(id);
            return ResponseEntity.ok().body(String.format("Delete product id = %d successfully", id));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    public String upload(MultipartFile file) throws IOException {
        if(file.getOriginalFilename() == null){
            throw new IOException("Invalid format file image");
        }
        String uploadDir = "uploads/";
        Path uploadFolder = Paths.get(uploadDir);
        if (!Files.exists(uploadFolder)) {
            Files.createDirectories(uploadFolder);
        }
        String uniqueName =UUID.randomUUID() + file.getOriginalFilename();
        Path filePath = uploadFolder.resolve(uniqueName);

        Files.copy(file.getInputStream(), filePath);

        return uniqueName;
    }

    public  boolean isValidImage(InputStream inputStream)  {
        MimetypesFileTypeMap mimeTypesMap = new MimetypesFileTypeMap();
        String mimeType = mimeTypesMap.getContentType(inputStream.toString());

        if (!mimeType.startsWith("image/")) {
            return false; // Not an image file
        }

        // Check for malicious code using ClamAV
//        ClamavClient clamAVClient = new ClamavClient("localhost", 3310);
//        ScanResult scanResult = clamAVClient.scan(inputStream);
//        if (scanResult.getStatus() == ScanResult.Status.VIRUS_FOUND) {
//            return false;
//        }

        // Image is valid and safe to upload
        return true;
    }

//    @PostMapping("/genFakeProducts")
    private String generateFakeProducts() {
        Faker faker = new Faker();
        int temp = 0;
        for (int i = 0; i < 3000; i++) {
            String nameProduct = faker.commerce().productName();
            if (productService.existByName(nameProduct)) {
                temp++;
                continue;
            }
            ProductDto product = new ProductDto();
            product.setName(nameProduct);
            product.setPrice((float) faker.number().randomDouble(2, 1, 10000000));
            product.setThumbnailUrl(faker.internet().url());
            product.setDescription(faker.lorem().paragraph());
            product.setCategoryId(faker.number().numberBetween(1, 6));
            productService.createProduct(product);
        }
        return "ok "+ temp;
    }
}
