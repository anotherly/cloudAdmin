package kr.co.hivesys.charge.service.impl;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import kr.co.hivesys.charge.mapper.ChargeMapper;
import kr.co.hivesys.charge.service.ChargeService;
import kr.co.hivesys.charge.vo.ChargeVo;

@Service("chargeService")
public class ChargeServiceImpl implements ChargeService{

	@Resource(name="chargeMapper")
	private ChargeMapper chargeMapper;
	
	@Override
	public List<ChargeVo> selectPriceList(ChargeVo thvo) {
		return chargeMapper.selectPriceList(thvo);
	}

	@Override
	public int chargeInsert(ChargeVo thvo) {
		return chargeMapper.chargeInsert(thvo);
	}

	@Override
	public void deleteCharge() {
		chargeMapper.deleteCharge();		
	}
	
}
