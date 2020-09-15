package desk.sample;

import java.math.BigDecimal;
import com.fasterxml.jackson.annotation.JsonProperty;

/** JacksonライブラリーによるJsonデータマッピング
 * @author none **/
public class ShohinMap {
	@JsonProperty("numId")
	private Integer _NumId;
	@JsonProperty("shohinCode")
	private Short _ShohinCode;
	@JsonProperty("shohinName")
	private String _ShohinName;
	@JsonProperty("editDate")
	private BigDecimal _EditDate;
	@JsonProperty("editTime")
	private BigDecimal _EditTime;
	@JsonProperty("note")
	private String _Note;
	
	public Integer getNumId() {
		return _NumId;
	}
	public void setNumId(Integer value) {
		this._NumId = value;
	}

	public Short getShohinCode() {
		return _ShohinCode;
	}
	public void setShohinCode(Short value) {
		this._ShohinCode = value;
	}

	public String getShohinName() {
		return _ShohinName;
	}
	public void setShohinName(String value) {
		this._ShohinName = value;
	}

	public BigDecimal getEditDate() {
		return _EditDate;
	}
	public void setEditDate(BigDecimal value) {
		this._EditDate = value;
	}

	public BigDecimal getEditTime() {
		return _EditTime;
	}
	public void setEditTime(BigDecimal value) {
		this._EditTime = value;
	}

	public String getNote() {
		return _Note;
	}
	public void setNote(String value) {
		this._Note = value;
	}
}