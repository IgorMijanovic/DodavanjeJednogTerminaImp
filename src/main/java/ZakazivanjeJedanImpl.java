import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

public class ZakazivanjeJedanImpl extends ObradaTermina{

    private Map<String, String> premenstanjeMapa = new HashMap<>();

    /**
     *vraca true ako se datumi poklapaju, ako ne onda false
     */
    private boolean proveriDatum(Termin noviTermin, Termin postojeciTermin){
        if(noviTermin.getPocetak().getYear()==postojeciTermin.getPocetak().getYear()
                && noviTermin.getPocetak().getMonth()==postojeciTermin.getPocetak().getMonth()
                && noviTermin.getPocetak().getDayOfMonth()==postojeciTermin.getPocetak().getDayOfMonth()){
            return true;

        }
        return false;
    }

    /**
     *ako se termini poklapaju vraca true, ako se ne poklapaju onda false
     */
    private boolean proveriVreme(Termin novi, Termin postojeci){
        if((novi.getPocetak().getHour() < postojeci.getPocetak().getHour() && novi.getKraj().getHour() < postojeci.getPocetak().getHour())
        || (novi.getPocetak().getHour() > postojeci.getKraj().getHour() && novi.getKraj().getHour() > postojeci.getKraj().getHour())){
            return false;
        }
        return true;
    }

    /**
     * ako se sobe poklapaju vraca true, ako ne onda false
     */
    private boolean proveriSobe(Termin novi, Termin postojeci){
        if(novi.getProstor().getIme().equals(postojeci.getProstor().getIme())){
            return true;
        }
        return false;
    }
    private boolean proveriNeradneDane(Termin novi){
        for(LocalDateTime ldt: getNeradniDani()){
            if(ldt.getYear() == novi.getPocetak().getYear()
                    && ldt.getMonth() == novi.getPocetak().getMonth()
                    && ldt.getDayOfMonth() == novi.getPocetak().getDayOfMonth()){
                return true;
            }
        }
        return false;

    }

    private boolean proveriRadnoVreme(Termin novi){
        if((novi.getPocetak().getHour() > getPocetakRadnogVremena() && novi.getPocetak().getHour() < getKrajRadnogVremena())
                && (novi.getKraj().getHour() > getPocetakRadnogVremena() && novi.getKraj().getHour() < getKrajRadnogVremena())){
            return  true;
        }
        return false;
    }

    private boolean provera(Termin t1, Termin t2){

        if(proveriNeradneDane(t1)){
            return true;
        }

        if(!proveriRadnoVreme(t1)){
            return true;
        }

        if(proveriDatum(t1,t2)){
            System.out.println("proverava datum");
            if (proveriVreme(t1, t2)){
                System.out.println("proverava vreme");
                if(proveriSobe(t1,t2)){
                    System.out.println("proverava sobu");
                    return true;
                }else
                    return false;
            }else
                return false;
        }else
            return false;



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
//            System.out.println("dodati prazna lista");
            return true;
        }

        for(Termin z: zakazani){
            if(provera(t, z)){
                System.out.println("termin je vec zauzet");

                return false;
            }
        }

//        System.out.println("dodat kroz proveru");
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


}
