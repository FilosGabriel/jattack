package com.softvision.jattack.manager;

import com.softvision.jattack.coordinates.Coordinates;
import com.softvision.jattack.coordinates.CoordinatesCache;
import com.softvision.jattack.elements.Element;
import com.softvision.jattack.elements.defender.Defender;
import com.softvision.jattack.util.Util;

import java.util.Iterator;
import java.util.concurrent.atomic.AtomicBoolean;

public class GameManager {

    private ElementManager invadersManager;
    private AtomicBoolean gameEnded;

    public GameManager(ElementManager invadersManager) {
        this.invadersManager = invadersManager;
        this.gameEnded = new AtomicBoolean(false);
    }

    public void choseAndExecuteAction(Element element) {
        boolean shouldShoot = Util.randomBoolean();
        if (shouldShoot) {
            element.shoot();
        } else {
            invadersManager.move(element);
        }

        invadersManager.drawElement(element);
    }

    public boolean isAlive(Element element) {
        if (!invadersManager.isAlive(element)) {
            invadersManager.removeElement(element);
        }

        return invadersManager.isAlive(element);
    }

    public void decrementLife(Element element) {
        invadersManager.decrementLife(element);
        if(!isAlive(element)) {
            invadersManager.removeElement(element);
        }
    }

    public boolean wasHit(Element element) {
        boolean wasHit = false;

        if(element instanceof Defender) {
            Iterator<Coordinates> invaderBulletsCoordinatesIterator = CoordinatesCache.getInstance().getEnemyBulletsCoordinates().iterator();
            while (invaderBulletsCoordinatesIterator.hasNext()) {
                Coordinates bulletCoordinates = invaderBulletsCoordinatesIterator.next();
                int bulletX = bulletCoordinates.getX();
                int bulletY = bulletCoordinates.getY();
                if (element.getCoordinates().getX() <= bulletX && bulletX <= element.getCoordinates().getX() + 100) {
                    if (element.getCoordinates().getY() - 10 <= bulletY) {
                        invaderBulletsCoordinatesIterator.remove();
                        wasHit = true;
                        break;
                    }
                }
            }
        } else {
            Iterator<Coordinates> defenderBulletCoordinatesIterator = CoordinatesCache.getInstance().getDefenderBulletsCoordinates().iterator();
            while (defenderBulletCoordinatesIterator.hasNext()) {
                Coordinates bulletCoordinates = defenderBulletCoordinatesIterator.next();
                int bulletX = bulletCoordinates.getX();
                int bulletY = bulletCoordinates.getY();
                if (element.getCoordinates().getY() < 0) {
                    defenderBulletCoordinatesIterator.remove();
                } else if (element.getCoordinates().getX() <= bulletX && bulletX <= element.getCoordinates().getX() + 110) {
                    if (element.getCoordinates().getY() + 100 >= bulletY) {
                        defenderBulletCoordinatesIterator.remove();
                        wasHit = true;
                        break;
                    }
                }
            }
        }
        return wasHit;
    }

    public void draw(Element element) {
        invadersManager.drawElement(element);
    }

    public boolean gameEnded() {
        return gameEnded.get();
    }

    public void setGameEnded(boolean gameEnded) {
        this.gameEnded.set(gameEnded);
    }
}
