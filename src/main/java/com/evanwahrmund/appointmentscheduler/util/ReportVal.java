package com.evanwahrmund.appointmentscheduler.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Utility class used to display report data in TableViews.
 * Not all columns may be used.
 * @param <T> any types
 */
public class ReportVal<T> {
    /**
     * first column for report
     */
    private T firstCol = null;
    /**
     * second column for report
     */
    private T secondCol = null;
    /**
     * third column for report
     */
    private T thirdCol = null;
    /**
     * ObservableList of all vals for report
     */
    private ObservableList<T> list = FXCollections.observableArrayList();

    /**
     * Assigns values from items list to report columns
     * @param items list of columns for report val
     */
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

    /**
     * Gets value of first col
     * @return T value of first col
     */
    public T getFirstCol(){
        return firstCol;
    }

    /**
     * Gets value of second col
     * @return T value of second col
     */
    public T getSecondCol() {
        return secondCol;
    }

    /**
     * Gets value of third col
     * @return T value of third col
     */
    public T getThirdCol(){
        return thirdCol;
    }
}
