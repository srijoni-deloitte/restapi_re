package Helper;

import org.json.JSONObject;

public class User {
    public String name;
    public String email;
    public String gender;
    public String status;
    public int uid;
    static int id;
    static int getID(){
        id++;
        return id;
    }
    public User(int id){
        this.id=id;
    }

    public User(String name, String email, String gender, String status) {
        this.name = name;
        this.email = email;
        this.uid = getID();
        this.gender = gender;
        this.status=status;
    }

    public JSONObject getFullUserJson() {
        JSONObject res = new JSONObject();
        res.put("id",this.uid);
        res.put("name", this.name);
        res.put("email", this.email);
        res.put("gender", this.gender);
        res.put("status", this.status);
        return res;
    }



    @Override
    public String toString() {
        return this.getFullUserJson().toString();
    }
}
