package algonquin.cst2335.dictionaryapi;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private EditText editText;
    private Button searchButton;
    private RecyclerView recyclerView;
    private DefinitionAdapter definitionAdapter;
    private TextView wordTextView;
    private TextView definitionTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = findViewById(R.id.editText);
        searchButton = findViewById(R.id.searchButton);
        recyclerView = findViewById(R.id.definitionRecyclerView);
        wordTextView = findViewById(R.id.wordTextView);
        definitionTextView = findViewById(R.id.definitionTextView);

        definitionAdapter = new DefinitionAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(definitionAdapter);

        // 从 SharedPreferences 获取上次保存的单词
        String lastSearchTerm = getLastSearchTerm();

        // 设置上次保存的单词到 EditText 中
        editText.setText(lastSearchTerm);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                performSearch();
            }
        });
    }

    private void performSearch() {
        // 获取用户输入的单词
        final String wordToSearch = editText.getText().toString();

        // 保存单词到 SharedPreferences
        saveSearchTerm(wordToSearch);

        String apiUrl = "https://api.dictionaryapi.dev/api/v2/entries/en/" + wordToSearch;

        // 使用 Volley 发起网络请求
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, apiUrl, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        List<Definition> definitions = parseJsonResponse(response, wordToSearch);
                        definitionAdapter.setDefinitions(definitions);

                        // 更新TextView
                        if (!definitions.isEmpty()) {
                            wordTextView.setText(definitions.get(0).getWord());
                            definitionTextView.setText(definitions.get(0).getDefinition());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // 处理请求错误
                    }
                });

        // 添加请求到请求队列
        requestQueue.add(request);
    }

    private List<Definition> parseJsonResponse(JSONArray jsonArray, String wordToSearch) {
        List<Definition> definitions = new ArrayList<>();
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String meaning = jsonObject.getString("definition");
                definitions.add(new Definition(wordToSearch, meaning));  // 传递单词给 Definition 构造函数
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return definitions;
    }

    // 保存搜索词到 SharedPreferences
    private void saveSearchTerm(String searchTerm) {
        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("searchTerm", searchTerm);
        editor.apply();
    }

    // 从 SharedPreferences 获取上次保存的单词
    private String getLastSearchTerm() {
        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        return preferences.getString("searchTerm", "");
    }
}
