package com.example.mmtbistore.repository;

import com.example.mmtbistore.model.catalog.AttributeValue;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AttributeValueRepository extends JpaRepository<AttributeValue, Long> {
    List<AttributeValue> findByAttribute_IdOrderBySortOrderAsc(Long attributeId);
    boolean existsByAttribute_IdAndValue(Long attributeId, String value);
    boolean existsByAttribute_IdAndValueAndIdNot(Long attributeId, String value, Long id);
}