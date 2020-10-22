
class ViewComment {
/**
     * The name of the DOM entry associated with 
     */
    private static readonly NAME = "ViewComment";


    /**
     * Track if the Singleton has been initialized
     */
    private static isInit = false;

    /**
     * Initialize the ViewComment by creating its element in the DOM and 
     * configuring its buttons.  This needs to be called from any public static 
     * method, to ensure that the Singleton is initialized before use
     */
    private static init() {
        if (!ViewComment.isInit) {
            $("body").append(Handlebars.templates[ViewComment.NAME + ".hb"]());
            $("#" + ViewComment.NAME + "-commentPost").on("click", function() { ViewComment.submitForm(); });
            //$("#" + ViewComment.NAME + "-Close").on("click", function() { ViewComment.hide(); });
            ViewComment.isInit = true;
        }
    }

    /**
     * Refresh() doesn't really have much meaning, but just like in sNavbar, we
     * have a refresh() method so that we don't have front-end code calling
     * init().
     */
    

    public static refresh() {
        ViewComment.init();
        let id = localStorage.getItem("id");
        $.ajax({
            type: "GET",
            url: "https://bmw-dba.herokuapp.com/buzz/" + id + "/comment",
            dataType: "json",
            success: ViewComment.updateComment
        });

    }

    private static update(data: any) {
        // Remove the table of data, if it exists
        $("#" + ViewComment.NAME).remove();
        // Use a template to re-generate the table, and then insert it
        ViewComment.refresh();
        // To be able to click on a thread and see it expand (To be done in a later phase)
        //$(".title").click(ElementList.clickView);
        // Find all of the delete buttons, and set their behavior
    }

    

    /**
     * Hide the ViewComment.  Be sure to clear its fields first
     */
    private static hide() {
        console.log("Hide form");
        $("#" + ViewComment.NAME + "-uId").val("")
        $("#" + ViewComment.NAME + "-cMessage").val("");
        $("#" + ViewComment.NAME + "-cId").val();
        $("#" + ViewComment.NAME + "-message").val("");
        $("#comments tr").remove(); 
        $("#" + ViewComment.NAME).modal("hide");
       

        $('#popupDelete').modal('toggle');
    }

    private static submitForm() {
        // get the values of the two fields, force them to be strings, and check 
        // that neither is empty
        

        let msg = "" + $("#" + ViewComment.NAME + "-message").val();
        if ( msg === "") {
            window.alert("Error: title or message is not valid");
            return;
        }
        
        let id = localStorage.getItem("id");
        // set up an AJAX post.  When the server replies, the result will go to
        console.log("getitem: "+id);
        // onSubmitResponse
        $.ajax({
            type: "POST",
            url: "https://bmw-dba.herokuapp.com/buzz/"+id+"/comment",
            dataType: "json",
            data: JSON.stringify({ cMessage: msg }),
            success: ViewComment.onSubmitResponse,
            error: ViewComment.onError
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
            console.log("request for comment made");
        }
        // Handle explicit errors with a detailed popup message
        else if (data.mStatus === "error") {
            window.alert("The server replied with an error:\n" + data.mMessage);
        }
        // Handle other errors with a less-detailed popup message
        else {
            window.alert("Unspecified error");
        }
        ViewComment.hide();
    }

    private static onError() {
        console.log("post didn't work");
    }

    /**
     * Show the ViewComment.  Be sure to clear its fields, because there are
     * ways of making a Bootstrap modal disapper without clicking Close, and
     * we haven't set up the hooks to clear the fields on the events associated
     * with those ways of making the modal disappear.
     */
    public static show() {
        console.log("show is called");
        $("#comments tr").remove(); 
        $("#" + ViewComment.NAME + "-uId").val("")
        $("#" + ViewComment.NAME + "-cMessage").val("");
        $("#" + ViewComment.NAME + "-cId").val();
        $("#" + ViewComment.NAME + "-message").val("");
        
        $("#" + ViewComment.NAME).modal("show");
    }

    public static updateComment(data: any) {
        console.log("update comment called");
        ViewComment.show();
        var tableRef = document.getElementById('comments') as HTMLTableElement;

        //$("body").append(Handlebars.templates[ViewComment.NAME + ".hb"](data));
        for (let i=0;i<data.mData.length; i++)
        {
            var row = tableRef.insertRow(0);
            var cMessage = row.insertCell(0);
            var uId = row.insertCell(0);
            console.log("comment"+ data.mData[i].cMessage);
            cMessage.innerHTML = data.mData[i].cMessage;
            uId.innerHTML = data.mData[i].uId;

            /*
            console.log("comment"+ data.mData[i].cMessage);
            $("#" + ViewComment.NAME + "-uId").val(data.mData[i].uId);
            console.log("comment"+ data.mData[i].cMessage);
            $("#" + ViewComment.NAME + "-cMessage").val(data.mData[i].cMessage);
            console.log("comment"+ data.mData[i].cMessage);
            $("#" + ViewComment.NAME + "-cId").val(data.mData[i].cId);
            console.log("comment"+ data.mData[i].cMessage);
            */
            $("#" + ViewComment.NAME).modal("show");
        }
        /*$("#" + ViewComment.NAME + "-uId").val(data.mData.uId);
        console.log("uId:"+data.mData.uId);
        $("#" + ViewComment.NAME + "-cMessage").val(data.mData.cMessage);
        console.log("cMessage:"+ data.mData.cMessage);
        $("#" + ViewComment.NAME + "-cId").val(data.mData.cId);
        console.log("cId:"+ data.mData.cId);
        $("#" + ViewComment.NAME).modal("show");*/
        //$("body").append(Handlebars.templates[.NAME + ".hb"](data2));
    }



       /**
        * Check if the input fields are both valid, and if so, do an AJAX call.
        */
    
       /**
        * onSubmitResponse runs when the AJAX call in submitForm() returns a 
        * result.
        * 
        * @param data The object returned by the server
        *
        */
}

