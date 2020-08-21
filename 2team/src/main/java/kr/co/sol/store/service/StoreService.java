package kr.co.sol.store.service;

import java.util.List;

import kr.co.sol.custom.dto.StoreDTO;

public interface StoreService {

	List<StoreDTO> getStoreList();

	List<StoreDTO> getStore(String searchOption, String keyword);
}
