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
package com.github.jankrause.javadoctools.utils;

import com.github.jankrause.javadoctools.domain.Column;
import com.github.jankrause.javadoctools.domain.Table;

public class JavaInlineTagParser {

    public static Table fetchTable(String text){
        return new Table(text);
    }

    public static Column fetchColumn(String text){
        if(text.indexOf('.') >= 0){
            String[] names = text.split("\\.");
            return new Column(new Table(names[0]), names[1]);
        }

        return new Column(new Table(""), "");
    }
}
