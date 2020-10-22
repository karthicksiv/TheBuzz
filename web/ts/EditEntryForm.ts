  /**
   * EditEntryForm encapsulates all of the code for the form for editing an entry
  */
 class EditEntryForm {
/**
     * The name of the DOM entry associated with EditEntryForm
     */
    private static readonly NAME = "EditEntryForm";


    /**
     * Track if the Singleton has been initialized
     */
    private static isInit = false;

    /**
     * Initialize the EditEntryForm by creating its element in the DOM and 
     * configuring its buttons.  This needs to be called from any public static 
     * method, to ensure that the Singleton is initialized before use
     */
    private static init() {
        if (!EditEntryForm.isInit) {
	        $("body").append(Handlebars.templates[EditEntryForm.NAME + ".hb"]());
	        $("#" + EditEntryForm.NAME + "-OK").on("click", function() { EditEntryForm.submitForm(); });
	        $("#" + EditEntryForm.NAME + "-Close").on("click", function() { EditEntryForm.hide(); });
            
            EditEntryForm.isInit = true;
        }
    }

    /**
     * Refresh() doesn't really have much meaning, but just like in sNavbar, we
     * have a refresh() method so that we don't have front-end code calling
     * init().
     */
    public static refresh() {
        EditEntryForm.init();
    }

    /**
     * Hide the EditEntryForm.  Be sure to clear its fields first
     */
    private static hide() {
        console.log("Hide form");
        $("#" + EditEntryForm.NAME + "-title").val("");
        $("#" + EditEntryForm.NAME + "-message").val("");
        $("#" + EditEntryForm.NAME).modal("hide");
    }

    /**
     * Show the EditEntryForm.  Be sure to clear its fields, because there are
     * ways of making a Bootstrap modal disapper without clicking Close, and
     * we haven't set up the hooks to clear the fields on the events associated
     * with those ways of making the modal disappear.
     */
    public static show() {
        console.log("show is called");
        $("#" + EditEntryForm.NAME + "-title").val("")
        $("#" + EditEntryForm.NAME + "-message").val("");
        $("#" + EditEntryForm.NAME + "-id").val();
        $("#" + EditEntryForm.NAME).modal("show");
    }

    public static editMsg(data: any) {
        EditEntryForm.show();
        console.log("editMsg show was called");
        $("#" + EditEntryForm.NAME + "-title").val(data.mData.mTitle);
        $("#" + EditEntryForm.NAME + "-message").val(data.mData.mMessage);
        $("#" + EditEntryForm.NAME + "-id").val(data.mData.mId);
        $("#" + EditEntryForm.NAME).modal("show");
        console.log("Title: "+data.mData.mTitle + "\nContent: "+data.mData.mMessage);
    }
   /**
    * Check if the input fields are both valid, and if so, do an AJAX call.
    */
    public static submitForm() {
       // get the values of the two fields, force them to be strings, and check 
       // that neither is empty
       let title = "" + $("#" + EditEntryForm.NAME + "-title").val();
       let msg = "" + $("#" + EditEntryForm.NAME + "-message").val();
       
      // NB: we assume that the user didn't modify the value of #editId
      let id = "" + $("#" + EditEntryForm.NAME + "-id").val();
      console.log(title + "\n"+msg+"\nID: "+id);
      if (title === "" || msg === "") {
          window.alert("Error: title or message is not valid");
          return;
      }
      $("#" + EditEntryForm.NAME).modal("hide");
      
      // set up an AJAX post.  When the server replies, the result will go to
      // onSubmitResponse
      $.ajax({
          type: "PUT",
          url: "https://bmw-dba.herokuapp.com/buzz/" + id,
          dataType: "json",
          data: JSON.stringify({ mTitle: title, mMessage: msg }),
          success: EditEntryForm.onSubmitResponse
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
        ElementList.refresh();
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
} // end class EditEntryForm