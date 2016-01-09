package javax.xml.parsers;

import java.util.Iterator;

public class Track {

	private int id;
    private String name;
    private String artist;
    private String album;
    
	public void setArtist(String artist) {
		this.artist = artist;
		
	}

	public void setAlbum(String album) {
		this.album = album;
		
	}

	public void size() {
		
	}

	public void setTrackid(int id) {
		this.id = id;
		
	}

	
	public int getTrackid() {
		return id;
	}

	public void setName(String name) {
		this.name = name;
		
	}

	public String getName() {
		return name;
	}

	public String getArtist() {
		
		return artist;
	}

	public static void addTrack(Track currentTrack) {
		currentTrack = new Track();
		
	}

	public static void addTrackToArtist(Track currentTrack) {
		// TODO Auto-generated method stub
		
	}

	public String getAlbum() {
		return album;
	}

	public static void addTrackToAlbum(String artist, String album, Track currentTrack) {
		// TODO Auto-generated method stub
		
	}
	
}
