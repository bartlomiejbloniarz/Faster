package com.bloniarz.faster.game;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Paint;
import android.util.TypedValue;

import androidx.annotation.ColorInt;

import com.bloniarz.faster.R;
import com.bloniarz.faster.game.levels.Level1;
import com.bloniarz.faster.game.levels.Level4;
import com.bloniarz.faster.game.levels.Level;
import com.bloniarz.faster.game.levels.Level2;
import com.bloniarz.faster.game.levels.Level3;
import com.bloniarz.faster.game.levels.Level5;
import com.bloniarz.faster.game.levels.Level6;
import com.bloniarz.faster.game.levels.Level7;
import com.bloniarz.faster.game.levels.Level8;
import com.bloniarz.faster.game.levels.Level9;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

public class LevelFactory {
    private final List<Item> itemsList;
    private final int startingProbability = 512;
    private final Random random = new Random();
    private Item lastItem = null;
    private int goodColor, badColor, neutralColor, playerColor, textColor;

    public LevelFactory(Context context, ISpeedFactory speedFactory){
        itemsList = new ArrayList<>();
        getColors(context);
        itemsList.add(new Item(() -> new Level1(speedFactory.getNextSpeed(), goodColor, badColor, playerColor), startingProbability));
        itemsList.add(new Item(() -> new Level2(speedFactory.getNextSpeed(), neutralColor, textColor), startingProbability));
        itemsList.add(new Item(() -> new Level3(context, speedFactory.getNextSpeed(), badColor, playerColor), startingProbability));
        itemsList.add(new Item(() -> new Level4(context, speedFactory.getNextSpeed(), badColor, playerColor), startingProbability));
        itemsList.add(new Item(() -> new Level5(speedFactory.getNextSpeed(), badColor, playerColor), startingProbability));
        itemsList.add(new Item(() -> new Level6(context, speedFactory.getNextSpeed(), neutralColor, playerColor), startingProbability));
        itemsList.add(new Item(() -> new Level7(speedFactory.getNextSpeed(), goodColor, neutralColor, textColor), startingProbability));
        itemsList.add(new Item(() -> new Level8(speedFactory.getNextSpeed(), badColor), startingProbability));
        itemsList.add(new Item(() -> new Level9(speedFactory.getNextSpeed(), goodColor, neutralColor), startingProbability));
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
                if (lastItem != null)
                    itemsList.add(lastItem);
                lastItem = item;
                break;
            }
        }
        itemsList.remove(lastItem);
        return lastItem.getLevel.get();
    }

    static class Item{
        public Supplier<Level> getLevel;
        public int relativeProbability;

        public Item(Supplier<Level> getLevel, int relativeProbability){
            this.getLevel = getLevel;
            this.relativeProbability = relativeProbability;
        }
    }

    private void getColors(Context context){
        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = context.getTheme();
        theme.resolveAttribute(R.attr.colorPrimary, typedValue, true);
        playerColor = typedValue.data;

        theme.resolveAttribute(R.attr.colorOnBackground, typedValue, true);
        textColor = typedValue.data;

        goodColor = context.getColor(R.color.game_good_color);
        badColor = context.getColor(R.color.game_bad_color);
        neutralColor = context.getColor(R.color.game_neutral_color);
    }
}
