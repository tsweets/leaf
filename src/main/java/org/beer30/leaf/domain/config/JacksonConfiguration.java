package org.beer30.leaf.domain.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jodamoney.JodaMoneyModule;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

/**
 * @author tsweets
 * Date: 3/28/21 - 7:02 PM
 */
@Configuration
public class JacksonConfiguration extends MappingJackson2HttpMessageConverter {

    public JacksonConfiguration(ObjectMapper objectMapper) {
        super(objectMapper);

        objectMapper.registerModule(new JodaMoneyModule());
    }

/*
    @Bean
    MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter() {
        return new CustomHttpMessageConverter();
    }

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper()
                .registerModule(new JodaMoneyModule());
        return mapper;
    }
*/
}
