package kr.ispark.common.system.service;

import kr.ispark.common.CommonVO;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class IdGenVO extends CommonVO {
	private static final long serialVersionUID = 6214337688896871246L;
	
	private String tableName;
	private String prefix;
	private int seq;
	private int length;
	private String fillChar;
	private String commonYn = "N";
	private String templateYn = "N";
}
