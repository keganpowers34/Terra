/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.terrascript.parser;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dfsek.terra.addons.terrascript.parser.exceptions.ParseException;
import com.dfsek.terra.addons.terrascript.parser.lang.Expression;
import com.dfsek.terra.addons.terrascript.tokenizer.Token;


public class ParserUtil {
    
    private static final Map<Token.Type, Map<Token.Type, Boolean>> PRECEDENCE = new HashMap<>(); // If second has precedence, true.
    private static final List<Token.Type> ARITHMETIC = Arrays.asList(Token.Type.ADDITION_OPERATOR, Token.Type.SUBTRACTION_OPERATOR,
                                                                     Token.Type.MULTIPLICATION_OPERATOR, Token.Type.DIVISION_OPERATOR,
                                                                     Token.Type.MODULO_OPERATOR);
    private static final List<Token.Type> COMPARISON = Arrays.asList(Token.Type.EQUALS_OPERATOR, Token.Type.NOT_EQUALS_OPERATOR,
                                                                     Token.Type.LESS_THAN_OPERATOR, Token.Type.LESS_THAN_OR_EQUALS_OPERATOR,
                                                                     Token.Type.GREATER_THAN_OPERATOR,
                                                                     Token.Type.GREATER_THAN_OR_EQUALS_OPERATOR);
    
    static { // Setup precedence
        Map<Token.Type, Boolean> add = new HashMap<>(); // Addition/subtraction before Multiplication/division.
        add.put(Token.Type.MULTIPLICATION_OPERATOR, true);
        add.put(Token.Type.DIVISION_OPERATOR, true);
        
        PRECEDENCE.put(Token.Type.ADDITION_OPERATOR, add);
        PRECEDENCE.put(Token.Type.SUBTRACTION_OPERATOR, add);
        
        Map<Token.Type, Boolean> numericBoolean = new HashMap<>();
        
        ARITHMETIC.forEach(op -> numericBoolean.put(op, true)); // Numbers before comparison
        COMPARISON.forEach(op -> PRECEDENCE.put(op, numericBoolean));
        
        
        Map<Token.Type, Boolean> booleanOps = new HashMap<>();
        ARITHMETIC.forEach(op -> booleanOps.put(op, true)); // Everything before boolean
        COMPARISON.forEach(op -> booleanOps.put(op, true));
        
        
        PRECEDENCE.put(Token.Type.BOOLEAN_AND, booleanOps);
        PRECEDENCE.put(Token.Type.BOOLEAN_OR, booleanOps);
    }
    
    public static void ensureType(Token token, Token.Type... expected) {
        for(Token.Type type : expected) if(token.getType().equals(type)) return;
        throw new ParseException("Expected " + Arrays.toString(expected) + " but found " + token.getType(), token.getPosition());
    }
    
    public static void ensureReturnType(Expression<?> returnable, Expression.ReturnType... types) {
        for(Expression.ReturnType type : types) if(returnable.returnType().equals(type)) return;
        throw new ParseException("Expected " + Arrays.toString(types) + " but found " + returnable.returnType(), returnable.getPosition());
    }
    
    public static void checkArithmeticOperation(Expression<?> left, Expression<?> right, Token operation) {
        if(!left.returnType().equals(Expression.ReturnType.NUMBER) || !right.returnType().equals(Expression.ReturnType.NUMBER)) {
            throw new ParseException(
                    "Operation " + operation.getType() + " not supported between " + left.returnType() + " and " + right.returnType(),
                    operation.getPosition());
        }
    }
    
    public static void checkBooleanOperation(Expression<?> left, Expression<?> right, Token operation) {
        if(!left.returnType().equals(Expression.ReturnType.BOOLEAN) || !right.returnType().equals(Expression.ReturnType.BOOLEAN)) {
            throw new ParseException(
                    "Operation " + operation.getType() + " not supported between " + left.returnType() + " and " + right.returnType(),
                    operation.getPosition());
        }
    }
    
    public static void checkVarType(Token token, Expression.ReturnType returnType) {
        if(returnType.equals(Expression.ReturnType.STRING) && token.getType().equals(Token.Type.TYPE_STRING)) return;
        if(returnType.equals(Expression.ReturnType.NUMBER) && token.getType().equals(Token.Type.TYPE_NUMBER)) return;
        if(returnType.equals(Expression.ReturnType.BOOLEAN) && token.getType().equals(Token.Type.TYPE_BOOLEAN)) return;
        throw new ParseException("Type mismatch, cannot convert from " + returnType + " to " + token.getType(), token.getPosition());
    }
    
    /**
     * Checks if token is a binary operator
     *
     * @param token Token to check
     *
     * @throws ParseException If token isn't a binary operator
     */
    public static void checkBinaryOperator(Token token) {
        if(!token.isBinaryOperator())
            throw new ParseException("Expected binary operator, found " + token.getType(), token.getPosition());
    }
    
    public static Expression.ReturnType getVariableReturnType(Token varToken) {
        return switch(varToken.getType()) {
            case TYPE_NUMBER -> Expression.ReturnType.NUMBER;
            case TYPE_STRING -> Expression.ReturnType.STRING;
            case TYPE_BOOLEAN -> Expression.ReturnType.BOOLEAN;
            case TYPE_VOID -> Expression.ReturnType.VOID;
            default -> throw new ParseException("Unexpected token " + varToken.getType() + "; expected variable declaration",
                                                varToken.getPosition());
        };
    }
    
    public static boolean hasPrecedence(Token.Type first, Token.Type second) {
        if(!PRECEDENCE.containsKey(first)) return false;
        Map<Token.Type, Boolean> pre = PRECEDENCE.get(first);
        if(!pre.containsKey(second)) return false;
        return pre.get(second);
    }
}
