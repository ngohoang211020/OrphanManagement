package com.orphan.common.service;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.util.Locale;

/**
 * Message bundle.
 *
 */
@Component
public class MessageService {

    private MessageSource messageSource;

    public MessageService(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public String getMessage(String code) {
        return messageSource.getMessage(code, null, LocaleContextHolder.getLocale());
    }

    public String getMessage(String code, String langKey) {
        Locale locale = langKey != null ? Locale.forLanguageTag(langKey) : Locale.US;
        return messageSource.getMessage(code, null, locale);
    }

    /**
     * Get lang key of user logged
     *
     */
    public Locale buildLocaleUserLogged() {
        return Locale.US;
    }

    public String buildMessages(String messageKey, String messageValue) {
        return String.format(this.getMessage(messageKey), this.getMessage(messageValue));
    }

    public String buildMessages(String messageKey) {
        return String.format(this.getMessage(messageKey));
    }

    public String buildMessages(String messageKey, String messageValueFirst, String messageValueSecond) {
        return String.format(this.getMessage(messageKey), this.getMessage(messageValueFirst),
            this.getMessage(messageValueSecond));
    }
}
