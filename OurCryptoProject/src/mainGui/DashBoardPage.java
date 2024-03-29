package mainGui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FileDialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import functions.EllipticCurveCryptography;
import functions.KMACXOF256;
import functions.KMACXOF256.DecryptionResult;
import functions.SignatureManager;

/**
 * This is the main dashboard page of this project.
 * 
 * @author rabin gora
 * @author Kezeba Yifat
 * @author Aman Gedefaw
 * @version 08/14/2020
 *
 */
public class DashBoardPage extends JFrame {
	
	private static JFrame mainFrame;
	private static JPanel mainPanel;
	
	private static JLabel titleLabel;
	private static JLabel hashLFileLabel;
	private static JButton addFileHashBtn;
	private static JLabel hashTextLabel;
	private static JTextField inputTextField;
	private static JButton textInputBtn;
	private static JLabel encryptLabel;
	private static JTextField encryptPassphraseTF;
	private static JButton encryptFileChooserButton;
	private static JLabel decryptLabel;
	private static JTextField decryptPassphraseTF;
	private static JButton decryptCryptogramChooserButton;
	private static JLabel ellipticLabel;
	private static JTextField ellipticTF;
	private static JButton ellipticPublicKeySaveButton;
	private static JLabel encryptEllipticLabel;
	private static JButton encryptEllipticButton;
	private static JLabel decryptEllipticLabel;
	private static JTextField decryptEllipticalTF;
	private static JButton decryptEllipticButton;
	private static JLabel signFileLabel;
	private static JTextField signFilePwTF;
	private static JButton singFileButton;
	private static JLabel verifyFileSignLabel;
	private static JButton verifyFileSignButton;
	
	public static JLabel resultTabLabel;
	public static JLabel infoLabel;
	public static JTextArea textArea;
	
	private static JScrollPane scrollPane;
	
