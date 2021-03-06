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

        Deque<Double> stack = new ArrayDeque<>();
        double res;

        for(String s : postfix) {

            if (isNumber(s)) {
                stack.push(Double.parseDouble(s));
            } else if (OPERATORS.contains(s)) {
                if (stack.size() == 1)
                    throw new IllegalArgumentException(MISSING_OPERAND);
                double op1 = stack.pop();
                double op2 = stack.pop();
                stack.push(applyOperator(s, op1, op2));
            }
        }

        if (stack.size() == 1) {
            res = stack.pop();
        } else {
            throw new IllegalArgumentException(MISSING_OPERATOR);
        }

        return res;
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
        Deque<String> stack = new ArrayDeque<>();
        List<String> postFix = new ArrayList<>();

        for (String current : tokens) {
            if (isNumber(current)) {    // If current is a number, add to postfix
                postFix.add(current);
            } else if (OPERATORS.contains(current)) { // If current is an operator
                while (stack.peek() != null && validForOpWhile(current, stack)) { // Top is an operator, has greater precedence or same precedence and left assoc, and is not left par
                    popToList(stack, postFix);
                }
                stack.push(current);

            } else if (current.equals("(")) { // If left parenthesis

                stack.push(current);

            } else if (current.equals(")")) { // If right parenthesis
                while (!stack.peek().equals("(")) {
                    postFix.add(stack.pop());
                    if (stack.isEmpty()) {
                        throw new IllegalArgumentException(MISSING_OPERATOR);
                    }
                }
                if (stack.peek().equals("(")) {
                    stack.pop();
                }
            }
        }

        while (!stack.isEmpty()) {
            if ("()".contains(stack.peek()))
                throw new IllegalArgumentException(MISSING_OPERATOR);
            else
            postFix.add(stack.pop());
        }
        //out.println("Result of postfix-conversion: "+postFix);
        return postFix;
    }

    private boolean validForOpWhile(String current, Deque<String> stack) {
        return OPERATORS.contains(stack.peek()) && !stack.peek().equals("(") && (getPrecedence(stack.peek()) > getPrecedence(current) ||
                (getPrecedence(stack.peek()) == getPrecedence(current) && getAssociativity(current) == Assoc.LEFT));
    }

    private void popToList(Deque<String> stack, List<String> postFix) {
        while(!stack.isEmpty()) {
            postFix.add(stack.pop());
        }
    }

    public boolean isNumber(String s) {
        int nr = 0;
        try {
            nr = Integer.parseInt(s);
        } catch (Exception e) {
            return false;
        }
        return true;
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
        //out.println("Tokenizing expression: "+expr);

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

        //out.println("Result of tokenization: "+res);
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
