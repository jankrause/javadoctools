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
package com.github.jankrause.javadoctools.traversers;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.RootDoc;
import com.sun.javadoc.Tag;

public class ClassDocTraverser {

    private ClassDocVisitor visitor;

    public ClassDocTraverser(ClassDocVisitor visitor) {
        this.visitor = visitor;
    }

    public void traverseComments(RootDoc rootDoc, String filterTagName) {
        ClassDoc[] classes = rootDoc.classes();

        for (int i = 0; i < classes.length; ++i) {
            ClassDoc classDoc = classes[i];

            Tag[] definitionTags = classDoc.tags(filterTagName);

            for(Tag definitionTag : definitionTags){
                this.visitor.visitClassComment(definitionTag);
            }
        }
    }
}
