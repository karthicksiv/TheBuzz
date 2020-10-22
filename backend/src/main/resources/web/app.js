"use strict";
/**
 * EditEntryForm encapsulates all of the code for the form for editing an entry
*/
var EditEntryForm = /** @class */ (function () {
    function EditEntryForm() {
    }
    /**
     * Initialize the EditEntryForm by creating its element in the DOM and
     * configuring its buttons.  This needs to be called from any public static
     * method, to ensure that the Singleton is initialized before use
     */
    EditEntryForm.init = function () {
        if (!EditEntryForm.isInit) {
            $("body").append(Handlebars.templates[EditEntryForm.NAME + ".hb"]());
            $("#" + EditEntryForm.NAME + "-OK").on("click", function () { EditEntryForm.submitForm(); });
            $("#" + EditEntryForm.NAME + "-Close").on("click", function () { EditEntryForm.hide(); });
            EditEntryForm.isInit = true;
        }
    };
    /**
     * Refresh() doesn't really have much meaning, but just like in sNavbar, we
     * have a refresh() method so that we don't have front-end code calling
     * init().
     */
    EditEntryForm.refresh = function () {
        EditEntryForm.init();
    };
    /**
     * Hide the EditEntryForm.  Be sure to clear its fields first
     */
    EditEntryForm.hide = function () {
        console.log("Hide form");
        $("#" + EditEntryForm.NAME + "-title").val("");
        $("#" + EditEntryForm.NAME + "-message").val("");
        $("#" + EditEntryForm.NAME).modal("hide");
    };
    /**
     * Show the EditEntryForm.  Be sure to clear its fields, because there are
     * ways of making a Bootstrap modal disapper without clicking Close, and
     * we haven't set up the hooks to clear the fields on the events associated
     * with those ways of making the modal disappear.
     */
    EditEntryForm.show = function () {
        console.log("show is called");
        $("#" + EditEntryForm.NAME + "-title").val("");
        $("#" + EditEntryForm.NAME + "-message").val("");
        $("#" + EditEntryForm.NAME + "-id").val();
        $("#" + EditEntryForm.NAME).modal("show");
    };
    EditEntryForm.editMsg = function (data) {
        EditEntryForm.show();
        console.log("editMsg show was called");
        $("#" + EditEntryForm.NAME + "-title").val(data.mData.mTitle);
        $("#" + EditEntryForm.NAME + "-message").val(data.mData.mMessage);
        $("#" + EditEntryForm.NAME + "-id").val(data.mData.mId);
        $("#" + EditEntryForm.NAME).modal("show");
        console.log("Title: " + data.mData.mTitle + "\nContent: " + data.mData.mMessage);
    };
    /**
     * Check if the input fields are both valid, and if so, do an AJAX call.
     */
    EditEntryForm.submitForm = function () {
        // get the values of the two fields, force them to be strings, and check 
        // that neither is empty
        var title = "" + $("#" + EditEntryForm.NAME + "-title").val();
        var msg = "" + $("#" + EditEntryForm.NAME + "-message").val();
        // NB: we assume that the user didn't modify the value of #editId
        var id = "" + $("#" + EditEntryForm.NAME + "-id").val();
        console.log(title + "\n" + msg + "\nID: " + id);
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
    };
    /**
     * onSubmitResponse runs when the AJAX call in submitForm() returns a
     * result.
     *
     * @param data The object returned by the server
     */
    EditEntryForm.onSubmitResponse = function (data) {
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
    };
    /**
         * The name of the DOM entry associated with EditEntryForm
         */
    EditEntryForm.NAME = "EditEntryForm";
    /**
     * Track if the Singleton has been initialized
     */
    EditEntryForm.isInit = false;
    return EditEntryForm;
}()); // end class EditEntryForm
/**
 * NewEntryForm encapsulates all of the code for the form for adding an entry
 */
