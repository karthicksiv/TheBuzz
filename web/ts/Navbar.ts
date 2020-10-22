/**
 * The Navbar Singleton is the navigation bar at the top of the page.  Through 
 * its HTML, it is designed so that clicking the "brand" part will refresh the
 * page.  Apart from that, it has an "add" button, which forwards to 
 * NewEntryForm
 */
class Navbar {
    /**
     * Track if the Singleton has been initialized
     */
    private static isInit = false;

    /**
     * The name of the DOM entry associated with Navbar
     */
    private static readonly NAME = "Navbar";

    /**
     * Initialize the Navbar Singleton by creating its element in the DOM and
     * configuring its button.  This needs to be called from any public static
     * method, to ensure that the Singleton is initialized before use.
     */
    private static init() {
        if (!Navbar.isInit) {
            $("body").prepend(Handlebars.templates[Navbar.NAME + ".hb"]());
            $("#"+Navbar.NAME+"-add").on("click", function() { NewEntryForm.show(); });
            $("#"+Navbar.NAME+"-signin").on("click", function() { SignIn.show(); });
            $("#"+Navbar.NAME+"-gensearch").on("click", function() {Navbar.postsearch(); });
            $("#"+Navbar.NAME+"-title").on("click", function() {Navbar.posttitle(); });
            $("#"+Navbar.NAME+"-message").on("click", function() {Navbar.postmess(); });
            $("#"+Navbar.NAME+"-author").on("click", function() {Navbar.postauth(); });
            $("#"+Navbar.NAME+"-date").on("click", function() {Navbar.postdate(); });
            $("#"+Navbar.NAME+"-profile").on("click", function() {Navbar.postprofile(); });
            Navbar.isInit = true;
        }
    }
    /**
     * Search by title and message (general search)
     */
    public static postsearch() {
        $.ajax({
            type: "GET",
            url: "https://bmw-dba.herokuapp.com/buzz/search_TM/" + $("#"+Navbar.NAME+"-search").val(),
            dataType: "json",
            success: ElementList.update
        });
        //alert($("#"+Navbar.NAME+"-search").val());
    }
    /**
     * Search by title 
     */
    public static posttitle() {
        $.ajax({
            type: "GET",
            url: "https://bmw-dba.herokuapp.com/buzz/search_title/" + $("#"+Navbar.NAME+"-search").val(),
            dataType: "json",
            success: ElementList.update
        });
        //alert($("#"+Navbar.NAME+"-search").val());
    }
    /**
     * Search by message
     */
    public static postmess() {
        $.ajax({
            type: "GET",
            url: "https://bmw-dba.herokuapp.com/buzz/search_message/" + $("#"+Navbar.NAME+"-search").val(),
            dataType: "json",
            success: ElementList.update
        });
        //alert($("#"+Navbar.NAME+"-search").val());
    }
    /**
     * Search by user
     */
    public static postprofile() {
        $.ajax({
            type: "GET",
            url: "https://bmw-dba.herokuapp.com/buzz/" + $("#"+Navbar.NAME+"-search").val() + "/profile",
            dataType: "json",
            success: ElementList.update
        });
        //alert($("#"+Navbar.NAME+"-search").val());
    }
    /**
     * Search by date YYYY-MM-DD
     */
    public static postdate() {
        $.ajax({
            type: "GET",
            url: "https://bmw-dba.herokuapp.com/buzz/search_date/" + $("#"+Navbar.NAME+"-search").val(),
            dataType: "json",
            success: ElementList.update
        });
        //alert($("#"+Navbar.NAME+"-search").val());
    }
    /**
     * Search for author
     */
    public static postauth() {
        $.ajax({
            type: "GET",
            url: "https://bmw-dba.herokuapp.com/buzz/search_author/" + $("#"+Navbar.NAME+"-search").val(),
            dataType: "json",
            success: ElementList.update
        });
        //alert($("#"+Navbar.NAME+"-search").val());
    }

    public static onSearchResult(data: any) {
        console.log("onSearchResult ", data);
    }

    /**
     * Refresh() doesn't really have much meaning for the navbar, but we'd 
     * rather not have anyone call init(), so we'll have this as a stub that
     * can be called during front-end initialization to ensure the navbar
     * is configured.
     */
    public static refresh() {
        Navbar.init();
    }
}

