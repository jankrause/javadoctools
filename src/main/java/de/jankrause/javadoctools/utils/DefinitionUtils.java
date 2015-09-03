/*******************************************************************************
 * Copyright 2015 Jan Christian Krause
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package de.jankrause.javadoctools.utils;

import de.jankrause.javadoctools.domain.Definition;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class DefinitionUtils {

    public static Character firstCharacterAsUpperCase(Definition definition){
        if(!definition.name().trim().isEmpty()) {
            return Character.valueOf(definition.name().toUpperCase().charAt(0));
        } else {
            throw new RuntimeException("Cannot return the first character of an empty string.");
        }
    }

    public static Map<Character, List<Definition>> initAlphabet(){
        Map<Character, List<Definition>> alphabet = new HashMap<Character, List<Definition>>();

        for(char character = 'A'; character <= 'Z'; character++){
            alphabet.put(character, new LinkedList<Definition>());
        }

        return alphabet;
    }

    public static Map<Character, List<Definition>> sortByFirstCharacter(List<Definition> definitions){
        Map<Character, List<Definition>> sortedByFirstCharacter = initAlphabet();

        for(Definition definition : definitions){
            Character firstCharacter = firstCharacterAsUpperCase(definition);
            sortedByFirstCharacter.get(firstCharacter).add(definition);
        }

        return sortedByFirstCharacter;
    }

}
