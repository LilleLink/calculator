package calc;

import java.util.*;

import static java.lang.Double.NaN;
import static java.lang.Math.pow;
import static java.lang.System.out;


/*
 *   A calculator for rather simple arithmetic expressions
 *
 *   This is not the program, it's a class declaration (with methods) in it's
 *   own file (which must be named Calculator.java)
 *
 *   NOTE:
 *   - No negative numbers implemented
 */
class Calculator {

    // Here are the only allowed instance variables!
    // Error messages (more on static later)
    final static String MISSING_OPERAND = "Missing or bad operand";
    final static String DIV_BY_ZERO = "Division with 0";
    final static String MISSING_OPERATOR = "Missing operator or parenthesis";
    final static String OP_NOT_FOUND = "Operator not found";

    // Definition of operators
    final static String OPERATORS = "+-*/^";

    // Method used in REPL
    double eval(String expr) {
        if (expr.length() == 0) {
            return NaN;
        }
        List<String> tokens = tokenize(expr);
        List<String> postfix = infix2Postfix(tokens);
        return evalPostfix(postfix);
    }

    // ------  Evaluate RPN expression -------------------

    public double evalPostfix(List<String> postfix) {
        return 0;  // TODO
    }


    double applyOperator(String op, double d1, double d2) {
        switch (op) {
            case "+":
                return d1 + d2;
            case "-":
                return d2 - d1;
            case "*":
                return d1 * d2;
            case "/":
                if (d1 == 0) {
                    throw new IllegalArgumentException(DIV_BY_ZERO);
                }
                return d2 / d1;
            case "^":
                return pow(d2, d1);
        }
        throw new RuntimeException(OP_NOT_FOUND);
    }

    // ------- Infix 2 Postfix ------------------------

    public List<String> infix2Postfix(List<String> tokens) {
        return null; // TODO
    }


    int getPrecedence(String op) {
        if ("+-".contains(op)) {
            return 2;
        } else if ("*/".contains(op)) {
            return 3;
        } else if ("^".contains(op)) {
            return 4;
        } else {
            throw new RuntimeException(OP_NOT_FOUND);
        }
    }

    Assoc getAssociativity(String op) {
        if ("+-*/".contains(op)) {
            return Assoc.LEFT;
        } else if ("^".contains(op)) {
            return Assoc.RIGHT;
        } else {
            throw new RuntimeException(OP_NOT_FOUND);
        }
    }

    enum Assoc {
        LEFT,
        RIGHT
    }

    // ---------- Tokenize -----------------------

    public List<String> tokenize(String expr) {
        out.println("Tokenizing expression: "+expr);

        //Below does not work

        List<String> res = new ArrayList<>();
        char[] exprArr = expr.toCharArray();
        for (int i = 0; i < expr.length(); i++) {
            if (Character.isDigit(exprArr[i])) {
                // Number is [i,stopIndex)
                int stopIndex = extractNumber(i, exprArr);
                String s = getArrayPart(i, stopIndex, exprArr);
                res.add(s);
                if(stopIndex-i > 1)
                    i = stopIndex-1;
            } else if (!Character.isDigit(exprArr[i]) && !Character.isWhitespace(exprArr[i])) {
                res.add(String.valueOf(exprArr[i]));
            }
        }

        // Best way:
        // Start counting if current is numerical, stop when operator. Add to list
        // Add operator to list
        // Continue

        out.println("Result of tokenization: "+res);
        return res;
    }

    private String getArrayPart(int i, int stopIndex, char[] exprArr) {
        StringBuilder s = new StringBuilder();
        for(int index = i; index < stopIndex; index++) {
            s.append(exprArr[index]);
        }
        return s.toString();
    }

    private int extractNumber(int startIndex, char[] exprArr) {
        for(int i = startIndex; i < exprArr.length; i++) {
            if(!Character.isDigit(exprArr[i])) {
                return i;
            } else if(i == exprArr.length-1) {
                return exprArr.length;
            }
        }
        return -1;
    }

}
