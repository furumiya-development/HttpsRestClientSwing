package desk.sample;

import java.io.UnsupportedEncodingException;
import java.security.SecureRandom;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Authentication {
	public static Map<String, String> mapParam = new HashMap<>();
	private static String uri = "";
	private static String user = "";
	private static char[] pass;
	public static final String BASIC = "Basic ";
	public static final String DIGEST = "Digest ";
	public static final String AUTHENTICATE = "WWW-Authenticate";
	private static final String MD5 = "MD5";
	private static final String SHA1 = "SHA1PRNG";
	private static String wNonce;
	private static String wNonceCnt = "00000001";
	private static String wCnonce;
	
	
	public Authentication(String uri) {
		this.uri = uri;
	}
	
	public static Map<String, String> analysis(Map<String, List<String>> maps) {
		String auths = maps.get(AUTHENTICATE).get(0).toString();
		
		if (auths.startsWith(BASIC)) {
			mapParam.put(AUTHENTICATE, BASIC.replace(" ", ""));
			auths = auths.replace(BASIC, "");
			Matcher mkey = Pattern.compile("(.*?)=\"").matcher(auths);
			mkey.find();
			Matcher mvalue = Pattern.compile("\"(.*?)\"").matcher(auths);
			mvalue.find();
			mapParam.put(mkey.group(1), mvalue.group(1));
		} else if (auths.startsWith(DIGEST)) {
			mapParam.put(AUTHENTICATE, DIGEST.replace(" ", ""));
			auths = auths.replace(DIGEST, "");			
			mapParam = digestAnalysis(auths, mapParam);
		}
		
		return mapParam;
	}
	
	public static String basicRequestHeader() {
		String base64Str = BASIC;
		try {
			base64Str += Base64.getEncoder().encodeToString((user + ":" + String.copyValueOf(pass)).getBytes("utf8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		return base64Str;
	}
	
	private static Map<String, String> digestAnalysis(String auths, Map<String, String> map) {
		//Map<String, List<String>> maps = response.headers().map();
		String[] auth = auths.split(", ");
		for (int i=0 ; i < auth.length; i++) {
			//項目の取り出し
			//()で囲むと一致する部分を取り出すgroup(1)でその文字(ここでは=)を除去
			Matcher mkey = getPatternMatch("(.*?)=\"", auth[i]);
			//""で囲まれた内容を取り出し項目とともにMapへ格納
			Matcher mvalue = getPatternMatch("\"(.*?)\"", auth[i]);
			map.put(mkey.group(1), mvalue.group(1));
		}
		return map;
	}
	
	private static Matcher getPatternMatch(String pattern, String mstr) {
		Matcher match = Pattern.compile(pattern).matcher(mstr);
		match.find();
		return match;
	}
	
	public static String digestRequest(String res) {
		String req = DIGEST + "username=\""+ user +"\", realm=\"" + mapParam.get("realm") + "\", nonce=\"";
		req += mapParam.get("nonce") + "\", uri=\"" + uri + "\", algorithm=MD5, qop=";
		req += mapParam.get("qop") + ", nc=" + wNonceCnt + ", cnonce=\"" + wCnonce + "\", response=\"" + res + "\"";
		System.out.println(req);
		
		return req;
	}
	
	public static String digestResponse(String a1, String a2, String nonce, String qop) {
		String res = "";
		
		//if (wNonce.equals(nonce) == false) {
			
		//}
		wCnonce = getRandomStr();
		res = a1 + ":" + nonce + ":" + wNonceCnt + ":" + wCnonce + ":" + qop + ":" + a2;
		res = toMd5(res);
		
		return res;
	}
	
	public static String digestResponseA1(String realm) {
		return toMd5((user + ":" + realm + ":" + pass.toString()));
	}
	
	public static String digestResponseA2(String method, String uri) {
		return toMd5((method + ":" + uri));
	}
	
	private static String toMd5(String md5str) {
		StringBuffer md5hex = new StringBuffer();
		
		try {
			MessageDigest md5 = MessageDigest.getInstance(MD5);
			byte[] md5byte = md5.digest((md5str).getBytes());
			//byte配列を１つづつ取り出して16進数変換し文字列生成
			for (byte b : md5byte) {
				md5hex.append(String.format("%02x", b));
			}
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return md5hex.toString();
	}
	
	private static String getRandomStr() {
		byte bytes[] = new byte[8]; //8バイト16文字列
		SecureRandom rand;
		StringBuffer sha1hex = new StringBuffer();
		
		try {
			rand = SecureRandom.getInstance(SHA1);
			rand.nextBytes(bytes);
			//byte配列を１つづつ取り出して16進数変換し文字列生成
			for (byte b : bytes) {
				sha1hex.append(String.format("%02x", b));
			}
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return sha1hex.toString();
	}
	
	public static String getUserID() {
		return user;
	}
	public static void setUserID(String value) {
		user = value;
	}
	
	public static char[] getPassword() {
		return pass;
	}
	public static void setPassword(char[] value) {
		pass = value;
	}
}