package com.evanwahrmund.appointmentscheduler.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ReportVal<T> {
    private T firstCol = null;
    private T secondCol = null;
    private T thirdCol = null;
    private ObservableList<T> list = FXCollections.observableArrayList();

    public ReportVal(T... items){

        int i = 0;
        for(T item : items){
            i += 1;
            switch(i){
                case 1: firstCol = item;
                        list.add(item);
                break;
                case 2: secondCol = item;
                        list.add(item);
                break;
                case 3: thirdCol = item;
                        list.add(item);
            }
        }
    }
    public void print(){
        for(T t: list){
            if(t != null)
                System.out.println("Contatins" + t.toString());
        }
    }
    public T getFirstCol(){
        return firstCol;
    }
    public T getSecondCol() {
        return secondCol;
    }
    public T getThirdCol(){
        return thirdCol;
    }
}
