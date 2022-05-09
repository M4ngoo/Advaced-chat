import java.io.Serializable;
import java.util.ArrayList;

public class Profile implements Serializable{
    
    private String name;
    private ArrayList<Object> posts;
    private ArrayList<String> friends;
    private ArrayList<String> friendRequests;
    private ArrayList<String> subscribed;
    
    public Profile(String name){
        this.name = name;
        this.posts = new ArrayList<>();
        this.friends = new ArrayList<>();
        this.friendRequests = new ArrayList<>(); //profiles that THIS profile has sent friend requests to
        this.subscribed = new ArrayList<>(); //topics that THIS profile has subscribed to
    }

    public String getName(){
        return this.name;
    }

    public void setSubscribed(String topic){
        this.subscribed.add(topic);
    }

    public void unsetSubscribed(String topic){
        this.subscribed.remove(topic);
    }

    public ArrayList<String> getSubscribed(){
        return this.subscribed;
    }

    public ArrayList<String> getFriendRequests(){
        return friendRequests;
    }

    public ArrayList<String> getFriends(){
        return friends;
    }

    public String showFriends(){
        String msg = "";
        for(int i=0;i<friends.size();i++){
            msg += friends.get(i) + "\n";
        }
        return msg;
    }

    public ArrayList<Object> getPosts(){
        return this.posts;
    }

    public void addToPosts(Object obj){
        this.posts.add(obj);
    }
}
