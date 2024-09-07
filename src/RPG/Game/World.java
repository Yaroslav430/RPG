package RPG.Game;

import RPG.Game.units.Player;
import RPG.Game.units.UnitAbstract;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class World {

    private BufferedReader reader;
    private UnitAbstract player = null;
    private Battle battle = null;
    private Menu menu = null;
    private boolean onFight = false;

    public World() {
        System.out.println("Загружаем мир...");
        this.startGame();
    }


    public void startGame() {
        reader = new BufferedReader(new InputStreamReader(System.in));
        battle = new Battle();
        menu = new Menu();
        System.out.println("Добро пожаловать в наш волшебный мир! Как мы можем к Вам обращаться?");
        try {
            this.commands(reader.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void commands(String string) throws IOException {
        if (player == null) {
            player = new Player(
                    string,
                    100,
                    20,
                    20,
                    0
            );
            System.out.printf("Рады знакомству %s! Готов? Поехали. ", player.getName());

            menu.printMainMenu();
        } else {
            switch (string) {
                case "1" -> {
                    if (!onFight) {
                        startShopping(player);
                        commands(reader.readLine());
                    } else {
                        System.out.println("Мы на поле боя, идем дальше бороться с нечестью? ( да / нет )");
                        commands(reader.readLine());
                    }
                }
                case "2" -> {
                    if (!onFight) {
                        startFight();
                    } else {
                        System.out.println("Мы на поле боя, идем дальше бороться с нечестью? (да/нет)");
                        commands(reader.readLine());
                    }
                }
                case "3" -> {
                    if (!onFight) {
                        System.exit(1);
                    } else {
                        System.out.println("Мы на поле боя, идем дальше бороться с нечестью? ( да / нет )");
                        commands(reader.readLine());
                    }
                }
                case "да" -> {
                    if (onFight) {
                        commands("2");
                    } else {
                        System.out.println("Эм, не совсем понятно...");
                        menu.printMainMenu();
                        commands(reader.readLine());
                    }
                }
                case "нет" -> {
                    if (onFight) {
                        onFight = false;
                    } else {
                        System.out.println("Эм, не совсем понятно...");
                    }
                    menu.printMainMenu();
                    commands(reader.readLine());
                }
            }
        }

        commands(reader.readLine());
    }

    private void startShopping(UnitAbstract player) throws IOException {
        Seller seller = new Seller(player, reader);
        seller.question(() -> {
            try {
                menu.printMainMenu();
                commands(reader.readLine());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void startFight() {
        onFight = true;
        battle.fight(player, new FightCallback() {
            @Override
            public void win() {
                System.out.printf("Вы победили! %n %d опыта %n %d золота %n %d едениц здоровья.%n", player.getXp(), player.getGold(), player.getHP());
                player.LvlUp();
                System.out.println("Идем дальше бороться с нечестью? (да/нет)");
                try {
                    commands(reader.readLine());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void lose() {
                System.out.println(" | Новая игра | ");
                startGame();
            }
        });
    }


    interface FightCallback {
        void win();
        void lose();
    }


    interface SellerCallback {
        void goOut();
    }
}
