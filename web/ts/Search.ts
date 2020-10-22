class Search{

    /**
     * The name of the DOM entry associated with Search
     */
    private static readonly NAME = "Search";

    /**
     * Track if the Singleton has been initialized
     */
    private static isInit = false;

    /**
     * Initialize the Search by creating its element in the DOM and 
     * configuring its buttons.  This needs to be called from any public static 
     * method, to ensure that the Singleton is initialized before use
     */
    private static init() {
        if (!Search.isInit) {
            $("body").append(Handlebars.templates[Search.NAME + ".hb"]());
                //$("#" + Search.NAME + "-Close").on("click", function() { Search.hide(); });
            //$("#" + Search.NAME + "-signout").on("click", function() { Search.signOut(); });
            $("#" + Search.NAME + "-Title").on("click", function() { Search.getsearch(); });
            $("#" + Search.NAME + "-Subject").on("click", function() { Search.getsearch(); });
            $("#" + Search.NAME + "-Message").on("click", function() { Search.getsearch(); });
            $("#" + Search.NAME + "-Date").on("click", function() { Search.getsearch(); });
            $("#" + Search.NAME + "-Close").on("click", function() { Search.hide(); });
                Search.isInit = true;
        }
            
    }

    public static show() {
        $("#" + Search.NAME + "-content").val("");
        $("#" + Search.NAME).modal("show");
        console.log(SignIn.getSession());
    }

    private static hide() {
        $("#" + Search.NAME + "-content").val("");
        $("#" + Search.NAME).modal("hide");
    }

    /*private static submitForm() {
        // get the values of the two fields, force them to be strings, and check 
        // that neither is empty
        let title = "" + $("#" + Search.NAME + "-title").val();
        let msg = "" + $("#" + Search.NAME + "-message").val();
        if (title === "" || msg === "") {
            window.alert("Error: title or message is not valid");
            return;
        }
        Search.hide();
        // set up an AJAX post.  When the server replies, the result will go to
        // onSubmitResponse
        $.ajax({
            type: "POST",
            url: "https://bmw-dba.herokuapp.com/buzz",
            dataType: "json",
            data: JSON.stringify({ mTitle: title, mMessage: msg }),
            success: Search.onSubmitResponse,
            error: Search.onError
        });
    }*/


    public static getsearch() {
        $.ajax({
            type: "GET",
            url: "https://bmw-dba.herokuapp.com/buzz/search_title/" + $("#"+Search.NAME+"-search").val(),
            dataType: "json",
            success: Search.onSearchResult
        });
    }

    public static onSearchResult(data: any) {
        console.log("onSearchResult ", data);
    }


}