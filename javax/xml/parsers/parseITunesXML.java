package javax.xml.parsers;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.Attributes;
import org.xml.sax.HandlerBase;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

public class parseITunesXML extends DefaultHandler {
	int dictLevel = 0;
	boolean nextIsTracks;
	boolean doingTracks;
	boolean nextIsPlaylists;
	boolean doingPlaylists;
	boolean insideKey;
	boolean insidePlaylist;
	boolean insideTrack;
	String currentKey;
	String currentQname;
	Track currentTrack;
	Playlist currentPlaylist;
	Artist currentArtist;
	Album currentAlbum;

	private static final HandlerBase DefaultHandler = null;

	public static void main(String[] args) {
		/* get a factory */
		SAXParserFactory spf = SAXParserFactory.newInstance();
		try {
			/* get a new instance of parser */
			SAXParser sp = spf.newSAXParser();
			/* parse the file and register class for call backs */
			sp.parse(new File("/Users/Erica/Music/iTunes/iTunes Music Library.xml"), new parseITunesXML());
		} catch (SAXException se) {
			se.printStackTrace();
		} catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		} catch (IOException ie) {
			ie.printStackTrace();
		}

		try {
			System.setOut(new PrintStream(new BufferedOutputStream(new FileOutputStream("iTunesParse_output.txt"))));
		} catch (FileNotFoundException e) {

			e.printStackTrace();
		}
	}

	/* Starting Document */
	public void startDocument() throws SAXException {
		System.out.println("starting document");
	}

	/*
	 * Starting Element Going down the three levels of "dict" to the info needed
	 */
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		System.out.println("Starting Element");
		if ("dict".equals(qName)) {
			dictLevel = dictLevel + 1;
		}
		if ("key".equals(qName)) {
			insideKey = true;
		}
		if ("dict".equals(qName) && dictLevel == 2 && nextIsTracks) {
			System.out.println("starting tracks");
			doingTracks = true;
			nextIsTracks = false;
			nextIsPlaylists = true;
		}

		if ("dict".equals(qName) && dictLevel == 2 && nextIsPlaylists) {
			System.out.println("starting playlists");
			doingPlaylists = true;
			nextIsPlaylists = false;
		}
		if ("dict".equals(qName) && dictLevel == 3 && doingTracks) {
			insideTrack = true;
			currentTrack = new Track();
			currentArtist = new Artist();
			currentAlbum = new Album();
		}
		if ("dict".equals(qName) && dictLevel == 2 && doingPlaylists) {
			insidePlaylist = true;
			Playlist currentPlaylist = new Playlist();
		}
		currentQname = qName;
		System.out.print(qName);
	}

	public void characters(char[] ch, int start, int length) throws SAXException {
		if (insideKey && dictLevel == 2) {
			String value = new String(ch, start, length);
			if ("Tracks".equals(value)) {
				nextIsTracks = true;
			}
			if ("Playlists".equals(value)) {
				nextIsPlaylists = true;
			}
		}

		if (insideTrack && insideKey) {
			currentKey = new String(ch, start, length);
		}

		if ("integer".equals(currentQname) && "Track ID".equals(currentKey) && currentTrack != null) {
			String value = new String(ch, start, length);
			(currentTrack).setTrackid(Integer.parseInt(value));
		}
		if ("string".equals(currentQname) && "Name".equals(currentKey) && currentTrack != null) {
			String value = new String(ch, start, length);
			currentTrack.setName(value);

		}
		if ("string".equals(currentQname) && "Artist".equals(currentKey) && currentArtist != null) {
			currentArtist.setArtist(currentArtist);
			/* FIX Album.addAlbumToArtist(currentAlbum.getName());*/

		}
		if ("string".equals(currentQname) && "Album".equals(currentKey) && currentTrack != null) {
			String value = new String(ch, start, length);
			currentAlbum.setAlbum(currentAlbum);
			/* FIX Track.addTrackToAlbum(currentTrack.getName()); */
		}

		if (insidePlaylist && insideKey) {
			currentKey = new String(ch, start, length);
		}
		if (insidePlaylist && currentPlaylist != null) {
			String value = new String(ch, start, length);
			if ("string".equals(currentQname) && "Name".equals(currentKey)) {
				currentPlaylist.setName(value);
			}
			if ("integer".equals(currentQname) && "Playlist ID".equals(currentKey)) {
				System.out.println("got the id of a playlist: " + value);
				currentPlaylist.setId(Integer.parseInt(value));
			}
			// pick up the track id inside the playlist
			if ("Track ID".equals(currentKey) && "integer".equals(currentQname)) {
				currentPlaylist.addTrack(Integer.parseInt(value));
			}
		}
	}

	public void endElement(String uri, String localName, String qName) throws SAXException {
		if ("dict".equals(qName)) {
			if (dictLevel == 2) {
				doingTracks = false;
			}
			dictLevel = dictLevel - 1;
			if (insideTrack && currentTrack != null) {
				if (currentTrack.getArtist() != null) {
					Track.addTrack(currentTrack);
					Track.addTrackToArtist(currentTrack);

				}
				currentTrack = null;
			}
			insideTrack = false;
			if (insidePlaylist && currentPlaylist != null && dictLevel == 1) {
				Playlist.add(currentPlaylist);
				currentPlaylist = null;
			}
		}
		if ("key".equals(qName)) {
			insideKey = false;
		}
		System.out.print("End Element");
	}

	public void endDocument() throws SAXException {
		System.out.println("Ending Document");
	}

}