var NewEntryForm = /** @class */ (function () {
    function NewEntryForm() {
    }
    /**
     * Initialize the NewEntryForm by creating its element in the DOM and
     * configuring its buttons.  This needs to be called from any public static
     * method, to ensure that the Singleton is initialized before use
     */
    NewEntryForm.init = function () {
        if (!NewEntryForm.isInit) {
            $("body").append(Handlebars.templates[NewEntryForm.NAME + ".hb"]());
            $("#" + NewEntryForm.NAME + "-OK").on("click", function () { NewEntryForm.submitForm(); });
            $("#" + NewEntryForm.NAME + "-Close").on("click", function () { NewEntryForm.hide(); });
            NewEntryForm.isInit = true;
        }
    };
    /**
     * Refresh() doesn't really have much meaning, but just like in sNavbar, we
     * have a refresh() method so that we don't have front-end code calling
     * init().
     */
    NewEntryForm.refresh = function () {
        NewEntryForm.init();
    };
    /**
     * Hide the NewEntryForm.  Be sure to clear its fields first
     */
    NewEntryForm.hide = function () {
        $("#" + NewEntryForm.NAME + "-title").val("");
        $("#" + NewEntryForm.NAME + "-message").val("");
        $("#" + NewEntryForm.NAME).modal("hide");
    };
    /**
     * Show the NewEntryForm.  Be sure to clear its fields, because there are
     * ways of making a Bootstrap modal disapper without clicking Close, and
     * we haven't set up the hooks to clear the fields on the events associated
     * with those ways of making the modal disappear.
     */
    NewEntryForm.show = function () {
        $("#" + NewEntryForm.NAME + "-title").val("");
        $("#" + NewEntryForm.NAME + "-message").val("");
        $("#" + NewEntryForm.NAME).modal("show");
        console.log(SignIn.getSession());
    };
    /**
     * Send data to submit the form only if the fields are both valid.
     * Immediately hide the form when we send data, so that the user knows that
     * their click was received.
     */
    NewEntryForm.submitForm = function () {
        // get the values of the two fields, force them to be strings, and check 
        // that neither is empty
        var title = "" + $("#" + NewEntryForm.NAME + "-title").val();
        var msg = "" + $("#" + NewEntryForm.NAME + "-message").val();
        if (title === "" || msg === "") {
            window.alert("Error: title or message is not valid");
            return;
        }
        NewEntryForm.hide();
        // set up an AJAX post.  When the server replies, the result will go to
        // onSubmitResponse
        $.ajax({
            type: "POST",
            url: "https://bmw-dba.herokuapp.com/buzz",
            dataType: "json",
            data: JSON.stringify({ mTitle: title, mMessage: msg }),
            success: NewEntryForm.onSubmitResponse,
            error: NewEntryForm.onError
        });
    };
    /**
     * onSubmitResponse runs when the AJAX call in submitForm() returns a
     * result.
     *
     * @param data The object returned by the server
     */
    NewEntryForm.onSubmitResponse = function (data) {
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
    };
    NewEntryForm.onError = function () {
        console.log("post didn't work");
    };
    /**
     * The name of the DOM entry associated with NewEntryForm
     */
    NewEntryForm.NAME = "NewEntryForm";
    /**
     * Track if the Singleton has been initialized
     */
    NewEntryForm.isInit = false;
    return NewEntryForm;
}());
/**
 * The ElementList Singleton provides a way of displaying all of the data
 * stored on the server as an HTML table.
 */
