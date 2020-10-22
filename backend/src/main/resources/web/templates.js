(function() {
  var template = Handlebars.template, templates = Handlebars.templates = Handlebars.templates || {};
templates['ElementList.hb'] = template({"1":function(container,depth0,helpers,partials,data) {
    var alias1=container.lambda, alias2=container.escapeExpression, lookupProperty = container.lookupProperty || function(parent, propertyName) {
        if (Object.prototype.hasOwnProperty.call(parent, propertyName)) {
          return parent[propertyName];
        }
        return undefined
    };

  return "            <tr>\n                <td>"
    + alias2(alias1((depth0 != null ? lookupProperty(depth0,"mTitle") : depth0), depth0))
    + "</td>\n                <td><button class=\"ElementList-upbtn\" data-value=\""
    + alias2(alias1((depth0 != null ? lookupProperty(depth0,"mId") : depth0), depth0))
    + "\">Like</button></td>\n                <td>"
    + alias2(alias1((depth0 != null ? lookupProperty(depth0,"mVote") : depth0), depth0))
    + "</td>\n                <td><button class=\"ElementList-downbtn\" data-value=\""
    + alias2(alias1((depth0 != null ? lookupProperty(depth0,"mId") : depth0), depth0))
    + "\">Dislike</button></td>\n                <td><button class=\"ElementList-editbtn\" data-value=\""
    + alias2(alias1((depth0 != null ? lookupProperty(depth0,"mId") : depth0), depth0))
    + "\" type=\"button\">Edit</button></td>\n                <td><button class=\"ElementList-delbtn\" data-value=\""
    + alias2(alias1((depth0 != null ? lookupProperty(depth0,"mId") : depth0), depth0))
    + "\">Delete</button></td>\n                <td><button class=\"ElementList-viewbtn\" data-value=\""
    + alias2(alias1((depth0 != null ? lookupProperty(depth0,"mId") : depth0), depth0))
    + "\">View</button></td>\n            </tr>\n";
},"compiler":[8,">= 4.3.0"],"main":function(container,depth0,helpers,partials,data) {
    var stack1, lookupProperty = container.lookupProperty || function(parent, propertyName) {
        if (Object.prototype.hasOwnProperty.call(parent, propertyName)) {
          return parent[propertyName];
        }
        return undefined
    };

  return "<div class=\"panel panel-default\" id=\"ElementList\">\n    <div class=\"panel-heading\">\n        <h3 class=\"panel-title\">All Posts</h3>\n    </div>\n    <table class=\"table\">\n        <tbody>\n"
    + ((stack1 = lookupProperty(helpers,"each").call(depth0 != null ? depth0 : (container.nullContext || {}),(depth0 != null ? lookupProperty(depth0,"mData") : depth0),{"name":"each","hash":{},"fn":container.program(1, data, 0),"inverse":container.noop,"data":data,"loc":{"start":{"line":7,"column":12},"end":{"line":17,"column":21}}})) != null ? stack1 : "")
    + "        </tbody>\n    </table>\n</div>\n";
},"useData":true});
})();
(function() {
  var template = Handlebars.template, templates = Handlebars.templates = Handlebars.templates || {};
templates['EditEntryForm.hb'] = template({"compiler":[8,">= 4.3.0"],"main":function(container,depth0,helpers,partials,data) {
    return "<div id=\"EditEntryForm\" class=\"modal fade\" role=\"dialog\">\n    <div class=\"modal-dialog\">\n        <div class=\"modal-content\">\n            <div class=\"modal-header\">\n                <h4 class=\"modal-title\">Edit an Entry</h4>\n            </div>\n            <div class=\"modal-body\">\n                <label for=\"EditEntryForm-title\">Title</label>\n                <input class=\"form-control\" type=\"text\" id=\"EditEntryForm-title\"/>\n                <label for=\"EditEntryForm-message\">Message</label>\n                <textarea class=\"form-control\" id=\"EditEntryForm-message\"></textarea>\n                <input type=\"hidden\" id=\"EditEntryForm-id\">\n            </div>\n            <div class=\"modal-footer\">\n                <button type=\"button\" class=\"btn btn-default\" id=\"EditEntryForm-OK\">OK</button>\n                <button type=\"button\" class=\"btn btn-default\" id=\"EditEntryForm-Close\">Close</button>\n            </div>\n        </div>\n    </div>\n</div>";
},"useData":true});
})();
(function() {
  var template = Handlebars.template, templates = Handlebars.templates = Handlebars.templates || {};
templates['NewEntryForm.hb'] = template({"compiler":[8,">= 4.3.0"],"main":function(container,depth0,helpers,partials,data) {
    return "<div id=\"NewEntryForm\" class=\"modal fade\" role=\"dialog\">\n    <div class=\"modal-dialog\">\n        <div class=\"modal-content\">\n            <div class=\"modal-header\">\n                <h4 class=\"modal-title\">Add a New Entry</h4>\n            </div>\n            <div class=\"modal-body\">\n                <label for=\"NewEntryForm-title\">Title</label>\n                <input class=\"form-control\" type=\"text\" id=\"NewEntryForm-title\" />\n                <label for=\"NewEntryForm-message\">Message</label>\n                <textarea class=\"form-control\" id=\"NewEntryForm-message\"></textarea>\n            </div>\n            <div class=\"modal-footer\">\n                <button type=\"button\" class=\"btn btn-default\" id=\"NewEntryForm-OK\">OK</button>\n                <button type=\"button\" class=\"btn btn-default\" id=\"NewEntryForm-Close\">Close</button>\n            </div>\n        </div>\n    </div>\n</div>\n";
},"useData":true});
})();
(function() {
  var template = Handlebars.template, templates = Handlebars.templates = Handlebars.templates || {};
templates['SignIn.hb'] = template({"compiler":[8,">= 4.3.0"],"main":function(container,depth0,helpers,partials,data) {
    var lookupProperty = container.lookupProperty || function(parent, propertyName) {
        if (Object.prototype.hasOwnProperty.call(parent, propertyName)) {
          return parent[propertyName];
        }
        return undefined
    };

  return "<div id=\"SignIn\" class=\"modal fade\" role=\"dialog\">\n    <div class=\"modal-dialog\">\n        <div class=\"modal-content\">\n            <div class=\"modal-header\">\n                <h4 class=\"modal-title\">My Profile</h4>\n                <meta name=\"google-signin-scope\" content=\"profile email\">\n                <meta name=\"google-signin-client_id\" content=\"433710337180-kb5utql63enl0kp57lmsfdvl1f97ciuj.apps.googleusercontent.com\">\n                <script src=\"https://apis.google.com/js/platform.js\" async defer></script>\n            </div>\n            <div class=\"modal-body\">\n                <div class=\"g-signin2\" data-onsuccess=\"onSignIn\" data-theme=\"dark\"></div>\n                <div class=\"user\">\n                    <p>Profile Details</p>\n                    <img id=\"pic\" class=\"img-circle\" width=\"100\" height=\"100\"/>\n                    <p>\n                    </p>\n                    <p>Email Address</p>\n                    <p id=\"email\" class=\"alert alert-danger\"></p>\n                    <p>\n                    </p>\n                    <p>Name</p>\n                    <p id=\"name\" class=\"alert alert-danger\"></p>\n                    \n                    <h1 hidden id=\"session\" class=\"alert alert-danger\" data-val=\""
    + container.escapeExpression(container.lambda((depth0 != null ? lookupProperty(depth0,"session") : depth0), depth0))
    + "\"></h1>\n\n                    <button onclick=\"signOut()\" class=\"btn btn-danger\">Sign Out</button>\n\n                <script>\n                    function onSignIn(googleUser) {\n                    // Useful data for your client-side scripts:\n                        var profile = googleUser.getBasicProfile();\n                        $(\".g-signin2\").css(\"display\",\"none\");\n                        $(\".user\").css(\"display\",\"block\");\n                        $(\"#pic\").attr('src',profile.getImageUrl());\n                        $(\"#email\").text(profile.getEmail());\n                        $(\"#name\").text(profile.getName());\n                        \n                        // The ID token you need to pass to your backend:\n                        var id_token = googleUser.getAuthResponse().id_token;\n                        console.log(\"ID Token: \" + id_token);\n\n                        $.ajax({\n                            type: \"POST\",\n                            url: \"https://bmw-dba.herokuapp.com/auth\",\n                            dataType: \"json\",\n                            data: JSON.stringify({ token: id_token }),\n                            success: function(output){\n                                session = output.mData.session;\n                                console.log(session);\n                                $(\"#session\").text(session);\n                            },\n                            error:  function(){alert('Sign-In Failure')}\n                        });                   \n\n                    }\n                    \n                    function signOut()\n                    {\n\n                        var auth2 = gapi.auth2.getAuthInstance();\n                        auth2.signOut().then(function() {\n                            console.log(\"successful signout\");\n                            $(\".g-signin2\").css(\"display\",\"block\");\n                            $(\".user\").css(\"display\",\"none\");\n                        });\n\n                    }\n\n                    \n                </script>\n                \n            </div>\n            <div class=\"modal-footer\">\n                <button type=\"button\" class=\"btn btn-default\" id=\"SignIn-Close\">Close</button>\n            </div>\n        </div>\n    </div>\n</div>";
},"useData":true});
})();
(function() {
  var template = Handlebars.template, templates = Handlebars.templates = Handlebars.templates || {};
templates['Navbar.hb'] = template({"compiler":[8,">= 4.3.0"],"main":function(container,depth0,helpers,partials,data) {
    return "<nav class=\"navbar navbar-default\">\n    <div class=\"container-fluid\">\n        <!-- Brand and toggle get grouped for better mobile display -->\n        <div class=\"navbar-header\">\n            <button type=\"button\" class=\"navbar-toggle collapsed\" data-toggle=\"collapse\" \n                data-target=\"#bs-example-navbar-collapse-1\" aria-expanded=\"false\">\n              <span class=\"sr-only\">Toggle navigation</span>\n              <span class=\"icon-bar\"></span>\n              <span class=\"icon-bar\"></span>\n              <span class=\"icon-bar\"></span>\n            </button>\n            <!-- Clicking the brand refreshes the page -->\n            <a class=\"navbar-brand\" href=\"/\">THE BUZZ</a>\n        </div>\n\n        <!-- Collect the nav links, forms, and other content for toggling -->\n        <div class=\"collapse navbar-collapse\" id=\"bs-example-navbar-collapse-1\">\n            <ul class=\"nav navbar-nav\">\n                <li>\n                    <a class=\"btn btn-link\" id=\"Navbar-add\">\n                        Add Entry\n                        <span class=\"glyphicon glyphicon-plus\"></span><span class=\"sr-only\">Show Trending Posts</span>\n                    </a>\n                </li>\n                <li>\n                    <a class=\"btn btn-link\" id=\"Navbar-signin\">\n                        My Profile\n                        <span class=\"glyphicon glyphicon-user\"></span><span class=\"sr-only\">Show Trending Posts</span>\n                    </a>\n                </li>\n                <li>\n                    <!--<a class=\"btn btn-link\" id=\"Navbar-searchbutton\">\n                        Search By\n                        <span class=\"glyphicon glyphicon-plus\"></span><span class=\"sr-only\">Show Trending Posts</span>\n                    </a> -->\n                    <input class=\"form-control\" type=\"text\" id=\"Navbar-search\" />\n                    <button type=\"button\" class=\"btn btn-default\" id=\"Navbar-gensearch\">Search</button> \n                    <button type=\"button\" class=\"btn btn-default\" id=\"Navbar-title\">Title</button> \n                    <button type=\"button\" class=\"btn btn-default\" id=\"Navbar-message\">Message</button> \n                    <button type=\"button\" class=\"btn btn-default\" id=\"Navbar-author\">Author</button> \n                    <button type=\"button\" class=\"btn btn-default\" id=\"Navbar-date\">Date</button> \n                    <button type=\"button\" class=\"btn btn-default\" id=\"Navbar-profile\">Profile</button> \n                </li>\n            </ul>\n        </div>\n    </div>\n</nav>";
},"useData":true});
})();
(function() {
  var template = Handlebars.template, templates = Handlebars.templates = Handlebars.templates || {};
templates['ViewMessage.hb'] = template({"compiler":[8,">= 4.3.0"],"main":function(container,depth0,helpers,partials,data) {
    var lookupProperty = container.lookupProperty || function(parent, propertyName) {
        if (Object.prototype.hasOwnProperty.call(parent, propertyName)) {
          return parent[propertyName];
        }
        return undefined
    };

  return "<div id=\"ViewMessage\" class=\"modal fade\" role=\"dialog\">\n    <div class=\"modal-dialog\">\n        <div class=\"modal-content\">\n            <div class=\"modal-header\">\n                <h4 class=\"modal-title\">Message</h4>\n            </div>\n            <div class=\"modal-body\">\n                <label for=\"ViewMessage-title\">Title</label>\n                <input class=\"form-control\" type=\"text\" id=\"ViewMessage-title\" readonly/>\n                <label for=\"ViewMessage-message\">Message</label>\n                <textarea class=\"form-control\" id=\"ViewMessage-message\" readonly></textarea>\n                <input type=\"hidden\" id=\"ViewMessage-id\">\n            </div>\n            <div class=\"modal-footer\">\n                <button class=\"btn btn-default\" id=\"ViewMessage-comment\" data-value=\""
    + container.escapeExpression(container.lambda((depth0 != null ? lookupProperty(depth0,"mId") : depth0), depth0))
    + "\">Comment</button>\n            </div>\n        </div>\n    </div>\n</div>";
},"useData":true});
})();
(function() {
  var template = Handlebars.template, templates = Handlebars.templates = Handlebars.templates || {};
templates['ViewComment.hb'] = template({"compiler":[8,">= 4.3.0"],"main":function(container,depth0,helpers,partials,data) {
    return "<div id=\"ViewComment\" class=\"modal fade\" role=\"dialog\">\n    <div class=\"modal-dialog\">\n        <div class=\"modal-content\">\n            <div class=\"modal-header\">\n                <h4 class=\"modal-title\">Comment</h4>\n            </div>\n            <div class=\"modal-body\">\n                <table class=\"table\"  id=\"comments\">\n                \n                </table>\n            </div>\n            <label for=\"ViewComment-message\">Post Comment</label>\n            <textarea class=\"form-control\" id=\"ViewComment-message\"></textarea>\n            <input type=\"hidden\" id=\"ViewComment-id\">\n        </div>\n\n        <div class=\"modal-footer\">\n            <button type=\"button\" class=\"btn btn-default\" id=\"ViewComment-commentPost\">Post Comment</button>\n        </div>\n    </div>\n</div>";
},"useData":true});
})();
