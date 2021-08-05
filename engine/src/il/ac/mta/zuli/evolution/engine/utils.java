package il.ac.mta.zuli.evolution.engine;

import java.util.Random;

public class utils {

    public static int generateRandomNum(int min, int max) {
        int result;
        if(max -min < 0){
            throw  new RuntimeException("max"+max+ "is smaller than min"+min);
        }

        if (max - min == 0) {
            result = 1;
        } else {
            Random random = new Random();
            result = random.nextInt(max - min) + min;
        }

        return result;
    }
}
