/**
 * The VoteCounter Singleton is the navigation bar at the top of the page.  Through 
 * its HTML, it is designed so that clicking the "brand" part will refresh the
 * page.  Apart from that, it has an "add" button, which forwards to 
 * NewEntryForm
 */
class VoteCounter {
        /**
         * Track if the Singleton has been initialized
         */
        private static isInit = false;
        // private static wasUpped = false;
        // private static wasDowned = false;
    
        /**
         * Initialize the VoteCounter Singleton by creating its element in the DOM and
         * configuring its button.  This needs to be called from any public static
         * method, to ensure that the Singleton is initialized before use.
         */
        private static init() {
                if (!VoteCounter.isInit) {
                        $(".ElementList-upbtn").on("click", function() { VoteCounter.upVote(); });
                        $(".ElementList-downbtn").on("click", function() { VoteCounter.downVote(); });
                        VoteCounter.isInit = true;
                }
        }
        
        public static upVote(){
                let id = $(this).data("value");
                VoteCounter.increment(id);
        }

        public static downVote(){
                let id = $(this).data("value");
                VoteCounter.decrement(id);
        }

        public static decrement(id: number){
                $.ajax({
                        type: "PUT",
                        url: "https://bmw-dba.herokuapp.com/buzz/" + id + "/downvote",
                        success: VoteCounter.refresh
                    });
        }

        public static increment(id: number){
                $.ajax({
                        type: "PUT",
                        url: "https://bmw-dba.herokuapp.com/buzz/" + id + "/upvote",
                        success: VoteCounter.refresh
                });
        }   
           

        // window.location.reload();
        /**
         * Refresh() doesn't really have much meaning for the VoteCounter, but we'd 
         * rather not have anyone call init(), so we'll have this as a stub that
         * can be called during front-end initialization to ensure the VoteCounter
         * is configured.
         */
        public static refresh() {
                if(VoteCounter.isInit){
                        window.location.reload();
                }
                VoteCounter.init();
        }
    }
    
    