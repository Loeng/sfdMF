package com.ekfans.pub.util;

import java.text.DecimalFormat;

public class FormatDigitalUtil {

    private FormatDigitalUtil(){}
    
    public static String priceFormat(Double number){
        return new DecimalFormat("##0.00").format(number);
    }
    
    public static String priceFormat(Object obj){
        return new DecimalFormat("##0.00").format(obj);
    }
    
    public static String formatPriceObject(Object obj){
        return new DecimalFormat("##0.00").format(obj);
    }
}
