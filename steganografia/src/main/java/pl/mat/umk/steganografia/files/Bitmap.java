package pl.mat.umk.steganografia.files;

public class Bitmap {
	
	
	public static final int TO_LONG_FIRST_BYTE = 256;
	public static final int TO_LONG_SECOND_BYTE = 65536;
	public static final int TO_LONG_THIRD_BYTE = 16777216;
	
	private String nazwa;
	private String sygnatura;
	private long size;
	private long size_image;
	private long height;
	private long vertical;
	private long size_header;
	private long here_data_image;
	private long count_olors;
	private long count_bit_on_pixel;
	private long kinf_of_compress;
	private long dim_height;
	private long dim_vertical;
	private long count_colors_in_pallet;
	private long count_colors;
	private long rotation_pallets;
	public String getNazwa() {
		return nazwa;
	}
	public void setNazwa(String nazwa) {
		this.nazwa = nazwa;
	}
	public String getSygnatura() {
		return sygnatura;
	}
	public void setSygnatura(String sygnatura) {
		this.sygnatura = sygnatura;
	}
	public long getSize() {
		return size;
	}
	public void setSize(long size) {
		this.size = size;
	}
	public long getSize_image() {
		return size_image;
	}
	public void setSize_image(long size_image) {
		this.size_image = size_image;
	}
	public long getHeight() {
		return height;
	}
	public void setHeight(long height) {
		this.height = height;
	}
	public long getVertical() {
		return vertical;
	}
	public void setVertical(long vertical) {
		this.vertical = vertical;
	}
	public long getSize_header() {
		return size_header;
	}
	public void setSize_header(long size_header) {
		this.size_header = size_header;
	}
	public long getHere_data_image() {
		return here_data_image;
	}
	public void setHere_data_image(long here_data_image) {
		this.here_data_image = here_data_image;
	}
	public long getCount_olors() {
		return count_olors;
	}
	public void setCount_olors(long count_olors) {
		this.count_olors = count_olors;
	}
	public long getCount_bit_on_pixel() {
		return count_bit_on_pixel;
	}
	public void setCount_bit_on_pixel(long count_bit_on_pixel) {
		this.count_bit_on_pixel = count_bit_on_pixel;
	}
	public long getKinf_of_compress() {
		return kinf_of_compress;
	}
	public void setKinf_of_compress(long kinf_of_compress) {
		this.kinf_of_compress = kinf_of_compress;
	}
	public long getDim_height() {
		return dim_height;
	}
	public void setDim_height(long dim_height) {
		this.dim_height = dim_height;
	}
	public long getDim_vertical() {
		return dim_vertical;
	}
	public void setDim_vertical(long dim_vertical) {
		this.dim_vertical = dim_vertical;
	}
	public long getCount_colors_in_pallet() {
		return count_colors_in_pallet;
	}
	public void setCount_colors_in_pallet(long count_colors_in_pallet) {
		this.count_colors_in_pallet = count_colors_in_pallet;
	}
	public long getCount_colors() {
		return count_colors;
	}
	public void setCount_colors(long count_colors) {
		this.count_colors = count_colors;
	}
	public long getRotation_pallets() {
		return rotation_pallets;
	}
	public void setRotation_pallets(long rotation_pallets) {
		this.rotation_pallets = rotation_pallets;
	}
	
	
	
	
}
