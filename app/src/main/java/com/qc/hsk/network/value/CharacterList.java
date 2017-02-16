package com.qc.hsk.network.value;

import java.io.Serializable;
import java.util.List;

/**
 * <ul>
 * <li>功能职责：HSK汉字列表</li>
 * </ul>
 *
 * @author chengqiu
 * @date 2016-11-10
 */
public class CharacterList implements Serializable {

    private static final long serialVersionUID = 135634422542L;

    private List<Character> characterList;

    public List<Character> getCharacterList() {
        return characterList;
    }

    public void setCharacterList(List<Character> characterList) {
        this.characterList = characterList;
    }


}
