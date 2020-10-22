/**
 * NewEntryForm encapsulates all of the code for the form for adding an entry
 */

class SignIn {

    /**
     * The name of the DOM entry associated with NewEntryForm
     */
    private static readonly NAME = "SignIn";

    /**
     * Track if the Singleton has been initialized
     */
    private static isInit = false;

    /**
     * Initialize the NewEntryForm by creating its element in the DOM and 
     * configuring its buttons.  This needs to be called from any public static 
     * method, to ensure that the Singleton is initialized before use
     */
    private static init() {
        if (!SignIn.isInit) {
                $("body").append(Handlebars.templates[SignIn.NAME + ".hb"]());
                $("#" + SignIn.NAME + "-Close").on("click", function() { SignIn.hide(); });
            //$("#" + SignIn.NAME + "-signout").on("click", function() { SignIn.signOut(); });
                SignIn.isInit = true;
            }
            
    }

    /*
    private static onSignIn(googleUser: { getBasicProfile: () => any; getAuthResponse: () => { (): any; new(): any; id_token: any; }; }) {
        // Useful data for your client-side scripts:
        var profile = googleUser.getBasicProfile();
        $("#g-signin2").css("display","none");
        $("#user").css("display","block");
        $("#pic").attr('src',profile.getImageUrl());
        $("#email").text(profile.getEmail());
        $("#name").text(profile.getName());

        console.log("ID: " + profile.getId()); // Don't send this directly to your server!
        console.log('Full Name: ' + profile.getName());
        console.log('Given Name: ' + profile.getGivenName());
        console.log('Family Name: ' + profile.getFamilyName());
        console.log("Image URL: " + profile.getImageUrl());
        console.log("Email: " + profile.getEmail());

        // The ID token you need to pass to your backend:
        var id_token = googleUser.getAuthResponse().id_token;
        console.log("ID Token: " + id_token);
    }

    private static signOut()
    {
        var auth2 = gapi.auth2.getAuthInstance();
        auth2.signOut().then(function(){
            console.log('User signed out.');
           $("#g-signin2").css("display","block");
        });
    }
    */

    /**
     * Refresh() doesn't really have much meaning, but just like in sNavbar, we
     * have a refresh() method so that we don't have front-end code calling
     * init().
     */
    public static refresh() {
        SignIn.init();
    }

    /**
     * Hide the NewEntryForm.  Be sure to clear its fields first
     */
    private static hide() {
        //$("#" + NewEntryForm.NAME + "-title").val("");
        //$("#" + NewEntryForm.NAME + "-message").val("");
        $("#" + "g-signin2");
        $("#" + SignIn.NAME).modal("hide");
    }

    /**
     * Show the NewEntryForm.  Be sure to clear its fields, because there are
     * ways of making a Bootstrap modal disapper without clicking Close, and
     * we haven't set up the hooks to clear the fields on the events associated
     * with those ways of making the modal disappear.
     */
    public static show() {
        //$("#" + NewEntryForm.NAME + "-title").val("");
        //$("#" + NewEntryForm.NAME + "-message").val("");
        $("#" + "g-signin2");
        $("#" + SignIn.NAME).modal("show");
    }

    public static getSession() {
        var session = $('h1').text();
        return session;
    }

    /*
    private static onSignIn(googleUser) {

        var profile = googleUser.getBasicProfile();
        console.log("ID: " + profile.getId()); // Don't send this directly to your server!
        console.log('Full Name: ' + profile.getName());
        console.log('Given Name: ' + profile.getGivenName());
        console.log('Family Name: ' + profile.getFamilyName());
        console.log("Image URL: " + profile.getImageUrl());
        console.log("Email: " + profile.getEmail());
        // The ID token you need to pass to your backend:
        var id_token = googleUser.getAuthResponse().id_token;
        console.log("ID Token: " + id_token);

    }
    */


}
