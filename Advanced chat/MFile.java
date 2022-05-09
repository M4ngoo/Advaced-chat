import java.io.Serializable;

public class MFile implements Serializable{
    private int id;
    private String name, profile;
    private byte[] chunk;

    public MFile(String name, String profile, byte[] chunk){
        this.name = name;
        this.profile = profile;
        this.chunk = chunk;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public void setId(int id) {
        this.id = id;
    }

    public byte[] getChunk() {
        return this.chunk;
    }

    public int getId(){
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getProfile() {
        return this.profile;
    }

    public String toString(){
        return   profile + ": " + name + " with id: " + id;
    }

    public String toStringPosts(){
        return name;
    }
}