var ElementList = /** @class */ (function () {
    function ElementList() {
    }
    /**
     * Initialize the ElementList singleton.
     * This needs to be called from any public static method, to ensure that the
     * Singleton is initialized before use.
     */
    ElementList.init = function () {
        if (!ElementList.isInit) {
            ElementList.isInit = true;
        }
    };
    /**
     * refresh() is the public method for updating the ElementList
    */
    ElementList.refresh = function () {
        // Make sure the singleton is initialized
        ElementList.init();
        // Issue a GET, and then pass the result to update()
        $.ajax({
            type: "GET",
            url: "https://bmw-dba.herokuapp.com/buzz",
            dataType: "json",
            success: ElementList.update
        });
    };
    /**
     * update() is the private method used by refresh() to update the
     * ElementList
     */
    ElementList.update = function (data) {
        // Remove the table of data, if it exists
        $("#" + ElementList.NAME).remove();
        // Use a template to re-generate the table, and then insert it
        $("body").append(Handlebars.templates[ElementList.NAME + ".hb"](data));
        // To be able to click on a thread and see it expand (To be done in a later phase)
        //$(".title").click(ElementList.clickView);
        // Find all of the delete buttons, and set their behavior
        $("." + ElementList.NAME + "-delbtn").click(ElementList.clickDelete);
        // Find all of the Edit buttons, and set their behavior
        $("." + ElementList.NAME + "-editbtn").click(ElementList.clickEdit);
        // Find all of the Like buttons, and set their behavior
        $("." + ElementList.NAME + "-viewbtn").click(ElementList.clickView);
        $("." + ElementList.NAME + "-upbtn").click(VoteCounter.upVote);
        // Find all of the Dislike buttons, and set their behavior
        $("." + ElementList.NAME + "-downbtn").click(VoteCounter.downVote);
    };
    /**
     * clickDelete is the code we run in response to a click of a delete button
     */
    ElementList.clickDelete = function () {
        // for now, just print the ID that goes along with the data in the row
        // whose "delete" button was clicked
        var id = $(this).data("value");
        $.ajax({
            type: "DELETE",
            url: "https://bmw-dba.herokuapp.com/buzz/" + id,
            dataType: "json",
            // TODO: we should really have a function that looks at the return
            //       value and possibly prints an error message.
            success: ElementList.refresh
        });
    };
    /**
     * clickEdit is the code we run in response to a click of a edit button
     */
    ElementList.clickEdit = function () {
        // as in clickDelete, we need the ID of the row
        console.log("Edit was clicked");
        var id = $(this).data("value");
        $.ajax({
            type: "GET",
            url: "https://bmw-dba.herokuapp.com/buzz/" + id,
            dataType: "json",
            success: EditEntryForm.editMsg
        });
    };
    ElementList.clickView = function () {
        // as in clickDelete, we need the ID of the row
        console.log("View was clicked");
        var id = $(this).data("value");
        $.ajax({
            type: "GET",
            url: "https://bmw-dba.herokuapp.com/buzz/" + id,
            dataType: "json",
            success: ViewMessage.ViewMsg
        });
    };
    /**
     * The name of the DOM entry associated with ElementList
     */
    ElementList.NAME = "ElementList";
    /**
     * Track if the Singleton has been initialized
     */
    ElementList.isInit = false;
    return ElementList;
}());
/**
 * The Navbar Singleton is the navigation bar at the top of the page.  Through
 * its HTML, it is designed so that clicking the "brand" part will refresh the
 * page.  Apart from that, it has an "add" button, which forwards to
 * NewEntryForm
 */
