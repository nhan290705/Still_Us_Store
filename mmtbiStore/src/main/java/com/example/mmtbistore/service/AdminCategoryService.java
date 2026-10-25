package com.example.mmtbistore.service;

import com.example.mmtbistore.dto.AdminCategoryResponse;
import com.example.mmtbistore.dto.CategoryRequest;
import com.example.mmtbistore.exception.BadRequestException;
import com.example.mmtbistore.exception.ConflictException;
import com.example.mmtbistore.exception.ResourceNotFoundException;
import com.example.mmtbistore.model.catalog.Category;
import com.example.mmtbistore.repository.CategoryRepository;
import com.example.mmtbistore.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminCategoryService {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    @Transactional(readOnly = true)
    public List<AdminCategoryResponse> getAllCategories() {
        return categoryRepository.findByParentIsNullOrderBySortOrderAsc().stream()
                .map(this::toAdminResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public AdminCategoryResponse getCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy danh mục với ID: " + id));
        return toAdminResponse(category);
    }

    @Transactional
    public AdminCategoryResponse createCategory(CategoryRequest request) {
        String slug = request.getSlug().trim();
        if (categoryRepository.existsBySlug(slug)) {
            throw new ConflictException("Slug danh mục '" + slug + "' đã tồn tại");
        }

        Category category = new Category();
        category.setName(request.getName().trim());
        category.setSlug(slug);
        category.setImageUrl(request.getImageUrl() != null ? request.getImageUrl().trim() : null);
        category.setSortOrder(request.getSortOrder() != null ? request.getSortOrder() : 0);
        category.setActive(request.getIsActive() != null ? request.getIsActive() : true);

        if (request.getParentId() != null) {
            Category parent = categoryRepository.findById(request.getParentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy danh mục cha với ID: " + request.getParentId()));
            category.setParent(parent);
        }

        Category saved = categoryRepository.save(category);
        return toAdminResponse(saved);
    }

    @Transactional
    public AdminCategoryResponse updateCategory(Long id, CategoryRequest request) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy danh mục với ID: " + id));

        String slug = request.getSlug().trim();
        if (categoryRepository.existsBySlugAndIdNot(slug, id)) {
            throw new ConflictException("Slug danh mục '" + slug + "' đã tồn tại");
        }

        category.setName(request.getName().trim());
        category.setSlug(slug);
        category.setImageUrl(request.getImageUrl() != null ? request.getImageUrl().trim() : null);
        category.setSortOrder(request.getSortOrder() != null ? request.getSortOrder() : 0);
        category.setActive(request.getIsActive() != null ? request.getIsActive() : true);

        if (request.getParentId() != null) {
            if (request.getParentId().equals(id)) {
                throw new BadRequestException("Danh mục không thể làm cha của chính mình");
            }
            Category parent = categoryRepository.findById(request.getParentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy danh mục cha với ID: " + request.getParentId()));
            category.setParent(parent);
        } else {
            category.setParent(null);
        }

        Category saved = categoryRepository.save(category);
        return toAdminResponse(saved);
    }

    @Transactional
    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy danh mục với ID: " + id));

        // Ràng buộc nghiệp vụ: Danh mục đang có sản phẩm thì chỉ ẩn, không xoá cứng
        if (productRepository.existsByCategoryId(id)) {
            throw new BadRequestException("Danh mục đang có sản phẩm thì chỉ được ẩn, không thể xoá cứng");
        }

        // Chặn xoá nếu còn danh mục con
        if (categoryRepository.existsByParentId(id)) {
            throw new BadRequestException("Danh mục đang có danh mục con, vui lòng xoá hoặc chuyển danh mục con trước");
        }

        categoryRepository.delete(category);
    }

    private AdminCategoryResponse toAdminResponse(Category category) {
        List<AdminCategoryResponse> children = category.getChildren().stream()
                .sorted(Comparator.comparingInt(Category::getSortOrder))
                .map(this::toAdminResponse)
                .toList();

        return AdminCategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .slug(category.getSlug())
                .imageUrl(category.getImageUrl())
                .parentId(category.getParent() != null ? category.getParent().getId() : null)
                .parentName(category.getParent() != null ? category.getParent().getName() : null)
                .sortOrder(category.getSortOrder())
                .isActive(category.isActive())
                .createdAt(category.getCreatedAt())
                .updatedAt(category.getUpdatedAt())
                .children(children)
                .build();
    }
}
