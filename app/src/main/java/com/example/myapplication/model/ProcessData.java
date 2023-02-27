package com.example.myapplication.model;
public class ProcessData {
    static String[][] word = new String[4][20];
    static String wordcontent = "";
    public static String getStr(String a, String b, String s) {
        String result = "";
        int begin, end;
        begin = s.indexOf(a);
        end = s.indexOf(b, begin + 2);
        result = s.substring(begin, end + 3);
        return result;
    }
    public static String processJanpan(String s) {
        String result = "";
        int begin, end;
        begin = s.indexOf("<div class=\"mw\">");
        end = s.indexOf("</div></div>", begin + 1);
        result = s.substring(begin, end);
        String mean= "";
        mean = processDataJF(result);
        return mean;
    }

    public static String processFrance(String s) {
        String result = "";
        int begin, end;
        begin = s.indexOf("class=\"vft\">");
        end = s.indexOf("</div></div>", begin + 1);
        result = s.substring(begin, end);
        String mean= "";
        mean = processDataJF(result);
        return mean;
    }
    public static String processDataJF(String s) {
        String mean= "";
        mean = s.replace("<div class=\"mw\">", "\n")
                .replace("<div class=\"fw\">", "\n")
                .replace("<div class=\"ex\">", "\n")
                .replace("<div class=\"tw\">", "\n")
                .replace("<div class=\"sw\">", "\n")
                .replace("<div class=\"xm\">", "\n")
                .replace("<div class=\"vf-me\">", "\n")
                .replace("<span class=\"vf-ex\">", "")
                .replace("<div class=\"pd4\">", "")
                .replace("<ul><ul><li class=\"vfl-1\">", "\n")
                .replace("class=\"vfl\">", "")
                .replace("<span class=\"fex\">", "")
                .replace("<ul><ul><ul><li ", "\n")
                .replace("<span >", "")
                .replace(" class=\"vfl-2\">", "")
                .replace("class=\"vfl-2\">", "")
                .replace("<ul><li", "\n")
                .replace("</li></ul></ul></ul>", "")
                .replace("</li></ul></ul>", "")
                .replace("</li></ul>", "")
                .replace("class=\"fp\">", "")
                .replace("</span>", "")
                .replace("</div>", "")
                .replace("</d", "").replace("class=\"vft\">", "");
        return mean;
    }



    //lấy dữ liệu từ các thẻ trong xử lý dữ liệu Anh Việt, Việt Anh
    public static String getContent(String s) {
        String strnew = "";
        while(s.contains("<td")) {
            String temp = getStr("<td", "</td", s);
            s = s.replace(temp, "");
            int begin, end,  index;
            if (temp.contains("#aaa") || temp.contains("▸")) {
                strnew = "   ";
            }
            if (temp.contains("#")) {
                index = temp.indexOf("#");
                begin = temp.indexOf('>', index);
                end = temp.indexOf("<", begin);
                strnew = strnew  + " " + temp.substring(begin + 1, end );
            } else {

                index = temp.indexOf("colspan");
                begin = temp.indexOf('>', index);
                end = temp.indexOf("</", begin);
                strnew = strnew  + " " + temp.substring(begin + 1, end);
            }
        }
        return strnew;
    }

    public static String processAV(String s) {
        s = getStr("<table id", "</table>", s);
        String temp = "";
        int i = 0,j = 0; String mean= "";
        while(s.contains("<tr")) {
            temp = getStr("<tr", "</tr", s);
            String test = temp;
            int begin = s.indexOf("</tr");
            s = s.substring(begin + 1, s.length());
            String content = getContent(temp);
            if (content == null) continue;
            if (content.contains("*")) {
                mean += content + "\n";
            }else if(content.contains("    ■")) {
            }else if(content.contains("■")) {
                mean += content + "\n";
            }
        }
        return mean;
    }

    //in ra từ loại và nghĩa (Anh Việt)
    public static String getMean(String s) {
        processAV(s);
        String mean = "";
        for (int i = 1; i < word.length - 1; i++) {
            for (int j = 0; j < word[i].length; j++) {
                if (word[i][j] == null) continue;
                mean += word[i][j];

            }
        }
        return mean;
    }

    public static String getmeanfirst(String s) {
        s = getStr("<table id", "</table>", s);
        String temp = "";
        String meanfirst = "";
        int i = 0, j = 0;
        String mean = "";
        while (s.contains("<tr")) {
            temp = getStr("<tr", "</tr", s);
            String test = temp;
            int begin = s.indexOf("</tr");
            s = s.substring(begin + 1, s.length());
            String content = getContent(temp);
            if (content == null) continue;
            if (content.contains("■")) {

                meanfirst = content;
                meanfirst = meanfirst.replace("■", "");
                break;
            }
        }
        return meanfirst;
    }

//    public static void main(String[] args) {
//        String chuoi;
//        Scanner scanner = new Scanner(System.in);
//        System.out.println("Nhập vào chuỗi bất kỳ: ");
//        chuoi = scanner.nextLine();
//        processAV(chuoi);
//        //processFrance(chuoi);
//        //processJanpan(chuoi);
//        System.out.println(wordcontent);
//    }

}
