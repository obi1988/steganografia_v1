package pl.mat.umk.steganografia;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.shape.Path;
import javafx.stage.FileChooser;
import pl.mat.umk.steganografia.files.Bitmap;

public class PictureAction {

	private PictureData data = new PictureData();
	private byte [] byteText;

	public void setByteText(byte[] byteText) {
		this.byteText = byteText;
		isZaladowanyText = true;
	}

	private File file;
	private Bitmap bmp = new Bitmap();
	boolean isZaladowanyObraz = false;
	boolean isZaladowanyText = false;

	public void setFile(File file) {
		this.file = file;
		isZaladowanyObraz = true;
	}
	
	public String getFile(boolean isOpen){
		
		FileChooser fchoose = new FileChooser();
		File f ;
		String result = "";
		fchoose.setTitle("Wybierz plik");
		fchoose.setInitialDirectory(new File(System.getProperty("user.home")+"/Desktop/"));
		
		if(isOpen){
			fchoose.getExtensionFilters().addAll(
	                new FileChooser.ExtensionFilter("All Images", "*.*"),
	                new FileChooser.ExtensionFilter("JPG", "*.jpg"),
	                new FileChooser.ExtensionFilter("PNG", "*.png")
	            );	
			f = fchoose.showOpenDialog(null);
		}else{
			f = fchoose.showSaveDialog(null);
		}
		
		if(f != null){
			result = f.getAbsolutePath();
		}
		
		
		return result;
	}
	
	public void close(ActionEvent actionEvent) {
		
		Platform.exit();
		/*Node source = (Node) actionEvent.getSource();
		  Stage stage = (Stage) source.getScene().getWindow();
		  stage.close();
		*/
	}
	
	public InputStream start(ProgressBar progBar, ProgressIndicator progIndi){
		InputStream im = null;
		try {
			progBar.setProgress(1);
			progIndi.setProgress(1);
//			byte[] b = data.extractBytesImage(this.file);
//			byte[] b = data.extractBytesImage2(this.file);
			
			byte []b = Files.readAllBytes(Paths.get(this.file.getAbsolutePath()));
			byte []c = data.textToBinary(b);
			
			byte[] dataBIT = data.saveText(b, new byte[] {0,0,0,1,0,0,1});
			progBar.setProgress(2);
			progIndi.setProgress(2);
			
			
			im = data.toImage(c,byteText);
			data.setBitmap(bmp, c);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return im;
	}
	
	
	/**
	 * Metoda pozwala odszyfrować zaszytą w bajtach obrazu informację 
	 * @param progBar
	 * @param progIndi
	 * @return
	 */
	public InputStream odszyfruj(ProgressBar progBar, ProgressIndicator progIndi){
		InputStream im = null;
		try {
			byte[] b = data.extractBytesImage(this.file);
			byte[] z  = data.textToBinary2(b); ///tablica z bitami obrazka + txt
			String wynik  = data.getText(z);
			
			System.out.println(wynik + " aaaaaaaaaaaaaaa");
//			im = data.toImage(b,byteText);
//			data.setBitmap(bmp, b);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return im;
	}
	
	public byte [] getFileAction(File f) throws Exception{
		
		return data.extractBytesText(f);
		
	}
	
	public void saveFile(InputStream inputStream){
		
		FileOutputStream outputStream = null;
		
	        try {
	        	inputStream.reset();    	
	        	String path = getFile(false);
	        	
	        	if(!path.equals(""))
	        	{
	        		outputStream = new FileOutputStream(new File(path));
	        	}
	 
	        	OutputStream out= outputStream;
	        	
	        	int read = 0;
	    		byte[] bytes = new byte[1024];
	    		
	    		while ((read = inputStream.read(bytes,0,1024)) != -1) {
	    			out.write(bytes, 0, read);
	    		}
	        	
	        	/*
	            FileWriter fileWriter = null;
	             
	            fileWriter = new FileWriter(file);
	            fileWriter.write(content);
	            fileWriter.close();*/
	        } catch (IOException ex) {
	            ex.printStackTrace();
	        }finally{
	        	if(outputStream != null){
	        		try{
	        			outputStream.close();
	        		}catch(IOException e){
	        			e.printStackTrace();
	        		}
	        	}
	        }
	         
	    }
	
}
