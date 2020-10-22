  /**
   * ViewMessage encapsulates all of the code for the form for editing an entry
  */
 class ViewMessage {
    /**
         * The name of the DOM entry associated with ViewMessage
         */
        private static readonly NAME = "ViewMessage";
    
    
        /**
         * Track if the Singleton has been initialized
         */
        private static isInit = false;
    
        /**
         * Initialize the ViewMessage by creating its element in the DOM and 
         * configuring its buttons.  This needs to be called from any public static 
         * method, to ensure that the Singleton is initialized before use
         */
        private static init() {
            if (!ViewMessage.isInit) {
                $("body").append(Handlebars.templates[ViewMessage.NAME + ".hb"]());
                //$("#" + ViewMessage.NAME + "-Close").on("click", function() { ViewMessage.hide(); });
                $("#" + ViewMessage.NAME + "-comment").on("click", function() { ViewMessage.clickComment(); });
                ViewMessage.isInit = true;
            }
        }
    
        /**
         * Refresh() doesn't really have much meaning, but just like in sNavbar, we
         * have a refresh() method so that we don't have front-end code calling
         * init().
         */
        public static refresh() {
            ViewMessage.init();
            let id = "" + $("#" + ViewMessage.NAME + "-id").val();

        }

        private static update(data: any) {
            // Remove the table of data, if it exists
            $("#" + ViewMessage.NAME).remove();
            // Use a template to re-generate the table, and then insert it
            $("body").append(Handlebars.templates[ViewMessage.NAME + ".hb"](data));
            // To be able to click on a thread and see it expand (To be done in a later phase)
            //$(".title").click(ElementList.clickView);
            // Find all of the delete buttons, and set their behavior
            $("#" + ViewMessage.NAME + "-comment").on("click", function() { ViewMessage.clickComment(); });
        }
    
        private static clickEdit() {
            // as in clickDelete, we need the ID of the row
            console.log("Edit was clicked");
            ViewMessage.hide();
            let id = $(this).data("value");
            $.ajax({
                type: "GET",
                url: "https://bmw-dba.herokuapp.com/buzz/" + id,
                dataType: "json",
                success: EditEntryForm.editMsg
            });
        }
    
        /**
         * Hide the ViewMessage.  Be sure to clear its fields first
         */
        private static hide() {
            console.log("Hide form");
            $("#" + ViewMessage.NAME + "-title").val("");
            $("#" + ViewMessage.NAME + "-message").val("");
            $("#" + ViewMessage.NAME).modal("hide");
            $('#popupDelete').modal('toggle');
        }
    
        /**
         * Show the ViewMessage.  Be sure to clear its fields, because there are
         * ways of making a Bootstrap modal disapper without clicking Close, and
         * we haven't set up the hooks to clear the fields on the events associated
         * with those ways of making the modal disappear.
         */
        public static show() {
            console.log("show is called");
            $("#" + ViewMessage.NAME + "-title").val("")
            $("#" + ViewMessage.NAME + "-message").val("");
            $("#" + ViewMessage.NAME + "-id").val();
            $("#" + ViewMessage.NAME).modal("show");
        }
    
        public static ViewMsg(data: any) {
            ViewMessage.show();
            console.log("ViewMsg show was called");
            $("#" + ViewMessage.NAME + "-title").val(data.mData.mTitle);
            $("#" + ViewMessage.NAME + "-message").val(data.mData.mMessage);
            $("#" + ViewMessage.NAME + "-id").val(data.mData.mId);
            $("#" + ViewMessage.NAME).modal("show");
            console.log("Title: "+data.mData.mTitle + "\nContent: "+data.mData.mMessage);


        }
        
        private static clickComment() {
            // as in clickDelete, we need the ID of the row
            console.log("View was clicked");
            ViewMessage.hide();
            let id = "" + $("#" + ViewMessage.NAME + "-id").val();
            console.log("https://bmw-dba.herokuapp.com/buzz/" + id +"/comment");
            console.log("id.tostring: "+id.toString());

            localStorage.setItem("id", id);

            console.log("local storage: "+localStorage["key"]);
            $.ajax({
                type: "GET",
                url: "https://bmw-dba.herokuapp.com/buzz/" + id +"/comment",
                dataType: "json",
                success: ViewComment.updateComment
            }); 
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