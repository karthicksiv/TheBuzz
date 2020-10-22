<div class="panel panel-default" id="ElementList">
    <div class="panel-heading">
        <h3 class="panel-title">All Posts</h3>
    </div>
    <table class="table">
        <tbody>
            {{#each mData}}
            <tr>
                <td>{{this.mTitle}}</td>
                <td><button class="ElementList-upbtn" data-value="{{this.mId}}">Like</button></td>
                <td>{{this.mVote}}</td>
                <td><button class="ElementList-downbtn" data-value="{{this.mId}}">Dislike</button></td>
                <td><button class="ElementList-editbtn" data-value="{{this.mId}}" type="button">Edit</button></td>
                <td><button class="ElementList-delbtn" data-value="{{this.mId}}">Delete</button></td>
                <td><button class="ElementList-viewbtn" data-value="{{this.mId}}">View</button></td>
            </tr>
            {{/each}}
        </tbody>
    </table>
</div>
