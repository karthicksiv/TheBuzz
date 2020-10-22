<nav class="navbar navbar-default">
    <div class="container-fluid">
        <!-- Brand and toggle get grouped for better mobile display -->
        <div class="navbar-header">
            <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" 
                data-target="#bs-example-navbar-collapse-1" aria-expanded="false">
              <span class="sr-only">Toggle navigation</span>
              <span class="icon-bar"></span>
              <span class="icon-bar"></span>
              <span class="icon-bar"></span>
            </button>
            <!-- Clicking the brand refreshes the page -->
            <a class="navbar-brand" href="/">THE BUZZ</a>
        </div>

        <!-- Collect the nav links, forms, and other content for toggling -->
        <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
            <ul class="nav navbar-nav">
                <li>
                    <a class="btn btn-link" id="Navbar-add">
                        Add Entry
                        <span class="glyphicon glyphicon-plus"></span><span class="sr-only">Show Trending Posts</span>
                    </a>
                </li>
                <li>
                    <a class="btn btn-link" id="Navbar-signin">
                        My Profile
                        <span class="glyphicon glyphicon-user"></span><span class="sr-only">Show Trending Posts</span>
                    </a>
                </li>
                <li>
                    <!--<a class="btn btn-link" id="Navbar-searchbutton">
                        Search By
                        <span class="glyphicon glyphicon-plus"></span><span class="sr-only">Show Trending Posts</span>
                    </a> -->
                    <input class="form-control" type="text" id="Navbar-search" />
                    <button type="button" class="btn btn-default" id="Navbar-gensearch">Search</button> 
                    <button type="button" class="btn btn-default" id="Navbar-title">Title</button> 
                    <button type="button" class="btn btn-default" id="Navbar-message">Message</button> 
                    <button type="button" class="btn btn-default" id="Navbar-author">Author</button> 
                    <button type="button" class="btn btn-default" id="Navbar-date">Date</button> 
                    <button type="button" class="btn btn-default" id="Navbar-profile">Profile</button> 
                </li>
            </ul>
        </div>
    </div>
</nav>