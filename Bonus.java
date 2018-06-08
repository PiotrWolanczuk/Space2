package com.company;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.ArrayList;
import java.util.Random;

/**
 * Klasa odpowiadająca za metody i parametry, które są pomocne przy bonusach
 */
public class Bonus {
    /**
     * Lista obrazów bonusów życia
     */
    ArrayList<Node> perks = new ArrayList<Node>();
    /**
     * Lista obrazów bonusów punktowych
     */
    ArrayList<Node> pointsPerks = new ArrayList<Node>();

    /**
     * Obraz bonusu życia
     */
    Image hpImage = new Image("res/hp.png");
    ImageView hpBonus;

    /**
     * Obraz bonusu punktowego
     */
    Image pointImage = new Image("res/point.png");
    ImageView pointBonus;

    /**
     * Wartość, która zostanie dodana do życia gracza
     */
    private int health = 0;
    /**
     * Wartość, która zostanie dodana do punktów gracza
     */
    private int point = 0;

    Random random = new Random();

    /**
     * Metoda odpowiadająca za dodanie bonusu życia do gry
     * @param width szerokość sceny
     * @param height wysokość sceny
     * @param textSpace miejsce tekstowe
     * @param root grupa, w której znajduje się gra
     */
    public void addHPBonus(int width, int height, int textSpace, Group root){
        hpBonus = new ImageView(hpImage);
        hpBonus.setLayoutX(random.nextInt(width - (int)(2*hpBonus.getBoundsInParent().getWidth())) +
                (int)(hpBonus.getBoundsInParent().getWidth()));
        hpBonus.setLayoutY(random.nextInt(height - textSpace) + textSpace);
        perks.add(hpBonus);
        root.getChildren().add(hpBonus);
    }

    /**
     * Metoda odpowiadająca za dodanie bonusu punktowego do gry
     * @param width szerokość sceny
     * @param height wysokość sceny
     * @param textSpace miejsce tekstowe
     * @param root grupa, w której gra jest umieszczona
     */
    public void addPoint(int width, int height, int textSpace, Group root){
        pointBonus = new ImageView(pointImage);
        pointBonus.setLayoutX(random.nextInt(width - (int)(2*pointBonus.getBoundsInParent().getWidth())) +
                (int)(pointBonus.getBoundsInParent().getWidth()));
        pointBonus.setLayoutY(random.nextInt(height - textSpace) + textSpace);
        pointsPerks.add(pointBonus);
        root.getChildren().add(pointBonus);
    }

    /**
     * Metoda sprawdzająca czy gracz zdobył bonus życia
     * @param player obraz gracza
     * @param root grupa, w której znajduje się gra
     * @return zwraca punkt życia, jeśli gracz zdobędzie bonus
     */
    public int checkPlayer(Node player, Group root){
        for (int j = 0; j < perks.size(); j++) {
            try {
                if (player.getBoundsInParent().intersects(perks.get(j).getBoundsInParent())) {
                    root.getChildren().remove(perks.get(j));
                    perks.remove(j);
                    health++;
                }
            } catch (IndexOutOfBoundsException e) {
                //DO NOTHING
            }
        }
        return health;
    }

    /**
     * Metoda sprawdzająca czy gracz zdobył bonus punktowy
     * @param player obraz gracza
     * @param root grupa, w której znajduje się gra
     * @return zwraca punkt, jeśli gracz zdobędzie bonus
     */
    public int checkPoint(Node player, Group root){
        for(int i = 0; i < pointsPerks.size(); i++){
            try{
                if(player.getBoundsInParent().intersects(pointsPerks.get(i).getBoundsInParent())){
                    root.getChildren().remove(pointsPerks.get(i));
                    pointsPerks.remove(i);
                    point++;
                }
            } catch(IndexOutOfBoundsException e){
                //DO NOTHING
            }
        }
        return point;
    }

    /**
     * Metoda zwracająca listę obrazów bunusów życia
     * @return listę obrazów bunusów życia
     */
    public ArrayList<Node> getPerks() { return perks; }

    /**
     * Metoda zwracająca listę obrazów bunusów punktowych
     * @return listę obrazów bunusów punktowych
     */
    public ArrayList<Node> getPointsPerks() { return pointsPerks; }
}
