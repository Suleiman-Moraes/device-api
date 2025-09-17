package com.moraes.device_api.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    public static final String MEDIA_TYPE_APPLICATION_YML_VALUE = "application/x-yaml";
    private static final MediaType MEDIA_TYPE_APPLICATION_YML = MediaType.valueOf(MEDIA_TYPE_APPLICATION_YML_VALUE);

    @Value("${cors.originPatterns:default}")
    private String corsOriginPatterns = "";

    /**
     * Configure content negotiation for the application.
     * <p>
     * The default content type is JSON. JSON, XML and YAML are supported.
     * <p>
     * The 'favorParameter' property is set to false, to favor the 'Accept' header
     * over the 'format' parameter.
     * <p>
     * The 'ignoreAcceptHeader' property is set to false, to honor the 'Accept'
     * header
     * in the request.
     * <p>
     * The 'useRegisteredExtensionsOnly' property is set to false, to allow the
     * application to use custom media types.
     * <p>
     * The 'defaultContentType' property is set to JSON, meaning that the
     * application will default to JSON if no media type is specified in the
     * request.
     * <p>
     * The 'mediaType' method is used to specify the supported media types.
     * <p>
     * The supported media types are JSON, XML and YAML.
     */
    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        configurer.favorParameter(false).ignoreAcceptHeader(false)
                .useRegisteredExtensionsOnly(false).defaultContentType(MediaType.APPLICATION_JSON)
                .mediaType("json", MediaType.APPLICATION_JSON)
                .mediaType("xml", MediaType.APPLICATION_XML)
                .mediaType("x-yaml", MEDIA_TYPE_APPLICATION_YML);
    }

    /**
     * Configure CORS mappings for the application.
     * 
     * The allowed methods are POST, GET, PUT, DELETE, OPTIONS and PATCH.
     * The allowed origins are taken from the 'cors.originPatterns' property,
     * which defaults to 'http://localhost:8080,http://localhost:4200' if not
     * specified.
     * All headers are allowed and credentials are allowed.
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedMethods("POST", "GET", "PUT", "DELETE", "OPTIONS", "PATCH")
                .allowedOrigins(corsOriginPatterns.split(","))
                .allowedHeaders("*")
                .allowCredentials(Boolean.TRUE);
    }
}
