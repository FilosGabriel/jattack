package com.softvision.jattack;

import com.softvision.jattack.coordinates.Coordinates;
import com.softvision.jattack.coordinates.CoordinatesCache;
import com.softvision.jattack.elements.Element;
import com.softvision.jattack.manager.ElementManager;
import com.softvision.jattack.util.Util;

import java.util.Iterator;

public class GameManager {

    private ElementManager invadersManager;

    public GameManager(ElementManager invadersManager) {
        this.invadersManager = invadersManager;
    }

    public void choseAndExecuteAction(Element element) {
        boolean shouldShoot = Util.randomBoolean();
        if (shouldShoot) {
            element.shoot();
        } else {
            invadersManager.move(element);
        }

        invadersManager.drawInvader(element);
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

        return wasHit;
    }

}
