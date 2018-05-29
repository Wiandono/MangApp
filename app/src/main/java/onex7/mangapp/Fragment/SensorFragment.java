package onex7.mangapp.Fragment;

import android.annotation.SuppressLint;
import android.hardware.Sensor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import onex7.mangapp.R;
import onex7.mangapp.Util.HttpHandler;

public class SensorFragment extends Fragment {

    private String TAG = SensorFragment.class.getSimpleName();
    private ListView listSensor;

    ArrayList<HashMap<String, String>> sensorData;

    public SensorFragment() {

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sensor, container, false);

        sensorData = new ArrayList<>();
        listSensor = view.findViewById(R.id.list_sensor);

        new getSensorData().execute();

        return view;
    }

    @SuppressLint("StaticFieldLeak")
    private class getSensorData extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(getActivity(), "Downloading JSON data", Toast.LENGTH_LONG).show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();
            // Making a request to url and getting response
            String url = "https://api.myjson.com/bins/lxal2";
            String jsonStr = sh.makeServiceCall(url);

            Log.e(TAG, "Response from url: " + jsonStr);
            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    JSONArray sensors = jsonObj.getJSONArray("sensor");

                    // looping through All Contacts
                    for (int i = 0; i < sensors.length(); i++) {
                        JSONObject c = sensors.getJSONObject(i);
                        String status = "Status: " + c.getString("status");
                        String waktu = c.getString("waktu");

                        // tmp hash map for single contact
                        HashMap<String, String> sensor = new HashMap<>();

                        // adding each child node to HashMap key => value
                        sensor.put("status", status);
                        sensor.put("waktu", waktu);

                        // adding contact to contact list
                        sensorData.add(sensor);
                    }
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

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            ListAdapter adapter = new SimpleAdapter(getActivity(), sensorData,
                    R.layout.layout_sensor, new String[]{"status", "waktu"},
                    new int[]{R.id.status, R.id.waktu});
            listSensor.setAdapter(adapter);
        }
    }
}
