package com.softvision.jattack.elements.invaders;

import com.softvision.jattack.GameManager;
import com.softvision.jattack.elements.Element;
import com.softvision.jattack.images.ImageType;
import com.softvision.jattack.util.Util;

import java.util.concurrent.atomic.AtomicBoolean;

public class InvaderFactory {

    public static Element generateElement(ImageType imageType, AtomicBoolean gameEnded, GameManager gameManager) {
        switch (imageType) {
            case TANK:
                return new Tank(Util.computeCoordinates(), gameEnded, gameManager);
            case PLANE:
                return new Plane(Util.computeCoordinates(), gameEnded, gameManager);
            case HELICOPTER:
                return new Helicopter(Util.computeCoordinates(), gameEnded, gameManager);
            default:
                throw new IllegalArgumentException();
        }
    }
}
