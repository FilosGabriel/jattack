package com.softvision.jattack.manager;

import com.softvision.jattack.elements.Element;

import java.util.List;

public interface ElementManager<T extends Element> {

    void decrementLife(T element);

    boolean isAlive(T element);

    void removeElement(T element);

    void drawInvader(T element);

    void addElement(T element);

    List<T> getElements();

    void move(T element);
}
