package functions;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.util.Arrays;

import javax.swing.JFileChooser;

import mainGui.DashBoardPage;

public class EllipticCurveCryptography {

    static final BigInteger MERSENNE_PRIME = BigInteger.valueOf(2).pow(521).subtract(BigInteger.ONE);
    static final Integer D = -376014;


    public static void generateKeyPair(String password) {
        // s  = KMACXOF256(pw, “”, 512, “K”);
        byte[] intermediate = SHAKE.KMACXOF256(password.getBytes(), "".getBytes(), 512, "K".getBytes());
        // s  = 4s

        BigInteger s = BigInteger.valueOf(4).multiply(new BigInteger(intermediate)); // private key
        // V = s*G
        EllipticCurvePoint v = EllipticCurvePoint.selfMultiply(s, EllipticCurvePoint.getBasePoint());


        try {
            FileOutputStream fileOutputStream = new FileOutputStream("./" + "/public_key_file_password=" + password);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(v);
            objectOutputStream.close();
            DashBoardPage.textArea.setText("Private Key: " + s.toString() + 
            								"\nPublic Key X : " + v.getX().toString() +
            								"\nPublic Key Y : " + v.getY().toString());
            DashBoardPage.infoLabel.setText("Your public key file has been saved in your current project folder as: "
                     + "public_key_file_password=" + password + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException e) { 
        	DashBoardPage.textArea.setText("You did not select a directory.\n");
        }

    }

    public static void encryptFile(JFileChooser public_key_fc, JFileChooser file_fc) {
        String publicKeyFileLocation = public_key_fc.getSelectedFile().toString();
        String toEncryptFileLocation = file_fc.getSelectedFile().toString();
        byte[] encryptFileContents = new byte[0];
        FileInputStream inputStream;
        ObjectInputStream objectInputStream = null;
        try {
            encryptFileContents = Files.readAllBytes(Paths.get(toEncryptFileLocation));
            inputStream = new FileInputStream(publicKeyFileLocation);
            objectInputStream = new ObjectInputStream(inputStream);
        } catch (IOException e) { e.printStackTrace(); }
        //z = Random(512)
        SecureRandom random = new SecureRandom();
        byte[] z = new byte[64]; // 64 * 8 = 512
        random.nextBytes(z);



        //k = 4z
        BigInteger k = BigInteger.valueOf(4).multiply(new BigInteger(z));
        // W = k * V where V is the public key file
        EllipticCurvePoint V = null;
        try {
            V = (EllipticCurvePoint) objectInputStream.readObject();
        } catch (ClassNotFoundException | IOException e) { e.printStackTrace(); }
        EllipticCurvePoint W = EllipticCurvePoint.selfMultiply(k, V);


        // Z = k*G
        EllipticCurvePoint Z = EllipticCurvePoint.selfMultiply(k, EllipticCurvePoint.getBasePoint());
        //(ke || ka) = KMACXOF256(Wx, “”, 1024, “P”)
        byte[] ke_II_ka = SHAKE.KMACXOF256(W.getX().toByteArray(), "".getBytes(), 1024, "P".getBytes());
        //c = KMACXOF256(ke, “”, |m|, “PKE”) XOR m

        byte[] ke = Arrays.copyOfRange(ke_II_ka, 0, 64);

        byte[] intermediateResult = SHAKE.KMACXOF256(ke, "".getBytes(), encryptFileContents.length * 8, "PKE".getBytes());
        byte[] c = new byte[encryptFileContents.length];
        for (int i = 0; i < encryptFileContents.length; i++) {
            c[i] = (byte) (encryptFileContents[i] ^ intermediateResult[i]);
        }
        // t =  KMACXOF256(ka, m, 512, “PKA”)
        byte[] ka = Arrays.copyOfRange(ke_II_ka, 64, 128);

        byte[] t = SHAKE.KMACXOF256(ka, encryptFileContents, 512, "PKA".getBytes());

        CryptogramECC result = new CryptogramECC(Z, c, t);
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(file_fc.getSelectedFile() + ".CryptogramECC");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(result);
            oos.close();
        } catch (IOException e) { e.printStackTrace(); }
        DashBoardPage.textArea.setText("File Encrypted: " + bytesToHex(encryptFileContents) +
        								"\n\n The path to the file is: " + System.getProperty("user.dir"));

    }

    static String bytesToHex(byte[] bytes) {
        char[] hexArray = "0123456789ABCDEF".toCharArray();
        char[] hexChars = new char[bytes.length * 2];
        for ( int j = 0; j < bytes.length; j++ ) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    public static void decrypt(String password, JFileChooser fileChooser) {
        String fileLocation = fileChooser.getSelectedFile().toString();
        FileInputStream inputStream;
        ObjectInputStream objectInputStream;
        CryptogramECC recoveredCryptogram = null;
        try {
            inputStream = new FileInputStream(fileLocation);
            objectInputStream = new ObjectInputStream(inputStream);
            recoveredCryptogram = (CryptogramECC) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) { e.printStackTrace(); }

        EllipticCurvePoint Z = recoveredCryptogram.getZ();

        byte[] C = recoveredCryptogram.getC();
        byte[] T = recoveredCryptogram.getT();

        //s = KMACXOF256(pw, “”, 512, “K”);
        byte[] intermediate = SHAKE.KMACXOF256(password.getBytes(), "".getBytes(), 512, "K".getBytes());

        //s = 4s
        BigInteger s = BigInteger.valueOf(4).multiply(new BigInteger(intermediate));

        //W = s*Z
        EllipticCurvePoint W = EllipticCurvePoint.selfMultiply(s, Z);


        //(ke || ka) = KMACXOF256(Wx, “”, 1024, “P”)
        byte[] ke_II_ka = SHAKE.KMACXOF256(W.getX().toByteArray(), "".getBytes(), 1024, "P".getBytes());

        //m = KMACXOF256(ke, “”, |c|, “PKE”) XOR c
        byte[] ke = Arrays.copyOfRange(ke_II_ka, 0, 64);
        byte[] mPrime = SHAKE.KMACXOF256(ke, "".getBytes(), C.length * 8, "PKE".getBytes());
        byte[] m = new byte[C.length];
        for (int i = 0; i < m.length; i++) {
            m[i] = (byte) (mPrime[i] ^ C[i]);
        }

        //t’ = KMACXOF256(ka, m, 512, “PKA”)
        byte[] ka = Arrays.copyOfRange(ke_II_ka, 64, 128);

        byte[] tPrime = SHAKE.KMACXOF256(ka, m, 512, "PKA".getBytes());

        //accept if, and only if, t’ = t
        if (Arrays.equals(tPrime, T)) {
        	DashBoardPage.infoLabel.setText("Your " + fileLocation + " has been decrypted form a provided passphrase.");
            DashBoardPage.textArea.setText("File decrytped as: " + (bytesToHex(m)));
        } else { 
        	DashBoardPage.infoLabel.setText("Incorrect passphrase!! Please use the same passphrase while creating elliptic public key file."); 
        }
    }
}
