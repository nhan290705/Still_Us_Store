package com.example.mmtbistore.service;

import com.example.mmtbistore.dto.AttributeRequest;
import com.example.mmtbistore.dto.AttributeResponse;
import com.example.mmtbistore.dto.AttributeValueRequest;
import com.example.mmtbistore.dto.AttributeValueResponse;
import com.example.mmtbistore.exception.ConflictException;
import com.example.mmtbistore.exception.ResourceNotFoundException;
import com.example.mmtbistore.model.catalog.AttributeValue;
import com.example.mmtbistore.model.catalog.ProductAttribute;
import com.example.mmtbistore.repository.AttributeValueRepository;
import com.example.mmtbistore.repository.ProductAttributeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminAttributeService {

    private final ProductAttributeRepository productAttributeRepository;
    private final AttributeValueRepository attributeValueRepository;

    @Transactional(readOnly = true)
    public List<AttributeResponse> getAllAttributes() {
        return productAttributeRepository.findAll().stream()
                .map(this::toResponseWithValues)
                .toList();
    }

    @Transactional(readOnly = true)
    public AttributeResponse getAttributeById(Long id) {
        ProductAttribute attribute = productAttributeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy thuộc tính với ID: " + id));
        return toResponseWithValues(attribute);
    }

    @Transactional
    public AttributeResponse createAttribute(AttributeRequest request) {
        String code = request.getCode().trim();
        if (productAttributeRepository.existsByCode(code)) {
            throw new ConflictException("Mã thuộc tính '" + code + "' đã tồn tại");
        }

        ProductAttribute attribute = new ProductAttribute();
        attribute.setName(request.getName().trim());
        attribute.setCode(code);
        attribute.setDisplayType(request.getDisplayType());

        ProductAttribute saved = productAttributeRepository.save(attribute);
        return toResponseWithValues(saved);
    }

    @Transactional
    public AttributeResponse updateAttribute(Long id, AttributeRequest request) {
        ProductAttribute attribute = productAttributeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy thuộc tính với ID: " + id));

        String code = request.getCode().trim();
        if (productAttributeRepository.existsByCodeAndIdNot(code, id)) {
            throw new ConflictException("Mã thuộc tính '" + code + "' đã tồn tại");
        }

        attribute.setName(request.getName().trim());
        attribute.setCode(code);
        attribute.setDisplayType(request.getDisplayType());

        ProductAttribute saved = productAttributeRepository.save(attribute);
        return toResponseWithValues(saved);
    }

    @Transactional
    public AttributeValueResponse addAttributeValue(Long attributeId, AttributeValueRequest request) {
        ProductAttribute attribute = productAttributeRepository.findById(attributeId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy thuộc tính với ID: " + attributeId));

        String val = request.getValue().trim();
        if (attributeValueRepository.existsByAttribute_IdAndValue(attributeId, val)) {
            throw new ConflictException("Giá trị '" + val + "' đã tồn tại trong thuộc tính này");
        }

        AttributeValue value = new AttributeValue();
        value.setAttribute(attribute);
        value.setValue(val);
        value.setColorHex(request.getColorHex() != null ? request.getColorHex().trim() : null);
        value.setSortOrder(request.getSortOrder() != null ? request.getSortOrder() : 0);

        AttributeValue saved = attributeValueRepository.save(value);
        return toValueResponse(saved);
    }

    @Transactional
    public AttributeValueResponse updateAttributeValue(Long valueId, AttributeValueRequest request) {
        AttributeValue value = attributeValueRepository.findById(valueId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy giá trị thuộc tính với ID: " + valueId));

        String val = request.getValue().trim();
        Long attributeId = value.getAttribute().getId();
        if (attributeValueRepository.existsByAttribute_IdAndValueAndIdNot(attributeId, val, valueId)) {
            throw new ConflictException("Giá trị '" + val + "' đã tồn tại trong thuộc tính này");
        }

        value.setValue(val);
        value.setColorHex(request.getColorHex() != null ? request.getColorHex().trim() : null);
        value.setSortOrder(request.getSortOrder() != null ? request.getSortOrder() : 0);

        AttributeValue saved = attributeValueRepository.save(value);
        return toValueResponse(saved);
    }

    @Transactional
    public void deleteAttributeValue(Long valueId) {
        AttributeValue value = attributeValueRepository.findById(valueId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy giá trị thuộc tính với ID: " + valueId));
        attributeValueRepository.delete(value);
    }

    private AttributeResponse toResponseWithValues(ProductAttribute attribute) {
        List<AttributeValueResponse> valueResponses = attributeValueRepository
                .findByAttribute_IdOrderBySortOrderAsc(attribute.getId())
                .stream()
                .map(this::toValueResponse)
                .toList();

        return AttributeResponse.builder()
                .id(attribute.getId())
                .name(attribute.getName())
                .code(attribute.getCode())
                .displayType(attribute.getDisplayType())
                .createdAt(attribute.getCreatedAt())
                .updatedAt(attribute.getUpdatedAt())
                .values(valueResponses)
                .build();
    }

    private AttributeValueResponse toValueResponse(AttributeValue value) {
        return AttributeValueResponse.builder()
                .id(value.getId())
                .attributeId(value.getAttribute().getId())
                .value(value.getValue())
                .colorHex(value.getColorHex())
                .sortOrder(value.getSortOrder())
                .createdAt(value.getCreatedAt())
                .updatedAt(value.getUpdatedAt())
                .build();
    }
}
