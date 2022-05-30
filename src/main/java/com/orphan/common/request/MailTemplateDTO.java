package com.orphan.common.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * @author van.lau-viet created 19/08/2021
 */
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MailTemplateDTO {
    private String name;
    private Map<String, Object> variables;

}
