package com.company;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.ArrayList;

/**
 * Klasa trzymająca metody i parametry, które pomagają graczowi w interakcji z grą
 */
public class Player {

    /**
     * Obraz gracza
     */
    Image playerImage = new Image("res/playerImage.png");
    ImageView player;

    /**
     * Liczba pocisków, które zniszczą gracza (nie licząc bonusów)
     */
    private int health = 3;
    /**
     * Wartość pomocna w momencie uderzenia w statek matke -  bossa
     */
    private int end;
    /**
     *  Wartość, z jaką przesuwamy gracza
     */
    private int playerSpeed = 3;

    /**
     *  Metoda dodająca gracza do gry
     * @return obraz gracza
     */
    public Node addPlayer(){
        player = new ImageView(playerImage);
        player.setLayoutX(0);
        player.setLayoutY(90);
        return player;
    }

    /**
     * Metoda zwracająca gracza
     * @return obraz gracza
     */
    public Node getPlayer() { return player; }

    /**
     * Metoda zwracająca liczbę pocisków, które zniszczą gracza
     * @return liczbę pocisków, które zniszczą gracza
     */
    public int getHealth() { return health; }

    /**
     * Metoda zwracająca wartość, z jaką gracz się przesuwa
     * @return wartość, z jaką gracz się przesuwa
     */
    public int getPlayerSpeed() { return playerSpeed; }

    /**
     * Metoda poruszająca graczem
     * @param x to parametr szerokości sceny, w który gracz się przesuwa
     * @param y to parametr wysokości sceny, w który gracz się przesuwa
     * @param width to wysokość sceny
     * @param height to szerokość sceny
     * @param player to obraz gracza
     */
    public void movePlayer(double x, double y, double width, double height, Node player){
        if(x >= 0 && x < (width - player.getBoundsInParent().getWidth()) && y >= 0 &&
                y <= (height - player.getBoundsInParent().getHeight())) {
            player.setLayoutX(x);
            player.setLayoutY(y);
        }
    }

    /**
     * Metoda sprawdzająca czy gracz nie trafił na wrogi statek - niszczy wrogi statek, lub na statek matka - gracz
     * jest niszczony i koniec gry
     * @param boss lista obrazów statku matki
     * @param enemies lista obrazów wrogich statów
     * @param root grupa, w której jest umieszczona gra
     * @return wartość, która kończy grę
     */
    public int check(ArrayList<Node> boss, ArrayList<Node> enemies, Group root){
        for(int i =0; i < enemies.size(); i++){
            try {
                if (enemies.get(i).getBoundsInParent().intersects(player.getBoundsInParent())) {
                    root.getChildren().remove(enemies.get(i));
                    enemies.remove(i);
                }
            } catch (IndexOutOfBoundsException e) {
                //DO NOTHING
            }
        }
        try {
            if (boss.get(0).getBoundsInParent().intersects(player.getBoundsInParent())) {
                root.getChildren().remove(player);
                end = 999999;
            }
        } catch (IndexOutOfBoundsException e) {
            //DO NOTHING
        }
        return -end;
    }
}
