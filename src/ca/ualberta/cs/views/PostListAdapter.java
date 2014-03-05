package ca.ualberta.cs.views;

import java.util.ArrayList;

import ca.ualberta.cs.R;
import ca.ualberta.cs.R.id;
import ca.ualberta.cs.R.layout;
import ca.ualberta.cs.models.CommentModel;
import ca.ualberta.cs.models.TopicModel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

class PostListAdapter extends BaseAdapter {

	Context context;
	ArrayList<TopicModel> data;
	private static LayoutInflater inflater = null;

	public PostListAdapter(Context context, ArrayList<TopicModel> data) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.data = data;
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int thePosition, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View vi = convertView;
		if (vi == null)
			vi = inflater.inflate(R.layout.row, null);
		if (data.get(thePosition).getClass() == TopicModel.class){
			TextView header = (TextView) vi.findViewById(R.id.header);
			header.setText( data.get(thePosition).getTitle());
		}
		
		TextView text = (TextView) vi.findViewById(R.id.text);
		text.setText(getFormatedData(thePosition));
		return vi;
	}
	
	public String getFormatedData(int thePosition){
		String theUserName = data.get(thePosition).getPostedBy().getUserName();
		String thePostScore = data.get(thePosition).getScore().toString();
		
		ArrayList<CommentModel> theComments = data.get(thePosition).getChildrenComments();
		Integer theReplyCount = 0;
		
		if (theComments != null) {
			theReplyCount = theComments.size();
		}
		
		return "Score:" + thePostScore + " Posted By:" + theUserName + " Replies:" + theReplyCount;	
	}
}