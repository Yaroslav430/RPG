package RPG.Game;

import RPG.Game.units.Player;
import RPG.Game.units.UnitAbstract;
import RPG.Game.units.monsters.Goblin;
import RPG.Game.units.monsters.Orc;

public class Battle {


    public void fight(UnitAbstract player, World.FightCallback fightCallback) {
        UnitAbstract monster = createMonster();
        System.out.printf("Вы встертили первого врага - ", monster.getName());
        Runnable fightThread = () -> {
            int hitCounter = 1;
            boolean victoryOrLose = false;
            while (!victoryOrLose) {
                if (hitCounter++ % 2 != 0) {
                    victoryOrLose = makeHit(monster, player, fightCallback);
                } else {
                    victoryOrLose = makeHit(player, monster, fightCallback);
                }
            }
        };
        Thread thread = new Thread(fightThread);
        thread.start();
    }


    private Boolean makeHit(UnitAbstract firstUnit, UnitAbstract secondUnit, World.FightCallback fightCallback) {

        int lostHealth = firstUnit.getHP() - secondUnit.attack();

        if (secondUnit.attack() != 0) {
            System.out.printf("%s Нанес удар в %d единиц!%n", secondUnit.getName(), secondUnit.attack());
            System.out.printf("У %s осталось %d единиц здоровья...%n", firstUnit.getName(), lostHealth);
        }

        if (lostHealth <= 0 && firstUnit instanceof Player) {
            System.out.println("Это был славный бой, но вы проиграли...");
            fightCallback.lose();

            return true;
        } else if(lostHealth <= 0) {

            System.out.printf("Враг повержен! Вы получаете %d опыт и нашли немного золотых - %d %n", firstUnit.getXp(), firstUnit.getGold());
            secondUnit.setXp(secondUnit.getXp() + firstUnit.getXp());
            secondUnit.setGold(secondUnit.getGold() + firstUnit.getGold());

            fightCallback.win();

            return true;
        } else {
            firstUnit.setHP(lostHealth);

            return false;
        }
    }

    /**
     * Создаем рандомного монстра (простым делением берем шанс выпадения персонажей)
     *
     * @return UnitAbstract
     */
    private UnitAbstract createMonster() {
        if ((int)(Math.random() * 10) % 2 == 0)  {
            return Goblin.createGoblin();
        } else {
            return Orc.createOrc();
        }
    }
}
