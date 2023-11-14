import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

public class ZakazivanjeJedanImpl extends ObradaTermina{

    private Map<String, String> premenstanjeMapa = new HashMap<>();

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

    private Termin napraviTermon(String soba, String vremePocetak, String vremeKraj, String datum){

        int i = -1;
        boolean flag = false;
        for(Prostor p : getProstori()){
            i++;
            if(p.getIme().equals(soba)){
                flag = true;
                break;
            }

        }
        Prostor prostor;
        if (flag) {
           prostor = getProstori().get(i);
        }else {
            System.out.println("prostor ne postoji");
            return null;
        }
        int pocetakSat;
        int krajSat;



        pocetakSat = Integer.parseInt(vremePocetak);
        krajSat = Integer.parseInt(vremeKraj);


        String[] datumSplit = datum.split("\\.");
        int d, m, g;
//        System.out.println(datumSplit[0] +":" + datumSplit[1] +":" + datumSplit[2]);
        d = Integer.parseInt(datumSplit[0]);
        m = Integer.parseInt(datumSplit[1]);
        g = Integer.parseInt(datumSplit[2]);

        LocalDateTime pocetak = LocalDateTime.of(g, m, d, pocetakSat, 0);
        LocalDateTime kraj = LocalDateTime.of(g, m, d, krajSat, 0);
        Termin t = new Termin(prostor, pocetak, kraj);
        return t;
    }
    @Override
    public boolean dodajNoviTermin(String... strings) {

        List<String> args = new ArrayList<>(Arrays.asList(strings));
        Termin t;
        if(!args.get(0).equals("P")) {

            Map<String, String> dodaci = new HashMap<>();
            if (args.size() > 4) {

                for (int i = 4; i < args.size(); i++) {
                    String[] dodatak = args.get(i).split(":");
                    dodaci.put(dodatak[0], dodatak[1]);
                }
            }


            t = napraviTermon(args.get(0), args.get(1), args.get(2), args.get(3));
            if(t == null){
                System.out.println("termin nije zakazan jer soba ne postoji");
                return false;
            }
            t.setDodaci(dodaci);
            t.setTipZakazivanja(PrvaDrugaImp.PRVA_IMP);
        }else{
            t = napraviTermon(args.get(1), args.get(2), args.get(3), args.get(4));
            t.setDodaci(premenstanjeMapa);
            t.setTipZakazivanja(PrvaDrugaImp.PRVA_IMP);
        }
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

        Termin t;
        if(!args.get(0).equals("P")) {
            t = napraviTermon(args.get(0), args.get(1), args.get(2), args.get(3));
            t.setTipZakazivanja(PrvaDrugaImp.PRVA_IMP);
        }
        else {
            t = napraviTermon(args.get(1), args.get(2), args.get(3), args.get(4));
            t.setTipZakazivanja(PrvaDrugaImp.PRVA_IMP);
        }



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
                if(args.get(0).equals("P"))
                    premenstanjeMapa = z.getDodaci();
//                System.out.println(i);
                break;
            }

        }

//        System.out.println(i);
        getRaspored().remove(i);
        return true;



    }

    @Override
    public boolean premestanjeTermina(String... strings) {
        List<String> args = new ArrayList<>(Arrays.asList(strings));


        brisanjeTermina("P",args.get(0),args.get(1),args.get(2), args.get(3));
//        System.out.println(premenstanjeMapa);
        dodajNoviTermin("P",args.get(0), args.get(1), args.get(2), args.get(4));

        return false;
    }

    @Override
    public boolean dodavanjeProstorija(String... strings) {
        List<String> args = new ArrayList<>(Arrays.asList(strings));

        String putanjaMetaData = args.get(0);
        try {
            FileReader fr = new FileReader(putanjaMetaData);
            BufferedReader br = new BufferedReader(fr);

            String line;
            int flag= 0;
            while ((line = br.readLine()) != null){

//                System.out.println(line);
                if(line.equals("PROSTOR") || line.equals("RADNOVREME") || line.equals("NERADNIDANI")){
                    if(line.equals("PROSTOR"))
                        flag = 1;

                    if(line.equals("RADNOVREME"))
                        flag = 2;

                    if(line.equals("NERADNIDANI"))
                        flag = 3;

                    continue;
                }
                if (flag == 1) {
                    String[] delovi = line.split("-");

                    String imeSobe = delovi[0];
//                    System.out.println(imeSobe);
                    int brojMesta = Integer.parseInt(delovi[1]);
//                    System.out.println(brojMesta);

                    Prostor prostor = new Prostor(imeSobe, brojMesta);
                    for(int i = 2; i< delovi.length; i++){
                        String[] dodatak = delovi[i].split(":");
                        prostor.getDodaci().put(dodatak[0], dodatak[1]);
                    }
//                    System.out.println(prostor);
                    getProstori().add(prostor);
                }
                if(flag == 2){
                    String[] delovi1 = line.split("-");
//                    System.out.println(delovi1[0] + "-" + delovi1[1]);
                    setPocetakRadnogVremena(Integer.parseInt(delovi1[0]));
                    setKrajRadnogVremena(Integer.parseInt(delovi1[1]));
                }
                if(flag == 3){
//                    System.out.println(line);
                    getNeradniDani().add(line);
                }

            }


        } catch (Exception e) {
            throw new RuntimeException(e);
        }

//        for (Prostor p : getProstori()){
//            System.out.println(p);
//
//        }
//        System.out.println("neradni: " + getNeradniDani());
        return false;
    }
}
