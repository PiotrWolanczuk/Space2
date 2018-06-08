package com.company;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.ArrayList;

/**
 * Klasa trzymająca metody i parametry, które są odpowiedzialne za poruszanie pociskami i sprawdzanie czy w coś uderzyły
 */
public class Bullet {
    /**
     * Lista przechowująca obrazy pocisków gracza
     */
    private ArrayList<Node> bullets = new ArrayList<Node>();
    /**
     * Lista przechowująca obrazy pocisków wrogich statków
     */
    private ArrayList<Node> enemyBullet = new ArrayList<Node>();

    /**
     * Obraz wrogiego statku
     */
    Image bulletImage = new Image("res/bullet.png");
    ImageView bullet, bullet2;

    /**
     * Wartość o jaką przesuwają się pociski
     */
    private int speed = 4;
    /**
     * Wartość, która będzie dodana do sumy punktów gracza - początkowa to 0
     */
    public int points = 0;

    /**
     * Metoda dodająca obraz pocisku gracza do gry i listy "bullets"
     * @param Y parametr wysokości sceny, w której pocisk się pojawia
     * @param X parametr szerokości sceny, w której pocisk się pojawia
     * @param root grupa, w której jest umieszczona gra
     */
    public void addBullet(double Y, double X, Group root){
        bullet = new ImageView(bulletImage);
        bullet.setLayoutY(Y);
        bullet.setLayoutX(X);
        bullets.add(bullet);
        root.getChildren().add(bullet);
    }

    /**
     * Metoda dodająca obraz pocisku wroga do gry i listy "enemyBullet"
     * @param Y parametr wysokości sceny, w której pocisk się pojawia
     * @param X parametr szerokości sceny, w której pocisk się pojawia
     * @param root grupa, w której jest umieszczona gra
     */
    public void addEnemyBullet(double Y, double X, Group root){
        bullet = new ImageView(bulletImage);
        bullet.setLayoutY(Y);
        bullet.setLayoutX(X);
        enemyBullet.add(bullet);
        root.getChildren().add(bullet);
    }

    /**
     * Metoda odpowiadająca za poruszanie pociskami gracza
     * @param width szerokość sceny
     * @param root grupa, w której gra się znajduje
     */
    public void shooting(double width, Group root) {
        if(bullets.size() != 0){
            for (int i = 0; i < bullets.size(); i++) {
                try{
                    if (bullets.get(i).getLayoutX() < width) {
                        bullets.get(i).setLayoutX(bullets.get(i).getLayoutX() + speed);
                    } else {
                        root.getChildren().remove(bullets.get(i));
                        bullets.remove(i);
                    }
                }catch(NullPointerException e){
                    //DO NOTHIG
                }
            }
        }
    }

    /**
     * Metoda odpowiadająca za poruszanie pociskami wrogów
     * @param root grupa, w której gra się znajduje
     */
    public void enemyShooting(Group root) {
        for (int i = 0; i < enemyBullet.size(); i++) {
            try{
                if (enemyBullet.get(i).getLayoutX() > 0) {
                    enemyBullet.get(i).setLayoutX(enemyBullet.get(i).getLayoutX() - speed);
                } else {
                    root.getChildren().remove(enemyBullet.get(i));
                    enemyBullet.remove(i);
                }
            }catch(IndexOutOfBoundsException e){
                //DO NOTHIG
            }
        }
    }

    /**
     * Metoda odpowiadająca za poruszanie pociskami bossa
     * @param Y parametr wysokości, w której jest umieszczany pierwszy pocisk
     * @param X parametr szerokosci, w której jest umieszczany pierwszy pocisk
     * @param bossRadius parametr, który pomaga odsunąć miejsce od pierwszego pocisku, w którym pojawia się drui pocisk
     * @param root grupa, w której gra się znajduje
     */
    public void bossFire(double Y, double X, double bossRadius,  Group root){
        bullet = new ImageView(bulletImage);
        bullet2 = new ImageView(bulletImage);
        bullet.setLayoutY(Y);
        bullet.setLayoutX(X);
        bullet2.setLayoutY(Y);
        bullet2.setLayoutX(X);

        bullet.setLayoutY(Y + bossRadius/2);
        bullet.setLayoutX(X);
        bullet2.setLayoutY(Y + 3* bossRadius);
        bullet2.setLayoutX(X);

        enemyBullet.add(bullet);
        enemyBullet.add(bullet2);
        root.getChildren().addAll(bullet, bullet2);
    }

    /**
     * Metoda zwracająca listę pocisków gracza
     * @return listę pocisków gracza
     */
    public ArrayList<Node> getBullets() { return bullets; }

    /**
     * Metoda zwracająca listę pocisków wrogów
     * @return listę pocisków wroga
     */
    public ArrayList<Node> getEnemyBullet() { return enemyBullet; }

    /**
     * Metoda sprawdzająca czy pociski gracza trafiły we wrogów
     * @param enemies lista wrogów
     * @param root grupa, w której gra się znajduje
     * @param bullets lista pocisków gracza
     * @return 1 jeśli pocisk trafi w któregoś przeciwnika, 0 jeśli nie
     */
    public int hit(ArrayList<Node> enemies, Group root, ArrayList<Node> bullets) {
        for (int i = 0; i < bullets.size(); i++) {
            for (int j = 0; j < enemies.size(); j++) {
                try {
                    if (bullets.get(i).getBoundsInParent().intersects(enemies.get(j).getBoundsInParent())) {
                        root.getChildren().remove(enemies.get(j));
                        enemies.remove(j);
                        root.getChildren().remove(bullets.get(i));
                        bullets.remove(i);
                        points++;
                    }
                } catch (IndexOutOfBoundsException e) {
                    //DO NOTHING
                }
            }
        }
        return points;
    }

    /**
     * Metoda sprawdzająca czy pociski przeciwników i gracza trafiły się
     * @param root grupa, w której znajduje się  gra
     * @param bullets lista pocisków gracza
     * @param enemyBullet lista wrogich pocisków
     */
    public void hit2(Group root, ArrayList<Node> bullets, ArrayList<Node> enemyBullet){
        for (int j = 0; j < enemyBullet.size(); j++) {
            for (int i = 0; i < bullets.size(); i++) {
                try {
                    if (bullets.get(i).getBoundsInParent().intersects(enemyBullet.get(j).getBoundsInParent())) {
                        root.getChildren().remove(enemyBullet.get(j));
                        enemyBullet.remove(j);
                        root.getChildren().remove(bullets.get(i));
                        bullets.remove(i);
                    }
                } catch (IndexOutOfBoundsException e) {
                    //DO NOTHING
                }
            }
        }
    }
}
