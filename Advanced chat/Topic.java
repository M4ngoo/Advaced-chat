import java.io.Serializable;
import java.util.ArrayList;

public class Topic implements Serializable{
    
    private String name;
    private String topicType;
    private ArrayList<Object> logs;
    private ArrayList<String> profiles; //profiles subscribed to this topic

    public Topic(String name){
        this.name = name;
        this.topicType = "topic";
        this.logs = new ArrayList<Object>();
        this.profiles = new ArrayList<>();
    }

    public String getName(){
        return this.name;
    }

    public void addToLog(Object log){
        this.logs.add(log);
    }

    public ArrayList<Object> getLog(){
        return this.logs;
    }

    public void subscribe(String pf){
        profiles.add(pf);
    }

    public void setTopicType(String topicType){
        this.topicType = topicType;
    }

    public String getTopicType(){
        return this.topicType;
    }

    public void unsubscribe(String pf){
        profiles.remove(pf);
    }

    public ArrayList<String> getProfiles(){
        return profiles;
    }

    public void printProfiles(){
        for(int i=0;i<profiles.size();i++){
            System.out.println(profiles.get(i));
        }
    }
}
