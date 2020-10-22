<div id="ViewMessage" class="modal fade" role="dialog">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title">Message</h4>
            </div>
            <div class="modal-body">
                <label for="ViewMessage-title">Title</label>
                <input class="form-control" type="text" id="ViewMessage-title" readonly/>
                <label for="ViewMessage-message">Message</label>
                <textarea class="form-control" id="ViewMessage-message" readonly></textarea>
                <input type="hidden" id="ViewMessage-id">
            </div>
            <div class="modal-footer">
                <button class="btn btn-default" id="ViewMessage-comment" data-value="{{this.mId}}">Comment</button>
            </div>
        </div>
    </div>
</div>