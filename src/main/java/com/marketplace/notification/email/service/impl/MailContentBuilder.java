package com.marketplace.notification.email.service.impl;

import lombok.Data;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Map;

@Data
@Service
class MailContentBuilder {

    private final TemplateEngine templateEngine;

    public String build(final String templateName, final Map<String, String> params) {
        final Context context = new Context();
        params.forEach((key, value) -> context.setVariable(key, value));
        return templateEngine.process(templateName, context);
    }
}
