package com.gt.manager;

import java.util.ArrayList;
import java.util.List;

import com.gt.observer.DataObserver;

public abstract class ObservableManager {

    private final List<DataObserver> observers =
            new ArrayList<>();

    public void addObserver(DataObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(DataObserver observer) {
        observers.remove(observer);
    }

    protected void notifyObservers() {

        for (DataObserver observer : observers) {
            observer.onDataChanged();
        }
    }
}
