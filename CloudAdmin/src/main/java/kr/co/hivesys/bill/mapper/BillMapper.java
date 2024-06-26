package kr.co.hivesys.bill.mapper;

import java.util.HashMap;
import java.util.List;

import egovframework.rte.psl.dataaccess.mapper.Mapper;
import kr.co.hivesys.bill.vo.BillVo;
import kr.co.hivesys.comm.file.vo.FileVo;

@Mapper("billMapper")
public interface BillMapper {
	List<BillVo> selectList(BillVo inputVo);

	String createId(BillVo inputVo);
	
	int insert(BillVo inputVo);

	BillVo selectOne(BillVo inputVo);

	int update(BillVo inputVo);

	int delete(HashMap<String, Object> map);

	int firstNumber(BillVo inputVo);
	
	
	//#####################수동청구서#####################//
	
	List<BillVo> manualList(BillVo inputVo);
	String createMnId(BillVo inputVo);
	int manualInsert(BillVo inputVo);

	FileVo firstFile();
	
}
