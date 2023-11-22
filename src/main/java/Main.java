public class Main {
    public static void main(String[] args) {
        ZakazivanjeJedanImpl z = new ZakazivanjeJedanImpl();
        z.initRaspored();
        z.dodajNoviTermin("s1", "13","15","01.10.2023", "Prof:aleksa", "Tip:predavanje");
        z.dodajNoviTermin("s1", "10","12","01.10.2023", "Prof:igor");
        z.dodajNoviTermin("s1", "13","15","03.10.2023", "Prof:kkkkkk");
        z.dodajNoviTermin("s1", "11","14","03.10.2023", "Prof:pp");
        z.dodajNoviTermin("s1", "11","14","31.12.2023", "Prof:pp");
        z.dodajNoviTermin("s2", "7","10","31.12.2023", "Prof:pp");

//        z.brisanjeTermina("s1", "13","15","03.10.2023");
        z.premestanjeTermina("s1", "13","15","01.10.2023", "05.10.2023");
//        z.pretragaTermina("1.10.2023");
        z.pretragaTermina("s1");
//        z.pretragaTermina("1.10.2023","s1");
//        z.pretragaTermina("1.10.2023","17","20");
//        z.pretragaTermina("t", "Prof:aleksa");
//        z.pretragaTermina("p", "racunari:da");



        for (Termin termin: z.getRaspored()){
            System.out.println(termin);
        }
    }
}
