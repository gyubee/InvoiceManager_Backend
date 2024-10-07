package com.project.service;


import com.project.repository.SalesRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.stream.Collectors;


@Service
public class SalesService {

    @Autowired
    private SalesRepository salesRepository;


    //    total revenue
    public BigDecimal getTotalRevenueForMonth(LocalDate date) {
        // 여기서는 2022/07/02를 사용 (매출이 있는 달이 그거밖에 없음)
        // 생각해보니 이건 총 수입량이었음
        // 매출은 어디서 받아올 수 있을까요
        // 나중에는 LocalDate.now() 넣으면 현재일자로 변경가능
//        LocalDate fixedDate = LocalDate.of(2022, 7, 2);
        return salesRepository.calculateTotalRevenueForMonth(date.getYear(), date.getMonthValue());
    }

//    5. category
    public Map<String, Double> getCategorySalesPercentage(LocalDateTime salesMonth) {
        List<Object[]> salesData = salesRepository.getSalesByCategoryAndMonth(salesMonth);

        // 전체 판매량을 계산
        double totalSales = salesData.stream()
                .mapToDouble(sales -> ((Number) sales[1]).doubleValue())
                .sum();

        // 카테고리별 판매 비율 계산
        Map<String, Double> categorySalesPercentage = new HashMap<>();

        for (Object[] row : salesData) {
            Integer categoryId = (Integer) row[0];
            Double salesAmount = ((Number) row[1]).doubleValue();

            // 카테고리 ID에 따라 카테고리 이름 매칭 (실제 카테고리 이름 매핑 필요)
            String categoryName = getCategoryNameById(categoryId);

            // 비율 계산 후 맵에 저장
            categorySalesPercentage.put(categoryName, (salesAmount / totalSales) * 100);
        }

        return categorySalesPercentage;
    }

    private String getCategoryNameById(Integer categoryId) {
        // 여기에 카테고리 ID와 이름을 매칭하는 로직 추가
        Map<Integer, String> categoryMap = new HashMap<>();
        categoryMap.put(1, "Fruit & Veg");
        categoryMap.put(2, "Bakery");
        categoryMap.put(3, "Meat & Seafood");
        categoryMap.put(4, "Chilled meals");
        categoryMap.put(5, "Dairy");
        categoryMap.put(6, "Pantry");
        categoryMap.put(7, "Snacks");
        categoryMap.put(8, "Freezer");
        categoryMap.put(9, "Drinks");
        categoryMap.put(10, "Living");
        categoryMap.put(11, "Kitchen");
        categoryMap.put(12, "Pet");

        return categoryMap.getOrDefault(categoryId, "Unknown");
    }


    //    bestseller
    public List<Map<String, Object>> getBestSellers() {
        List<Object[]> results = salesRepository.findTopSellingProducts(5);

        return results.stream().map(result -> {
            Map<String, Object> item = new HashMap<>();
            item.put("productName", result[0]);
            item.put("totalQuantity", result[1]);
            return item;
        }).collect(Collectors.toList());
    }


//    3. positive trending

    // 특정 월의 판매 데이터를 가져오는 메서드
    public List<Object[]> getSalesByMonth(LocalDateTime salesMonth) {
        return salesRepository.findSalesByMonth(salesMonth);
    }

