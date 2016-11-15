package com.qc.hsk.network.value;

import java.io.Serializable;

public class Character implements Serializable {

    private static final long serialVersionUID = 1390892342542L;

    private String characterName;//汉字

    private String pinyin;//拼音

    private String definition;//英文注释

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

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }


}