	public DashBoardPage() {
		
		//main JFrame
		mainPanel = new JPanel();
		mainFrame = new JFrame();
		mainFrame.setSize(1000, 630);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setTitle("The cryptography app!");
		
		//main JPanel
		mainFrame.add(mainPanel);
		mainPanel.setLayout(null);
		mainPanel.setSize(1000, 630);
		
		//main panel title label
		titleLabel = new JLabel("Project for TCSS 487 - Rabin Gora, Aman Gedefaw, Kezeba Yifat");
		titleLabel.setBounds(10, 20, 500, 25);
		titleLabel.setForeground(Color.BLUE);
		mainPanel.add(titleLabel);
		
		//1) hash JLabel
		hashLFileLabel = new JLabel("1) Compute a plain crytographic hash of a given file.");
		hashLFileLabel.setBounds(10, 50, 380, 25);
		mainPanel.add(hashLFileLabel);
		//hash file add-button
		addFileHashBtn = new JButton("Add a file to Hash");
		addFileHashBtn.setBounds(385, 50, 150, 25);
		mainPanel.add(addFileHashBtn);
		
		//2) hash text input JLabel
		hashTextLabel = new JLabel("2) Compute a plain cryptographic hash of text input.");
		hashTextLabel.setBounds(10, 80, 380, 25);
		mainPanel.add(hashTextLabel);
		//hash text input JTextField
		inputTextField = new JTextField("Enter input text to hash");
		inputTextField.setBounds(385, 80, 415, 25);
		mainPanel.add(inputTextField);
		//button to hash text input
		textInputBtn = new JButton("Hash text input");
		textInputBtn.setBounds(800, 80, 180, 25);
		mainPanel.add(textInputBtn);
		
		//3) encrypt label
		encryptLabel = new JLabel("3) Encrypt a given data file symmetrically under a given passphrase. Type passphrase here:");
		encryptLabel.setBounds(10, 110, 575, 25);
		mainPanel.add(encryptLabel);
		//encryption passphrase jtextfield area.
		encryptPassphraseTF = new JTextField("Enter passphrase here");
		encryptPassphraseTF.setBounds(580, 110, 220, 25);
		mainPanel.add(encryptPassphraseTF);
		//button to encrypt the given data file symmetrically under a given passphrase.
		encryptFileChooserButton = new JButton("Choose file to encrypt");
		encryptFileChooserButton.setBounds(800, 110, 180, 25);
		mainPanel.add(encryptFileChooserButton);
		
		//4) decrypt passphrase Jlabel
		decryptLabel = new JLabel("4) Decrypt a given symmetric cryptogram under a given passphrase. Type passphrase here:");
		decryptLabel.setBounds(10, 140, 575, 25);
		mainPanel.add(decryptLabel);
		//decrypt JTextField area
		decryptPassphraseTF = new JTextField("Enter passphrase here");
		decryptPassphraseTF.setBounds(580, 140, 220, 25);
		mainPanel.add(decryptPassphraseTF);
		//decrypt JButton
		decryptCryptogramChooserButton = new JButton("Choose file to decrypt");
		decryptCryptogramChooserButton.setBounds(800, 140, 180, 25);
		mainPanel.add(decryptCryptogramChooserButton);
		
		//5) Elliptic JLabel
		ellipticLabel = new JLabel("5) Generate an elliptic key pair from a given passphrase and write the public key to a file.");
		ellipticLabel.setBounds(10, 170, 575, 25);
		mainPanel.add(ellipticLabel);
		//elliptic JTextField
		ellipticTF = new JTextField("Enter passphrase here");
		ellipticTF.setBounds(580, 170, 220, 25);
		mainPanel.add(ellipticTF);
		//write elliptic Key to a file button
		ellipticPublicKeySaveButton = new JButton("Save public key");
		ellipticPublicKeySaveButton.setBounds(800, 170, 180, 25);
		mainPanel.add(ellipticPublicKeySaveButton);
		
		
		//6) encrytp elliptic jlabel
		encryptEllipticLabel = new JLabel("6) Encrypt a data file under a given elliptic public key file generated on step 5.");
		encryptEllipticLabel.setBounds(10, 200, 575, 25);
		mainPanel.add(encryptEllipticLabel);
		//encrypt elliptic jbutton
		encryptEllipticButton = new JButton("Encrypt file with public key");
		encryptEllipticButton.setBounds(730, 200, 250, 25);
		mainPanel.add(encryptEllipticButton);
		
		//7) decrypt elliptic jlabel
		decryptEllipticLabel = new JLabel("7) Decrypt a given elliptic-encrypted file from a given password.");
		decryptEllipticLabel.setBounds(10, 230, 575, 25);
		mainPanel.add(decryptEllipticLabel);
		//decryptEllipticalTF JTextfield
		decryptEllipticalTF = new JTextField("Enter passphrase here");
		decryptEllipticalTF.setBounds(580, 230, 220, 25);
		mainPanel.add(decryptEllipticalTF);
		//decryptElliptic button
		decryptEllipticButton = new JButton("Decrypt elliptic file");
		decryptEllipticButton.setBounds(800, 230, 180, 25);
		mainPanel.add(decryptEllipticButton);
		
		//8) sign file from pw and write a signature to a file
		signFileLabel = new JLabel("8) Sign a fiven file from a given password and write the signature to a file.");
		signFileLabel.setBounds(10, 260, 575, 25);
		mainPanel.add(signFileLabel);
		//signFile TextField
		signFilePwTF = new JTextField("Enter password here");
		signFilePwTF.setBounds(580, 260, 220, 25);
		mainPanel.add(signFilePwTF);
		//signFile button
		singFileButton = new JButton("write signature to file");
		singFileButton.setBounds(800, 260, 180, 25);
		mainPanel.add(singFileButton);
		
		//9) verify signature Jlabel
		verifyFileSignLabel = new JLabel("9) Verify a given data file and its signature file under a given public key file from step 5 above.");
		verifyFileSignLabel.setBounds(10, 290, 750, 25);
		mainPanel.add(verifyFileSignLabel);
		//verify sign button
		verifyFileSignButton = new JButton("verify file/sing with public-key file");
		verifyFileSignButton.setBounds(730, 290, 250, 25);
		mainPanel.add(verifyFileSignButton);
		
		
		
		//result label:
		resultTabLabel = new JLabel("Result:");
		resultTabLabel.setBounds(10, 380, 200, 25);
		resultTabLabel.setForeground(Color.blue);
		mainPanel.add(resultTabLabel);
		
		//info JLable
		infoLabel = new JLabel("");
		infoLabel.setBounds(10, 410, 980, 25);//570
		mainPanel.add(infoLabel);
		
		//scroll pane. does not work for now.
//		scrollPane = new JScrollPane(textArea);
//		mainPanel.add(scrollPane, BorderLayout.CENTER);
		
		//Main JTextField to display the results. 
		textArea = new JTextArea();
		textArea.setBounds(10, 440, 980, 150);
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		textArea.setEditable(false);
		mainPanel.add(textArea);
		
		
		//set JFrame visibility at last for convenience.
		mainFrame.setVisible(true);
		
		//listener methods call for the buttons.
		addListener();
	}
	
