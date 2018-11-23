package GUI;

import javax.crypto.*;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import java.io.*;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Utility {
    File walletFile;

    public Utility() {
        String paths = System.getProperty("user.dir");
        paths += File.separator + "Wallets";
        if (new File(paths).exists()) {
            System.out.println("Folder exist");
        } else {
            new File(paths).mkdir();
            System.out.println("Utworzono foler: Wallets");
        }
    }

    public static void main(String[] args) throws NoSuchPaddingException, InvalidKeyException, NoSuchAlgorithmException, IOException, BadPaddingException, IllegalBlockSizeException, InvalidAlgorithmParameterException, InvalidKeySpecException {
        //System.out.println(new Utility().getListOfWallets());
        System.out.println(new Utility().getListOfWallets());

        Utility a = new Utility();
        // a.createWalletFile("a","aaaaa2","bbbb","Wallet4");

        //a.cipherWalletFile("Wallet4","a");
        a.deleteWalletFile("ada.wallet");
    }

    public List<String> getListOfWallets() {

        String paths = System.getProperty("user.dir");
        paths += File.separator + "Wallets";

        File folder = new File(paths);
        System.out.println(paths);
        File[] listOfFiles = folder.listFiles();
        List<String> namesOfWallets = new ArrayList<>();

        for (File a : listOfFiles) {
            if (a.getName().endsWith(".wallet")) {
                System.out.println(a.getName());
                namesOfWallets.add(a.getName());
            }
        }
        return namesOfWallets;
    }

    public boolean fileExist(String fileName) {
        for (String a : getListOfWallets()) {
            if (a.equals(fileName)) {
                return true;
            }
        }
        return false;

    }

    public void createWalletFile(String password, String publicKey, String privateKey, String walletName) throws NoSuchPaddingException, InvalidKeyException, NoSuchAlgorithmException, IOException, BadPaddingException, IllegalBlockSizeException, InvalidAlgorithmParameterException, InvalidKeySpecException {
        //walletName += ".wallet";
        System.out.println(walletName);
        String paths = System.getProperty("user.dir");
        paths += File.separator + "Wallets";
        File folder = new File(paths);
        if (fileExist(walletName)) {
            System.out.println("File already exist:");
        } else {
            walletFile = new File(paths + File.separator + walletName);
            try {
                walletFile.createNewFile();
                System.out.println("Wallet created");

                try (FileWriter fw = new FileWriter(walletFile, true);
                     BufferedWriter bw = new BufferedWriter(fw);
                     PrintWriter out = new PrintWriter(bw)) {
                    out.println(publicKey);
                    //more code
                    out.println(privateKey);
                    //more code

                    //cipherWalletFile(walletName,password);


                    System.out.println("Zaszyfrowano");


                } catch (IOException e) {
                    //exception handling left as an exercise for the reader
                }


            } catch (Exception ex) {
                System.out.println("Can't create a file");
            }

            cipherWalletFile(walletName, password);
            walletFile.delete();
        }

    }

    public void cipherWalletFile(String walletName, String password) throws NoSuchAlgorithmException, InvalidKeySpecException, IOException, InvalidAlgorithmParameterException, InvalidKeyException, NoSuchPaddingException, BadPaddingException, IllegalBlockSizeException {
        String paths = System.getProperty("user.dir");
        paths += File.separator + "Wallets";

        FileInputStream inFile = new FileInputStream(paths + File.separator + walletName);
        FileOutputStream outFile = new FileOutputStream(paths + File.separator + walletName + ".wallet");

        PBEKeySpec pbeKeySpec = new PBEKeySpec(password.toCharArray());
        SecretKeyFactory secretKeyFactory = SecretKeyFactory
                .getInstance("PBEWithMD5AndTripleDES");
        SecretKey secretKey = secretKeyFactory.generateSecret(pbeKeySpec);

        byte[] salt = new byte[8];
        Random random = new Random();
        random.nextBytes(salt);

        PBEParameterSpec pbeParameterSpec = new PBEParameterSpec(salt, 100);
        Cipher cipher = Cipher.getInstance("PBEWithMD5AndTripleDES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, pbeParameterSpec);
        outFile.write(salt);

        byte[] input = new byte[1024];
        int bytesRead;
        while ((bytesRead = inFile.read(input)) != -1) {
            byte[] output = cipher.update(input, 0, bytesRead);
            if (output != null)
                outFile.write(output);
        }

        byte[] output = cipher.doFinal();
        if (output != null)
            outFile.write(output);

       /* File newFile = new File(paths +"/"+ walletName);
        newFile.delete();*/

        inFile.close();
        outFile.flush();
        outFile.close();
    }

    public String[] decipherWalletFile(String walletName, String password) throws NoSuchAlgorithmException, InvalidKeySpecException, IOException, InvalidAlgorithmParameterException, InvalidKeyException, NoSuchPaddingException, BadPaddingException, IllegalBlockSizeException {
        String lines[] = null;
        {

            String paths = System.getProperty("user.dir");
            paths += File.separator + "Wallets";

            PBEKeySpec pbeKeySpec = new PBEKeySpec(password.toCharArray());
            SecretKeyFactory secretKeyFactory = SecretKeyFactory
                    .getInstance("PBEWithMD5AndTripleDES");
            SecretKey secretKey = secretKeyFactory.generateSecret(pbeKeySpec);

            FileInputStream fis = new FileInputStream(paths + File.separator + walletName);
            byte[] salt = new byte[8];
            fis.read(salt);

            PBEParameterSpec pbeParameterSpec = new PBEParameterSpec(salt, 100);

            Cipher cipher = Cipher.getInstance("PBEWithMD5AndTripleDES");
            cipher.init(Cipher.DECRYPT_MODE, secretKey, pbeParameterSpec);


            //FileOutputStream fos = new FileOutputStream(paths +"/"+ walletName +".txt");

            ByteArrayOutputStream fos = new ByteArrayOutputStream();
            byte[] in = new byte[1024];
            int read;
            while ((read = fis.read(in)) != -1) {
                byte[] output = cipher.update(in, 0, read);
                if (output != null)
                    fos.write(output);
            }

            byte[] output = cipher.doFinal();
            if (output != null)
                fos.write(output);

            //System.out.println(fos.toString());

            lines = fos.toString().split("\\r?\\n");

            fis.close();
            fos.flush();
            fos.close();
        }


        return lines;
    }

    public void deleteWalletFile(String walletName) {
        String paths = System.getProperty("user.dir");
        paths += File.separator + "Wallets";
        if (fileExist(walletName)) {
            File temp = new File(paths + File.separator + walletName);
            temp.delete();
        } else {
            System.out.println("There is no: " + walletName);
        }
    }
}
