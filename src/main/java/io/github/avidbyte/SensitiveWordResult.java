package io.github.avidbyte;

import java.io.Serializable;

/**
 * @author Aaron
 * @since 2024-08-14
 */
public class SensitiveWordResult implements Serializable {

    private boolean containsSensitiveWord;
    private String filteredText;

    public SensitiveWordResult(boolean containsSensitiveWord, String filteredText) {
        this.containsSensitiveWord = containsSensitiveWord;
        this.filteredText = filteredText;
    }

    public boolean isContainsSensitiveWord() {
        return containsSensitiveWord;
    }

    public void setContainsSensitiveWord(boolean containsSensitiveWord) {
        this.containsSensitiveWord = containsSensitiveWord;
    }

    public String getFilteredText() {
        return filteredText;
    }

    public void setFilteredText(String filteredText) {
        this.filteredText = filteredText;
    }

}
