package org.ifno.restquest;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.apache.bcel.Constants;
import org.apache.bcel.Repository;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.generic.*;

import java.io.IOException;
import java.util.*;

import static org.apache.bcel.Constants.*;

/**
 * Created by Maksym.Palamarchuk (Maksym.Palamarchuk@infopulse.com.ua)
 */
public class CompilerListener extends RESTQuestBaseListener {
    public static final String HEADERS_FIELD_NAME = "headers";
    private final String outputPath;
    private ClassGen outClass;
    private ConstantPoolGen constantPool;

    private InstructionFactory _factory;
    private String fullClassName;
    private MethodGen currentMethod;
    private final ObjectType hashMapType = new ObjectType(HashMap.class.getName());

    public CompilerListener(String outputPath) {
        this.outputPath = outputPath;
    }

    @Override
    public void enterRequestSegment(@NotNull RESTQuestParser.RequestSegmentContext ctx) {
        String methodName = ctx.Segment().getText();
        RESTQuestParser.ParamListContext paramListContext = ctx.paramList();
        if (paramListContext != null) {
            List<TerminalNode> paramsList = paramListContext.Identifier();
            Type[] paramTypes = new Type[paramsList.size()];
            Arrays.fill(paramTypes, Type.STRING);
            String[] paramNames = new String[paramsList.size()];
            for (int i = 0; i < paramsList.size(); i++) {
                TerminalNode terminalNode = paramsList.get(i);
                paramNames[i] = terminalNode.getText();
            }
            InstructionList instructionList = new InstructionList();

            currentMethod = new MethodGen(ACC_PUBLIC, Type.VOID, paramTypes, paramNames, methodName, fullClassName, instructionList, constantPool);
        }
    }

    @Override
    public void exitRequestSegment(@NotNull RESTQuestParser.RequestSegmentContext ctx) {
        InstructionList instructionList = currentMethod.getInstructionList();
        instructionList.append(InstructionFactory.createReturn(Type.VOID));
        currentMethod.setMaxStack();
        currentMethod.setMaxLocals();
        outClass.addMethod(currentMethod.getMethod());
        instructionList.dispose();
        currentMethod = null;
    }

    @Override
    public void enterRequest(@NotNull RESTQuestParser.RequestContext ctx) {
        String packagePath = ctx.packagePath().getText();
        String className = ctx.Identifier().getText();
        fullClassName = packagePath+"."+className;
        outClass = new ClassGen(fullClassName, "java.lang.Object",
                "<generated>", ACC_PUBLIC | ACC_SUPER,
                null);
        constantPool = outClass.getConstantPool();
        _factory = new InstructionFactory(outClass);

        createFields();
        createConstructor();
    }

    @Override
    public void exitRequest(@NotNull RESTQuestParser.RequestContext ctx) {
        try {
            outClass.getJavaClass().dump(outputPath+fullClassName.replace(".", "/")+".class");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createConstructor() {
        InstructionList il = new InstructionList();

        MethodGen method = new MethodGen(ACC_PUBLIC, Type.VOID, Type.NO_ARGS, new String[] {  }, "<init>", fullClassName, il, constantPool);
        il.append(InstructionFactory.createLoad(Type.OBJECT, 0));
        il.append(_factory.createInvoke(Object.class.getName(), "<init>", Type.VOID, Type.NO_ARGS, Constants.INVOKESPECIAL));
        il.append(InstructionFactory.createLoad(Type.OBJECT, 0));
        il.append(_factory.createNew(hashMapType));
        il.append(InstructionConstants.DUP);
        il.append(_factory.createInvoke(HashMap.class.getName(), "<init>", Type.VOID, Type.NO_ARGS, Constants.INVOKESPECIAL));
        il.append(_factory.createFieldAccess(fullClassName, "headers", hashMapType, Constants.PUTFIELD));
        il.append(InstructionFactory.createReturn(Type.VOID));
        method.setMaxStack();
        method.setMaxLocals();
        outClass.addMethod(method.getMethod());
        il.dispose();
    }

    private void createFields() {
        FieldGen fieldGen = new FieldGen(ACC_PRIVATE|ACC_FINAL, new ObjectType(HashMap.class.getName()), HEADERS_FIELD_NAME, constantPool);
        outClass.addField(fieldGen.getField());
    }

    @Override
    public void enterAssignment(@NotNull RESTQuestParser.AssignmentContext ctx) {
        InstructionList instructionList = currentMethod.getInstructionList();
        System.out.println("instructionList.append(InstructionFactory.createLoad(Type.OBJECT, 0));");
        System.out.println("instructionList.append(_factory.createFieldAccess(fullClassName, HEADERS_FIELD_NAME, hashMapType, Constants.GETFIELD));");
        instructionList.append(InstructionFactory.createLoad(Type.OBJECT, 0));
        instructionList.append(_factory.createFieldAccess(fullClassName, HEADERS_FIELD_NAME, hashMapType, Constants.GETFIELD));
    }

    @Override
    public void exitAssignment(@NotNull RESTQuestParser.AssignmentContext ctx) {
        InstructionList instructionList = currentMethod.getInstructionList();
        System.out.println("instructionList.append(_factory.createInvoke(\"java.util.HashMap\", \"put\", Type.OBJECT, new Type[] { Type.OBJECT, Type.OBJECT }, Constants.INVOKEVIRTUAL));");
        System.out.println("instructionList.append(InstructionConstants.POP);");
        instructionList.append(_factory.createInvoke("java.util.HashMap", "put", Type.OBJECT, new Type[] { Type.OBJECT, Type.OBJECT }, Constants.INVOKEVIRTUAL));
        instructionList.append(InstructionConstants.POP);
    }

    @Override
    public void enterKey(@NotNull RESTQuestParser.KeyContext ctx) {
        InstructionList instructionList = currentMethod.getInstructionList();
        TerminalNode stringLiteral = ctx.StringLiteral();
        if (stringLiteral != null) {
            System.out.println("PUSH"+stringLiteral.getSymbol().getText());
            instructionList.append(new PUSH(constantPool, stringLiteral.getSymbol().getText()));
        }
    }

    @Override
    public void enterValue(@NotNull RESTQuestParser.ValueContext ctx) {
        InstructionList instructionList = currentMethod.getInstructionList();
        TerminalNode stringLiteral = ctx.StringLiteral();
        if (stringLiteral != null) {
            System.out.println("PUSH"+stringLiteral.getSymbol().getText());
            instructionList.append(new PUSH(constantPool, stringLiteral.getSymbol().getText()));
            return;
        }

        RESTQuestParser.ParameterContext parameter = ctx.parameter();
        if (parameter != null) {
            String paramName = parameter.Parameter().getSymbol().getText();
            String[] argumentNames = currentMethod.getArgumentNames();
            for (int i = 0; i < argumentNames.length; i++) {
                if (argumentNames[i].equals(paramName.replace("$",""))) {
                    System.out.println("PUSH"+paramName);
                    instructionList.append(InstructionFactory.createLoad(Type.OBJECT, i+1));
                    return;
                }
            }
        }
    }
}
