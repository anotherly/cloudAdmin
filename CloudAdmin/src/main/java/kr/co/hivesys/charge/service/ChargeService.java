package kr.co.hivesys.charge.service;

import java.util.List;

import kr.co.hivesys.charge.vo.ChargeVo;
import kr.co.hivesys.company.vo.CompanyVo;


public interface ChargeService {
	
	//과금 관련
	List<ChargeVo> selectPriceList(ChargeVo thvo);

	int chargeInsert(ChargeVo thvo);

	void deleteCharge();
}
