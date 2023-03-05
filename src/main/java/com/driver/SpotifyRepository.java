package com.driver;

import java.util.*;

import org.springframework.stereotype.Repository;

@Repository
public class SpotifyRepository {
    public HashMap<Artist, List<Album>> artistAlbumMap;
    public HashMap<Album, List<Song>> albumSongMap;
    public HashMap<Playlist, List<Song>> playlistSongMap;
    public HashMap<Playlist, List<User>> playlistListenerMap;
    public HashMap<User, Playlist> creatorPlaylistMap;
    public HashMap<User, List<Playlist>> userPlaylistMap;
    public HashMap<Song, List<User>> songLikeMap;

    public List<User> users;
    public List<Song> songs;
    public List<Playlist> playlists;
    public List<Album> albums;
    public List<Artist> artists;

    public SpotifyRepository(){
        //To avoid hitting apis multiple times, initialize all the hashmaps here with some dummy data
        artistAlbumMap = new HashMap<>();
        albumSongMap = new HashMap<>();
        playlistSongMap = new HashMap<>();
        playlistListenerMap = new HashMap<>();
        creatorPlaylistMap = new HashMap<>();
        userPlaylistMap = new HashMap<>();
        songLikeMap = new HashMap<>();

        users = new ArrayList<>();
        songs = new ArrayList<>();
        playlists = new ArrayList<>();
        albums = new ArrayList<>();
        artists = new ArrayList<>();
    }

    public User createUser(String name, String mobile) {
        User user = new User(name,mobile);
        users.add(user);
        return user;
    }

    public Artist createArtist(String name) {
        Artist artist = new Artist(name);
        artists.add(artist);
        return artist;
    }

    public Album createAlbum(String title, String artistName) {
        boolean flag = false;
        Artist artist1 = null;
        for (Artist artist : artists) {
            if (artist.getName().equals(artistName)) {
                flag = true;
                artist1 = artist;
                break;
            }
        }

        if (!flag) {
            artist1 = new Artist(artistName);
        }

        Album album = new Album(title);
        albums.add(album);
        List<Album> list = artistAlbumMap.getOrDefault(artist1, new ArrayList<>());
        list.add(album);
        artistAlbumMap.put(artist1,list);
        return album;
    }

    public Song createSong(String title, String albumName, int length) throws Exception{
        Song song = new Song(title,length);
        songs.add(song);
        Album name = null;
        boolean flag =  false;
        for (Album album: albums){
            if (album.getTitle().equals(albumName)){
                flag = true;
                name = album;
                break;
            }
        }
        if (flag){
            List<Song> list = albumSongMap.getOrDefault(name, new ArrayList<>());
            list.add(song);
            albumSongMap.put(name,list);
        }
        else {
            throw new Exception("Album does not exist");
        }

        return song;
    }

    public Playlist createPlaylistOnLength(String mobile, String title, int length) throws Exception {
        Playlist playlist = new Playlist(title);
        playlists.add(playlist);
        User user1 =  null;
        boolean flag = false;
        for (User user: users){
            if (user.getMobile().equals(mobile)){
                flag = true;
                user1 = user;
                break;
            }
        }
        if (flag){
            for (Song song: songs){
                if (song.getLength() == length){
                    List<Song> list = playlistSongMap.getOrDefault(playlist, new ArrayList<>());
                    list.add(song);
                    playlistSongMap.put(playlist,list);

                    List<User> list1 = playlistListenerMap.getOrDefault(playlist,new ArrayList<>());
                    list1.add(user1);
                    playlistListenerMap.put(playlist,list1);

                    creatorPlaylistMap.put(user1,playlist);

                    List<Playlist> list2 = userPlaylistMap.getOrDefault(user1, new ArrayList<>());
                    list2.add(playlist);
                    userPlaylistMap.put(user1,list2);
                }
            }
        }
        else {
            throw new Exception("User does not exist");
        }
        return playlist;
    }

    public Playlist createPlaylistOnName(String mobile, String title, List<String> songTitles) throws Exception {
        Playlist playlist = new Playlist(title);
        User user1 =  null;
        playlists.add(playlist);
        boolean flag = false;
        for (User user: users){
            if (user.getMobile().equals(mobile)){
                flag = true;
                user1 = user;
                break;
            }
        }
        if (flag){
            for (Song song: songs){
                for (String songName: songTitles) {
                    if (song.getTitle().equals(songName)) {
                        List<Song> list = playlistSongMap.getOrDefault(playlist, new ArrayList<>());
                        list.add(song);
                        playlistSongMap.put(playlist, list);

                        List<User> list1 = playlistListenerMap.getOrDefault(playlist, new ArrayList<>());
                        list1.add(user1);
                        playlistListenerMap.put(playlist, list1);

                        creatorPlaylistMap.put(user1, playlist);

                        List<Playlist> list2 = userPlaylistMap.getOrDefault(user1, new ArrayList<>());
                        list2.add(playlist);
                        userPlaylistMap.put(user1, list2);
                        break;
                    }
                }
            }
        }
        else {
            throw new Exception("User does not exist");
        }
        return playlist;
    }

    public Playlist findPlaylist(String mobile, String playlistTitle) throws Exception {
        User user1 =  null;
        Playlist playlist = null;
        boolean flag = false;
        for (User user: users){
            if (user.getMobile().equals(mobile)){
                flag = true;
                user1 = user;
                break;
            }
        }
        if (flag){
            boolean flag1 = false;
            for (Playlist playlist1: playlists){
                if (playlist1.getTitle().equals(playlistTitle)){
                    flag1 = true;
                    playlist =  playlist1;
                    break;
                }
            }
            if (flag1){
                List<User> list =  playlistListenerMap.getOrDefault(playlist,new ArrayList<>());
                list.add(user1);
                playlistListenerMap.put(playlist,list);

                List<Playlist> list1 =  userPlaylistMap.getOrDefault(user1, new ArrayList<>());
                list1.add(playlist);
                userPlaylistMap.put(user1,list1);
            }
            else {
                throw new Exception("Playlist does not exist");
            }
        }
        else {
            throw new Exception("User does not exist");
        }
        return playlist;
    }

    public Song likeSong(String mobile, String songTitle) throws Exception {

    }

    public String mostPopularArtist() {
    }

    public String mostPopularSong() {
    }
}
