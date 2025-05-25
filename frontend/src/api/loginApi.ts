import axios from "axios";
import { API_URL } from "@/constants/api";
import { ApiResponse } from "@/types/response";
import { LoginParams, TokenInfo } from "@/types/login";

export const login = async (loginParams: LoginParams) => {
    const response = await axios.post<ApiResponse<TokenInfo>>(
        API_URL.BASE + API_URL.LOGIN,
        loginParams,
        {
            headers: {
                'Content-Type': 'application/json',
            }
        }
    );
    
    return response.data;
}