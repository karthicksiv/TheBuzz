/**
 * NewComment encapsulates all of the code for the form for adding an entry
 */

class NewComment {

    /**
     * The name of the DOM entry associated with NewComment
     */
    private static readonly NAME = "NewComment";

    /**
     * Track if the Singleton has been initialized
     */
    private static isInit = false;

    /**
     * Initialize the NewComment by creating its element in the DOM and 
     * configuring its buttons.  This needs to be called from any public static 
     * method, to ensure that the Singleton is initialized before use
     */
    private static init() {
        if (!NewComment.isInit) {
	    $("body").append(Handlebars.templates[NewComment.NAME + ".hb"]());
	    $("#" + NewComment.NAME + "-OK").on("click", function() { NewComment.submitForm(); });
	    $("#" + NewComment.NAME + "-Close").on("click", function() { NewComment.hide(); });
            NewComment.isInit = true;
        }
    }

    /**
     * Refresh() doesn't really have much meaning, but just like in sNavbar, we
     * have a refresh() method so that we don't have front-end code calling
     * init().
     */
    public static refresh() {
        NewComment.init();
    }

    /**
     * Hide the NewComment.  Be sure to clear its fields first
     */
    private static hide() {
        
        $("#" + NewComment.NAME + "-message").val("");
        $("#" + NewComment.NAME).modal("hide");
    }

    /**
     * Show the NewComment.  Be sure to clear its fields, because there are
     * ways of making a Bootstrap modal disapper without clicking Close, and
     * we haven't set up the hooks to clear the fields on the events associated
     * with those ways of making the modal disappear.
     */
    public static show() {
        
        $("#" + NewComment.NAME + "-message").val("");
        $("#" + NewComment.NAME).modal("show");
        
    }
   /**
     * onSubmitResponse runs when the AJAX call in submitForm() returns a 
     * result.
     * 
     * @param id id
     */
    /**
     * Send data to submit the form only if the fields are both valid.  
     * Immediately hide the form when we send data, so that the user knows that 
     * their click was received.
     */
    private static submitForm() {
        // get the values of the two fields, force them to be strings, and check 
        // that neither is empty
        
        let msg = "" + $("#" + NewComment.NAME + "-message").val();
        if ( msg === "") {
            window.alert("Error: title or message is not valid");
            return;
        }
        NewComment.hide();
        // set up an AJAX post.  When the server replies, the result will go to
        // onSubmitResponse
        $.ajax({
            type: "POST",
            url: "https://bmw-dba.herokuapp.com/buzz/"+id+"/comment",
            dataType: "json",
            data: JSON.stringify({ cMessage: msg }),
            success: NewComment.onSubmitResponse,
            error: NewComment.onError
        });
    }

    /**
     * onSubmitResponse runs when the AJAX call in submitForm() returns a 
     * result.
     * 
     * @param data The object returned by the server
     */
    private static onSubmitResponse(data: any) {
        // If we get an "ok" message, clear the form and refresh the main 
        // listing of messages
        if (data.mStatus === "ok") {
            ViewComment.refresh();
        }
        // Handle explicit errors with a detailed popup message
        else if (data.mStatus === "error") {
            window.alert("The server replied with an error:\n" + data.mMessage);
        }
        // Handle other errors with a less-detailed popup message
        else {
            window.alert("Unspecified error");
        }
    }

    private static onError() {
        console.log("post didn't work");
    }
}
