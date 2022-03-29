package com.orphan.common.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.orphan.api.controller.common.dto.RegisterRequestDto;

import javax.validation.Valid;
import java.io.IOException;

public class StringToJson {
    @Valid
    public static RegisterRequestDto StringToRegisterRequestDto(String user) {
        RegisterRequestDto registerRequestDto=new RegisterRequestDto();
        try{
            ObjectMapper objectMapper=new ObjectMapper();
            registerRequestDto=objectMapper.readValue(user,RegisterRequestDto.class);
        }
        catch (IOException err){

        }
        return registerRequestDto;
    }
}
