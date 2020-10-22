<div id="SignIn" class="modal fade" role="dialog">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title">My Profile</h4>
                <meta name="google-signin-scope" content="profile email">
                <meta name="google-signin-client_id" content="433710337180-kb5utql63enl0kp57lmsfdvl1f97ciuj.apps.googleusercontent.com">
                <script src="https://apis.google.com/js/platform.js" async defer></script>
            </div>
            <div class="modal-body">
                <div class="g-signin2" data-onsuccess="onSignIn" data-theme="dark"></div>
                <div class="user">
                    <p>Profile Details</p>
                    <img id="pic" class="img-circle" width="100" height="100"/>
                    <p>
                    </p>
                    <p>Email Address</p>
                    <p id="email" class="alert alert-danger"></p>
                    <p>
                    </p>
                    <p>Name</p>
                    <p id="name" class="alert alert-danger"></p>
                    
                    <h1 hidden id="session" class="alert alert-danger" data-val="{{this.session}}"></h1>

                    <button onclick="signOut()" class="btn btn-danger">Sign Out</button>

                <script>
                    function onSignIn(googleUser) {
                    // Useful data for your client-side scripts:
                        var profile = googleUser.getBasicProfile();
                        $(".g-signin2").css("display","none");
                        $(".user").css("display","block");
                        $("#pic").attr('src',profile.getImageUrl());
                        $("#email").text(profile.getEmail());
                        $("#name").text(profile.getName());
                        
                        // The ID token you need to pass to your backend:
                        var id_token = googleUser.getAuthResponse().id_token;
                        console.log("ID Token: " + id_token);

                        $.ajax({
                            type: "POST",
                            url: "https://bmw-dba.herokuapp.com/auth",
                            dataType: "json",
                            data: JSON.stringify({ token: id_token }),
                            success: function(output){
                                session = output.mData.session;
                                console.log(session);
                                $("#session").text(session);
                            },
                            error:  function(){alert('Sign-In Failure')}
                        });                   

                    }
                    
                    function signOut()
                    {

                        var auth2 = gapi.auth2.getAuthInstance();
                        auth2.signOut().then(function() {
                            console.log("successful signout");
                            $(".g-signin2").css("display","block");
                            $(".user").css("display","none");
                        });

                    }

                    
                </script>
                
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" id="SignIn-Close">Close</button>
            </div>
        </div>
    </div>
</div>