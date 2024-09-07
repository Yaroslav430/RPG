package RPG.Game;

import RPG.Game.units.UnitAbstract;

import java.io.BufferedReader;
import java.io.IOException;

public class Seller {

    private final BufferedReader reader;
    private final UnitAbstract player;
    private final int price = 10;
    private final int addHp = 50;

    public Seller(UnitAbstract player, BufferedReader reader) {
        this.player = player;
        this.reader = reader;
    }

    public void question(World.SellerCallback sellerCallback) throws IOException {
        System.out.println("Хотите купить зелье здоровья? (да / нет)");
        String response = this.reader.readLine();
        switch (response) {
            case "да" -> {
                if (player.getGold() >= this.price) {
                    this.player.setHP(this.player.getHP() + this.addHp);
                    System.out.printf("Пополнили здоровье на %d HP, теперь - %d", this.addHp, this.player.getHP());
                } else {
                    System.out.println("Не хватает деняг =(");
                }
                sellerCallback.goOut();
            }
            case "нет" -> {
                sellerCallback.goOut();
            }
        }
    }
}