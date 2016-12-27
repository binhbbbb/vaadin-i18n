package org.vaadin.i18n.impl;

import com.vaadin.server.VaadinSession;

import org.vaadin.i18n.api.Translator;

import java.util.Locale;
import java.util.ResourceBundle;

import static java.lang.String.format;

/**
 * a 'default'- or reference-implementation of a translator that
 * uses resource-bundles
 */
public class ResourceTranslator implements Translator {

    private final String resourceName;
    private Locale oldLocale;
    private ResourceBundle resourceBundle;

    /**
     * @param resourceBundle the name of the {@link ResourceBundle} to use
     */
    public ResourceTranslator(String resourceBundle) {

        if (resourceBundle == null || resourceBundle.isEmpty()) {
            throw new IllegalArgumentException("resourceName cannot be null or empty");
        }

        this.resourceName = resourceBundle;
    }

    @Override
    public String translate(String template, Object[] parameters) {
        if (template == null) throw new IllegalArgumentException("template cannot be null");
        if (parameters == null) throw new IllegalArgumentException("parameters cannot be null");

        final Locale currentLocale = VaadinSession.getCurrent().getLocale();

        if (oldLocale == null || !oldLocale.equals(currentLocale)) {
            resourceBundle = ResourceBundle.getBundle(resourceName, currentLocale);
            oldLocale = currentLocale;
        }

        final String resource = resourceBundle.getString(template);

        return parameters.length == 0
                ? resource
                : format(currentLocale, resource, parameters);
    }
}
