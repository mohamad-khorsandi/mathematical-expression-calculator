package com.company;

import com.company.dateStructurs.stack;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    static Scanner sc = new Scanner(System.in);
    static String strExp;
    static Polynomial mainPolynomial;
    public static void main(String[] args) {
        while (sc.hasNextLine()) {
            strExp = sc.nextLine().replaceAll("\\s", "");
            strExp = strExp.replaceAll(Number.negativeSignRegex, "-1*");

            if (expChecker()) {
                mainPolynomial = new Polynomial(strExp);
                System.out.println(mainPolynomial.calculate());
            }
        }
    }

    static void fullPrint(){
        mainPolynomial.print();
        System.out.println();
    }

    // haft khan E rostam
    static boolean expChecker(){
        String ops = Operator.regex;
        String opsExceptMinus = ops.replaceFirst("\\\\-", "");

        if(contains(opsExceptMinus+"($|\\)|"+ops+")","operator's second operand not valid")) return false;
        if(contains("[^\\)\\d]"+opsExceptMinus, "operator's first operand not valid")) return false;

        if(contains(ops + "-|-("+ops+"|$|\\))", "wrong usage of \"-\" character")) return false;
        if(contains("/0", "division by zero")) return false;

        if(contains("\\(\\)", "empty parentheses")) return false;

        if(!parenthesesCheck()){
            System.out.println("parentheses are not correct");
            return false;
        }

        return true;
    }

    private static boolean contains(String regex, String errorMessage){
        Matcher m = Pattern.compile(regex).matcher(strExp);
        if(m.find()) {
            System.out.println("ERROR: ");
            System.out.println(errorMessage + " in char " + m.start() + " :" + m.group());
            return true;
        }
        return false;
    }

    static boolean parenthesesCheck(){
        stack<Boolean> stack = new stack<>();
        for (int i = 0; i < strExp.length(); i++) {
            if(strExp.charAt(i) == '(')
                stack.push(true);
            else if (strExp.charAt(i) == ')')
                if(stack.isEmpty())
                    return false;
                else
                    stack.pop();
        }
        return stack.isEmpty();
    }
}

class Polynomial{
    public Polynomial(String strExp) {
        // str to Polynomial
        for (int i = 0; i < strExp.length(); i++) {
            terms.add(null);
        }

        Matcher m = Pattern.compile(Function.regex).matcher(strExp);
        while (m.find()){
            String name = m.group();
            String strArg = Function.findArg(strExp, m.end());
            terms.set(m.start(), new Function(name, strArg));

            String entireFunc = name+"("+strArg+")";
            strExp = strExp.replace(entireFunc,"!".repeat(entireFunc.length()));
            m.region(m.start() + entireFunc.length() , strExp.length());
        }

        m = Pattern.compile(Number.regex).matcher(strExp);
        while (m.find()){
            terms.set(m.start(), new Number(m.group()));
        }

        // change negative signs, so they won't be taken as operator
        strExp = strExp.replaceAll(Number.negativeSignRegex, "!");

        m = Pattern.compile(Operator.regex).matcher(strExp);
        while (m.find()){
            terms.set(m.start(), Operator.fetchObject(m.group()));
        }

        for (int i = 0; i < terms.size(); i++) {
            if (terms.get(i) == null) {
                terms.remove(i);
                i--;
            }
        }
    }

    LinkedList<MathElements> terms = new LinkedList<>();

    public MathElements get(int index){
        return terms.get(index);
    }

    void replaceWithResult(Number result, int iPlus1, int i, int iMinus1){
        terms.set(iPlus1, result);
        terms.remove(i);
        terms.remove(iMinus1);
    }

    void print(){
        for (MathElements mathElements : terms)
            mathElements.print();
    }

    double calculate(){
        return new Function(this).getValue();
    }
}

interface IValuable {
    double getValue();
}

abstract class MathElements{
    abstract void print();
}

class Function extends MathElements implements IValuable{
    public Function(String name, String strArg) {
        this.name = name;
        this.argument = new Polynomial(strArg);
        this.operation = fetchOperation(this.name);
    }
    public Function(Polynomial argument) {
        this.name = "";
        this.argument = argument;
        this.operation = fetchOperation(this.name);
    }

