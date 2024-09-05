package com.project.service;

import com.project.dto.ItemDiscountDTO;
import com.project.repository.InvoiceItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class InvoiceItemService {

    @Autowired
    private InvoiceItemRepository invoiceItemRepository;

    // Details for setting discount for product items
    public List<ItemDiscountDTO> findDiscountDetailsByProductId(Integer productId) {
        List<Object[]> results = invoiceItemRepository.findDiscountDetailsByProductId(productId);
        List<ItemDiscountDTO> dtoList = new ArrayList<>();

        for (Object[] result : results) {
            Integer invoiceItemId = (Integer) result[0];
            Integer quantity = ((Number) result[1]).intValue();
            Integer unitPrice = ((Number) result[2]).intValue();
            Float discountRate = ((Number) result[3]).floatValue();
            LocalDate expirationDate = ((java.sql.Date) result[4]).toLocalDate();

            ItemDiscountDTO dto = new ItemDiscountDTO();
            dto.setInvoiceItemId(invoiceItemId);
            dto.setQuantity(quantity);
            dto.setUnitPrice(unitPrice);
            dto.setDiscountRate(discountRate);
            dto.setExpirationDate(expirationDate);

            dtoList.add(dto);
        }

        return dtoList;
    }
}
