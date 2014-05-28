package org.ifno.restquest;

import javassist.*;
import org.antlr.v4.runtime.ANTLRFileStream;
import org.antlr.v4.runtime.BufferedTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.ifno.restquest.compiler.output.TestService;

import java.io.IOException;
import java.util.List;

/**
 * Created by Maksym.Palamarchuk (Maksym.Palamarchuk@infopulse.com.ua)
 */
public class Compiler {

    public static void main(final String[] args) {
        try {

            final ClassPool pool = ClassPool.getDefault();
            pool.importPackage("org.springframework.http");
            final CtClass stringClass = pool.get(String.class.getName());
            final CtClass[] outClass = {null};

            RESTQuestLexer lexer = new RESTQuestLexer(new ANTLRFileStream(args[0]));
            RESTQuestParser parser = new RESTQuestParser(new BufferedTokenStream(lexer));
            RESTQuestListener listener = new RESTQuestListener() {
                @Override
                public void enterExpression(@NotNull RESTQuestParser.ExpressionContext ctx) {

                }

                @Override
                public void exitExpression(@NotNull RESTQuestParser.ExpressionContext ctx) {

                }

                @Override
                public void enterRequestBody(@NotNull RESTQuestParser.RequestBodyContext ctx) {

                }

                @Override
                public void exitRequestBody(@NotNull RESTQuestParser.RequestBodyContext ctx) {

                }

                @Override
                public void enterTypedSegment(@NotNull RESTQuestParser.TypedSegmentContext ctx) {

                }

                @Override
                public void exitTypedSegment(@NotNull RESTQuestParser.TypedSegmentContext ctx) {

                }

                @Override
                public void enterBlock(@NotNull RESTQuestParser.BlockContext ctx) {

                }

                @Override
                public void exitBlock(@NotNull RESTQuestParser.BlockContext ctx) {

                }

                @Override
                public void enterSegmentDeclaration(@NotNull RESTQuestParser.SegmentDeclarationContext ctx) {

                }

                @Override
                public void exitSegmentDeclaration(@NotNull RESTQuestParser.SegmentDeclarationContext ctx) {

                }

                @Override
                public void enterType(@NotNull RESTQuestParser.TypeContext ctx) {

                }

                @Override
                public void exitType(@NotNull RESTQuestParser.TypeContext ctx) {

                }

                @Override
                public void enterRequestDeclaration(@NotNull RESTQuestParser.RequestDeclarationContext ctx) {
                    outClass[0] = pool.makeClass("org.ifno.restquest.compiler.output." + ctx.Identifier().getSymbol().getText());
                }

                @Override
                public void exitRequestDeclaration(@NotNull RESTQuestParser.RequestDeclarationContext ctx) {

                }

                @Override
                public void enterFunctionCall(@NotNull RESTQuestParser.FunctionCallContext ctx) {
                    RESTQuestParser.ExpressionContext expression = ctx.expression();

                }

                @Override
                public void exitFunctionCall(@NotNull RESTQuestParser.FunctionCallContext ctx) {

                }

                @Override
                public void enterSimpleSegment(@NotNull RESTQuestParser.SimpleSegmentContext ctx) {
                    if (ctx.CONFIG() != null) {
                        RESTQuestParser.BlockContext configBlock = ctx.block();
                        List<RESTQuestParser.FunctionCallContext> functionCallList = configBlock.functionCall();

                        for (RESTQuestParser.FunctionCallContext functionCallContext : functionCallList) {
                            if (functionCallContext.METHOD() != null) {
                                String fieldName = functionCallContext.METHOD().getSymbol().getText();
                                String fieldInitialValue = functionCallContext.StringLiteral().getSymbol().getText();
                                createStringFieldAndGetter(fieldName, fieldInitialValue, outClass[0]);
                            }

                            if (functionCallContext.URL() != null) {
                                String fieldName = functionCallContext.URL().getSymbol().getText();
                                String fieldInitialValue = functionCallContext.StringLiteral().getSymbol().getText();
                                createStringFieldAndGetter(fieldName, fieldInitialValue, outClass[0]);
                            }
                        }
                    }
                }

                private void createStringFieldAndGetter(String fieldName, String initialValue, CtClass declaringClass) {
                    try {
                        CtField urlField = new CtField(stringClass, fieldName, declaringClass);
                        urlField.setModifiers(Modifier.PRIVATE | Modifier.FINAL);
                        declaringClass.addField(urlField, initialValue);
                        String getterName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
                        CtMethod newStringMethod = CtMethod.make("public String " + getterName + "() {return this." + fieldName + ";}", outClass[0]);
                        declaringClass.addMethod(newStringMethod);

                    } catch (CannotCompileException e) {
                        e.printStackTrace();
                    }
                }


                @Override
                public void exitSimpleSegment(@NotNull RESTQuestParser.SimpleSegmentContext ctx) {

                }

                @Override
                public void enterImportPath(@NotNull RESTQuestParser.ImportPathContext ctx) {
                    List<TerminalNode> nodeList = ctx.Identifier();
                    String importPackage = nodeList.get(0).getSymbol().getText();
                    for (int i = 1; i < nodeList.size()-1; i++) {
                        TerminalNode terminalNode = nodeList.get(i);
                        importPackage += "."+terminalNode.getSymbol().getText();
                    }
                    pool.importPackage(importPackage);
                }

                @Override
                public void exitImportPath(@NotNull RESTQuestParser.ImportPathContext ctx) {

                }

                @Override
                public void enterCompileObject(@NotNull RESTQuestParser.CompileObjectContext ctx) {

                }

                @Override
                public void exitCompileObject(@NotNull RESTQuestParser.CompileObjectContext ctx) {

                }

                @Override
                public void enterParameterList(@NotNull RESTQuestParser.ParameterListContext ctx) {

                }

                @Override
                public void exitParameterList(@NotNull RESTQuestParser.ParameterListContext ctx) {

                }

                @Override
                public void enterImportDeclaration(@NotNull RESTQuestParser.ImportDeclarationContext ctx) {

                }

                @Override
                public void exitImportDeclaration(@NotNull RESTQuestParser.ImportDeclarationContext ctx) {

                }

                @Override
                public void visitTerminal(@NotNull TerminalNode terminalNode) {

                }

                @Override
                public void visitErrorNode(@NotNull ErrorNode errorNode) {

                }

                @Override
                public void enterEveryRule(@NotNull ParserRuleContext parserRuleContext) {

                }

                @Override
                public void exitEveryRule(@NotNull ParserRuleContext parserRuleContext) {

                }
            };
            RESTQuestParser.CompileObjectContext compileObjectContext = parser.compileObject();
            ParseTreeWalker walker = new ParseTreeWalker();
            walker.walk(listener, compileObjectContext);

            if (outClass[0] != null) {
                outClass[0].writeFile("./compiled/");
            }

            TestService testService = new TestService();
            System.out.printf("Method: %s, and Url %s", testService.getMethod(), testService.getUrl());

        } catch (IOException e) {
            e.printStackTrace();
        } catch (CannotCompileException e) {
            e.printStackTrace();
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
    }
}
