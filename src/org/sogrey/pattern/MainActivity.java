package org.sogrey.pattern;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.text.Html.ImageGetter;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MainActivity extends ActionBarActivity {
	static String url = "http://123.139.89.90:5080";
	TextView tv ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}

		 tv = (TextView) findViewById(R.id.tv);
		tv.setText("测试");

		 String str = "8. 如图{0}，已知FD∥BE，{1}则∠1+∠2-∠3=(    ) {2}."
//		String str = "8. 如图{0}，已知FD∥BE，则∠1+∠2-∠3=(    ) "
				+ "@{/upload/image/20140807/20140807000914_165.png"
				 + "/upload/image/20140807/20140807000656_442.jpg"
				 + "/upload/image/20140807/20140807000741_188.jpg"
				+ "}";
		// http://123.139.89.90:5080/upload/image/20140807/20140807000656_442.jpg
		// http://123.139.89.90:5080/upload/image/20140807/20140807000741_188.jpg
		// http://123.139.89.90:5080/upload/image/20140807/20140807000914_165.png

		tv.setText(str + "\n\n");
		showTxtImg(tv,str);
	}
	
	public static void showTxtImg(TextView tv, String str) {
		String title = "";
		ArrayList<String> imgs = new ArrayList<String>();
		Map<String, ArrayList<String>> map = patternPic(str);
		for (Map.Entry entry : map.entrySet()) {
			System.out.println("key= " + entry.getKey() + "\nvalue= "
					+ entry.getValue());
			title = (String) (entry.getKey());
			imgs.addAll((ArrayList<String>) entry.getValue());
		}
		String[] questionTitle = title.split("#\\*\\*#");
		for (int i = 0; i < questionTitle.length; i++) {
			String titleAll = "";
			titleAll += questionTitle[i];
			if (i < questionTitle.length - 1) {
//				titleAll += "<br> <img src=\"" + imgs.get(i) + "\"><br>";
				titleAll += " <img src=\"" + imgs.get(i) + "\">";
			}
			tv.append(Html.fromHtml(titleAll, imgGetter2, null));
		}
	}

	public  static Map<String, ArrayList<String>> patternPic(String str) {
		Map<String, ArrayList<String>> map = new HashMap<String, ArrayList<String>>();
		String[] arr = str.split("@");
		for (int i = 0; i < arr.length; i++) {
			System.out.println("arr[" + i + "]=" + arr[i]);
		}
		// arr[1]掐头去尾
		arr[1] = arr[1].replace("{", "");
		arr[1] = arr[1].replace("}", "");
		System.out.println("arr[1]==" + arr[1]);
		String[] arr2 = arr[1].split("/upload/");
		ArrayList<String> listImgs = new ArrayList<String>();
		for (int i = 0; i < arr2.length; i++) {
			// if (!TextUtils.isEmpty(arr2[i])) {
			if (!"".equals(arr2[i])) {
				listImgs.add(url + "/upload/" + arr2[i]);
			}
		}
		System.out.println(listImgs.toString());

		String pat = "\\{\\d+\\}";
		Pattern pattern1 = Pattern.compile(pat);
		Matcher matcher1 = pattern1.matcher(arr[0]);
		arr[0] = matcher1.replaceAll("#**#");
		System.out.println(arr[0]);
		map.put(arr[0], listImgs);
		return map;
	}

	static ImageGetter imgGetter2 = new Html.ImageGetter() {
		@Override
		public Drawable getDrawable(String source) {
			Drawable drawable = null;
			URL url;
			try {
				url = new URL(source);
				drawable = Drawable.createFromStream(url.openStream(), "");
			} catch (Exception e) {
				return null;
			}

			drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
					drawable.getIntrinsicHeight());
			return drawable;
		}
	};

	ImageGetter imageGetter = new ImageGetter() {

		@Override
		public Drawable getDrawable(String source) {
			int id = Integer.parseInt(source);
			Drawable drawable = getResources().getDrawable(id);
			drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
					drawable.getIntrinsicHeight());
			tv.postInvalidate();
			return drawable;
		}
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			return rootView;
		}
	}

}
