package ir.vasl.library.helper;

public class PropertyHelper {

    private String clientId      = "http.clientId";
    private String clientSecret  = "http.clientSecret";
    private String site          = "http.site";
    private String appId         = "http.appId";
    private String username      = "http.username";
    private String password      = "http.password";

    public PropertyHelper() {
    }

    public String getClientId() {
        return System.getProperty(clientId);
    }

    public void setClientId(String clientId) {
        System.setProperty(clientId, String.valueOf(clientId));
    }

    public String getclientSecret() {
        return System.getProperty(clientSecret);
    }

    public void setClientSecret(String clientSecret) {
        System.setProperty(this.clientSecret, String.valueOf(clientSecret));
    }

    public String getSite() {
        return System.getProperty(site);
    }

    public void setSite(String site) {
        System.setProperty(this.site, String.valueOf(site));
    }

    public String getAppId() {
        return System.getProperty(appId);
    }

    public void setAppId(String appId) {
        System.setProperty(this.appId, String.valueOf(appId));
    }

    public String getUserName() {
        return System.getProperty(username);
    }

    public void setUserName(String userName) {
        System.setProperty(username, String.valueOf(userName));
    }

    public String getPassword() {
        return System.getProperty(password);
    }

    public void setPassword(String password) {
        System.setProperty(this.password, String.valueOf(password));
    }

}
