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

import com.github.jankrause.javadoctools.domain.CommentType;
import com.github.jankrause.javadoctools.utils.CommentTypeUtil;
import com.sun.javadoc.Doc;
import com.sun.javadoc.Tag;

import org.junit.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class CommentTypeUtilTest {

    @Test
    public void shouldDeriveField(){
        Doc mockedDoc = createDocMock(CommentType.FIELD);
        Tag mockedTag = Mockito.mock(Tag.class);
        when(mockedTag.holder()).thenReturn(mockedDoc);

        assertEquals(CommentTypeUtil.deriveCommentType(mockedTag), CommentType.FIELD);
    }

    @Test
    public void shouldDeriveClass(){
        Doc mockedDoc = createDocMock(CommentType.CLASS);
        Tag mockedTag = Mockito.mock(Tag.class);
        when(mockedTag.holder()).thenReturn(mockedDoc);

        assertEquals(CommentTypeUtil.deriveCommentType(mockedTag), CommentType.CLASS);
    }

    @Test
    public void shouldDeriveInterface(){
        Doc mockedDoc = createDocMock(CommentType.INTERFACE);
        Tag mockedTag = Mockito.mock(Tag.class);
        when(mockedTag.holder()).thenReturn(mockedDoc);

        assertEquals(CommentTypeUtil.deriveCommentType(mockedTag), CommentType.INTERFACE);
    }

    @Test
    public void shouldDeriveNone(){
        Doc mockedDoc = createDocMock(CommentType.NONE);
        Tag mockedTag = Mockito.mock(Tag.class);
        when(mockedTag.holder()).thenReturn(mockedDoc);

        assertEquals(CommentTypeUtil.deriveCommentType(mockedTag), CommentType.NONE);
    }

    private Doc createDocMock(CommentType type) {
        Doc mockedDoc = Mockito.mock(Doc.class);
        when(mockedDoc.isField()).thenReturn(type == CommentType.FIELD);
        when(mockedDoc.isInterface()).thenReturn(type == CommentType.INTERFACE);
        when(mockedDoc.isClass()).thenReturn(type == CommentType.CLASS);
        return mockedDoc;
    }

}
