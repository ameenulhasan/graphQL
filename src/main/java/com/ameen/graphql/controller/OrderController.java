package com.ameen.graphql.controller;

import com.ameen.graphql.dto.OrderDto;
import com.ameen.graphql.response.SuccessResponse;
import com.ameen.graphql.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;

@Validated
@Controller
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @MutationMapping
    public SuccessResponse<Object> saveOrder(@Valid @Argument OrderDto orderDto) {
        return orderService.saveOrder(orderDto);
    }

    @QueryMapping
    public SuccessResponse<Object> getOrderById(@Argument Long orderId) {
        return orderService.getOrderById(orderId);
    }

    @QueryMapping
    public SuccessResponse<Object> getUserOrderStats(@Argument Long userId) {
        return orderService.getUserOrderStats(userId);
    }

    @QueryMapping
    public String orderDownload(@Argument String format,
                                @Argument String date,
                                @Argument String month) {
        return orderService.orderDownload(format, date, month);
    }

}
