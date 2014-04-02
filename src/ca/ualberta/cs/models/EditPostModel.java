package ca.ualberta.cs.models;

public class EditPostModel {
	private static EditPostModel singleton = null;
	private static PostModel theParent = null;
	private PostModel thePost = null;
	
	public static EditPostModel getInstance() {
		if (singleton == null) {
			singleton = new EditPostModel();
		}
		
		return singleton;
	}
	
	public Boolean isNewPost() {
		return thePost == null;
	}
	
	public Boolean parentIsATopic() {
		return theParent instanceof TopicModel;
	}
	
	public Boolean hasParent() {
		return theParent != null;
	}

	/**
	 * @return the theParent
	 */
	public static PostModel getTheParent() {
		return theParent;
	}

	/**
	 * @param theParent the theParent to set
	 */
	public void setTheParent(PostModel theParent) {
		this.theParent = theParent;
	}

	/**
	 * @return the thePost
	 */
	public PostModel getThePost() {
		return thePost;
	}

	/**
	 * @param thePost the thePost to set
	 */
	public void setThePost(PostModel thePost) {
		this.thePost = thePost;
	}
}
