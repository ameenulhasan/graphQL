package com.ameen.graphql.service;

import com.ameen.graphql.dto.OrderDto;
import com.ameen.graphql.response.SuccessResponse;

public interface OrderService {

    SuccessResponse<Object> saveOrder(OrderDto orderDto);

    SuccessResponse<Object> getOrderById(Long orderId);

    SuccessResponse<Object> getUserOrderStats(Long userId);

    String orderDownload(String format, String date, String month);

}
