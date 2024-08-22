package io.github.avidbyte;


import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.logging.Logger;


/**
 * @author Aaron
 * @since 2024-08-21
 */

@Configuration
@EnableConfigurationProperties({SensitiveWordProperties.class})
public class SensitiveWordAutoConfiguration {

    private static final Logger logger = Logger.getLogger(SensitiveWordAutoConfiguration.class.getName());

    public SensitiveWordAutoConfiguration(SensitiveWordProperties sensitiveWordProperties) {
        this.sensitiveWordProperties = sensitiveWordProperties;
    }

    private final SensitiveWordProperties sensitiveWordProperties;

    @Bean
    @ConditionalOnMissingBean
    public SensitiveWordFilter sensitiveWordFilter() {
        logger.info("io.github.avidbyte.SensitiveWordProperties.loadDefault: " + sensitiveWordProperties.isLoadDefault());
        logger.info("io.github.avidbyte.SensitiveWordProperties.source: " + sensitiveWordProperties.getSource());
        logger.info("io.github.avidbyte.SensitiveWordProperties.filePath: " + sensitiveWordProperties.getFilePath());
        return new SensitiveWordFilter(sensitiveWordProperties);
    }

}
