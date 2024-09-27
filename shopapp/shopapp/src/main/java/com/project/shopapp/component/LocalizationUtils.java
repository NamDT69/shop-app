package com.project.shopapp.component;

import com.project.shopapp.utils.WebUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.LocaleResolver;

import java.util.Locale;
@Component
@RequiredArgsConstructor
public class LocalizationUtils {
    private final MessageSource messageSource;
    private final LocaleResolver localResolver;
    public String getLocalizedMessage(String messageKey, Object ... params){
        Locale locale  = localResolver.resolveLocale(WebUtils.getCurrentRequest());
        return messageSource.getMessage(messageKey,params, locale);
    }
}
