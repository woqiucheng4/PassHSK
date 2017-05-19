package com.qc.hsk.network.value;

import java.io.Serializable;
import java.util.List;

public class WordDetail implements Serializable {

    private String characterName;//汉字

    private String pinyin;//拼音

    private List<String> english;//英文注释

    private List<String> components;//组成部分

    private List<Sentence> sentences;//例句

    public String getCharacterName() {
        return characterName;
    }

    public void setCharacterName(String characterName) {
        this.characterName = characterName;
    }

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }

    public List<String> getEnglish() {
        return english;
    }

    public void setEnglish(List<String> definition) {
        this.english = definition;
    }


    public List<String> getComponents() {
        return components;
    }

    public void setComponents(List<String> components) {
        this.components = components;
    }

    public List<Sentence> getSentences() {
        return sentences;
    }

    public void setSentences(List<Sentence> sentences) {
        this.sentences = sentences;
    }

    @Override
    public String toString() {
        return "WordDetail{" + "characterName='" + characterName + '\'' + ", pinyin='" + pinyin + '\'' + ", english=" + english + ", components=" + components + ", sentences=" + sentences + '}';
    }
}
