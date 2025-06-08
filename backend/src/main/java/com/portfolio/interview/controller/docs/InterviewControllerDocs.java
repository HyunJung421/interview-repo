package com.portfolio.interview.controller.docs;

import com.portfolio.interview.dto.InterviewDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Interview", description = "Interview API")
public interface InterviewControllerDocs {

    @Operation(summary = "면접 질문 목록 조회", description = "면접 질문 목록 조회 API")
    @SecurityRequirement(name = "BearerAuth")
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Success",
            content = @Content(
                mediaType = "application/json",
                examples = {
                    @ExampleObject(summary = "Success", name = "면접 질문 조회 성공", value = """
                        {
                            "code": "S20000",
                            "success": true,
                            "data": {
                                "list": [
                                    {
                                        "key": "a38baad5-e964-466e-b516-527249813c9c",
                                        "category": "BACKEND",
                                        "title": "면접 질문 테스트2",
                                        "updatedAt": "2025-05-08T23:32:49.582956"
                                    },
                                    {
                                        "key": "4a705a0b-3003-48b0-9a50-b06190cdc631",
                                        "category": "FRONTEND",
                                        "title": "면접 질문 테스트",
                                        "updatedAt": "2025-05-08T23:30:49.582"
                                    }
                                ],
                                "pageInfo": {
                                    "currentPage": 1,
                                    "perPage": 10,
                                    "totalCount": 2
                                }
                            }
                        }
                    """)
                }
            )
        )
    })
    InterviewDto.ListResponse getInterviewList(@ModelAttribute InterviewDto.ListRequest request);

    @Operation(summary = "면접 질문 상세 조회", description = "면접 질문 상세 조회 API")
    @SecurityRequirement(name = "BearerAuth")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Success",
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(summary = "Success", name = "면접 질문 상세 조회 성공", value = """
                                                {
                                                     "code": "S20000",
                                                     "success": true,
                                                     "data": {
                                                         "category": "BACKEND",
                                                         "title": "CDN이란 무엇인가요?",
                                                         "content": "CDN은 Content Delivery Network의 약자로, 전 세계에 분산된 서버들이 사용자에게 더 빠르고 안정적으로 콘텐츠를 전달하는 시스템입니다.",
                                                         "bookmarked": false
                                                     }
                                                 }
                                            """)
                            }
                    )
            )
    })
    default InterviewDto.InterviewDetailResponse getInterviewDetail(@PathVariable("key") String key) {
        return null;
    }

    @Operation(summary = "면접 질문 등록", description = "면접 질문 등록 API")
    @SecurityRequirement(name = "BearerAuth")
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Success",
            content = @Content(
                mediaType = "application/json",
                examples = {
                    @ExampleObject(summary = "Success", name = "면접 질문 등록 성공", value = """
                        {
                            "code": "S20000",
                            "success": true
                        }
                    """)
                }
            )
        )
    })
    InterviewDto.AddResponse addInterview(@RequestBody InterviewDto.AddRequest request);
}
