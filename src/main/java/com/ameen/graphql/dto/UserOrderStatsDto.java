package com.ameen.graphql.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserOrderStatsDto {
    private Long userId;
    private String userName;
    private Long totalBooks;
    private Long totalSpent;
    private List<BookDetailsDto> books;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class BookDetailsDto {
        private String title;
        private String author;
        private String originalPrice;
    }

}
