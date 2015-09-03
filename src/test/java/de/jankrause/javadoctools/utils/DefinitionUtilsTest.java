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
import org.junit.Test;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class DefinitionUtilsTest {

    @Test
    public void shouldReturnTheFirstCharacterOfTheDefinedTerm(){
        assertEquals(Character.valueOf('A'), DefinitionUtils.firstCharacterAsUpperCase(new Definition("automobile", "")));
        assertEquals(Character.valueOf('C'), DefinitionUtils.firstCharacterAsUpperCase(new Definition("Car", "")));
    }

    @Test(expected = RuntimeException.class)
    public void shouldThrowARuntimeExceptionForEmptyString(){
        assertEquals(Character.valueOf('a'), DefinitionUtils.firstCharacterAsUpperCase(new Definition("", "")));
    }

    @Test(expected = RuntimeException.class)
    public void shouldThrowARuntimeExceptionForSpaceString(){
        assertEquals(Character.valueOf('a'), DefinitionUtils.firstCharacterAsUpperCase(new Definition("   ", "")));
    }

    @Test
    public void shouldInitializeMapWithUpperCaseLettersAndEmptyLists(){
        Map<Character, List<Definition>> exspected = new HashMap<Character, List<Definition>>();

        for(char character = 'A'; character <= 'Z'; character++){
            exspected.put(Character.valueOf(character), new LinkedList<Definition>());
        }

        assertEquals(exspected, DefinitionUtils.initAlphabet());
    }


}
