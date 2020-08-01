package com.kardasland;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.util.Scanner;

public class Archiver {
    public static void main(String[] args) throws IOException {
        System.out.println("  _  __             _           _                    _ \n" +
                " | |/ /__ _ _ __ __| | __ _ ___| |    __ _ _ __   __| |\n" +
                " | ' // _` | '__/ _` |/ _` / __| |   / _` | '_ \\ / _` |\n" +
                " | . \\ (_| | | | (_| | (_| \\__ \\ |__| (_| | | | | (_| |\n" +
                " |_|\\_\\__,_|_|  \\__,_|\\__,_|___/_____\\__,_|_| |_|\\__,_|\n" +
                "     _             _     _                             \n" +
                "    / \\   _ __ ___| |__ (_)_   _____ _ __              \n" +
                "   / _ \\ | '__/ __| '_ \\| \\ \\ / / _ \\ '__|             \n" +
                "  / ___ \\| | | (__| | | | |\\ V /  __/ |                \n" +
                " /_/   \\_\\_|  \\___|_| |_|_| \\_/ \\___|_|                \n" +
                "Versiyon: 1.0 - KardasLand#1241                                                       ");
        System.out.println(" ");
        Scanner scan = new Scanner(System.in);
        System.out.println("Lütfen google aramasını gir.");
        String search = scan.nextLine();
        System.out.println("Filtreyi girin. Örnek: dosya.co");
        String site = scan.nextLine();
        System.out.println("Kayıt edilcek dosyanın ismini girin. Eğer yoksa direk es geçin.");
        String file = scan.nextLine();
        System.out.println("Kaç sayfa? (1 sayfa = ~10 sonuç)");
        int number = scan.nextInt();
        for (int i = 0; i < number; ++i){
            String url;
            if (!site.equals("dosya.tc")){
                url = "https://google.com/search?q="+search+" site:"+site+"&start="+(i*10); //+(number == 1 ? "" : "&start="+(number-1) * 10);
            }else{
                url = "https://google.com/search?q="+search+" dosya.tc&start="+(i*10); //+(number == 1 ? "" : "&start="+(number-1) * 10);
            }
            print((i + 1)+". sayfa taranıyor... (%s)", url);

            Document doc = Jsoup.connect(url).get();
            Elements links = doc.select("a[href]");
            Elements media = doc.select("[src]");
            Elements imports = doc.select("link[href]");

        /*
        print("\nMedia: (%d)", media.size());
        for (Element src : media) {
            if (src.normalName().equals("img"))
                print(" * %s: <%s> %sx%s (%s)",
                        src.tagName(), src.attr("abs:src"), src.attr("width"), src.attr("height"),
                        trim(src.attr("alt"), 20));
            else
                print(" * %s: <%s>", src.tagName(), src.attr("abs:src"));
        }
         */


        /*
        print("\nImports: (%d)", imports.size());
        for (Element link : imports) {
            print(" * %s <%s> (%s)", link.tagName(),link.attr("abs:href"), link.attr("rel"));
        }
         */

            print("\nFiltresiz Toplam Linkler: (%d)", links.size());
            if (!file.isEmpty()){
                File dosya = new File("./"+file+".txt");
                //FileWriter io = new FileWriter(dosya);
                FileWriter fr = new FileWriter(dosya, true);
                BufferedWriter br = new BufferedWriter(fr);
                PrintWriter pr = new PrintWriter(br);
                for (Element link : links) {
                    if (link.text().contains(site) && !link.attr("abs:href").contains("google")){
                        print(" * a: <%s>  (%s)", link.attr("abs:href"), trim(link.text(), 35));
                        //io.append(" * a: <").append(link.attr("abs:href")).append(">  (").append(trim(link.text(), 35)).append(")\n");
                        pr.println(" * a: <"+link.attr("abs:href")+">  ("+trim(link.text(), 35));
                    }
                }
                System.out.println("Dosyaya başarıyla yazıldı.");
                pr.println("_________________ "+ (i + 1) +". SAYFA SONU _________________");
                pr.close();
                br.close();
                fr.close();
            }else {
                for (Element link : links) {
                    if (link.text().contains(site) && !link.attr("abs:href").contains("google")){
                        print(" * a: <%s>  (%s)", link.attr("abs:href"), trim(link.text(), 35));
                    }
                }
            }
        }
    }
    private static void print(String msg, Object... args) {
        System.out.println(String.format(msg, args));
    }

    private static String trim(String s, int width) {
        if (s.length() > width)
            return s.substring(0, width-1) + ".";
        else
            return s;
    }
}

