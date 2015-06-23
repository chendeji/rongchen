package com.example.chendeji.rongcheng;

import android.test.AndroidTestCase;

import com.chendeji.rongchen.common.util.ChineseSpelling;
import com.chendeji.rongchen.common.util.Logger;

import junit.framework.Assert;

/**
 * Created by chendeji on 23/6/15.
 */
public class PinyinUtilTest extends AndroidTestCase {


//    public void testHanziToPinyin(){
//        String source = "长乐";
//        String pinyin = PinyinUtil.getHanYuPinYingWithOutToneNumber(source);
//        Logger.i("chendeji","pinyin:"+pinyin);
//
//        Assert.assertNotNull(pinyin);
//        Assert.assertTrue("changle".equals(pinyin));
//
//        String firstSpell = PinyinUtil.getPinyinFirstSpell(source);
//        Logger.i("chendeji","firstSpell:"+firstSpell);
//        Assert.assertNotNull(firstSpell);
//        Assert.assertTrue("cl".equals(firstSpell));
//
//    }
//
//    public void testMutiPinyin(){
//        String source = "长乐hao";
//
//        String resultFirstSpell = PinyinUtil.converterToFirstSpell(source);
//        Logger.i("chendeji", "多音字，首字母："+resultFirstSpell);
//
//        String resultSpell = PinyinUtil.converterToSpell(source);
//        Logger.i("chendeji", "多音字，全拼："+resultSpell);
//    }

    public void testChineseSpelling(){
        String source = "齐齐哈尔";
        String result = ChineseSpelling.getInstance().getSelling(source);

        Logger.i("chendeji", "不是开源框架："+result);

        String firstSpellingResult = ChineseSpelling.getInstance().getFirstSpelling(source);
        Logger.i("chendeji", "不是开源框架："+firstSpellingResult);
    }

//    public void testHanziToPinyin(){
//        String source = "齐齐哈尔";
//
//        String firstSpell = PinYin.getFirstSpell(source);
//        String spell = PinYin.getPinYin(source);
//
//        Logger.i("chendeji","firstSpell:"+firstSpell);
//        Logger.i("chendeji","spell:"+spell);
//    }

}
