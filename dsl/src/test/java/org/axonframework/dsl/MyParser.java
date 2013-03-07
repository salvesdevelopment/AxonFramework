/*
 * Copyright (c) 2010-2013. Axon Framework
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
 */

package org.axonframework.dsl;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.BufferedTokenStream;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.Token;
import test.EventDefinitionBaseListener;
import test.EventDefinitionLexer;
import test.EventDefinitionParser;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

/**
 * @author Allard Buijze
 */
public class MyParser {

    public static void main(String[] args) throws IOException {
        final InputStream input = new FileInputStream(
                "C:\\development\\workspaces\\axon\\axon\\dsl\\src\\test\\resources\\org\\axonframework\\dsl\\events.axon");
        EventDefinitionParser parser = new EventDefinitionParser(new BufferedTokenStream(new EventDefinitionLexer(
                new ANTLRInputStream(input))));
        parser.addErrorListener(new BaseErrorListener() {
            @Override
            public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line,
                                    int charPositionInLine,
                                    String msg, RecognitionException e) {
                System.out.println("Offending command on line " + line + ". " + ((Token) offendingSymbol).getText());
            }
        });
        parser.addParseListener(new EventDefinitionBaseListener() {
            @Override
            public void exitImportDefinition(EventDefinitionParser.ImportDefinitionContext ctx) {
                System.out.println("importing " + ctx.packageName().getText());
            }

            @Override
            public void exitIdentifierDefinition(EventDefinitionParser.IdentifierDefinitionContext ctx) {
                System.out.println("defining identifier " + ctx.identifierName().getText()
                                           + " that wraps a " + ctx.className().getText());
            }

            @Override
            public void exitEventDefinition(EventDefinitionParser.EventDefinitionContext ctx) {
                System.out.print("event " + ctx.className().getText());
                if (!ctx.metaDef().isEmpty() ) {
                    System.out.print(" (abstract)");
                }
                if (ctx.attributes() != null && !ctx.attributes()
                                                    .typeDefinition().isEmpty()) {
                    System.out.print(" with " + ctx.attributes().typeDefinition().size()
                                             + " parameters: ");
                    Iterator<EventDefinitionParser.TypeDefinitionContext> iterator = ctx.attributes()
                                                                                        .typeDefinition()
                                                                                        .iterator();
                    while (iterator.hasNext()) {
                        EventDefinitionParser.TypeDefinitionContext type = iterator.next();
                        System.out.print(type.paramType().getText() + " " + type.paramName().getText());
                        if (iterator.hasNext()) {
                            System.out.print(", ");
                        }
                    }
                    System.out.println();
                } else {
                    System.out.println(" without parameters");
                }
                if (ctx.classContent() != null) {
                    System.out.println("Special content: " + ctx.classContent().getText());
                }
            }

            @Override
            public void exitValueObjectDefinition(EventDefinitionParser.ValueObjectDefinitionContext ctx) {
                System.out.println(
                        "defining value object " + ctx.className().getText() + " with "
                                + ctx.attributes().typeDefinition().size() + " parameters");
            }
        });
        while (parser.getCurrentToken().getType() != Token.EOF) {
            parser.statement();
        }
        input.close();
    }
}
