/**
   * Profile encapsulates all of the code for the form for editing an entry
  */
 class Profile {
    /**
         * The name of the DOM entry associated with Profile
         */
        private static readonly NAME = "Profile";
    
    
        /**
         * Track if the Singleton has been initialized
         */
        private static isInit = false;
    
        /**
         * Initialize the Profile by creating its element in the DOM and 
         * configuring its buttons.  This needs to be called from any public static 
         * method, to ensure that the Singleton is initialized before use
         */
        private static init() {
            if (!Profile.isInit) {
                $("body").append(Handlebars.templates[Profile.NAME + ".hb"]());
                $("#" + Profile.NAME + "-Close").on("click", function() { Profile.hide(); });
                Profile.isInit = true;
            }
        }
    
        /**
         * Refresh() doesn't really have much meaning, but just like in sNavbar, we
         * have a refresh() method so that we don't have front-end code calling
         * init().
         */
        public static refresh() {
            Profile.init();

            $.ajax({
                type: "GET",
                url: "https://bmw-dba.herokuapp.com/buzz",
                dataType: "json",
                success: Profile.update
            });
        }

        private static update(data: any) {
            // Remove the table of data, if it exists
            $("#" + Profile.NAME).remove();
            // Use a template to re-generate the table, and then insert it
            $("body").append(Handlebars.templates[Profile.NAME + ".hb"](data));
            // To be able to click on a thread and see it expand (To be done in a later phase)
            //$(".title").click(ElementList.clickView);
            // Find all of the delete buttons, and set their behavior

        }
    
    
        /**
         * Hide the Profile.  Be sure to clear its fields first
         */
        private static hide() {
            console.log("Hide form");
            $("#" + Profile.NAME + "-username").val("");
            $("#" + Profile.NAME + "-email").val("");
            $("#" + Profile.NAME).modal("hide");
        }
    
        /**
         * Show the Profile.  Be sure to clear its fields, because there are
         * ways of making a Bootstrap modal disapper without clicking Close, and
         * we haven't set up the hooks to clear the fields on the events associated
         * with those ways of making the modal disappear.
         */
        public static show() {
            console.log("show is called");
            $("#" + Profile.NAME + "-username").val("")
            $("#" + Profile.NAME + "-email").val("");
            $("#" + Profile.NAME + "-id").val();
            $("#" + Profile.NAME).modal("show");
        }
    
        public static getProfile(data: any) {
            Profile.show();
            console.log("getProfile show was called");
            $("#" + Profile.NAME + "-username").val(data.mData.mTitle);
            $("#" + Profile.NAME + "-email").val(data.mData.mMessage);
            $("#" + Profile.NAME + "-id").val(data.mData.mId);
            $("#" + Profile.NAME).modal("show");
            console.log("username: "+data.mData.mTitle + "\nContent: "+data.mData.mMessage);
        }

       /**
        * Check if the input fields are both valid, and if so, do an AJAX call.
        */
    
       /**
        * onSubmitResponse runs when the AJAX call in submitForm() returns a 
        * result.
        * 
        * @param data The object returned by the server
        */
    }