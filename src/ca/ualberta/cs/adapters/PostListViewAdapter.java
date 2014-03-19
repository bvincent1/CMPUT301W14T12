package ca.ualberta.cs.adapters;

import java.util.ArrayList;

import android.support.v4.app.FragmentActivity;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import ca.ualberta.cs.R;
import ca.ualberta.cs.models.PostModel;

/**
 * 
 * @author vincent
 * 
 */

public abstract class PostListViewAdapter<T extends PostModel> extends ArrayAdapter<T> {
	private LayoutInflater layoutInflater = null;

	public PostListViewAdapter(FragmentActivity theActivity, ArrayList<T> arrayList) {
		super(theActivity, R.layout.row, arrayList);
		// TODO Auto-generated constructor stub
		this.layoutInflater = theActivity.getLayoutInflater();
	}

	@Override
	public View getView(int thePosition, View convertView, ViewGroup parent) {
		View postRowView = convertView;
		
		if (postRowView == null) {
			postRowView = layoutInflater.inflate(R.layout.row, parent, false);
		}

		// set row elements to the required data
		populateRowView(postRowView, thePosition);
		
		return postRowView;
	}
	
	/**
	 * fill in the row at the called position with the appropriate data
	 * @param theView
	 * @param thePosition
	 */
	protected void populateRowView(View theView, int thePosition){
		// Get the post object
		T theObject = getItem(thePosition);
		PostModel thePost = theObject;
		
		// Add position tag
		RelativeLayout cellActiveArea = (RelativeLayout) theView.findViewById(R.id.cellActiveArea);
		cellActiveArea.setTag(thePosition);
		
		// Fill date
		TextView dateTextView = (TextView) theView.findViewById(R.id.textViewAge);
		String date = (String) DateFormat.format("yyyy/MM/dd", thePost.getDatePosted());
		dateTextView.setText(date);		
		
		// Fill author
		TextView authorTextView = (TextView) theView.findViewById(R.id.textViewAuthor);
		authorTextView.setText(thePost.getPostedBy().getUserName());		
		
		// Fill comment count
		TextView commentTextView = (TextView) theView.findViewById(R.id.textViewComments);
		if (thePost.getChildrenComments() == null) {
			commentTextView.setText("0");
		}
		else {
			String commentCount = Integer.toString(thePost.getChildrenComments().size());
			commentTextView.setText(commentCount);
		}
		
		// Fill location
		// TODO: Add location text
		// TextView locationText = (TextView) theView.findViewById(R.id.textViewLocation);
		// locationText.setText(thePost.getLocation().toString());
		
		// Fill score
		TextView scoreText = (TextView) theView.findViewById(R.id.textViewScore);
		String scoreString = "";
		if (thePost.getScore() > 0) {
			scoreString = "+";
		}
		else if (thePost.getScore() < 0) {
			scoreString = "-";
		}
		scoreString = scoreString + thePost.getScore().toString();
		scoreText.setText(scoreString);
		
		populateCellTitle(theView, theObject);
	}

	abstract protected void populateCellTitle(View theView, T thePost);
}