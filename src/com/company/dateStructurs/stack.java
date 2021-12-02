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

    public boolean isEmpty(){
        return innerList.size() == 0;
    }
}
