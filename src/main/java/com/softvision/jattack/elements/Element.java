package com.softvision.jattack.elements;

import com.softvision.jattack.GameManager;
import com.softvision.jattack.coordinates.Coordinates;
import com.softvision.jattack.coordinates.CoordinatesCache;
import com.softvision.jattack.util.Util;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class Element implements Runnable {
    private AtomicBoolean gameEnded;
    private int life;
    private List<Coordinates> bulletsCoordinates;
    private Coordinates coordinates;
    private Coordinates previousPosition;
    private GameManager gameManager;

    public Element(Coordinates coordinates, AtomicBoolean gameEnded, GameManager gameManager) {
        this.coordinates = coordinates;
        this.gameEnded = gameEnded;
        this.life = 3;
        this.bulletsCoordinates = new ArrayList<>();
        this.gameManager = gameManager;
    }

    public void decrementLife() {
        this.life--;
    }

    public boolean isAlive() {
        return life > 0;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public void setPreviousPosition(Coordinates previousPosition) {
        this.previousPosition = previousPosition;
    }

    public abstract Image getImage();

    public abstract int getBulletHeight();

    public abstract int getBulletWidth();

    public abstract int getBulletVelocity();

    public abstract Color getBulletColor();

    public void addBulletCoordinates(Coordinates bulletCoordinates) {
        this.bulletsCoordinates.add(bulletCoordinates);
        CoordinatesCache.getInstance().getEnemyBulletsCoordinates().add(bulletCoordinates);
    }

    public List<Coordinates> getBulletsCoordinates() {
        return bulletsCoordinates;
    }

    public Coordinates getPreviousPosition() {
        return previousPosition;
    }

    public abstract void shoot();

    public void run() {
        while (!gameEnded.get() && gameManager.isAlive(this)) {
            try {
                Thread.sleep(Util.getTick());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            synchronized (Util.lockOn()) {
                if (gameManager.wasHit(this)) {
                    gameManager.decrementLife(this);
                }

                //either move or shoot
                gameManager.choseAndExecuteAction(this);
            }
        }
    }
}
