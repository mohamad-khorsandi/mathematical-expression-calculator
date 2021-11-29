package com.company;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    static Scanner sc = new Scanner(System.in);
    public static void main(String[] args) {
        Operator.define();
        Parenthesis.define();
        StringBuilder strExp = new StringBuilder(sc.nextLine().replaceAll("/s", ""));
        Polynomial polynomial = new Polynomial(strExp);
        polynomial.print();
    }
}

class Polynomial{
    public Polynomial(StringBuilder strExp) {
        // str to Polynomial
        for (int i = 0; i < strExp.length(); i++) {
            expression.add(null);
        }

        Matcher m = Pattern.compile(Number.regex).matcher(strExp);
        while (m.find()){
            expression.set(m.start(), new Number(m.group()));
        }

        m = Pattern.compile(Operator.regex).matcher(strExp);
        while (m.find()){
            expression.set(m.start(), Operator.fetchObject(m.group()));
        }

        m = Pattern.compile(Parenthesis.regex).matcher(strExp);
        while (m.find()){
            expression.set(m.start(), Parenthesis.fetchObject(m.group()));
        }

        for (int i = 0; i < expression.size(); i++) {
            if (expression.get(i) == null) {
                expression.remove(i);
                i--;
            }
        }
    }

    LinkedList<MathElements> expression = new LinkedList<>();

    void print(){
        for (MathElements mathElements : expression)
            mathElements.print();
    }
}


abstract class MathElements{
    public MathElements(String stringForm) {
        this.stringForm = stringForm;
    }
    String stringForm;

    void print() {
        System.out.print(this.stringForm);
    }
}

class Number extends MathElements {
    public Number(String stringForm) {
        super(stringForm);
        this.value = Double.parseDouble(stringForm);
    }
    static String regex = "\\d+\\.*\\d*";
    double value;

    @Override
    void print() {
        System.out.printf("%.2f",this.value);
    }
}

abstract class Operator extends MathElements {
    public Operator(String stringForm) {
        super(stringForm);
        list.add(this);
    }

    static ArrayList<Operator> list = new ArrayList<>();
    static String regex = "\\+|-|\\*|\\/|\\^";

    static Operator fetchObject(String symbol){
        for (Operator operator : list) {
            if (operator.stringForm.equals(symbol))
                return operator;
        }
        throw new RuntimeException("wrong symbol was identified as Operator :" + symbol);
    }

    static void define(){
        new Operator("+"){
            @Override
            double operation(double a, double b) {
                return a + b;
            }
        };

        new Operator("-"){
            @Override
            double operation(double a, double b) {
                return a - b;
            }
        };

        new Operator("*"){
            @Override
            double operation(double a, double b) {
                return a * b;
            }
        };

        new Operator("/"){
            @Override
            double operation(double a, double b) {
                return a / b;
            }
        };

        new Operator("^"){
            @Override
            double operation(double a, double b) {
                return Math.pow(a, b);
            }
        };
    }
    
    abstract double operation(double a, double b);
}

class Parenthesis extends MathElements{
    public Parenthesis(String stringForm) {
        super(stringForm);
    }
    boolean isOpen;

    static String regex = "\\(|\\)";

    final static Parenthesis open = new Parenthesis("(");
    final static Parenthesis close = new Parenthesis(")");

    static void define() {
        open.isOpen = true;
        close.isOpen = false;
    }

    static Parenthesis fetchObject(String symbol){
        if (symbol.equals("("))
            return open;
        else
            return close;
    }
}