    // define function operations
    static {
        Operation.namesRegex = new StringBuilder();
        new Operation(""){
            @Override
            double applyFunc(double argument) {
                return argument;
            }
        };
        new Operation("sin"){
            @Override
            double applyFunc(double argument) {
                return Math.sin(argument);
            }
        };
        new Operation("cos"){
            @Override
            double applyFunc(double argument) {
                return Math.cos(argument);
            }
        };
        new Operation("tan"){
            @Override
            double applyFunc(double argument) {
                return Math.tan(argument);
            }
        };
        new Operation("cot"){
            @Override
            double applyFunc(double argument) {
                return Math.cos(argument) / Math.sin(argument);
            }
        };
        new Operation("ln"){
            @Override
            double applyFunc(double argument) {
                return Math.log10(argument)/Math.log10(Math.E);
            }
        };
        new Operation("e"){
            @Override
            double applyFunc(double argument) {
                return Math.pow(Math.E, argument);
            }
        };
        new Operation("log"){
            @Override
            double applyFunc(double argument) {
                return Math.log10(argument);
            }
        };
        new Operation("abs"){
            @Override
            double applyFunc(double argument) {
                return Math.abs(argument);
            }
        };
    }

    static String regex = "("+Operation.namesRegex+")(?=\\()";
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

    @Override
    public double getValue(){
        LinkedList <Operator> sortedOps = sortOpsDec();

        while (argument.terms.size() > 1) {
            Operator highestOpr = sortedOps.removeFirst();
            int operatorIndex;
            if (highestOpr.symbol.equals("^"))
                operatorIndex = argument.terms.lastIndexOf(highestOpr);
            else
                operatorIndex = argument.terms.indexOf(highestOpr);

            IValuable operand1 = ((IValuable)(argument.get(operatorIndex - 1)));
            IValuable operand2 = ((IValuable)(argument.get(operatorIndex + 1)));

            double result = highestOpr.operation(operand1.getValue(), operand2.getValue());
            argument.replaceWithResult(new Number(result), operatorIndex + 1, operatorIndex, operatorIndex - 1);

            Main.fullPrint();
        }
        return this.operation.applyFunc(((IValuable)argument.get(0)).getValue());
    }

    private LinkedList<Operator> sortOpsDec(){
        LinkedList<Operator> ops = new LinkedList<>();
        for (int i = 0; i < argument.terms.size(); i++) {
            if (argument.get(i) instanceof Operator) {
                Operator newOp = (Operator) argument.get(i);
                ops.add(newOp);
            }
        }
        Collections.sort(ops);
        return ops;
    }

    static String findArg(String strExp, int openIndex){
        stack<Boolean> stack = new stack<>();
        int i;
        for (i = openIndex; i < strExp.length(); i++) {
            char c = strExp.charAt(i);
            if (c == '(') {
                stack.push(true);
            }
            else if (c == ')'){
                stack.pop();
            }
            if (stack.isEmpty()) break;
        }
        return strExp.substring(openIndex + 1, i);
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
        abstract double applyFunc(double argument);
    }
}

class Number extends MathElements implements IValuable{
    public Number(String strNum) {
        this.value = Double.parseDouble(strNum);
    }
    public Number(double num) {
        this.value = num;
    }
    static String negativeSignRegex = "(?<=\\(|^)-";
    static String regex = "("+negativeSignRegex+")?\\d+\\.*\\d*";
    double value;

    @Override
    void print() {
        System.out.printf("%.1f",this.value);
    }

    @Override
    public double getValue() {
        return value;
    }
}

abstract class Operator extends MathElements implements Comparable<Operator>{
    public Operator(String symbol, int priority) {
        this.symbol = symbol;
        list.add(this);
        this.priority = priority;
    }
    // define Operator in static block
    static {
        list = new ArrayList<>();
        new Operator("+", 1){
            @Override
            double operation(double a, double b) {
                return a + b;
            }
        };

        new Operator("-", 1){
            @Override
            double operation(double a, double b) {
                return a - b;
            }
        };

        new Operator("*", 2){
            @Override
            double operation(double a, double b) {
                return a * b;
            }
        };

        new Operator("/", 2){
            @Override
            double operation(double a, double b) {
                return a / b;
            }
        };

        new Operator("^", 3){
            @Override
            double operation(double a, double b) {
                return Math.pow(a, b);
            }
        };
    }

    static ArrayList<Operator> list;
    static String regex = "[+\\-*/^]";
    final String symbol;
    final int priority;

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

    @Override
    public int compareTo(Operator operator) {
        return Integer.compare(operator.priority, this.priority);
    }
}