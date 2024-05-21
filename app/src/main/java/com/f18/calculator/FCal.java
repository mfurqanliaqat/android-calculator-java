package com.f18.calculator;

import java.math.BigDecimal;
import java.math.RoundingMode;
// Author: MUHAMMAD FURQAN LIAQAT
// Grammar:
// expression = term | expression `+` term | expression `-` term
// term = factor | term `*` factor | term `/` factor
// factor = `+` factor | `-` factor | `(` expression `)` | number | functionName factor | factor `^` factor
public class FCal {
    public static double evaluate(final String str) {
        return new Object() {
            int pos = -1, ch;

            int nextChar() { return ch = (++pos < str.length()) ? str.charAt(pos) : -1; }

            boolean eat(int charToEat) {
                while (ch == ' ') nextChar();
                if (ch == charToEat) {
                    nextChar();
                    return true;
                }
                return false;
            }

            double parse() {
                nextChar();
                double x = parseExpression();
                if (pos < str.length()) throw new RuntimeException("Unexpected: " + (char)ch);
                return x;
            }

            double parseExpression() {
                double x = parseTerm();
                for (;;) {
                    if      (eat('+')) x += parseTerm(); // addition
                    else if (eat('-') || eat('−')) x -= parseTerm(); // subtraction
                    else return x;
                }
            }

            double parseTerm() {
                double x = parseFactor();
                double y = x; int i;
                for (;;) {
                    if      (eat('*') || eat('×')) x *= parseFactor(); // multiplication
                    else if (eat('/') || eat('÷')) x /= parseFactor(); // division
                    else if (eat('π')) x *= Math.PI; // pi
                    else if (eat('%')) x /= 100; // percentage  // binary % is not handled
                    else if (eat('!')) for(i=1, x=1; i <= y; i++) x*=i; // factorial
                    else if ( eat('E')) x *= Math.pow(10, parseFactor()); // handle E values
                    else return x;
                }
            }

            double parseFactor() {
                if (eat('+')) return parseFactor(); // unary plus
                if (eat('-') || eat('−')) return -parseFactor(); // unary minus
                if (eat('π')) return Math.PI; // unary pi

                double x;
                int startPos = this.pos;
                if (eat('(')) { // parentheses
                    x = parseExpression();
                    eat(')');
                } else if ((ch >= '0' && ch <= '9') || ch == '.') { // numbers
                    while ((ch >= '0' && ch <= '9') || ch == '.') nextChar();
                    x = Double.parseDouble(str.substring(startPos, this.pos));
                } else if (ch >= 'a' && ch <= 'z') { // functions
                    while (ch >= 'a' && ch <= 'z') nextChar();
                    String func = str.substring(startPos, this.pos);
                    x = parseFactor();
                    if (func.equals("sin"))         x = Math.sin(Math.toRadians(x));
                    else if (func.equals("cos"))    x = Math.cos(Math.toRadians(x));
                    else if (func.equals("tan"))    x = Math.tan(Math.toRadians(x));
                    else if (func.equals("asin"))   x = Math.toDegrees(Math.asin(x));
                    else if (func.equals("acos"))   x = Math.toDegrees(Math.acos(x));
                    else if (func.equals("atan"))   x = Math.toDegrees(Math.atan(x));
                    else if (func.equals("sinh"))   x = Math.sinh(x);
                    else if (func.equals("cosh"))   x = Math.cosh(x);
                    else if (func.equals("tanh"))   x = Math.tanh(x);
                    else if (func.equals("log"))    x = Math.log10(x);
                    else if (func.equals("ln"))     x = Math.log(x);
                    else if (func.equals("deg"))    x = Math.toDegrees(x);
                    else if (func.equals("rad"))    x = Math.toRadians(x);

                    else throw new RuntimeException("Unknown function: " + func);
                }
                else if (eat('√')) x = Math.sqrt(parseFactor()); // square root
                else if (eat('³') && eat('√')) x = Math.cbrt(parseFactor()); // cube root
                else if (eat('\uD835') && eat('\uDC52') && eat('^')) x = Math.exp(parseFactor()); // exponent
                else throw new RuntimeException("Unexpected: " + (char) ch);

                if (eat('^')) x = Math.pow(x, parseFactor()); // power factor
                else if (eat('ʸ') && eat('√')) x = Math.round(Math.pow(parseFactor(), 1/x)); // n root
                //return Math.round(x*100)/100.0d;

                BigDecimal bd = new BigDecimal(x).setScale(14, RoundingMode.HALF_EVEN);
                return bd.doubleValue();
            }
        }.parse();
    }
}