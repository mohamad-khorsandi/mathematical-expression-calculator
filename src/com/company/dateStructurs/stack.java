package com.company.dateStructurs;

import java.util.LinkedList;

public class stack<T> {
    LinkedList<T> innerList = new LinkedList<>();

    public void push(T newElement){
        innerList.add(newElement);
    }

    public T pop(){
        return innerList.remove();
    }

    public T top(){
        return innerList.element();
    }

    public boolean isEmpty(){
        return innerList.size() == 0;
    }

    public int size(){
        return innerList.size();
    }
}
