import java.time.LocalDateTime;
import java.util.*;

public class ZakazivanjeJedanImpl extends ObradaTermina{

    private boolean provera(Termin t1, Termin t2){
        if (t1.getPocetak().getYear()==t2.getPocetak().getYear() && t1.getPocetak().getMonth()==t2.getPocetak().getMonth() && t1.getPocetak().getDayOfMonth()==t2.getPocetak().getDayOfMonth()){
            if (t1.getPocetak().getHour() >= t2.getKraj().getHour() || t1.getKraj().getHour() <= t2.getPocetak().getHour()){
                // System.out.println("Slobodan");
                return false;
            }else {
                // System.out.println("Zauzetttttttttttt");
                return true;
            }
        }else {
            //System.out.println("zauzet");
            return false;
        }

    }
    @Override
    public boolean dodajNoviTermin(String... strings) {
//        List<String> args = new ArrayList<>();
//        for(String s: strings)
//            args.add(s);
        List<String> args = new ArrayList<>(Arrays.asList(strings));

        String prostor;

        int pocetakSat;
        int krajSat;
        String datum;

        prostor = args.get(0);

        pocetakSat = Integer.parseInt(args.get(1));
        krajSat = Integer.parseInt(args.get(2));

        datum = args.get(3);
        String[] datumSplit = datum.split("\\.");
        int d, m, g;
//        System.out.println(datumSplit[0] +":" + datumSplit[1] +":" + datumSplit[2]);
        d = Integer.parseInt(datumSplit[0]);
        m = Integer.parseInt(datumSplit[1]);
        g = Integer.parseInt(datumSplit[2]);

        LocalDateTime pocetak = LocalDateTime.of(g, m, d, pocetakSat, 0);
        LocalDateTime kraj = LocalDateTime.of(g, m, d, krajSat, 0);



        Map<String, String> dodaci = new HashMap<>();
        if(args.size() > 4){

            for(int i = 4; i < args.size(); i++){
                String[] dodatak = args.get(i).split(":");
                dodaci.put(dodatak[0], dodatak[1]);
            }
        }
//        System.out.println(dodaci);

        Termin t = new Termin(null, pocetak, kraj);
        t.setDodaci(dodaci);
        t.setTipZakazivanja(PrvaDrugaImp.PRVA_IMP);
        //System.out.println(t);

        List<Termin> zakazani = getRaspored();
        if (zakazani.isEmpty()){
            getRaspored().add(t);
            System.out.println("dodati prazna lista");
            return true;
        }

        for(Termin z: zakazani){
            if(provera(t, z)){
                System.out.println("termin je vec zauzet");

                return false;
            }
        }

        System.out.println("dodat kroz proveru");
        getRaspored().add(t);
        return true;





    }

    @Override
    public boolean brisanjeTermina(String... strings) {
        List<String> args = new ArrayList<>(Arrays.asList(strings));

        String prostorB;

        int pocetakSatB;
        int krajSatB;

        String datumB;

        prostorB = args.get(0);

        pocetakSatB = Integer.parseInt(args.get(1));
        krajSatB = Integer.parseInt(args.get(2));

        datumB = args.get(3);
        String[] datumSplit = datumB.split("\\.");
        int d, m, g;
//        System.out.println(datumSplit[0] +":" + datumSplit[1] +":" + datumSplit[2]);
        d = Integer.parseInt(datumSplit[0]);
        m = Integer.parseInt(datumSplit[1]);
        g = Integer.parseInt(datumSplit[2]);
        pocetakSatB = Integer.parseInt(args.get(1));
        krajSatB = Integer.parseInt(args.get(2));

        LocalDateTime pocetak = LocalDateTime.of(g, m, d, pocetakSatB, 0);
        LocalDateTime kraj = LocalDateTime.of(g, m, d, krajSatB, 0);

        Termin t = new Termin(null, pocetak, kraj);
        t.setTipZakazivanja(PrvaDrugaImp.PRVA_IMP);

        List<Termin> zakazani = getRaspored();
        if (zakazani.isEmpty()){

            System.out.println("Termin ne postoji u rasporedu");
            return false;
        }
        int i = -1;
        for(Termin z: zakazani){
            i++;
            if(provera(t, z) ){

                System.out.println("termin za brisanje je pronadjen");

                System.out.println(i);
                break;
            }

        }

        System.out.println(i);
        getRaspored().remove(i);
        return true;



    }

    @Override
    public boolean premestanjeTermina(String... strings) {
        return false;
    }

    @Override
    public boolean dodavanjeProstorija(String... strings) {
        return false;
    }
}
