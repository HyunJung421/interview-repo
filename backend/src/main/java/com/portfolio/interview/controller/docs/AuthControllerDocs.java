package com.portfolio.interview.controller.docs;

import com.portfolio.interview.dto.AuthDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Auth", description = "Authentication API")
public interface AuthControllerDocs {

    @Operation(summary = "로그인", description = "사용자 로그인 API")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Success",
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(summary = "Success", name = "로그인 성공", value = """
                        {
                             "code": "S20000",
                             "success": true,
                             "data": {
                                 "accessToken": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0SWQyIiwiaWF0IjoxNzQ2MTExMDY5LCJleHAiOjE3NDYxMTE5Njl9...",
                                 "refreshToken": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0SWQyIiwiaWF0IjoxNzQ2MTExMDY5LCJleHAiOjE3NDYxOTc0Njl9..."
                             }
                         }
                    """)
                            }
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad Request",
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(summary = "Bad Request", name = "password 틀렸을 경우", value = """
                        {
                            "code": "E20008",
                            "success": false,
                            "message": "Password is incorrect"
                        }
                    """)
                            }
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Not Found",
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(summary = "Not Found", name = "사용자를 찾을 수 없음", value = """
                        {
                             "code": "E20012",
                             "success": false,
                             "message": "User not found"
                         }
                    """)
                            }
                    )
            )
    })
    AuthDto.Response login(@RequestBody AuthDto.LoginRequest loginRequest);

    @Operation(summary = "토큰 갱신", description = "리프레시 토큰을 사용한 액세스 토큰 갱신 API")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Success",
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(summary = "Success", name = "토큰 갱신 성공", value = """
                        {
                             "code": "S20000",
                             "success": true,
                             "data": {
                                 "accessToken": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0SWQyIiwiaWF0IjoxNzQ2ODg4MzY1LCJleHAiOjE3NDY4ODkyNjV9...",
                                 "refreshToken": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0SWQyIiwiaWF0IjoxNzQ2ODg4MzY1LCJleHAiOjE3NDY5NzQ3NjV9..."
                             }
                         }
                    """)
                            }
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized",
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(summary = "Unauthorized", name = "만료된 리프레시 토큰", value = """
                        {
                            "code": "E00004",
                            "success": false,
                            "message": "JWT token has expired"
                        }
                    """)
                            }
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized",
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(summary = "Unauthorized", name = "잘못된 리프레시 토큰", value = """
                        {
                             "code": "E00001",
                             "success": false,
                             "message": "Invalid refresh token"
                         }
                    """)
                            }
                    )
            )
    })
    AuthDto.Response refresh(@RequestBody AuthDto.RefreshRequest refreshRequest);

    @Operation(summary = "로그아웃", description = "사용자 로그아웃 및 토큰 무효화 API")
    @SecurityRequirement(name = "BearerAuth")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Success",
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(summary = "Success", name = "로그아웃 성공", value = """
                        {
                             "code": "S20000",
                             "success": true,
                             "data": {
                                 "message": "Logout successfully"
                             }
                         }
                    """)
                            }
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad Request",
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(summary = "Bad Request", name = "잘못된 요청", value = """
                        {
                            "code": "E40000",
                            "success": false,
                            "message": "Invalid request parameters"
                        }
                    """)
                            }
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized",
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(summary = "Unauthorized", name = "토큰 전달 안되었을 경우", value = """
                        {
                             "code": "E00001",
                             "success": false,
                             "message": "Invalid refresh token"
                         }
                    """)
                            }
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized",
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(summary = "Unauthorized", name = "잘못된 토큰의 경우", value = """
                        {
                              "code": "E00007",
                              "success": false,
                              "message": "Invalid JWT signature"
                          }
                    """)
                            }
                    )
            )
    })
    AuthDto.LogoutResponse logout(@RequestBody AuthDto.LogoutRequest logoutRequest);
}