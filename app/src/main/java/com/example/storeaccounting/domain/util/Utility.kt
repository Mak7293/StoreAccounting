package com.example.storeaccounting.domain.util

import android.util.Log

object Utility {
    private val mapString: Map<String,String> = mapOf(
        "0" to "صفر","1" to "یک","2" to "دو","3" to "سه","4" to "چهار","5" to "پنج","6" to "شش","7" to "هفت","8" to "هشت","9" to "نه","10" to "ده",
        "11" to "یازده","12" to "دوازده","13" to "سیزده","14" to "چهارده","15" to "پانزده","16" to "شانزده","17" to "هفده","18" to "هجده","19" to "نوزده",
        "20" to "بیست","30" to "سی","40" to "چهل","50" to "پنجاه","60" to "شست","70" to "هفتاد","80" to "هشتاد","90" to "نود",
        "100" to "صد","200" to "دویست","300" to "سیصد","400" to "چهارصد","500" to "پانصد","600" to "ششصد","700" to "هفتصد","800" to "هشتصد","900" to "نهصد",
        "1000" to "هزار","1000000" to "میلیون","1000000_000" to "میلیارد",
    )
    fun changeNumberToString(number: Long):String{

        val map : MutableMap<Int,List<Char>> = mutableMapOf()
        val numberSize = number.toString().count()
        val stringNumber = number.toString()
        var string = ""
        var reverseNumber = mutableListOf<Char>()
        var key = 0
        var list = mutableListOf<Char>()
        if (numberSize <= 12){
            val list1 = mutableListOf<Char>()
            val list2 = mutableListOf<Char>()
            val list3 = mutableListOf<Char>()
            val list4 = mutableListOf<Char>()
            stringNumber.reversed().forEachIndexed { index, c ->
                if(index<3){
                    list1.add(0,c)
                }else if(index<6){
                    list2.add(0,c)
                }else if(index<9){
                    list3.add(0,c)
                }else if(index<12){
                    list4.add(0,c)
                }
            }
            Log.d("list1",list1.toString())
            Log.d("list2",list2.toString())
            Log.d("list3",list3.toString())
            Log.d("list4",list4.toString())
            string = listToString("میلیارد",list4)+
                    separator(list4,list3) +listToString("میلیون",list3)+
                    separator(list3,list2) +listToString("هزار",list2)+
                    separator(list2,list1) +listToString("",list1)
            Log.d("string",string)
            return string
        }else{
            return "مبلغ مورد نظر را به حروف وارد کنید."
        }
    }
    private fun separator(list: List<Char>,firstList: List<Char>): String{
        return if (list.isNotEmpty() && list != listOf('0','0','0') && firstList != listOf('0','0','0')) {
            " و "
        } else {
            ""
        }

    }
    private fun listToString(index: String, list: MutableList<Char>):String{
        var s: String = ""
        if (list.isNotEmpty()){
            if(list.size < 2){
                var c: String = ""
                c = mapString[list[0].toString()]!!
                s = "$c $index"
            }else if(list.size < 3){
                var b: String = ""
                var c: String = ""
                if (list[0] == '1'){
                    val bb:String = "${list[0]}${list[1]}"
                    b = mapString[bb]!!
                    c = mapString["0"]!!
                }else if(list[0] != '0'){
                    val bb:String = list[0]+"0"
                    b = mapString[bb]!!
                    c = mapString["0"]!!
                }else{
                    b=mapString["0"]!!
                }
                if (list[0]!='1'){
                    c = mapString[list[1].toString()]!!
                }
                if(b == mapString["0"]){
                    s = "$c $index"
                }else if(c == mapString["0"]){
                    s = "$b $index"
                }else{
                    s = "$b و $c $index"
                }
            }else if(list.size == 3){
                var a: String = ""
                var b: String = ""
                var c: String = ""
                if(list[0] != '0'){
                    val aa: String = list[0]+"00"
                    a = mapString[aa]!!
                }else{
                    a = mapString["0"]!!
                }
                if (list[1] == '1'){
                    val bb:String = "${list[1]}${list[2]}"
                     b = mapString[bb]!!
                }else if(list[1] != '0'){
                    val bb:String = list[1]+"0"
                    c = mapString["0"]!!
                    b = mapString[bb]!!
                }else{
                    b = mapString["0"]!!
                }
                if (list[1]!='1'){
                    c = mapString[list[2].toString()]!!
                }
                if(c == "0"){

                }
                if(a == mapString["0"] && b == mapString["0"] && c == mapString["0"]){
                    s=""
                }else if(a == mapString["0"] && b == mapString["0"]){
                    s = "$c $index"
                }else if(b == mapString["0"] && c == mapString["0"]){
                    s = "$a $index"
                }else if(a == mapString["0"]  && c == mapString["0"]){
                    s = "$b $index"
                }else if(a == mapString["0"]){
                    s = "$b و $c $index"
                }else if(b == mapString["0"]){
                    s = "$a و $c $index"
                }else if(c == mapString["0"]){
                    s = "$a و $b $index"
                }else{
                    s = "$a و $b و $c $index"
                }
            }
            return s
        }else{
           return s
        }
    }

}

// 000,000,000,000