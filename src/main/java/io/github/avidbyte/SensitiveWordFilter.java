package io.github.avidbyte;

import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * @author Aaron
 * @since 2024-08-09
 */
public class SensitiveWordFilter {

    private final SensitiveWordProperties sensitiveWordProperties;

    /**
     * 根结点
     */
    private final TrieNode root;


    public SensitiveWordFilter(SensitiveWordProperties sensitiveWordProperties) {
        this.root = new TrieNode();
        this.sensitiveWordProperties = sensitiveWordProperties;
        initializeFilter();
    }

    private void initializeFilter() {
        boolean loadDefault = sensitiveWordProperties.isLoadDefault();
        if (loadDefault) {
            // 从 classpath 加载默认文件 sensitive_word.txt
            loadSensitiveWordsFromClasspath();
        }

        String source = sensitiveWordProperties.getSource();

        if (StringUtils.isNotBlank(source)) {
            if ("file".equalsIgnoreCase(sensitiveWordProperties.getSource())) {
                String filePath = sensitiveWordProperties.getFilePath();
                if (filePath != null && !filePath.isEmpty()) {
                    loadSensitiveWordsFromFile(filePath);
                }
            }
            // 对于其他来源，提供公开方法供使用者手动添加敏感词
        }
    }

    private void loadSensitiveWordsFromClasspath() {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("sensitive_word.txt")) {
            if (inputStream == null) {
                throw new FileNotFoundException("Sensitive words resource not found: " + "sensitive_word.txt");
            }
            // 读取资源文件中的敏感词并进行处理
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                addSensitiveWord(line.trim());
            }
        } catch (IOException e) {
            // 处理文件读取异常
            System.err.println("Error loading sensitive words from resource: " + e.getMessage());
        }
    }

    private void loadSensitiveWordsFromFile(String filePath) {
        InputStream inputStream = null;
        try {
            // 尝试从文件路径加载，如果失败则从 classpath 加载默认文件
            File file = new File(filePath);
            if (file.exists() && file.isFile()) {
                inputStream = Files.newInputStream(file.toPath());
            } else {
                // 从 classpath 中加载默认文件
                inputStream = getClass().getClassLoader().getResourceAsStream(filePath);
                if (inputStream == null) {
                    throw new FileNotFoundException("Sensitive words file not found: " + filePath);
                }
            }

            // 读取文件中的敏感词并进行处理
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                addSensitiveWord(line.trim());
            }
        } catch (IOException e) {
            // 处理文件读取异常
            System.err.println("Error loading sensitive words from file: " + e.getMessage());
        } finally {
            // 关闭输入流
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    // 处理流关闭异常
                    System.err.println("Error closing input stream: " + e.getMessage());
                }
            }
        }
    }


    public TrieNode getRoot() {
        return root;
    }


    public void addSensitiveWords(List<String> words) {
        if (words == null || words.isEmpty()) {
            return;
        }
        for (String word : words) {
            addSensitiveWord(word);
        }
    }

    /**
     * 将敏感词添加到字典树
     *
     * @param word 敏感词
     */
    public void addSensitiveWord(String word) {
        if (StringUtils.isBlank(word)) {
            return;
        }
        TrieNode curr = root;
        String trim = word.trim();
        for (char ch : trim.toCharArray()) {
            if (!curr.child.containsKey(ch)) {
                curr.child.put(ch, new TrieNode());
            }
            curr = curr.child.get(ch);
        }
        curr.count++;
    }

    /**
     * 将敏感词从字典树中移除
     *
     * @param words 敏感词
     */
    public void removeSensitiveWords(List<String> words) {
        for (String word : words) {
            removeSensitiveWord(word);
        }
    }

    /**
     * 将敏感词从字典树中移除
     *
     * @param word 敏感词
     */
    public void removeSensitiveWord(String word) {
        if (StringUtils.isBlank(word)) {
            return;
        }
        TrieNode curr = root;
        String trim = word.trim();
        char[] chars = trim.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            char ch = chars[i];
            if (curr.child.containsKey(ch)) {
                curr = curr.child.get(ch);
                if (i == chars.length - 1) {
                    curr.count--;
                }
            }
        }
    }

    /**
     * 判断是否为特殊符号，是则返回 true，不是则返回 false
     *
     * @param c c
     * @return boolean
     */
    public boolean isSymbol(Character c) {
        // CharUtils.isAsciiAlphanumeric(c)方法：a、b、1、2···返回true，特殊字符返回false
        // 0x2E80  ~  0x9FFF 是东亚的文字范围，东亚文字范围我们不认为是符号
        return !CharUtils.isAsciiAlphanumeric(c) && (c < 0x2E80 || c > 0x9FFF);
    }


    /**
     * 检查是否有敏感词
     *
     * @param sentence sentence
     * @param replace  替换的字符 例如 **
     * @return SensitiveWordResult
     */
    public SensitiveWordResult checkAndFilter(String sentence, String replace) {
        if (StringUtils.isBlank(sentence)) {
            return new SensitiveWordResult(false, sentence);
        }
        String text = sentence.trim();
        TrieNode curr = root;
        int begin = 0;
        int position = 0;
        StringBuilder sb = new StringBuilder();
        boolean containsSensitiveWord = false;

        while (begin < text.length()) {
            if (position >= text.length()) {
                sb.append(text.charAt(begin));
                position = ++begin;
                continue;
            }
            char c = text.charAt(position);
            if (isSymbol(c)) {
                if (curr == root) {
                    sb.append(c);
                    begin++;
                }
                position++;
                continue;
            }
            curr = curr.child.get(c);
            if (curr == null) {
                sb.append(text.charAt(begin));
                position = ++begin;
                curr = root;
            } else if (curr.count > 0) {
                containsSensitiveWord = true;
                sb.append(replace);
                begin = ++position;
                curr = root;
            } else {
                position++;
            }
        }

        return new SensitiveWordResult(containsSensitiveWord, sb.toString());
    }

    /**
     * 返回所有的敏感词
     *
     * @return {@code List<String>} 敏感词列表
     */
    public List<String> getAllSensitiveWords() {
        List<String> sensitiveWords = new ArrayList<>();
        StringBuilder currentWord = new StringBuilder();
        collectSensitiveWords(root, currentWord, sensitiveWords);
        return sensitiveWords;
    }

    /**
     * 递归遍历字典树，收集所有敏感词
     *
     * @param node           当前节点
     * @param currentWord    当前路径上的词
     * @param sensitiveWords 敏感词列表
     */
    private void collectSensitiveWords(TrieNode node, StringBuilder currentWord, List<String> sensitiveWords) {
        if (node.count > 0) {
            sensitiveWords.add(currentWord.toString());
        }
        for (Map.Entry<Character, TrieNode> entry : node.child.entrySet()) {
            currentWord.append(entry.getKey());
            collectSensitiveWords(entry.getValue(), currentWord, sensitiveWords);
            currentWord.deleteCharAt(currentWord.length() - 1);
        }
    }


}
