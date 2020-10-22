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
let Handlebars: any;

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
