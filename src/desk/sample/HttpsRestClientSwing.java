package desk.sample;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.Builder;
import java.net.http.HttpResponse;
import java.net.http.HttpClient.Version;
import java.net.http.HttpConnectTimeoutException;
import java.util.List;
import java.util.Map;
import java.net.HttpURLConnection;
import javax.net.ssl.SSLParameters;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class HttpsRestClientSwing implements Runnable, ActionListener, ListSelectionListener {

	private Frame1 window;
	private FrameAuth windowauth;
	private boolean fAuthentication = false;
	private String authType = "";
	private static final String APP_EXCEPTION_MSG = "システムエラーが発生しました。\nアプリケーションを終了します。\n";
	private static final String APP_EXCEPTION_TITLE = "例外発生";		
	private static final String CONTENT_TYPE_NAME = "Content-Type";
	private static final String CONTENT_TYPE_JSON = "application/json; charset=utf-8";
	private static final String AUTHORIZATION = "Authorization";
	
	public static void main(String[] args) {
		Thread thread = new Thread(new HttpsRestClientSwing());
		thread.setUncaughtExceptionHandler(new OriginalUncaughtException());
        thread.start();
	}
	
	@Override
	public void run() {
		window = new Frame1();
		windowauth = new FrameAuth();
		window.tableSetting();
		//ActionEvent event = null;
		//window.actionPerformed(event);
		window.getButtonQuery().addActionListener(this);
		window.getButtonInsert().addActionListener(this);
		window.getButtonUpdate().addActionListener(this);
		window.getButtonDelete().addActionListener(this);
		window.getTable1().getSelectionModel().addListSelectionListener(this);
		windowauth.getButtonAuth().addActionListener(this);
		windowauth.getButtonCancel().addActionListener(this);
		window.getTextUri().setText("https://localhost:5001/api/ShohinEntities");//"https://localhost:8443/rest");
		window.getContentPane().add(window.getFpanel(),BorderLayout.CENTER);
		windowauth.getContentPane().add(windowauth.getPanel(),BorderLayout.CENTER);
		window.setVisible(true);
		HttpSettings.setHttpVer(Version.HTTP_1_1);
	}
	
	@Override
	public void actionPerformed(ActionEvent event) {
        var sslParams = new SSLParameters();
        sslParams.setEndpointIdentificationAlgorithm("HTTPS"); //LDAPS
        sslParams.setProtocols(new String[] {HttpSettings.sslProtocol[3]});
		HttpClient httpClient = HttpClient.newBuilder()
				//.sslContext(TrustCertificate.CertificateThrough()) //証明書検証スルー
		        .sslParameters(sslParams)
		        .connectTimeout(java.time.Duration.ofMillis(1000))
				.version(HttpClient.Version.HTTP_1_1).build();
		if (window.getButtonQuery().equals(event.getSource())) {
			httpGet(httpClient);
		} else if (window.getButtonInsert().equals(event.getSource())) {
			httpPost(httpClient);
		} else if (window.getButtonUpdate().equals(event.getSource())) {
			httpPut(httpClient);
		} else if (window.getButtonDelete().equals(event.getSource())) {
			httpDelete(httpClient);
		} else if (windowauth.getButtonAuth().equals(event.getSource())) {
			Authentication.setUserID(windowauth.getTextUserName().getText());
			Authentication.setPassword(windowauth.getTextPassword().getPassword());
			windowauth.setVisible(false);
		} else {
			windowauth.setVisible(false);
		}
	}
	
	@Override
	public void valueChanged(ListSelectionEvent listevent) {
		if(listevent.getValueIsAdjusting()) {
			return;
		}
		window.getTableRowSetTextField();
	}
	
	private void httpGet(HttpClient httpClient) {
		URI uri = URI.create(window.getTextUri().getText() + "/" + window.getLabelNumId().getText());
		HttpRequest req = requestSetting(HttpRequest.newBuilder().GET(), uri);
		HttpResponse<String> res = httpRequest(httpClient, req);
		if(res.statusCode() == HttpURLConnection.HTTP_UNAUTHORIZED) {
			Map<String, List<String>> maps = res.headers().map();
			Map<String, String> dmap = Authentication.analysis(maps);
			authType = dmap.get(Authentication.AUTHENTICATE);
			req = requestSetting(HttpRequest.newBuilder().GET(), uri);
			res = httpRequest(httpClient, req);
		}
		if (res.statusCode() == HttpURLConnection.HTTP_OK) {
			String json = res.body().toString();
			setTable(json);
			window.getLabelArea().append("データを全件取得しました。\n");
		}
	}
	
	private void setTable(String json) {
		window.getDtableModel().setRowCount(0); //表示行クリア
		List<ShohinMap> list;
		try {
			list = new ObjectMapper().readValue(json, new TypeReference<List<ShohinMap>>() {});
			for (int i = 0; i < list.size(); i++) {
	            String ldate = ((ShohinMap)list.get(i)).getEditDate().toString();
	            ldate = ldate.substring(0,4) + "/" + ldate.substring(4,6) + "/" + ldate.substring(6,8);
	            String ltime = ((ShohinMap)list.get(i)).getEditTime().toString();
	            ltime = String.format("%6s", ltime).replace(" ", "0");
	            ltime = ltime.substring(0,2) + ":" + ltime.substring(2,4) + ":" + ltime.substring(4,6);
	            Object[] Objrs = {((ShohinMap)list.get(i)).getNumId(),
	            		((ShohinMap)list.get(i)).getShohinCode(),
	            		((ShohinMap)list.get(i)).getShohinName(),
	            			ldate,
	            			ltime,
	            		((ShohinMap)list.get(i)).getNote()};
	            window.getDtableModel().addRow(Objrs);
			}
			window.tableSetting();
	        window.textFieldClear();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	}
	
	private void httpPost(HttpClient httpClient) {
		if (window.getTextShohinNum().getText().matches("^[0-9]{1,4}$") == false) {
			window.showDialog("商品番号は半角数値の0～9999でなければなりません。","メッセージ", JOptionPane.WARNING_MESSAGE);
			return;
		}
		
		String jsonStr = createJsonStr();
		URI uri = URI.create(window.getTextUri().getText() + "/" + window.getLabelNumId().getText());
		HttpRequest req = requestSetting(HttpRequest.newBuilder().POST(HttpRequest.BodyPublishers.ofString(jsonStr)), uri);
		HttpResponse<String> res = httpRequest(httpClient, req);
		if(res.statusCode() == HttpURLConnection.HTTP_UNAUTHORIZED) {
			req = requestSetting(HttpRequest.newBuilder().POST(HttpRequest.BodyPublishers.ofString(jsonStr)), uri);
			res = httpRequest(httpClient, req);
		}
		if (res.statusCode() == HttpURLConnection.HTTP_CREATED) {
			window.getLabelArea().append("データを1件追加しました。\n");
		}
	}
	
	private void httpPut(HttpClient httpClient) {
		if (window.getLabelNumId().getText().equals("")) {
			window.showDialog("更新する商品行が選択できていません。", "商品IDなし", JOptionPane.WARNING_MESSAGE);
			return;
		}
		if (window.getTextShohinNum().getText().matches("^[0-9]{1,4}$") == false) {
			window.showDialog("商品番号は半角数値の0～9999でなければなりません。","メッセージ", JOptionPane.WARNING_MESSAGE);
			return;
		}
		
		String jsonStr = createJsonStr();
		URI uri = URI.create(window.getTextUri().getText() + "/" + window.getLabelNumId().getText());
		HttpRequest req = requestSetting(HttpRequest.newBuilder().PUT(HttpRequest.BodyPublishers.ofString(jsonStr)), uri);
		HttpResponse<String> res = httpRequest(httpClient, req);
		if(res.statusCode() == HttpURLConnection.HTTP_UNAUTHORIZED) {
			req = requestSetting(HttpRequest.newBuilder().PUT(HttpRequest.BodyPublishers.ofString(jsonStr)), uri);
			res = httpRequest(httpClient, req);
		}
		if (res.statusCode() == HttpURLConnection.HTTP_OK) {
			window.getLabelArea().append("選択されたレコードを1件更新しました。\n");
		}
	}
	
	private void httpDelete(HttpClient httpClient) {
		if (window.getLabelNumId().getText().equals("")) {
			window.showDialog("更新する商品行が選択できていません。", "商品IDなし", JOptionPane.WARNING_MESSAGE);
			return;
		}
		
		URI uri = URI.create(window.getTextUri().getText() + "/" + window.getLabelNumId().getText());
		HttpRequest req = requestSetting(HttpRequest.newBuilder().DELETE(), uri);
		HttpResponse<String> res = httpRequest(httpClient, req);
		if(res.statusCode() == HttpURLConnection.HTTP_UNAUTHORIZED) {
			req = requestSetting(HttpRequest.newBuilder().DELETE(), uri);
			res = httpRequest(httpClient, req);
		}
		if (res.statusCode() == HttpURLConnection.HTTP_NO_CONTENT) {
			window.getLabelArea().append("選択されたレコードを1件削除しました。\n");
		}
	}
	
	private HttpResponse<String> httpRequest(HttpClient client, HttpRequest req) {
		HttpResponse<String> response = null;
		String resStr = "";
		
		try {
			response = client.send(req, HttpResponse.BodyHandlers.ofString());
		} catch (HttpConnectTimeoutException e) { 
			window.showDialog("サーバーに接続できませんでした。", "HTTP接続タイムアウト", JOptionPane.ERROR_MESSAGE);
			return response;
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
			window.showDialog(APP_EXCEPTION_MSG, APP_EXCEPTION_TITLE, JOptionPane.ERROR_MESSAGE);
			window.dispose();
		}
		if (response.statusCode() == HttpURLConnection.HTTP_OK || 
				response.statusCode() == HttpURLConnection.HTTP_CREATED || 
				response.statusCode() == HttpURLConnection.HTTP_NO_CONTENT) {
			resStr = response.body().toString();
			window.getLabelArea().append(response.headers().toString());
		} else {
			switch (response.statusCode()) {
			case HttpURLConnection.HTTP_UNAUTHORIZED : //認証が必要
				window.getLabelArea().append(response.headers().toString());
				windowauth.setVisible(true);
				fAuthentication = true;
				break;
			case HttpURLConnection.HTTP_BAD_REQUEST :
				resStr = response.body().toString();
				window.getLabelArea().append(response.headers().toString());
				break;
			default : //その他のエラー
				window.showDialog(response.headers().toString(), String.valueOf(response.statusCode()), JOptionPane.ERROR_MESSAGE);
				break;
			}
		}
		window.getTextReqBody().setText(resStr);
		
		return response;
	}
	
	private HttpRequest requestSetting(Builder builder, URI uri) {
		if (fAuthentication && Authentication.getUserID().equals("") == false) {
			if (authType == Authentication.BASIC) {
				String basicStr = Authentication.basicRequestHeader();
				builder.setHeader(AUTHORIZATION, basicStr);
			} else {
				String a1 = Authentication.digestResponseA1(Authentication.mapParam.get("realm"));
				String a2 = Authentication.digestResponseA2("GET", uri.toString());
				String res = Authentication.digestResponse(a1, a2, Authentication.mapParam.get("nonce"), Authentication.mapParam.get("qop"));
				String req = Authentication.digestRequest(res);
				builder.header(AUTHORIZATION, req);
			}
		}
		HttpRequest request = builder
				.uri(uri)
				.version(HttpSettings.getHttpVer())
				.timeout(java.time.Duration.ofMillis(3000)) //タイムアウト3秒固定
				.setHeader(CONTENT_TYPE_NAME, CONTENT_TYPE_JSON).build();
		
		return request;
	}
	
	private String createJsonStr() {
		String str = "{ \"shohinCode\":" + Short.valueOf(window.getTextShohinNum().getText());
		str += ", \"shohinName\": \"" + window.getTextShohinName().getText() +  "\", \"note\": \"" + window.getTextNote().getText() + "\" }";
		
		return str;
	}

}