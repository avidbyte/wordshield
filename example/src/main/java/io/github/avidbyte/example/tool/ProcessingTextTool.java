package io.github.avidbyte.example.tool;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @author Aaron
 * @since 2024-08-29
 */
public class ProcessingTextTool {


    public static void main(String[] args) {
        String inputFilePath = "sensi_words.txt"; // 输入文件路径
        String outputFilePath = "sensi_words1.txt"; // 输出文件路径
        removeAsterisksFromWords(inputFilePath, outputFilePath);
    }

    /**
     * 移除敏感词末尾的*
     *
     * @param inputFilePath  输入文件路径
     * @param outputFilePath 输出文件路径
     */
    public static void removeAsterisksFromWords(String inputFilePath, String outputFilePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFilePath));
             BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath))) {

            String line;
            while ((line = reader.readLine()) != null) {
                String processedLine = line.replaceAll("(.*?)\\*", "$1"); // 移除末尾的 *
                writer.write(processedLine);
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error processing file: " + e.getMessage());
        }
    }

}
