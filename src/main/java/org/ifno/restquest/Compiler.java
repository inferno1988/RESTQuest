package org.ifno.restquest;

import org.antlr.v4.runtime.ANTLRFileStream;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.BufferedTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.atn.ATN;
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.io.IOException;
import java.util.List;

/**
 * Created by Maksym.Palamarchuk (Maksym.Palamarchuk@infopulse.com.ua)
 */
public class Compiler {

    public static void main(String[] args) {
        try {
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
                    System.out.println(ctx.getText());
                }

                @Override
                public void exitSimpleSegment(@NotNull RESTQuestParser.SimpleSegmentContext ctx) {
                    System.out.println(ctx.getText());
                }

                @Override
                public void enterImportPath(@NotNull RESTQuestParser.ImportPathContext ctx) {
                    System.out.println(ctx.getText());
                }

                @Override
                public void exitImportPath(@NotNull RESTQuestParser.ImportPathContext ctx) {
                    System.out.println(ctx.getText());
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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
