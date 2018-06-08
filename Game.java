package com.company;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.ArrayList;

/**
 * Główna klasa gry
 */
public class Game extends Application {
    Boss boss = new Boss();
    Player dude = new Player();
    Bullet bullet = new Bullet();
    Enemy enemy = new Enemy();
    Bonus bonus = new Bonus();

    /**
     * Listy obrazów pocisków
     */
    private ArrayList<Node> bullets = new ArrayList<Node>();
    /**
     * Listy obrazów wrogów
     */
    private ArrayList<Node> enemies = new ArrayList<Node>();
    /**
     * Listy obrazów statków matek
     */
    private ArrayList<Node> bosses = new ArrayList<Node>();
    /**
     * Listy obrazów bonusów życia
     */
    private ArrayList<Node> perks = new ArrayList<Node>();
    /**
     * Listy obrazów bonusów punktowych
     */
    private ArrayList<Node> pointPerks = new ArrayList<Node>();

    /**
     * Tekst punktów
     */
    Text score;
    /**
     * Tekst życia gracza
     */
    Text healthP;
    /**
     * Teskt pojawiający się na końcu gry
     */
    Text end;

    /**
     * Wartość punktów
     */
    private int points = 0;
    /**
     * Wartość życia gracza
     */
    private int health = dude.getHealth();
    /**
     * Wartość pomagająca ustalić wyświetlające się życie
     */
    private int health2 = health;
    /**
     * Wartość miejsca tekstowego
     */
    private int textSpace = 30;

    /**
     * Licznik bossów
     */
    private int bossCounter = 0;
    /**
     * Liczba początkowa wrogów
     */
    private int quantity = 5;
    /**
     * Wartość pomagająca przy ustaleniu liczby wrogów w kolejnych falach
     */
    private int quantity2 = quantity;
    /**
     * Wartości o jakie przesuwamy gracza
     */
    private int moveX = 0, moveY = 0;

    /**
     * Wartość resetująca czas przy dodawaniu przeciwników
     */
    private int time = 20;
    /**
     * Wartość przy jakiej strzelają przeciwnicy
     */
    private int enemyTime = 200;
    /**
     * Wartość pomagająca przy dodawaniu wrogów
     */
    private int delay = time;
    /**
     * Wartość pomagająca przy dodawaniu bonusów
     */
    private int delayBonus = 10*time;

    /**
     * Wartość opóźniająca strzelanie statku matki
     */
    private int delayShooting = 30;

    /**
     * Flaga odpowiadająca za pozwolenie na strzelanie gracza
     */
    private boolean canShoot = true;
    /**
     * Flaga odpowiadająca za pozwolenie na  strzelanie przeciwników
     */
    private boolean enemyCan = false;
    /**
     * Flaga odpowiadająca za pozwolenie dodawania wrogów
     */
    private boolean bossIsDead = true;
    /**
     * Flaga odpowiadająca za poruszanie gracza w górę
     */
    private boolean moveup;
    /**
     * Flaga odpowiadająca za poruszanie gracza w dol
     */
    private boolean movedown;
    /**
     * Flaga odpowiadająca za poruszanie gracza w lewo
     */
    private boolean moveleft;
    /**
     * Flaga odpowiadająca za poruszanie gracza w prawo
     */
    private boolean moveright;

    /**
     * Wysokość sceny
     */
    private int H;
    /**
     * Szerokość sceny
     */
    private int W;
    /**
     * Grupa w której znajduje się gra
     */
    private Group root;

    /**
     * Obraz gracza
     */
    private Node player;

    /*** THE GAME ***/
    /**
     * Metoda odpowiadająca za grę - pętla gry, wszystkie metody są wywołane tutaj
     * @param primaryStage arena, w której  znajduje się scena
     */
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Space Impact");
        root = new Group();
        Scene scene = new Scene(root, 610, 250);

        W = (int)scene.getWidth();
        H = (int)scene.getHeight();

        score = new Text(100, 10, "Points: " + points);
        healthP = new Text(30, 10, "Lives: " + health);
        end = new Text(scene.getWidth() / 4, scene.getHeight() / 2, "GAME OVER!");

