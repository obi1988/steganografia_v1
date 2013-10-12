package pl.mat.umk.steganografia;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.stage.FileChooser;

import javax.imageio.ImageIO;

import pl.mat.umk.steganografia.files.Bitmap;

public class PictureAction {

	private PictureData data = new PictureData();
	private byte [] byteText;
	SliderActionListener sliderAction;

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
	
	/**
	 * Pobieranie sciezki do pliku
	 * 
	 * @param isOpen true w przypadku wybrania pliku do otwarcia  \n
	 * 				  false w przypadku wybrania pliku do zapisu.
	 * @param isGraphic dla plików graficznych
	 * @return
	 */
	public String getPathToFile(boolean isOpen, boolean isGraphic){
		
		FileChooser fchoose = new FileChooser();
		File file ;
		String result = "";
		fchoose.setTitle("Wybierz plik");
		fchoose.setInitialDirectory(new File(System.getProperty("user.home")+"/Desktop/"));
		
		if(isOpen){
			if(isGraphic)
				fchoose.getExtensionFilters().addAll(
	                new FileChooser.ExtensionFilter("All Images", "*.*"),
	                new FileChooser.ExtensionFilter("JPG", "*.jpg"),
	                new FileChooser.ExtensionFilter("PNG", "*.png")
	            );	
			
			file = fchoose.showOpenDialog(null);
		}else{
			file = fchoose.showSaveDialog(null);
		}
		
		if(file != null){
			result = file.getAbsolutePath();
		}

		return result;
	}
	
	public void showAbout(){
		Dialog.showInfo("About", "Program pozwala zakodować tekst w obrazie.\n"
				+ "*Najpierw należy załadować obraz: File - Open image (rodzaj pliku to mapa bitowa [*.bmp]).\n"
				+ "*Następnie należy załadować plik z tekstem: File - Open text (plik z rozszrzeniem [*.txt])\n"
				+ "*A następnie w celu zakodowania należy wybrać: Action - Encode.\n"
				+ "*Zakodowany plik można zapisać na dysk: File - Save. \n"
				+ "*Aby rozkodować należy wybrać: Action - Decode");
	}
	
	public void close(ActionEvent actionEvent) {
		
		Platform.exit();
		/*Node source = (Node) actionEvent.getSource();
		  Stage stage = (Stage) source.getScene().getWindow();
		  stage.close();
		*/
	}
	
	public InputStream start(){
		
		Dialog.showProgress("Attension", "Please Wait...");
		
		byte[] dataBIT = null;
		try {
			sliderAction.setSliders(1);
			
			BufferedImage img = data.add_text(ImageIO.read(new File(this.file.getAbsolutePath())), byteText);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(img, "bmp", baos);
			baos.flush();
			
			dataBIT = baos.toByteArray();
			
			baos.close();
			
			data.setBitmap(bmp, dataBIT);
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ByteArrayInputStream(dataBIT);
	}
	
	
	/**
	 * Metoda pozwala odszyfrować zaszytą w bajtach obrazu informację 
	 * @param progBar
	 * @param progIndi
	 * @return
	 */
	public String odszyfruj(){
		String wynik = "";
		try {
			wynik = data.decode(ImageIO.read(new File(this.file.getAbsolutePath())));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return wynik;
	}
	
	public void saveFile(InputStream inputStream) {

		OutputStream outputStream = null;

		try {
			inputStream.reset();
			String path = getPathToFile(false, false);

			if (!path.equals("")) {
				outputStream = (OutputStream) new FileOutputStream(new File(path));
			}

			int read = 0;
			byte[] bytes = new byte[1024];

			while ((read = inputStream.read(bytes, 0, 1024)) != -1) {
				outputStream.write(bytes, 0, read);
			}

		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (outputStream != null) {
				try {
					outputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}

	public void setSliderListener(SliderActionListener listener){
		this.sliderAction = listener; 
	}
	
	public Task<Void>  getTask(){
		Task<Void> task = new Task<Void>() {
	      @Override public Void call() {
	        for (int i = 1; i < 100; i++) {
	          try {
	            Thread.sleep(3);
	          } catch (InterruptedException e) {
	            e.printStackTrace();
	          }
	          updateProgress(i, 100);
	        }
	        return null;
	      }
		};
		return task;
	}
}
