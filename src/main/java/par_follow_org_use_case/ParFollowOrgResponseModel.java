package par_follow_org_use_case;

public class ParFollowOrgResponseModel {

    private String orgName;

    private String message;

    public ParFollowOrgResponseModel(String orgName){
        this.orgName=orgName;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage(){return this.message;}
}