package onex7.mangapp.Fragment;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import onex7.mangapp.R;
import onex7.mangapp.Util.HttpHandler;

public class AnalisisFragment extends Fragment {

    private TextView value1;
    private TextView value2;

    private String TAG = AnalisisFragment.class.getSimpleName();

    public AnalisisFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_analisis, container, false);

        value1 = view.findViewById(R.id.value1);
        value2 = view.findViewById(R.id.value2);

        new AnalisisFragment.getAnalisis().execute();

        return view;
    }

    @SuppressLint("StaticFieldLeak")
    private class getAnalisis extends AsyncTask<Void, Void, String[]> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(getActivity(), "Downloading JSON data", Toast.LENGTH_LONG).show();
        }

        @Override
        protected String[] doInBackground(Void... arg0) {

            HttpHandler sh = new HttpHandler();

            String url = "https://api.myjson.com/bins/1fhhji";
            String jsonStr = sh.makeServiceCall(url);
            String[] result = new String[2];

            Log.e(TAG, "Response from url: " + jsonStr);
            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    JSONArray analisis = jsonObj.getJSONArray("spark");
                    JSONObject c = analisis.getJSONObject(analisis.length() - 1);

                    result[0] = c.getString("jAsB");
                    result[1] = c.getString("rAh");

                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity(), "Json parsing error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
                Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), "Couldn't get json from server. Check LogCat for possible errors!", Toast.LENGTH_LONG).show();
                    }
                });
            }

            return result;
        }

        @Override
        protected void onPostExecute(String[] result) {
            super.onPostExecute(result);
            value1.setText(result[0]);
            value2.setText(result[1]);
        }
    }
}
