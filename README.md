[![构建状态](https://img.shields.io/travis/yourusername/wordshield.svg)](https://travis-ci.org/yourusername/wordshield)
[![许可证](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/io.github.avidbyte/wordshield/badge.svg)](https://maven-badges.herokuapp.com/maven-central/io.github.avidbyte/wordshield)


# 简介
WordShield 是一款轻量级且灵活的敏感词过滤库，基于 Spring Boot 构建。它提供了简单易用的 API，用于过滤和管理敏感词汇。

# 特性
敏感词过滤：自动过滤字符串中的敏感词汇。
默认敏感词列表：内置预设的敏感词列表。
可定制敏感词列表：支持从指定路径加载自定义敏感词列表。
动态管理：允许动态添加或移除敏感词。
Spring Boot 集成：自动在应用上下文中注册 SensitiveWordFilter Bean。


# 快速开始
## 准备工作
Java 8 或更高版本
Spring Boot 2.x 或更高版本
Maven 或 Gradle
## 添加依赖
Maven
在 pom.xml 文件中添加以下依赖：

```xml
<dependency>
    <groupId>io.github.avidbyte</groupId>
    <artifactId>wordshield</artifactId>
    <version>1.0.0</version>
</dependency>
```

Gradle
在 build.gradle 文件中添加以下依赖：
```groovy
dependencies {
    implementation 'io.github.avidbyte:wordshield:1.0.0'
}
```

## 使用方法
初始化过滤器
添加依赖后，SensitiveWordFilter Bean 会被自动注册。您可以在服务类中直接使用它：

```java
import io.github.avidbyte.wordshield.SensitiveWordFilter;

@Service
public class ContentService {

    private final SensitiveWordFilter sensitiveWordFilter;

    public ContentService(SensitiveWordFilter sensitiveWordFilter) {
        this.sensitiveWordFilter = sensitiveWordFilter;
    }

    public String filterContent(String content) {
        SensitiveWordResult sensitiveWordResult = sensitiveWordFilter.checkAndFilter(content, "*");
        log.info("文本: {} 是否是敏感词: {}", content, sensitiveWordResult.isContainsSensitiveWord());
        log.info("文本: {} 敏感词处理结果: {}", content, sensitiveWordResult.getFilteredText());
    }
}
```

## 自定义敏感词列表
默认情况下，库会加载预设的敏感词列表。不过，您也可以通过提供文件路径来自定义敏感词列表：

在 application.yml 配置

```yaml
sensitive:
  word:
    loadDefault: false
    source: file
    filePath: /opt/sensitive_word.txt
```

loadDefault 为 true, wordshield 会先加载预设的敏感词列表, 如果不希望使用预设的敏感词列表, 就将 loadDefault 设置为 false

其他数据来源: database, api, etc. 可以通过这种方式加载
```yaml
sensitive:
  word:
    loadDefault: false
    source: database
```
当前配置 wordshield 不会加载预设的敏感词, 敏感词库完全是空的, 需要使用者自己调用新增敏感词的方法构造敏感词库

```java
import io.github.avidbyte.wordshield.SensitiveWordFilter;

@Service
public class ContentService {

    private final SensitiveWordFilter sensitiveWordFilter;

    public ContentService(SensitiveWordFilter sensitiveWordFilter) {
        this.sensitiveWordFilter = sensitiveWordFilter;
    }

    public String addSensitiveWords(List<String> words) {
        sensitiveWordFilter.addSensitiveWords(words);
    }

    public String addSensitiveWord(String word) {
        sensitiveWordFilter.addSensitiveWord(word);
    }

    public String removeSensitiveWords(List<String> words) {
        sensitiveWordFilter.removeSensitiveWords(words);
    }

    public String removeSensitiveWord(String word) {
        sensitiveWordFilter.removeSensitiveWord(word);
    }
    
}
```
通过这四个方法也可以动态地管理敏感词列表