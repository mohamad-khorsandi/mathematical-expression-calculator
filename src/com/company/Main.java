package com.company;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    static Scanner sc = new Scanner(System.in);
    public static void main(String[] args) {
        String strExp = sc.nextLine().replaceAll("/s", "");
        Polynomial polynomial = new Polynomial(strExp);
        polynomial.print();
    }
}

class Polynomial{
    public Polynomial(String strExp) {
        // str to Polynomial
        for (int i = 0; i < strExp.length(); i++) {
            expression.add(null);
        }

        Matcher m = Pattern.compile(Function.regex).matcher(strExp);
        while (m.find()){
            String strFunc = m.group();
            expression.set(m.start(), new Function(strFunc));
            strExp = strExp.replace(strFunc,"!".repeat(strFunc.length()));
        }

        m = Pattern.compile(Number.regex).matcher(strExp);
        while (m.find()){
            expression.set(m.start(), new Number(m.group()));
        }

        m = Pattern.compile(Operator.regex).matcher(strExp);
        while (m.find()){
            expression.set(m.start(), Operator.fetchObject(m.group()));
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
    abstract void print();
}

class Function extends MathElements{
    public Function(String strFunc) {
        this.name = strFunc.replaceFirst("\\(.*","");
        String strArgument = strFunc.replaceFirst(this.name, "").replaceAll("^\\(|\\)$", "");
        argument = new Polynomial(strArgument);
        this.operation = fetchOperation(this.name);
    }
    // define function operations
    static {
        Operation.namesRegex = new StringBuilder();
        new Operation(""){
            @Override
            double calculate(double argument) {
                return argument;
            }
        };
        new Operation("sin"){
            @Override
            double calculate(double argument) {
                return Math.sin(argument);
            }
        };
        new Operation("cos"){
            @Override
            double calculate(double argument) {
                return Math.cos(argument);
            }
        };
        new Operation("tan"){
            @Override
            double calculate(double argument) {
                return Math.tan(argument);
            }
        };
        new Operation("cot"){
            @Override
            double calculate(double argument) {
                return Math.cos(argument) / Math.sin(argument);
            }
        };
        new Operation("ln"){
            @Override
            double calculate(double argument) {
                return Math.log10(argument)/Math.log10(Math.E);
            }
        };
        new Operation("e"){
            @Override
            double calculate(double argument) {
                return Math.pow(Math.E, argument);
            }
        };
        new Operation("log"){
            @Override
            double calculate(double argument) {
                return Math.log10(argument);
            }
        };
        new Operation("abs"){
            @Override
            double calculate(double argument) {
                return Math.abs(argument);
            }
        };
    }

    static String regex = "("+Operation.namesRegex+")\\(.*?\\)(?=[^)]*?\\(|[^)]*?$)";
    Polynomial argument;
    String name;
    Operation operation;

    private Operation fetchOperation(String name){
        for (Operation o : Operation.list)
            if (o.name.equals(name))
                return o;
        throw new RuntimeException("wrong str was identified as functions name :" + name);
    }

    @Override
    void print() {
        System.out.print(name+"(");
        this.argument.print();
        System.out.print(")");
    }

    private abstract static class Operation{
        public Operation(String name) {
            namesRegex.append("|").append(name);
            this.name = name;
            list.add(this);
        }
        String name;
        private static StringBuilder namesRegex;
        static ArrayList<Operation> list = new ArrayList<>();
        abstract double calculate(double argument);
    }
}

class Number extends MathElements {
    public Number(String strNum) {
        this.value = Double.parseDouble(strNum);
    }
    static String regex = "\\d+\\.*\\d*";
    double value;

    @Override
    void print() {
        System.out.printf("%.1f",this.value);
    }
}

abstract class Operator extends MathElements {
    public Operator(String symbol) {
        this.symbol = symbol;
        list.add(this);
    }
    // define Operator in static block
    static {
        list = new ArrayList<>();
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

    static ArrayList<Operator> list;
    static String regex = "[+\\-*/^]";
    final String symbol;

    abstract double operation(double a, double b);

    static Operator fetchObject(String symbol){
        for (Operator operator : list) {
            if (operator.symbol.equals(symbol))
                return operator;
        }
        throw new RuntimeException("wrong symbol was identified as Operator :" + symbol);
    }

    @Override
    void print() {
        System.out.print(symbol);
    }
}