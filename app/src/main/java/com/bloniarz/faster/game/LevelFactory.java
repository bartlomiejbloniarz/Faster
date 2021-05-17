package com.bloniarz.faster.game;

import android.content.Context;

import com.bloniarz.faster.R;
import com.bloniarz.faster.game.levels.Level1;
import com.bloniarz.faster.game.levels.Level4;
import com.bloniarz.faster.game.levels.Level;
import com.bloniarz.faster.game.levels.Level2;
import com.bloniarz.faster.game.levels.Level3;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

public class LevelFactory {
    private final List<Item> itemsList;
    private final int startingProbability = 512;
    private final Random random = new Random();

    public LevelFactory(Context context, ISpeedFactory speedFactory){
        itemsList = new ArrayList<>();
        itemsList.add(new Item(() -> new Level1(speedFactory.getNextSpeed(), context.getColor(R.color.game_good_color), context.getColor(R.color.game_bad_color)), startingProbability));
        itemsList.add(new Item(() -> new Level2(speedFactory.getNextSpeed(), context.getColor(R.color.game_neutral_color)), startingProbability));
        itemsList.add(new Item(() -> new Level3(context, speedFactory.getNextSpeed(), context.getColor(R.color.game_bad_color)), startingProbability));
        itemsList.add(new Item(() -> new Level4(context, speedFactory.getNextSpeed(), context.getColor(R.color.game_bad_color)), startingProbability));
    }

    public Level getNextLevel(){
        int denominator = 0;
        for (Item item: itemsList)
            denominator += item.relativeProbability;
        int rand = random.nextInt(denominator)+1, sum = 0;
        for (Item item: itemsList){
            sum += item.relativeProbability;
            if (sum >= rand){
                item.relativeProbability/=2;
                if (item.relativeProbability == 1){
                    for (Item i: itemsList)
                        i.relativeProbability*=2;
                }
                return item.getLevel.get();
            }
        }
        return itemsList.get(random.nextInt()).getLevel.get();
    }

    static class Item{
        public Supplier<Level> getLevel;
        public int relativeProbability;

        public Item(Supplier<Level> getLevel, int relativeProbability){
            this.getLevel = getLevel;
            this.relativeProbability = relativeProbability;
        }
    }
}
