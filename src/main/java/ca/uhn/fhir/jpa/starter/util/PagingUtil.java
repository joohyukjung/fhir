package ca.uhn.fhir.jpa.starter.util;

import java.util.ArrayList;
import java.util.List;

public class PagingUtil {
	
	// 페이징 처리
	// 첫 페이지 : 1
	public static <T> List<T> pagingList(List<T> item, int offset, int count) {
		
		// 선택 페이지가 전체 페이지보다 큰 경우 [] 리턴
		if (! checkPageNumber(item.size(), offset, count)) {
			return new ArrayList<>();
		}
		
		int startIndex = (offset - 1) * count;
		int endIndex = (startIndex + count);
		
		if (! checkEndIdx(item.size(), endIndex)) {
			endIndex = item.size();
		}
		
		return item.subList(startIndex, endIndex);
	}
	
	// 전제조회 여부 체크(offset == 0)
	public static boolean checkAllSearch(int offset) {
		if (offset == 0) {
			return true;
		}
		return false;
	}
	
	// 전체 페이지수
	public static int getTotalPages(int totalCount, int count) {
		return (int) Math.ceil((double)totalCount / count);
	}
	
	private static boolean checkEndIdx(int size, int endIndex) {
		if (endIndex > size) {
			return false;
		}
		return true;
	}
	
	private static boolean checkPageNumber(int totalCount, int offset, int count) {
		int totalPages = getTotalPages(totalCount, count);
		
		if (offset > totalPages) {
			return false;
		}
		return true;
	}
}
