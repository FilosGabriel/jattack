package com.softvision.jattack.elements.defender;

import com.softvision.jattack.coordinates.Coordinates;
import com.softvision.jattack.coordinates.CoordinatesCache;
import com.softvision.jattack.coordinates.FixedCoordinates;
import com.softvision.jattack.images.ImageLoader;
import com.softvision.jattack.images.ImageType;
import com.softvision.jattack.util.Constants;
import com.softvision.jattack.util.Util;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class Defender implements Runnable {

    private final Image image = ImageLoader.getImage(ImageType.DEFENDER);
    private AtomicBoolean gameEnded;
    private int life;
    private List<Coordinates> bulletsCoordinates;
    private Coordinates coordinates;
    private EventHandler<KeyEvent> eventHandler;
    private GraphicsContext graphicsContext;

    public Defender(Coordinates coordinates, AtomicBoolean gameEnded, GraphicsContext graphicsContext) {
        this.coordinates = coordinates;
        this.gameEnded = gameEnded;
        this.life = 5;
        this.bulletsCoordinates = new ArrayList<>();
        this.eventHandler = new DefenderEventHandler(this);
        this.graphicsContext = graphicsContext;
        this.draw();
    }

    public void shoot() {
        //the x coordinate of the bullet is computed based on the width of the image for the defender and also the bullet width
        Coordinates bulletCoordinates = new FixedCoordinates(getCoordinates().getX() + 45, getCoordinates().getY() - 30);
        bulletsCoordinates.add(bulletCoordinates);
        CoordinatesCache.getInstance().getDefenderBulletsCoordinates().add(bulletCoordinates);
    }

    public boolean wasHit() {
        boolean wasHit = false;

        Iterator<Coordinates> invaderBulletsCoordinatesIterator = CoordinatesCache.getInstance().getEnemyBulletsCoordinates().iterator();
        while (invaderBulletsCoordinatesIterator.hasNext()) {
            Coordinates bulletCoordinates = invaderBulletsCoordinatesIterator.next();
            int bulletX = bulletCoordinates.getX();
            int bulletY = bulletCoordinates.getY();
            if (this.getCoordinates().getX() <= bulletX && bulletX <= this.getCoordinates().getX() + 100) {
                if (this.getCoordinates().getY() - 10 <= bulletY) {
                    invaderBulletsCoordinatesIterator.remove();
                    wasHit = true;
                    break;
                }
            }
        }

        return wasHit;
    }

    public void move(KeyCode keyCode) {
        graphicsContext.clearRect(coordinates.getX(), coordinates.getY(), image.getWidth(), image.getHeight());
        if (keyCode.equals(KeyCode.LEFT) && this.getCoordinates().getX() - 10 > 0) {
            this.getCoordinates().setX(this.getCoordinates().getX() - 10);
        } else if (keyCode.equals(KeyCode.RIGHT) && this.getCoordinates().getX() + 10 < Constants.WIDTH - 100) {
            this.getCoordinates().setX(this.getCoordinates().getX() + 10);
        }
        draw();
    }

    public Image getImage() {
        return image;
    }

    public EventHandler<KeyEvent> getEventHandler() {
        return eventHandler;
    }

    public List<Coordinates> getBulletsCoordinates() {
        return bulletsCoordinates;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void decrementLife() {
        this.life--;
    }

    public boolean isAlive() {
        return life > 0;
    }

    @Override
    public void run() {
        while (!gameEnded.get()) {
            try {
                Thread.sleep(Util.getTick());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            synchronized (Util.lockOn()) {
                if (this.wasHit()) {
                    this.decrementLife();
                    if (!this.isAlive()) {
                        gameEnded.set(true);
                    }
                }
            }
        }
    }

    public void draw() {
        graphicsContext.drawImage(this.getImage(),
                this.getCoordinates().getX(),
                this.getCoordinates().getY(),
                this.getImage().getWidth(),
                this.getImage().getHeight());
    }
}
