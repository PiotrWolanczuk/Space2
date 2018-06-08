package com.company;

import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.Node;
import java.util.ArrayList;

/**
 * Klasa trzymająca wszystkie metody i parametry pomagające sterować statkiem matką
 */
public class Boss {
    Bullet bullet = new Bullet();
    /**
     * Lista obrazów statków matek
     */
    private ArrayList<Node> bosses = new ArrayList<Node>();
    /**
     * Lista obrazów pocisków
     */
    private ArrayList<Node> enemyBullet = new ArrayList<Node>();

    /**
     * Obrazy statkó matek
     */
    Image bossImage = new Image("res/boss1.png");
    Image bossImage2 = new Image("res/boss2.png");
    Image bossImage3 = new Image("res/boss3.png");
    ImageView boss;

    /**
     * Liczba pocisków, które zniszczą statek matkę, który pierwszy się pojawi
     */
    private int bossHP = 5;
    /**
     * Wartość, która jest pomocna do ustawiania wartości bossHP, przy dodawaniu statku do gry
     */
    private int bossHP2 = bossHP;
    /**
     * Wartość punktów, które zostaną dodane do puly punktów, początkowa wartość to 0
     */
    private int points = 0;
    /**
     * Wartość "życia", które zabierze graczowi, jeśli pociski trafią w gracza
     */
    private int health = 0;

    /**
     * Wartość pomocna do poruszania statkiem
     */
    private boolean point = false;

    /**
     * Metoda dodająca statek matki do gry
     * @param width parametr szerokości, w którym pojawi się statek
     * @param height parametr wysokości, w którym pojawi się statek
     * @param bossCounter wartość pomocna do ustalania "życia" bossa
     * @return obraz statku matki
     */
    public Node boss(double width, double height, int bossCounter) {
        bossHP = bossHP2 * bossCounter;
        if(bossCounter % 5 != 0)
        {
            if(bossCounter % 2 == 0)
                boss = new ImageView(bossImage);
            else if(bossCounter % 2 == 1)
                boss = new ImageView(bossImage2);
        } else{
            boss = new ImageView(bossImage3);
        }

        boss.setLayoutX(width - boss.getBoundsInParent().getWidth());
        boss.setLayoutY(height / 2);
        bosses.add(boss);
        return boss;
    }

    /**
     * Metoda sprawdzająca czy pociski gracza trafiły statek
     * @param root grupa, w której znajduje się gra
     * @param bullets lista pocisków gracza
     * @return 5 punktów za zniszczenie statku matki
     */
    public int hit(Group root, ArrayList<Node> bullets) {
        for (int i = 0; i < bullets.size(); i++) {
            for (int j = 0; j < bosses.size(); j++) {
                try {
                    if (bullets.get(i).getBoundsInParent().intersects(bosses.get(j).getBoundsInParent())) {
                        bossHP--;
                        if (bossHP == 0) {
                            points += 5;
                            root.getChildren().remove(bosses.get(j));
                            bosses.remove(j);
                        }
                        root.getChildren().remove(bullets.get(i));
                        bullets.remove(i);
                    }
                } catch (IndexOutOfBoundsException e) {
                    //DO NOTHIG
                }
            }
        }
        return points;
    }

    /**
     * Metoda zwracająca liste obrazów statków
     * @return liste obrazów
     */
    public ArrayList<Node> getBosses() { return bosses; }

    /**
     * Metoda odpowiadająca za poruszanie statkiem
     * @param H wysokość sceny
     */
    public void bossMovement(double H) {
        for (int i = 0; i < bosses.size(); i++) {
            if (!point)
                bosses.get(i).setLayoutY(bosses.get(i).getLayoutY() - 0.5);
            else
                bosses.get(i).setLayoutY(bosses.get(i).getLayoutY() + 0.5);
            if (bosses.get(i).getLayoutY() <= H / 12)
                point = true;
            else if (bosses.get(i).getLayoutY() >= 4 * H / 6)
                point = false;
        }
    }

    /**
     * Metoda odpowiadająca za strzelanie pociskami
     * @param root grupa, w której znajduje się  gra
     */
    public void bossFire(Group root) {
        bullet.bossFire(bosses.get(0).getLayoutY(), bosses.get(0).getLayoutX(),
                bosses.get(0).getBoundsInParent().getHeight() / 4, root);
    }

    /**
     * Metoda odpowiadająca za poruszanie pociskami
     * @param root grupa, w której znajduje się  gra
     */
    public void shooting(Group root) { bullet.enemyShooting(root); }

    /**
     * Metoda odpowiadająca za sprawdzenie czy pociski statku trafiły pociski gracza
     * @param root grupa, w której znajduje się gra
     * @param bullets lista obrazów pocisków gracza
     */
    public void hitEnemyBullet(Group root, ArrayList<Node> bullets) {
        enemyBullet = bullet.getEnemyBullet();
        bullet.hit2(root, bullets, enemyBullet);
    }

    /**
     * Metoda sprawdzająca czy pociski trafiły gracza
     * @param player obraz gracza
     * @param root grupa, w której znajduje się gra
     * @return odejmuje punkt życia gry pocisk trafi gracza
     */
    public int hitPlayer(Node player, Group root) {
        for (int j = 0; j < enemyBullet.size(); j++) {
            try {
                if (player.getBoundsInParent().intersects(enemyBullet.get(j).getBoundsInParent())) {
                    root.getChildren().remove(enemyBullet.get(j));
                    enemyBullet.remove(j);
                    health--;
                }
            } catch (IndexOutOfBoundsException e) {
                //DO NOTHING
            }
        }
        return health;
    }

    /**
     * Metoda sprawdzająca czy statek trafił na bonus, jeśli tak bonus jest niszczony
     * @param bonus lista obrazów bonusów
     * @param root grupa, w której się znajduje gra
     */
    public void check(ArrayList<Node> bonus, Group root){
        for(int i =0; i<bosses.size(); i++){
            for(int j =0; j< bonus.size(); j++){
                if(bosses.get(i).getBoundsInParent().intersects(bonus.get(j).getBoundsInParent())){
                    root.getChildren().remove(bonus.get(j));
                    bonus.remove(j);
                }
            }
        }
    }
}
