package com.cnam.discover.observer;

import com.cnam.discover.IPerson;

import java.util.List;
import java.util.Observable;

public class IdentifiedObserver extends Observable {
    private static IdentifiedObserver instance = new IdentifiedObserver();

    public static IdentifiedObserver getInstance() {
        return instance;
    }

    private IdentifiedObserver() {
    }

    public void updateValue(List<IPerson> personList) {
        synchronized (this) {
            setChanged();
            notifyObservers(personList);
        }
    }

}