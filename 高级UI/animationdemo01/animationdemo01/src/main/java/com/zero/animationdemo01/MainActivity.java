package com.zero.animationdemo01;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;


public class MainActivity extends BaseActivity implements OnItemClickListener{
	private ListView mTrainListAnim;
	private ArrayAdapter<String> mTrainItemAdapter;

	@Override
	public void setView() {
		setContentView(R.layout.activity_train);		
	}

	@Override
	public void initView() {
		mTrainListAnim = (ListView) findViewById(R.id.listView_anim_train);
		mTrainItemAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, ConstantValues.trainEffectItems);
		
	}
	
	public void startIntent(Class class1){
		Intent intent = new Intent(MainActivity.this,class1);
		startActivity(intent);
	}

	@Override
	public void setListener() {
		mTrainListAnim.setAdapter(mTrainItemAdapter);
		mTrainListAnim.setOnItemClickListener(this);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		switch (position) {
		case 0:
            Intent intent = new Intent(MainActivity.this, SimpleViewAnimation.class);
			startActivity(intent);
            break;
		case 1:
            Intent intent2 = new Intent(MainActivity.this, ComplicateViewAnimation.class);
			startActivity(intent2);
            break;
		case 2:
            Intent intent3 = new Intent(MainActivity.this, SimplePropertyAnimation.class);
			startActivity(intent3);
			break;
		case 3:
            Intent intent4 = new Intent(MainActivity.this, ComplicatePropertyAnimation.class);
			startActivity(intent4);
            break;
		case 4:
            break;
		case 5:
            Intent intent6 = new Intent(MainActivity.this, InterPolatorAnimation.class);
			startActivity(intent6);
			break;
		case 6:

			break;
		case 7:

			break;
		case 8:

			break;
		case 9:
	
			break;
		case 10:
			
			break;
		default:
			break;
		}
		
	}

}