        dude.addPlayer();
        player = dude.getPlayer();

        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                switch (event.getCode()) {
                    case SPACE:
                        if (canShoot) {
                            bullet.addBullet(player.getLayoutY() + player.getBoundsInParent().getHeight() / 2,
                                    player.getLayoutX() + player.getBoundsInParent().getWidth(),  root);
                            canShoot = false;
                        } break;
                    case DOWN:
                        movedown = true; break;
                    case UP:
                        moveup = true; break;
                    case LEFT:
                        moveleft = true; break;
                    case RIGHT:
                        moveright = true; break;
                }
            }
        });

        scene.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                switch (event.getCode()) {
                    case SPACE:
                        canShoot = true; break;
                    case UP:
                        moveup = false; break;
                    case DOWN:
                        movedown = false; break;
                    case LEFT:
                        moveleft = false; break;
                    case RIGHT:
                        moveright = false; break;
                }
            }
        });

        root.getChildren().addAll(player, score, healthP);
        primaryStage.setScene(scene);
        primaryStage.show();

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                moveX = 0;
                moveY = 0;

                bosses = boss.getBosses();
                enemies = enemy.getEnemies();
                pointPerks = bonus.getPointsPerks();
                perks = bonus.getPerks();

                /*PLAYER*/
                if(moveup)
                    moveY -= dude.getPlayerSpeed();
                if(movedown)
                    moveY += dude.getPlayerSpeed();
                if(moveleft)
                    moveX -= dude.getPlayerSpeed();
                if(moveright)
                    moveX += dude.getPlayerSpeed();

                dude.movePlayer(player.getLayoutX() + moveX, player.getLayoutY() + moveY,
                        scene.getWidth(), scene.getHeight(), player);

                /*ENEMIES*/
                if (quantity > 0) {
                    delay--;
                    if (delay == 0 && bosses.size() == 0) {
                        try {
                            root.getChildren().add(enemy.addEnemy(W, H, textSpace));
                        }catch(IllegalArgumentException e){
                            //DO NOTHIG
                        }
                        quantity--;
                        delay = time;
                    }
                } else {
                    if (enemies.size() == 0) {
                        bossCounter++;
                        quantity = bossCounter * quantity2;
                        root.getChildren().add(boss.boss(scene.getWidth(), scene.getHeight(),  bossCounter));
                    }
                    bossIsDead = false;
                }

                if (bosses.size() == 0 && !bossIsDead) {
                    delay = time;
                    bossIsDead = true;
                }

                /*BONUS*/
                delayBonus--;
                if (delayBonus == 0) {
                    if(perks.size() <= 0)
                        bonus.addHPBonus(W, H, textSpace, root);
                    delayBonus = 25*time;
                }
                else if(delayBonus == 15*time && pointPerks.size() <= 0){
                    bonus.addPoint(W,H, textSpace, root);
                }

                /*MOVEMENT*/
                for (int i = 0; i < enemies.size(); i++) {
                    enemyCan = enemy.movement(scene.getHeight(), textSpace, enemyTime, root, i);
                }
                boss.bossMovement(scene.getHeight());

                /*SHOOTING*/
                bullet.shooting(scene.getWidth(), root);
                enemy.shooting(root);
                boss.shooting(root);

                if (bosses.size() != 0) {
                    delayShooting--;
                    if (delayShooting == 0) {
                        boss.bossFire(root);
                        delayShooting = time;
                    }
                }
                if (enemyCan)
                    enemyCan = enemy.fire(root);

                /* HITS */
                bullets = bullet.getBullets();

                points = enemy.hitBullet(root, bullets) + boss.hit(root, bullets)
                        + bonus.checkPoint(player, root);
                health = health2 + enemy.hitPlayer(player, root) + boss.hitPlayer(player, root)
                        + bonus.checkPlayer(player, root) + dude.check(bosses,enemies, root);

                enemy.hitEnemyBullet(root, bullets);
                boss.hitEnemyBullet(root, bullets);
                enemy.hitFriendlyBullet(root);
                enemy.check(perks, root);
                enemy.check(pointPerks, root);
                boss.check(perks, root);
                boss.check(pointPerks, root);

                score.setText("Points: " + points);
                healthP.setText("Lives: " + health);

                /*THE END*/
                if (health <= 0) {
                    health = 0;
                    healthP.setText("Lives: " + health);

                    end.setFill(Color.RED);
                    end.setFont(Font.font(30));
                    end.setText("GAME OVER! " + " Points: " + points);
                    root.getChildren().add(end);
                    this.stop();
                }
            }
        };
        timer.start();
    }
}
