package com.orphan.config.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

@Component
public class AuthEntryPointJwt implements AuthenticationEntryPoint {
    private static final Logger logger = LoggerFactory.getLogger(AuthEntryPointJwt.class);
//để xử lí thông tin từ request và gửi về response,
// chỗ này sẽ gửi về một lỗi khi mà request tới đường
// dẫn yêu cầu xác thực (cần chuỗi JWT) nhưng không có kèm chuỗi JWT

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {

        response.setContentType("application/json");
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        Map<String, Object> data = new HashMap<>();
        data.put("timestamp", Calendar.getInstance().getTime());
        data.put("status", HttpStatus.UNAUTHORIZED.value());
        data.put("error", "UNAUTHORIZED");
        data.put("message", "Unauthorized");
        data.put("path", request.getRequestURI());
        response.getOutputStream().println(objectMapper.writeValueAsString(data));
    }
}
