package br.edu.unisep.bellfoods.task;

import android.os.AsyncTask;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import java.util.ArrayList;
import java.util.List;

import br.edu.unisep.bellfoods.MainActivity;
import br.edu.unisep.bellfoods.vo.PratoVO;

/**
 * Created by Cleiton on 12/06/2015.
 */
public class ListaPratosTask extends AsyncTask<Void, List<PratoVO>, Void> {

    private static final int CONNECTION_TIMEOUT = 1000 * 15;

    private static final String SERVER_ADDRESS = "http://www.icompsoft.com.br/bellfoods.php";

    private String query;

    private MainActivity activity;

    private HttpParams getHttpRequestParams() {
        HttpParams httpRequestParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpRequestParams,
                CONNECTION_TIMEOUT);
        HttpConnectionParams.setSoTimeout(httpRequestParams,
                CONNECTION_TIMEOUT);
        return httpRequestParams;
    }

    public ListaPratosTask(MainActivity activity, String query) {
        this.activity = activity;
        this.query = query;
    }

    @Override
    protected Void doInBackground(Void... params) {

        ArrayList<NameValuePair> dataToSend = new ArrayList<>();
        dataToSend.add(new BasicNameValuePair("action", "buscar"));
        dataToSend.add(new BasicNameValuePair("query", this.query));

        HttpParams httpRequestParams = getHttpRequestParams();

        HttpClient client = new DefaultHttpClient(httpRequestParams);
        HttpPost post = new HttpPost(SERVER_ADDRESS);

        try {
            post.setEntity(new UrlEncodedFormEntity(dataToSend));
            HttpResponse httpResponse = client.execute(post);

            HttpEntity entity = httpResponse.getEntity();
            String result = EntityUtils.toString(entity);

            Gson gs = new Gson();
            TypeToken<List<PratoVO>> token = new TypeToken<List<PratoVO>>() {
            };
            List<PratoVO> lista = gs.fromJson(result, token.getType());

            publishProgress(lista);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(List<PratoVO>... values) {
        List<PratoVO> lista = values[0];
        this.activity.atualizarLista(lista);
    }

}
