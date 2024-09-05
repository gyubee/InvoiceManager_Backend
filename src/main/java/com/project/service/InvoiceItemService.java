package com.project.service;

import com.project.dto.ItemDiscountDTO;
import com.project.repository.InvoiceItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class InvoiceItemService {

    @Autowired
    private InvoiceItemRepository invoiceItemRepository;

    // Details for setting discount for product items
    public List<ItemDiscountDTO> findDiscountDetailsByProductId(Integer productId) {
        return invoiceItemRepository.findDiscountDetailsByProductId(productId);
    }

//    public List<ItemDiscountDTO> findDiscountDetailsByProductId(Integer productId) {
//        List<Object[]> results = invoiceItemRepository.findDiscountDetailsByProductId(productId);
//        List<ItemDiscountDTO> dtoList = new ArrayList<>();
//
//        for (Object[] result : results) {
//            Integer invoiceItemId = (Integer) result[0];
//            Integer quantity = ((Number) result[1]).intValue();
//            BigDecimal unitPrice = ((BigDecimal) result[2]);
//            Float discountRate = ((Number) result[3]).floatValue();
//            LocalDate expirationDate = ((LocalDate) result[4]);
//
//            ItemDiscountDTO dto = new ItemDiscountDTO();
//            dto.setInvoiceItemId(invoiceItemId);
//            dto.setQuantity(quantity);
//            dto.setUnitPrice(unitPrice);
//            dto.setDiscountRate(discountRate);
//            dto.setExpirationDate(expirationDate);
//
//            dtoList.add(dto);
//        }
//
//        return dtoList;
//    }
}
