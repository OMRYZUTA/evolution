package il.ac.mta.zuli.evolution.engine;

import java.util.Random;

public class utils {

    public static int generateRandomNum(int min, int max) {
        int result;
        if(max -min < 0){
            throw new RuntimeException("Cannot generate random number since max " + max + " is smaller than min " + min);
        }

        if (max - min == 0) {
            result = 1;
        } else {
            Random random = new Random();
            //+1 to include out bound max in randomization
            result = random.nextInt(max+1 - min) + min;
        }

        return result;
    }

    public  static int generateRandomNumZeroBase(int num){
        return new Random().nextInt(num);
    }
}