var Navbar = /** @class */ (function () {
    function Navbar() {
    }
    /**
     * Initialize the Navbar Singleton by creating its element in the DOM and
     * configuring its button.  This needs to be called from any public static
     * method, to ensure that the Singleton is initialized before use.
     */
    Navbar.init = function () {
        if (!Navbar.isInit) {
            $("body").prepend(Handlebars.templates[Navbar.NAME + ".hb"]());
            $("#" + Navbar.NAME + "-add").on("click", function () { NewEntryForm.show(); });
            $("#" + Navbar.NAME + "-signin").on("click", function () { SignIn.show(); });
            $("#" + Navbar.NAME + "-gensearch").on("click", function () { Navbar.postsearch(); });
            $("#" + Navbar.NAME + "-title").on("click", function () { Navbar.posttitle(); });
            $("#" + Navbar.NAME + "-message").on("click", function () { Navbar.postmess(); });
            $("#" + Navbar.NAME + "-author").on("click", function () { Navbar.postauth(); });
            $("#" + Navbar.NAME + "-date").on("click", function () { Navbar.postdate(); });
            $("#" + Navbar.NAME + "-profile").on("click", function () { Navbar.postprofile(); });
            Navbar.isInit = true;
        }
    };
    /**
     * Search by title and message (general search)
     */
    Navbar.postsearch = function () {
        $.ajax({
            type: "GET",
            url: "https://bmw-dba.herokuapp.com/buzz/search_TM/" + $("#" + Navbar.NAME + "-search").val(),
            dataType: "json",
            success: ElementList.update
        });
        //alert($("#"+Navbar.NAME+"-search").val());
    };
    /**
     * Search by title
     */
    Navbar.posttitle = function () {
        $.ajax({
            type: "GET",
            url: "https://bmw-dba.herokuapp.com/buzz/search_title/" + $("#" + Navbar.NAME + "-search").val(),
            dataType: "json",
            success: ElementList.update
        });
        //alert($("#"+Navbar.NAME+"-search").val());
    };
    /**
     * Search by message
     */
    Navbar.postmess = function () {
        $.ajax({
            type: "GET",
            url: "https://bmw-dba.herokuapp.com/buzz/search_message/" + $("#" + Navbar.NAME + "-search").val(),
            dataType: "json",
            success: ElementList.update
        });
        //alert($("#"+Navbar.NAME+"-search").val());
    };
    /**
     * Search by user
     */
    Navbar.postprofile = function () {
        $.ajax({
            type: "GET",
            url: "https://bmw-dba.herokuapp.com/buzz/" + $("#" + Navbar.NAME + "-search").val() + "/profile",
            dataType: "json",
            success: ElementList.update
        });
        //alert($("#"+Navbar.NAME+"-search").val());
    };
    /**
     * Search by date YYYY-MM-DD
     */
    Navbar.postdate = function () {
        $.ajax({
            type: "GET",
            url: "https://bmw-dba.herokuapp.com/buzz/search_date/" + $("#" + Navbar.NAME + "-search").val(),
            dataType: "json",
            success: ElementList.update
        });
        //alert($("#"+Navbar.NAME+"-search").val());
    };
    /**
     * Search for author
     */
    Navbar.postauth = function () {
        $.ajax({
            type: "GET",
            url: "https://bmw-dba.herokuapp.com/buzz/search_author/" + $("#" + Navbar.NAME + "-search").val(),
            dataType: "json",
            success: ElementList.update
        });
        //alert($("#"+Navbar.NAME+"-search").val());
    };
    Navbar.onSearchResult = function (data) {
        console.log("onSearchResult ", data);
    };
    /**
     * Refresh() doesn't really have much meaning for the navbar, but we'd
     * rather not have anyone call init(), so we'll have this as a stub that
     * can be called during front-end initialization to ensure the navbar
     * is configured.
     */
    Navbar.refresh = function () {
        Navbar.init();
    };
    /**
     * Track if the Singleton has been initialized
     */
    Navbar.isInit = false;
    /**
     * The name of the DOM entry associated with Navbar
     */
    Navbar.NAME = "Navbar";
    return Navbar;
}());
/**
 * The VoteCounter Singleton is the navigation bar at the top of the page.  Through
 * its HTML, it is designed so that clicking the "brand" part will refresh the
 * page.  Apart from that, it has an "add" button, which forwards to
 * NewEntryForm
 */
var VoteCounter = /** @class */ (function () {
    function VoteCounter() {
    }
    // private static wasUpped = false;
    // private static wasDowned = false;
    /**
     * Initialize the VoteCounter Singleton by creating its element in the DOM and
     * configuring its button.  This needs to be called from any public static
     * method, to ensure that the Singleton is initialized before use.
     */
    VoteCounter.init = function () {
        if (!VoteCounter.isInit) {
            $(".ElementList-upbtn").on("click", function () { VoteCounter.upVote(); });
            $(".ElementList-downbtn").on("click", function () { VoteCounter.downVote(); });
            VoteCounter.isInit = true;
        }
    };
    VoteCounter.upVote = function () {
        var id = $(this).data("value");
        VoteCounter.increment(id);
    };
    VoteCounter.downVote = function () {
        var id = $(this).data("value");
        VoteCounter.decrement(id);
    };
    VoteCounter.decrement = function (id) {
        $.ajax({
            type: "PUT",
            url: "https://bmw-dba.herokuapp.com/buzz/" + id + "/downvote",
            success: VoteCounter.refresh
        });
    };
    VoteCounter.increment = function (id) {
        $.ajax({
            type: "PUT",
            url: "https://bmw-dba.herokuapp.com/buzz/" + id + "/upvote",
            success: VoteCounter.refresh
        });
    };
    // window.location.reload();
    /**
     * Refresh() doesn't really have much meaning for the VoteCounter, but we'd
     * rather not have anyone call init(), so we'll have this as a stub that
     * can be called during front-end initialization to ensure the VoteCounter
     * is configured.
     */
    VoteCounter.refresh = function () {
        if (VoteCounter.isInit) {
            window.location.reload();
        }
        VoteCounter.init();
    };
    /**
     * Track if the Singleton has been initialized
     */
    VoteCounter.isInit = false;
    return VoteCounter;
}());
/**
 * NewEntryForm encapsulates all of the code for the form for adding an entry
 */