    // 전월 대비 판매량 상승 비율을 계산하는 로직
    public List<Map<String, Object>> calculatePositiveTrending(LocalDateTime salesMonth) {
        LocalDateTime previousMonth = salesMonth.minusMonths(1);  // 전월 계산

        // 현재 달과 전월의 판매 데이터를 가져옵니다.
        List<Object[]> currentMonthSales = getSalesByMonth(salesMonth);
        List<Object[]> previousMonthSales = getSalesByMonth(previousMonth);

        // 이전 달의 판매 데이터를 매핑 (productId를 기준으로) - 중복된 key에 대해서 합산 처리
        Map<Integer, Integer> previousMonthSalesMap = new HashMap<>();
        for (Object[] data : previousMonthSales) {
            Integer productId = (Integer) data[0];
            Integer monthlySales = (Integer) data[1];
            previousMonthSalesMap.merge(productId, monthlySales, Integer::sum);  // 중복된 productId에 대해 판매량 합산
        }

        // 판매량 상승 비율을 계산
        List<Map<String, Object>> salesGrowthList = new ArrayList<>();

        // 현재 달의 판매 데이터 처리
        for (Object[] currentSales : currentMonthSales) {
            Integer productId = (Integer) currentSales[0];
            Integer currentSalesAmount = (Integer) currentSales[1];

            // 전월 판매량 (전월 판매량이 없으면 0으로 설정)
            Integer previousSalesAmount = previousMonthSalesMap.getOrDefault(productId, 0);

            double growthPercentage;

            // 전월 데이터가 없으면 0%로 처리
            if (previousSalesAmount == 0) {
                growthPercentage = 0.0;
            } else {
                growthPercentage = ((double) (currentSalesAmount - previousSalesAmount) / previousSalesAmount) * 100;
            }

            // 결과를 맵에 저장
            Map<String, Object> result = new HashMap<>();
            result.put("productId", productId);
            result.put("currentSales", currentSalesAmount);
            result.put("previousSales", previousSalesAmount);
            result.put("growthPercentage", growthPercentage);

            salesGrowthList.add(result);
        }

        // 성장률이 높은 상위 5개 항목을 정렬하여 리턴
        return salesGrowthList.stream()
                .sorted((s1, s2) -> Double.compare((double) s2.get("growthPercentage"), (double) s1.get("growthPercentage")))
                .limit(5)
                .collect(Collectors.toList());
    }

//    4. negative

    // 전월 대비 판매량 감소 비율을 계산하는 로직
    public List<Map<String, Object>> calculateNegativeTrending(LocalDateTime salesMonth) {
        LocalDateTime previousMonth = salesMonth.minusMonths(1);  // 전월 계산

        // 현재 달과 전월의 판매 데이터를 가져옵니다.
        List<Object[]> currentMonthSales = getSalesByMonth(salesMonth);
        List<Object[]> previousMonthSales = getSalesByMonth(previousMonth);

        // 이전 달의 판매 데이터를 매핑 (productId를 기준으로) - 중복된 key에 대해서 합산 처리
        Map<Integer, Integer> previousMonthSalesMap = new HashMap<>();
        for (Object[] data : previousMonthSales) {
            Integer productId = (Integer) data[0];
            Integer monthlySales = (Integer) data[1];
            previousMonthSalesMap.merge(productId, monthlySales, Integer::sum);  // 중복된 productId에 대해 판매량 합산
        }

        // 판매량 감소 비율을 계산
        List<Map<String, Object>> salesDeclineList = new ArrayList<>();

        // 현재 달의 판매 데이터 처리
        for (Object[] currentSales : currentMonthSales) {
            Integer productId = (Integer) currentSales[0];
            Integer currentSalesAmount = (Integer) currentSales[1];

            // 전월 판매량 (전월 판매량이 없으면 0으로 설정)
            Integer previousSalesAmount = previousMonthSalesMap.getOrDefault(productId, 0);

            double declinePercentage;

            // 전월 데이터가 없으면 0%로 처리
            if (previousSalesAmount == 0) {
                declinePercentage = 0.0;  // 전월 데이터가 없으면 감소율도 0%
            } else if (currentSalesAmount < previousSalesAmount) {
                // 현재 판매량이 전월보다 적은 경우에만 감소율 계산
                declinePercentage = ((double) (previousSalesAmount - currentSalesAmount) / previousSalesAmount) * 100;
            } else {
                // 판매량이 증가하거나 유지된 경우 0% 처리
                declinePercentage = 0.0;
            }

            // 결과를 맵에 저장
            Map<String, Object> result = new HashMap<>();
            result.put("productId", productId);
            result.put("currentSales", currentSalesAmount);
            result.put("previousSales", previousSalesAmount);
            result.put("declinePercentage", declinePercentage);

            salesDeclineList.add(result);
        }

        // 감소율이 높은 상위 5개 항목을 정렬하여 리턴
        return salesDeclineList.stream()
                .sorted((s1, s2) -> Double.compare((double) s2.get("declinePercentage"), (double) s1.get("declinePercentage")))
                .limit(5)
                .collect(Collectors.toList());
    }
}
