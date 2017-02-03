package cn.com.jinke.assist.utils;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Created by apple on 2016/12/14.
 */

public class StringUtils implements CodeConstants{

    public static int getProgress(int a, int b){
        // 创建一个数值格式化对象
        int progress = 0;
        if(a != 0 && b != 0){
            NumberFormat numberFormat = NumberFormat.getInstance();
            //设置精确到小数点后面的位数
            numberFormat.setMaximumFractionDigits(0);
            String result = numberFormat.format((float) a / (float) b * 100);
            progress = Integer.valueOf(result);
        }
        return progress;
    }

    public static long getChangeTime(String aCreateTime){

        long time = 0;
        try{
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            time = sdf.parse(aCreateTime).getTime();
        }catch (ParseException e){
            e.printStackTrace();
        }
        return time;
    }
}
