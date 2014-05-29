package org.ifno.restquest;

import org.antlr.v4.runtime.ANTLRFileStream;
import org.antlr.v4.runtime.BufferedTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.apache.bcel.util.BCELifier;
import org.apache.bcel.verifier.Verifier;
import org.ifno.restquest.demo.TestService;

import java.io.IOException;

/**
 * Created by Maksym.Palamarchuk (Maksym.Palamarchuk@infopulse.com.ua)
 */
public class Compiler {

    public static void main(final String[] args) {
        try {
            RESTQuestLexer lexer = new RESTQuestLexer(new ANTLRFileStream(args[0]));
            RESTQuestParser parser = new RESTQuestParser(new BufferedTokenStream(lexer));

            RESTQuestParser.RequestContext request = parser.request();
            ParseTreeWalker walker = new ParseTreeWalker();
            walker.walk(new CompilerListener("./compiled/"), request);

            Verifier.main(new String[]{TestService.class.getName()});
            TestService testService = new TestService();
            testService.headers("userAgent", "BASIC");

            BCELifier.main(new String[]{TestClass.class.getName()});

        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
