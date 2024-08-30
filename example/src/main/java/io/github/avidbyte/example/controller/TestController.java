package io.github.avidbyte.example.controller;


import io.github.avidbyte.SensitiveWordFilter;
import io.github.avidbyte.SensitiveWordResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author Aaron
 * @since 2022-12-08 15:27
 */
@Slf4j
@RestController
@RequestMapping("/test")
public class TestController {

    @Resource
    private SensitiveWordFilter sensitiveWordFilter;

    /**
     * @param text text
     */
    @GetMapping("/message")
    public SensitiveWordResult redisTemplateUtil(@RequestParam("text") String text) {
        SensitiveWordResult sensitiveWordResult = sensitiveWordFilter.checkAndFilter(text, "*");
        log.info("contains Sensitive Word : {}", sensitiveWordResult.isContainsSensitiveWord());
        log.info("filtered Text : {}", sensitiveWordResult.getFilteredText());
        return sensitiveWordResult;
    }


}
