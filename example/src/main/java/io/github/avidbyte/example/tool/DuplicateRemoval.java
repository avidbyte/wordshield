package io.github.avidbyte.example.tool;


import java.io.*;
import java.util.HashSet;
import java.util.Set;

/**
 *  移除重复敏感词
 * @author Aaron
 * @since 2024-08-22
 */
public class DuplicateRemoval {

    public static void main(String[] args) {
        String inputFilePath = "sensi_words.txt"; // 输入文件路径
        String outputFilePath = "sensi_words1.txt"; // 输出文件路径

        try {
            Set<String> uniqueWords = readSensitiveWords(inputFilePath);
            writeUniqueWords(outputFilePath, uniqueWords);
            System.out.println("去重完成，结果已保存至：" + outputFilePath);
        } catch (IOException e) {
            System.err.println("处理文件时发生错误: " + e.getMessage());
        }
    }

    /**
     * 从指定文件中读取敏感词并去重。
     *
     * @param filePath 文件路径
     * @return 去重后的敏感词集合
     * @throws IOException 如果读取文件时发生错误
     */
    private static Set<String> readSensitiveWords(String filePath) throws IOException {
        Set<String> uniqueWords = new HashSet<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // 去除空白并转换为小写
                String trimmedLine = line.trim();
                uniqueWords.add(trimmedLine);
            }
        }
        return uniqueWords;
    }

    /**
     * 将去重后的敏感词写入文件。
     *
     * @param filePath 文件路径
     * @param uniqueWords 去重后的敏感词集合
     * @throws IOException 如果写入文件时发生错误
     */
    private static void writeUniqueWords(String filePath, Set<String> uniqueWords) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (String word : uniqueWords) {
                writer.write(word);
                writer.newLine(); // 换行
            }
        }
    }


}
