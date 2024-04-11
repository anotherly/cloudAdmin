package kr.co.hivesys.bill.service;

import java.util.List;

import kr.co.hivesys.bill.vo.BillVo;


public interface BillService {
	List<BillVo> selectList(BillVo inputVo);

	String createId(BillVo inputVo);
	
	int insert(BillVo inputVo);

	BillVo selectOne(BillVo inputVo);

	int update(BillVo inputVo);

	int delete(List<String> inputList);

	int firstNumber(BillVo inputVo);
}
