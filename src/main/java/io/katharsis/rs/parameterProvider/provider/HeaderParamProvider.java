package io.katharsis.rs.parameterProvider.provider;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.ws.rs.HeaderParam;
import javax.ws.rs.container.ContainerRequestContext;
import java.io.IOException;

public class HeaderParamProvider implements RequestContextParameterProvider<Object> {

    @Override
    public Object provideValue(Parameter parameter, ContainerRequestContext requestContext, ObjectMapper objectMapper) {
        Object returnValue;
        String value = requestContext.getHeaderString(parameter.getAnnotation(HeaderParam.class).value());
        if (value == null) {
            return null;
        } else {
            if (String.class.isAssignableFrom(parameter.getType())) {
                returnValue = value;
            } else {
                try {
                    returnValue = objectMapper.readValue(value, parameter.getType());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        return returnValue;
    }

    @Override
    public boolean provides(Parameter parameter) {
        return parameter.isAnnotationPresent(HeaderParam.class);
    }
}
