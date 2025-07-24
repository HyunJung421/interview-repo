
package com.portfolio.interview.controller.docs;

import com.portfolio.interview.dto.SignUpDto;
import com.portfolio.interview.dto.UserDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "User", description = "User API")
public interface UserControllerDocs {

    @Operation(summary = "회원가입", description = "신규 사용자 회원가입 API")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Success",
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(summary = "Success", name = "회원가입 성공", value = """
                                        {
                                             "code": "S20000",
                                             "success": true,
                                             "data": {
                                                 "message": "User signUp successfully"
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
                                    @ExampleObject(summary = "Bad Request", name = "ID 중복", value = """
                                                {
                                                      "code": "E20003",
                                                      "success": false,
                                                      "message": "Id is already taken"
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
                                    @ExampleObject(summary = "Bad Request", name = "Email 중복", value = """
                                                {
                                                       "code": "E20004",
                                                       "success": false,
                                                       "message": "Email is already taken"
                                                   }
                                            """)
                            }
                    )
            )
    })
    SignUpDto.Response signUp(@RequestBody UserDto.Request userRequest);

    @Operation(summary = "아이디 찾기", description = "이름과 이메일을 통한 아이디 찾기 API")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Success",
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(summary = "Success", name = "아이디 찾기 성공", value = """
                                                {
                                                     "code": "S20000",
                                                     "success": true,
                                                     "data": {
                                                         "id": "testId",
                                                         "createdAt": "2025-05-01T00:00:00"
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
                                    @ExampleObject(summary = "Bad Request", name = "Email이 없는 경우", value = """
                                                {
                                                    "code": "E00005",
                                                    "success": false,
                                                    "message": "Email not found"
                                                }
                                            """)
                            }
                    )
            )
    })
    UserDto.FindIdResponse findId(@RequestBody UserDto.FindIdRequest findIdRequest);

    @Operation(summary = "비밀번호 찾기", description = "아이디와 이메일을 통한 비밀번호 찾기 API")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Success",
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(summary = "Success", name = "비밀번호 찾기 성공", value = """
                                                {
                                                     "code": "S20000",
                                                     "success": true,
                                                     "data": {
                                                         "message": "Temporary password sent to your email."
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
                                    @ExampleObject(summary = "Bad Request", name = "Id 혹은 Email이 중복", value = """
                                                {
                                                     "code": "E20007",
                                                     "success": false,
                                                     "message": "Id or email is incorrect"
                                                 }
                                            """)
                            }
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Server error",
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(summary = "Server error", name = "email 전송 실패", value = """
                                                {
                                                     "code": "E00009",
                                                     "success": false,
                                                     "message": "Email sending failed"
                                                 }
                                            """)
                            }
                    )
            )
    })
    UserDto.FindPasswordResponse findPassword(@RequestBody UserDto.FindPasswordRequest findPasswordRequest);
}