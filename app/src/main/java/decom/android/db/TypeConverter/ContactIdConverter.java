package decom.android.db.TypeConverter;

import androidx.room.TypeConverter;

import java.util.ArrayList;
import java.util.Arrays;

import decom.android.utils.Constants;

public class ContactIdConverter {

    @TypeConverter
    public static ArrayList<String> stringToArrayList(String value){
        return new ArrayList<>(Arrays.asList(value.split(Constants.DELIMITER)));
    }

    @TypeConverter
    public static String arrayListToString(ArrayList<String> strings){
        StringBuilder stringBuilder = new StringBuilder();
        for (String s: strings) {
            stringBuilder.append(s);
            stringBuilder.append(Constants.DELIMITER);
        }
        return stringBuilder.toString();
    }
}
