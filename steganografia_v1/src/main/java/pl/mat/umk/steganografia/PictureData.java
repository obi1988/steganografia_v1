package pl.mat.umk.steganografia;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.File;

import javax.imageio.ImageIO;

import pl.mat.umk.steganografia.files.Bitmap;

public class PictureData {
	
	
	
	/**
	 * Ustawianie obiektu Bitmapy
	 * @param bmp
	 * @param b
	 * @return
	 */
	
	public Bitmap setBitmap(Bitmap bmp, byte[] b){
		
		bmp.setSygnatura( new String(new byte[]{b[0],b[1]}));
		bmp.setSize(getLongFromByte(new byte[]{b[2],b[3],b[4],b[5]},4));
		bmp.setHere_data_image(getLongFromByte(new byte[]{b[9],b[10],b[11],b[12]},4));
		bmp.setSize_header(getLongFromByte(new byte[]{b[13],b[14],b[15],b[16]},4));
		bmp.setVertical(getLongFromByte(new byte[]{b[17],b[18],b[19],b[20]},4));
		bmp.setHeight(getLongFromByte(new byte[]{b[21],b[22],b[23],b[24]},4));
		bmp.setCount_colors_in_pallet(getLongFromByte(new byte[]{b[25],b[26]},2));
		bmp.setCount_bit_on_pixel(getLongFromByte(new byte[]{b[27],b[28]},2));
		bmp.setKinf_of_compress(getLongFromByte(new byte[]{b[29],b[30],b[31],b[32]},4));
		bmp.setSize_image(getLongFromByte(new byte[]{b[33],b[34],b[355],b[36]},4));
		
		try{
			System.out.println(bmp.getSygnatura() + " rozmiar " + bmp.getKinf_of_compress());
			
			byte [] zb = new String("Michal").getBytes();
			
			
			String s2 = String.format("%8s", Integer.toBinaryString(zb[0] & 0xFF)).replace(' ', '0');
			System.out.println(s2);
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return bmp;
	}
	

	
	
	public byte[] textToBinary2(byte... bs) {
		byte[] result = new byte[Byte.SIZE * bs.length];
		int offset = 0;
		for (byte b : bs) {
			for (int i = 0; i < Byte.SIZE; i++)
				result[i + offset] = (b >> i & 0x1) != 0x0 ? (byte)0 : 1;
			offset += Byte.SIZE;
		}
		return result;
	}

	
	
	/**
	 * Konwersja z byte na long
	 * 
	 * @param b tablica byte
	 * @param size ilosc elementów w tablicy
	 * @return liczba powstała z zamiany bajtów
	 */
	private long getLongFromByte(byte [] b, int size){
		
		long n = 0;
		
		if(size >= 1){
			n = (int)b[0];
		}
		if(size >= 2){
			n += (int)b[1] * Bitmap.TO_LONG_FIRST_BYTE;
		}
		if(size == 4){
			n += (int)b[2] * Bitmap.TO_LONG_SECOND_BYTE;
			n += (int)b[3] * Bitmap.TO_LONG_THIRD_BYTE;
		}
		
		return n;
		
	}
	
	
	private byte[] encode_text(byte[] image, byte[] addition, int offset)
	{
		//check that the data + offset will fit in the image
		if(addition.length + offset > image.length)
		{
			throw new IllegalArgumentException("File not long enough!");
		}
		//loop through each addition byte
		for(int i=0; i<addition.length; ++i)
		{
			//loop through the 8 bits of each byte
			int add = addition[i];
			for(int bit=7; bit>=0; --bit, ++offset) //ensure the new offset value carries on through both loops
			{
				//assign an integer to b, shifted by bit spaces AND 1
				//a single bit of the current byte
				int b = (add >>> bit) & 1;
				//assign the bit by taking: [(previous byte value) AND 0xfe] OR bit to add
				//changes the last bit of the byte in the image to be the bit of addition
				image[offset] = (byte)((image[offset] & 0xFE) | b );
			}
		}
		return image;
	}
	
	/*
	 *Retrieves hidden text from an image
	 *@param image Array of data, representing an image
	 *@return Array of data which contains the hidden text
	 */
	public byte[] decode_text(byte[] image)
	{
		int length = 0;
		int offset  = 32;
		//loop through 32 bytes of data to determine text length
		for(int i=0; i<32; ++i) //i=24 will also work, as only the 4th byte contains real data
		{
			length = (length << 1) | (image[i] & 1);
		}
		
		byte[] result = new byte[length];
		
		//loop through each byte of text
		for(int b=0; b<result.length; ++b )
		{
			//loop through each bit within a byte of text
			for(int i=0; i<8; ++i, ++offset)
			{
				//assign bit: [(new byte value) << 1] OR [(text byte) AND 1]
				result[b] = (byte)((result[b] << 1) | (image[offset] & 1));
			}
		}
		return result;
	}


	/*
	 *Gernerates proper byte format of an integer
	 *@param i The integer to convert
	 *@return Returns a byte[4] array converting the supplied integer into bytes
	 */
	public byte[] bit_conversion(int i)
	{
		//originally integers (ints) cast into bytes
		//byte byte7 = (byte)((i & 0xFF00000000000000L) >>> 56);
		//byte byte6 = (byte)((i & 0x00FF000000000000L) >>> 48);
		//byte byte5 = (byte)((i & 0x0000FF0000000000L) >>> 40);
		//byte byte4 = (byte)((i & 0x000000FF00000000L) >>> 32);
		
		//only using 4 bytes
		byte byte3 = (byte)((i & 0xFF000000) >>> 24); //0
		byte byte2 = (byte)((i & 0x00FF0000) >>> 16); //0
		byte byte1 = (byte)((i & 0x0000FF00) >>> 8 ); //0
		byte byte0 = (byte)((i & 0x000000FF)	   );
		//{0,0,0,byte0} is equivalent, since all shifts >=8 will be 0
		return(new byte[]{byte3,byte2,byte1,byte0});
	}
	public BufferedImage add_text(BufferedImage image, byte msg[])
	{
		//convert all items to byte arrays: image, message, message length
		byte img[]  =   get_byte_data(image);
		byte len[]   = bit_conversion(msg.length);
		try
		{
			encode_text(img, len,  0); //0 first positiong
			encode_text(img, msg, 32); //4 bytes of space for length: 4bytes*8bit = 32 bits
		}
		catch(Exception e)
		{
			Dialog.showWarning("Warning","Brak wystarczającego miejsca w obrazie by zakodować taką ilość danych");
		}
		
		
		return image;
	}
	public byte[] get_byte_data(BufferedImage image)
	{
		WritableRaster raster   = image.getRaster();
		DataBufferByte buffer = (DataBufferByte)raster.getDataBuffer();
		return buffer.getData();
	}
	
	public BufferedImage getImage(String f)
	{
		BufferedImage 	image	= null;
		File 		file 	= new File(f);
		
		try
		{
			image = ImageIO.read(file);
		}
		catch(Exception ex)
		{
			Dialog.showError("Error","Image could not be read!");
		}
		return image;
	}
	
	public String decode(BufferedImage image)
	{
		byte[] decode;
		try
		{
			decode = decode_text(get_byte_data(image));
			return(new String(decode));
		}
		catch(Exception e)
		{
			Dialog.showWarning("Ostrzeżenie!", "Nie znaleziono ukrytej wiadomości");
			return "";
		}
	}
}