var SignIn = /** @class */ (function () {
    function SignIn() {
    }
    /**
     * Initialize the NewEntryForm by creating its element in the DOM and
     * configuring its buttons.  This needs to be called from any public static
     * method, to ensure that the Singleton is initialized before use
     */
    SignIn.init = function () {
        if (!SignIn.isInit) {
            $("body").append(Handlebars.templates[SignIn.NAME + ".hb"]());
            $("#" + SignIn.NAME + "-Close").on("click", function () { SignIn.hide(); });
            //$("#" + SignIn.NAME + "-signout").on("click", function() { SignIn.signOut(); });
            SignIn.isInit = true;
        }
    };
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
    SignIn.refresh = function () {
        SignIn.init();
    };
    /**
     * Hide the NewEntryForm.  Be sure to clear its fields first
     */
    SignIn.hide = function () {
        //$("#" + NewEntryForm.NAME + "-title").val("");
        //$("#" + NewEntryForm.NAME + "-message").val("");
        $("#" + "g-signin2");
        $("#" + SignIn.NAME).modal("hide");
    };
    /**
     * Show the NewEntryForm.  Be sure to clear its fields, because there are
     * ways of making a Bootstrap modal disapper without clicking Close, and
     * we haven't set up the hooks to clear the fields on the events associated
     * with those ways of making the modal disappear.
     */
    SignIn.show = function () {
        //$("#" + NewEntryForm.NAME + "-title").val("");
        //$("#" + NewEntryForm.NAME + "-message").val("");
        $("#" + "g-signin2");
        $("#" + SignIn.NAME).modal("show");
    };
    SignIn.getSession = function () {
        var session = $('h1').text();
        return session;
    };
    /**
     * The name of the DOM entry associated with NewEntryForm
     */
    SignIn.NAME = "SignIn";
    /**
     * Track if the Singleton has been initialized
     */
    SignIn.isInit = false;
    return SignIn;
}());
/**
 * ViewMessage encapsulates all of the code for the form for editing an entry
*/
var ViewMessage = /** @class */ (function () {
    function ViewMessage() {
    }
    /**
     * Initialize the ViewMessage by creating its element in the DOM and
     * configuring its buttons.  This needs to be called from any public static
     * method, to ensure that the Singleton is initialized before use
     */
    ViewMessage.init = function () {
        if (!ViewMessage.isInit) {
            $("body").append(Handlebars.templates[ViewMessage.NAME + ".hb"]());
            //$("#" + ViewMessage.NAME + "-Close").on("click", function() { ViewMessage.hide(); });
            $("#" + ViewMessage.NAME + "-comment").on("click", function () { ViewMessage.clickComment(); });
            ViewMessage.isInit = true;
        }
    };
    /**
     * Refresh() doesn't really have much meaning, but just like in sNavbar, we
     * have a refresh() method so that we don't have front-end code calling
     * init().
     */
    ViewMessage.refresh = function () {
        ViewMessage.init();
        var id = "" + $("#" + ViewMessage.NAME + "-id").val();
    };
    ViewMessage.update = function (data) {
        // Remove the table of data, if it exists
        $("#" + ViewMessage.NAME).remove();
        // Use a template to re-generate the table, and then insert it
        $("body").append(Handlebars.templates[ViewMessage.NAME + ".hb"](data));
        // To be able to click on a thread and see it expand (To be done in a later phase)
        //$(".title").click(ElementList.clickView);
        // Find all of the delete buttons, and set their behavior
        $("#" + ViewMessage.NAME + "-comment").on("click", function () { ViewMessage.clickComment(); });
    };
    ViewMessage.clickEdit = function () {
        // as in clickDelete, we need the ID of the row
        console.log("Edit was clicked");
        ViewMessage.hide();
        var id = $(this).data("value");
        $.ajax({
            type: "GET",
            url: "https://bmw-dba.herokuapp.com/buzz/" + id,
            dataType: "json",
            success: EditEntryForm.editMsg
        });
    };
    /**
     * Hide the ViewMessage.  Be sure to clear its fields first
     */
    ViewMessage.hide = function () {
        console.log("Hide form");
        $("#" + ViewMessage.NAME + "-title").val("");
        $("#" + ViewMessage.NAME + "-message").val("");
        $("#" + ViewMessage.NAME).modal("hide");
        $('#popupDelete').modal('toggle');
    };
    /**
     * Show the ViewMessage.  Be sure to clear its fields, because there are
     * ways of making a Bootstrap modal disapper without clicking Close, and
     * we haven't set up the hooks to clear the fields on the events associated
     * with those ways of making the modal disappear.
     */
    ViewMessage.show = function () {
        console.log("show is called");
        $("#" + ViewMessage.NAME + "-title").val("");
        $("#" + ViewMessage.NAME + "-message").val("");
        $("#" + ViewMessage.NAME + "-id").val();
        $("#" + ViewMessage.NAME).modal("show");
    };
    ViewMessage.ViewMsg = function (data) {
        ViewMessage.show();
        console.log("ViewMsg show was called");
        $("#" + ViewMessage.NAME + "-title").val(data.mData.mTitle);
        $("#" + ViewMessage.NAME + "-message").val(data.mData.mMessage);
        $("#" + ViewMessage.NAME + "-id").val(data.mData.mId);
        $("#" + ViewMessage.NAME).modal("show");
        console.log("Title: " + data.mData.mTitle + "\nContent: " + data.mData.mMessage);
    };
    ViewMessage.clickComment = function () {
        // as in clickDelete, we need the ID of the row
        console.log("View was clicked");
        ViewMessage.hide();
        var id = "" + $("#" + ViewMessage.NAME + "-id").val();
        console.log("https://bmw-dba.herokuapp.com/buzz/" + id + "/comment");
        console.log("id.tostring: " + id.toString());
        localStorage.setItem("id", id);
        console.log("local storage: " + localStorage["key"]);
        $.ajax({
            type: "GET",
            url: "https://bmw-dba.herokuapp.com/buzz/" + id + "/comment",
            dataType: "json",
            success: ViewComment.updateComment
        });
    };
    /**
         * The name of the DOM entry associated with ViewMessage
         */
    ViewMessage.NAME = "ViewMessage";
    /**
     * Track if the Singleton has been initialized
     */
    ViewMessage.isInit = false;
    return ViewMessage;
}());
var ViewComment = /** @class */ (function () {
    function ViewComment() {
    }
    /**
     * Initialize the ViewComment by creating its element in the DOM and
     * configuring its buttons.  This needs to be called from any public static
     * method, to ensure that the Singleton is initialized before use
     */
    ViewComment.init = function () {
        if (!ViewComment.isInit) {
            $("body").append(Handlebars.templates[ViewComment.NAME + ".hb"]());
            $("#" + ViewComment.NAME + "-commentPost").on("click", function () { ViewComment.submitForm(); });
            //$("#" + ViewComment.NAME + "-Close").on("click", function() { ViewComment.hide(); });
            ViewComment.isInit = true;
        }
    };
    /**
     * Refresh() doesn't really have much meaning, but just like in sNavbar, we
     * have a refresh() method so that we don't have front-end code calling
     * init().
     */
    ViewComment.refresh = function () {
        ViewComment.init();
        var id = localStorage.getItem("id");
        $.ajax({
            type: "GET",
            url: "https://bmw-dba.herokuapp.com/buzz/" + id + "/comment",
            dataType: "json",
            success: ViewComment.updateComment
        });
    };
    ViewComment.update = function (data) {
        // Remove the table of data, if it exists
        $("#" + ViewComment.NAME).remove();
        // Use a template to re-generate the table, and then insert it
        ViewComment.refresh();
        // To be able to click on a thread and see it expand (To be done in a later phase)
        //$(".title").click(ElementList.clickView);
        // Find all of the delete buttons, and set their behavior
    };
    /**
     * Hide the ViewComment.  Be sure to clear its fields first
     */
    ViewComment.hide = function () {
        console.log("Hide form");
        $("#" + ViewComment.NAME + "-uId").val("");
        $("#" + ViewComment.NAME + "-cMessage").val("");
        $("#" + ViewComment.NAME + "-cId").val();
        $("#" + ViewComment.NAME + "-message").val("");
        $("#comments tr").remove();
        $("#" + ViewComment.NAME).modal("hide");
        $('#popupDelete').modal('toggle');
    };
    ViewComment.submitForm = function () {
        // get the values of the two fields, force them to be strings, and check 
        // that neither is empty
        var msg = "" + $("#" + ViewComment.NAME + "-message").val();
        if (msg === "") {
            window.alert("Error: title or message is not valid");
            return;
        }
        var id = localStorage.getItem("id");
        // set up an AJAX post.  When the server replies, the result will go to
        console.log("getitem: " + id);
        // onSubmitResponse
        $.ajax({
            type: "POST",
            url: "https://bmw-dba.herokuapp.com/buzz/" + id + "/comment",
            dataType: "json",
            data: JSON.stringify({ cMessage: msg }),
            success: ViewComment.onSubmitResponse,
            error: ViewComment.onError
        });
    };
    /**
     * onSubmitResponse runs when the AJAX call in submitForm() returns a
     * result.
     *
     * @param data The object returned by the server
     */
    ViewComment.onSubmitResponse = function (data) {
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
    };
    ViewComment.onError = function () {
        console.log("post didn't work");
    };
    /**
     * Show the ViewComment.  Be sure to clear its fields, because there are
     * ways of making a Bootstrap modal disapper without clicking Close, and
     * we haven't set up the hooks to clear the fields on the events associated
     * with those ways of making the modal disappear.
     */
    ViewComment.show = function () {
        console.log("show is called");
        $("#comments tr").remove();
        $("#" + ViewComment.NAME + "-uId").val("");
        $("#" + ViewComment.NAME + "-cMessage").val("");
        $("#" + ViewComment.NAME + "-cId").val();
        $("#" + ViewComment.NAME + "-message").val("");
        $("#" + ViewComment.NAME).modal("show");
    };
    ViewComment.updateComment = function (data) {
        console.log("update comment called");
        ViewComment.show();
        var tableRef = document.getElementById('comments');
        //$("body").append(Handlebars.templates[ViewComment.NAME + ".hb"](data));
        for (var i = 0; i < data.mData.length; i++) {
            var row = tableRef.insertRow(0);
            var cMessage = row.insertCell(0);
            var uId = row.insertCell(0);
            console.log("comment" + data.mData[i].cMessage);
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
    };
    /**
         * The name of the DOM entry associated with
         */
    ViewComment.NAME = "ViewComment";
    /**
     * Track if the Singleton has been initialized
     */
    ViewComment.isInit = false;
    return ViewComment;
}());
/// <reference path="ts/EditEntryForm.ts"/>
/// <reference path="ts/NewEntryForm.ts"/>
/// <reference path="ts/ElementList.ts"/>
/// <reference path="ts/Navbar.ts"/>
/// <reference path="ts/VoteCounter.ts"/>
/// <reference path="ts/SignIn.ts"/>
/// <reference path="ts/ViewMessage.ts"/>
/// <reference path="ts/ViewComment.ts"/>
// / <reference path="ts/ViewMessage.ts"/>
// Prevent compiler errors when using Handlebars
var Handlebars;
// Run some configuration code when the web page loads
$(document).ready(function () {
    Navbar.refresh();
    NewEntryForm.refresh();
    ElementList.refresh();
    VoteCounter.refresh();
    // ViewMessage.refresh();
    SignIn.refresh();
    ViewMessage.refresh();
    ViewComment.refresh();
    // // Create the object that controls the "Edit Entry" form
    // editEntryForm = new EditEntryForm();
    // // set up initial UI state
    // $("#editElement").hide();
    EditEntryForm.refresh();
});
