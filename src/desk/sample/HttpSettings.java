package desk.sample;

import java.net.http.HttpClient.Version;

public class HttpSettings {
	private static final String HTTP1_0 = "HTTP1.0";
    private static final String HTTP1_1 = "HTTP1.1";
    private static final String HTTP2 = "HTTP2";
    public static String[] httpVersion = { "", HTTP1_1, HTTP2 };
    private static final String SSL3_0 = "SSLv3";
    private static final String TLS1_1 = "TLSv1.1";
    private static final String TLS1_2 = "TLSv1.2";
    private static final String TLS1_3 = "TLSv1.3";
    public static String[] sslProtocol = { "", SSL3_0, TLS1_1, TLS1_2, TLS1_3 };
	
    public HttpSettings()
    {
        httpVer = Version.HTTP_1_1;
        sslProtocolVer[0] = TLS1_2;
    }
    
    private static Version httpVer;
    public static Version getHttpVer() {
    	return httpVer;
    }
    public static void setHttpVer(Version value) {
    	httpVer = value;
    }
    
    private String _HttpVerStr;
    public String getHttpVerStr() {
    	return _HttpVerStr;
    }
    public void setHttpVerStr(String value)
    {
    	switch (value) {
    		case HTTP1_1:
    			httpVer = Version.HTTP_1_1;
    			break;
    		case HTTP2:
    			httpVer = Version.HTTP_2;
    			break;
    		default:
    			httpVer = Version.HTTP_1_1;
    			break;
    	}
    }

    private static String[] sslProtocolVer;
    public static String[] getSslProtocolVer() {
    	return sslProtocolVer;
    }
    public static void setSslProtocolVer(String[] value) {
    	sslProtocolVer = value;
    }
    private String _SslProtocolVerStr;
    public String getSslProtocolVerStr() {
    	return _SslProtocolVerStr;
    }
    public void setSslProtocolVerStr(String value) {
    	switch (value) {
    	case SSL3_0:
    		sslProtocolVer[0] = SSL3_0;
    		break;
    	case TLS1_1:
    		sslProtocolVer[0] = TLS1_1;
    		break;
    	case TLS1_2:
    		sslProtocolVer[0] = TLS1_2;
    		break;
    	case TLS1_3:
    		sslProtocolVer[0] = TLS1_3;
    		break;
    	default:
    		sslProtocolVer[0] = TLS1_2;
    		break;
    	}
    }
}