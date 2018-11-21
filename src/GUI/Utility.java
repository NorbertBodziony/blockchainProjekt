package GUI;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

 public class Utility {

    public List<String>  getListOfWallets()
    {

        String paths = System.getProperty("user.dir");
        paths += "/Wallets";

        File folder = new File(paths);
        System.out.println(paths);
        File[] listOfFiles = folder.listFiles();
        List<String> namesOfWallets = new ArrayList<>();

        for(File a: listOfFiles)
        {
            if(a.getName().endsWith(".wallet")) {
                System.out.println(a.getName());
                namesOfWallets.add(a.getName());
            }
        }
        return namesOfWallets;
    }



    public static void main(String[] args) {
        //System.out.println(new Utility().getListOfWallets());
        System.out.println(new Utility().getListOfWallets());
    }
}
