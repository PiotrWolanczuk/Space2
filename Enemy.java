package com.company;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.ArrayList;
import java.util.Random;

/**
 * Klasa trzymająca metody i parametry pomagające sterować wrogami
 */
public class Enemy {
    Bullet bullet = new Bullet();
    Random random = new Random();

    /**
     * Obraz wroga
     */
    Image enemyImage = new Image("res/enemy.png");
    ImageView enemy;

    /**
     * Lista obrazów wrogów
     */
    private ArrayList<Node> enemies = new ArrayList<Node>();
    /**
     * Lista obrazów wrogich pocisków
     */
    private ArrayList<Node> enemyBullet = new ArrayList<Node>();
    /**
     * Lista flag pomagających przy poruszaniu przeciwnikami
     */
    private ArrayList<Boolean> movement = new ArrayList<Boolean>();

    /**
     * Wartość z jaką przesuwany jest przeciwnik
     */
    private int enemySpeed = 2;
    /**
     * Wartość jaką odejmuje się od życia gracza
     */
    public int health = 0;
    /**
     * Wartości pomagające przy poruszaniu przeciwnikami
     */
    private double up;
    private double down;

    /**
     * Flaga, która pozwala przeciwnikowi strzelać
     */
    private boolean enemyCan = false;

    /**
     * Metoda dodająca obraz przeciwnika
     * @param width szerokość sceny
     * @param height wysokość sceny
     * @param textSpace miejsce tekstowe
     * @return obraz przeciwnika
     */
    public Node addEnemy(int width, int height, int textSpace){
        enemy = new ImageView(enemyImage);
        enemy.relocate(width, random.nextInt((height - textSpace) + textSpace + 1));
        movement.add(true);
        enemies.add(enemy);
        return enemy;
    }

    /**
     * Metoda odpowiadająca za poruszanie przeciwnikami
     * @param H wysokość sceny
     * @param textSpace miejsce tekstowe
     * @param enemyTime flaga, czy przeciwnik może strzelać
     * @param root grupa, w której znajduje się gra
     * @param i identyfikator wroga w liście
     * @return flagę odpowiadjącą za pozwolenie na strzelanie
     */
    public boolean movement(double H, int textSpace, int enemyTime, Group root, int i) {
        down = 99*H/100 - enemy.getBoundsInParent().getHeight();
        up = textSpace + 10;
        if (enemies.get(i).getLayoutX() != 0) {
            enemies.get(i).setLayoutX(enemies.get(i).getLayoutX() - enemySpeed);
            if (enemies.get(i).getLayoutY() - enemySpeed > up && movement.get(i)) {
                enemies.get(i).setLayoutY(enemies.get(i).getLayoutY() - enemySpeed);
                if (enemies.get(i).getLayoutY() - enemySpeed <= up) movement.set(i, false);
            } else if (enemies.get(i).getLayoutY() + enemySpeed < down && !movement.get(i)) {
                enemies.get(i).setLayoutY(enemies.get(i).getLayoutY() + enemySpeed);
                if (enemies.get(i).getLayoutY() + enemySpeed >= down) movement.set(i, true);
            }
        } else {
            root.getChildren().remove(enemies.get(i));
            enemies.remove(i);
            movement.remove(i);
        }
        try {
            if (enemies.get(0).getLayoutX() % enemyTime == 0)
                enemyCan = true;
        } catch (IndexOutOfBoundsException e) {
            //DO NOTHING
        }
        return enemyCan;
    }

    /**
     * Metoda odpowiadająca za strzelanie przeciwnikó
     * @param root grupa, w której znajduje się gra
     * @return zmienia wartość flagi, odpowiadajacej za strzelanie przeciwników
     */
    public boolean fire(Group root) {
        for (int i = 0; i < enemies.size(); i++) {
            bullet.addEnemyBullet(enemies.get(i).getLayoutY(),
                    enemies.get(i).getLayoutX() - enemies.get(i).getBoundsInParent().getWidth(), root);
        }
        enemyCan = false;
        return false;
    }

    /**
     * Metoda odpowiadająca za poruszanie wrogimi pociskami
     * @param root grupa, w której znajduje się gra
     */
    public void shooting(Group root) { bullet.enemyShooting(root); }

    /**
     *  Metoda odpowiadająca za sprawdzenie czy pociski gracza zniszczyly wroga
     * @param root grupa, w której znajduje się gra
     * @param bullets lista obrazów pocisków gracza
     * @return zwraca punkt jeśli zniszczyly
     */
    public int hitBullet(Group root, ArrayList<Node> bullets) { return bullet.hit(enemies, root, bullets); }

    /**
     * Metoda odpowiadająca za sprawdzenie czy pociski się zderzyły
     * @param root grupa, w której znajduje się gra
     * @param bullets lista obrazów pocisków gracza
     */
    public void hitEnemyBullet(Group root, ArrayList<Node> bullets) {
        enemyBullet = bullet.getEnemyBullet();
        bullet.hit2(root, bullets, enemyBullet);
    }

    /**
     * Metoda odpowiadająca za sprawdzenie czy wrogie pociski zniszczyły wroga
     * @param root grupa, w której znajduje się gra
     */
    public void hitFriendlyBullet(Group root){
        enemyBullet = bullet.getEnemyBullet();
        bullet.hit2(root, enemyBullet, enemies);
    }

    /**
     * Metoda sprawdzająca czy pociski wroga trafiły gracza
     * @param player obraz gracza
     * @param root grupa, w której znajduje się gra
     * @return zmniejsza życie
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
     * Metoda sprawdzająca czy przeciwnik trafił na bonus, jeśli tak to niszczy bonus
     * @param bonus lista obrazów bonusów
     * @param root grupa, w której jest gra
     */
    public void check(ArrayList<Node> bonus, Group root){
        for(int i =0; i<enemies.size(); i++){
            for(int j =0; j< bonus.size(); j++){
                if(enemies.get(i).getBoundsInParent().intersects(bonus.get(j).getBoundsInParent())){
                    root.getChildren().remove(bonus.get(j));
                    bonus.remove(j);
                }
            }
        }
    }

    /**
     * Metoda zwracająca listę obrazów wrogów
     * @return lstę obrazów  wrogów
     */
    public ArrayList<Node> getEnemies() { return enemies; }
}