	/**
	 * method to clear the texts
	 */
	public void clearResultLines() {
		infoLabel.setText("");
		textArea.setText("");
	}

	/**
	 * Action Listeners for the buttons.
	 */
	private void addListener() {
		
		//hash add-file button listener
		addFileHashBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				//clear everything before performing action
				clearResultLines();
				
				infoLabel.setText("choose a file to hash.");
				//then perform the actions
				FileDialog filedialog = new FileDialog(new JFrame(), "Choose a file to hash", FileDialog.LOAD);
				filedialog.setVisible(true);
				
				String filename = filedialog.getFile();
				
				if (filename == null) {
					infoLabel.setText("you did not choose any file!!!");
				} else {
					Path path = Paths.get(filedialog.getDirectory() + filedialog.getFile());
					try {
						byte[] data = Files.readAllBytes(path);
						String fileHashResult = KMACXOF256.getCryptographicHash("".getBytes(), (new String(data)).getBytes(),
								512, "D".getBytes());
						String info = "Success!! Your file \"" + filedialog.getFile() + "\" hashed to:" ;
						
						//set the info Jlabel text
						infoLabel.setText(info);
						
						//set the textArea with text
						textArea.setText(fileHashResult);
						
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}
			
		});
		
		
		//hash input text Button listener
		textInputBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				//initially set nothing
				clearResultLines();
				
				//then perform the actions
				String textToHash = inputTextField.getText().toString();
				String inputTextHashResult = KMACXOF256.getCryptographicHash("".getBytes(), textToHash.getBytes(),
						512, /*"My Tagged Application".getBytes()*/ "D".getBytes());
				String info = "your text \"" + textToHash + "\" hashed to:" ;
				
				//set the info JLabel with info
				infoLabel.setText(info);
				
