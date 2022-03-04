package ca.uhn.fhir.jpa.starter.vo.code;

import lombok.Getter;

/**
 * 조회 기간 검색 Key
 * 
 * @author jhkang
 *
 */
@Getter
public enum InquiryPeriodSearchCode {
  
  CONDITION_DATE(1, "A01", "진단일"),
  DRUG_DATE(2, "A02", "약물처방일"),
  INSPECTION_DATE(3, "A03", "검사일"),
  PROCEDURE_DATE(4, "A04", "수술일"),
  PRACTICE_DATE(5, "A05", "진료일");

  private int idx;
  private String code;
  private String name;

  InquiryPeriodSearchCode(int idx, String code, String name) {
    // TODO Auto-generated constructor stub
    this.idx = idx;
    this.code = code;
    this.name = name;
  }

  public int getIdx() {
    return idx;
  }

  public String getCategory() {
    return code;
  }

  // 코드성 데이터 배열 생성
  public static InquiryPeriodSearchCode[] createInquiryPeriodSearchCode() {
    return InquiryPeriodSearchCode.class.getEnumConstants();
  }
  
  // 특정 코드 존재 확인
  public static boolean existInquiryPeriodSearchCode(InquiryPeriodSearchCode[] codes, int idx) {
    
    InquiryPeriodSearchCode Searchcodes = findInquiryPeriodSearchCode(codes, idx);
    return Searchcodes.getIdx() == idx;
  }

  // 특정 코드 데이터 찾기
  public static InquiryPeriodSearchCode findInquiryPeriodSearchCode(InquiryPeriodSearchCode[] codes, int idx) {
    int index = binarySearch(codes, idx);

    return codes[index];
  }

  // 이분 탐색
  public static int binarySearch(InquiryPeriodSearchCode[] codes, int idx) {

    int left = 0;
    int right = codes.length - 1;
    int mid = 0;

    while (left < right) {
      mid = (left + right) / 2;

      if (codes[mid].getIdx() >= idx) {
        right = mid;
      } else {
        left = mid + 1;
      }

    }
    return right;
  }
  
}


