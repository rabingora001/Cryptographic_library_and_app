package functions;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

import javax.swing.JFileChooser;

import mainGui.DashBoardPage;

public class SignatureManager {

    public static void generateSignature(String password, JFileChooser fileChooser) {

        String fileLocation = fileChooser.getSelectedFile().toString();
        byte[] m = null;
        try {
            m =  Files.readAllBytes(Paths.get(fileLocation));
        } catch (IOException e) {
            e.printStackTrace();
        }

        byte[] intermediate = SHAKE.KMACXOF256(password.getBytes(), "".getBytes(), 512, "K".getBytes());
        BigInteger s = BigInteger.valueOf(4).multiply(new BigInteger(intermediate));

        byte[] k_intermediate = SHAKE.KMACXOF256(s.toByteArray(), m, 512, "N".getBytes());
        BigInteger k = BigInteger.valueOf(4).multiply(new BigInteger(k_intermediate));

        EllipticCurvePoint U = EllipticCurvePoint.selfMultiply(k, EllipticCurvePoint.getBasePoint());
        byte[] h = SHAKE.KMACXOF256(U.getX().toByteArray(), m, 512, "T".getBytes());

        //𝑟 = 2519 − 337554763258501705789107630418782636071\904961214051226618635150085779108655765.
        //337554763258501705789107630418782636071904961214051226618635150085779108655765

        BigInteger r = new BigInteger("2");
        r = r.pow(519);
        r = r.subtract(new BigInteger("337554763258501705789107630418782636071904961214051226618635150085779108655765"));

        BigInteger z = (k.subtract(new BigInteger(h).multiply(s))).mod(r);
        //(k - (hs)) hs


        Signature result = new Signature(h, z);

        FileOutputStream fos;
        try {
            fos = new FileOutputStream(fileChooser.getSelectedFile() + ".Signature");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(result);
            oos.close();
            DashBoardPage.infoLabel.setText("Success!! A .Signature file has been created with the password for the selected data file.");
            DashBoardPage.textArea.setText("The path to the file is "  + System.getProperty("user.dir"));
        } catch (IOException e) {
            e.printStackTrace();
            DashBoardPage.infoLabel.setText("An error occured!!");
        } 
    }

    public static void verifySignature(JFileChooser public_key_fc, JFileChooser file_fc, JFileChooser sig_fc) {

        /*

        Verifying a signature σ = (h, z) for a given byte array m under the (Schnorr/ECDHIES) public key V:
        ▪ Uz*G+h*V
        ▪ accept if, and only if, KMACXOF256(Ux, m, 512, “T”) = h
         */

        String publicKeyFileLocation = public_key_fc.getSelectedFile().toString();
        String dataFileLocation = file_fc.getSelectedFile().toString();
        String signatureFileLocation = sig_fc.getSelectedFile().toString();

        EllipticCurvePoint recoveredCryptogram = null;
        Signature recoveredSignature = null;
        byte[] m = new byte[0];
        FileInputStream inputStream;
        ObjectInputStream objectInputStream = null;
        try {
            inputStream = new FileInputStream(publicKeyFileLocation);
            objectInputStream = new ObjectInputStream(inputStream);
            recoveredCryptogram = (EllipticCurvePoint) objectInputStream.readObject();
            inputStream = new FileInputStream(signatureFileLocation);
            objectInputStream = new ObjectInputStream(inputStream);
            recoveredSignature = (Signature) objectInputStream.readObject();
            m = Files.readAllBytes(Paths.get(dataFileLocation));
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        /*
        U = z*G+h*V
        ▪ accept if, and only if, KMACXOF256(Ux, m, 512, “T”) = h*/

        EllipticCurvePoint U =
                EllipticCurvePoint.selfMultiply(recoveredSignature.getZ(), EllipticCurvePoint.getBasePoint())
                        .summation(EllipticCurvePoint.selfMultiply(new BigInteger(recoveredSignature.getH()),
                                recoveredCryptogram));

        byte[] test = SHAKE.KMACXOF256(U.getX().toByteArray(), m, 512, "T".getBytes());
        if (Arrays.equals(test, recoveredSignature.getH())) {
            DashBoardPage.infoLabel.setText("Signature is legitimate.");
        } else {
        	DashBoardPage.infoLabel.setText("signature is not legitimate!!");
        }
    }
}