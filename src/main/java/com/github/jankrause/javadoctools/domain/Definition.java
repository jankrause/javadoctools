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
package com.github.jankrause.javadoctools.domain;

public class Definition extends NamedObject implements Comparable<Definition>{

    private String definition;

    public Definition(String term, String definition) {
        super(term);
        this.definition = definition;
    }

    public String definition() {
        return this.definition;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Definition that = (Definition) o;

        return !(definition != null ? !definition.equals(that.definition) : that.definition != null);

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (definition != null ? definition.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Definition{" +
                "definition='" + definition + '\'' +
                '}';
    }

    public int compareTo(Definition definition) {
        return name().compareTo(definition.name());
    }
}