				//set the textArea with result.
				textArea.setText(inputTextHashResult);
			}
		});
		
		
		//encryption add file button listener for encrypting a given file symmentrically under a given passphrase.
		encryptFileChooserButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//clear everything first
				clearResultLines();
				
				//then do the remaining process
				String passphrase = encryptPassphraseTF.getText().toString();
				infoLabel.setText("choose a file to encrypt with the provided passphrase.");
				//file loader
				FileDialog fileDialog = new FileDialog(new JFrame(), "choose a file to encrypt", FileDialog.LOAD);
				fileDialog.setVisible(true);
				if (fileDialog.getFile() ==  null) {
					infoLabel.setText("you did not choose any file to encrypt!!");
				} else {
					try {
						//KMACXOF256.encryptFile(fileDialog, passphrase);
						
						//encryption process for the file.
						String fileLocation = fileDialog.getDirectory() + fileDialog.getFile();
				        Path path = Paths.get(fileLocation);
				        byte[] message = Files.readAllBytes(path);
				        byte[] pw = (passphrase != null && passphrase.length() > 0) ? passphrase.getBytes() : new byte[0];
				        FileOutputStream outputStream = new FileOutputStream(fileLocation + ".cryptogram");
			            outputStream.write(KMACXOF256.getOutputContents(message, pw));
				        
						//set the info jlabel message
						String info = "success!! Your file \"" + fileDialog.getFile() + "\" has been encrypted to:" ;
						infoLabel.setText(info);
				        
				        //set the JtextArea.
						textArea.setText(KMACXOF256.convertBytesToHex(message) + "\n\nYou can also find the encrypted file with an"
								+ " extension .cryptogram in the same directory as the source file. \nThe file path is: \n" + fileLocation + ".cryptogram");
						
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
				
			}
			
		});
		
		/*
		 * decryption cryptogram file chooser button listener.
		 */
		decryptCryptogramChooserButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//clear the result lines first
				clearResultLines();
				
				//then do the remaining stuff.
				String passphrase = decryptPassphraseTF.getText().toString();
				infoLabel.setText("Choose a .Cryptogram file to decrypt");
				FileDialog fileDialog = new FileDialog(new JFrame(), "Choose a .Cryptogram file to decrypt", FileDialog.LOAD);
				fileDialog.setVisible(true);
				
				
				String fileName = fileDialog.getFile();
				if (fileName == null) {
					infoLabel.setText("You did not choose any Cryptogram file!!");
				} else if (!fileName.endsWith(".cryptogram")) {
					infoLabel.setText("The file you chose is not .Cryptogram file");
				} else {
					try {
						 //KMACXOF256.decryptFile(fileDialog, passphrase);
						
						//file decryption process.
						String fileLocation = fileDialog.getDirectory() + fileDialog.getFile();
				        Path path = Paths.get(fileLocation);
				        byte[] cryptogram = Files.readAllBytes(path);
				        byte[] pw = (passphrase != null && passphrase.length() > 0) ? passphrase.getBytes() : new byte[0];
				        DecryptionResult result = KMACXOF256.getMessage(cryptogram, pw);
				        
				        if (result.tPrimeEqualsT) {
				        	//set the inforLabel text
				        	String info = "success!! Your file \"" + fileDialog.getFile() + "\" has been decrypted to:" ;
							infoLabel.setText(info);
							//set the textArea text
				            textArea.setText(KMACXOF256.convertBytesToHex(result.m));
				        } else {
				            infoLabel.setText("Passphrase did not match match. please match the passphrase to perform the decryption operation!!");
				        }
						 
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}
		});

		
		/*
		 * elliptic save public key destination button listener.
		 */
		ellipticPublicKeySaveButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//clear the results lines before doing anything
				clearResultLines();
				
				//do rest of the stuff
				String passphrase = ellipticTF.getText().toString();
				EllipticCurveCryptography.generateKeyPair(passphrase);
				
				//the result texts are in EllipticCurveCryptography.generateKeyPair(passphrase);
			}
		});
		
		
		/*
		 * button for encryting file ellipctic public key
		 */
		encryptEllipticButton.addActionListener(new ActionListener () {
			@Override
			public void actionPerformed(ActionEvent e) {
				//clear result lines
				clearResultLines();
				//perform actions for the button.
				infoLabel.setText("Choose a public key file you generated in step 5");
		        JFileChooser publicKeyFileChooser = new JFileChooser(System.getProperty("user.dir"));
		        publicKeyFileChooser.setDialogTitle("Choose public key file created on step 5");
		       
		        JFileChooser fileChooser = new JFileChooser(System.getProperty("user.dir"));
		        fileChooser.setDialogTitle("Choose file to encrypt with the given public key");
		        
		        int result = publicKeyFileChooser.showOpenDialog(null);
		        
		        if (result == JFileChooser.CANCEL_OPTION) {
		            infoLabel.setText("You did not choose any public key file!!");
		        } else {
		        	infoLabel.setText("Choose a file to encrypt with the given elliptic public key file.");
		            result = fileChooser.showOpenDialog(null);
		            if (result == JFileChooser.CANCEL_OPTION) {
		                infoLabel.setText("You did not choose any file to encrypt!!");
		            } else {
		                EllipticCurveCryptography.encryptFile(publicKeyFileChooser, fileChooser);
		                infoLabel.setText("success!! Your encrypted data file under the elliptic public-key file "
		                					+ "has been saved as .CryptogramECC in your current project folder."); 
		                //textArea result text is in the called method above.
		            }
		        }
			}
		});
		
		/*
		 * decryptEllipticButton button action listener.
		 */
		decryptEllipticButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//clear result lines
				clearResultLines();
				//do stuffs with this btn.
				String passphrase = decryptEllipticalTF.getText().toString();
				infoLabel.setText("Please choose a .CryptogramECC file to decrypt");
				JFileChooser fileChooser = new JFileChooser(System.getProperty("user.dir"));
				fileChooser.setDialogTitle("choose a CryptogramECC file to decrypt");
				int result = fileChooser.showOpenDialog(null);
				if (result == JFileChooser.CANCEL_OPTION) {
					infoLabel.setText("you did not choose any CryptogramECC file!!");	
				} else {
					if (fileChooser.getSelectedFile().toString().endsWith(".CryptogramECC")) {
						EllipticCurveCryptography.decrypt(passphrase, fileChooser);
						//infoLabel and textArea's result texts are in this called method.
		            } else {
		                infoLabel.setText("You did not choose the correct file type.");
		            }
				}
			}
		});
		
		
		/*
		 * sign file button listener
		 */
		singFileButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//clear the result lines
				clearResultLines();
				
				//do rest
				String password = signFilePwTF.getText().toString();
				infoLabel.setText("Please choose a data file");
				
				JFileChooser fileChooser = new JFileChooser(System.getProperty("user.dir"));
				fileChooser.setDialogTitle("choose a data file");
				
		        int result = fileChooser.showOpenDialog(null);
		        
		        if (result == JFileChooser.CANCEL_OPTION) {
		            infoLabel.setText("You did not select any file");
		        } else {
		            SignatureManager.generateSignature(password, fileChooser);
		            //result texts are in the above called methods.
		        }
			}
		});
		
		/*
		 * verify file signature with the public-key file from step 5.
		 */
		verifyFileSignButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//clear result lines
				clearResultLines();
				//do rest
				infoLabel.setText("Select a public key file you created from part 5");
		        JFileChooser publicKeyFileChooser = new JFileChooser(System.getProperty("user.dir"));
		        publicKeyFileChooser.setDialogTitle("Select public key file.");
		        
		        JFileChooser fileChooser = new JFileChooser(System.getProperty("user.dir"));
		        JFileChooser signatureFileChooser = new JFileChooser(System.getProperty("user.dir"));

		        int result = publicKeyFileChooser.showOpenDialog(null);
		        if (result == JFileChooser.CANCEL_OPTION) {
		            infoLabel.setText("You did not select a public key file!!");
		        } else {

		        	fileChooser.setDialogTitle("Select a data file");
		        	infoLabel.setText("Select a data file.");

		            result = fileChooser.showOpenDialog(null);
		            if (result == JFileChooser.CANCEL_OPTION) {
		                infoLabel.setText("You did not select a data file!!");
		            } else {
		            	
		            	signatureFileChooser.setDialogTitle("select a signature file");
		                infoLabel.setText("Select a Signature file.");

		                result = signatureFileChooser.showOpenDialog(null);
		                if (result == JFileChooser.CANCEL_OPTION) {
		                    infoLabel.setText("You did not select a signature file!!");
		                } else {
		                    SignatureManager.verifySignature(publicKeyFileChooser, fileChooser, signatureFileChooser);
		                    //result texts are inside the called methods above.
		                }
		            }
		        }
			}
		});
		
		
	}
}
