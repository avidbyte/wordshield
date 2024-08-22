package io.github.avidbyte;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author Aaron
 * @since 2024-08-09
 */
@Configuration
@ConfigurationProperties(prefix = SensitiveWordProperties.SENSITIVE_WORD_PREFIX)
public class SensitiveWordProperties {

    public static final String SENSITIVE_WORD_PREFIX = "sensitive.word";

    private String source; // 数据来源: file, database, api, etc.
    private String filePath; // 文件路径
    private Boolean loadDefault = true; // 默认为true

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public boolean isLoadDefault() {
        return loadDefault;
    }

    public void setLoadDefault(boolean loadDefault) {
        this.loadDefault = loadDefault;
    }

}
