package com.portfolio.interview.dto;

public class PageDto {

    public record Request(
        int currentPage,
        int perPage
    ) {
        public static Request of(int currentPage, int perPage) {
            return new Request(currentPage, perPage);
        }
    }

    public record Response (
        int currentPage,
        int perPage,
        long totalCount
    ) {
        public static Response of(int currentPage, int perPage, long totalCount) {
            return new Response(currentPage, perPage, totalCount);
        }
    }
}